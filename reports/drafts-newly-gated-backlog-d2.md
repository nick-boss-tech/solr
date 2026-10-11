# Newly gated backlog, drafts part d2 (SOLR-9091, SOLR-9382, SOLR-10364)

Result: three drafts written, text only. Nothing committed, pushed, posted, built or tested. SOLR-9091 and SOLR-9382 are at their receipt heads and match the live fork tips. SOLR-10364 is drafted at the receipt's packaged head `efb1e6717f9ffda64b52c0820efb52ba58832b50`, which is not on the fork and not in the local object store, so its links will not resolve until that commit is pushed. Hold SOLR-10364 for that push or a re-point.

Drafts:
- `pr-drafts/replication-backup/SOLR-9091.md`
- `pr-drafts/replication-backup/SOLR-9382.md`
- `pr-drafts/solrj/SOLR-10364.md`

Read-only git fetch: `git fetch origin solr-9091-submit solr-9382-submit solr-10364-submit` updated the worktree's remote-tracking refs (9091 `e31bdaa4d27..e6bfa5c6268`, 9382 `c0b5fec1be2..2952bae9d39`). No other write.

## Receipt numbers used

SOLR-9091 (`receipts/SOLR-9091.md`):
- Line 5: gated head `e6bfa5c626812f1452ed8c0158697ab8653a40d2`; proof base (merge-base) `cabedd1d968059215188f4e7563fb303241899ed`.
- Line 7: proof leg on base `TestRestoreCore tests=4 failures=1 errors=0`; failure `testRestoreRejectsCorruptBackupFile`, "Expected exception AssertionError but no exception was thrown", at `TestRestoreCore.java:284`.
- Line 12: focused `TestRestoreCore tests=4 failures=0 errors=0 skipped=0` (JUnit XML written 23:49Z).
- Line 14: gate 2026-10-10T23:33:35Z to 23:52:40Z. Proof date used: 2026-10-10.
- Line 4 (live tip `e31bdaa4d27`, verified 2026-10-10) is superseded by the fork tip now.

SOLR-9382 (`receipts/SOLR-9382.md`):
- Line 5: gated head `2952bae9d3927c6f1e648308469f042795f8886a`; proof base `cabedd1d968059215188f4e7563fb303241899ed`.
- Line 8: proof leg on base `TestReplicationConfFileGlob tests=4 failures=1 errors=0`; failure `testGlobExpandsToMatchingFiles`, "expected several stopword files, got []", at `TestReplicationConfFileGlob.java:50`.
- Line 13: focused `tests=4 failures=0 errors=0 skipped=0`.
- Line 15: gate 2026-10-11T00:08:24Z to 00:27:03Z. Proof date used: 2026-10-11.

SOLR-10364 (`receipts/SOLR-10364.md`):
- Line 3: gate green at packaged head `efb1e6717f9ffda64b52c0820efb52ba58832b50` (2026-10-11).
- Line 4: live tip `502bdbf033faa648792372960d01c54728349ee9`.
- Line 5: base `cabedd1d968059215188f4e7563fb303241899ed`; change set `cabedd1d96..502bdbf033`.
- Line 10: premise run on base `tests=6 failures=1`; BindingException on `SetItem`, "Can not set java.util.Set field ... to java.util.ImmutableCollections$ListN".
- Line 11 and line 20: head `tests=6 failures=0`.
- Line 26: SOLR-4422 import-block interaction (landing note only, kept out of the draft).
- Line 27: `Map<String, Set>` not handled (used in Limits).

## Head check

- SOLR-9091: `ls-remote origin refs/heads/solr-9091-submit` = `e6bfa5c626812f1452ed8c0158697ab8653a40d2`. Matches the receipt head. Drafted at it. The packaging commit is on the fork now (the vm2 queue monitor line 377 said local only, before the push; the live ref settles it).
- SOLR-9382: `ls-remote` = `2952bae9d3927c6f1e648308469f042795f8886a`. Matches the receipt head. Drafted at it.
- SOLR-10364: `ls-remote` = `502bdbf033faa648792372960d01c54728349ee9`, the receipt's live tip, not its packaged head. Per the brief, drafted at the packaged head `efb1e6717f9ffda64b52c0820efb52ba58832b50`. `git cat-file -t efb1e6717f...` fails locally, and the fork does not have it. Consequences:
  - Every GitHub link to `efb1e6717f` returns 404 until the commit is pushed to `solr-10364-submit`. Decide before posting: push the packaging commit, or re-point the draft's links to `502bdbf033` (the receipt says the packaging commit only removes `SOLR-10364-TESTING.md`, so the Java line numbers should match).
  - Packaging delta (not in the draft): the packaged head removes `SOLR-10364-TESTING.md`, which is present at the live tip (`ls-tree`). The receipt says nothing else changes; I could not check that because the commit is not local.

