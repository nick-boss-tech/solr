# Gate: core-menu lifecycle on the frontend-only baseline, round 1

Status: OPEN. Intended host: Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain.

- Supersedes withdrawn `gates/solr-12613-adjacent-selection-lifecycle-round-2.md`; its earlier rounds stay superseded. Scope correction `gates/solr-12613-adjacent-scope-standdown-round-1.md` remains in force for old heads.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-frontend-scope-windows-round-1.md`.
- Fork `nick-boss-tech/solr:solr-12613-selection-lifecycle-followup`.
- Required head `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`, plain-pushed and independently read back. Ordinary conflict-free merge of the clean baseline and original C3 head; source/test delta is unchanged.
- UI baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, a synthetic frontend-only picker integration. All solr/api and solr/core files match main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`; contributor ancestry and existing browser prerequisites are preserved. Reconcile later with the actual contributor revision; this is not an adopted PR head.
- Baseline job `gates/solr-12613-frontend-only-baseline-round-1.md`. Its completed matching receipt is a prerequisite to execution; if the baseline is repaired, recompose this branch and issue a new exact-head job before running.
- Receipt target `receipts/SOLR-12613-selection-frontend-round-1.md`.
- Seed `5110C0AE`; full standalone checkouts suitable for root checks, not the sparse Windows authoring tree. Serialize under the host build lock and regenerate the JavaScript client from this head's API sources.

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
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew check -x test
```

Require tidy-clean tracked tree, all 8 picker methods executed and passing, all 3 inherited native-contract methods and other neighbors passing, no errors/skips/thread leaks and successful root check. Preserve fresh XML before the next cleanTest. Report neighbor totals from XML. Record any Antora/npm/NODE_ENV setup condition beside the actual check result.

## Required fail-before leg

In an isolated checkout of UI baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, overlay only the candidate's AdminUiNodePickerTest.java. Production app.js remains baseline. Record the test overlay diff, then run:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuIgnoresLateResponseFromPreviousNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuKeepsNewestRefreshForSameNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testRestoredSelectionTargetsInitialCoreMenuRequest --tests org.apache.solr.webapp.AdminUiNodePickerTest.testSystemInfoFailureStillRefreshesLocalCoreMenu -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Require all 4 methods to execute and fail at their intended explicit assertions: (1) new_core versus late old_core, (2) new_core versus late old_core for same-node refresh, (3) an initial core request with null/wrong target instead of the persisted remote target, (4) zero dispatched local core refreshes after the controlled metadata failure. Compilation, CDP registration failure, browser/API startup errors, script errors, wait timeouts or skips are not valid fail-before proof. The positive fallback leg must complete the real core API read, not only increment a counter.

## Repair and receipt requirements

Investigate any executed failure before retrying, separating setup from assertion failures. Tidy/test/source repairs require scoped ordinary commits as Nick Shanin, no trailers or PR references, plain fork push, independent new-SHA read-back and a superseding gate. All required legs must then run at the repaired head; do not transfer earlier proof.

The receipt must name exact head/baseline, commands/seed, toolchain/times, exits/counts, actual negative assertion messages, fresh logs/XML, final clean tracked tree and live fork SHA. End with a concise PR-description draft. Windows performed source review/diff --check only; no runtime, compilation, browser or Gradle run. The existing prerequisite receipt proves only those inherited fixes. This adjacent bug still needs its own valid public issue/PR identity before a new changelog is packaged. No PR, GitHub comment, Jira action, contributor-branch change or upstream push is authorized. Overall status stays Implemented, Proof Pending.
