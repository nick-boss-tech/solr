🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

The recursion described in this ticket is still in the code on main. I did not reproduce the stack overflow.

- On [commit b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb), `ShardLeaderElectionContext.rejoinLeaderElection` calls `LeaderElector.joinElection`, which calls `checkIfIamLeader` and then `runLeaderProcess`. That can call `rejoinLeaderElection` again. The call is at [line 494](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L494), and nothing in the path limits how many times it repeats.
- Rejoining happens when a sync fails ([line 245](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L245)), when the leader publish fails ([line 318](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L318)), or when the replica is not allowed to lead in time ([line 188](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L188)).
- The only pause is a 2.5 second sleep ([line 209](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java#L209)). It runs only for a replacement leader on a core that is not read-only.
- Two duplicates are resolved, SOLR-5692 and SOLR-6285. I found no commit on main that fixes this path.
- I read the code but did not write a test that forces the loop, so the overflow is not confirmed.

**Recommendation:** Keep open. A limit on the rejoin loop changes leader election, so it needs its own change and a test that forces repeated failures.