## Title source

- SOLR-9091: changelog `changelog/unreleased/SOLR-9091-restore-verify-backup-checksum.yml` at `e6bfa5c626`. Title: "Replication-handler restore now verifies every file copied from the backup against the checksum in its footer, so a corrupt backup file fails the restore (and rolls back) instead of being switched in silently."
  - Conflict: "(and rolls back)" does not match `reports/replication-backup-round-1-g1.md` finding 3 or the code. The download step throws (`RestoreCore.java` head L209-L212) before `core.modifyIndexProps` at L219, so the index is never switched and no rollback of the live index happens. The draft does not repeat "rolls back"; it says the restore "fails before the index is switched, and the live index stays in place". The changelog title needs a branch fix (not in the adopted list in `material/replication-backup-round-1-answers.md`) before posting.
- SOLR-9382: changelog `changelog/unreleased/SOLR-9382-replication-conffiles-glob.yml` at `2952bae9d3`, corrected by the packaging commit as material finding 15 asks. Title: "Replication handler confFiles on the leader accepts * and ? in the file name part (for example _schema_analysis_*.json, ...), so managed synonyms, stopwords and their language variants replicate without listing each file." Checked against the code; consistent.
- SOLR-10364: changelog `changelog/unreleased/SOLR-10364-bean-set-fields.yml` at the live tip `502bdbf033` (not local at `efb1e6717f`; the receipt says that commit does not touch it). Title: "SolrJ bean binding (DocumentObjectBinder) now supports Set, HashSet and LinkedHashSet fields, not only List, Collection and arrays."

## Material and category round

- SOLR-9091: `material/replication-backup-round-1-answers.md` lines 29-34 (audit only, then adopted fixes). Done at head: no `SOLR-9091-TESTING.md` in the tree; the test comment at `TestRestoreCore.java:295` now reads "the failed restore left the live index in place" (finding 3). The new-check wording follows finding 4 (own footer; no backup-recorded comparison; backup-side read unchanged). Lands last in the RestoreCore cluster (line 34). Material and round-1 report predate the gate; the receipt governs.
- SOLR-9382: `material/replication-backup-round-1-answers.md` lines 72-76. Done at head: changelog title corrected (finding 15); no `SOLR-9382-TESTING.md`; long lines shortened (finding 16, per the receipt). Finding 14 requires the PR to say it "answers the wildcard suggestion only"; the Limits summary says so.
- SOLR-10364: no `material/` entry (grep for 10364 found nothing). Category round: `reports/solrj-clients-round-1.md` line 28 (audit only; open question "is a Set-support PR worth it?") and line 105 (recommend bean Set fields only, `Map<String, Set>` in Limits). `reports/solrj-clients-round-1-s2.md` lines 13-20, 35-40, 60, 69. The worth-a-PR question is not answered in the material; the draft assumes yes because the assignment asks for it. Lead to confirm.

## Citation checks

Method: each range read at its SHA with `git show <sha>:<path>` or `git grep -n`, line numbers from that output. Pre-change symptom code links the merge-base `cabedd1d96`; code the change produces links the head.

SOLR-9091 (head `e6bfa5c626`):
- `RestoreCore.java` L104-L108 (`verifyRestoredFile`, `checksumEntireFile`): checked.
- L180 and L187 (calls on the repoCopy paths): checked. L183 (localCopy, unchecked): checked.
- L209-L212 (rethrow before switch), L219 (`modifyIndexProps`): checked; used in the report only.
- L335-L338 (checksum warn and `return null`, still present): checked.
- `TestRestoreCore.java` L234-L297 (new test method, `@Test` at 234, closing brace at 297): checked. L284 (`expectThrows`): checked. L295 (comment): checked.
- Symptom at base `cabedd1d96`: `RestoreCore.java` L169 (repoCopy when a local file differs) and L175 (repoCopy for files the local index does not have): checked. L316-L327 (base `checksum`, warn at 323, `return null` at 326): checked.
- Changelog path: checked by `ls-tree`.

