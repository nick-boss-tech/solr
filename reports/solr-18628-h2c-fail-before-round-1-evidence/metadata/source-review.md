# SOLR-18628 H1 source and history review

## Exact source scope

- Fork branch: `nick-boss-tech/solr:solr-18628-submit`
- H1: `aebe1d8e9b0f226948e092f2610682c9c2596f27`
- Production baseline B: `c0c219e374611e0edc475c0f793c9bd688b13292`
- H1 commit: `aebe1d8e9b0f226948e092f2610682c9c2596f27`, subject `SOLR-18628: shorten idle reconnect reproduction`, parent `ac85e718803994d258c766e66d18831c5de24d3a` (`SOLR-18628: reproduce streamed h2c idle reconnect failure`).
- `git diff --name-status B H1` reports exactly one changed path: `solr/solrj/src/test/org/apache/solr/client/solrj/impl/HttpJdkSolrClientTest.java`. The diff is 41 added lines; `git diff --check B H1` returned 0. No production or changelog file differs from B.

## Relevant code paths read

- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpJdkSolrClient.java:306-324`: when a content writer is present, request preparation calls `maybeTryHeadRequest(url)`; if that returns false it forces HTTP/1.1, otherwise it creates the streamed request body with `BodyPublishers.ofInputStream`.
- `HttpJdkSolrClient.java:417-467`: `maybeTryHeadRequest` bypasses the HEAD probe for forced HTTP/1.1, null URLs, or HTTPS; the synchronized helper checks `headSucceededByBaseUri`, sends one HEAD request for a URI not already cached, then stores the success/failure for future requests.
- `solr/solrj/src/test/org/apache/solr/client/solrj/impl/HttpJdkSolrClientTest.java:643-680`: the test asserts the first h2c streamed update succeeded and recorded a HEAD success, performs an HTTP/1.1 control update, waits `httpClientKeepAliveTimeoutSeconds() + 2`, then performs an HTTP/1.1 control and the second h2c update. The helper reads `jdk.httpclient.keepalive.timeout.h2`, then `jdk.httpclient.keepalive.timeout`, defaulting to 30 seconds.
- `gradle/testing/alternative-jdk-support.gradle:34-40,65-77`: `runtime.java.home` or `RUNTIME_JAVA_HOME` selects the alternative runtime; `Test` tasks use that Java executable. This corrects the initial claim's `SOLR_JAVA_HOME` wording via the separately posted claim addendum.

## Relevant history and result

The test-file history is H1 `aebe1d8e9b` (property-shortened wait), parent H0 `ac85e71880` (fixed 35-second wait), followed by earlier SolrJ changes. The production client file's recent history includes `78862e7f54` (`SOLR-18351: remove ContentStream from SolrJ's client write path`) and `282346f941` (`SOLR-18312: HttpJdkSolrClient: Fix Thread Pool Starvation`). This is context only; no production hypothesis was advanced after the H1 test passed.

The exact H1 focused run was executed once with seed `18628C0DE`, Gradle on JDK 21.0.12.1, the test runtime selected as Temurin JDK 25.0.5+8, and `JAVA_TOOL_OPTIONS=-Djdk.httpclient.keepalive.timeout=1`. The fresh JUnit XML reports 1 test, 0 failures, 0 errors, 0 skipped, with the test taking 3.674 seconds. The test passed, including the second h2c update; the intended fail-before at that update was not reproduced. The worker output identifies Jetty's JVM as `25.0.5+8-LTS`; the Gradle log reports the JDK 25 alternative runtime and the keepalive option immediately after the Test Executor started.

**Verdict: NON-REPRODUCED, not a green fail-before proof.** No production source, fork head, PR, Jira, or public comment was changed. No test rerun or root `check -x test` was performed; the latter belongs to a later pass-after gate, not this fail-before phase.
