# Gate job: SOLR-16124 review pass 1 (Solr Example should prompt for ZK port)

Status: DONE (GATE GREEN at 7b81166ae9, vm4, 2026-10-22T12:57:02Z)

- Branch: solr-16124-zk-port-prompt. Head: `7b81166ae905bc12f3cd7d18a7265ae446d62e38` (ls-remote 2026-10-22T12:21:10Z, from the claim). Prior gated head: `7b81166ae9` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.cli.TestSolrCLIRunExample`. Seed: `16124C0FFEE16124`.
- Why: pass 1 of the review round for unit 31.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-16124-cloud-example-zk-port-prompt.yml`)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (12:41:01)
- [x] step 3 proof leg (base `RunExampleTool.java` in worktree `proof-16124`, test at head): rc=1. The failure is `testInteractiveSolrCloudExample`: "test collection 'testCloudExamplePrompt' not found". This is not the expected `zoo.cfg` assertion. The test input has an extra port line, so on base the ZooKeeper port is read as the collection name. The proof does not isolate the new check (see review). Tests 6, failed 1.
- [x] step 4 focused head run, from fresh XML: TestSolrCLIRunExample tests=6, skipped=0, failures=0, errors=0 (ts 2026-10-22T12:52:25Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (12:57:02).

Logs: `/workspace/gates/logs/vm4/16124/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-16124-zk-port-prompt.md`.
- Receipt section in `receipts/SOLR-16124.md`, top section.
