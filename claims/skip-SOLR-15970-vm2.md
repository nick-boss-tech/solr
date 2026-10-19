# Claim: skip-SOLR-15970-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15970, item 15 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 15. Start from `material/skip-audit/remediation-current.json` (item SOLR-15970) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:48:58Z (UTC).

Heartbeat: 2026-10-19T18:48:58Z

Result: 2026-10-19T18:48:58Z: keep-open (changed from close-fixed). The abort path cleans the RC folder only when import_svn is done (releaseWizard.py:269-283, 481-482). Maven and docker artifacts are not cleaned on abort (releaseWizard.yaml:1105). The cleanup predates the ticket (87c131baa73), so no fix commit follows the report. Not run: the wizard needs staging access.

DONE: 2026-10-19T18:48:58Z. Deliverables: `drafts/SOLR-15970/record.md` and `drafts/SOLR-15970/jira-comment.md` on branch `solr-15970-draft-open` (68461ed77b), `material/skip-audit/queue-current.json` (SOLR-15970 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 15, verdict changed).
