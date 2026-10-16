# Claim: SOLR-6975 gate at the patched head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T05:54ZZ by vm2. Job: gates/SOLR-6975.md (head c666155ed058, fork tip verified by the runner at start).
- Capability tags: gate. Runner: gates/g6975-vm2-gate.sh (worktree /workspace/gates/solr-6975-gate, detached). Seed 6975C0FFEE6975.
- Decisions D1 to D5 applied as the job states. Proof leg: ShardFieldSortedHitQueue.java reverted to e432df19c4a5.
- Note: the queue entry named gates/g6975-gate.sh was a superseded local entry; its file was overwritten by this runner's name clash before the rename to g6975-vm2-gate.sh. The superseded content was not recorded and is not run.
- Status: CLAIMED, runner queued.

- Completion note (appended by vm1, 2026-10-15, on verification per the WORKFLOW.md missed-marks rule): this claim's run finished as recorded in the takeover log's 2026-10-15 01:19 MDT queue pass (head leg 10 of 10; proof leg stopped under D3 on a failure-shape difference). The proof shape was then handed to vm1 in assignments/pool-vm1-proof-shape-handoff-round-1.md and settled there: GATE GREEN at c666155ed058, receipts/SOLR-6975.md top section, gates/SOLR-6975.md marked DONE. Status: DONE (gate completed under the handoff claim claims/pool-vm1-proof-shape-handoff-round-1.md).

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-6975.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
