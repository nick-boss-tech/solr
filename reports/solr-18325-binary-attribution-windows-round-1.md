# SOLR-18325 released consumer binary failure attribution

Keep the released-consumer binary requirement. Before restoring classes or adding a shim, run the missing unchanged-base artifact control. The source evidence points to an existing Solr client refactor, and the current receipt explicitly says this runtime comparison was not performed. This recommendation neither waives binary compatibility nor authorizes unrelated production changes.

## Evidence and recommendation

Board snapshot: `d5c92b3f35d26f458f4a8e81d02f2cf09b488612`. Read the owner-decision assignment, round 2 receipt/report, superseding gate and vm2 claim. The live fork source head is `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`; the exact pre-Jackson base is `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.

The round 2 receipt reports successful candidate HTTP, Java17/21 harness, six-class focused SolrJ, tidy, Error Prone compilation and root check at that head. It reports a failed released-10.0.0 `binaryHttp` lane and one inherited failure among 33 focused core tests. Those failures must remain visible; this is not an all-green gate.

Source history identifies [e5340a954b6700627b8826431a243c2e2759b0f3](https://github.com/apache/solr/commit/e5340a954b6700627b8826431a243c2e2759b0f3), SOLR-17968, as the client refactor deleting the top-level `HttpSolrClientBuilderBase`. It also renamed `HttpSolrClientBase` to `HttpSolrClient`, nested the builder as `HttpSolrClient.BuilderBase`, and changed `builder()` to `toBuilder(url)`.

At the released10.0.0 tag, `HttpJdkSolrClient.java:563-564` extends the top-level generic builder; its `withConnectionTimeout` method is declared at `HttpSolrClientBuilderBase.java:169`. At the pre-Jackson base, `HttpJdkSolrClient.java:595` extends nested `BuilderBase`, whose timeout method is at `HttpSolrClient.java:588`. These return-type descriptors are part of JVM method linkage; source-compatible fluent calls can still fail from already-compiled bytecode.

`git diff --stat f45b38d5f6c0 8bfca8edd30 -- solr/solrj/src/java/org/apache/solr/client/solrj/impl` is empty. Thus this prototype changes no source in that implementation package. The history and unchanged sources strongly support inheritance of the builder failure; the missing executable control will distinguish that from an artifact/classpath or Jackson-specific regression.

The assignment's A/B/C choices are premature as an engineering response. Retain the binary test and first compare the same old class bytes against three separately resolved runtimes: released10.0.0, the unmodified pre-Jackson base, and the Jackson candidate. If the base fails identically, the absolute backward-compatibility problem remains real, but it is a separate prerequisite rather than evidence that Jackson3 caused it. If the base passes and candidate fails, investigate the candidate/artifact difference before proposing a builder repair.

A nominally small shim must not be assumed sufficient: the same refactor also removed `HttpSolrClientBase` and changed other APIs. Any later repair needs a descriptor/supertype inventory and old-bytecode proof, not merely recreation of one class name. No restore, shim, source-only waiver or release-policy decision has been made here.

## Confirmed test design

Keep the `new com.fasterxml.jackson.databind.ObjectMapper()` changes in the four core tests: CoreAdminCreateDiscoverTest, CoreAdminHandlerTest, TestCloudManagedSchema and TestManagedSchema. Their diffs change the mapper/import and formatting, retaining the same converted inputs, TypeReference2 targets and assertions. The server intentionally remains on Jackson2 in this client-first prototype; these are independent server/oracle checks. Using the migrated client singleton with a Jackson2 TypeReference would couple the tests to the wrong runtime.

These four tests are not offered as proof of Jackson3 client conversion. That is covered separately by the added client compatibility tests and the real `candidateHttp` core-status conversion reported in the receipt. No assertion should be removed to hide the unrelated unload failure.

## Existing CoreAdmin tracking

The failing `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` is already tracked under SOLR-18587. The board has `assignments/solr-18587-coreadmin-unload-failed-reload-implementation-round-1.md`, `reports/solr-18587-coreadmin-windows-implementation-round-1.md` and `reports/rc3-test-failure-causes-round-2.md`. The latter records the unload fix and the MockDirectoryFactory test setup issue with matched pairs. The SOLR-18325 receipt independently reproduces the failure at its own unchanged base and seed. Do not file a duplicate or import that fix into the Jackson branch merely to make its core test summary green.

## Follow-up and evidence limits

The new bounded Linux job is `gates/solr-18325-binary-attribution-round-1.md`. It keeps the current candidate head fixed, preserves old fixture hashes, adds the missing base/released HTTP controls and asks for the complete JVM failure descriptors and runtime graphs. It does not rerun already-good broad checks without a changed input.

The round 2 receipt does not document the separately required actual Maven consumer resolution, mutation runs or a complete public/protected API inventory. Ask the execution host to link existing evidence or explicitly identify these as pending/unrun. A POM graph resolved by Gradle is useful evidence but is not a reported Maven resolver execution. Do not expand 'everything else passed' beyond what the receipt records.

Until that control finishes, the client-first verdict stays INCOMPLETE. A later report may distinguish 'no additional failure caused by Jackson in the measured comparison' from 'released10.0.0 binaries work unchanged'; those are different claims. Full server migration, 3.1 LTS target and release readiness remain unestablished.

This Windows follow-up made no Solr source change, ran no Gradle/test/queue and posted no public GitHub/JIRA communication. The next required action is executable Linux proof, so it is handed back to a capable Linux host.
