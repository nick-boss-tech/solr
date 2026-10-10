# SOLR-16437 receipt check: part a

Receipt: `receipts/SOLR-16437.md` at `9125af019ec`. Gated head `aa2a6b8afb6f1720c0a04b7869f331ab47bf554d`, which matches `origin/solr-16437-submit`. Base `e2cdb2d7e8ae`. Read only: no builds, tests, `gh` calls, fetches, commits, or edits.

## Verdict

Usable gate, for drafting. Not cleared for push or PR open. One FIX blocks the push: commit `b7d642d4caa` still has a process line in its body. Removing it moves the head (the tree does not change), so the receipt head and any commit-based links in the draft would need refreshing after you approve the rewrite.

## Findings

1. NOTE, packaging diff. `git diff 673ae584de0 aa2a6b8afb6` touches one file: `SOLR-16437-TESTING.md`, deleted, 29 lines. The tree at the gated head differs from `673ae584de0` by that note file alone. The receipt does not name the tidied commit. `673ae584de0` is the only candidate, and the log that would confirm it is missing.
2. FIX, commit body `b7d642d4caa` (subject "SOLR-16437: Reject ADDREPLICAPROP for a collection, shard or replica that does not exist"). The body says "Hypothetical, unrun regression test; see SOLR-16437-TESTING.md." Replacement: delete that body line, so the commit has no body. The adopted packaging rule covers this, but the packaging commit removed only the note file. The rewrite is an owner decision (see the round roll-up), so it waits. Other commits in the range have empty bodies. No trailers in the range.
3. NOTE, subjects. `4fdcad1dc58` ("add hypothetical-reproduction handoff doc") and `aa2a6b8afb6` ("remove the handoff note (packaging for the first gate)") have process words in their subjects. The adopted rule keeps subjects, so these stay unless you decide otherwise. `b7d642d4caa`, `3bbe2d82c93` and `673ae584de0` have clean subjects.
4. NOTE, base and trial merge. The merge-base with `upstream/main` is `e2cdb2d7e8ae`, as the receipt says. Local `upstream/main` is `8e62c2686882`. `git merge-tree --write-tree upstream/main origin/solr-16437-submit` exits 0 (tree `b0c2208d2891`). The shared file with SOLR-8576 checks out: `origin/solr-8576-submit` (`4c46f95c785`) also changes `CollectionsAPISolrJTest.java`, and a trial merge with the 16437 head is clean (exit 0).
5. NOTE, premise, by reading. On base, the call returns success for an unknown collection, shard or replica.
   - Base `AddReplicaPropCmd` (`CollApiCmds.java` line 323) has no existence check. It queues the update at line 349. The request succeeds once the message is queued.
   - Overseer path (the default): `ReplicaMutator.java` line 162 calls `getCollection`, which throws for a missing collection. Line 163 calls `collection.getReplica`, which searches all slices. Lines 165 to 175 throw BAD_REQUEST. That throw happens on the Overseer thread. `Overseer.java` lines 427 to 441 catch, log, and skip it. The client already had success.
   - Overseer-disabled path (`DistributedClusterStateUpdater.java` lines 811 to 829): the same catch, log and skip.
   - Head: `CollApiCmds.java` lines 343 to 362 check the collection (348 to 349), the shard (350) and the replica within that shard (351). Lines 352 to 362 throw BAD_REQUEST. The 400 reaches the client through several hops. The last v1 hop from `CollectionsHandler` to the HTTP status was not traced line by line.
   - Test: `CollectionsAPISolrJTest.java` lines 1293 to 1322. The replica `expectThrows` is at lines 1299 to 1307, with `assertEquals(400, e.code())` at 1307. On base the call returns normally, so that assertion fails. That is the one failure the receipt names. The shard and collection assertions (1310 to 1321) are never reached on base, so the proof discriminates only the replica case. The receipt's wording is accurate on this point.
6. NOTE, receipt numbers, not verified. `g16437-gate.log` is not on disk. There is no SOLR-16437 worktree, and `queue.json` and `timing.csv` have no 16437 entry. A static check gives 24 `@Test` methods at base and 25 at head, which fits one added test. The file has no `@Ignore`, so the source of "1 skipped" is not identified. Recorded as the receipt's numbers.
7. NOTE, changelog fragment at head. The `nick:` line is removed. The title matches the code (400 at `CollApiCmds.java` lines 352 to 362). Authors, type and links are correct. Read by eye; no YAML parser was run.
8. NOTE, receipt wording against the code. No receipt line is wrong. The three checks are at head lines 348 to 362. The diff has two hunks, two imports and the check in `AddReplicaPropCmd`, and `DeleteReplicaPropCmd` is untouched.
9. FIX, adopted wording, not a receipt line. The adopted answer and p5 item 13 say that before this change a wrong-shard request "changed the replica in the other shard". Base does not do that. Line 186 dereferences null for a non-unique property. Preferredleader (unique, `SliceMutator.java` lines 50 to 53) removes the property from every replica in the named shard and sets nothing. The other shard is never written. The draft already has the correct wording (draft line 17). Correction owed to `material/solrcloud-round-1-answers.md` line 168 and `reports/solrcloud-round-1-p5.md` line 79.
10. NOTE, other callers not in the gate. Other base tests call ADDREPLICAPROP and were not in the gate run: `TestCollectionAPI` (about lines 720 to 1004), `TestReplicaProperties` (158 to 220), `TestRebalanceLeaders` (474 to 500), `TestPullReplica` (774, 837), `TestTlogReplica` (791), and `AddReplicaPropertyAPITest`. The sampled calls name replicas inside the shard they give, so the new check should not reject them. The rest were not read. The draft does not claim wider coverage. A run of those classes at the head is main-side work before opening.
11. NOTE, two gaps the proof does not reach. (a) The check reads the cached `ZkStateReader` state (lines 348 to 349), so a replica added moments earlier may not be visible yet. Not exercised. (b) Per-replica-state collections: base `DocCollection.getReplica` has a replicaMap path (`DocCollection.java` lines 382 to 385); the head uses `Slice.getReplica`. Not read. The draft's Limits says per-replica-state collections were not checked.
12. NOTE, scan. The added lines in `solr/` and `changelog/` across the range contain no "hypothetical", "handoff", "TESTING", "receipt", "gate", "premise", "owed" or "round", and no em or en dashes.

## Not checked

- No build, Gradle, test run, `gh` call, fetch, or commit. The receipt's counts, Proof, tidy, Error Prone, module check and changelog parse were not verified. The gate log is absent.
- The source of "1 skipped".
- The final v1 HTTP status hop from `CollectionsHandler` (only the v2 `AdminAPIBase` path was traced).
- The receipts ledger and takeover log the receipt names were not located.
- Live state: `origin/solr-16437-submit` and `upstream/main` were read from local refs, which match the claim's values. No GitHub or JIRA check.
- Whether the gated run used the same test-file bytes. The file is unchanged between `673ae584de0` and `aa2a6b8afb6`, but the run is not on disk.
