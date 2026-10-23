# Gate job: SOLR-18503 review pass 1 (JSON Facet API: seven parser-accepted keys are missing from the reference guide)

Status: DONE (GATE GREEN at cc5b84dfcd, vm4, 2026-10-23T02:26:01Z)

- Branch: solr-18503-submit. Head: `cc5b84dfcdd783ec2c2626aba4ba9931bb027893` (ls-remote 2026-10-23T01:53:31Z, from the claim). Prior gated head: `cc5b84dfcd` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.facet.TestJsonRangeFacets`. Seed: `18503C0FFEE18503`.
- Why: pass 1 of the review round for unit 42.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-18503-json-facet-parser-options.yml`)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (02:10:21)
- [x] step 3 proof leg (base `FacetRangeParser.java` in worktree `proof-18503`, test at head): rc=1. Expected failures `testRangeFacetWithRanges` and `testRangeFacetWithRangesDistrib`. Tests 10, failed 2.
- [x] step 4 focused head run, from fresh XML: TestJsonRangeFacets tests=10, skipped=0, failures=0, errors=0 (ts 2026-10-23T02:22:03Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (02:26:01).

Logs: `/workspace/gates/logs/vm4/18503/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18503-submit.md`.
- Receipt section in `receipts/SOLR-18503.md`, top section.
