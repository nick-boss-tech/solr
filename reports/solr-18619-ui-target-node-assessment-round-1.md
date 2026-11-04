# SOLR-18619 assessment: Admin UI target-node requests

- Claim: `claims/pool-ui-assessment-SOLR-18619-windows-round-1.md`.
- Board at refresh: `coordination-ui` `89bae9c5b3ab29369413ec28c824c376f2776cca`. It contains no ticket-specific assignment or competing claim; the only 18619 entry is this assessment claim.
- JIRA: open, created 2026-11-04 04:53Z and updated 04:53Z. It is a follow-up to SOLR-18537 and explicitly leaves deprecating `node`/`nodes` out of scope.
- Code snapshot: `upstream/main` `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- This ticket targets the legacy Angular UI in `solr/webapp/web`, not the separate Kotlin Compose build under `ui/`.

## The ask

Move three cloud per-node Admin UI call paths to `X-Solr-targetNode`: the Metrics resource, the Nodes view's system information, and the Logging page's recent messages. In cloud mode, the Logging page should enumerate live nodes and make one proxied request to each; standalone mode should keep its direct local request.

## What exists

- `solr/webapp/web/js/angular/services.js:24-41` defines `Metrics` as an Angular `$resource` for `admin/metrics` with `node: "@node"` in its query parameters. `solr/webapp/web/js/angular/controllers/cloud.js:418-424` already makes one Metrics request per live node, passing `node`.
- `solr/webapp/web/js/angular/controllers/cloud.js:222-229` already obtains the live-node list with `ClusterV2.listClusterNodes()`. The Nodes view then calls `SystemV2.getNodeSystemInfo({nodes: ...})` once with the selected nodes at line 366 and maps the fan-out response into rows at lines 371-397.
- `solr/webapp/web/js/angular/controllers/logging.js:26-34` polls `LoggingV2.fetchLocalLogMessages({since: 0})` every ten seconds. The controller does not list cloud nodes. Its `nodes=all` logic at lines 165-179 applies to log-level changes, a separate behavior.
- The server already supports the requested routing. `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java:66-111` reads `X-Solr-targetNode` and proxies requests; lines 146-155 require SolrCloud and reject non-live nodes. The filter also accepts a GET parameter, but the ticket specifically asks the UI to use the header.
- The current V2 endpoint definitions expose only the old fan-out/query inputs: `solr/api/src/java/org/apache/solr/client/api/endpoint/NodeSystemInfoApi.java:33` accepts `nodes`, and `NodeLoggingApis.java:58` accepts `since` for local messages. The generated JS methods therefore populate empty per-call header maps. Their underlying `ApiClient.callApi` does accept a per-request header map; the webapp factories in `services.js:100-129` use the shared `ApiClient.instance`. Mutating shared `defaultHeaders` for parallel node requests would race, so the implementation needs a per-request header path or an explicit generated-client extension.
- Existing browser coverage is reusable but does not assert routing. `AdminUiNodeScreensTest.java:72-83` opens the two-node cloud Nodes view and checks row count and node ports. `AdminUiLoggingScreenTest.java:44-77` checks a single probe event in the log viewer. `AdminUiLoggingStandaloneTest.java:57-68` covers standalone log-level changes, not local log-message polling.
- Relevant history: `15bbe71c5aa` moved the live_nodes view onto `ClusterV2`; `b1cbbbf1a1f` migrated the Java Properties, Schema, and Zookeeper tree screens to V2. `0390a7e61e1` migrated log-level changes to V2 and retained the cloud-only `nodes=all` behavior; `e432df19c4a` fixed that behavior for standalone mode.

## What is missing

- Metrics and node-system-info calls need a safe per-request target header; neither should send the legacy `node` or `nodes` selector for this UI behavior.
- The cloud log viewer needs the live-node list, one targeted fetch per node, aggregation of the responses, and an unchanged local-only standalone path.
- Browser assertions need to distinguish the node being requested and verify the cloud and standalone paths. The current UI tests check rendered content but do not prove the target header or per-node fan-out.

## Windows baseline attempt

The workspace user explicitly authorized a Windows Gradle attempt. I ran the focused Selenium tests from a clean detached worktree at `ad26fdf2c6bee8168eae043b94833f05643d1995`; no source files were changed. Java was 21.0.11. ChromeDriver 156.0.8078.80 started Chrome 156.0.8078.48, confirming that the browser driver can start here.

The corrected run selected `AdminUiNodeScreensTest`, `AdminUiLoggingScreenTest`, and `AdminUiLoggingStandaloneTest` with `-Ptests.selenium=true`. It ended with 17 tests, 5 failures, and 10 skipped under seed `3B2F14147363C85B`:

- `AdminUiLoggingScreenTest.testChangeLogLevelViaUi` timed out waiting for `org.apache.solr.core` to read back as WARN. Server output shows the level update reached both cloud nodes, followed by a `nodes=all` request. The cause of the subsequent readback mismatch is unresolved and this test is outside the ticket's log-message behavior.
- `AdminUiLoggingStandaloneTest.testChangeLogLevelViaUi` passed, but its suite failed on three leaked `JdkHttpClient-*` threads. The cloud Nodes suite was skipped after the earlier leaked threads; the log viewer test was also skipped because this node's watcher did not observe the probe event.
- Gradle reported an internal test-result reporter error after the thread-leak failures.

I then ran only `AdminUiNodeScreensTest.testCloudNodesListsAllNodes` in a fresh worker, seed `85D41F38F0DE48AA`. It failed at initial browser navigation with a Chrome renderer timeout (`Timed out receiving message from renderer: 28.694`); the suite then reported three leaked `JdkHttpClient-*` threads and another internal Gradle test-reporter error.

The likely thread-leak source is visible in `AdminUiTestBase.java:170`: its Selenium thread filter excludes names beginning `HttpClient-`, while this JDK's leaked threads are named `JdkHttpClient-*`. The Chrome navigation timeout remains unexplained. These baseline runs are not proof of the ticket behavior and do not establish a clean Windows test path.

Local evidence is under `wt/SOLR-18619/solr/webapp/build/test-results/test/`, including the per-class XML and output logs.

## Verdict and handoff

**READY TO VOLUNTEER**, with the first code slice being a per-request target-header helper and conversion of the cloud Nodes system-info and Metrics calls. Then extend the Logging viewer to enumerate live nodes and fan out targeted reads while preserving its standalone branch. The staged ask should confirm the helper approach and name a reviewer before code changes, as required by `pool/decision-map.md`.

Windows can trace and implement the legacy Angular slice under its implementation/Selenium capabilities, but this baseline did not produce usable browser proof. The eventual phase-specific Selenium/build proof should go to a Linux gate host at the exact pushed ticket head. No code change, JIRA comment, GitHub comment, or gate job was made in this assessment.
