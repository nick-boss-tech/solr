# SOLR-17708: JAX-RS v2 requests are authorized twice, round 1 trace

Claim: `claims/warm-ticket-evidence-units-round-1-unit-1-SOLR-17708-vm2.md`. Job: `assignments/warm-ticket-evidence-units-round-1.md`, unit 1.
Base: apache/solr main `13c693d805f6541f7cec684cd27d28eef7b3b614`, read with `git grep`. Ticket read through the Jira REST API on 2026-10-25 (Open, reporter Jason Gerlowski, unassigned, updated 2026-10-23, one comment from 2025-03-19 suggesting an override).
Paths are under `solr/core/src/java/org/apache/solr/` unless noted.

## Result

The ticket is confirmed for both container-level and core-level v2 APIs on main. Authorization runs in two layers, in this order:

1. `HttpSolrCall.call()`, `servlet/HttpSolrCall.java:480-490`, before any dispatch. It calls `AuthorizationUtils.authorize` with the context from `getAuthCtx()` (line 1035). The ticket's cited range `L534-L544` is stale; the code is at 480-490.
2. `SolrRequestAuthorizer.filter()`, `jersey/SolrRequestAuthorizer.java:54-91`, which runs as a Jersey request filter inside `invokeJerseyRequest`.

The ticket's suggested fix does not compile as written: `private boolean shouldAuthorize()` is declared at `servlet/HttpSolrCall.java:608`, so `V2HttpCall` cannot override it.

## Why both layers run

- `HttpSolrCall` skips authorization only for `REMOTEPROXY` and `FORWARD` actions (`HttpSolrCall.java:480-482`). Requests that reach Jersey are local, so layer 1 runs for them.
- The dispatch comes after layer 1. `switch (action)` is at `HttpSolrCall.java:493`. Container v2 requests go to `V2HttpCall.handleAdminOrRemoteRequest` (`api/V2HttpCall.java:371`) and `handleAdmin` (`:395`). Core requests go to `executeCoreRequest` (`:426`). Both call `invokeJerseyRequest` (`:377`, `:397`, `:431`, `:449`).
- The Jersey route match happens inside `invokeJerseyRequest`, after layer 1. The `jerseyResourceFound` flag is set there (`:376-384`). So at the point of layer 1 the code does not yet know whether Jersey will serve the request.
- Both Jersey applications register the filter. `CoreContainerApp` registers `SolrRequestAuthorizer` at `jersey/JerseyApplications.java:44`. `SolrCoreApp` extends `CoreContainerApp` (`:114`) and inherits it. `PluginBag.java:100-103` picks `SolrCoreApp` for core handlers and `CoreContainerApp` for container handlers.

So the double check covers container-level and core-level JAX-RS requests, not only one of them.

## Endpoints traced (one per level)

Container level: `ClusterFileStore.getFile`.
- Registered by `core/CoreContainer.java:841` (`registerV2Api(ClusterFileStore.class)`).
- Implementation `filestore/ClusterFileStore.java:159-160`, annotated `@PermissionName(FILESTORE_READ_PERM)`.
- Route fragments: `solr/api/src/java/org/apache/solr/client/api/endpoint/ClusterFileStoreApis.java:40` `@Path("/cluster/filestore")` and `:80` `@Path("/files{filePath:.+}")`. The `/api` prefix is not checked in this unit.

Core level: `GetSchemaField.getFieldInfo`.
- Registered through `handler/SchemaHandler.java:333`, which returns `GetSchemaField` from `getJerseyResources()`.
- Implementation `handler/admin/api/GetSchemaField.java:82-83`, annotated `@PermissionName(SCHEMA_READ_PERM)`.
- Route fragments: `solr/api/src/java/org/apache/solr/client/api/endpoint/GetSchemaApi.java:42` `@Path(INDEX_PATH_PREFIX + "/schema")`, `:57` `@Path("/fields/{fieldName}")`, method at `:62`.

For each endpoint, the trace is the same:
- Layer 1 builds its context in `getAuthCtx()` (`HttpSolrCall.java:1035-1048`). Its request type comes from the resource path, for example `/select` and `/get` are READ and `/update` is WRITE.
- Layer 2 builds its context in `getAuthzContext()` (`SolrRequestAuthorizer.java:105-138`). Its permission name comes from the resource method's `@PermissionName` (line 123). Its collection list comes from the request context (line 65).

Layer 1 uses the path and query parameters. Layer 2 uses the Jersey resource method and the collection list stored in the request context. Those are two different inputs.

## What the trace does not show

- Whether the two decisions can disagree. Both call the same plugin with contexts built from different inputs. The trace shows the inputs differ, not that the outputs differ. No test was run.
- Whether any failure is visible to a user. A layer-1 failure returns early (`HttpSolrCall.java:486-489`), so layer 2 only runs for requests that passed layer 1. The visible cost is the duplicate check.
- The intent in the ticket's 2025 comment. The ticket says the skip logic "was never added." That is consistent with the code, but git history was not searched in this unit.

## Fix options, for the reporter's answer

- Remove the layer 2 check from the Jersey applications. That leaves layer 1 covering every route. It is the smaller change, but it depends on layer 1 having the same permission data as the resource annotation. Today layer 1 has no resource-method permission name, so this option would need a new source for it.
- Skip layer 1 for requests that Jersey will serve. The route match happens only inside `invokeJerseyRequest`, after layer 1, so this needs a route check before layer 1 or a restructured dispatch. The `shouldAuthorize()` override suggested in the ticket cannot do this as written, because the method is private and the route is not known at that point.

## Limits

- Code reading and `git grep` at the stated commit. No Solr instance, no build, no test.
- Two endpoints traced, one per level. Other JAX-RS resources were not sampled.
- No Jira or GitHub write. The draft is not reviewed by Nick.
