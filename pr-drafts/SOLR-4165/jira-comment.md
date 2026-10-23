🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

A query sent to a live node did not stall while another node stopped, on current main. The report was a stall of 1 to 5 seconds.

- [`NodeStopQueryStallTest`](https://github.com/nick-boss-tech/solr/blob/9dd93f937177818133d15df58487611fbed40407/solr/core/src/test/org/apache/solr/cloud/NodeStopQueryStallTest.java) runs on [commit b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb). It starts 4 nodes with a 4-shard collection and 2 replicas per shard, sends queries straight to one node that stays up, and stops another node meanwhile. In two runs of each kind of stop, abrupt and graceful, the slowest query during the stop took 13 ms or less. Stopping took about 120 to 145 ms. Zero or one query failed per run, and the failure came back at once.
- The reporter ran 10 nodes behind load balancers. This test uses 4 nodes in one JVM with no load balancer. A stalled connection to a machine that disappears without closing its sockets would not show up here.
- The ticket says starting a node stopped blocking queries after SOLR-3655. Only the stop was left open.
- The stop-time fix is [384d42b5e3](https://github.com/apache/solr/commit/384d42b5e3be31514ca8c21dbfd706d82914e800) (SOLR-4421). The reporter tried it in February 2013 and still saw 3 to 4 seconds of blocking. The node is now published as DOWN before the containers close ([`ZkController.preClose`, line 847](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ZkController.java#L847)). No release was tested.

**Recommendation:** Close as Cannot Reproduce. If the stall still happens on a current release, a report with the Solr version and the load balancer setup can reopen it, and the test above is a starting point.
