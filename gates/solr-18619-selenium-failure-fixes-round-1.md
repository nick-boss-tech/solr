# Gate: SOLR-18619 Selenium failure repair slice, round 1

Status: OPEN. Implemented, Proof Pending.

- Scope: the Selenium thread-filter and duplicate UI property-value fixes only. This does not implement the ticket's `X-Solr-targetNode` request migration.
- Fork branch: `nick-boss-tech/solr:solr-18619-submit`.
- Required exact head: `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.
- Parent: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Changed files: `solr/webapp/src/test/org/apache/solr/webapp/AdminUiTestBase.java` and `solr/webapp/web/partials/java-properties.html`.
- Capability requirements: Linux, JDK 21, pinned Gradle 9.7.0 and build dependencies, Node/npm access for the generated JavaScript client, and working Chrome/Chromium plus ChromeDriver/Selenium Manager.
- Windows report: `reports/solr-18619-selenium-failure-fixes-round-1.md`.
- Receipt: `receipts/SOLR-18619-selenium-failure-fixes-round-1.md`, written by the gate host.

The Windows implementation run no longer reports `JdkHttpClient-*` thread leaks, and `testCloudNodesListsAllNodes` passes with duplicate values preserved. Two other tests still hit intermittent Chrome renderer timeouts at initial navigation, including under the emitted JVM settings. A timeout is an incomplete environment result; do not count it as a pass or retry without recording the reason and environment change.

## Required proof

1. Fetch the fork branch and verify it equals the exact required head above. Use a fresh isolated worktree. Record JDK, Gradle, Chrome and ChromeDriver versions, UTC start/end, commands, exit codes, and fresh XML/log paths.
2. Run `./gradlew tidy`; require a clean tracked tree. If tidy changes tracked files, stop and request a formatting repair commit, push and a superseding exact-head gate.
3. Run the selected Selenium suites with a fixed seed and fresh test output:

   ```text
   ./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest.testChangeLogLevelViaUi "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.seed=3B2F14147363C85B -Ptests.useSecurityManager=true -Ptests.file.encoding=US-ASCII -Ptests.selenium=true
   ```

   Require all selected methods to execute with no failures, errors, skips, or thread-leak reports. `AdminUiLoggingScreenTest.testEventsViewerShowsWarnings` is excluded because its existing watcher-availability assumption can skip; do not count it as covered. Preserve the fresh XML and logs before another `cleanTest`.
4. Run `./gradlew :solr:webapp:check -x test` at the same head.
5. Run `git diff --check`, confirm the tracked tree is clean, and separately verify the remote fork ref still equals the tested head.

If Chrome/ChromeDriver fails to start or the renderer timeout recurs, record it as incomplete with fresh logs and artifacts; do not mark this gate green. Any source, test, or formatting repair needs a new fork push and a superseding gate for that head. This gate authorizes no PR, GitHub comment, or JIRA write.
