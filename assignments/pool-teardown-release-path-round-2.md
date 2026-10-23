# Assignment: pool-teardown-release-path-round-2 (follow-up to the teardown ObjectTracker leak diagnosis)

- Status: CLAIMED (claims/pool-windows-teardown-release-path-round-2.md). Windows source analysis and deterministic test preparation; Linux execution follows separately.
- Builds on: `assignments/pool-teardown-objecttracker-leak.md` (DONE, report `reports/teardown-objecttracker-leak.md`). Read that report first. This assignment covers the one thing it leaves open: exactly which release call is skipped on each failure path, and whether a candidate change makes the tracker empty.
- Scope: reproduce each path deterministically on a scratch branch, and test candidate changes there. Do not push to any PR branch, do not comment on any PR, do not open a JIRA issue, do not push a branch to `apache/solr`. Scratch branches go to the fork `nick-boss-tech/solr` as fast-forward pushes only.

## What "the release path" means

An object is released when its owner's close runs and reaches `SolrMetricsContext.close()`, or the client's own `close()` reaches `ObjectReleaseTracker.release(this)`. Every object the tracker reports is one of these. Line numbers below are from PR head `7e0251ee02` (`solr/core/...`, `solr/solrj-jetty/...`). The report cites `498a09aea9`, where the lines differ, so re-locate by method name.

1. Container shutdown. `CoreContainer.shutdown()` (around line 1220). Its `finally` (line 1297) submits `shardHandlerFactory.close()` to `customThreadPool` (line 1300), then `updateShardHandler::close`, then `zkSys.close()` (line 1313). Line 1315 calls `ExecutorUtil.shutdownAndAwaitTermination(customThreadPool)`, which waits up to 60 s, then calls `shutdownNow()`, then waits 60 s more. If the second wait also times out, `ExecutorUtil.awaitTermination` throws `RuntimeException("Timeout waiting for pool to shutdown")` (solrj `ExecutorUtil.java`, lines 134-155). That exception escapes `shutdown()`, so the remaining closes after this block never run. Those are the plugin closes, `containerHandlers.close()`, and the root `solrMetricsContext` close. This is the path PR 5192 (SOLR-18564) addresses.
2. Factory close. `HttpShardHandlerFactory.close()` (lines 345-362). It closes `loadbalancer`, then `defaultClient` via `IOUtils.closeQuietly` (line 353), then shuts down `commExecutor` (line 356), then closes `asyncRequestsGauge` (line 359), then calls `SolrMetricProducer.super.close()` (line 361), which closes the factory's metrics context. The factory's context is a child created by `getChildContext(this)` in `initializeMetrics` (line 461). `getChildContext` does not register the child with its parent (`SolrMetricsContext.java` lines 87-89), so a parent's `close()` never closes it.
3. Client close. `HttpJettySolrClient.close()` (lines 346-367 on `main`; the local commit `f0adb05094` changes this method). It calls `asyncTracker.waitForComplete()`, then `httpClient.stop()` and `destroy()`, and releases the tracker only at the end. The client's executor is passed in by the factory (`withExecutor(commExecutor)`, `HttpShardHandlerFactory` line 309), so `shutdownExecutor` is false on that path.
4. Context close. `SolrMetricsContext.close()` (lines 284-287) runs `assert ObjectReleaseTracker.release(this)`, then closes registered closeables.
5. Test class check. `SolrTestCase` (`solr/test-framework/.../SolrTestCase.java`, lines 97-108) runs `clearObjectTrackerAndCheckEmpty()` only if the class passed. The tracker is JVM-wide, so an object leaked by an earlier class in the same worker JVM is reported at a later class's teardown.

## What is proven

- Interrupted client stop. A probe on `f0adb05094`'s base reproduced the client failure in 3 ms. The interrupt makes `httpClient.stop()` throw `Exception on closing client` before the release, and the class check reports `HttpJettySolrClient`. The fix branch releases the client in `finally` and passes its regression test. Pushed to `nick-boss-tech/solr` branch `fix-interrupted-http-client-close` at `f0adb05094`.
- Ordinary close. A plain close after real requests takes 0-4 ms. A close during an in-flight request takes 7 ms. Neither reproduces the 60-second CI wait.
- Single-class reruns on `main` pass, with the CI seeds: `DistributedQueryComponentCustomSortTest` (1 test), `CloudHttp2SolrClientTest` (20 tests), `TestThinCache` (2 tests).
- Full core suite on `main` with seed `FFF5A3C3AAC69084`: 5843 tests, 4 failures (`TestTlogReplica.testRecovery`, `TestTlogReplica.testKillLeader`, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, `TestCoordinatorRole.testNRTRestart`), and no ObjectTracker report. Log: `/workspace/gates/logs/vm4/leak-repro/full-core.log` on vm4. The leak did not reproduce on that run, so it depends on something CI has and the local run does not.

## What is not proven

- Which statement skips the factory's context close in the CustomSort CI run (PR 5202, run 39903819776). The log shows the client close throwing, but `IOUtils.closeQuietly` catches that. Something else must throw or stall between line 353 and line 361: the `loadbalancer.close()` at line 348, the `shutdownAndAwaitTermination(commExecutor)` at line 356, or the close task itself never finishing. This is the main open question.
- Why `httpClient.stop()` waits about 60 seconds in CI and not locally. Probes so far did not create the condition.
- Whether the CloudHttp2 CI leak (SOLR-18564, PR 5192) is only the container tail, or also involves a stuck worker. The PR 5192 test has not been run on this path.

## Steps

1. Read the report and the three CI logs (ask the main agent for copies if your host cannot download them). For the CustomSort case, list every statement between `HttpShardHandlerFactory.close()` line 353 and line 361, and say which ones can throw or block.
2. Write one deterministic test per path on a scratch branch off `origin/main` (`79b23df58d`, not the fork's `498a09aea9`):
   a. Factory context: a `HttpShardHandlerFactory` whose `commExecutor` task blocks, closed from `CoreContainer.shutdown` or directly. Check whether its `SolrMetricsContext` is still tracked after `close()`.
   b. Container tail: a close-pool task that blocks past the timeout. Check which of the plugin closes, `containerHandlers` and the root context remain tracked. PR 5192's `CoreContainerShutdownTest` is the starting point.
   c. Client stop interrupted: the existing regression test from `f0adb05094`, rerun on the scratch branch.
   Record for each: the tracker output before and after the candidate change.
3. For each path that reproduces, try the smallest change that makes the tracker empty, and say whether it keeps the failure visible. Do not weaken `assert ObjectReleaseTracker.release` or the class check.
4. Do not run `:solr:core:test` or `:solr:solrj:test` in full without the main agent's OK. The full core run took 1 hour 29 minutes.
5. Do not relaunch a run that executed tests and failed. Record the result and stop.

## Deliverable

- `reports/teardown-release-path-round-2.md`: the step-by-step statement list for the CustomSort factory path, one reproduction per path with its tracker output, the candidate changes tried, and which owner decisions they raise.
- Scratch branch name and commit SHAs for any candidate change, with the exact test command for each.
- Status line above set to DONE or HANDED BACK, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to file a JIRA for the teardown problem, and whether to open a PR for `fix-interrupted-http-client-close`.
- Whether to change or comment on PR 5192 or any other PR, rerun CI, or run a full suite.
