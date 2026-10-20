# Claim: skip-SOLR-12542-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:03:37Z. Remediation redo of batch 08, item 11, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 11, SOLR-12542).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:01:12Z (UTC).

Heartbeat: 2026-10-20T06:01:12Z

Result: 2026-10-20T06:03:37Z: keep-open on solr-12542-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head d5a82dbadf, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, no comments. Code read: ExportWriter.java:634 (getSortDoc raises the ticket's DocValues error), ExportBuffers.java:97, ExportWriter.java:176-193 and 195-348 (EXCEPTION doc not used for this path; only EOFException caught); TestExportWriter.java:1725-1750 expects the IOException to escape h.query. A live HTTP request at the pin not run, so the client-visible status and body are not established.

DONE: 2026-10-20T06:03:37Z. Deliverables: drafts/SOLR-12542/record.md and jira-comment.md on solr-12542-draft-open (d5a82dbadf); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
