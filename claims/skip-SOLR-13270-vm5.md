# Claim: skip-SOLR-13270-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T03:22:00Z. Remediation redo of batch 07, item 2, under `assignments/pool-aaa-skip-verdict-remediation-07.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-07.md` (item 2, SOLR-13270).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T03:16:40Z (UTC).

Heartbeat: 2026-10-20T03:16:40Z

Result: 2026-10-20T03:22:00Z: close-obsolete on solr-13270-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head 54a23bc46e; verdict changed from keep-open, scoped to the Apache HttpClient client). Jira read this pass: Open, no resolution. Greps at the pin: RequestConfig, 100-continue, expectcontinue return zero lines in *.java; SOLR-17962 (cf7f5079e8) moved the Apache client out of main. The current clients (HttpJdkSolrClient, Http2SolrClient) set no Expect header; a request for them would be a new ticket.

DONE: 2026-10-20T03:22:00Z. Deliverables: drafts/SOLR-13270/record.md and jira-comment.md on solr-13270-draft-close (54a23bc46e); queue-current set; reports/skip-verdict-remediation-07.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
