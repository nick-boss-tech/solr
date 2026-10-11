# Core admin and replication-backup draft fidelity, slice s4

Assignment: `assignments/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Claim: `claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md`, slice B4 (this part is s4). Brief: `scratchpad/brief-draft-fidelity.md`. Worktree `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD `d627304e96b`.

Drafts: `pr-drafts/core-admin/SOLR-8576.md`, `pr-drafts/core-admin/SOLR-9750.md`, `pr-drafts/replication-backup/SOLR-11650.md`, `pr-drafts/replication-backup/SOLR-12085.md`, `pr-drafts/replication-backup/SOLR-12246.md`. Receipts: `receipts/SOLR-<n>.md` for all five (each exists).

Sources read: `pr-formula.md` (full); `material/core-admin-round-1-answers.md` (full); `material/replication-backup-round-1-answers.md` (full); `reports/replication-backup-round-1.md` (full); `reports/replication-backup-round-1-g2.md` (full); `reports/core-admin-round-1.md`, `-k4`, `-k6` and `reports/core-admin-answers-round-1.md`, `-c1`, `-c2` (every line naming the five tickets, plus the answers roll-up); `reports/replication-backup-round-1-g3.md` (line 97, the only reference to these tickets); `reports/replication-backup-round-1-g1.md` and `-g4.md` and core-admin `-k1` to `-k3`, `-k5` (grep: no reference to the five tickets).

## Head check

`git ls-remote origin refs/heads/solr-<n>-submit`, run 2026-10-11:

| Ticket | Live tip | Draft head | Match |
|---|---|---|---|
| SOLR-8576 | 4c46f95c7851f2adbf3377d746aadc9c2249cdb4 | 4c46f95c7851 | yes |
| SOLR-9750 | f97da6da14aafc1667fff59f279b404843598a00 | f97da6da14aa | yes |
| SOLR-11650 | e4f5e941cd8e88524196b03f2022261c2215c3ba | e4f5e941cd8 | yes |
| SOLR-12085 | c8dba502339631218190b3b08115f64047d2551c | c8dba502339 | yes |
| SOLR-12246 | 3ffc2539ec76cee5615f5bbfbcca784b5024ed9d | 3ffc2539ec7 | yes |

Base commits for pre-change links (`git merge-base <head> 8e62c2686882`, local upstream main): SOLR-8576 `b6b2b8f10e9827e3b86e44649fb7966ce646c185`; SOLR-9750, SOLR-11650, SOLR-12085, SOLR-12246 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. Every SHA named here resolves with `cat-file -t`.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-8576 | 4c46f95c7851 (live) | DRIFT (4 items) |
| SOLR-9750 | f97da6da14aa (live) | DRIFT (1 item) |
| SOLR-11650 | e4f5e941cd8 (live) | DRIFT (6 items) |
| SOLR-12085 | c8dba502339 (live) | DRIFT (3 items) |
| SOLR-12246 | 3ffc2539ec7 (live) | DRIFT (2 items) |

Pattern across the five: the heads, Proof counts, Choice sections and Limits all match their receipts and round reports. Most DRIFT is in citations. Four drafts cite pre-change code at the head SHA, where the head line now shows the fix (11650), or where the head line is shifted (12085, 12246), or where the text does not say the code is pre-change (9750). `pr-formula.md` requires pre-change links to use the merge-base and the text to say so. The replacements below do that.

## SOLR-8576

Verdict: DRIFT (4 items).

1. Draft says: "**One new test covers both name collisions, and it checks that the rejected creates change nothing.**" and "After that, it checks that the original collection and the alias are unchanged."
   - Evidence: `CollectionsAPISolrJTest.java` at 4c46f95c7851, lines 1186-1188. Lines 1186-1187 read the original collection by name. Line 1188 is `assertFalse(solrClient.getClusterState().hasCollection(aliasName));`, which checks that no collection has the alias's name. No line reads the alias. Round k6 FIX 1 and the material's 8576 entry say the same, and name the fix as owed. The receipt's "green" does not cover this assertion's meaning.
   - Replacement (summary line): "**One new test covers both name collisions, and it checks that the rejected creates leave the original collection in place.**"
   - Replacement (sentence): "After that, it checks that the original collection is still there under its name, and that no collection took the alias's name."
   - Note: once FIX 1 lands (the assertion reads the alias through `resolveSimpleAlias`), this sentence must say the alias still resolves to the original collection. The head, counts and links then move (material main-side item 5).

