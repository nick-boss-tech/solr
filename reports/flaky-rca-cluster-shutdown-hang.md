# Flaky RCA: MiniSolrCloudCluster shutdown hang ("Timeout waiting for pool to shutdown")

Assignment: `assignments/pool-flaky-rca-cluster-shutdown-hang.md`. Reading only. No builds, tests, gate runs, test-queue commands, GitHub or Jira writes, claim edits, commits or pushes. Builds on `reports/flaky-rca-gcs-install-shard.md`.

Trees and records read:
- Current main: `upstream/main` = `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (source checkout, read with `git show` and `git grep`). Every line number below is at this SHA.
- Failing-run log: `crave-37384974639-failed.log` in the session scratchpad (outside the worktree; the log the prior report read). Log line numbers below refer to that file.
- Jetty: the local Gradle cache holds `jetty-io-12.1.12.jar` and its pom only, no sources. Jetty internals are inferred from stack frames and from one log line, and are marked as inferred.

## Bottom line

1. Top-ranked mechanism (M1): the close runs `HttpShardHandlerFactory.close`, which stops the Jetty client before it drains `commExecutor`, and both sit under the closeThreadPool budget of 60 s + 60 s. The Jetty selector loops run on `commExecutor`. The pool's interrupt reaches only the close thread. If the client stop is held past the first 60 s, the selector loop survives the aborted stop, and the commExecutor drain burns the second 60 s. The "Timeout waiting for pool to shutdown" is the result. Given a stall past 60 s, the failure follows whenever the selector loop survives the aborted stop, which the observed log shows happened.
2. The log does not say what held the close for the first 60 s or what kept the selector loop from ending. Two code paths are candidates and the log cannot separate them: an untimed phaser wait on outstanding async requests (M2), and an interruptible park of the selector loop on a permit acquire (M3). A thread dump about 30 s into the close separates them (section 5).
3. Ruled out for this signature: a test-held shared client (it hangs before any pool, with no pool message), and "executor never shut down" (the factory's executor is shut down, but after the client stop, which is the ordering in M1).
4. This refines H1 of the prior report. The blocked parties are the close thread (client stop, then drain), not shard I/O. Log line 13097 reads as the selector loop ending on the forced interrupt, not as in-flight shard I/O.

## 1. The shutdown path at upstream/main

1. `MiniSolrCloudCluster.shutdown()` (`solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java:615-646`):
   - 618-625: `closeQuietly(solrClient)` and each per-collection client, on the calling thread, outside any pool. No bound.
   - 627-634: one stop task per jetty (`stopJettySolrRunner`) submitted to `executorCloser`, then `invokeAll`. The wait is for all tasks, with no bound.
   - 635-640: `shutdownAndAwaitTermination(executorCloser)`, then `checkForExceptions` (718-736), which adds each `ExecutionException` cause as suppressed and throws "Error shutting down MiniSolrCloudCluster".
2. Each jetty stop reaches `CoreContainer.shutdown()` on a jetty-closer thread (log 13041-13042).
3. `CoreContainer.shutdown()` (`solr/core/src/java/org/apache/solr/core/CoreContainer.java:1220`):
   - 1233-1234: creates `customThreadPool` (closeThreadPool).
   - 1300: `customThreadPool.execute(() -> shardHandlerFactory.close())`.
   - 1313: `zkSys.close()`, on the jetty-closer thread.
   - 1315: `ExecutorUtil.shutdownAndAwaitTermination(customThreadPool)`. The 60 s + 60 s budget is measured from here.
4. `HttpShardHandlerFactory.close()` (`solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java:344-365`):
   - 347-348: `loadbalancer.close()`.
   - 352-353: `IOUtils.closeQuietly(defaultClient)`.
   - 355-356 (finally): `ExecutorUtil.shutdownAndAwaitTermination(commExecutor)`. The drain runs on the same close thread, after the client close returns.
5. `HttpJettySolrClient.close()` (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java:341-365`):
   - 343: `asyncTracker.waitForComplete()`.
   - 345-347: `httpClient.stop()`, then `destroy()`.
   - 357-358: `RuntimeException("Exception on closing client")`.
   - 360-362: executor shutdown only when the client owns it. The factory passes `commExecutor`, so `shutdownExecutor = false` (`HttpJettySolrClient.java:214-220`).
