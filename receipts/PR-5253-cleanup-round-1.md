# Receipt: SOLR-18601 / PR 5253 migration cleanup follow-up (vm2, Linux, round 1)

Result: NOT GREEN at the pinned head H `98d1127e4f5d4d149992e1dc494b95928bac057a`. One executed test fails in the focused leg. Positive legs after that point, negative legs A and B, the root check and the guide link check were not run at H. No proof is claimed for any head.

Jobs and heads:
- Original job: `gates/pr-5253-cleanup-round-1.md` (board `3e84be3f668`). Positive F `5cbffe2f243c4f2cf3606de9f2434981fc097019`, original PR head O `32b91612edde057c19bf1a7963e8276b816551df` (verified ancestor, one commit between).
- Superseding round 1 (board `3c03ad95773`): G `cc8c42399909fb8df85c94376a042d291fd56292`. Superseded by round 2 because its focused leg failed to compile.
- Superseding round 2 (board `e768687d951`): H `98d1127e4f5d4d149992e1dc494b95928bac057a`, the head this receipt reports on.
- Claims: `claims/pr-5253-cleanup-linux-vm2-round-1.md` (board `4651b5ed3e5`), with the Windows implementation claim `claims/pr-5253-cleanup-windows-round-1.md` unchanged.
- Fork `nick-boss-tech/solr:solr-18601-submit`: F, then G (formatting repair), then H (compile repair). Each a plain fast-forward. Author Nick Shanin `<nick.boss.us@gmail.com>`, no attribution trailers. Live `ls-remote` read-back after each push matched.

## Changes made by the gate host

1. F to G (`cc8c423`): tidy at F changed three files, whitespace-only (verified: with all whitespace removed, the contents match F). Files: `MigrateCmd.java`, `MigrateRouteKeyTest.java`, `SliceMutatorRoutingRuleTest.java`. Committed and pushed as the formatting repair.
2. G to H (`98d1127`), a repair to two compile errors found in G's focused leg:
   - `MigrateCmd.java:580`: `new ZkNodeProps(...)` was given `Map` values for `expectedRule` and `restoreRule` through the `String...` constructor. Repaired by building a `HashMap` with the same keys and values, then `new ZkNodeProps(props)`. `SliceMutator` reads both keys as maps, and a null `restoreRule` means remove the rule. Semantics unchanged.
   - `MigrateRouteKeyTest.java`: 8 `new SolrInputDocument("id", ..., "n_ti", ...)` calls do not compile, because `SolrInputDocument(String...)` takes strings only. Routed through a private `document(Object...)` helper that calls `addField`. Assertions and data unchanged.
   - Both edits were compiled (`:solr:core:compileTestJava` rc 0 at 03:23:38, before tidy), then tidied (tidy rewrote only these two files), then committed. The committed tree compiled in the focused run at H, which executed tests.
   - Tidy at H ran clean: rc 0, no tree change, `git diff --check` clean.

## Results at H (all times UTC)

| Step | Command | Time | Result |
| --- | --- | --- | --- |
| tidy | `./gradlew tidy` at H | 03:27:09 to 03:28:49, rc 0 | `git diff --exit-code` rc 0, untracked 0, `git diff --check` rc 0 |
| focused, seed 5253C0DE | `:solr:core:cleanTest :solr:core:test` with MigrateRouteKeyTest, MigrateCmdCleanupTest, SliceMutatorRoutingRuleTest | 03:28:55 to 03:32:17, rc 1 | 15 executed, 1 failure, 0 errors, 0 skipped |

Focused XML, `focused-xml/`:
- `MigrateRouteKeyTest`: 7 run, 1 failure, 0 errors, 0 skipped.
- `MigrateCmdCleanupTest`: 3 run, 0 failures, 0 errors, 0 skipped.
- `SliceMutatorRoutingRuleTest`: 5 run, 0 failures, 0 errors, 0 skipped.

