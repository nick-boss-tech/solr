# Claim: SOLR-9155 declared InterruptedException route, gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T02:46Z by vm2. Job: gates/SOLR-9155-declared-route-gate.md.
- Head at claim: dbd88c84ff246631875afc010c1c4dff4e50c28a (solr-9155-submit). Merge-base 97d973814336101e12475558d7419321c743de79. The change is already on the head (three files: changelog, ZkController.java, ZkControllerGetLeaderTest.java); no code change in this claim.
- Capability tags: gate.
- Runner: gates/g9155-gate.sh, worktree /workspace/gates/solr-9155-gate (detached), log gates/logs/vm2/9155-*.log.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2.

- 2026-10-13T03:37Z vm2 gate backlog coordinator heartbeat: gate running under the vm2 serial queue (pid 4757, started 03:16Z, log logs/vm2/queue-g9155-gate-20261013T031600Z.out, no GATE END yet). Changelog parse, tidy, Error Prone compile and focused head run (ZkControllerGetLeaderTest, log logs/vm2/9155-head.log, BUILD SUCCESSFUL) have passed; the module check is next.
- Premise-base leg defect (recorded 03:37Z): the runner's checkout path `solr/core/src/main/java/...` does not exist in the tree (the path is `solr/core/src/java/...`). The base swap failed with a pathspec error, so logs/vm2/9155-premise-base.log compiled the head and reports BUILD SUCCESSFUL. That log is not a premise record. The premise compile is re-run with the correct path as a separate step after GATE END; the gate is not GREEN until that is recorded.

- 2026-10-13T03:47Z vm2 gate backlog coordinator: GATE END 03:36:27Z. Steps green (changelog, tidy, Error Prone compile, focused head, module check; all rc=0). Gate NOT GREEN: the premise-base leg checked the wrong path and is void. Premise-only re-run queued at the front of gates/queue (gates/g9155-premise-rerun.sh, log logs/vm2/9155-premise-base-rerun.log), starts after SOLR-17680. Claim stays CLAIMED until the premise compile message is recorded and the receipt is written.
