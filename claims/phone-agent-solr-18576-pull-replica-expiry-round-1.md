# Claim: SOLR-18576 pull-replica expiry, round 1

- Host: phone-agent (vm1).
- Assignment: `assignments/solr-18576-pull-replica-expiry-round-1.md`.
- Claimed: 2026-10-23.
- Status: DONE, 2026-10-23.
- Outcome: item 1 answers no in the normal path (adds route to the leader; a PULL replica is never a leader; the expired node hosts only the PULL replica, on a different node), with one formal exception: the client route lists the other replicas as load-balancer failover targets after the leader. Item 2 therefore ran: `testPullReplicaDisconnectsFromZooKeeper` at base `79b23df58da`, 10 iterations, 10 passed, 0 failures. Recommendation: no change to the test. Report: `reports/solr-18576-pull-replica-expiry.md`.
