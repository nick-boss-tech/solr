# Surface dossier: SolrClientCache

Pinned revision: `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main). Callers found by `git grep` at that revision in this pass. Written by vm2, 2026-10-20T10:51:03Z (UTC). Confirmation review by a second host is pending.

## Naming note

The assignment names `SolrClientCache` with a core-side subclass `InternalSolrClientCache`. At the pinned revision, `git grep -l InternalSolrClientCache 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` finds nothing. The class lives in `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/SolrClientCache.java` (line 37). Its core-side owner is `CoreContainer`. The subclass named in the assignment does not exist here, so the dossier covers the base class and its owner.

## 1. What the surface is and what depends on it

`SolrClientCache` (`implements Closeable`) caches SolrJ clients for streaming expressions: `HttpSolrClient` by base URL (`getHttpSolrClient`, line 171) and `CloudSolrClient` by connection (`getCloudSolrClient`, line 121), plus a connection validator and default ZK host (lines 80-101). Streaming (`TupleStream` sources and the cross-collection join) gets its clients from it.

## 2. Callers and consumers (grep at 498a09aea97aac86c2cc7ac114113b946d249942)

Production:
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java`: owns the instance (field line 284; created at line 802 with the node's `solrClientProvider` client; a connection validator is set at 805; default ZK host set at 832; closed in the shutdown path near line 1301). `getSolrClientCache()` (line 713) is the accessor.
- `solr/core/src/java/org/apache/solr/handler/StreamHandler.java`: takes the cache from `CoreContainer` (line 104) and gives it to the stream context (line 233).
- `solr/core/src/java/org/apache/solr/handler/ExportHandler.java`: takes the cache from `CoreContainer` (line 102) and sets it on the initial stream context (line 117).
- `solr/core/src/java/org/apache/solr/handler/GraphHandler.java`: uses the cache for graph traversal (listed by grep; lines not reread here).
- `solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java`: listed by grep; lines not reread here.
- `solr/core/src/java/org/apache/solr/search/join/CrossCollectionJoinQParser.java` and `CrossCollectionJoinQuery.java`: cross-collection join, listed by grep.
- `solr/core/src/java/org/apache/solr/cli/StreamTool.java`: CLI streaming tool, listed by grep.
- Tests: `solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/SolrClientCacheTest.java` (named by path; not reread here).

## 3. Invariants and contracts

- Ownership. The cache closes the clients it created (`close()`, line 202, under `synchronized`, guarded by `isClosed` compare-and-set). Clients it was given (the node's own client, passed to the constructor at line 62) are not closed by it.
- Thread safety. `getCloudSolrClient` and `getHttpSolrClient` are `synchronized` on the cache. The two maps (`httpSolrClients` line 50, `cloudSolClients` line 51) are plain `HashMap`s, so they are only safe under that lock.
- Lifecycle. `ensureOpen` (line 216) throws `AlreadyClosedException` after close; callers must not use a cache after the `CoreContainer` shuts down.
- Connection rules. `validateConnection` runs the connection validator set by `CoreContainer` (line 805), and `zkHostRejectionMessage` (line 94) refuses ZK host strings where the setting does not allow them.

## 4. Live work

Fork branches whose names point at streaming or cross-collection work, by name only (not commit-checked here): none found by the name grep `streaming\|join\|solrclient` in this pass beyond the `ci/` list in `pool/guides/surface-indexfetcher.md`. Recent upstream changes that touch the file are in section 5.

## 5. Recent history (path-scoped, at 498a09aea97aac86c2cc7ac114113b946d249942)

- `b66758a1d0` (2026-09-21) SOLR-18224: `allowZkHosts` setting for cross-collection join and streaming zkHost.
- `5352b58c42` (2026-08-28) SOLR-18352: deprecated-code status wording.
- `0c77e420e8` (2026-08-25) SOLR-18380: removed `CloudSolrClient.connect()` and `ClusterStateProvider.connect()`.
- `b2e1f8ed4a` (2026-06-02) SOLR-18130: new `solrConnection` parameter in solrj-streaming, with HTTP support.
- `8d3f995c6a` (2026-05-17) SOLR-17968: `HttpSolrClient` as the new base class.
- `bf610531f1` (2026-04-09) SOLR-18130: universal connection string for `CloudSolrClient.Builder`.

Review requests for these changes were not located in this pass.

## 6. Danger spots

- Ownership split. A change that closes the node's shared client from inside the cache, or stops the cache from closing its own clients, breaks either the shared client or leaks clients. The `close()` method (line 202) is the only place that closes the cache's own clients.
- Maps under the lock. Reading `httpSolrClients` or `cloudSolClients` outside `getCloudSolrClient`/`getHttpSolrClient` or `close()` is unsafe, because both maps are plain `HashMap`s.
- Shutdown order. `CoreContainer` closes the cache during shutdown (line 1301 area). A stream that still holds the cache after that gets `AlreadyClosedException` from `ensureOpen`; a change to shutdown order can turn into a user-visible error.
- The connection validator and default ZK host are set on the cache by `CoreContainer` after construction (lines 805 and 832). A cache used before those lines runs without them.

## Limits

- Callers are grep-verified at the pinned revision. Callers reached only by reflection or configuration are not.
- The named subclass `InternalSolrClientCache` is not present at this revision; if it lands later, the dossier needs a section on it.
