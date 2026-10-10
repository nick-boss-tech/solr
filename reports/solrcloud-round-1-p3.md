# SolrCloud round 1, part p3 (overseer lifecycle and ZkController): report

Result: SOLR-13186, 15106, 9155 and 15386 are draftable (drafts written); SOLR-16013 is held (its live tip is not gated, and commit 3059f9be884 carries a Claude co-author trailer). No builds, tests, or gh write calls were run.

Heads: the five live origin refs match the claim table (13186 b436d90d2a88, 15106 40b7e5d0efa7, 16013 ba26b7028917, 15386 ca8cb61ee957, 9155 9f08d0330233). The receipts' gated heads equal the live tips for 13186, 15106, 15386 and 9155. For 16013 the gated head is e1bd21fd11a, and the tip has moved.

## Findings

1. FIX. SOLR-16013 carries a Claude co-author trailer.
   - Where: commit 3059f9be884 (message only). Range upstream/main..origin/solr-16013-submit.
   - Evidence: the message ends with "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>". The other four branches have no such trailer. All commits on the five branches are authored and committed by Nick Shanin.
   - Replacement: remove the trailer line from the message of 3059f9be884. That rewrites the fork branch and changes the hashes, so it needs your explicit go-ahead. Do not force-push without it. A rewrite also means the gate has to run again at the new head.

2. FIX. SOLR-16013 live tip has moved past the gated head, with a behavior change the record does not mention.
   - Where: solr/core/src/java/org/apache/solr/cloud/ZkController.java lines 907-926 on the tip (new wait loop); solr/core/src/test/org/apache/solr/cloud/OverseerCloseOrderingTest.java lines 136-193 on the tip (new test).
   - Evidence: the receipt gate is at e1bd21fd11a. After it come 002d0d08a45 ("keep waiting for the overseer close when the shutdown thread is interrupted") and ba26b702891 (tidy). The diff from e1bd21fd11a to the tip touches ZkController.java (24 lines) and the test (96 lines). The round-28 review (research/branch-reviews/round-28/SOLR-16013-review.md, P1) flagged the interrupt path at e1bd21fd11a, and 002d0d08a45 fixes it. The tip's test class has two @Test methods (lines 59 and 136). The receipt counts one. Neither the new wait loop nor the second test has a run on record.
   - Replacement: none for the PR. The record should say: "Gated at e1bd21fd11a. Live tip ba26b702891 adds an interrupt-safe wait (002d0d08a45) and a second test. Not gated. Re-gate at the live tip before any draft."

3. NOTE. SOLR-16013 changelog title says "before releasing the Overseer leader node".
   - Where: changelog/unreleased/SOLR-16013.yml line 1 on the tip.
   - Evidence: the leader node and the election node are ephemeral. They go when the ZooKeeper session ends. The branch changes the order of the overseer close and the session close (ZkController.java lines 930 and 936 on the tip). It does not change any release call.
   - Replacement: "Stopping a node now closes its Overseer before the ZooKeeper session ends, so the old Overseer finishes before a new one can start."

4. NOTE. SOLR-16013 shutdown timing and the wait.
   - Where: ZkController.java on the tip, lines 907-926 (wait loop) and 930-936 (state reader and client close).
   - Evidence: base also waits for the Overseer, but after it has closed the ZooKeeper client (upstream/main ZkController.java line 918, ExecutorUtil.shutdownAndAwaitTermination in the finally block after zkClient.close() at line 910). So the wait is not new. It moves earlier. The state reader and the client now close after the Overseer, so shutdown can take longer by the time those two take. The loop ignores interrupts until the Overseer close ends, as base did.
   - Replacement for the PR text (if the branch is opened): "The node waits for its Overseer to finish before it closes its state reader and ZooKeeper client. Shutdown can take a little longer as a result."

5. NOTE. SOLR-15106 widens the give-up path.
   - Where: solr/core/src/java/org/apache/solr/cloud/Overseer.java lines 776-777 on the tip. Early exits in OverseerTaskProcessor.java (identical on the tip and upstream/main): lines 185-186 and 199-200 (return on IllegalStateException), 226-227 (break when leadership is NO), 415 (return on InterruptedException).
   - Evidence: the wrapper calls stopStateUpdaterAfterProcessorExit for every processor end that the Overseer did not request. Session expiry is one case, not the only one. The changelog says "for example after a ZooKeeper session expiry", which is accurate.
   - Replacement: the draft already says "any unrequested end" and names the early returns. Keep that wording. Do not write "only after a session expiry".

