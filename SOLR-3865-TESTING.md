# SOLR-3865 - hypothetical reproduction (nothing was compiled or run)

JIRA (2012, 4.0): `CloudSolrServer` leaked a ZooKeeper connection when given a wrong zk connection string. The audit note said
"class removed". The class is gone, but the same lifecycle gap exists in its successor:
`ZkClientClusterStateProvider` connects lazily in `getZkStateReader()`, and `close()` only did anything when a
`ZkStateReader` already existed (`if (false == isClosed && zkStateReader != null)`). A client closed before its first request
therefore stayed "open"; a later call (or a concurrent request thread) went through `getZkStateReader()`, built a new
`ZkStateReader` plus `SolrZkClient`, and nothing ever closed it.

## Change
`close()` always sets `isClosed`; it closes the reader only when one exists and the provider owns it. `getZkStateReader()` then
throws `AlreadyClosedException` after any close. New `ZkClientClusterStateProviderCloseTest` (no ZooKeeper server needed: a
provider for `127.0.0.1:1` with a 200 ms connect timeout).

## Guesses to verify first
- Before the fix the test's `getZkStateReader()` fails with a connect timeout (not `AlreadyClosedException`) - confirms the leaked attempt.
- No caller closes a `CloudSolrClient` and then keeps using it (a grep for reuse after `close()` found none, not exhaustive).
- `ZkStateReader`'s constructor failure path (zk == null in the catch) is not changed.

## Fail-before
Expected: `testCloseBeforeFirstUseIsFinal` and `testCloseIsIdempotent` fail on `upstream/main`. Enqueue with `-WithFailBefore`, module
`:solr:solrj-zookeeper:test`.
