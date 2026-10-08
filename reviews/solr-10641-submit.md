# solr-10641-submit

- Branch: origin/solr-10641-submit
- Head: 4ab4bd2040f3 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/cloud/OverseerTaskQueue.java` (new `setResultAndRemoveInOneTransaction`, called first in `remove(event, true)`), `solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java` (two new tests), `changelog/unreleased/SOLR-10641-overseer-task-queue-remove-multi.yml`, and `SOLR-10641-TESTING.md` (hypothetical-reproduction note, kept in place).
- Verdict: Nearly
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code. Patches: none.

## Premise check (hypothetical-reproduction handoff)

The handoff says: on main, `ZkDistributedQueue.remove(Collection)` already batches deletes, and the one remaining pair of dependent writes is `OverseerTaskQueue.remove(event, true)`, a `setData` on the response node followed by a `delete` of the request node.

- VERIFIED: `ZkDistributedQueue.remove(Collection)` batches deletes with `multi` in chunks of 1000 (`ZkDistributedQueue.java:219-222`). The JIRA's "multiple deletes from workQueue" idea is already on main.
- VERIFIED: on base, `OverseerTaskQueue.remove(event, true)` does `setData(responsePath, ...)` then `delete(path, -1)` as two sequential calls (base lines 127-141; the branch leaves those lines in place after the new early return).
- VERIFIED: `setResult` is true only for synchronous callers, which create a response node (`OverseerTaskQueue.java:105-113` javadoc, `:257` in `offerAndWait`).
- HYPOTHESIS: that one fewer ZooKeeper round trip per synchronous task is a real saving. Not measured. The author's own TESTING note expects the tests to show nothing about it.
- NOT TOUCHED: the JIRA's asynchronous `getData` half (`research/jira-context/SOLR-10641.json`). The TESTING note says so explicitly.

So the premise holds as written. The saving itself is unproven, and the ticket is an exploration ticket ("Explore asynchronous read/write ...").

## Findings (ranked)

MEDIUM (verified): The fail-before gate cannot pass as designed. `testRemoveSetsResponseAndDeletesRequest` and `testRemoveWithMissingResponseNodeStillDeletesRequest` assert observable results that base code already produces, so both should pass on main too. The TESTING note expects `NOT_PROVEN`. Under the round's gate, a `NOT_PROVEN` job stays queued and does not reach SUCCESS. Owner call: (a) ship as a perf-only change with an explicit exemption from fail-before; (b) add a test that counts ZooKeeper transactions, which needs a wrapper or mock and is not cheap; or (c) drop the change and close the exploration ticket with the finding. Not decided here.

MEDIUM (hypothesis, unverified): Curator and ZooKeeper failure shape. The new code catches only `KeeperException.NoNodeException` (`OverseerTaskQueue.java:160-161`). If a failed transaction throws a different `KeeperException` (for example RUNTIMEINCONSISTENCY, if that is what the client reports first), `remove()` throws and skips the fallback that the old code would have taken. Evidence that the throw case is not the live one: `ZkDistributedQueue.remove(Collection)` (lines 222-229) reads per-op `getError()` results and does not catch from `multi`, which suggests Curator returns failed results instead of throwing. Those results are handled by the `allMatch(getError() == 0)` path. The TESTING note's guesses 1 and 2 are still open. Not confirmed against Curator or ZooKeeper source.

LOW (verified): Test coverage. `testRemoveSetsResponseAndDeletesRequest` covers the multi success path. `testRemoveWithMissingResponseNodeStillDeletesRequest` covers the fallback, with the response deleted first. Neither asserts that one transaction was issued, and neither covers the race where the request node is already gone (the multi fails on the delete, the fallback runs, and the setData writes a response for a removed request, as the old code does).

LOW (verified): The changelog fragment uses `type: other`. That is a valid value. Whether an internal ZooKeeper change needs a changelog entry at all is an owner call.

## Verified correct (by reading; not run)

- `SolrZkClient.multi(CuratorOpBuilder...)` exists and returns `List<CuratorTransactionResult>` (`solr/solrj-zookeeper/.../SolrZkClient.java:764-767`). The branch's `List<...>` assignment matches. The batch code's `Collection<...>` assignment also compiles.
- `multi` wraps the Curator transaction in `runWithCorrectThrows` (`SolrZkClient.java:769-783`), which rethrows `KeeperException` unchanged. So the `NoNodeException` catch can fire if Curator throws one.
- The lambda shapes follow the existing `op -> op.delete().withVersion(-1).forPath(...)` pattern. `setData().withVersion(-1).forPath(path, data)` is the matching builder shape for `CuratorOp`. Not compiled.
- ZooKeeper `multi` is atomic. A failed transaction changes nothing, so the fallback sees the same state the old code saw. On success the early return is the only difference from the old code.
- The fallback is the old code, unchanged (`OverseerTaskQueue.java:127-141`), so the failure path behaves as before.
- `zkClient.exists(String)` returns `Boolean` (`SolrZkClient.java:361`), so `assertFalse(msg, zkClient.exists(...))` compiles through unboxing.
- `zkClient.delete(String, int)` (`SolrZkClient.java:320`), `zkClient.getData(String, Watcher, Stat)`, `createResponseNode()` (returns the full path, `OverseerTaskQueue.java:287-289`), `QueueEvent.getId()` and `setBytes(byte[])` (`:360`, `:364`), and `peekTopN(int, Predicate<String>, long)` (`:291`) all match how the tests call them.
- `OverseerTaskQueueTest.java` imports `java.util.List`, `java.nio.charset.StandardCharsets`, and `org.junit.Test` (lines 19-30).
- The changelog fragment matches the upstream format (title, `type`, authors, links).
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions posed (not decided, no patch made)

1. Fail-before gate: accept a perf-only change without a `FAIL_BEFORE` proof, add a counting test, or drop the change (see the first MEDIUM).
2. Scope of an exploration ticket: is a single `multi` on the Overseer response path the right answer to SOLR-10641, given that the batch-delete idea is already on main and the async `getData` half is untouched?

## Not checked

- Nothing was compiled, run, or tested. Compile-level checks were done by reading API signatures.
- Curator's behavior on a failed transaction (throw or result, and which error code) was not confirmed. No Curator or ZooKeeper source is in the repo.
- The round-trip saving was not measured.
- The asynchronous `getData` half of the ticket was not evaluated.
- Other callers of `OverseerTaskQueue.remove(event, true)` were not enumerated. The review covers the synchronous `offerAndWait` path.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
