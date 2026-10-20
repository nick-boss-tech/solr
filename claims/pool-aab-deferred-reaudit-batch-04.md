# Claim: pool-aab-deferred-reaudit-batch-04

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T02:11:21Z. Re-audit of the 14 deferred-own-host items in batch 04 under `assignments/pool-aab-deferred-reaudit-round-1.md`. Excluded host for this slice is vm2, so vm5 may claim it.
- Job or assignment: `assignments/pool-aab-deferred-reaudit-round-1.md` (slice batch 04).
- Slice: batch 04 deferred items, 14 tickets: SOLR-10511, SOLR-10497, SOLR-10407, SOLR-10395, SOLR-10334, SOLR-10326, SOLR-10325, SOLR-10242, SOLR-10201, SOLR-10110, SOLR-10096, SOLR-10041, SOLR-10022, SOLR-10010.
- Capability tags used: review.
- Started: 2026-10-20T02:06:58Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-aab-deferred-reaudit-batch-04@498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket or parent: skip-verdict audit batch 04 (reports/skip-verdict-audit-batch-04.md). Category: skip-queue verdict audit. Siblings: batch 04 audit by vm2. Supersedes: none.

Heartbeat: 2026-10-20T02:06:58Z

Result: 2026-10-20T02:11:21Z: tally pass 0, suspect 14, deferred 0. All 14 lack a per-ticket closeout claim and their records say JIRA was not re-read. Jira read this pass for all 14 (open, no resolution). Verdict-level gaps: SOLR-10407 and SOLR-10326 grep not empty at the pin; SOLR-10022 close-obsolete does not fit (DocValues type-change error still reachable). Code lines cited in the records were checked at the pin. Report: reports/skip-verdict-audit-batch-04-reaudit.md. The 14 are added to material/skip-audit/remediation-current.json as open.

DONE: 2026-10-20T02:11:21Z. Deliverables: reports/skip-verdict-audit-batch-04-reaudit.md; material/skip-audit/remediation-current.json (14 open entries).

Append one heartbeat line at least once an hour while the claim is active, and push it.
