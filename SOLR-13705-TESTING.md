# SOLR-13705 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-13705 - "Double-checked Locking Should Not be Used" (2019). The ticket has FixVersion 8.3, but names no code location; the old skip note said "no code point named, only non-volatile DCL (SSLConfigurationsFactory) has no repro". Reopened in audit round audit-1 (Tier 1 batch 11).
- Branch: `solr-13705-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`SSLConfigurationsFactory.current()` is double-checked locking on `private static SSLConfigurations currentConfigurations`, which is not `volatile` (the writer `setCurrent` is `synchronized`, the lazy reader path is not). Without `volatile`, a second thread can observe a non-null reference to a not-yet-fully-constructed `SSLConfigurations`. The Java memory model fix is `volatile`. This is a correctness-by-construction fix; the race itself is not practically reproducible.

## What the branch changes
- `SSLConfigurationsFactory`: `currentConfigurations` is now `volatile`.
- New test `SSLConfigurationsFactoryTest`:
  - `testLazilyInitializedSingletonIsVolatile` reflects on the field and asserts `Modifier.isVolatile` (deterministic, fails before the fix).
  - `testConcurrentCurrentReturnsSingleInstance` races 8 threads through `current()` and asserts one shared instance (passes with and without the fix, a sanity check only).

## Guesses to verify first
1. `SSLConfigurationsFactory.setCurrent(null)` is acceptable to reset state (it is `@VisibleForTesting`, no null guard).
2. `new SSLConfigurations(new SSLCredentialProviderFactory())` can be constructed in a plain `SolrTestCase` without extra system properties.
3. Other lazy-singleton DCL sites in the repo may exist; this branch only touches the one the earlier triage identified. A broader search (`synchronized` + second `== null` check on a non-volatile static) was not done.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.util.configuration.SSLConfigurationsFactoryTest"
```
Fail-before: remove `volatile`; `testLazilyInitializedSingletonIsVolatile` should fail.

## Not done
No JIRA comment, no PR.
