# Claim: SOLR-5813 and SOLR-12991 dated focused runs, round 2 (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T05:36Z by vm2 (coordinator). Job: gates/solrcloud-dated-runs-round-2.md, items 1 and 2. Item 3 (SOLR-15106) is skipped: covered by claims/pool-vm2-gate-backlog-15106.md.
- Heads read at claim time (ls-remote origin): solr-5813-submit 90b8baa08aefd83cbee7e6e17ca9c59fcc93b30f; solr-12991-submit 1a86966179e1817e4572fb0008a3b82a7c14184f.
- Focused classes: org.apache.solr.cloud.CloudDescriptorTest (solr/core), org.apache.solr.cloud.RecoveryStrategyLeaderUnreachableLogTest (solr/core).
- Runner paths: /workspace/gates/solr-5813-dated and /workspace/gates/solr-12991-dated (detached worktrees); gate script /workspace/gates/g5813-12991-dated-gate.sh.
- No code change. Capability tags: gate. Staffing: 1.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: DONE.
- DONE: 2026-10-13T06:12Z. Item 1 SOLR-5813 GREEN at 90b8baa08a (CloudDescriptorTest 3 of 3, seed 9131063B4B318158). Item 2 SOLR-12991 GREEN at 1a86966179 (RecoveryStrategyLeaderUnreachableLogTest 1 of 1, seed 89E7FC232747D3AC). Item 3 skipped as covered by the 15106 claim. Receipts receipts/SOLR-5813.md and receipts/SOLR-12991.md.
