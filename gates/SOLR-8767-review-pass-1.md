# Gate job: SOLR-8767 review pass 1 (RTG and copyField targets)

Status: DONE (GATE GREEN at 3b5f2d2357, vm4, 2026-10-22T06:42:05Z)

- Branch: solr-8767-submit. Head: `3b5f2d2357325ba993fe1d2716fafee55741fc1b` (ls-remote 2026-10-22T06:00Z, from the claim). Unchanged through the run.
- Prior gated head: none on the branch's own commits; the receipt's gate (2026-10-03) ran the earlier code at the same head.
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestRealTimeGet`. Seed: `8767C0FFEE876700`.
- Diff scope against `14c7aac0d1` (the branch's first own commit's parent): 3 files, 55 insertions, 23 deletions (`RealTimeGetComponent.java`, `TestRealTimeGet.java`, `changelog/unreleased/SOLR-8767-rtg-copy-field-targets.yml`).
- Why: pass 1 of the review round for unit 26.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0
- [x] step 3 proof leg: `RealTimeGetComponent.java` restored to `14c7aac0d1` in worktree `proof-8767`, tests at head. Expected failures: `testCopyFieldTargetSetDirectly`, `testCopyFieldTargetSetDirectlyAfterCommit`, `testAtomicUpdateOfDocumentWithCopyFieldTarget`. Result: tests 7, failed 3, all three named (rc=1).
- [x] step 4 focused head run, counted from fresh XML: TestRealTimeGet tests=7, skipped=0, failures=0, errors=0 (ts 2026-10-22T06:37:55Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0.

Logs: `/workspace/gates/logs/vm4/8767/` (pass1-*.log, xml/).

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-8767-submit.md`.
- Receipt section in `receipts/SOLR-8767.md`, top section.
