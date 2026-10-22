# Gate job: SOLR-16220 review pass 3 (Documentation leads to NPE Missing SslContextFactory for SolrJ client)

Status: DONE (GATE GREEN at d7db0784a1, vm4, 2026-10-22T14:43:29Z)

- Branch: solr-16220-ssl-config-mismatch. Head: `d7db0784a19500aa5be380ccd8e88600084c2b8c` (unchanged since pass 1). Prior gated head: `d7db0784a1` (pass 2, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:solrj-jetty`. Focused class: `org.apache.solr.client.solrj.jetty.HttpJettySolrClientSSLConfigTest`. Seed: `16220C0FFEE16220`.
- Why: pass 3 of the review round for unit 32, regate on the head pass 2 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (14:37:14)
- [x] step 3 proof leg (base `HttpJettySolrClient.java`): rc=1. Expected failure `testNonSslConfigWithHttpsUrlFailsWithClearMessage`, same shape as passes 1 and 2.
- [x] step 4 focused head run, from fresh XML: HttpJettySolrClientSSLConfigTest tests=2, skipped=0, failures=0, errors=0 (ts 2026-10-22T14:42:01Z). rc=0.
- [x] step 5 module check `:solr:solrj-jetty:check -x test`, rc=0 (14:43:29).

Logs: `/workspace/gates/logs/vm4/16220/`.

## Deliverable

- Pass 3 result in `reports/newdev-critblock-review-round-1/solr-16220-ssl-config-mismatch.md`.
- Receipt section in `receipts/SOLR-16220.md`, top section.
