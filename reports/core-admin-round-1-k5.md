# Core admin round 1, part k5 (SOLR-5262, 8554, 8628, 11939, 16499, 14098)

Result: one draft written (SOLR-11939, docs only, head d4cff5e76430). SOLR-5262, 8554, 8628 and 16499 are audit only with no draft. SOLR-14098 is green on the receipt but held with no draft. Most important FIX: SOLR-8554 title and premise do not match its ticket.

## Scope and heads

- Live heads checked with `git ls-remote` on this run. All six match the claim table: 5262 `ade8b80264ac`, 8554 `32697b6c3f82`, 8628 `ce8211e05e06`, 11939 `d4cff5e76430`, 16499 `6a2ff7618d9a`, 14098 `dd4995515e88`.
- Local refs: `solr-16499-submit` (local) is at `46b51a28b16`, one commit behind the live tip `6a2ff7618d9`. There is no local `solr-14098-submit`. The other local refs match.
- Behind upstream main (`8e62c2686882`): 5262, 8554 and 8628 are 37 commits behind, 11939 is 39, 16499 is 56, 14098 is 67.
- Trial merge onto `upstream/main` with `git merge-tree --write-tree` (no ref written): all six merge cleanly.
- No builds, no tests, no `gh` calls, nothing posted or committed.

## Findings

1. FIX (5262, 8554, 8628, 11939, 16499). Each branch head carries a root-level handoff file: `SOLR-5262-TESTING.md` (31 lines), `SOLR-8554-TESTING.md` (31), `SOLR-8628-TESTING.md` (41), `SOLR-11939-TESTING.md` (21), `SOLR-16499-TESTING.md` (31). Each has a "hypothetical-reproduction handoff doc" commit. Evidence: `git diff --name-only <merge-base> <head>`. Replacement: before any PR, squash to the code, test and changelog commits only. Drop the root TESTING file and the "hypothetical-reproduction" commit subjects. (14098 has no such file.)

2. FIX (8554, title and premise). The ticket (`research/jira-context/SOLR-8554.json`, Description) asks to move RebalanceLeader and ForceLeader into `OverseerCollectionMessageHandler`, for concurrency and an async option. The branch does neither. Its own note says the move "is NOT attempted" (`SOLR-8554-TESTING.md`, line 5). The branch changes only the wait loop in `solr/core/src/java/org/apache/solr/handler/admin/api/ForceLeader.java` (helper at line 96, call at line 184). Replacement title: "SOLR-8554: FORCELEADER returns an error when no active leader appears or the shard is removed". Replacement Limits line: "This does not move FORCELEADER or REBALANCELEADERS into the Overseer, and it adds no async option. The ticket stays open for that."

3. FIX (8554, changelog title). `changelog/unreleased/SOLR-8554-forceleader-wait-failure.yml`, line 3: "(it used to throw a NullPointerException)". On upstream main, `ForceLeader.java` lines 161-168 catch the NPE in the generic `catch (Exception e)` and wrap it in a SolrException "Error executing FORCELEADER operation". Replacement for that phrase: "(it used to fail with a generic error that wrapped a NullPointerException)".

4. NOTE (8554, side effects). `ForceLeader.java` sets shard terms before the wait (`setTermEqualsToLeader`, line 181), and the wait starts at line 184. With this change, a FORCELEADER that times out returns an error after those term changes. Nothing undoes them. Limits line: "If no active leader appears, FORCELEADER still makes its shard term changes first, and this change does not undo them. The request then returns an error."

5. NOTE (8554, proof). `ForceLeaderWaitTest` calls `ForceLeader.waitForActiveLeader`, which upstream main does not have. On the base code the test does not compile, so it cannot show a failing base run. The queue rules call a compile failure INCONCLUSIVE. Replacement Proof wording when drafted: "The new test class calls a helper the base code does not have. It checks the new behavior and does not show a failing run on the base code." Mocking is fine: `Slice.getLeader` (`Slice.java` line 288) and `Replica.getState` (`Replica.java` line 318) are public and not final.

6. NOTE (8554, behavior change). A timed out FORCELEADER now returns a 500 where main returned success after an INFO log line. `ForceLeaderTest.doForceLeader` (upstream main, line 312) sends the request and does not check the reply, so it may surface the new error. Not run. Limits line for the draft.

7. NOTE (8554, internal note). `SOLR-8554-TESTING.md` line 5 says the "ForceLeader never waited for the shard responses" defect still shows on main. Upstream main `ForceLeader.java` sends no shard requests (no shardHandler or ShardRequest use in that file). The branch does not touch that point. Do not cite it in the PR. Drop the claim from the internal note.

