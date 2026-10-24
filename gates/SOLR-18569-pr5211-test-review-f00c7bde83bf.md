# Gate job: SOLR-18569 PR 5211 test review, pinned head f00c7bde83bf

Status: CLAIMED (vm6, claim `claims/pr-5211-test-review-gate-round-1.md`).

- Ticket: SOLR-18569. Upstream PR: apache/solr #5211 (Jason Gerlowski), reviewed head `6f72e0cac151d139626299a680f90a7c9c123ceb`, merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Branch: `solr-18569-pr5211-test-review` on nick-boss-tech/solr. Pinned head: `f00c7bde83bf5d82ac4607ecf086a4397eca563b` (one commit by Nick Shanin on the PR head; read back by ls-remote at claim time). Never move this gate head. A new head needs a superseding job file.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.ZkControllerTest`. Sensitivity method: `testStartupWaitsForLeftoverEphemeralNodesToDisappear`. Seed: `18569C0FFEE01`.
- Host and runner: vm6, `pool/bin/bg-steps.sh` with `pool/bin/sweep-run.sh` under the host build lock. Preflight: `pool/bin/gate-preflight.sh`, tidy clean at the pinned head (log `/workspace/gates/logs/vm6/pr-5211-gate/preflight.log`).
- Mutation for legs 1 and 2: in `solr/core/src/java/org/apache/solr/cloud/ZkController.java`, the body of package-private `checkForExistingEphemeralNode()` is emptied; the signature is unchanged. Local scratch worktrees only, not pushed.

## Legs

1. Original-test control. Worktree at PR head `6f72e0cac151`, mutation applied. Clean, then the original positive method alone. Expected: PASS. This shows the original test does not detect an early return.
2. Strengthened negative control. Worktree at `f00c7bde83bf`, mutation applied. Clean, then the strengthened positive method alone. Expected: FAIL with the assertion `should be gone when the check returns`, raised from the test. A compile, harness, worker or timeout failure is not sensitivity proof.
3. Unmutated final head. Worktree at `f00c7bde83bf`, clean. Tidy, Error Prone compile, then clean and the whole focused class. Expected: PASS. Then `:solr:core:check -x test`.

## Steps

- Leg 1 steps: `l1-clean|:solr:core:cleanTest`; `l1-original-positive|:solr:core:test --tests org.apache.solr.cloud.ZkControllerTest.testStartupWaitsForLeftoverEphemeralNodesToDisappear -Ptests.seed=18569C0FFEE01`.
- Leg 2 steps: `l2-clean|:solr:core:cleanTest`; `l2-strengthened-negative|:solr:core:test --tests org.apache.solr.cloud.ZkControllerTest.testStartupWaitsForLeftoverEphemeralNodesToDisappear -Ptests.seed=18569C0FFEE01`.
- Leg 3 steps: `l3-tidy|tidy`; `l3-errorprone|:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`; `l3-clean|:solr:core:cleanTest`; `l3-full-class|:solr:core:test --tests org.apache.solr.cloud.ZkControllerTest -Ptests.seed=18569C0FFEE01`; `l3-module-check|:solr:core:check -x test`.

## Result

Filled in by the gating host after the run. Job DONE is written in the same push as the receipt or the failure verdict.
