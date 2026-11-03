# Gate: SOLR-16398 rebalance and migrate coverage follow-up, round 2

Status: OPEN

Supersedes gates/pr-5284-coverage-round-1.md solely to correct TestApiFramework's fully qualified class name to org.apache.solr.handler.admin.TestApiFramework. Do not execute round 1. The source head, proof requirements and reserved receipt are unchanged; no gate execution has been reported at supersession.

- Tags: gate, implementation, review, records
- Intended host: any capable Linux gate host with JDK 21 and the branch's Gradle wrapper/build dependencies, serialized under its build lock
- Posted by: windows, 2026-11-03T18:02:04Z
- Implementation claim: claims/pr-5284-coverage-round-1-windows.md
- Implementation report: reports/pr-5284-coverage-round-1-windows.md
- Ticket: SOLR-16398; public PR: apache/solr#5284
- Fork: nick-boss-tech/solr
- Branch: solr-16398-rebalance-migrate
- Exact head H1: 08d13e33495689b67561f451b62a3e2d017df720
- Original PR head H0: 793e1ffa0f1d67271fbf090773651cd8a26171e4
- PR base: 1fdb455a524a7b8d334d5baea04e395ed4d2d7f8
- Module: :solr:core; documentation: solr/solr-ref-guide/modules/deployment-guide/pages/collection-management.adoc
- Fixed seed: 16398C0DE
- Receipt: receipts/pr-5284-coverage-round-1.md, added and owned by the executing host

## Scope and proof meaning

This is a coverage and documentation follow-up. No production defect was established and no production Java file changes between H0 and H1. Both the original suite at H0 and the expanded suite at H1 should pass. Do not claim fail-before on H0, or on main where these resource APIs do not exist.

The patch adds 10 MigrateDocsAPITest methods, 5 RebalanceLeadersAPITest methods, stronger synchronous migration and rebalance cloud assertions, one alias/async migration method, updated invalid-body HTTP coverage, and corrected guide examples. Reuses MockV2APITest, SolrCloudTestCase, cloud-minimal, the existing RequestStatus wait helper and ordinary SolrJ requests. No new fixture/schema/dependency.

Before claiming, refresh the live board and fork/PR head, check ownership/receipts, and perform detached tidy preflight. Record the execution host, clean checkout, JDK identity, runner, logs and XML paths in a new claim. A job posted here is not an execution claim or test result.

## Required positive legs

1. Original suite: clean checkout H0, fixed seed 16398C0DE, fresh logs/XML:

   `./gradlew :solr:core:test --tests org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest --tests org.apache.solr.handler.admin.api.V2CollectionAPIMappingTest --tests org.apache.solr.handler.admin.TestCollectionAPIs --tests org.apache.solr.handler.admin.TestApiFramework --tests org.apache.solr.handler.V2ApiIntegrationTest -Ptests.seed=16398C0DE --rerun-tasks`

2. Expanded suite: clean checkout H1, same seed, fresh logs/XML:

   `./gradlew :solr:core:test --tests org.apache.solr.handler.admin.api.MigrateDocsAPITest --tests org.apache.solr.handler.admin.api.RebalanceLeadersAPITest --tests org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest --tests org.apache.solr.handler.admin.api.V2CollectionAPIMappingTest --tests org.apache.solr.handler.admin.TestCollectionAPIs --tests org.apache.solr.handler.admin.TestApiFramework --tests org.apache.solr.handler.V2ApiIntegrationTest -Ptests.seed=16398C0DE --rerun-tasks`

   Require all selected tests executed and passed, zero failures/errors/skips. New unit counts: 10 and 5. CollectionCommandsV2IntegrationTest count: 3. Verify all three HTTP formats return 400 in V2ApiIntegrationTest.testException. The cloud test must actually move preferred leadership on both shards, preserve top-level Summary/successes, verify routing target and exact migrated target IDs (a!1 and existing!1, excluding b!1), and complete alias-based async migration with the exact returned request ID. A request ID or routing rule alone is insufficient.

3. Formatting and checks at the final submitted head: `./gradlew tidy` with zero changes, `./gradlew :solr:core:compileJava -Pvalidation.errorprone=true`, and `./gradlew check -x test`. Include api/core/reference-guide validation covered by those tasks. No silent exclusions. If tidy needs a scoped repair, commit and ordinary-push it to this fork branch with Nick's ICLA identity, read back the remote, add a superseding gate naming the new full SHA and old/new SHA, and rerun H1 positive/mutation proof at that new head. Do not issue a green receipt for H1 after formatting changed it.

## Isolated mutation legs

In disposable proof checkouts derived from the final submitted head, keep all tests unchanged. Apply exactly one compiling mutation at a time, record its diff, and execute the named method with `:solr:core:test --tests <fully-qualified-class>.<method> -Ptests.seed=16398C0DE --rerun-tasks`. Restore production code before the next mutation. Do not commit/push these mutations. Compilation, setup, missing-method or skipped-test failures do not qualify.

| Mutation | Required method | Intended runtime assertion failure |
|---|---|---|
| MigrateDocs: omit messageProps.put(COLLECTION_PROP, collectionName) | org.apache.solr.handler.admin.api.MigrateDocsAPITest.testAllPropertiesAndAsyncId | Captured message differs from expected source alias |
| MigrateDocs: omit target.collection insertion | same method | Captured message is missing target alias |
| MigrateDocs: omit SPLIT_KEY insertion | same method | Captured message is missing tenant! |
| MigrateDocs: omit forward.timeout insertion | same method | Captured message is missing explicit 45 |
| MigrateDocs: omit followAliases insertion | same method | Captured message is missing true |
| MigrateDocs: pass null instead of requestBody.async to submission | same method | Captured AdminCmdContext async ID differs from migrateAsyncId |
| RebalanceLeaders resource: omit params.set(MAX_AT_ONCE_PROP, requestBody.maxAtOnce) | org.apache.solr.handler.admin.api.RebalanceLeadersAPITest.testBodyParametersOverrideQueryParameters | Expected 2, observed original query value 99 |
| RebalanceLeaders resource: omit params.set(MAX_WAIT_SECONDS_PROP, requestBody.maxWaitSeconds) | same method | Expected 30, observed original query value 999 |

The shared mapping method contains multiple assertions: record which mutation each one discriminates. Assertions without a demonstrated mutation (including cloud outcome/default/validation/response pins) remain coverage pins; do not call the whole suite mutation-proven. Additional mutations are only justified by a remaining specific concern.

## Failure handling and receipt

Preserve every failed run's logs/XML and classify it before a retry. A setup/environment refusal that executes no tests may be relaunched once after recording the cause and exact repair; an executed failure needs diagnosis and a bounded repair. Do not disable security checks, change expected errors, remove assertions, increase replicas to hide routing failures, or widen exclusions to get green. Test/code/formatting repairs require a new ordinary fork commit, independent read-back and superseding exact-head job before proof is accepted.

The receipt must include commands, checkout/JDK/seed, start/end times, exit codes, fresh test counts, raw log/XML paths, each mutation diff and assertion, check/tidy results, and final fork/PR read-back. Finish with a proposed PR validation paragraph for Nick; do not edit the live PR body or post a public comment under this gate. Status remains Implemented, Proof Pending until all required legs pass at the actual live head.
