# Fix assignment: SOLR-18446 review round 1 fixes (round 2)

Status: TAKEN IN SESSION by the phone agent at the owner’s direction (2026-10-22). Pool hosts: do not claim.

Posted by the phone agent, 2026-10-22. Source: the round 1 review, `reports/pool-review-solr-18446-round-1.md` (vm3), verdict FIX FIRST. Claim before starting by adding `claims/pool-<host>-fix-SOLR-18446-round-2.md` per WORKFLOW.md. The worker must not be phone-agent-2 (builder) and must not be vm3 (round 1 reviewer).

## Scope decision, recorded

The V2 question is settled at rung 3 of `material/decision-ladder.md` (the stated intent): SOLR-18446 says "The URL path alone should govern which collection receives a collection-scoped request," and `V2HttpCall` performs that same resolution as a subclass of `HttpSolrCall`. V2 is in scope for this branch. SOLR-18470 (handler scope) stays out. Keep the work in two commits as specified below so the owner can still split the packaging at opening time.

## Branch

- Branch: `solr-18446-path-routing-phone-agent-2` at `bc15134268e2fba2e29fb3fbc62546065906713d`. Verify by ls-remote at claim time; if the tip moved, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.

## Commit A: complete the v1 behavior (review fixes 2 and 3)

1. Rewrite the four tests that rely on the old precedence so the intended collections are in the URL path (or reached through a `CloudSolrClient`, which moves the parameter into the path):
   - `BasicDistributedZkTest.java:1541` and `:1546` (`testMultipleCollections`).
   - `TestDistribIDF.java:163` and `:172` (`testMultiCollectionQuery`).
   - `RuleBasedAuthorizationCoreCollectionTest.java:125` (`testSearchAcrossPermittedCollections`).
   - `DistributedQueryComponentOptimizationTest.java` (`testDistributedQueryDoesNotReadFromZk`, the calls near lines 749 and 757): the second collection goes into the path so the `existsBefore == existsAfter` assertion is exercised again, not vacuous.
   The round 1 findings here are static predictions. Run each rewritten class once at the new head and record the counts; if a class does not behave as the review predicted, record what actually happened instead of forcing the prediction.
2. Fix the ref guide: `solr/solr-ref-guide/modules/deployment-guide/pages/solrcloud-distributed-requests.adoc:277-285` ("collection Parameter"). State that the parameter can only narrow a path that names several collections (an alias or a comma list) and change the example to such a path.
3. Restore the `CloudSolrClient` null-path case in `AliasIntegrationTest.searchSeveralWays` that the original diff dropped, if it can be restored without weakening the path-governs assertions (review answer 3, recommended not blocking). If it cannot, say why in the report and leave it out.

## Commit B: V2 routing (review fix 1)

1. `V2HttpCall.java:135-137`: replace `queryParams.get(COLLECTION_PROP, origCorename)` with `origCorename` and remove the "&collection= takes precedence" comment. No narrowing logic: V2 already rejects a multi-collection resolution (lines 143-152).
2. Add one discriminating case (`HttpSolrCallCloudTest` or a V2 test): `/____v2/c/collection1/select?collection=collection2` routes to collection1. Show it failing with the parameter winning before this commit and passing after, by run.

## Gate, at the final head

Run the full hardening gate on the final head of both commits: changelog parse, tidy, Error Prone compile, the focused classes (`HttpSolrCallCloudTest`, `AliasIntegrationTest`, plus the four rewritten classes from commit A and the V2 case from commit B), the proof legs for any new or rewritten assertion (a rewritten assertion that never fails on the old behavior is a pin; label it as one in the report, per the ladder's lessons), and `:solr:core:check -x test`.

## PR draft

`receipts/SOLR-18446.md` carries the draft as its final section; vm3 already corrected two false sentences in it. After the fixes, update it once more: the V2 path is now covered, and the Limits must state the root-path behavior (a request with no collection in the path and only a `collection` parameter now returns 404) and the disjoint-parameter behavior (a parameter matching none of the path's collections is ignored). Those two behaviors follow from the ticket and are stated as behavior, not posed as questions.

## Deliverable

`reports/pool-fix-solr-18446-round-2.md` with: both commits and their files; per-class run counts at the final head and, for each rewritten or new assertion, its base or mutation leg result or its pin label; the gate step results; the final head confirmed by ls-remote; and the updated receipt (gate section plus corrected draft) in the same push.

## Limits

No PR opening or state change, no comments, no Jira edits. Do not touch SOLR-18470's handler scope. No history rewrite of `bc15134268`; the two commits stack on top of it. No em dashes. Commit as Nick Shanin with no trailers.
