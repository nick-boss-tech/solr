# Receipt section: PR 5281 (advisory PR triage follow-up)

Prepend one section per gate to `receipts/PR-5281-triage-followup-round-1.md`.

## Gate PR-5281-triage-followup-round-1 (vm2, 2026-11-03): GATE GREEN

- Branch: `pr-5281-triage-followup`. Head: `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40` (confirmed by ls-remote before the run). Base: `35970681650b4e602632bdfc344c5922b9f1581f`.
- Links: Ticket or parent: https://github.com/apache/solr/pull/5281. Category: triage tooling / GitHub Actions. Siblings: `claims/pr-5281-triage-followup-windows-round-1.md` (implementation). Supersedes: none (new receipt).
- Runner: vm2 (Linux aarch64), JDK 21.0.12.1, Python 3.11.17. Log: `/workspace/logs/vm2/gate-5281/`.
- Preflight: detached tidy at H1, rc=0 "tidy clean" (`preflight/preflight.log`).
- Step 0, changelog: not required — this is a Python/GitHub Actions tooling patch with no user-facing changelog requirement (per gate job).
- Step 1, tidy (at H1, worktree): rc=0; post-tidy `git status --porcelain` clean (no source changes). Log: `leg5/leg5-tidy.log`.
- Step 2, Error Prone compile: not required — tooling-only patch, no Solr Java production/test changes (per gate job; `check -x test` covers the root checks instead).
- Step 3, proof legs:
  - Positive offline suite at H1: `python -m unittest discover -s dev-tools/scripts/tests -p test_pr_triage.py -v`, 23 tests, 0 failures, 0 skipped (stdlib unittest; no JUnit XML produced — full log retained). Log: `leg1/leg1-positive-suite.log`.
  - H0 fail-before: H1's `test_pr_triage.py` (sha256 `194a820310b75002d5493f7e222ec4925f39bbbf5fadac669cc3474068ec1fd7`) copied unchanged into H0 checkout; original H0 production script sha256 `27fc13254425e100aa121cdf87df354e24247b92f26c9d9272a1b2f806785ff4`. Three tests fail at H0 exactly as predicted: `test_deleted_evidence_is_missing` (`AssertionError: True is not false` — removed entry counted as present), `test_missing_answer_is_unknown` (`AssertionError: False is not None` — missing answer coerced to false), `test_comment_does_not_emit_active_model_markup` (`AssertionError: '<img' unexpectedly found in ...` — active model markup retained). 3 failures, no import/compile/startup/API/missing-helper errors. Same three tests at H1: all pass. Logs: `leg2/leg2-h0-failbefore.log`, `leg2/leg2-h1-selection.log`.
  - Rendered output boundary: H1 generated comment from the markup fixture rendered with cmarkgfm 2025.10.22; the reason cell contains no `<img`, `[click]`, `![image]`, `@someone`, `@org/team`, backtick, bold, pipe, newline/CR, or `https://`; renders as a plain `<p>` with no anchor/image/injected element. H0 from the same payload renders active `<a href>`, `<img>`, `<code>`, `<strong>`, `@someone` and bare `https://` autolink markup. Logs: `leg3/leg3-h1-render.log`, `leg3/leg3-h0-render.log`, harness `leg3/leg3-harness.py`.
  - Workflow syntax and offline wiring: actionlint 1.7.7 clean on both `pr-triage-llm.yml` and `pr-triage-test.yml` (with ShellCheck 0.10.0 available and enabled). judgements.yml parses; 5 judgement IDs: needs_jira, needs_changelog, needs_ref_guide, needs_upgrade_notes, needs_tests. Fetch/consistency shell step exercised with a local `gh` stub: stable metadata/files passes the jq guard; head-change and base-change each fail it. File-count mismatch (changed_files=5 vs 2 listed) refused by the helper before any model call (`Incomplete changed-file list; refusing to report missing evidence.`, no result.json). Privileged checkout pinned to `github.workflow_sha`, sparse-checkout of script+requirements+config only, no PR code executed in the judge job; `ASF_LLM_API_KEY` only in the judge step; comment job has no model key. Test workflow runs the exact Leg 1 command with `permissions: {}` / contents:read, no secrets. Logs: `leg4/`.
- Step 4, focused head run: not applicable — Python tests are deterministic, no seed; no Java focused suite required for this tooling-only patch.
- Step 5, module check `./gradlew check -x test` at H1: rc=0 (BUILD SUCCESSFUL, 18m39s); `git diff --check` clean; post-check `git status --porcelain` clean. Log: `leg5/leg5-check.log`. Environment note: the first attempt in a git worktree failed on `:solr:solr-ref-guide:buildLocalAntoraSite` with "Local content source must be a git repository" — a worktree's `.git` is a file, and Antora requires a real `.git` directory (host limitation per `gates/gates-vm2/antora-guide.md`, not a patch defect). Rerun in a real shared clone of H1 succeeded with no exclusions beyond `-x test`. Failed worktree log preserved: `leg5/leg5-check-worktree-fail.log`.

Activation limits (NOT proven by this gate): GitHub event delivery, the installed ASF secret, live gateway/model behavior, GitHub notification processing, and production comment publication. Those need an eventual controlled maintainer smoke test after the workflow is available from the trusted branch.

## Proposed offer for David

The H1 fork branch `nick-boss-tech/solr:pr-5281-triage-followup` pins the contributor parent H0 `35970681650b4e602632bdfc344c5922b9f1581f` as its first parent and adds one commit (`Harden advisory triage and automate contribution reminders`, author Nick Shanin) implementing the discussed follow-up plus offline tests. It is fast-forwardable into the upstream PR branch:

```sh
# In an upstream checkout with the contributor branch fetched:
git fetch https://github.com/nick-boss-tech/solr pr-5281-triage-followup
git diff 35970681650b4e602632bdfc344c5922b9f1581f...FETCH_HEAD   # pinned H0..H1 diff
git merge --ff-only FETCH_HEAD                                    # pull the follow-up in
```

Proof status: the offline unit suite (23 tests), H0 fail-before, rendered-output boundary, workflow lint and root checks are green on vm2 and recorded above. Live LLM gateway behavior and workflow activation on GitHub are not yet exercised — that is the separate maintainer smoke test once the workflow is available from the trusted branch. The offer is informational; nothing is posted publicly by this gate.
