Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-14187.md.
Capability tags: premise-run, gate. Staffing: 1.
Started: 2026-10-11T03:53:23Z (UTC).
Branch: solr-14187-submit at 45b0f7ce34f802d0568c499bb3e5357f41be77ec (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T03:53:23Z.

Heartbeat: 2026-10-11T03:59:14Z. Premise run starting: worktrees /workspace/gates/solr-14187 (head) and /workspace/gates/solr-14187-proof (merge-base production, scratch vehicle ScratchSolr14187BaseDropTest named in gates/SOLR-14187.md). Runner /workspace/gates/g14187-premise.sh, logs /workspace/gates/logs/vm2/14187-premise-*.log.

Result 2026-10-11T04:19:06Z: premise HOLDS (base scratch vehicle shows the static helper dropping the user; head branch test carries alice on poll and delete). Gate steps green at packaged head 29023ec1ecabc2cd0a87675bdcdccf5d71e719b8 (changelog OK, tidy clean, compileTestJava rc 0, focused CollectionAdminRequestAsyncAuthTest tests=2 failures=0 from fresh XML, solrj check rc 0).
BLOCKED ON PROOF LEG: the branch test does not compile against merge-base production, so no branch test fails there. Job stopped per protocol; main agent decides. Receipt receipts/SOLR-14187.md. Claim DONE with outcome BLOCKED.
DONE: 2026-10-11T04:20:24Z (UTC), outcome BLOCKED ON PROOF LEG (main agent decision on proof shape).
Follow-up 2026-10-11 (UTC): main-side decision made the gate GREEN; packaged head 29023ec1ecabc2cd0a87675bdcdccf5d71e719b8 pushed to solr-14187-submit on the fork (fast-forward from 45b0f7ce34f8). Outcome GREEN. Receipt receipts/SOLR-14187.md.