8. FIX (16499, ref guide default). `solr/solr-ref-guide/modules/deployment-guide/pages/cluster-node-management.adoc`, upstream main line 858: timeout "Default: `300` seconds". The code default is 600: `ReplaceNodeCmd.java` line 55 (main) reads `message.getInt("timeout", 10 * 60)`. The branch note says the guide "already documents both parameters; no doc change was made", which misses the wrong number. Replacement for line 858, if 600 is kept: "|Optional |Default: `600` seconds". Owner picks the default (see Owner decisions).

9. FIX (16499, v2 description). `solr/api/src/java/org/apache/solr/client/api/model/ReplaceNodeRequestBody.java` lines 63-66 (branch): "Time in seconds to wait for the replicas to become active, per replica move." The wait is not per move. `ReplicaMigrationUtils.java` (upstream main) line 182 waits once for all new replicas, and line 191 waits once for leader recovery. Replacement: "Time in seconds to wait until new replicas are created, and until leader replicas are fully recovered. Defaults to 600 (10 minutes)."

10. NOTE (16499, behavior change). On main, v1 REPLACENODE drops `parallel` and `timeout` (`CollectionsHandler.java` REPLACENODE_OP, and `ReplaceNode.createRemoteMessage`). After this change both take effect. A request with `parallel=true` now moves replicas in parallel. A request with `timeout` now uses that value. Replacement wording for "What this change does" when drafted: "REPLACENODE now passes parallel and timeout to the overseer, so parallel=true moves replicas in parallel and timeout sets the wait."

11. NOTE (16499, decision). The ticket allows two routes: keep the parameters and plumb them, or remove them from the guide and the code. The branch plumbs them. Noble Paul's 2022 comment on the ticket supports plumbing. Owner call. If drafted, this becomes a Choice section.

12. NOTE (16499, generated artifacts). The branch changes a public v2 model (`ReplaceNodeRequestBody.java` lines 53-68). Whether generated OpenAPI or SolrJ artifacts need regenerating was not checked.

13. NOTE (16499, local ref). Use the origin tip `6a2ff7618d9`, not the local branch at `46b51a28b16`. No replacement text needed.

14. NOTE (5262, premise holds from reading). Upstream `CoreDescriptor.java` lines 248-255 build the substitutable map from `coreProperties` only. The defaults map (lines 82-88) has no ulogDir entry. `git grep` finds no `solr.core.ulogDir` outside docs in upstream main. First gate must show: (a) a config using `${solr.core.ulogDir}` with no ulogDir in core.properties fails on base; (b) `TestCoreDescriptorImplicitProperties.testUlogDirDefaultsToDataDir` fails on base (the base value is null, so the assert fails); (c) both pass with the change. Not run.

15. NOTE (5262, default value). Using dataDir is consistent with the update log. `UpdateLog.java` (upstream main) resolves a null ulogDir to the core data dir (lines 412-432) and puts the tlog under `<ulogDir>/tlog` (line 377). So `solr.core.ulogDir` set to dataDir gives the same tlog location as today. Replacement sentence for "What this change does": "When ulogDir is not set, solr.core.ulogDir takes the dataDir value. The update log already keeps its tlog under the data directory in that case, so the tlog location does not change."

16. NOTE (5262, Limits). The default value is the raw dataDir property, which is relative (`data/`) by default. `solr.core.dataDir` already behaves the same way. Limits line: "The value is the configured dataDir as written, so the default stays relative, as solr.core.dataDir already is."

17. NOTE (8628, scope). The ticket's case already works on main. `CachingDirectoryFactory.exists` (upstream main, lines 368-377) returns true only when the directory has an entry, so an empty directory already gets a new index. The branch fixes only a directory whose one file is `write.lock` (`SolrCore.java` lines 860-874 on the branch). Replacement PR title: "SOLR-8628: Create a new index over a directory that holds only write.lock". Limits line: "The empty directory case from the ticket already works on main. This change covers only a directory whose only file is write.lock."

18. NOTE (8628, premise). The branch note says a crash leaves only `write.lock`, because the first segments file appears only after the first commit. Not checked: Lucene 10.4.0 behavior on `OpenMode.CREATE`. If IndexWriter writes its first segments file on open, the window is very small. The premise run must show that the state is reachable.

