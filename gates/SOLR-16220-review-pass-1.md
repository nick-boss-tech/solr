# Gate job: SOLR-16220 review pass 1 (Documentation leads to NPE Missing SslContextFactory for SolrJ client)

Status: DONE (GATE GREEN at d7db0784a1, vm4, 2026-10-22T14:18:05Z)

- Branch: solr-16220-ssl-config-mismatch. Head: `d7db0784a19500aa5be380ccd8e88600084c2b8c` (ls-remote 2026-10-22T13:49:46Z, from the claim). Prior gated head: `d7db0784a1` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:solrj-jetty`. Focused class: `org.apache.solr.client.solrj.jetty.HttpJettySolrClientSSLConfigTest`. Seed: `16220C0FFEE16220`.
- Why: pass 1 of the review round for unit 32.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-16220-ssl-config-mismatch-error.yml`)
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile `:solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true`, rc=0 (14:08:25)
- [x] step 3 proof leg (base `HttpJettySolrClient.java` in worktree `proof-16220`, test at head): rc=1. Expected failure `testNonSslConfigWithHttpsUrlFailsWithClearMessage` ("Expected exception IllegalArgumentException but no exception was thrown"). The run also reports two `classMethod` ObjectTracker failures: the base constructor creates an `HttpJettySolrClient` that the failing test never closes. Tests 4, failed 3.
- [x] step 4 focused head run, from fresh XML: HttpJettySolrClientSSLConfigTest tests=2, skipped=0, failures=0, errors=0 (ts 2026-10-22T14:16:36Z). rc=0.
- [x] step 5 module check `:solr:solrj-jetty:check -x test`, rc=0 (14:18:05).

Logs: `/workspace/gates/logs/vm4/16220/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-16220-ssl-config-mismatch.md`.
- Receipt section in `receipts/SOLR-16220.md`, top section.
