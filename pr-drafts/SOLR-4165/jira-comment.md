🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Stopping a node did not block queries in a test on current main.

- A two-node SolrCloud test on current main (commit [b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb)) sent a query every 20 ms while one node was stopped. Stopping took 147 ms. None of the 268 queries failed, and the slowest query near the stop took 4 ms.
- The reporter saw stalls of 1 to 5 seconds on a ten-node cluster behind load balancers. The test used two nodes and no load balancer, so it is not the same setup.
- Shutdown now publishes the node as DOWN before the containers close. See [ZkController.preClose](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/ZkController.java#L819) at line 847. The earlier change for this is [384d42b5e3](https://github.com/apache/solr/commit/384d42b5e3be31514ca8c21dbfd706d82914e800) under SOLR-4421.
- The test was run once and is not committed. It did not cover startup, which the ticket also describes as blocking for 10 to 30 seconds.

**Recommendation:** Check the startup delay on a current release before closing. If it does not show there either, close the ticket as Cannot Reproduce.
