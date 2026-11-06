# SOLR-18628 gate environment and execution

- Host: `phone-agent-2`, Linux sandbox; worktree `/workspace/worktrees/solr-18628-h2c-fail-before-round-1-H1`.
- Exact source: fork `solr-18628-submit` remained at H1 `aebe1d8e9b0f226948e092f2610682c9c2596f27`; detached worktree was clean before and after the run.
- Gradle: wrapper Gradle 9.7.0; `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64`, OpenJDK 21.0.12.1.
- Test runtime: Eclipse Temurin JDK 25.0.5+8 at `/workspace/tools/jdk25/jdk-25.0.5+8`, selected by `RUNTIME_JAVA_HOME`. The official Linux x64 release asset was 141,341,188 bytes and matched SHA-256 `68982529869257d0ee6b824ec5aa270ff8b118bb438670da0b97ebdab178d7fb`; details are in `raw/toolchain/jdk25-install-verification.txt`.
- Test JVM startup option: `JAVA_TOOL_OPTIONS=-Djdk.httpclient.keepalive.timeout=1`. The test worker log records the option after `Gradle Test Executor 1` started; the JUnit XML's running Jetty reports JVM `25.0.5+8-LTS`.
- Pre-claim tidy preflight: `2026-11-06T17:14:37Z`–`17:15:56Z`, rc=0, 62 actionable tasks, tidy clean at H1.
- Compile leg: runner context records `2026-11-06T17:23:55Z`–`17:25:15Z`, rc=0; `:solr:solrj:compileJava` and `:solr:solrj:compileTestJava`, 18 tasks executed.
- Focused test: runner context records `2026-11-06T17:26:11Z`–`17:27:51Z`, rc=0. JUnit XML reports 1 test, 0 failures/errors/skips; the test case duration is 3.674 seconds.
- All Gradle tasks used `/workspace/gates/scripts/solr-gate-step.sh` with the live `pool/bin/sweep-run.sh`, inode/disk guard and serialized lock `/workspace/tools/.gradle-phone-agent-2.lock`; `SWEEP_WAIT=0` prevented queueing behind an occupied lock. The lock was free after the run and no Gradle/gate process remained.
- Final recorded headroom at `2026-11-06T17:33:43Z`: 38 GB disk free; 506,960 of 1,868,096 inodes free (about 27% free), above the 15% stop threshold. During the run, the lowest recorded free-inode count was 506,964 (about 27% free).
- Procedural caveat: before the build/test legs, a non-task `./gradlew --version` probe was run without the host lock. It executed no Gradle task or test, left the worktree clean, and no process remained. This was documented in the posted claim addendum; all compile/test tasks were subsequently guarded and locked.