2. Draft says: "[OWED BEFORE POSTING: the new test's alias check needs a fix on the branch first. After that edit and a run of `CollectionsAPISolrJTest` at the new head, update the head, the counts, and the links in this draft.]" (Proof, line 24).
   - Evidence: internal process text in public text (brief check 7). It is a deliberate placeholder (c2 item 5; material "Draft corrections" 1; main-side item 5), and it must come out before any posting.
   - Replacement: none to paste. Delete line 24 and the blank line after it when FIX 1 has landed, the head has moved, and `CollectionsAPISolrJTest` has run at that head. Until then the draft is not posted.

3. Draft says: "`CollectionsAPISolrJTest` 25 tests, 1 skipped, 0 failures at head `4c46f95c7851` (verified 2026-10-07)."
   - Evidence: `receipts/SOLR-8576.md` line 9: "Recorded in the main side's takeover log (round 28, 2026-10-07)." That is a record date. The receipt gives no run date. Same class as the s1 items for SOLR-12007 and SOLR-13246. The counts themselves match line 6 of the receipt.
   - Replacement: "`CollectionsAPISolrJTest` 25 tests, 1 skipped, 0 failures at head `4c46f95c7851` (recorded 2026-10-07)."

4. Draft says: "The create path rejects a name taken by a collection with "collection already exists: <name>", and a name taken by an alias with "collection alias already exists: <name>" ([CreateCollectionCmd.java L127-L135](https://github.com/nick-boss-tech/solr/blob/4c46f95c7851f2adbf3377d746aadc9c2249cdb4/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L127-L135))."
   - Evidence: this is pre-change behavior. The branch changes nothing under `solr/core/src/java` (`git diff` from `b6b2b8f10e98` to the head is empty for that path). The two throws are at base `b6b2b8f10e98` lines 127-135, the same text. The link must use the base commit and the text must say so.
   - Replacement: "The create path, which this branch does not change, rejects a name taken by a collection with "collection already exists: <name>", and a name taken by an alias with "collection alias already exists: <name>" ([CreateCollectionCmd.java L127-L135](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L127-L135))."

Checked without drift: head; the new test at head L1157-L1189 (one new test, `testCreateCollectionNameAlreadyTaken`); the collection-exists assertion at L1173 and the alias assertion text at L1183; no base test checks the two create messages (only a RENAME assertion uses "exists"); the changelog file exists at the head and its title ("Add test coverage for CREATE of a collection whose name is already taken by a collection or an alias.") matches the summary; the "passes on base" statement matches the receipt (premise run, 0 failures); the Limits match round k6 NOTE 2 and the material (name collisions only; error-text match); no Choice section, which matches the material and k6 (none owed); no em or en dashes; no other internal vocabulary.

Optional notes, not blocking:
- Receipt says "PR-ready" at 4c46f95c7851. Round k6 and the material hold the draft until FIX 1 lands and the run is done. The receipt and the held state disagree; the owner should settle which governs before posting.
- Commit subjects on the branch name handoff docs ("add hypothetical-reproduction handoff doc", "remove the reproduction handoff doc"; k6 NOTE 1). The draft text does not carry them. Material adopted no history rewrite; squash on merge is the maintainer's choice.
- The Limits bullets repeat the Limits opener word for word (c1 item 2 left this to the owner).

## SOLR-9750

Verdict: DRIFT (1 item).

