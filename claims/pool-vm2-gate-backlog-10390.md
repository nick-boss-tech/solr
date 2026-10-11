# Claim: SOLR-10390 premise run (round 1, tranche 1, job 12)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-10390-premise.md.
Capability tags: premise-run (BATS). Staffing: 1.
Started: 2026-10-11T01:25:00Z (UTC).

Heartbeat: 2026-10-11T01:25:00Z.

Result 2026-10-11T01:49Z: premise HOLDS on the test body. Base cabedd1d96 bin/solr fails at refute 'Please install lsof' (test_start_solr.bats:129). Head 4af4a6834e bin/solr passes the body; the bats report is "not ok" only from teardown (solr stop --all, 30s wait, force-killed on this host; reproduced manually on base and head). Receipt receipts/SOLR-10390.md; no gate run.
DONE: 2026-10-11T01:49Z (UTC), outcome PREMISE HELD.
