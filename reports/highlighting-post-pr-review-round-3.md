# Highlighting post-PR review round 3: confirmation

Assignment: `assignments/pool-highlighting-post-pr-review-round-3.md`. Claim: `claims/pool-highlighting-post-pr-review-round-3.md`. Lead: the windows review agent. Part reports: `reports/highlighting-post-pr-review-round-3-s1.md` (SOLR-3704, PR #5103), `-s2.md` (SOLR-2681, PR #5104) and `-s3.md` (SOLR-4540, PR #5105).

All three heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-3704 | #5103 (draft) | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` | STILL OPEN: 1 item |
| 2, SOLR-2681 | #5104 (draft) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | STILL OPEN: 1 item |
| 3, SOLR-4540 | #5105 (draft) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | STILL OPEN: 1 item |

The main side marks the three PRs ready only when all three slices are SATISFIED. None is yet.

## Slice 1: SOLR-3704 (PR #5103)

Satisfied: the round 2 fetch-condition item (the body and the answers file's correction), the symptom citations at the merge-base `cabedd1d968059215188f4e7563fb303241899ed`, which the text labels as the base commit, and the Extra runs verification dates. The Proof numbers, the title, the Limits and the body-versus-draft check all hold.

Still open:

1. **The changelog line (body line 38).** "Changelog:" is a bare code span, not a link to the head SHA. The fix is to link it at `de63d4e5d5d1ddde0da6a100a631e254c078bd55`. See the decision note below.

## Slice 2: SOLR-2681 (PR #5104)

Satisfied: the changelog link (the file is at the head and the link points there), the Proof numbers and dates, the Limits line on the nested form, the citations, the title, and the body-versus-draft check.

Still open:

1. **Limits, line 33 (summary and draft).** "A follow-up submission is planned for the nested forms." The word "submission" is banned as a workspace word in public text. `claims/query-parsing-round-1.md` line 47 names it. Fix, in the live body and in `pr-drafts/highlighting/SOLR-2681.md` together: "A follow-up pull request is planned for the nested forms."

## Slice 3: SOLR-4540 (PR #5105)

Satisfied: the symptom links at the base commit (`cabedd1d968059215188f4e7563fb303241899ed`), with the text saying "At the base commit"; the fix citations at the head, which contain the new code; the Proof numbers (3 of 3, verified 2026-10-09, the fork CI run at commit `3543d10`, and the line 106 claim) against the receipt; and the body-versus-draft check.

Still open:

1. **The changelog line is bare code.** It needs a link to the head SHA, the same as SOLR-2681's changelog line now has. Fix, in the live body and in `pr-drafts/highlighting/SOLR-4540.md`.

## Decision for Nick: the changelog line

Two of the three verdicts above turn on whether the changelog reference must be a link. The 2026-10-08 presentation rule in `pr-formula.md` says file citations are links, and SOLR-2681's changelog was linked during round 2. But the approved template in `pr-formula.md` shows the changelog line as code (`changelog/unreleased/SOLR-<ticket>.yml` in backticks). The two rules disagree. Nick should decide which governs, and then the same answer applies to SOLR-3704 and SOLR-4540. If the template governs, both changelog items are withdrawn.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on all three PRs is the same: the labeler check has run (success), and the upstream checks are `action_required` and have not run. That is a state, not a code finding.
