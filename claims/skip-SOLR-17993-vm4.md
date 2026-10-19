# Claim: skip-SOLR-17993 (closeout)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout verdict close-obsolete, recorded in `material/skip-audit/queue-current.json` at pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17993); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T06:38:00Z (UTC).

Heartbeat: 2026-10-19T06:38:00Z

Result: 2026-10-19T06:50:00Z: close-obsolete. The greps at the pinned head are empty: `CLOUD_CORE_PATTERN` in `solr/core/src/java`, and the `metrics/prometheus` package. Conclusive for the removal; the reopen condition is in the record.

DONE: 2026-10-19T06:50:00Z. Deliverables: `drafts/SOLR-17993/record.md`, `drafts/SOLR-17993/jira-comment.md` on draft branch `solr-17993-draft-close` (head `pending`, see the draft branch).
