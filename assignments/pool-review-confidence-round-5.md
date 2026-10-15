# Pool assignment: review confidence round 5 (the newest ready PR bodies, as they now stand)

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 2. Claim path: claims/pool-review-confidence-round-5.md (slice-level claims by PR group are fine).

Purpose: a fresh independent fidelity pass over the seven newest PRs on apache/solr that were flipped to ready after their internal review rounds closed. The earlier post-PR rounds reviewed the drafts before those flips, and several bodies changed during the fix rounds that followed. This pass verifies the bodies as they now stand, against the gate evidence in receipts/ at each branch's live head. It does not reopen the code review, rerun tests, or re-litigate findings the earlier rounds already closed.

## Scope: 7 live bodies

- SOLR-18530 (flaky fix, test-only change).
- SOLR-18532 (flaky fix, production fix in RestoreCore rollback).
- SOLR-18531 (flaky fix, JettySolrRunner port reservation).
- SOLR-3704 (Highlighting).
- SOLR-2681 (Highlighting).
- SOLR-4540 (Highlighting).
- SOLR-16630 (flaky fix, test-only change in TestCoordinatorRole).

## Method for every body

1. Read the current live body through the GitHub API. Record the PR state, title, head ref, and head SHA from that response. Do not review from a local draft or an older report excerpt; several of these bodies were edited after the rounds that reviewed them.
2. Resolve the same head ref with `git ls-remote` and record whether the fork tip matches the API head SHA. Read `receipts/<ticket>.md` and identify the head it verifies. If the live tip, the receipt head, and the body do not line up, report the exact mismatch before judging individual claims. Note that a body can legitimately sit one docs-only or changelog-only commit past the gated head where the receipt records that delta; judge the delta against the receipt's own record rather than calling it drift by default.
3. Compare the title and every factual statement in the body with the receipt at the live head and, where a statement depends on the branch contents, with the branch tree at that head. This includes every test count and total, every pass, failure, skip, and run claim, every verification date, every statement about what changed or did not change, every Limits line, and every follow-up statement (a promise that a follow-up will be submitted must match what the records actually plan).
4. Check every citation. A citation to code the change produces must link the PR head SHA and land on the cited lines. A citation to the pre-change symptom must link the merge-base commit, land on the cited lines, and be described in the text as the base commit. The changelog line must link the head SHA. Check that links resolve and that the linked code supports the sentence carrying the link.
5. Check the body against `pr-formula.md`: the AI header, a bold one-line summary opening each section, plain language, no internal process vocabulary in public text, and no unsupported precision. If a Lucene version is mentioned, the statement must be complete for the branches it describes (main and branch_10x pin one version, branch_9x pins another; a single-version claim that implies coverage of the other line is drift).
6. Give one verdict per body: CONSISTENT, or DRIFT. For every DRIFT item, quote the live text, state the receipt or branch evidence that contradicts or fails to support it, and give the exact replacement text the main side should apply. If two records conflict and no replacement can be chosen safely from the evidence, say so and flag the record conflict instead of inventing replacement text.

## Known context

- These seven bodies went through between four and nine post-PR review rounds each before their ready flips; the rounds' SATISFIED verdicts applied to the drafts as they then stood. A DRIFT finding here is not a contradiction of those verdicts where the body changed afterwards; note the change point where you can establish one.
- SOLR-18530 is test-only and carries no changelog fragment; do not flag the missing fragment as drift.
- SOLR-18531's body carries Limits lines for two recorded gaps (a failed start does not re-reserve the port; the reservation map is keyed by port and shared). Check those lines against the receipt rather than proposing stronger or weaker wording from the code alone.

## Deliverable and limits

Deliverable: `reports/review-confidence-round-5.md`, with verdict counts up front, one section per body, and the exact replacement text for every drift item.

This assignment is read-only apart from the claim and the report. Do not edit a live PR body, post a comment or review, edit a branch, edit a draft, or run a build, test, or gate. The main side applies any body fixes.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
