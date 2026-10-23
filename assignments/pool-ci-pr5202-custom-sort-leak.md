# Assignment: pool-ci-pr5202-custom-sort-leak (CI failure on apache/solr PR 5202)

Status: DONE (reports/ci-pr5202-custom-sort-leak.md). Requested by vm4 on behalf of Nick Shanin, 2026-10-23.
- Scope: find out whether the PR 5202 CI failure is caused by the PR. Do not push to the PR branch, do not comment on the PR, do not change production code.

## Context

- PR: https://github.com/apache/solr/pull/5202, "SOLR-18446: Keep collection routing path-scoped in HttpSolrCall", head `solr-18446-path-routing-phone-agent-2` at `7e0251ee02` on fork `nick-boss-tech/solr`, base `main` at `79b23df58d`.
- CI run: https://github.com/apache/solr/actions/runs/39903819776 (attempt 2), job 120373474924 "Run Solr Tests using Crave.io resources". Job log saved on vm4 at `/workspace/gates/logs/vm4/ci-5202/job.log`.
- Failure: `DistributedQueryComponentCustomSortTest` class teardown reports `ObjectTracker found 2 object(s) that were not released: [SolrMetricsContext, HttpJettySolrClient]`. Gradle then failed while reporting it ("No reporter found"), which produced a second, meta failure "Gradle Test Run :solr:core:test". Full run: 6119 tests, 2 failed, 286 skipped. Seed `FFF5A3C3AAC69084`.
- The PR's own diff touches only `HttpSolrCall.java`, `V2HttpCall.java`, and tests. The failing test and the leaked objects are not in the diff.
- vm4 reran the single class on the PR head with the CI seed (command in the logs below). It passed: 1 test, 0 failures, 38 s. Log: `/workspace/gates/logs/vm4/ci-5202/local-head-run.log`, JUnit XML: `/workspace/gates/logs/vm4/ci-5202/local-head-junit/`.
- From the CI log: the leaked client's `httpClient.stop()` was interrupted (by randomized-testing's thread leak control at the end of the suite), so `HttpJettySolrClient.close()` threw `Exception on closing client` before releasing the tracker. The stop itself took about 60 seconds. vm4 is working on the teardown code separately; this assignment only covers the PR question.

## Steps

1. On main (apache/solr), look for other recent CI failures with `ObjectTracker found` naming `HttpJettySolrClient` or `SolrMetricsContext` in a class that is not in the PR. Record the run ids and job ids. Keep the logs under `/workspace/gates/logs/<host>/` on your own host.
2. Run `DistributedQueryComponentCustomSortTest` on the PR base `79b23df58d` with seed `FFF5A3C3AAC69084`, in a scratch worktree, under the Gradle lock:
   `flock /workspace/tools/.gradle-vm4.lock bash -c 'source /workspace/tools/vm4-env.sh && ./gradlew :solr:core:test --tests org.apache.solr.handler.component.DistributedQueryComponentCustomSortTest --console=plain -Ptests.seed=FFF5A3C3AAC69084'`
   Record whether the base passes or fails.
3. Do not run the full `:solr:core:test` suite without the main agent's OK.
4. Do not relaunch a run that executed tests and failed. Record the result and stop.
5. Remove the scratch worktree after the report is pushed. Keep the logs.

## Deliverable

- `reports/ci-pr5202-custom-sort-leak.md`: the main run ids checked, the base run result, log paths, and one sentence on whether the PR is involved.
- Status line above set to DONE or HANDED BACK, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to rerun CI, change the PR, comment on it, or ask the author. These stay with the main agent and Nick.
