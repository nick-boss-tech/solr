# Claim: SOLR-8628 packaging commit and top-up gate (vm2, gate backlog coordinator)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job gates/SOLR-8628-packaging.md: Status: CLAIMED (claims/pool-vm2-gate-ba)

- Claimed: 2026-10-14T21:52Z by vm2. Job: gates/SOLR-8628-packaging.md.
- Head at claim: 5b606dc779d0f367d7c8ac7235988a65f092b6a4 (solr-8628-submit, ls-remote matched). The packaging commit deletes SOLR-8628-TESTING.md only.
- Capability tags: gate.
- Runner: gates/g8628-packaging-gate.sh, worktree /workspace/gates/solr-8628-packaging (detached). The packaging commit is pushed as a plain fast-forward only after the gate is green.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED, gate classified GATE GREEN by the main side 2026-10-14. Run finished 2026-10-14T22:48:07Z with a nonzero rc at the proof leg (rc 1; the revert-to-base run ran TestCoreContainer 26 tests, 3 skipped, 1 failure, testCreateCoreOverIndexDirWithOnlyWriteLock, exactly as step 5 of the job expects). Other rc lines 0; focused head leg 26 tests, 3 skipped, 0 failures from fresh JUnit XML. Not relaunched. Remaining under this claim: push the packaged head 2e56ce9990b008c00d2d07b2498cf28cdc101a06 as a fast-forward (fork still at 5b606dc779d0), verify by ls-remote, re-point the draft citations in the same pass, and mark the job and this claim DONE. See gates/SOLR-8628-packaging.md, Classification.

- 2026-10-14T23:26ZZ vm2 finish list section A: DONE. Pushed 2e56ce9990b0 (fast-forward from 5b606dc779d0), ls-remote verified; draft citations re-pointed.

STALE: no live work by vm2 since 2026-10-18; marked by the vm2 coordinator so the pool tick does not resume it. Work not finished, see the job or assignment file.
