# Metrics and monitoring round 1: round roll-up (SOLR-17987, audit; no draft this round)

Claim: `claims/metrics-round-1.md` (commit `294e556c6ee`). Assignment: `assignments/metrics-round-1.md` (commit `5bc7acc7c1a`). Part reports: `reports/metrics-round-1-m1.md` (premise and mechanism) and `-m2.md` (consumers, tidy and proof). No draft this round, as the assignment says.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Two subagents, one per question cluster. No build, Gradle run, test or gate run was done. No branch, live PR, JIRA item or comment was touched. Nothing was posted.

## The tip moved: flagged

The assignment names `dba26c39877a`. The live tip is `38abf6423126`, which adds one packaging commit: it removes the handoff note and applies the tidy formatting. The receipt (`d5189425c7c`) records a gate green at this head ("first gate, finished 2026-10-10"). The audit runs against `38abf6423126`. The assignment's starting state ("NO GATE", four commits, seven files) is out of date: the branch has five commits over base and six changed files.

## Verdict

**Audit only. The branch is not draftable yet, and the reason is the proof, not the code.** The premise holds, and the mechanism is complete. But the recorded fail-before is inconclusive by construction, and two of the handoff note's consumer guesses were wrong. Owner decisions are needed before a draft (below).

## What holds

- **The premise.** Disabling is all-or-nothing today for every registry except `solr.jvm`, which has its own switch (`solr.metrics.jvm.enabled`). The `solr.xml` `<metrics enabled>` flag is one boolean.
- **The mechanism is complete.** `meterProvider` is the only path in main code that creates a meter provider or a Prometheus reader for a registry name. A disabled name never enters the map.
- **The list and prefix handling works.** A value such as `"jvm, jetty"` reaches the loader as `["jvm", "jetty"]` (comma split, then trimmed). Matching is exact after the `solr.` prefix. Case-sensitive, and lookups are not trimmed.
- **The read paths agree.** The metrics API, `registryNames`, `hasRegistry`, `getPrometheusMetricReader` and `removeRegistry` all behave the same for a disabled name.
- **The tidy commit changes no tokens.** The SHA-256 of `CPUCircuitBreaker.java` with whitespace stripped is the same at the old tip and the new head, so the only change is formatting (one joined line) and the handoff-note deletion.
- **The changelog reads as valid YAML** (by eye; not parsed by a tool).

## What does not hold, or is not verifiable

- **The environment variable needs a custom line.** `SOLR_METRICS_DISABLEDREGISTRIES` maps to `solr.metrics.disabledRegistries` only through the new line in `EnvToSyspropMappings.properties`. Without it, the variable is silently ignored. But the ref guide's own rule for the OTLP property gives `SOLR_METRICS_DISABLED_REGISTRIES`, which needs no line. The head departs from the documented convention (owner decision 1).
- **The CPU breaker throws instead of degrading.** The branch adds a guard for the null reader, but it throws the "JVM metrics disabled" `IllegalStateException` instead of disabling the breaker. A CPU breaker configured by a system property therefore fails core creation when the JVM registry is disabled per registry. Base already fails this way for the JVM switch, so the change extends an existing behavior to a new configuration, and the ref guide and changelog do not say so (owner decision 2).
- **The `OtelRuntimeJvmMetrics` path differs.** With the JVM registry disabled per registry, `initialize` still runs, against a no-op provider. The global-off case never calls it. Whether `RuntimeTelemetry` tolerates a no-op provider cannot be verified by reading, because the library is not in the tree.
- **The example names `jetty`.** No `solr.jetty` registry exists in this tree. The ref guide and the changelog use it as the example. Replace it with a real registry (owner decision 5).
- **The null-name order changed.** With metrics off, a null name now throws an NPE at the head, where base returned the no-op. Minor, but the handoff's "as before" is not quite true (owner decision 8).
- **A bracketed value takes the JSON path**, which fails on unquoted items. The documented form is unbracketed, so this is an undocumented edge.

## Handoff note's guesses

