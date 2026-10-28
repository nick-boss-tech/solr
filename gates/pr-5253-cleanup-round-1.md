# Gate: SOLR-18601 / PR 5253 migration cleanup follow-up, round 1

Status: OPEN. Immutable once posted. Proves only the exact head below.

- Required tags: gate, review, records. Intended host: any capable Linux CPU gate host with JDK 21, Git, Python 3, Gradle 9.7.0 from this branch's wrapper and the Solr build dependencies. One build at a time under the host's build lock and normal disk/inode guards.
- Fork: nick-boss-tech/solr. Branch: solr-18601-submit.
- Exact positive head F: 5cbffe2f243c4f2cf3606de9f2434981fc097019. Verified by GitHub ref API after push.
- Original PR head O / negative production base: 32b91612edde057c19bf1a7963e8276b816551df. F is one ordinary commit above O.
- Public PR: https://github.com/apache/solr/pull/5253. Its contributor branch remains at O. Do not modify it.
- Implementation claim: claims/pr-5253-cleanup-windows-round-1.md.
- Implementation report: reports/pr-5253-cleanup-implementation-round-1.md. Read its behavior choice and limits before claiming.
- Windows status: implementation pushed, proof pending. git diff --check clean. No Gradle/formatting/compile/tests ran on Windows.
- Claimant records before running: host, JDK path/version, Gradle cache, build lock, runner path, positive/negative/mutation worktrees, and log/XML directory paths in its add-only claim. Suggested local worktree names gate-5253-positive, gate-5253-original-negative, gate-5253-cleanup-mutation; use the host's own /workspace layout. Retain per-leg logs, headroom records and fresh JUnit XML under its gate log directory.
- Receipt destination: receipts/PR-5253-cleanup-round-1.md, owned by the execution host. No public posting is part of this gate.

## Acceptance and repairs

Run detached tidy preflight at F before claiming execution, following WORKFLOW.md. If tidy changes files, commit the formatting repair with Nick's identity and plain-push to the fork branch, then add a superseding gate naming the new exact head before running proof. Never count runs at F as proof for a changed head. Source/test fixes after a compilation or executed-test failure also require a recorded repair, new pushed head, and superseding job. Do not silently rerun a failed executed test. Record the failure first, investigate, then repair under WORKFLOW.md.

This gate validates replay of acknowledged buffered writes after exact prior-rule restoration. It intentionally permits partial target data from a failed migration. If restoration fails or installation cannot be confirmed, the target stays buffering; this is not a complete automatic recovery for every failure. Do not change these choices silently to make a test green.

Require every named test to execute, with zero positive failures/errors/skips, fresh XML, and explicit actual counts. A Mockito assumption skip is not proof. Assert-enabled test execution is required so the injection hook runs. Root check must be reported honestly, including any environment condition or repair; do not convert action_required upstream workflows into test evidence.

## Positive legs

1. At clean F, tidy: `./gradlew tidy`. Require rc 0 and no tracked changes, then `git diff --check`. Record head/tree and changelog parse result. Changelog authors must be Boyi Dai and Nick Shanin by name, with no nick line.
2. Focused suite, seed 5253C0DE:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest --tests org.apache.solr.cloud.overseer.SliceMutatorRoutingRuleTest -Ptests.seed=5253C0DE
```

Require fifteen tests at F (7 migration, 3 failure-path, 5 state-mutator), all green. This includes the original synchronous/asynchronous retry case. Verify the new shared-buffer test reached its BUFFERING/log-count assertions and the intentional failure marker, then passed its update/delete/partial-data assertions.

3. Three new cloud regressions only, once at seed 5253BEEF and once at seed 5253CAFE. Use this command with the stated seed substituted:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigratePreservesEarlierForwardedUpdates --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMigrateRestoresExistingRule --tests org.apache.solr.cloud.MigrateRouteKeyTest.testFailedMultiShardMigratePreservesEarlierRange -Ptests.seed=5253BEEF
```

Require three tests green at each seed. Preserve each run's XML before cleanTest overwrites it.

