# Claim: PR 5235 JDK raw-body capture, round 2 (vm2)

Status: CLAIMED

- Host: vm2 (Linux, aarch64). Tags: gate, premise-run.
- Started: 2026-10-26T18:39:02Z.
- Board snapshot reviewed: b678edc6e9d. Competing claims: only the Windows preparation claim (`claims/pr-5235-jdk-raw-capture-windows-round-2.md`). No vm2 claim or receipt existed for this round.
- Gate: `gates/pr-5235-jdk-raw-capture-round-2.md`, superseded by `gates/pr-5235-jdk-raw-capture-round-2-superseding-1517b48.md` for the tidy repair.
- Pinned: fork head `1517b48953dd` (parent `63e517018db`, the reviewed capture repair); base `f45b38d5f6c0`. Test blob `6ae19ed015bc` identical on both legs.
- Preflight: tidy at the tip rc 0 after the formatting repair. PR tree clean.
- Trees: `/workspace/worktrees/pr5235-pr` and `/workspace/worktrees/pr5235-base`.
- Evidence: `/workspace/gates/logs/vm2/pr5235-jdk-raw-round-2/`.
- Deliverables: `receipts/PR-5235-jdk-raw-capture-round-2.md` and `reports/pr-5235-jdk-raw-capture-round-2-outcome.md`.
- Scope: test runs only. No production change, no upstream write, no PR, no comment, no Jira.
