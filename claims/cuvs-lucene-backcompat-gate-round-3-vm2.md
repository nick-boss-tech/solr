# Claim: cuvs-lucene-backcompat-gate-round-3-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job: `gates/cuvs-lucene-backcompat-round-3.md`, routed by `assignments/cuvs-lucene-cpu-proof-round-3.md`. No other Linux gate claim or receipt existed at the snapshot.
- Board snapshot: `3dc46ddd6689` (coordination-ui, fetched at 2026-10-24T21:45:40Z).
- Package: `material/cuvs-lucene-backcompat-round-3/` at `e174c1a3e0dcfccf9d30a31847637733823df84b`, exported with git archive into the fresh runtime `/workspace/gates/cuvs-backcompat-round-3/material/cuvs-lucene-backcompat-round-3`.
- Patch SHA-256: `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238` (verified).
- Host: vm2 (Linux aarch64). JDK: 21.0.12.1 at `/workspace/jdk/jdk-21.0.12.1+1` (CUVS_CPU_JDK). Python: 3.11.17.
- Preflight: `reproduce.py --preflight-only` rc=0, log `/workspace/gates/cuvs-backcompat-round-3/preflight.log`.
- Full run log: `/workspace/gates/cuvs-backcompat-round-3/reproduce.log`.
- Started: 2026-10-24T21:45:40Z.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: cuvs-lucene-backcompat-round-3@e174c1a3e0d.
- Constraints: CPU only; no Gradle, Maven, Solr, native, GPU, source push, or public comment. Receipt goes to `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`.

Heartbeat: 2026-10-24T21:45:40Z
