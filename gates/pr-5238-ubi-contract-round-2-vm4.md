# Gate: listener UBI contract proof, round 2 (superseding head)

Status: OPEN

- Supersedes: `gates/pr-5238-ubi-contract-round-1.md` at `de4d222bb12302eb0e10aeb5697bae307eb2cd22`. The round-1 run halted at the tidy clean-tree check, before any test ran. The round-1 job file is unchanged.
- Exact candidate: `c2c387064a83def34cece4163eccded28318caeb` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`, one commit above `de4d222bb1`.
- Repair: `PR 5238: tidy UBI contract test formatting`, formatting only. It rewraps two `cluster.getSolrClient().request(...)` calls in `SearchRequestListenerUbiTest.java`.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Executing host: vm4. Claim: `claims/pr-5238-ubi-contract-linux-round-2-vm4.md`.
- Requirements: identical to round 1 (two seeds of `SearchRequestListenerUbiTest`, `SearchRequestListenerTest` with 19 methods, the four neighbor classes, root `check -x test` with NODE_ENV unset, and clean tidy at the exact head). The round-1 job file gives the commands and interpretation rules and applies unchanged.
- Receipt target: `receipts/PR-5238-ubi-contract.md`, covering the round-2 head only.
