# Metrics and monitoring round 1, part M1: premise and mechanism (SOLR-17987)

Branch `solr-17987-submit` at head `38abf6423126112cf8a451f4d3fedea0920eae90`. "H" means head `38abf642312`; "B" means base `cabedd1d9680`. Read-only: no builds, tests, gates, `gh` writes, commits, pushes, or edits to repo files. Scratch copies of the head and base files are in the session scratchpad, outside the repo.

## Q1. Recorded state against the branch

Verified by reading:
- The head `38abf6423126112cf8a451f4d3fedea0920eae90` exists as a commit object.
- Five commits over base, in order: `715a4fd3108` (mechanism), `ad6ae2b9439` (changelog), `e8c22910f50` (handoff note), `dba26c39877` (environment mapping), and `38abf642312` (remove the handoff note, tidy). This matches the claim.
- Six files against base: `SolrMetricManager.java`, `CPUCircuitBreaker.java`, `SolrMetricManagerTest.java`, `metrics-reporting.adoc`, `EnvToSyspropMappings.properties`, and the changelog. This matches.
- The assignment's "four commits" and "seven files" describe the old tip `dba26c39877`, which still has `SOLR-17987-TESTING.md`. The claim's moved-tip flag is correct.
- The tidy commit changes only `CPUCircuitBreaker.java` (four lines: the `reader.collect(...).stream()` chain is joined onto one line, head line 116) and deletes `SOLR-17987-TESTING.md`. This matches the receipt's description. Whether the tree is tidy-clean is not verifiable by reading.
- The handoff note at `e8c22910f50` says nothing was compiled or run, and it lists the four guesses checked below.
- `@Test` count: 17 at base, 18 at head. This matches the receipt's "18 tests" as a static count, not a run.
- Base has no three-argument constructor (base lines 147 to 160 have only the one- and two-argument `MetricExporter` constructors and the two loader constructors). The receipt's "inconclusive by construction" is consistent with the code.
- The changelog reads as valid YAML (folded title, type `added`, author Nick Shanin, the SOLR-17987 link).
- The test class extends `SolrTestCaseJ4` (per the hunk header in the diff).

Not verifiable by reading (the receipt is the only record): the gate green at `38abf642312`, the Error Prone compile, the module check, the tidy-clean tree, the 18 of 0 count, and the seed `17987C0FFEE17987`. The gate log `g17987-gate.log` was not found under `research/`, `env/` or `wt/` (depth 5), or at the workspace root (depth 4). The ticket worktree `wt/SOLR-17987` is still at `dba26c39877` with the handoff file present, so it is not the branch record.

Mismatches: none between the receipt and the branch diff. The only mismatch is the superseded starting state in the assignment (tip, commit count, file count), which the claim already flags.

## Q2. Premise: is disabling all-or-nothing today?

**Yes, for every registry except `jvm`, which already has its own switch.**
- `solr.xml` `<metrics enabled>` is one boolean (`SolrXmlConfig.java` lines 701 to 706 at H). When it is false, `meterProvider` returns the no-op for every name (H 470 and 471; B 449 and 450). The ticket text says the same.
- The existing per-registry switch is `solr.metrics.jvm.enabled` (`OtelRuntimeJvmMetrics.java` lines 47 to 49 and 57 to 60 at H; documented in the ref guide at `adoc` line 104). It gates only the JVM registry, because `initialize` runs only when the global flag is on (`SolrMetricManager.java` H 183 to 185). So "not one registry" holds for everything except `jvm`, and the new property overlaps that switch for `jvm`.

