# Skip record: SOLR-12369

- Ticket: SOLR-12369, Create core sometimes failed because of ZkController.waitForShardId. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2018-05-17 and 2018-05-18, no fix noted).
- Verdict: keep-open.
- Draft branch: solr-12369-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: when tests are run repeatedly, core creation sometimes fails because ZkController.waitForShardId does not find the core's replica in the node's cluster state, which the reporter thinks is never updated. The first comment says a forced collection refresh from ZooKeeper would fix the stale view but adds ZooKeeper load and still leaves a window for missed notifications (SOLR-9140 suggested). The second comment asks why the core passes ZkController.checkStateInZk.

The full description is 190 characters and was read from the Jira REST record.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/ZkController.java:2086-2115 (waitForShardId): calls zkStateReader.waitForState(collection, 320 s, predicate) and, on timeout, throws SolrException "Failed getting shard id for core". There is no forced refresh of the collection before or during the wait.
- solr/core/src/java/org/apache/solr/cloud/ZkController.java:2054, 2058: the two call sites of waitForShardId.
- solr/core/src/java/org/apache/solr/cloud/ZkController.java:2190-2215 (checkStateInZk): waits up to 10 s on zkStateReader.waitForState for the shard and fails with "Invalid shard" or "No shard id" when the shard is not found; it runs after waitForShardId sets the shard id, so the two checks use the same watched view.
- forceUpdateCollection is called from ZkController.java:1528 and other places (ShardLeaderElectionContext.java:397, HttpSolrCall.java:356, 429), but not from waitForShardId.

So the stale-view wait the ticket describes is still present at the pin: waitForShardId waits on the watched state for up to 320 s and does not force a refresh, which is the change the first comment discusses. Whether a stale state actually causes the failure in the reported beast-test pattern is not established.

## History

- No commit at the pin names SOLR-12369 (git log --grep at the pin: none).
- SOLR-9140 is named in the ticket comment; its commit was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a repeated core-create test (the reporter used beasting) at the pin, recording whether waitForShardId times out with a stale view. Not built or run in this pass.

## Gap analysis

The code at the pin matches the ticket's description of the wait: the watched state is used without a forced refresh. The ticket's failure requires a run to confirm, and the fix choice (forced refresh with ZooKeeper cost, or accepting the window) is the design question the comments raise.

## Verdict justification

keep-open: the wait without refresh is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a repeated core-create run at the pin (the beast pattern) that shows whether the stale view causes the timeout, and a maintainer decision on the forced-refresh trade-off the comments describe.

## Limits

This record does not run the repeated core-create test, does not trace the SOLR-9140 history, and does not post anything to JIRA or GitHub.
