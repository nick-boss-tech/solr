# Final round 2 report: update processing

Round: `assignments/update-processing-final-round-2.md`. Claim: `claims/update-processing-final-round-2.md`. Material: `material/update-processing-final-round-2.md`, with `material/update-processing-receipts-addendum.md`. Draft format: `pr-formula.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Three subagents reviewed the branches. The lead agent checked their results, re-verified the key claims, and made the commits.

## Summary

- 15 in-scope items: 12 drafted or updated, 3 held.
  - New drafts (10): SOLR-4841, SOLR-5754, SOLR-5939, SOLR-5941, SOLR-5065, SOLR-6065, SOLR-7504, SOLR-12703, SOLR-6045, SOLR-14718.
  - Draft updates (2): SOLR-13265, SOLR-16356.
  - Held (3): SOLR-16673 (new), SOLR-12705 (new), SOLR-7022 (update).
- All 15 live fork tips match the material's heads.
- No Gradle, builds, or tests were run. No submit branch was edited. Nothing was posted to GitHub or Jira. No live PR description was touched. SOLR-18505 was closed in round 1 and is not part of this round.

## Per-branch rows

Proof sources are the files or records each draft names. Where a source gives no counts, the draft says so and gives none.

| Ticket | Head verified | Proof source | Result | Notes |
|---|---|---|---|---|
| SOLR-4841 | `e4c878627108`, yes | Audit evidence section. Gate record at the earlier tip `524afb62181`, per the audit | Drafted (new) | Changelog type is `added`. The only delta from `f8850ffd4214` is the fragment type. The base test does not compile, checked by reading. The GitHub run 37614488227 ran at `41c0087c8a2`, not at this head, and the draft says so. The Choice section (public or package-private) stays, since the audit calls it a maintainers' judgment. |
| SOLR-5754 | `46b919e2d4e8`, yes | Combined receipt, 26 of 26, steps 0 to 4, at `7fbe0128d8b0` with `f8d4bdbea518` | Drafted (new) | The receipt does not cover this head byte for byte. Merged trees: `f8d4bdbea518` with `7fbe0128d8b0` gives `f2e33340f0ea` (matches the addendum). `f8d4bdbea518` with `46b919e2d4e8` gives `7a5d27018596`. The only difference between the two heads is the deletion of `SOLR-5754-TESTING.md` (37 lines). The draft states this. The fail-before run is inconclusive by construction, since base lacks the accessors the new tests use. |
| SOLR-5939 | `f8d4bdbea518`, yes | Combined receipt, 26 of 26, merged tree `f2e33340f0ea` (addendum) | Drafted (new) | The receipt has no class list. The draft claims no per-class counts. The audit's blocking fixes are in the code. The draft names a third public SolrJ addition the audit missed. Adopted items are in Limits, and per-request attribution is in the Choice section. |
| SOLR-5941 | `a4df7bfd214b`, yes | Material gate: AutoCommitUpdateChainTest 2 of 2, CommitThroughNonLeaderTest 1 of 1, ParallelCommitExecutionTest 1 of 1, HttpPartitionOnCommitTest 1 of 1, check clean. Pre-fix history at `62516cc338e`, not base | Drafted (new) | The ref-guide sentence is corrected at this head. The draft discloses the wider effect, per the adopted decision: `DistributedZkUpdateProcessor.java:158` acts on any request whose flag is `true`. A flagged commit is no longer distributed by a leader, and a non-leader commits locally. Five autocommit classes from round-30 records at `62516cc` are not in this head's gate list, and Limits say so. The material gives no gate date, so none is given. |
| SOLR-16673 | `d5c19e64ba1b`, yes | Addendum receipt (the main side's ledger): changelog parse clean, tidy 0, Error Prone 0, pre-fix PASS, ParsingFieldUpdateProcessorsTest 43 of 43, check 0 | **Held** | The changelog title says the bug "also made the Schema Designer fail on sample documents with empty values". Audit finding 3 says that link is untested. Narrow the title on the branch, then release the draft. The draft is held in the scratchpad (`SOLR-16673-held-draft.md`), not in the deliverables. |
| SOLR-5065 | `ab894a996c8a`, yes | Material gate: pre-fix PASS, DefaultSchemaSuggesterTest 2 of 2, ParsingFieldUpdateProcessorsTest 44 of 44, check clean | Drafted (new) | The Proof claims no per-method failures, since the material does not name them. TESTING.md asks for a fail-before run for the suggester pin test. The material's pre-fix PASS does not say it covers that test, and the draft does not claim it. Locale-aware parsing stays open in Limits. |
| SOLR-6065 | `3d2cec9e1ab3`, yes | Material gate: premise PASS, MaxDocsLimitCloudTest 1 of 1, DirectUpdateHandlerTest 8 of 8, check clean | Drafted (new) | The Choice poses 500 against 400. The material calls it "per the recorded decision", but TESTING.md records no SERVER_ERROR decision, so the draft poses it as open. The message still says "Delete documents and optimize", which Limits flag. The merge path through `addIndexes` is not traced, and Limits say so. |
| SOLR-7504 | `22b77196e662`, yes | Material gate: pre-fix PASS, FieldMutatingUpdateProcessorTest 29 of 29, ParsingFieldUpdateProcessorsTest 42 of 42, check clean | Drafted (new) | The narrow scope is as decided: the counter's chain placement stays, and the changelog title does not claim a full fix. A null counts as 0, and duplicate values count. That is current code behavior, not a recorded owner decision. The draft is about 4.1 KB, over the 3.5 KB guide. |
| SOLR-12703 | `63c84919c80b`, yes | Material gate: pre-fix PASS (the updated test fails at the earlier tip `ed95d555e62`), AtomicUpdatesTest 27 tests, 1 skipped, check clean | Drafted (new) | The rejection names the outer operation, the field, and the nested keys, not the nested value. Sequencing with SOLR-6045 is stated. The audit still lists finding 1 as open, since no appended note was added. The draft describes only the code change. |
| SOLR-12705 | `8624b7c3238b`, yes | The material cites a settling run. It is not recorded in `audits/update-processing/SOLR-12705.md` or anywhere in the repo. TESTING.md says the fix lane records it | **Held** | The Proof would state a run the audit does not record. Release it by appending the settling note to the audit, or by drafting without the settling-run sentence. The draft also needs Limits lines for audit findings 1 and 4. The changelog does not mention that `processAdd` removes the field when the mutator returns null (subagent reading, `FieldMutatingUpdateProcessor.java` lines 118-120 and 176-177, unrun). |
| SOLR-6045 | `e4b77fa7ae53`, yes | Material gates: factory fix at `e06aa7624853`. Child-document guard at this head: pre-fix PASS, AtomicUpdatesTest 29 with 1 skipped, AtomicUpdateProcessorFactoryTest 6 of 6, check clean | Drafted (new) | The material names no commit before the guard. The draft uses `e06aa7624853`, which the log confirms is the guard commit's parent. The guard also changes a single child document in an atomic update. By reading, base read it field by field as operation names. The material mentions only lists, so the owner should confirm this is in scope. The audit predates the fix and the guard, and no re-audit note exists. Audit findings 3 to 5 are in Limits, with no test. Sequencing with SOLR-12703 is stated. About 4 KB. |
| SOLR-14718 | `29c09959791a`, yes | Settling note appended to `audits/update-processing/SOLR-14718.md` on 2026-10-09: with the clone reverted, 3 of 3 runs fail deterministically, and the control passes. Green gate at this head per the material, with no counts in any workspace record | Drafted (new) | The draft says "green" without counts. Limits: the reported document can be the wrong one for async failures after the first document, and retries are counted per request, not per document. |
| SOLR-13265 (update) | `c134b34aa27f`, yes | Fail-before verdict in `audits/update-processing/SOLR-13265.md`, section "Fail-before verdict (2026-10-09, verification lane)": head 3 tests, 0 failures. Base with only the test file applied: 1 of 3 fails, at the assertion (line 231) | Updated | "3 of 3" is 3 tests in one run, not 3 runs. The audit's finding table still reads "Still open" for finding 3. The verdict section settles it. Posting status (kept out of the draft): the verdict is recorded, posting needs the owner's go-ahead, and no spotless or tidy record exists at this head. |
| SOLR-16356 (update) | `39c0585072f0`, yes | Addendum receipt: tidy 0, Error Prone 0, pre-fix PASS, UpdateLogClosedCoreTest 1 of 1, UpdateLogTest 5 of 5, check -x test 0 | Updated | The three branch-changed files are identical at `dcb16c775d62` and `39c0585072f0` (empty diff). Round 1's sentence that the module check "excluded the ecjLint tasks" was unsupported and is removed: `gradle/validation/ecj-lint.gradle` attaches ecjLint to check. The receipt has no date, so none is given. |
| SOLR-7022 (update) | `5b822b7e8e98`, yes (new head; drafted head `6a233ab2fdb0`) | CI run 37558668356 covers CI head `7940e98b0ee0`, whose parent is `6a233ab2fdb0`. It does not cover `5b822b7e8e98` | **Held** | The changelog reword removes the cause from the title only. The same unproven cause stays in production Javadoc at `DirectUpdateHandler2.java` lines 948-949: "An interrupt (for example the core closing during a reload while an autocommit waits)". Audit finding 1 says nothing in the branch shows the interrupter. Owner: reword the Javadoc on the branch, then re-verify. The round-1 draft is unchanged. |

## Held items

- **SOLR-16673:** narrow the changelog title on the branch, or accept it with the Limits note. Then release the scratchpad draft.
- **SOLR-12705:** append the settling note to the audit, or draft without that sentence.
- **SOLR-7022:** reword the Javadoc at `DirectUpdateHandler2.java` lines 948-949 on the branch. Then re-verify and update the draft's head line and changelog citation.

## Owner decisions

1. SOLR-16673: narrow the changelog title, or accept the Limits note.
2. SOLR-12705: append the settling note to the audit, or draft without that sentence. Add the Limits lines for audit findings 1 and 4.
3. SOLR-7022: reword the Javadoc at `DirectUpdateHandler2.java` lines 948-949 on the branch.
4. SOLR-5754: correct the material's "byte for byte" wording to name the gated pair, `7fbe0128d8b0` with `f8d4bdbea518`. Confirm the root-note deletion needs no gate of its own. SOLR-5939: name the classes in the 26-test combined run, if known.
5. SOLR-5941: give the gate date, if one exists. Decide whether to re-run the five round-30 autocommit classes at this head, or accept the disclosed gap.
6. SOLR-4841: keep or drop the Choice section (public or package-private constructor).
7. SOLR-6065: 500 or 400. The material says "per the recorded decision", but TESTING.md has none. Confirm whether one exists.
8. SOLR-5065: the suggester pin test needs a fail-before run per TESTING.md. Confirm it is covered, or name the run.
9. SOLR-7504: confirm that null counts as 0 and duplicates count, or move them into a Choice.
10. SOLR-6045: confirm the pre-fix commit `e06aa7624853`. Decide whether mixed input and JSON arrays get tests or stay in Limits. Confirm the single-child-document behavior change is in scope.
11. SOLR-14718: accept the green-gate wording without counts.
12. SOLR-13265: give the posting go-ahead. Add a spotless or tidy record if wanted.
13. Receipt dates: the material gives no verification dates, and the drafts say "verified at this head" without one. Add the dates if they exist.

## Outside this round

- SOLR-16655 is out of scope and was not touched. Its live fork tip is now `b201a57fb3e5`. Round 1 recorded `aa7898d972a7`, so the round-1 row is stale. The 2026-10-09 decision in `TESTING.md` gates the descent, and a fix lane is running. That will move the head again, so the round-1 draft will need a new round.

## Checks

- Heads: all 15 live tips match the material, checked with `git ls-remote`.
- Citations link to the full head SHA. Each subagent checked its line ranges against that head with `git show`. The lead re-checked the SOLR-5754 merge trees, the SOLR-6045 commit order, the SOLR-7022 Javadoc, and the SOLR-16356 ecj-lint claim.
- Drafts: no em dashes (U+2014), no PR numbers, no trailers, and no internal status comments. The 13265 draft's status comment was removed and is recorded above.
- Changelog fragments were checked by reading. This environment has no YAML parser.
- Length: SOLR-5939 (about 6.7 KB with links), SOLR-5941 (about 6.2 KB), SOLR-7504 (about 4.1 KB), and SOLR-6045 (about 4 KB) exceed the 3.5 KB guide. The changes are multi-part. The owner may trim.
- Not verified here: gate logs, receipts, the main-side ledger, and CI runs. All are main-side records and were not re-run.
