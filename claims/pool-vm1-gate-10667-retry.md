# Claim: SOLR-10667 premise retry and first gate on vm1

Claimant host: vm1. Job: gates/SOLR-10667.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T04:46:00Z (UTC).
Branch: solr-10667-submit at 32b594f280c5c5f0e5241dadbad7677cdb563c3e (live tip, verified by ls-remote at claim time).

Why a retry on vm1: vm2's round is BLOCKED on environment (claims/pool-vm2-gate-backlog-10667.md): both :solr:packaging:assembleDist runs failed in :solr:webapp:js-client:jsClientDownloadDeps with "Cannot find package '@babel/plugin-syntax-dynamic-import'" before any test ran. The main side's decision authorizes a retry on vm1 with a clean npm cache. If the same babel package failure reproduces on vm1, the lane stops and records an environment block with the log evidence; no build-script changes are improvised.

Premise, per gates/SOLR-10667.md: base run at merge-base cabedd1d9680 with gradle/solr/packaging.gradle reverted and the head BATS file copied in, confirming modules/ltr/example is absent from the assembled distribution; head run at 32b594f280c5, then bats solr/packaging/test/test_modules.bats with SOLR_TIP set to the distribution directory ('ltr module ships its example directory'). If the premise holds, the lane finishes the gate (packaging commit removing SOLR-10667-TESTING.md, changelog parse, tidy guard, BATS check at the packaged head), pushes the packaged branch, and writes receipts/SOLR-10667.md. If the premise does not hold, the lane records NO GATE by finding with the run evidence.

Heartbeat: 2026-10-11T04:46:00Z (claim).

Heartbeat: 2026-10-11T05:06:00Z (gate runner done).

Result 2026-10-11T05:06:00Z: DONE, GATE GREEN at packaged head 1e3f022552be4d281a020e015f4c8c05ce4eec6c (pushed to the fork, ls-remote verified). Premise holds by run: the base distribution (cabedd1d9680, pre-fix packaging.gradle, head BATS file copied in) lacks modules/ltr/example; the head distribution has it with config.json, train_and_upload_demo_model.py, and exampleFeatures.json; the BATS check passes 1 of 1 at the head distribution and again at the packaged head. Changelog parse rc=0, tidy clean. The vm2 babel failure does not reproduce on vm1 with a fresh npm cache: jsClientDownloadDeps executed and passed at both legs. Distributions were assembled with :solr:packaging:installFullDist because assembleDist's own task action requires -Psolr.ui.buildFromSource=true on this tree (hit in run 1 after all dependency tasks passed). Gate log g10667-gate.log on vm1; receipt receipts/SOLR-10667.md; gates/SOLR-10667.md marked DONE.
DONE: 2026-10-11T05:06:00Z (UTC), outcome GATE GREEN.
