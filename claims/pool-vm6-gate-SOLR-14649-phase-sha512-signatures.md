# Claim: pool-vm6-gate-SOLR-14649-phase-sha512-signatures

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE. Job: `gates/SOLR-14649-phase-sha512-signatures.md`. Mark DONE with the receipt path when the gate lands.
- Job or assignment: `gates/SOLR-14649-phase-sha512-signatures.md`.
- Slice: the whole gate job.
- Capability tags used: gate.
- Started: 2026-10-23T15:31:53Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-14649-phase-sha512-signatures@800e91188e0d9f975b9096c9413915ea18713b23.
- Links: Ticket or parent: SOLR-14649 (branch `solr-14649-submit`). Category: see pool/work-map.md. Siblings: none known. Supersedes: none.
- Preflight: `/workspace/gates/logs/vm6/preflight/SOLR-14649-phase-sha512-signatures/`, exit 3 at 2026-10-23T15:30Z. Tidy changed one file, `solr/core/src/test/org/apache/solr/filestore/TestDistribFileStore.java` (formatting only). The patch is `tidy.patch` in that directory. It is applied as a gate repair under WORKFLOW.md "Gate repairs".

Heartbeat: 2026-10-23T15:31:53Z
