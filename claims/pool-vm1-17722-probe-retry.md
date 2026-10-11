# Claim: SOLR-17722 corrected premise probe run on vm1

Claimant host: vm1. Job: gates/SOLR-17722.md.
Capability tags: premise-run. Staffing: 1.
Started: 2026-10-11T06:43:08Z (UTC).
Branch: solr-17722-submit at fee3a26beb3c8289057ac41d94dfc8d83a969b71 (live tip, per gates/SOLR-17722.md). Merge-base: 56ec140e3636d5f4150fa87fbf7103529536ac99.

Why a corrected run on vm1: vm3's base premise attempt (gates/SOLR-17722.md, Failed run record) ran the scratch wire-probe class without a method filter. The probe class extends HttpSolrClientTestBase, so its inherited testQuery* methods also ran, and six of them failed with NullPointerException because the probe's testQuerySetup did not set the "a" parameter those methods expect. That is a harness cause in the probe setup, not premise evidence; the probe method itself, testRawWireQueryOverridesRequestWtJson, passed on base. Main-agent decision: rerun the probe with the inherited tests excluded.

Correction: run only the probe method, by method-level --tests filter (org.apache.solr.client.solrj.jetty.Solr17722WireProbeTest.testRawWireQueryOverridesRequestWtJson), at merge-base and at the live tip, with the scratch probe recreated outside the branch per reports/build-docs-misc-round-1-g5.md. Judge by the gate file's rule: the premise holds only if the base run shows the wt param mirrored as the ticket describes and the head run shows it corrected. If the premise does not hold, record NO GATE by finding in receipts/SOLR-17722.md with the run evidence and mark the job and this claim DONE. If it holds, record the premise outcome in the gate file and mark this claim DONE; the gate itself is a separate later job.

Heartbeat: 2026-10-11T06:43:08Z (claim).
