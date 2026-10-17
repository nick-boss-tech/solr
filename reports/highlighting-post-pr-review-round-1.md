# Highlighting post-PR review round 1

Assignment: `assignments/pool-highlighting-post-pr-review-round-1.md`. Claim: `claims/pool-highlighting-post-pr-review-round-1.md`. Lead: the windows review agent. Part reports: `reports/highlighting-post-pr-review-round-1-s1.md` (SOLR-3704, PR #5103), `-s2.md` (SOLR-2681, PR #5104) and `-s3.md` (SOLR-4540, PR #5105).

All three PRs are open as drafts, and all three heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-3704 | #5103 (draft) | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` | STILL OPEN: 3 items |
| 2, SOLR-2681 | #5104 (draft) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | STILL OPEN: 1 item |
| 3, SOLR-4540 | #5105 (draft) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | STILL OPEN: 2 items |

The main side marks the three PRs ready only when all three slices are SATISFIED. None is yet.

## Slice 1: SOLR-3704 (PR #5103)

Still open:

1. **The docValues wording is too narrow.** The body says "docValues fields that are not stored" and "docValues-only". The code is wider. `RetrieveFieldsOptimizer` substitutes stored fields with docValues where it can (`SolrDocumentFetcher.java` lines 793 to 796, the `dvsCanSubstituteStored` branch). So a stored, single-valued date field also takes the Date branch when the request's `fl` lists only such fields. The subagent gives the replacement text in `reports/highlighting-post-pr-review-round-1-s1.md`. The lead confirmed the substitution branch at the head.
2. **The Proof line has no verification date.** The receipt has no run date either. The main side records a date in `receipts/SOLR-3704.md` first, then adds it to the body and the draft.
3. **The "Extra runs" line puts two test classes on one line.** Split it so each class has its own line.

## Slice 2: SOLR-2681 (PR #5104)

Still open:

1. **Proof line.** "At head `a3b1ea7994d`, HighlighterTest passes 36 of 36." needs the verification date that pr-formula section 3 requires. Append " (verified 2026-10-09)". The receipt's re-gate and the recount are both logged 2026-10-09. Apply to the live body and to `pr-drafts/highlighting/SOLR-2681.md`.

Satisfied: the changelog title is top-level only, which matches the answers; the Limits line on the nested `query(...)` form is true of the code; the citations hold at the head; and the body matches the draft.

## Slice 3: SOLR-4540 (PR #5105)

Still open:

1. **The Proof has no pass count.** Add "FastVectorHighlighterTest 3 of 3 pass with this change (verified 2026-10-09 at this head)" after the main side confirms that date in the receipt. Apply to the live body and to `pr-drafts/highlighting/SOLR-4540.md`.
2. **"Passes at this head" is not what the receipt records.** The Proof says a CI run passed at this head. The receipt's run was on fork commit `3543d10`, which has the same Java code but is an earlier commit. Restate it as a run at an earlier commit, or drop the sentence.

Satisfied: the citations, the title, the Limits, and the claim about the test at line 106 (fails on the base code).

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on all three PRs is the same: the labeler check has run (success), and the upstream checks are `action_required` and have not run. That is a state, not a code finding.
