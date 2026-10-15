# Pool assignment: open decisions from the vm2 queue, round 1

Capability tags: `review`. Intended host: Windows (review coordinator), or the main side for items marked owner. Claim under `claims/` before starting. Windows runs no builds. Posted by vm2 (gate backlog coordinator), 2026-10-15, at Nick's direction.

vm2 has finished the gate work it can do. Each item below needs a decision or a record that only the review side or the owner can make. Record each decision in the file it names and in the deliverable.

## 1. SOLR-17055: maxScore for a score-sorted knn request (owner decision)

- Gate result: `gates/SOLR-17055-sort-path-proof-rerun.md` is green at `2fe0986158` and is on the fork. The follow-up that pins `maxScore` (commit `5d6b81d86b`, local only, not pushed) is RED: in `DistributedKnnTopKTest` at line 95, `sort=score desc` with `fl=id` gives the distributed response `maxScore` 0.99756694 and the single-node control `null` (`BaseDistributedSearchTestCase` line 996). Log: `logs/vm2/queue-g17055-followup-gate-20261015T003834Z.out`. Recorded in `claims/pool-vm2-finish-list-round-1-c.md`.
- Decision needed: which of these is the contract?
  (a) knn requests report `maxScore` on a score sort, distributed and control both. The fix must change the single-node control path, or the test must compare the field only where both sides agree.
  (b) knn requests report `maxScore` only on the distributed path. Then the control comparison must exclude `maxScore` for knn queries, and the follow-up test asserts the distributed value.
- Vm2 does not pick. Once the owner chooses, the review side writes the test under the chosen contract, and vm2 gates the new head.

## 2. SOLR-17842: docs-only placeholder fix, fork push (owner decision)

- `gates/SOLR-17842-placeholder-fix.md` is OPEN and unclaimed. The job assigns the fork push to the branch owner (decision 10). vm2 will not push it without the owner's go-ahead. Owner: say yes or no, and whether vm2 may push the patch as the claimant.

## 3. SOLR-14231: takeover-log line (review side)

- The restatement is recorded (`gates/SOLR-14231-fail-before.md`, `receipts/SOLR-14231.md`, pr-prepare `22c036fe63`). The assignment's step 4 asks for one line in `files/remaining-branches-takeover-log.md` in the goal records. That file is not in the pr-prepare tree, so the review side or the main side appends the line from its own records.

## 4. SOLR-4374: changelog wording and the function-alias Limits (owner decision, before the gate result is used)

- The handoff says the changelog wording for the catch-all `*` behavior change and the Limits line on function aliases are not in the branch. Owner decision: include them now, or leave them as follow-ups.
- vm2 is gating `f0e23fca1b7` now (the job `gates/SOLR-4374.md`). If the owner says include, the change is a branch commit after the gate, so the gate must run again at the new head. If the owner says follow-up, the Limits line is written as a named follow-up in the draft and nothing else changes.
- Owner: answer include or follow-up. The gate result is recorded either way.

## Deliverable

`reports/vm2-open-decisions-round-1.md`: one section per item with the decision (or "awaiting owner"), the file it was recorded in, and any follow-up gate job filed (path and head). Commit as Nick Shanin, no trailers, no em dashes.

## Limits

No PR, comment, Jira or email. No force push. No fork push except where an item above names one and the owner has said yes.
