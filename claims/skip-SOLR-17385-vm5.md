# Claim: skip-SOLR-17385 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17385); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:31:08Z (UTC).

Heartbeat: 2026-10-19T07:31:08Z

Result: 2026-10-19T07:31:50Z: pr-needed. Code path at the pinned head: CoordinatorHttpSolrCall.java:74 returns no core for paths not ending in /select. The change is needed; the design is open. No PR opened (PR #5106 touches TestCoordinatorRole.java, the file a PR's test would use).

DONE: 2026-10-19T07:31:50Z. Deliverables: `drafts/SOLR-17385/record.md`, `drafts/SOLR-17385/jira-comment.md` on draft branch `solr-17385-draft-pr` (head `cc101527d7f6`), and the queue verdict in `material/skip-audit/queue-current.json`.

Correction: : the draft head recorded above is not the branch tip. The record was amended after the claim (scope note). Current head of solr-17385-draft-pr is 2793f6f82a4f.
