# Claim: PR 5235 compatibility premise gate, round 1 (vm2)

Status: CLAIMED

- Host: vm2 (Linux, Amazon Linux 2023 aarch64). Tags used: gate, premise-run.
- Started: 2026-10-26T17:18:49Z.
- Board snapshot reviewed: e60890c1c70. Gate file: `gates/pr-5235-compatibility-premise-round-1.md`. No existing Linux claim, receipt, or report for this gate was found.
- Pinned inputs: fork test head `de1ffbbb55add66391eed68e4153322d6a8cc6c0` (parent `73fa87f1f41b`, the reviewed PR head). Unchanged base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. Seed `5235C0FFEE18351`.
- Blob hashes verified against the pins: `d48a15f9`, `e3a8821c`, `d3d4cc29` (the three test files, identical on both legs).
- Trees: PR leg `/workspace/worktrees/pr5235-pr` (detached at the test head, clean). Base leg `/workspace/worktrees/pr5235-base` (detached at base, three test files overlaid with `git restore --source`; tracked production unchanged).
- Preflight: `./gradlew tidy` at the test head, rc 0, tree clean. Log `/workspace/gates/logs/vm2/pr5235/pr-tidy.log`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1`; Gradle from the branch wrapper; `GRADLE_USER_HOME=/workspace/gradle-home`.
- Lock: `/workspace/tools/.gradle-vm2.lock`, taken by a `flock` wrapper around each leg.
- Logs and fresh XML: `/workspace/gates/logs/vm2/pr5235/`.
- Deliverables: `receipts/PR-5235-compatibility-premise-round-1.md` (receipt, written by this host only) and `reports/pr-5235-compatibility-premise-round-1-outcome.md`.
- Scope: run the gate as written. No production change, no upstream write, no PR, no comment, no Jira.