6. `AsyncTracker.waitForComplete()` (`HttpJettySolrClient.java:919-931`): `phaser.arrive()`, then `awaitAdvanceInterruptibly(phase)` with no timeout. It returns when every registered party (each tracked async request) has arrived. On interrupt it calls `forceTermination()` and re-sets the interrupt flag (925-929).
7. Jetty stop frames in the failing run (log 13070-13094): `HttpClient.doStop(HttpClient.java:281)`, `ClientConnector.doStop(ClientConnector.java:356)`, `SelectorManager.doStop(SelectorManager.java:281)`, `ManagedSelector.doStop(ManagedSelector.java:139)`, `CountDownLatch.await(CountDownLatch.java:230)`, `AbstractQueuedSynchronizer.acquireSharedInterruptibly`. JDK line 230 is the untimed `await()`. The stop waits with no bound short of the latch being counted down or an interrupt.
8. The client's executor is `commExecutor`: `HttpJettySolrClient.java:286` (`setExecutor`) and `HttpShardHandlerFactory.java:312` (`withExecutor`). The client has two selectors (`HttpJettySolrClient.java:261`). `commExecutor` is created at `HttpShardHandlerFactory.java:279-289` with `maximumPoolSize = Integer.MAX_VALUE` (line 95) and a SynchronousQueue, so every submit gets a thread.

Waits on this path:

| Wait | Where | Bound | Ends when |
|---|---|---|---|
| Test-held clients closed in `shutdown()` | MiniSolrCloudCluster.java:618-625 | none (caller thread, outside any pool) | their own in-flight requests finish |
| `invokeAll` of jetty stops | MiniSolrCloudCluster.java:634 | none | all stop tasks finish |
| closeThreadPool, first wait | ExecutorUtil.java:142 | 60 s | pool drains; else interrupt |
| closeThreadPool, second wait | ExecutorUtil.java:146 | 60 s | pool drains; else throw |
| Phaser in `waitForComplete` | HttpJettySolrClient.java:924 | none; per request 90 s in test cluster | all tracked requests complete, or interrupt |
| Jetty stop latch | ManagedSelector.java:139 (frame) | none in frame | selector acknowledges stop, or interrupt |
| commExecutor drain, first wait | ExecutorUtil.java:142 via HttpShardHandlerFactory.java:356 | 60 s | drain; else `shutdownNow` at 144 |
| commExecutor drain, second wait | ExecutorUtil.java:146 | 60 s | drain; else throw |
| Permit acquire in queued listener | HttpJettySolrClient.java:890 | none | permit released, or interrupt (caught at 891-894) |
| Pause of updates | CoreContainer.java:1242; SolrCoreState.java:117-124 | `PAUSE_UPDATES_TIMEOUT_MILLIS` (SolrCoreState.java:43) | bounded; on jetty-closer thread, outside the pool |

Structural point. The pool's `shutdownNow` (ExecutorUtil.java:144 for the outer pool) interrupts only the closeThreadPool workers. The commExecutor threads, including any Jetty selector loop that runs there, are interrupted only by the inner `shutdownNow` at ExecutorUtil.java:144, which runs after the inner drain's first 60 s.

Timing consequence, independent of the trigger. The outer second wait starts at the interrupt. The inner drain starts a moment later, when the close thread's exception unwinds. Both have 60 s. So a drain that is not complete within 60 s of the interrupt is reported as a closeThreadPool timeout. In the observed run the outer wait fired first, by a few milliseconds. The observed times fit: interrupt at 102184 (log 13060), outer timeout at 162184 (log 13095), selector failure at 162186 (log 13097). The last is about 2 ms after the forced `shutdownNow` of the inner drain would have landed.

## 2. What the close can block on

Threads:
- closeThreadPool worker (the close thread). The only thread the pool interrupts.
- commExecutor threads. (a) Jetty selector loops, two per client. Evidence that a loop runs on commExecutor: log 13097 is a Jetty `ManagedSelector select() failure` logged from `httpShardExecutor-19-thread-2`. That is one line; the Jetty source is not on disk, so this is inferred. (b) Shard submit tasks, for `ParallelHttpShardHandler` only (`ParallelHttpShardHandler.java:161-174`). (c) Callbacks that run inline on whichever thread completes a future, including the LB retry path (section 3, state B).
- Jetty IO callbacks. The shard response callback (`HttpShardHandler.java:299`) and the LB retry callback (`LBAsyncSolrClient.java:147-154`) run where the future completes. `ParallelHttpShardHandler.java:55-56` says the inner callback runs "on a Jetty IO thread".