- **G1** (the `OtelRuntimeJvmMetrics` no-op tolerance): the parenthetical "it already does when all metrics are disabled" is wrong, because the global-off case never calls `initialize`. The tolerance itself is not verifiable by reading.
- **G2** (the CPU breaker's null reader): right that the reader is null, and right that a guard is needed. Wrong that the base case was handled: base threw an NPE. The guard the branch adds throws rather than degrades.
- **G3** (`EnvUtils` splits on commas): right.
- **G4** (`hasRegistry` for a no-op-only name): right by construction, but the wording should read "never had a provider".
- **Fail-before** (a compile failure on base): right.

## The proof position

- **The receipt's gate green** is at this head, per the receipt. The receipt does not say which Gradle task "module check" ran, and if test tasks were skipped the module check is not behavioral.
- **The fail-before is inconclusive by construction.** The new test calls a three-argument constructor that does not exist on base. The queue rule says only PASS reaches SUCCESS, so the receipt's "GATE GREEN" label and the INCONCLUSIVE fail-before need reconciling (owner decision 4).
- **The gate settles:** the branch compiles at the head with Error Prone, the `SolrMetricManagerTest` class passes at the head (18 tests, 0 failures per the receipt), the tree is tidy, and the changelog parses.
- **The gate cannot settle:** the loader-constructor property path (the only new test passes a list); the `EnvUtils` splitting; the environment-variable mapping; the no-op `initialize` behavior; any CPU breaker behavior (no breaker test is in the focused run); and the metrics-API claim.
- **A behavioral premise run is available on paper.** On base, the loader constructor reads no property. With `solr.metrics.disabledRegistries=quiet` set as a system property, a test using only base API would create a real registry for `quiet`, and the disabled-name assertion would fail for a behavioral reason. On head it would pass if the loader path works. The run would confirm the all-or-nothing premise on base. It would not cover the environment mapping, the consumers, or the metrics-API claim. The head needs its own run. The fail-before stage overlays only test files onto base, so the branch's test file, which calls the three-argument constructor, cannot become PASS without changing that test.

## Interactions

- **The per-registry path against the global path, inside this branch:** the two differ for `OtelRuntimeJvmMetrics` (the per-registry case runs `initialize` on a no-op; the global case does not) and match for `CPUCircuitBreaker` (both reach the same guard and the same exception).
- **The metrics-reporting page** is this branch's alone in this round. Its diff on this head is three lines, all from this branch. This round did not check other branches' diffs, so the openings slate should not expect a landing order here without that check.
- **SOLR-13265 and SOLR-18317** are metric-adjacent and were not re-audited.

## Owner decisions

1. **Environment name:** keep `SOLR_METRICS_DISABLEDREGISTRIES` with the custom mapping line, or rename it to `SOLR_METRICS_DISABLED_REGISTRIES` and drop the line, which matches the ref guide's OTLP rule. Recommendation: rename.
2. **CPU breaker** with the JVM registry disabled, or metrics off, and a system-property-configured CPU breaker: keep failing core creation (as the base JVM switch already does), or degrade to disabled with the existing error log by mapping a null reader to `-1`, which the existing test comment already treats as "unsupported". Recommendation: degrade, and document it either way.
3. **Overlap with `solr.metrics.jvm.enabled`:** keep both and document how they combine. Both leave no `solr.jvm` registry.
4. **Proof label:** "GATE GREEN" beside an INCONCLUSIVE fail-before. Which stages does the green label cover? Also, which Gradle task was the "module check"?
5. **Example names:** replace `jetty` with a registry Solr creates (for example `node` or `overseer`) in the ref guide and the changelog.
6. **Scope:** the property reaches only the node's manager. The cross-dc `OtelMetrics` ignores it. State this in the docs or as a Limit.
7. **Exact names only:** per-core registries cannot be disabled as a group. Accept it as a Limit, or plan a pattern later.
8. **The null-name order:** restore the enabled check first, so the global-off path is exactly as before, or accept the change.
9. **Bracketed or JSON values:** document them as unsupported, or fail with a clear message.
10. **A behavioral test:** add a base-compilable test through the loader constructor, so the fail-before can discriminate. Who writes it, and does this branch change?

## Main-side work owed before a draft

- The gate log `g17987-gate.log` is not on disk. The receipt's counts rest on it.
- The "module check" task identity, and whether it included test tasks.
- A behavioral premise run on base (the system-property route described above), and the head run.
- A test for the environment-variable path and the loader-constructor property path, if the owner wants those covered.
- The CPU breaker behavior, if decision 2 changes the code.
- The answers pass on this report, then the draft.

## Receipt and branch corrections (main side)

- The receipt's "GATE GREEN" label and the INCONCLUSIVE fail-before.
- The receipt's silence on which Gradle task "module check" ran.
- The handoff note's parenthetical on `OtelRuntimeJvmMetrics` (in the note, which the head removed).
- The receipt's "as before" for the global-off path, which changed for a null name.

## Not done

- No build, Gradle run, test, or gate run. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The OpenTelemetry library's behavior was not checked, because the library is not in the tree.
- The changelog YAML was read by eye, not parsed by a tool.
- No draft was written, as the assignment requires for this round.
