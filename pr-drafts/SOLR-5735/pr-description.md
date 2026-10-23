🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

**An update cut off by a stopped node fails the zero-failure check.** `ChaosMonkeySafeLeaderWithPullReplicasTest` stops nodes and then asserts that no update failed ([line 209](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeySafeLeaderWithPullReplicasTest.java#L209)). On main, with seed `E4EFD2B86C0B81D4` on local disk, one update fails with `ClosedChannelException` about 4 ms after the test stops the node it was sent to. The check reports `expected:<0> but was:<1>`.

**Failures cut off by a stopped node are counted separately.** [`StoppableIndexingThread`](https://github.com/nick-boss-tech/solr/blob/0136fab1b05d32d3d9fe883aa37af4976599cc71/solr/test-framework/src/java/org/apache/solr/cloud/StoppableIndexingThread.java) records an add or delete whose root cause is `ClosedChannelException` in its own set. `getFailCount()` leaves those out. The ids stay in the add and delete failure sets, so the bookkeeping the other chaos tests use does not change. The consistency checks that run after each test still read every replica.

**Proof.** `ChaosMonkeySafeLeaderWithPullReplicasTest`, seed `E4EFD2B86C0B81D4`, nightly tests on, local disk. On main (b73f03976f), it fails with `expected:<0> but was:<1>`. On this branch, it passes in two runs on 2026-10-23 (on head 2f188494dd): one run with two cut-off requests excluded, and one run with none. The last commit (0136fab1b0) only makes two fields private.

**The choice.** Two routes were available:
1. Retry a cut-off update in the client. Not chosen. The load-balancing client does not retry an update after an I/O error, because the server may already have applied it ([line 576](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java#L576)). A retry could apply a non-idempotent update twice.
2. Leave cut-off failures out of the count (this PR).

Context: the test still checks replica consistency after the stop, so a lost or diverging document still fails the test.

Ladder: Can the client replay an update safely? Not in general. Does the test still check the data? Yes, through the consistency checks. Does excluding one exception hide real loss? Only if the consistency checks miss it.

**Recommendation:** route 2.

**Limits.** The exclusion matches on the exception type, not on the monkey's stop. Any `ClosedChannelException` on an update is excluded in every chaos test. The `Too many links` error that a network file system returned during an index copy ([IndexFetcher line 1224](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1224)) is not addressed here. The timeout in `ChaosMonkeyNothingIsSafeWithPullReplicasTest` reproduced only on that file system, and the same seed passes on local disk. `ChaosMonkeyNothingIsSafeTest` and `ChaosMonkeyNothingIsSafeWithPullReplicasTest` also read `getFailCount()` against a tolerance, so the same exclusion makes their tolerance check more lenient. Both classes pass on head 0136fab1b0 (one run each, random seed).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
