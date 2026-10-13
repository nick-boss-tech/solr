# Pool assignment: strong review of the four draft PRs, round 2 (re-review after the gates)

Capability tags: `review`. Intended host: Windows. Do not start until the gate jobs below are DONE or the owner says to proceed on a subset. This assignment is self-contained for a reviewer joining cold; read `reports/four-new-prs-strong-review-round-1.md` (the canonical round 1 report) and then `reports/four-new-prs-strong-review-round-1-windows-addendum.md` (a second, independent review of the same PRs, with a table of where the two differ).

## Context

Round 1 returned SATISFIED for #5116 (SOLR-18540) and NOT SATISFIED for #5115 (SOLR-18539), #5117 (SOLR-18541) and #5118 (SOLR-18010), in both reviews. The addendum wrote the code findings as patches on three review branches and posted them as gate jobs. Wording findings were applied to the drafts in `pr-drafts/` by both reviews and to the four live PR bodies by the addendum. The two reviews disagree on one rule: whether a skipped shard that reports an exact zero clears `numFoundExact` (canonical report: no; addendum: yes). The `QueryComponent` settling run below decides it; the patch branches follow whichever the run supports.

## Inputs to wait for

| Input | Job file | Review branch |
|-------|----------|---------------|
| #5115 patch gate | `gates/SOLR-18539-review-r1-patch.md` | `review-r1-solr-18539` at `fbf7610644d178049e1110ffc0ec34dad8c5b32f` |
| #5117 patch gate | `gates/SOLR-18541-review-r1-patch.md` | `review-r1-solr-18541` at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e` |
| #5118 patch gate | `gates/SOLR-18010-review-r1-patch.md` | `review-r1-solr-18010` at `e99be67c9a6fe15f86306104843a30b35196f159` |
| Plain `QueryComponent` real-exit shape | `gates/queryComponent-real-exit-shape-premise.md` | none |
| #5118 symlink and bind-mount semantics | `gates/SOLR-18010-replace-semantics-premise.md` | none |
| #5118 fail-before value, `MultiAuthPluginTest`, copy-failure log level, bind mount (G1 to G4b) | `gates/SOLR-18010-permissions-fail-before-and-multiauth.md` (canonical report) | none, or `review-r1-solr-18010` for G3 |
| #5117 score-only exit input | `gates/SOLR-18541-score-only-exit-input.md` (canonical report) | none |

## Work

1. For each gate that returned GREEN, read the result and the receipt section, and check that the failure texts match the predictions in the job file. A gate that executed tests and failed is not relaunched; investigate and record first.
2. For the two settling runs, compare the verdicts with the round 1 reading: the zero-hit and exact shape of a shard that stopped early (#5115), and EBUSY or symlink replacement for #5118. If a verdict contradicts the reading, say so first in the report and change the patch or the body before anything else.
3. Settle the owner items in the round 1 report, as far as the owner has answered. Where he has not, leave them listed.
4. Rewrite each live PR body against the receipt of the head it will describe, only after the owner agrees to move the PR branch onto the gated patch. The passages to change are listed at the end of the round 1 report. Keep the formula in `pr-formula.md` and the voice rules: no em dashes, never "we" for the owner plus agents, no internal process words in public text, links to head SHAs for changed code and to the merge-base for pre-change behavior. Edit the PR through the REST endpoint (`gh api -X PATCH repos/apache/solr/pulls/N -F body=@file`); `gh pr edit` fails on this token's scopes. Read each body back and compare.
5. Return a verdict line per PR: SATISFIED, or NOT SATISFIED with the blocking items.

## Rules carried over

- No comments are posted. Anything that needs one is drafted in the report for the owner.
- No build on the Windows host. Doubts that need a run become a gate job naming the class, the seed and the question.
- A maintainer actively commenting on a PR means the description is left alone and Nick is asked first; check `comments` and `reviews` before every edit.
- Never mark a PR ready; that stays with the owner.

## Deliverable

`reports/four-new-prs-strong-review-round-2.md`, one section per PR with the verdict line. The loop ends when all four read SATISFIED, or when a finding is recorded as an owner item with the reason it cannot be settled.

## Owner decisions, 2026-10-13

1. SOLR-18541, multi-query `numFoundExact`: the question stays posed as the PR's Choice. Do not add the reviewer's recommended change (report `numFoundExact` false whenever more than one sub-query contributes) to any patch. The current behavior and the posed Choice stand, and the maintainers answer it.
2. SOLR-18541, the missing per-query entry crash (the Q2 `IndexOutOfBoundsException` and its family): this stays a named follow-up in the PR's Limits. No follow-up ticket is filed for it until after the PR merges.

Both rulings are recorded on the main side as examples under the decision ladder: where a genuine choice exists, the PR poses it as a Choice rather than settling it silently in code; and an item listed as a follow-up does not get its own ticket until after the PR that names it has merged.

## Settling outcome recorded 2026-10-13 (main side)

The plain `QueryComponent` real-exit shape input is in. The run (unmodified main `d76fc14b9857`, seed `C0FFEE42C0FFEE42`, verdicts in `gates/queryComponent-real-exit-shape-premise.md` and in the premise addendum at the end of `reports/four-new-prs-strong-review-round-1.md`) produced no short-circuited shape and no partial response carrying a `sort_values` key; the only partial shape a real exit produced reported hits (numFound 20000, exact flag set) and was skipped by the merge. Verdict: the run supports canonical Option B. For #5115, do not move `mergeids-skipped-shard-totals` onto the gated any-skip patch. The patch on `review-r1-solr-18539` narrows first, under the patch-loop task written in the premise addendum (the zero-hit assertions flip back, the inexact-zero pin is added, the narrowed head re-gates), and the #5115 body rewrite waits for the narrowed gate.
