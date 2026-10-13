# Gate job: SOLR-18541 review patch, round 1 (strong review of the four new draft PRs)

- Branch: `review-r1-solr-18541` on the fork. Head: `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`. Parent chain: two review commits on `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2` (the side branch `solr-18541-combined-query-merge-totals`, gated green on vm2), whose parent is `d5ed34125db9582b584bbfdd149a4de4b40650d0`, the head of draft PR #5117. Not the PR branch.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `reports/four-new-prs-strong-review-round-1.md`, section SOLR-18541.
- Job type: full focused gate with a fail-before run.

## Changes to check

1. `CombinedQueryComponent.mergeIds`: a skipped shard now always clears the merged numFoundExact, replacing the side branch's condition `docs != null && docs.getNumFound() > 0`. Reason: the real timeAllowed exit shape (`reports/combinedquerycomponent-premise.md`, real-exit addendum, Q1) sends no per-query entry for the sub-query it did not reach, and an exit during the main search sends an empty result with zero hits and an exact flag. Neither shape reports hits, so the hits-only condition cannot see them.
2. Changelog fragment sentence: "...and no shard was skipped."
3. `CombinedQueryComponentMergeTotalsTest`, now 10 tests (the side branch has 8):
   - new `skippedPartialShardWithoutCountClearsExactness`: skipped shard with `response_per_query` absent, merged shard exact with 3 documents: numFound 3, numFoundExact false, 3 documents.
   - new `skippedPartialShardWithoutHitsClearsExactness`: skipped shard with numFound 0 and an exact flag, merged shard exact: numFound 3, numFoundExact false, 3 documents.
   - `skippedPartialShardApproximateTotalCounted` is renamed `skippedPartialShardApproximateTotalExcluded`. No change to its assertions.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/review-r1-solr-18541` equals `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`.
2. Diff scope against `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2`: the changelog fragment, `CombinedQueryComponent.java` and `CombinedQueryComponentMergeTotalsTest.java` only.
3. Changelog YAML parse.
4. Tidy first; same rule as the SOLR-18539 job for a formatting-only diff in these files.
5. Error Prone compile.
6. Focused run: `CombinedQueryComponentMergeTotalsTest`, fresh JUnit XML. Expect 10 tests, 0 failures. Record the seed.
7. Fail-before against the side branch production: revert `CombinedQueryComponent.java` to `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2`, keep the branch tests. Expect failures in exactly two tests: `skippedPartialShardWithoutCountClearsExactness` and `skippedPartialShardWithoutHitsClearsExactness`, each `numFoundExact expected:<false> but was:<true>`. Any other failure is a finding.
8. Module check `:solr:core:check -x test`.
9. On green: add a section to `receipts/SOLR-18541.md` for this review branch head. Do not push to `combinedquerycomponent-merge-totals` and do not edit PR #5117. Mark this job DONE.

## Checklist

- [ ] head confirmed
- [ ] diff scope
- [ ] changelog parse
- [ ] tidy
- [ ] Error Prone compile
- [ ] focused run, 10 tests
- [ ] fail-before against the side branch, the two named tests only
- [ ] module check
- [ ] receipt section; job DONE

## Claim (vm1, 2026-10-13T17:10Z)

- Claimed by vm1: claims/pool-vm1-gate-18541-review-r1-patch.md.
- Runner: ~/workspace/tools/gate-strong-review-vm1-chain.sh (one chained runner for the four strong-review jobs vm1 claimed in this batch, serial on the test-queue flock). Log: ~/workspace/tools/g18541-r1-gate.log. Worktree: ~/workspace/solr-worktrees/strong-r1-18541-gate.
- The focused run uses seed C0FFEE42C0FFEE42 (the seed this test class was gated with at the side branch head); the job names no seed, so the runner records the one it uses here in the checklist when it runs.
