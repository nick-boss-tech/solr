# Highlighting post-PR review round 2: confirmation

Assignment: `assignments/pool-highlighting-post-pr-review-round-2.md`. Claim: `claims/pool-highlighting-post-pr-review-round-2.md`. Lead: the windows review agent. Part reports: `reports/highlighting-post-pr-review-round-2-s1.md` (SOLR-3704, PR #5103), `-s2.md` (SOLR-2681, PR #5104) and `-s3.md` (SOLR-4540, PR #5105).

All three heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-3704 | #5103 (draft) | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` | STILL OPEN: 3 items |
| 2, SOLR-2681 | #5104 (draft) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | STILL OPEN: 1 item |
| 3, SOLR-4540 | #5105 (draft) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | STILL OPEN: 1 item |

The main side marks the three PRs ready only when all three slices are SATISFIED. None is yet.

## Slice 1: SOLR-3704 (PR #5103)

Satisfied: the round 1 narrow docValues wording is gone, the Proof verification date matches the receipt, the "Extra runs" split is in place, the Proof numbers match the receipt, and the title, Limits and body-versus-draft check all hold.

Still open:

1. **Bullet 2 uses the wrong input.** The bullet reads "That includes a stored, single-valued date field when the request's `fl` names only such fields". The highlighter does not fetch the request's `fl`. It fetches only the highlighted fields, the alternate field and the unique key (`DefaultSolrHighlighter.java` lines 474 to 483 and 496: `returnFields` is built from `preFetchFieldNames`, plus the key). The lead checked those lines at the head. The replacement text is in `reports/highlighting-post-pr-review-round-2-s1.md`, section 9. The answers file's 2026-10-10 correction carries the same condition, so it needs the same fix.
2. **Symptom citations link at the head.** The links to `FieldType.java` (lines 405 to 413) and `DatePointField.java` (lines 197 to 198) point at the head. Those lines describe the symptom, which is base behaviour. They should link at the merge-base `cabedd1d968059215188f4e7563fb303241899ed` (`DatePointField.java` lines 192 to 193), and say so. See the tension note below.
3. **"Extra runs" lines lack a verification date.** The two lines need the date that pr-formula section 3 requires. The receipt supports 2026-10-06.

## Slice 2: SOLR-2681 (PR #5104)

Satisfied: the round 1 item. The Proof now reads "At head `a3b1ea7994d`, HighlighterTest passes 36 of 36 (verified 2026-10-09)", which matches the receipt's gate-log date. The fork tip equals the PR head, and the body matches the draft. The code and test citations hold at the head.

Still open:

1. **The changelog line is bare code.** Line 36 of the live body gives the changelog file as backticks, not a link. The presentation rule requires file citations to be links. Fix, in the live body and in `pr-drafts/highlighting/SOLR-2681.md`: link it to `https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml`.

## Slice 3: SOLR-4540 (PR #5105)

Satisfied: round 1 item 1 (the Proof names "FastVectorHighlighterTest 3 of 3 pass with this change", with the verification date 2026-10-09, which matches the receipt's count and date) and round 1 item 2 (the fork CI sentence now names run `37622803991` at commit `3543d10`, success, 2026-10-07, with the same Java code, which matches the receipt and the live run).

Still open:

1. **Symptom links point at the head.** The links to `DefaultSolrHighlighter.java` line 641 (and lines 415 to 421) point at head `180b6e8a7d3c23ac308a28d547f61816b5815f04`. At the head, lines 636 to 640 are the new guard above line 641, so the sentence "for each field name ... first" holds only at the base. Fix, in the live body and in `pr-drafts/highlighting/SOLR-4540.md`: link both to the base `cabedd1d968059215188f4e7563fb303241899ed` (lines 636 and 415 to 421) and say "at the base commit".

## Tension between the formula and the assignment

pr-formula.md says every citation links the head SHA. The highlighting assignment says symptom citations point at base or merge-base code and fix citations at the head. The three items above that move symptom links to base follow the assignment. Nick should decide which rule governs symptom citations in this category. The same question is open for the flaky-fix PRs (see `reports/flaky-fix-post-pr-review-round-1.md`).

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on all three PRs is the same: the labeler check has run (success), and the upstream checks are `action_required` and have not run. That is a state, not a code finding.
