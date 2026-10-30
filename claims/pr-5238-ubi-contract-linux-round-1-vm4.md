# Claim: listener UBI contract proof, Linux round 1 (vm4)

Status: CLAIMED (Linux positive proof)

- Claimant host: vm4.
- Started: 2026-10-29T14:20Z (UTC).
- Job: `gates/pr-5238-ubi-contract-round-1.md` (OPEN, intended host: capable Linux Solr gate host). The job file is not edited.
- Candidate: `de4d222bb12302eb0e10aeb5697bae307eb2cd22` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`, verified live before claiming.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`. Diff against the parent is one added test file, `SearchRequestListenerUbiTest.java`, with no non-test paths.
- Competing claims: `claims/pr-5238-ubi-contract-windows-round-1.md` (Windows implementation, not a Linux run). No other live Linux claim for this job.
- Plan: preflight tidy at the exact head in a detached worktree, then the two seed runs of `SearchRequestListenerUbiTest`, `SearchRequestListenerTest`, neighbor classes, and a clean standalone-clone root `check -x test` with NODE_ENV unset. Logs under `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-1/`.
- Constraints: no GitHub PR, comment, or Jira write. No fork push unless tidy changes files, in which case the run stops and reports. No force push.

Status: CLOSED (halted at tidy at `de4d222bb1`; superseded by round 2 at `c2c387064a`)

CLOSED: `reports/pr-5238-ubi-contract-linux-round-1-closeout.md`. No test ran at round 1. Proof is in `receipts/PR-5238-ubi-contract.md`.
