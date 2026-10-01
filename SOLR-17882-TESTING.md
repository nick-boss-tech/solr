# SOLR-17882 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** This patch was written without running
Gradle (no compile, no tidy, no tests). A reviewer must compile and test
before this goes anywhere near a PR.

## What the patch does

With the new node-roles setup, a stateless coordinator node fails every LTR
query with an NPE: `mr` is null in `LTRQParserPlugin.LTRQParser.parse()` when
it calls `mr.getModel(...)`. Registration is logged
(`registerManagedModelStore` ran from `inform()`), but the
`onManagedResourceInitialized` callback that sets `mr`/`fr` and calls
`mr.loadStoredModels()` never fires in the coordinator lifecycle.

The patch adds a `modelStore(SolrCore)` fallback on `LTRQParserPlugin`, used
by `parse()` instead of dereferencing `mr` directly:

- If `mr` is already set (normal path), behavior is unchanged.
- If `mr` is null, it resolves the `ManagedModelStore` from the core's
  `RestManager` (the authoritative holder — the resource is created there via
  `addRegisteredResource` even when the observer callback is missed), wires
  the `ManagedFeatureStore` the same way, and calls `loadStoredModels()` —
  replicating exactly what `onManagedResourceInitialized` does. The wiring is
  synchronized and cached in `mr`/`fr` for subsequent requests.
- If the managed resource doesn't exist at all, `getManagedResource` throws a
  clean `SolrException` (NOT_FOUND, "No ManagedResource registered for path:
  /schema/model-store") instead of the NPE.

Note: the fallback also calls `loadStoredModels()` because the store's models
are only materialized there (see `ManagedModelStore.onManagedDataLoadedFrom-
Storage` — data loads lazily pending the feature-store wiring). A bare
`getManagedModelStore(core)` without the wiring would return an empty store.

## Recommended reviewer commands

```bash
# copy gradle.properties into the worktree first (worktrees don't inherit it)
cp ~/workspace/solr/gradle.properties .

~/workspace/tools/solr-gradle.sh :solr:modules:ltr:spotlessApply
~/workspace/tools/solr-gradle.sh :solr:modules:ltr:compileJava -Pvalidation.errorprone=true

# targeted tests, serially (never two Gradle builds at once on this VM)
~/workspace/tools/solr-gradle.sh :solr:modules:ltr:test \
  --tests "org.apache.solr.ltr.search.*" \
  -Pvalidation.errorprone=true
```

Suggested new tests (not written):

1. `LTRQParser.parse()` with a plugin instance whose `mr` is null but whose
   core has the managed resources registered → expect the query to build
   (fallback path), not NPE.
2. Same, with no managed resources at all → expect clean `SolrException`, not
   NPE.

## Patch limits, risks, open questions

- **Not compiled or tested.** Single-file change, but the coordinator
  lifecycle is the crux and cannot be validated without a real node-roles
  cluster — the reviewer must reproduce the ticket's coordinator setup.
- **Root cause is still open**: why `onManagedResourceInitialized` never fires
  on coordinator nodes is unknown. This patch repairs the symptom at the
  dereference site (queries work via the fallback); a maintainer may prefer to
  fix the lifecycle ordering in `RestManager`/coordinator core init instead,
  which would make the fallback dead code. Flagging explicitly — do not merge
  both a lifecycle fix and this fallback without removing one.
- `loadStoredModels()` re-entrancy: only runs when `mr` was null, i.e. the
  callback never ran, so no double-load through this path. `ModelStore.
  addModel` is synchronized; models are keyed by name on re-add.
- The fallback assumes the coordinator core's `RestManager` actually holds the
  managed resources. If coordinator cores never initialize them, the fallback
  throws NOT_FOUND — still strictly better than NPE, but queries still fail
  and the lifecycle fix becomes mandatory.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).

## What the reviewer should improve

- Reproduce on a node-roles cluster: coordinator (no replicas) + LTR query.
  Confirm the fallback path fires and queries return reranked results.
- Decide fallback vs lifecycle fix with a maintainer; remove whichever loses.
- Write the two tests sketched above.
- Run spotless/tidy — formatting was done by hand, not verified.
- Remove this file before opening the upstream PR.
