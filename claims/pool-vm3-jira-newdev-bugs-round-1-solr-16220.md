# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-16220 (item 11 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED. Slice: item 11 only, SOLR-16220. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 11.
- Base: apache/main at `c8ab71430f`.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T20:25Z (UTC).

Heartbeat: 2026-10-19T20:25Z

Result (vm3, 2026-10-19T20:45Z): not reproduced for the documented case on `c8ab71430f`. Probe passed with the destination built (`/workspace/gates/logs/vm3/solr-16220-repro/`). The non-SSL `SSLConfig` variant (null factory) is the candidate for the ticket's NPE and is left to the owner. No branch. Deliverable: the SOLR-16220 section in `reports/2026-10-19-jira-newdev/A-bugs-round-1.md`.

DONE: 2026-10-19 by vm3, not reproduced. Heartbeat: 2026-10-19T20:45Z
