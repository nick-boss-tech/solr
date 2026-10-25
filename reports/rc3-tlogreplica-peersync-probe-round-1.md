# TestTlogReplica peersync probe (round 1)

Verdict: the peersync hypothesis is ruled out as the cause of the failures in this probe. With a TLOG replica allowed to attempt peersync first, `testKillLeader` and `testRecovery` still fail at seed 9D6BB87C4F145405, with the same messages as the control. The captured state recorded in `reports/rc3-tlogreplica-rootcause-round-1.md` is therefore not explained by the `firstTime` flag alone. The next step is a different hypothesis.

## Scratch change (scaffolding, not a fix candidate)

Base: apache/main `13c693d805f`, scratch worktree `/workspace/worktrees/tlog-probe` (removed after this report). Diff, verbatim:

```diff
diff --git a/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java b/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java
index 38a0d8a0d1e..ebb87fbbf2e 100644
--- a/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java
+++ b/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java
@@ -569,7 +569,7 @@ public class RecoveryStrategy implements Runnable, Closeable {
     }
 
     // we temporary ignore peersync for tlog replicas
-    boolean firstTime = replicaType != Replica.Type.TLOG;
+    boolean firstTime = true;
 
     List<Long> recentVersions;
     try (UpdateLog.RecentUpdates recentUpdates = ulog.getRecentUpdates()) {
```

## Runs (focused, host lock, JDK 21.0.12.1, seed 9D6BB87C4F145405)

| Tree | Methods | Result (from JUnit XML) | Log |
|---|---|---|---|
| scratch (`firstTime = true`) | testKillLeader, testRecovery | 2 tests, 2 failed | `/workspace/smoke-rc3/tlog-probe/scratch-peersync.log` |
| control (unmodified main) | testKillLeader, testRecovery (round-1 main run, same seed) | both failed | `/workspace/smoke-rc3/tlog-repro/main-run1.log` |

Failure messages, identical in both: `testKillLeader`: "Expected collection to be 1x2"; `testRecovery`: "Replica didn't recover".

## Limits

- One seed, and one run per arm. The control is the round-1 run, so it was not re-run in this round.
- The scratch run filtered two methods. In the control run the class ran all 14 tests; the two methods' random state is derived from the same seed, but this was not separately verified.
- The probe changes only the peersync flag. It does not test other TLOG recovery paths.

## Not done

- No fix branch, no PR, no JIRA comment, no dev-list text.
