# Gate: PR #5098 direct leader-only routing follow-up, round 1

Status: OPEN. Immutable once posted. Proves only the pinned final head below.

- Required tags: `gate`, `review`, `records`. Intended host: any available capable Linux gate host with JDK 21, the pinned Gradle 9.7.0 distribution and this branch's build dependencies. Serialize under the host build lock.
- Fork: `nick-boss-tech/solr`. Branch: `solr-18530-submit`.
- Parent O: `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`.
- Final head F: `d822b88b1bd58c197e4ad09c53ffff0920b76190`.
- Public PR: [apache/solr #5098](https://github.com/apache/solr/pull/5098), open at F when this gate was prepared.
- Implementation report: `reports/pr-5098-direct-leader-followup-implementation-round-1.md`.
- Receipt: `receipts/PR-5098-direct-leader-only-round-1.md`, owned by the execution host.
- Status at handoff: Implemented, Proof Pending. No Gradle task, compiler, formatter or test ran on Windows.

This is the mainline PR review-follow-up gate for the test configuration correction. It is not the separate RC3 matched-pair assignment and must not be presented as RC3 proof. The earlier `receipts/SOLR-18530.md` green result covers `98e5368d99`; its carryover to O was for a changelog-only delta. It does not cover F.

The change is test-only. It exposes the existing `CloudSolrClient.Builder.sendDirectUpdatesToShardLeadersOnly()` setting through the existing test helper, keeps the older overload behavior unchanged, and asserts both leader routing settings in `RecoveryAfterSoftCommitTest.test`. There is no changelog fragment.

## Claim and repair rules

Before claiming, verify the live fork ref equals F and inspect this gate, current claim state and the receipt path. Run the detached tidy preflight at F. Record the host, JDK, workspace, build lock, runner, local worktrees and exact log/XML paths in a new add-only execution claim. Use separate clean worktrees for O, F and the synthetic mutation. Preserve each test's fresh XML and output before cleaning for the next seed.

If tidy or a source/test repair changes tracked files, record the repair and obtain a superseding pushed head and gate. Do not treat any result at O or an intermediate head as proof for F. An executed test failure requires investigation before retry; a pre-test environment refusal may be relaunched once under WORKFLOW.md.

## 1. Original-head comparison at O

Use a fresh detached worktree at O and run:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=A57A22E346C237C6
```

Expect one executed test, zero failures, zero errors and zero skips. This is a baseline comparison only; it is not a fail-before leg. Record and investigate any different outcome without calling it mutation proof.

## 2. Final-head positive proof at F

In a fresh worktree at F, record the full head and tree. Require `git diff --check` to pass. Run `./gradlew tidy` and require both rc 0 and no tracked changes. A tidy change invalidates this gate's exact-head proof.

Run the focused method separately at each seed below, using a fresh test output directory (or archive the logs/XML after each invocation before the next `cleanTest`):

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=A57A22E346C237C6
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000001
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000002
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000003
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000004
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000005
```

Expect one executed test and zero failures, errors or skips for every seed. Preserve each fresh log and XML and record actual return codes and counts.

Run the root check:

```bash
./gradlew check -x test
```

Require success without additional exclusions. Finish with a clean tree, a clean `git diff --check`, and a separate remote-ref read confirming F remains the live `solr-18530-submit` head.

## 3. Discriminating configuration mutation

In a separate fresh worktree at F, leave the test file unchanged and make one compiling mutation in `AbstractFullDistribZkTestBase`: omit the call to `builder.sendDirectUpdatesToShardLeadersOnly()` from the new overload. Do not push the mutation. Save its one-hunk diff and hashes for the mutated helper and unchanged test.

Run the same focused method and seed:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=A57A22E346C237C6
```

Expect exactly one executed test and one assertion failure at `assertTrue(leaderClient.isDirectUpdatesToLeadersOnly())`; the preceding `isUpdatesToLeaders()` assertion must pass. Compilation errors, setup failures, missing tests, skips, unrelated failures or a pass do not satisfy this leg. This proves the new assertion detects omission of the direct-leader-only option; do not describe it as a production fail-before result.

Restore exact F in a clean worktree and rerun the same command and seed. Expect one pass with zero failures, errors or skips. Preserve the restored run's fresh logs/XML.

## Completion

GREEN requires the O comparison, all six positive runs at F, the specified compiling mutation failure, the restored positive run, clean tidy, root `check -x test`, and the final remote ref at F. Record actual host/JDK, commands, seeds, return codes, test counts, fresh artifact paths and hashes, mutation diff/hashes, tree state, and any environment condition in the execution claim and receipt. Keep the receipt distinct from the RC3 matched-pair records.

Do not post a PR reply until the receipt is green for F and the live PR head and discussion have been refreshed.