SOLR-9382 (head `2952bae9d3`):
- `ReplicationHandler.java` L774 (call inside the loop): checked. L806-L843 (Javadoc 805-809, method 810-843): checked.
- `TestReplicationConfFileGlob.java` L48-L54 (`testGlobExpandsToMatchingFiles`; the assert at L50): checked.
- Symptom at base `cabedd1d96`: `ReplicationHandler.java` L776-L777 (path resolve; `continue` when not an existing file, no log): checked.
- `ManagedResourceStorage.java` L128-L131 at base (config dir when `storageDir` is unset): checked. L444-L446 at base (`getStoredResourceId`, L445 `replace('/', '_') + ".json"`): checked.
- Changelog path: checked by `ls-tree`.

SOLR-10364 (draft links `efb1e6717f`; ranges read at the parent `502bdbf033`, since `efb1e6717f` is not local):
- `DocumentObjectBinder.java` L299-L304 (Set branch in storeType; L301 child error): checked at `502bdbf033`.
- L498-L505 (inject Set branch): checked at `502bdbf033`.
- `TestDocumentObjectBinder.java` L172-L189 (`@Test` at 172, `testSetFields` to 189): checked at `502bdbf033`.
- Symptom at base `cabedd1d96`: L288-L297 (Collection/List and array branches; no Set branch): checked. L492-L493 (final `set(obj, val)`): checked.
- Changelog path: checked by `ls-tree` at `502bdbf033`.

## Choice section decisions

- SOLR-9091: no choice section. The check is the one change. The broader question (compare with a checksum recorded in the backup, and stop swallowing the backup-side read) is the wider fix that the standing rule puts in Limits with a follow-up offer. The extra full read is a cost, so it is stated as a behavior change in "What this change does", not as a question.
- SOLR-9382: no choice section. The leader reads `confFiles` for its file list, so expanding there is the one natural place. The reload report is wider and is named in Limits, as finding 14 asks.
- SOLR-10364: no choice section. The base has no Set branch, so a Set field could not take a non-empty value before; the change adds support without changing an existing working binding (the base multi-valued case is shown by the premise run; the single-value base case was not run). LinkedHashSet against HashSet is not a live choice, because a declared HashSet accepts a LinkedHashSet. The `Map<String, Set>` route is named in Limits with the follow-up offer.

## Receipt gaps and other findings

- SOLR-9091 changelog title "(and rolls back)": see Title source. Branch correction owed before posting.
- TestRestoreCore count (9091 receipt 4): consistent with the head. `TestRestoreCore.java` at `e6bfa5c626` has three `@Test` methods (lines 90, 182, 234) and one more test-named method, `testBackupFailsMissingAllowPaths` (line 168, no annotation), so four test methods. This is not a receipt gap for 9091.
- The same counting may clear the 9865 and 17287 mismatch in `reports/replication-backup-round-1-g1.md` finding 2. At `4937608bb181`, `git grep -c` on "@Test" or "public void test" in `TestRestoreCore.java` gives 7 lines, which fits 3 annotated methods plus 4 test-named methods. I did not read those files in full, and the JUnit XML would settle it. The lead may want to check before holding those two.
- The material predates the receipts (9091 "audit only", 9382 "no draft this round"). Per the material's own rule, the receipts govern; the drafts follow the receipts.
- SOLR-10364 landing: 4422 changes the import block next to the 10364 imports (receipt line 26). Keep both import sets when combining; not in the draft.

## Not verified

- The packaging delta at `efb1e6717f` (only the receipt's statement). Its existence and content are not checked locally.
- Test counts, dates and base-run results: taken from the receipts. The JUnit XML and gate logs are on vm2 and are not on disk.
- 10364 links: not resolvable until the commit is pushed (see Head check).
- Sizes: drafts are about 3,641 (9091), 3,561 (9382) and 3,161 (10364) characters with links, so 9091 and 9382 run slightly over the 3,500 guide. Link text is most of the overage.
- No build, Gradle, test, gate or test-queue command was run. No commit, push, PR, review, Jira or submit-branch write.
