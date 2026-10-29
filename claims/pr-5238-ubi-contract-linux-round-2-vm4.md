# Claim: listener UBI contract proof, Linux round 2 (vm4)

Status: CLAIMED (Linux positive proof at superseding head)

- Claimant host: vm4.
- Started: 2026-10-29T14:45Z (UTC).
- Job: `gates/pr-5238-ubi-contract-round-2-vm4.md`, superseding round 1 at `de4d222bb1`.
- Candidate: `c2c387064a83def34cece4163eccded28318caeb` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`, verified live after the fork push. The repair is formatting only and was pushed fast-forward by vm4 with main-agent authorization.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Competing claims: none for this head. Round-1 claim `claims/pr-5238-ubi-contract-linux-round-1-vm4.md` ended at the tidy halt.
- Plan: reuse the standalone full clone at `/workspace/worktrees/gate-5238-clone`, checked out detached at the new head, then run the round-2 driver under the host lock. Logs under `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-2/`.
- Constraints: no GitHub PR, comment, or Jira write. No further fork push unless tidy changes files, in which case the run stops and reports. No force push.
