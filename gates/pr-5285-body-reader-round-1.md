# Gate: SOLR-16398 JSON request-body error scope, round 1

Status: OPEN

- Tags: gate, implementation, review, records
- Intended host: any capable Linux gate host with JDK 21 and the branch's Gradle wrapper/dependencies, serialized under its build lock
- Posted by: windows, 2026-11-03
- Authority: Nick requested a patch to PR 5285 and a gating handoff.
- Implementation claim: claims/pr-5285-body-reader-round-1-windows.md
- Implementation report: reports/pr-5285-body-reader-round-1-windows.md
- Ticket: SOLR-16398; PR: https://github.com/apache/solr/pull/5285
- Fork: nick-boss-tech/solr
- Branch: solr-16398-malformed-body-400
- Exact submitted head H1: 176f29c71a5dbfab56c2ff03193b7c5ff8078f59
- Original reviewed PR head H0: e6fbade6773b3bf78e95f50ecc2335276daca651
- Original production base B: 1fdb455a524a7b8d334d5baea04e395ed4d2d7f8
- Module: :solr:core
- Fixed seed: 16398C0DE
- Receipt reserved: receipts/pr-5285-body-reader-round-1.md, added and owned by the executing host

## Scope

H1 adds one ordinary commit on H0. The global JsonProcessingExceptionMapper and registration are removed. CachingJsonMessageBodyReader.readFrom converts only JsonParseException and MismatchedInputException to a SolrException BAD_REQUEST with the original cause. Definition failures, general I/O failures, and resource/output failures retain their existing handling. Successful bodies still go through the existing caching reader.

Five files change from H0: JerseyApplications.java, deleted JsonProcessingExceptionMapper.java, MessageBodyReaders.java, PostRequestLoggingFilterTest.java, V2ApiIntegrationTest.java. All are under solr/core/src in their existing package locations. The original changelog remains correct and is unchanged. No new fixture/configset/dependency.

The new unit cases live beside existing reader cases in PostRequestLoggingFilterTest. Runnable is deliberately used as an invalid abstract server entity type; FailingResponse has a getter that fails during actual Jackson serialization. The server-error test uses Jersey's actual provider lookup from CoreContainerApp and then invokes the selected catch-all with the existing mocked context pattern. This proves provider selection and rendered status at that boundary, not a real HTTP serializer failure after response commitment.

Before claiming: refresh board, fork and PR head; verify no existing claim/receipt; run detached tidy preflight; record clean checkout/JDK, execution host, runner, logs/XML and timestamps in a new claim. Do not run alongside another gate on the same host. A posted job is not a claim or a pass.

## Required positive proof at H1

1. Run with fresh logs and XML, zero failed/error/skipped tests:

   `./gradlew :solr:core:test --tests org.apache.solr.jersey.PostRequestLoggingFilterTest --tests org.apache.solr.handler.V2ApiIntegrationTest --tests org.apache.solr.jersey.SolrJacksonMapperTest --tests org.apache.solr.jersey.JerseyApplicationSharingTest --tests org.apache.solr.handler.admin.TestApiFramework --tests org.apache.solr.handler.admin.TestCollectionAPIs -Ptests.seed=16398C0DE --rerun-tasks`

   Require all five new PostRequestLoggingFilterTest methods to execute (testJsonReaderCachesValidBody, testJsonReaderRejectsMalformedSyntax, testJsonReaderRejectsWrongFieldType, testJsonReaderPreservesDefinitionFailures, testServerJacksonFailuresRemainServerErrors). A Mockito/platform assumption skip is incomplete proof, not success. Verify the two real Jackson failures choose CatchAllExceptionMapper and render status 500. Require both V2ApiIntegrationTest.testUnparseableRequestBody and testMalformedJsonRequestBody to return 400 and the expected message through all three response parsers (XML, Javabin, JSON); the request body is JSON in every case.

2. Formatting/checks at the exact final submitted head:

   `./gradlew tidy` (zero diff), `./gradlew :solr:core:compileJava -Pvalidation.errorprone=true`, `./gradlew check -x test`.

   No silent exclusions. Record all required exit codes. Tidy/source/test repairs require an ordinary commit to the fork branch, independent remote read-back, and a superseding gate naming old/new full SHAs before any green receipt. Repeat positive and negative proof at the new submitted head. Never force-push the PR branch.

## Required negative proof

Use separate clean disposable checkouts; copy only the named H1 test files, retaining production code at the named older revision. Save the complete resulting diff before running. Tests must remain identical to H1. Missing APIs, compile failures, startup errors and skips do not establish fail-before.

1. On H0, copy only H1 `solr/core/src/test/org/apache/solr/jersey/PostRequestLoggingFilterTest.java`. Run:

   `./gradlew :solr:core:test --tests org.apache.solr.jersey.PostRequestLoggingFilterTest.testServerJacksonFailuresRemainServerErrors -Ptests.seed=16398C0DE --rerun-tasks`

   Expected runtime assertion failure: provider lookup selects JsonProcessingExceptionMapper instead of CatchAllExceptionMapper for the definition/output exception family. Record the assertion and fresh XML. This demonstrates the scope defect in the reviewed PR. The test checks both errors on H1; its first failing assertion on H0 may stop before the second case, so do not claim two independent negative executions.

2. On B, copy only H1 `solr/core/src/test/org/apache/solr/handler/V2ApiIntegrationTest.java`. Run:

   `./gradlew :solr:core:test --tests org.apache.solr.handler.V2ApiIntegrationTest.testUnparseableRequestBody -Ptests.seed=16398C0DE --rerun-tasks`

   Expected runtime assertion failure: 500 instead of 400 for the wrong-typed replicationFactor. Record HTTP status/assertion, logs/XML. This proves the original defect remains fixed by H1 without depending on the old mapper. The first failing parser may prevent later parser attempts on B; all three must pass on H1.

No synthetic production mutation is required: the original production revisions provide the two discriminating failures. Other assertions (cache, cause/message, definition passthrough and malformed syntax) remain positive coverage pins unless separately demonstrated; do not call the whole suite mutation-proven.

## Failure handling and completion

Preserve failed logs/XML and investigate before retrying. An environment refusal with zero tests may be relaunched once after recording cause and exact repair. Executed failures need diagnosis and a bounded repair; do not weaken assertions or disable security checks. Source/test/formatting repairs require new pushed head and superseding gate. If Jersey bootstrap/provider lookup needs fixture repair, record the cause and preserve the intended 500/400 discrimination.

Receipt must record checkout/JDK/seed, commands, start/end UTC times, exit codes, fresh counts and logs/XML paths, each negative diff and assertion, formatting/check results, final fork and PR read-back, and material environment conditions. Include a proposed validation paragraph for Nick and identify the needed PR body corrections: the implementation now converts client input errors at the reader, and the old proof head/breadth claims are stale. Do not edit the public body or post a comment under this job. Keep **Implemented, Proof Pending** until all legs pass at the live head.
