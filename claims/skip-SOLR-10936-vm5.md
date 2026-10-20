# Claim: skip-SOLR-10936-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:59:27Z. Remediation redo of batch 10, item 13, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 13, SOLR-10936).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:58:40Z (UTC).

Heartbeat: 2026-10-20T07:58:40Z

Result: 2026-10-20T07:59:27Z: keep-open on solr-10936-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head dce5fea4e0, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, 6 comments. Failing frame is Lucene (external; libs.versions.toml:39 apache-lucene 10.4.0), not in this tree; close-obsolete not applicable since the code was never here. Solr wrapper DirectUpdateHandler2.java:419-445 present. No Solr fix commit. Reproduction on Lucene 10.4.0 not run.

DONE: 2026-10-20T07:59:27Z. Deliverables: drafts/SOLR-10936/record.md and jira-comment.md on solr-10936-draft-open (dce5fea4e0); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
