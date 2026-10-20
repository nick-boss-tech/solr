# Claim: pool-solr-18507-fingerprint-benchmark-round-1

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: HANDED (partial). Item 1 first (Phase 2 versus Phase 3 persisted-summary costs), then item 2 only if its gate prerequisite is met.
- Job or assignment: `assignments/pool-solr-18507-fingerprint-benchmark-round-1.md`.
- Slice: the whole assignment, item 1 first.
- Capability tags used: sweep.
- Started: 2026-10-20T08:33:00Z (UTC).
- Links: Ticket or parent: SOLR-18507. Category: Core admin and cores. Siblings: gates/SOLR-18507-phase-3.md (DONE), gates/SOLR-18507-phase-4.md (head moved, not claimed). Supersedes: none.

Heartbeat: 2026-10-20T08:33:00Z

Heartbeat: 2026-10-20T09:17:12Z (SOLR-18507 benchmark item 1: Phase 2 core jar built at 137e77a4c8; Phase 3 core jar build run build-p3-core-jar; harness next)

Heartbeat: 2026-10-20T09:45:28Z (SOLR-18507 benchmark item 1: harness Bench.java compiled; matrix running detached: /workspace/gates/bench-harness/run-matrix.sh, out/matrix.csv, out/matrix.log; no product changes)

Result: 2026-10-20T10:35:16Z: item 1 partial. Cold reopen and warm timings measured for both heads (480 rows, reports/data/SOLR-18507-fingerprint-benchmark-round-1.csv); flush and merge cost, retained memory, persisted metadata size and the max-version case NOT measured. No optimization claim. Item 2 blocked (Phase 4 gate not DONE). Report: reports/SOLR-18507-fingerprint-benchmark-round-1.md. Handed back for direction.

DONE: 2026-10-20T10:35:16Z. Deliverables: reports/SOLR-18507-fingerprint-benchmark-round-1.md (partial), reports/data/SOLR-18507-fingerprint-benchmark-round-1.csv.
