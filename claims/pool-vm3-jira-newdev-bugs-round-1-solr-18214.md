# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-18214 (item 15 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED. Slice: item 15 only, SOLR-18214. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 15.
- Base: apache/main at `c8ab71430f`.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T20:45Z (UTC).

Heartbeat: 2026-10-19T20:45Z

Result (vm3, 2026-10-19T20:55Z): stopped, no branch. An omitted `baseConfigSet` on v2 create is rejected, and v1 deliberately maps it to `_default`. The ticket's "truly optional" needs a defined result (default vs create-from-scratch with a minimum file set), which is an owner decision. Deliverable: the SOLR-18214 section in `reports/2026-10-19-jira-newdev/A-bugs-round-1.md`.

DONE: 2026-10-19 by vm3, stopped for an owner decision. Heartbeat: 2026-10-19T20:55Z
