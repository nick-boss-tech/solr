# Claim: dated base-code runs for SOLR-5813, SOLR-12991, SOLR-13186, SOLR-15863 (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T15:19Z by vm2. Job: gates/solrcloud-base-dated-runs-batch-a.md (items 1 to 4).
- Heads confirmed at claim time (ls-remote origin): solr-5813-submit 90b8baa08aefd83cbee7e6e17ca9c59fcc93b30f; solr-12991-submit 1a86966179e1817e4572fb0008a3b82a7c14184f; solr-13186-submit b436d90d2a889cc8511a493a524048ca1543edb5; solr-15863-submit 78dff1d52f6cbb00b5ce04c20c064d478c259eb5.
- Bases (git merge-base upstream/main <head>): 5813 cabedd1d968059215188f4e7563fb303241899ed; 12991, 13186, 15863 97d973814336101e12475558d7419321c743de79.
- Overlays: item 1 CloudDescriptorTest.java; item 2 RecoveryStrategyLeaderUnreachableLogTest.java; item 3 OverseerElectionContextTest.java; item 4 BackupCmdTest.java, BackupCoreAPITest.java, test-framework AbstractIncrementalBackupTest.java (base compile only; the receipt names no class for the base check).
- Runner: gates/g5813-12991-13186-15863-base-gate.sh. Worktrees: /workspace/gates/solr-batcha-base-cabedd (detached at cabedd1d96), /workspace/gates/solr-batcha-base-97d9 (detached at 97d973814336).
- No code change; nothing is committed in the base worktrees and nothing is pushed to any fork.
- Capability tags: gate. Staffing: 1.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Results (heads confirmed unchanged at run time):
  - Item 1, SOLR-5813 on base cabedd1d96: rc=1; CloudDescriptorTest tests=3 failures=1; testEmptyCollectionNameDefaultsToCoreName (expected:<core1> but was:<>); seed D30D1CE43814160A; run 2026-10-13 15:32Z.
  - Item 2, SOLR-12991 on base 97d9738143: rc=1; RecoveryStrategyLeaderUnreachableLogTest tests=1 failures=1; testLeaderConnectFailureLogsRootCause; seed 1D24E02231DFF0EA; run 15:39Z.
  - Item 3, SOLR-13186 on base 97d9738143: rc=1; OverseerElectionContextTest tests=1 failures=1; testLeaderNodeRemovedWhenOverseerIsNotStarted ("leader registration must not outlive a skipped Overseer start"); seed 4CF3601BF5D59D9B; run 15:43Z.
  - Item 4, SOLR-15863 on base 97d9738143: compile rc=1; BackupCmdTest uses minIndexVersion(List<String>), which does not exist on base (cannot find symbol); run 15:45Z.
- Status: DONE 2026-10-13T15:50Z. Deliverables: receipts/SOLR-5813.md, receipts/SOLR-12991.md, receipts/SOLR-13186.md, receipts/SOLR-15863.md (base-run sections), gates/solrcloud-base-dated-runs-batch-a.md. All four base results match the job's expected outcomes.
