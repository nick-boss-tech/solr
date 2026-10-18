# Claim: SOLR-13059 test fix, round 1 (TimeRoutedAliasUpdateProcessorTest)

Status: CLAIMED 2026-10-18T01:54:40Z by vm3 (assignments/pool-solr-13059-fix-round-1.md)

- Claimed by: vm3 (Linux, JDK 21.0.12.1, Gradle 9.7.0). Capability tags: implementation, gate.
- Branch: solr-13696-submit, tip da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9 (verified by ls-remote at claim time). The branch backs PR 5088.
- Scope: test code only, per the assignment. No production change.
- Note: reports/solr-13059-diagnosis-round-1.md, which the assignment cites, is not in the coordination tree. Re-check the diagnosis against the test file before editing, as the assignment asks.
- Heartbeat: 2026-10-18T01:54:40Z (claim).
- 2026-10-18T02:56:04Z: heartbeat. Baseline on the unchanged head (solr-13696-submit da4fa6df11): testPreemptiveCreation with awaitsfix and tests.iters=8 failed 2 of 8 with expected:<3> but was:<4> in concurrentUpdates (log gates/logs/vm3/13059/baseline-preemptive-iters8.log is the first run; the failing assertion came from the Throwable-recording run, step2-preemptive-iters8.log). Local commit in /workspace/worktrees/13059: removes the mid-creation alias snapshot from concurrentUpdates (step 1), records Throwable in the workers (step 2). Not pushed. Gate queued: tidy, Error Prone compile, testPreemptiveCreation x8 with awaitsfix, whole class with awaitsfix; logs gates/logs/vm3/13059/queue-*.log. The diagnosis report the assignment cites is still missing from the tree, and the six seeds it names are unavailable; this run uses seed 13059C0FFEE13059 instead.
- 2026-10-18T03:08:23Z: heartbeat. The gate moved to pool/bin/sweep-run.sh so it takes the host lock that scheduled ticks check. The earlier tidy (EXIT 0, tree clean) and Error Prone compile (EXIT 0) on 21e4407d79 still stand. Stopped the running testPreemptiveCreation step on the old lock and requeued: testPreemptiveCreation x8 with awaitsfix, then the whole class with awaitsfix, both through sweep-run.sh (logs gates/logs/vm3/13059/queue-preemptive-iters8.* and queue-class-awaitsfix.*). The lock name is /workspace/tools/.gradle-localhost.lock on this VM because hostname -s is localhost.
