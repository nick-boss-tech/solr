# Gate: SOLR-12613 core-menu fixes on the actual contributor head

Status: OPEN. Intended host: capable Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain. Tags: gate, selenium, review, records. One run at a time under the host build lock.

Direct owner instruction: implement the small remaining correctness fixes on the PR head and hand them to the gate. Implementation claim: `claims/solr-12613-pr-head-lifecycle-windows-round-1.md`. Report: `reports/solr-12613-pr-head-lifecycle-round-1.md`.

## Exact inputs and supersession

- Fork: `nick-boss-tech/solr:pr-5110-core-menu-lifecycle`.
- Required candidate F: `f02bb7bb06decce976c3ed2fdfbf5907764c2183`, plain-pushed and independently read back through the fork refs API.
- Required parent/base P: `6b5d830132b941faa85856347099ade8b190be0e`, actual OPEN apache/solr PR 5110 head when prepared, not a synthetic integration. F has exactly P as its single parent.
- Outbound files only: `solr/webapp/web/js/angular/app.js`, `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java`. +261/-11. No API/core, generated-client, dependency, changelog or contributor-branch change.
- Receipt: `receipts/SOLR-12613-pr-head-lifecycle-round-1.md`. Keep Implemented, Proof Pending until every required leg succeeds at the final verified head.
- This supersedes `gates/solr-12613-selection-frontend-round-1.md` for the lifecycle author-offer slice. Do not spend a new run on its obsolete synthetic head `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`. Preserve that job/branch and all old evidence.
- The synthetic baseline job and its partial browser receipt are NOT prerequisites to this actual-head job and do not prove it. This receipt does not satisfy old combined/synthetic jobs at different heads; those would need explicit recomposition and new jobs. SOLR-18619 and transport/deprecation work stay outside this patch.

## Behavior and proof boundaries

MainController delays its first core request until mode/target identity settles, clears parent selection consistently for standalone/metadata failure, dispatches a real local core refresh after that failure, and guards callbacks by generation plus captured selection. Four added methods extend the existing picker class (4 original + 4 new = 8 methods).

The two ordering tests let the real generated core API request execute and consume its target header; only delivery of its controller callback is held. They supply controlled valid old/new inventories in reversed completion order and assert parent state plus rendered options. One changes nodes; one refreshes the same node. The restored-selection method installs Chrome CDP Page.addScriptToEvaluateOnNewDocument before reload and observes outgoing core-request URL and target header without injecting a target. Registration is removed in finally. AdminUiTestBase already constructs ChromeDriver; no new browser library is required. The metadata-failure method injects a controlled SystemV2 403 callback but delegates the subsequent local core read to the real generated API and requires successful completion. It is not an authenticated-role matrix or physical node-outage proof. No authorization header or system-property values are captured.

## Preflight and environment

Read live board/claim rules and actual host capabilities. Verify the live fork ref, P ancestry, complete outbound manifest and clean source tree. Use fresh full Linux checkouts; use a normal clone for root checks if required by Antora. Verify the clone actually contains F (`git rev-parse HEAD`), including fetching that commit explicitly when cloning locally. Do not run from the sparse Windows authoring worktree.

Record JDK/Gradle/Chrome/driver versions, OS, UTC windows, NODE_ENV and any CA/download setup. Regenerate or validate the JS client through this exact checkout's API inputs and `:solr:webapp:js-client:generateJsClientBundle`; record tasks, cache provenance and native API inputs. A downloaded wasm UI artifact SHA is not generated OpenAPI client provenance. P and F have identical API trees. Do not use an old bundle with optional v2 nodes arguments.

Run root tidy detached preflight before claiming execution, per board rules. Formatting repair moves the head: make a scoped ordinary Nick Shanin commit, plain-push/read back, publish a superseding exact-head job, then run every required leg at that repaired head. No force push or edits to the approved contributor branch.

Known fixture difference: the actual PR's AdminUiTestBase lacks the synthetic baseline's extra JdkHttpClient-prefixed thread filter. It was deliberately not copied into this patch. A reported thread leak must be investigated against P and F; do not suppress leaks or import broad thread exclusions to manufacture a pass. A demonstrated fixture defect may receive a minimal documented repair, with updated manifest, pushed head and replacement job; fresh proof is then required.

## Required positive legs

