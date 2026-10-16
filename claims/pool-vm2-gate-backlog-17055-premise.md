# Claim: SOLR-17055 sort-path premise run (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T10:13ZZ by vm2. Job: gates/SOLR-17055-sort-path.md, step 1 (premise run only).
- Head: c2396f425fa2a67a40fea7afee341840c3896358 (solr-17055-submit, ls-remote matched by the job; the scratch edit is never committed).
- Capability tags: premise-run.
- Runner: gates/g17055-premise-gate.sh, worktree /workspace/gates/solr-17055-premise (detached). Seed 17055C0FFEE17055.
- The fix (job steps 2 onward) is not claimed here; it waits on the premise result.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED, runner queued.

- 2026-10-14T10:44ZZ vm2 gate backlog coordinator: premise step DONE (not the whole job). Distributed 9 documents vs control 3 with sort=id asc at c2396f425f. Recorded in gates/SOLR-17055-sort-path.md. Fix not started.

Status: CLOSED (vm2 audit, 2026-10-16T00:56Z). Gate job gates/SOLR-17055-fix-gate.md is DONE on pr-prepare; the queued runner was never started and is not run.
