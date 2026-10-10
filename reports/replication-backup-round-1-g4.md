# Replication and backup round 1, part g4: live PR consistency for SOLR-18249 and SOLR-18280

Result: both live PRs match their branch tips and their CI is green. The drift is in the PR text and the receipts, not in the code. Five fixes are owed for the PR body and receipt of SOLR-18249, and several for SOLR-18280.

Written from the subagent's hand-back text, which the subagent could not save as a file. Heads checked read-only: `origin/solr-18249-submit` at `deffea4c51be` and `origin/solr-18280-submit` at `0da92abd9e96`, both matching the claim table.

## SOLR-18249 (live PR #4969)

1. **FIX, PR body, Proof, first paragraph.** The parenthetical says "(Round 29 added only a Javadoc sentence and this text at that head; the behavior verification below was re-run there, and the fail-on-base premise for the overwrite test was also re-run locally ...)". "Round 29" and "premise" are process words, and the "re-run there" claim is not in the receipt. Replacement for the parenthetical: "(The overwrite test was also run locally against base production code: 1 of 1 fails with "overwrite must not delete the previous metadata file", seed 1805F6D2F640339B.)" The `ShardBackupMetadataTest` bullet should say: "These cases cannot run against base code, because they call the `writeBytes` method this PR adds."
2. **FIX, receipt `receipts/SOLR-18249.md` lines 4 and 9 (gated head and date).** Line 4 names `deffea4c51be` as gated, and calls its tip "Javadoc-only, on top of the gated tree", which points at `c4cd38c`. Line 9 says the round 29 gate finished 2026-10-06. Commit `deffea4` was committed 2026-10-07 11:03 UTC. The round 29 review dated 2026-10-07 reviewed `c4cd38c`. `git diff c4cd38c deffea4` is a four-line Javadoc addition in `LocalFileSystemRepository.java`. Replacement for line 4: "- Gated head: `c4cd38c7bd3` (the head the round 29 gate ran against, per the 2026-10-06 date; owner to confirm). Tip `deffea4c51be` adds a four line Javadoc note to `LocalFileSystemRepository.java` and is not separately gated; CI at `deffea4` is green."
3. **NOTE, receipt lines 5 to 7 (logs and counts).** The gate and premise logs are not under `research/`. The only on-disk record is `research/test-queue/results/SOLR-18249.json`: SUCCESS, `ShardBackupMetadataTest` 4 of 4, no head recorded, finished 2026-09-28. That predates the earliest PR commit, so it cannot be tied to any current head. The 1 of 1 and 5 of 5 counts have no on-disk record.
4. **NOTE, PR body, `DeleteBackupCmdTest` bullet.** "with only `DeleteBackupCmd.java` swapped to base, both fail with `IllegalArgumentException`" is not in any receipt. The code side holds: base `DeleteBackupCmd.java` line 171 parses each listed name with no guard, and `ShardBackupId.fromShardMetadataFilename` throws for names without the `.json` suffix (`ShardBackupId.java` 74-76). If no run record is added, replace the last sentence with: "On base, `DeleteBackupCmd.java` parses every listed name without a guard (line 171), so a staging file without the `.json` suffix makes that parse throw `IllegalArgumentException`."
5. **NOTE, verified, no change.** The PR cites fork test run 37238130723. It is a manual dispatch on `ci/solr-18249-proof` at head `e6f1e4880aaf`, conclusion failure, created 2026-10-04. That head has the delete-then-write store and no `writeBytes`, so it is a base-code run. The failure text was not read.
6. **NOTE, PR body, "A choice to check".** The last sentence, "Failing was chosen; please say if you would rather see the logged fallback.", is not a pointed question. Replacement: "Was failing the right call here, or should a logged fallback be the default?"

## SOLR-18280 (live PR #4996)

