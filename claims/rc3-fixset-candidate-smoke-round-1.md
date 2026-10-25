# Claim: rc3-fixset-candidate-smoke-round-1

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: owner direction, 2026-10-25: all RC3 failures fixed, the fixes applied to RC3, and the candidate run through the smoke tester successfully at least once. Decision ladder applies (`material/decision-ladder.md`).
- Slice: (1) candidate branch `rc3-fixset-candidate` on the fork = RC3 tip `749712b7194` plus the PR commits of #5106 (SOLR-16630), #5098 (SOLR-18530), #5192 (SOLR-18564); (2) local release build from that tree with the tester's documented `assembleRelease`; (3) smoke tester on the `file://` build (`--not-signed --revision skip`); (4) the TLOG (SOLR-18586) and CoreAdmin (SOLR-18587) root causes, by probes, since no fix exists for either.
- Capability tags used: implementation, premise-run, sweep, records. Pushes: the candidate branch on the fork only. No PR, no JIRA or dev-list posts, no vote.
- Started: 2026-10-25T02:47Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-fixset-candidate-smoke-round-1@vm5.
- Links: `reports/rc3-tlogreplica-rootcause-round-1.md`, `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md`, `receipts/rc3-vm5-clean-receipt-round-1.md`. Vote closes 2026-10-27 11:00 UTC. Supersedes: none.

Heartbeat: 2026-10-25T02:47Z
