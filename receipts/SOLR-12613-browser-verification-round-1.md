# SOLR-12613 Windows browser verification, round 1

Status: **PARTIAL — Windows browser slice recorded; full baseline gate remains OPEN.**

- Assignment: `assignments/solr-12613-selenium-handoff-round-1.md`.
- Active execution claim: `claims/solr-12613-selenium-baseline-windows-round-2.md`.
- Gate job: `gates/solr-12613-frontend-only-baseline-round-1.md`.
- Candidate: `nick-boss-tech/solr:solr-12613-frontend-only-baseline` at `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`.
- Parent: `b45bae5da3a151c7b439ff4e42009c383b538fae`; tree: `999a8f8199ea9a2449b44d634b70e0bdab6831a6`.
- The fork ref was re-read after execution and still points to the required head. This is a synthetic baseline; it is not the contributor's adopted revision.

## Host and generated client

| Item | Value |
| --- | --- |
| Host / OS | Windows 11 Enterprise, 10.0.26200 (build 26200), amd64 |
| JDK | Eclipse Temurin 21.0.11+10, 64-bit |
| Gradle | 9.7.0 distribution; the wrapper bootstrap downloaded its 8.10.2 wrapper JAR |
| Browser / driver | Chrome 157.0.8103.38; ChromeDriver 157.0.8103.38 via Selenium Manager |
| Browser flags | `--headless=new --window-size=1440,1024 --disable-gpu --no-sandbox --disable-dev-shm-usage` |
| `NODE_ENV` | Unset |
| Seed / JVM args | `12613F00D`; `-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m` |
| Generated API client | Built by `:solr:webapp:js-client:generateJsClientBundle`; the task log identifies source artifact commit `473a55ef64a9a1605e00eb6c1c386887d4cb7dac`. The baseline worktree's `solr/api/` and `solr/core/` diff against main was empty. |

All local wall-clock windows below are America/New_York on 2026-11-05. JUnit XML timestamps are UTC. The explicit `--tests` selection, seed, Selenium property and JVM args are recorded below; the screenshots and fresh XML are under the evidence directory. The uploaded text evidence was scanned for authorization headers, bearer values and secret-valued password/secret properties; none were found.

## Positive browser runs

| Slice | Command/result | Counts and evidence |
| --- | --- | --- |
| Contract + picker | `:solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest`; local log window 03:08–04:06; Gradle 58m26s; exit 0. | 7 passed, 0 failures/errors/skips: contract 3/3 and picker 4/4. [log](SOLR-12613-browser-verification-round-1-evidence/logs/positive-contract-picker.log), [contract XML](SOLR-12613-browser-verification-round-1-evidence/xml/positive/TEST-org.apache.solr.webapp.AdminUiNodeContractTest.xml), [picker XML](SOLR-12613-browser-verification-round-1-evidence/xml/positive/TEST-org.apache.solr.webapp.AdminUiNodePickerTest.xml). XML timestamp: 09:03:37Z. |
| Named neighbors | `:solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest`; local log window 04:08–04:16; Gradle 7m57s; exit 1. | 17 total: 16 passed, 1 failed, 0 errors/skips. NodeScreens 8/8, CoreAdminStandalone 4/5, LoggingScreen 2/2, LoggingStandalone 1/1, SecurityAuth 1/1. `AdminUiCoreAdminStandaloneTest.testAddCoreViaUi` timed out waiting for `add_name` after the Add Core panel stayed closed; the browser console was empty. [log](SOLR-12613-browser-verification-round-1-evidence/logs/positive-neighbors.log), [all five XML files](SOLR-12613-browser-verification-round-1-evidence/xml/neighbors/), [failure screenshot](SOLR-12613-browser-verification-round-1-evidence/screenshots/neighbor-add-core-failure.png), [captured page source](SOLR-12613-browser-verification-round-1-evidence/screenshots/neighbor-add-core-failure-page.html). |
| Isolated diagnosis | `:solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testAddCoreViaUi`; local log window 04:22–04:28; Gradle 5m19s; exit 0. | 1/1 passed. This investigated the suite failure; it does not replace or clear the failed neighbor run. [log](SOLR-12613-browser-verification-round-1-evidence/logs/diagnostic-add-core-rerun.log), [XML](SOLR-12613-browser-verification-round-1-evidence/xml/diagnostic/TEST-org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.xml). XML timestamp: 09:25:26Z. |

