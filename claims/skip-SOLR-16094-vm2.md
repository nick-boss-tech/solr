# Claim: skip-SOLR-16094-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16094, item 10 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 10. Start from `material/skip-audit/remediation-current.json` (item SOLR-16094) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:26:19Z (UTC).

Heartbeat: 2026-10-19T18:26:19Z

Result: 2026-10-19T18:26:19Z: keep-open. Code path at the pinned head: SolrCore.java:775-778 (reload lock), :781-788, :1073 (log point). Not reproduced: the failure is macOS-only; no run on this Linux host. Missing piece: a thread dump from a hung macOS run.

DONE: 2026-10-19T18:26:19Z. Deliverables: `drafts/SOLR-16094/record.md` and `drafts/SOLR-16094/jira-comment.md` on branch `solr-16094-draft-open` (3e19b54051), `material/skip-audit/queue-current.json` (SOLR-16094 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 10).
