# Skip record: SOLR-17376

- Ticket: SOLR-17376, "Direct buffer memory leak in Solr Java Client". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-07-23, updated 2025-03-24, no fix version, four comments.
- Verdict: keep-open.
- Draft branch: solr-17376-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:38-13:50 UTC.

## Ticket reading

A long-running service using the Solr Java client sees direct ByteBuffers accumulate until the JVM hits OutOfMemoryError after hundreds of thousands of `DirectByteBuffer` instances. The reporter rules out heap and says the leak is in the client's allocation. Comments: a suggestion to set `jdk.nio.maxCachedBufferSize` (magibney, 2024-07-23), a reply that the allocation pattern points to Jetty's `ArrayRetainableByteBufferPool` (veita, 2024-07-23), a redirect to the Jetty project (dsmiley, 2024-07-24), and the reporter's filing of Jetty issue 12084 (2024-07-24).

## Code reading

- `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java:258-285`: builds `ClientConnector`, then `new HttpClient(transport)` for HTTP/1.1 (`HttpClientTransportOverHTTP`) or HTTP/2 (`HttpClientTransportOverHTTP2`). No `ByteBufferPool` is set on the connector or the client, so Jetty's default pool is used.
- `git grep -n -E 'ByteBufferPool|DirectByteBuffer|setUseInputDirect|setUseOutputDirect|ArrayByteBufferPool' 498a09aea97... -- solr/solrj-jetty/src/java solr/solrj/src/java solr/core/src/java/org/apache/solr/client`: no hits. Solr sets no buffer-pool or direct-buffer option.
- `gradle/libs.versions.toml:84`: `eclipse-jetty = "12.1.12"`. The pinned client is Jetty 12.1.12. This record does not check whether Jetty 12.1.12 changes the default pool's retention.

What this shows at the pinned head: the direct buffers come from Jetty's default pool on the Solr-created client. Solr does not add a pool or a limit of its own. The upstream Jetty issue the reporter filed (jetty/jetty.project#12084) was closed as stale with no fix (from the queue note, not re-checked in this pass).

## History

- No commit on the pinned head names SOLR-17376 (evidence tool: no hits).
- The Jetty dependency bump history for `eclipse-jetty` was not traced in this pass.

## Reproduction attempts

Not attempted. A reproduction needs a loop of update requests through `HttpJettySolrClient` on the pinned build, counting direct buffers (for example through `ManagementFactory` buffer pool MXBeans) over time, with the same request mix as the reporter. This pass is review-only and runs no builds.

## Gap analysis

The code path is clear: Solr's client relies on Jetty's default buffer pool. Whether the default pool leaks, or only retains buffers up to its configured limit, is the open question. The ticket's observed growth to hundreds of thousands of instances is not explained by the code read alone, and the earlier suggestion (`jdk.nio.maxCachedBufferSize`) was not tested.

Missing piece for a verdict change: a count of direct buffers over time for the pinned client under a sustained update load, and the Jetty pool's configured maximum. With that, the record can say whether the growth is unbounded (a Jetty defect to report upstream, with a Solr-side workaround to consider) or bounded (a configuration matter).

## Verdict justification

keep-open meets the rubric: the code path is read (`HttpJettySolrClient.java:258-285`), the attempts are listed (none run), and the missing piece is named (the buffer-count run and the pool's limit). A close needs a demonstrated non-issue or a fix on main, and neither is shown. A pr-needed needs a Solr line that causes the growth; the code shows the default pool is used, which is not by itself a defect.

## Limits

This record does not reproduce the leak and does not say whether Jetty 12.1.12 is affected. It does not confirm the upstream closure. It does not post anything to JIRA or GitHub.
