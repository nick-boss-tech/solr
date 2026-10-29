Status: OPEN
Tags: gate

# Gate job: SOLR-16390 ZooKeeper v2 APIs (JAX-RS)

Requested by: phone agent (Nick's directed v2 implementation push, 2026-10-28)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16390-zookeeper-jaxrs
- Head: 66d3775134e8169952bd1c1d97a28befac7c29f6
- Base: origin/main 74197140a666d4f0396b68484c2bf9df7e79221e
- Worktree if needed: ~/workspace/solr-worktrees/16390-zk
- Ticket: SOLR-16390 (the sheet's ZooKeeper rows; the ticket covers the v2 cluster migration)

## What the branch does

New JAX-RS APIs GET /api/cluster/zookeeper/files (tree at a path given in the URL or as a query
parameter, optional znode detail) and GET /api/cluster/zookeeper/status, backed by the existing
v1 handlers (ZookeeperInfoHandler.fetchPathView, ZookeeperStatusHandler.fetchZkStatus) so both
APIs serve the same content. Registered from the handlers' getJerseyResources. The v2 files API
refuses the /security.json path (the v1 API requires security-read there; the Jersey resource
carries only zookeeper-read). The Admin UI graph view stays v1-only.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16390.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true.
3. Focused tests: no dedicated test class exists for these endpoints; if the module check leg
   surfaces a natural home, note it in the receipt rather than writing tests on this job. Pay
   attention to any test enumerating registered v2 APIs or Jersey resources, and to
   ZookeeperInfoHandler / ZookeeperStatusHandler tests.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16390.md (head gated, seed, per-leg results), flip this job's
  Status to DONE.
- Failure: record the failing leg and first error in this job and in receipts/SOLR-16390.md;
  do not fix code on this job. Fixes return to the phone agent as new work.
