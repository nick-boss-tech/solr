# Claim: solr-18539-port-main-round-1 (implementation and gate)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job or assignment: `assignments/solr-18539-main-port-round-1.md`.
- Slice: the whole assignment. Port `f1bb8743d5d` (QueryComponent.mergeIds partial-shard totals) onto upstream main `79b23df58da` as a new fork branch `solr-18539-merge-totals-main`, port the test helpers, run the fail-before on main, and run the gate at the pushed head.
- Capability tags used: implementation, gate.
- Started: 2026-10-24T20:23Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18539-port-main-round-1@main-79b23df58da.
- Source: local commit `f1bb8743d5d` on the fork branch `solr-18541-followups`, now pushed to `nick-boss-tech/solr`.
- Constraints: no PR, no JIRA or GitHub comment. Commit as Nick Shanin with no trailers. The new branch is on the fork; nothing else on the fork changes.
- Runner: `/workspace/tools/gate-18539.sh` (adapted from gate-18541.sh), serialized by flock on the host build lock.
- Decision ladder: rung 3 (plan in `reports/pr-5117-followup-plan.md`). The `CombinedQueryComponent` bounds-check is not in this claim.
