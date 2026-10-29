# Receipt: SOLR-16390 cluster set-ratelimiter (JAX-RS) gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16390-rate-limiter
- Head gated: 61a48beac7fb381d2df64d630d9ffd448fa69880
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Seed (focused tests): 16390C0FFEE001
- Verdict: PASS at the head above.

## Legs at the gated head

- Step 0 changelog YAML parse: OK (changelog/unreleased/SOLR-16390-ratelimiter.yml).
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (:solr:api, :solr:core, :solr:solrj compileJava,
  :solr:core and :solr:api compileTestJava, -Pvalidation.errorprone=true): rc 0.
- Step 3 focused tests: V2ClusterAPIMappingTest 1/1; TestRequestRateLimiter 4 tests,
  3 passed, 1 skipped, 0 failures. Counts read from the fresh JUnit XML.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

## Path to the gated head (all fixes by the phone agent, each pushed before re-gating)

- 6357c755 (as implemented): did not parse; the Commands-class removal had dropped
  ClusterAPI's closing brace. Fixed in 2450061eb01, which also took tidy's SetRateLimiter
  formatting.
- 2450061eb01: tidy removed two stale static imports and extra blank lines in ClusterAPI;
  formatting-only commit eea2bbfda3e on top.
- eea2bbfda3e: main compile failed on two references the removal missed: CoreContainer
  still registered clusterAPI.commands (removed in 59636a55aa1) and CollectionsHandler
  lacked the SetRateLimiter import (added in 93b7e7c736b).
- 93b7e7c736b: test compile failed on ApiBag registrations of clusterAPI.commands in
  V2ClusterAPIMappingTest and TestCollectionAPIs; removed in 629037642bd.
- 629037642bd: tidy fixed one brace indent in TestCollectionAPIs; formatting-only commit
  61a48beac7f on top, which is the gated head.

## Coverage note

No test exercises the new PUT /cluster/rate-limiters endpoint directly; the focused legs
cover the mapping surface around it and the rate limiter itself. The endpoint writes the
same cluster property (RL_CONFIG_KEY) through the same ClusterProperties path the v1
command used.
