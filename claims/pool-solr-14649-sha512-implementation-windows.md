# Claim: SOLR-14649 SHA-512 package signature implementation

- Claimant host: windows, matching `hosts/windows.md`.
- Status: DONE.
- Job or assignment: `assignments/pool-solr-14649-sha512-implementation-round-1.md`.
- Slice: the single SOLR-14649 implementation item.
- Capability tags used: implementation.
- Started: 2026-10-23T07:12:56Z (UTC).
- Lease: closed at 2026-10-23T08:05:16Z (UTC). Attempts: 0. Idempotency key: SOLR-14649@upstream-main-79b23df58daac14b92cc35f23b8b81f72098b4ba.
- Links: Ticket or parent: SOLR-14649. Category: 6, configsets and the filestore. Siblings: SOLR-17733 branch `solr-17733-submit` at `cf1b283272641e8e4944cf5a80773cb336be8e0c` edits later deletion sections of `TestDistribFileStore`; preserve those additions and keep the signature change in its separate setup block. Supersedes: none; follows the signing-path caveat in `reports/csv-critblock-03.md`.

Heartbeat: 2026-10-23T08:05:16Z

Result: source implementation committed and pushed to `solr-14649-submit` at `800e91188e0d9f975b9096c9413915ea18713b23`. Exact-head Linux gate request `gates/SOLR-14649-phase-sha512-signatures.md` is OPEN. No tests were run on Windows; proof remains pending.

DONE: implementation pushed at `800e91188e0d9f975b9096c9413915ea18713b23`; gate request posted and open under `SOLR-14649-phase-sha512-signatures`.