1. Draft says: "Each implicit handler names its paramset after its endpoint."
   - Evidence: the links in this paragraph (ImplicitPlugins.json L70, L86, L97 and reference guide L471) point at the merge-base `14c7aac0d151`, which is correct for pre-change code. Base L70 is `_ADMIN_SEGMENTS`, L86 `_EXPORT`, L97 `_ADMIN_GRAPH`, and guide L471 `_ADMIN_GRAPH`. The paragraph does not say it describes the code before this change, which `pr-formula.md` requires for merge-base links (the same point as the s1 items for SOLR-13246 and SOLR-15024).
   - Replacement: "Before this change, each implicit handler names its paramset after its endpoint."

Checked without drift: head `f97da6da14aa`; `ImplicitPlugins.json` L97 at head is `"useParams":"_GRAPH"` and the guide L471 at head is `Paramset: `_GRAPH``, both as the draft's "What this change does" says; no `_ADMIN_GRAPH` is left at head (grep), so the change keeps no alias; the test `testGraphHandlerParamsetName` (TestImplicitPlugins.java L46-L58) reads the resource and sends no request, as the Limits say; Proof counts match the receipt (TestImplicitPlugins 2, TestSolrConfigHandler 8, TestReqParamsAPI 1, all 11 pass; base failure "expected _GRAPH but was _ADMIN_GRAPH"); "verified 2026-10-05" is the reconciliation gate date in receipt line 8, so it is supported; the Choice (plain rename against a one-release `_ADMIN_GRAPH` fallback) is a live alternative and matches the material's adopted decision; the Limits match k4 finding 7; the changelog title "The paramset of the implicit /graph request handler is now named _GRAPH instead of _ADMIN_GRAPH, matching the naming of the other implicit paramsets." matches the summary.

Optional notes, not blocking:
- The head changelog (`changelog/unreleased/SOLR-9750-graph-paramset-name.yml`) has no upgrade step. The draft's "What this change does" states it. The material records the fragment fix as owed (k4 finding 5); the fragment should catch up before posting.
- The Limits bullets partly repeat the opener ("No other implicit paramset is renamed").
- "paramset" and "implicit handler" are not glossed for general reviewers.

## SOLR-11650

Verdict: DRIFT (6 items).

1. Draft says: "The replication details response returns that value as configured ([ReplicationHandler.java](https://github.com/nick-boss-tech/solr/blob/e4f5e941cd8e88524196b03f2022261c2215c3ba/solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java#L1041))."
   - Evidence: this is the pre-change symptom. At head, L1041 is the changed line `follower.add(LEADER_URL, URLUtil.redactUserInfo(fetcher.getLeaderCoreUrl()));`, so the link shows the fix. The symptom is base L1040 at `14c7aac0d151`: `follower.add(LEADER_URL, fetcher.getLeaderCoreUrl());` (the branch adds one import, which moves the line to 1041 at head).
   - Replacement: "Before this change, the replication details response returns that value as configured ([ReplicationHandler.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java#L1040))."

2. Draft says: "`IndexFetcher` also prints the same URL in an "Updated leaderUrl" log line and in the "Malformed 'leaderUrl'" and "is not allowed" errors ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/e4f5e941cd8e88524196b03f2022261c2215c3ba/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L306-L336))."
   - Evidence: head L306-L336 is the redaction code this branch adds (head L308 `URLUtil.redactUserInfo`, head L331 `redactedLeaderUrl`). The symptom lines at base `14c7aac0d151`: the malformed error is L306-L308, the not-allowed error is L309-L319, and the "Updated leaderUrl" log is L470 (`log.info("Updated leaderUrl to {}", leaderCoreUrl);`).
   - Replacement: "Before this change, `IndexFetcher` also printed the same URL in an "Updated leaderUrl" log line ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L470)) and in the "Malformed 'leaderUrl'" and "is not allowed" errors ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L306-L319))."

3. Draft says: "The "Leader at ... is not available" warnings print the leader URL as configured ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/e4f5e941cd8e88524196b03f2022261c2215c3ba/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L501-L510))."
   - Evidence: the branch does not change these two warnings. Head L501-L510 is the same code as base L483-L492. Unchanged code cited as current behavior links the base commit, and the text says so.
   - Replacement: "The "Leader at ... is not available" warnings print the leader URL as configured, and this branch does not change them ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L483-L492))."

