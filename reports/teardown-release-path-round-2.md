# Teardown release paths, round 2: source findings and prepared experiments

Status: HANDED BACK after partial Linux proof at ee1eb2474f. Factory-timeout test passed; interrupted-wait method body passed but its class tracker check failed with three Jetty clients and a default client provider context. Container-tail, client-interrupt and fail-before legs did not run. No Java compilation, Gradle tasks, or tests ran on Windows. The preparation below is retained as the original test design, not a green result.

Review/correction: `reports/teardown-release-path-review-round-3.md`. The interrupted-wait fixture joined only the factory close and its caller, not the sibling update-handler/provider close tasks that it also interrupted. Provider and update-handler ownership were omitted from this report's original analysis. A test-only isolation repair is pushed at 68fd05e5300; its Linux proof is a separate assignment, not a retry of the unchanged failed head.

Parent: `assignments/pool-teardown-release-path-round-2.md`. Claim: `claims/pool-windows-teardown-release-path-round-2.md`. Linux continuation: `assignments/pool-teardown-release-path-round-2-linux-proof.md`.

## Main finding

CustomSort has a plausible fourth path: an interrupted container wait can return while the factory close is still running. That produces a tracked factory child even though its parent's context was closed. A factory timeout is a real source weakness, but the supplied CustomSort log does not show that exception. Do not claim that the factory timeout candidate fixes this CI incident.

The 60-second interval need not be a timeout internal to Jetty. `BaseDistributedSearchTestCase.destroyServers()` has an outer `closeThreadPool` running Jetty stops; each stop can call a container shutdown that waits on another `closeThreadPool`. At the outer pool's first 60-second expiry, `shutdownNow()` interrupts the Jetty/container worker. `ExecutorUtil.awaitTermination()` on that worker catches the interrupt, calls `shutdownNow()` on the container's pool, restores interruption, and returns immediately, without a second wait. The container tail can run and the Jetty stop can finish while the factory task is still closing.

This is a source-supported hypothesis for the CI sequence, not proof of the historical interrupt origin or the original selector stall. Neither the initial stall nor the factory's actual stack after the client exception was captured.

## Evidence re-read

Reviewed the failure sections in the three original CI logs from `material/teardown-objecttracker-evidence.tar.gz`, together with the earlier report and source trace. This was a targeted review of the known incidents, not an exhaustive CI scan. Extracted logs match the archive's provenance hashes:

| Log | SHA-256 |
|---|---|
| `ci/pr5202-job.log` | `965987429dab7f4d1a8f7710b0b09d461d19f61304bfa3f21d0fb7272f493c93` |
| `ci/pr5030-job.log` | `1f3d264cea8858d5322c566b6fdfc5a14ca96a8c1261403bd8b715c26c794a24` |
| `ci/thincache-job.log` | `b87839de5ff9368ab58ee5aae61918c96022ca3d3f765ecad80b2c210a15d099` |

CustomSort's embedded elapsed log times, rather than the GitHub timestamps on replayed output, are useful here:

| Original log lines | Observed event |
|---|---|
| 6721-6733 | Test ends at elapsed 56443 ms; `closeThreadPool-351` workers stop Jettys and enter container shutdown at 56445-56447 ms. |
| 6751 | Worker `closeThreadPool-351-thread-7` finishes its servlet-context stop at 116447 ms. |
| 6752-6786 | Worker `closeThreadPool-354-thread-1` logs the quiet client-close error at 116447 ms, with `InterruptedException` in `ManagedSelector.doStop`. It is a distinct, nested pool. |
| 6787-6793 | Suite worker continues ordinary teardown at 116450-116457 ms. |
| 6794-6821 | Runner finds live `httpShardExecutor-311-thread-2` in selector polling, interrupts it, logs a selector failure at 121216 ms, and reports that leaked threads terminated. |
| 6822 onward | Repeated tracker report names the client and the factory-owned context. These are repeated reports of the same entries, not independent snapshots of current live state. |

116447 - 56443 = 60004 ms. This fits a first executor budget followed by interrupt cancellation, without requiring the second 60-second wait or a thrown timeout. There is no `Timeout waiting for pool to shutdown` in this CustomSort log. The log does not identify the factory's completion point. A metrics warning during thread cleanup also does not establish the identity or close stack of the warning's owner.

