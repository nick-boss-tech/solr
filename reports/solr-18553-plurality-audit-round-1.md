# SOLR-18553 plurality and consistency audit, round 1

Ticket: SOLR-18553, "Review params and path segments for plurality and consistency" (reporter Eric Pugh, Open, Major, created 2026-10-17). The ticket's own pointer is the review thread on apache/solr PR 5064.
Pinned main: `e59ce3c97ddb` (apache/solr main, committed 2026-10-26 19:44 -0400).
Scope: the v2 endpoint interfaces in `solr/api/src/java/org/apache/solr/client/api/endpoint/` (JAX-RS `@Path`, `@QueryParam`, `@PathParam`). All citations are at the pinned SHA.
Method: rung 1 only. A `git grep` tally of every path template and parameter name in that directory, then a read of each flagged site. No probe or run was needed; no finding depends on a runtime result.

## Findings

### F1. `detailed` is the only adjective-named "return more" flag

- `ListCollectionsApi.java:37`: `@QueryParam("detailed") Boolean detailed` on `GET /collections`. Behavior at `ListCollections.java:44` and `:66`.
- The same "include more, toggled by a param" role is named with the `xxxInfo` suffix elsewhere: `coreInfo`, `fieldInfo`, `sizeInfo` (`SegmentsApi.java:57`, `:58`, `:66`; `CollectionStatusApi.java:46`, `:57`, `:72`), `indexInfo` (`CoreApis.java:44`, `:53`). The size flags use `rawSizeSummary` and `rawSizeDetails` (`SegmentsApi.java:60`, `:62`).
- The PR 5064 review thread (dsmiley, 2026-10-09, on `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc`) states a preference for "details" (noun) over "detailed" (adjective): "I missed my chance to voice a preference of `details` (noun) instead of what seems like an adjective." A later comment in the same thread says "I dislike `detailed`."
- The thread is a preference, not a decision, and it is about the Ref Guide wording. The code uses `detailed` as a boolean.

### F2. `collName` and `collectionName` name the same path segment

- `collName` is the path parameter on 10 sites in 5 files: `AddReplicaPropertyApi.java:40`, `CollectionPropertyApi.java:37, 46, 55, 66`, `CollectionSnapshotApis.java:46, 64, 85`, `DeleteReplicaPropertyApi.java:43`, `InstallShardDataApi.java:34`.
- `collectionName` is the path parameter on 14 sites in 12 files: for example `CollectionStatusApi.java:44`, `DeleteCollectionApi.java:37`, `CreateShardApi.java:35`, `RenameCollectionApi.java:35`, and `DeleteReplicaApi.java:51, 69, 87`.
- Both name `/collections/{...}`. Examples: `CollectionPropertyApi.java:31` uses `/collections/{collName}/properties`; `CollectionStatusApi.java:33` uses `/collections/{collectionName}`.

### F3. The same concept has two path names: `configs` and `configsets`

- `SchemaDesignerApi.java:121`: `@Path("/configs")` under `/schema-designer` (`SchemaDesignerApi.java:52`). Its operation is "List all configSets available for schema design" (`SchemaDesignerApi.java:121`-`:131`).
- `ConfigsetsApi.java:46` and `:60`: `@Path("/configsets")`, the configset list, and `:75` onward for `/configsets/{configSetName}`.
- Both return configSets. One concept, two spellings, in the same v2 surface.

### F4. camelCase path segments where the rest of the surface uses lowercase or hyphens

- `DeleteCollectionBackupApi.java:66`: `@Path("/purgeUnused")`.
- `ConfigApi.java:76`: `@Path("/requestHandlers")`.
- Other multi-word segments are lowercase or hyphenated: `/collections/{collectionName}/balance-shard-unique` (`BalanceShardUniqueApi.java:33`), `/collections/{collectionName}/shards/{shardName}/force-leader` (`ForceLeaderApi.java:26`), `/schema-designer` (`SchemaDesignerApi.java:52`).

## Checked and not flagged

- `/node/...` (singular, for example `NodeHealthApi.java:27`, `NodeSystemInfoApi.java:26`, `NodeThreadsApi.java:25`, `NodePropertiesApi.java:27`, `NodeLoggingApis.java:31`, `GetPublicKeyApi.java:25`, `GetNodeCommandStatusApi.java:37`) against `/cluster/nodes` (plural, `ListClusterNodesApi.java:25`). This reads as the singleton (this node) versus collection (all nodes) pattern. Not flagged; a rename would be a breaking v2 change for no clear gain.
- The `nodes` query parameter on `/node/logging` (`NodeLoggingApis.java:51`) is a list, so the plural is correct.
- Plural collection segments (`/collections`, `/cores`, `/aliases`, `/fields`, `/fieldtypes`, `/dynamicfields`, `/copyfields`, `/backups`, `/snapshots`) are consistent.

## Answer to the ticket's ask

The ticket asks for plurality and consistency. Concrete items are F1 and F3 (plurality and naming of the same concept), and F2 and F4 (consistency of path naming). Items F1 to F4 each have a file and line; none was checked at runtime.

## Verdict

Findings are concrete. A draft is produced for the owner (Eric) to pick from. No code change is proposed here.

Not checked in this unit: whether any of these names are documented in the Ref Guide in a form that would also need changing (the Ref Guide is a separate tree); whether an open PR already renames any of them (the GitHub search for `SOLR-18553` returned no PR on 2026-10-27).

## Reproduce

`git grep -n '@PathParam("collName")' e59ce3c97d -- solr/api/src/java/org/apache/solr/client/api/endpoint`
`git grep -n '@QueryParam("detailed")' e59ce3c97d -- solr/api/src/java/org/apache/solr/client/api/endpoint`
`git grep -n '@Path("/configs")' e59ce3c97d -- solr/api/src/java/org/apache/solr/client/api/endpoint`
`git grep -n '@Path("/purgeUnused")\|@Path("/requestHandlers")' e59ce3c97d -- solr/api/src/java/org/apache/solr/client/api/endpoint`
