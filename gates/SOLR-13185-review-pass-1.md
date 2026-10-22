# Gate job: SOLR-13185 review pass 1 (NPE in query parsing because of missing null check)

Status: DONE (GATE GREEN at 84523828bb, vm4, 2026-10-22T11:39:26Z)

- Branch: solr-13185-query-parse-null. Head: `84523828bba8eb780472d037af3ff60a919d34eb` (ls-remote 2026-10-22T11:04:18Z, from the claim). Prior gated head: `84523828bb` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestComplexPhraseQParserPlugin`. Seed: `13185C0FFEE13185`.
- Why: pass 1 of the review round for unit 30.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable, no changelog file on this branch
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0
- [x] step 3 proof leg (base `ComplexPhraseQParserPlugin.java` in worktree `proof-13185`, test at head): rc=1. Expected failure `testMissingQueryStringIsNotAnError` with NullPointerException ("s" is null). Tests 7, failed 1.
- [x] step 4 focused head run, from fresh XML: TestComplexPhraseQParserPlugin tests=7, skipped=0, failures=0, errors=0 (ts 2026-10-22T11:35:29Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (11:39:26).

Logs: `/workspace/gates/logs/vm4/13185/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-13185-query-parse-null.md`.
- Receipt section in `receipts/SOLR-13185.md`, top section.
