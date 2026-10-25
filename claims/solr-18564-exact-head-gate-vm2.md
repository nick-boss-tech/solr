# Claim: solr-18564-pr5192-exact-head-gate (gate)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job: `gates/SOLR-18564-pr5192-exact-head.md`. Head `dd65b21b41dc1d008c51031b71f61964ee2ca169` on `nick-boss-tech/solr:corecontainer-shutdown-closes`, verified by ls-remote at claim time.
- Snapshot: coordination-ui `3db39c0c5df`, fetched 2026-10-25T03:33:51Z.
- Slice: the job's legs at the exact head: changelog YAML, tidy with GATE INVALID guard, Error Prone compile, focused tests CoreContainerShutdownTest and TestCoreContainer, `:solr:core:check -x test`. Deliverable: exact-head section in `receipts/SOLR-18564.md` (owned by vm2 as its gating host).
- Capability tags used: gate.
- Started: 2026-10-25T03:33:51Z.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18564-exact-head-gate@dd65b21b41dc.

Heartbeat: 2026-10-25T03:33:51Z