Failed test: `MigrateRouteKeyTest.testFailedMigratePreservesEarlierForwardedUpdates`, line 293 (the assertion after the second migration). The second MIGRATE returned `MIGRATE failed to request node to buffer updates`, not the injected failure. Root cause analysis is below.

Not run at H: the three cloud regressions at seeds 5253BEEF and 5253CAFE (the beef step started and was stopped), the neighbor set, `./gradlew check -x test`, `:solr:solr-ref-guide:checkSiteLinks`, negative leg A at O, and negative leg B. Round 1's F and G compile-failure runs and the H focused run are the only executed evidence.

Assertions enabled: the injected hook ran through `assert TestInjection.injectMigrateAfterRoutingRule();`, so assertions were enabled in the test JVM. Verified from the logs, not from the build file.

## Root cause of the failed test

Evidence: `focused-MigrateRouteKeyTest-output.txt`, `diag2-H-MigrateRouteKeyTest-output.txt`, `attempt` logs.

1. The second MIGRATE (`b!`) starts and buffers the target (`Starting to buffer updates ... state=ACTIVE`, then `REQUESTBUFFERUPDATES status=0`). The routing rule for `b!` is added.
2. The injected hook (`MigrateRouteKeyTest`, invoked from `MigrateCmd` line 323) forwards three writes: `a!updated`, the `a!deleted` delete, and `b!1`. The server logs all three as `Leader logic applied but update log is buffering`.
3. The hook's own buffer-count assertion fails: `All acknowledged writes must be in the target buffer expected:<3> but was:<2>`. This is an `AssertionError`, which is an `Error`. `MigrateCmd` catches only `Exception` (line 515), so `cleanupFailedMigrate` does not run.
4. The Overseer runner exits without `success`, so it logs `Resetting task ... qn-0000000021` (`OverseerTaskProcessor` `finally`) and re-runs the whole MIGRATE for `b!`.
5. The retry starts buffering on the target, which is still BUFFERING from step 1. The core rejects it: `Core preserveUpdates-target_shard1_replica_n1 not in active state`. The client sees `MIGRATE failed to request node to buffer updates`.

The count itself. A diagnostic run in a scratch clone (not gate evidence) printed the buffered versions just before the failing assertion:
- `set=[0, -1880263547099807744]`
- `recent=[0, -1880263547099807744]`

So the three buffered writes collapse to two distinct versions, and one of them is version 0. `UpdateLog.RecentUpdates.getBufferUpdates()` is a `Set<Long>` of versions. The test counts distinct versions, which cannot reach 3 here.

## Open questions for the owner (not decided by this gate)

- Test oracle or production: is it correct for forwarded writes buffered in the BUFFERING state to carry version 0? If so, the test should count buffered entries, not distinct versions. If not, the buffering path needs a versioning fix. The replay in `REQUESTAPPLYUPDATES` may depend on versions, so this must be decided before the test expectation changes.
- Error handling: `MigrateCmd` catches only `Exception`. Any `Error` (assertion, OOM, or the like) during a MIGRATE skips `cleanupFailedMigrate`, leaves the target BUFFERING, and the Overseer retries the task. The failure contract says "cleanup errors are suppressed", but it does not cover `Error`. This is a production-side gap independent of the test assertion.
- Repeated retry: the retry then fails at the buffer request. The retried MIGRATE should not start a second buffer on a target that is still buffering from an earlier attempt, or the target needs an operator reset. The design says unconfirmed states "may still require operator intervention", which is currently the only recovery path.

## Limits

- No public GitHub or JIRA write. The public PR 5253 is unchanged at O.
- No change to the contributor's branch.
- The diagnostic instrumentation was applied only in the scratch clone `pr5253-B-clone` and reverted. Its output is kept in the evidence directory.
- The superseding gates and this receipt are not green. Any repair (test oracle, cleanup handling of `Error`, or the retry behaviour) needs a new pushed head and another superseding gate.

Evidence directory: `/workspace/gates/logs/vm2/solr18601-cleanup/`. Steps record: `steps.txt`. Attempt logs: `attempt-F-5cbffe2/`, `attempt-G-cc8c423/`.
