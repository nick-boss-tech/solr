# Receipt: SOLR-18564 / PR 5192 typed executor timeout follow-up, round 1

## Gate solr-18564-timeout-type-round-1 at 6ca1cba8061b (vm5, 2026-10-27): GREEN

- Branch: `nick-boss-tech/solr:corecontainer-shutdown-closes` (apache/solr PR 5192). Head: `6ca1cba8061b3c6298040634972afc4361f55add` (confirmed by ls-remote before the gate; the repaired head). Repaired-from F: `bd42a1d58b9fba4656a200536a007d9d79dc45b2` (the job's head; not gated as final). Original head O: `dd65b21b41dc1d008c51031b71f61964ee2ca169`.
- Gate job: `gates/SOLR-18564-pr5192-timeout-type-round-1.md`, superseded for the repaired head by `gates/SOLR-18564-pr5192-timeout-type-round-1-superseding-6ca1cba8061b.md`. Claim: `claims/solr-18564-timeout-type-round-1-vm5-repaired.md`.
- Links: Ticket or parent: SOLR-18564 / PR 5192. Category: review. Siblings: the O-head gates (`receipts/SOLR-18564.md`, vm6 and vm2 claims). Supersedes: none for the repaired head.
- Repair before the gate: the preflight tidy at F changed one line in `solr/solrj/src/test/org/apache/solr/common/util/ExecutorUtilTest.java` (a wrapped `assertTrue`). The repair commit "SOLR-18564: wrap ExecutorUtilTest assertion for tidy" is `6ca1cba8061b`, one commit above F, author Nick Shanin, no trailers, no PR number, fast-forward push. Diff: `/workspace/gates/logs/vm5/solr-18564-timeout-type/tidy.patch`.
- Runner: vm5 (Linux aarch64), detached full checkouts of `6ca1cba8061b` at `/workspace/gates/solr-18564/repair` and a mutation copy at `/workspace/gates/solr-18564/mutant`, under the host lock `/workspace/tools/.gradle-vm5.lock`, one build at a time. Toolchain: JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`, Gradle wrapper, Node v22.18.0. Test work directories: `/tmp/solr18564-timeout-type-tests` and `/tmp/solr18564-timeout-type-mutation-tests`.
- Log root: `/workspace/gates/logs/vm5/solr-18564-timeout-type/`.

### Steps

- Changelog YAML parse of `changelog/unreleased/SOLR-18564.yml`: parses; top-level keys `authors`, `links`, `title`, `type`. Run with `uv run --no-project --with pyyaml python -I`.
- Step 1, tidy at `6ca1cba8061b`: rc=0, `solr18564-head/tidy.log`, tree clean afterwards (`git status --porcelain` empty).
- Step 2, Error Prone compile (`:solr:solrj:compileJava :solr:solrj:compileTestJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, `--no-daemon --no-build-cache`): rc=0, `solr18564-head/compile.log`. The four compile tasks executed (not UP-TO-DATE).
- Step 3, `:solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.ExecutorUtilTest`, seed `18564ABC0001`: rc=0. `ExecutorUtilTest` 6 tests, 0 failures, 0 errors, 0 skipped. XML: `solr18564-head/solrj-s1.xml/`.
- Step 4, `:solr:core:cleanTest :solr:core:test --tests CoreContainerShutdownTest --tests TestCoreContainer`, seed `18564ABC0001`: rc=0. `CoreContainerShutdownTest` 1 test, 0 failures, 0 errors, 0 skipped. `TestCoreContainer` 25 tests, 0 failures, 0 errors, 3 skipped (the expected Windows-only tests). XML: `solr18564-head/core-s1.xml/`.
- Step 5, same two commands with seed `18564ABC0002`: rc=0. `ExecutorUtilTest` 6 tests, 0 failures, 0 errors, 0 skipped (`solrj-s2.xml/`). `CoreContainerShutdownTest` 1 test, 0 failures, 0 errors, 0 skipped. `TestCoreContainer` 25 tests, 0 failures, 0 errors, 3 skipped (`core-s2.xml/`).
- Step 6, matched mutation, seed `18564ABC0001`: in the separate copy `/workspace/gates/solr-18564/mutant`, one line changed in `ExecutorUtil.java` (the diff is `solr18564-mutation/mutation.diff`; `new ExecutorTerminationTimeoutException(` replaced by `new RuntimeException(` at the timeout throw). The nested exception declaration and the test file (blob sha256 prefix `44c77f1c7eeb694f`, identical to the head's copy) are unchanged. Run `ExecutorUtilTest.testExecutorUtilAwaitsTerminationWhenTaskIgnoresInterupt`: rc=1, required failure: `java.lang.AssertionError: unexpected exception type thrown; expected:<ExecutorTerminationTimeoutException> but was:<java.lang.RuntimeException>` at `ExecutorUtilTest.java:68` (`assertThrows`). Not a compile error or an unrelated failure. The same method passes at head with the same seed (`solrj-s1.xml`, 0 failures). XML and log: `solr18564-mutation/mut-s1.xml/`, `mut-s1.log`. The mutation is a synthetic copy and is not published.
- Step 7, final tidy at `6ca1cba8061b`: rc=0, tree clean afterwards.
- Step 8, root check `env -u NODE_ENV ./gradlew --no-daemon --no-build-cache check -x test`: rc=0, `BUILD SUCCESSFUL in 14m 57s` (`solr18564-check/check.log`). 805 tasks, 41 UP-TO-DATE; the test tasks were not UP-TO-DATE in this gate, since the focused steps ran their own fresh `cleanTest` runs.

### Diff checks

- `git diff --check dd65b21b41dc1d008c51031b71f61964ee2ca169 HEAD` at `6ca1cba8061b`: clean.
- Range O..R: three files, 32 insertions and 9 deletions (`CoreContainer.java`, `ExecutorUtil.java`, `ExecutorUtilTest.java`), plus the repair in the last commit is a formatting-only change in `ExecutorUtilTest.java`.

### Environment conditions

- `NODE_ENV` was unset for the whole job. The root check was run with `env -u NODE_ENV`.
- No environment repairs were needed in this gate; the only change to the head was the tidy repair commit.

### Result summary for the response to sigram

- At the repaired head `6ca1cba8061b`: tidy clean, Error Prone compile clean, the two focused suites green on both seeds (`ExecutorUtilTest` 6 of 6 each; `CoreContainerShutdownTest` 1 of 1 each; `TestCoreContainer` 25 run with 3 expected skips each), the strengthened timeout-type assertion fails on the one-line mutation with the expected type mismatch, and the root `check -x test` passes.
- The new exception type is observable to callers through `ExecutorUtilTest`. The mutation proves the test pins the type; it does not prove the original incident.
- Limits: this gate ran on Linux only. The three Windows-only `TestCoreContainer` tests were skipped here, as the job expected. No public reply, PR body edit, JIRA write, approval, or merge was made.
