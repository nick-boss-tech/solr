# Claim: SOLR-12916 first gate with premise run (round 1, tranche 1, job 5)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-12916.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-10T22:02:06Z (UTC).

Heartbeat: 2026-10-10T22:02:06Z.

Heartbeat: 2026-10-10T22:15:50Z. Proof leg on base (cabedd1d96 production, branch test at head) ran: testFlatNameValueQueryFromConfigApi failed with "expected:<2> but was:<0>" as the premise predicts.

Result 2026-10-10T22:26:52Z: GREEN at packaged head 50b1f4bcab039f8d25a729a8d02a1e8a16defd3e. Changelog OK, tidy clean, compile OK, QuerySenderListenerTest tests=2 failures=0 at head, module check rc 0. Receipt receipts/SOLR-12916.md. Config API round trip not run (stated limit). Packaging commit local only, not pushed.
DONE: 2026-10-10T22:26:52Z (UTC), outcome GREEN.
