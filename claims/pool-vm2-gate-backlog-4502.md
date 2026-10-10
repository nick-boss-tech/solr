# Claim: SOLR-4502 first gate with premise run (round 1, tranche 1, job 3)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-4502.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-10T21:11:08Z (UTC).

Heartbeat: 2026-10-10T21:11:08Z.

Heartbeat: 2026-10-10T21:23:56Z. Premise run failed as predicted on base (create NPE, no load() message); gate running on packaged head 0ee4c644ee63.

Result 2026-10-10T21:37:39Z: premise held (base run failed as predicted). Gate green at packaged head 0ee4c644ee63: changelog OK, tidy clean, compile OK, TestCoreContainer 26 run, 0 failed, 3 skipped (Windows-only), new test passed, module check rc 0. Receipt receipts/SOLR-4502.md. Packaging commit is local only, not pushed.
DONE: 2026-10-10T21:37:39Z (UTC), outcome GREEN.
