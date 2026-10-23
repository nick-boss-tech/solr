# Gate job: SOLR-8755 review pass 1 (TokenMgrError tests)

Status: DONE (GREEN, 2026-10-23T06:35:59Z)

- Branch: `solr-8755-tokenmgrerror-tests`. Head: `e338b6e26eda711e564b242a996f8e0dc8eee451`, confirmed by ls-remote 2026-10-23T06:25Z.
- Prior gated head: none in this review round. Merge-base with upstream/main: `c8ab71430f6e7547c773da596101d680862310ff`.
- Intended host: phone-agent-2. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.parser.TokenMgrErrorTest`. Seed: `8755C0FFEE000001`.
- Diff scope: `solr/core/src/test/org/apache/solr/parser/TokenMgrErrorTest.java` (+61) and `drafts/SOLR-8755/pr-description.md` (+27). No production changes.
- Why: first of three required review regates, with no prior receipt.

Links: Ticket: SOLR-8755. Category: Test quality. Siblings: none known. Supersedes: none.

## What to run

Runner: `/workspace/tools/phone-agent-2-8755-gate.py`, a test-only adaptation of the host standard `phone-agent-2-hardening-gate-v2.py`. Configuration: `/workspace/tools/phone-agent-2-8755-pass-1.json`. Worktree: `/workspace/solr-8755-review`. Logs: `/workspace/phone-agent-2-logs/solr-8755-review/pass-1/`. One host lock, disk/inode guards, archived fresh XML, and restoration in finally are retained.

0. Verify changelog exemption against the exact diff: test plus Markdown only, per `.github/workflows/validate-changelog.yml`. No YAML fragment exists or is required for this test-only change.
1. `tidy`; clean diff required, otherwise scope-limited repair and one retry.
2. `:solr:core:compileJava :solr:core:compileTestJava --rerun-tasks -Pvalidation.errorprone=true`.
3. Temporary mutation: replace `errorCode = reason;` with `errorCode = LEXICAL_ERROR;` in generated `TokenMgrError.java`. Keep all head test assertions intact. Run the five tests at the recorded seed. Require exactly one failure, in `testMessageConstructorKeepsMessageAndReason`, containing `expected:<3> but was:<0>`, and no errors/skips. Archive XML and restore production source. This proves sensitivity, not a base-versus-head bug fix: production is identical on base.
4. `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.parser.TokenMgrErrorTest -Ptests.seed=8755C0FFEE000001 -Ptests.jvms=1`; archive/read fresh XML before any further build.
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

## Evidence-checker continuation

2026-10-23T06:33Z: tidy and forced Error Prone compile both returned 0. The mutation run executed five tests with exactly one failure and no skips/errors; archived XML names `testMessageConstructorKeepsMessageAndReason` with `expected:<3> but was:<0>`. The runner compared the counts mapping including its timestamp metadata with a numeric-only mapping and stopped despite matching numeric results. Corrected this evidence-only comparison and resumed at step 4 using the existing archived proof XML and unchanged clean source. No completed build step was rerun; no product repair or retry was consumed. Continuation configuration: `/workspace/tools/phone-agent-2-8755-pass-1-continuation.json`.

Result: GREEN at unchanged head. Fresh head XML: 5 tests, 0 failures, 0 errors, 0 skips. Steps 1, 2, 4, 5 rc 0; mutation proof exactly one expected failure. No repairs.
