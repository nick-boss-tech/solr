# Highlighting round 1: main-side answers (2026-10-09)

Report: `reports/highlighting-round-1.md`. The owner cannot make calls now; recorded recommendations are adopted under his standing decision practice, and the two public-posture decisions stay open below.

## SOLR-2681

- Count: the receipt records HighlighterTest 36 of 36 counted from the gate's JUnit XML; the class declares 31 `@Test` methods at the head (30 at base). The gate worktree and its XML are pruned, so the count cannot be re-derived from the record. A recount run at head `4cb25b1691b` is queued on the main side (GATE PENDING in the takeover log); the draft posts only with the recounted number.
- Changelog title: narrows to the top-level `query(...)` form the code handles. Branch edit plus re-gate at packaging, the same treatment as the SOLR-6045 and SOLR-12703 changelog fixes.
- Limits: "on request" is replaced with the planned-submission sentence (done in the draft). The draft's internal note is removed; the held state lives in the report and here.

## SOLR-3704

- Limits confirmed as the reviewers derived them from the diff: the docValues-only Date branch is fixed by the same reasoning and is not separately discriminated, and date unique keys are untested. That matches the main-side ledger review (the stored path is the observed one; the docValues branch was not separately discriminated).

## SOLR-4540

- OPEN line filled from the run log on the main side: on base code the new test fails (premise run, seed 4540C0FFEE4540, log g4540-premise.log, FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped FAILED at test line 106). Done in the draft.
- Changelog title: the slowdown claim has no timing anywhere in the record. The title is corrected at packaging to state the behavior change (fields absent from a document are skipped), with a re-gate, the same treatment as SOLR-2681's title.

## SOLR-2632 (owner decision, held)

Options (a) test-only pin PR, (b) Jira comment and no PR, (c) bank as received. Recommendation on record: (a) or (b). This one is a public-posture call and stays open for the owner.

## SOLR-16885

- The premise run is funded on the main side (queued, GATE PENDING in the takeover log): base production plus the branch test file, expecting the IndexOutOfBoundsException the branch note claims. Whether a Solr-side change is wanted at all stays open until that result is in; the branch's TESTING.md does not ship either way.