4. Draft says: "A unit test checks that case ([test](https://github.com/nick-boss-tech/solr/blob/e4f5e941cd8e88524196b03f2022261c2215c3ba/solr/solrj/src/test/org/apache/solr/common/util/URLUtilTest.java#L143-L160))." (the case is a password with a raw `/` or `?`).
   - Evidence: the pattern at head `URLUtil.java` L36-L37 does stop at `?`, so the behavior is right. But `testRedactUserInfoUnusualUserInfo` (L143-L160) asserts only the raw `/` case (L157-L159). No assertion covers a raw `?`.
   - Replacement: "A unit test checks the raw slash case ([test](https://github.com/nick-boss-tech/solr/blob/e4f5e941cd8e88524196b03f2022261c2215c3ba/solr/solrj/src/test/org/apache/solr/common/util/URLUtilTest.java#L157-L159))."

5. Draft says: "`URLUtilTest` 18 of 18 passes. Three of those tests call the new helper, so they do not apply to the base code."
   - Evidence: receipt line 6 gives 18 of 18 and no split, so the count "three" is not in the receipt. The code supports it (three added methods: `testRedactUserInfo`, `testRedactUserInfoFailsClosedOnUnparseableUrls`, `testRedactUserInfoUnusualUserInfo`, all calling the helper), and round g2 says the same. Under the brief, a Proof count the receipt does not give is DRIFT.
   - Replacement: "`URLUtilTest` 18 of 18 passes. Its new redaction tests call the new helper, so they do not apply to the base code."

6. Draft summary: "**The details response and three messages show `********` in place of the password.**" (the draft has no title line; the changelog link is the title check).
   - Evidence: the cited changelog at head, `changelog/unreleased/SOLR-11650-redact-replication-leader-url.yml` line 1, reads "Replication no longer exposes the password from a leaderUrl that contains user-info in the follower details response or in IndexFetcher log and error messages". That overstates the change: the two "Leader at" warnings and the client error still show the URL (items 3 and the Limits). The draft's summary and Limits are the correct, narrower text. Round g2 F3 and the material (ADOPTED) say to narrow the title. This is a branch-side fix; the draft text stays.
   - Replacement (changelog `title:` line, branch side): "title: Replication no longer shows the password from a leaderUrl with user-info in the follower details response or in three leaderUrl messages"

Checked without drift: head `e4f5e941cd8` (live); the URLUtil helper at head L121-L136 (new, produced by the change); the "What this change does" link to `IndexFetcher.java` L486-L489 at head (the redacted "Updated leaderUrl" block); the redaction test at head L60-L92 (two tests); the details test at head L188-L221 (`testFollowerDetailsRedactLeaderUrlPassword`); Proof counts match the receipt (IndexFetcherLeaderUrlRedactionTest 2 of 2 with the password visible on base in both messages; TestUserManagedReplicationWithAuth 3 of 3; "verified 2026-10-07" is the gate-finished date in the receipt); the base run for the details case is now in the receipt (vm2, 2026-10-11) and the draft does not claim that case fails on base, so no change; the cross-area statements hold (the diff adds one public static method to URLUtil and no existing method changes; a grep of `solr/solrj/src/java` finds no read of a follower `leaderUrl` from the details response); the Limits match round g2 F4 (client error text), g2 decisions 2 and 3 (scope and raw/no-scheme values), and the follow-up offer; no Choice section, which matches the material; the changelog file exists at head.

Optional notes, not blocking:
- Moved head: the material adopts a comment and changelog commit (URLUtil javadoc at L109-L114 says `#` ends the user-info, but the pattern does not stop at `#`, g2 F5; the test comment at L154-L156, g2 F6; the title in item 6). When those commits land, re-point the draft's head, links and Proof, and add the moved-head sentence the material gives.
- The receipt names `e044bf20b405` as the merge-base for its base run. The merge-base with upstream main is `14c7aac0d151`, and the three production files are identical at both commits (`git diff` empty), so the links above are correct either way.
- The Ticket claim "on the Replication tab of the admin UI" is not checked (no Jira packet on disk; see Not done).
- The draft runs over the 3,500-character guide (about 4,500, citations included); the material accepted this.
- "user-info", "basic-auth" and "leaderUrl" are not glossed.