Connections and in-flight requests:
- A request is tracked by the phaser only when it is async (`HttpJettySolrClient.java:663-666`). Shard requests are async: `HttpShardHandler.java:285` calls `lbClient.requestAsync`. A synchronous request is not tracked, so `waitForComplete` does not wait for it.
- A tracked request registers in `queuedListener` (`HttpJettySolrClient.java:880-895`) before `available.acquire()` (889-890). It deregisters in `completeListener` (896-903). The semaphore is sized by `solr.solrj.http.jetty.async_requests.max`, default 1000 (`HttpJettySolrClient.java:107-108`, 877). The acquire runs on the thread that queues the request.
- Retries. `LBAsyncSolrClient.doAsyncRequest` (135-160) registers a whenComplete (147-154), and `onFailure` (80-99) sends a new request from inside it. Each retry adds a new phaser party and may acquire a permit on the same thread.

Bounds on a single request:
- Total request timeout defaults to the idle timeout (`HttpSolrClient.java:597-601`). For shard requests the idle timeout is `socketTimeout` (`HttpShardHandlerFactory.java:311`; the property name is `SolrHttpConstants.java:31`).
- Test cluster: `socketTimeout` 90000 (`MiniSolrCloudCluster.java:117`), so an outstanding request fails at 90 s at the latest. Production default `DEFAULT_SO_TIMEOUT` is 600000 (`SolrHttpConstants.java:23`).
- A retry starts a new request with its own budget, so a chain of retries can keep the phaser populated longer than one budget.

Bounded versus unbounded, summarized:
- Bounded: pool awaits (60 s each), a single request (90 s in the test cluster, 600 s in production), the update pause (`SolrCoreState.java:43`).
- Unbounded: the phaser (until tracked requests finish or an interrupt), the Jetty stop latch (until the selector acknowledges or an interrupt), a permit acquire (until a release or an interrupt), `invokeAll` of the stops, and the test-held client closes at MiniSolrCloudCluster.java:618-625.

## 3. What a test class can set up

A. Outstanding async shard request at close (feeds M2). If a request is still outstanding when the close starts, the phaser holds the close thread until the request fails or the 60 s interrupt arrives. Test-settable: a distributed request, or an async Collections task whose per-replica request is still outstanding, against a target that does not answer. A test tool exists: `SocketProxy.pause()` (`solr/test-framework/src/java/org/apache/solr/util/SocketProxy.java:231`) on a jetty started with a proxy (`JettySolrRunner.java:186-194`).
   - Not evidenced in `GCSInstallShardTest`. The install tests wait with `processAndWait(client, 15)` (`AbstractInstallShardTest.java:212-214`, `273-275`). That returns the status on timeout without throwing (`CollectionAdminRequest.java:256-260`). The expected-failure steps assert FAILED, so their Collections task has finished. The Overseer shard path was not read, so outstanding requests from Collections tasks are not ruled out.

B. Permits exhausted with an IO-thread retry (feeds M3). When an LB retry runs on a thread that completes a future, and the permit count is 0, that thread parks in `available.acquire()` (`HttpJettySolrClient.java:890`). The park ends on a permit release or an interrupt (caught at 891-894).
   - Test shape: `solr/core/src/test/org/apache/solr/handler/component/AsyncTrackerSemaphoreLeakTest.java` (MAX_PERMITS 40; RST of fake connections). The regression test for this deadlock is `@Ignore`d (lines 123-126, citing SOLR-18174). The fix its comment names, `failureDispatchExecutor`, is not on main (a grep over `solr/solrj-jetty/src/java` finds nothing).
   - Not evidenced in `GCSInstallShardTest`. It does not set the sysprop (a grep over `solr/modules/gcs-repository` finds no match), so exhaustion needs about 1000 outstanding requests on one client. The Pattern A leak guard (`PERMIT_ACQUIRED_ATTR`, `HttpJettySolrClient.java:866-888`) is on main, and its test is active (`AsyncTrackerSemaphoreLeakTest.java:246`).

