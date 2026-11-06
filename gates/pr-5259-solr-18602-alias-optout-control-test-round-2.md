# Gate: SOLR-18602 alias opt-out control test, round 2

Status: OPEN. Proof pending.

This gate supersedes `gates/pr-5259-solr-18602-alias-optout-control-test-round-1.md`. The round-1 preflight ran `tidy` at `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`, which completed successfully but reformatted one `expectThrows` call in the new test. No compile, test, or root-check proof ran at that old head. The gate-authorized repair was committed and plainly fast-forward pushed as Nick Shanin at `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`; it is a direct child of `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7` and changes only `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java` formatting.

- Intended host: `phone-agent-2`, a Linux host with JDK 21 and the pinned Solr build dependencies.
- Source authoring record: `claims/pr-5259-solr-18602-alias-optout-control-test-windows-round-1.md`; implementation report: `reports/pr-5259-solr-18602-alias-optout-control-test-round-1.md`.
- Fork branch: `nick-boss-tech/solr:solr-18602-pr5259-alias-optout-test`.
- Exact offered implementation head F2: `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`.
- Previous candidate F1: `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7` (the parent of F2).
- Baseline / current public PR head H: `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`.
- Live PR #5259 read-back before gate claim: OPEN, unmerged, head H above, base `35e2be2a7de9855a0f909613065ba22243097102`. Do not merge or rebase the candidate onto the moving base.
- Scope: `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java`; test `deleteReplicaByCountDoesNotFollowAliasesWhenDisabled`.
- Gate claim: `claims/pool-phone-agent-2-gate-pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Receipt target: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Evidence root: `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-2/`; board evidence target: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2-evidence/`.

## Preflight and execution rules

A fresh detached preflight at exact F2 ran `./gradlew tidy` and returned 0: `tidy clean at 2efbd9884eac22cc8ddd0bdad2f8715f800eebee`, Gradle 9.7.0, 62 actionable tasks. It ran 2026-11-06T06:05:25Z–06:06:58Z. The live monitor recorded 33 samples, minimum 778,286/1,868,096 free inodes (41.6620%) and 45,666,197,504 free disk bytes; no stop marker. Logs are under the round-2 evidence root's `preflight/` directory. The gate's detached preflight clone was automatically removed by the established preflight script.

Run each proof leg serially under `/workspace/tools/.gradle-phone-agent-2.lock`. Use `pool/bin/sweep-run.sh` under the live `tools/inode-watch-run.py` watchdog, which samples root inode and disk headroom every 3 seconds and stops a run at or below 15% free inodes or below 2 GiB free disk. Set `SWEEP_WAIT=0` so a busy lock refuses instead of overlapping another gate. Unset `NODE_ENV` for Gradle. Preserve each full log, resource samples, and fresh JUnit XML before the next `cleanTest` removes build outputs. Record exact commands, UTC intervals, runner return codes, actual executed test counts, failures/errors/skips, toolchain, lock state, and checkout paths in the receipt.

Use separate clean checkouts: baseline H, implementation F2, and synthetic mutation F2. The root check must run in a standalone clone of F2, not a Git worktree, because Solr's Antora build requires `.git` to be a directory.

## Required proof

1. **Exact-head root check:** in a standalone, clean clone at F2, run `./gradlew check -x test` through the serialized runner. Record the exact head, clean state, actual result, and resource monitor.
2. **Parent baseline positive run:** at H, run:
   ```sh
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.DeleteReplicaTest -Ptests.seed=18602C0DE
   ```
   Require zero failures, errors, or skips and record the executed class count.
3. **Implementation positive run:** at F2, run the same command and seed. Require zero failures, errors, or skips; confirm the new test name appears in fresh XML and record the full class count.
4. **Coverage-only fail-before mutation:** in a separate clean checkout at F2, keep `DeleteReplicaTest.java` byte-identical to F2 and record its SHA-256. Make only the assignment-specified uncommitted synthetic production mutation in `DeleteReplicaCmd.java`: change the `FOLLOW_ALIASES` conditional in `deleteReplicaBasedOnCount` to unconditional `resolveSimpleAlias(extCollectionName)`. Run only:
   ```sh
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.DeleteReplicaTest.deleteReplicaByCountDoesNotFollowAliasesWhenDisabled -Ptests.seed=18602C0DE
   ```
   The test must execute and fail at its expected-`SolrException` assertion because unconditional alias resolution makes the deletion succeed. A compile/startup failure, skip, timeout, or unrelated failure is not mutation proof. Save the exact one-file mutation diff, unchanged test SHA-256, full log, fresh XML, and assertion failure. Never commit or push the synthetic mutation.
5. Finish with `git diff --check` and a clean F2 implementation tree; re-read the fork ref and PR state/head/base. Keep the candidate at F2; no rebase/merge onto the moving PR base.

GREEN requires the clean tidy preflight, the exact-head standalone root check, both positive focused-class runs, and the discriminating mutation leg with fresh logs/XML. This is coverage-only evidence that the control test distinguishes the unconditional-resolution mutation; it does not claim a production false-branch bug. No PR-body edit, public PR comment/review, Jira write, upstream write, or unrelated source change is in scope. The old round-1 claim is superseded before tests, with no round-1 proof receipt.