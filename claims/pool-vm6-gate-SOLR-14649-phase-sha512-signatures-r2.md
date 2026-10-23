# Claim: pool-vm6-gate-SOLR-14649-phase-sha512-signatures-r2

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE. Job: `gates/SOLR-14649-phase-sha512-signatures.md`. Mark DONE with the receipt path when the gate lands.
- Job or assignment: `gates/SOLR-14649-phase-sha512-signatures.md`.
- Supersedes: `claims/pool-vm6-gate-SOLR-14649-phase-sha512-signatures.md` (HANDED BACK at head 25589eb5e6, PackageToolTest install failure).
- Owner decision (Nick, 2026-10-23): re-sign the question-answer fixture and widen this job's diff scope to the two key files. The existing 512-bit test key cannot produce SHA-512 signatures, so it was rotated to 2048-bit.
- Repair commit: `1767e9dce81453233bad30c49b5b19dfff8a017a` (`publickey.der`, `question-answer-repository-private-key.pem`, `repository.json` signatures for 1.0.0 and 1.1.0). Tree `ca050af3c84d8074c5aee41a2c70edcdedb6eb01`.
- Slice: the whole gate job, from step 0 on head `1767e9dce8`.
- Capability tags used: gate.
- Started: 2026-10-23T18:32:46Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 2. Idempotency key: SOLR-14649-phase-sha512-signatures@1767e9dce81453233bad30c49b5b19dfff8a017a.
- Links: Ticket or parent: SOLR-14649 (branch `solr-14649-submit`). Category: see pool/work-map.md. Siblings: none known. Supersedes: as above.

Heartbeat: 2026-10-23T18:32:46Z