**Registries in the tree and their creators (H):**
- `solr.jvm`: the loader constructor, through `OtelRuntimeJvmMetrics` (`SolrMetricManager.java` H 183 to 185; `OtelRuntimeJvmMetrics.java` lines 55 to 112).
- `solr.node`: `CoreContainer.java` line 762. Also `AttributedInstrumentFactory.java` lines 60 to 74 when dual-registry aggregation is on. In that mode, the node copy goes to `solr.node` (lines 81 to 87), and the core copy still goes to the core registry.
- `solr.core.<collection>.<shard>.<replica>` in cloud, and `solr.core.<name>` standalone: `SolrCoreMetricManager.java` lines 178 to 186, one per core, created dynamically.
- `solr.overseer`: `Overseer.java` lines 720 to 722.
- No `solr.jetty` registry exists. `SolrInfoBean.Group` (`SolrInfoBean.java` lines 47 to 56) has `jetty`, `cluster`, `collection` and `shard` values, but no main code creates a registry from them (the only `Group` uses in core main code are `overseer` and `node`). The ref guide example (`adoc` line 106) and the changelog use `jetty`.
- The cross-dc `OtelMetrics.java` lines 59 and 60 create a separate `SolrMetricManager` and scope in a separate application.

**Name and shape.** The `-D` name `solr.metrics.disabledRegistries` has a camelCase last segment, the same shape as `solr.metrics.otlpExporterEnabled` (`MetricExporterFactory.java` line 25; `adoc` line 332). The JVM neighbor `solr.metrics.jvm.enabled` is all lowercase. So the `-D` name is consistent with the OTLP neighbor, not the JVM one. Matching is exact after the prefix (H 470), so per-core registries cannot be disabled as a group. The handoff's claim that the property is documented next to the JVM switch is true (`adoc` lines 104 to 107).

## Q3. Mechanism completeness

- Inside `SolrMetricManager`, the only creation of a `MeterProviderAndReaders` (an `SdkMeterProvider` plus a `FilterablePrometheusMetricReader`) is the `computeIfAbsent` in `meterProvider` (H 473 to 499; the reader at line 477, `SdkMeterProvider.builder` at line 479). The map has no other `put` or `compute`. Every instrument method (H 188 to 450), `batchCallback` (H 372 to 380), and the `*Measurement` helpers (H 382 to 400) reach `meterProvider` or a builder that calls it.
- Outside `SolrMetricManager`, in main code: there is no construction of `SdkMeterProvider`, `PrometheusMetricReader` or `FilterablePrometheusMetricReader`. The last is constructed only at H 477, including in tests. No `GlobalOpenTelemetry` meter use exists, and `OpenTelemetryConfigurator` has no meter reference. `OtelRuntimeJvmMetrics` reaches meters only through the anonymous `OpenTelemetry` at H 66 to 68, which calls `meterProvider`. **Result, by reading: `meterProvider` is the only creation path in main code. Complete.**
- The disabled set is populated only in the loader constructor (H 180 and 181, from `EnvUtils.getPropertyAsList`) and the three-argument constructor (H 167 to 171, used by tests). The one- and two-argument `MetricExporter` constructors pass `List.of()` (H 153 to 159).
- Managers created another way:
  - `CoreContainer.java` line 406 is the only production call that goes through the loader constructor, so the property reaches the node's manager.
  - The cross-dc `OtelMetrics.java` line 59 uses the one-argument exporter constructor. It never reads the property.
  - Test managers (`BufferStoreTest` 46, `TestCaffeineCache` 51, `TestSolrCachePerf` 107, `SolrMetricManagerTest` 58, `SolrMetricsContextTest` 38) get an empty set.
  - No class in the tree extends `SolrMetricManager`.
- **Behavior change:** H 469 and 470 now call `enforcePrefix` before the enabled check. B 449 to 452 returned the no-op first. With metrics off, a null name now throws an NPE at the head, where base returned the no-op. The handoff says the global path is "as before", which is not quite true. Minor.

## Q4. Prefix and list handling

