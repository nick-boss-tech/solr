# Closeout: listener UBI contract proof, Linux round 1 (vm4)

- Gate: `gates/pr-5238-ubi-contract-round-1.md`. Claim: `claims/pr-5238-ubi-contract-linux-round-1-vm4.md`.
- Outcome: HALTED at tidy. No test ran. Not green.
- Head tested: `de4d222bb12302eb0e10aeb5697bae307eb2cd22` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`. Clone and tidy ran at this head.
- Failure: `tidy-clean-tree` failed after `tidy` exited 0. Tidy rewrapped two `cluster.getSolrClient().request(...)` calls in `SearchRequestListenerUbiTest.java`. Formatting only. Log: `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-1/tidy.log` and `state.txt`.
- Repair: `c2c387064a83def34cece4163eccded28318caeb`, "PR 5238: tidy UBI contract test formatting", pushed fast-forward to the fork with main-agent authorization.
- Superseded by round 2: `gates/pr-5238-ubi-contract-round-2-vm4.md` at `c2c387064a`. The round-2 proof is green. Receipt `receipts/PR-5238-ubi-contract.md`. Report `reports/pr-5238-ubi-contract-linux-round-2.md`.

## Consequences

- No test result exists for `de4d222bb1`. Do not cite round 1 as a proof.
- A review comment or PR reference should use `c2c387064a`, the head that the round-2 receipt covers.
- Round 1 does not need a rerun. Its gate job file and claim are unchanged except for the closing line in the claim.
