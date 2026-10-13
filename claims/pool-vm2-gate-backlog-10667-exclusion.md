# Claim: SOLR-10667 exclusion branch change and full re-gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T02:33:47Z by vm2. Job: gates/SOLR-10667-exclusion.md.
- Head at claim: 1e3f022552be4d281a020e015f4c8c05ce4eec6c (solr-10667-submit, claim-time tip). Merge-base with main: cabedd1d968059215188f4e7563fb303241899ed.
- Capability tags: gate.
- Runner: gates/g10667-exclusion-gate.sh, worktree /workspace/gates/solr-10667-exclusion (detached). Log: gates/logs/vm2/10667-exclusion-run.out.
- Fork push: plain fast-forward to solr-10667-submit only after the gate is green at the committed tree.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: DONE, GATE GREEN at 9ce9ef1db5e9d33312ac628d2e3f0b236a474c36, 2026-10-13T03:16:00Z. Pushed as a fast-forward to solr-10667-submit; receipts/SOLR-10667.md refreshed; draft pr-drafts/cli/SOLR-10667.md rewritten at the new head.

- 2026-10-13T02:37:38Z vm2 gate backlog coordinator heartbeat: fix commit 9ce9ef1db5 at /workspace/gates/solr-10667-exclusion (local, not pushed; fork push only after green). Runner gates/g10667-exclusion-gate.sh queued behind SOLR-14919 on the vm2 serial queue. Not started.
- 2026-10-13T03:16:00Z vm2 gate backlog coordinator result: GATE GREEN with one recorded check correction (`.gitignore` expected-file row, see gates/SOLR-10667-exclusion.md). Recording finished in this run.
