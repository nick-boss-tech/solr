# SolrCloud round 1, part p2: AddReplicaCmd and backup/restore

Result: SOLR-15035 and SOLR-15863 are draftable (drafts written); SOLR-12651 is draftable but held until its live tip f3131d1ee84 is run; SOLR-11479 is audit only and held. Six FIX items; the most important is that the 12651 changelog title and draft overstate what the cleanup covers.

Heads checked against the claim table (local origin/* refs, not re-fetched): 11479 at 7e538e844c4 (no gate), 15035 at 12d7491e82c (gated), 12651 gated at 90032e274b7 with live tip f3131d1ee84 (two commits ahead, not gated), 15863 at f381fd8d4dd (gated at live tip). No live PR exists for any of the four heads (`gh pr list --repo apache/solr --head <branch> --state all`, read only, 2026-10-10).

## Findings

1. FIX (SOLR-11479). File `SOLR-11479-TESTING.md` (23 lines) is in the branch diff against upstream/main at 7e538e844c4. Evidence: line 1 reads "hypothetical reproduction (nothing was compiled or run)", and line 19 reads "Test (guessed)". Replacement: delete the file in a commit on the ticket branch before any push or PR. Not done here.

2. FIX (SOLR-11479). `changelog/unreleased/SOLR-11479-addreplica-property-corenodename.yml` line 1. Evidence: `AddReplicaCmd.java:141-148` rejects `property.coreNodeName` when `totalReplicas > 1`, so "accept ... as an alias" is true only for one replica. Replacement for line 1: `title: "ADDREPLICA: accept property.coreNodeName as an alias of coreNodeName for one replica, and reject it when several replicas are requested."`

3. NOTE (SOLR-11479). `AddReplicaCmd.java:146-148`. The new 400 text names only `'coreNodeName'`, so a caller who sent `property.coreNodeName` gets a message about a parameter it did not send. Replacement for the last string literal: `" replicas if 'coreNodeName' or 'property.coreNodeName' parameter is specified"`.

4. NOTE (SOLR-11479). `CollectionsAPISolrJTest.java:497-515`. The only new test uses one replica. The several-replica rejection has no test. Replacement Limits line: "Requests for several replicas with property.coreNodeName are rejected with a 400. No test covers that path yet. A follow-up can add one on request." Owner may prefer a test before the first gate.

5. NOTE (SOLR-11479 and SOLR-15035). Both change `AddReplicaCmd.java`, but on different lines: 11479 at 141-148 and 360-363, 15035 at 305. Trial merges with `git merge-tree --write-tree` are clean in both orders, and each branch merges cleanly onto local upstream/main 8e62c268688 (exit 0 each). Landing order: 15035 first (gated), then 11479 after its first gate. This is a textual check only.

6. FIX (SOLR-15035). `AddReplicaCmd.java:305` sets the count with `coll.getSlices().size()`. Evidence: `DocCollection.java:297-299` returns every slice; `SplitShardCmd.java:727` sets a split parent to INACTIVE, so it stays in the count; `CreateCollectionCmd.java:370` writes `shardNames.size()` at creation. No test covers a split. Replacement: the Limits bullet in `pr-drafts/solrcloud/SOLR-15035.md` (already written). Owner decision 1.

7. NOTE (SOLR-15035). The receipt records Proof PASS and says the banked line alone fails, but it does not record the observed base failure line, and the gate logs are not on disk. Replacement: the draft carries a bracketed placeholder; paste the base failure line from the run output before opening.

8. NOTE (SOLR-15035). No Jira packet for 15035 is on disk (`research/jira-context` has 11479, 12651, 15863 only), and the round-28 review says the connector returned an empty description and no comments. The draft uses the inventory title. Replacement: none; confirm the ticket summary before opening.

9. FIX (SOLR-12651). `changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml` lines 2-3 at f3131d1ee84. Evidence: the cleanup try covers `RestoreCmd.java:265-316`, which ends before `markAllShardsAsActive` (line 302 inside the try, then `addReplicasToShards` at 319 and `restoringAlias` at 324 are outside). The title says "fails after the new collection has been created", which overstates it. Replacement for lines 2-3: `  A RESTORE that fails before its shards become active now deletes the new collection instead of leaving a partly restored collection behind, unless the restore runs asynchronously.` Owner decision 3 can remove the "unless" clause by a code fix.

10. FIX (SOLR-12651). Async restores are not cleaned up. Evidence: with an async id, each shard call carries a core admin async id (`CollectionHandlingUtils.java:729-731`). A shard install failure is found later in `waitForAsyncCallsToComplete` (lines 794-816), where `addFailure` records it and nothing is thrown, so the catch at `RestoreCmd.java:303` never runs. Replacement: the Limits bullet in the draft (already written). Optional code fix for the owner, placed before `markAllShardsAsActive` (`RestoreCmd.java:302`), with a test still needed:
    ```java
            Object shardFailures = results.get("failure");
            if (shardFailures != null && ((SimpleOrderedMap<?>) shardFailures).size() > 0) {
              throw new SolrException(
                  SolrException.ErrorCode.SERVER_ERROR,
                  "Restore failed while copying shard data for " + rc.restoreCollectionName);
            }
    ```

11. FIX (SOLR-12651). The property-upload leg and its move into the cleanup try are after the gated head. `git diff --stat 90032e274b7 origin/solr-12651-submit` shows `RestoreCmd.java` and `TestLocalFSCloudBackupRestore.java` changed (commits 5fb9fb01717 and f3131d1ee84). Replacement: the draft's Proof line is bracketed. The main side must run the class at f3131d1ee84 before opening. Owner decision 4.

12. NOTE (SOLR-12651, positive check). The round-28 MEDIUM finding (upload outside the try) is fixed at the tip: `RestoreCmd.java:269` is inside the try, and the new props leg is `TestLocalFSCloudBackupRestore.java:172-197` with `PropsPoisonedRepository` at lines 273-285. Not checked: that the constant `ZkStateReader.COLLECTION_PROPS_ZKNODE` (used at line 281) equals the file name `BackupManager.java:291` reads.

13. NOTE (SOLR-12651). Branch history holds `SOLR-12651-TESTING.md` (added in 13e6789badd, removed in 5d38986cf08). The net diff is three files. No action unless the PR is not squashed.

14. NOTE (all four). Bases lag local upstream/main: 12651 by 66 commits, 15035 by 67, 15863 by 40, 11479 by 37. Trial merges onto upstream/main are clean. The base each gate ran on is not recorded. A re-gate should use a current base.

15. NOTE (SOLR-15863). The round-28 P1 is still open at the tip. `BackupCmd.java:345` adds the shard value, which can be null. `minIndexVersion` (`BackupCmd.java:371-389`) skips null and unreadable values, and `BackupCmdTest.java:41-42` says "the caller keeps its default". `BackupProperties.java:83` seeds the running version. A mixed-version cluster can therefore record a value newer than the oldest segment. The behavior is intended by the tests. Replacement: the Limits bullet in the draft. Owner decision 5.

16. NOTE (SOLR-15863). The shared base `AbstractIncrementalBackupTest.java:851-855` is also the base of `solr/modules/s3-repository/src/test/org/apache/solr/s3/S3IncrementalBackupTest.java:44` and `solr/modules/gcs-repository/src/test/org/apache/solr/gcs/GCSIncrementalBackupTest.java:31`. Those tests inherit the new assertion and were not in the gate. Replacement: the Limits bullet in the draft. Owner decision 6.

17. NOTE (SOLR-15863). `changelog/unreleased/SOLR-15863-backup-index-version.yml` line 2 starts in lower case; other entries start with a capital. Replacement for line 2: `  Incremental backup properties now record the oldest Lucene version of the backed up segments instead of the running Lucene version.`

18. NOTE (SOLR-15863). The round-28 P2 (tests cover only a current-version report) is addressed at the tip by `BackupCmdTest`. The round-28 snapshot head 4bda993525e is stale; the live head is f381fd8d4dd. No action.

19. NOTE (SOLR-15863 and SOLR-12651). The assignment says both share `TestLocalFSCloudBackupRestore`. They do not. 15863 changes no file that 12651 changes. Its test-framework edit is `AbstractIncrementalBackupTest.java`, which `TestLocalFSCloudBackupRestore` does not extend (it extends `AbstractCloudBackupRestoreTestCase`). Trial merges are clean in both orders. No landing order is needed.

20. NOTE (all four). Local branch refs are behind the live heads: `solr-11479-submit` at e6fdc9ed5d5, `wt-solr-11479-submit` at 7e538e844c4, `solr-12651-submit` at 13e6789badd, `solr-15863-submit` at def5924a8d1. The checks used the origin/* refs, which match the claim table. Do not check out the local names without updating them.

21. NOTE (verification dates). SOLR-15035 is dated 2026-10-03 from the receipts ledger, which has no run date. SOLR-15863 is dated 2026-10-08 from the takeover log entry (the ledger says 2026-10-06). SOLR-12651 is dated 2026-10-04 from the ledger. Confirm these before opening.

## Task results

**SOLR-11479 (audit only, held).** The code commit at 7e538e844c4 does what the ticket asks for one replica: `property.coreNodeName` becomes the replica's coreNodeName when no coreNodeName is given (`AddReplicaCmd.java:360-363`), and several replicas with it are rejected (lines 141-148). The branch is not ready. It has no gate, its own note calls the test "guessed", and the note file ships in the diff (Finding 1). The changelog title overstates the change (Finding 2). No draft. Before any first gate: Findings 1, 2, and the test decision in Finding 4.

**SOLR-15035 (draftable, drafted at 12d7491e82c).** Verdict: ready to open once the base failure line is pasted in (Finding 7). The change is one line in `AddReplicaCmd.java:305` and one mapping line in `CoreAdminHandler.java:250`. The draft is `pr-drafts/solrcloud/SOLR-15035.md`. The main open point is the split case (Finding 6), now in Limits. The 15035 Jira text is not on disk (Finding 8).

**SOLR-12651 (draftable, held for a run at f3131d1ee84, drafted).** Verdict: hold. The draft is `pr-drafts/solrcloud/SOLR-12651.md`, written against the live tip. The proof covers the copy-failure leg only, at 90032e274b7. The property-upload leg is new since the gate and is not run (Finding 11). The changelog title and the async gap need fixing before opening (Findings 9 and 10). The round-28 MEDIUM is fixed at the tip (Finding 12). The retry question from the ticket is the draft's Choice.

**SOLR-15863 (draftable, PR-ready at the live tip, drafted at f381fd8d4dd).** Verdict: ready to open once the owner rules on omitted versions (Finding 15, decision 5) and the S3 and GCS note is accepted (Finding 16). The draft is `pr-drafts/solrcloud/SOLR-15863.md`. Its Choice (oldest segment version against a new field, with the ticket thread's question) and its Limits follow the receipt. The Proof says the base leg is inconclusive by construction, as the receipt records. The changelog wording needs a capital letter (Finding 17).

**Pairs.** AddReplicaCmd (11479 and 15035): shared file, separate hunks, clean merges both ways; 15035 lands first. Backup (12651 and 15863): no shared files, clean merges both ways; no required order. The assignment's claim about the shared test class is wrong (Finding 19).

## Owner decisions

1. SOLR-15035, shard split: ship with the Limits bullet as drafted (recommended), or change the count source before opening (the active count still differs after a split; exact parity needs a stored create-time count and a test). Optionally pose the split question as a Choice.
2. SOLR-12651, retry: keep delete-on-failure (the draft's Choice), or make cleanup optional so a retry can resume.
3. SOLR-12651, async: add the shard-failure check from Finding 10 with a test, or ship with the Limits bullet and the "unless" wording from Finding 9.
4. SOLR-12651: main side runs the class at f3131d1ee84 before opening (owed; it is not a local run).
5. SOLR-15863, shard with no version: keep the skip with the Limits bullet, fail the backup, or record an explicit unknown (round-28 P1).
6. SOLR-15863: run the S3 and GCS incremental tests, or accept that they were not run for this change.
7. SOLR-11479: add a test for the several-replica rejection before the first gate, or keep it as a Limit. The first gate itself is owed.
8. Landing order: SOLR-15035 before SOLR-11479 (both touch AddReplicaCmd). No order needed between 12651 and 15863.

## Not checked

- No build, compile, or test run. Compile-level points (Solr and Lucene method names such as `SegmentInfos.readCommit`, `getMinSegmentLuceneVersion`, `Version.parse`, the `copyIndexFileTo` and `openInput` overrides, the rethrow in `RestoreCmd`) were read by eye only.
- Gate logs are not on disk. The base failure lines for 15035 and 15863, and the gate dates, come from receipts only.
- No Jira packet for 15035. The 11479, 12651, and 15863 packets were read from `research/jira-context`.
- 11479: how the core treats `coreNodeName` and `property.coreNodeName` together in one CREATE request.
- 15035: `SplitShardCmd.java` was checked at one line (727). Other split paths were not read.
- 12651: the async path was read from `CollectionHandlingUtils.java` (lines 729-816) and not run. The value of `ZkStateReader.COLLECTION_PROPS_ZKNODE` was not read.
- 15863: whether the S3 and GCS module tests run in CI, and their results.
- CI check status for the four heads. No PR exists, so there are no checks.
- Local upstream/main (8e62c268688, dated 2026-10-09) was not re-fetched. Trial merges used local refs.
- Lucene version claims: no Lucene version number is named in any draft, so the 10.4.0 and 9.12.3 rule was not triggered.
- Nothing was committed, pushed, posted, or written to gh. Files written: this report and three drafts in `pr-drafts/solrcloud/`. One temporary file was written under /tmp (merge-tree output) and removed.