**What the loader receives for `"jvm, jetty"`** (from `-D` or the environment):
- `EnvUtils.getProperty` (lines 110 to 129) returns the raw string `"jvm, jetty"`.
- `stringValueToList` (`EnvUtils.java` lines 254 to 263): the value is not bracketed, so it goes to `StrUtils.splitSmart(s, ",", true)` (the String overload, `StrUtils.java` lines 111 to 159, by reading). That splits on commas and keeps the leading space, giving `["jvm", " jetty"]`. Empty runs are dropped (`StrUtils.java` lines 154 to 156 test the buffer), so `"jvm,,jetty"` and leading or trailing commas produce no empty items. The `map(String::trim)` then gives `["jvm", "jetty"]`.
- `EnvUtils` does not drop whitespace-only items: `"jvm, ,jetty"` gives `["jvm", "", "jetty"]`. Only `prefixed()` removes that empty (H 589 to 595).
- **The handoff's assumption holds:** the list arrives split on commas and trimmed. Supporting evidence, by reading: the SolrJ test `EnvUtilsTest` line 71 asserts that `"one,two, three"` becomes `["one","two","three"]`. Not run.
- `prefixed()` (H 589 to 595): the trim is redundant on the `-D` and environment path, but the test path bypasses `EnvUtils` and passes `" solr.noisy "`, so the trim is needed there. The empty filter matters only for whitespace-only items. The result for the example is an unmodifiable set `{"solr.jvm", "solr.jetty"}`.
- The requested name is prefixed at H 469, before the check at H 470. So `"jvm"` and `"solr.jvm"` both match. Lookups are not trimmed, and matching is case-sensitive (`"JVM"` is not disabled). The ref guide says neither.
- **Edge:** `""` gives an empty list. A bracketed value takes the JSON branch (`EnvUtils.java` lines 255 to 257; `Utils.fromJSONString` at `Utils.java` lines 409 to 415), not the splitter. Unquoted items probably fail to parse (not run). A parse error is wrapped in a `SolrException` and would fail the constructor, so `CoreContainer` startup (line 406) would fail. A JSON array of non-strings would hit `String::trim` (H 591) with a `ClassCastException`. The documented form is unbracketed, so this is an undocumented edge.

## Q5. Environment-variable path

- **Mapping line:** `EnvToSyspropMappings.properties` line 39 at H: `SOLR_METRICS_DISABLEDREGISTRIES=solr.metrics.disabledRegistries`. The only copy in the tree is `solr/solrj/src/resources`.
- **With the line:** `EnvUtils` loads `CUSTOM_MAPPINGS` (lines 59 to 76). `init()` (lines 206 to 217) sees the `SOLR_` prefix. `envNameToSyspropName` (lines 243 to 247) returns `solr.metrics.disabledRegistries`. `setProperty` (lines 198 to 201) writes that key and its dotted alias `solr.metrics.disabled.registries`. The reader (`SolrMetricManager` line 181, then `EnvUtils` lines 193 to 195 and 120 to 129) finds the value under the primary key. A `-D` value takes precedence, because `init` writes only when the property is absent (line 213).
- **Without the line:** the default rule (lines 243 to 247) gives `solr.metrics.disabledregistries`. Neither that key nor the alias matches, so the environment variable is silently ignored. The handoff's claim is confirmed.
- **Neighbor `SOLR_METRICS_ENABLED=metricsEnabled`** (line 40): a bare name with no `solr.` prefix, so it also needs a custom line. It is read by `solr.xml` substitution (the core test-files `solr.xml` line 24: `<metrics enabled="${metricsEnabled:true}">`), resolved in `SolrXmlConfig` line 701. Its key shape differs from both the new line and the JVM neighbor.
- **Neighbor `SOLR_METRICS_JVM_ENABLED`** has no mapping line. The default rule yields `solr.metrics.jvm.enabled` exactly.
- **The ref guide's OTLP section** (`adoc` lines 328 to 330) states the environment rule: replace "." with "_", convert camelCase to UPPER_SNAKE, and uppercase. Under that rule the environment name is `SOLR_METRICS_DISABLED_REGISTRIES`. By reading, that name needs no mapping line: the default rule gives `solr.metrics.disabled.registries`, which the reader finds through its camelCase alias (`EnvUtils` lines 126 and 127, and 131 to 135). The OTLP property relies on the same alias (`MetricExporterFactory.java` line 25). So the head's environment name departs from the documented convention and needs a custom line that the convention avoids. Not run.

## Q7. Read paths and the disabled name

