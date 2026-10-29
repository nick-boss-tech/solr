# Receipt: SOLR-16396 config paramset read APIs (JAX-RS) gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16396-config-read
- Head gated: dc89f6c64f8a8e21eccb19d995d1b6c5de5678e9
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Seed (focused tests): 16396C0FFEE001
- Verdict: PASS at the head above.

## Legs at the gated head

- Step 0 changelog YAML parse: OK (changelog/unreleased/SOLR-16396-read.yml).
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (:solr:api, :solr:core, :solr:solrj compileJava,
  :solr:core and :solr:api compileTestJava, -Pvalidation.errorprone=true): rc 0.
  The SolrJ OpenAPI generation leg inside this step passes at the gated head.
- Step 3 focused tests: V2ConfigAPIMappingTest 2/2; TestConfigOverlay 2/2;
  TestReqParamsAPI 1/1. Counts read from the fresh JUnit XML.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

## Path to the gated head (fixes by the phone agent, each pushed before re-gating)

- c0bce1c24a (as implemented): tidy import order, line joins, and one unused import
  in the mapping test; formatting-only commit f2fa83bb15 on top.
- f2fa83bb15: api compile failed because the two paramset operations had been
  inserted between the annotations and the signature of getConfigComponentByName,
  stacking duplicate @GET/@Path/@Operation annotations; moved below it in b8169ff932.
  Tidy then removed one extra blank line (commit d4aa535520).
- d4aa535520: OpenAPI generation failed: the paramset operations lacked
  @StoreApiParameters, which declares the indexType/indexName path parameters on
  ConfigApi; added in 4d12f51f7a.
- 4d12f51f7a: Error Prone failed under -Werror: the unchecked suppression in
  GetConfig.getAllParamSets sat on a local variable while the cast is on the next
  line; moved to the method in dc89f6c64f, the gated head.

## Note for landing

This branch and the gated write-side branch solr-16396-config-jaxrs both touch the
SolrConfigHandler registration region from independent main bases; expect a merge
decision if both land.
