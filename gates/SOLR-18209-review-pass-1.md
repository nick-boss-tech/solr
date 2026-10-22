# Gate job: SOLR-18209 review pass 1 (Replace Arrays.asList with List.of for immutable static final fields)

Status: DONE (GATE GREEN at c8fef7c4cd, vm4, 2026-10-22T21:05:39Z)

- Branch: solr-18209-list-of. Head: `c8fef7c4cd970fb84cae835a7a2f7a497b7dd4fc` (ls-remote 2026-10-22T20:43:27Z, from the claim). Prior gated head: `c8fef7c4cd` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.admin.api.NodeHealthSolrCloudTest, org.apache.solr.handler.BinaryUpdateRequestHandlerTest`. Seed: `18209C0FFEE18209`.
- Why: pass 1 of the review round for unit 38.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable, no changelog file on this branch (refactor)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (20:57:13)
- [ ] step 3 proof leg: not run. Refactor (Arrays.asList to List.of); no changed behaviour that a test fails on base.
- [x] step 4 focused head run, from fresh XML: NodeHealthSolrCloudTest tests=2, failures=0, errors=0, skipped=0; BinaryUpdateRequestHandlerTest tests=1, failures=0, errors=0, skipped=0 (ts 2026-10-22T21:01:57Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (21:05:39).

Logs: `/workspace/gates/logs/vm4/18209/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18209-list-of.md`.
- Receipt section in `receipts/SOLR-18209.md`, top section.
