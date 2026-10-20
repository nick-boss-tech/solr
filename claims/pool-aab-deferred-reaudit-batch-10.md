# Claim: pool-aab-deferred-reaudit-batch-10

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:33:07Z. Re-audit of the 19 deferred-own-host items in batch 10 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 10).
- Slice: batch 10 deferred items, 19 tickets: SOLR-8171, SOLR-8158, SOLR-8117, SOLR-8106, SOLR-8098, SOLR-8089, SOLR-8087, SOLR-8079, SOLR-8076, SOLR-8024, SOLR-7998, SOLR-7992, SOLR-7982, SOLR-7973, SOLR-7953, SOLR-7947, SOLR-7867, SOLR-7846, SOLR-7806.
- Capability tags used: review.
- Started: 2026-10-20T02:30:41Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-10@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 10 (reports/skip-verdict-audit-batch-10.md). Category: skip-queue verdict audit. Siblings: batch 10 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:30:41Z

Result: 2026-10-20T02:33:07Z: tally pass 0, suspect 19, deferred 0. All 19 lack a per-ticket closeout claim and their records say JIRA was not re-read or cite a prior reading. Jira read this pass for all 19 (all Open, no resolution). Verdict-level notes: close-obsolete greps not in the records for 8158, 8117, 8089, 8087, 8076, 7973 (all hold at the pin). Code lines cited were checked at the pin. Report: reports/skip-verdict-audit-batch-10-reaudit.md. The 19 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:33:07Z. Deliverables: reports/skip-verdict-audit-batch-10-reaudit.md; material/skip-audit/remediation-current.json (19 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
