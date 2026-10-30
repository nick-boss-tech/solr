# Claim: solr-5258-wasm-browser-leg-round-1-vm5

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: LIVE.
- Job or assignment: `assignments/wasm-browser-test-5258-round-1.md`.
- Slice: run the wasm browser suite `:shared:wasmJsBrowserTest` for PR #5258 at head `d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a`, in a scratch worktree `/workspace/worktrees/pool-vm5-5258-wasm`. Change nothing on GitHub or on the branch.
- Capability note: the assignment names the `ui` capability. `hosts/vm5.md` does not list `ui`. vm5 has Chromium headless shell at `/opt/playwright-browsers/chromium-current` and Node 22 with npm. The npm install step (`kotlinWasmNpmInstall`) passes on this host, which is the step the phone agent's VM could not finish. The receipt will say so; the owner or coordinator decides whether vm5 counts as a ui host for this leg.
- Capability tags used: gate, ui (see note).
- Started: 2026-10-30T17:09:54Z.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-5258-wasm-browser-leg-round-1@d02ccdcdd8.
- Links: PR #5258 (SOLR-17914, draft). Parent assignment: `assignments/wasm-browser-test-5258-round-1.md`. Blocker record: `receipts/pr-5258-wasm-attempt-2026-10-30.md`. Supersedes: none.

Heartbeat: 2026-10-30T17:09:54Z
