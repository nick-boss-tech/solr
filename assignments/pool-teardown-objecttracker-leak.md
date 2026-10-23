# Assignment: pool-teardown-objecttracker-leak (SolrMetricsContext and HttpJettySolrClient leaks at CI class teardown)

- Status: DONE. Diagnostic report: `reports/teardown-objecttracker-leak.md`; exact seeded baseline runs passed, with unresolved causality and baseline/CI comparability limits recorded. Requested by vm4 on behalf of Nick Shanin, 2026-10-23.
- Scope: find out what causes the ObjectTracker leak reports in three test classes, and which open PRs on the nick-boss-tech fork are involved. Do not push to any PR branch, do not comment on any PR, do not change production code.

## Context

- Three CI test classes failed their class-level ObjectTracker check (the check runs after the class, and reports objects created during the class that were never released).
  1. `org.apache.solr.handler.component.DistributedQueryComponentCustomSortTest` (core). Run https://github.com/apache/solr/actions/runs/39903819776, job 120373474924, PR 5202. Leaked: `SolrMetricsContext` and `HttpJettySolrClient`. Seed `FFF5A3C3AAC69084`. Log: `/workspace/gates/logs/vm4/ci-5202/job.log` on vm4.
  2. `org.apache.solr.client.solrj.impl.CloudHttp2SolrClientTest` (solrj). Run https://github.com/apache/solr/actions/runs/39699700526, job 119565807999, PR 5030. Leaked: 16 `SolrMetricsContext`. Seed `A35B8669BA78E261`. This matches SOLR-18564: `CoreContainer.shutdown` skips its remaining closes when the client close pool times out.
  3. `org.apache.solr.search.TestThinCache` (core). Run 39596535427, job 119215684665. Leaked: 3 `SolrMetricsContext`. Seed `1668E8BD438E6830`.
- Across 12 Crave test jobs in the last 100 failed runs on apache/solr, only these three runs show ObjectTracker leaks. Scan log: `/workspace/gates/logs/vm4/fix-jetty-close/ci-scan/summary.tsv` on vm4.
- Open fork PRs that touch the teardown path or these tests (checked by vm4 on 2026-10-23 by listing changed files for every open PR by nick-boss-tech on apache/solr):
  - 5192 (SOLR-18564): `CoreContainer.java`
  - 5029 (SOLR-18506): `TestThinCache.java`
  - 5002 (SOLR-11431): `CoreContainer.java`, `TestCoreContainer.java`
  - 5101 (SOLR-18531): `JettySolrRunner.java`, `TestJettySolrRunner.java`
  - 5202 (SOLR-18446): `HttpSolrCall.java`, `V2HttpCall.java`, and tests. Not in the diff: any of the three classes or the shutdown path.
- vm4 has a local, unpushed commit on branch `fix-interrupted-http-client-close` (`f0adb05094`, based on `origin/main` `498a09aea9`) that makes `HttpJettySolrClient.close()` release its tracker even when stopping the client throws. It is not a fix for the `SolrMetricsContext` leak.
- Known facts from vm4's reading: `HttpShardHandlerFactory.close()` closes its client, then shuts down its executor, then closes its metrics context. `CoreContainer.shutdown()` closes the shard handler factory inside a pool and awaits that pool with `ExecutorUtil.awaitTermination`, which throws `Timeout waiting for pool to shutdown` if the pool does not finish in about 120 seconds.

## Steps

1. Read the three CI logs listed above (the Crave job logs are on GitHub; ask the main agent for copies if your host cannot download them). For each leaked object, record the creation stack and which teardown call should have released it.
2. On `apache/solr` `main` at `498a09aea9`, run each of the three classes once with its seed, under the Gradle lock, with the JVM flags used in CI:
   `-Ptests.jvmargs="-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.timeoutSuite=600000! -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1 -Ptests.seed=<seed>`
   Keep logs and JUnit XML under `/workspace/gates/logs/<host>/`. Record whether the leak reproduces on main.
3. For each open PR listed above, check whether its diff changes the teardown path (`CoreContainer.shutdown`, `HttpShardHandlerFactory`, `HttpJettySolrClient`, `CoreContainerProvider`, `JettySolrRunner`, `BaseDistributedSearchTestCase`). Record yes or no per PR, with the file and line.
4. Do not run the full `:solr:core:test` or `:solr:solrj:test` suites without the main agent's OK.
5. Do not relaunch a run that executed tests and failed. Record the result and stop.

## Deliverable

- `reports/teardown-objecttracker-leak.md`: the creation stack and the missed release for each leaked object, the main reproduction result per class, and the per-PR yes or no table.
- Status line above set to DONE or HANDED BACK, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to fix `CoreContainer.shutdown`, change any PR, comment on any PR, or rerun CI. These stay with the main agent and Nick.
