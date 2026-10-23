# Gate job: SOLR-2534 review pass 1 (Add min() and max() on strings)

Status: DONE (GATE GREEN at a75b012a20, vm4, 2026-10-23T05:01:30Z)

- Branch: solr-2534-string-min-max. Head: `a75b012a20630acc706eb0427a1d31638b2e0c22` (ls-remote a75b012a20 at claim time, from the claim). Prior gated head: `a75b012a20` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.function.TestStringMinMaxFunction`. Seed: `2534C0FFEE2534`.
- Why: pass 1 of the review round for unit 43.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0 (04:32:34). `git status` clean.
- [x] step 2 Error Prone compile, rc=0 (04:45:14). The first attempt was interrupted at 04:34 with no result and was rerun from 04:37:47Z by run-pass1-resume.sh.
- [x] step 3 proof leg (base `ValueSourceParser.java`): rc=1. Three of the five tests fail on base (`testMaxOfStringFieldsIsLexicographic`, `testMinOfStringFieldsIsLexicographic`, `testMissingValuesAreIgnored`), so the focused tests discriminate. The other two do not depend on the parser change (04:51:57).
- [x] step 4 focused head run, TestStringMinMaxFunction tests=5, skipped=0, failures=0, errors=0 (ts 2026-10-23T04:57:03Z). rc=0 (04:57:21).
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (05:01:04).

Logs: `/workspace/gates/logs/vm4/43/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-2534-string-min-max.md`.
- Receipt section in `receipts/SOLR-2534.md`, top section.
