# JIRA newdev small round 1

Assignment: `assignments/pool-jira-newdev-small-round-1.md`. Base: apache/main c8ab71430f.

## Item 4: SOLR-8755 (vm4), 2026-10-19T18:51:27Z

- Verified: JIRA status Open, no resolution, on 2026-10-19 (anonymous REST read). Scope is clear: tests for `org.apache.solr.parser.TokenMgrError`.
- Status: fixed branch. Branch `solr-8755-tokenmgrerror-tests` on the fork: test commit 31bdbd2488 (test only), draft commit e338b6e26e (`drafts/SOLR-8755/pr-description.md`).
- Gate: `:solr:core:test --tests org.apache.solr.parser.TokenMgrErrorTest` and `:solr:core:spotlessJavaCheck` at 31bdbd2488, 2026-10-19: 5 of 5 tests pass, 0 failures, 0 skipped. Log `/workspace/gates/logs/vm4/solr-8755-test-3.log`, XML `/workspace/gates/logs/vm4/solr-8755-TEST-TokenMgrErrorTest.xml`.
- No fail-before run: test-only change, no source change.
- Observation, not changed: the lexical error message quotes the numeric code point (`'120' (120)`), not the character. Recorded in the draft as a maintainer question.

