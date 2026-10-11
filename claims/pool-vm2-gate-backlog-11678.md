# Claim: SOLR-11678 premise run, then first gate (round 2, job 9)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-11678.md.
Capability tags: gate, premise-run. Staffing: 1.
Branch: solr-11678-submit at 55d8cd189d15367b6896f0ea07570d3395ce404b (live tip per the job file).
Started: 2026-10-11T05:02:30Z (UTC).
Heartbeat: 2026-10-11T05:02:30Z.

Gate result 2026-10-11T05:28Z (UTC): GREEN at packaged head 611eae2464. Changelog OK, tidy rc 0 clean, compile rc 0, focused SSLConfigurationsTest 20/0, EnvSSLCredentialProviderTest 2/0, SysPropSSLCredentialProviderTest 2/0, module check rc 0.
Proof leg: merge-base production compile rc 1 (10 errors, all three branch test files use the new API). INCONCLUSIVE by compile; no rerun, since no branch test would remain to run.
DONE: 2026-10-11T05:28Z (UTC), outcome GREEN; proof INCONCLUSIVE by compile. Receipt receipts/SOLR-11678.md.
