# Claim: skip-SOLR-2420 (closeout)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: STALE. Duplicate: a keep-open skip record for SOLR-2420 was already pushed to `solr-2420-draft-open` at 2026-10-19T07:45:50Z (commit `91d40d2c86`), before this claim, by another host. That branch is not overwritten here, and this host recorded no queue verdict.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-2420); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T08:00:18Z (UTC).

Heartbeat: 2026-10-19T08:00:18Z

Result: at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`, `TermVectorComponent.java:81` (the `termVectors` key), `:134-135` (the NamedList added under it), and `:165`, `:179` (the warnings path). The existing draft says it was not re-checked at the pin; this host's reading agrees with its verdict. Not written to the branch.

STALE: 2026-10-19T08:02:00Z. Reason: duplicate of the draft on `solr-2420-draft-open`; the queue verdict and any draft update are left to whoever owns that draft.
