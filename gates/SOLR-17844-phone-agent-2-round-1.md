# Gate job: SOLR-17844 (restore field-cache sort assertions)

Status: CLAIMED (claims/phone-agent-2-blockers-round-1.md)

- Branch: solr-17844-restore-sort-assertions-phone-agent-2. Head: `0a7ffa07a40c123bd541df3068548247f40055d7`, pushed to fork 2026-10-22.
- Prior gated head: none. Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Claiming host: phone-agent-2. Capability tags: gate, implementation, review. Covered by the directed round claim.
- Module: `:solr:core`. Focused class: `org.apache.solr.uninverting.TestFieldCacheSortRandom`. Seed: `1BB02D3762B56611`.
- Diff scope: only `solr/core/src/test/org/apache/solr/uninverting/TestFieldCacheSortRandom.java`.
- Why: existing current test skips all expected order checks; restore meaningful count, doc order and value checks using Lucene comparator semantics.

Links: Ticket: SOLR-17844. Category: Test quality; secondary Search and query components. Siblings: SOLR-17631 upgrade and SOLR-17840, no overlapping source changes. Supersedes: no earlier implementation or receipt.

## What to run

Runner: `/workspace/tools/phone-agent-2-hardening-gate.py`, configuration `/workspace/phone-agent-2-logs/gate-17844.json`. Logs: `/workspace/phone-agent-2-logs/gate-17844-attempt-1`. Shared lock `/workspace/tools/.gradle-$(hostname -s).lock`; disk guard before each build.

0. Changelog is test-only exempt under `.github/workflows/validate-changelog.yml:63-75`; runner verifies the entire diff is test sources. No fragment to parse.
1. Root `tidy`; record and commit any repair before a retry.
2. `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Mutation proof: actual `SortField` direction becomes `!reverse`, expected comparator remains `reverse`. Expected fresh XML failure contains `Unexpected document at position`. An absent/different failure is not accepted.
4. Restore committed head, clean test outputs, run the full focused class, archive fresh XML and count tests/skips/failures/errors.
5. `:solr:core:check -x test`.

## Decisions

| Decision | Answer | Failure route |
| --- | --- | --- |
| Proof shape | Mutation, not a base where the new assertions do not exist | Accept only executed assertion failures with the expected marker |
| Scope | Test-only; no production sort rewrite | Out-of-scope defect returns to implementation |
| Repair | WORKFLOW gate repairs, one tree-changing repair and one full retry | Failed retry is HANDED BACK |

## Checklist

- [ ] fork head read back
- [ ] step 0 exemption verified
- [ ] step 1 tidy
- [ ] step 2 Error Prone compile
- [ ] step 3 discriminating mutation
- [ ] step 4 focused XML counts
- [ ] step 5 module check
- [ ] receipt written
- [ ] terminal gate status and round claim updated

No external posts, PR opening, ready flips or force pushes. Commits as Nick Shanin without trailers.
