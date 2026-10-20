# Claim: pool-windows-solr-18507-phase-4-implementation-repair-round-1

- Claimant host: `windows`, matching `hosts/windows.md`. Nick authorized this assignment-specific implementation claim on 2026-10-20. This does not authorize builds or tests on Windows.
- Status: DONE: 2026-10-20T13:29:52Z. Deliverables: `solr-18507-phase-4-submit` at `81a7e5d78fa2be005a81e29d3dfc47a742272928`, `gates/SOLR-18507-phase-4-r4.md`, and `reports/SOLR-18507-phase-4-repair-round-1.md`.
- Job or assignment: `assignments/pool-solr-18507-phase-4-implementation-repair-round-1.md`.
- Slice: Diagnose and repair the five Phase 4 incremental fingerprint failures on `solr-18507-phase-4-submit`, starting at `807f9d42db448ee229a00d25eda9c9b63e3ed2f9`. Include the Phase 3 green fix. Keep changes within Phase 4 implementation and focused tests. Linux proof remains required before the phase can be treated as gated.
- Capability tags used: `implementation` (assignment-specific owner authorization on 2026-10-20).
- Started: 2026-10-20T12:58:23Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-18507-phase-4-repair-round-1@807f9d42db.
- Links: Ticket or parent: SOLR-18507. Category: Core admin and cores. Siblings: `gates/SOLR-18507-phase-3.md`, `gates/SOLR-18507-phase-4.md`, `assignments/pool-solr-18507-phase-3-benchmark-followup-round-1.md`, `assignments/pool-solr-18507-fingerprint-benchmark-round-1.md`. Supersedes: none.

Heartbeat: 2026-10-20T13:29:52Z

Result: 2026-10-20T13:29:52Z: Added per-test index clearing and core reload to isolate cache sizes and counters. Pushed commit `81a7e5d78fa2be005a81e29d3dfc47a742272928`; the cached-versus-uncached comparisons and expected values remain unchanged. The exact-head Linux gate request is OPEN at `gates/SOLR-18507-phase-4-r4.md`; focused result is pending.

DONE: 2026-10-20T13:29:52Z. Deliverables: `solr-18507-phase-4-submit` at `81a7e5d78fa2be005a81e29d3dfc47a742272928`, `gates/SOLR-18507-phase-4-r4.md`, and `reports/SOLR-18507-phase-4-repair-round-1.md`.
