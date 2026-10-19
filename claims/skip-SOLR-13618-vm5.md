# Claim: skip-SOLR-13618-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T22:55:00Z. Remediation redo of batch 06, item 6, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 6, SOLR-13618).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:40:00Z (UTC).

Heartbeat: 2026-10-19T22:40:00Z

Result: 2026-10-19T22:55:00Z: keep-open on solr-13618-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 797c3dcb3c, unchanged verdict). Jira read this pass: Open, no resolution, updated 2019-12-21. Part 1 (instanceDir outside SOLR_HOME rejected) present: CoreContainer.java:1525, SolrPaths.java:65-79, CreateCore.java:102 and :109-117, added by 936b9d770e (SOLR-14561, ancestor). Part 2 (child or ancestor containing core.properties) absent: CorePropertiesLocator reads only the instance dir; no commit names SOLR-13618. Build and TestCoreContainer run not done.

DONE: 2026-10-19T22:55:00Z. Deliverables: drafts/SOLR-13618/record.md and jira-comment.md on solr-13618-draft-open (797c3dcb3c); queue-current set; reports/skip-verdict-remediation-06.md line.
