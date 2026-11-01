# Claim: solr-17988-property-audit-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE. Job or assignment: `assignments/solr-17988-property-audit-round-1.md`.
- Slice: the whole assignment, SOLR-17988 (Blocker) property-transition audit between 9.x and 10.0.
- Capability tags used: review, records. Research only; no code change, no Jira or GitHub post.
- Started: 2026-10-24T04:41:04Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-17988-property-audit-round-1@871051524b.
- Links: Ticket or parent: SOLR-17988. Category: 12, Build, scripts, and release tooling (env vars and bin/solr scripts); 14, Reference guide and docs (upgrade notes). Siblings: none known. Supersedes: none.
- Constraints: no Jira or GitHub posts, no code change. Commits as Nick Shanin with no trailers.
- Decision ladder: rung 1 for the reads. Verdict choices recorded in the report with the rung used.

Heartbeat: 2026-10-24T04:41:04Z

Result: 2026-10-24T04:49:32Z: audit written. Verdict: covered with named gaps. Five user-set env vars renamed or removed with no note and no warning (SOLR_PORT, SOLR_HOST, SOLR_REQUESTLOG_ENABLED, SOLR_ADMIN_UI_DISABLED, SOLR_WAIT_FOR_ZK). Control-script reference still documents SOLR_PORT.

DONE: 2026-10-24T04:49:32Z. Deliverables: `reports/solr-17988-property-audit-round-1.md`, `assignments/solr-17988-property-audit-round-1.md` (status line).