## SOLR-12085

Verdict: DRIFT (3 items).

1. Draft says (the body paragraph under the bold summary): "With `SolrDeletionPolicy` set to keep more than one commit (`maxCommitsToKeep` above 1), the older commits stay on disk. ... The fetch then calls `deleteUnusedFiles` and sleeps one second per round, up to 30 rounds ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/c8dba502339631218190b3b08115f64047d2551c/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L640-L649)). ... ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/c8dba502339631218190b3b08115f64047d2551c/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L622))."
   - Evidence: this paragraph is the pre-change symptom, but both links point at the head. At head, L640-L649 and L622 show the same loop and `if` as base L639-L648 and L621 (the branch adds one import at line 80, which moves everything below it by one line, and the loop itself is unchanged). The links must go to the merge-base `14c7aac0d151`, and the text must say so.
   - Replacement (the whole body paragraph): "Before this change, with `SolrDeletionPolicy` set to keep more than one commit (`maxCommitsToKeep` above 1), the older commits stay on disk. `IndexFetcher.hasUnusedFiles` built its used set only from the files of the commit it is given, so the files of each older kept commit counted as unused. The fetch then called `deleteUnusedFiles` and slept one second per round, up to 30 rounds ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L639-L648)). Those files are kept, not deleted, so the loop runs to its limit. The loop runs only when the fetch is not already a full copy and `fetchFromLeader` is off ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L621))."

2. Draft says (Proof): "`IndexFetcherPacketProtocolTest` runs 18 tests with no failures at the same head. `TestReplicationHandler` is marked `@Nightly`, and that run skipped its 25 tests. No replication test ran at this head."
   - Evidence: the same paragraph says `IndexFetcherPacketProtocolTest` ran 18 tests with no failures, and that is a replication test (receipt line 6: 18 run). The receipt supports only "TestReplicationHandler was skipped". The sentence contradicts the draft's own count.
   - Replacement: "No `TestReplicationHandler` test ran at this head."

3. Draft summary: "**A follower that keeps older commits waits 30 seconds on each replication, then copies the whole index.**" The changelog at head (`changelog/unreleased/SOLR-12085-index-fetcher-retained-commits.yml`) says "...made replicas wait 30 seconds and then fall back to a full index copy on every replication."
   - Evidence: the wait runs only on the path at head L622 (`!isFullCopyNeeded && !fetchFromLeader`), so "every replication" overstates it. Round g2 F14 and the material (ADOPTED) say to narrow it. The draft's "each replication" and its path sentence are the correct text. This is a changelog-only branch fix that moves the head; the draft text stays.
   - Replacement (changelog title, branch side): "IndexFetcher no longer treats the files of commits kept by the deletion policy (maxCommitsToKeep > 1) as unused, which made a replica wait 30 seconds and then fall back to a full index copy on each replication that reaches the check."

Checked without drift: head `c8dba502339` (live); the used-set change at head L868-L878 (javadoc, static method, `DirectoryReader.listCommits` loop; produced by the change); the test at head L36-L63 (`testRetainedCommitFilesAreNotUnused`; keeps two commits, checks a retained file is not unused and an orphan file is reported); the receipt counts (IndexFetcherUnusedFilesTest 1 of 1, IndexFetcherPacketProtocolTest 18, TestReplicationHandler 25 skipped, `@Nightly`); "verified 2026-10-05" is the gate-finished date in the receipt; the base failure "Found unused file: segments_1" matches the receipt; the visibility change (private to static, package-private) matches the diff; no Choice section, which matches the material (no choice owed; g2 F15); the Limits match the material and round g2 (no end-to-end run, nightly suite skipped).

Optional notes, not blocking:
- Moved head: the changelog commit (item 3) moves the head. The material adopts it without a new run. Re-point the draft's head and links when it lands.
- Lucene calls in the change (`DirectoryReader.listCommits`, `IndexCommit.getFileNames`) were not checked against the main or branch_9x Lucene version (g2 notes). The draft names no Lucene version.
- "deletion policy", "full copy" and "maxCommitsToKeep" are not glossed.

