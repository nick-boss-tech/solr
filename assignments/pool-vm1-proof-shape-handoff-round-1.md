# Pool assignment: SOLR-6831 and SOLR-6975 proof shape, handoff from vm2

- Capability tags: gate, implementation. Intended host: vm1 (Linux gate host). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2 (gate backlog coordinator), 2026-10-15, at the direction of Nick.

Both gates run green on their head tests, but their proof legs fail in a shape the job files did not expect. vm2 stopped and handed both over. vm2 will not relaunch either run.

## Items

1. **SOLR-6831 omitted-refinement, proof shape and gate.** Head: `vm2-solr-6831-r3` (fork), commit `4afed04987b2`, one commit on `3e07d801df5`. It skips an incomplete refinement answer with no pivot data instead of throwing. Head test `TestQueryLimits`: 8 of 8 pass. Proof leg at base `2ee19dc646`: `testPivotFacetRefinementStopsWhenOmittedPartialResults` fails with `SolrServerException: No live SolrServers available`, not the `n` message in the job, because the base coordinator throws before the refinement loop. Start from: `gates/SOLR-6831-omitted-refinement-round-2.md` (run records at the end). Output: the receipt, the job file result, and the claim.
2. **SOLR-6975 distributed docid sort, proof shape and gate.** Head: `c666155ed058`, `solr-6975-submit`. Head test `DistributedQueryComponentOptimizationTest`: 10 of 10 pass. Proof leg (`ShardFieldSortedHitQueue.java` reverted to `e432df19c4a5`): `testSortByDocId` fails with `SolrServerException: No live SolrServers available`, not the `IndexOutOfBoundsException` in the job. Start from: `gates/SOLR-6975.md`. Output: the same as item 1.

Both proof legs share the same symptom: the first failing shard request surfaces as a dead load balancer. Find out whether the failure is in the test cluster setup or in what the test asserts, then make the smallest change that gives a valid proof.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof form | default: outcome proof (fails at base, passes at head) | if the base failure is the SolrServerException and the head passes, accept it and record the shape change in the job file; otherwise stop and record | Nick |
| Test change | default: change only the test, not production code, unless the base failure cannot be reached otherwise | if a production change is needed for the test to reach its assertion, stop and record it, do not push | Nick |
| Fork push | default: push to a new branch named for the round, never to `solr-6831-submit` or `solr-6975-submit` | if the gate is green, push to a new branch and record it; do not move a PR head | Nick |
| PR body | default: no PR edits | none; the owner decides when to update PR #5134 or #5135 | Nick |

## Deliverable

`reports/vm1-proof-shape-round-1.md`: one section per item with its status, the changes made, the gate job filed or run (path and head), and the new branch name if one was pushed.

## Limits

- No PR, comment, JIRA or email. No force push. Never move `solr-6831-submit` or `solr-6975-submit`.
- Commit as Nick Shanin with no trailers. No em dashes.
