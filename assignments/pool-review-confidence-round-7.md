# Pool assignment: review confidence round 7 (confirmation pass over the round 6 settlement)

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-review-confidence-round-7.md.

Purpose: confirm that the round 6 settlement actually landed, and give a closing verdict per body. Round 6 (`reports/review-confidence-round-6.md`) returned 15 STILL OPEN over the bodies fixed from rounds 4 and 5. The main side has now settled every one of those 15: the decisions and the per-body record are in `reports/review-confidence-round-6-response.md`. On 2026-10-11 the main side applied the settlement's body edits through the GitHub API (13 bodies) and verified each returned body against the intended text; two bodies were settled with no body change, for the reasons the response records. This pass re-reads the bodies as they now stand. It does not reopen the code review, rerun tests, or re-litigate findings the earlier rounds already closed.

## The two decisions to judge under

- **C1, date convention (decided):** body verification dates are the author's local dates (America/Denver, MDT). A head timestamped 2026-10-07 UTC is 2026-10-06 MDT, and the bodies record the local date. The C1 items closed with no body change. Judge the affected bodies (SOLR-18178, SOLR-18129, SOLR-16130, SOLR-12849, SOLR-13568, SOLR-13706) under this convention; do not re-flag the dates as drift.
- **C2, fork runs (decided):** a fork run is cited "at this head" only when the run executed that head; otherwise the body names the CI commit the run executed. The restatements are in SOLR-18129 (items 4b, 4c), SOLR-16130 (5a) and SOLR-13706 (9b). Confirm the restated text is present and accurate against the CI commits the round 6 report named.

## Scope: the 15 settled bodies

- SOLR-18178 (PR #4968): item 1a deletion landed; 1b closed by C1.
- SOLR-18391 (PR #5027): item 2a landed with the recovered date 2026-10-10.
- SOLR-18129 (PR #5000): items 4a to 4d landed; 4e closed by C1.
- SOLR-16130 (PR #5004): item 5a landed; 5b closed by C1.
- SOLR-18482 (PR #5009): items 6a (first form) and 6b landed; the record conflict is resolved in the response and clarified in `receipts/SOLR-18482.md`.
- SOLR-12849 (PR #5011): items 7a to 7c landed; 7d closed by C1.
- SOLR-13568 (PR #5014): no body change (C1). One explicit ruling is asked of this round: the "What happens today" claim "the cached entries are rarely hit" has no hit-rate record behind it. Rule on it: keep as written, soften, or strike, with the reason. That ruling is this body's only open question.
- SOLR-13706 (PR #5015): items 9a and 9b landed; 9c closed by C1.
- SOLR-13097 (PR #5016): item 10a landed. This PR is CONFLICTING and has changes requested; a fidelity verdict says nothing about readiness.
- SOLR-18505 (PR #5028): items 1 and 2 landed.
- SOLR-18506 (PR #5029): the appended title-conflict sentence landed at the end of "What happens today"; the rest of the body is unchanged.
- SOLR-18119 (PR #5061): items a, b and c landed (three citations re-pointed to the base commit `9becf6c6a154` with "(base commit)" link text).
- SOLR-18523 (PR #5062): item 1 landed (twelve citations re-pointed to the base commit `9becf6c6a154` with "(base commit)" link text; `documentation.gradle` and the changelog link keep the head SHA).
- SOLR-18531 (PR #5101): no body change; the CI sentence is now backed by the run record added to `receipts/SOLR-18531.md` (run 37991927523). Judge the body against the updated receipt, including the corrected Change bullet wording.
- SOLR-3704 (PR #5103): item b landed (five citations re-pointed to the merge-base `cabedd1d968` with "(base commit)" link text). Item a (the changelog fragment title on the branch) is out of scope for this pass: it is a branch edit plus re-gate, recorded in the settlement for opening-time packaging, and its absence is not a body finding.

The four bodies round 6 returned SATISFIED (SOLR-18391 draft PR #4997, SOLR-18530 PR #5098, SOLR-18532 PR #5100, SOLR-2681 PR #5104) are out of scope and are not re-judged.

## Method for every body

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft, a report excerpt, or the settlement's notes.
2. Read the body's section in `reports/review-confidence-round-6.md` and its settlement entry in `reports/review-confidence-round-6-response.md`. For every applied item, check the live body: the replacement text is present as specified, and the text it replaced is gone. Quote any item where the landed text differs from the specified replacement, in either direction.
3. Resolve the head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/<ticket>.md` and identify the head it verifies. Judge each body's claims against the receipt at the live head and the branch tree at that head, as in round 6.
4. Check citations the settlement touched: re-pointed links must land on the cited lines at the base commit and carry "(base commit)" in the link text; links the settlement kept at the head SHA must land on the cited lines at the head.

## Deliverable

`reports/review-confidence-round-7.md`: one section per body in scope, verdict SATISFIED or STILL OPEN per body, with any remaining item quoted against the live text and the record it conflicts with. Read-only: no body edits, no comments, no branch or draft edits, no builds or gates. The main side applies anything this round specifies.
