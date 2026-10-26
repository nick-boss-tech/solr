# Claim: PR 5235 compatibility fixes, round 3 (vm2)

Status: CLAIMED

- Host: vm2 (Linux, aarch64). Tags: gate, premise-run, review, records.
- Started: 2026-10-26T19:27:35Z.
- Board snapshot reviewed: 903d8365a88. No vm2 claim or receipt existed for round 3.
- Gate: `gates/pr-5235-compatibility-fixes-round-3.md`, superseded for the tidy repair by `gates/pr-5235-compatibility-fixes-round-3-superseding-caf8dde.md`.
- Pinned: fixed head `caf8dde9063f`, original O `9ff44cdd0b81` (ancestor, live PR head unchanged at claim).
- Trees: `/workspace/worktrees/pr5235-r3-F` (caf8dde, clean) and `/workspace/worktrees/pr5235-r3-O` (9ff44cd with the three F test files overlaid).
- Preflight: tidy at 5f279c7 changed one javadoc line; committed as caf8dde; tidy at caf8dde re-run as step 5 of the superseding gate.
- Evidence: `/workspace/gates/logs/vm2/pr5235-r3/`.
- Deliverables: `receipts/PR-5235-compatibility-fixes-round-3.md` and `reports/pr-5235-compatibility-fixes-round-3-outcome.md`, plus the author-facing draft in the receipt.
- Scope: tests and gates only. No upstream write, no PR, no comment, no Jira, no change to the contributor's branch.