6. NOTE. SOLR-15106 base failure text is not on record.
   - Where: the receipt for 15106 (receipts/SOLR-15106.md, Proof line).
   - Evidence: the receipt says "the premise step fails as designed" and gives no base failure text. The gate log is not on disk. The draft's Proof names the assertion the test makes ("no new processor exists"), which follows from the test code (OverseerProcessorExitTest.java line 53).
   - Replacement: confirm the base failure from the base run before posting. If it cannot be confirmed, keep the draft's wording, which describes the assertion and not a logged message.

7. NOTE. SOLR-15106 adds a public accessor.
   - Where: Overseer.java lines 827-829 on the tip (getCollectionProcessorThread, marked @lucene.internal). Line 686: updaterThread becomes volatile.
   - Evidence: the accessor is only used by the test. The draft mentions it.
   - Replacement: none. The draft's "What this change does" names the accessor.

8. NOTE. Round-28 P2 findings for 15106 and 15386 are resolved at the live tips.
   - Where: research/branch-reviews/round-28/SOLR-15106-review.md (finding 1) and SOLR-15386-review.md (finding 1).
   - Evidence: both reviews asked to remove an internal TESTING.md from the diff. Those reviews were at older snapshot heads (6263b9ef18b and 994cf509179). The live diffs have three files for 15106 and four for 15386, and no TESTING file.
   - Replacement: none.

9. NOTE. SOLR-9155 restored interrupt status reaches callers.
   - Where: ZkController.java on the 9155 tip. Register (line 1384 and 1399 declare throws Exception) calls getLeader at line 1485. rejoinOverseerElection calls it at line 2608, inside a try whose catch (line 2618) rethrows as SolrException.
   - Evidence: base swallowed the flag, because the InterruptedException was wrapped and the flag was cleared. With this change the flag is set when the SolrException leaves getLeader, and any blocking call those callers make later on the same thread sees the interrupt.
   - Replacement: already in the draft ("Behavior changes, stated openly").

10. NOTE. SOLR-9155 remedy text on the conflicting-leader path.
    - Where: ZkController.java lines 1695-1702 (throws "There is conflicting information about the leader") and 1731-1739 (generic catch that wraps it again with the remedy text).
    - Evidence: the conflict is thrown inside the try. The generic catch wraps it with "Error getting leader from zk for shard ... check that ZooKeeper is reachable and that the shard has an elected leader". On that path ZooKeeper may be reachable, so the remedy is misleading. The cause still carries the conflict message.
    - Replacement, owner decision. Option (a): before the generic catch add `} catch (SolrException e) { throw e; }`, so the conflict message stands. This also affects any other SolrException raised inside the try. getLeaderProps was not read, so that effect is not checked. Option (b): keep the catch and reword the remedy to "check ZooKeeper and the shard's leader state; the cause may name a conflict".

11. NOTE. SOLR-9155 Choice is a real alternative.
    - Where: SOLR-9155 packet (research/jira-context/SOLR-9155.json, Description) and register and rejoin callers (see finding 9).
    - Evidence: the ticket itself complains that InterruptedException is "rethrown as a SolrException". Declaring `throws InterruptedException` on getLeader is feasible: register already throws Exception, and rejoin catches Exception.
    - Replacement: the draft's Choice section already states both routes.

12. NOTE. SOLR-15386 guard does not run in the distributed path.
    - Where: ZkController.java lines 3053-3060 on the tip (distributed branch, executeNodeDownStateUpdate). Base ZkController.java lines 397-399 (distributed updater is on when the Overseer is disabled).
    - Evidence: with the Overseer disabled, publishNodeAsDown marks the node down synchronously and never queues DOWNNODE, so NodeMutator.downNode is not reached.
    - Replacement: the draft's Limits says so. No code change requested.

