# Claim: skip-SOLR-10974-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:42:30Z. Remediation redo of batch 10, item 9, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 9, SOLR-10974).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:41:38Z (UTC).

Heartbeat: 2026-10-20T07:41:38Z

Result: 2026-10-20T07:42:30Z: keep-open on solr-10974-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 1a86106018, unchanged verdict; earlier record replaced, which named missing items J and C). Jira read this pass: Open, no resolution, 2 comments. Code read: IndexFetcher.java:1777-1795 (FileFetcher.cleanup throws "Unable to download ... completely" on a size mismatch); :406-416 (only InvalidIndexGenerationException retried); downloadTlogFiles call site not traced line by line. Replication run with live indexing not done.

DONE: 2026-10-20T07:42:30Z. Deliverables: drafts/SOLR-10974/record.md and jira-comment.md on solr-10974-draft-open (1a86106018); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
