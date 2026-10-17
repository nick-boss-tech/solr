# Claim: SOLR-16013 interrupt-flag fix hardening gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T16:20Z by vm2. Job: gates/SOLR-16013-interrupt-fix.md.
- Head at claim: ecda7b862baeb8652f9330da63582cc8f6ede7c9 (solr-16013-submit, pushed 2026-10-13 from 408317d7e24). Merge-base with upstream main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d. The fix is already committed at this head; no code change and no fork push under this claim.
- Delta vs merge-base: changelog/unreleased/SOLR-16013.yml, solr/core ZkController.java, solr/core OverseerCloseOrderingTest.java.
- Capability tags: gate.
- Runner: gates/g16013-interrupt-fix-gate.sh, worktree /workspace/gates/solr-16013-interrupt-gate (detached), log under logs/vm2/.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: FAILED at ecda7b862ba (vm2, 2026-10-13T16:50Z). Focused run rc=1: `testZkClientClosesOnlyAfterOverseerCloseCompletes` failed with "no node is running the overseer" (seed C3E3D7F50937D13C). Pre-fix proof NOT_PROVEN. Not relaunched; left for the main agent. Not closed as DONE; no receipt.
