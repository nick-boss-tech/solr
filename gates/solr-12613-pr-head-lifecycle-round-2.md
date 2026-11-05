# Gate round 2: picker isolation repair on the actual PR head

Status: OPEN. Intended host: capable Linux gate/Selenium host with JDK 21 and a verified compatible Chrome/ChromeDriver. Tags: gate, selenium, review, records. Serialize through the host build lock and current resource guard.

## Inputs, failure investigation and supersession

- Fork branch: `nick-boss-tech/solr:pr-5110-core-menu-lifecycle`.
- Required candidate F2: `b0e096ae19ad637107227e1bc04e951b9023a680`.
- Failed predecessor F1: `f02bb7bb06decce976c3ed2fdfbf5907764c2183`, F2's only parent.
- Original contributor/base P: `6b5d830132b941faa85856347099ade8b190be0e`, ancestor of F1 and F2; actual PR 5110 head when prepared. Use P, not F1, for original-production proof.
- Source delta F1..F2: only `AdminUiNodePickerTest.java`, +1/-3 in @Before. Production app.js is unchanged. Full P..F2 offer is still app.js plus that picker test only.
- Receipt target: `receipts/SOLR-12613-pr-head-lifecycle-round-2.md`.
- Repair claim/report: `claims/solr-12613-pr-head-lifecycle-isolation-round-2-windows.md`; `reports/solr-12613-pr-head-lifecycle-isolation-round-2.md`.
- Supersedes `gates/solr-12613-pr-head-lifecycle-round-1.md` for execution. Preserve its NOT GREEN receipt, evidence and terminal failed verdict. Earlier synthetic lifecycle/baseline/combined heads remain outside this proof.

READ the complete round-1 job and receipt, failure verdict and new repair report before claiming. All round-1 requirements remain mandatory unless explicitly tightened below. This is the repair after investigation, not a blind retry. The old receipt's passing legs are partial evidence at F1, not success at F2.

The F1 candidate XML shows cloud landing pass, metadata-failure pass, then all six remaining methods failing in @Before waiting for the node selector. The metadata test deliberately leaves the shared Angular app in non-cloud mode. Its API functions are restored, but direct hash navigation in the original @Before can keep that app alive. Existing AdminUiTestBase.openPage already forces a fresh same-page instance. F2 reuses that helper in @Before, then clears persisted selection as before. Confirm this explanation by the fresh full-suite run; do not assert the runtime issue solved from source alone.

## Preflight and decisive positive run

Verify remote F2, both ancestor pins, outbound paths, source cleanliness, toolchain/browser/driver and exact checkout/clone HEAD. Root tidy preflight before claim per WORKFLOW. No Windows Gradle exception. Record NODE_ENV and actual checksum/cache/download conditions; keep the old rejected wasm archive separate from a verified pinned archive. Build/use the JS client from F2's native API sources; a wasm artifact commit is not OpenAPI provenance.

Run root `./gradlew tidy` with a clean tracked tree; `./gradlew :solr:webapp:compileTestJava -Pvalidation.errorprone=true`; `./gradlew :solr:webapp:js-client:generateJsClientBundle`. Then prioritize the mandatory full F2 picker at the failing seed:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

Require all 8 methods passing, zero failures/errors/skips/thread leaks, and no node-selector setup timeouts. Preserve method execution order and fresh XML/logs so isolation across the metadata-failure method is visible. An isolated pass is not a substitute. If this full suite fails, investigate the executed failure before any retry or additional source repair; do not grind repeated seeds or raise waits to manufacture a pass.

## Remaining required fresh legs

Every baseline and candidate leg from round 1 must be executed fresh, replacing its F with F2 while keeping P unchanged:

1. Original picker on P at `5110C0AE`: 4 methods passing. Candidate F2: 8 methods, as above.
2. Full neighboring selection on BOTH P and F2 at BOTH `5110C0AE` and `12613F00D`: Nodes 8, standalone CoreAdmin 5, the two logging-level methods, standalone logging 1 and security auth 1 (17 methods per full selection; report actual fresh XML). Use the exact command from round-1 step 3, changing only checkout and seed. All runs zero failures/errors/skips/leaks. Include AddCore and preserve the prior Windows timeout's non-reproduction limit; do not relabel historical failures green.
3. Combined runtime fail-before below, with repaired F2 tests unchanged and only app.js restored from P.
4. Root `./gradlew check -x test` in a verified normal F2 clone where needed for Antora, clean tree afterward. Record material NODE_ENV, asset/cache/CA/npm/Antora conditions and actual command exits. No backend tests are required or claimed for this UI-only patch.

There is no synthetic-baseline receipt prerequisite. Do not import the absent synthetic contract test or a broad thread filter; investigate any real fixture defect. Preserve all logs/XML before the next cleanTest. Root checks and actual API/client generation remain required even though the repair is test-only.

## Combined fail-before: all four intended assertions

Use a separate exact F2 checkout. Restore ONLY `solr/webapp/web/js/angular/app.js` from P. Verify the repaired candidate test file remains byte-identical to F2 and capture its blob/hash plus restoration diff. APIs/generated-client/other fixtures remain unchanged. Execute all four methods together:

```sh
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuIgnoresLateResponseFromPreviousNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testCoreMenuKeepsNewestRefreshForSameNode --tests org.apache.solr.webapp.AdminUiNodePickerTest.testRestoredSelectionTargetsInitialCoreMenuRequest --tests org.apache.solr.webapp.AdminUiNodePickerTest.testSystemInfoFailureStillRefreshesLocalCoreMenu -Ptests.selenium=true -Ptests.seed=5110C0AE "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
```

The combined run must execute 4 methods and fail only at the 4 intended explicit assertions: `new_core` versus `old_core` for each ordering test, initial target missing/wrong instead of restored target, and missing local core refresh after metadata failure. Unlike round 1, isolated method runs do not satisfy this combined isolation bar. They may diagnose a failure, but setup timeouts, script/CDP/browser errors, compilation, skips and leaks are not intended proof. Keep the original combined result and investigate any masking; no blind retry. Restore clean F2 after the disposable negative run.

## Finish and further repair

Tidy/source/test/fixture changes move the candidate head. Commit ordinary scoped Nick Shanin repair, plain-push to the fork and separately verify the full SHA, publish a superseding job with new manifest/head, then run fresh proof. Never force or alter the approved contributor head. Do not edit another executor's receipt.

Receipt must give exact inputs/ancestry/tree, before/after remote refs and clean source, commands/seeds/versions/UTC windows/exits, full picker order and counts, all four combined fail-before assertion messages, all paired neighbor XML, prior AddCore reconciliation, tidy/Error Prone/client/root results and material execution conditions, plus accessible fresh evidence paths. End with factual comment wording. Do not call this GREEN unless every required leg passes at its final exact head. New source remains Implemented, Proof Pending until then. No public posting is authorized by this gate job; existing owner/clean-gate rules govern any later comment.
