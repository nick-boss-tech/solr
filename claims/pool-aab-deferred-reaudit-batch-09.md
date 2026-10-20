# Claim: pool-aab-deferred-reaudit-batch-09

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:27:12Z. Re-audit of the 8 deferred-own-host items in batch 09 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 09).
- Slice: batch 09 deferred items, 8 tickets: SOLR-8681, SOLR-8635, SOLR-8623, SOLR-8572, SOLR-8469, SOLR-8463, SOLR-8365, SOLR-8310.
- Capability tags used: review.
- Started: 2026-10-20T02:25:48Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-09@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 09 (reports/skip-verdict-audit-batch-09.md). Category: skip-queue verdict audit. Siblings: batch 09 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:25:48Z

Result: 2026-10-20T02:27:12Z: tally pass 0, suspect 8, deferred 0. All 8 lack a per-ticket closeout claim and their records say JIRA was not re-read or cite a prior reading. Jira read this pass for all 8 (all Open, no resolution). SOLR-8572 queue note ("resolved") is wrong. Code lines cited were checked at the pin. Report: reports/skip-verdict-audit-batch-09-reaudit.md. The 8 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:27:12Z. Deliverables: reports/skip-verdict-audit-batch-09-reaudit.md; material/skip-audit/remediation-current.json (8 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