Use JDK 21 and the pinned Gradle 9.7.0 wrapper, with Chrome and matching driver. Primary seed `5110C0AE`. Every Selenium command below also takes:

```sh
-Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

1. On clean F, root `./gradlew tidy`; require clean tracked tree. Compile browser tests with Error Prone:

```sh
./gradlew :solr:webapp:compileTestJava -Pvalidation.errorprone=true
./gradlew :solr:webapp:js-client:generateJsClientBundle
```

2. On clean P, execute the unchanged original picker suite with the primary flags: expect 4 passing methods. On clean F, execute the full picker suite: expect 8 passing methods, including every newly added method. Separate logs/XML per head; preserve them before the next cleanTest.

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

3. Execute this full neighboring selection on both P and F at primary seed `5110C0AE`. Expected inventory is 17 methods: Nodes 8, standalone CoreAdmin 5, the two named logging-level methods, standalone logging 1, auth 1. Actual fresh XML counts govern. Require zero failures, errors, skips and thread leaks.

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

4. Prior neighbor failure reconciliation: inspect `receipts/SOLR-12613-browser-verification-round-1.md` and its AddCore failure log/XML/screenshot. Its synthetic-head Windows run was 16/17 at seed `12613F00D`; an isolated pass did not clear that selection. Repeat the entire step-3 selection on BOTH P and F, changing only the seed to `12613F00D`. Preserve fresh full-selection evidence. Do not substitute an isolated method or infer the old failure's cause from fresh success. Explain what the investigation establishes and whether any current patch/fixture concern remains. If the timeout repeats, investigate it as a failed gate before any retry. A historical cause can remain unproven while new exact-head paired proof is reported accurately; never relabel the old receipt green.

5. On clean F in a verified normal clone, run root `./gradlew check -x test`, require success and clean tree. Record material NODE_ENV, npm, Antora, truststore or downloaded-artifact conditions and distinguish failed setup attempts from the successful run. No separate backend test run is required by this UI-only job; do not claim one occurred. Existing changelog remains the contributor feature entry; validate it through the root check, with no unrelated author-field cleanup.

## Required runtime fail-before leg

In an isolated F checkout restore ONLY `app.js` from P. Candidate test file must stay byte-identical to F; other production/API/client/fixture files remain unchanged. Record the exact restoration diff and test blob, then execute:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuIgnoresLateResponseFromPreviousNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuKeepsNewestRefreshForSameNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testRestoredSelectionTargetsInitialCoreMenuRequest --tests org.apache.solr.webapp.AdminUiNodePickerTest.testSystemInfoFailureStillRefreshesLocalCoreMenu -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Require all 4 methods to execute and fail at their intended explicit assertions:

- Cross-node: expected parent inventory `new_core`, got late `old_core`.
- Same-node refresh: same intended stale-inventory assertion.
- Restored selection: an initial core request has null/wrong target instead of the persisted remote target, caught by `Every initial core-menu request must target the restored node`.
- Metadata failure: zero dispatched core refreshes, caught by `Failure to discover system info must not prevent a local core refresh`.

Compilation, CDP registration, browser startup, thread leaks, JavaScript errors, missing APIs, skips or wait timeouts do not count as these failures. If combined execution masks a failure, investigate and use fresh separate method runs with the same unchanged tests/seed; preserve the original result. No source scaffolding is needed because this is the actual PR's existing API. Restore/verify clean F after the isolated leg.

## Failure, repair and finish

No blind retry after an executed failure. Diagnose actual assertion/setup results and compare the named heads. A never-executed host interruption may follow the board's limited restart rule. Source, fixture or formatting repairs require ordinary scoped commits, fork push, separate full-SHA read-back, updated manifest and a superseding job. No force, contributor or upstream push. Do not edit another host's receipt or relax the acceptance bar to count skipped tests as proof.

Receipt must include final exact P/F (or superseding candidate), parents/tree, remote read-backs before/after, commands/seeds/toolchain/UTC times/exits, per-head fresh XML counts, all four actual negative assertion messages, old AddCore investigation and paired selection results, tidy/Error Prone/root results, client provenance, clean final source, and exact accessible log/XML/diff locations. End with a factual comment/PR-description draft. This execution job does not authorize public posting; existing owner instructions and standing clean-gate rules govern any later comment.

The source handoff is real when this file lands, but no host has claimed or executed it merely because it was posted. Current state: Implemented, Proof Pending.
