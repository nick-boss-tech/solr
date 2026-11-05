# Gate: composed adjacent follow-ups, integration round 1

Status: OPEN (execute after the required slice receipts are green at the pinned inputs).

- Intended host: capable Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium, the pinned Gradle toolchain and a serialized host build lock.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; composition claim `claims/solr-12613-adjacent-integration-windows-round-1.md`.
- Fork `nick-boss-tech/solr:pr-5110-followup-integration`.
- Required composed head `2dd6003d4b97e2b7eeeb762af361cf510851b087`, plain-pushed and independently read back.
- Baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, and existing browser prerequisites `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`.
- Receipt target `receipts/PR-5110-adjacent-integration-round-1.md`.
- Seed `5110F0110`. Use full standalone checkouts suitable for root check, not the sparse Windows authoring tree.

## Required slice prerequisites

Before claiming execution, read the actual receipts and matching job requirements, then verify the live input heads:

| Input | Pinned head | Required receipt |
| --- | --- | --- |
| encoded-key | `bd05d1ed39a005317f0a6655e4cd8fed078531b8` | `receipts/PR-5110-adjacent-encoded-key-round-1.md` |
| path-coverage | `96747864862bf716daf451e02a7c24194d41c267` | `receipts/PR-5110-adjacent-path-coverage-round-1.md` |
| selection-lifecycle | `a331e7faf96c8c215f2d623f4865d4cbabca6492` | `receipts/PR-5110-adjacent-selection-lifecycle-round-2.md` |
| request-targeting | `67cc03dfbed1866216c8cda8c31a65beb2c5672e` | `receipts/SOLR-18619-request-targeting-round-2.md` |

Their jobs are `gates/solr-12613-adjacent-encoded-key-round-1.md`, `gates/solr-12613-adjacent-path-coverage-round-1.md`, `gates/solr-12613-adjacent-selection-lifecycle-round-2.md`, and `gates/solr-18619-request-targeting-round-2.md`. Earlier UI round-1 jobs are withdrawn/superseded. A posted job is not execution proof. Do not substitute a prior prerequisite or original-PR green receipt.

If a slice was formatted/repaired and moved, this composition is stale for that input: publish an add-only handoff naming the new tested head and request refreshed composition plus a superseding integration job. Do not run the stale job or silently merge new inputs into this pinned head. Until prerequisites exist, capable gate hosts should work the open slice jobs first. Their required fail-before/mutation proof is not waived or repeated by this positive integration gate.

## Composition and scope

All four inputs were merged with ordinary commits, no manual resolutions. Merge sequence: encoded keys at `3815d5faedd58907e56161b989cbcc56b0417b2d`; path coverage at `933d9a42dd41f47a48c6a0e12cd3c3eae9bd5ace`; lifecycle at `694c204eb5afcb1c33b36bf978e32d2881b6a81f`; request targeting at the required final head. The common core test file auto-merged cleanly. Independent branches are preserved and are not declared proven by this integration receipt.

The 17 changed paths relative to baseline are the three core filter source/tests, SOLR-18619 changelog, configuring-logging.adoc, AdminUiTestBase, AdminUiNodePickerTest, AdminUiNodeScreensTest, AdminUiLoggingScreenTest, AdminUiLoggingStandaloneTest, index.html, services.js, app.js, controllers/cloud.js, controllers/logging.js, partials/cloud.html and partials/logging.html. This is the union of the exact file scopes in the slice reports, with the shared core test counted once. No new production edit was made during composition.

Combined contracts include encoded targeting/query errors; URI and directive preservation across a real replica hop; initialization before core-menu reads; local fallback after controlled metadata failure; obsolete-response rejection; explicit selected-local/remote headers; per-request Metrics/system reads; selected-node default logs plus optional all-node aggregation with origin/failure/recovery handling; and an untargeted standalone path. The logging default remains the reviewable choice documented through the decision ladder and must remain in the eventual unposted PR draft.

## Required integration execution

Verify the composed fork equals the required head and every input is an ancestor. Record Git tree/parents, versions/Chrome/driver, NODE_ENV and run times. Preserve fresh XML between cleanTest commands:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest --tests org.apache.solr.handler.admin.api.NodeAdminProxySolrCloudTest -Ptests.seed=5110F0110
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testSelectedLogMessagesUseTargetHeader --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogRequestsUseIndependentHeaders --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogPartialFailureAndRecovery --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew check -x test
```

Require tidy-clean tracked tree, all selected methods executed without failure/error/skip/thread leak, and successful root/changelog checks. Expected source inventories: 21 core filter methods plus 6 auth, 8 picker, 11 node-screen, 5 selected logging methods, and 2 standalone logging methods. Report all actual focused/neighbor totals from fresh XML. The old log-watcher warning test's shared-log4j skip is not a substitute for the new log coverage. Capture screenshots of the all-node log view and partial-failure view with test data, alongside browser logs, to assess the new column/control and error labels; screenshots are visual evidence only, not routing proof.

This gate specifically exercises C3's initialized core path together with C4's selected-local pinning and new request helper. It does not establish behavior merely because all four merges succeeded. Each new assertion's negative/mutation status remains as documented in the actual slice receipts; do not relabel the whole combined suite mutation-proven.

## Failure handling and receipt

Investigate executed failures before retrying under WORKFLOW.md. Any tracked formatting/source/test correction requires a scoped ordinary Nick Shanin commit, no trailers/PR references, plain fork push, independent new-SHA read-back and a superseding exact-head job. Rerun all integration legs on that head; do not transfer earlier proof. If integration reveals a slice defect, record that finding in an add-only report and preserve the independent branch provenance while arranging its repair.

Receipt must name exact composed/input heads, prerequisite receipt paths and verified proof legs, commands/seed, toolchain/times, exits and actual totals, fresh logs/XML/screenshots, final clean tree and live fork SHA. End with a concise unposted PR-description draft retaining the logging choice. No Windows runtime/Gradle/compiler/browser/test run occurred. No PR, comment, Jira action, contributor-branch change or upstream push is authorized. Remain Implemented, Proof Pending until all slice and composed proof is complete.
