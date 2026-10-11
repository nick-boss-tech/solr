# Claim: SOLR-12347 premise retry and first gate on vm1

Claimant host: vm1. Job: gates/SOLR-12347.md.
Capability tags: gate, premise-run, bats. Staffing: 1.
Started: 2026-10-11T05:14:05Z (UTC).
Branch: solr-12347-submit at b77acba2ad603433057e618e2fddbcb3412d736c (live tip, per gates/SOLR-12347.md).

Why a retry on vm1: vm2's round is BLOCKED on environment (claims/pool-vm2-gate-backlog-12347.md): both :solr:packaging:assembleDist runs (base cabedd1d9680, head b77acba2ad6) failed in :solr:webapp:js-client:jsClientDownloadDeps with "Cannot find package '@babel/plugin-syntax-dynamic-import'" before any BATS run. The identical failure blocked SOLR-10667 on vm2 and did not reproduce on vm1 with a fresh npm cache, so this lane retries on vm1 following the SOLR-10667 route: NPM_CONFIG_CACHE pointed at a clean directory, distributions assembled with :solr:packaging:installFullDist. If the same babel package failure reproduces on vm1, the lane stops and records an environment block with the log evidence.

Premise, per gates/SOLR-12347.md: part 1 is already read from code by vm2 (default in force is SOLR_STOP_WAIT:=180 at merge-base and at cabedd1d96, solr/bin/solr line 154; the branch's new BATS expectation "waiting up to 600 seconds" cannot print on base). Part 2: the branch's test_start_solr.bats expectations run against base scripts must fail for the premise reason (content checked: the 600-second message absent on base) and pass at the packaged head. If the premise holds, the lane finishes the gate (packaging commit removing SOLR-12347-TESTING.md, changelog parse, tidy guard, BATS at the packaged head), pushes the packaged branch, and writes receipts/SOLR-12347.md. If the premise does not hold, the lane records NO GATE by finding with the run evidence.

Heartbeat: 2026-10-11T05:14:05Z (claim).
