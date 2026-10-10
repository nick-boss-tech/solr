# Claim: SOLR-5011 first gate with premise run (round 1, tranche 1, job 4)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-5011.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-10T21:39:07Z (UTC).

Heartbeat: 2026-10-10T21:39:07Z.

Heartbeat: 2026-10-10T21:47:10Z. Gate running at packaged head 5b2cfdfe4709 (proof shape corrected to revert SolrCore.java only).

Result 2026-10-10T22:01:11Z: BLOCKED ON PROOF LEG. Gate steps green at packaged head 5b2cfdfe4709 (changelog OK, tidy clean, compileTestJava rc 0). Proof leg (base SolrCore.java, branch SolrResourceLoader.java and test, at cabedd1d96): CoreCloseResourceLoaderTest executed and failed at line 44 (oldCore.close()) with "Too many closes on SolrCore" (SolrCore.close, refcount already at zero), not at the line 45 assertTrue(oldLoader.isClosed()) the proof shape requires. Premise not shown by this proof. Focused tests at head and module check not run: stopped after the proof failure per protocol. Shared-schema scenario not covered by any branch test (recorded in the job file). No receipt written. Logs: /workspace/gates/logs/vm2/5011-proof-1.log, 5011-gate.out.
DONE: 2026-10-10T22:01:11Z (UTC), outcome BLOCKED ON PROOF LEG.
