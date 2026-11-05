# Gate: frontend-only picker baseline and generated API contracts, round 1

Status: OPEN. Intended host: capable Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium and the pinned Gradle toolchain, serialized under its build lock.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-frontend-scope-windows-round-1.md`.
- Scope correction `gates/solr-12613-adjacent-scope-standdown-round-1.md` remains in force for its old inputs. This job is a new frontend-only baseline, not a reopening of any withdrawn head.
- Fork `nick-boss-tech/solr:solr-12613-frontend-only-baseline`.
- Required head `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, independently verified after plain push.
- Parent `b45bae5da3a151c7b439ff4e42009c383b538fae`: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, contributor picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, Selenium prerequisites `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.
- Receipt `receipts/SOLR-12613-frontend-only-baseline-round-1.md`; seed `12613F00D`.
- Synthetic integration candidate: the contributor has not adopted this revision. Reconcile later with their actual frontend-only head. Do not change their branch or post an adoption offer.

## Scope and preflight

All 17 inherited files under `solr/api/` and `solr/core/` are restored to main, including removal of the picker-only NodeAdminProxySolrCloudTest. Require `git diff --exit-code 07ccc73cd24e26e59da169ca35b3fd5f64c35986 HEAD -- solr/api/ solr/core/` to succeed. Keep main's existing log-level PUT broadcast unchanged. The inherited picker frontend, reference docs, original contributor changelog, and previously gated Selenium fixes remain.

The three controller adjustments call getNodeProperties, getThreadDump and listAllLoggersAndLevels with only their callback, matching main's existing OpenAPI contracts. Properties and threads prepare the existing targeting plugin immediately before the generated call. No backend schema, generated source, global API compatibility wrapper or new dependency is added.

New `AdminUiNodeContractTest` has three methods. It selects a remote node through the real UI, observes each controller's generated-service invocation, delegates to the original generated method, waits for a successful native response and checks rendered rows/tree. The observer stores argument count/type and a payload-shape boolean only; it does not retain property values, response contents or Authorization. This tests call compatibility and rendering, not independent node identity in the same-JVM fixture.

Use a fresh full standalone checkout suitable for root check. Build the generated client from this checkout's main-equivalent API sources; never reuse a bundle produced from the old expanded picker API. Record Git parents/tree, remote head, toolchain/Chrome/driver, UTC times, NODE_ENV and log/XML paths. No old prerequisite receipt proves this baseline.

## Positive legs

```sh
./gradlew tidy
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=12613F00D "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=12613F00D "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=12613F00D
./gradlew check -x test
```

Require tidy-clean tracked tree; zero failures/errors/skips/thread leaks in every selected test; successful root check and changelog validation. First webapp total is 3+4 methods; NodeScreens has 8; transport classes have 14+4. Report all other counts from fresh XML, preserved before each cleanTest. Do not substitute the historically skippable log-watcher warning test. Record any NODE_ENV/npm/Antora repair condition beside the successful result rather than relabeling an earlier failure as passing.

## Isolated runtime mutations

Keep APIs, generated client and tests unchanged. Use three separate isolated candidate checkouts and insert an empty options argument before the callback in exactly one controller call per checkout:

1. JavaPropertiesController: `NodeV2.getNodeProperties({}, function...` instead of the callback-only call. Retain the immediately preceding NodeTarget.withNodes() call. Run `AdminUiNodeContractTest.testPropertiesUseCallbackOnlyContract`.
2. ThreadsController: `SystemV2.getThreadDump({}, function...`. Retain NodeTarget.withNodes(). Run `AdminUiNodeContractTest.testThreadsUseCallbackOnlyContract`.
3. LoggingLevelController: `LoggingV2.listAllLoggersAndLevels({}, function...`. Run `AdminUiNodeContractTest.testLoggingLevelsUseCallbackOnlyContract`.

Use the same Selenium flags/JVM settings/seed as the positive leg. Each method must execute and fail at the explicit `the existing API accepts only its callback` assertion, expected 1 argument versus 2. The observer records arguments before delegating so an incompatible generated callback cannot turn the intended failure into a wait timeout. Record each actual mutation diff, assertion message and fresh logs/XML. Compile errors, browser startup/JavaScript setup errors, missing methods, skipped tests or timeouts do not satisfy this leg. This proves detection of the caller/contract mismatch; the successful native-payload and rendered-data assertions remain positive coverage pins.

## Repair and receipt

Investigate executed failures before retrying and distinguish host setup from assertion failures. Any formatting/source/test repair must be a scoped ordinary commit as Nick Shanin, without PR references or attribution trailers, plain-pushed to the fork and independently verified. Publish a superseding job at the new head and rerun all required proof; old-head results do not transfer.

Receipt records exact head/main/parent, generated-client source/build provenance, commands/seeds, toolchain/times, exits/counts, mutation diffs and intended assertion failures, fresh logs/XML, final tree and remote head. Include a concise PR-description draft stating that this is a synthetic baseline for follow-up proof and requires reconciliation with the contributor. No PR opening, GitHub/Jira comment, contributor/upstream write or claim of adoption is authorized. Windows ran no Gradle, compiler, browser or tests. Status remains Implemented, Proof Pending until all required legs succeed.
