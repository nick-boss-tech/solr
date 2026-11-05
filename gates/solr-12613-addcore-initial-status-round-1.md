# Gate: SOLR-12613 Add Core initial-status race candidate

Status: OPEN. Intended host: capable Linux gate/Selenium host with JDK 21 and verified compatible Chrome/ChromeDriver. Tags: gate, selenium, review, records. Use the current host resource guard and serialize through its build lock.

This is a NEW proof job for a separately pushed implementation under `assignments/solr-12613-addcore-neighbor-timeout-implementation-round-1.md`, not a rerun or reclassification of the failed round-2 job. Read that assignment, its evidence/handoff, `reports/solr-12613-addcore-neighbor-timeout-implementation-round-1.md`, and full prior `gates/solr-12613-pr-head-lifecycle-round-2.md` before claiming. Preserve both old receipts, old claims and F2.

## Exact inputs

- Fork branch `nick-boss-tech/solr:solr-12613-addcore-initial-status`.
- Required G `75a18ecd07925a26a869dac8af8eed9ab4176bb9`, independently read back after a plain push.
- Only parent F2 `b0e096ae19ad637107227e1bc04e951b9023a680`, preserved on `pr-5110-core-menu-lifecycle`.
- Original contributor/base P `6b5d830132b941faa85856347099ade8b190be0e`, ancestor of F2 and G. PR 5110 remains unchanged by this task.
- F2..G has only cores.js and AdminUiCoreAdminStandaloneTest.java (+108/-1). P..G has those two plus app.js and AdminUiNodePickerTest.java (+370/-15). API/core/generated sources, fixtures, dependencies and changelog are unchanged.
- Implementation claim `claims/solr-12613-addcore-neighbor-timeout-windows-round-1.md`.
- Receipt target `receipts/SOLR-12613-addcore-initial-status-round-1.md`. Do not edit older receipts or call F2's failed gate green.

## Investigated failure and proposed mechanism

F2 round-2 picker passed 8/8, but the old 17-method neighbor selection failed Add Core at seed 5110C0AE; P at that seed and P/F2 at 12613F00D passed. Screenshot/DOM show a loaded first core and a closed Add form, console empty. Source can redirect bare Core Admin to its first core after the user has opened Add; view replacement then discards that form. This source race is plausible and consistent with the captured final state, not yet runtime-proven as the historical failure's precise event order.

G guards only that automatic URL change while Add is open. It preserves default selection/data and navigation when no form is open. New tests hold delivery of a real successful native status response, with normal generated request construction. They type name/instanceDir before release and assert the route stayed bare, form visible and values unchanged. The closed-form control checks canonical first-core navigation. Parent reads are not held (they explicitly pass indexInfo:false). No fake inventory, increased waits, skips or retry loops. The original testAddCoreViaUi is unchanged.

## Preflight and commands

Verify live G ref, exact ancestry, manifests, source cleanliness, toolchain/browser/driver and actual checkout/normal-clone HEAD. Root detached tidy before claiming per WORKFLOW. Record NODE_ENV (prior successful runs unset it), checksum/cache/CA/download conditions, UTC windows and source/client provenance. A wasm artifact commit is not OpenAPI provenance. Use full Linux checkouts; no Windows Gradle exception. A formatting/source/fixture repair moves the head and requires ordinary scoped commit, plain fork push/read-back, superseding job and fresh required proof. No force or contributor write.

Fresh root tidy, Error Prone compile, and exact-input generated client:

```sh
./gradlew tidy
./gradlew :solr:webapp:compileTestJava -Pvalidation.errorprone=true
./gradlew :solr:webapp:js-client:generateJsClientBundle
```

Every browser invocation below uses primary seed 5110C0AE and these flags unless the alternate seed is explicitly named:

