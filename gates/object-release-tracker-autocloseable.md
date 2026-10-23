# Gate: ObjectReleaseTracker AutoCloseable cleanup

Status: DONE

- Assignment: `assignments/pool-objectrelease-autocloseable-round-1.md`
- Host: `phone-agent-2`, JDK 21, `GRADLE_USER_HOME=/home/ubuntu/.gradle`
- Base: Apache main `79b23df58daac14b92cc35f23b8b81f72098b4ba`; upstream main matched this SHA at gate time.
- Candidate branch: `phone-agent-2-objectrelease-autocloseable-candidate` (local only)
- Candidate commit: `35faf539e94e613d5f5528c71962e1970786bc93`
- Diff scope: only `ObjectReleaseTracker.java` and `TestObjectReleaseTracker.java` as listed in the assignment.
- Runner: `/workspace/phone-agent-2-coordination-objectrelease-20261023/pool/bin/sweep-run.sh`
- Lock: `/workspace/tools/.gradle-phone-agent-2.lock`
- Guard: the runner invoked `pool/bin/inode-guard.py` before every Gradle build; all invocations passed the 15% free-inode threshold. The workspace NFS inode-counter fallback checked local root as specified by that guard.

## Results

- Tidy: PASS, exit 0, 62 actionable tasks.
- Error Prone: the initial compile invocation exited 1 on javac `-Xlint:try` under `-Werror` because a test fixture declared `close() throws Exception`. The fixture was narrowed to `IllegalStateException`. The subsequent focused test compilation and scoped checks ran with `-Pvalidation.errorprone=true` and passed.
- Focused test: PASS, `TestObjectReleaseTracker`, 10 tests, 0 failures, 0 errors, 0 skipped, seed `3B1F3E98A7C2D144`.
- Scoped checks: PASS, core and SolrJ checks with the `test` task excluded, 78 actionable tasks.
- No full core or SolrJ test suite was run. The candidate was not pushed; no PR or Jira action was taken.

## Commands

All commands below were invoked through the host runner above, so each held the host lock and passed the inode guard.

1. Tidy:

```text
./gradlew --no-daemon --max-workers=2 tidy
```

2. Initial Error Prone-enabled compile (exit 1 on the warning described above):

```text
./gradlew --no-daemon --max-workers=2 -Pvalidation.errorprone=true :solr:core:compileJava :solr:core:compileTestJava :solr:solrj:compileJava :solr:solrj:compileTestJava
```

3. Focused test, after the fixture correction:

```text
./gradlew --no-daemon --max-workers=2 -Pvalidation.errorprone=true -Ptests.jvms=1 '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m' -Ptests.timeoutSuite=600000! -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1 -Ptests.seed=3B1F3E98A7C2D144 :solr:core:test --tests org.apache.solr.common.util.TestObjectReleaseTracker
```

4. Scoped checks, tests excluded:

```text
./gradlew --no-daemon --max-workers=2 -Pvalidation.errorprone=true :solr:core:check -x test :solr:solrj:check -x test
```

## Local logs

- Tidy: `/workspace/phone-agent-2-logs/objectrelease-autocloseable-round-1/tidy.log`
- Initial Error Prone compile: `/workspace/phone-agent-2-logs/objectrelease-autocloseable-round-1/errorprone-compile.log`
- Focused test and preserved JUnit XML: `/workspace/phone-agent-2-logs/objectrelease-autocloseable-round-1/focused-tracker-test.log` and `/workspace/phone-agent-2-logs/objectrelease-autocloseable-round-1/focused-tracker-test.xml/TEST-org.apache.solr.common.util.TestObjectReleaseTracker.xml`
- Scoped checks: `/workspace/phone-agent-2-logs/objectrelease-autocloseable-round-1/scoped-checks.log`
- Each run's `.headroom.log` records pre/post free disk and inode counts.

The candidate changes only the test-failure fallback. They do not alter normal production shutdown or establish this as the cause of all historical CI leaks.