- A disabled name never enters the map. The only insertion is the `computeIfAbsent` at H 473 to 499, reached only after the return at H 470 and 471. The disabled set and the map are disjoint.
- Metrics API read paths: `MetricsHandler.java` lines 138 and 146, and `GetMetrics.java` lines 160 and 165, iterate `getPrometheusMetricReaders()` (H 598 to 601). Disabled names are absent. They agree.
- `hasRegistry` (H 458 to 460) is `containsKey` on the same map, so it returns false for disabled names. It has no main-code caller; only tests use it (`SolrMetricsIntegrationTest` 161 to 175; the new test 86 to 88).
- `registryNames()` (H 503 to 507) has no main-code caller. The test-only users are `JettySolrRunner` 676 to 678, `PeerSyncReplicationTest` 172, and `SolrMetricsDisabledIntegrationTest` 68 and 81 (global off, asserts empty).
- `getPrometheusMetricReader` (H 603 to 606): the only main-code caller is `CPUCircuitBreaker.java` line 110 (see the consumer section). The others are tests and test-framework helpers.
- `removeRegistry` (H 515 to 521) on a disabled name: `map.remove` returns null, so it is a no-op (callers `CoreContainer` 2113 and 2157, `SolrCoreMetricManager` 99).
- **Result: every read path agrees.** Caveat: all paths normalize with `enforcePrefix` only (no trim, no case folding). Configuration-side values are trimmed.

## Guess verdicts

The handoff note's four guesses, plus the fail-before:

- **G1, `OtelRuntimeJvmMetrics` tolerates a no-op provider for `solr.jvm`:** the parenthetical is wrong. With all metrics off, `initialize` never runs (`SolrMetricManager.java` B 163 to 165; H 183 to 185 inside `if (enabled)`). The per-registry JVM case is the first path to call `initialize` with a no-op provider. The code in `initialize` touches the provider only through `meterProvider` (H 66 to 68) and no-op builders (H 91 to 103), with no null check or cast that would fail. Whether `RuntimeTelemetry`'s internals tolerate it cannot be verified by reading, because the library source is not in the tree. **Verdict: not verifiable by reading; the parenthetical is wrong.**
- **G2, `CPUCircuitBreaker` and a null reader:** right that the reader is null in the per-registry case. Wrong that the code handles it: base (B 112 to 119) has no null check, so it throws an NPE. The head adds a guard (H 108 to 114), but the guard throws instead of degrading. `enableIfSupported` (H 142 to 153, called from the constructor at H 56 and from `inform` at H 129) has no catch. A CPU breaker configured by system property (`CircuitBreakerRegistry.java` lines 102 to 106, built in `parseCircuitBreakersFromProperties`, called from `initGlobal` at lines 134 to 136, which runs in the `CircuitBreakerRegistry` constructor that `SolrCore` builds at `SolrCore.java` line 1043) therefore makes core creation fail in the per-registry JVM case. **Verdict: a guard was needed, but the one written does not make the breaker degrade.**
- **G3, `EnvUtils` splits on commas, and the environment name:** right. The split is `StrUtils.splitSmart`, then trim (`EnvUtils.java` lines 254 to 263). The environment name works only with the mapping line.
- **G4, `hasRegistry` is false for a no-op-only name:** right by construction. The wording should be "never had a provider", not "only ever had a no-op provider".
- **Fail-before, compile failure on base:** right (base lines 147 to 160 have no three-argument constructor). "`quiet` would get a real registry": right on base, which has no disabled concept.
- **Design pick (a node-level property, not a `solr.xml` element):** not a code premise. It is consistent with the JVM neighbor being a system property (`adoc` line 104).

## Consumer checks (interactions)

