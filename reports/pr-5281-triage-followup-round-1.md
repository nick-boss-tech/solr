# PR 5281 triage follow-up implementation, round 1

Status: IMPLEMENTED, PROOF PENDING

Host: Windows Codex desktop. Started 2026-11-03 16:36:47 UTC; pushed and remote-verified before 16:49:18 UTC.

## Identity and authorization

Nick requested validation of the prior review and a branch implementing the desirable feature and David Smiley's inline suggestions. The live PR and all eight author comments were refreshed. The upstream PR remained open at `35970681650b4e602632bdfc344c5922b9f1581f`, with no conversation comments or outside review in the captured state. New evidence was saved locally under `research/branch-reviews/2026-11-03-pr-5281-followup/evidence/PR-5281`.

- Contributor parent H0: `35970681650b4e602632bdfc344c5922b9f1581f`.
- Fork branch: `nick-boss-tech/solr:pr-5281-triage-followup`.
- Pushed H1: `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40`.
- Commit: `Harden advisory triage and automate contribution reminders`; Nick Shanin's ICLA identity, no attribution trailers.
- Diff: https://github.com/nick-boss-tech/solr/compare/35970681650b4e602632bdfc344c5922b9f1581f...6bcaa9e1191fb8d0c6def2eaf005b602bf882e40
- Claim: `claims/pr-5281-triage-followup-windows-round-1.md`.
- Gate: `gates/pr-5281-triage-followup-round-1.md`.
- Expected receipt: `receipts/PR-5281-triage-followup-round-1.md`.
- Board intake snapshot: `af67bddb0a960d099977c9afb786f0e35530ad41`; refreshed pre-handoff snapshot `200a476cfded9ef96073dae1a226dfff61cd43e1` had only this host's matching claim and no competing assignment/gate.
- Current upstream main fetched: `e9a2ecde7fca8e4020604d50ae7879da21a543d7`; its intervening paths do not overlap this follow-up.

## Review validation and changes

The deleted-evidence and active-Markdown findings were confirmed by tracing H0's `main()` through comment construction. They remain runtime proof pending. The markup finding is scoped to a manipulated bot comment on the current PR; the model has no GitHub token and cannot change branches. It is not evidence of a Solr server vulnerability. A further correctness defect was confirmed: omitted/malformed answers could become a negative verdict, including a green agreement mark. H1 reports unknowns and requires human review instead.

H1 excludes removed evidence files, validates answer types and duplicate JSON fields, and encodes model text before inserting it into the comment. It flattens/bounds reasons, removes control characters, and breaks mention/autolink tokens. File presence is explicitly distinguished from adequate coverage, and the comment no longer claims the PR description cannot reveal the author's evidence choices.

David's comments are implemented as early deterministic notices for non-main targets and optional maintainer edits on personal forks, with organisation/same-repository/unavailable metadata exceptions. The three matching template checkboxes are removed; the contribution-guideline acknowledgement and existing minor-change Jira exception remain. A fifth advisory judgement asks whether a Jira issue is warranted, comparing against a canonical title reference without claiming issue existence or relevance was checked.

The workflow checks out its own trusted revision rather than the PR's potentially older base commit, installs the existing PyYAML requirement explicitly in a venv, checks head/base stability around paginated file fetching, and refuses incomplete file lists. It retains the original separate read-only judge and write-enabled comment jobs. The assessed commit appears in the one-time comment.

The helper has 23 offline unittest methods, with existing main/HTTP boundaries mocked rather than executing PR code or contacting GitHub/the LLM. A separate `pull_request` test workflow has read-only contents permission and no secrets. The existing PyYAML requirement is shared through a small requirements file, without a version change or new production dependency. Developer documentation explains setup, advisory semantics and offline commands. Test evidence patterns include Python tooling and Kotlin test sources.

## Exact outbound files

- `.github/PULL_REQUEST_TEMPLATE.md`
- `.github/pr-triage/judgements.yml`
- `.github/workflows/pr-triage-llm.yml`
- `.github/workflows/pr-triage-test.yml`
- `dev-tools/scripts/README.md`
- `dev-tools/scripts/pr-triage-llm.py`
- `dev-tools/scripts/requirements-pr-triage.txt`
- `dev-tools/scripts/requirements.txt`
- `dev-tools/scripts/tests/test_pr_triage.py`

Nine files, 550 insertions and 67 deletions relative to H0. No changelog fragment: this is developer automation, preserving the author's intended removal of the mandatory changelog check and opt-out command workflow.

## Architecture, reuse, verification and limits

Correctness: confirmed source defects have scoped repairs and regression expectations. Architecture: subjective questions stay configurable, metadata checks stay deterministic, and privileged jobs never execute PR code. Reuse: stdlib unittest/mock uses the existing CLI and HTTP boundaries; the scripts tree had no relevant Python test harness to extend. A separate unprivileged workflow covers the helper because the existing Gradle check does not exercise it. Maintenance: the Python dependency version is defined once and shared with the existing scripts requirements.

Windows performed no runtime/build/test/formatter execution. `git diff --check` passed; the scoped ordinary commit was plain-pushed, and a separate GitHub ref read returned the identical full H1 SHA. Original H0 upstream checks remain green (gradle check, changelog structure, labeler), but none validates H1. The Linux gate names all 23 positives, three unchanged-test H0 assertion failures, rendered GFM output, workflow lint/offline wiring, tidy and root check. No live gateway credentials, model quality, event delivery or comment publication is claimed.

Reference checks: GitHub documents `github.workflow_sha` as the workflow commit (https://docs.github.com/en/actions/reference/workflows-and-actions/contexts); current `pull_request_target` sources its workflow from the default branch (https://github.blog/changelog/2025-11-07-actions-pull_request_target-and-environment-branch-protections-changes/). The REST files endpoint has a 3,000-file cap (https://docs.github.com/en/rest/pulls/pulls), motivating the file-count refusal. Maintainer edits are optional for personal forks (https://docs.github.com/en/pull-requests/how-tos/work-with-forks/allowing-changes-to-a-pull-request-branch-created-from-a-fork). These are source/API design checks, not executed workflow proof.

No public comment, upstream write, contributor-branch update or PR-body edit was made. The upstream PR head did not move. Source implementation is delivered on the fork; readiness remains **Implemented, Proof Pending** until the named Linux receipt satisfies every leg at the exact offered head.
