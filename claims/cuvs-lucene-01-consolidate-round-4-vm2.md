# Claim: cuvs-lucene-01-consolidate-round-4-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job: `assignments/cuvs-lucene-01-consolidate-round-4.md`. No other round-4 claim on the board for step 01 at the snapshot.
- Snapshot: coordination-ui `c5c758b0f6c` (fetched 2026-10-24T22:36:55Z).
- Live upstream: NVIDIA/cuvs PR 2702 is still a draft, open, head `23004d0f0de2014f11b09313f04fcf0de7643c3b` (the pinned base), updated 2026-10-15. No drift, so no replacement gate.
- Inputs: packages `2ad555a87a22acdf617f679ca03f5831f246b4a8` (cpu-regression.patch, SHA-256 d9ad4658...b154) and `e174c1a3e0dcfccf9d30a31847637733823df84b` (backcompat.patch, SHA-256 56cb5cb1...8238). Receipts `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md` and `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`.
- Slice: consolidate the four disjoint test files into `material/cuvs-lucene-contribution-round-4/combined-tests.patch`, with the two originals, a manifest, reconstruction steps, and the report `reports/cuvs-lucene-consolidation-round-4.md`. No production file changes. No replay of either gate.
- Capability tags used: implementation, records.
- Started: 2026-10-24T22:36:55Z.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: cuvs-lucene-01-consolidate-round-4@c5c758b0f6c.
- Constraints: no Gradle, Maven, native, or GPU build; no source-repository push, public PR, or comment. Records commit as Nick Shanin, no trailers.

Heartbeat: 2026-10-24T22:36:55Z
