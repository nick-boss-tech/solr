# Claim: SOLR-18586 Linux probe run, round 2 (vm2)

- Host: vm2 (Linux pool host, `hosts/vm2.md`).
- Assignment: `assignments/solr-18586-probe-run-round-2.md`, whole assignment.
- Slice: run the prepared RC3 logging probe from `reports/solr-18586-tlog-windows-investigation-round-1.md` on a clean control arm and a probe arm at RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`, seed `3635CC89D23D0929`, then `testRecovery` and `testOnlyLeaderIndexes` at both seeds. Premise run only: no fix, no gate, no receipt, no PR, no post.
- Capability tags used: premise-run, records.
- Started: 2026-11-03T02:26Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18586-probe-run-round-2@vm2.
- Links: `reports/solr-18586-tlog-windows-investigation-round-1.md`, `assignments/solr-18586-tlog-rejoin-recovering-implementation-round-1.md`. Supersedes: none.
- Decision ladder: rung 2, a probe that separates the explanations. Choices recorded in the report.
- Deliverable: `reports/solr-18586-probe-run-round-2.md`.
