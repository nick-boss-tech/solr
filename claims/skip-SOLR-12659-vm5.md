# Claim: skip-SOLR-12659-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T05:44:23Z. Remediation redo of batch 08, item 7, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 7, SOLR-12659).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T05:42:51Z (UTC).

Heartbeat: 2026-10-20T05:42:51Z

Result: 2026-10-20T05:44:23Z: keep-open on solr-12659-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head cfd440e80a, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution. Code read: createMethod and ContentStreamUpdateRequest gone (0 hits); HttpJdkSolrClient.java:302-304 (multipart refused), :316-318 (stream body), :336-337 (all params on the URL when a stream is present). The bug is present in the JDK client; the fix is a design decision; a long-parameter run not done.

DONE: 2026-10-20T05:44:23Z. Deliverables: drafts/SOLR-12659/record.md and jira-comment.md on solr-12659-draft-open (cfd440e80a); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
