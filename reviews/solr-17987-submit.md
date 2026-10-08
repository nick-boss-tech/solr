# solr-17987-submit

- Branch: origin/solr-17987-submit
- Head: e8c22910f507 (reviewed); patch pushed on top: dba26c39877 (tip now dba26c39877)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 6 files, +90/-6. `solr/core/.../metrics/SolrMetricManager.java` (disabled-registry set; `meterProvider` returns noop for a listed registry; new 3-argument constructor), `solr/core/.../util/circuitbreaker/CPUCircuitBreaker.java` (null-reader guard), `SolrMetricManagerTest.java` (one new test), changelog fragment (type `added`), `metrics-reporting.adoc` (3 lines), `SOLR-17987-TESTING.md` (author's unrun note, left in place).
- Verdict: Nearly (at dba26c39877; the env-var defect is patched, see HIGH; the test does not cover the env path)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

HIGH (patched in dba26c39877): the documented env var `SOLR_METRICS_DISABLEDREGISTRIES` is silently ignored. `EnvUtils.init` maps an env name with `envNameToSyspropName`, which lowercases and turns `_` into `.` (`EnvUtils.java` line 246), so the env value lands under `solr.metrics.disabledregistries`. `SolrMetricManager` reads `solr.metrics.disabledRegistries` through `EnvUtils.getPropertyAsList`. The camel-case fallback looks up `solr.metrics.disabled.registries`, so neither key matches. The ref-guide line that documents the env var would therefore do nothing. The `-Dsolr.metrics.disabledRegistries=...` system property works. The fix is one line in `solr/solrj/src/resources/EnvToSyspropMappings.properties`, `SOLR_METRICS_DISABLEDREGISTRIES=solr.metrics.disabledRegistries`, the same kind of mapping as the existing `SOLR_METRICS_ENABLED=metricsEnabled` and `SOLR_MAX_BOOLEAN_CLAUSES=solr.max.booleanClauses`. verified by reading EnvUtils; patch by reading, not run. No test was added for the env path; the Linux gate should cover it.

LOW (verified by reading): `meterProvider()` now calls `enforcePrefix(providerName)` before the enabled check. `enforcePrefix(null)` throws NPE (`SolrMetricManager.java` line 582). With metrics globally disabled, a null name used to return the no-op provider and now throws. Callers pass non-null names, so this is an edge case only.

LOW (verified, OK): `CPUCircuitBreaker.calculateLiveCPUUsage` now throws IllegalStateException with the same message when the `solr.jvm` reader is null. That covers "jvm registry disabled while jvm metrics are on", which previously hit a NullPointerException. The global-disabled case throws the same exception as before. Whether the caller handles the exception is not traced.

verified (checked against the code, the author's guesses):
- `OtelRuntimeJvmMetrics.initialize` only calls `solrMetricManager.meterProvider(registryName)` (`OtelRuntimeJvmMetrics.java` line 67). A no-op provider is tolerated, and no reader is dereferenced there.
- `hasRegistry` is `meterProviderAndReaders.containsKey(...)` (line 459). Only `meterProvider()` creates entries (line 474), after the disabled check, so a disabled registry never appears in `hasRegistry`, `getPrometheusMetricReader`, or the registry-name list (line 505). The ref-guide claim that a disabled registry is not exposed by the metrics API holds.
- `enforcePrefix` is `public static`, so `prefixed(...)` compiles with `SolrMetricManager::enforcePrefix`.
- `EnvUtils.getPropertyAsList` splits on commas and trims, through `stringValueToList` and `splitSmart`. The system-property form works as documented.

Test coverage: `testDisabledRegistryUsesNoopProvider` uses the 3-argument constructor. It does not exercise the system property or the env var, so the HIGH defect was not caught by the branch's own test.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- Whether `CPUCircuitBreaker`'s caller catches the IllegalStateException when `jvm` is disabled (LOW above).
- Windows `solr.cmd` env passthrough for `SOLR_METRICS_DISABLEDREGISTRIES` (the Java side reads the env directly, so it should match the Linux behavior).
