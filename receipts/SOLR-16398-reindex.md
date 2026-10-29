# Receipt: SOLR-16398 reindex-collection and overseer status (JAX-RS) gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16398-reindex
- Head gated: 528a9eb954941ad87c9ca3b636dd9495aa245551
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Seed (focused tests): 16398C0FFEE001
- Verdict: PASS at the head above.

## Legs at the gated head

- Step 0 changelog YAML parse: OK (changelog/unreleased/SOLR-16398-reindex.yml).
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (:solr:api, :solr:core, :solr:solrj compileJava,
  :solr:core and :solr:api compileTestJava, -Pvalidation.errorprone=true): rc 0.
- Step 3 focused tests: V2CollectionAPIMappingTest 4/4; ReindexCollectionTest 7/7.
  Counts read from the fresh JUnit XML.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

## Path to the gated head (fixes by the phone agent, each pushed before re-gating)

- 5e595f66930 (as implemented): tidy joined lines and reflowed javadoc in the two new
  resource files; formatting-only commit fa1dfe82b7 on top.
- fa1dfe82b7: compile failed because CollectionsHandler lacked the OverseerStatus and
  ReindexCollection imports; fixed in 528a9eb9549, the gated head.

## Watch items from the job

- The NamedList.forEach two-argument lambda in OverseerStatus compiled clean under
  Error Prone.
- Whether the overseer status values arrive nested under a "success" key is not
  exercised by any test in this gate; no test calls the new GET endpoint directly.
  The delegation returns every response value except responseHeader, so a nested
  success map would pass through under its own key either way.
