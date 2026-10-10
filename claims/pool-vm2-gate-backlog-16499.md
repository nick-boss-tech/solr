# Claim: SOLR-16499 first gate with premise run (round 1, tranche 1, job 6)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-16499.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-10T22:30:05Z (UTC).

Heartbeat: 2026-10-10T22:30:05Z.

Packaged head: 51b7addddd45dfbb8e18e4f80982a3793206bdcd (local, not pushed). Gate running on runner /workspace/gates/solr-16499; logs /workspace/gates/logs/vm2/16499-*.

Heartbeat: 2026-10-10T22:33:46Z.

Heartbeat: 2026-10-10T22:36:00Z. Runner in progress: changelog parse rc=0, tidy running (log /workspace/gates/logs/vm2/16499-tidy.log).

Heartbeat: 2026-10-10T22:41:03Z. Gate still running (tidy rc=0, tree unchanged; compileTestJava in progress, log /workspace/gates/logs/vm2/16499-compile.log).

Heartbeat: 2026-10-10T22:51:21Z. Compile BUILD SUCCESSFUL (16499-compile.log). Proof leg ran (16499-proof.log): testParallelAndTimeoutAreForwardedToTheOverseerMessage failed with "expected:<4> but was:<2>" on the message size, as the job predicts; premise held. Focused head run (16499-head.log) shows ReplaceNodeAPITest tests=4 failures=0. Module check running (16499-check.log).

Result 2026-10-10T22:53:44Z: GREEN at packaged head 51b7addddd45dfbb8e18e4f80982a3793206bdcd. Changelog OK, tidy clean, compile OK, premise held on base, ReplaceNodeAPITest tests=4 failures=0 at head from fresh XML, module check rc 0. Receipt receipts/SOLR-16499.md. Packaging commit local only, not pushed.
DONE: 2026-10-10T22:53:44Z (UTC), outcome GREEN.
