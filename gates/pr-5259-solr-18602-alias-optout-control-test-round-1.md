# Gate: SOLR-18602 alias opt-out control test, round 1

Status: OPEN. Implemented, Proof Pending.

- Tags: `gate`. Intended host: any capable Linux pool host with JDK 21, Git, and the pinned Solr Gradle/build dependencies.
- Source: Windows implementation claim `claims/pr-5259-solr-18602-alias-optout-control-test-windows-round-1.md` and report `reports/pr-5259-solr-18602-alias-optout-control-test-round-1.md`.
- Fork branch: `nick-boss-tech/solr:solr-18602-pr5259-alias-optout-test`.
- Exact implementation head: `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`.
- Parent / baseline: contributor PR #5259 head `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`.
- Scope: `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java`; test `deleteReplicaByCountDoesNotFollowAliasesWhenDisabled`.
- Receipt target: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-1.md`.

## Preflight and evidence

Read the live `WORKFLOW.md` and `START-HERE.md`, check for competing claims, and verify that the fork branch still points to the exact implementation head. Claim this gate before running it. Use separate clean checkouts for the parent baseline, pushed head, and synthetic mutation. Record the host, UTC start, JDK/toolchain, build lock, detached checkout paths, runner, exact commands, full raw logs, fresh XML/results, and actual test counts in the receipt. Preserve each run's outputs before the next `cleanTest` removes them.

## Formatting and required checks

At the exact implementation head, run `./gradlew tidy` and require a clean tree. If tidy changes files, do not claim proof for the old head: make a scoped Nick-authored commit, push it plainly to the fork, and post a superseding gate for the new head before testing. Run `./gradlew check -x test` at the verified implementation head as required by the branch's `AGENTS.md`. Record the actual result and any material environment condition. Finish with `git diff --check` and a clean tree at the exact verified head.

## Positive proof: parent and implementation

On a clean detached checkout of parent `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`, run the original focused class:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.DeleteReplicaTest -Ptests.seed=18602C0DE
```

Require zero failures, errors, or skips and record the executed count. This establishes that the original PR class passes before the new control test is added.

On a clean checkout of implementation head `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`, run the same command and seed. Require zero failures, errors, or skips; verify that the new named test appears in fresh XML and record the full class count. Both positive legs must pass.

## Coverage-only fail-before mutation

Use a separate clean checkout of the implementation head. Keep `DeleteReplicaTest.java` byte-identical to the pushed version and record its SHA-256. Make one uncommitted synthetic production mutation in `DeleteReplicaCmd.java`: replace the `FOLLOW_ALIASES` conditional in `deleteReplicaBasedOnCount` with unconditional `resolveSimpleAlias(extCollectionName)`. Do not include any other change.

Run only the new test with the fixed seed:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.DeleteReplicaTest.deleteReplicaByCountDoesNotFollowAliasesWhenDisabled -Ptests.seed=18602C0DE
```

Require the test to execute and fail at its expected-`SolrException` assertion because the alias is now resolved and the deletion succeeds. Retain the mutation diff, unchanged test SHA-256, fresh log and XML, and the exact assertion failure. A compile/startup failure, skip, timeout, or unrelated failure is not mutation proof. This leg proves only that the new control assertion distinguishes the unconditional-resolution mutation.

## Completion

GREEN requires tidy, the required root check, both positive class runs, and the discriminating mutation leg, with fresh logs/XML and a clean implementation tree at the exact verified head. Keep the test in place and restore/discard the synthetic mutation checkout after recording its evidence; do not push the mutation. If any code or test changes, push the scoped repair and post a superseding exact-head gate. The receipt should distinguish coverage-only proof from a production defect: this follow-up pins the existing opt-out behavior and does not claim that the original PR has a false-branch bug.
