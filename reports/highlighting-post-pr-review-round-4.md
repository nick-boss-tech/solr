# Highlighting post-PR review round 4: confirmation

Assignment: `assignments/pool-highlighting-post-pr-review-round-4.md`. Claim: `claims/pool-highlighting-post-pr-review-round-4.md`. Lead: the windows review agent. Part reports: `reports/highlighting-post-pr-review-round-4-s1.md` (SOLR-3704, PR #5103), `-s2.md` (SOLR-2681, PR #5104) and `-s3.md` (SOLR-4540, PR #5105).

All three heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-3704 | #5103 (draft) | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` | SATISFIED |
| 2, SOLR-2681 | #5104 (draft) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | SATISFIED |
| 3, SOLR-4540 | #5105 (draft) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | SATISFIED |

All three slices of the Highlighting round are satisfied, so the main side can mark the three draft PRs ready for review. That is Nick's direction to the main side.

## Slice 1: SOLR-3704 (PR #5103)

Satisfied: the changelog line is a link at the head, the file exists there, and its title matches the PR title and the code. The fix citations hold at the head, and the symptom citations sit at the base commit with the text saying so. The Proof numbers and dates match the receipt, and the body matches the draft after CR stripping.

Optional notes, not blocking: the third Limits bullet does not say that the date query also runs with `hl.method=original`. The test inherited in `HighlighterWithoutStoredIdTest` was not run, so the property leak was not verified. These are in `reports/highlighting-post-pr-review-round-4-s1.md`.

## Slice 2: SOLR-2681 (PR #5104)

Satisfied: the Limits line now reads "A follow-up pull request is planned for the nested forms.", with no workspace word. The changelog link points at the head, and the file's title claims only the top-level `query(...)` form. The Proof numbers and the verification date match the receipt, and the body matches the draft.

Optional note, not blocking: the symptom link range (lines 295 to 298) could be widened to 295 to 303.

## Slice 3: SOLR-4540 (PR #5105)

Satisfied: the changelog line is a link at the head, and the file exists there with the PR title. The symptom links are at the base commit and the text says so, and the fix links hold at the head. The Proof numbers match the receipt, and the body is byte-identical to the draft.

Optional wording notes N1 to N5 are in `reports/highlighting-post-pr-review-round-4-s3.md`.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on all three PRs is the same: the labeler check has run (success), and the upstream checks are `action_required` and have not run. That is a state, not a code finding.
