# Claim: skip-SOLR-12460-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:17:25Z. Remediation redo of batch 08, item 14, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 14, SOLR-12460).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:15:51Z (UTC).

Heartbeat: 2026-10-20T06:15:51Z

Result: 2026-10-20T06:17:25Z: keep-open on solr-12460-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 8a2bfb7f8d, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments; full text read from the Jira REST record. Code read: FacetProcessor.java:238-243 (handleJoinField); FacetRequest.java:260-273 (JoinField.createDomainQuery: inner WrappedQuery setCache(false), outer join query still given to getDocSet); WrappedQuery.java:60-69; DocSetQuery.java:57-67 (key depends on base docset); JoinQParserPlugin.java:249-263. No fix commit at the pin. A multi-shard run not done; whether to cache base-dependent join domains is a design question.

DONE: 2026-10-20T06:17:25Z. Deliverables: drafts/SOLR-12460/record.md and jira-comment.md on solr-12460-draft-open (8a2bfb7f8d); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
