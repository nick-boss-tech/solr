# Metrics and monitoring round 1, part M2: SOLR-17987 (questions 6, 8, 9 and 10)

Head `38abf6423126112cf8a451f4d3fedea0920eae90` (matches the claim). Base `cabedd1d9680`. Reading was done by explicit SHA. The local ref `solr-17987-submit` in this worktree resolves to `e8c22910f50`, so the branch name alone gives the handoff-note commit. Nothing was built, run, committed, pushed, posted or edited. Line numbers are at the head unless marked base.

## Q6. Consumers of a null or no-op `solr.jvm`

### (a) `OtelRuntimeJvmMetrics.initialize`: the per-registry case does not take the global-disabled path

- **Global disabled** never calls `initialize`. The loader constructor calls it only under `if (enabled)` (`SolrMetricManager.java` lines 183 to 185; base 163 and 164). `otelRuntimeJvmMetrics` stays null.
- **Per-registry JVM disabled** does call it. `enabled` is true, and the JVM switch passes (`OtelRuntimeJvmMetrics.java` lines 47, 48 and 57; the default is true). The dummy OpenTelemetry returns `meterProvider("solr.jvm")` (line 67), which returns `MeterProvider.noop()` (`SolrMetricManager.java` lines 470 and 471). `RuntimeTelemetry` is then built on the no-op (lines 81 to 87). The physical-memory gauge is created through no-op builders (lines 91 to 103; `SolrMetricManager.java` lines 313 to 319 and 402 to 413), and `isInitialized = true` is set (line 110).
- **Visible difference:** the field is non-null, `isInitialized()` is true (lines 131 to 133), and the object is closed at container close (`SolrMetricManager.java` lines 532 and 533). The metrics API output is the same (no registry, no points).
- **The JVM-switch case is a third path:** an early return at `OtelRuntimeJvmMetrics.java` lines 57 to 60, asserted by `JvmMetricsTest.java` line 186.

**Handoff guess:** "tolerates a no-op provider (it already does when all metrics are disabled)."
- The parenthetical is **wrong**: the all-disabled case never reaches `initialize`.
- The tolerance claim is **not contradicted by reading**: nothing on the path dereferences null. Whether `RuntimeTelemetry.build()` on a no-op provider is safe, and whether it still starts JFR-based collection, depends on the OpenTelemetry instrumentation library, which is not in this repo. **Not verifiable by reading.** The gate does not exercise this path.

### (b) `CPUCircuitBreaker.calculateLiveCPUUsage` (`CPUCircuitBreaker.java` lines 107 to 124)

The per-registry case lands on the same throw as the global-disabled case.
- **Head:** the JVM switch and the registry lookup are folded into one ternary (lines 108 to 111). One null guard (lines 112 to 114) throws `IllegalStateException("JVM metrics disabled. Cannot calculate CPU usage")`.
- **Base:** an explicit early throw for the JVM switch (base lines 108 and 109), and an unguarded `.getPrometheusMetricReader("solr.jvm").collect(...)` (base lines 114 and 115). A null reader was a `NullPointerException`.
- **Per-registry JVM off:** the map has no `solr.jvm` entry, because `meterProvider` returns the no-op before `computeIfAbsent` (`SolrMetricManager.java` lines 470 to 472), so `getPrometheusMetricReader` returns null (lines 603 to 606). It hits the same guard as global off.

**Handoff guess:** "reader is null for jvm disabled, same as the global-disabled case; may need a guard."
- Null reader: **right**. Same as global: **right**. Guard needed: **right**, and the branch added it.
- The implication that the global case was already handled: **wrong**. The base global-disabled case gave an NPE.

**Every other case where the reader can be null (head, by reading):**
1. **Global metrics off.** `CoreContainer.java` line 406 passes the enabled flag. This only matters when a CPU breaker is configured through the system-property path (`CircuitBreakerRegistry.java` line 104, inside `initGlobal` at lines 134 to 146, reached from `SolrCore.java` line 1043). Base: NPE. Head: ISE.
2. **Manager closed.** `closeAllRegistries` (`SolrMetricManager.java` lines 524 to 531) is called at container close (`CoreContainer.java` line 1369). A breaker asked `isTripped` after that gets null (`SearchHandler.java` lines 387 and 391). Base: NPE. Head: ISE. Reachability at shutdown time was not run.
3. **Managers without the loader constructor** (`SolrMetricManager.java` lines 153 to 172 never call `initialize`). In production only `CoreContainer.java` line 406 builds a manager for a container. The others are tests (`BufferStoreTest`, `SolrMetricManagerTest`, `SolrMetricsContextTest`, `TestCaffeineCache`, `TestSolrCachePerf`, `TestThinCache`, `OtelInstrumentedExecutorServiceTest`) and the cross-dc `OtelMetrics.java` line 59. Not production-reachable by grep.
4. **`removeRegistry("solr.jvm")`.** No caller removes it. The core removals (`CoreContainer.java` lines 2113 and 2157; `SolrCoreMetricManager.java` line 99) remove per-core registries. Not reachable.

