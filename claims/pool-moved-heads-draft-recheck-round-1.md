# Claim: moved-heads draft re-check, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review (and the mechanical draft patches the owner's patch-loop directive allows)
- Assignment: `assignments/pool-moved-heads-draft-recheck-round-1.md`
- Trigger check at claim time (2026-10-12T14:38Z, ls-remote): solr-11939-submit `cdb9903506e9`, solr-17731-submit `18165d35a7e3`, solr-16437-submit `f72a9edd0a8b`, solr-6711-submit `73c5e6e0d243`, solr-17612-submit `3a456c27dda5`, solr-16013-submit `966bb4c4eb44`. All six match the heads the assignment names. No draft exists under pr-drafts/ for SOLR-6711, SOLR-17612 or SOLR-16013.
- Staffing: up to three read-only subagents, one draft each. The patches are applied by this host, not by a subagent.
- Scope: `pr-drafts/core-admin/SOLR-11939.md`, `pr-drafts/core-admin/SOLR-17731.md`, `pr-drafts/solrcloud/SOLR-16437.md`, each checked against its receipt at the live head. Patches are limited to what the receipt settles (head SHAs, Proof counts, run dates, changelog link targets) and to the ADDREPLICA wording in the SOLR-11939 Limits. No branch is edited, built or gated.
- Deliverable: `reports/moved-heads-draft-recheck-round-1.md`, verdict first, with one section per ticket.
- Heartbeat: 2026-10-12T14:38Z (claim taken).
- Standing coordinator claim: `claims/coordinator-review-rounds.md`.
- Status: DONE. Deliverable: `reports/moved-heads-draft-recheck-round-1.md`. Patched and re-checked: SOLR-11939, SOLR-17731, SOLR-16437 (each CONVERGED UNCHANGED on pass 2). No draft on record: SOLR-6711, SOLR-17612, SOLR-16013.
