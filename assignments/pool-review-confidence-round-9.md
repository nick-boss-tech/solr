# Pool assignment: review confidence round 9 (confirmation pass over SOLR-13568)

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-review-confidence-round-9.md.

Purpose: confirm that the round 8 settlement actually landed, and give a closing verdict on the last open body in this set. Round 8 (`reports/review-confidence-round-8.md`) returned 2 SATISFIED (SOLR-18178, SOLR-18505) and 1 STILL OPEN: SOLR-13568 (PR #5014), where the "rarely hit" ruling was FURTHER SOFTEN with one exact replacement. The main side has now applied that replacement; the record is in `reports/review-confidence-round-8-response.md`. On 2026-10-11 the main side applied the body edit through the GitHub API and verified the returned body against the intended text. This pass re-reads that one body as it now stands. It does not reopen the code review, rerun tests, or re-litigate findings the earlier rounds already closed. On SATISFIED, the 19-body set that rounds 4 to 9 covered is fully settled.

Judge under the two decisions already recorded (round 6 response): C1 (body verification dates are the author's local MDT dates; do not re-flag dates as drift) and C2 (a fork run is cited "at this head" only when the run executed that head; otherwise the body names the CI commit). Neither is expected to bite on this body.

## Scope: SOLR-13568 (PR #5014) only

Head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721` (branch `solr-13568-submit`), open, not a draft.

Main-side annotation, 2026-10-11: the branch-side comment fix named in the out-of-scope paragraph below is DONE, and the head has moved. New head: `aa5e81b8993769701f7e969f2344eaa58a6626d6`. The commit changes one comment line only (ExpandComponent.java line 437: "so this filter is rarely reused" becomes "so no other page can reuse it"); production code is otherwise identical to `ac5d60c214cf`, so the code reading this pass relies on (lines 840 to 897) is unchanged. The new head is gated GREEN (log g13568-comment-gate.log: changelog YAML parse, tidy clean, Error Prone compile, TestExpandComponent 9 of 9 from fresh JUnit XML, module check; no pre-fix proof leg, because the delta is comment-only) and `receipts/SOLR-13568.md` is refreshed to it. Judge the body against the new head `aa5e81b899`; where the scope text below names `ac5d60c214cf` as the head, read the new head instead. The body's only blob citation is the changelog file at the old SHA, which this commit does not change, and no body citation points into ExpandComponent.java (verified against the live body on 2026-10-11), so no citation is affected by the move.

- The exact replacement, in "A choice to check": "Only a repeat of the same page could hit, and this change does not measure how often that happens." is now "Only a later request whose page holds the same groups could hit, and this change does not measure how often that happens." Confirm the new sentence is present verbatim, the old sentence is gone, and the claim matches the code at the head (the group filter is a `TermInSetQuery` over the page's group values, `ExpandComponent.java` lines 840 to 897, whose equality is over the term set) and the receipt (`receipts/SOLR-13568.md`, which records the premise run and no hit-rate record).
- Confirm the surrounding text is as round 8 left it: the sentence before the replacement ("Moving to another page never reuses these entries, because each page's group query is built from that page's own groups.") and the switch sentence after it are unchanged, and no unmeasured frequency word ("rarely" or the like) appears anywhere in the body.
- Confirm the Limits text is unchanged from the state round 8 accepted, including "...paid in exchange for the filter cache no longer filling with per-page group queries while paging" and the "same page of the same query, requested again by any user" example.
- Round 8 item 3 (the optional tighter scoping "entries that no other page of those results can reuse" in "What happens today") was marked optional and not required for the ruling, and the settlement did not apply it. Confirm the body reads accurately as it stands, or state why the optional wording is in fact required.

Out of scope: the branch-side comment round 8 flagged at `ExpandComponent.java` line 437 (the "rarely reused" wording in the code comment). It is recorded in the settlement as a main-side decision, since fixing it moves the head and requires the gate again. Do not judge the body on it.

## Method

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft, a report excerpt, or the settlement's notes.
2. Read the SOLR-13568 section of `reports/review-confidence-round-8.md` and the settlement in `reports/review-confidence-round-8-response.md`. Check the live body: the replacement text is present as specified, and the text it replaced is gone. Quote any place where the landed text differs from the specified replacement, in either direction.
3. Resolve the head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/SOLR-13568.md` and identify the head it verifies.
4. Readiness is not judged in this pass; a fidelity verdict says nothing about it.

## Deliverable

`reports/review-confidence-round-9.md`, verdict first: SATISFIED or STILL OPEN for SOLR-13568, with, for any item that remains open, the exact passage and the evidence it disagrees with. If the verdict is SATISFIED, state plainly that the 19-body set covered by rounds 4 to 9 is fully settled.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on bodies and branches: no body is edited, no comment posted, nothing filed in this pass. No em dashes in authored text.