The CloudHttp2 log does contain a close-pool timeout and the 16 container-owned contexts listed by the first report. That remains the strongest evidence for the container-tail candidate. ThinCache still has the separate assertion-before-test-local-close mechanism; no shared production fix is needed to explain its three entries.

## Every statement on the factory path

Line numbers in this table are from the requested Apache baseline `79b23df58daac14b92cc35f23b8b81f72098b4ba`, and match the incident's factory source.

| Statement | Throw/block behavior and effect on release |
|---|---|
| `loadbalancer.close()` at 348, before the client | `LBSolrClient.close()` synchronizes on the balancer and can await its health-check executor. It can block or throw. The surrounding finally still attempts the client and communication executor. A pending balancer exception propagates after those finish and bypasses 359-361. |
| `if (defaultClient != null)` at 352 | Field/null check; no blocking operation. |
| `IOUtils.closeQuietly(defaultClient)` at 353 | Can block in the client. `IOUtils` catches `Exception`, including the logged `RuntimeException`; it does not catch `Error`. A caught client exception alone does not bypass the factory's metrics close. |
| Enter the inner `finally` at 355 | Runs even if client close escapes. No wait or throw by itself. |
| `ExecutorUtil.shutdownAndAwaitTermination(commExecutor)` at 356 | Calls shutdown, waits 60 s, calls shutdownNow if needed, then waits another 60 s. Two false awaits throw RuntimeException and bypass metrics. Shutdown methods can also throw unchecked exceptions. An interrupted await instead calls shutdownNow, restores the flag, and returns. A still-running await postpones the metrics close. The OTEL executor wrapper delegates these lifecycle calls to its underlying executor. |
| Leave nested try/finally at 357-358 | A pending balancer/client throwable resumes here if executor cleanup returned. No metrics cleanup is in this finally on the baseline. |
| `IOUtils.closeQuietly(asyncRequestsGauge)` at 359 | Can block in a callback close. Ordinary exceptions are logged and caught. An Error can escape before context release. The gauge is also registered with the context, so double-close warnings by themselves do not prove this is the failing statement. |
| Enter `try` at 360 | No blocking operation. |
| `SolrMetricProducer.super.close()` at 361 | Quietly closes `getSolrMetricsContext()`. Context close releases its own tracker entry first, then closes registered callbacks. Thus a callback problem after entry to this context close is not an explanation for that context's retained tracker entry. |

`SolrMetricsContext.getChildContext()` creates a child without registering it as a parent closeable. Parent release is not evidence of child release. `ObjectReleaseTracker` records release calls, not successful underlying resource termination. Moving a release to finally must not be described as proof that selectors, sockets or scanner threads stopped.

## Published scratch branch and isolated candidates

Branch: `nick-boss-tech/solr:solr-18564-release-path-r2`. All commits were pushed fast-forward and each remote head was verified. No PR branch moved.

The assignment's `origin/main (79b23df58d)` wording conflates two refs. Fork `origin/main` is `498a09aea9`; the scratch branch starts at the explicit Apache SHA `79b23df58daac14b92cc35f23b8b81f72098b4ba` as requested.

| Commit | Change |
|---|---|
| `82c6459609cac315846333b0ca39423434f1b4cb` | Tests only: factory timeout, real container timeout, and interrupted container-wait race. Production code is the requested baseline. |
| `263a45b9c82c9a95390a6fb25d06ae54129c43ab` | Factory candidate: put gauge/context cleanup in an outer finally. Executor timeout still propagates; an endlessly blocked factory still has not reached that finally. |
| `25efbd323011e106776830c2f85faddb2b1a98bf` | Container candidate: catch/log the close-pool RuntimeException and continue the tail. Same behavior as PR 5192, applied only to this scratch branch. Failure remains logged, but no longer propagates to shutdown's caller. |
| `ee1eb2474f6650f9c8f8a51c6729383496a4256e` | Cherry-pick of `f0adb05094c6ae1412af75a95d301c0934be5304`: destroy in the stop finally and release in the executor-shutdown finally, plus its existing interrupted-close regression. Original fork branch was fetched and verified; it was not changed. |

Tests-only before branch: `solr-18564-release-path-r2-before` at `f92d77151295b5b237230843e3897e4c441da26d`, pushed and verified. It adds the interrupted client test to the tests-only commit 82c6459609c. Git comparisons confirm all three test files are identical to the candidate and production Java has no difference from Apache 79b23df58d. This avoids reconstructing tests for the fail-before experiment.

