# SOLR-17987 - hypothetical reproduction (nothing was compiled or run)

JIRA (2025, Tier 4, audit note "feature proposal"): solr.xml can switch all metrics off, but not one registry. The ticket text asks for a
NOOP meter provider for a disabled registry. David Smiley's comment: no gauge may keep a lambda reference that could leak if Solr forgets
to unregister something; a no-op provider satisfies that because nothing is registered.

## Change
`SolrMetricManager` gets a set of disabled registries. `meterProvider(name)` returns `MeterProvider.noop()` when metrics are off globally
(as before) or when the prefixed name is in the set. The set comes from the system property / env var `solr.metrics.disabledRegistries`
(comma separated, with or without `solr.`) in the production constructor; a new three-argument constructor takes it directly for tests.
Ref guide `metrics-reporting.adoc` documents it next to the JVM switch. Design pick: a node-level property, not a `solr.xml` element
(the metrics config in `solr.xml` has no registry list today).

## Guesses to verify first
- `OtelRuntimeJvmMetrics.initialize` tolerates a no-op provider for `solr.jvm` (it already does when all metrics are disabled).
- `CPUCircuitBreaker` reads `getPrometheusMetricReader("solr.jvm")`; with `jvm` disabled that is null, same as the global-disabled case. Check
  whether it handles null (it may need a guard).
- `EnvUtils.getPropertyAsList(key, default)` splits on commas and the env var name is `SOLR_METRICS_DISABLEDREGISTRIES`.
- The test relies on `hasRegistry` being false for a registry that only ever had a no-op provider.

## Fail-before
Without the change the three-argument constructor does not exist (compile failure); `quiet` would get a real registry.
