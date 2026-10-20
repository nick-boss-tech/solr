# Claim: skip-SOLR-11672-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:18:40Z. Remediation redo of batch 10, item 3, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 3, SOLR-11672).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:17:38Z (UTC).

Heartbeat: 2026-10-20T07:17:38Z

Result: 2026-10-20T07:18:40Z: keep-open on solr-11672-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 9b0b97c0e6, unchanged verdict; bulk audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments. Code read: FacetComponent.java:716-745 (countFacets; qf = fi.queryFacets.get(key), qf.count += count at :734, no null check); :844 (removeQueryFacetsUnderLimits called at the end of countFacets); :847-870; :1090-1092 (finishStage, STAGE_GET_FIELDS). The NPE needs a second countFacets call after the removal; sub-request batching not traced. A two-shard run not done.

DONE: 2026-10-20T07:18:40Z. Deliverables: drafts/SOLR-11672/record.md and jira-comment.md on solr-11672-draft-open (9b0b97c0e6); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