Unchanged, and not null-reader cases: a null `cc` (NPE at `this.cc.getMetricManager()` on both base and head) and a null `getMetricManager()` (NPE on both).

**Does NPE-to-ISE change behavior beyond the disabled-registry case?** Cases 1 to 3 change only the exception class and message. The failure point is the same. `enableIfSupported` (`CPUCircuitBreaker.java` lines 142 to 153) calls the method with no catch, so construction throws. For case 1, both base and head fail `SolrCore` construction: `SolrCore.java` line 1043 is inside a `try` that catches `Throwable` (line 1167) and rethrows `SolrException(e.getMessage())` (line 1186). The operator-visible text changes from a JDK null-pointer message to the JVM-disabled text. No `NullPointerException` catch was found on this path. The only NPE catch in core main code is `DefaultSolrCoreState.java` line 383 (`cancelRecovery`), which is unrelated.

**Consumer-path comparison:**

| Consumer | Global off | Per-registry JVM off (head) | JVM switch off (head) | Verdict |
|---|---|---|---|---|
| `OtelRuntimeJvmMetrics` | `initialize` not called; field null (`SolrMetricManager.java` lines 183 to 185) | `initialize` runs on the no-op; field set; `isInitialized` true (`OtelRuntimeJvmMetrics.java` lines 57 to 110) | early return (`OtelRuntimeJvmMetrics.java` lines 57 to 60) | Different path |
| `CPUCircuitBreaker` | lookup null; the guard throws ISE (lines 112 to 114) | same guard, same ISE | no lookup; same guard, same ISE | Same path |

The new per-registry JVM-off case also makes a configured CPU breaker fail core load. Base already does this for the JVM switch, so the branch extends existing behavior to a new configuration. The ref guide and the changelog do not say so.

## Q8. The test `testDisabledRegistryUsesNoopProvider` (`SolrMetricManagerTest.java` lines 76 to 94)

The class already extends `SolrTestCaseJ4` (line 49). The branch adds one method and one import (`java.util.List`). That is all.

- **Asserts:** the three-argument constructor with `["quiet", " solr.noisy ", ""]` (lines 79 and 80). It creates counters on `quiet`, `solr.noisy` and `loud` (lines 82 to 84). Then `hasRegistry("quiet")` is false (line 86), `hasRegistry("noisy")` is false (line 87), `hasRegistry("loud")` is true (line 88), `getPrometheusMetricReader("quiet")` is null (line 89), and `getPrometheusMetricReader("loud")` is non-null (line 90).
- **Covers:** two disabled spellings (a bare name, and a padded name with a prefix, which exercises trim and `enforcePrefix`) create no registry and no reader. An enabled name creates both.
- **Does not cover:**
  - That the returned provider is the no-op. Only the map state is checked, not that `add()` is discarded.
  - The empty-entry filter (`SolrMetricManager.java` lines 589 to 595). Removing it would put `solr.` in the set, and no assertion would fail.
  - `registryNames()` and the metrics API (`MetricsHandler`, `GetMetrics`).
  - The loader-constructor property path (`SolrMetricManager.java` lines 178 to 186). The property is never set, and `EnvUtils` is not exercised.
  - The environment-variable path.
  - All JVM consumer cases. No `jvm` name is in the list. `JvmMetricsTest.java` lines 178 to 195 cover only the JVM switch.
  - The global-off CPU case. `SolrMetricsDisabledIntegrationTest` asserts null readers for global off (lines 69 and 82), but it has no CPU or JVM assertion.

## Q9. Tidy position: verified

- `git diff --stat dba26c39877 38abf642312` shows two files: `SOLR-17987-TESTING.md` (22 lines deleted) and `CPUCircuitBreaker.java` (1 insertion, 3 deletions). Nothing else.
- The `CPUCircuitBreaker.java` diff is one hunk. It joins `reader.collect(...)` and `.stream()` onto head line 116. The same tokens remain.
- **Token check:** with all spaces, tabs and newlines stripped, the file has the same SHA-256 at `dba26c39877` and at `38abf642312` (`a74ae641...`). **No token changed.**
- The joined line is 87 characters. The formatter was not run.
- The only non-formatting change in the tidy commit is the handoff-note deletion, which the commit message states.
- The receipt's description agrees. The head changes six files versus base.

