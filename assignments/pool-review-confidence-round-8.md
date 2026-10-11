# Pool assignment: review confidence round 8 (confirmation pass over the round 7 settlement)

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-review-confidence-round-8.md.

Purpose: confirm that the round 7 settlement actually landed, and give a closing verdict per body. Round 7 (`reports/review-confidence-round-7.md`) returned 12 SATISFIED and 3 STILL OPEN over the bodies settled from round 6. The main side has now settled all three open items; the record is in `reports/review-confidence-round-7-response.md`. On 2026-10-11 the main side applied the settlement's body edits through the GitHub API and verified each returned body against the intended text. This pass re-reads the three bodies as they now stand. It does not reopen the code review, rerun tests, or re-litigate findings the earlier rounds already closed. The 12 bodies round 7 returned SATISFIED are out of scope and are not re-judged.

Judge under the two decisions already recorded (round 6 response): C1 (body verification dates are the author's local MDT dates; do not re-flag dates as drift) and C2 (a fork run is cited "at this head" only when the run executed that head; otherwise the body names the CI commit).

## Scope: the 3 settled bodies

- SOLR-18178 (PR #4968), head `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`: the `DownloadConfigSetAPITest` 5 of 5 sentence was taken out of the GitHub Actions runner bullet in Proof and now stands as its own bullet: "- A separate run on 2026-10-10, not on the GitHub runner, passed `DownloadConfigSetAPITest` 5 of 5 at head `b75e7d4d3c4`, covering archives containing subdirectories." Confirm the sentence is gone from the runner bullet, the new bullet is present as written, and the claim matches `receipts/SOLR-18178.md` (the 2026-10-10 run was the evidence restoration run on vm1).
- SOLR-13568 (PR #5014), head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`: round 7 ruled SOFTEN on the unmeasured frequency claims and the settlement applied its two specified replacements verbatim (the "no other page can reuse" sentence in "What happens today"; the rewritten "Always-off is the simpler rule..." passage in "A choice to check"). Confirm both are present as specified and the replaced text is gone. The settlement also changed one Limits word in main-side wording, not round-specified text: "the filter cache no longer churning while paging" is now "the filter cache no longer filling with per-page group queries while paging". Judge that sentence on its own merits against the receipt and the premise run.
- SOLR-18505 (PR #5028), head `e28739b4069d`: the "Update 2026-10-06" paragraph in Proof now reads "...a GitHub Actions run of the same class on the fork also passed on this code (run 37577283523, CI commit `71a978d5982`, which adds only the workflow file)." Confirm the replacement is present, the "at the same head" wording is gone, and the citation matches the run's executed commit. The settlement also corrected `receipts/SOLR-18505.md` to name CI commit `71a978d5982` as the executed commit; confirm the receipt now says so.

## The explicit ruling this round owes: SOLR-13568 "rarely hit"

Round 6 recorded the observation that the body's frequency claims ("the cached entries are rarely hit", "rarely reused") had no hit-rate record behind them, without ruling. Round 7 ruled SOFTEN and the settlement applied the softening. This round gives the final ruling on the question as the body now stands: whether any unmeasured frequency claim remains in the SOLR-13568 body (including the Limits sentence the settlement reworded), and whether the softened text is accurate against the code at the head and the receipt. Rule: closed as softened, further soften (with the exact replacement), or strike (with the exact deletion). That ruling is part of this body's verdict, not a side note.

## Method for every body

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft, a report excerpt, or the settlement's notes.
2. Read the body's section in `reports/review-confidence-round-7.md` and its settlement entry in `reports/review-confidence-round-7-response.md`. For every applied item, check the live body: the replacement text is present as specified, and the text it replaced is gone. Quote any item where the landed text differs from the specified replacement, in either direction.
3. Resolve the head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/<ticket>.md` and identify the head it verifies. Judge each body's claims against the receipt at the live head and the branch tree at that head, as in round 7.
4. Readiness is not judged anywhere in this pass; a fidelity verdict says nothing about it.

## Deliverable

`reports/review-confidence-round-8.md`, verdict first: counts, then one section per body with SATISFIED or STILL OPEN and, for any item that remains open, the exact passage and the evidence it disagrees with. The SOLR-13568 section carries the explicit "rarely hit" ruling described above.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on bodies and branches: no body is edited, no comment posted, nothing filed in this pass. No em dashes in authored text.