19. NOTE (8628, live alternative). The ticket has a 2017 comment from Shawn Heisey: "I strongly believe this should be fixed in Lucene, not Solr." (`research/jira-context/SOLR-8628.json`, comment 16222742). A draft would need a Choice section that names the Lucene route.

20. NOTE (8628, shared file). `TestCoreContainer.java`: the branch adds imports at lines 46 and 50 and a test at lines 116-142. SOLR-17297 (part k3) also changes line 46 of its file, so the import block may conflict. Not verified. The two branches have different bases, so a pairwise merge-tree run would not be reliable, and I recorded none. Lead to run it on the final heads. `SolrCore.java`: the 8628 hunk is at lines 856-874 of its file. The SOLR-12007, 5011 and 15003 hunks are at other line numbers in their own files. No line overlap was seen; this is not a trial.

21. NOTE (11939, ADDREPLICA). The branch note says ADDREPLICA gives no way to pick a name. That is wrong on main. `CreateReplica.java` lines 128 and 157-158 pass `property.*` into the message. `AddReplicaCmd.java` lines 355-357 use `property.name` as the core name when `name` is blank. The same lines appear at head `d4cff5e76430`. The draft's Limits names this. Correct the internal note. No change to the PR text is needed for this.

22. NOTE (11939, wording, optional). The branch NOTE is accurate for CREATE. `CreateCollectionCmd.java` lines 306-311 (head) build core names with `buildSolrCoreName`. Upstream `CoreDescriptor.java` line 188 sets the core name, and line 198 skips the "name" property when copying properties. Optional tighter wording for `collection-management.adoc` lines 264-265: "NOTE: Core names are generated by Solr from the collection, shard and replica, such as `techproducts_shard1_replica_n1`. On CREATE, setting `property.name` does not change these names, because one name cannot be valid for every shard and replica."

23. FIX (14098, what the fix covers). `RequestApplyUpdatesOp.java` (branch and main) refuses the call unless the log is BUFFERING: "Core ... not in buffering state" (branch line 41). The 2026-10-02 review (`research/branch-reviews/SOLR-14098-review.md`) quotes a handoff describing a replica "stuck in RECOVERING with the log already ACTIVE". That state is refused, and this branch does not change it. The branch changes only the case where the log is BUFFERING with no buffered updates. Upstream `UpdateLog.java` lines 2052-2059 set the log ACTIVE and return null in that case. Replacement for "What happens today" in any draft: describe the BUFFERING case. The owner must confirm that this is the symptom SOLR-14098 reports.

24. NOTE (14098, ticket text). No `SOLR-14098.json` packet is in `research/jira-context`. The 2026-10-07 round-28 review says the JIRA read returned an empty description. The symptom could not be checked against the ticket here.

25. NOTE (14098, ledger date). `receipts/SOLR-14098.md` says the row was recorded in the 2026-10-02 in-flight table. The head `dd4995515e8` has committer date 2026-10-03 07:25 UTC (author 07:22 UTC). Main side should confirm the row date, or say that the row was updated later.

26. NOTE (14098, proof). The receipt counts match the files: `RequestApplyUpdatesOpTest` has 5 `@Test` methods and `TestRequestApplyUpdates` has 2. No run on the base code is recorded. The round-28 review (2026-10-07) records no focused queue result. The gate log is not on disk. A draft must not claim a failing base run.

27. NOTE (14098, Choice). The op publishes ACTIVE for a replica whose cluster state is RECOVERING, when its buffer is empty. The v2 endpoint `RequestApplyCoreUpdatesAPI.java` (upstream main, line 63) reaches the same action. The 2026-10-02 review's finding 3 asked for a guard against cutting a real recovery short. The branch has only the log state check. The live alternative is to fix the recovery path that leaves the replica in RECOVERING. Owner call.

28. NOTE (14098, publish failure). If `publish` throws after the log is ACTIVE, the op returns "Could not apply buffered updates" (branch lines 51-55). Limits line for any draft: "If the cluster state update fails after the local log is ACTIVE, the request fails, and the two states can differ until a retry."

29. NOTE (14098, standalone). The `isZooKeeperAware` guard (branch lines 88-92) is covered by `testStandaloneCoreIsNotPublished`. Standalone behavior changes: after a buffer is applied, main hit a null ZooKeeper controller, and this branch returns BUFFER_APPLIED. The changelog does not mention this. Optional changelog wording: add "and no longer fails in standalone mode".

## Task results

