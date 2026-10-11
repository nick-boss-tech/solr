# Review confidence round 5, part A (RC5A): SOLR-18530, SOLR-18532, SOLR-18531

Assignment: `assignments/pool-review-confidence-round-5.md`. Claim: `claims/pool-review-confidence-round-5.md` (RC5A slice; the claim file was not edited by this part, per the one-file rule). Date: 2026-10-11.

Scope and limits: read-only. No GitHub write, no comment, review, edit, or API write. No build, test, or gate. One file written (this report). Live bodies read through `research/gh.ps1 pr view` (fields: body, title, state, isDraft, head and base OIDs). Fork tips resolved with `git ls-remote origin`. Receipts read from `receipts/`. Code checked at the PR heads and at the base commit with `git show` and `git diff` on the source checkout (all four SHAs present there). The worktree HEAD is detached at ff97d2f0726, not on a branch named pr-prepare; nothing was changed in it.

## Verdict counts

- SOLR-18530 (PR #5098): DRIFT (2 items, both minor)
- SOLR-18532 (PR #5100): DRIFT (2 items; item 1 is a record conflict with no replacement text)
- SOLR-18531 (PR #5101): DRIFT (2 items)

Record conflicts flagged without replacement text: 2 (see the last section).

## SOLR-18530, PR #5098: DRIFT (2 items)

Live API: OPEN, isDraft false, title "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first". Head `solr-18530-submit` at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d. Base 3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c. Body updatedAt 2026-10-11T01:11:34Z.

Alignment: fork tip (ls-remote) = 14868bc, matching the API head. Receipt gated head is 98e5368d996518b9f4a94f85d7c2fcd933d3f485. The receipt records 14868bc as a changelog-only commit ("SOLR-18530 Drop the changelog entry") that carries the gate over. `git diff` confirms that 98e5368..14868bc only deletes changelog/unreleased/SOLR-18530.yml. The body sits on that recorded delta, so this is not drift. Branch diff base to head: RecoveryAfterSoftCommitTest.java only (the changelog fragment is added and removed inside the range, net zero). The missing changelog fragment is not flagged, per the assignment.

Checked and consistent:
- Title matches the change.
- Receipt counts: 6 of 6 fixed runs, 5 of 5 base runs, 0 failures in both, all as the body says. The body's "checked 2026-10-10 at 14868bc" matches the receipt's carry-over rule.
- "The change touches only this test. No product code changes." (diff confirmed).
- Test citation RecoveryAfterSoftCommitTest.java L102-L117 lands on the leaders-only client block.
- Randomized flag claim: RandomizingCloudSolrClientBuilder sets directUpdatesToLeadersOnly randomly (SolrTestCaseJ4.java L2518); createNewCloudSolrClient calls sendUpdatesOnlyToShardLeaders, which sets only shardLeadersOnly (CloudSolrClient.java L1416-1418). The body's Limits sentence is correct.
- "The wait after the cut allows 30 seconds": ensureAllReplicasAreActive(..., 30) at test L130 (maxWaitSecs).
- Changelog line: "Changelog: none" with the section 4 quote and link at 14868bc (dev-docs/changelog.adoc L123-L131). The quoted sentence matches the source.
- AI header and AI assistance footer present. Each section opens with a bold summary.

Drift 1 (minor): leader-path error sentence.
- Live text (What this change does, URLs shortened to their line anchors): "The error is returned to the client instead in two cases: when the leader changed during the send (the `LeaderChanged` case) ([DistributedZkUpdateProcessor](...#L1254-L1262)), and when the replica is on another shard ([DistributedZkUpdateProcessor](...#L1345-L1348))."
- Evidence: DistributedZkUpdateProcessor.java at 14868bc adds to errorsForClient at L1223 (forward-node errors), L1261 (LeaderChanged), and L1348 (only for a node that is not in this core's replica list and is on another shard). The sentence sits after "a failed send to a replica of this shard". For such a replica the only return path is LeaderChanged. The other-shard case does not apply to this test's cut replica.
- Replacement: "The error is returned to the client instead when the leader changed during the send (the `LeaderChanged` case) ([DistributedZkUpdateProcessor](https://github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java#L1254-L1262))."

Drift 2 (minor, citation convention): unchanged code linked at the head.
- Live links to code the PR does not change, all at 14868bc: DistributedZkUpdateProcessor.java L1229-L1231, L1254-L1262, L1345-L1348; LBSolrClient.java L675-L680; CloudSolrClient.java L213-L217; CheckRetryUnrollTest.java L97-L103. None of these paths is in the branch diff. They explain the pre-change failure path, and method step 4 asks for such citations to link the merge-base and say so. The text does not say "base commit". The lines are identical at both SHAs, so the links resolve to the same code.
- Replacement: in each of those six URLs, replace `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` with `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (line ranges unchanged), and add "base commit" to the link text, for example `([DistributedZkUpdateProcessor, base commit](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java#L1229-L1231))`. The changelog policy link (dev-docs/changelog.adoc) is a changelog line and keeps the head SHA.

Not checkable in this pass: the "What happens today" error text (SolrServerException with a ClosedChannelException cause) comes from the ticket and CI logs. No receipt or workspace file records it.

## SOLR-18532, PR #5100: DRIFT (2 items; 1 record conflict)

Live API: OPEN, isDraft false, title "SOLR-18532: RestoreCore rollback restores the previous index.properties". Head `solr-18532-submit` at 7dfd3d98d0a2f016c520139cbca96ea4a49f6689. Base 3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c. Body updatedAt 2026-10-11T01:11:36Z.

Alignment: fork tip = 7dfd3d98, matching the API head. Receipt gate green at f1e5031fc3a9624b03daf353f26c977891d7d876. The receipt records 7dfd3d98 as a changelog-title-only commit. `git diff` confirms that the only difference from f1e5031 is one line in changelog/unreleased/SOLR-18532.yml. The body's Proof names f1e5031 for the test counts, which is the gated head, and its code links use 7dfd3d98. Both are correct under the receipt's own record.

Checked and consistent:
- Receipt counts: TestRestoreCore 4 tests, 0 failures; RestoreCoreOpTest 1 test, 0 failures; both at f1e5031, verified 2026-10-10.
- Pre-fix proof: with RestoreCore reverted to base, testFailedRestoreKeepsNonDefaultIndexPointer fails at the pointer check (null where the earlier pointer was expected). The body says the same.
- Code at head: bytes read before the switch (RestoreCore.java L210-213); delete only when no earlier file (L297-298); base code deletes unconditionally (base L227). The "file deleted only when the core had no index.properties" claim holds.
- Behavior change "a read error other than file not found now stops the restore before the switch. Before, that error was ignored." Holds: base SolrCore.writeNewIndexProps (L1505-1518) ignores any IOException on open.
- "The base code has the same shape around its delete" holds (base L227 inside try/finally; an exception skips the rest of the rollback).
- Test citation L242-L319 and changelog L1-L7 land on the right lines (the changelog file is 7 lines).
- Polling claim: the 10 x 50 ms loop matches the existing testFailedRestore (TestRestoreCore.java L227-230 and L300-303).
- Teardown Limit ("does not address the teardown failure seen in the install tests") matches the round 9 record (SATISFIED, wording as it stands).
- Non-atomic fallback claim: the delete-then-rename fallback is the base DirectoryFactory.renameWithOverwrite. StandardDirectoryFactory L147-148 reaches it only for a non-file-system base directory, and StandardDirectoryFactory.create() always returns FSDirectory, so "only the EphemeralDirectoryFactory factories use it today" holds in practice. No change proposed.
- AI header and footer present; bold summaries present.

Drift 1 (record conflict; no replacement text): follow-up promise on SOLR-9865.
- Live text (Limits, last bullet about SOLR-9865): "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict."
- Evidence: the facts about SOLR-9865 are right (its head 4937608bb181 writes back the previous directory name; checked in the round 7 to 9 reports). The promise is not. reports/flaky-fix-review-round-1.md D-a says the two changes "cannot both land as written" and that Nick decides "which branch carries the change, or whether to sequence them (s3b-D1)". reports/flaky-fix-post-pr-review-round-9-s2.md item 43 and note N4 call the "I will follow up" commitment Nick's call; the same note was open in rounds 5 to 7. reports/replication-backup-round-1.md L31 records landing order 9865, then 17287, then 9091. That order has 9865 first, the opposite of the body's implied order. No record plans a follow-up on the 9865 branch.
- Replacement: none chosen. The sentence commits Nick to a sequencing decision that the records still list as open. Nick should confirm or strike the promise before the body is changed.

Drift 2 (minor, citation convention): unchanged code linked at the head.
- Live link: "[StandardDirectoryFactory](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java#L130-L148) has a similar non-atomic move when an atomic move is not supported."
- Evidence: StandardDirectoryFactory.java is not in the branch diff (diff covers RestoreCore.java, TestRestoreCore.java and the changelog only). The lines are identical at the base commit. The cited move is existing behavior, so method step 4 asks for the base commit and the words "base commit".
- Replacement: "[StandardDirectoryFactory](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java#L130-L148) (base commit) has a similar non-atomic move when an atomic move is not supported."

Not checkable in this pass: none beyond the ticket context.

## SOLR-18531, PR #5101: DRIFT (2 items)

Live API: OPEN, isDraft false, title "SOLR-18531: Reserve a stopped test runner's port until its next start". Head `solr-18531-submit` at c8a67c7267d00e33ad8323c683c040b539989b0d. Base 3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c. Body updatedAt 2026-10-11T01:11:38Z.

Alignment: fork tip = c8a67c7, matching the API head and the receipt's gated head. gates/SOLR-18531.md records re-gate 3 GREEN at this head. The receipt's status line agrees; its "In flight" paragraph does not (see record notes).

Checked and consistent:
- Diff base to head: JettySolrRunner.java, MiniSolrCloudCluster.java, TestJettySolrRunner.java, and the changelog only (226 insertions, 2 deletions). All are test-framework or changelog paths, so "test-framework change only" holds.
- Test citations: testStoppedRunnerKeepsItsPortUntilRestart at TestJettySolrRunner.java L122; testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart at L177. Both names match the receipt. Both tests do what the body says (close() releases the port; a served-traffic case with address reuse off and on; restart serves again).
- Proof counts: TestJettySolrRunner 4 tests, 2 failures on the reverted framework (both reservation assertions); 4 of 4 at head. LeaderElectionIntegrationTest 1 test, 0 failures; TestCoordinatorRole 9 tests, 0 failures; TestPullReplica 39 tests, 1 skipped (the @Ignore), 0 failures with nightly on. All match the receipt. TestJettySolrRunner has 4 @Test methods.
- Code citations: reserve socket binds with address reuse at JettySolrRunner.java L721-722 (comment L715-720); MiniSolrCloudCluster.shutdown() release loop at L639-646; changelog file has 9 lines (L1-L9).
- Retry claim: retryOnPortBindFailure stops after portRetryTime, which defaults to 60 seconds (JettyConfig.java L83).
- Proxy Limit: SocketProxy.reopen() catches a failed rebind and logs at debug (SocketProxy.java L220-223); AbstractFullDistribZkTestBase.createProxiedJetty exists (L821).
- Linux qualifier on the refused-bind claim matches the receipt's round 4 correction (Windows not verified).
- Stop-then-release behavior for runners removed by stopJettySolrRunner (MiniSolrCloudCluster.java L528-532) matches Limits line 2.
- AI header and footer present; bold summaries present.

Limits check (the assignment's two lines): the body's Limits bullets "A failed start leaves the port unreserved. ..." and "The reservations are held in one map keyed by port, not by runner. ..." are not recorded in receipts/SOLR-18531.md, so the receipt neither confirms nor contradicts them. Their basis is the round 6 record: reports/flaky-fix-post-pr-review-round-6-s2.md items R6-2a and R6-2b (both TRUE against the code at c8a67c7). This pass also finds both true in the code: start(boolean) releases the port at JettySolrRunner.java L517 before init (L521) and server start (L530), with no re-reserve after a failure; RESERVED_PORTS is keyed by port (L125), with putIfAbsent by port (L731) and removal by port (L750). No wording change is proposed for these two bullets. Record gap: the receipt should record both gaps (see record notes).

Drift 1: close() wording in the summary.
- Live text (What this change does): "... and a runner's `close()` releases its own socket."
- Evidence: close() (JettySolrRunner.java L968-978) calls releasePortReservation(), which removes the entry keyed by jettyPort (L746, L750). That entry can be another runner's socket (L731 putIfAbsent keyed by port). The body's own Limits bullet ("Closing a runner releases the reservation on its port even when a different runner has taken that port over") says so. "Its own socket" contradicts both.
- Replacement: see the combined replacement under Drift 2.

Drift 2: shutdown wording in the summary.
- Live text (What this change does, URL shortened to its anchor): "[`MiniSolrCloudCluster.shutdown()`](...#L639-L646) closes the sockets for the runners still in its list after the stops finish, and a runner's `close()` releases its own socket."
- Evidence: MiniSolrCloudCluster.java copies the list before the stops (L627, stoppedJettys), clears it (L632), and then stops each runner, which removes it from the list (stopJettySolrRunner(JettySolrRunner), L528-532). The release loop (L644-646) walks the pre-stop copy. After the stops finish the list is empty, so "still in its list after the stops finish" is false. The accurate condition is "in its list when shutdown began". Limits line 2 and the Javadoc at L639-643 already say this.
- Replacement (replaces the whole sentence): "[`MiniSolrCloudCluster.shutdown()`](https://github.com/nick-boss-tech/solr/blob/c8a67c7267d00e33ad8323c683c040b539989b0d/solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java#L639-L646) closes the sockets for the runners that were in its list when shutdown began, after the stops finish, and a runner's `close()` releases the reservation on its current port, whichever runner holds it (see Limits)."

## Record notes (no body change; main side)

1. Record conflict, no replacement (SOLR-18532): the SOLR-9865 follow-up promise. See SOLR-18532 Drift 1. Nick's decision (reports/flaky-fix-review-round-1.md D-a; s3b-D1; round 9 N4).
2. receipts/SOLR-18531.md: the status line says GATE GREEN at c8a67c7 after re-gate 3, and gates/SOLR-18531.md agrees. The "In flight" paragraph still says re-gate 3 is running with GATE PENDING, and the status line still says "The PR is open as a draft". The live PR is ready (isDraft false). Main side should finalize the receipt; no replacement text is chosen here.
3. receipts/SOLR-18531.md does not record the two Limits gaps (failed start; port map keyed by port and shared). Main side should add them so the body's two bullets have a receipt basis. The round 6 items R6-2a and R6-2b are the current source.
4. receipts/SOLR-18531.md "Change" says "a runner's close() releases its own reservation" and the shutdown sentence says "still in its list after the stops complete". Both repeat the body wording and have the same problem as Drift 1 and Drift 2. Suggested receipt fix: the replacement text in Drift 1 and Drift 2 above.
5. CI-sourced statements are not re-checked in this pass: SOLR-18531's "hit this once in CI ... No assertion ran" (round 1 test 4 record, CI run 37991927523; no CI log on disk), and SOLR-18530's error text.
