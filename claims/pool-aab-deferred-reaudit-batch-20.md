# Claim: pool-aab-deferred-reaudit-batch-20

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T03:07:19Z. Re-audit of the 12 deferred-own-host items in batch 20 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 20).
- Slice: batch 20 deferred items, 12 tickets: SOLR-3201, SOLR-3124, SOLR-3064, SOLR-3050, SOLR-3034, SOLR-3027, SOLR-3023, SOLR-2976, SOLR-2865, SOLR-2845, SOLR-2751, SOLR-2507.
- Capability tags used: review.
- Started: 2026-10-20T03:05:39Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-20@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 20 (reports/skip-verdict-audit-batch-20.md). Category: skip-queue verdict audit. Siblings: batch 20 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T03:05:39Z

Result: 2026-10-20T03:07:19Z: tally pass 0, suspect 12, deferred 0. All 12 lack a per-ticket closeout claim and their records say JIRA was not re-read or cite a prior reading. Jira read this pass for all 12 (all Open, no resolution). Code lines cited were checked at the pin. Close-obsolete greps not in the records for 3201 and 3064 (both hold at the pin). Report: reports/skip-verdict-audit-batch-20-reaudit.md. The 12 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T03:07:19Z. Deliverables: reports/skip-verdict-audit-batch-20-reaudit.md; material/skip-audit/remediation-current.json (12 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
