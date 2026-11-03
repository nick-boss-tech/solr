# Gate: advisory PR triage follow-up, round 1

Status: DONE (GREEN 2026-11-03, vm2; all five legs at H1 6bcaa9e1191; receipt receipts/PR-5281-triage-followup-round-1.md).

Tags: gate, review, records.
Intended host: any available capable Linux CPU gate host with Python 3/venv, Git, Bash, jq, actionlint/ShellCheck, JDK 21 and this branch's Gradle wrapper/build dependencies.

## Revision and scope

- Upstream PR: https://github.com/apache/solr/pull/5281
- Contributor parent H0: `35970681650b4e602632bdfc344c5922b9f1581f`.
- Fork repository: `nick-boss-tech/solr`.
- Fork branch: `pr-5281-triage-followup`.
- Required head H1: `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40` (remote ref separately verified).
- Diff: https://github.com/nick-boss-tech/solr/compare/35970681650b4e602632bdfc344c5922b9f1581f...6bcaa9e1191fb8d0c6def2eaf005b602bf882e40
- Implementation claim: `claims/pr-5281-triage-followup-windows-round-1.md`.
- Implementation report: `reports/pr-5281-triage-followup-round-1.md`.
- Receipt owned by execution host: `receipts/PR-5281-triage-followup-round-1.md`.

Windows performed source review, diff whitespace review, commit and fork push only. No runtime proof has run. State remains **Implemented, Proof Pending**. This is a Python/GitHub Actions change, with no Solr Java production or Java test changes and no user-facing changelog requirement.

## Claim and environment

Read current board rules, check for an existing live gate claim/receipt, verify the fork ref, and perform the detached formatting preflight before claiming. Use a clean task-specific detached checkout at H1, one gate at a time under the host lock. Record UTC, exact checkout SHA, tool versions, all commands, exit codes, and fresh evidence paths. Python tests are deterministic; no seed is applicable. Use `5281C0DE` for any incidental seeded Gradle tasks. No model or GitHub credentials are needed for the offline tests. Do not call the live LLM or post a comment as part of this gate.

## Required proof legs

1. **Positive offline suite at H1.** Create a disposable venv, install `dev-tools/scripts/requirements-pr-triage.txt`, then run from the checkout root:

   ```sh
   python -m unittest discover -s dev-tools/scripts/tests -p test_pr_triage.py -v
   ```

   Use the venv's `python`. Expect all 23 test methods to pass, with no skipped methods. Keep the complete fresh stdout/stderr and exit status. The tests mock all model calls, including the HTTP request boundary. They exercise CLI JSON I/O, removed/present evidence, malformed/duplicate/missing answers, safe reasons, title evidence, branch/access metadata, hidden evidence, dry-run, incomplete file lists and diff-budget selection. JUnit XML is not produced by stdlib unittest; say so, and retain its full test log instead of inventing Java XML.

2. **Runtime fail-before with unchanged tests.** Make a separate H0 checkout. Copy only H1's `dev-tools/scripts/tests/test_pr_triage.py` into its matching path, preserving the test byte content. Keep the original H0 production script and a disposable venv with PyYAML. Set `PYTHONPATH=dev-tools/scripts/tests` and run:

   ```sh
   python -m unittest -v test_pr_triage.TriageTest.test_deleted_evidence_is_missing test_pr_triage.TriageTest.test_missing_answer_is_unknown test_pr_triage.TriageTest.test_comment_does_not_emit_active_model_markup
   ```

   Expect three assertion failures: removed entry counted as present, missing answer coerced to false, and active model markup retained. There must be no import, compilation, startup, API or missing-helper errors. These methods enter the pre-existing `main()` function and require no added production API. Record the original script hash, unchanged test hash, exact diff, full failure log and exit status. Run the same selection at H1 and require all three to pass. Other new assertions are coverage pins, not mutation-proven claims. Do not demand the whole new suite pass on H0: some tests intentionally target new helpers/configuration.

3. **Rendered output boundary.** In a disposable gate-only environment, use a GFM renderer (for example `cmarkgfm`, recording its version) to render the actual generated comment from the markup fixture in `test_comment_does_not_emit_active_model_markup`. The reason cell must contain readable text without an anchor, image, or injected HTML element; model mentions and bare URL/issue-reference tokens must remain broken by zero-width separators. Retain source runner, input, generated Markdown, rendered HTML and assertions. Compare the H0 comment from the same payload to confirm its reason renders active link/image markup. This is bounded bot-output integrity proof, not a claim of token exposure or a Solr vulnerability.

4. **Workflow syntax and offline wiring.** Run actionlint with ShellCheck available against `.github/workflows/pr-triage-llm.yml` and `.github/workflows/pr-triage-test.yml`. Validate the YAML config and the five configured judgement IDs. Exercise the production workflow's fetch/consistency shell step with a local `gh` stub and fixtures: stable metadata/files succeeds, a head change or base change fails the jq guard, and a file-count mismatch is rejected by the helper before any model call. Keep the harness/fixtures and full logs. Inspect that privileged checkout is pinned to `github.workflow_sha`, no PR code is executed there, the API key exists only in the judge step, and the separate comment job has no model key. Exercise the new test-workflow commands in the clean Linux environment. Do not claim this proves GitHub event delivery, the installed ASF secret, live gateway/model behavior, GitHub's notification processing, or production comment publication; those need an eventual controlled maintainer smoke test after the workflow is available from the trusted branch.

5. **Formatting and root checks at H1.** Run the branch's `./gradlew tidy`, require no source changes, then run `./gradlew check -x test`. Record fresh logs and exit codes. No Java focused suite is required for this tooling-only patch. No exclusion beyond `-x test`, no weakened validation, and no unrelated source repair. Check `git diff --check` and clean status at the end.

## Failure and repair rules

An executed failure must be investigated and reported before retry. Distinguish the three required H0 assertion failures from positive failures, and distinguish host/dependency interruptions from test defects. An environment run that never executed tests may be relaunched once after recording its cause and repair. Do not silently rerun assertions or weaken expectations.

Source, tests, or formatting changes invalidate H1 proof. Commit any authorized scoped repair to the fork with Nick's ICLA identity, plain-push and verify it, then post an add-only implementation report and superseding gate naming the new head before treating positive proof as complete. Do not overwrite this job or another host's claim. Report unrelated build blockers separately rather than broadening the patch. Never force-push, change David's branch, or write upstream.

## Completion

Write the named receipt with exact head, all five legs, log/evidence paths, counts, exit codes, negative assertion details and remaining activation limits. A positive suite alone is not gate completion. Include a concise proposed offer for David with the pinned H0-to-H1 diff and fast-forward pull-in commands, clearly distinguishing offline proof from live LLM/workflow activation. Do not post it publicly as part of this gate. Public follow-up must reconcile the actual receipt, remote fork head, live PR head/discussion and standing authorization first.
