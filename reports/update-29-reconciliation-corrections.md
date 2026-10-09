# Update-29 reconciliation corrections

Claim: `claims/update-29-reconciliation-corrections.md` (commit `3e3fa1b2685`). Source: `material/update-29-reconciliation-answers.md` (commit `e47afe13b0c`), the refreshed receipts for SOLR-4366 and SOLR-17612 at the same commit, and the receipts at `cfb8f96c4c3`. Corrections: commits `e1d468d5f9d` (drafts) and `bda8019dec2` (audits).

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Five subagents made the edits, each in a named set of files. The lead reviewed every diff, fixed what the review found, and made the commits. Nothing was posted. No PR, submit branch, or PR description was changed. No builds, Gradle or tests were run.

## Summary

- **18 files corrected.** Fifteen drafts (Proof, and two Limits and a status sentence) and three audits.
- **Every draft Proof now cites only what its receipt, or the answers file, names.** Unnamed heads, CI runs, commits and counts were removed or labelled. Internal log file names were removed, since maintainers cannot open them.
- **Three audit notes were corrected** against the code at the named commits: SOLR-12705 (the settling note), SOLR-5939 (the tolerant count, which the draft had right), and SOLR-7504 (the mechanism, which is now the opt-out).
- **The SOLR-6065 status is stated as decided.** The 500 status is recorded as decided on 2026-10-06, and the draft keeps 400 only as the alternative that was not taken.
- **The SOLR-5939 and SOLR-5754 tree note now follows the addendum.** `1ddbf36202d` is a merge commit whose tree is `f2e33340f0ea`.

## What changed, by ticket

| Ticket | Change |
|---|---|
| 3657 | Proof cites the 2026-10-07 gate at `14edaca577c0`: 17/17 and 11/11, with 2 failures on base. CI run and head removed. |
| 4841 | Proof cites the gate at `f8850ffd421`, with the counts and the pre-fix run. The live tip `e4c878627108` is named as one changelog-type commit past it. Gate record `524afb62181` removed. |
| 5505 | Proof cites the base run: 2 tests, 1 failure (`testMessagesNameTheCore`). Counts at `44c444aa5cd3`. "Read only" removed. |
| 5754 | Proof cites the combined gate, merge commit `1ddbf36202d` with tree `f2e33340f0ea`. Live tip `46b919e2d4e8` named as one deletion past the gated head. Unnamed trees and steps removed. |
| 5887 | Proof cites the base run: 17 tests, 1 failure (`testAddUpdateCommandErrorNamesCore`). Counts 17/17 at `c4c57ef7bcbd`. |
| 5939 | Proof cites the combined run, merge commit `1ddbf36202d`, tree `f2e33340f0ea`. Base result is 2 of 2 failing. The 19/19 and 2/2 counts come from the SOLR-5754 receipt, which names the same combined gate. |
| 11483 | Proof cites the 2026-10-06 gate at `4431a250f665`: 21/21, and a base run of 21 tests with 1 failure. CI run and head `759f705e205b` removed. |
| 12245 | Limits: the MDC ask is not addressed, and is offered as a follow-up. Null-guard coverage is not included. Both follow `TESTING.md` as recorded. |
| 13265 | Date corrected to 2026-10-07. |
| 14262 | Proof cites the gate at `1e8d2b0075d7` and the base run, 1 of 1 failing at `commitIgnored`. CI commit and run removed. The audit citation in the Choice removed. |
| 14718 | Proof cites the gate at `29c09959791a`: 1/1, and the premise run. Unnamed failure text and unrecorded run counts removed. |
| 16356 | Proof cites the gate at `39c0585072f0`: 1/1 and 5/5. Unnamed commits and the module-check command removed. |
| 16910 | Proof cites the base run, 1 of 3 failing, and the gate at `9fce3e9a7058` at 3/3. |
| 6065 | Status stated as decided on 2026-10-06, with 400 as the alternative not taken. The "Was 500 the right call?" question removed. Verified at `3d2cec9e1ab3`: the code returns SERVER_ERROR. |
| 7022 | Proof states that the gate ran at `6a233ab2fdb`, the live tip was never gated, and the CI run is on `7940e98b0ee0`, which is not on the live branch. Unnamed "does not compile" bullet removed. |
| 12705 | Draft heading kept as "tests". The heading covers both new tests. |

Audits:

- **12705, settling note.** "Unchanged" is corrected. With the default `true`, Concat, FieldValueSubset and Ignore take the new path. Their source files are identical to base.
- **5939, item 2.** Marked addressed at `f8d4bdbea518`. The earlier reading was of `8f7a36fa6dd`, which had no dedupe. The draft's statement is right.
- **7504, item 8.** The mechanism is corrected to the opt-out at `8624b7c3238b`. The pinned-test collision with 12705 (R1) is not decided here.

## Left open, for the next round or the owner

1. **SOLR-12705 audit scope note.** It says the two branches touch different files. Both change `CountFieldValuesUpdateProcessorFactory.java` and `FieldMutatingUpdateProcessorTest.java`, and the merge has no conflict markers. Its "Files touched" list omits those two files. Correct in the next audit round.
2. **SOLR-11483 failing method.** The draft says the `expected:<1000> but was:<10>` failure is the 1000 check. The receipt does not name the failing method. The main side should confirm against `g11483-gate.log`.
3. **Record conflict on the tree note.** The answers file says the SOLR-5754 and SOLR-5939 receipts now note that the tree is a local merge commit. They do not. The commit `e47afe13b0c` changed only the SOLR-4366 and SOLR-17612 receipts. The main-side addendum also says `1ddbf36202d` has tree `f2e33340f0ea`, which contradicts the answers file's "different object" claim. The main side should correct its answers file and the two receipts.
4. **Length.** The guide is about 3,500 characters. Several drafts are over: SOLR-5939 (about 6,950), SOLR-12705 (5,618), SOLR-14718 (4,604 raw, 2,465 without URLs), SOLR-4841 (3,889), SOLR-12245 (3,840) and SOLR-13265 (3,780). The owner decides whether to trim or accept.
5. **SOLR-6065 wording.** The draft now says the status was decided on 2026-10-06. The owner should confirm that the public wording reads correctly.
6. **Still open: the three owner calls.** R1 (counted fields, 7504 against 12705), R2 (plain value ahead of an operation map, 7504 against 6045) and R3 (failed-stream attribution, 14718 against 5939) are recommendations only, as the answers file says. None is taken here.
7. **Spellcheck holds that have moved.** SOLR-10789 now has a green gate receipt (commit `e7cc46a7880`). Its draft is the next spellcheck round. SOLR-4366 has a receipt at its live tip, but decision 3 is still DISCUSS. SOLR-17612 has a baseline receipt, but decision 10 is still DISCUSS.

## Not done

No gate, build or test was run. No draft was posted. No PR, comment, submit branch, or PR description was changed.
