# Replication and backup round 1, part g3 (SOLR-5589, 9382, 8430, 9598)

Result: 8430 is held for one code fix and one title fix (draft written); 9598 is held for one owner decision on the RESTORE default (draft written); 5589 and 9382 are audit only, with premises checked and fixes owed before any gate; nothing was committed, built, tested, or posted.

Drafts: `pr-drafts/replication-backup/SOLR-8430.md` (head 49af21be58927a62b50bd7ba1df1688110fba63e) and `pr-drafts/replication-backup/SOLR-9598.md` (head c8407773f76ca49711b5777ff9f69736d87b1e90). Each file opens with an owner note, to be deleted before posting.

## Heads and base

- 5589 `origin/solr-5589-submit` c707aa95e2ae, 9382 c0b5fec1be21, 8430 49af21be5892, 9598 c8407773f76c. All four match the claim table. 8430 and 9598 match their receipts.
- Base for review is `upstream/main` 8e62c2686882 (apache/solr). The fork's `origin/main` (498a09aea97) is behind it, and diffs against it show about 770 files. Merge bases with upstream/main: cabedd1d968 for 5589, 9382 and 8430 (37 commits behind), 22a8cfebbbd for 9598 (42 behind).
- All four trial-merge cleanly onto upstream/main with `git merge-tree --write-tree` (no ref written).

## Findings

1. **FIX (8430).** `solr/core/src/java/org/apache/solr/handler/admin/api/ReplicationAPIBase.java` lines 99-110 at 49af21be589 (`SHARED_RATE_LIMITERS`, `rateLimiterFor`). Evidence: the rate is client input. `solr/api/src/java/org/apache/solr/client/api/endpoint/ReplicationApis.java` lines 87-91 declare `@QueryParam("maxWriteMBPerSec") double maxWriteMBPerSec`. The legacy path reads it at `ReplicationHandler.java` line 370. The endpoint needs `CORE_READ_PERM` (`CoreReplication.java` lines 58-59). Each distinct value adds a static map entry that is never removed, so a client can grow the map without limit. Replacement for lines 99-110:
   ```java
     /** Caps the shared limiters. The rate comes from the request, so the map must stay small. */
     private static final int MAX_SHARED_RATE_LIMITERS = 64;
     private static final ConcurrentMap<Double, RateLimiter> SHARED_RATE_LIMITERS =
         new ConcurrentHashMap<>();

     /**
      * Returns the limiter that throttles file streams to {@code maxWriteMBPerSec}. Streams with the
      * same rate share one limiter. A rate of 0 means no throttle. Past the cap, a stream gets its
      * own limiter.
      */
     static RateLimiter rateLimiterFor(double maxWriteMBPerSec) {
       final double mbPerSec = maxWriteMBPerSec == 0 ? Double.MAX_VALUE : maxWriteMBPerSec;
       RateLimiter shared = SHARED_RATE_LIMITERS.get(mbPerSec);
       if (shared != null) {
         return shared;
       }
       if (SHARED_RATE_LIMITERS.size() >= MAX_SHARED_RATE_LIMITERS) {
         return new RateLimiter.SimpleRateLimiter(mbPerSec);
       }
       return SHARED_RATE_LIMITERS.computeIfAbsent(mbPerSec, RateLimiter.SimpleRateLimiter::new);
     }
   ```
   The size check and the insert are not atomic, so the map can exceed the cap by a few entries under a race. That is harmless. The draft's Limits bullet about memory must be replaced once this lands, and the head moves.

2. **FIX (8430).** `changelog/unreleased/SOLR-8430-shared-replication-throttle.yml` line 1. Evidence: the title says the throttle is shared "by all concurrent file requests on a node". The code shares only among equal rate values (`ReplicationAPIBase.java` lines 107-109 at head). Replacement line 1:
   `title: "Replication maxWriteMBPerSec throttling is shared by concurrent file requests on a node that use the same rate, instead of each request getting its own limiter"`

3. **NOTE (8430).** Grain of the limit. The ticket (`research/jira-context/SOLR-8430.json`) asks for a limit "per-node instead of only per-leader". The branch keeps one limiter per rate value, so streams at different rates are not capped together. The draft's choice section names this as the decision for the owner. No code change is owed by this finding alone.

4. **NOTE (8430).** The shipped test (`ReplicationRateLimiterTest.java`, 3 methods) calls `rateLimiterFor`, which the base code lacks. On base it fails to compile. That is the new-API case, inconclusive by construction, and the draft says so. The probe in the receipt (one test, one failure on base, passes at head) is not in the branch, so the draft says it is not part of this change. The probe result comes from the receipt only.

