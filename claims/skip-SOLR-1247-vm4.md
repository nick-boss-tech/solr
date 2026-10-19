# Claim: skip-SOLR-1247 (closeout)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout verdict keep-open, recorded in `material/skip-audit/queue-current.json` at pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-1247); overnight step 6a. Picked from the end of the queue order to avoid a race with other hosts.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:12:00Z (UTC).

Heartbeat: 2026-10-19T07:12:00Z

Result: 2026-10-19T07:30:00Z: keep-open. The audit's citation (StatsField.java:439) covers only multi-valued fields; single-valued fields still use a per-segment value source (StatsField.java:441, 462-475), so the audit's "1.4 behaviour is gone" is too broad. No fixing commit identified. Missing piece: a memory measurement on a current release. Conclusive for the code path; not for memory.

DONE: 2026-10-19T07:30:00Z. Deliverables: `drafts/SOLR-1247/record.md`, `drafts/SOLR-1247/jira-comment.md` on draft branch `solr-1247-draft-open`, and the queue verdict.
