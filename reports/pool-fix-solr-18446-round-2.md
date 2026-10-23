# Fix report: SOLR-18446 round 2 (phone agent, in session)

Assignment: `assignments/pool-fix-solr-18446-round-2.md`, taken in session by the phone agent at the owner's direction. Source review: `reports/pool-review-solr-18446-round-1.md` (vm3), verdict FIX FIRST. Scope decision (V2 in scope) settled at rung 3 of `material/decision-ladder.md` and recorded in the assignment.

Final head: `40e2c40516ff2dfae51c603f10c91dbcfa7a4688` on `solr-18446-path-routing-phone-agent-2`, confirmed by ls-remote after push. Base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Diff against base: 11 files, 116 insertions, 32 deletions.

## Commits

- `6a3922f18fb` SOLR-18446: Update tests and docs for path-scoped routing. The four precedence-dependent tests rewritten to put their collections in the URL path (BasicDistributedZkTest.testMultipleCollections section, TestDistribIDF.testMultiCollectionQuery, RuleBasedAuthorizationCoreCollectionTest.testSearchAcrossPermittedCollections now addresses the alias in the path, DistributedQueryComponentOptimizationTest.testDistributedQueryDoesNotReadFromZk). AliasIntegrationTest: the param-only CloudSolrClient case restored (the client moves the parameter into the path). Ref Guide: solrcloud-distributed-requests.adoc "collection Parameter" rewritten as narrowing; major-changes-in-solr-11.adoc gains a removal note.
- `d19116b07da` SOLR-18446: Keep V2 collection routing path-scoped. V2HttpCall resolves from `origCorename`; the "takes precedence" comment is removed. COLLECTION_PROP is still used for the path template value at line 189, so the import stays. HttpSolrCallCloudTest gains testV2CollectionPathGovernsRouting.
- `40e2c40516f` SOLR-18446: gate repair: apply tidy formatting. Two test files; token streams verified identical before and after, so the commit is formatting only.

## Proof legs

- V2 case, before the fix (tree with the test, without the V2 change), seed 18446: HttpSolrCallCloudTest 5 run, 1 failure, `testV2CollectionPathGovernsRouting`: `expected:<[collection1]> but was:<[collection2]>`. Log `~/workspace/tools/fix18446-v2-before.log` on vm1.
- The four rewritten tests are behavior adaptations, not new assertions; their discrimination is inherited from the routing tests above and from vm3's round 1 base leg (`expected:<[collection1]> but was:<[collection2]>` on base production). The DistributedQueryComponentOptimizationTest assertion is exercised again rather than vacuous because the second collection is now in the path it queries.

## Gate at the final head (vm1, seed 18446, counts from fresh JUnit XML)

- Step 0, changelog parse of `SOLR-18446-path-controls-collection-routing.yml`: rc=0.
- Step 1, tidy: rc=0; two files reformatted, committed as `40e2c40516f` (see above); the gate below ran on the tidied, committed tree.
- Step 2, Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): rc=0.
- Step 4, focused runs: HttpSolrCallCloudTest 5, AliasIntegrationTest 9, TestDistribIDF.testMultiCollectionQuery 1, RuleBasedAuthorizationCoreCollectionTest.testSearchAcrossPermittedCollections 1, DistributedQueryComponentOptimizationTest.testDistributedQueryDoesNotReadFromZk 1, BasicDistributedZkTest.test 1 (the monolithic test that contains the multi-collection section): 18 run, 0 skipped, 0 failures, 0 errors.
- Step 5, `:solr:core:check -x test`: rc=0.
- Process note, recorded for honesty: the first focused invocation filtered on `BasicDistributedZkTest.testMultipleCollections`, a private helper rather than a test method, matched nothing, and still returned rc=0 with no XML for that class. The miss was caught in the XML census and the leg was rerun as the enclosing `test()` method; the counts above include that rerun. A first attempt at the rerun was also killed by a runtime restart at 95 seconds and was relaunched; the XML counted here is from the completed run.

## PR draft

The draft in `receipts/SOLR-18446.md` was updated at the new head: V2 coverage stated, the four rewritten tests and both documentation updates described, Proof replaced with this gate, and the Limits keep the round 1 corrections (root-path 404, disjoint parameter ignored) as behavior statements.

## Verdict

The round 1 FIX FIRST items are all addressed. The branch is ready to open when the owner gives the go.
