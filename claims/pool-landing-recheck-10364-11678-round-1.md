# Claim: landing re-check for SOLR-10364 and SOLR-11678, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-landing-recheck-10364-11678-round-1.md` (commit `ef2e8d4411e`)
- Staffing: two read-only subagents, one draft each (the assignment allows up to two in parallel).
- Scope: `pr-drafts/security/SOLR-11678.md` (SEC-R1-03, SEC-R1-04, SEC-R1-05) and `pr-drafts/solrj/SOLR-10364.md` (SJ-R1-45), against the live tips and the refreshed receipts.
- Deliverable: `reports/landing-recheck-10364-11678-round-1.md`, verdict first.
- Hold: SEC-R1-07 and SEC-R1-08 are out of scope for this round, as the assignment says.
- Heartbeat: 2026-10-12T21:37Z (claim taken).
- Standing coordinator claim: `claims/coordinator-review-rounds.md`.
- Status: DONE. Deliverable: `reports/landing-recheck-10364-11678-round-1.md`. SOLR-10364 converged at the live head; SOLR-11678 patched for SEC-R1-03 and SEC-R1-04, still held for SEC-R1-05.
