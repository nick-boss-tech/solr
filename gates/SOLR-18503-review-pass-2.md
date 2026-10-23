# Gate job: SOLR-18503 review pass 2 (JSON Facet API: seven parser-accepted keys are missing from the reference guide)

Status: DONE (GATE GREEN at cc5b84dfcd, vm4, 2026-10-23T02:44:15Z)

- Branch: solr-18503-submit. Head: `cc5b84dfcdd783ec2c2626aba4ba9931bb027893` (unchanged since pass 1). Prior gated head: `cc5b84dfcd` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.facet.TestJsonRangeFacets`. Seed: `18503C0FFEE18503`.
- Why: pass 2 of the review round for unit 42, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (02:35:09)
- [x] step 3 proof leg (base `FacetRangeParser.java`): rc=1. The same two range-facet tests fail, as in pass 1.
- [x] step 4 focused head run, from fresh XML: TestJsonRangeFacets tests=10, skipped=0, failures=0, errors=0 (ts 2026-10-23T02:42:04Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (02:44:15).

Logs: `/workspace/gates/logs/vm4/18503/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18503-submit.md`.
- Receipt section in `receipts/SOLR-18503.md`, top section.
