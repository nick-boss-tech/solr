# Claim: SOLR-9852 premise run and first gate (round 2, job 1)

Claimant host: vm1. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-9852.md.
Started: 2026-10-11T02:06:49Z (UTC).
Capability tags: gate, premise-run.
Branch: solr-9852-submit at 31f58dbe8e6130c3b58dc35903d7aa3aa542e29b (live tip, verified by ls-remote at claim time). Merge-base: cabedd1d968059215188f4e7563fb303241899ed.
Claimed for vm1 under Nick's 2026-10-10 direction that the local queue take backlog work; the job's intended host vm2 is busy with its round 1 backlog (WORKFLOW.md: any capable Linux host may claim).
Runner on vm1: ~/workspace/tools/gate-9852r.sh (blocking flock on the vm1 test queue). Log: ~/workspace/tools/g9852-gate.log. Worktree: ~/workspace/solr-worktrees/solr-9852-gate.
Focused class: org.apache.solr.client.solrj.io.sql.JdbcTest in :solr:solrj-streaming (copied from the branch's test file). Premise shape: the branch's SOLR-9852-TESTING.md scenario (JdbcTest.testJDBCMethods) run against merge-base production with the branch tests kept, then at the head; the premise holds only if the base run fails for the premise reason (getColumns returns null; getTypeInfo throws UnsupportedOperationException) and the head run passes.
Heartbeat: 2026-10-11T02:06:49Z. Claim pushed; gate worktree, runner and GATE PENDING entry follow on vm1.
