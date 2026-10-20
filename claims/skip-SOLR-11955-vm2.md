# Claim: skip-SOLR-11955-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (keep-open, changed from the recorded close-obsolete). Remediation item 9 of `assignments/pool-aaa-skip-verdict-remediation-09.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-09.md` (batch 9, item 9), claim `claims/pool-aaa-skip-verdict-remediation-09.md`.
- Slice: SOLR-11955 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:32:40Z (UTC).
- Links: Ticket: SOLR-11955. Category: Analysis and schemas. Siblings: none known. Supersedes: the recorded close-obsolete verdict (contradicted: text_en_splitting query analyzer chains synonymGraph into wordDelimiterGraph in both shipped configsets).
- Idempotency key: SKIP-SOLR-11955@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T06:32:40Z

Result: 2026-10-20T06:32:40Z: keep-open at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-11955-draft-open (drafts/SOLR-11955/record.md, jira-comment.md). Shipped text_en_splitting query chain at managed-schema.xml (_default and sample_techproducts_configs) contains synonymGraph then wordDelimiterGraph. Query run not done.

DONE: 2026-10-20T06:32:40Z. Deliverables: drafts/SOLR-11955/record.md and jira-comment.md on solr-11955-draft-open; material/skip-audit/queue-current.json (keep-open); line in reports/skip-verdict-remediation-09.md.
