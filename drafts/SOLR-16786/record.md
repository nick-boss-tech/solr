# Skip record: SOLR-16786

- Ticket: SOLR-16786, "Zookeeper connection timeout even after Solr trying to connect. Connection establishes after restarting the Solr service". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-05-03, updated 2023-07-21, no fix version, no comments.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-16786-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:57-13:57 UTC.

## Ticket reading

On Solr 8.6.0 with a three-node ZooKeeper ensemble, a Solr node loses its ZooKeeper session ("Client session timed out, have not heard from server in 20208ms"). The node is removed from live nodes and stays out until the Solr service is restarted. The reporter says it happens almost daily, on different nodes. The ticket has no comments and no reproduction steps.

## Code reading

- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:217-219`: the `onReconnect` listener and the `zkSessionExpired` flag.
- `ZkController.java:421`: `zkSessionExpired.set(true)` on the session-expiry path.
- `ZkController.java:450-451`: `onReconnect()` runs only if the flag compare-and-set from true to false succeeds, so reconnect handling is tied to a recorded expiry.
- `ZkController.java:493` and `:731`: `SessionExpiredException` is caught on the reconnect and register paths.
- `ZkController.java:688`, `registerAllCoresAsDown`: the code that marks cores down after a session loss.

What this shows at the pinned head: the reconnect path is driven by the session-expiry flag and runs a re-registration. The ticket's failure (a node that stays out until restart) would need the flag or the re-registration to fail silently in 8.6. The 8.6 code is not checked in this pass, and the pinned head is many releases later, so the pinned head's behavior does not establish what happened on 8.6.

## History

- No commit on the pinned head names SOLR-16786 (evidence tool: no hits).
- Not traced: the commits that changed the ZooKeeper reconnect path between 8.6 and the pin.

## Reproduction attempts

Not attempted. Reproducing a ZooKeeper session loss needs a ZooKeeper ensemble with network or pause injection, and a Solr node that re-registers after the session comes back. This pass is review-only and runs no builds.

## Gap analysis

The reporter's ensemble and node logs are the evidence that decides the question: whether the session expired (which should trigger the reconnect path), or whether the session stayed connected while the node's registration did not come back. The ticket gives only one warning line. The ticket also does not say whether the failing nodes ran the same Solr build. The pinned head's reconnect path cannot be tied to the 8.6 behavior without that log.

Missing piece for a verdict change: the 8.6 node log for a failure window, covering the session-expiry and reconnect messages, and the ZooKeeper server log for the same window. With those, the record can say whether the pinned head's path would behave differently, or whether the failure is environmental.

## Verdict justification

keep-open meets the rubric: the code path at the pin is read (`ZkController.java:217-219, 421, 450-451, 493, 688, 731`), no attempt is run, and the missing piece is named (the node and ZooKeeper logs for a failure window). A close needs a fix commit that is an ancestor of the pin or a non-issue shown, and neither is shown. A pr-needed needs a line that causes the failure at the pin, and no such line is shown; the 8.6 build is not the pinned build.

## Limits

This record does not establish the cause of the reporter's failures and does not check the 8.6 code. It does not say the pinned head is free of the bug. It does not post anything to JIRA or GitHub.
