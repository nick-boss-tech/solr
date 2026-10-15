# Landing-order and interaction review, round 1

- Assignment: `assignments/pool-landing-order-interaction-review-round-1.md`.
- Claim: `claims/pool-landing-order-interaction-review-round-1.md` (Windows review host, standing coordinator claim).
- Date: 2026-10-15. Host: Windows review agent. No builds, tests or gate runs. No public writes. No refs moved.
- Read first: `reports/branch-review-verdicts-landing-round-1.md`, `reports/verdict-landing-and-security-round-1.md`, `material/replication-backup-round-1-answers.md`, `material/solr-9865-packaging-input.md`, `reports/internal-blocks-round-1.md`, `material/update-29-rulings.md`, `reports/review-opened-28-g3.md`.

## Summary

| Set | Tickets (order stated) | Verdict | Open items (not wording) |
|---|---|---|---|
| 1. RestoreCore | 18532 (PR #5100), then 9865, then 17287, then 9091 | SATISFIED as a set (order and interaction stated the same way in all four drafts) | G1 (9865 packaging and gate on top of PR #5100); G2 (9091 rebase after PR #5100, test-file conflict) |
| 2. IndexFetcher | 12246, then 12085, then 11650, then 6711 | SATISFIED | none (no conflicts, no shared lines; receipts are a separate record) |
| 3. Atomic merger | 6045, then 12703 | SATISFIED | G3 (combined run and rebase of 12703 on 6045) |
| 4. Counter and atomic operands | 7504, then 16655, then 12705 | SATISFIED | G4 (combined run and rebase of 12705; import hunk); G5 (12705 rebase onto 16655, 2 hunks) |
| 5. Schema and configset freshness | 15674, then 15478 | SATISFIED | none |

No draft in these sets needed a code change to state its order correctly. Eleven drafts were corrected (wording only, Limits and landing-order lines). Three drafts were checked and left alone (SOLR-15674, SOLR-15478, SOLR-18532).

The G items are gates or rebases that can only run once the rebased or packaged heads exist. They are specified in the last section. They are not posted, because no such head exists yet and no build may run on this host.

## Method

- Heads: taken from `git ls-remote origin refs/heads/solr-<n>-submit` (read on 2026-10-15) and checked equal to the local tracking refs. Drafts cite the same heads.
- Merge-base: `git merge-base upstream/main <head>`, with upstream/main at `7767d36d29f`.
- Overlap: `git diff -U0 <merge-base> <head>` per file, and the hunk lists in the section tables.
- Trial merges: `git merge-tree --write-tree --merge-base=<branch merge-base> <current> <head>`, chained. Each step applies one branch's own diff onto the landed state. Trees and commit objects were written with `commit-tree` (no refs). One packaged tree for SOLR-9865 was built with a temporary index file in the scratchpad (no ref). Tree ids are listed so a later pass can compare.
- Behavior: read from the merged tree (`git show <tree>:<path>`) and from `upstream/main` code, with line numbers.

Heads and merge-bases used:

| Ticket | Branch head | Merge-base with upstream/main | Own commits | Live PR |
|---|---|---|---|---|
| SOLR-18532 | `7dfd3d98d0a2` | `3f5d4c5bf8ac` | 2 | #5100 OPEN, head matches (gh read, 2026-10-15) |
| SOLR-9865 | `4937608bb181` | `14c7aac0d151` | 6 (cd46e4a is the rollback commit) | none (HOLD) |
| SOLR-17287 | `6957daf82610` | `e2cdb2d7e8ae` | 6 | none |
| SOLR-9091 | `a596a39d2e1e` | `cabedd1d9680` | 5 | none (REVIEW OWED) |
| SOLR-12246 | `3ffc2539ec76` | `14c7aac0d151` | 6 | none |
| SOLR-12085 | `a220b0728dc8` | `14c7aac0d151` | 7 | none |
| SOLR-11650 | `e4f5e941cd8e` | `14c7aac0d151` | 3 | none (HOLD) |
| SOLR-6711 | `73c5e6e0d243` | `cabedd1d9680` | 6 | none |
| SOLR-6045 | `04208ba7f0b4` | `97d973814336` | 8 | #5077 OPEN, head matches |
| SOLR-12703 | `bdd29ba19900` | `97d973814336` | 4 | #5085 OPEN, head matches |
| SOLR-7504 | `2fe06bfd917f` | `97d973814336` | 4 | #5081 OPEN, head matches |
| SOLR-12705 | `aa56b7b1be4c` | `0cc328310f8f` | 4 | #5096 OPEN, head matches |
| SOLR-15674 | `75ddf9ba518a` (scrubbed) | `b5c71bc5573c` | 3 | none (REVIEW OWED) |
| SOLR-15478 | `0478bdf0ac5c` | `b5c71bc5573c` | 5 | none (READY TO OPEN) |
| SOLR-16655 (only for the 12705 order line) | `5e2317443f41` | `14c7aac0d151` | 6 | not checked |

## Set 1: RestoreCore chain (18532, 9865, 17287, 9091)

Order of record: `material/replication-backup-round-1-answers.md` adopts 9865, then 17287, then 9091 (9091 last). The 18532 interaction is recorded in `material/solr-9865-packaging-input.md` (decision of 2026-10-10: SOLR-18532 lands first, and the SOLR-9865 branch drops its overlapping RestoreCore rollback hunk when prepared for opening).

Overlap by diff:

| File | 9865 | 17287 | 9091 | 18532 |
|---|---|---|---|---|
| `RestoreCore.java` | import (line 23); previous directory recorded at line 210, rollback at lines 232 to 235 (commit cd46e4a) | import (line 51); `if (success)` at line 242, clear at about lines 247 to 256 | new method `verifyRestoredFile` (line 104); two calls after the backup copies (lines 180 and 187) | switch at line 214, rollback in the catch block from line 222 |
| `TestRestoreCore.java` | adds a test method at the end of the class (+63 lines; +4 lines later) | adds a test method at the end (+87) | adds a test method at the end (+65) | adds a test method at the end (+118) |
| `UpdateLog.java` | none | adds two methods (+56) | none | none |

Trial results (from `upstream/main`):

- Recorded order 9865, 17287, 9091: clean at each step. Final tree `aa2382fe8aa7`.
- 9865 and 17287 in either order: clean, same tree `00e5be7e1a5c`.
- 9865 and 9091 in either order: clean, same tree `8621b9ddaa85`.
- 17287 and 9091 in either order: clean.
- 18532 alone, and 18532 with 17287: clean.
- 18532, then the full 9865 head (cd46e4a included): conflict in `RestoreCore.java` at the rollback lines. Expected: this is the overlap the packaging decision removes.
- Packaged 9865 (the 9865 head with the RestoreCore.java blob set back to the merge-base blob `2d5e1f2fa92`; the test and changelog kept). Commit object `6f0eb739050b`, tree `ec54ac41a81b`. Diff against the 9865 head: RestoreCore.java only (1 insertion, 10 deletions). Applies clean on upstream/main alone, and clean after 18532 (tree `8468d888ac87`).
- 18532, then packaged 9865, then 17287: clean (tree `5572a1c75893` after 17287).
- 18532, then packaged 9865, then 17287, then 9091: conflict in `TestRestoreCore.java`, three hunks at the end of the class. Each side adds a new test method at the same place (18532's `testFailedRestoreKeepsNonDefaultIndexPointer` against 9091's `testRestoreRejectsCorruptBackupFile`). `RestoreCore.java` auto-merges. Keeping both methods is the whole resolution. The same conflict occurs in every ordering that places 9091 after 18532.
- 9091 with 18532 alone: the same three-hunk conflict in `TestRestoreCore.java`.

Behavior interaction (code reading, `upstream/main` RestoreCore.java, lines 207 to 256):

- The SOLR-9091 check runs in the download futures (lines 177 to 205) before the switch at line 207 (`core.modifyIndexProps(restoreIndexName)`). A checksum mismatch throws there, so neither the 18532 rollback nor the 9865 rollback is reached, and the 17287 clear is not reached either. A corrupt backup file leaves the live index and its pointer untouched.
- The 17287 clear is in the `if (success)` block after `deleteNonSnapshotIndexFiles`. The rollback path (catch, lines 215 to 240) throws before that block, so a rolled-back restore keeps its update log. This is the behavior the 17287 draft needs to state when 18532 is merged first.
- The 18532 and 9865 rollback edits sit in the same catch block. Packaging removes the 9865 edit and leaves 18532's in place, so the packaged 9865 test then exercises 18532's rollback.

Draft statements, before and after:

- SOLR-9865: before, a conditional sentence ("If PR #5100 lands first ...") and no order line. After: "Landing order: SOLR-18532 (PR #5100) lands first. This change then drops its RestoreCore rollback edit and keeps only the regression test. SOLR-17287 and SOLR-9091 follow, in that order."
- SOLR-17287: before, no order line. After: a landing bullet that names 18532 and 9865 first, 9091 after, and states that the clear does not run on a rolled-back restore. The bullet says the diffs apply without a textual conflict in that order (verified above).
- SOLR-9091: before, no order line. After: a landing bullet saying 9091 is last, names the TestRestoreCore.java conflict with PR #5100 (three hunks, keep both methods), and says 9865 and 17287 apply cleanly.
- SOLR-18532 (live PR #5100, not edited): "Once this change lands, I will follow up on that branch by dropping its rollback edit" matches the order above. No change proposed.

Decisions:

- D1. The packaged 9865 state is modeled as cd46e4a's RestoreCore hunk reverted, test and changelog kept. Basis: the packaging input's main-side record and the assignment's "drops its overlapping rollback hunk (commit cd46e4a3521)".
- D2. The 9865 packaging itself is not pushed in this round. Reason: packaging moves the gated head of `solr-9865-submit` and the receipt, and it also needs a changelog decision (the fragment describes the rollback fix that this branch will no longer carry) and a re-derived What, Proof and Choice section. The main side owns that pass. This is a judgement call against the "write the patch and push" rule. The coordinator can reverse it; the recipe is in G1.
- D3. The 9865 draft's What, Proof and Choice sections still describe the write-back. They are left as they are and the draft stays on hold. The Choice section asks about the write-back route, which has no live alternative once PR #5100 carries the fix, so it should be removed at packaging.

Verdict: SATISFIED as a set for order and wording. Not in this verdict, and open at opening: G1 and G2.

## Set 2: IndexFetcher chain (12246, 12085, 11650, 6711)

Order of record: `material/replication-backup-round-1-answers.md` adopts 12246, then 12085, then 11650, with 6711 last. The census records the same.

Overlap by diff (hunks, new-file line numbers, at each head):

- SOLR-12246: `IndexFetcher.java` line 1265 (one log level).
- SOLR-12085: `IndexFetcher.java` line 79 (import), and the `hasUnusedFiles` method at lines 868 to 879.
- SOLR-11650: `IndexFetcher.java` lines 306 to 333 and 486 to 489 (leader URL messages); `ReplicationHandler.java` line 90 (import) and 1041 (details response); `URLUtil.java` (SolrJ) only.
- SOLR-6711: `IndexFetcher.java` lines 898 to 987 and 2030 (replication.properties writes and the time and failure records); `ReplicationHandler.java` lines 312, 813 to 839, 1347, 1675 (poll flag, persistence).

No two branches change the same line. Shared file only: `IndexFetcher.java` (all four) and `ReplicationHandler.java` (11650 and 6711, at different lines).

Trial results: all twelve ordered pairs clean. Chains in four orders (recorded; reverse; 6711 first; 11650 first) are clean and give the same final tree `dcab4676a149`.

Behavior interaction: none found. 12246 changes a log level. 12085 changes which files count as used, inside `hasUnusedFiles`. 11650 changes what the details response and three messages print, and adds a SolrJ helper. 6711 changes when the persisted poll flag is written, under one lock shared with the time and failure records. None of the four changes the same statement.

Draft statements, before and after: none of the four drafts had a landing-order line. Each now has one. 12246: first, then 12085, then 11650, 6711 last. 12085: second. 11650: third. 6711: last. Each says the four merge without a conflict in either order of every pair.

Verdict: SATISFIED. SOLR-11650 is on hold for owed branch commits (title, javadoc, test comment). Those commits do not touch any IndexFetcher line above, so the order holds after they land.

## Set 3: SOLR-6045 with SOLR-12703

Order: no order was on record (grep of `reports/`, `material/`, `claims/` and `assignments/`). The drafts said "apply in sequence" (6045) and "should be sequenced" (12703). Decision D4 below.

Overlap by diff: `AtomicUpdateDocumentMerger.java` only, in `mergeDocHavingSameId` (6045 changes its loop source at line 202 and adds the `containsOperationMap` and `atomicOperations` helpers at lines 88 to 141; 12703 adds the nested check at lines 162 to 175 in the old loop and a helper at lines 710 to 720). Both add tests to `AtomicUpdatesTest.java`.

Trial results: 6045 then 12703, and 12703 then 6045, both clean, both final tree `01a8554c50ef`.

What the second one must reconcile: in the merged file, the nested-operation check from 12703 sits inside the `for (Entry ... : atomicOperations(sif))` loop that 6045 introduces (merged file lines 198 to 217). Git places it there without a hand edit. So the reconciliation is the placement, which the merge does, and the combined rule: a nested operation is rejected for each operation, including each operation of a repeated field. The combined case (a repeated field whose second map holds a nested operation) is not in either test set.

Draft statements, before and after:

- SOLR-6045: before, "Apply them in sequence" (no order). After: "Landing order. This change lands first, then SOLR-12703", with the reason and the merge result.
- SOLR-12703: before, "should be sequenced" (no order). After: "Landing order: SOLR-6045 lands first, and this change follows and reconciles with it", with the placement of its check and the untested combined case named as a Limit.

Decision D4: 6045 first. No record existed. Option taken: the sequencing in which the second change must carry the combined case, because the 12703 draft already describes its check as written inside the loop that 6045 replaces. The merged result is the same in either order, so the choice affects the record, not the code.

Verdict: SATISFIED for order and wording. Open at opening: G3.

## Set 4: SOLR-7504 with SOLR-12705 (and SOLR-16655 in the 12705 order line)

Order of record: 7504 before 12705 (`reports/review-opened-28-g3.md`, the cross-PR ruling that the counted-field rule governs; `material/update-29-rulings.md`). 16655 before 12705 (`reports/review-opened-28.md` and the 12705 draft; 12705's draft says it rebases onto 16655).

Overlap by diff:

- `CountFieldValuesUpdateProcessorFactory.java`: both. 7504 changes the lambda body (lines 73 to 136). 12705 adds a fourth argument `false` to the `mutator(...)` call (line 89). Auto-merges in both orders.
- `FieldMutatingUpdateProcessorTest.java`: both. 7504 adds a test; 12705 adds an import line. One conflict hunk in the import block, in both orders (verified: one `<<<<<<<` marker).
- `FieldMutatingUpdateProcessor.java`: 12705 only, against 16655. Two conflict hunks in both orders (verified: two markers).
- `ParsingFieldUpdateProcessorsTest.java`: 12705 only.

Combined behavior when both land (read from the merged counter, tree `17b119aa9ea1` before the import hand-merge):

- The counter's mutator call has `false` (line 89). `FieldMutatingUpdateProcessor.processAdd` checks `mutateAtomicOperands()` before touching a field's operands (line 152), so the counter does not receive operations one by one. It receives the whole atomic map, and 7504's `countAtomicUpdate` handles it (lines 82 and 101 to 136).
- Counted field after both: a set counts its elements (null counts 0), the last set decides, add, remove, inc, add-distinct and removeregex are rejected with BAD_REQUEST, and a plain value mixed with a map is rejected. This is 7504's rule, unchanged by 12705.
- Other field processors after both: set, add, add-distinct and remove operands go through the mutator (12705). For example a date parser now parses the operand, and IgnoreFieldUpdateProcessorFactory keeps `{set: null}` where it used to remove the field. 7504 does not touch those processors.
- The overlap on set-null and add/remove therefore depends on the `false` argument. A rebase that dropped it would send counted operands through the counter's lambda one by one. No test on the combined tree pins the counted-field outcome with 12705's change present. 12705's draft already says so (its counting test pins only the opt-out).

Draft statements, before and after:

- SOLR-7504: its Limits said only that 12705 changes the base class and conflicts in an import block. After: the bullet also says 12705 changes set, add, add-distinct and remove for other processors, keeps the counter out of that change with `false`, and that after both land the counted-field rules above hold.
- SOLR-12705: its Limits said "The combined run above is the reason SOLR-7504 goes first." That reason is not the run; the reason is the counted-field rule. After: "SOLR-7504 goes first because its counted-field rule governs the counter, and this change keeps the counter on that rule through the opt-out above." The two-conflict and one-hunk statements were checked and are correct.

Verdict: SATISFIED for order and for the behavior statements of both drafts. Open at opening: G4 and G5.

## Set 5: SOLR-15674 with SOLR-15478

Order: 15674 first, adopted (census: "landing order: after SOLR-15674" on 15478; 15674 receipt "pairing" note).

Overlap by diff: no shared file. 15674 changes `ZkSolrResourceLoader.java`, `SolrConfig.java`, `IndexSchemaFactory.java`, its test and its changelog. 15478 changes `ZkConfigSetService.java` (line 118, `stat.getMzxid()`), its test and its changelog. Trial merges clean in both orders, same final tree `e31ec9e121d3`.

Heads and gates: 15674 at the scrubbed head `75ddf9ba518a` (receipt GREEN 2026-10-13, receipt tip tree `e1a6fafc1377` matches `git rev-parse 75ddf9ba518a^{tree}`). 15478 at `0478bdf0ac5c` (receipt GREEN). No draft cites a pre-scrub commit (`ba01c83d4c5`, `9b51865402`, `4cc98731`); scanned.

Limits cross-check: 15674's Limits name the configset cache as SOLR-15478's and say 15478 changes it separately (accurate: 15478 touches only ZkConfigSetService). 15478's Limits name the default SolrCloud schema path as SOLR-15674's (accurate: 15674 changes IndexSchemaFactory). 15478 does not reference `VersionedConfig` or `getZkResourceInfo` (grep at the head), so the 15674 constructor change does not reach it.

Draft statements: 15674 says "meant to land before SOLR-15478"; 15478 says "meant to land after SOLR-15674". Consistent. No edit.

Verdict: SATISFIED.

## Corrections applied (pool worktree, drafts only, not committed)

Eleven draft files changed. Each change is wording only: a Limits bullet or paragraph, or one sentence. No em dash or en dash in any added line (scanned). `git diff --stat` on the eleven files: 11 files, 14 insertions, 5 deletions.

1. `pr-drafts/replication-backup/SOLR-9865.md`: the conditional Limits bullet replaced by the landing-order bullet (18532, then this change, then 17287 and 9091).
2. `pr-drafts/replication-backup/SOLR-17287.md`: landing-order bullet added (18532 and 9865 first, then this change, then 9091; clear does not run on a rolled-back restore).
3. `pr-drafts/replication-backup/SOLR-9091.md`: landing-order bullet added (last; TestRestoreCore.java conflict with PR #5100, keep both methods).
4. `pr-drafts/replication-backup/SOLR-12246.md`: landing-order paragraph added (first).
5. `pr-drafts/replication-backup/SOLR-12085.md`: landing-order paragraph added (second).
6. `pr-drafts/replication-backup/SOLR-11650.md`: landing-order bullet added (third).
7. `pr-drafts/replication-backup/SOLR-6711.md`: landing-order paragraph added (last).
8. `pr-drafts/update-processing/SOLR-6045.md`: "Sequencing ... apply in sequence" bullet replaced by a landing-order bullet (6045 first, the placement of 12703's check, combined case untested).
9. `pr-drafts/update-processing/SOLR-12703.md`: "Sequencing with SOLR-6045" sentences replaced by a landing-order statement (6045 first, the check sits in the flattened loop).
10. `pr-drafts/update-processing/SOLR-7504.md`: one sentence added at the end of the SOLR-12705 Limits bullet (the opt-out keeps counted fields on this rule).
11. `pr-drafts/update-processing/SOLR-12705.md`: the reason sentence for landing SOLR-7504 first corrected to the counted-field rule.

Unchanged after checking: `pr-drafts/solrcloud/SOLR-15674.md`, `pr-drafts/configsets/SOLR-15478.md`, `pr-drafts/flaky-fixes/SOLR-18532.md`.

## Pending approval (public writes; none made)

These are PR body edits on live open PRs. Each needs Nick's explicit go-ahead for that exact edit, then a read of the live body, a REST PATCH from a file, and a read-back. The intended text is the corrected draft. Read the live body first; the draft and the live body may differ.

- PR #5077 (SOLR-6045, head `04208ba7f0b4`): replace the Sequencing bullet with the landing-order bullet from `pr-drafts/update-processing/SOLR-6045.md`.
- PR #5081 (SOLR-7504, head `2fe06bfd917f`): add the opt-out sentence to the SOLR-12705 Limits bullet, from `pr-drafts/update-processing/SOLR-7504.md`.
- PR #5085 (SOLR-12703, head `bdd29ba19900`): replace the sequencing sentences with the landing-order statement, from `pr-drafts/update-processing/SOLR-12703.md`.
- PR #5096 (SOLR-12705, head `aa56b7b1be4c`): change the reason sentence for landing SOLR-7504 first, from `pr-drafts/update-processing/SOLR-12705.md`.
- PR #5100 (SOLR-18532): no edit proposed.

No PR comment is drafted. No comment is needed for these sets.

Tickets with no PR number in the census (READY TO OPEN, HOLD or REVIEW OWED): SOLR-9865, 9091, 17287, 12246, 12085, 11650, 6711, 15674, 15478. Not checked on GitHub this round. Their drafts are the text when they open, so there is nothing to edit live.

## Gate and opening-time items (not run; not posted)

These need heads that do not exist yet, so no gate file is posted this round. The coordinator decides when to create the heads and post the gates.

- G1 (SOLR-9865 packaging). Create on `solr-9865-submit` the packaged commit: RestoreCore.java set back to blob `2d5e1f2fa92` (the merge-base content), test kept, changelog decided (remove, or rewrite as test-only). Trial tree `ec54ac41a81b`. The focused gate must run on a head that contains PR #5100 (`7dfd3d98d0a2`), not on the packaged commit alone, because on base the test fails by design. Test: `org.apache.solr.handler.TestRestoreCore`, module `:solr:core`, with the proof step. Then re-derive 9865's What, Proof and Choice sections at that head.
- G2 (SOLR-9091 rebase). After PR #5100 is on main: rebase onto main, resolve TestRestoreCore.java by keeping both test methods, then run the focused TestRestoreCore gate at the rebased head. Textual resolution only; behavior not run.
- G3 (SOLR-12703 on SOLR-6045). Rebase 12703 onto 6045 (trial tree `01a8554c50ef`), then a focused `AtomicUpdatesTest` run at that head, with a case for a nested operation inside a repeated field.
- G4 (SOLR-12705 on SOLR-7504). Merge the `FieldMutatingUpdateProcessorTest.java` import block by hand (one hunk), then focused `FieldMutatingUpdateProcessorTest` and `ParsingFieldUpdateProcessorsTest` at the combined head, with a counted-field test that fails if the `false` argument is removed.
- G5 (SOLR-12705 on SOLR-16655). Rebase onto 16655 (`5e2317443f41`), two hunks in FieldMutatingUpdateProcessor.java; gate at the rebased head.

Order between G3, G4 and G5 follows the landing order of each pair. G2 waits for PR #5100.

## Decisions

- D1 to D3 (set 1): see the set section. Packaging not pushed this round (D2).
- D4 (set 3): 6045 first. No record existed; the thorough option was taken, so the later change carries the combined case.
- Set 4 order: 7504, then 16655, then 12705 (recorded). The combined-run reason in 12705 was replaced by the recorded counted-field rule.
- Set 2 and set 5 orders: as recorded.
- Drafts that had no order line now state one (set 2 four drafts; set 1 17287 and 9091; set 3 both).

## Not verified

- Gate receipts for 12246, 12085, 11650, 6711, 6045, 12703, 7504, 12705 and 16655 were not re-read in this round. Heads were checked against ls-remote; gate state was taken from the drafts and census.
- No test was run. Behavior statements come from merged code reading and trial merges. The combined behavior in G1 to G5 is not established.
- The 16655 branch was checked only for the two-hunk conflict claim in the 12705 draft.
- PR state was read on 2026-10-15 (5100, 5077, 5081, 5085, 5096 OPEN; heads matched).
