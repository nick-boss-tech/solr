# Skip record: SOLR-13247

- Ticket: SOLR-13247, Incorrect shard placement during Collection creation.
- Verdict: keep-open.
- Draft branch: solr-13247-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 4 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-14, updated 2019-06-08, no fix versions. Links: "Incorporates SOLR-13248 (Closed)"; "Reference SOLR-12944 (Open)". The report: a collection created with `createNodeSet` and several shards places the shards on nodes that look random, the node set is not respected, and sometimes not every listed node gets a shard; `createNodeSet.shuffle` is false. One comment (2019-02-14) names a cluster property workaround that reverts to the old behavior.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/api/collections/Assign.java:212-236`: the create node set is split from `createNodeSet`, the list is limited to live nodes (`nodeList.retainAll(liveNodes)`), and it is shuffled only when `createNodeSet.shuffle` is true. So the node set limits the candidate nodes; the shard placement that uses this list is the code after line 236 and was not traced here.
- `Assign.java:238-245`: without a node set, candidates are the live data nodes, shuffled.
- `solr/core/src/java/org/apache/solr/cluster/placement/plugins/SimplePlacementFactory.java:30-36`: the default placement factory replicates the old legacy assign strategy (nodes with the fewest cores of the same collection).
- The SOLR-13248 history is in the pin: `97875af3f9` and `7e2d40197c` are ancestors. The upgrade notes (`solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-8.adoc:238-242`) say the default replica placement was reverted and autoscaling placement can be turned on again. The ticket links SOLR-13248 as incorporated, so the default-placement change addresses part of the ticket by design.
- Tests that use createNodeSet with shards exist (for example `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZkTest.java`, `TestCollectionsAPIViaSolrCloudCluster.java`). None was run in this pass.

## History

- No commit in the pinned history names SOLR-13247 (the evidence tool lists none).
- SOLR-13248 (`7e2d40197c`, `97875af3f9`) is an ancestor and is the placement revert the ticket links.
- SOLR-12944 (linked, Open) is a separate ticket, batch 07 item 21.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run: create a collection with `createNodeSet` listing a subset of live nodes and several shards (with `createNodeSet.shuffle=false`), then check which nodes hold the shard replicas at the pin. The ticket's "not all nodes used" case is the one that decides the verdict.

## Gap analysis

- The node-set restriction is present in `Assign.java`, so the ticket's "node set not respected" claim is not confirmed by reading alone; the placement step that uses the list was not traced.
- The default placement changed after the ticket (SOLR-13248), so the ticket's behavior may no longer occur with the default; that is not shown by a run either.
- The workaround comment on the ticket is not in the pinned docs under that name; the upgrade note's revert path is the documented route.

## Verdict justification

keep-open: the code reading does not settle whether shard placement honours createNodeSet at the pin, and the ticket's specific behavior has not been run. close-fixed would need the fix commit named and checked (the SOLR-13248 placement revert is linked but names a different ticket); close-obsolete does not fit (the createNodeSet feature is present).

Missing for this verdict: a run of a multi-shard create with createNodeSet at the pin, checking the replica nodes against the node set and with shuffle=false.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
