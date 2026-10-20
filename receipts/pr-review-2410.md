# Receipt: PR review apache/solr #2410 (vm4)

- PR: https://github.com/apache/solr/pull/2410, state open, not draft, mergeable_state unknown at queue snapshot. Head `db8390838d` on `HoustonPutman/solr` branch `http2-maxConnections`. Base `abf6e8f43c` (commons-cli bump, #2408).
- Fetched the head from the author's fork (`git fetch https://github.com/HoustonPutman/solr.git http2-maxConnections`). Two commits: `9db532e1d2` (SOLR-17240 fix) and `db8390838d` (divide by multiplexing factor, docs).
- Diff vs base: 3 files, +19/-10 under `solr/` (CHANGES.txt, Http2SolrClient.java, HttpSolrClientBuilderBase.java). No test file changed.
- Compatibility: `git merge-tree --write-tree --merge-base abf6e8f43c c8ab71430f db8390838d` returned conflicts (rc=1). `git ls-tree -r c8ab71430f` shows no `Http2SolrClient.java` or `HttpSolrClientBuilderBase.java` under `solr/`; the client is `solr/solrj-jetty/.../HttpJettySolrClient.java`, which hardcodes `setMaxConnectionsPerDestination(4)` at line 289.
- Jetty API: `javap` on the `jetty-http2-client` jar from `/workspace/gradle-home` lists `getMaxConcurrentPushedStreams` and `getMaxLocalStreams` on `org.eclipse.jetty.http2.client.HTTP2Client`. The PR uses the pushed-stream one.
- Gate: none run. The head cannot be compiled against current main without the rebase.
- Outcome: STALE-BUMP. Draft: `drafts/pr-review/2410.md`.
