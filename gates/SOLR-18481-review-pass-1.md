# Gate job: SOLR-18481 review pass 1 (Proxied requests should use X-FORWARDED-FOR header)

Status: DONE (GATE GREEN at 8f687ca04d, vm4, 2026-10-23T01:02:44Z)

- Branch: solr-18481-xff. Head: `8f687ca04de6cfb6baa6075cb69fcb03737de918` (ls-remote 2026-10-23T00:31:31Z, from the claim). Prior gated head: `8f687ca04d` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.servlet.HttpSolrProxyForwardedForTest`. Seed: `18481C0FFEE18481`.
- Why: pass 1 of the review round for unit 41.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable, no changelog file on this branch
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (00:48:51)
- [ ] step 3 proof leg: NOT A BEHAVIOUR PROOF. With `HttpSolrProxy.java` at base, the test does not compile: `HttpSolrProxy.forwardedFor` is missing (`HttpSolrProxyForwardedForTest.java:27`). rc=1, inconclusive by construction (the test calls the new helper directly).
- [x] step 4 focused head run, from fresh XML: HttpSolrProxyForwardedForTest tests=5, skipped=0, failures=0, errors=0 (ts 2026-10-23T00:58:58Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (01:02:44).

Logs: `/workspace/gates/logs/vm4/18481/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18481-xff.md`.
- Receipt section in `receipts/SOLR-18481.md`, top section.
