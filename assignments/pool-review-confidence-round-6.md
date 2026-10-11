# Pool assignment: review confidence round 6 (confirmation pass over the bodies fixed from rounds 4 and 5)

Capability tags: `review`. Intended host: windows. Staffing: 2. Claim path: claims/pool-review-confidence-round-6.md (slice-level claims by PR group are fine).

Purpose: confirm that the fixes rounds 4 and 5 specified actually landed, and that each fixed body is now consistent with the gate evidence in `receipts/` at the branch's live head. On 2026-10-11 the main side applied the rounds 4 and 5 replacement texts to 19 live PR bodies on apache/solr and verified each returned body against the intended text. This pass is the independent confirmation: it re-reads the bodies as they now stand and gives a closing verdict per body. It does not reopen the code review, rerun tests, or re-litigate findings the earlier rounds already closed.

## Scope: 19 live bodies

The round 4 set (14 bodies; SOLR-18391 counts twice, once for each body):

- SOLR-18178.
- SOLR-18391, the open PR body.
- SOLR-18391, the superseded draft body. It stays a draft and stays superseded; judge its fidelity only, and do not recommend reopening or updating it as the live submission.
- SOLR-18129.
- SOLR-16130.
- SOLR-18482.
- SOLR-12849.
- SOLR-13568.
- SOLR-13706.
- SOLR-13097. This PR is CONFLICTING and has changes requested; body fidelity still applies, and a fidelity verdict says nothing about readiness.
- SOLR-18505.
- SOLR-18506.
- SOLR-18119.
- SOLR-18523.

The round 5 set (5 bodies; SOLR-4540 and SOLR-16630 were CONSISTENT in round 5, were not edited, and are out of scope):

- SOLR-18530 (flaky fix, test-only change, no changelog fragment; do not flag the missing fragment).
- SOLR-18532 (flaky fix, production fix in RestoreCore rollback).
- SOLR-18531 (flaky fix, JettySolrRunner port reservation).
- SOLR-3704 (Highlighting).
- SOLR-2681 (Highlighting).

## Method for every body

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft, a report excerpt, or the fix lane's notes.
2. Read the body's verdict section in `reports/review-confidence-round-4.md` or `reports/review-confidence-round-5.md`, including the part files (`reports/review-confidence-round-4-*.md`, `reports/review-confidence-round-5-*.md`). For every DRIFT item the round specified a replacement for, check the live body: the replacement text is present as specified, and the drifted text it replaced is gone. Quote any item where the landed text differs from the specified replacement, in either direction.
3. Resolve the head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/<ticket>.md` and identify the head it verifies. If the live tip, the receipt head, and the body do not line up, report the exact mismatch before judging individual claims. A body can legitimately sit one docs-only or changelog-only commit past the gated head where the receipt records that delta; judge the delta against the receipt's own record.
4. Check the whole body, not only the fixed spots: every test count and total, every pass, failure, skip, and run claim, every verification date, every statement about what changed, every Limits line, and every follow-up statement must be supported by the receipt at the live head or by the branch tree at that head.
5. Check every citation. A citation to code the change produces must link the PR head SHA and land on the cited lines. A citation to the pre-change symptom must link the merge-base commit, land on the cited lines, and be described in the text as the base commit. The changelog line must link the head SHA.
6. Check the body against `pr-formula.md`: the AI header, a bold one-line summary opening each section, plain language, no internal process vocabulary in public text, and no unsupported precision. If a Lucene version is mentioned, the statement must be complete for the branches it describes.
7. Give one verdict per body: SATISFIED, or STILL OPEN. For every STILL OPEN item, quote the live text, state the evidence that contradicts or fails to support it, and give the exact replacement text the main side should apply.

## Settled items that are not drift

Two items read as open in the older reports and are settled decisions. Do not flag either as drift, and do not propose replacement text for them:

- SOLR-18532's Limits promise about the SOLR-9865 rollback hunk. Nick decided the sequencing on 2026-10-10: this change lands first, and the SOLR-9865 branch drops its overlapping rollback edit when that branch is prepared for opening. The promise in the body matches the decision.
- SOLR-2681's planned follow-up pull request for the nested query forms. The Highlighting round 1 answers record the planned submission as the decision; the receipt recording no plan is an evidence record, not a competing decision. The Limits line matches the decision.

Items the fix lane skipped because the underlying records conflict (rather than because a decision settled them) are not closed by this rule. If such an item is still unresolved in the live body, list it under that body's STILL OPEN items with the conflicting records quoted.

## Deliverable and limits

Deliverable: `reports/review-confidence-round-6.md`, with verdict counts up front (how many of the 19 are SATISFIED, how many STILL OPEN), one section per body, the per-item landed-or-not result for every round 4 and 5 replacement, and the exact replacement text for every item still open.

This assignment is read-only apart from the claim and the report. Do not edit a live PR body, post a comment or review, edit a branch, edit a draft, or run a build, test, or gate. The main side applies any remaining fixes.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