7. **FIX, PR title and first sentence.** The title says "Fix flaky `TestReplicationHandler.testUrlAllowList`". The local JIRA export says "reproducible `TestReplicationHandler.testUrlAllowList` failures", and the description says the failures come from nightly builds with seeds that reproduce reliably. "Flaky" does not match. Replacement title: "SOLR-18280: Fix `TestReplicationHandler.testUrlAllowList` failures". Replacement first sentence: "`TestReplicationHandler.testUrlAllowList` failed in nightly builds, with seeds that reproduce reliably." The JIRA export is dated 2026-08-18; live JIRA was not re-read.
8. **FIX, PR body, last line.** "Changelog: `changelog/unreleased/SOLR-18280.yml` (fixed)" names a file that is not on `origin/solr-18280-submit` and not in the PR file list. Commit `6d1aaf7` (2026-10-02) says "Drop the changelog entry; test-only fixes are not changelog worthy", and the PR carries the no-changelog label. Replacement: delete the line, or write "No changelog entry: this is a test-only change."
9. **FIX, PR body, Proof.** "`TestReplicationHandler` is a `@Nightly` suite (25 tests)" and "the head passes 24 of 25, twice". On the head the class has 24 `@Test` methods. Replacement for "(25 tests)": "(24 tests)". The run counts must be re-derived from the run's JUnit XML before they stay. A single failure in 24 leaves 23 passing.
10. **FIX, receipt `receipts/SOLR-18280.md` lines 5 and 6 (counts).** Line 5 says counts and proof are "none recorded locally". Two records exist for an older head. `research/test-queue/results/SOLR-18280.json` is SUCCESS: `TestReplicationHandlerUrlAllowList` 2 of 2 and `TestReplicationHandler.testUrlAllowList` 1 of 1, finished 2026-10-01. That run predates `0da92`, which converted the class (committed 2026-10-03). No record exists for the counts the PR body states. Replacement for line 5: "- Counts: no record at `0da92abd9e96`. Older head queue result (2026-10-01, before `0da92`): `TestReplicationHandlerUrlAllowList` 2 of 2 and `TestReplicationHandler.testUrlAllowList` 1 of 1."
11. **NOTE, receipt line 6 (test-state basis).** The receipt says the test state "rests on the live PR's own CI at this head". CI at `0da92` is green (runs 37118857090 and 37118857176, both success). But the changed `testUrlAllowList` sits in `TestReplicationHandler`, which carries `@LuceneTestCase.Nightly`, so CI does not run that change. CI runs only the new class `TestReplicationHandlerUrlAllowList`. Replacement for line 6: "Test state: CI at this head is green (runs 37118857090 and 37118857176). CI runs the new class `TestReplicationHandlerUrlAllowList`. The changed `testUrlAllowList` sits in the `@Nightly` `TestReplicationHandler`, which CI does not run."
12. **NOTE, receipt lines 6 and 7 (date).** The receipt dates the round 31 review 2026-10-06. The review file says "Reviewed: 2026-10-07". Replace both dates with 2026-10-07.
13. **NOTE, closed.** Round 31 findings 1 and 2 are now in the PR body. Finding 3 is closed: `SolrTestCase` applies `SystemPropertiesRestoreRule` as a class rule, and the new class extends it. No change needed.

## Task results

**SOLR-18249 (PR #4969).** Consistent on head, file list, and code. The live head `deffea4c51be` equals the branch tip. The PR file list (9 files, +439/-16) matches the local diff against upstream main. The code checks hold: `LocalFileSystemRepository.writeBytes` stages a sibling temp file and publishes with `ATOMIC_MOVE` and `REPLACE_EXISTING`; base `store()` deletes before writing (`ShardBackupMetadata.java` 114-116). Verdict: hold the receipt until item 2 is settled. The PR is otherwise consistent. The PR has no comments or reviews.

**SOLR-18280 (PR #4996).** Consistent on head, file list, and code. The live head `0da92abd9e96` equals the branch tip. The PR file list (three test files, +162/-14) matches the local diff. Test-only. Verdict: the PR is ready as code; fix the wording and the receipt.

## Owner decisions

1. SOLR-18249 gated head: accept `c4cd38c` with `deffea4` as an ungated Javadoc tip (recommended, from the receipt text and dates), or ask for a focused verify at `deffea4` later.
2. Whether to edit the PR #4969 body (items 1, 4 and 6). The edit is yours; there are no maintainer comments or reviews yet.
3. SOLR-18280 title: the JIRA wording (recommended), or keep "flaky".
4. SOLR-18280 counts: record a run at `0da92`, or cut the counts from the Proof line.
5. SOLR-18280 changelog line: delete (recommended).

## Not checked

- No builds or tests. The gate and premise logs named in the 18249 receipt are not on disk.
- CI: only the head SHA and conclusion of four runs were read.
- PR #4996 inline review threads were not read. Open thread status is unknown.
- JIRA: the local export only, not re-queried live.
- The fork run's failure text for 18249 was not read.
- Receipt claims for 18249 (tidy, Error Prone, module check, changelog parse) have no logs on disk and were not re-checked.