5. **NOTE (9598).** Changelog line 2 `pass waitForFinalState=false to restore the old behaviour.` The changelog uses both spellings (7 files with "behavior", 4 with "behaviour"). Optional replacement: "old behavior".

6. **FIX (9598).** `solr/core/src/java/org/apache/solr/cloud/api/collections/RestoreCmd.java` lines 315-319 at c8407773f76 (Javadoc of `waitForReplicasToBeActive`). Evidence: lines 317-318 say the wait "Follows the direction of SOLR-17712 (always wait for final state); {@code waitForFinalState=false} on the request opts out". The direction of SOLR-17712 is the opposite. Upstream `solr/api/src/java/org/apache/solr/client/api/model/CreateCollectionRequestBody.java` lines 42-47 deprecate the same option: "Solr is moving toward always waiting for final state, with no option to opt out". The branch adds a new, undeprecated opt-out at `RestoreCollectionRequestBody.java` lines 41-45. Replacement for lines 315-319:
   ```java
       /**
        * SOLR-9598: wait until the restored collection is usable. All replicas were requested first,
        * so the recoveries run concurrently; only then do we wait for the whole collection to be
        * active. The default is to wait; {@code waitForFinalState=false} on the request opts out.
        */
   ```
   This is a comment-only change, but it moves the head. The focused test must be re-run at the new head, or the proof must say that only a comment changed.

7. **NOTE (9598).** Default and direction. `RestoreCmd.java` line 321 defaults `waitForFinalState` to true. `CreateCollectionCmd.java` line 114, `AddReplicaCmd.java` line 116 and `MoveReplicaCmd.java` line 75 (upstream) default to false. The SOLR-9598 Jira comment of 2018-06-25 (Varun Thacker) points at the existing waitFor flag as the fix. The draft's choice section sets out the three options. Owner decision 2.

8. **NOTE (9598 and SOLR-12651).** Both branches change `RestoreCmd.java`. Trial merge `origin/solr-9598-submit` with `origin/solr-12651-submit` is clean (no ref written). In the merged file, 12651's cleanup block (catch at line 305) ends before `addReplicasToShards` (line 321). The 9598 wait is at line 326, and `restoringAlias` is at 327. So the wait sits after the cleanup block, which matches 12651's rule that a failure once the shards are active does not delete the collection. 9598 does not edit `TestLocalFSCloudBackupRestore.java`. It edits the parent `solr/test-framework/.../AbstractCloudBackupRestoreTestCase.java`, which 12651 does not touch. Landing order from 9598's side: 9598 first. It is gated at its live tip. Its change is additive. 12651 is gated at an older head (`receipts/SOLR-12651.md`: gated 90032e274b7, live tip f3131d1ee84), so it must be re-gated and can rebase over one inserted line. Owner decision 3.

9. **NOTE (9598 and SOLR-15863). The assignment is wrong here.** It says 15863 shares `TestLocalFSCloudBackupRestore` with 9598. At 15863's head f381fd8d4dd, the branch changes `BackupCmd.java`, `BackupProperties.java`, `IncrementalShardBackup.java`, `BackupCmdTest.java`, `BackupCoreAPITest.java` and `AbstractIncrementalBackupTest.java`. It does not change `TestLocalFSCloudBackupRestore.java` or `AbstractCloudBackupRestoreTestCase.java`. There is no file overlap with 9598. The lead should correct the assignment wording.

10. **FIX (5589).** `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java` line 1333 at c707aa95e2a. The new block disables replication when a follower section is present, even if no leader section is. Evidence: the ticket thread (`research/jira-context/SOLR-5589.json`, Vitaliy Zhovtyuk, 2014-01-29) argues that a disabled slave should not stop a master from serving another instance: "it's only make sense to disable replication when both master and slave explicitly disabled". The test (`TestDisabledReplicationHandler.java` lines 35-56) has no follower-only case, so this change is untested. A node whose only section is a disabled follower would stop serving replication, which the thread argued against. Replacement for line 1333:
    `      if (leader != null) {`
    (in place of `      if (leader != null || follower != null) {`). Read against the code, this gives: leader-only disabled, disabled (test passes); both disabled, disabled (test passes); follower-only disabled, default leader stays enabled, as before; not configured, enabled. The other option, disable only when both sections are present and disabled, would fail `testDisabledLeaderStaysDisabled` and needs a test change. Owner decision 4.

