# Claim: SOLR-10322 re-gate at the live tip (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T11:17Z by vm2. Job: gates/SOLR-10322-tip-regate.md.
- Fork tip read at claim time (ls-remote): 80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff (solr-10322-submit).
- Capability tags: gate. No code change, no fork push.
- Runner: gates/g10322-gate.sh, worktree /workspace/gates/solr-10322-tip (detached), log gates/logs/vm2/10322-*.log.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2 at 80ce9d7a3c8 (queued behind the running SOLR-17539 gate).
