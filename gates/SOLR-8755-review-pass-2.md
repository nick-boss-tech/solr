# Gate job: SOLR-8755 review pass 2 (TokenMgrError tests)

Status: DONE (GREEN, 2026-10-23T06:44:38.769965+00:00)

- Branch: `solr-8755-tokenmgrerror-tests`. Head: `e338b6e26eda711e564b242a996f8e0dc8eee451`, confirmed by ls-remote 2026-10-23T06:25Z.
- Prior gated head: `e338b6e26eda711e564b242a996f8e0dc8eee451` (pass 1 GREEN). Merge-base with upstream/main: `c8ab71430f6e7547c773da596101d680862310ff`.
- Intended host: phone-agent-2. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.parser.TokenMgrErrorTest`. Seed: `8755C0FFEE000002`.
- Diff scope: `solr/core/src/test/org/apache/solr/parser/TokenMgrErrorTest.java` (+61) and `drafts/SOLR-8755/pr-description.md` (+27). No production changes.
- Why: second required review regate after pass 1 GREEN.

Links: Ticket: SOLR-8755. Category: Test quality. Siblings: none known. Supersedes: none.

## What to run

Runner: `/workspace/tools/phone-agent-2-8755-gate.py`, a test-only adaptation of the host standard `phone-agent-2-hardening-gate-v2.py`. Configuration: `/workspace/tools/phone-agent-2-8755-pass-2.json`. Worktree: `/workspace/solr-8755-review`. Logs: `/workspace/phone-agent-2-logs/solr-8755-review/pass-2/`. One host lock, disk/inode guards, archived fresh XML, and restoration in finally are retained.

0. Verify changelog exemption against the exact diff: test plus Markdown only, per `.github/workflows/validate-changelog.yml`. No YAML fragment exists or is required for this test-only change.
1. `tidy`; clean diff required, otherwise scope-limited repair and one retry.
2. `:solr:core:compileJava :solr:core:compileTestJava --rerun-tasks -Pvalidation.errorprone=true`.
3. Temporary mutation: replace `errorCode = reason;` with `errorCode = LEXICAL_ERROR;` in generated `TokenMgrError.java`. Keep all head test assertions intact. Run the five tests at the recorded seed. Require exactly one failure, in `testMessageConstructorKeepsMessageAndReason`, containing `expected:<3> but was:<0>`, and no errors/skips. Archive XML and restore production source. This proves sensitivity, not a base-versus-head bug fix: production is identical on base.
4. `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.parser.TokenMgrErrorTest -Ptests.seed=8755C0FFEE000002 -Ptests.jvms=1`; archive/read fresh XML before any further build.
5. `:solr:core:check -x test`; verify clean source.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Test-only changelog | decided: use repository CI exemption | Exact diff must stay test and Markdown only | Repository CI policy |
| Base proof | decided: no fail-before regression claim | Production unchanged on base; use disclosed mutation sensitivity instead | Ticket requests tests only |
| Failure | default: repair only within WORKFLOW.md scope, retry once on changed tree | Out-of-scope failure or failed retry stops this unit | WORKFLOW.md |
| Environment refusal | default: one relaunch if tests never execute | Preserve initial logs and state | WORKFLOW.md |

## On green

Write top receipt section, report pass findings, and mark gate DONE. Claim becomes DONE only after all three green passes. Coordinator publishes the atomic closeout.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog exemption verified
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 expected mutation failure verified from XML
- [x] step 4 focused head run counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] public edits not applicable
- [x] job DONE; claim DONE after all three green passes

## Deliverable and limits

Gate record, receipt and report. No PR opening/state change, new comments, Jira, email, force push, upstream change or branch deletion. Identity: Nick Shanin; no trailers.


Result: GREEN, all steps completed afresh. Fresh head XML: 5 tests, 0 failures, 0 errors, 0 skips; mutation XML: 5 tests, exactly 1 expected assertion failure, 0 errors/skips. No repairs, no continuation required.