13. NOTE. SOLR-15386 owner question has its evidence on record.
    - Where: NodeMutator.java lines 58-61 on the tip (one check against clusterState.liveNodesContain).
    - Evidence: the ticket comments (David Smiley, 2021-04-30, research/jira-context/SOLR-15386.json) propose a second check after computing the update, or a ZooKeeper multi tied to live_nodes. The record's open question is the same as this one. The draft poses it as the Choice.
    - Replacement: owner decision (see Owner decisions). The draft describes the code as it stands.

14. NOTE. SOLR-15386 mixed-version behavior.
    - Where: base NodeMutator.java lines 47-53 (downNode reads only the node name).
    - Evidence: an older Overseer ignores the new flag and applies the message as before.
    - Replacement: the draft's Limits says so.

15. NOTE. SOLR-13186 leaves the election sequence node in place.
    - Where: OverseerElectionContext.java lines 70-75 on the tip (start check, return, removal call). LeaderElector.java lines 125-134 on upstream/main (runIamLeaderProcess is followed by no removal of the sequence node).
    - Evidence: after the leader node is removed, runLeaderProcess returns normally. The sequence node stays until the context is cancelled or the session ends. Base does the same.
    - Replacement: the draft's Limits says so.

16. NOTE. Overlap and landing order across the five branches.
    - Evidence: trial merges with `git merge-tree --write-tree` (no ref written) are clean for each branch against upstream/main, and for ten pairs: 9155 with 15386 and 16013, 15386 with 16013, 13186 with 15106, 16013, 15386 and 9155, and 15106 with 16013, 15386 and 9155. The only shared file is ZkController.java (9155, 15386, 16013), and the hunks do not overlap: 9155 at lines 1672-1740, 15386 at lines 845-848 and 3029-3086, 16013 at lines 878-930. Overseer.java (15106) and OverseerElectionContext.java (13186) are each touched by one branch.
    - Shared assumptions: 15106 relies on Overseer.close() setting closeRequested before the processor ends (Overseer.java lines 663-668 and 857-858). 16013 changes when the Overseer is closed, not that flag, so the assumption holds. 13186's cleanup runs while the ZooKeeper client is open; 16013 keeps the client open longer during shutdown, so the cleanup runs against a live client. The 15106 rejoin is skipped when the node is shutting down, which matches 13186's start check.
    - Suggested order: 13186, 15106, 9155, 15386, 16013. There is no content dependency. 16013 goes last because it needs a re-gate and a history fix first.

17. NOTE. The five branches are behind current main.
    - Evidence: `rev-list --count origin/<branch>..upstream/main` gives 40 (13186), 49 (15106), 66 (16013), 56 (15386), 40 (9155). Trial merges onto current upstream/main are clean.
    - Replacement: owner decision (see Owner decisions). A rebase changes every head and every Proof line.

18. NOTE. Local branch refs are not the live tips.
    - Evidence: solr-13186-submit (aebe1eaa0770), solr-15106-submit (efd5f8bd0602), solr-15386-submit (4e1ebd17c4c4), solr-9155-submit (6d9e6b9baa3c) and wt-solr-16013-submit (9c2d70346671) each differ from their origin refs. For example, 13186 has 2 commits only on the local ref and 3 only on origin.
    - Replacement: none. The audit and the drafts use the origin refs. Do not draft from the local refs.

19. NOTE. SOLR-16013 re-gate notes for when the tip is run.
    - Evidence: the new test interrupts the stopper thread after the overseer close has started (OverseerCloseOrderingTest.java line 178). The stopper may not yet be in overseerClosed.get() at that moment. An interrupt that lands in another shutdown wait would make stop throw, and the assertion at line 193 would fail. The two tests share a two-node cluster; the first stops a node and does not restart it, and MiniSolrCloudCluster.stopJettySolrRunner removes the stopped runner from the list (MiniSolrCloudCluster.java lines 528-532), so the second test sees only the live node. None of this has run.
    - Replacement: none now. Check both points on the re-gate.

## Task results

**SOLR-13186: draftable, PR-ready.** Head b436d90d2a88 matches the receipt, the live tip, and the round-28 review. The production change and the test match the receipt's description. Draft: pr-drafts/solrcloud/SOLR-13186.md. Limits: skipped-start path only, mocked container with a real ZooKeeper test server, no multi-node run, sequence node unchanged (finding 15). Owner item: confirm the run date (the receipt gives none).

