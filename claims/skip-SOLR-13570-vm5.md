# Claim: skip-SOLR-13570-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T23:20:00Z. Remediation redo of batch 06, item 10, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 10, SOLR-13570).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T23:08:00Z (UTC).

Heartbeat: 2026-10-19T23:08:00Z

Result: 2026-10-19T23:20:00Z: close-fixed on solr-13570-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head af05ec7c80, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, updated 2019-07-26. Fixing commit a80eb84d56 (SOLR-14354, ancestor): removes getDefaultHttpClient calls; call sites present at pin (OverseerNodePrioritizer.java:140, CollectionHandlingUtils.java:532); HttpShardHandlerFactory.java:292-310 reads timeouts from config. Greps at pin: getDefaultHttpClient empty over solr; socketTimeoutMillis in Overseer paths empty. The earlier record's owner-decision basis has no written decision in coordination files and is withdrawn; the verdict rests on this evidence.

DONE: 2026-10-19T23:20:00Z. Deliverables: drafts/SOLR-13570/record.md and jira-comment.md on solr-13570-draft-close (af05ec7c80); queue-current set; reports/skip-verdict-remediation-06.md line.
