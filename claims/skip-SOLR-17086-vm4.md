# Claim: skip-SOLR-17086 (remediation, batch 01 item 15)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 15. The earlier vm5 closeout (`claims/skip-SOLR-17086-vm5.md`, DONE) predates this pass and is not reused.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17086); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T14:19:30Z (UTC).

Heartbeat: 2026-10-19T14:19:30Z

Result: 2026-10-19T14:21:28Z: keep-open. The filter NPE is guarded at the pin (`MediaTypeOverridingFilter.java:67-69`). The mapper path passes a possibly null request into `RequestHandlerBase.java:325-327`, which dereferences it unguarded. Reachability not shown; not reproduced. Missing: the full stack trace and a reachability check.

DONE: 2026-10-19T14:21:28Z. Deliverables: `drafts/SOLR-17086/record.md` and `drafts/SOLR-17086/jira-comment.md` on draft branch `solr-17086-draft-open` (head 9a11fc4814); queue verdict `keep-open`; batch line in `reports/skip-verdict-remediation-01.md`.
