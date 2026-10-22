# Gate job: SOLR-8767 review pass 2 (RTG and copyField targets)

Status: DONE (GATE GREEN at 3b5f2d2357, vm4, 2026-10-22T07:05:22Z)

- Branch: solr-8767-submit. Head: `3b5f2d2357325ba993fe1d2716fafee55741fc1b` (unchanged since pass 1). Prior gated head: `3b5f2d2357` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestRealTimeGet`. Seed: `8767C0FFEE876700`.
- Why: pass 2 of the review round for unit 26, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged from pass 1
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0
- [x] step 3 proof leg (base `RealTimeGetComponent.java` in worktree `proof-8767`, tests at head): rc=1. Failed: `testAtomicUpdateOfDocumentWithCopyFieldTarget`, `testCopyFieldTargetSetDirectlyAfterCommit`, `testCopyFieldTargetSetDirectly`. Tests 7, failed 3.
- [x] step 4 focused head run, from fresh XML: TestRealTimeGet tests=7, skipped=0, failures=0, errors=0 (ts 2026-10-22T07:03:02Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (07:05:22).

Logs: `/workspace/gates/logs/vm4/8767/` (pass2-*.log, xml2/).

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-8767-submit.md`.
- Receipt section in `receipts/SOLR-8767.md`, top section.
