# Claim: dated base-code runs for SOLR-5813, SOLR-12991, SOLR-13186, SOLR-15863 (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T15:19Z by vm2. Job: gates/solrcloud-base-dated-runs-batch-a.md (items 1 to 4).
- Heads confirmed at claim time (ls-remote origin): solr-5813-submit 90b8baa08aefd83cbee7e6e17ca9c59fcc93b30f; solr-12991-submit 1a86966179e1817e4572fb0008a3b82a7c14184f; solr-13186-submit b436d90d2a889cc8511a493a524048ca1543edb5; solr-15863-submit 78dff1d52f6cbb00b5ce04c20c064d478c259eb5.
- Bases (git merge-base upstream/main <head>): 5813 cabedd1d968059215188f4e7563fb303241899ed; 12991, 13186, 15863 97d973814336101e12475558d7419321c743de79.
- Overlays: item 1 CloudDescriptorTest.java; item 2 RecoveryStrategyLeaderUnreachableLogTest.java; item 3 OverseerElectionContextTest.java; item 4 BackupCmdTest.java, BackupCoreAPITest.java, test-framework AbstractIncrementalBackupTest.java (base compile only; the receipt names no class for the base check).
- Runner: gates/g5813-12991-13186-15863-base-gate.sh. Worktrees: /workspace/gates/solr-batcha-base-cabedd (detached at cabedd1d96), /workspace/gates/solr-batcha-base-97d9 (detached at 97d973814336).
- No code change; nothing is committed in the base worktrees and nothing is pushed to any fork.
- Capability tags: gate. Staffing: 1.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED. Queued, not yet run.
