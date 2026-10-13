# Claim: SOLR-12505 FetchStream join field name escaping fix, gate at the new head (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T20:35Z by vm2. Job: gates/SOLR-12505-fetchstream-fix.md.
- Head at claim: 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (solr-12505-submit). Production delta: FetchStream.java only, plus one new test in StreamDecoratorTest; the fix commits are pushed as a fast-forward after the gate is green.
- Capability tags: implementation (FetchStream and test), gate.
- Runner: gates/g12505-gate.sh, worktree /workspace/gates/solr-12505-gate (detached), log gates/logs/vm2/12505-run.out.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Gate run: head d81a77311773 (the claimant's implementation head), GATE END 2026-10-12T21:45:47Z, log logs/vm2/queue-g12505-gate-20261012T213052Z.out. FAILED: compile leg rc=1 (Error Prone AssertThrowsMultipleStatements at StreamDecoratorTest.java:1033 under -Werror); focused head leg rc=1 (testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams: "Unable to construct instance of FetchStream"); proof leg rc=1 with the same construction error, so the premise is not established. Not relaunched; left for the main agent. Details in gates/SOLR-12505-fetchstream-fix.md.
- Status: FAILED (gate at d81a77311773), not DONE.
- Later run: head ee67d374219275907ef1b78e3d2b455b6a15e43f, GATE END 2026-10-12T23:41:00Z, log logs/vm2/queue-g12505-gate-20261012T232607Z.out. All legs rc=0 except the proof leg, rc=1 by content (expected IOException not thrown on pre-fix FetchStream). Not GREEN under the all-rc-0 rule; left for the main side. Fork not pushed.
- Interrupted run: head ee67d37421, GATE START 2026-10-13T01:00:51Z, log logs/vm2/queue-g12505-gate-20261013T010051Z.out. Compile leg and proof leg completed (proof rc=1 by content, as above); the focused-head leg started 01:12Z and no further output was written, no Gradle process was alive at 01:38Z, and there is no GATE END. Not relaunched: the run executed tests, so the rule requires investigation before any relaunch. Next step for the main agent: decide whether the focused head leg should be re-run at ee67d37421 (vm2 would run it on the main agent's go-ahead).

- 2026-10-13T16Z vm2 gate backlog coordinator: heartbeat at pickup of round 3 (assignments/pool-vm2-gate-backlog-round-3.md, item 1). Proof leg only per amendment 2; run queued as gates/g12505-proof-gate.sh.
