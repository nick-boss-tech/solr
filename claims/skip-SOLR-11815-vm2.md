# Claim: skip-SOLR-11815-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (close-fixed, changed from the recorded keep-open). Remediation item 17 of `assignments/pool-aaa-skip-verdict-remediation-09.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-09.md` (batch 9, item 17), claim `claims/pool-aaa-skip-verdict-remediation-09.md`.
- Slice: SOLR-11815 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:35:10Z (UTC).
- Links: Ticket: SOLR-11815 (linked SOLR-11920, closed). Category: Replication and recovery. Siblings: SOLR-11920 commits fbdf7fa736, 72ee0f64d4. Supersedes: the recorded keep-open verdict.
- Idempotency key: SKIP-SOLR-11815@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T07:35:10Z

Result: 2026-10-20T07:35:10Z: close-fixed at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-11815-draft-close at 265f2f1ca2 (drafts/SOLR-11815/record.md, jira-comment.md). Fix fbdf7fa736 (SOLR-11920, differential copy) is an ancestor; doDifferentialCopy present at IndexFetcher.java:1132-1164. Limit: isIndexStale still sets the full-copy flag (IndexFetcher.java:617).

DONE: 2026-10-20T07:35:10Z. Deliverables: drafts/SOLR-11815/record.md and jira-comment.md on solr-11815-draft-close; material/skip-audit/queue-current.json (close-fixed); line in reports/skip-verdict-remediation-09.md.
