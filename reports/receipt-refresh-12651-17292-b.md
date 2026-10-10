# Receipt refresh round 2, part b: SOLR-17292

Result: draftable, with the Proof numbers taken from the refreshed receipt only. No gate log is on disk and nothing was run. The head `f614a42fbc80` matches the receipt. The remedy is in the code at that head: `ZkController.java` lines 3058 to 3068 wrap the per-collection persist call in its own `catch (KeeperException)` that logs a warning, so the DOWNNODE offer at lines 3074 to 3079 still runs after a persist failure.

The draft had two problems, both fixed in place:

- It cited the old head `e43200b0fb6` with line anchors that match only that old head. At that head there was no inner try, so a persist failure reached the outer catch and the offer was skipped.
- Its node-down outcome sentence ("the same as before") was wrong for non-stale KeeperExceptions. Those were skipped at base and are now sent past. The draft now says so.

Every head reference in the draft names `f614a42fbc80`, the "owed" note is removed, the remedy and the wider node-down behavior are stated, and the Proof is the receipt's numbers.

Length: 4,808 characters with link URLs (UTF-8), 3,071 without. The guide is about 3,500, so the draft is over only when the URLs are counted. It was not trimmed.

## Findings

Draft: `pr-drafts/solrcloud/SOLR-17292.md`.

1. NOTE. `ZkController.java` lines 3052 to 3088 at `f614a42fbc80`. The remedy is confirmed in the code. The inner try opens at line 3058 inside the per-collection loop (line 3053). The persist call is at line 3065, the catch at 3066, and `log.warn("Could not mark replicas down in per replica states for {}", collName, e)` at 3067. The DOWNNODE offer at 3074 to 3079 sits outside the inner try. The outer KeeperException catch is at 3086 to 3088. The receipt's wording is accurate. No change.
2. FIX, draft line 20, before the edit. The old node-down bullet cited `e43200b0fb6` at line 3064 and lines 3070 to 3075. At that head the persist failure reached the outer catch, so the offer was skipped. The "message is still sent" claim holds only at `f614a42fbc80`. Replaced (see Edit 3).
3. FIX, draft line 20, the "same outcome" sentence. Base `PerReplicaStatesOps.java` line 139 catches only NodeExistsException and NoNodeException in persist, so any other KeeperException propagates. Base `ZkController.java` has no inner try, so that exception reaches the outer catch and the offer is skipped. At the head, the inner catch takes every KeeperException from persist, so the offer is now sent after any persist KeeperException, not only after the stale-state retries are exhausted. The draft's "unchanged" sentence was wrong. Replaced (see Edit 3). This widening is the kind of behavior change the draft must state openly.
4. NOTE. The receipt says "the duplicate ERROR log line stays". In the code there is one ERROR per give-up, at `PerReplicaStatesOps.java` lines 151 to 156 at head. The node-down caller logs at WARN (`ZkController.java` line 3067). The second ERROR is the Overseer loop, `Overseer.java` line 388, on the ZkStateWriter path. The draft's Limits line now says exactly that (Edit 6). The receipt's wording should be corrected by the main side.
5. FIX, design question, draft line 15. A grep for `.persist(` in the main sources at head finds six call sites: `DistributedClusterStateUpdater.java` line 467, `ShardLeaderElectionContextBase.java` line 241, `ZkController.java` lines 1968, 2064 and 3065, and `ZkStateWriter.java` line 274. Only the node-down site (line 3066) catches the exception. The others have no try around them, so a NodeExists or NoNode from persist skips `updates.clear()` at `ZkStateWriter.java` line 342 and the updates stay queued. `ZkStateWriter.java`'s catch at lines 349 to 352 handles only BadVersionException. That matches the draft's line 17. Replaced (see Edit 2).
6. NOTE. Distributed node-down path. `ZkController.java` line 3050 runs `executeNodeDownStateUpdate` outside the try at 3052. The calculator path catches Exception at `DistributedClusterStateUpdater.java` lines 941 to 947 and logs an ERROR. Grep finds no persist call on that path. The bullet's scope ("when distributed state updates are off") is correct.
7. NOTE. The inner try also contains `PerReplicaStatesOps.fetch` (line 54 to 55, no throws clause) and `downReplicas` (line 326, no throws clause). Neither declares a checked exception, so the catch matches only persist's KeeperException. No draft change.
8. NOTE. Proof. `TestPerReplicaStates.java` at head has four `public void test` methods (lines 40, 50, 88, 143). The new one is at 143. The draft's two cases match the test body: a stale ADD that the refresh retry fixes, and a case that expects NodeExistsException through `expectThrows`. On base, persist returns silently, so the second case fails. The counts (4 of 4 at head, and 4 tests with exactly 1 failure with the two files reverted) come from the receipt. No run is on disk. The date 2026-10-10 comes from the receipt's ledger line.
9. NOTE. The draft has no "A choice to check" section. That matches the round-1 answer that the Choice section comes out. Nothing removed or added.
10. NOTE. Links. Line 9 (`PerReplicaStatesOps.persist` at `14c7aac0d151` lines 131 to 147) is the only base-blob link. It describes base behavior, which is correct. All other links point at `f614a42fbc80`. `ZkStateWriter.java`, `Overseer.java`, `DistributedClusterStateUpdater.java` and `ShardLeaderElectionContextBase.java` are not in the base-to-head diff, so their base and head line numbers agree.
11. NOTE. Flush and Overseer claims checked against the code. "The flush stops and the updates stay queued": `ZkStateWriter.java` lines 274, 342 and 349 to 353. "The Overseer loop logs the error and retries": `Overseer.java` lines 387 to 390 (catch Exception, log.error at 388, refreshClusterState at 389). The Limits line "retried, not dropped" is a code reading only.
12. NOTE. Changelog: `changelog/unreleased/SOLR-17292.yml` at head has a plain-scalar title with no colon, type `fixed`, author Nick Shanin, and the ticket link. Read by eye, not parsed by a tool.
13. NOTE. Formula. Bold one-line summaries are on all four sections. The AI header and AI assistance footer are kept. No hits for the old hash, no process words, and no em or en dashes.

