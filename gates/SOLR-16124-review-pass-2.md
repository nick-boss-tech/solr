# Gate job: SOLR-16124 review pass 2 (Solr Example should prompt for ZK port)

Status: DONE (GATE GREEN at 7b81166ae9, vm4, 2026-10-22T13:17:28Z)

- Branch: solr-16124-zk-port-prompt. Head: `7b81166ae905bc12f3cd7d18a7265ae446d62e38` (unchanged since pass 1). Prior gated head: `7b81166ae9` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.cli.TestSolrCLIRunExample`. Seed: `16124C0FFEE16124`.
- Why: pass 2 of the review round for unit 31, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (13:07:01)
- [x] step 3 proof leg (base `RunExampleTool.java`): rc=1. Failure is `testInteractiveSolrCloudExample` ("test collection not found"), the same shape as pass 1 (see review).
- [x] step 4 focused head run, from fresh XML: TestSolrCLIRunExample tests=6, skipped=0, failures=0, errors=0 (ts 2026-10-22T13:14:33Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (13:17:28).

Logs: `/workspace/gates/logs/vm4/16124/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-16124-zk-port-prompt.md`.
- Receipt section in `receipts/SOLR-16124.md`, top section.
