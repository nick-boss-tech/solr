🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@gerlowskija, here is a concrete version of the test suggestion from earlier, on top of `6f72e0cac151`.

In `testStartupWaitsForLeftoverEphemeralNodesToDisappear`, the deleter thread is joined in `finally` before the absence assertion runs. If `checkForExistingEphemeralNode()` returned early, the join would still wait for the deleter to remove the leftover paths, and the assertions after it would pass. The test would not notice the early return.

The change in `ZkControllerTest.java` moves the absence assertion to run immediately after the check returns, before the join. The join stays in `finally` with a 30 second bound, the deleter's exception is captured and checked after the join, and the test asserts that the deleter thread has finished. The production code is not touched.

Diff of this change only: https://github.com/nick-boss-tech/solr/commit/f00c7bde83bf5d82ac4607ecf086a4397eca563b
Compare from the PR head: https://github.com/nick-boss-tech/solr/compare/6f72e0cac151d139626299a680f90a7c9c123ceb...f00c7bde83bf5d82ac4607ecf086a4397eca563b

What I ran, at the exact commits:

- With the body of `checkForExistingEphemeralNode()` emptied in a scratch tree at `6f72e0cac151`, the test as it stands passes. So it does not catch that early return.
- With the same emptied body at `f00c7bde83bf`, the changed test fails at the new assertion, with `should be gone when the check returns` for `/node_roles/overseer/preferred/127.0.0.1:8983_solr`.
- At `f00c7bde83bf`, unmodified, `ZkControllerTest` runs 13 tests with 0 failures and 0 errors. Two tests are skipped by their existing `@Ignore` annotations.

Limits: all of this was run on one Linux aarch64 machine, not on Windows or in CI. The one second delay before the deleter starts still provides the timing margin, so the test depends on that margin as the original did. A tighter handshake would need an observable point inside the production wait, which I did not add.

Would a test-only change like this be useful here?
