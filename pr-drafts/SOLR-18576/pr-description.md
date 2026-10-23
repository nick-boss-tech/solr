🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18576

## What happens today

**`BasicDistributedZk2Test` can index into a node whose ZooKeeper session has just expired, and the update fails with "Updates are disabled".**

After [`ChaosMonkey.expireSession`](https://github.com/apache/solr/blob/79b23df58da/solr/test-framework/src/java/org/apache/solr/cloud/ChaosMonkey.java#L149-L169) returns, the test calls `blockUntilConnected` and then indexes ([test lines 154-164 at the merge base](https://github.com/apache/solr/blob/79b23df58da/solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java#L152-L164)). `expireSession` returns once the client reports disconnected, but the expired-session event reaches Curator later. The wait can return before the event arrives, so the update races the expiry. In a CI run on [#5194](https://github.com/apache/solr/pull/5194), the update was rejected with 503 "Cannot talk to ZooKeeper - Updates are disabled" 7 ms after the expiry was logged.

## What this change does

**The test waits for the expired-session event, then for reconnection, before it indexes.**

A connection-state listener is registered before the session is expired. The test waits on a latch that counts down on the expired-session callback, then calls `blockUntilConnected`. Only [`BasicDistributedZk2Test.java`](https://github.com/nick-boss-tech/solr/blob/56b86524e4d6c475b9d1d3a51b5375df749d20b4/solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java) changes. Production code is unchanged.

## Proof

**The proof is inconclusive by construction: the race did not reproduce before or after the change, so these runs show the test still passes, not that the CI failure is gone.**

- `BasicDistributedZk2Test`, seed `7E5494314774A841` (the seed from the failing run): 1 test, 0 failures, 0 errors. Verified 2026-10-23 at head `56b86524e4d`.
- `BasicDistributedZk2Test`, 4 random seeds: 4 tests, 0 failures, 0 errors. Verified 2026-10-23 at head `56b86524e4d`.
- Module check `:solr:core:check -x test` passes at that head.

## Limits

**The race is timing-dependent, and the cause is inferred from the CI log, not confirmed by a failing run.**

- The test already allows up to 30 seconds for the expired-session event and then for reconnection. If either wait times out, the test fails at that wait, not at the update.
- The change does not touch the production retry or update path, so the same race can still occur for other test code that expires sessions without this wait.
- Changelog: none. The change is test-only.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
