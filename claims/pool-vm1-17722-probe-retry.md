# Claim: SOLR-17722 corrected premise probe run on vm1

Claimant host: vm1. Job: gates/SOLR-17722.md.
Capability tags: premise-run. Staffing: 1.
Started: 2026-10-11T06:43:08Z (UTC).
Branch: `solr-17722-submit` at `fee3a26beb3c8289057ac41d94dfc8d83a969b71`. Merge-base: `56ec140e3636d5f4150fa87fbf7103529536ac99`.

Status: DONE as a duplicate, superseded by the earlier vm3 claim under first-push-wins. The vm3 claim started at 05:53:14Z and its main-agent-authorized base-only retry began at 06:42:53Z, before this VM1 claim was posted. The vm3 run passed the exact base-only probe; its receipt records NO GATE because the captured wire already uses `wt=javabin` and not `wt=json`. The gate is closed. No VM1 test result is asserted in this record. Do not run the head leg or any broader gate for this item.

Evidence for the completed item: `/workspace/gates/logs/vm3/17722-base-wire-isolated-method-retry-20261011T064253Z.log` and `/workspace/gates/vm3-build/solr/solrj-jetty/build/test-results/test/TEST-org.apache.solr.client.solrj.jetty.Solr17722WireProbeTest.xml`; see `gates/SOLR-17722.md` and `receipts/SOLR-17722.md` for the full result. No other assignment is claimed here.
