# Review re-check, round 4: changed passages from the open fixes lane

Verdict: **NOT CONVERGED as checked, with both findings handled.** The check found two findings in slice R4-A (SOLR-7022 lines 24 and 26). The coordinator corrected both in this push, against the receipt and the audit. The check also found two findings in slice R4-B (CombinedQueryComponent lines 20 and 56). Main-side settlement `345dfdfd988` had already scoped both, and the new text matches the replacements the check proposed, so they are superseded. Everything else the lane changed is CONFIRMED.

Assignment: `assignments/pool-review-recheck-round-4.md`. Diff checked: `19e916d0972..181818be5a9` for the lane's own edits, and the current draft text for the superseded CQC passages. Slices were run by two read-only subagents (R4-A and R4-B). The coordinator checked the findings that changed the draft and wrote this report. Nothing was posted, filed or built.

## Slice R4-A: update-processing drafts

- **SOLR-12705 line 17 ("Three existing processors"): CONFIRMED.** The bullet names IgnoreFieldUpdateProcessorFactory, ConcatFieldUpdateProcessorFactory and FieldValueSubsetUpdateProcessorFactory, which are the three.
- **SOLR-7022 line 24 ("the formatting check", and the run clause): FINDING, corrected.** The run clause "Run at `6a233ab2fdb` (2026-10-06) ... DirectUpdateHandler2CommitWaitTest 1 of 1" is not in the record. `receipts/SOLR-7022.md` line 5 records the original gate green on 2026-10-05 at `a2ed957063b`, with DirectUpdateHandler2AwaitSearcherTest 2 of 2 and DirectUpdateHandlerTest 7 of 7 and no CommitWaitTest. Line 7 says the top-up did not run CommitWaitTest. The owner's ruling in `reports/review-28-round-2-g4.md` line 63 dropped the 2026-10-06 counts.
- **SOLR-7022 line 26, last sentence: FINDING, corrected.** "fails 1 of 1 ... (2026-10-06)" reports a run no readable record holds. `audits/update-processing/SOLR-7022.md` finding 3 says "read against the base code; not run."
- **SOLR-14718 line 24: CONFIRMED.** The receipt (lines 4, 5, 7) gives head `29c09959791a`, 2026-10-07, SolrCmdDistributorTest 1 of 1. The head has one `@Test`.
- **SOLR-16673 line 23: CONFIRMED.** The receipt gives head `d5c19e64ba1b`, 2026-10-04, and "pre-fix proof PASS" with no failure count, so "the observed failure is not recorded" holds. The changelog-parse clause is gone.
- **SOLR-3657 line 25: CONFIRMED.** `git merge-base 14edaca577c0 upstream/main` is `97d973814336`. The receipt's 17 tests with 2 failures match.
- **SOLR-3657 bold line 22, against line 25 (finding bar (b)): CONFIRMED, no contradiction.** Line 25 adds the test-file overlay the bold line omits. That is wording, not a conflict.
- **SOLR-5065 line 25: CONFIRMED.** `DefaultSchemaSuggesterTest.java` does not exist at `0cc328310f8f`. The class exists at `ab894a996c8a`.
- **SOLR-5941 line 35 (bold): CONFIRMED** against the round 3 replacement.
- **SOLR-5941 line 38: CONFIRMED.** `CommitTracker.java` line 280 at `cabedd1d968` reads `core.getUpdateHandler().commit(command);`.
- **SOLR-5941 Limits bold line 45 ("three SolrCloud commit paths"): CONFIRMED.** The Proof names three SolrCloud classes: CommitThroughNonLeaderTest, ParallelCommitExecutionTest and HttpPartitionOnCommitTest. AutoCommitUpdateChainTest is the standalone one.
- **SOLR-6973 line 7 (bold): CONFIRMED** and consistent with line 15.
- **SOLR-6973 line 9 (the loop link): CONFIRMED.** `SignatureUpdateProcessorFactory.java` at `cabedd1d968` holds the loop at 169 to 178.
- **SOLR-6973 line 23: CONFIRMED.** The receipt gives gated head `fa5b59ba07b4`, 2026-10-08, 7 tests with 1 failure. At base, lines 199 to 203 set `signatureField` and `updateTerm`.
- **SOLR-6973 line 25 (the deleted clause): CONFIRMED.** No "module" wording remains.
- **SOLR-6973 Limits line 37: CONFIRMED.** The test asserts on the processAdd command and indexes nothing.
- **SOLR-7504 line 25: CONFIRMED.** The receipt (line 7) gives 1 failure in 30 at `22b77196e662`.
- **SOLR-7504 line 27: CONFIRMED.** The receipt (line 6) says the formatting check leaves a clean tree.
- **SOLR-7504 line 38: CONFIRMED.** The receipt (line 8) gives the combined run at `22b77196e662` on 2026-10-09, before the null fix. No other line names a different combined-run head.

