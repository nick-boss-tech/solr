# Claim: skip-SOLR-17086 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17086); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:51:28Z (UTC).

Heartbeat: 2026-10-19T07:51:28Z

Result: 2026-10-19T07:52:10Z: close-fixed. The NPE site (c058ea5dac^ MediaTypeOverridingFilter.java:68, unguarded getParams) is guarded at the pinned head (line 69). Fixing commit c058ea5dac is an ancestor of the pinned head; its subject names SOLR-15751, not this ticket.

DONE: 2026-10-19T07:52:10Z. Deliverables: `drafts/SOLR-17086/record.md`, `drafts/SOLR-17086/jira-comment.md` on draft branch `solr-17086-draft-close` (head `a210672ca482`), and the queue verdict in `material/skip-audit/queue-current.json`.

Revised: 2026-10-19T07:55:42Z: verdict changed from close-fixed to keep-open. Problem 1 (backup NPE) is fixed by c058ea5dac. Problem 2 (ADDREPLICA, same 500) is not covered: CreateReplica is an annotated v2 API, and the Jersey media-type filter does not run for it. Draft branch solr-17086-draft-close is superseded and kept; the record there says so. New draft: solr-17086-draft-open at `fdf29b716508`.
