# Claim: PR 5281 test-workflow repair, round 1

Status: CLAIMED (Windows implementation; Linux proof to be handed off).

- Claimant: windows, Codex desktop.
- Started: 2026-11-06 04:11:02 UTC (2026-11-05 America/New_York).
- Tags used: implementation, review, records.
- Source: Nick's direct instruction, "OK try to patch this and send it to the gate".
- Board snapshot inspected: `c2b1030a445fec89386896474a8490f117aa7e8f`.
- Contributor parent: `d05b60284ad24d842597fe9210cd23d74eb643b3`, live PR and fetched PR ref matched.
- Slice: indent the unittest command beneath `run: |` in `.github/workflows/pr-triage-test.yml`. No judgement/policy or production-script changes.
- Fork branch: `pr-5281-workflow-repair`, based directly on the contributor parent, ordinary commit and plain push to `nick-boss-tech/solr` only.
- Worktree: `wt/PR-5281-workflow-repair`.
- Deliverables: `reports/pr-5281-test-workflow-repair-round-1.md` and `gates/pr-5281-test-workflow-repair-round-1.md`.
- Expected gate receipt: `receipts/PR-5281-test-workflow-repair-round-1.md`, owned by the Linux execution host.
- Non-overlap: earlier triage follow-up gate is DONE at `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40`; no matching workflow-repair claim or gate exists in the inspected snapshot.
- Decision ladder: rung 1. The command is at column 1 instead of under the workflow's block scalar, and the current upstream run failed before creating jobs. Restore executable workflow structure; require Linux validation of failure-before and success-after rather than claiming an offline test assertion failure.
- Proof: no tests or Gradle on Windows. Request actionlint/ShellCheck on the original and patched workflow, the current offline suite at the patched head, tidy and root check on Linux. No gateway call, secret setup, comment publication or contributor-branch changes are part of the gate.
