# Assignment: SolrJ V2Request default response parser against the server default, round 1

Status: CLOSED (2026-10-29, owner directed, after vm6 probe). Not reproduced; no action. Receipt: `receipts/solr-v2request-default-parser-wire-vm6.md`. Reopen only if the round 3 default-split failure reappears on a later head.

- Tags: investigation, solrj, records.
- Intended host: any capable Linux host with JDK 21 and the Solr build. One build or probe job at a time under the host lock.
- Owner request: the issue vm6 raised while gating SOLR-16398 (SolrJ default V2 response parser). Establish the exact mechanism, decide whether it needs a ticket, and report. No code change, no JIRA or GitHub post, no PR.

## Scope note: this is not a binary-compatibility issue

It is a behavior mismatch between SolrJ's default V2 response handling and the server's default content type. It does not involve removed classes or changed method descriptors, so it is not SOLR-18600 or the SOLR-18325 inherited builder break. If the owner meant a different SolrJ defect, stop and report that instead; do not reuse this assignment for it.

## What vm6 observed (evidence checked)

Test run results, collection-branch gate (`solr16398-r3-green`, log directory `/workspace/gates/logs/vm6/solr16398-r3-green/`):
- With no response parser set on the request, the split-shard and move-replica calls failed on the client with `Expected mime type in [application/vnd.apache.solr.javabin, application/octet-stream] but got application/json`. The response body was a successful JSON payload (`status 0`, `success` present). Per-test XML from that run was overwritten by later runs; the messages quoted here come from that run's JUnit XML before overwrite.
- The same endpoints with `request.setResponseParser(new JavaBinResponseParser())` returned status 0 with javabin (`solr16398-r5-probe3`, log directory `/workspace/gates/logs/vm6/solr16398-r5-probe3/`).
- The same explicit parser on GET `/collections` (`solr16398-r5-probe`) and on POST `/aliases`, an existing JAX-RS POST (`solr16398-r5-probe2`), also returned javabin and passed.

Code read (branch `solr-16398-core-commands` in `/workspace/tmp/inspect-core`, equal to fork commit `cf2bbb0558` for these paths; SolrJ unchanged on both vm6 branches):
- `solr/solrj/src/java/org/apache/solr/client/solrj/request/V2Request.java:52-59`, `:89-99`: content type from `useBinary`; the builder default is `useBinary = false` (`:147`).
- `V2Request.java:137-140`: `getResponseParser()` returns the request parser, or the superclass value (null if unset).
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:97`: the client's default parser is `new JavaBinResponseParser()`.
- `HttpSolrClient.java:144-146`: `responseParser(request)` falls back to the client's parser.
- `HttpSolrClient.java:153-163`: `wt` is set from the chosen parser's writer type.
- `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java:585-610`, `:747`: the Jetty client, which the test cluster client used, calls `initializeSolrParams(request, responseParser(request))`, so the same `wt` rule applies.
- Server side: `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java:70-79`: with `wt` present the response Content-Type is set from `wt`; with no `wt` and no `Accept`, JSON is the default.
- Existing tests: `solr/core/src/test/org/apache/solr/handler/V2ApiIntegrationTest.java`, `testRespondsWithJsonWhenWtAndAcceptAreMissing` (JSON default on a JAX-RS endpoint) and `testObeysWtParameterWhenProvided` (`wt=xml` honored).

## vm6's earlier explanation is not verified

vm6 recorded (`claims/SOLR-16398-command-jaxrs-fix-r3-vm6.md`, decision 2) that the default parser "does not send `wt=javabin`". The code above says the client does set `wt` from its default javabin parser, so that explanation is unsupported. The request that failed and the request that passed differ in some way the code reading did not explain. Do not carry the old explanation forward.

## Questions to settle

1. What exact request reaches the server for a default `V2Request` through the test cluster client (`cluster.getSolrClient()`), compared with the explicit-parser request? Capture the URL query (`wt`), the `Accept` header and the response Content-Type for each. A wire capture or a logged request is enough.
2. Why does the default path fail when the code says `wt=javabin` is sent? Candidates to check: the parser the test cluster client actually holds (`cluster.getSolrClient()` setup in `SolrCloudTestCase`), a different client class on the default path, or the `MediaTypeOverridingFilter` branch taken.
3. Does the same default-request mismatch affect other JAX-RS endpoints on `main`, for example `GET /collections` with no parser? Use an existing test or one probe; do not sweep the whole API.
4. Is any of this binary? Expected answer: no. If the evidence says otherwise, stop and report.

## Deliverables

- `receipts/solrj-v2request-default-parser-round-1.md`: the captured requests for each leg, the mechanism, and the answers to questions 1 to 4.
- `reports/solrj-v2request-default-parser-round-1.md`: only if the mechanism is non-obvious.
- A recommendation on whether a JIRA ticket is warranted. Draft it under `jira/` if so. Do not file or post it.

## Constraints

- No code change to SolrJ or the server. Read and probe only, on a scratch clone.
- No JIRA, GitHub, PR or board write other than this assignment's records.
- Commit identity Nick Shanin, no trailers, for any branch commit.
