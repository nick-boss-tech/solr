# SOLR-10641 - hypothetical reproduction (nothing was compiled or run)

JIRA (2017, Tier 4, audit note "exploration/perf idea"): Noble Paul suggests using ZooKeeper's asynchronous calls and `multi` for the
state/work queues, e.g. doing several deletes of the work queue in one `multi`. On main `ZkDistributedQueue.remove(Collection)` already
batches deletes with `multi` (1000 per transaction). The one remaining pair of dependent writes is `OverseerTaskQueue.remove(event, setResult)`:
a `setData` on the response node followed by a `delete` of the request node, two round trips for every Overseer task that has a waiting caller.

## Change
When `setResult` is true, `remove` first tries one `multi` (`setData` on the response, `delete` of the request). If the transaction does not
apply (NoNode thrown, or any result carrying a non-zero error), it falls back to the old sequential code, which already tolerates a response node
that disappeared because the requestor left. The asynchronous `getData` half of the ticket is not touched. Design pick: keep the old code path as the
fallback instead of reasoning about partial failures, at the cost of one wasted transaction in the rare race.

Tests in `OverseerTaskQueueTest`: `testRemoveSetsResponseAndDeletesRequest` (happy path) and
`testRemoveWithMissingResponseNodeStillDeletesRequest` (response node deleted first, request must still go).

## Guesses to verify first
- `SolrZkClient.multi(CuratorOpBuilder...)` with two lambdas compiles (varargs of a functional interface) and either throws `NoNodeException` or
  returns results with a non-zero error on a failed transaction. Both are handled, but check which one Curator actually does.
- `CuratorTransactionResult.getError()` is 0 for success (used the same way in `ZkDistributedQueue.remove(Collection)`).
- `op.setData().withVersion(-1).forPath(path, data)` is the right Curator builder shape for `TransactionOp`.
- `zkClient.getData(watchID, null, null)` returns the response bytes for an ephemeral sequential response node.

## Fail-before
The tests pass on main as well (same observable result), so they pin behaviour rather than prove the round-trip saving; there is no cheap way to count
ZooKeeper calls without a mock. Expected verdict `NOT_PROVEN`.