## Edits made (draft only, in place)

- Edit 1, nine occurrences (replace all). Old: `e43200b0fb6da9c5f443b9368628e5f2103f8e31`. New: `f614a42fbc8046e47cb7ed216f6b52b8a01f594c`.
- Edit 2, line 15. Old: "The last attempt no longer re-reads the state before it gives up. Callers that used to get a silent return now see the failure, at these call sites:". New: the same sentence with the `PerReplicaStatesOps.java` lines 132 to 158 link added, then "Callers that relied on the silent return now get the exception, which is a behavior change for them. These are the call sites:".
- Edit 3, line 20. The node-down bullet now cites `ZkController.java` lines 3058 to 3068 and says the failure to persist one collection's replica states is logged and the loop moves on. It states that the node-down message is now sent after any persist KeeperException, not only after the last stale-state retry (lines 3074 to 3079), and that before this change a KeeperException from persist, for example a lost ZooKeeper connection, skipped that message. The first version of this sentence still said "unchanged", and the agent corrected it before reporting.
- Edit 4, line 24. Old: "**TestPerReplicaStates passed 4 of 4 at head e43200b0fb6 on 2026-10-04. That run is before the node-down change, so a run at the current head is owed before opening.**" New: "**TestPerReplicaStates passes 4 of 4 at head f614a42fbc80, verified 2026-10-10.**"
- Edit 5, inserted after line 24. "With PerReplicaStatesOps.java and ZkController.java reverted to the merge-base, the class runs 4 tests with exactly 1 failure."
- Edit 6, line 34. Old: "The new error log line is written at ERROR. Callers that log the exception again will log it twice." New: "persist writes an ERROR line when it gives up. The Overseer loop logs the same exception again at ERROR, so that failure appears twice at that level. The node-down call site logs it again as a warning."

## Not checked

- Gate logs `g17292-regate.log` and `g17292-regate-attempt1-vm-killed-2026-10-10-0701.log` are not on disk. Not verified here: the 4 of 4 count, the revert result (4 tests, 1 failure), tidy, the Error Prone compile, the module checks, and the changelog parse.
- The seed is left out of public text as an internal run detail.
- The JIRA text the draft quotes at line 9 was not read. JIRA was not accessed.
- The 2026-10-10 verification date comes from the receipt's ledger line, not from a run.
- The ZkController publish and unregister callers, and the `ShardLeaderElectionContextBase` caller chain, were checked only for a missing try around persist, not traced to the top.
- `ZkUpdateApplicator` (the distributed node-down path) was not read in full. Grep shows no persist call on it.
- Overseer re-entry on the still-queued updates was not traced.
- The merge-base used is `14c7aac0d151`. upstream/main is now at `8e62c2686882`. Whether `14c7aac0d151` is still the intended base was not checked.
