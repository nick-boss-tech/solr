# SOLR-12613 Selenium handoff, round 1

Status: Windows runner assessment complete. The baseline execution is handed to a capable Linux browser/gate host. The overall Selenium assignment remains open until the ordered proof legs have actual receipts.

## Assignment and current board state

- Assignment: `assignments/solr-12613-selenium-handoff-round-1.md`.
- Board snapshot and live tip checked before report posting: `ee47c32965e840fbae55d8ee6e6894dd953d04ff`.
- Windows handoff claim: `claims/solr-12613-selenium-handoff-windows-round-1.md`, posted at board commit `ee47c32965e840fbae55d8ee6e6894dd953d04ff`.
- First required input: fork branch `solr-12613-frontend-only-baseline`, exact remote head `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`.
- Baseline job: `gates/solr-12613-frontend-only-baseline-round-1.md`, still OPEN. Its receipt `receipts/SOLR-12613-frontend-only-baseline-round-1.md` is absent.
- No result from an older picker job substitutes for this synthetic frontend-only baseline. The independently green transport inputs listed in the assignment cover transport code only and do not establish any UI/Selenium result.

The live coordination ref returned the same SHA as the fetched board snapshot. The fork ref for the baseline branch independently returned the required head. No test run or browser receipt was made from this Windows session.

## Windows execution finding

This host has the registered `selenium`, `review`, and `records` tags, but it does not have an approved runner for this exact JUnit job. The source checkout is on another branch, the checked SOLR-12613 worktree paths are absent, and no generated client or build artifact with verifiable provenance for the required head was identified. `chrome` and `chromedriver` were not found on PATH; the standard Chrome install paths checked were absent. Microsoft Edge was present, but that alone does not provide the Java Selenium runner or exact-head build artifacts. The assignment and workspace policy prohibit bypassing the Windows Gradle guard, using `-AllowGradle`, or reviving the Windows test queue.

The latest demonstrated Linux browser host is `phone-agent-2`. Its separate GREEN receipt, `receipts/SOLR-18619-selenium-failure-fixes-round-1.md`, records an x86_64 Linux run with JDK 21.0.12.1, Gradle 9.7.0, Chrome 155, and Selenium Manager's ChromeDriver 155. That run finished with its lock free and no process remaining. It covered a different branch and different tests, so it is capability evidence only. Before claiming SOLR-12613, that host must recheck its live claim and lock; another Linux pool host with the same required browser/build capability may take the handed slice.

## Exact first slice for the Linux executor

Use a fresh standalone checkout of `nick-boss-tech/solr:solr-12613-frontend-only-baseline` at `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. Preserve the job's exact source boundaries: the 17 inherited `solr/api/` and `solr/core/` files must match main, so `git diff --exit-code 07ccc73cd24e26e59da169ca35b3fd5f64c35986 HEAD -- solr/api/ solr/core/` must succeed. Build the generated client from this checkout's main-equivalent API sources. Record its commit/build provenance; do not reuse a bundle made from the older API-expanded picker.

Run and preserve fresh logs/XML for the following baseline gate commands, with the gate's JVM flags and seed unchanged:

```sh
./gradlew tidy
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=12613F00D "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=12613F00D "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=12613F00D
./gradlew check -x test
```

For the two webapp positives, require the named test counts from the gate, zero failures/errors/skips/thread leaks, and the rendered/native-response assertions. Then make three separate isolated mutation checkouts without changing APIs, generated client, or tests: add an empty options argument before the callback in `JavaPropertiesController` and run `AdminUiNodeContractTest.testPropertiesUseCallbackOnlyContract`; repeat independently for `ThreadsController` with `AdminUiNodeContractTest.testThreadsUseCallbackOnlyContract`; repeat for `LoggingLevelController` with `AdminUiNodeContractTest.testLoggingLevelsUseCallbackOnlyContract`. Each must fail at the intended callback-only argument-count assertion, expected 1 argument versus 2. A timeout, setup failure, compile error, skipped method, or missing browser is not a passing negative leg. Record each mutation diff, assertion, fresh log/XML, browser/JDK/driver versions, exact source provenance, times, exit code, and test counts. Keep mutations off fork branches.

The full baseline gate also requires tidy-clean state, the core neighbor tests, and `./gradlew check -x test`; a browser-only result must not be labeled as the full gate. Its actual Linux gate executor owns the full receipt. The required baseline receipt must be green before either follow-up browser slice starts.

## Remaining ordered slices

| Order | Exact input | Current gate and receipt | State |
| --- | --- | --- | --- |
| Order | Exact input | Required proof still outstanding | Current gate and receipt |
| --- | --- | --- | --- |
| Baseline | `solr-12613-frontend-only-baseline` at `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | Contract (3) and picker (4) tests; named node/core/standalone/logging/auth neighbors; three callback-argument mutations; tidy, core proxy/auth tests, root check. Handed to Linux; no browser result here. | `gates/solr-12613-frontend-only-baseline-round-1.md`; `receipts/SOLR-12613-frontend-only-baseline-round-1.md` |
| Lifecycle | `solr-12613-selection-lifecycle-followup` at `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` | Eight picker methods, three native-contract methods and named neighbors; four fail-before methods on the exact clean baseline with unchanged candidate tests; stale cross/same-node response handling, restored initial target and local core refresh after controlled metadata failure. Not started; baseline receipt required first. | `gates/solr-12613-selection-frontend-round-1.md`; `receipts/SOLR-12613-selection-frontend-round-1.md` |
| Request targeting | `solr-18619-ui-request-targeting-followup` at `be369c41c633d639ad66a7291a4d82891ea9836c` | Eleven node-screen methods, three new logging methods, two standalone logging methods and named neighbors; six isolated mutations; target headers, local pinning, log origins, partial failure/recovery, stale-event clearing and no standalone targeting/discovery. Not started; baseline receipt required first. | `gates/solr-18619-ui-request-targeting-round-1.md`; `receipts/SOLR-18619-ui-request-targeting-round-1.md` |
| Combined candidate | `solr-12613-adjacent-integration` at `84743822082ed2c409d87e40a6f8142e2a240042` | Waits for five matching prerequisite receipts; then full selected browser/core legs, including 22 filter and 6 auth tests, 3 contract, 8 picker, 11 node-screen, 5 selected logging-screen and 2 standalone logging tests, screenshots of All nodes/origin and partial-failure states, tidy and root check. Not started. | `gates/solr-12613-scoped-integration-round-2.md`; `receipts/SOLR-12613-scoped-integration-round-2.md` |

The baseline and both follow-up gate files remain OPEN; the combined gate is OPEN pending its prerequisites. The listed receipt paths were absent at the most recently checked tip. Only the baseline fork ref was refreshed for this handoff; the later branch refs must be refreshed before their respective claims. This handoff transfers only the first baseline execution slice and does not claim adoption, full readiness, or passing browser behavior.

