# Claim: solr-5258-wasm-browser-leg-round-1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: `assignments/wasm-browser-test-5258-round-1.md` (HANDED to vm6 by `67c5a6b37c`).
- Slice: the whole job: run `:shared:wasmJsBrowserTest` for PR #5258 at head `d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a` in a scratch worktree, and file a receipt. No GitHub or branch change.
- Capability tags used: gate, ui, build.
- Started: 2026-10-30T19:49:04Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-5258-wasm-browser-leg-round-1@d02ccdcdd8.
- Toolchain: JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, Gradle 9.7.0 from the branch wrapper, GRADLE_USER_HOME `/workspace/gradle-home`, Node v22.23.4 and npm 10.9.9 on the host. Browser: `/opt/playwright-browsers/chromium-current` (chrome-headless-shell, resolves to `chromium_headless_shell-1248`); version recorded in the receipt.
- Scratch worktree: `/workspace/worktrees/pool-vm6-5258-wasm` (detached at `d02ccdcdd8`).
- Log directory: `/workspace/gates/logs/vm6/solr-5258-wasm-round-1/`.
- Build lock: `/workspace/tools/.gradle-vm6.lock`, taken by `pool/bin/sweep-run.sh`.
- Handoff inputs: the vm5 logs named in the handoff are on vm5's disk and are not reachable from vm6 (checked at claim time); the run here is fresh.
- Links: Ticket or parent: PR #5258 (SOLR-17914, draft). Parent assignment: `assignments/wasm-browser-test-5258-round-1.md`. Predecessor claim: `claims/solr-5258-wasm-browser-leg-round-1-vm5.md` (RELEASED). Blocker record: `receipts/pr-5258-wasm-attempt-2026-10-30.md`. Supersedes: none.

Heartbeat: 2026-10-30T19:49:04Z

Result: 2026-10-30T20:04:05Z: wasm browser suite at d02ccdcdd8 PASS. 19 suites, 69 tests, 0 failures, 0 errors, 3 skipped (pre-existing @Ignore). Browser ChromeHeadless 156.0.8078.4. Receipt: receipts/solr-5258-wasm-browser-leg-round-1.md.

DONE: 2026-10-30T20:04:05Z. Deliverables: receipts/solr-5258-wasm-browser-leg-round-1.md; receipts/solr-5258-wasm-browser-leg-round-1-xml/.
