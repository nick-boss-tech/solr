# Gate: SOLR-18601 cleanup oracle and assertion repair, superseding round 3

Status: OPEN. Immutable once posted. This is a repaired input after an executed failure, not a blind rerun.

Supersedes gates/pr-5253-cleanup-superseding-98d1127-round-2.md for proof at the new exact head. Keep all earlier jobs and the NOT GREEN receipt receipts/PR-5253-cleanup-round-1.md as history.

## Input, ownership and preflight

- Exact positive head I: 6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4.
- Fork nick-boss-tech/solr, branch solr-18601-submit. Plain fast-forward push and GitHub ref read-back confirmed I.
- Previous failed input H: 98d1127e4f5d4d149992e1dc494b95928bac057a, parent of I. Its tidy/compile repairs are retained.
- Original contributor head O: 32b91612edde057c19bf1a7963e8276b816551df. PR 5253 and its contributor branch are not gate write targets.
- Repair claim: claims/pr-5253-cleanup-repair-windows-round-2.md. Decision/evidence report: reports/pr-5253-cleanup-repair-round-2.md. Read the report before claiming.
- Required tags: gate, review, records. Intended host: any capable Linux CPU host with JDK 21 and Gradle 9.7.0 / pinned dependencies. vm2 can reuse its retained evidence/layout if available. The old execution is stopped per its receipt; still verify that no build holds the host lock before starting.
- Use standalone clones, not Git worktrees, for root/Antora validation: vm2's earlier claim records the .git-file limitation. Suggested positive clone /workspace/worktrees/pr5253-I-clone, negative-A clone at O, negative-B clone at I, negative-C clone at H. Claimant records actual paths, JDK, cache, runner, logs/XML and build lock in its add-only claim.
- Serialize every Gradle invocation under the host build lock and normal disk/inode guards; vm2's lock is /workspace/tools/.gradle-vm2.lock. Suggested new log prefix/directory /workspace/gates/logs/vm2/solr18601-cleanup/repair-I/. Preserve the old logs.
- Before claiming execution, run detached/clean-clone tidy preflight at I under WORKFLOW.md. If tidy changes files, commit/push the formatting repair and post a new superseding exact-head gate before accepting proof. Source/test repairs after failures follow the same rule. No silent reruns, exclusions or amended assertions.
- Receipt destination: receipts/PR-5253-cleanup-repair-round-3.md, owned by the execution host. Preserve the earlier failed receipt; no public communication is requested.

The repaired oracle counts actual buffer operations via the existing TLOG gauge, not distinct versions. The envelope version may be zero for a buffered ADD whose document retains its positive source version; positive proof must verify the final equal source/target versions as well as document effects. Do not change production version assignment to satisfy the count. AssertionError now triggers guarded cleanup and is wrapped as a reported failed command to prevent Overseer Error-reset retry. Fatal/other unhandled Errors still propagate; automatic recovery is not promised there.

## Positive proof at I

Capture every Gradle exit code directly, retain per-leg full logs and fresh XML before cleanTest overwrites it, and record head/tree/hashes. No skips or missing named methods count as proof. Assertions must be enabled so the deterministic injection runs.

1. Clean tidy at I: `./gradlew tidy`, `git diff --check`, tracked/untracked clean. Parse the changelog YAML and retain its authors. A changed tree is a repair requiring another head/job.

2. Complete focused leg, eighteen methods at I (8 migration, 5 cleanup, 5 routing-mutator):

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest --tests org.apache.solr.cloud.overseer.SliceMutatorRoutingRuleTest -Ptests.seed=5253C0DE
```

Require eighteen run, zero failures/errors/skips. In testFailedMigratePreservesEarlierForwardedUpdates, require BUFFERING, a non-null operation-count gauge equal to 3, intended failure marker, correct earlier update/delete and failed-key partial document, and exact equal positive source/target ADD versions. In testRetryAfterMigrateAssertionFailure, require the original assertion marker, one injection, restored ACTIVE/routing state and successful explicit retry. A wrong client exception is not acceptable.

3. Run all four new cloud regressions at seed 5253BEEF and again at seed 5253CAFE (substitute the seed in this command):

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigratePreservesEarlierForwardedUpdates --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigrateRestoresExistingRule --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMultiShardMigratePreservesEarlierRange --tests org.apache.solr.cloud.MigrateRouteKeyTest.testRetryAfterMigrateAssertionFailure -Ptests.seed=5253BEEF
```

Require four green methods at each seed, preserving each run's XML separately.

