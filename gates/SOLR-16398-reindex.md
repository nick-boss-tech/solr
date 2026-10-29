Status: OPEN
Tags: gate

# Gate job: SOLR-16398 reindex-collection and overseer status (JAX-RS)

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16398-reindex
- Head: fa1dfe82b7bfcc6a859c77ce4106df51dbfb5d6f
- Superseded head: 5e595f66930a6f74487759ae99212ea2c26635b5 (tidy joined lines and reflowed javadoc in the two new resource files; formatting-only commit fa1dfe82b7 on top, per the step 1 rule)
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16398-reindex
- Ticket: SOLR-16398

## What the branch does

Two delegation-pattern Jersey resources registered from CollectionsHandler.getJerseyResources():
ReindexCollection (POST /collections/{collection}/reindex; body translated to the v1
REINDEXCOLLECTION parameters, run through CollectionsHandler.handleRequestBody via wrapParams,
success map returned) and OverseerStatus (GET /cluster/overseer/status; OVERSEERSTATUS command
delegated the same way, response values returned via a JsonAnyGetter map). Not yet compiled
anywhere; this gate is the first check. Watch items: the NamedList.forEach two-argument lambda
in OverseerStatus (same shape as the replication slice), and whether the overseer status values
arrive nested under a "success" key.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16398-reindex.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true.
3. Focused tests: V2CollectionAPIMappingTest (unchanged by this branch; run as a regression
   check), plus any reindex test class the runner identifies.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16398-reindex.md (head gated, seed, per-leg results), flip this
  job's Status to DONE.
- Failure: record the failing leg and first error in this job and in
  receipts/SOLR-16398-reindex.md; do not fix code on this job. Fixes return to the phone agent
  as new work.
