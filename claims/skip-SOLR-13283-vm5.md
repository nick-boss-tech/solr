# Claim: skip-SOLR-13283-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T01:39:14Z. Remediation redo of batch 06, item 25, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 25, SOLR-13283).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T00:15:00Z (UTC).

Heartbeat: 2026-10-20T00:15:00Z

Result: 2026-10-20T01:39:14Z: keep-open on solr-13283-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head bf72b8f191, unchanged verdict; earlier draft replaced). Jira read this pass: Open, no resolution, created 2019-03-01, updated 2019-11-25, one comment (mkhl: patches are welcome). Code read: SimpleFacets.java:904-913 ({!terms} routes to getListedTermCounts), :967-996 (reads facet.sort only), :442-443 (getTermCounts reads FACET_OFFSET and FACET_LIMIT). Explicit term lists ignore facet.limit and facet.offset; whether they should apply is a design question with no decision in the ticket. Reproduction not run.

DONE: 2026-10-20T01:39:14Z. Deliverables: drafts/SOLR-13283/record.md and jira-comment.md on solr-13283-draft-open (bf72b8f191); queue-current set; reports/skip-verdict-remediation-06.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
