# Claim: SOLR-15003 proof recount and fail-before at the merge base (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T09:09ZZ by vm2. Job: gates/SOLR-15003-proof-recount.md.
- Head: 1004abee39ab07d5eae1a5ea6327572997ff50d2 (solr-15003-submit, live tip). Merge base b5c71bc5573. Intermediate pre-fix 8f5b6f8360a.
- Capability tags: gate (record job, no branch change, no push).
- Runner: gates/g15003-proof-gate.sh, worktree /workspace/gates/solr-15003-proof (detached). Seed 15003C0FFEE15003, nightly tests enabled.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: FAILED 2026-10-14T09:25Z, handed to the main agent. Gate run finished rc=1 at the merge base (one of four new tests fails there; three pass) and rc=0 at 8f5b6f8360a where the receipt expects the reload test to fail. Not relaunched. Details in gates/SOLR-15003-proof-recount.md under the gate run record.

- 2026-10-14T09:41ZZ vm2 gate backlog coordinator: DONE. Record run: head 4 of 4; merge base 1 failure (testFullCopyKeepsSnapshotFilesInOldIndexDir); pre-fix 8f5b6f8360a reload test passed, which contradicts the receipt. See gates/SOLR-15003-proof-recount.md.

- 2026-10-14 main side: classification recorded in `gates/SOLR-15003-proof-recount.md` and `receipts/SOLR-15003.md`. The three NOT_PROVEN tests are class (b) pins: the merge-base code already produced each asserted outcome, so each test passes there without the change and none of the three may be cited as a fail-before. The recount itself stands. The old receipt's step 3a failure statement is withdrawn because 8f5b6f8360a is a test-only commit on top of the production fix, not a pre-fix state. Handoff closed.