Important baseline difference: f0adb05094 is based on old fork main `498a09aea9`, not on the incident base. The cherry-pick applied without conflicts on Apache `79b23df58d` and retains its abortable-request implementation. Old-baseline test results are not fresh proof of the integrated head.

## Prepared experiments, not executed results

| Test | Injection | What is asserted / expected, not observed here |
|---|---|---|
| `HttpShardHandlerFactoryReleaseTest.testExecutorTimeoutStillClosesMetrics` | Real blocked communication worker; test executor returns false for both termination waits immediately. Production factory metrics initialization is used, without starting a client. | Timeout exception stays visible. Baseline retains the factory context after parent close; candidate releases it. Prints tracker output before test cleanup. Finally releases/joins the worker and genuinely closes owned contexts without clearing the tracker. The immediate false awaits test the timeout control flow, not real timeout duration. |
| `CoreContainerReleasePathTest.testTimeoutStillClosesContainerTail` | PR 5192-style blocking factory wrapper survives pool interruption until a test latch opens. Real two 60-second production waits. | Marker authorization plugin, marker handler and handler context, and container root context must close. Baseline should fail these assertions. The blocked factory child intentionally stays tracked until its own delegate close finishes; after the latch, the class tracker check must be empty. Prints the tracker before assertions/cleanup. |
| `CoreContainerReleasePathTest.testInterruptedContainerWaitReturnsBeforeFactoryClose` | Interrupt the shutdown worker after the factory task signals entry; no sleeps or timeout expiry needed. | Container returns with interruption preserved and plugin/handler/root closed, while factory context remains tracked. Releasing the latch makes the factory close and removes its entry. Expected to pass both before and after candidates. This models early return; it does not reproduce the Jetty selector's original stall. |
| `HttpJettySolrClientTest.testCloseReleasesClientWhenInterrupted` | Exact regression from f0adb05094, applied to Apache baseline. | Client close still throws RuntimeException, while successful class cleanup requires no residual tracked client. Existing test covers a client-owned executor, not a factory-shared executor. |

Linux hand-back recorded on coordination at `assignments/pool-teardown-release-path-review-round-3.md`: tidy exit 0 with no changes; factory-timeout command passed; interrupted-wait method body passed, then class check reported `[HttpJettySolrClient, HttpJettySolrClient, SolrMetricsContext, HttpJettySolrClient]`, with the context owned by `HttpSolrClientProvider`. This is not a clean result. Later commands and fail-before were not run. The original log and XML paths are on vm4, and this Windows review has not independently read those raw artifacts. No blanket tracker clearing, release-assertion weakening, class-check changes, or tracker auto-close candidate was included in either proof tree.

## Exact focused Linux commands

Run in a disposable Linux worktree of the recorded scratch head under that host's build lock and preflight rules. See the continuation assignment for order, formatter repair rules, expected failures, and stop conditions.

```sh
./gradlew :solr:core:test --tests org.apache.solr.handler.component.HttpShardHandlerFactoryReleaseTest.testExecutorTimeoutStillClosesMetrics --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1

./gradlew :solr:core:test --tests org.apache.solr.core.CoreContainerReleasePathTest.testInterruptedContainerWaitReturnsBeforeFactoryClose --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1

./gradlew :solr:core:test --tests org.apache.solr.core.CoreContainerReleasePathTest.testTimeoutStillClosesContainerTail --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1

./gradlew :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseReleasesClientWhenInterrupted --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

Do not rerun an executed failing test invocation. Record XML, stdout/stderr, tracker output and actual head, then stop and hand back. No full suites are authorized.

## Verification and decisions

Static review confirmed the called APIs, plugin-loading path, existing OTEL test dependency, scratch baseline and scoped six-file diff. `git diff --check 79b23df58d HEAD` passes and the source worktree is clean. These checks do not establish compilation or runtime correctness. Linux must compile and run the prepared experiments.

Recommendation: hold a new PR/JIRA until focused proof distinguishes release bookkeeping from actual termination. SOLR-18564 already covers the container timeout tail. A client-specific follow-up should describe interrupted stop and unconditional cleanup rather than imply a fix for the original selector stall or all metrics leaks. No JIRA was filed and no PR was opened or edited.

The nested-wait hypothesis still needs Linux results and, if the precise CI cause is to be claimed, a closer Jetty reproduction or stack capture before/after its client stop. Full-suite reruns, CI reruns, public comments, and PR changes remain outside this work.
