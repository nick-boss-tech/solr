# Gate: SOLR-18619 request targeting on the frontend-only baseline, round 1

Status: OPEN. Intended host: Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain.

- Supersedes withdrawn `gates/solr-18619-request-targeting-round-2.md`; its earlier round stays superseded. Scope correction `gates/solr-12613-adjacent-scope-standdown-round-1.md` remains in force for old heads.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-frontend-scope-windows-round-1.md`; logging choice record `claims/solr-12613-adjacent-decision-ladder-round-1.md`.
- Fork `nick-boss-tech/solr:solr-18619-ui-request-targeting-followup`.
- Required head `be369c41c633d639ad66a7291a4d82891ea9836c`, independently verified after plain push. Ordinary merge of the clean baseline and original C4 head. LoggingController auto-merges while retaining main's callback-only logging-level GET contract.
- Baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, synthetic frontend-only picker integration preserving contributor ancestry and browser prerequisites. All solr/api and solr/core files match main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`; no backend expansion is in the final tree. Reconcile later with the contributor's actual revised head.
- Baseline job `gates/solr-12613-frontend-only-baseline-round-1.md`. Its completed matching receipt is a prerequisite to execution; a repaired baseline requires recomposition and a new exact-head job.
- Receipt `receipts/SOLR-18619-ui-request-targeting-round-1.md`.
- Seed `18619C0DE`. Run serially under the host build lock in full standalone checkouts suitable for root check, not the sparse Windows authoring tree. Regenerate the JavaScript client from this head's API sources.

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
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=18619C0DE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=18619C0DE
./gradlew check -x test
```

Require tidy-clean tracked tree; all selected tests executed with zero errors, failures, skips or thread leaks; root check success and changelog validation. Expected first three totals are 11, 3 and 2 methods. The inherited native-contract class has 3 methods and the picker neighbor remains 4 methods on this independent branch; report other neighbor totals from XML. The old event-watcher warning test is not substituted for these new no-skip tests: its historical shared-log4j skip cannot prove message aggregation. Record any root check/npm/Antora environment repair explicitly.

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
