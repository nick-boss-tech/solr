# Claim: solr-10928-live-head-regate (gate)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: LIVE.
- Job: `gates/SOLR-10928-regate-live-head.md`. Head `f2001e640a464e8e861ee3c202821b6dcc4bd497` on `nick-boss-tech/solr:solr-10928-submit`, verified by ls-remote at claim time.
- Snapshot: coordination-ui `3db39c0c5df`, fetched 2026-10-25T03:33:51Z.
- Slice: tidy (GATE INVALID guard), Error Prone compile, focused classes QueryElevationComponentTest, QueryElevationComponentParamTest and the distributed elevation test, `:solr:core:check -x test`. Deliverable: section in `receipts/SOLR-10928.md` (create if absent; vm2 as gating host).
- Capability tags used: gate.
- Started: 2026-10-25T03:33:51Z.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-10928-live-head-regate@f2001e640a46.

Heartbeat: 2026-10-25T03:33:51Z
