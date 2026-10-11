# Pool assignment: review confidence round 4 (live PR body fidelity)

Capability tags: `review`. Intended host: windows. Staffing: 3. Claim path: claims/pool-review-confidence-round-4.md (slice-level claims by PR group are fine).

Purpose: give the named live PR bodies a dedicated fidelity pass against the current branch tips and the gate evidence in `receipts/`. Review rounds are stochastic, and this pass is limited to body fidelity: it does not reopen the code review, rerun tests, or decide any question that a PR state or receipt leaves open.

## Scope: 14 live bodies

- SOLR-18178: PR #4968.
- SOLR-18391: PR #5027, the open PR, and PR #4997, the superseded draft. Treat them as two separate bodies.
- SOLR-18129: PR #5000.
- SOLR-16130: PR #5004.
- SOLR-18482: PR #5009.
- SOLR-12849: PR #5011.
- SOLR-13568: PR #5014.
- SOLR-13706: PR #5015.
- SOLR-13097: PR #5016.
- SOLR-18505: PR #5028.
- SOLR-18506: PR #5029.
- SOLR-18119: PR #5061.
- SOLR-18523: PR #5062.

## Method for every body

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft or an older report excerpt.
2. Resolve the same head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/<ticket>.md` and identify the head or heads it verifies. If the live tip, the receipt head, and the body do not line up, report the exact mismatch before judging individual claims.
3. Compare the title and every factual statement in the body with the receipt at the live tip and, where a statement depends on the branch contents, with the branch tree at that tip. This includes every test count and total, every pass, failure, skip, and run claim, every verification date, every statement about what changed or did not change, and every Limits or Choice statement.
4. Check every citation. A citation to code the change produces must link the PR head SHA and land on the cited lines. A citation to the pre-change symptom must link the merge-base commit, land on the cited lines, and be described in the text as the base commit. The changelog line must link the head SHA. Check that links resolve and that the linked code supports the sentence carrying the link.
5. Check the body against `pr-formula.md`: the AI header, a bold one-line summary opening each section, plain language, no internal process vocabulary in public text, and no unsupported precision. If a Lucene version is mentioned, check whether the version statement is complete for the branch being described.
6. Give one verdict per body: CONSISTENT, or DRIFT. For every DRIFT item, quote the live text, state the receipt or branch evidence that contradicts or fails to support it, and give the exact replacement text the main side should apply. If two records conflict and no replacement can be chosen safely from the evidence, say so and flag the record conflict instead of inventing replacement text.

## Known special cases

- SOLR-13097, PR #5016, is CONFLICTING and has changes requested. Body fidelity still applies. Do not treat the conflict or review state as body drift, do not propose a conflict resolution under this assignment, and do not let a fidelity verdict imply that the PR is ready.
- SOLR-18391: PR #4997 is superseded by PR #5027. Compare both bodies separately against the receipt and their own live heads. Do not recommend reopening or updating the superseded draft as the live submission; the open PR is the submission under review.
- SOLR-18178 and SOLR-18482: the evidence restoration job updated both bodies on 2026-10-10. Use the live API body and the current receipt, not claim text preserved in an earlier review report.

## Deliverable and limits

Deliverable: `reports/review-confidence-round-4.md`, with verdict counts up front, one section per body, and the exact replacement text for every drift item.

This assignment is read-only apart from the claim and the report. Do not edit a live PR body, post a comment or review, edit a branch, edit a draft, or run a build, test, or gate. The main side applies any body fixes.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
