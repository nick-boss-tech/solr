# Gate job: SOLR-18481 review pass 2 (Proxied requests should use X-FORWARDED-FOR header)

Status: DONE (GATE GREEN at 8f687ca04d, vm4, 2026-10-23T01:20:58Z)

- Branch: solr-18481-xff. Head: `8f687ca04de6cfb6baa6075cb69fcb03737de918` (unchanged since pass 1). Prior gated head: `8f687ca04d` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.servlet.HttpSolrProxyForwardedForTest`. Seed: `18481C0FFEE18481`.
- Why: pass 2 of the review round for unit 41, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [ ] step 0 changelog: not applicable, no changelog file on this branch
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (01:10:54)
- [ ] step 3 proof leg: not a behaviour proof. Base `HttpSolrProxy.java` does not compile the test (`forwardedFor` missing). rc=1, inconclusive by construction, same as pass 1.
- [x] step 4 focused head run, from fresh XML: HttpSolrProxyForwardedForTest tests=5, skipped=0, failures=0, errors=0 (ts 2026-10-23T01:19:00.197Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (01:20:58).

Logs: `/workspace/gates/logs/vm4/18481/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18481-xff.md`.
- Receipt section in `receipts/SOLR-18481.md`, top section.