- **Per-registry JVM against global off:**
  - Global off: `initialize` never runs (H 183 to 185). There is no `solr.jvm` registry. The CPU breaker gets null.
  - Per-registry JVM: `initialize` runs, because the global flag and the JVM flag are both still true. `RuntimeTelemetry` is built against a no-op provider (`OtelRuntimeJvmMetrics` lines 81 to 87), and the memory gauge at lines 91 to 103 is built on no-op builders. No registry is created. `otelRuntimeJvmMetrics` is non-null and is closed in `closeAllRegistries` (H 532 to 534). `isInitialized()` (`OtelRuntimeJvmMetrics` lines 131 to 133) has no main-code caller (only `JvmMetricsTest` 186), so the difference is not visible in main code.
  - CPU breaker: both routes reach the same guard (H 108 to 114) and the same ISE text. The downstream outcome is the same; the route into the breaker differs.
- **Every case in which the CPU reader can be null, at the head:**
  1. Global off (`<metrics enabled="false">`): null. At base, construction NPEs in `enableIfSupported` when a CPU breaker is configured. At the head, it throws ISE from the same place. The failure mode is unchanged: the constructor throws, and `SolrCore` rethrows (`SolrCore.java` lines 1167 to 1186).
  2. `solr.metrics.jvm.enabled=false`: short-circuit at H 109 with the ISE. The same at base.
  3. Per-registry JVM (new): null, ISE at the head. Not reachable at base, because the property does not exist there.
  4. After `closeAllRegistries` (H 524 to 531 clears the map): base NPE, head ISE, on the `isTripped` path with no catch in `checkTripped`.
  5. `solr.jvm` never created while the flag is on: depends on whether `RuntimeTelemetry` registers instruments at build time. Not verifiable by reading. The live-usage test (`TestCircuitBreakers.java` lines 279 to 282 comment, test at 284 to 290) implies the registry exists on HotSpot.
  - **Net:** the change alters the exception type and message in every case, but not the failure mode (it still throws). The core-creation `SolrException` message changes from the NPE text to "JVM metrics disabled. Cannot calculate CPU usage".
  - The existing test comment (`TestCircuitBreakers.java` lines 279 to 282) treats `-1` as the "unsupported" signal, which `enableIfSupported` turns into `enabled=false` (H 143 to 151). A null reader could map to `-1` to match that design. See owner decision 2.

## Owner decisions

1. **Environment name and mapping:** keep `SOLR_METRICS_DISABLEDREGISTRIES` with the custom line, or rename it to `SOLR_METRICS_DISABLED_REGISTRIES` and drop the line, which matches the ref guide's OTLP rule. The rename is supported by reading only; not run.
2. **CPU breaker with the JVM disabled (or metrics off) and a system-property-configured CPU breaker:** keep failing core creation, or degrade to disabled with the existing error log, by mapping a null reader to `-1`.
3. **Overlap with `solr.metrics.jvm.enabled`:** keep both and document how they combine. Both leave no `solr.jvm` registry.
4. **Exact names only:** per-core registries cannot be disabled as a group. Accept it as a Limit, or plan a pattern later.
5. **Example names:** `jetty` is not a registry Solr creates in this tree, but the ref guide (`adoc` line 106) and the changelog use it. Replace it with a real one (for example `node` or `overseer`).
6. **Scope:** the property reaches only the node's `CoreContainer` manager. The cross-dc `OtelMetrics` (line 59) ignores it. State this in the docs or as a Limit.
7. **Bracketed or JSON values:** document them as unsupported, or fail with a clear message.
8. **`meterProvider` order** (H 468 to 472): restore the enabled check first, so the global-off path is exactly as before, or accept the null-name change.

## Not checked

- The gate, build, test, spotless, Error Prone, the module check, the tidy-clean status, and the changelog YAML parse by a tool. The gate log and the JUnit XML were not in the workspace that could be searched.
- OpenTelemetry `RuntimeTelemetry` internals (eager registration, no-op tolerance, non-HotSpot behavior). The library is not in the tree.
- Noggit parsing of unquoted bracketed values (no run).
- Whether `CoreContainer` catches and reports `SolrCore` construction failures in every caller. Only the `SolrCore` rethrow was read.
- The new test's coverage (part M2's item 8) and the `SolrTestCaseJ4` setup.
- Ref-guide rendering.
- The ticket worktree `wt/SOLR-17987`: only its HEAD and status were read.
