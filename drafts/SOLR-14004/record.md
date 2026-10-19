# Skip record: SOLR-14004

- Ticket: SOLR-14004, NPE on exceeding timeAllowed. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-04, updated 2020-01-21, no fix versions, no links; one comment (2019-12-04) with the cluster configuration.
- Verdict: close-fixed.
- Draft branch: solr-14004-draft-close (new; supersedes the keep-open draft solr-14004-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Fix commit, ancestry, changed code and test checked at this head (detached worktree).
- Source: remediation redo of batch 05, item 12, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-3, 2026-10-06, confirmed-skip) said the NPE was fixed by SOLR-14758; that claim is checked here.

## Ticket reading

With timeAllowed set and a sort in a distributed query, a shard that returns a partial response without sort_values produces an NPE when the coordinator merges ids.

## Fix commit and code at the pinned head

- Fix commit: a72c993123 (2021-08-10), "SOLR-14758: Fix NPE in QueryComponent.mergeIds when using timeAllowed and sorting (#251)". `git merge-base --is-ancestor a72c993123 498a09aea97aac86c2cc7ac114113b946d249942` succeeds.
- The commit changes solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java (the sort_values check in mergeIds), and adds a regression check to solr/core/src/test/org/apache/solr/handler/component/DistributedQueryComponentCustomSortTest.java.
- Changed code present at the pin: QueryComponent.java:1178-1183 (in mergeIds), `sortFieldValues = SolrResponseUtil.getSubsectionFromShardResponse(rb, srsp, "sort_values", true)` followed by `if (null == sortFieldValues) { sortFieldValues = new NamedList<>(); }`. The null from a partial shard response is replaced by an empty list before the sort-values check, so the NPE path is closed. The surrounding code was refactored after the fix, but the null protection is still there.
- Regression test present at the pin: DistributedQueryComponentCustomSortTest.java:240-251, "Regression check on timeAllowed in combination with sorting, SOLR-14758", which runs the distributed query with `timeAllowed` set to 1000 and a sort.

## Reproduction attempts

Not run in this pass. The regression test runs the exact combination (timeAllowed plus sort, distributed). Running it on the pin was not done here.

## Gap analysis

The ticket's symptom (NPE when timeAllowed is exceeded and the query sorts) is covered by the null handling and the regression test in the same fix line. The later SOLR-14758 commit (47c1fdcfb7, "Fix query execution to pass shards") is an ancestor too and is not needed to close this. The record does not claim the regression test was run.

## Verdict justification

close-fixed: the fix commit a72c993123 addresses the reported NPE, it is an ancestor of the pinned head, the changed null handling is present at QueryComponent.java:1178-1183, and the regression test is present at DistributedQueryComponentCustomSortTest.java:240-251. The earlier audit's claim is confirmed by these reads.

## Limits

This record does not run the regression test on the pin. It does not post anything to JIRA or GitHub. No PR is opened from this draft.
