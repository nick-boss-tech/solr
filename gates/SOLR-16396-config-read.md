Status: OPEN
Tags: gate

# Gate job: SOLR-16396 config paramset read APIs (JAX-RS)

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16396-config-read
- Head: d4aa535520425e9309cc69975c65c865f42a7d0a
- Superseded head: b8169ff932bbec2e71a29acbece53bceda05da7f (tidy removed one extra blank line left by the paramset move in ConfigApi; formatting-only commit d4aa535520 on top, per the step 1 rule)
- Superseded head: f2fa83bb153258d1c381e8d0618091bbbd0e47a0 (tidy clean; api compile failed: the paramset operations were inserted between the annotations and the signature of getConfigComponentByName, stacking duplicate annotations; moved below it in b8169ff932)
- Superseded head: c0bce1c24a5a0b7f7bd6e10a228a9b4151e6a76e (tidy import order, line joins, and one unused import in the mapping test; formatting-only commit f2fa83bb15 on top, per the step 1 rule)
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16396-configread
- Ticket: SOLR-16396 (companion to the gated write-side branch solr-16396-config-jaxrs; both are
  based on main independently, so expect the SolrConfigHandler registration region to need a
  merge decision if both land)

## What the branch does

The last two annotated config read endpoints (GET /config/params and GET /config/params/{name},
held by GetConfigAPI) move to ConfigApi.Get and are implemented in the existing GetConfig Jersey
resource from the core's RequestParams (new ConfigParamsResponse: params map + znodeVersion).
GetConfigAPI is deleted with its SolrConfigHandler registration, and the V2ConfigAPIMappingTest
case asserting the annotated endpoints is removed. Recon notes: the rest of the config read
surface (full config, overlay, znodeVersion, requestHandlers, migrated components) is already
JAX-RS on main, and the sheet's "Fetch Config File" row maps to the configset file download,
which is already JAX-RS in ConfigsetsApi; neither is touched here. Not yet compiled anywhere;
this gate is the first check.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16396-read.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true.
3. Focused tests: V2ConfigAPIMappingTest (remaining cases), TestConfigOverlay, TestReqParamsAPI
   if present.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16396-read.md (head gated, seed, per-leg results), flip this job's
  Status to DONE.
- Failure: record the failing leg and first error in this job and in receipts/SOLR-16396-read.md;
  do not fix code on this job. Fixes return to the phone agent as new work.
