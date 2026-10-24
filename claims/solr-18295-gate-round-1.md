# Claim: solr-18295-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job or assignment: `gates/SOLR-18295-routing-round-1.md` (the whole job, legs 1 to 3). The implementation is `claims/solr-18295-routing-implementation-round-1.md` (windows, CLAIMED, not edited here).
- Slice: the whole gate on the pinned head `b273b245115beff0cc131448c277dc9bde909a0c` on `nick-boss-tech/solr:solr-18295-submit`, base upstream/main `13c693d805f6541f7cec684cd27d28eef7b3b614`. Diff checked before the claim: one file, `ReplicationFactorTest.java`, 8 insertions and 2 deletions, author Nick Shanin.
- Capability tags used: gate, settling-run.
- Started: 2026-10-24T20:11:17Z (UTC). Claim written before any run.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18295-gate-round-1@b273b24511.
- Runner and paths: `pool/bin/gate-preflight.sh` then `pool/bin/bg-steps.sh` (calls `pool/bin/sweep-run.sh`) under the host build lock `/workspace/tools/.gradle-vm6.lock`. Log root `/workspace/gates/logs/vm6/solr-18295/`. Exact-head worktree `/workspace/worktrees/pool-vm6-18295`. Synthetic baseline worktree `/workspace/worktrees/pool-vm6-18295-base` at `13c693d805`. XML is read from `solr/core/build/test-results/test/` in each worktree, since the runner copies only that path.
- Seeds: smoke `18295ABC0001` (leg 1, reported separately). Legs 2 and 3: `18295ABC0001` through `18295ABC0014`, 20 seeds, one fresh whole-class invocation each, `:solr:core:cleanTest` before each.
- Capability limit: no GPU and no CI mode. The job's local retry configuration is kept as it is.
- Constraints: no PR, no JIRA or GitHub post, no fork push, no comment on #4563. Commit as Nick Shanin with no trailers.
- Decision ladder: the job's acceptance and failure rows govern. A failure at the exact head is recorded and diagnosed, not relaunched.

Heartbeat: 2026-10-24T20:11:17Z

Result: 2026-10-24T23:02:12Z: gate on b273b245115beff0cc131448c277dc9bde909a0c. Format, compile, Error Prone, check and final spotless pass; smoke pass; 20 of 20 matched final-head runs pass; synthetic baseline 16 of 20 pass, with 4 pre-commit transport failures at cut followers (see report).

DONE: 2026-10-24T23:02:12Z (gate). Deliverables: `reports/solr-18295-gate-round-1.md` and `receipts/SOLR-18295.md`.