11. **FIX (5589).** `changelog/unreleased/SOLR-5589-disabled-replication-config.yml` line 2. The title says "leader and follower sections that are all enable=false". That does not match the code under either option. With Finding 10 applied, replacement:
    ```yaml
    title: >
      ReplicationHandler configured with a leader section that has enable=false now starts with replication disabled instead of falling back to the default leader.
    ```

12. **FIX (5589 and 9382).** Stray process files on the branches. `SOLR-5589-TESTING.md` is added at tip c707aa95e2a. `SOLR-9382-TESTING.md` is added at tip c0b5fec1be2. Both say that nothing was compiled or run. Delete both in a branch commit before any gate or PR. The 8430 branch removed its note at its tip (49af21be589). This is a file deletion, so there is no replacement text.

13. **NOTE (5589).** Commit listeners. In `upstream/main` `ReplicationHandler.java`, the `postCommit` of `getEventListener` (line 1544) has no `replicationEnabled` check, and the `if (snapshoot)` branch runs regardless. So a disabled leader with `backupAfter` set still takes commit-time snapshots. This matches the `disablereplication` command today, so the code comment "equivalent to disablereplication" holds. The PR should say so. Limits sentence for the draft: "A disabled leader still takes commit-time snapshots when backupAfter is set, as the disablereplication command does."

14. **NOTE (9382). Premise and scope.** The Jira packet (`research/jira-context/SOLR-9382.json`) has the title "Replication of managed resources on standalone servers". The reporter's main problem is that a standalone follower does not see managed resource changes until a reload. The wildcard idea comes from the second comment (2016-08-04). The branch implements only the wildcard. The premise that a wildcard name is silently dropped holds on `upstream/main`: `ReplicationHandler.java` line 799 (`if (!Files.exists(f) || Files.isDirectory(f)) continue;`) skips it with no message. The PR must say the change answers the wildcard suggestion and does not change reload behavior.

15. **FIX (9382).** `changelog/unreleased/SOLR-9382-replication-conffiles-glob.yml` lines 1-3. The example path `managed-resources/_schema_analysis_*.json` is not where default standalone storage writes. `upstream/main` `ManagedResourceStorage.java` lines 129-141 use the core's config directory as the storage directory when `storageDir` is not set. Lines 444-445 name stored files `resourceId.replace('/', '_') + ".json"`. So the managed synonyms and stopwords files sit in the config directory with names like `_schema_analysis_*.json`. Replacement for the title block:
    ```yaml
    title: >
      Replication handler confFiles on the leader accepts * and ? in the file name part (for example _schema_analysis_*.json, the managed synonyms and stopwords files in the config directory), so managed synonyms, stopwords and their language variants replicate without listing each file.
    ```
    The TESTING.md guess "managed resources live under `<conf>/managed-resources/`" is wrong for the same reason. That file is removed under Finding 12.

16. **NOTE (9382).** Line length. `ReplicationHandler.java` line 823 is 105 characters. `TestReplicationConfFileGlob.java` line 26 is 102 characters. Spotless would reflow both, and the gate's apply step would change them. Replacement for line 823:
    ```java
            log.warn(
                "Ignoring replication confFiles pattern '{}': not a directory in the config dir", name);
    ```
    Replacement for test line 26:
    `/** Wildcards in the leader's {@code confFiles}, such as managed resource variants. */`

17. **NOTE (ReplicationHandler cluster).** Trial merges are clean for 5589 with 9382, 11650 and 6711, and for 9382 with 11650 and 6711 (no ref written). Hunks: 5589 at base lines 1332-1336 (fallback block). 9382 at base 42-46 (imports), 771-773 (loop) and 802-803 (method insert). 11650 at line 90 (import of `URLUtil`) and in the follower details around line 1041 (`LEADER_URL`). 6711 at base 1311-1316 (follower block, adds the persisted `pollDisabled` load) and in `disablePoll`. None of these touches `replicationEnabled`, the enable flags, or the fallback block. Upstream SOLR-18124 (6d6504668e3) changes `doFetch` and imports only and does not overlap any of these hunks.

18. **NOTE (8430). API surface.** The assignment says 8430 changes the replication admin API. The diff changes no endpoint, parameter or response. The new helper is package-private. The draft says the request parameter and the response are unchanged.

## Task results

