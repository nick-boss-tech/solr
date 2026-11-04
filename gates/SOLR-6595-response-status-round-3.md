# Gate: SOLR-6595 review repairs, round 3

Status: HANDED (Nick schedules Linux execution after the implementation review)
Tags: gate, review, records.
Intended host: a capable Linux gate host selected by Nick, with JDK 21, Gradle 9.7.0, Git, and the pinned Solr build dependencies.

Do not claim or execute this job autonomously. Nick said he will take the branch to the gate after review. This file prepares that handoff. Once Nick directs execution, the selected host may claim this exact job and produce its receipt; no edit of this immutable job is needed.

Supersedes `gates/SOLR-6595-response-status-round-1.md` and `gates/SOLR-6595-response-status-round-2.md`. Their heads are obsolete for the current implementation; do not run them as proof for F.

## Pinned inputs

- Fork: `nick-boss-tech/solr`, branch `solr-6595-submit`.
- Candidate F: `fb93b62a252fb09d90084abd80b904db512e2e1b`.
- Pre-repair parent P: `2b5a1d174f0471edb8280207ba877a4d7eb6e619`.
- Upstream base B: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Review-repair commit: `fb93b62a252` (`SOLR-6595: cover legacy failures and HTTP response status`).
- Windows independently read back the fork ref at F and checked the clean ticket worktree. No Gradle, compilation, tidy, or tests ran there. Worktree `git diff --check` passed before commit.
- Receipt to add: `receipts/SOLR-6595-response-status-round-3.md`.

## Scope and acceptance

The shared failure predicate now lives in `CollectionsHandler` and is used by both the older dispatch path (including MOVEREPLICA and its legacy V2 wrapper) and migrated `AdminAPIBase` endpoints. Non-empty failures set status 500 while preserving success and failure results. Null/empty failures retain status 0. Real exceptions retain precedence.

`DeleteNodeAPITest` now checks the actual HTTP code, parsed header status, and failure details for V1 and native V2. It also checks HTTP 200/header status 0 for a successful empty-node deletion and retains the async test. The generated V2 request's parser is overridden directly because its generated `getResponseParser()` ignores the base setter. `InputStreamResponseParser.consumeResponseToString` closes each raw stream. The two new unit suites now extend `SolrTestCase`.

SolrJ behavior remains relevant to interpreting results: V1 ordinarily throws on HTTP 500, whereas current V2 parsing may return a typed body when there is no top-level `error`. The new wire tests bypass that behavior with the raw stream parser and assert `HTTP_STATUS_KEY`. SolrJ exception payload storage is outside this patch.

## Execution rules

After Nick directs execution, refresh the fork head and require F, check for a live claim/receipt, then add the execution claim. Use one build at a time under the host lock, disk/inode guards, the supported gate runner, clean detached worktrees, fresh absolute work directories, and fresh logs/XML. Record host, UTC times, JDK version/path, runner, commands, exit codes, test counts/skips, and artifact paths.

A source or formatting repair changes F. Commit and push the repair to the fork, verify the ref, and obtain a superseding job before treating further results as proof. Preserve environment/setup failures separately from executed assertion failures. Investigate each failure under the board's repair rules before retrying; no silent exclusion or security-manager bypass.

## Candidate proof at F

1. Run the detached tidy preflight. Run `./gradlew --no-daemon --no-build-cache tidy` and require a clean tree.
2. Compile production and test sources with Error Prone:

   `./gradlew --no-daemon --no-build-cache :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`

3. Run all five focused classes with seed `6595C0FFEE0001`:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.core.SolrCoreResponseStatusTest --tests org.apache.solr.handler.admin.TestCollectionAPIs --tests org.apache.solr.handler.admin.api.DeleteNodeAPITest --tests org.apache.solr.handler.admin.api.ReloadCollectionAPITest --tests org.apache.solr.jersey.PostRequestDecorationFilterTest -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-positive`

   Require execution and success of the new legacy failure/empty-failure/exception cases, both DeleteNode wire tests, the async case, and both response-header unit cases. A Mockito assumption skip is incomplete proof. Record actual test counts from fresh XML.
4. Run the root check at pristine F:

   `./gradlew --no-daemon --no-build-cache check -x test`

   Record any execution condition needed for the successful check. Cached/up-to-date focused tasks do not count as fresh test execution.

## Five required negative proof legs

Use independent disposable worktrees. Keep candidate test source unchanged in each leg. Preserve exact source/test blob hashes, diffs, commands, exit codes, logs and fresh XML. Every negative leg must compile and reach the intended assertion. Missing APIs, compilation errors, startup failure, skipped tests, and environment interruption are not fail-before proof.

1. **Legacy failure regression against P:** start at P and copy only F's `solr/core/src/test/org/apache/solr/handler/admin/TestCollectionAPIs.java` unchanged. Run:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.TestCollectionAPIs.testLegacyMoveReplicaFailureSetsResponseStatus -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-parent`

   Expect header status 0 instead of 500 in the real older dispatch path. P already contains the status-override API used by the test, so no production scaffold is needed.
2. **Shared failure detection mutation at F:** change only `CollectionsHandler.hasFailedSubResponses` to return false. Run:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.api.ReloadCollectionAPITest.testFailedSubRequestReturnsServerErrorStatus -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-detection`

   Expect header status 0 instead of 500.
3. **V1 body status mutation at F:** remove only the `SolrCore.postDecorateResponse` branch that copies the override into the header. Run:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.core.SolrCoreResponseStatusTest.testHttpStatusOverrideIsAddedToResponseHeader -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-header`

   Expect header status 0 instead of 500.
4. **V1 HTTP status mutation at F:** remove only the override branch in `HttpSolrCall.writeResponse`. Keep the override and body-header machinery intact. Run:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.api.DeleteNodeAPITest.testDeleteNodeFailureReturnsHttp500 -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-v1-http`

   Expect the V1 `HTTP_STATUS_KEY` assertion to see HTTP 200 instead of 500 while the JSON body retains header status 500 and failure details.
5. **Native V2 HTTP status mutation at F:** remove only `PostRequestDecorationFilter`'s non-zero header-status mapping. Run:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.api.DeleteNodeAPITest.testDeleteNodeFailureReturnsHttp500 -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-round3-v2-http`

   Expect the V1 wire assertion to pass and the following native V2 wire assertion to see HTTP 200 instead of 500, with body header status 500 and failure details retained.

The success, empty-container, payload-preservation and exception-precedence assertions are coverage pins unless separately demonstrated by a discriminating mutation. Do not describe all assertions as mutation-proven.

## Deliverables

Add the execution claim, an evidence report, and `receipts/SOLR-6595-response-status-round-3.md` with every required result and limitation at exact F. A posted handoff is not an execution claim or a green gate. Keep **Implemented, Proof Pending** until the complete receipt passes. No PR, Jira, public comment, merge, or upstream write is authorized by this job.
