Status: OPEN
Tags: gate

# Gate job: SOLR-16398 replication command APIs and debug dump (JAX-RS)

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16398-replication-commands
- Head: 956d95d66a358bf7c57cf222a5d8259d8fb94e91
- Superseded head: 218aa604a4e6dc5239eeeb3645e71a876acec65f (the LinkedHashMap import fix used the diamond form under var, which inferred Map<Object, Object>; explicit type arguments restored in 956d95d66a)
- Superseded head: 07678f5155a5b46a70738dba14cbbafc8a9d1c30 (tidy clean; Error Prone failed under -Werror on two fully qualified LinkedHashMap uses in the CoreReplication response harvest; imported in 218aa604a4)
- Superseded head: b941aadf092a056d9d7ae699488c80fc02c9ed8b (tidy re-sorted the corrected JerseyResource import in DebugDump; formatting-only commit 07678f5155 on top, per the step 1 rule)
- Superseded head: 11d159b95dbddae9579809faa82a4105a5bb5176 (OpenAPI generation passed; core compile failed: DebugDump had the wrong JerseyResource package, and CoreReplication set a Long into ModifiableSolrParams for an indexVersion body field that the v1 fetchindex path never reads; the field and its mapping are removed, import fixed, in b941aadf09)
- Superseded head: 1f11ad8c2fed898c30883a5a928a9f2bd710f590 (the six replication operations passed spec validation; the remaining error was DebugDumpApi with the same missing CoreApiParameters annotation; fixed in 11d159b95d)
- Superseded head: eaf0141332748f91943b4ee704d06b2570c24ac3 (tidy clean; SolrJ OpenAPI generation failed spec validation: the six new operations lacked @CoreApiParameters, which declares the coreName path parameter for this interface; added in 1f11ad8c2f)
- Superseded head: bb6f0e7d5d71f970aca6d8c5bff30ecd81ee2e4c (tidy import order, line joins, and a duplicate static import removed in CoreReplication; formatting-only commit eaf0141332 on top, per the step 1 rule)
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16398-replication
- Ticket: SOLR-16398

## What the branch does

ReplicationApis gains POST endpoints for the six v1 replication commands (fetch-index,
abort-fetch, enable-poll, disable-poll, restore, delete-backup), implemented in CoreReplication
by translating each request to the v1 command parameters and running it through the core's
ReplicationHandler. A new DebugDump Jersey resource serves DumpRequestHandler's content, and
DumpRequestHandler registers it (getJerseyResources + registerV2, mirroring ReplicationHandler).
Not yet compiled anywhere; this gate is the first check. Watch items: the response harvest uses
NamedList.forEach with a two-argument lambda in CoreReplication and DebugDump; if NamedList's
forEach shape differs, that is the expected first compile error. Also the fetch-index body maps
indexVersion to the v1 "indexVersion" parameter.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16398-replication.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true.
3. Focused tests: any ReplicationHandler or CoreReplication test class the runner identifies
   (for example TestCoreReplication), plus DumpRequestHandler tests if present.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16398-replication.md (head gated, seed, per-leg results), flip this
  job's Status to DONE.
- Failure: record the failing leg and first error in this job and in
  receipts/SOLR-16398-replication.md; do not fix code on this job. Fixes return to the phone
  agent as new work.
