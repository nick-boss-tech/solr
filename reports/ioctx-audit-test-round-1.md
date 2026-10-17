# IOContext audit focused tests, round 1

Assignment: assignments/pool-solr-ioctx-audit-test-round-1.md (posted 1c2c3ff2d6). Claim: claims/pool-solr-ioctx-audit-round-1.md. Run by vm2, 2026-10-17, 20:12Z to 20:22Z.

Result: the first pass stopped on a skipped class (TestReplicationHandler, 25 of 25 skipped). The follow-up below runs that class with nightly tests enabled and it passes 25 of 25. All nine classes are green on the head with JDK 21. The branch is verified for the nine focused classes, subject to the assignment owner's acceptance of the nightly follow-up.

## Branch and command

- Branch solr-ioctx-audit, detached at 2a35fb87ff5e58f08678fa719dd4ea1ca05de514 (ls-remote matched at the start). Base 70aa8d75bd, an ancestor of the head.
- JDK 21.0.12.1 (/workspace/jdk/jdk-21.0.12.1+1). Gradle 9.7.0 from the branch wrapper. The wrapper jar matches the committed gradle-wrapper.jar.sha256.
- Command: `./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/ioctx-tests -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:test` with the nine `--tests` filters from the assignment.
- Build: BUILD SUCCESSFUL (35 actionable tasks, all executed). Log: logs/vm2/ioctx-head.log. Runner: gates/g-ioctx-audit.sh. Summary: logs/vm2/ioctx-audit-result.txt.
- The runner copied the main checkout's gradle.properties into the worktree. The branch is based on main with the same Gradle 9.7 template, so the copy matches what the build would generate. Noted as a difference from the 9.x runs, which generated their own file.

## JUnit counts per class (fresh XML, 20:21Z)

| Class | tests | failures | errors | skipped | Result |
| --- | --- | --- | --- | --- | --- |
| org.apache.solr.core.SolrCoreTest | 9 | 0 | 0 | 0 | OK |
| org.apache.solr.handler.TestReplicationHandler | 25 | 0 | 0 | 25 | SKIPPED, stop |
| org.apache.solr.handler.TestReplicationHandlerBackup | 2 | 0 | 0 | 0 | OK |
| org.apache.solr.core.TestBackupRepositoryFactory | 4 | 0 | 0 | 0 | OK |
| org.apache.solr.handler.TestIncrementalCoreBackup | 3 | 0 | 0 | 0 | OK |
| org.apache.solr.handler.TestSnapshotCoreBackup | 5 | 0 | 0 | 0 | OK |
| org.apache.solr.cloud.api.collections.TestLocalFSCloudBackupRestore | 2 | 0 | 0 | 0 | OK |
| org.apache.solr.core.snapshots.TestSolrCoreSnapshots | 2 | 0 | 0 | 0 | OK |
| org.apache.solr.handler.admin.api.CoreSnapshotAPITest | 6 | 0 | 0 | 0 | OK |

## The skip

- TestReplicationHandler.java has no @Ignore, @AwaitsFix, or assume call at the head. The only annotation is a commented-out @BadApple at line 1303. The file is identical at the base 70aa8d75bd, so the skip is not from this branch's change.
- The cause was not traced. The reason is not in the Gradle log. Finding it would need the XML skip messages, which the worktree removal discarded.
- The assignment's head rule is that any skip stops the item. Triage on base is only specified for failures, so no base run was made.

## Decision needed

Whether to (a) run TestReplicationHandler on base 70aa8d75bd to see whether it skips there too, or (b) accept the skip as pre-existing on the basis of the identical source, or (c) stop the branch's verification at this class. The head result for the other eight classes is 0 failures, 0 errors, and 0 skipped.

## Not done

- No triage run, no whole-set rerun, no source edits, no pushes to the fork branch.

## Follow-up: TestReplicationHandler with nightly tests enabled

- Cause of the skip: TestReplicationHandler.java line 95 is annotated `@LuceneTestCase.Nightly`. Those tests run only when `tests.nightly` is on. The head's randomization config (gradle/testing/randomization.gradle) sets it to false by default.
- Run: branch head 2a35fb87ff5e58f08678fa719dd4ea1ca05de514, JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper, seed 18551C0FFEE18551, with `-Ptests.nightly=true`. Command: `./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/ioctx-nightly-tests -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 -Ptests.nightly=true :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.TestReplicationHandler`.
- Result, fresh XML (2026-10-17T20:42:00Z): tests=25, failures=0, errors=0, skipped=0. Build: BUILD SUCCESSFUL in 11m 1s.
- Log: logs/vm2/ioctx-nightly.log. Summary: logs/vm2/ioctx-nightly-result.txt. Runner: gates/g-ioctx-nightly.sh.
- Reading: the skip is explained by the nightly gate. The class passes on the head when its tests run.
- The base run was not made, because the skip is explained and the head result is a pass. No triage applies.
