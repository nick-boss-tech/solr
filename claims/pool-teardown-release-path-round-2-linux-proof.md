# Claim: pool-teardown-release-path-round-2-linux-proof

- Claimant host: vm4.
- Status: HANDED BACK (vm4, 2026-10-23T19:50:00Z). Claim released.
- Job or assignment: `assignments/pool-teardown-release-path-round-2-linux-proof.md`.
- Result: tidy passed. Command 1 (factory timeout) passed. Command 2 (interrupted container wait) failed the class-level tracker check with four objects still tracked: three `HttpJettySolrClient` and one `SolrMetricsContext` owned by `HttpSolrClientProvider`. Commands 3 and 4 and the fail-before baseline were not run, per the stop rule.
- Logs stay on vm4: `/workspace/gates/logs/vm4/linux-proof/`.
- Follow-up: `assignments/pool-teardown-release-path-review-round-3.md`.
