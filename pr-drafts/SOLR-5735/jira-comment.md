🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Most of the failures in this ticket depend on the test's file system. One failure still reproduces on local disk.

Both runs used [commit b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb), nightly tests on, seed `E4EFD2B86C0B81D4`. The first run used a network file system. The second used local disk.

- `ChaosMonkeyNothingIsSafeWithPullReplicasTest` timed out with "Cluster state still in flux after 30 retry intervals" on the network file system. That message comes from a shard consistency check. The pull replica kept failing to copy the index with "Too many links", which the network file system returned when the copy hard-linked a file ([IndexFetcher, line 1224](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/handler/IndexFetcher.java#L1224)). On local disk the test passed, with no fetch errors.
- `ChaosMonkeySafeLeaderTest` failed one check (expected 0, got 1) on the network file system. On local disk it passed.
- `ChaosMonkeySafeLeaderWithPullReplicasTest` fails on both. One update request failed with a `ClosedChannelException` 4 ms after the test stopped the node it was sent to. The test asserts zero failed updates. That assertion has been there since at least 2014 ([SOLR-5740](https://issues.apache.org/jira/browse/SOLR-5740)).

**Recommendation:** Keep open, with a narrower scope. The open question is whether an update request in flight to a stopped node should be retried, or whether the test should allow it. The first two tests passed on local disk, so their failures on the network file system likely come from the environment.
