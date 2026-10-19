# Claim: skip-SOLR-13346-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T00:10:00Z. Remediation redo of batch 06, item 20, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 20, SOLR-13346).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T23:50:00Z (UTC).

Heartbeat: 2026-10-19T23:50:00Z

Result: 2026-10-20T00:10:00Z: keep-open on solr-13346-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 1e3c595ba5, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, updated 2019-06-08, no comments. Code read: ExtendedDismaxQParser.java:297-321 (normalClauses built from the whole query; operators skipped), :341-348 (addShingledPhraseQueries). The whole-query phrase behavior is present; per-group phrase is a scoring decision for the maintainers. Parser run not done.

DONE: 2026-10-20T00:10:00Z. Deliverables: drafts/SOLR-13346/record.md and jira-comment.md on solr-13346-draft-open (1e3c595ba5); queue-current set; reports/skip-verdict-remediation-06.md line.
