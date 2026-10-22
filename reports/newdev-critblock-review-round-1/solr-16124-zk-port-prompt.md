# Review: SOLR-16124, branch solr-16124-zk-port-prompt (unit 31)

Head reviewed: 7b81166ae905bc12f3cd7d18a7265ae446d62e38. Base: c8ab71430f. Ticket: SOLR-16124, "Solr Example should prompt for ZK port".

## Pass 1

- Ticket fit. The ticket asks that the cloud example prompt for the embedded ZooKeeper port. The branch adds that prompt (default: first node port + 1000), checks the port against node ports and by connecting, and writes `clientPort=` to node 1's `zoo.cfg`. That fits the ticket.
- Finding, major: the test checks only that node 1's `zoo.cfg` contains `clientPort=<zkPort>`. It does not check that the running embedded ZooKeeper listens on that port, which is the user-visible result the ticket asks for. Recorded, not repaired (a stronger test is outside the repair scope).
- Finding, major: the proof leg does not isolate the new check. On base the test fails at the collection-not-found assertion, because the extra input line shifts the answers. The branch's own zoo.cfg assertion is never the failing check on base. Recorded, not repaired.
- Finding, minor: the test picks `zkPort` by opening and closing a `ServerSocket`, which can race with another process before the cluster binds. Recorded only.
- Repairs: none.

Regate: `gates/SOLR-16124-review-pass-1.md`, GATE GREEN at 7b81166ae9 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 7b81166ae9. - Open pass 1 finding, major (test checks zoo.cfg, not the listening port), narrowed in code. `SolrZkServer` reads `zoo.cfg` from the node's Solr home (`solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java:104`), and the test asserts on that same file (`node1SolrHome/zoo.cfg`). So the written `clientPort` reaches the embedded ZooKeeper. The gap that remains is that no test checks the running ZooKeeper listens on the chosen port. Recorded, not repaired.
- Open pass 1 finding, major (proof leg not isolated), unchanged. Not repaired.
- Open pass 1 finding, minor (zkPort race), unchanged.
- Repairs: none.

Regate: `gates/SOLR-16124-review-pass-2.md`, GATE GREEN at 7b81166ae9 on vm4.

## Pass 3

Review (2026-10-22, vm4), same head 7b81166ae9. - No new findings against the head. The pass 1 and pass 2 findings stand: the test checks the `zoo.cfg` file, not the listening ZooKeeper port (major); the proof leg does not isolate the `zoo.cfg` check (major); the zkPort race (minor). `SolrZkServer` reads `zoo.cfg` from the node's Solr home, so the written port is honored.
- Repairs: none.

Regate: `gates/SOLR-16124-review-pass-3.md`, GATE GREEN at 7b81166ae9 on vm4.

Final head: 7b81166ae905bc12f3cd7d18a7265ae446d62e38. Unit 31 DONE with the three pass sections.
