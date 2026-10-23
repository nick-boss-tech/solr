🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

**A failed election watch is retried until the stack overflows.** When a replica is not the leader, `LeaderElector.checkIfIamLeader` sets a watch on the election node just ahead of it. If setting that watch fails with a `KeeperException` other than `NoNode`, the method calls itself again at once ([line 161](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/LeaderElector.java#L161)). Nothing limits the repeats and nothing waits between them. A failure that does not go away, such as the `NoAuth` error in the [2025 report on SOLR-6213](https://issues.apache.org/jira/browse/SOLR-6213), ends in a `StackOverflowError`.

**The retry now stops after three attempts.** The third failure is thrown to the caller, so a join sees the real `KeeperException` and the log shows it. The `NoNode` case, where the node ahead disappeared, still checks again at once, because the next check finds a new node to watch. The limit is `LeaderElector.MAX_WATCH_FAILURES`.

**Proof.** `LeaderElectionTest.testRepeatedWatchFailureIsRaisedInsteadOfRetriedForever` makes every `getData` call on the second elector's ZooKeeper client fail with `NoAuth`. On main (b73f03976f) it fails with `StackOverflowError` after 9 seconds. On this branch (bee5c511da) it passes: the `NoAuth` exception reaches the caller after exactly three `getData` calls. `LeaderElectionTest` as a whole passes on this branch: 6 tests, 0 failures. Run 2026-10-23. The other election tests (`Leader*`, `TestLeaderElection*`) pass on this branch: 16 tests, 0 failures, 2 skipped.

**The choice.** Two routes were available:
1. Stop after three attempts and throw (this PR).
2. Keep retrying with a growing pause, which never gives up and needs a thread that can wait.

Context: the retry only runs inside a watcher callback or a join, so there is no thread of its own to wait on. A persistent failure such as a missing permission does not clear in the milliseconds a short retry spans.

Ladder: Can a retry fix the error? Only a short ZooKeeper hiccup, and three attempts cover that. Is a thread available to wait on? No. Which is worse, an exception that reaches the log or an endless loop? The loop.

**Recommendation:** route 1. Is a limit of three the right number?

**Limits.** After the third failure the replica has no watch until it joins the election again, for example after a session expiry. Before this change that replica also ended without a watch, through the overflow. The nested rejoin path in `ShardLeaderElectionContext` is a different loop and is in a separate change. `retryElection` also re-joins without a limit; it creates a new election node on each pass and was left alone.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
