# SOLR-8755: TokenMgrError test review

Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 45. Claimant: phone-agent-2. Branch: `solr-8755-tokenmgrerror-tests`. Head: `e338b6e26eda711e564b242a996f8e0dc8eee451`. Merge-base: `c8ab71430f6e7547c773da596101d680862310ff`.

Ticket source: https://issues.apache.org/jira/rest/api/2/issue/SOLR-8755, read 2026-10-23. Description: add test cases for `org.apache.solr.parser.TokenMgrError`; issue type Test, status Open. No production behavior change is requested.

## Pass 1

Reviewed the full two-file diff against the merge-base, unchanged production `TokenMgrError.java`, neighboring `SolrQueryParserBaseTest`, and repository changelog policy. An independent read-only review agrees that all five assertions match current implementation. Direct test coverage was absent on base, so the addition is justified.

Findings:

- Minor: `solr/core/src/test/org/apache/solr/parser/TokenMgrErrorTest.java:19-22` extends `SolrTestCaseJ4`, contrary to the pool's newer-test convention in `pool/guides/traps-build-and-test.md:19`. The adjacent parser test uses the same J4 base, and no functional problem is identified. Not a compile/lint/test failure and not repaired under this bounded review scope.
- Minor, non-blocking coverage gap: `solr/core/src/test/org/apache/solr/parser/TokenMgrErrorTest.java:48-52` covers tab, quote, backslash and one Unicode escape, but not backspace, newline, form feed, carriage return, single quote, non-ASCII, or escaped prefixes. `:40-46` does not cover null prefix, and the no-argument constructor is untested. The ticket does not require exhaustive branch coverage. No expansion in this round.
- Nit: `drafts/SOLR-8755/pr-description.md:23` cites an earlier historical test head and is not fresh evidence for this head. This review's receipt and archived XML provide current evidence; no public PR body was edited.

No blocker or major correctness finding. The numeric `'120' (120)` expectation at test lines 34-36 is intentional: production `LexicalErr` stringifies an int. Changing wording would be unrelated production work. Changelog absent but exempt under `.github/workflows/validate-changelog.yml:63-75` because only a test and Markdown draft change.

Production source is identical on base, so these tests should pass there too; a fail-before bug-fix claim would be false. Gate proof temporarily mutates constructor reason retention and requires exactly one assertion failure while leaving tests intact, then restores production source. No mutation is committed.

Repairs: none. Regate: `gates/SOLR-8755-review-pass-1.md`, GREEN, 5/5 head tests with zero failures/errors/skips. Logs: `/workspace/phone-agent-2-logs/solr-8755-review/pass-1/`. Runner: `/workspace/tools/phone-agent-2-8755-gate.py`.

Gate harness note: the first invocation stopped after the expected mutation failure because the count checker included timestamp metadata in an equality comparison. The numeric counts and exact assertion content were correct. Fixed the evidence-only checker and resumed at step 4 from preserved evidence; no production-tree change, no test failure repair, and no discarded attempt.

## Pass 2

Started only after pass 1 GREEN. Reconfirmed remote head unchanged, reread the full test diff and production constructor/escaping/message implementation, and ran `git diff --check`. No blocker or major finding. Pass 1 minor J4-base convention and coverage gaps remain unchanged; no repairs. The test is additional direct coverage, not a production fix; missing changelog remains repository-policy exempt. The numeric code-point message remains intentional.

Regate: `gates/SOLR-8755-review-pass-2.md`, GREEN, 5/5 head tests with zero failures/errors/skips at the same head. Seed `8755C0FFEE000002`. Logs `/workspace/phone-agent-2-logs/solr-8755-review/pass-2/`. All six steps run afresh, including forced compilation, mutation and fresh head XML.

## Pass 3

Started after pass 2 GREEN. Reconfirmed live fork head unchanged, reread the exact numbered test and two-file diff, checked whitespace, and checked that no generated production file or unrelated source is changed. No new findings. Pass 1 minor J4 convention and optional coverage gaps remain disclosed, with no correctness blocker. Ticket still warrants direct tests, not changes to generated message wording. Changelog remains test/Markdown-only exempt.

Repairs: none. Regate: `gates/SOLR-8755-review-pass-3.md`, GREEN, 5/5 head tests with zero failures/errors/skips at unchanged head. Seed `8755C0FFEE000003`. Logs `/workspace/phone-agent-2-logs/solr-8755-review/pass-3/`. The full sequence is run afresh, not reused from a prior pass.

## Final outcome

All three passes GREEN on unchanged head `e338b6e26eda711e564b242a996f8e0dc8eee451`. Each pass ran tidy, forced Error Prone compilation, a five-test mutation run with exactly the expected one failure, a fresh five-test clean-head run with zero failures/errors/skips, and the core module check excluding tests. Three distinct seeds. No production repairs, commits, pushes, public text edits, PRs, comments, Jira writes, or email. The only interruption was the disclosed pass-1 evidence-checker continuation.

This review unit is DONE; that does not change the underlying ticket or imply a production bug fix. The overall 45-unit assignment remains OPEN because the other units' current terminal states were not audited here. No product changes or public actions were made.
