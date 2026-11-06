# Gate: SOLR-18530 PR 5098 review clarity follow-up, round 1

Status: OPEN. Immutable once posted; receipt determines completion.

- Required tags: `gate`, `review`, `records`. Intended host: an available Linux gate host with JDK 21 and the branch's pinned Gradle 9.7.0; serialize under its local build lock.
- Fork: `nick-boss-tech/solr`; branch: `solr-18530-submit`; parent `d822b88b1bd58c197e4ad09c53ffff0920b76190`; exact head H `981b7c230bdd1428b60867dd1ea700576b94b393`. PR #5098 was open at H when posted.
- Implementation report: `reports/pr-5098-review-clarity-implementation-round-1.md`. Execution host writes new receipt `receipts/PR-5098-review-clarity-round-1.md` and an add-only claim.
- Status: Implemented, Proof Pending. No Gradle, compilation, formatting or tests ran on Windows. No changelog fragment: test-only change.

This follows the green direct-leader-only gate at parent `receipts/PR-5098-direct-leader-only-round-1.md`. The only changes from that parent are a source-comment correction and renaming/documenting the boolean parameter and its reference. Do not claim the parent receipt as an H run or treat this as RC3 matched-pair proof.

## Claim and preflight

Confirm live fork and PR heads are H; check for existing claims and receipt before taking the job. Use a clean detached H worktree and a separate clean H mutation worktree; record host, JDK, runner, lock, artifact paths and head. Run detached tidy preflight at H and require clean tracked output and `git diff --check`. A formatting repair changes the head and needs a superseding gate. An executed test failure requires investigation before retry, following `WORKFLOW.md`.

## Focused proof at H

Run each seed independently, preserving fresh logs and XML before the next `cleanTest`. Require one executed test and zero failures, errors and skips for each:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=A57A22E346C237C6
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000001
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000002
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000003
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000004
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test -Ptests.seed=18530C0DE0000005
./gradlew check -x test
```

Require root check success without extra exclusions; use a clean standalone H clone if the runner refuses a Git worktree. Record return codes, JUnit counts, log/XML paths and hashes, and final clean tree.

## Configuration sensitivity mutation

In the separate H worktree, leave the test unchanged and omit only `builder.sendDirectUpdatesToShardLeadersOnly()` from the helper; keep it compiling. Save the mutation diff and hashes of helper and unchanged test. Run the focused method at seed `A57A22E346C237C6` as above. Require exactly one executed test and one assertion failure at `assertTrue(leaderClient.isDirectUpdatesToLeadersOnly())`, with the preceding leader-routing assertion passing. A compilation or setup failure, skip, unrelated failure or pass does not meet this leg. This is a synthetic test-configuration mutation, not a reproduction of the intermittent production failure. Restore H in a clean worktree and rerun the same seed, requiring one passing test with no skips, errors or failures.

## Completion

GREEN requires clean H tidy, all six H seed runs, the specified compiling mutation and restored H pass, root `check -x test`, clean trees, and a final remote ref read matching H. Record commands, conditions, actual counts and return codes, fresh logs/XML and hashes, mutation diff, and final fork/PR heads in the receipt. No PR comment, PR body edit, JIRA write or other public communication: Nick holds the draft for review.