C. Test-held shared clients (`solrClient`, `solrClientByCollection`). Closed on the caller thread before any jetty stop (`MiniSolrCloudCluster.java:618-625`). An outstanding async request on one of them hangs there, with no "Shutting down CoreContainer" line and no closeThreadPool message. That is a different signature. Ruled out for this one.

D. Test-supplied executors. A caller-supplied executor is never shut down by the client (`HttpJettySolrClient.java:214-220`). The factory drains its own `commExecutor` (`HttpShardHandlerFactory.java:356`). A test executor is not on this path. Ruled out.

## 4. Ranked mechanisms

### M1 (rank 1): client stop plus drain ordering with a live selector loop. Observed.

The chain, at the failing SHA's code and the failing run's log:
- Log 13060 (102184): `IOUtils` logs "Error while closing" on closeThreadPool-532-thread-2, caused by "Exception on closing client" with an InterruptedException (13070). The interrupt landed in the client close, at `HttpJettySolrClient.java:346` (the stop frame, 13093).
- Log 13070-13073: the InterruptedException was raised in the untimed `CountDownLatch.await` inside `ManagedSelector.doStop` (ManagedSelector.java:139).
- Log 13095 (162184): closeThreadPool "did not forcefully stop ... active threads = 1". The captured log has only this closeThreadPool message. There is no "httpShardExecutor did not forcefully stop" line, which fits the inner drain's first wait ending at the same moment, with its own message not yet logged when the outer thread threw.
- Log 13097 (162186): "ManagedSelector select() failure ClosedSelectorException" from `httpShardExecutor-19-thread-2`. The selector loop was on a commExecutor thread and was still alive until the forced `shutdownNow`.
- Log 13100-13101 and 13133: the pool exception, surfaced by `MiniSolrCloudCluster.checkForExceptions`.

What the chain shows. The first 60 s are spent inside the client close. The second 60 s are spent in the commExecutor drain, waiting for a live selector loop. Remove either piece and the signature does not follow. If the client close returns within 60 s, the drain never runs inside the pool's window. If the selector loop ends on the stop, the drain finishes.

What the log implies about the loop. A `ClosedSelectorException` needs the selector to be closed before the loop calls `select()` again. A loop idle in `select()` at 102 s would have failed near 102 s, not at 162 s. So the loop was most likely parked off `select()` from before 102 s until the forced interrupt. Which code parked it is not in the log. This is an inference from the timing of one line and the Jetty ordering, which is not on disk.

Open question, which decides the park site: M2 or M3 (below), or a site not yet found.

Classification: production shutdown code (close order in `HttpShardHandlerFactory.close` and the untimed waits in `HttpJettySolrClient.close`), with Jetty in the middle. Not GCS code, not the branch. The prior report's H1 stands, with the blocked parties now named.

### M3 (rank 2): interruptible park of a selector-loop thread on a permit acquire. Code-evidenced; fits the timing; trigger improbable here.

Path: `LBAsyncSolrClient.java:147-154` (whenComplete) → `onFailure` (80-99) → `doAsyncRequest` (92-93) → `HttpJettySolrClient.requestAsync`, which calls `makeRequest` with `isAsync` → `decorateRequest` (663-664) → `queuedListener` → `available.acquire()` (889-890). If this runs on a commExecutor thread that is also the Jetty selector, the park holds the loop, the stop latch cannot complete, and the drain waits.
- Fit: it is an interruptible park, so only the forced `shutdownNow` frees it. That matches log 13097.
- Trigger: needs zero permits on the client. Nothing in the GCS class as read sets the sysprop or leaks permits, so this needs about 1000 outstanding requests on one client. That is why it ranks second, not first.
- Discriminator: a thread dump shows an httpShardExecutor thread in `Semaphore.acquire` under the `queuedListener` lambda, with `LBAsyncSolrClient` frames below it.

### M2 (rank 3): untimed phaser wait on outstanding async requests holds the close thread. Code-evidenced; trigger plausible; weaker fit.

