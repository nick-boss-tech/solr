# Claim: SOLR-10667 premise retry and first gate on vm1

Claimant host: vm1. Job: gates/SOLR-10667.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T04:46:00Z (UTC).
Branch: solr-10667-submit at 32b594f280c5c5f0e5241dadbad7677cdb563c3e (live tip, verified by ls-remote at claim time).

Why a retry on vm1: vm2's round is BLOCKED on environment (claims/pool-vm2-gate-backlog-10667.md): both :solr:packaging:assembleDist runs failed in :solr:webapp:js-client:jsClientDownloadDeps with "Cannot find package '@babel/plugin-syntax-dynamic-import'" before any test ran. The main side's decision authorizes a retry on vm1 with a clean npm cache. If the same babel package failure reproduces on vm1, the lane stops and records an environment block with the log evidence; no build-script changes are improvised.

Premise, per gates/SOLR-10667.md: base run at merge-base cabedd1d9680 with gradle/solr/packaging.gradle reverted and the head BATS file copied in, confirming modules/ltr/example is absent from the assembled distribution; head run at 32b594f280c5, then bats solr/packaging/test/test_modules.bats with SOLR_TIP set to the distribution directory ('ltr module ships its example directory'). If the premise holds, the lane finishes the gate (packaging commit removing SOLR-10667-TESTING.md, changelog parse, tidy guard, BATS check at the packaged head), pushes the packaged branch, and writes receipts/SOLR-10667.md. If the premise does not hold, the lane records NO GATE by finding with the run evidence.

Heartbeat: 2026-10-11T04:46:00Z (claim).
