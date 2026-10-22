# Gate job: SOLR-13182 review pass 1 (child query with no clauses, NPE)

Status: DONE (GATE GREEN at 2cf82a0b94, vm4, 2026-10-22T09:05:37Z)

- Branch: solr-13182-child-null-parent. Head: `2cf82a0b946432393d6dc3cc8216e67eb443007d` (ls-remote 2026-10-22T08:30Z, from the claim). Unchanged through the run.
- Prior gated head: none on this unit. This is the first gate in the round (the unit has no receipt).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.join.BJQParserTest`. Seed: `13182C0FFEE13182`.
- Diff scope against `c8ab71430f`: 3 files, 19 insertions (`BlockJoinChildQParser.java` guard, `BJQParserTest.java` test, `changelog/unreleased/SOLR-13182-child-null-parent.yml`).
- Why: pass 1 of the review round for unit 28; no receipt exists, so this gate is the first.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (08:50:56)
- [x] step 3 proof leg: `BlockJoinChildQParser.java` restored to `c8ab71430f` in worktree `proof-13182`, test at head. Expected failure: `testChildQueryWithNoClausesAndNoParentFilterIsBadRequest` with `NullPointerException: Query must not be null`. Result: tests 19, failed 1, the expected test, rc=1.
- [x] step 4 focused head run, from fresh XML: BJQParserTest tests=19, skipped=0, failures=0, errors=0 (ts 2026-10-22T09:01:44Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (09:05:37).

Logs: `/workspace/gates/logs/vm4/13182/` (pass1-*.log, xml/).

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-13182-child-null-parent.md`.
- Receipt in `receipts/SOLR-13182.md` (new file), top section.
