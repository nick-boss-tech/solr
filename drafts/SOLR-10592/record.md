# Skip record: SOLR-10592

- Ticket: SOLR-10592, "SolrCoreMetricManagerTest.testRegisterMetricsWithReplacements() failure". JIRA status read 2026-10-20T12:17:20Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: close-obsolete. This replaces the recorded keep-open: the test method the ticket names is no longer in the tree.
- Draft branch: solr-10592-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:17:20Z (UTC).

## Ticket reading

A seeded run of `SolrCoreMetricManagerTest.testRegisterMetricsWithReplacements` fails with `expected:<79> but was:<81>` in `assertRegistered`. Two Jenkins reports, 2017. No later comments.

## Code reading (pinned head)

- The grep the verdict rests on: `git grep -n testRegisterMetricsWithReplacements 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`. Result: empty. The method does not exist at the pinned head.
- The class `solr/core/src/test/org/apache/solr/metrics/SolrCoreMetricManagerTest.java` (171 lines) now has four methods: `testRegisterMetrics` (line 60), `testReregisterMetrics` (96), `testNonCloudRegistryName` (139), `testNoRegistryAfterUnload` (147). Its assertions were rewritten in the metrics cleanup (SOLR-17855, commits `cee53df8e0` and `a466f04441`, both 2025).
- The `assertRegistered` helper at line 72 is still present, but the failing assertion from the ticket is not.

## History

- `cee53df8e0` (2025-10-14) SOLR-17855: cleanup of Dropwizard metric code and tests. `a466f04441` (2025-10-06) SOLR-17855 follow-up. Both are ancestors of the pinned head (path-scoped log on the test file).

## Reproduction attempts

Not run. The ticket's method is absent, so the seed from 2017 has nothing to run against. A run of the four current methods is not a test of the ticket.

## Gap analysis

The failing test was removed by the metrics rewrite. The current metrics tests are a separate question, and not raised here.

## Verdict justification

close-obsolete meets the rubric: the test method the ticket is about is removed, and the grep under `solr/` at the pinned head is empty. The grep and its result are recorded above.

## Limits

This record does not establish that the replacement tests cover the same behavior. It does not name the exact commit that removed the method.
