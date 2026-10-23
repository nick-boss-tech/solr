🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

**Repeated leader rejoins nest on the same thread.** When a shard leader's sync fails, its leader publish fails, or it cannot become leader in time, [`rejoinLeaderElection`](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L188) calls `joinElection`, which runs the leader process again. That can call `rejoinLeaderElection` again before the first call returns. Each round adds about six frames to the thread's stack and nothing removes them. A replica that keeps failing can therefore grow its stack until it overflows, as SOLR-6213 reports. The nested join is [line 494 on main](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L494).

**Each rejoin now runs on its own thread.** The three callers return right after `rejoinLeaderElection`, so the join can run after they return. The rejoin hands `joinElection` to a lazy single-thread executor that keeps its thread for 10 seconds ([line 502](https://github.com/apache/solr/blob/4be5d1a69b94f2075149da69b150b99cc4dec25f/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L504)). [Closing the context](https://github.com/apache/solr/blob/4be5d1a69b94f2075149da69b150b99cc4dec25f/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L85) shuts that executor down. A rejoin that races with close is dropped with a debug log, and a queued join is skipped once the context is closed. A join already running is not interrupted; it finishes and the thread exits.

**Proof.** [`ShardLeaderElectionRejoinTest`](https://github.com/nick-boss-tech/solr/blob/1978d06df830f0f818981a0c355a91212317af7e/solr/core/src/test/org/apache/solr/cloud/ShardLeaderElectionRejoinTest.java) forces 10 failed syncs and records the stack depth at each rejoin. On main with the test's hook, the depth grows by 54 frames over 11 rejoins, and the test fails. With this change, the depth stays within 6 frames and the collection becomes active, and the test passes. One test, 0 failures, on test branch head 1978d06df8, run 2026-10-23. The test uses a hook that forces sync failures. The hook lives on the test branch and is not part of this PR. The existing election suites (`LeaderElectionTest`, `LeaderElectionIntegrationTest`, `LeaderTragicEventTest`, `LeaderVoteWaitTimeoutTest`, `LeaderFailoverAfterPartitionTest`, `TestLeaderElectionWithEmptyReplica`, `TestLeaderElectionZkExpiry`) pass on the PR head 4be5d1a69b: 13 tests, 0 failures, 1 skipped.

**The choice.** Two routes were available:
1. Turn the nested calls into a loop inside the election. The stack stays flat, but control flow changes in the three callers and in `LeaderElector`.
2. Hand each rejoin to its own thread (this PR). The callers are unchanged, and each rejoin starts with a clean stack.

Context: each rejoin already waits at a leader throttle of five seconds ([line 127](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L127)), so rejoins are paced.

Ladder: Do the callers need the rejoin to finish before they return? No, each returns right after the call. Does the rejoin need the caller's thread? No, it reads and writes ZooKeeper state only. Cost of route 2: a `SessionExpiredException` during a rejoin is now logged on the rejoin thread, instead of being thrown to the caller.

**Recommendation:** route 2. It is the smaller change, and the callers keep their behavior. Is that the trade-off you want?

**Limits.** No overflow was reproduced. The test shows the growth per rejoin and the pause between rejoins. An overflow needs a long run of failed syncs, and the stack size of the thread was not measured. The session-expiry change is not covered by a test. The two duplicate tickets, SOLR-5692 and SOLR-6285, are resolved and were not re-checked.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
