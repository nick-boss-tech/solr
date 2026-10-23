🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

The ChaosMonkey tests still fail on current main. Three of the five classes fail, and the failures repeat with the same seed.

Run on [commit b73f03976f](https://github.com/apache/solr/commit/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb) with nightly tests enabled and seed `E4EFD2B86C0B81D4`:

- `ChaosMonkeyNothingIsSafeWithPullReplicasTest` fails with "Cluster state still in flux after 30 retry intervals". That is the timeout the ticket describes. It fails at [line 264](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeyNothingIsSafeWithPullReplicasTest.java#L264).
- `ChaosMonkeySafeLeaderTest` fails its check of zero failed index requests (expected 0, got 1) at [line 167](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeySafeLeaderTest.java#L167). `ChaosMonkeySafeLeaderWithPullReplicasTest` fails the same check at [line 209](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/cloud/ChaosMonkeySafeLeaderWithPullReplicasTest.java#L209).
- Running the three again with the same seed failed the same way. Running `ChaosMonkeySafeLeaderTest` alone with a random seed passed once, so the failures depend on the seed.
- The cause of the one failed index request is not known yet. It was an IOException from an update request.

**Recommendation:** Keep open. The seed reproduces the failures, so the next step is to find which node the failed request reached and why the wait times out.
