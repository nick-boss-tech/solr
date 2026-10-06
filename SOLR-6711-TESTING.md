# SOLR-6711 - hypothetical reproduction (nothing was compiled or run)

JIRA: `replication?command=disablepoll` (and `disablereplication`) are lost after a restart. Earlier audit note: "feature:
persistent disablepoll". Treated here as a gap in operability (a follower silently resumes polling after a restart).
Only `disablepoll` is addressed; `disablereplication` (leader side) is not.

## Change
`ReplicationHandler`: `disablepoll` takes an optional `persist=true`. It then stores `pollDisabled=true` in the core's
`replication.properties` (the file `IndexFetcher` already keeps for replication stats and rewrites by load-modify-store, so the
key survives its updates). `enablepoll` removes the key (writes only if it was stored). `inform()` reads the key and starts
with `pollDisabled` set accordingly. Default behaviour (no `persist`) is unchanged. `IndexFetcher.storeReplicationProperties`
is a package-private copy of the write sequence of `logReplicationTimeAndConfFiles`.

## Test
`TestPersistedPollDisabled` with new `solrconfig-follower-nopoll.xml` (follower without `pollInterval`, so nothing polls):
plain disable lost on `h.reload()`, `persist=true` kept, `enablepoll` clears.

## Guesses to verify first
- `h.reload()` keeps the same data dir, and `MockDirectoryFactory` returns the same META_DATA directory for the stored file.
- `ReplicationHandler.handleRequestBody` can be called directly with a `SolrQueryRequest` that has no core context wiring
  beyond `req(...)`; `rsp.getValues().get("status")` equals `OK`.
- `loadReplicationProperties()` is safe in `inform()` (the directory factory is ready; before this change it was only used
  from request paths and `IndexFetcher`).
- A race with `IndexFetcher.logReplicationTimeAndConfFiles` (both rewrite the file) could lose the flag; not guarded.
- A v2 API for disablepoll, if any, is not changed (it would need the `persist` field).
- Not covered: a SolrCloud-managed core replacing its data dir (index.properties switching) moves `replication.properties`.

## Fail-before
Expected: on main `persist` is ignored, so after the second reload `isPollingDisabled()` is false and the assertion fails.
