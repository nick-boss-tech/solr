# Claim: skip-SOLR-11659-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:22:35Z. Remediation redo of batch 10, item 4, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 4, SOLR-11659).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:21:38Z (UTC).

Heartbeat: 2026-10-20T07:21:38Z

Result: 2026-10-20T07:22:35Z: close-obsolete on solr-11659-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head 46f7f18670, unchanged verdict; earlier record and its non-re-run grep replaced). Jira read this pass: Open, no resolution, 0 comments. Named classes JmxObjectNameFactory, JmxMetricsReporter, onTimerAdded and reporters.jmx references: 0 hits in git grep -- solr at the pin. Removed by 3d35f4defe (SOLR-17827, "Remove JXM, SLF4J and Graphite reporters"), an ancestor of the pin (checked). Remaining createName methods unrelated (CorePropertiesLocator.java:235, ErrorLogMuter.java:75).

DONE: 2026-10-20T07:22:35Z. Deliverables: drafts/SOLR-11659/record.md and jira-comment.md on solr-11659-draft-close (46f7f18670); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
