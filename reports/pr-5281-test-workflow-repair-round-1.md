# PR 5281 test-workflow indentation repair, round 1

Status: Implemented, Proof Pending.

## Authorization and revisions

- Nick authorized the narrow patch and Linux handoff in this chat: "OK try to patch this and send it to the gate".
- Upstream PR: https://github.com/apache/solr/pull/5281
- Historical contributor head: `d05b60284ad24d842597fe9210cd23d74eb643b3`. PR merged at 2026-11-06 04:19:43 UTC.
- Verified merged main parent H0: `00a5f9701ea9850a26b1bc1ba83326edbd92f383`, the fetched upstream/main tip and PR squash merge.
- Fork branch: `nick-boss-tech/solr:pr-5281-workflow-repair-main`.
- Pushed head H1: `a3a930aa07d9676315f6133bdb1d7c88a1940942`, independently verified through the fork ref API after the plain push.
- Pinned diff: https://github.com/nick-boss-tech/solr/compare/00a5f9701ea9850a26b1bc1ba83326edbd92f383...a3a930aa07d9676315f6133bdb1d7c88a1940942
- Claim: `claims/pr-5281-test-workflow-repair-main-windows-round-1.md`, continuing the original pre-merge claim.
- Gate: `gates/pr-5281-test-workflow-repair-round-1.md`.
- Expected execution receipt: `receipts/PR-5281-test-workflow-repair-round-1.md`.

## Change and assessment

The Python command in `.github/workflows/pr-triage-test.yml:26` was at column 1, outside the step's `run: |` scalar. Merged main retains it. Indent that existing command to match the step's scalar content. The repair changes exactly one line in one file, with one ordinary commit on the main merge. No judgement-policy change is included; the broader tooling-test exemption remains an optional, separately evaluated follow-up.

- Correctness: restore workflow parsing and execution of the existing offline test command. Upstream run https://github.com/apache/solr/actions/runs/42155946264 is `failure` with no jobs at historical contributor head `d05b602`; source inspection confirms the same malformed workflow in merged H0. Source evidence is not a substitute for the requested Linux fail-before validator result.
- Architecture and reuse: retain the existing separate, unprivileged Python test workflow and its current test suite. No new helper, API, dependency or test fixture is introduced.
- Security and compatibility: triggers, action pins, `contents: read`, `persist-credentials: false`, secret isolation, and the production triage workflow are unchanged.
- Maintenance: one indentation correction, no new explanatory source comments, changelog or unrelated formatting. No Solr user-facing release note is warranted.
- Verification performed on Windows: source inspection, scoped diff, whitespace review, commit, plain fork push and independent remote-SHA read-back. No Gradle, actionlint, Python tests or gateway calls were executed on Windows.

## Gate scope and limits

Require actionlint to reject the original workflow for its YAML syntax, accept the patched test and production workflows with ShellCheck available, execute the current offline suite at H1 (44 methods by source inspection), and pass tidy/root `check -x test` in a real Linux clone. Tests and production script are unchanged, so the Python suite is expected to pass on H0 too; the demonstrated failure-before must be workflow validation, not a unit-test assertion.

The older GREEN receipt at `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40` is historical and does not validate H1. A posted gate is a request for execution, not a claimed or passing run. Live gateway behavior and GitHub activation are outside this narrow repair's proof.

The contributor branch and upstream PR head were not changed. No public comment was posted and no new PR was opened. A future public follow-up must reconcile the fresh receipt, fork ref, current main, merged PR discussion and publication authority. This is now a follow-up repair to main, not an offer to fast-forward the closed contributor branch.
