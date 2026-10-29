Status: OPEN
Tags: gate

# Gate job: SOLR-16390 cluster set-ratelimiter (JAX-RS)

Requested by: phone agent (Nick's directed overnight v2 implementation push, 2026-10-29)
Runner tags needed: gate

## Branch under gate

- Fork branch: solr-16390-rate-limiter
- Head: eea2bbfda3ee46a2c0fe3d5c39390aca12b255bc
- Superseded head: 2450061eb015205a363bd255574148079a111f27 (parsed and tidied, but tidy then
  removed two stale static imports and extra blank lines in ClusterAPI; formatting-only commit
  eea2bbfda3 on top, per the step 1 rule)
- Superseded head: 6357c7555b2c02d4cacf74859e439991c76a43ce (did not parse: the Commands-class
  removal dropped ClusterAPI's closing brace; fix commit 2450061eb01 restores it and takes
  tidy's SetRateLimiter formatting)
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Worktree if needed: ~/workspace/solr-worktrees/16390-ratelimiter
- Ticket: SOLR-16390 (checked before taking: Open, unassigned on Jira; umbrella for the v2
  cluster API migration; last comment by epugh 2026-09-26)
- Note: this file was accidentally zeroed by a bad edit post on 2026-10-29 and reconstructed
  in full the same day; the head history above is the complete record.

## What the branch does

ClusterAPI's Commands inner class held one remaining annotated command, set-ratelimiter. It is
replaced by a SetRateLimiter Jersey resource (PUT /cluster/rate-limiters) that copies the
request body into a RateLimiterPayload and writes it to the same cluster property
(RL_CONFIG_KEY) through ClusterProperties, with the same error wrapping. The Commands class,
its field on ClusterAPI, and the imports only it used are removed; the resource is registered
from CollectionsHandler.getJerseyResources().

## Steps (standard gate)

0. Changelog YAML parse on changelog/unreleased/SOLR-16390-ratelimiter.yml.
1. Tidy (scoped if applicable, else root). If tidy modifies branch files, stop GATE INVALID per
   the standing rule, unless the diff is formatting only, in which case commit the tidy output
   (Nick Shanin identity, no trailer), update this job's head, and rerun from step 1.
2. Compile with Error Prone enabled: :solr:api and :solr:core compileJava and compileTestJava,
   :solr:solrj:compileJava, with -Pvalidation.errorprone=true.
3. Focused tests: any rate-limiter test class the runner identifies (for example
   TestRateLimiters), plus V2ClusterAPIMappingTest if present.
4. Module check: :solr:core:check -x test.

## On completion

- Green: write receipts/SOLR-16390-ratelimiter.md (head gated, seed, per-leg results), flip
  this job's Status to DONE.
- Failure: record the failing leg and first error in this job and in
  receipts/SOLR-16390-ratelimiter.md; do not fix code on this job. Fixes return to the phone
  agent as new work.
