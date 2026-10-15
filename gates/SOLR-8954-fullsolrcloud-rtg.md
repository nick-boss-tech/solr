# Run job: SOLR-8954 measured run of the existing composite-router RTG test (premise-run)

- Branch: solr-8954-submit. Head: 1d981abe7000bacf7cc3473059f8bf693a9c7ce9 (live tip, checked by ls-remote on 2026-10-14). Base: e432df19c4a50b9d54d7fba545397b859cc54f98 (merge-base with upstream/main).
- Module: `:solr:core`. Test: `org.apache.solr.cloud.FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField`. Run this one method only.
- Intended host: any Linux host with the `premise-run` tag (WORKFLOW.md). Windows never claims this.
- Filed by the review lane on 2026-10-14 from the census round 1 follow-up (search components drafts). No code change, no commit, no push, no receipt write.
Status: CLAIMED (claims/pool-vm2-gate-backlog-8954.md)

## Why this run is owed

The gate receipt for this head covers CustomCollectionTest (4 of 4), TestRandomFlRTGCloud (2 of 2) and TestRealTimeGet (4 of 4). It does not cover FullSolrCloudDistribCmdsTest. Reading the head, the test should now fail:

- The test asks a real-time get with no `_route_` on a `compositeId` collection that has a router field, and expects 0 documents (`solr/core/src/test/org/apache/solr/cloud/FullSolrCloudDistribCmdsTest.java` lines 300-301).
- `CompositeIdRouter.getTargetSlice` returns null for that get (`solr/solrj/src/java/org/apache/solr/common/cloud/CompositeIdRouter.java` lines 163-167).
- The branch sends every null-slice id to all shards for every router (`solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java` lines 1061-1069 and 1091-1097). The document is found, so the assertion should see 1.

This is a reading, not a measured result. The run settles it.

## Run shape

1. Run the one method at the head, 1d981abe7000. Record pass or fail, the assertion text, the line, and the seed.
2. Run the same method at the base, e432df19c4a5. Record the result. The expected base result is pass.
3. Record both outcomes below. Do not edit the branch from this job.

## Use of the result

The owner decision recorded in `reports/search-components-2.md` (decision 1) says: close SOLR-8954 or make it a test-only follow-up, with SOLR-8009 carrying the all-shards fix. This job does not change that decision. Its only purpose is that the next step starts from a measured fact, not from a reading.

## Step checklist

- [ ] Head checkout at 1d981abe7000 and base checkout at e432df19c4a5 established and recorded
- [ ] `FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField` run at the head
- [ ] Same method run at the base
- [ ] Results recorded in the Result section, job marked DONE

## Result

Run 2026-10-14 (log `gates/logs/vm2/queue-g8954-gate-20261014T100614Z.out`, runner `gates/g8954-gate.sh`), one method per run, worktree tree clean after each run.

- Head `1d981abe7000`: rc=1. `FullSolrCloudDistribCmdsTest` tests=1 failures=1 errors=0. Failure message `java.lang.AssertionError: expected:<0> but was:<1>`. Seed `15942F994B64B75C`. The stack line was not captured: the base run overwrote the JUnit XML before it was read, so the line is open. The job's expected line is 300-301.
- Base `e432df19c4a5`: rc=0. tests=1 failures=0 errors=0 (passes, as expected).

Reading: the head fails the assertion that the real-time get returns 0 documents, and the base passes it. The failing assertion matches the job's reading of the head. The owner decision for this ticket (`reports/search-components-2.md`, decision 1) is unchanged by this run. Not relaunched; the failure is for the main agent to decide on.
