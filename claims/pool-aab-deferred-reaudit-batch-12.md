# Claim: pool-aab-deferred-reaudit-batch-12

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:38:20Z. Re-audit of the 11 deferred-own-host items in batch 12 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 12).
- Slice: batch 12 deferred items, 11 tickets: SOLR-7249, SOLR-7221, SOLR-7192, SOLR-7153, SOLR-7088, SOLR-7009, SOLR-6934, SOLR-6929, SOLR-6922, SOLR-6903, SOLR-6890.
- Capability tags used: review.
- Started: 2026-10-20T02:36:43Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-12@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 12 (reports/skip-verdict-audit-batch-12.md). Category: skip-queue verdict audit. Siblings: batch 12 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:36:43Z

Result: 2026-10-20T02:38:20Z: tally pass 0, suspect 11, deferred 0. All 11 lack a per-ticket closeout claim and their records say JIRA was not re-read or cite a prior reading. Jira read this pass for all 11 (all Open, no resolution). Verdict-level notes: close-obsolete greps not in the records for 7221 and 6890 (both hold at the pin). Code lines cited were checked at the pin. Report: reports/skip-verdict-audit-batch-12-reaudit.md. The 11 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:38:20Z. Deliverables: reports/skip-verdict-audit-batch-12-reaudit.md; material/skip-audit/remediation-current.json (11 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