## Q10. Proof position

**Recorded (receipt, not re-run):** gate green at `38abf642312`. Error Prone compile passes. `SolrMetricManagerTest` has 18 tests, 0 failures, including the new one. Tidy is clean. The changelog parses. The "module check" passes, but the receipt does not say which task ran.

**Fail-before:** INCONCLUSIVE by construction. The new test calls the three-argument constructor, which does not exist on base. The Solr-issues `AGENTS.md` Test Runs rule says a job that did not compile on old code is INCONCLUSIVE, and only PASS reaches SUCCESS. So the fail-before cannot discriminate. The receipt's "GATE GREEN" label conflicts with that rule unless the label covers only some stages.

**The gate settles (per the receipt):** the branch compiles at the head with Error Prone; the `SolrMetricManagerTest` class passes at the head, including the three-argument test; the tree is tidy; the changelog parses.

**The gate cannot settle:**
- The loader-constructor property path (`SolrMetricManager.java` lines 180 and 181). The only new test passes a list.
- `EnvUtils` splitting and trimming (part M1's item).
- The environment-variable mapping (part M1's item).
- The no-op `initialize` behavior, and any CPU breaker behavior. No breaker test is in the focused run.
- The metrics-API claim.
- Which Gradle task "module check" ran. If test tasks were skipped, it is not behavioral.

**Behavioral premise run (available on paper, not run).** On base, the loader constructor reads no property. With `solr.metrics.disabledRegistries=quiet` set as a system property (saved and restored, as `JvmMetricsTest.java` lines 181 to 193 do), `longCounter("quiet", ...)` creates a real registry. `hasRegistry("solr.quiet")` is true, and the reader is non-null. A test using only base API (the loader constructor and `hasRegistry` assertions) would compile on base and fail its disabled-name assertion for a behavioral reason. It would pass on head if the loader path works. That confirms the all-or-nothing premise on base. It would not cover the environment-variable mapping, the consumers, or the metrics-API claim. The head needs its own run.

The verify-fail-before stage overlays only `src/test` files onto base. The branch's test file still calls the three-argument constructor, so the recorded fail-before cannot become PASS without changing that test.

## Interactions: `metrics-reporting.adoc`

From this round's side only: this round has one branch. This part did not check any other branch's diff and did not run `ls-remote`. The Scope section names SOLR-13265 and SOLR-18317 as tickets, not as branch names. The page's diff on this head is three lines, all from this branch.

## Owner decisions

1. **CPU breaker with JVM disabled.** Keep the throw (core load fails, as the JVM switch already does on base), or log and disable the breaker as the existing unsupported path does (`CPUCircuitBreaker.java` lines 142 to 153)? Either way, document it.
2. **Loader constructor with JVM disabled.** Keep running `initialize` against a no-op provider, or skip it as the global path does? This decides whether to accept a no-op `RuntimeTelemetry`, with possible JFR cost.
3. **Fail-before design.** Add or replace a base-compilable test through the loader constructor, so the gate can discriminate. Who writes it, and does this branch change?
4. **Receipt label.** "GATE GREEN" sits beside an INCONCLUSIVE fail-before, and the queue rule says only PASS reaches SUCCESS. Which stages does the green label cover?
5. **"Module check."** Which Gradle task ran, and did it include test tasks?

## Not checked

- No builds, tests, gate runs, `gh` writes, commits, pushes, or edits (the round's rules).
- OpenTelemetry library behavior (`RuntimeTelemetry` on a no-op provider, JFR cost, the `MeterProvider.noop()` contract): outside this repo.
- The gate log `g17987-gate.log`: not found under `research/test-queue` (logs or results). No SOLR-17987 queue entry exists there. The rest of the workspace was not searched, and the log was not read.
- Whether any existing test reaches the CPU breaker null guard: not searched in detail.
- Whether request handlers map an NPE and an ISE to the same HTTP status, and the handling around `SearchHandler.java` lines 380 to 396: not read beyond that block.
- Whether a shutdown-time request can reach a breaker after `closeAllRegistries`: not verified.
- Whether the test base class supplies a `SolrResourceLoader` for the premise run: not verified.
- Part M1's items (`EnvUtils`, the mappings file and the metrics-API read paths) are not covered here.
