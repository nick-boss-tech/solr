🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

The ChaosMonkey tests still fail on current main. Three of the five classes fail, and the failures repeat with the same seed.

Run on [commit b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb) with nightly tests enabled and seed `E4EFD2B86C0B81D4`:

- `ChaosMonkeyNothingIsSafeWithPullReplicasTest` fails with "Cluster state still in flux after 30 retry intervals". That is the timeout the ticket describes. It fails at [line 264](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeyNothingIsSafeWithPullReplicasTest.java#L264).
- `ChaosMonkeySafeLeaderTest` fails its check of zero failed index requests (expected 0, got 1) at [line 167](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeySafeLeaderTest.java#L167). `ChaosMonkeySafeLeaderWithPullReplicasTest` fails the same check at [line 209](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeySafeLeaderWithPullReplicasTest.java#L209).
- Running the three again with the same seed failed the same way. Running `ChaosMonkeySafeLeaderTest` alone with a random seed passed once, so the failures depend on the seed.
- Each failed index request went to a node that the test had just stopped, 1 to 40 ms after the stop. The error was a closed HTTP/2 channel. The test counts any failed request, so a request in flight during a stop fails the check. The safe-leader tests have asserted zero failed updates since 2014 ([SOLR-5740](https://issues.apache.org/jira/browse/SOLR-5740)).
- The timeout in `ChaosMonkeyNothingIsSafeWithPullReplicasTest` is not explained yet.

**Recommendation:** Keep open. The seed reproduces the failures. The open questions are whether a request in flight to a stopped node should fail the test, or be retried, and why the cluster state does not settle in the pull-replica test.
