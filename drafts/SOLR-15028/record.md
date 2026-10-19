# Skip record: SOLR-15028

- Ticket: SOLR-15028, SolrCloud shows cluster still healthy without failover even the node data directory is deleted. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2020-12-03, updated 2021-02-13, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15028-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 03, item 24, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited a feature-proposal reading from audit evidence; not checked there.

## Ticket reading

With SolrCloud 7.x-era setup, deleting one node's data directory leaves the node reported as healthy, the Admin UI green, and the replicas on that node up in the cluster state. Index and query requests fail with NoSuchFileException, and they do not fail over to healthy replicas.

## Grep and code reading at the pinned head

- solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java:136: the node-health check reads the live-node list from the cluster state (`clusterState.getLiveNodes().contains(...getNodeName())`). Liveness comes from ZooKeeper, not from the local disk.
- NodeHealth.java:112: the check counts unhealthy local cores with `findUnhealthyCores(coreDescriptors, clusterState)`. The definition is at NodeHealth.java:259, and its comment (:251-253) says it checks replicas that are DOWN or RECOVERING, or missing locally, against the cluster state.
- `git show 498a09aea97aac86c2cc7ac114113b946d249942:solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java | grep -n -i "dataDir\|Files.exists\|isDirectory\|Directory"` returns no lines. So the node-health path does not check the local data directory.

So the health endpoint reports what ZooKeeper says about the node and its replicas. A deleted data directory does not change those states, so the endpoint reports healthy. That matches the ticket's report.

## History

- No commit names SOLR-15028. Not searched further in this pass.

## Reproduction attempts

Not run. A reproduction needs a SolrCloud with two nodes and replicas, deletion of one node's data directory, and a query routed to that node. This pass did not start a cluster.

## Gap analysis

The gap is real in the code path: the health endpoint and the replica states do not see a local IO failure. Whether a replica should go DOWN on a local IO error, and whether the health endpoint should check the data directory, are design choices the ticket proposes but the maintainers have not made. The reporter's failover complaint (no failover to healthy replicas) is not traced in this pass: the request path that handles the NoSuchFileException was not read.

## Verdict justification

keep-open: the attempts made are the code reads and the grep above at the pinned head, the Jira read, and the endpoint check. The missing piece is (1) a run on a two-node cluster showing the healthy report and the failed query after deleting a data directory, and (2) the maintainer decision on marking replicas down for local IO errors or adding a directory check to the health endpoint. No close verdict is supported (the gap is in the code). No PR verdict is supported until the decision is made.

Missing for a closing or PR verdict: (1) the run output; (2) the design decision.

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
