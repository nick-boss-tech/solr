# Pool assignment: Replication and backup convergence, round 3

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-replication-backup-convergence-round-3.md.

## Purpose

Round 2 (`reports/replication-backup-convergence-round-2.md`) returned NOT CONVERGED: 4 of its 8 drafts held open should-fix or blocking items. The main side has applied RB-R2-21 and RB-R2-22 and written its adjudication in `reports/replication-backup-convergence-round-2-response.md`. Six of the 8 drafts now converge on their text. This round is a confirmation round over only the two drafts still open: SOLR-8430 and SOLR-9091. Each waits on a gate or a branch commit whose state may have moved.

## Scope

- The 2 drafts still open: SOLR-8430, 9091, both in `pr-drafts/replication-backup/`.
- Every other Replication and backup draft is out of scope; do not re-audit converged drafts.
- No item was rebutted in round 2, so there is no rebuttal to judge.
- Settled decisions recorded in `material/replication-backup-round-1-answers.md` are not reopened, including the landing orders (RestoreCore 9865, then 17287, then 9091; IndexFetcher 12246, then 12085, then 11650, with 6711 last; 9598 before 12651). Gate state is settled by the receipt in `receipts/` at the exact live tip and by the gate job record in `gates/`.

## What to check

1. SOLR-8430, RB-R1-01 (blocking) and RB-R2-01 (should-fix): re-check the gate state in `gates/SOLR-8430.md` and its claim, and the live tip of `solr-8430-submit` with `git ls-remote origin`. The cap, the RB-R1-03 retitle and a test fixture fix are on the branch (tip `0d64c35c5a7d` when the round 2 response was written); the gate at the implementation head failed on a test fixture defect and a re-gate was outstanding. If the re-gate has closed green at the live tip and receipts/SOLR-8430.md is refreshed at that head, judge the draft against the post-cap text RB-R1-01 and RB-R2-01 prescribe (the first Limits bullet replaced, the "map holds at most 64 rate values" sentence present, RB-R2-01's three bounded statements, links and Proof at the gated head); the main side applies that text in the pass that closes the gate, so report which state you found and whether the live draft matches it. If the gate is still open, confirm both findings stay open and the draft stays held.
2. SOLR-9091, RB-R1-04 (should-fix, branch-side): check whether the changelog fragment retitle has landed on `solr-9091-submit` (the fragment `changelog/unreleased/SOLR-9091-restore-verify-backup-checksum.yml` at the live tip). If it has, check the title against the replacement recorded in `reports/replication-backup-convergence-round-1-response.md`, and check RB-R2-02's follow-on state: the draft's `e6bfa5c` links (lines 15, 23 and 33) re-pointed and line 23 carrying "Only the changelog title changed after this verified run." If it has not landed, confirm the item stays open.
3. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that no round or response records is a new finding. Anything else (style, length, settled decisions, the recorded landing orders) is out of scope.

## Findings register

Carried findings keep their IDs (RB-R1-01, RB-R1-04, RB-R2-01, RB-R2-02) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs RB-R3-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/replication-backup-convergence-round-3.md`, with:

- Counts up front: drafts re-checked, items confirmed landed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
