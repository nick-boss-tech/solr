# Claim: SOLR-12347 premise verification, then first gate (round 2, job 8)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-12347.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T04:39:31Z (UTC).
Branch: solr-12347-submit at b77acba2ad603433057e618e2fddbcb3412d736c (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T04:39:31Z.

Premise part 1 (code read, 2026-10-11T04:50Z): default in force is SOLR_STOP_WAIT:=180 at merge-base b6b2b8f10e and at base cabedd1d96 (bin/solr line 154); bin and packaging/test are identical between those two commits.

Heartbeat: 2026-10-11T04:56:15Z (relaunch after first environment failure).

Result 2026-10-11T05:05Z: BLOCKED, environment, no BATS run. Both assembleDist runs fail in :solr:webapp:js-client:jsClientDownloadDeps ("Cannot find package '@babel/plugin-syntax-dynamic-import'"), same as SOLR-10667. Premise part 2 not shown. No packaging commit, no receipt.
BLOCKED: 2026-10-11T05:05Z (UTC), outcome BLOCKED (environment).
