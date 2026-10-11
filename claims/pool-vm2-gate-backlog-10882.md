# Claim: SOLR-10882 premise run, then first gate (round 2, job 2)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-10882.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T02:09:11Z (UTC).
Branch: solr-10882-submit at 83fc3dfeb24b875639150579a2f8aeb35b7d6a9c (live tip, verified by ls-remote at claim time). Merge-base with origin/main: e044bf20b405b8ca985730be3553d77d1fc8ff31.

Heartbeat: 2026-10-11T02:09:11Z.

Heartbeat: 2026-10-11T02:23:40Z. Premise HOLDS (base fails arrayMixedTypesSortTest with ClassCastException at ArrayEvaluatorTest.java:148; head passes 7/7). Packaged head 5d7d07a5b1 local. Gate running, log /workspace/gates/logs/vm2/10882-gate.out.

Result 2026-10-11T02:32:06Z: GREEN at packaged head 5d7d07a5b17bfbf9370fe8562ce7b5d9909118ee. Proof leg (base production, branch test kept) ArrayEvaluatorTest tests=7 failures=1, arrayMixedTypesSortTest ClassCastException as premised; focused head tests=7 failures=0; tidy clean, compile rc 0, module check rc 0. Receipt receipts/SOLR-10882.md. Packaging commit local only.
DONE: 2026-10-11T02:32:06Z (UTC), outcome GREEN.
