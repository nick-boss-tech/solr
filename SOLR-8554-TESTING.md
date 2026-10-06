# SOLR-8554 - hypothetical reproduction (nothing was compiled or run)

JIRA: "RebalanceLeader and ForceLeader APIs should be part of OverseerCollectionMessageHandler" (2016). Earlier audit note:
"refactor: move APIs into overseer handler". The refactor itself (running through the Overseer for serialization and async) is a
design change and is NOT attempted. Varun's patch also listed concrete defects: "ForceLeader never waited for the shard
responses to be collected", and the example of a shard deleted while FORCELEADER runs. Both still show on main in
`ForceLeader.doForceLeaderElection`.

## Change
The wait loop moved into package-private `ForceLeader.waitForActiveLeader(Supplier<Slice>, attempts, pauseMs, collection, shard)`:
- no active leader after 9 x 5 s: now `SolrException` SERVER_ERROR "Couldn't force an active leader ..."; on main it logged at
  INFO and the API answered OK (the caller could not tell the command had no effect).
- the shard vanishes while waiting: the supplier returns null (cluster state read with `getCollectionOrNull`) and the method
  throws "was removed while waiting for a forced leader"; on main `collection.getSlice` / `slice.getLeader()` threw an NPE
  (or `getCollection` a different exception for a deleted collection).
- `InterruptedException` restores the interrupt flag before wrapping.
Behaviour change: a timeout is now a 500. Callers (scripts, `ForceLeaderTest`) that tolerated the silent success see an error.

## Test
`ForceLeaderWaitTest` (Mockito mocks of `Slice`/`Replica`, 1 ms pauses): returns after 3 polls once the leader is ACTIVE; throws
when it never is; throws when the supplier returns null.

## Guesses to verify first
- `Slice` and `Replica` can be mocked with Mockito (non-final, `getLeader()` / `getState()` not final); mock `toString` is fine
  in the message.
- `ForceLeaderTest` (cloud) still passes: it expects an active leader after the call, which now also fails fast if none.
- Nothing else catches the old silent behaviour; the v1 handler maps the exception to a 500 like other `SolrException`s.
- RebalanceLeaders and the move into the Overseer are untouched.

## Fail-before
Expected: on main `waitForActiveLeader` does not exist, so the test does not compile (the strongest possible fail-before).