**SOLR-5262 (audit only, no draft). Verdict: not ready.** The premise holds from reading upstream code (finding 14). The code change is small, and the test is new. No gate or pipeline record exists on the main side. The root TESTING file must go (finding 1). The default value matches the update log's tlog location (finding 15). The first gate and premise run are main-side work still owed.

**SOLR-8554 (audit only, no draft). Verdict: not ready; title and premise mismatch.** The branch narrows FORCELEADER error handling and does not do the Overseer move the ticket asks for (finding 2). The new test does not compile on base, so the fail-before is inconclusive (finding 5). The timeout now errors and leaves term changes behind (findings 4 and 6). Keep SOLR-8554 open. No draft.

**SOLR-8628 (audit only, no draft). Verdict: held.** The ticket's empty-directory case already works on main (finding 17). The branch covers a directory holding only `write.lock`, and the crash premise is not verified (finding 18). The ticket itself names a Lucene route (finding 19). No draft.

**SOLR-11939 (docs only, drafted). Verdict: ready as a review question, once the root TESTING file is dropped.** A docs-only branch needs no test gate, so readiness is judged on the text. The premise checks out from code (finding 22). The ADDREPLICA gap is named in the draft's Limits (finding 21). The draft is at `pr-drafts/core-admin/SOLR-11939.md`, written against head `d4cff5e76430e6deb88115d6675db537b630948e`. The guide was not built.

**SOLR-16499 (audit only, no draft). Verdict: not ready.** There is no gate. The only evidence is a GitHub corroboration run, which does not settle gate state. The plumbing matches the ticket's direction. Two text FIX items (the 300 versus 600 default, and "per replica move") and a behavior change to state (findings 8-10). Owner decides plumb or remove (finding 11). First gate is owed.

**SOLR-14098 (held, no draft). Verdict: green on the receipt, held on scope.** The receipt counts match the test files, and the trial merge is clean. The fix covers only the BUFFERING case with an empty buffer (finding 23). The handoff's symptom (log already ACTIVE) is refused and not changed. The ticket text is not on disk (finding 24). The ledger date is inconsistent with the head date (finding 25). No base run is recorded (finding 26). Publishing ACTIVE for a RECOVERING replica is a live Choice (finding 27). It becomes draftable when the owner confirms the symptom, the proof wording is settled, the ledger date is checked, and the Choice is decided.

## Owner decisions

1. SOLR-8554: keep the ticket open for the Overseer move. Decide whether this narrower change ships now, and approve the new title (finding 2).
2. SOLR-8628: Solr-side fix for a `write.lock`-only directory (this branch), or the Lucene-side route from the 2017 comment (finding 19).
3. SOLR-11939: approve the docs draft. Decide whether ADDREPLICA `property.name` gets its own docs change (finding 21).
4. SOLR-16499: plumb `parallel` and `timeout` (this branch), or remove them. Pick the timeout default: 600 in code, 300 in the guide (findings 8, 11).
5. SOLR-14098: confirm the symptom (BUFFERING with empty buffer, or log ACTIVE with RECOVERING). Decide whether REQUESTAPPLYUPDATES should publish ACTIVE for a RECOVERING replica, or whether the recovery path should change (findings 23, 27).
6. SOLR-5262: accept dataDir as the default value by reading (finding 15), and schedule the premise run and first gate on the main side.
7. Housekeeping (not a PR item): scratch files I created outside the worktree. `/tmp_files_<ticket>.txt` (six files at the Git Bash root) and `C:\Users\shaninna\all_files.txt`. A removal was blocked by the safety check, and I did not work around it. Please delete them. Nothing was committed from them.

## Not checked

- No builds or tests were run. Every "fails on base" statement is a prediction from reading the code.
- Lucene 10.4.0 behavior when IndexWriter opens with `OpenMode.CREATE` (8628, finding 18).
- Whether the reference guide builds (11939).
- Whether generated OpenAPI or SolrJ artifacts need regenerating (16499, finding 12).
- JIRA text for 5262, 8554, 8628, 11939 and 16499 came from packets in `research/jira-context` dated 2026-10-04. Not re-fetched live. The SOLR-14098 ticket text is not on disk.
- Pairwise landing trials for shared files. Merge-tree between branches with different bases is not reliable, so none is recorded. Overlaps are listed by line number only (finding 20).
- `ForceLeaderTest` and `ForceLeaderWithTlogReplicasTest` beyond the send call (finding 6).
- Gate logs for all six tickets are not on disk. 14098 has only a ledger row.
- The Lucene version claim rule does not apply to the drafts here. None names a Lucene version.