Path: `HttpJettySolrClient.java:343` → `waitForComplete` (919-931), untimed, on the close thread.
- Log note: the stack at 13070-13094 is consistent with two different sequences. (i) The phaser was empty and the stop latch held the close thread. (ii) The phaser held the close thread until the interrupt; `waitForComplete` then force-terminated, re-set the flag, and the stop's latch await threw at once. The two sequences print the same stack at the stop frame, so the log cannot choose between them.
- Bound: each request ends by 90 s in the test cluster. Under (ii) the interrupt at 60 s forces the phaser, the stop throws, and M1's drain then applies.
- Weaker fit: under (ii) the selector loop would not be parked by the phaser wait. Explaining the 162 s `ClosedSelectorException` then needs an extra assumption about Jetty's stop ordering (for example, the aborted stop closing the selector).
- Discriminator: `asyncTrackerAvailablePermits()` below the max at close start, and a thread dump showing the close thread in `Phaser.awaitAdvanceInterruptibly`.

### Ruled out for this signature

- Test-held shared client (state C). It hangs at MiniSolrCloudCluster.java:618-625 with no pool message, so it cannot produce "Timeout waiting for pool to shutdown" from the closeThreadPool.
- "Executor never shut down" (state D). `commExecutor` is shut down at `HttpShardHandlerFactory.java:356`. The real issue is the order (stop, then drain), which is part of M1, not a missing shutdown.
- Branch-caused. Unchanged from the prior report (H3 there).

## 5. Reproduction spec (Linux lane)

### 5a. Class-level (the observed trigger)

Run the prior report's section 6 spec unchanged: module `:solr:modules:gcs-repository`, class `org.apache.solr.gcs.GCSInstallShardTest`, seed `B15D4D92C6F2B656`, JDK 21, `-XX:ActiveProcessorCount=1`, 5 repetitions per tree, same seed each time. Trees: base `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a`, main `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (record the SHA the lane reads), PR head `77c019e1ff0e922e3c2350379dece841651b1d44`.

Add the diagnostic, once per repetition that shows the signature (or as a second run): a thread dump about 30 s after "Shutting down CoreContainer" appears, with `jcmd <pid> Thread.print`, and a second one about 70 s after. Keep them with the console log.

Pass conditions, per mechanism:
- M2 confirmed: the 30 s dump shows the closeThreadPool thread in `HttpJettySolrClient.waitForComplete` → `Phaser.awaitAdvanceInterruptibly`.
- M3 confirmed: the 30 s dump shows an httpShardExecutor thread in `Semaphore.acquire` under the `HttpJettySolrClient` queued-listener lambda, with `LBAsyncSolrClient` frames below.
- Neither, but M1: the 30 s dump shows the closeThreadPool thread in the Jetty latch and an httpShardExecutor thread in `ManagedSelector` select or produce frames that are not parked on a lock or acquire. The park is then a new site. Record the frames verbatim; this needs a Jetty-level look.
- State C: the dump shows the close thread in a test-held client's close, before any "Shutting down CoreContainer" line. Not this signature.

Repetition counts and signatures are as in the prior report (SIG-SHUTDOWN, SIG-OTHER, CLEAN). A CLEAN result is not clearance.

### 5b. Unit-level, deterministic, no cluster (for the ordering claim and M3)

Core test in `org.apache.solr.handler.component`, shaped on `TestShardHandlerFactory` (`CoreContainer.createAndLoad(home, solr.xml)` and `cc.getShardHandlerFactory()`). Its solr.xml copies the test cluster's shardHandlerFactory values: `socketTimeout` 90000, `connTimeout` 15000 (`MiniSolrCloudCluster.java:115-119`). Hold server: a `ServerSocket` that accepts and never answers (the `FakeTcpServer` shape in `AsyncTrackerSemaphoreLeakTest.java`, without `rstAll`).

