# Claim: pool-aab-deferred-reaudit-batch-07

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:22:53Z. Re-audit of the 15 deferred-own-host items in batch 07 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 07).
- Slice: batch 07 deferred items, 15 tickets: SOLR-9328, SOLR-9312, SOLR-9267, SOLR-9250, SOLR-9248, SOLR-9213, SOLR-9208, SOLR-9206, SOLR-9183, SOLR-9173, SOLR-9144, SOLR-9126, SOLR-9122, SOLR-9094, SOLR-9089.
- Capability tags used: review.
- Started: 2026-10-20T02:20:46Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-07@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 07 (reports/skip-verdict-audit-batch-07.md). Category: skip-queue verdict audit. Siblings: batch 07 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:20:46Z

Result: 2026-10-20T02:22:53Z: tally pass 0, suspect 15, deferred 0. All 15 lack a per-ticket closeout claim, and their records say JIRA was not re-read or cite an earlier round. Jira read this pass for all 15 (all Open, no resolution). Verdict-level notes: close-obsolete greps not in the records for 9248 and 9183 (both hold at the pin); close-fixed 9094 has no regression test. Code lines cited were checked at the pin. Report: reports/skip-verdict-audit-batch-07-reaudit.md. The 15 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:22:53Z. Deliverables: reports/skip-verdict-audit-batch-07-reaudit.md; material/skip-audit/remediation-current.json (15 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
