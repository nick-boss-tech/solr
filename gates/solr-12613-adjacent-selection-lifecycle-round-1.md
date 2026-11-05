# Gate: core-menu response lifecycle, adjacent follow-up round 1

Status: OPEN. Intended host: Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium capability and the pinned Gradle toolchain.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; implementation claim `claims/solr-12613-adjacent-windows-round-1.md`.
- Fork `nick-boss-tech/solr:pr-5110-followup-selection-lifecycle`.
- Required head `e59bc25bf6dda7d3ca05ec6aad727d6ed7c51bd4`, plain-pushed and independently verified. Guard/test commit `eb1548c7ac9491b5bb409c81ac8ba31508175b40`; the later ordinary merge adds already-gated browser prerequisites, not a force-push.
- UI integration baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`: initial main-plus-picker baseline `6ec2e527ad716bde74404b2037bb1a1e6c52f79c` plus existing gated Selenium/property repairs at `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.
- Main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`; picker parent `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`. No conflicts in either baseline merge.
- Receipt target `receipts/PR-5110-adjacent-selection-lifecycle-round-1.md`.
- Seed `5110C0AE`; serialize under the host build lock. Use full standalone checkouts suitable for root check, not the sparse Windows authoring tree.

## Delta and test design

Only `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java` differ from the UI baseline. The parent controller captures a request generation and selected node before fetching cores and ignores callbacks that belong to an obsolete request/selection. The already-gated thread filter and duplicate property-value fixes are inherited baseline prerequisites; do not claim them as new work or omit them from the baseline.

The two new browser tests exercise the actual MainController, selected-node state and rendered core options. They send the generated CoresV2 requests normally so the existing targeting plugin is consumed, hold delivery of the controller callbacks after successful network completion, then supply distinguishable valid core-status payloads in reversed order. One changes from remote to local; one refreshes the same node twice. The old callback is released after the new inventory renders, and an explicit $timeout sentinel waits for its digest. API methods are restored in finally. This is deterministic callback-order proof with controlled payloads, not a claim that distinct physical-node inventories were reproduced over a slow network.

## Required positive proof

Verify the live fork head, record parents/tree, toolchain, Chrome/driver and NODE_ENV, then run:

```sh
./gradlew tidy
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew check -x test
```

Require tidy-clean tree, all 6 picker methods passing, no errors/skips/thread leaks, neighbors passing and root check passing. Preserve fresh XML before each cleanTest and report neighbor totals from it. Record any Antora/npm environment repair beside the root check result.

## Required fail-before proof

Use isolated UI baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`; overlay only the new AdminUiNodePickerTest.java from the candidate and retain baseline production app.js. Record the overlay diff and run:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuIgnoresLateResponseFromPreviousNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuKeepsNewestRefreshForSameNode -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Both methods must execute and fail at the explicit stale-menu assertion (expected new_core, got old_core). Browser startup, API readiness, script errors, compilation, timeouts or skipped tests are not valid fail-before proof. Do not turn the callbacks into no-op requests, because doing so leaves the original picker's pending-target plugin state unconsumed and changes the scenario.

## Repairs and receipt

Investigate executed failures before retrying under WORKFLOW.md. If tests need repair or tidy changes tracked content, commit the scoped correction as Nick Shanin without trailers or PR references, plain-push, verify the new SHA, post a superseding gate and rerun every required leg. No proof transfers to a different source head.

Receipt names exact head/baseline, command/seed, toolchain/times, exit codes and test totals, fresh logs/XML, intended baseline assertion failures, final clean tree and live fork SHA. End with a short PR-description draft. Source was reviewed and diff --check passed; Windows ran no Gradle or tests. No new changelog was fabricated before this adjacent bug has an issue/PR identity. No public issue, PR, comment, contributor-branch change or upstream push is authorized. Keep Implemented, Proof Pending until all required legs pass.