4. Neighbor and buffering/replay behavior at I:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.CoreAdminOperationTest --tests org.apache.solr.handler.admin.CoreAdminHandlerTest --tests org.apache.solr.handler.admin.api.V2CoreAPIMappingTest --tests org.apache.solr.cloud.overseer.TestClusterStateMutator --tests org.apache.solr.search.TestRecovery.testBuffering --tests org.apache.solr.search.TestRecovery.testBufferedMultipleCalls -Ptests.seed=5253C0DE
```

Require all named tests execute green with zero skips; record actual counts. No-matching-tests is not evidence.

5. Root check: `./gradlew check -x test`, success without additional exclusions. Record environment conditions and setup repairs honestly, including NODE_ENV if relevant.
6. Reference Guide link check as a separate result: `./gradlew :solr:solr-ref-guide:checkSiteLinks`, success.
7. Final clean state and exact remote read-back: git diff --check, tracked/untracked clean, head/tree at I, fork branch still at I. No proof for a later head without a superseding job.

## Negative A: original cleanup at O, corrected identical harness

Use a standalone clone at O. Overlay exactly I's TestInjection.java and MigrateRouteKeyTest.java at their original paths. Add only the hook import/call to O's MigrateCmd using the two-anchor preparation in gates/pr-5253-cleanup-round-1.md (negative leg A). Do not copy I/H's MigrateCmd cleanup, SliceMutator, CoreAdminOperation, CoreAdminParams or MigrateCmdCleanupTest. O's discard endpoint must remain available. Record the full instrumentation-only production diff, synthetic tree, base O and SHA-256 of harness files.

Run only the three cleanup regressions, not the new assertion test:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigratePreservesEarlierForwardedUpdates --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigrateRestoresExistingRule --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMultiShardMigratePreservesEarlierRange -Ptests.seed=5253C0DE
```

Require three executed failures at the intended assertions: earlier update expected 1/actual 0 after the corrected three-operation setup and intentional failure marker; prior rule missing; first completed range's rule missing after exactly two transfers. Do not claim later assertions executed after the first failure. A gauge/label/RTG setup failure, compile failure, skip, timeout or wrong exception is not negative proof. In particular, the old 3-versus-2 distinct-version setup failure is invalid and must not recur.

## Negative B: remove the cleanup prerequisite return at I

In a separate clone at I, remove exactly the return in the first cleanup catch. Unlike the original job, the catch now includes AssertionError. Preparation:

```python
from pathlib import Path
p = Path('solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java')
s = p.read_text()
start = s.index('  void cleanupFailedMigrate(')
prefix, tail = s[:start], s[start:]
old = '    } catch (Exception | AssertionError e) {\n      addCleanupFailure(failure, e);\n      return;\n    }'
assert tail.count(old) == 1
tail = tail.replace(old, old.replace('      return;\n', ''), 1)
p.write_text(prefix + tail)
```

Record the one-line production mutation, synthetic tree and file hash. Run four prerequisite methods only; the replay-assertion method is a positive pin, not a failure expected from this mutation:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest.testRemovalFailureKeepsTargetBuffering --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest.testRemovalTimeoutKeepsTargetBuffering --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest.testInterruptedRemovalKeepsTargetBuffering --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest.testRemovalAssertionKeepsTargetBuffering -Ptests.seed=5253C0DE
```

Require all four execute and fail because newShardHandler was called when the test required no target request construction. Retain actual Mockito assertion/stack. An unrelated exception or compile failure is not proof. The complete positive class must pass at I and preserve original/suppressed failures.

## Negative C: assertion failure at stopped input H

Use a standalone clone at H, with only I's MigrateRouteKeyTest.java overlaid. H already has the TestInjection hook. Do not overlay I's MigrateCmd or MigrateCmdCleanupTest; H's Exception-only outer catch and helper signature must remain unchanged. The overlaid cloud file uses existing APIs and includes H's compile-repaired document helper.

Record H, synthetic tree, exact test-file delta/hash and absence of production changes, then run:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest.testRetryAfterMigrateAssertionFailure -Ptests.seed=5253C0DE
```

Require one executed assertion failure caused by missing assertion cleanup/reporting: either the client exception is the buffer-request rejection instead of the injected assertion marker (the observed Overseer path), or the target remains BUFFERING when ACTIVE is required. Retain logs showing the original injected AssertionError and subsequent command/target state. The injection throws only once, so an Overseer retry cannot repeatedly inject assertions. No harness timeout, missing hook, compile failure, assumption skip or unrelated error counts as proof. Positive I must report the original marker once, restore state and permit a successful explicit retry.

## Completion and failure rules

GREEN requires every positive leg at the final pinned head and all three discriminating negative legs. H's earlier fourteen passing tests do not replace fresh I runs. If repaired source/tests/tidy change I, plain-push a new head, post another superseding gate and prove that exact input; retain the failed evidence. Do not weaken the operation count, version equality, failure marker or state assertions without investigation and a recorded design decision.

Receipt records actual commands/seeds/counts, original O / H / I and synthetic trees, logs/XML/hashes, clean tidy/compile/root/guide results, environment conditions, and final remote ref. Keep the prior NOT GREEN receipt intact and post an add-only completion/failure report. No GitHub comment, JIRA comment, PR action or contributor-branch write is requested.