**SOLR-5589: audit only, no draft. Verdict: held, fixes owed before any gate.** The premise holds on `upstream/main`: `ReplicationHandler.java` lines 1354-1356 fall back to a default leader when no section is enabled. The branch change does what its text says for leader-only and both-disabled configs. The test's XPath matches the details output: the leader block is added only when `isLeader` (line 1041), and the details are returned at line 1167. The TESTING.md guess that existing test families use both flags disabled is resolved: no test config, server configset or reference guide page sets `enable` on a leader or follower section on `upstream/main`, so the existing families are not affected. The follower-only case (Finding 10), the changelog title (Finding 11), the stray TESTING.md (Finding 12) and the commit-snapshot note (Finding 13) are open.

**SOLR-9382: audit only, no draft. Verdict: held, NO GATE stands.** The premise that a wildcard in `confFiles` is silently dropped holds on `upstream/main` (line 799). The branch adds a glob expansion with a guard that keeps the directory inside the config directory, and the leader expands at `filelist` time, so the follower needs no change. The ticket's main report is wider than the glob (Finding 14). Fixes owed: the changelog example path (Finding 15), the stray TESTING.md (Finding 12), and the two long lines (Finding 16). The branch lookup of the implicit `/replication` handler matches an existing pattern at `TestReplicationHandler.java` line 1539.

**SOLR-8430: draftable after two fixes. Verdict: HOLD for Finding 1 (cap) and Finding 2 (title); then post after the owner picks the grain.** The draft is at `pr-drafts/replication-backup/SOLR-8430.md`, written against 49af21be589. The receipt matches the head, and the test counts come from the receipt. Thread safety of the shared limiter was checked in the Lucene 10.4.0 class file (javap of `lucene-core-10.4.0.jar` in the local Gradle cache): `SimpleRateLimiter.pause` holds a monitor around its `lastNS` bookkeeping and sleeps outside it, so sharing one instance across streams is safe. The API surface is unchanged (Finding 18).

**SOLR-9598: draftable after one owner decision and one comment fix. Verdict: HOLD until the RESTORE default is chosen.** The draft is at `pr-drafts/replication-backup/SOLR-9598.md`, written against c8407773f76. The receipt matches the head. The proof is timing-dependent (one failure in five base runs), and the draft says so and claims no deterministic base failure. The Javadoc claim about SOLR-17712 is backwards (Finding 6). The overlap with SOLR-12651 is textual in `RestoreCmd.java` only, merges cleanly, and 9598 should land first (Finding 8). The SOLR-15863 overlap in the assignment does not exist (Finding 9). The draft runs about 4,000 characters, above the 3,500 guide, because the choice and timing paragraphs need the room.

## Owner decisions

1. SOLR-8430: accept the cap (Finding 1) and the retitle (Finding 2). Then choose one limiter per rate value (this branch) or one limiter for the whole node (draft choice section).
2. SOLR-9598: choose the RESTORE default. (a) Wait by default with an opt-out (this branch). (b) Always wait, no opt-out (SOLR-17712 direction). (c) No default wait, as CREATE does. The draft waits on this choice.
3. Landing order: 9598 before SOLR-12651 (Finding 8). SOLR-12651 is SolrCloud round 1, so its owner must agree.
4. SOLR-5589: which disable rule ships. Recommended: `if (leader != null)` (Finding 10). The alternative needs a test change.
5. SOLR-9382: whether to draft at all. If yes, the PR text answers the wildcard suggestion only, as Finding 14 says.
6. Branch edits (Findings 1, 2, 6, 10 to 12, 15, 16) are for the owner or the main side. Reviewers do not edit branches.

## Not checked

- No builds, Gradle, tests, or `gh` calls. Gate counts and the 9598 timing claim come from the receipts. The gate logs are not on disk.
- Spotless, tidy, Error Prone and the changelog YAML parse were not run. Line lengths were checked with awk.
- Jira text came from the `research/jira-context` packets, not from live JIRA.
- The v2 mapping of an absent `waitForFinalState` to the default true was not traced past the code. The receipt says `RestoreCollectionAPITest` 7 of 7.
- SOLR-12651 and SOLR-15863 were checked for file overlap and merges only. Their content was not audited.
- The Lucene thread-safety reading is from bytecode, not source.
- Windows separators in `confFiles` names, and symlinks under the config directory, were not checked. Plain names already follow links.
- branch_9x (Lucene 9.12.3) was not checked. The drafts target main and name no Lucene version.
- Live PR state was not checked (round part g4).
- The 8430 probe was not re-run. Its result is from the receipt only.
