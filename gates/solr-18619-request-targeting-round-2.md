# Gate: selected-node pinning and admin read targets, superseding round 2

Status: OPEN. Intended host: Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain.

- Supersedes and withdraws `gates/solr-18619-request-targeting-round-1.md` after branch movement. Old-head proof cannot satisfy this job.
- Source continuation claim `claims/solr-18619-request-targeting-windows-round-2.md` pins the selected-local amendment.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-adjacent-windows-round-1.md`; choice record `claims/solr-12613-adjacent-decision-ladder-round-1.md`.
- Fork `nick-boss-tech/solr:pr-5110-followup-request-targeting`.
- Required head `67cc03dfbed1866216c8cda8c31a65beb2c5672e`, independently verified after plain push. Production/test commit `7e2076f391bf084e0e9964b8bfbcef3879712e37`; the next commit strengthens explicit failure assertions. The latest commit also pins selected-local generated reads once cloud identity is known.
- Baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, and previously gated Selenium/property fixes `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`, integrated without conflicts. The prior receipt for those fixes does not prove this feature.
- Receipt `receipts/SOLR-18619-request-targeting-round-2.md`.
- Seed `18619C0DE`. Run serially under the host build lock in full standalone checkouts suitable for root check, not the sparse Windows authoring tree.

## Contract and scope

NodeRequests allocates an Angular $http target header map for each call and keeps ordinary authentication/error interception. Metrics removes the old node query parameter; Nodes system information makes one native-response request per node. The Nodes table identifies per-node system/metrics failures. After cloud identity is known, selected-local generated properties, threads and core-menu reads also carry explicit target headers, because a load balancer may route a later request to another node. Standalone and unselected discovery calls remain untargeted. The selected-node log view remains the default; a cloud-only All nodes control lists live nodes, reads each through its own header, labels message origins, preserves healthy siblings and names failures. Standalone does one untargeted local message read and no node discovery. Log-level broadcasting is unchanged. Polling schedules after settlement and obsolete/destroyed-view completions are ignored.

This logging default is a reviewable proposal selected through the decision ladder at Nick's direction. Preserve it for this proof; do not redesign the default inside the gate. The eventual PR draft poses the alternative of always aggregating cloud messages. This gate does not authorize public posting.

Exact baseline-to-head files:

- `changelog/unreleased/SOLR-18619-target-node.yml` (Nick Shanin, name only).
- `solr/solr-ref-guide/modules/deployment-guide/pages/configuring-logging.adoc`.
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiTestBase.java`.
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodeScreensTest.java`.
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.java`.
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingStandaloneTest.java`.
- `solr/webapp/web/index.html`.
- `solr/webapp/web/js/angular/services.js`.
- `solr/webapp/web/js/angular/controllers/cloud.js`.
- `solr/webapp/web/js/angular/controllers/logging.js`.
- `solr/webapp/web/partials/cloud.html`.
- `solr/webapp/web/partials/logging.html`.

The shared XHR capture helper stores only method, URL and targeting header; it must never collect or print Authorization headers. It is reused by cloud node, log and standalone tests. Controlled failure tests stub NodeRequests results while retaining the real controller and rendered template; they do not claim a physical node was killed. Existing request-targeting transport tests remain real integration neighbors.

## Positive legs

Verify live fork head before execution and record toolchain/Chrome/driver, parents/tree, times and NODE_ENV. Use the following exact selections; preserve fresh XML between cleanTest invocations:

```sh
./gradlew tidy
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest -Ptests.selenium=true -Ptests.seed=18619C0DE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testSelectedLogMessagesUseTargetHeader --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogRequestsUseIndependentHeaders --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogPartialFailureAndRecovery -Ptests.selenium=true -Ptests.seed=18619C0DE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest -Ptests.selenium=true -Ptests.seed=18619C0DE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=18619C0DE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=18619C0DE
./gradlew check -x test
```

Require tidy-clean tracked tree; all selected tests executed with zero errors, failures, skips or thread leaks; root check success and changelog validation. Expected first three totals are 11, 3 and 2 methods. The picker neighbor remains 4 methods on this independent branch; report other neighbor totals from XML. The old event-watcher warning test is not substituted for these new no-skip tests: its historical shared-log4j skip cannot prove message aggregation. Record any root check/npm/Antora environment repair explicitly.

## Isolated mutations: functioning feature scaffold, tests unchanged

This adds a UI control/helper absent from the original baseline. Missing controls/APIs are not valid fail-before proof. Use separate isolated candidate checkouts for the following runtime mutations, restoring unmutated source between each. Record each exact diff and fresh logs/XML. Use the same Selenium/JVM flags and seed as above, with the named test selections. A failure must occur at the intended explicit assertion, never compile/setup/timeout/skip.

1. In NodeRequests.get, replace `config.headers['X-Solr-targetNode'] = node;` with `config.headers = {};`. Run `AdminUiNodeScreensTest.testCloudNodesUseRequestSpecificTargets`, `AdminUiLoggingScreenTest.testSelectedLogMessagesUseTargetHeader` and `AdminUiLoggingScreenTest.testAllNodeLogRequestsUseIndependentHeaders`. Require failures at node-identity or required-target-header assertions; the helper/UI remain executable and the server still returns successful local responses. This proves routing/header assertions, not log-event contents.
2. In LoggingController replace `copy.node = result.node;` with `copy.node = 'wrong-node';`. Run `AdminUiLoggingScreenTest.testAllNodeLogPartialFailureAndRecovery`. Require failure at the explicit healthy-event origin assertion after the healthy probe has rendered.
3. Remove only `$scope.failedLogNodes.push(result.node || 'this node');`, retaining the error branch's return and all healthy aggregation. Run that same partial-failure test. Require failure at its explicit assertion that the failed remote node is named beside retained healthy messages.
4. Replace `$scope.events = events;` with `if (events.length) { $scope.events = events; }`. Run the partial-failure/recovery test. Require failure at its final `events.length == 0` assertion after both nodes fail, showing stale recovered events cannot remain presented as current data.
5. After NodeRequests.get creates config, add `config.headers['X-Solr-targetNode'] = node || 'stale-node';`, retaining the existing node branch. Run `AdminUiLoggingStandaloneTest.testLogMessagesStayLocalWithoutNodeDiscovery`. Require failure at its explicit null-target assertion on the captured outgoing message request, demonstrating accidental targets are detected for the local path.

6. In NodeTarget.withNodes only, restore `if (isRemote())` in place of `if (state.localNode && state.selectedNode)`. Run `AdminUiNodeScreensTest.testSelectedLocalNodeReadsRemainPinned`. Require failure at the explicit required-target-header assertion for the selected-local properties read; the real endpoint must still answer and the executable picker/generated API scaffold remains intact. The positive method also checks threads and core-menu reads. Do not claim independently negative loop legs after the first baseline assertion stops execution.

The system-info partial-failure test and other assertions without a named discriminating mutation remain positive coverage pins. Do not call the entire suite mutation-proven. Existing unauthenticated/authenticated transport neighbors do not constitute client-certificate proof; no new security claim is made.

## Failure handling and receipt

Investigate any executed failure before retrying. Distinguish host setup from assertion failures. Formatting/source/test repairs must be committed as Nick Shanin without trailers/PR references, plain-pushed to this fork branch, independently verified and assigned a superseding exact-head gate. Rerun all required proof at the repaired head; do not reuse old results for it.

Receipt records head/baseline, commands/seed, toolchain/times, exits/counts, each mutation diff and intended assertion failure, fresh logs/XML, final clean tree and live remote SHA. Include a short PR-description draft with the logging default/All nodes tradeoff for maintainer review as Nick requested. Windows ran no Gradle or tests. Status stays Implemented, Proof Pending until the complete receipt is green. No PR, comment, Jira action, contributor-branch write or upstream push is authorized.
