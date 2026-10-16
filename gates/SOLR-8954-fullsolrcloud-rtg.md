# Run job: SOLR-8954 measured run of the existing composite-router RTG test (premise-run)

- Branch: solr-8954-submit. Head: 1d981abe7000bacf7cc3473059f8bf693a9c7ce9 (live tip, checked by ls-remote on 2026-10-14). Base: e432df19c4a50b9d54d7fba545397b859cc54f98 (merge-base with upstream/main).
- Module: `:solr:core`. Test: `org.apache.solr.cloud.FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField`. Run this one method only.
- Intended host: any Linux host with the `premise-run` tag (WORKFLOW.md). Windows never claims this.
- Filed by the review lane on 2026-10-14 from the census round 1 follow-up (search components drafts). No code change, no commit, no push, no receipt write.
Status: DONE (premise confirmed by measurement: at head 1d981abe7000 the no-route real-time get returns 1 document where the existing test asserts 0, FullSolrCloudDistribCmdsTest.java:301, seed 15942F994B64B75C; the branch does not land as written)

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

- [x] Head checkout at 1d981abe7000 and base checkout at e432df19c4a5 established and recorded
- [x] `FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField` run at the head
- [x] Same method run at the base
- [x] Results recorded in the Result section, job marked DONE

## Result

Run 2026-10-14 (log `gates/logs/vm2/queue-g8954-gate-20261014T100614Z.out`, runner `gates/g8954-gate.sh`), one method per run, worktree tree clean after each run.

- Head `1d981abe7000`: rc=1. `FullSolrCloudDistribCmdsTest` tests=1 failures=1 errors=0. Failure message `java.lang.AssertionError: expected:<0> but was:<1>`. Seed `15942F994B64B75C`. The stack line was not captured: the base run overwrote the JUnit XML before it was read, so the line is open. The job's expected line is 300-301.
- Base `e432df19c4a5`: rc=0. tests=1 failures=0 errors=0 (passes, as expected).

Reading: the head fails the assertion that the real-time get returns 0 documents, and the base passes it. The failing assertion matches the job's reading of the head. The owner decision for this ticket (`reports/search-components-2.md`, decision 1) is unchanged by this run. Not relaunched; the failure is for the main agent to decide on.

## Main-agent classification (2026-10-15): premise confirmed by measurement

- Classification: premise confirmed by measurement. This run is the job's deliverable: a recorded measurement, and the measured result is the one the job's reading predicted.
- What the 0 and the 1 count: documents returned (numFound) by a real-time get with no `_route_` for document "3" on a compositeId collection with router field `routefield_s`. The method's only assertion expecting 0 is line 301, `assertEquals(0, docsNoRoute.getNumFound())`; its with-route assertion at line 306 expects 1.
- The open stack line is now captured. A corroborating run on vm1 at the same head and the same seed (log `g8954-vm1.log`, worktree solr-8954-vm1, fresh JUnit XML) gives tests=1 skipped=0 failures=1 errors=0 with the failure at FullSolrCloudDistribCmdsTest.java:301, message `java.lang.AssertionError: expected:<0> but was:<1>`, seed 15942F994B64B75C. Two hosts, one result. The same method passes at the merge-base e432df19c4a5 (vm2's leg, rc=0), and the test file is byte-identical at base and head: an existing test the branch's production change breaks.
- Mechanism, from the trees: CompositeIdRouter is unchanged by the branch and returns null deliberately for a no-route get when a router field is defined (its own comment states this), and pre-branch code skipped null-slice ids, so the get returned 0. The branch's only production change (RealTimeGetComponent.java, +12 lines) collects every null-slice id for every router and sends it to all shards, so the document is found and the get returns 1.
- Interaction with the standing classification: unchanged, and now measured rather than read. The branch is branch-caused and does not land as written (owner decision 1 in `reports/search-components-2.md`). SOLR-8009 carries the all-shards fix; this branch's fan-out is not the vehicle for it.
