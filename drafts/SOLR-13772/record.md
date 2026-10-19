# Skip record: SOLR-13772

- Ticket: SOLR-13772, You can only safely use one MiniSolrCloudCluster in a JVM. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-09-17, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13772-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code and greps run at this head (detached worktree).
- Source: remediation redo of batch 05, item 23, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the redesign proposal from audit evidence; the code is read here.

## Ticket reading

MiniSolrCloudCluster starts a ZooKeeper and tells its nodes where it is through the JVM-wide `zkHost` system property, so a second cluster in the same JVM conflicts with the first. The ticket asks that the ZooKeeper address come through CoreContainer, not a global property.

## Code and grep at the pinned head

- solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java:449: `nodeProps.setProperty("zkHost", zkServer.getZkAddress())`. The cluster passes its ZooKeeper address through the node's own properties, not a JVM system property. This is the part of the ticket that is addressed.
- MiniSolrCloudCluster.java:1129: `System.setProperty("solr.cloud.overseer.enabled", ...)`. A JVM-wide property is still set by the cluster, so a second cluster in one JVM still shares that setting.
- `git grep -n 'setProperty("zkHost"' 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/test-framework/src/java/*' 'solr/core/src/test/*' | wc -l` gives 7. Several core tests still set the global `zkHost` system property themselves (for example ZkSubcommandsTest.java:78, DistributedQueueTest.java:364, OutOfBoxZkACLAndCredentialsProvidersTest.java:66, OverriddenZkACLAndCredentialsProvidersTest.java:77, VMParamsZkACLAndCredentialsProvidersTest.java:84).
- The commit that introduced the node-properties form was not identified in this pass.

## History

- No commit names SOLR-13772.

## Reproduction attempts

Not run. A reproduction starts two MiniSolrCloudCluster instances in one JVM and checks whether the second one sees the first one's ZooKeeper. This pass did not run a test.

## Gap analysis

The ticket is partly addressed: the cluster class itself no longer depends on the global zkHost property. What remains is the overseer system property the cluster sets (MiniSolrCloudCluster.java:1129), and the core tests that still set the global property. Removing those is the cleanup the ticket asks for, and it touches several tests.

## Verdict justification

keep-open: the attempts made are the code and grep reads above at the pinned head, the Jira read, and the partial-fix check. The missing piece is the rest of the cleanup: the overseer property at MiniSolrCloudCluster.java:1129 and the seven test sites, plus a run of two clusters in one JVM. close-fixed is not supported (the global property is still set in tests and for the overseer flag); close-obsolete is not supported (the coupling exists).

Missing for a closing verdict: the remaining property sites removed, and a two-cluster run.

## Limits

This record does not run a two-cluster test. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
