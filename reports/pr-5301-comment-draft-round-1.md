🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

I found another ordering case during session replacement: if the replacement's `Disconnected` is applied before its delayed `SyncConnected`, the connected callback is correctly rejected. If that session then expires, its expiration callback waits behind the reconnect callback that is still waiting for a connection.

I put a follow-up on top of `01e820f0539cddc4697d26176d216614a776a99a` that lets the connection wait detect a terminal replacement and retry, while retaining your sequence guard. It adds five ordinary deterministic tests for state ordering and replacement expiration. The nightly stress test now waits for the disconnect state to be applied or rejected before checking recovery.

[Changes from your head to the offered head](https://github.com/nick-boss-tech/solr/compare/01e820f0539cddc4697d26176d216614a776a99a...19579b2ed2397929c0cc903a6c682331cc865586).

Linux verification of this follow-up is pending, including the focused tests, the explicitly enabled nightly class, formatting and root checks, and a failure on the original PR production code. I do not have passing execution results for this follow-up yet.

The branch preserves your commit ancestry. To take it with a fast-forward after verification:

```sh
git switch SOLR-18623-connection-manager-event-ordering
git fetch https://github.com/nick-boss-tech/solr.git solr-18623-reconnect-liveness
git merge --ff-only 19579b2ed2397929c0cc903a6c682331cc865586
```