**SOLR-15106: draftable, PR-ready.** Head 40b7e5d0efa7 matches the receipt and the live tip. The branch's main code and the test match the receipt. The round-28 P2 is resolved (finding 8). Draft: pr-drafts/solrcloud/SOLR-15106.md. The processor-exit versus session-expiry race stays a Limits line with a follow-up offer. Owner items: base failure text (finding 6), run date, and the follow-up call. The draft states the widened give-up path (finding 5).

**SOLR-9155: draftable, PR-ready with one Choice.** Head 9f08d0330233 matches the receipt and the live tip. Draft: pr-drafts/solrcloud/SOLR-9155.md. The Choice is SolrException with the flag restored, against a declared InterruptedException (finding 11). Limits: the flow-control point is not addressed (finding 10 notes the conflict path), and the pre-interrupt covers only the first blocking step. Owner decision on finding 10 before posting.

**SOLR-15386: draftable, with an open owner question.** Head ca8cb61ee957 matches the receipt and the live tip. The receipt's proof is inconclusive by construction, which is correct: the test uses the flag constant that base lacks. Draft: pr-drafts/solrcloud/SOLR-15386.md. The Choice poses the cached live-node check question (finding 13), and the owner must settle the position before posting. Limits: queue mode only (finding 12), no end-to-end test, mixed-version behavior (finding 14).

**SOLR-16013: held, no draft.** Gated at e1bd21fd11a. The live tip ba26b702891 adds the interrupt-safe wait and a second test, and neither has a run. The branch carries a Claude co-author trailer (finding 1). A draft cannot state an honest Proof at the live tip. Once the re-gate runs and the trailer is gone, a draft can follow. Its "What this change does" will need the wait behavior from finding 4 and the changelog wording from finding 3.

## Owner decisions

1. SOLR-16013: approve a fork-branch history rewrite that drops the Claude trailer (finding 1), and say whether to squash the five commits. The head changes, so the gate runs again.
2. SOLR-16013: main-side re-gate at the live tip (or at the rewritten head) before any draft or opening (finding 2 and finding 19).
3. SOLR-15386: keep the single cached live-node check, as drafted, or add a check after the update before opening (finding 13).
4. SOLR-9155: keep SolrException with the flag restored, as drafted, or declare InterruptedException on getLeader (finding 11).
5. SOLR-9155: for the conflict path, rethrow the SolrException unchanged, or reword the remedy to cover both cases (finding 10).
6. SOLR-15106: ship the processor-exit versus session-expiry race as a follow-up, or leave it as an offer only (Limits).
7. All five: rebase onto current upstream/main (finding 17), or open on the current bases and keep the pinned heads. A rebase changes every head and every Proof line.

## Not checked

- No builds, Gradle, or tests (per the claim). Compile, tidy, Error Prone, and test counts come from the receipts. Gate logs are not on disk and were not read.
- Run dates: the receipts give record dates, not run dates. The drafts say "[date to confirm]" in the Proof lines.
- Base failure text: given in the receipts for 13186 and 9155. Not recorded for 15106. For 15386 no base run is possible by construction.
- Heads came from the origin/* remote-tracking refs, not a fresh `git ls-remote`. They match the claim table.
- Live PR state for these five: no gh calls (p6 owns consistency). The round-28 reviews (dated 2026-10-07) found no open PR for them at that time.
- Jira: read from the hydrated JSON packets in research/jira-context, not a live Jira call. The packets are old; the 16013 packet is a 2022 snapshot. Current Jira state was not checked.
- Changelog YAML parse, tidy, Error Prone, and module checks: receipts only.
- The round-2 review of 16013 was not read. The round-28 reviews were used only as context, and each claim taken from them was checked against the live tip.
- The 15106 receipt says the shipped tree is byte-identical to the gated tree. No tree hash is recorded, so this cannot be checked.
- The closed-client exception type for SolrZkClient.getData (which the 13186 catch relies on) was not verified. The client wraps Curator.
- Whether the cached live-node list is current when a DOWNNODE is applied (15386) was not verified from Overseer code. Watch lag is an owner question.
- getLeaderProps internals (needed for finding 10, option a) were not read.
- No draft names a Lucene version, so the Lucene version rule was not applied.
- Drafts and this report were checked for em dashes and en dashes (none) and for internal words in the PR text (none outside the owner notes, which come off before posting).
