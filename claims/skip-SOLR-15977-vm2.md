# Claim: skip-SOLR-15977-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15977, item 14 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 14. Start from `material/skip-audit/remediation-current.json` (item SOLR-15977) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:43:30Z (UTC).

Heartbeat: 2026-10-19T18:43:30Z

Result: 2026-10-19T18:43:30Z: keep-open. Likely cause (analyzer order, stemmer before synonym) is from a comment; the reporter's schema is not available, so it is not confirmed. Guide example: filters.adoc:2591-2593. Not reproduced.

DONE: 2026-10-19T18:43:30Z. Deliverables: `drafts/SOLR-15977/record.md` and `drafts/SOLR-15977/jira-comment.md` on branch `solr-15977-draft-open` (43203f3e77), `material/skip-audit/queue-current.json` (SOLR-15977 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 14).
