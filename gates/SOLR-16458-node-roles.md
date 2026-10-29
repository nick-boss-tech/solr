Status: OPEN
Tags: gate

# Gate job: SOLR-16458 cluster node-roles listings (JAX-RS)

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16458-node-roles
- Head: 67a7243a93a394a521b052f8b8b4e6af5f952e84
- Superseded head: 7d93118791bc54455ce647b252e18c727eea8d11 (tidy clean; compile failed: ListNodeRoles imported DistribStateManager from the wrong package, and CollectionsHandler lacked the ListNodeRoles import; fixed in 67a7243a93a)
- Superseded head: ca2675f5173c03e9c38c0bb5701e7e98ed8f0234 (tidy removed one unused java.util.Set import left in ClusterAPI by the endpoint removal; formatting-only commit 7d93118791 on top, per the step 1 rule)
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16458-noderoles
- Ticket: SOLR-16458 (checked before taking: PR #4171 under this ticket migrated NodeHealth
  only; the ticket is Open and unassigned on Jira, last comment 2026-10-06; the five node-role
  listings were still annotated on main)

## What the branch does

The five annotated ClusterAPI node-role endpoints move to a ListNodeRoles Jersey resource
(NodeRolesApi under /cluster/node-roles) reading the same ZooKeeper tree; the readRecursive
helper moves with it. The endpoints are removed from ClusterAPI (its other endpoints and the
static getNodesByRole helper stay), and the resource is registered from
CollectionsHandler.getJerseyResources(). Not yet compiled anywhere; this gate is the first
check.

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16458-node-roles.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true. Watch for unused imports left in
   ClusterAPI beyond the HashMap import already removed.
3. Focused tests: NodeRolesTest if the runner's environment supports it (it is a SolrCloud
   test), plus V2ClusterAPIMappingTest if present.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16458-node-roles.md (head gated, seed, per-leg results), flip this
  job's Status to DONE.
- Failure: record the failing leg and first error in this job and in
  receipts/SOLR-16458-node-roles.md; do not fix code on this job. Fixes return to the phone
  agent as new work.
