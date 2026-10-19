# Claim: skip-SOLR-15801-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15801, item 18 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 18. Start from `material/skip-audit/remediation-current.json` (item SOLR-15801) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:08:06Z (UTC).

Heartbeat: 2026-10-19T19:08:06Z

Result: 2026-10-19T19:08:06Z: close-fixed (recorded verdict was close-answered; the docs fix is the stronger rubric match). Fix commit bea30b959f9 (2022-04-08, after the ticket was filed), ancestor of the pin. Page system-requirements.adoc:30 states Java 21; old 2019 list absent (count 0).

DONE: 2026-10-19T19:08:06Z. Deliverables: `drafts/SOLR-15801/record.md` and `drafts/SOLR-15801/jira-comment.md` on branch `solr-15801-draft-close` (c32b2ac5e1), `material/skip-audit/queue-current.json` (SOLR-15801 close-fixed on that branch), `reports/skip-verdict-remediation-02.md` (row 18, verdict label changed from close-answered; branch suffix unchanged).
