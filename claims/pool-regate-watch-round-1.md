# Claim: re-gate watch and internal-block inventory, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-regate-watch-round-1.md`
- Staffing: two read-only subagents. A takes SOLR-14231, SOLR-15357 and SOLR-15945; B takes the internal-block inventory across `pr-drafts/`.
- Scope: draft text after the vm2 re-gates named in the claims; the list of internal hold and status blocks in drafts; removal of fourteen internal blocks from draft bodies, with each block copied verbatim into `reports/internal-blocks-round-1.md`.
- Deliverable: `reports/regate-watch-round-1.md`, verdict first.
- Hold: SOLR-14231 is held at `2f73d00a3996`, because the fork tip moved to a changelog-only commit `593463f398cb`; SOLR-15357 and SOLR-15945 are held at their current named heads until the re-gates report.
- Heartbeat: 2026-10-12T20:29Z (claim taken).
- Standing coordinator claim: `claims/coordinator-review-rounds.md`.
- Status: DONE. Deliverable: `reports/regate-watch-round-1.md`. Internal blocks removed and recorded in `reports/internal-blocks-round-1.md`.
