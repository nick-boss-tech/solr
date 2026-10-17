# Claim: PR drift round 1, run records, items (a) to (d) (vm2, gate backlog coordinator)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job gates/pr-drift-round-1-run-records.md: Status: UNCONFIRMED (see the census note)

- Claimed: 2026-10-13T09:05Z by vm2. Job: gates/pr-drift-round-1-run-records.md. All four items, one runner.
- Heads and merge-bases at claim (merge-base with upstream main):
  - (a) solr-18280-submit 0da92abd9e96616ed817a25da47078c753766119, merge-base 0d2a4649c79a404e9f4aa1bfe26b88c3bb37407c. No production delta, so the base leg has the head's production by construction; both legs still run.
  - (b) solr-7394-recovery 9857d9ee80297784bd7dea9968a5943fe4d7a3e6, merge-base 56ec140e3636d5f4150fa87fbf7103529536ac99. Production delta: RecoveryStrategy.java, ShardTerms.java, ZkShardTerms.java, TestInjection.java.
  - (c) solr-18178-verify b75e7d4d3c464ceeb9f14fdaf7711212adcccac2, merge-base 0d2a4649c79a404e9f4aa1bfe26b88c3bb37407c.
  - (d) solr-11431-core-init-503 968fad873c4b35fff0605ae88dbecca16d1dfa2f (head run only).
- Base legs: the branch's test tree stays at head; non-test production files are reverted to the merge-base version (added files removed). No branch change and no push to any branch.
- Capability tags: gate (record runs only).
- Runner: gates/g-prdrift-gate.sh, worktree /workspace/gates/prdrift-records (detached), logs gates/logs/vm2/prdrift-*.log and XML copies under gates/logs/vm2/prdrift-xml/.
- Status: CLAIMED, not DONE. Base-leg failures are expected by content and are recorded, not relaunched.
- 2026-10-13T12:55Z run: item (a) head leg rc=1 (TestReplicationHandler 1 of 25 failed, doTestIndexAndConfigAliasReplication timed out); not relaunched, left for the main agent. Items (b) and (c) base legs recorded; head legs and item (d) not yet run. See gates/pr-drift-round-1-run-records.md, coordinator run record.
- Failure, 2026-10-13T09:45Z: item (b) base leg `7394-base` rc=1 at compileTestJava (head test tree does not compile against base production; no tests ran). Recorded in the job file; not relaunched; left for the main side.
- Failure, 2026-10-13T09:50Z: item (c) base leg `18178-base` rc=1 at compileTestJava (24 errors; no tests ran). Recorded in the job file; not relaunched; left for the main side.

- 2026-10-13T18:49Z vm2 gate backlog coordinator: PARKED for the main agent; no runner live. Item (a): the SOLR-18280 record run passed the head at 25 of 25 (0da92abd9e96, seed 18280C0FFEE18280; receipts/SOLR-18280.md), which records the 12:55Z head failure as a flake. Items (b) and (c) need the main side to quote the sentences named for cutting (receipts/SOLR-7394.md, receipts/SOLR-18178.md). Item (d) recorded (receipts/SOLR-11431.md). Not relaunched; the claim stays with vm2 until the main side decides.
