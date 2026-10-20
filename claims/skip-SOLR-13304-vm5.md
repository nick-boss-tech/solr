# Claim: skip-SOLR-13304-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T00:20:00Z. Remediation redo of batch 06, item 23, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 23, SOLR-13304).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T00:05:00Z (UTC).

Heartbeat: 2026-10-20T00:05:00Z

Result: 2026-10-20T00:20:00Z: close-obsolete on solr-13304-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head bcdf8498d3, unchanged verdict; B2 record replaced). Jira read this pass: Open, no resolution, updated 2019-06-08. Removal: 3a33fa7009 (SOLR-17631, SOLR-17839, ancestor) deletes PreAnalyzedField.java, JsonPreAnalyzedParser.java, SimplePreAnalyzedParser.java, PreAnalyzedUpdateProcessorFactory.java; upgrade note major-changes-in-solr-10.adoc:298. Grep at pin: preanalyzed in solr excluding ref guide, code 0 (one Javadoc phrase at SortableTextField.java:38, not a use). Not a rename.

DONE: 2026-10-20T00:20:00Z. Deliverables: drafts/SOLR-13304/record.md and jira-comment.md on solr-13304-draft-close (bcdf8498d3); queue-current set; reports/skip-verdict-remediation-06.md line.
