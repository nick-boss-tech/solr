# Claim: CombinedQueryComponent totals premise, verify or refute

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review (the reading). Any confirmation run is spec'd for a Linux host and is not run here.
- Assignment: `assignments/pool-combinedquerycomponent-premise.md`
- Date: 2026-10-11
- Staffing: 2 read-only subagents, where the assignment says 1, because the scheduled check asks for at least 2. Slice 1 traces the accumulation, skip and fallback order, the exactness fold, and the maximum fold, against main `d76fc14b985` and the merge-base; it answers the questions on those points. Slice 2 compares the shape with the fixed `QueryComponent.mergeIds` and designs the confirmation run spec and the smallest test shape. Both report as text; the coordinator writes the one report.
- Scope: read-only. No branch edit, no fix implemented, no build, test or gate, no filing, no post.
- Deliverable: `reports/combinedquerycomponent-premise.md`, verdict first, one verdict per defect (skip ordering, fallback, exactness fold).
- Heartbeat: 2026-10-11T21:50Z (claim taken).
- Status: ACTIVE.
