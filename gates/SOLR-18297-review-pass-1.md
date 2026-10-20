# Gate job: SOLR-18297, review pass 1 (critical-blocker review round 1, unit 2)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-18297.md)

- Branch: `solr-18297-jetty-12-1-14`. Head: `5cdc657470` (ls-remote at claim time). Base: `498a09aea9`.
- Host: vm3. Runner: `/workspace/tools/gate-solr-18297-p1.sh` under flock, worktree `/workspace/worktrees/solr-18297`. Logs: `/workspace/gates/logs/vm3/solr-18297-review-pass-1/`.
- Steps: 0 changelog YAML parse; 1 tidy (repair if it changes files); 2 Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava :solr:server:compileJava -Pvalidation.errorprone=true`); 4 focused `org.apache.solr.cli.StatusToolTest`, seed `18297A001` (starts a Jetty-backed server); 5 `:solr:core:check -x test`.
- Not in this pass: the full core and server test suites (the draft PR says a pool gate host must run them before merge). Recorded as a limit of pass 1; the next passes decide whether they run.

## Attempt 1 (head 5cdc657470): compile failed

- Step 0 changelog: rc=1, the same two errors as base 498a09aea9 (solr-17231.yml, SlowCompositeReaderWrapper-sort-hasblocks-fix.yml). Not in this branch's diff.
- Step 1 tidy: rc=0, changed `gradle/libs.versions.toml` (removed the unused `oshai-logging = "8.0.4"` entry; no use of `oshai` in the tree). Repair commit `7c07821070` (`SOLR-18297: review repair pass 1: tidy removes unused oshai-logging version entry`), pushed fast-forward from 5cdc657470.
- Step 2 compile: rc=1. `CoreReplicationAPITest.java:213` and `:217`, Error Prone `DirectInvocationOnMock` warning, `-Werror`. The file is not in this branch's diff and the Error Prone version is not changed by the branch, so the warning exists on base too. Message does not name a fix, so it is outside the repair scope.
- Steps 4 and 5: test and check ran against the failed compile; they are not evidence for this head.
- Log: /workspace/gates/logs/vm3/solr-18297-review-pass-1/ (compile.log).
- Next: one retry from step 0 on 7c07821070 (tree changed by the tidy repair), under the same claim.

## Attempt 1 addendum: the compile result depends on Gradle's incremental state

- A second focused run of the same head (log in the attempt-1 record, test step) recompiled nothing for the file, and `compileTestJava` passed without the warning. The compile failure is therefore not reproduced without a clean compile. It is a warning on an unchanged file that fails `-Werror` only when javac recompiles it.
- Attempt 2 (retry, head 7c07821070, the one retry for this pass): the same steps with `:solr:core:cleanCompileJava :solr:core:cleanCompileTestJava` before the compile, so every source is recompiled. Logs: /workspace/gates/logs/vm3/solr-18297-review-pass-1/. Attempt-1 logs: /workspace/gates/logs/vm3/solr-18297-review-pass-1-attempt1/.
- If attempt 2 fails at compile with the same warning, the pass is HANDED BACK: the warning is in a test file outside the branch diff, and the fix is outside the repair scope.
