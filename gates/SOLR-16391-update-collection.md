Status: OPEN
Tags: gate

# Gate job: SOLR-16391 modify-collection and ping healthcheck-file JAX-RS migration

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16391-update-collection
- Head: aae6592199e997e56a83cc80ebd5df91c0f47f04
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16391-updatecoll
- Ticket: SOLR-16391

## What the branch does

ModifyCollectionAPI (annotated) replaced by a Jersey resource (ModifyCollection) that builds the
same v1 parameters from the request body and delegates to CollectionsHandler.handleRequestBody,
returning the response's success map; registration moved from CollectionsHandler.getApis() to
getJerseyResources(). New per-core PingHealthCheck resource (enable, disable, status under
/cores/{core}/ping/healthcheck-file) delegating to new public methods on PingRequestHandler,
registered from PingRequestHandler.getJerseyResources(). The modify case was removed from
V2CollectionAPIMappingTest. Not yet compiled anywhere; this gate is the first check.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16391.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true. Watch for: the ModifyCollection
   resource fetching CollectionsHandler via COLLECTIONS_HANDLER_PATH, unused imports left in
   V2CollectionAPIMappingTest after the modify case removal, and the PingHealthCheck SolrCore
   injection (mirrors CoreReplication's constructor shape).
3. Focused tests: V2CollectionAPIMappingTest (remaining cases), plus any PingRequestHandler test
   class the runner identifies.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16391.md (head gated, seed, per-leg results), flip this job's
  Status to DONE.
- Failure: record the failing leg and first error in this job and in receipts/SOLR-16391.md;
  do not fix code on this job. Fixes return to the phone agent as new work.
