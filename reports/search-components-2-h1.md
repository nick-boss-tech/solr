# Search components round 1, sub-batch 2, part h1 (RealTimeGet family)

Result: SOLR-15018 and SOLR-8009 are draftable (8009 after two checks). SOLR-8954 is held and must not land as written, because by reading it breaks an existing test. SOLR-8767 is held for the owner's /get shape decision. No build, test, gh call, post, or commit was made.

Heads used: the origin/* refs in this worktree, which match the claim table (8009 at 8795661ddc98, 8767 at 3b5f2d235732, 8954 at 1d981abe700, 15018 at d0f29b4630c). Base for the diffs: 8009 at 14c7aac0d15, 8767 at 14c7aac0d15, 8954 at e432df19c4a5, 15018 at b5c71bc5573. Upstream main used for merge checks: 8e62c2686882.

## Findings

1. FIX. SOLR-8954 breaks an existing test (by reading; not run).
   - Where: origin/solr-8954-submit, solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java lines 1061-1069 and 1091-1095. Every id with no target slice goes to all shards, for any router.
   - Evidence: CompositeIdRouter.getTargetSlice at 14c7aac0d15 (solr/solrj/src/java/org/apache/solr/common/cloud/CompositeIdRouter.java lines 163-167) returns null for a get with no route when a router field is set. The existing test FullSolrCloudDistribCmdsTest.java asserts that such a get returns 0 documents (lines 300-301 on 14c7aac0d15 and on upstream main; the 8954 head has the same lines). Under 8954 the id goes to every shard, the document is found, and the assert sees 1. The 8954 receipt lists CustomCollectionTest, TestRandomFlRTGCloud and TestRealTimeGet only, so this class was not in its gate. The 8009 head keeps this behavior by restricting the fallback to ImplicitDocRouter (8009 head lines 1072-1074), and its own comment says the composite result is the established behavior.
   - Round-28 review (research/branch-reviews/round-28/SOLR-8954-review.md, MEDIUM) found the broad fan-out but not this test.
   - Replacement: do not post or merge 8954 as it stands. If its fix is kept, add `import org.apache.solr.common.cloud.ImplicitDocRouter;` and change line 1068 from `unroutedIds.add(id);` to `if (coll.getRouter() instanceof ImplicitDocRouter) unroutedIds.add(id);` (8009 uses the same check at its lines 1072-1074), then re-gate with FullSolrCloudDistribCmdsTest in the run. The changelog title already says implicit, so it stays.

2. FIX. The 8009 receipt's test count does not match the file.
   - Where: receipts/SOLR-8009.md line 6 says "FullSolrCloudDistribCmdsTest 10 tests".
   - Evidence: at 8795661ddc98 the file has 9 public test methods: testBasicUpdates (line 98), testRealTimeGetImplicitRouterWithoutRoute (157), testDeleteByIdImplicitRouter (186), testRTGCompositeRouterWithRouterField (304), testDeleteByIdCompositeRouterWithRouterField (338), testThatCantForwardToLeaderFails (455), testIndexingOneDocPerRequestWithHttpSolrClient (757), testIndexingBatchPerRequestWithHttpSolrClient (779), testConcurrentIndexing (845). The base has 8. The other 8009 counts match the files: TestRealTimeGet 4 active tests, TestRandomFlRTGCloud 2 (testCoverage, testRandomizedUpdatesAndRTGs), TestAddFieldRealTimeGet 1. The gate log is not on disk, so the 10 cannot be confirmed here.
   - Replacement: the Proof line in pr-drafts/search-components/SOLR-8009.md reads "10 of 10". Confirm the count from g8009-gate.log. If it is 9, change that item to "FullSolrCloudDistribCmdsTest 9 of 9". Do not post until settled.

3. FIX. In 8009, one id on two shards trips an existing assertion, and the receipts do not name it.
   - Where: 8009 head RealTimeGetComponent.java, mergeResponses lines 1169-1181 (docList.addAll at line 1179, no dedupe), and addDocListToResponse lines 1190-1199 (assert docList.size() <= 1 at line 1197 for the single-id form, then rsp.add of get(0) at line 1199).
   - Evidence: an add with no route on an implicit collection goes to the receiving core's own shard (DistributedZkUpdateProcessor.java lines 720-727 at 14c7aac0d15), so one id can sit on two shards. The multi-id form returns both copies. The single-id form hits the assert when assertions are on. Test JVMs run with -ea -esa when tests.asserts is true, and its default is true (gradle/testing/randomization.gradle lines 91, 187-188 on upstream main).
   - Replacement: owner decision. Either delete line 1197 in the 8009 branch (the next line already takes the first copy), which needs a new gate, or keep the second Limits bullet in the draft as written, which it is now. The draft already states the case.

4. NOTE. 8009 and 8954 overlap, and the two cannot both land.
   - Trial merges (git merge-tree --write-tree, no refs written): origin/solr-8009-submit with origin/solr-8954-submit gives a content conflict in RealTimeGetComponent.java (both edit createSubRequests). These pairs merge clean: 8009 with 8767, 8009 with 15018, 8954 with 8767, 8954 with 15018, 8767 with 15018. Each head merges clean onto upstream/main 8e62c2686882.
   - Hunks: 8009 and 8954 change createSubRequests (8009 lines 1062-1103). 8767 changes the ulog add and update branch (base lines 276-299), the removeCopyFieldTargets helper (base lines 626-636), and toSolrDoc(Document) (base line 917). 15018 changes toSolrInputDocument (head lines 873-889). No hunk overlaps another, apart from the 8009 and 8954 pair.
   - Semantics: 8767 and 15018 both touch copy-field targets on different paths. 8767 returns them from /get. 15018 changes the atomic rebuild. The atomic path keeps its copy-field filter in both (8767 head line 860; 15018 head line 880), so they do not contradict each other.
   - Landing order: see Owner decisions.

5. NOTE. 8767 changes the default /get response shape. This is the owner's decision.
   - Evidence: 8767 deletes the copy-field skip in toSolrDoc(Document) (base line 917), so /get returns copy-field targets for both update-log and index documents. The atomic path still filters them (8767 head line 860, toSolrInputDocument). The round-28 review (research/branch-reviews/round-28/SOLR-8767-review.md, MEDIUM) raises the read-modify-write and Cloud MLT effects.
   - Cloud MLT: CloudMLTQParser.java getFieldsFromDoc (lines 87-98) reads the /get document, so copy-field targets with an explicit analyzer now join the default MLT fields. The comment at lines 118-119 says copy-field destinations are not in RealTime Get output, which is now false. The 8767 branch does not change this file.
   - Replacement wording for the changelog and description, if the owner accepts the change: "/get now returns copyField target values. A client that sends a fetched document back can send those values again. Cloud MLT default fields can include them." Only the owner can accept it.

6. NOTE. The 8767 gate ran one test class, so the wider effect is unchecked.
   - Evidence: receipts/SOLR-8767.md line 6 lists TestRealTimeGet 7 of 7 only. A static search finds 49 test files in solr/core/src/test on the 8767 head that call /get or getById. None was checked beyond the RTG tests.

7. NOTE. The 8767 receipt's Proof does not name a test.
   - Evidence: receipts/SOLR-8767.md line 7 says "PASS, as recorded in the gate entry". By reading the 8767 head, TestRealTimeGet.java adds testCopyFieldTargetSetDirectly (line 47), testCopyFieldTargetSetDirectlyAfterCommit (line 59) and testAtomicUpdateOfDocumentWithCopyFieldTarget (line 72). The base skips copy-field targets in the index conversion (base line 917), so these should fail on base. That is a reading, not a record.
   - Replacement: once the owner accepts the change, the Proof must name the test that the gate log shows failing on base.

8. NOTE. The 15018 receipt names two heads. The head that counts is d0f29b4630c.
   - Evidence: receipts/SOLR-15018.md line 3 says the current head is a test-only strengthening on 5d3d36a9ab4, and line 4 names d0f29b4630c as the gated head. git diff --stat 5d3d36a9ab4 d0f29b4630c shows only NestedAtomicUpdateIgnoredFieldTest.java changed (13 insertions, 8 deletions). The production code is the same at both heads.
   - Replacement: none to the draft. The Proof cites d0f29b4630c only. Confirm that g15018r35-gate.log names that head.

9. NOTE. The round-28 15018 finding about the update log is addressed at the head.
   - Evidence: the round-28 review (research/branch-reviews/round-28/SOLR-15018-review.md, P1) said the test added only four filler records. The head writes 120 records over 12 commits (NestedAtomicUpdateIgnoredFieldTest.java lines 86-94). Whether that evicts the add depends on solrconfig-tlog.xml lines 51-52 (maxNumLogsToKeep 10, numRecordsToKeep 100, both system-property overridable). Not run. The second Limits bullet in the draft names this.

10. NOTE. The main-side inventory is out of date for 8954.
   - Evidence: branch-focus-inventory-2026-10-08.md line 212 marks SOLR-8954 "PR-ready". Finding 1 and the round-28 review ("Needs work") say otherwise. Suggested state: held, overlaps 8009, breaks FullSolrCloudDistribCmdsTest.testRTGCompositeRouterWithRouterField by reading.

11. NOTE. The local branch refs are behind the live tips for three of the four tickets.
   - Evidence: refs/heads/solr-8009-submit is 438a50bd94ae (origin 8795661ddc98), refs/heads/solr-8767-submit is dc0184f4e1b (origin 3b5f2d235732), refs/heads/solr-8954-submit is a681a95baa92 (origin 1d981abe700). This review used the origin refs. Do not use the local heads for any later step.

## Task results

SOLR-8009: DRAFTABLE after two checks (Findings 2 and 3). Head 8795661ddc98 matches the claim table. The premise holds by reading: at 14c7aac0d15 the id is dropped (RealTimeGetComponent.java lines 1065-1067). Three files change, the changelog is present, and the final tree has no handoff file (the handoff doc added in 438a50bd94a was removed in 8795661ddc9). The Proof rests on the new test, as the receipt says, but the gate and premise logs are not on disk. The choice is fan-out versus a 400, which has a real cost, so it is posed as the choice to check. Draft: C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\search-components\SOLR-8009.md (about 4,000 characters with links, a little over the 3,500 guide).

SOLR-8767: HELD for the owner's decision. No draft. Head 3b5f2d235732 matches. The gate receipt is green for TestRealTimeGet 7 of 7, but the /get shape change is a compatibility choice the receipt itself says needs an owner decision and a user-facing description. Findings 5 to 7 give the consequences. Round-28 review: Needs work. Nothing here says the change is wrong. The owner needs to accept or reject it first.

SOLR-8954: HELD. Do not draft, and do not land as written. Head 1d981abe700 matches. Its fix is the same as 8009's, but it applies to every router that returns no slice, including compositeId with a router field. That breaks an existing assertion (Finding 1), which its gate did not run. It also conflicts with 8009 on merge (Finding 4). Recommended: 8009 carries the fix, and 8954 then closes as a duplicate or becomes a test-only follow-up on top of 8009 with a new gate.

SOLR-15018: DRAFTABLE. Head d0f29b4630c matches. The production change is limited to toSolrInputDocument (head lines 873-889), and it keeps the copy-field filter for plain values. The head is a test-only strengthening over 5d3d36a9ab4 (Finding 8), and the round-28 update-log finding is addressed (Finding 9). Static counts agree with the receipt: NestedAtomicUpdateTest has 14 annotated tests plus testIncorrectlyUpdateChildDoc, which is 15; NestedAtomicUpdateIgnoredFieldTest has 1; TestRealTimeGet has 4 active. The Jira text for SOLR-15018 is not in the local export, so the draft uses the inventory summary wording. Draft: C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\search-components\SOLR-15018.md. The draft names no Lucene behavior, so no 9.x or 10.x check was owed.

## Owner decisions

1. Which ticket carries the all-shards get fix: SOLR-8009 (recommended, because its code is narrower and its gate ran after the narrowing) or SOLR-8954, narrowed to the implicit router and re-gated with FullSolrCloudDistribCmdsTest in the run. Either way only one lands.
2. For 8009: keep the single-id assertion as it is and publish the second Limits bullet, or delete line 1197 in the branch first (Finding 3).
3. For 8009: confirm the implemented route, fan out to every shard, over the 400 alternative. The draft asks that question.
4. For 8767: accept the default /get shape change with a user-facing description covering read-modify-write and Cloud MLT, or hold it (Finding 5). The Proof needs a named test first (Finding 7).
5. For 8767: whether to run the wider /get test sweep (Finding 6) before deciding.
6. Landing order if the owner approves: 8009 first, then 15018 (independent of 8009 and 8767 on the merge check), then 8767 last. 8954 lands no code.
7. The lead confirms the 8009 count of 10 or 9 from the gate log (Finding 2) before the draft is posted.

## Not checked

- No build, Gradle, test run, gh call, post, push, or commit. Nothing was written except the report and the two drafts.
- Gate logs are not on disk: g8009-gate.log, g8009-premise.log, g8767-harden.log, g8954-gate.log, g15018r35-gate.log. Proof counts come from the receipts. Static counts are from file reads only.
- Live Jira was not queried; no Jira tool was available in this session. Local packets used: research/jira-context/SOLR-8009.json, SOLR-8767.json and SOLR-8954.json. No SOLR-15018 record exists in the local export or the packets.
- Live PRs: no gh call was made, since none of the four is a live PR in the claim table. Newer PRs were not searched for.
- Runtime behavior is read from code and not run: the 8954 regression, the single-id assertion, and the 8767 test-failure reading.
- Not checked: how HttpShardHandler sends an all-shards request (one request per shard, and what happens when a shard has no live replica, which is the open question in the SOLR-8954 Jira comments); the single-valued copy rejection that the round-28 review cites for 8767 (DocumentBuilder lines 177-203 and 356, not re-derived); the other 48 test files that call /get or getById, beyond the RTG tests; the Cloud MLT runtime effect.
- The 8009 receipt's history (first gate failed on the composite-router test, then the fallback was restricted) is not on disk. Only the final head was audited.
- The round-28 reviews were read as context only, not re-run.
