# Claim: SOLR-18431 v2 read endpoint, Fetch Replica Prop (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: CLAIMED.
- Job or assignment: none on the board. Chosen from Jira SOLR-18431 (reporter Eric Pugh, Open, Major, created 2026-09-08, no comments), from the no-PR list. Picked on Nick's go, 2026-10-27.
- Slice: confirm at apache/solr main `e59ce3c97ddb` that no v2 read exists for this resource and what data path exists; draft a Jira comment only. No implementation.
- Capability tags used: review, draft.
- Started: 2026-10-27T01:37:19Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18431@e59ce3c97d.
- Links: Ticket: SOLR-18431; sibling of SOLR-18426/18427 (PRs #4911/#4912 closed without merge) and the SOLR-16392 family.
- Decision ladder: rung 1 (endpoint interfaces and the v1 CLUSTERSTATUS path at the pinned SHA).

Heartbeat: 2026-10-27T01:37:19Z
