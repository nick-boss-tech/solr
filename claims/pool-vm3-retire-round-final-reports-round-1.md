# Claim: pool-vm3-retire-round-final-reports-round-1

- Claimant host: vm3, matching a file in `hosts/` (hosts/vm3.md when present; the host id is in ~/.pool-host).
- Status: ACTIVE. Takes over claims/pool-retire-round-final-reports-round-1.md (STALE since 2026-10-17, WORKFLOW.md rule 4).
- Job or assignment: `assignments/pool-retire-round-final-reports-round-1.md`.
- Slice: the whole assignment. Restore the nine packs, the SOLR-18277 receipt record and the index, which the prior claim wrote on the old pr-prepare board (commit 6e0910c561, 2026-10-15) but which never reached coordination. Verify each pack's branch tip against the live fork before the owner step.
- Capability tags used: review.
- Started: 2026-10-18T07:01:19Z (UTC).

Heartbeat: 2026-10-18T07:01:19Z

Bounds: no branch deletions, PR closings, JIRA changes, or pushes to submit branches. No builds on this host.
