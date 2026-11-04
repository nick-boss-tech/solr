# Gate receipt: SOLR-18619 Selenium failure repair slice, round 1

Status: **GREEN** at the exact live fork head `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.

## Scope and refs

This gate covered only the Selenium thread-filter and duplicate UI property-value fixes. It did not implement the ticket’s `X-Solr-targetNode` migration. The gate job is `gates/solr-18619-selenium-failure-fixes-round-1.md`; the active claim was `claims/pool-phone-agent-2-gate-SOLR-18619-selenium-failure-fixes-round-1.md`. The fork branch `nick-boss-tech/solr:solr-18619-submit` was checked before the run and again after all proof legs; it remained at `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`. The final checkout was a fresh standalone shallow clone (a real `.git` directory), suitable for the Gradle check task.

## Environment and preflight

The gate host was `phone-agent-2` on Linux x86_64. Runtime versions: OpenJDK `21.0.12.1`, Gradle `9.7.0`, Node `24.22.1`, npm `11.19.1`, Google Chrome `155.0.8059.39`, and Selenium Manager’s ChromeDriver `155.0.8059.84`.

At the exact candidate head, `./gradlew tidy` ran from 2026-11-04T21:13:54Z to 21:15:04Z UTC and exited 0: 62 actionable tasks, 62 executed, with no tracked changes. The wrapper is pinned to Gradle 9.7.0. Version evidence is in `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/preflight/gradle-version.log`.

## Selenium tests

The required fixed-seed command was run once with `NODE_ENV` unset:

```text
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest.testChangeLogLevelViaUi "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.seed=3B2F14147363C85B -Ptests.useSecurityManager=true -Ptests.file.encoding=US-ASCII -Ptests.selenium=true
```

Run window: 2026-11-04T21:17:37Z–21:18:47Z; exit code 0. Gradle reported 11 tests. Fresh JUnit XML totals: `AdminUiNodeScreensTest` 8, `AdminUiLoggingScreenTest` 2, `AdminUiLoggingStandaloneTest` 1; 11 total, 0 failures, 0 errors, 0 skips. No thread-leak markers were found. The XML was preserved before any later task:

- `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/tests/selenium.xml/TEST-org.apache.solr.webapp.AdminUiNodeScreensTest.xml`
- `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/tests/selenium.xml/TEST-org.apache.solr.webapp.AdminUiLoggingScreenTest.xml`
- `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/tests/selenium.xml/TEST-org.apache.solr.webapp.AdminUiLoggingStandaloneTest.xml`

Full test log: `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/tests/selenium.log`.

## Non-test check and final verification

`./gradlew :solr:webapp:check -x test` ran at the same head from 2026-11-04T21:19:31Z to 21:19:51Z UTC and exited 0 (`BUILD SUCCESSFUL`; 69 actionable tasks, 38 executed and 31 up-to-date). Log: `/workspace/gates/logs/phone-agent-2/solr-18619-selenium-failure-fixes-round-1/check/webapp-check.log`.

`git diff --check` passed and `git status --porcelain` was empty after the check. The remote fork ref was re-read at the final verification and still equaled the tested head. Live five-second inode/disk monitoring ran during tidy, tests, and check; minimum free-inode headroom was 63.77%, minimum root free disk remained above 50 GiB, and no stop marker occurred. The Gradle daemon was then stopped successfully; the host build lock is free and no Gradle/test process remains.

No PR, GitHub comment, Jira update, source edit, or upstream write was made. The gate file permits no such posting.

## PR description draft

**Summary:** Fix Selenium test thread-leak filtering for `JdkHttpClient-*` workers and preserve duplicate system-property values in the legacy UI template.

**Validation:** Exact-head Linux gate at `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`: all 11 selected Selenium tests passed with zero failures, errors, skips, or thread-leak reports; `:solr:webapp:check -x test` passed. This slice does not include the `X-Solr-targetNode` request migration.