4. Neighbor behavior:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.CoreAdminOperationTest --tests org.apache.solr.handler.admin.CoreAdminHandlerTest --tests org.apache.solr.handler.admin.api.V2CoreAPIMappingTest --tests org.apache.solr.cloud.overseer.TestClusterStateMutator -Ptests.seed=5253C0DE
```

Require zero failures/errors/skips; record counts. Do not treat a no-matching-tests result as proof.

5. Root validation: `./gradlew check -x test`. Require success. Include the Reference Guide check since the failure contract is documented there. Record any named environment condition, including NODE_ENV if relevant, separately from source fixes. No silent exclusions beyond `-x test`.
6. Final positive cleanliness: `git diff --check`, clean tracked/untracked state, exact F head and remote fork ref. Record fresh tree/hash evidence. Gate only the final pinned head; no formatting changes may remain local.

## Negative leg A: original cleanup with identical regression harness

Use a separate detached worktree at O. Overlay only these F files:

- solr/core/src/java/org/apache/solr/util/TestInjection.java
- solr/core/src/test/org/apache/solr/cloud/MigrateRouteKeyTest.java

Then add only the hook call/import to O's MigrateCmd. The following Python preparation is the full allowed production-file delta for this synthetic negative harness; save/run it in the negative worktree after overlaying the two files:

```python
from pathlib import Path
p = Path('solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java')
s = p.read_text()
anchor = 'import org.apache.solr.handler.component.ShardHandler;\n'
assert s.count(anchor) == 1
s = s.replace(anchor, anchor + 'import org.apache.solr.util.TestInjection;\n')
anchor = '      log.info("Routing rule added successfully");\n'
assert s.count(anchor) == 1
s = s.replace(anchor, anchor + '      assert TestInjection.injectMigrateAfterRoutingRule();\n')
p.write_text(s)
```

Do not overlay F's MigrateCmd cleanup, SliceMutator, CoreAdminOperation, CoreAdminParams or MigrateCmdCleanupTest. O's original discard action must remain intact. Record exact diff, synthetic tree, base O, and SHA-256 hashes for the three harness files. These two hook statements are instrumentation, not a cleanup fix.

Run the three-method cloud command from positive leg 3 at seed 5253C0DE. Require exactly three executed tests and all three fail after reaching the intentional injected-failure path:

- earlier-forwarded-updates: buffer state/count and failure-marker setup must pass; the assertion that the updated earlier-migration document is present with n_ti:2 must fail (expected 1, actual 0). Report only the observed first failing assertion; do not claim the later delete assertion also executed.
- restores-existing-rule: expected earlier routing rule is missing, failing the named not-null assertion.
- preserves-earlier-range: transfer counter must be two; expected first transfer's routing rule is missing, failing the named not-null assertion.

A compile error, timeout/startup failure, missing hook invocation, failing buffer-count setup, assumption skip, wrong exception, or passing test is not negative proof. Investigate and record it, without weakening the assertions to fit the result.

## Negative leg B: cleanup prerequisite mutation

Use a separate detached worktree at F. Remove only the return after addCleanupFailure in the first catch inside cleanupFailedMigrate, leaving all tests and other production behavior unchanged. Exact preparation:

```python
from pathlib import Path
p = Path('solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java')
s = p.read_text()
start = s.index('  void cleanupFailedMigrate(')
prefix, tail = s[:start], s[start:]
old = '    } catch (Exception e) {\n      addCleanupFailure(failure, e);\n      return;\n    }'
assert tail.count(old) == 1
tail = tail.replace(old, old.replace('      return;\n', ''), 1)
p.write_text(prefix + tail)
```

Record the one-line production diff and synthetic tree/hash. Run:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.api.collections.MigrateCmdCleanupTest -Ptests.seed=5253C0DE
```

Require all three methods execute and fail because target request construction was attempted despite removal failure/timeout/interruption. Depending on the first assertion reached, this appears as an extra suppressed exception from the attempted target-handler use or as Mockito reporting an unwanted newShardHandler call. Retain the actual stack and show the attempted target path; an unrelated setup/compile failure is not proof. The positive F run must preserve the original exception with exactly the single expected cleanup failure and create no target shard handler.

## Completion

GREEN requires all positive legs at the final pinned head and both discriminating negative legs. Record commands, seeds, executed counts, XML/log paths and hashes, O/F/synthetic trees, tidy cleanliness, root-check result and environment details, remote fix ref read-back, and any limits. Receipt's final section may provide an internal factual handoff draft, but this job does not request public posting. Post the receipt and an add-only completion/failure report; leave this immutable job and claim untouched. If a repair changed the head, cite the superseding gate instead of calling this job green at another head.
