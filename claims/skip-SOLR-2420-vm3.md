# Claim: skip ticket SOLR-2420 (vm3)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: STALE. Duplicate of vm4's closeout for this ticket (claims/skip-SOLR-2420-vm4.md).
- Job or assignment: skip queue record `SOLR-2420` in `material/skip-audit/queue-current.json` (audit verdict confirmed-skip).
- Slice: closeout (steps 11 to 15 of pool/cron-prompt.md): re-check at a pinned head, one verdict per the rubric, draft files on `solr-2420-draft-STATUS`, queue record set.
- Capability tags used: review.
- Started: 2026-10-19T09:36:26Z (UTC).

Heartbeat: 2026-10-19T09:36:26Z

STALE: 2026-10-19T09:38:03Z. Reason: vm4 wrote the keep-open record for SOLR-2420 at 2026-10-19T07:45:50Z (commit 91d40d2c86 on solr-2420-draft-open) before this claim, and its own claim is STALE (claims/skip-SOLR-2420-vm4.md). Two claims on one ticket: stopped here. This host wrote no verdict, pushed no draft (the push was rejected) and did not set the queue record. The queue record for SOLR-2420 still has no verdict; the owner of that draft sets it.
