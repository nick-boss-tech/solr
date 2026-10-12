# Pool assignment: Core admin convergence, round 3

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-core-admin-convergence-round-3.md.

## Purpose

Round 2 (`reports/core-admin-convergence-round-2.md`) returned NOT CONVERGED: 7 of its 18 drafts held open should-fix items. The main side has applied the four new findings (CA-R2-01, CA-R2-02, CA-R2-41, CA-R2-42) and the note CA-R2-04, and written its adjudication in `reports/core-admin-convergence-round-2-response.md`. Fifteen of the 18 drafts now converge on their text. This round is a confirmation round over only the three drafts still open: SOLR-4502, SOLR-8576 and SOLR-12916. Each waits on a branch commit or a gate job whose state may have moved.

## Scope

- The 3 drafts still open: SOLR-4502, 8576, 12916, all in `pr-drafts/core-admin/`.
- Every other Core admin draft is out of scope; do not re-audit converged drafts.
- No item was rebutted in round 2, so there is no rebuttal to judge.
- Settled decisions recorded in `material/core-admin-round-1-answers.md` are not reopened. Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. SOLR-4502, CA-R1-01 (should-fix, branch-side): check whether the changelog fragment retitle has landed on `solr-4502-submit` (read the live tip with `git ls-remote origin`, then the fragment `changelog/unreleased/SOLR-4502-create-before-load.yml` at that tip). If it has landed, check the title against the replacement recorded in `reports/core-admin-convergence-round-1-response.md`, and record the head move the round 2 response names under CA-R2-03: the draft's head references (lines 15, 21, 33 and 37) and the top-up gate state at the new head are main-side items that pass with the landing; judge the draft text against the landed state and say which parts the main side still owes. If it has not landed, confirm the item stays open.
2. SOLR-8576, CA-R1-24 (should-fix): re-check the state of `gates/SOLR-8576-alias-fix.md` and the live tip of `solr-8576-submit`. If the alias fix has landed and `CollectionsAPISolrJTest` has run at a new head, judge the draft against that head: the [OWED BEFORE POSTING] marker deleted, the head, counts and links moved, and CA-R1-21's post-fix form ("...that the original collection is unchanged and that the alias still resolves to it") in place. If the tip is unchanged, confirm the marker still stands and the item stays open.
3. SOLR-12916, CA-R1-34 (should-fix, branch-side): check whether the changelog fragment retitle has landed on `solr-12916-submit`. If it has, check the title against the replacement recorded in the round 1 response and that the draft's changelog link points at the new head. If it has not landed, confirm the item stays open. (The fragment commit `1a4eb16c567` in the branch history is the original fragment commit, not the retitle.)
4. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that no round or response records is a new finding. Anything else (style, length, settled decisions) is out of scope.

## Findings register

Carried findings keep their IDs (CA-R1-01, CA-R1-24, CA-R1-34) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs CA-R3-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/core-admin-convergence-round-3.md`, with:

- Counts up front: drafts re-checked, items confirmed landed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
