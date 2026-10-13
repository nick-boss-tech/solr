# Claim: SOLR-18541 review patch gate, round 1 (vm1)

- Claimed: 2026-10-13T17:10Z by vm1. Job: gates/SOLR-18541-review-r1-patch.md.
- Capability tags: gate.
- Slice: the whole job. Full focused gate with fail-before on the review branch `review-r1-solr-18541` at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`: changelog parse, tidy (the SOLR-18539 rule for a formatting-only diff in the in-scope files), Error Prone compile, focused run of `CombinedQueryComponentMergeTotalsTest` (expect 10 tests, 0 failures), fail-before against the side branch production at `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2` (expect failures in exactly `skippedPartialShardWithoutCountClearsExactness` and `skippedPartialShardWithoutHitsClearsExactness`), module check. No push to `combinedquerycomponent-merge-totals` from this claim; on green the gated head becomes eligible for that move by the main side.
- Host paths on vm1: worktree ~/workspace/solr-worktrees/strong-r1-18541-gate; runner ~/workspace/tools/gate-strong-review-vm1-chain.sh (one chained runner for the four jobs claimed by vm1 in this batch, serial on the test-queue flock); log ~/workspace/tools/g18541-r1-gate.log.
- Status: DONE (closed 2026-10-13; see the closing line).
- Heartbeat: 2026-10-13T17:10Z claim pushed, runner being launched.
- Closed: 2026-10-13 by vm1. Status: DONE. The gate ran GREEN at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e` (CombinedQueryComponentMergeTotalsTest 10 of 10 at seed C0FFEE42C0FFEE42, fail-before confirmed in exactly the two named tests, module check clean); the result is recorded in the job file `gates/SOLR-18541-review-r1-patch.md` and in `receipts/SOLR-18541.md`.
