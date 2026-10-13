# Pool assignment: strong review of the four draft PRs, round 2 (re-review after the gates)

Capability tags: `review`. Intended host: Windows. Do not start until the gate jobs below are DONE or the owner says to proceed on a subset. This assignment is self-contained for a reviewer joining cold; read `reports/four-new-prs-strong-review-round-1.md` first.

## Context

Round 1 returned SATISFIED for #5116 (SOLR-18540) and NOT SATISFIED for #5115 (SOLR-18539), #5117 (SOLR-18541) and #5118 (SOLR-18010). Code findings were written as patches on three review branches and posted as gate jobs. Wording findings were applied to the four live PR bodies and the drafts in `pr-drafts/`.

## Inputs to wait for

| Input | Job file | Review branch |
|-------|----------|---------------|
| #5115 patch gate | `gates/SOLR-18539-review-r1-patch.md` | `review-r1-solr-18539` at `fbf7610644d178049e1110ffc0ec34dad8c5b32f` |
| #5117 patch gate | `gates/SOLR-18541-review-r1-patch.md` | `review-r1-solr-18541` at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e` |
| #5118 patch gate | `gates/SOLR-18010-review-r1-patch.md` | `review-r1-solr-18010` at `e99be67c9a6fe15f86306104843a30b35196f159` |
| Plain `QueryComponent` real-exit shape | `gates/queryComponent-real-exit-shape-premise.md` | none |
| #5118 symlink and bind-mount semantics | `gates/SOLR-18010-replace-semantics-premise.md` | none |

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
