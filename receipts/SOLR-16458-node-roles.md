# Receipt: SOLR-16458 cluster node-roles listings (JAX-RS) gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16458-node-roles
- Head gated: 67a7243a93a394a521b052f8b8b4e6af5f952e84
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Seed (focused tests): 16458C0FFEE001
- Verdict: PASS at the head above.

## Legs at the gated head

- Step 0 changelog YAML parse: OK (changelog/unreleased/SOLR-16458-node-roles.yml).
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (:solr:api, :solr:core, :solr:solrj compileJava,
  :solr:core and :solr:api compileTestJava, -Pvalidation.errorprone=true): rc 0.
- Step 3 focused tests: NodeRolesTest 1/1; V2ClusterAPIMappingTest 1/1. Counts read
  from the fresh JUnit XML.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

## Path to the gated head (fixes by the phone agent, each pushed before re-gating)

- ca2675f5173 (as implemented): tidy removed one unused java.util.Set import left in
  ClusterAPI by the endpoint removal; formatting-only commit 7d93118791 on top.
- 7d93118791: compile failed because ListNodeRoles imported DistribStateManager from
  org.apache.solr.cloud instead of org.apache.solr.client.solrj.cloud, and
  CollectionsHandler lacked the ListNodeRoles import; fixed in 67a7243a93a, the gated
  head.
