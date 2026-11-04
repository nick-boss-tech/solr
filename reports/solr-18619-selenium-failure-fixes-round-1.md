# SOLR-18619 Selenium failure repair slice, round 1

Status: Implemented, proof pending.

- Owner direction: Nick asked for the Selenium thread leak and the five failures from the Windows baseline to be patched.
- Base: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Pushed branch: `nick-boss-tech/solr:solr-18619-submit`.
- Exact pushed head: `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.
- Scope is limited to repairing the Selenium test thread filter and allowing duplicate system-property values in the legacy UI template. The ticket's `X-Solr-targetNode` migration is not implemented by this slice.

## Changes

- `AdminUiTestBase.WebDriverThreadsFilter` now recognizes the JDK's `JdkHttpClient-*` worker names as well as the older `HttpClient-*` prefix. These are daemon workers in the shared JDK HTTP client pool used by Selenium and can outlive `WebDriver.quit()`.
- `java-properties.html` tracks repeated property values by `$index`. Repeated Windows PATH entries are valid data and previously raised Angular `ngRepeat:dupes` in the Nodes screen.

## Windows evidence

The unmodified baseline had 17 tests, 5 failures and 10 skips under seed `3B2F14147363C85B`; it reported leaked `JdkHttpClient-*` threads, a log-level readback timeout, and downstream skips. A separate fresh run also timed out at initial Chrome navigation.

After the thread-filter change, the three focused classes ran 12 tests with one failure: `testCloudNodesListsAllNodes` raised `ngRepeat:dupes` for the duplicate value `C:\WINDOWS\system32`. No thread-leak failure remained.

After both changes, the same three-class run completed 12 tests with 2 failures and 1 assumption skip. `testCloudNodesListsAllNodes` passed. `testCloudTreeShowsZkNodes` and `testChangeLogLevelViaUi` timed out while Chrome was navigating to the initial `/solr/index.html#/` page, before their UI assertions. The log-viewer test was skipped by its existing watcher-availability assumption. No `JdkHttpClient-*` leak was reported.

A retry of those two methods with the Gradle-emitted JVM settings completed 2 tests with 1 failure: `testChangeLogLevelViaUi` again timed out at initial browser navigation. This is an unresolved Windows Chrome renderer/setup failure, not a passing result or an application assertion failure.

The positive and failure XML and logs are under `wt/SOLR-18619/solr/webapp/build/test-results/test/`; failure artifacts are under `wt/SOLR-18619/solr/webapp/build/tmp/tests-tmp/` when retained by Gradle. Chrome 156.0.8078.48 and ChromeDriver 156.0.8078.80 started successfully.

Linux exact-head proof is assigned in `gates/solr-18619-selenium-failure-fixes-round-1.md`. The ticket remains **Implemented, Proof Pending** for this slice.
