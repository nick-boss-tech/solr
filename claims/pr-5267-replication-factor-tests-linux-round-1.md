# Claim: SOLR-18603 replication-factor coverage gate, Linux round 1

Status: CLAIMED by vm5, 2026-10-31 05:17 UTC.

- Claimant host: vm5 (Linux 6.1 aarch64, JDK 21.0.12.1+1 at /workspace/jdk/jdk-21.0.12.1+1, Gradle 9.7.0 from each tree's wrapper, GRADLE_USER_HOME /workspace/gradle-home).
- Build lock: /workspace/tools/.gradle-vm5.lock, taken with flock around every Gradle run.
- Tags used: gate, records.
- Final head F: b244ae94d6c84eec06bf1de5754e566b0fdb88d1 (fork branch solr-18603-submit, verified equal before claim). Contributor head O: 7f9acb9df610338c20537e1efd6ed75a6c64a13b. Only solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudHttp2SolrClientTest.java differs between O and F.
- Worktrees: /workspace/worktrees/gate-5267-F (plain clone, because Antora rejects linked worktrees for the root check); /workspace/worktrees/gate-5267-O, gate-5267-M1, gate-5267-M2 (detached linked worktrees).
- Logs: /workspace/gates/logs/vm5/pr-5267-replication-factor-tests-round-1/.
- Implementation claim: claims/pr-5267-replication-factor-tests-windows-round-1.md. Gate job: gates/pr-5267-replication-factor-tests-round-1.md. Receipt will be receipts/PR-5267-replication-factor-tests-round-1.md, written by this claim.
- Public communication: none. No PR/JIRA/comment action, no contributor-branch push.
- Decision ladder: rung 1 for the worktree layout (Antora requirement found on PR 5268, same board); no owner question needed.
