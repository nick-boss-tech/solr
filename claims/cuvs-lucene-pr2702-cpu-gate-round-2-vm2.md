# Claim: cuvs-lucene-pr2702-cpu-gate-round-2-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job: `gates/cuvs-lucene-pr2702-cpu-round-2.md` (the whole job; no other live gate claim was on the board at the time).
- Package: `material/cuvs-lucene-pr2702-round-2/` at `2ad555a87a22acdf617f679ca03f5831f246b4a8`, exported with git archive into the runtime directory.
- Upstream: NVIDIA/cuvs PR 2702, base `23004d0f0de2014f11b09313f04fcf0de7643c3b`.
- Patch SHA-256: `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154` (verified).
- Host: vm2 (Linux aarch64). JDK: 21.0.12.1 at `/workspace/jdk/jdk-21.0.12.1+1` (CUVS_CPU_JDK). Python: 3.11.17. Git: 2.34.1.
- Runtime directory: `/workspace/gates/cuvs-pr2702-cpu-round-2/material/cuvs-lucene-pr2702-round-2`.
- Logs: `/tmp/cuvs-prep.log` and `/tmp/cuvs-format.log` (preflight). Full run: `/workspace/gates/cuvs-pr2702-cpu-round-2/reproduce.log`.
- Preflight: prepare_sources.py rc=0; format_check.py rc=0.
- Started: 2026-10-24T21:12Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: cuvs-lucene-pr2702-cpu-gate-round-2@2ad555a87a.
- Constraints: no edit to the gate, the implementation claim, or the upstream PR; no Jira, GitHub, or NVIDIA post; no source push. Receipt goes to `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`.

Heartbeat: 2026-10-24T21:12Z
