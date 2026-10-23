# SOLR-18576: the other expireSession caller (TestPullReplicaErrorHandling)

Assignment: `assignments/solr-18576-pull-replica-expiry-round-1.md`. Claimant: phone-agent (vm1). Base: apache/solr `main` at `79b23df58daac14b92cc35f23b8b81f72098b4ba`. Question: can the expire-then-index race fixed for `BasicDistributedZk2Test` in PR #5206 also hit `TestPullReplicaErrorHandling.testPullReplicaDisconnectsFromZooKeeper`, which calls `expireZkSession` and then `addDocs(30)` with no wait?

## Item 1: routing (read the code)

Answer: no in the normal path. The add is routed to the shard leader, the leader is the NRT replica, and the expired node hosts only the PULL replica, which is never a leader and sits on a different node. One formal exception keeps the answer from being a flat no: the client route for a slice lists the leader first and the other replicas after it as load-balancer failover targets, so the expired node can receive the add directly if the leader attempt fails first inside the expiry window. That is why the item 2 probe ran.

Evidence, all at `79b23df58da`:

- The test builds one shard with one NRT replica and one PULL replica: [`createCollection(collectionName, "conf", 1, 1, 0, 1)`](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/test/org/apache/solr/cloud/TestPullReplicaErrorHandling.java#L218-L221), where the overload's parameters are numShards, numNrtReplicas, numTlogReplicas, numPullReplicas ([CollectionAdminRequest.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/solrj/src/java/org/apache/solr/client/solrj/request/CollectionAdminRequest.java#L400-L410)). The node whose session is expired is the one hosting the PULL replica ([test lines 227-229](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/test/org/apache/solr/cloud/TestPullReplicaErrorHandling.java#L227-L229)).
- `addDocs` is a plain add plus commit through the cluster client ([test lines 281-289](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/test/org/apache/solr/cloud/TestPullReplicaErrorHandling.java#L281-L289)). That client is the shared `CloudSolrClient` built by `MiniSolrCloudCluster` from a plain builder, with no routing options set ([MiniSolrCloudCluster.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java#L653-L655)).
- For an update, `CloudSolrClient` builds one route per slice in `buildUrlMap`: the leader's core URL goes first ("put the leaderUrl first"), and because `directUpdatesToLeadersOnly` defaults to false in the builder, the slice's other replicas follow in the same list ([CloudSolrClient.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L446-L486), [builder default](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1297)). The load-balancing client tries the list in order, so a non-leader URL receives the add only after the leader attempt fails.
- A PULL replica is never the leader: `Replica.Type.PULL` has `leaderEligible=false`, and its javadoc says PULL replicas "can't become shard leaders" and "don't even participate in elections" ([Replica.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/solrj/src/java/org/apache/solr/common/cloud/Replica.java#L113-L124)). The shard's only leader-eligible replica is the single NRT replica.
- The two replicas sit on different nodes. The cluster has 4 nodes ([test line 80](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/test/org/apache/solr/cloud/TestPullReplicaErrorHandling.java#L80-L80)), no placement plugin is configured, and the out-of-the-box default is the simple placement plugin ([PlacementPluginFactoryLoader.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/cluster/placement/impl/PlacementPluginFactoryLoader.java#L102-L129)), which weights a node by 1000 times its same-shard replica count when choosing where a replica goes ([SimplePlacementFactory.java](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/cluster/placement/plugins/SimplePlacementFactory.java#L96-L135)). With three empty nodes available, the second replica of the shard does not land on the leader's node. Collocation is permitted by the code only when every alternative scores worse, which cannot happen in this cluster shape.

So before line 228 the expired node is never a leader, and the add's first and normally only target is the leader on another node, whose ZooKeeper session is untouched.

## Item 2: probe (10 seeds)

Ran on vm1 local disk at the base commit `79b23df58da`, in a scratch worktree, under the test-queue lock: `:solr:core:test --tests 'org.apache.solr.cloud.TestPullReplicaErrorHandling.testPullReplicaDisconnectsFromZooKeeper' -Ptests.iters=10`. Build rc=0. Counted from the fresh JUnit XML: 10 tests, 0 skipped, 0 failures, 0 errors. Suite seed `6942793AB5AC29A5`; per-iteration seeds and results:

| Iteration seed | Result |
| --- | --- |
| 760B59B9904626C5 | pass |
| C25DE545A484EDE9 | pass |
| 4CB47399F540A522 | pass |
| 7D5AD87C99BEFE0B | pass |
| 319B5DD138B63EB0 | pass |
| A0618E8E450A73B0 | pass |
| 9EBFEA08573A63B6 | pass |
| 20C7072742EF718 | pass |
| 30A0951C03E5E042 | pass |
| E72BC3A667B2D710 | pass |

No failure occurred, so there is no failing line to record. Log: `~/workspace/tools/assign-logs-2026-10-23/18576-probe.log` on vm1; JUnit XML beside it as `18576-probe-junit.xml`. One limit on what the probe shows: the failover path in item 1 needs the leader attempt to fail first for an unrelated reason, and nothing in these runs made it fail, so the probe confirms the normal path rather than exercising the failover.

## Recommendation

No change to `TestPullReplicaErrorHandling`. The race that hit `BasicDistributedZk2Test` needed the update to be delivered to the node whose session had just expired; here the update goes to the leader on a different node, a PULL replica cannot be a leader, and the probe passes 10 of 10 at the base. The only route to the expired node is load-balancer failover after an independent leader failure inside the expiry window, which has never been observed for this test. If a CI failure ever shows this test failing at the add after line 228 with "Updates are disabled", that is the evidence that would justify adding the same wait used in PR #5206.
