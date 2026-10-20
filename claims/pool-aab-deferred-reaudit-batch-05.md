# Claim: pool-aab-deferred-reaudit-batch-05

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:17:42Z. Re-audit of the 20 deferred-own-host items in batch 05 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 05).
- Slice: batch 05 deferred items, 20 tickets: SOLR-9970, SOLR-9965, SOLR-9942, SOLR-9925, SOLR-9913, SOLR-9872, SOLR-9871, SOLR-9866, SOLR-9830, SOLR-9826, SOLR-9825, SOLR-9809, SOLR-9791, SOLR-9769, SOLR-9754, SOLR-9749, SOLR-9741, SOLR-9723, SOLR-9713, SOLR-9677.
- Capability tags used: review.
- Started: 2026-10-20T02:14:40Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-05@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 05 (reports/skip-verdict-audit-batch-05.md). Category: skip-queue verdict audit. Siblings: batch 05 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:14:40Z

Result: 2026-10-20T02:17:42Z: tally pass 0, suspect 20, deferred 0. All 20 lack a per-ticket closeout claim and their records say JIRA was not re-read. Jira read this pass for all 20; SOLR-9749 is now Resolved (Invalid), updated 2026-10-07. Record errors: SOLR-9809 (createFields guard misstated), SOLR-9871 (commit path cited is not a commit path). Verdict-level notes: close-obsolete greps not in the records for 9913, 9872, 9754, 9741 (the greps are empty at the pin); 9749 status changed. Report: reports/skip-verdict-audit-batch-05-reaudit.md. The 20 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:17:42Z. Deliverables: reports/skip-verdict-audit-batch-05-reaudit.md; material/skip-audit/remediation-current.json (20 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
