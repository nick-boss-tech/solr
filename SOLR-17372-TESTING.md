# SOLR-17372 - hypothetical-reproduction handoff

**Read this first: the change on this branch was written without being compiled or run.** The audit pipeline has no Gradle access.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17372 - "Reproducing failure in StatsComponentTest.testPercentiles" (Jan Høydahl, 2024). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-17372-submit` off `apache/solr` main `e2cdb2d7e8ae`
- Known failing seed from the ticket: `-Ptests.seed=2074D8EC40F42163` (expected 60.0, got 58.29).

## Nature of the change
**Test-only.** `testPercentiles` asserted every percentile of 100 values (each repeated 5 times) within 1.0. The percentiles are t-digest approximations; with some random digest settings the mid-range ones are off by up to ~2.7 (e.g. 40 -> 37.3), so the test is flaky. The branch widens the tolerance to `max(1.0, 10% of expected)`, which is what Jan suggested in the ticket.

This only masks the flake. It does not investigate whether the compression chosen under random seeds is lower than intended (that would be a product-side question, e.g. in the percentiles stats implementation).

## Guesses to verify first
1. 10% is enough for all seeds (observed worst case: 40 -> 37.3 is 6.75%; 60 -> 58.29 is 2.85%). A relative tolerance may be too loose for p=1.0 and p=2.0, so a floor of 1.0 is kept.
2. No changelog fragment (test-only branch, per the pipeline rule).

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.component.StatsComponentTest.testPercentiles" -Ptests.seed=2074D8EC40F42163
```
Fail-before: revert `StatsComponentTest.java`; the seed above should fail.

## Not done
No JIRA comment, no PR.
