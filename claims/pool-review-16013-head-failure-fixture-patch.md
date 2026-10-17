# Claim: SOLR-16013 head failure, fixture patch preparation (review side)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-vm2-16013-head-failure-fixture-patch.md: none linked)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review (writes the patch)
- Assignment: `assignments/pool-vm2-16013-head-failure-fixture-patch.md` (posted by vm2 at 2026-10-13T17:00Z).
- Scope: the test-only change to `solr/core/src/test/org/apache/solr/cloud/OverseerCloseOrderingTest.java` on `solr-16013-submit` at `ecda7b862baeb8652f9330da63582cc8f6ede7c9`. The patch is prepared as a diff in the scratchpad, checked by reading the code, and summarized in the round report. It is not pushed to the fork in this round, because this job's rules forbid edits to submit branches. Pushing it, and the vm2 re-gate at the patched head, wait for Nick's go-ahead.
- Limits: no build, test or Gradle on this host. A patch counts as reviewed only after a Linux host gates the patched head.
- Heartbeat: 2026-10-13T17:05Z (claim taken).
- Status: SUPERSEDED BY OWNER DECISION 2026-10-13. The patch was PREPARED 2026-10-13T17:40Z (READY FOR GATE, saved as `gates/SOLR-16013-fixture-patch-round-1.patch`; Route A, restart the stopped node in `finally`, guarded by `!stopper.isAlive()`; deliverable summary in `reports/12505-16013-7394-17292-recheck-round-1.md`). The owner ruled on 2026-10-13 that the 2026-10-12 investigation's BRANCH-CAUSED classification governs the `ecda7b862ba` failure and that this fixture patch is NOT pushed. The production route was taken instead: the interrupt-flag completion fix at local head `4215c8cee5` on `solr-16013-submit`, gated under `gates/SOLR-16013-interrupt-fix.md`. This claim is closed; do not push the patch and do not re-gate a patched fixture head under it.
