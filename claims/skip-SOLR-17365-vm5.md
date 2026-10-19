# Claim: skip-SOLR-17365 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17365); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:35:07Z (UTC).

Heartbeat: 2026-10-19T07:35:07Z

Result: 2026-10-19T07:35:46Z: pr-needed. RAT excludes ui-src/** (rat-sources.gradle:227-228); the first-party css and js under ui-src carry no ASF header. A change is needed; the minified-bundle header is left open.

DONE: 2026-10-19T07:35:46Z. Deliverables: `drafts/SOLR-17365/record.md`, `drafts/SOLR-17365/jira-comment.md` on draft branch `solr-17365-draft-pr` (head `a54f42c8dfce`), and the queue verdict in `material/skip-audit/queue-current.json`.

Correction: : the draft head recorded above is not the branch tip. The record was amended after the claim (file-count sentence). Current head of solr-17365-draft-pr is b9f1789b9769.
