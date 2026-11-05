# Gate: scope-separated adjacent follow-ups, integration round 1

Status: OPEN (execute after the required slice receipts are green at the pinned inputs).

- Intended host: capable Linux gate host with JDK 21, Chrome/ChromeDriver, Selenium, the pinned Gradle toolchain and a serialized host build lock.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; composition claim `claims/solr-12613-scoped-integration-windows-round-1.md`.
- Fork `nick-boss-tech/solr:solr-12613-adjacent-integration`.
- Required composed head `dabf21456845c0ae44b05b31e3893a5849dfc281`, plain-pushed and independently read back.
- Baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`: synthetic frontend-only picker integration with contributor/prerequisite ancestry retained, all inherited backend API expansion restored to main, three generated callback-only callers adapted, and three contract tests. Reconcile later with the actual contributor revision; this is not an adopted PR head.
- Receipt target `receipts/SOLR-12613-scoped-integration-round-1.md`.
- Seed `5110F0110`. Use full standalone checkouts suitable for root check, not the sparse Windows authoring tree.

This supersedes the withdrawn `gates/solr-12613-adjacent-integration-round-1.md`. The stand-down record `gates/solr-12613-adjacent-scope-standdown-round-1.md` continues to withdraw its old heads. This job supplies a new scope-separated input. No backend API work or SOLR-15823 logging-level GET broadcast is added.

## Required slice prerequisites

Before claiming execution, read the actual receipts and matching job requirements, then verify the live input heads:

| Input | Pinned head | Required receipt |
| --- | --- | --- |
| frontend-only baseline | `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | `receipts/SOLR-12613-frontend-only-baseline-round-1.md` |
| encoded-key | `7b83425fe7ce704ee781f273cd099b4425b795af` | `receipts/SOLR-18537-encoded-key-main-round-1.md` |
| path-coverage | `59ad950e1ac14d628e711e82746ee37c489cf151` | `receipts/SOLR-18537-path-coverage-main-round-1.md` |
| selection-lifecycle | `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` | `receipts/SOLR-12613-selection-frontend-round-1.md` |
| request-targeting | `be369c41c633d639ad66a7291a4d82891ea9836c` | `receipts/SOLR-18619-ui-request-targeting-round-1.md` |

The baseline job is `gates/solr-12613-frontend-only-baseline-round-1.md`. The four slice jobs are `gates/solr-18537-encoded-key-main-round-1.md`, `gates/solr-18537-path-coverage-main-round-1.md`, `gates/solr-12613-selection-frontend-round-1.md`, and `gates/solr-18619-ui-request-targeting-round-1.md`. All previous picker/API-expanded jobs remain withdrawn/superseded. A posted job is not execution proof. Do not substitute a prior prerequisite or original-PR green receipt.

If a slice was formatted/repaired and moved, this composition is stale for that input: publish an add-only handoff naming the new tested head and request refreshed composition plus a superseding integration job. Do not run the stale job or silently merge new inputs into this pinned head. Until prerequisites exist, capable gate hosts should work the open slice jobs first. Their required fail-before/mutation proof is not waived or repeated by this positive integration gate.

## Composition and scope

All four inputs were merged with ordinary commits, no manual resolutions. Merge sequence: encoded keys at `3466903dac6e2b417716748646f3cc68e6e11d0e`; path coverage at `fbeaeb24ae27928b7c8f4fa5c964c4d3229486af`; lifecycle at `3d0f598f19dab7f3de1a66d3068e063679147ac8`; request targeting at the required final head. The common core test file auto-merged cleanly. Independent branches are preserved and are not declared proven by this integration receipt.

The 17 changed paths relative to baseline are the three core filter source/tests, SOLR-18619 changelog, configuring-logging.adoc, AdminUiTestBase, AdminUiNodePickerTest, AdminUiNodeScreensTest, AdminUiLoggingScreenTest, AdminUiLoggingStandaloneTest, index.html, services.js, app.js, controllers/cloud.js, controllers/logging.js, partials/cloud.html and partials/logging.html. This is the union of the exact file scopes in the slice reports, with the shared core test counted once. No new production edit was made during composition.

Combined contracts include encoded targeting/query errors; URI and directive preservation across a real replica hop; initialization before core-menu reads; local fallback after controlled metadata failure; obsolete-response rejection; explicit selected-local/remote headers; per-request Metrics/system reads; selected-node default logs plus optional all-node aggregation with origin/failure/recovery handling; and an untargeted standalone path. The logging default remains the reviewable choice documented through the decision ladder and must remain in the eventual unposted PR draft.

## Required integration execution

Verify the composed fork equals the required head and every input is an ancestor. Require `git diff --exit-code 07ccc73cd24e26e59da169ca35b3fd5f64c35986 HEAD -- solr/api/` to succeed; main-relative solr/core changes must be exactly NodeTargetProxyFilter.java and its two test classes. Regenerate the JavaScript client from this exact head's API sources, never reuse an expanded-picker bundle. Record Git tree/parents, versions/Chrome/driver, NODE_ENV and run times. Preserve fresh XML between cleanTest commands:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest -Ptests.seed=5110F0110
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeContractTest --tests org.apache.solr.webapp.AdminUiNodePickerTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiNodeScreensTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testSelectedLogMessagesUseTargetHeader --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogRequestsUseIndependentHeaders --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testAllNodeLogPartialFailureAndRecovery --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testLoggingLevelTree --tests org.apache.solr.webapp.AdminUiLoggingScreenTest.testChangeLogLevelViaUi -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiLoggingStandaloneTest --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5110F0110 "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
./gradlew check -x test
```

Require tidy-clean tracked tree, all selected methods executed without failure/error/skip/thread leak, and successful root/changelog checks. Expected source inventories: 21 core filter methods plus 6 auth, 3 native-contract methods plus 8 picker, 11 node-screen, 5 selected logging methods, and 2 standalone logging methods. Report all actual focused/neighbor totals from fresh XML. The old log-watcher warning test's shared-log4j skip is not a substitute for the new log coverage. Capture screenshots of the all-node log view and partial-failure view with test data, alongside browser logs, to assess the new column/control and error labels; screenshots are visual evidence only, not routing proof.

This gate specifically exercises C3's initialized core path together with C4's selected-local pinning and new request helper. It does not establish behavior merely because all four merges succeeded. Each new assertion's negative/mutation status remains as documented in the actual slice receipts; do not relabel the whole combined suite mutation-proven.

## Failure handling and receipt

Investigate executed failures before retrying under WORKFLOW.md. Any tracked formatting/source/test correction requires a scoped ordinary Nick Shanin commit, no trailers/PR references, plain fork push, independent new-SHA read-back and a superseding exact-head job. Rerun all integration legs on that head; do not transfer earlier proof. If integration reveals a slice defect, record that finding in an add-only report and preserve the independent branch provenance while arranging its repair.

Receipt must name exact composed/input heads, prerequisite receipt paths and verified proof legs, commands/seed, toolchain/times, exits and actual totals, fresh logs/XML/screenshots, final clean tree and live fork SHA. End with a concise unposted PR-description draft retaining the logging choice. No Windows runtime/Gradle/compiler/browser/test run occurred. No PR, comment, Jira action, contributor-branch change or upstream push is authorized. Remain Implemented, Proof Pending until all slice and composed proof is complete.