The neighbor failure is therefore **not green**. The isolated pass suggests a suite-order or timing interaction, but does not establish its cause. No source or test changes were made.

Exact Selenium invocations from each worktree root (PowerShell quoted the `-P` arguments):

```powershell
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testAddCoreViaUi '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest.testPropertiesUseCallbackOnlyContract '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest.testThreadsUseCallbackOnlyContract '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
.\gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest.testLoggingLevelsUseCallbackOnlyContract '-Ptests.selenium=true' '-Ptests.seed=12613F00D' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m'
```

## Isolated callback-argument mutations

Each mutation used a separate worktree at the same baseline head and changed only its designated controller call. Tests, API source and generated client were unchanged. Each command used the same Selenium flag, seed and JVM args as the positive run. Every test executed in Chrome and failed at the intended runtime assertion, `the existing API accepts only its callback expected:<1> but was:<2>`; none failed during compilation, browser startup or timeout.

| Isolated worktree change | Target method | Local log window / Gradle duration / exit | Result and evidence |
| --- | --- | --- | --- |
| Add `{}` to `NodeV2.getNodeProperties` in `java-properties.js`; keep `NodeTarget.withNodes()`. | `testPropertiesUseCallbackOnlyContract` | 04:38–05:11; 33m15s; exit 1 | Intended 1-versus-2 argument assertion. [diff](SOLR-12613-browser-verification-round-1-evidence/diffs/properties.diff), [log](SOLR-12613-browser-verification-round-1-evidence/logs/mutation-properties.log), [XML](SOLR-12613-browser-verification-round-1-evidence/xml/mutations/properties/TEST-org.apache.solr.webapp.AdminUiNodeContractTest.xml), [screenshot](SOLR-12613-browser-verification-round-1-evidence/screenshots/mutation-properties.png). XML timestamp: 10:08:41Z. |
| Add `{}` to `SystemV2.getThreadDump` in `threads.js`; keep `NodeTarget.withNodes()`. | `testThreadsUseCallbackOnlyContract` | 05:17–05:43; 26m10s; exit 1 | Intended 1-versus-2 argument assertion. [diff](SOLR-12613-browser-verification-round-1-evidence/diffs/threads.diff), [log](SOLR-12613-browser-verification-round-1-evidence/logs/mutation-threads.log), [XML](SOLR-12613-browser-verification-round-1-evidence/xml/mutations/threads/TEST-org.apache.solr.webapp.AdminUiNodeContractTest.xml), [screenshot](SOLR-12613-browser-verification-round-1-evidence/screenshots/mutation-threads.png). XML timestamp: 10:42:07Z. |
| Add `{}` to `LoggingV2.listAllLoggersAndLevels` in `logging.js`. | `testLoggingLevelsUseCallbackOnlyContract` | 05:50–06:13; 23m30s; exit 1 | Intended 1-versus-2 argument assertion. [diff](SOLR-12613-browser-verification-round-1-evidence/diffs/logging.diff), [log](SOLR-12613-browser-verification-round-1-evidence/logs/mutation-logging.log), [XML](SOLR-12613-browser-verification-round-1-evidence/xml/mutations/logging/TEST-org.apache.solr.webapp.AdminUiNodeContractTest.xml), [screenshot](SOLR-12613-browser-verification-round-1-evidence/screenshots/mutation-logging.png). XML timestamp: 11:11:40Z. |

## Remaining gate work

This receipt covers only the Windows browser slice. A capable Linux gate host still owns `./gradlew tidy`, `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=12613F00D`, and `./gradlew check -x test`, plus reconciliation of the browser evidence and neighbor failure. No full baseline receipt is claimed here. Lifecycle, request-targeting and combined candidate jobs were not started; their ordering remains governed by the assignment.

Two earlier baseline invocations were setup errors and were not counted: one ran from the workspace root rather than the Gradle project, and one let PowerShell split the dotted `-Ptests.selenium=true` argument. The quoted, corrected command above is the one whose 7 passing tests are reported. Windows performance-counter warnings appeared but did not prevent any reported test from executing.
