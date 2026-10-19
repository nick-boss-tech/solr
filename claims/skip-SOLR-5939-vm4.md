# Claim: skip-SOLR-5939 (closeout)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout verdict pr-needed, recorded in `material/skip-audit/queue-current.json` at pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-5939); overnight step 6a. Picked from the end of the queue order, skipping tickets with a draft branch already on the remote.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T08:15:00Z (UTC).

Heartbeat: 2026-10-19T08:15:00Z

Result: 2026-10-19T08:30:00Z: pr-needed. Code path read at the pinned head: `StreamingSolrClients.java:66-78` (one cached client per node, built with the first request) and `:131-146` (errors recorded against that request). Confirmed from the code, not by a test. The fork branch `solr-5939-submit` has two unreviewed commits by Nick (2026-10-07, 2026-10-09) that touch this area; not reviewed here. The queue note "fixed in 4.9/6.0" is not supported by the code.

DONE: 2026-10-19T08:30:00Z. Deliverables: `drafts/SOLR-5939/record.md`, `drafts/SOLR-5939/jira-comment.md` on draft branch `solr-5939-draft-pr` (head `07e223e1e9`), and the queue verdict.