```sh
-Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

### Positive candidate and original controls

1. G full `AdminUiCoreAdminStandaloneTest`: 7 methods must pass, including unchanged testAddCoreViaUi and both new methods. Start with this focused suite to detect a broken controlled setup before broader work. Use `:solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest` plus flags. Preserve XML before cleanTest.
2. G full `AdminUiNodePickerTest`: 8 pass at primary seed; original P picker: 4 pass at the same seed. No shared setup timeouts. Keep method order and XML.
3. Expanded full neighboring selection on G at BOTH 5110C0AE and 12613F00D, expect 19 methods per run (Nodes 8, standalone CoreAdmin 7, logging-level methods 2, standalone logging 1, auth 1):

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

4. Preserve the ORIGINAL 17-method selection too: run the following on G at BOTH seeds and on original P at BOTH seeds. New methods are intentionally excluded. Record actual method order rather than assuming the expanded class has the same order as the old failure. This extra selection is required because an existing 17-method failure must not be silently replaced with a changed inventory.

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testStandaloneMenus --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testAddCoreViaUi --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testRenameCoreViaUi --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testSwapCoresViaUi --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testUnloadCoreViaUi --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Every G positive run must have zero failures/errors/skips/thread leaks; the original P picker must also pass. P's original-17 selections are comparison controls, not candidate passing counts: the known Add Core symptom may reproduce there. If it does, retain that run, inspect its URL/form/console state and report it as an original-head failure; do not retry it into a pass or waive any failure on G. A different P failure, setup error, skip or leak needs investigation and cannot be silently attributed to the Add Core race. The deterministic fail-before below remains mandatory even if P's ordinary test times out.

If Add Core times out on G, preserve screenshot, current URL/path, page HTML, console and request/controller timing as available without secrets; investigate before retrying. New regression success alone does not clear an original-selection failure on G. Do not label a timeout a flake based only on other passing runs.

### Add Core runtime fail-before and navigation control

In isolated exact G, restore ONLY `solr/webapp/web/js/angular/controllers/cores.js` from F2. Keep both Java test files byte-identical to G and other production unchanged. Record diff and blobs. Run these two methods together with the primary flags:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testInitialCoreStatusKeepsAddCoreFormOpen --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.testInitialCoreStatusStillSelectsDefaultCore -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Require 2 executed methods: the open-form regression fails at the explicit assertion `Loading the default core must not navigate away from an open Add Core form`, expected `/~cores` versus `/~cores/<first core>`; the closed-form normal-navigation control PASSES. Request capture, successful real native status data, nonempty fixture and callback-delivery marker must all work. Compile/startup/script errors, timeouts, skipped tests or merely seeing a closed form after a setup failure do not satisfy this proof. Restore clean G afterward.

Separately from clean G, remove ONLY the `$location.path("/~cores/" + $scope.selectedCore).replace();` line inside the new guard (keep legal executable JS and all tests unchanged). Run `testInitialCoreStatusStillSelectsDefaultCore` with primary flags. It must fail at the explicit normal-navigation assertion, expected first-core path versus bare `/~cores`. This demonstrates the compatibility assertion notices accidentally disabling default navigation entirely. Record mutation diff, fresh XML/log and restore clean G. No mutation enters a fork branch.

### Existing lifecycle proof for the complete offer

In a separate clean G checkout restore ONLY app.js from original P, keeping cores.js at G and both Java test files at G. Run the exact four-method combined fail-before command from `gates/solr-12613-pr-head-lifecycle-round-2.md`, seed 5110C0AE. Require all four intended explicit old-production assertions in one run: cross-node and same-node stale inventory, initial target, missing local fallback refresh. No setup masking or isolated-only substitution. Restore clean G. This new branch contains the lifecycle offer, so its proof remains part of the whole acceptance bar.

### Root check and receipt

Run root `./gradlew check -x test` in a verified normal G clone as required for Antora, require success/clean tracked tree. Record actual NODE_ENV, npm/asset/cache/CA conditions and failed setup attempts separately. No backend tests are required or claimed; API/core trees are unchanged.

Receipt records exact G/F2/P, parents/trees/manifests, before/after fork refs and clean source; every command/seed/version/time/exit; fresh XML/log paths, actual counts/order; deterministic Add fail-before plus passing normal-navigation control; navigation-removal mutation; all original lifecycle assertions together; full 7/8/19 and original-17 selections; formatting/Error Prone/client/root results and historical-failure reconciliation. Explain what is proven about the automatic-navigation race and what remains unknown about the original unrecorded event order. Do not claim the old Windows or F2 receipts became green.

If a required run fails or the proposed cause is not established, return a factual failure/investigation result under this new job; no blind retries or waived assertions. Repairs move head and require replacement job/proof. Gate GREEN only when the complete new-head bar succeeds with no remaining patch concern. Source is Implemented, Proof Pending until then. No public PR/Jira comment, PR opening, contributor change or deprecation work is authorized by this execution job.
