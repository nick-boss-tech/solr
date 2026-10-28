# Gate: SOLR-18589 alias lock follow-up, round 1

Status: OPEN

- Tags: gate, premise-run, review. Intended host: any capable Linux gate host with JDK 21 and the pinned branch's Gradle/build dependencies, one gate at a time under its host build lock.
- Source: Nick's request to implement and gate both PR 5255 review findings.
- Fork branch: `nick-boss-tech/solr:solr-18589-submit`.
- Exact implementation head: `fd49245a38ba779f78840477dbf6db34b92c5f6b`.
- Parent / negative proof base: contributor's PR 5255 head `1edd6a113b2a9e1b029f509c9279f4fb58683bba` (`sarkaramrit1993/solr:alias-lock-key-18589`).
- Implementation claim: `claims/pr-5255-followup-windows-round-1.md`.
- Implementation report: `reports/pr-5255-alias-lock-followup-windows-round-1.md`.
- Receipt target: `receipts/PR-5255-alias-lock-followup.md`.
- State: Implemented, Proof Pending. Pushed fork head verified; clean implementation worktree and git show --check. No build/test ran on Windows.

## Preflight

Read live WORKFLOW.md and START-HERE.md, check competing gate claims and the current fork head, then claim this job. Record host, UTC start, JDK/toolchain, build lock, detached checkout, background runner, full raw logs, fresh XML/output paths, and results. Retain the full source tree. Confirm the head is exactly one commit above the named parent and review only that follow-up delta: 19 files, 330 additions, 11 deletions.

The repairs reject execution when a command resolves a different source collection from its acquired lock (409, retry), and preserve structured 400 errors from Overseer lock acquisition. The original alias message is retained. The report names the guarded command paths and exclusions. This gate changes only Nick's derived fork branch and board records. No contributor-branch or apache/solr write is authorized.

## Formatting and positive proof

At the exact head, run `./gradlew tidy` and require a clean tree. If tidy changes files, commit only scoped formatting with Nick Shanin's ICLA identity, push plainly to the fork, and add a superseding gate naming the new exact head before proving it. Do not edit this immutable job or claim proof for a different head. Record inherited PR formatting separately.

Run the two focused classes from fresh results:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.api.collections.AliasCollectionApiLockTest --tests org.apache.solr.cloud.api.collections.AliasCollectionApiOverseerTest -Ptests.seed=5255C0DE
```

Require six executed tests, zero failures/errors/skips. Retain full logs, fresh XML, and class outputs before cleaning results again. Repeat these two classes once with `-Ptests.seed=18589C0DE`, preserving that evidence separately.

Inspect these results explicitly:

- Distributed retargeting reaches the intended wait barrier (DELETE remains SUBMITTED behind A before retargeting), then returns FAILED/409 after X -> B and A's release while B stays locked. Both collections and X -> B survive; B's lock stays acquired. The fresh retry after releasing the locks succeeds and deletes B plus X while retaining A.
- Overseer retargeting acquires the handler's A lock before changing X -> B and calling processMessage. It must return 409 and preserve both collections and X -> B. This is a direct, controlled handler test; do not describe it as a queued Overseer scheduling test.
- Synchronous multi-collection-alias DELETE returns 400 with the intended validation message, not a ClassCastException/500.
- Async multi-collection-alias DELETE reaches FAILED; REQUESTSTATUS returns a structured NamedList exception with rspCode 400 and the validation message.
- Stable followed-alias deletion still removes the intended collection and alias; the inherited distributed DELETEREPLICA test still waits, completes after release, and deletes the selected replica.

Run the affected neighboring classes, also preserving fresh evidence:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.TestLockTree --tests org.apache.solr.cloud.api.collections.CollectionApiLockingTest --tests org.apache.solr.cloud.api.collections.DeleteCollectionLockCleanupTest --tests org.apache.solr.cloud.OverseerCollectionConfigSetProcessorTest --tests org.apache.solr.cloud.AliasIntegrationTest --tests org.apache.solr.cloud.DeleteReplicaTest --tests org.apache.solr.cloud.CollectionsAPISolrJTest -Ptests.seed=5255C0DE
```

Require zero failures/errors/skips and record actual counts. Pay attention to callingLockId mirroring, subpath validation, nested command contexts, collection/shard exclusion and DELETE lock-directory cleanup. The count-based DeleteReplica alias-support defect SOLR-18602 is not implemented here; its actual source collection is checked against the acquired lock so a mismatch cannot execute.

Run `./gradlew check -x test` as required by the branch AGENTS.md, including its normal compilation/static checks. Preserve the actual result and any material environment condition; do not silently exclude tasks. Finish with git diff --check and a clean tree at the exact verified head.

## Negative proof: original PR production code with follow-up tests

Use a separate clean detached checkout of parent `1edd6a113b2a9e1b029f509c9279f4fb58683bba`. Overlay ONLY these two test files from the implementation head (or its superseding formatting head):

- `solr/core/src/test/org/apache/solr/cloud/api/collections/AliasCollectionApiLockTest.java`
- `solr/core/src/test/org/apache/solr/cloud/api/collections/AliasCollectionApiOverseerTest.java`

Keep every production file at the parent. These tests use no new production API. Record the synthetic tree and test-file hashes. Run the same two-class command with seed 5255C0DE. Require six executed tests: exactly four intended failures and two passing stability controls:

1. `testAliasRetargetedWhileWaitingDoesNotDeleteUnlockedCollection`: DELETE incorrectly completes instead of FAILED/409 after retargeting, despite B's held lock.
2. `testAliasRetargetedAfterLockDoesNotDeleteUnlockedCollection`: original handler executes against B and returns no expected conflict error instead of preserving B.
3. `testMultiCollectionAliasReturnsBadRequest`: original lock failure response becomes ClassCastException/500 instead of 400.
4. `testAsyncMultiCollectionAliasPreservesErrorDetails`: original stored exception is a string instead of the required structured NamedList/error details.

The stable-alias deletion and inherited distributed lock tests must pass. Capture the actual assertions and observed values. Compilation errors, startup failures, timeout before the synchronization barrier, skips, missing methods, or unrelated teardown failures do not prove either finding. If a method passes on the parent, investigate and correct the premise or test; never label a passing negative leg as proof.

## Completion and repair

GREEN requires both exact-head focused seeds, neighboring suites, clean tidy, the required root check, and discriminating fail-before proof for both findings, with fresh XML and raw logs retained. An executed failure is investigated and repaired under WORKFLOW.md, not retried blindly. An environment refusal before any test execution may be repaired and relaunched once with its cause recorded.

If source/tests change, commit and push the scoped repair and add a superseding immutable gate at the new head before running proof. Do not change this job's proof base or acceptance bar silently. Record original and final heads, commands/seeds, counts/skips, negative assertion messages, JDK/toolchain, runner/evidence locations, any environment conditions, and limitations in the receipt. Include a concise suggested contributor follow-up as the final section, with the tested diff and pinned fast-forward commands, but do not post it publicly as part of this gate.