## SOLR-12246

Verdict: DRIFT (2 items).

1. Draft says: "A file whose checksum differs is logged at WARN as "File _0_1.liv did not match" ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/3ffc2539ec76cee5615f5bbfbcca784b5024ed9d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1264-L1274)). The follower then fetches the file again. On the same method, a file with no checksum to compare and a different length is already logged at INFO ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/3ffc2539ec76cee5615f5bbfbcca784b5024ed9d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1249-L1252))."
   - Evidence: at head, L1264-L1274 shows the new INFO line (head L1266), not the WARN. The symptom is at base `14c7aac0d151`: L1264-L1274 holds the `log.warn(` at L1265, and L1249-L1252 is the length-only INFO branch. The text must say the links are pre-change.
   - Replacement: "Before this change, a file whose checksum differs is logged at WARN as "File _0_1.liv did not match" ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1264-L1274)). The follower then fetches the file again. On the same method, a file with no checksum to compare and a different length was already logged at INFO ([IndexFetcher.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1249-L1252))."

2. Draft says (Proof): "`TestReplicationHandler` is marked `@Nightly`, and that run skipped its 25 tests, so no replication test ran at this head."
   - Evidence: the same paragraph reports `IndexFetcherPacketProtocolTest` 18 tests passing at the same head (receipt line 6: 44 tests run, with 18 and 1 run and 25 skipped). The receipt supports only "TestReplicationHandler was skipped".
   - Replacement: "`TestReplicationHandler` is marked `@Nightly`, and that run skipped its 25 tests, so no `TestReplicationHandler` test ran at this head."

Checked without drift: head `3ffc2539ec7` (live); the one production change at head L1265-L1266 (comment and `log.info(`), produced by the change; the test at head L29-L48 (`testChecksumMismatchIsNotLoggedAsWarning`: no WARN, the mismatch logged at INFO); the exact base message "File _0_1.liv did not match. expected checksum is 12345 and actual is checksum 3589274946. expected length is 33 and actual length is 33" matches receipt line 7; the receipt counts (IndexFetcherCompareFileTest 1 of 1; IndexFetcherPacketProtocolTest 18); "verified 2026-10-05" is the gate-finished date in the receipt; the Choice (lower the whole message, or keep WARN when lengths also differ) is a live alternative with a stated cost, and matches the material's ADOPTED answer (the draft's Choice stands, the branch is not changed); the Limits match the material and g2 F17 (equal length only; the `.liv` cause stays open with the follow-up offer; no end-to-end run); the changelog at head exists and its title matches the summary.

Optional notes, not blocking:
- The changelog title says "A segment file", and the file is a `.liv` (live docs). "Segment file" is loose; `.liv` is clearer.
- ".liv" is not glossed for general reviewers.

## Not done

- No build, Gradle, test, gate, or `test-queue` command. Proof counts were checked against `receipts/SOLR-<n>.md`, not re-run. Gate logs named in the receipts (for example `g8576-gate.log`, `g11650-proof.log`) are not on disk, so base failure text was checked only against the text the receipts quote.
- Jira ticket text was not checked. No Jira packet for these five is in `research/jira-context/`, and this session has no Jira tool. The ticket claims in the drafts (for example "on the Replication tab of the admin UI" in SOLR-11650) are unverified.
- Fork branches were read with `git ls-remote` only; nothing was fetched. Cited SHAs resolve in the worktree.
- Core admin parts k1 to k3 and k5, and replication parts g1 and g4, were checked by grep for the five ticket numbers; they name none of them.
- The receipt for SOLR-11650 names base `e044bf20b405`; the upstream merge-base is `14c7aac0d151`. The three production files are identical at both, so the links are unaffected. Noted for the receipt owner.
- Lucene API use in SOLR-12085 was not checked against a Lucene version (the draft names none).
- No GitHub call, commit, push, comment, or Jira write. Nothing was edited except this report.
