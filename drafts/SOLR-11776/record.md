# Skip record: SOLR-11776

- Ticket: SOLR-11776, "HttpSolrClient should handle SocketException:broken pipe". JIRA status read 2026-10-20T07:55:31Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open. This replaces the recorded close-obsolete: the premise the close-obsolete rested on (LIR removed) is only half true; the same leader response to a replica error is present at the pinned head under the shard-term mechanism.
- Draft branch: solr-11776-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:55:31Z (UTC).

## Ticket reading

When the leader sends an update to a replica and gets `SocketException: broken pipe`, the leader treats it like any other socket error and marks the replica for recovery. The reporter says a broken pipe means a closed connection, not an unhealthy replica, and asks the client to reconnect instead. The test named is HttpPartitionTest. No comments.

## Code reading (pinned head)

- The LIR mechanism named by the ticket is gone as a znode flag. The grep `git grep -n -i "leaderInitiatedRecovery\|\bLIR\b" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java solr/solrj/src/java` returns only two comments and log lines in `solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java:1316, 1352`.
- The same leader decision still exists in that file. For a replica that returned an error and is still the leader's replica (lines 1296-1319), the leader does not mark it DOWN; it adds the replica to `replicasShouldBeInLowerTerms` (line 1322-1331), which asks that replica to recover by increasing its term. Any error that reaches this path, including a broken pipe, leads there.
- `git grep -l -i "broken pipe" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/solrj/src/java solr/core/src/java`: no HttpSolrClient or update-path handling of broken pipe. The only hit is `ExportBuffers.java`, which is unrelated.
- HttpPartitionTest exists at the pinned head (`solr/core/src/test/org/apache/solr/cloud/HttpPartitionTest.java:67`).

## History

- evidence.py at the pinned head: no commits naming SOLR-11776.

## Reproduction attempts

Not run. A run would send an update to a replica whose connection the test closes from one side, and check whether the replica is asked to recover. HttpPartitionTest is the place for it.

## Gap analysis

The ticket's premise (an error on a healthy connection moves the replica out of service) still holds through the term mechanism, so the ticket is not obsolete. Whether the broken-pipe case should reconnect instead of recovering is a design choice the pool has not made. The run in HttpPartitionTest is the missing piece.

## Verdict justification

keep-open meets the rubric: attempts made are the grep for the removed mechanism and the reading of the leader's error handler; missing pieces are a HttpPartitionTest run that shows the broken-pipe path, and a decision on reconnect versus recovery. close-obsolete is not supported: the leader's response to a replica error is present at the pinned head. close-fixed is not supported: no fix commit is named.

## Limits

This record does not establish what a broken pipe does on a healthy connection at the pinned head. It does not check the HttpSolrClient retry settings.
