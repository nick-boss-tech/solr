# Receipt: PR review apache/solr #4874 (vm4)

- PR: https://github.com/apache/solr/pull/4874, open, not draft, head branch `SOLR-18417-remove-prh-code-2` on `gerlowskija/solr`, 39 files, +27/-1197 (GitHub files API, two pages), updated 2026-09-08, `mergeable_state` unknown at the snapshot.
- Main removals: `PingRequestHandler.java` (-343), `PingRequestHandlerTest.java` (-233), `SolrPing.java` (-101), `SolrPingResponse.java` (-26), `SolrPingTest.java` (-85), `UsingPingRefGuideExamplesTest.java` (-83), `ping.adoc` (-93), `SolrClient.java` (-31, the ping methods), `CommonParams.java` (+1/-22).
- Base checks at `c8ab71430f`: `SolrClient.ping(String)` and `ping()` carry `@Deprecated(since = "10.1")` (lines 900 and 915). `PingRequestHandler.java` is in base core and its Javadoc shows the `solr.PingRequestHandler` class name used in `solrconfig.xml`.
- Gate: none run.
- Outcome: NEEDS-OWNER. Draft: `drafts/pr-review/4874.md`.
