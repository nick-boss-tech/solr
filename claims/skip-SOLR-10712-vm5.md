# Claim: skip-SOLR-10712-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T08:43:33Z. Remediation redo of batch 10, item 24, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 24, SOLR-10712).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T08:42:38Z (UTC).

Heartbeat: 2026-10-20T08:42:38Z

Result: 2026-10-20T08:43:33Z: close-fixed on solr-10712-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head 24cd829def; unchanged verdict; earlier record replaced, now checks the writer at the pin). Jira read this pass: Open, no resolution, 1 comment (2022-10-14). Fix 78862e7f54 (SOLR-18351, 2026-09-02) is an ancestor of the pin (checked). Changed code present: JavaBinRequestWriter.java:34-50 (marshal to the output stream); no buffered ContentStream getContentStream on UpdateRequest in solrj request package; UpdateRequest.java:349 setDocIterator present. Linkage by code path; memory not measured.

DONE: 2026-10-20T08:43:33Z. Deliverables: drafts/SOLR-10712/record.md and jira-comment.md on solr-10712-draft-close (24cd829def); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
