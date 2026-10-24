# Claim: vm2-pr5117-leftover-decisions-round-1

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE.
- Job or assignment: none; the open questions left after the PR #5117 receipt (`receipts/SOLR-18541.md`).
- Slice: decide the two unpushed SOLR-18541 commits and the leftover worktree edits on vm2; hand the rest to an assignment.
- Capability tags used: review, records.
- Started: 2026-10-24T20:05Z (UTC).
- Constraints: no JIRA or GitHub post, no PR, no force push. Commit as Nick Shanin with no trailers.

## Decisions

1. `c91b606355e`, bounds-check the per-query list in `CombinedQueryComponent` (main line 338 is the unchecked `docList.get(queryIndex)`). Rung 3: `reports/pr-5117-followup-plan.md` keeps the short-list failure out of SOLR-18539, and the defect predates PR #5117 (main has the same access). Not added to PR #5117, whose review scope is the merge totals. Filing a Jira for it is public, so it is NEEDS-OWNER: `assignments/solr-18541-short-list-owner-round-1.md`.
2. `f1bb8743d5d`, the `QueryComponent.mergeIds` partial-shard totals fix. Rung 3: the plan names it the SOLR-18539 PR, based on current main. Checked at rung 1: `QueryComponent.java` is byte-identical between main `79b23df58da` and the PR chain `49bcb450a27`, so the production change ports cleanly. Its test needs main's helpers. Handed off: `assignments/solr-18539-main-port-round-1.md`.
3. Leftover uncommitted worktree edits (rung 1):
   - `vm2-18541-head`: a scratch repro of the short-list crash. Superseded by the committed test in `c91b606355e`.
   - `gate-18563-base`: the synthetic proof-base edit for the SOLR-18563 receipt. Kept as evidence.
   - `vm2-18010-head`, `vm2-9124-scratch`, `criticals-main`: no claim, assignment, or receipt on this board names SOLR-18010, SOLR-9124, or SOLR-6213 in the records. Treated as stale experiments.
   - All diffs saved under `/workspace/logs/vm2/wip-2026-10-24/`. Worktrees kept; removal waits for the owner's go.
4. Pushing from vm2 to the fork fails: GitHub rejects the agent token for git operations ("Password authentication is not supported"). Not a ladder question. The commits and this board commit are local until credentials are fixed.

Heartbeat: 2026-10-24T20:05Z

DONE: 2026-10-24T20:12Z. Deliverables: this claim, `assignments/solr-18539-main-port-round-1.md`, `assignments/solr-18541-short-list-owner-round-1.md`.
