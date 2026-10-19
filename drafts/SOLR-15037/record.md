# Skip record: SOLR-15037

- Ticket: SOLR-15037, config update listener reloads solr core while schema is changed. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2020-12-09, updated 2021-01-12, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15037-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 23, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the C reading) cited commit 669aff2108, which is not in this repository; that citation is dropped here.

## Ticket reading

A schema update command returns before the core is fully reloaded, so requests after it can still use the old schema until the reload finishes. The reporter links an upstream commit and a user-list thread from November 2020.

## Grep and code reading at the pinned head

- `git cat-file -t 669aff2108` fails ("Not a valid object name"). The commit cited in the ticket is not in this fork, so its diff could not be read.
- solr/core/src/java/org/apache/solr/schema/SchemaManager.java:146-160 (the handling path, `class SchemaManager` at :69): the node persists the schema to ZooKeeper with `ZkController.persistConfigResourceToZooKeeper` (:146-152), then calls `req.getCore().getCoreContainer().reload(...)` (:157-158) under the config reload lock, and only then breaks out of the retry loop. So on the node that handles the update, the reload completes before the command returns.
- solr/core/src/java/org/apache/solr/core/SolrCore.java:3424-3440 (the config listener): a ZooKeeper watch on the config and schema resources calls `cc.reload(coreName, coreId)` (line 3431) under the same lock when the resource version is stale. This is the path other nodes take when they see the change, and it runs on the watch, not in the update request.

So the handling node reloads before it responds. The window the ticket describes is on the other nodes in the cluster: after the update returns, a request routed to another node can run on its old schema until that node's watch fires and its reload finishes.

## History

- The upstream commit cited by the reporter is not in this fork. The fork's commit history for the listener and SchemaManager was not searched in this pass.

## Reproduction attempts

Not run. The window is between one node's update response and another node's watch-driven reload; it needs a multi-node SolrCloud run with a schema update and a query routed to a non-handling node, which this pass did not start.

## Gap analysis

The code shows the handling node is synchronous and the other nodes are asynchronous. The ticket does not say which node its failing request reached, and the upstream commit that changed the behavior is not available. What is missing is a run that shows the stale-schema window on a non-handling node, and a decision on whether the update should wait for the other nodes.

## Verdict justification

keep-open: the attempts made are the code reads and the grep of the cited commit above, plus the Jira read. The missing piece is the multi-node run (schema update, then a query on another node, at the pinned head), and the maintainer decision on waiting for other nodes. No close verdict is supported: the window is shown by the code structure, not disproved. No PR verdict is supported without the run and the decision.

Missing for a closing or PR verdict: (1) the multi-node run output; (2) the decision on the wait.

## Limits

This record does not reproduce the stale schema. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