U1 (M1 ordering, plus M2 as the first-phase holder):
1. Build the factory `f` (an `HttpShardHandlerFactory`). Send one request with `f.loadbalancer.requestAsync(lbReq)`, endpoint = the hold server, the same call as `HttpShardHandler.java:285`. `defaultClient` is `protected volatile` (`HttpShardHandlerFactory.java:88`), so the test sits in the same package.
2. Assert `asyncTrackerAvailablePermits()` is max minus 1.
3. Mirror CoreContainer's close structure: `ExecutorService p = ExecutorUtil.newMDCAwareCachedThreadPool("closeThreadPool"); p.execute(f::close); ExecutorUtil.shutdownAndAwaitTermination(p);`. Time it. Start a monitor thread that reads `f.commExecutor` active count at 60 s after the interrupt.
4. Predicted under M1: `shutdownAndAwaitTermination` throws "Timeout waiting for pool to shutdown" for closeThreadPool at about 120 s, and a second error names poolName=httpShardExecutor.
   - Pass for M1's ordering claim: the signature appears, and commExecutor still has an active thread at 60 s after the interrupt.
   - Refutes section 1's ordering claim if the signature does not appear, or if commExecutor is empty at that point while the close still times out.

U2 (M3 shape):
1. Set `solr.solrj.http.jetty.async_requests.max=1` before building the factory (read at client construction, `HttpJettySolrClient.java:877`).
2. Send request R1 to the hold server. It takes the only permit.
3. Send request R2 with endpoints [an RST server, the hold server]. The RST (the `rstAll` technique) fails R2 on an IO or executor thread, and LB retry runs `queuedListener` there. The acquire parks because R1 holds the only permit.
4. Dump threads (`jcmd` or `Thread.getAllStackTraces()`). Expect an httpShardExecutor thread parked in `Semaphore.acquire` under the queued-listener lambda.
5. Run the close as in U1 step 3.
   - Pass for M3: the parked thread appears in the dump and the signature appears; the selector failure (if logged) appears only after the forced `shutdownNow`.
   - Caveat: `AsyncTrackerSemaphoreLeakTest`'s Pattern B test builds its client without an executor (the client's own pool), so it does not exercise the shutdown ordering. U2 is the variant that does.

U3 (state C, test-held client):
- An async request on `cluster.getSolrClient()` against a jetty whose proxy is paused (`SocketProxy.pause()`), left in flight at the end of a test.
- Pass: `MiniSolrCloudCluster.shutdown()` hangs at MiniSolrCloudCluster.java:618 or 623 with no "Shutting down CoreContainer" line and no closeThreadPool message, bounded only by the suite timeout.

### Seeds and settings that matter

- Class-level: seed `B15D4D92C6F2B656` and the locale and timezone from the prior report.
- Test cluster `socketTimeout` 90000 (`MiniSolrCloudCluster.java:117`) sets the M2 bound.
- Sysprop `solr.solrj.http.jetty.async_requests.max` (default 1000). U2 needs 1.
- Do not relaunch on a different seed for the class-level spec; a different seed changes the test set, not the timing.

## 6. Fix sketch (not applied; production owner decision)

- Bound the phaser: `awaitAdvanceInterruptibly(phase, timeout)` with `forceTermination()` on timeout (`HttpJettySolrClient.java:919-931`). Changes shutdown semantics; owner call.
- Break the ordering: when the client close throws or is interrupted, shut down `commExecutor` with `shutdownNow` rather than waiting 60 s for a loop the interrupt has already ended. The natural place is `HttpShardHandlerFactory.close` (lines 352-356).
- Retry dispatch: run LB retries off the completing thread (the SOLR-18174 fix named in the ignored test; not on main). This removes the IO-thread permit park (M3).
- Do not change `MiniSolrCloudCluster.checkForExceptions`. It makes the failure visible.

## 7. Not done

- No builds, tests, gate runs, test-queue commands, GitHub or Jira writes, commits, pushes, or claim edits. This file is the only write.
- No claim file was written: `claims/pool-flaky-rca-cluster-shutdown-hang.md` (the one-file limit). WORKFLOW.md rules 1 and 5 require a claim before work and a DONE mark in the same push. The parent must create or mark it.
- No thread dump exists on disk. The M2 versus M3 split stays open until the section 5a dump is taken.
- Jetty source is not on disk. ManagedSelector, ClientConnector and selector-thread placement are inferred from frames and from log line 13097.
- Not read: `OverseerCollectionMessageHandler` and the CoreAdmin install and restore shard requests (to check whether GCS tasks leave async requests outstanding); the run 37987579785 log (the prior report's cross-check of its chain still stands); the CI merge commit.
