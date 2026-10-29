# Claim: SolrJ V2Request default parser wire capture (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/solr-v2request-default-parser-round-1.md`, question 1 (what a default V2Request sends).
- Slice: capture the raw request for the default and explicit-javabin parser with a local listener; no server, no gate, no code change.
- Capability tags used: none beyond the probe.
- Started: 2026-10-29T16:00:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-v2request-default-parser-wire@vm6.
- Links: Ticket or parent: SOLR-16398 (source of the observation). Category: see `pool/work-map.md`. Siblings: none. Supersedes: none.
- Constraints: scratch directory `/workspace/tmp/v2probe` only; no JIRA or GitHub post, no PR; no branch commits.

Heartbeat: 2026-10-29T16:00:00Z

Result: pending.
