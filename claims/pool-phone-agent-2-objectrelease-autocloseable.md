# Claim: ObjectReleaseTracker AutoCloseable failure cleanup
- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-objectrelease-autocloseable-round-1.md`.
- Slice: full assignment, local implementation and focused gate.
- Capability tags used: implementation, gate.
- Started: 2026-10-23T17:36:00Z (UTC).
- Lease: live while heartbeats continue; heartbeat at claim. Attempts: 1. Idempotency key: `ObjectReleaseTracker.tryClose AutoCloseable @ 79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Links: Parent: SOLR-17857 / PR #4226 lifecycle work; no new Jira item. Category: Test quality, with core test infrastructure as the production category. Siblings: `reports/object-release-tracker-autocloseable-handoff.md`, `assignments/pool-solr-18506-cache-cleanup-vm5.md` is a separate test-owned cleanup issue. Supersedes: none.
Heartbeat: 2026-10-23T17:36:00Z
Plan: inspect all tracked AutoCloseable-only types, implement only the test-failure fallback and its focused regression tests on a local candidate based on current Apache main, and run the assigned scoped gate. No product push or PR action.