Not checked: the `g7022-*.log` files, which are not in this worktree. The audit's CI run 37558668356 is at head `7940e98b0ee0`, not the cited head.

## Slice R4-B: the other drafts and material

- **SOLR-18368 line 1: CONFIRMED.** The lead-in is "decides under which key the PR opens". Checked again in `reports/draft-fidelity-confirmation-round-3.md`, which reads the receipt directly.
- **SOLR-9396 lines 26 and 28: CONFIRMED.** Also checked in fidelity round 3, against the head pattern at line 307 of `SubQueryAugmenterFactory.java` at `a5ab2eda4e6`.
- **SOLR-9396 Limits lines 34 and 35 against lines 26 and 28 (finding bar (b)): CONFIRMED, no contradiction.** See Q1.
- **gradlew line 5: CONFIRMED.** No run date is claimed. The other 2026-10-11 dates are a rule date and a Jira search date.
- **gradlew line 51: CONFIRMED.** The loop matches the traces and the `d76fc14b9857` lines 222 to 242. The subagent noted that after the fix the numbers shift by three lines, and the draft cites the unfixed snapshot in line 48. Not a finding.
- **CQC line 20, sentence one: FINDING, superseded.** It said the real exit folds neither count with no scoping. Main-side settlement `345dfdfd988` scopes it to the index-0, unmarshalling shape and names the unsettled shapes. The subagent's proposed replacement matches the new text in substance.
- **CQC line 20, sentence two: CONFIRMED.** Line 386 adds the header value inside the block at 381 to 387, with no per-query guard.
- **CQC line 48 ("whose question is posed in Fix options"): CONFIRMED.**
- **CQC line 54 (the causal clause removed): CONFIRMED.** Not present in the current draft.
- **CQC line 56: FINDING, superseded.** The paragraph needed scoping and the fold and skip sentences needed their minors. `345dfdfd988` applies all three: the scoped shape, the unsettled shapes, the `needsUnmarshalling` skip condition, and the folds limited to merges where a shard sends a per-query entry. The code lines match: the skip at 399, the fold guard at 338.

## Answers requested by the lane

- **Q1, composed SOLR-9396 Choice wording:** accurate to the code and supported by the records. At `a5ab2eda4e6`, line 307 compiles `\$\{?row\.`, which matches both spellings, and the scan at line 337 reads only the subquery's own parameters. The wording does not contradict Limits bullets 1 or 2. It depends on the `\{?` branch staying in the pattern. If the owner narrows the pattern (the s10 follow-up), lines 26, 28 and Limits bullets 1 and 2 must revert together.
- **Q2, second label on CQC line 20:** needed, not a duplicate. The two sentences make different real-exit claims, and the first label does not reach the second. The lane report suggested dropping it; that suggestion was wrong, and the lane report now says so.

## Corrections made in this push

1. **`pr-drafts/update-processing/SOLR-7022.md` lines 24 and 26.** The draft asserted a 2026-10-06 CommitWaitTest run that no record holds. The round 3 replacement text carried that claim forward, and the lane applied it faithfully, so the error is the lane's to fix. Line 24 now cites the receipted 2026-10-05 gate at `a2ed957063b`. Line 26 now says the CommitWaitTest result is "read to fail ... this was not run." **For the main side to confirm.** The round 3 record's replacement text should be corrected to match.
2. **`reports/review-open-fixes-lane-round-1.md`.** The SOLR-7022 entries and the CQC line 20 suggestion are corrected to point here.

## Relation to other records

- Fidelity round 3 (`reports/draft-fidelity-confirmation-round-3.md`) covers SOLR-18368 and SOLR-9396 from the receipt, with the same result.
- The CQC items are checked against the settled text in `reports/combinedquerycomponent-ticket-review-round3.md`. That round is STILL OPEN on its own two findings.
- The gradlew items are checked in `reports/gradlew-ticket-review-round4.md`, which is SATISFIED.

## Open for the main side

- Confirm the SOLR-7022 correction and fix the round 3 record's replacement text.
- SOLR-12705 response line 33 names the wrong head for the combined run (the round 3 record's own item).
- Receipts for SOLR-11483, 16655, 3657 and 5065 still need refreshing, as the round 3 record asked.
