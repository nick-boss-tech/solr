# Claim: skip-SOLR-10952-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:55:35Z. Remediation redo of batch 10, item 12, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 12, SOLR-10952).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:54:39Z (UTC).

Heartbeat: 2026-10-20T07:54:39Z

Result: 2026-10-20T07:55:35Z: keep-open on solr-10952-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head b806f8db02, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, 4 comments; link SOLR-10824 (Resolved). Code read: SolrIndexSearcher.java:1567-1578 (query result cache lookup keyed without stats mode), :744-760 (regenerator); DocSetQuery-free key; ExactStatsCache.java:61, ExactSharedStatsCache.java:37 (no cache bypass). TestExactSharedStatsCache exists at the pin; not run.

DONE: 2026-10-20T07:55:35Z. Deliverables: drafts/SOLR-10952/record.md and jira-comment.md on solr-10952-draft-open (b806f8db02); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
