# Claim: SOLR-17722 premise run and build gate (round 2)

Claimant host: vm3. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-17722.md.
Capability tags: premise-run, gate. Staffing: 1.
Branch: `solr-17722-submit` at `fee3a26beb3c8289057ac41d94dfc8d83a969b71` (live tip verified at claim time).
Claim-time merge-base with live `apache/solr` main `d76fc14b9857e77b830d7a82514b28c523a239c2`: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
Started: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T06:29:50Z (UTC).

Status: BLOCKED. The base-only wire-probe task ended BUILD FAILED with 8 tests, 6 failures, 0 errors, 0 skipped, seed `8E23EBE00D9764B2`. All six failures were NPEs in inherited `HttpSolrClientTestBase.testQuery*` methods because the scratch subclass's `testQuerySetup` did not set the expected `a` parameter. The run is a harness failure, not premise evidence. No head run, no premise verdict, and no gate steps beyond test prerequisites. Stopped per WORKFLOW.md; no retry or relaunch until the main agent investigates.

Evidence: `/workspace/gates/logs/vm3/17722-base-wire-test.log`; fresh JUnit XML: `/workspace/gates/vm3-build/solr/solrj-jetty/build/test-results/test/TEST-org.apache.solr.client.solrj.jetty.Solr17722WireProbeTest.xml`. The target probe method is listed as passing in that failed class run, but no premise conclusion is drawn. Topic branch unchanged at `fee3a26beb3c8289057ac41d94dfc8d83a969b71`; local builder restored to that clean tip. The SOLR-17722 receipt remains unchanged and still says the premise is unproven.
