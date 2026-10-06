# SOLR-4502 - hypothetical reproduction (nothing was compiled or run)

JIRA: creating a core with `CoreContainer.create` on a container whose `load()` was never called gives a core
whose searches NPE (the shard handler factory is created in `load()`). Alan Woodward's last comment: the
usage is "trappy". The audit note said "fixed in 4.9/6.0", which is Uwe's bulk version move, not a fix.
On `upstream/main` `shardHandlerFactory` is still only assigned in `load()` and `create(...)` has no guard.

## Change
`CoreContainer.create(name, path, params, newCollection)` throws SERVER_ERROR naming `load()` when
`shardHandlerFactory == null` (checked before `inFlightCreations` is touched).

## Test (guessed)
`TestCoreContainer.testCreateBeforeLoadIsRejected`: construct without `load()`, expect SolrException containing "load()".

## Guesses to verify first
- `shardHandlerFactory` is a reliable "loaded" marker (set at the top of `load()`, before any core is created).
- No test or helper creates cores on an unloaded container on purpose (e.g. subclassed or mocked containers). Grep for `new CoreContainer(` without `load()`.
- `shutdown()` on a never-loaded container is safe (the test calls it).

## Fail-before
Remove the guard: `create` proceeds into core construction and fails differently (or succeeds); no "load()" message.
