# Claim: solr-16013-overseer-shutdown-order-round-1, phase 2 gate (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: HANDED, phase 2, from NEARLY (2026-11-02T01:47Z). The checks are green; the pre-fix proof leg did not reproduce as recorded (see the receipt). No further runs by vm6.
- Job or assignment: `assignments/solr-16013-overseer-shutdown-order-round-1.md`, phase 2, after the phase 1 report `reports/solr-16013-implementation-round-1.md` (claim `claims/solr-16013-overseer-shutdown-order-round-1-implementation-vm6.md`, DONE).
- Slice: the whole phase 2 gate at the fork head `37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7` (branch `solr-16013-submit`): tidy, Error Prone compile, focused `OverseerCloseOrderingTest` at seeds `2E201C17D50DCEAA` and `16013C0FFEE16013`, the base-revert proof, and the module check.
- Capability tags used: gate.
- Started: 2026-11-01T05:23:58Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-16013-overseer-shutdown-order-round-1-gate@37b01337eb.
- Runner: `/workspace/tmp/16013/gate-driver.sh` (detached), worktree `/workspace/worktrees/pool-vm6-16013-gate` (detached at 37b01337eb, ignored root `gradle.properties` copied in), log dir `/workspace/gates/logs/vm6/solr-16013-gate-round-1/`, status file `driver.status` in that directory.
- Build lock: `/workspace/tools/.gradle-vm6.lock`, taken by `pool/bin/sweep-run.sh`.
- Links: Ticket or parent: SOLR-16013. Category: gate. Siblings: the archived-board receipt `receipts/SOLR-16013.md` (GREEN at 37b01337eb on vm1, 2026-10-13/14), not re-used as evidence for this head. Supersedes: none.


Result: 2026-11-01T06:19:52Z: gate at 37b01337eb: tidy, Error Prone, focused 3/3 at both seeds, module check green in a standalone clone; base-revert proof fails with a fixture message for the ordering method, not the premise. Receipt receipts/SOLR-16013.md.

NEARLY: 2026-11-01T06:19:52Z. Deliverables: receipts/SOLR-16013.md.

HANDED: 2026-11-02T01:47Z. Nick directed vm6 onto another assignment. Open item for the next claimant: run `OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes` alone at base `14c7aac0d151` and at `37b01337eb`, same seed `2E201C17D50DCEAA`, to separate the ordering premise from the shared-cluster method-order interaction. Do not rerun the full base-revert set without a changed input.
Heartbeat: 2026-11-02T01:47Z (final; claim released)
