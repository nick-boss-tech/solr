# Skip record: SOLR-13570

- Ticket: SOLR-13570, Unable to control timeout for requests from Overseer to other nodes.
- Verdict: close-fixed.
- Draft branch: solr-13570-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier record (vm5, 2026-10-18) is replaced. It cited an owner decision for close-fixed that is not in the coordination files; this record rests on the evidence below.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-06-21, updated 2019-07-26, no fix versions. The ticket says Overseer requests use the update shard handler's HTTP client, so the timeout should follow `distribUpdateConnTimeout` in solr.xml, but since SOLR-11004 the Overseer's calls use a hardcoded timeout. The 2019-06-23 comment proposes removing the `getShardHandler(getDefaultHttpClient())` calls in the collection command classes so the shard handler's configured client is used.

## Code reading

Fixing commit: `a80eb84d56` (SOLR-14354, "HttpShardHandler send requests in async", ancestor of the pin). Its diff removes 12 `getDefaultHttpClient` lines and adds 15 no-arg `getShardHandler()` lines across 28 files, including `solr/core/src/java/org/apache/solr/cloud/OverseerNodePrioritizer.java` and the collection command classes.

Changed code present at the pin:

- `solr/core/src/java/org/apache/solr/cloud/OverseerNodePrioritizer.java:140`: `shardHandlerFactory.getShardHandler()` (no default client argument).
- `solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:532`: `shardHandlerFactory.getShardHandler()`.
- `solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java:292-310`: the shard handler's client takes its connection and socket timeouts from the factory configuration (`PROP_CONNECTION_TIMEOUT`, `PROP_SO_TIMEOUT`), so Overseer requests that go through it follow that configuration.

Greps at the pin:

- `git grep -n getDefaultHttpClient 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` returns no matches (empty).
- `git grep -n socketTimeoutMillis ... -- solr/core/src/java/org/apache/solr/cloud/overseer solr/core/src/java/org/apache/solr/cloud/Overseer*.java solr/core/src/java/org/apache/solr/cloud/api` returns no matches (empty).
- `120000` in `solr/core/src/java/org/apache/solr/cloud` has two hits, `SyncStrategy.java:397` (idle timeout for sync) and `MigrateCmd.java:333` (leader wait); neither is an Overseer shard-request timeout.

## History

- `a80eb84d56` (SOLR-14354): ancestor of the pin. The fix commit.
- `7eb46b3f03` (SOLR-16595, "Standardize Solr Client Builders handling of times"): ancestor of the pin. Not verified in this pass to be the commit that removed the hardcoded 120000 value; it is not cited as the fix.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed by the rubric.

## Gap analysis

- The fix is the routing change in `a80eb84d56`, not a removal. The Overseer's requests now use the configured shard handler client, which is the change the ticket's 2019 comment proposes.
- The earlier record called the case close-obsolete under the rubric and cited an owner decision for close-fixed. No written owner decision for this ticket is in the coordination files (checked: the assignment, the batch audit, and the owner complaints file), so the verdict rests on this record's evidence.
- Not verified in this pass: that a configured `soTimeout` change in solr.xml reaches Overseer requests end to end. The code reading shows the path, not a run.

## Verdict justification

close-fixed: the fixing commit `a80eb84d56` is named, it is an ancestor of the pin, and the changed call sites are present at the pin. The default-client and hardcoded-timeout searches are empty at the pin.

Missing for the close: none for the rubric's close-fixed minimum. An end-to-end check of solr.xml timeout reaching an Overseer request would strengthen the record but is not required by the rubric.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
