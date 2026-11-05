🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

I found another ordering case during session replacement: if the replacement's `Disconnected` is applied before its delayed `SyncConnected`, the connected callback is correctly rejected. If that session then expires, its expiration callback waits behind the reconnect callback that is still waiting for a connection.

I put a follow-up on top of `01e820f0539cddc4697d26176d216614a776a99a` that lets the connection wait detect a terminal replacement and retry, while retaining your sequence guard. It adds five ordinary deterministic tests for state ordering and replacement expiration. The nightly stress test now waits for the disconnect state to be applied or rejected before checking recovery, and drops client connections with `halfClose()` while keeping its proxy listener open.

[Changes from your head to the offered head](https://github.com/nick-boss-tech/solr/compare/01e820f0539cddc4697d26176d216614a776a99a...dfde9bde0a348ad44e6f6ceec197d478ef11bfd0).

Linux runs at the preceding head `27a9cf105b25f1bf3d604534cf8bdd8674c470fc` passed the five new tests and nine neighboring tests. The liveness test failed at its intended assertion with your original production file, and three stale-state tests failed under a synthetic accept-all mutation. That run also found a missing Mockito assumption guard and a repeatable connection-refused failure after the stress fixture closed/reopened its listener. This offered head adds the guard and keeps the listener open. Verification of those repairs, including the same-seed stress runs and full checks, is still pending.

The branch preserves your commit ancestry. To take it with a fast-forward after verification:

```sh
git switch SOLR-18623-connection-manager-event-ordering
git fetch https://github.com/nick-boss-tech/solr.git solr-18623-reconnect-liveness
git merge --ff-only dfde9bde0a348ad44e6f6ceec197d478ef11bfd0
```
