🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

The stack trace on this ticket is the leader-election rejoin loop that [SOLR-6213|https://issues.apache.org/jira/browse/SOLR-6213] covers, not a logging failure.

* The trace repeats one cycle at least three times: {{ShardLeaderElectionContext.runLeaderProcess}} calls into {{LeaderElector.joinElection}}, which reaches {{ShardLeaderElectionContext.rejoinLeaderElection}} and starts {{runLeaderProcess}} again, with {{SyncStrategy.sync}} and {{PeerSync.sync}} inside each round. A leader process whose sync does not complete rejoins on the same thread, and each rejoin nests the process one level deeper.
* The log4j 1.x and {{java.text}} frames appear once, inside the warning that {{PeerSync.handleResponse}} logs. That call is where the stack ran out; it is not a cycle. Log4j 1.x is gone from current main, but the rejoin loop is still there: on [commit b73f03976f|https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb] a test that forces failed syncs shows the stack growing by 12 frames over 4 rejoins.

*Recommendation:* Keep this ticket open as a duplicate of SOLR-6213. The fix belongs on SOLR-6213, which covers both leader-election loops.
