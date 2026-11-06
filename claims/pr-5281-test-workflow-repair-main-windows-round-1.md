# Claim amendment: merged triage test-workflow repair, round 1

Status: CLAIMED (Windows implementation; Linux proof to be handed off).

- Claimant: windows, Codex desktop; tags implementation, review, records.
- Continues and supersedes the proof base in `claims/pr-5281-test-workflow-repair-windows-round-1.md`.
- New event: PR 5281 merged at 2026-11-06 04:19:43 UTC, by squash as `00a5f9701ea9850a26b1bc1ba83326edbd92f383`. The fetched `upstream/main` tip equals that commit, and its `.github/workflows/pr-triage-test.yml:26` retains the unindented command.
- Updated source/proof base H0: `00a5f9701ea9850a26b1bc1ba83326edbd92f383`.
- Historical contributor head: `d05b60284ad24d842597fe9210cd23d74eb643b3`; do not use it as the new patch parent or propose pull-in commands against the closed branch.
- Updated fork branch: `pr-5281-workflow-repair-main`.
- Updated worktree: `wt/PR-5281-workflow-repair-main`, with sparse checkout limited to the workflow area on Windows. Linux gate uses a full real clone.
- Original `wt/PR-5281-workflow-repair` checkout has no authored patch or implementation commit and is not the outbound branch.
- Scope and authorization are unchanged: one-line indentation repair under Nick's instruction to patch and send to the gate. No new public PR or comment is authorized by this amendment.
- Deliverables remain `reports/pr-5281-test-workflow-repair-round-1.md` and `gates/pr-5281-test-workflow-repair-round-1.md`; receipt `receipts/PR-5281-test-workflow-repair-round-1.md` belongs to the Linux execution host.
- Decision ladder rung 1: the broken workflow is confirmed in merged source; base the fix on main and require fail-before validation there. No source work or test execution has happened on the old base.
