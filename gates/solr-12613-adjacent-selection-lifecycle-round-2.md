# Gate: core-menu targeting and response lifecycle, superseding round 2

Status: OPEN. Intended host: Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain.

- Supersedes and withdraws `gates/solr-12613-adjacent-selection-lifecycle-round-1.md` because the source head moved. No old-head result transfers.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; source continuation claim `claims/solr-12613-adjacent-windows-round-2-lifecycle.md`.
- Fork `nick-boss-tech/solr:pr-5110-followup-selection-lifecycle`.
- Required head `a331e7faf96c8c215f2d623f4865d4cbabca6492`, plain-pushed and independently read back. Previous head `e59bc25bf6dda7d3ca05ec6aad727d6ed7c51bd4` is now historical.
- UI baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, existing browser prerequisites `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`. The baseline merges are conflict-free.
- Receipt target `receipts/PR-5110-adjacent-selection-lifecycle-round-2.md`.
- Seed `5110C0AE`; full standalone checkouts suitable for root checks, not the sparse Windows authoring tree. Serialize under the host build lock.

## Delta and scenarios

The exact baseline-to-head files remain `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java`. The parent menu now waits for mode/target discovery before its first core read, clears target/menu-selection state for standalone and metadata-failure fallback, and keeps generation/selected-node guards against obsolete completions.

The picker suite now has 8 methods: the original 4 plus 4 regressions. The first two new methods retain the round-1 design: generated core requests execute normally, controller callbacks are held and supplied controlled valid inventories in reversed order, and assertions check new_core versus old_core in parent state and rendered options. One changes node; one refreshes the same node. Normal request construction must execute so the existing generated-client targeting plugin is consumed.

The startup regression uses Chrome's Page.addScriptToEvaluateOnNewDocument to capture only core-request URL and targeting header before Angular bootstraps, then reloads a persisted remote selection. AdminUiTestBase explicitly constructs ChromeDriver; no new browser framework or alternate browser is assumed. The CDP registration is removed in finally. The assertion requires every outgoing initial core request to carry the restored target; dashboard identity must also settle at that target. Do not normalize or inject a target in the capture shim. No Authorization header is captured.

The metadata-failure regression starts with cloud mode already known, substitutes a controlled 403 callback for SystemV2, and observes a normal successful local CoresV2 request after the failure callback settles. It asserts a core refresh was dispatched, waits for real core API success, and checks selection cleared. This is a controlled API-failure fallback test, not runtime evidence of a particular authenticated role configuration.

## Required positive legs

Verify the live fork matches the required head, record parents/tree, JDK/Gradle/Chrome/driver, UTC run times and NODE_ENV, then run:

```sh
./gradlew tidy
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew check -x test
```

Require tidy-clean tracked tree, all 8 picker methods executed and passing, neighbors passing, no errors/skips/thread leaks and successful root check. Preserve fresh XML before the next cleanTest. Report neighbor totals from XML. Record any Antora/npm/NODE_ENV setup condition beside the actual check result.

## Required fail-before leg

In an isolated checkout of UI baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`, overlay only the candidate's AdminUiNodePickerTest.java. Production app.js remains baseline. Record the test overlay diff, then run:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuIgnoresLateResponseFromPreviousNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuKeepsNewestRefreshForSameNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testRestoredSelectionTargetsInitialCoreMenuRequest --tests org.apache.solr.webapp.AdminUiNodePickerTest.testSystemInfoFailureStillRefreshesLocalCoreMenu -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Require all 4 methods to execute and fail at their intended explicit assertions: (1) new_core versus late old_core, (2) new_core versus late old_core for same-node refresh, (3) an initial core request with null/wrong target instead of the persisted remote target, (4) zero dispatched local core refreshes after the controlled metadata failure. Compilation, CDP registration failure, browser/API startup errors, script errors, wait timeouts or skips are not valid fail-before proof. The positive fallback leg must complete the real core API read, not only increment a counter.

## Repair and receipt requirements

Investigate any executed failure before retrying, separating setup from assertion failures. Tidy/test/source repairs require scoped ordinary commits as Nick Shanin, no trailers or PR references, plain fork push, independent new-SHA read-back and a superseding gate. All required legs must then run at the repaired head; do not transfer earlier proof.

The receipt must name exact head/baseline, commands/seed, toolchain/times, exits/counts, actual negative assertion messages, fresh logs/XML, final clean tracked tree and live fork SHA. End with a concise PR-description draft. Windows performed source review/diff --check only; no runtime, compilation, browser or Gradle run. The existing prerequisite receipt proves only those inherited fixes. This adjacent bug still needs its own valid public issue/PR identity before a new changelog is packaged. No PR, GitHub comment, Jira action, contributor-branch change or upstream push is authorized. Overall status stays Implemented, Proof Pending.
