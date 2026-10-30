# Repair report: SOLR-17241 wt gate, round 2 (vm5)

Add-only. Gate: `gates/SOLR-17241-wt-format-round-2.md`. Failing candidate: `solr-17241-submit` at `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e` (parent `9859d8bb2e8d63061116b8281639a4beb1be480c`). Host vm5, JDK 21.0.12.1+1, seed `17241C0FFEE17241`.

## Result

The gate does not pass at `7a5a998ee9`. The candidate leg `org.apache.solr.handler.V2ApiIntegrationTest` fails, which means the regression is not fixed on the non-hosting path.

| Leg | Command | Status | Evidence |
|---|---|---|---|
| tidy after repair | `./gradlew tidy` | exit 0, tree clean | `tidy-after-repair.log` |
| changelog parse | YAML load of `changelog/unreleased/SOLR-17241-wt-non-hosting-node.yml` | exit 0 | `changelog-parse.log` |
| compile with Error Prone | `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | exit 0, 0 errors | `compile-errorprone.log` |
| candidate filter | `:solr:core:test --tests ...MediaTypeOverridingFilterTest -Ptests.seed=17241C0FFEE17241` | exit 0; tests=8 failures=0 errors=0 skipped=0 | `xml/candidate-filter/TEST-...MediaTypeOverridingFilterTest.xml` |
| candidate integration | `:solr:core:test --tests ...V2ApiIntegrationTest -Ptests.seed=17241C0FFEE17241` | **exit 1**; 14 tests, 1 failed | `candidate-v2api.log`, `xml/candidate-v2api/TEST-...V2ApiIntegrationTest.xml` |

The integration run was interrupted once by a session loss before its result was recorded; the runner restarted that leg from scratch. The attempt-1 logs are in `solr17241-wt-format-round-2-attempt1-crashed/`.

## Failing leg and exact error

Test: `V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode` (line 213, the `request.process(client)` call in the loop).

The loop runs the hosting node first, then the non-hosting node. Mapping from the log:

- Replica `reload_one_replica_shard1_replica_n1` is created on `127.0.0.1:38027_solr` (`jetty-launcher-8-thread-2`). That is the hosting node. Its reload passes.
- `127.0.0.1:35747_solr` (`jetty-launcher-8-thread-1`) is the non-hosting node. Its reload fails.

Exact error from the fresh XML (`RemoteSolrException`, `Error from server at http://127.0.0.1:35747/api/collections/reload_one_replica/reload?wt=javabin`):

```
Expected mime type in [application/vnd.apache.solr.javabin, application/octet-stream] but got application/json.
{"responseHeader":{"status":500,"QTime":0},"error":{"code":500,"errorClass":"java.lang.NullPointerException",
"msg":"Cannot invoke \"org.apache.solr.request.SolrQueryRequest.getParams()\" because \"req\" is null",
"trace":{"stackTrace":["org.apache.solr.response.JavaBinResponseWriter.write(JavaBin..."
```

Server-side stack (XML lines 393-394): `java.lang.NullPointerException: Cannot invoke "org.apache.solr.request.SolrQueryRequest.getParams()" because "req" is null` at `org.apache.solr.response.JavaBinResponseWriter.write(JavaBinResponseWriter.java:56)`.

## Cause (rung 1, read from code)

`solr/core/src/java/org/apache/solr/response/JavaBinResponseWriter.java:56` dereferences `req.getParams()` and `Resolver(req, ...)` also takes the request. On the non-hosting path `req` is null, so the javabin body cannot be written. The Jersey error path then answers with a 500 JSON body, and `MediaTypeOverridingFilter` leaves error responses alone (`status >= 400` return), so the client receives `application/json` and SolrJ reports the MIME mismatch.

The candidate's change in `MediaTypeOverridingFilter` only moves the content-type decision to `SOLR_PARAMS` when `SOLR_QUERY_REQUEST` is absent. That fixes the header decision for successful responses, but the successful javabin body still needs a `SolrQueryRequest` that the non-hosting path does not supply. So the candidate does not yet produce a javabin response on that path.

This is a source failure in the candidate, not a test or environment failure. The test is not weakened, and the candidate is not changed in this gate.

## Controls (original-production, parent `9859d8bb2e` filter in disposable tree)

Control tree `/workspace/worktrees/pool-vm5-17241-control` at `7a5a998ee9` with only `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java` replaced by the parent blob. Diff: `control-diff.patch`, 2 insertions, 7 deletions. Filter file sha256 `7a3e48192f160fc2f0e27a330e7c891c6b6f2504a6404e9328a14b06fbfe102c` equals the parent's `git show 9859d8bb2e:...` hash. Nothing committed or pushed.

| Control | Status | Evidence |
|---|---|---|
| `MediaTypeOverridingFilterTest.testWtFromQueryParamsWithoutSolrQueryRequest` | exit 1, failures=1, errors=0; **assertion failure** `json expected:<application/json> but was:<text/plain>` at `MediaTypeOverridingFilterTest.java:101` | `control-filter-no-sqr.log`, `xml/control-filter-no-sqr/` |
| `V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode` | exit 1; failure at `V2ApiIntegrationTest.java:213`: `Expected mime type in [application/octet-stream, application/vnd.apache.solr.javabin] but got application/json` from `127.0.0.1:40745` (non-hosting). The hosting node `45287` (replica `reload_one_replica_shard1_replica_n1`) completed its request first. | `control-v2-reload.log`, `xml/control-v2-reload/` |

Both controls fail for the regression reason, so the test is sensitive to the bug.

## Candidate integration: the same line, different error

On the candidate the non-hosting request fails at the same line (213) with a different error. The MIME type is now `application/json` from the server's error body, and the server error is `NullPointerException` in `JavaBinResponseWriter.write` (line 56, `req` null). So the filter fix moves the header, but the response body still cannot be produced on that path.

## Remaining legs

`:solr:core:check -x test` was started and then stopped by vm5 after the candidate failure (`core-check.log` is incomplete, marked stopped). Root `check -x test` was not run. Both are not claimed.

## What is needed

Implementation repair, not a gate action. The candidate's non-hosting reload response must be written by a javabin writer that does not need a `SolrQueryRequest` (or the non-hosting path must supply one). Options belong to the implementation owner and need a new candidate head and a new superseding gate. Per gate rules, the next gate is pinned to that new head. Round 2 does not claim PASS.

`:solr:core:check -x test` and root `check -x test` were not run on this head; the gate records them as not run.
