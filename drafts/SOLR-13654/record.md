# Skip record: SOLR-13654

- Ticket: SOLR-13654, Scary jenkins failure related to collection creation: "non legacy mode coreNodeName missing". JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-07-25, updated 2020-10-16, no fix versions, no links; one comment (2020-10-16, erickerickson): "I think this can be closed? ... that error cannot be thrown now since that code was removed in SOLR-12823."
- Verdict: keep-open.
- Draft branch: solr-13654-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Greps and code read run at this head (detached worktree).
- Source: remediation redo of batch 06, item 4, under assignments/pool-aaa-skip-verdict-remediation-06.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) said the error path was removed with legacy mode; that is not right at this head, and the correction is below.

## Ticket reading

A SplitShardTest run failed when creating a collection with a node rule: the CREATE core command arrived without a coreNodeName, which the cluster requires outside legacy mode. The reporter could not reproduce it. The 2020 comment says SOLR-12823 removed the code path, so the error should no longer occur.

## Grep and code at the pinned head

- `git grep -n "coreNodeName missing\|non legacy\|non-legacy" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives two matches: solr/core/src/java/org/apache/solr/core/CoreContainer.java:1547 and solr/core/src/test/org/apache/solr/cloud/DeleteInactiveReplicaTest.java:124.
- CoreContainer.java:1545-1547: when a ZooKeeper controller is present (SolrCloud), a core descriptor with no core node name throws SERVER_ERROR "coreNodeName missing " + parameters. So the check the ticket's failure came from is still in the code at the pin. The 2020 comment that the error "cannot be thrown now" does not hold at this head.
- The legacy-mode references left in core are `HealthCheckHandler.java:55` and `NodeHealth.java:86` (log text and a comment); the legacy cluster-state path itself is not in the tree. SOLR-12823 commits on the pin include 1ab9b811c6 ("remove /clusterstate.json", 2020-06-09), an ancestor of the pin.

So the check still exists, and the failing path needs a core create without a node name. Whether the collection-create flow can still produce such a request was not traced in this pass.

## Reproduction attempts

Not run. The original failure was a flaky run of SplitShardTest with a node rule; this pass did not run that test.

## Gap analysis

The earlier claim that the error path is removed is incorrect: the guard is present. The open question is whether the collection-create path that the test used can still send a create without a node name. If it cannot, the guard is a safety check that never fires; if it can, the ticket is a live bug.

## Verdict justification

keep-open: the attempts made are the greps and the code read above at the pinned head, the Jira read, and the history check. The missing piece is the trace of the collection-create path to show whether a create without coreNodeName is possible, or a run of SplitShardTest with the node rule. close-obsolete and close-fixed are not supported: the guard is in code, and the path is not shown dead.

Missing for a closing verdict: the path trace or the test run.

## Limits

This record does not run SplitShardTest. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
