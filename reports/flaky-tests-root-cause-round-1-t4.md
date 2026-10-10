# Flaky tests round 1, test 4: LeaderElectionIntegrationTest.testSimpleSliceLeaderElection, port already in use

Result: most likely, while runner A was stopped, a process outside this test JVM took A's pinned port P as a listening socket and kept it for the whole 60-second retry budget. So A's restart at `LeaderElectionIntegrationTest.java` line 95 failed with BindException. Confidence about 60 percent. The code shows the path exists and that it is fatal. The record does not name the holder.

The main side's "infrastructure" label fits the location. "Port collision" alone is not the whole story, though: the framework releases the port without holding it, and it retries only on that same port.

## Where the code is

The assignment's path does not exist at `8e62c2686882`. `JettySolrRunner` is at `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`, and `retryOnPortBindFailure` is at line 559 of that file. Its unit test is `solr/test-framework/src/test/org/apache/solr/embedded/TestJettySolrRunner.java`.

Short names (repo-relative at `8e62c2686882`):
- JSR = `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`
- JC = `solr/test-framework/src/java/org/apache/solr/embedded/JettyConfig.java`
- MSCC = `solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java`
- LEIT = `solr/core/src/test/org/apache/solr/cloud/LeaderElectionIntegrationTest.java`

## Questions 1 to 4

**Q1. Who allocates the port, and how long is the gap?**
- First start: no Solr code chooses a port. JC line 73 defaults `port` to 0. MSCC lines 454 to 456 clone that config and start the node. JSR line 275 (test-mode branch) or 289 (other branch) sets the connector to that port, so the kernel picks a free one at bind time. JSR line 320 reads it back into `jettyPort`. ZooKeeper also binds port 0 (`solr/test-framework/src/java/org/apache/solr/cloud/ZkTestServer.java` lines 368 to 369). Nothing on this path probes a port and closes it.
- Restart: LEIT lines 94 to 96 call `runner.start()`, which is `start(true)` (JSR lines 480 to 481). JSR line 497 picks `jettyPort`. JSR lines 501 to 502 rebuild the connector on that port, and lines 274 to 276 (or 288 to 290) bind it. The port is the one from the first bind. No new port is chosen.
- Gap: the port is released at LEIT line 84 (`jetty.stop()`, JSR lines 611 to 670, server stop at line 627). JSR keeps no socket on the port after stop. Jetty closes the listening channel when the connector closes (checked in bytecode; the call from `doStop` was not checked). The restart comes at LEIT lines 94 to 96, after four stop-and-wait iterations (lines 68 to 92). The record gives no elapsed time for the gap.

**Q2. What does `retryOnPortBindFailure` retry?**
- Same port only. Each attempt is `server.start()` at JSR line 566, on the connector built for `jettyPort`. The `port` argument is used only in the log line (line 565).
- Budget: `TimeOut(portRetryTime, SECONDS)` at JSR line 560, default 60 seconds (JC line 83). A failed attempt logs (line 571), stops the server (line 572), sleeps 3 seconds (line 573), and loops if the budget is not spent (lines 574 to 576). Otherwise it throws (line 579). That is about 20 attempts. The timeout is checked after the sleep, so there is no attempt after the budget ends.
- The retry does engage for this exception. Checked in jetty-server 12.1.12 bytecode: the Server constructor sets `_openEarly` to true. In `Server.doStart`, the lambda `lambda$doStart$0` calls `NetworkConnector.open()` and adds any failure to an `ExceptionUtil$MultiException`, whose `ifExceptionThrow` rethrows an Exception unchanged. So the java.net.BindException reaches JSR line 568, which catches IOException.
- Why it did not save the run: the holder stayed for every attempt. This is an inference. A surfaced exception means every attempt in the budget failed. The record does not show the attempt count or the elapsed time.

**Q3. A race in one JVM, a leftover, or a cross-suite conflict?**
- Two runners in one JVM grabbing the same free port before either binds: ruled out. The first-start port comes from the kernel's bind(0) (JSR lines 275 and 289), and two live listeners cannot share a port.
- In-JVM collision on restart: ruled out for this test. Until LEIT line 84, runner A's listener held P, so any in-JVM socket with local port P must have been created after line 84. LEIT lines 68 to 96 start no runner between the stop and the restart. The in-JVM sockets in that window are Solr HTTP client sockets, which set SO_REUSEADDR (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` line 259). A kernel rule, not checked in this repo: a reuse-enabled socket does not block a reuse-enabled bind unless it is in LISTEN state. No in-JVM listener exists in the window.
- Leftover from an earlier suite in the same JVM: ruled out for a listener on P. The cluster is shut down in `@AfterClass` (`solr/test-framework/src/java/org/apache/solr/cloud/SolrCloudTestCase.java` lines 157 to 163), and by the argument above no earlier listener can hold P. A leaked runner in another JVM is not excluded.
- Cross-suite conflict on the same host: the one the code allows. `gradle/testing/defaults-tests.gradle` lines 44 to 48 set `tests.jvms` to min(max(1, cpus/2), 4), and line 106 uses it as maxParallelForks. So forked test JVMs share one host and one ephemeral port pool. Every other suite's nodes bind port 0 (JC line 73). Once A releases P, a process in another fork can take P as a listener, and nothing prevents it. The CI workflow is `.github/workflows/tests-via-crave.yml` on a self-hosted runner (lines 19 and 41). Whether that host runs several jobs at once is not in the repo.
- TIME_WAIT from an earlier suite: a server-side TIME_WAIT on P does not block, because accepted sockets inherit the reuse flag (kernel rule). A client-side TIME_WAIT with reuse off would block for up to 60 seconds (kernel rule). It would have to be created just before the restart, so it is the least likely.

**Q4. How widespread is the pattern?**
Grep counts at `8e62c2686882`, under `solr/**/src/test`:
- Files matching `SolrCloudTestCase|MiniSolrCloudCluster|JettySolrRunner|startJettySolrRunner`: 335. Of those, 329 have a test method.
- Files reaching runners through shared helpers (`extends AbstractFullDistribZkTestBase|BaseDistributedSearchTestCase`, `ChaosMonkey`, `SolrJettyTestRule`, `RestTestBase`, `SolrTestCaseHS`): 130, of which 126 have test methods. Union with the 329: 435 suites.
- Same-object restarts on a pinned port: 81 call-site lines. Files among the 435 that also contain a stop call: 44. That is an upper bound, because a few start a runner for the first time and stop it in a finally block. Adding helper users that restart (AbstractFullDistribZkTestBase, BaseDistributedSearchTestCase, ChaosMonkey users): 113. The helper restart sites are `AbstractFullDistribZkTestBase.java` lines 524, 565 and 600, `ChaosMonkey.java` lines 595 and 669, and `BaseDistributedSearchTestCase.java` line 300.
- Coverage by Fix A (Part 3): it covers the same-object restarts (44 direct, up to 113 with helpers). It does not cover first starts on port 0, or `startJettySolrRunner(jetty)` (start(false), which takes a fresh port). `SSLMigrationTest` (`solr/core/src/test/org/apache/solr/cloud/SSLMigrationTest.java` lines 63 to 71, start at line 84) builds a new runner pinned to the old port with `setPort`, so it is covered only if the reservation is keyed by port across objects. Fixed-port setups outside this path (`solr/test-framework/src/java/org/apache/solr/SolrTestCaseHS.java` line 444) were not checked.
- `TestCoordinatorRole` also restarts on the pinned path (`solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java` lines 254, 359 and 389). Its failure is ClosedChannelException (test 3), not BindException, so its restarts did bind. The pinned path alone does not explain test 3.

## Part 1: ranked hypotheses

**H1 (most likely; moderate confidence). A process outside this JVM listened on P during the gap and kept it for the whole budget.**
1. P is chosen by the kernel at first bind: JC line 73, JSR lines 275 and 289, JSR line 320.
2. A releases P at stop: LEIT line 84, JSR lines 611 to 670 (server stop at line 627). No reservation is kept.
3. Another process binds port 0 in the gap and receives P. Possible because P is free and other forks bind port 0 (`gradle/testing/defaults-tests.gradle` lines 44 to 48 and 106; JC line 73). A kernel rule, not checked here: bind(0) and connect() skip ports in use.
4. The restart requests P: LEIT lines 94 to 96, then JSR lines 480 to 481, 497, 501 to 502, and 274 to 276 (or 288 to 290).
5. The bind fails inside `Server.doStart` (openEarly, see Q2). The BindException reaches JSR lines 568 to 570.
6. Retry repeats the same bind for the 60-second budget (JSR lines 566 and 573 to 579; JC line 83). A listener held for the rest of another suite outlasts that budget. This matches the record.

**H2 (possible; less likely). The holder is a non-listening socket of another process with SO_REUSEADDR off:** an established client socket, or a client-side TIME_WAIT (kernel rule). Steps 1, 2, 4, 5 and 6 are as in H1, with a different holder at step 3. It needs a socket created in the gap that lasts about 60 seconds. The record does not show it.

**H3 (rejected for this test). An in-JVM holder.** Reasons are in Q3.

**H4 (contributing, not a cause). The design makes a transient holder fatal.** The framework keeps no reservation (JSR lines 611 to 670), retries only on the same port (JSR line 566), and gives up after 60 seconds (JC line 83). No test covers the retry. The only test in this area is `lookForBindException` (`solr/test-framework/src/test/org/apache/solr/embedded/TestJettySolrRunner.java` lines 73 to 115). No test sets `withPortRetryTime` (JC lines 145 to 146) or forces a bind conflict.

Other findings, not causal here:
- `lookForBindException` (JSR lines 593 to 604) spins forever when an IOException's cause is an Error rather than an Exception: the loop at lines 595 to 602 never advances. Not triggered by this record.
- After the sleep, the loop throws without a final attempt (JSR lines 573 to 579), so up to 3 seconds of budget are lost.

## Part 2: evidence

**The record proves** (from the quote in the assignment): a java.net.BindException ("Address already in use") escaped `JettySolrRunner.start` through `retryOnPortBindFailure`, and no assertion ran.

**The record suggests, but does not prove:**
- That the bind was the restart at LEIT line 95. The only pinned bind in this test is there, and a bind(0) at first start does not realistically return EADDRINUSE. Confirm with the test frame line in the full trace, which the record does not quote.
- That the holder is outside the JVM (H1 or H2, over H3). The code excludes an in-JVM listener in this window. The branch (SOLR-7504) changes only `CountFieldValuesUpdateProcessorFactory`, so the branch did not touch ports.
- That the holder lived about 60 seconds. This is suggested by JSR lines 560 and 574 to 579. The stack trace does not show it: a rethrown exception keeps its creation-time trace, which passes through `server.start()` at JSR line 566 on every attempt. The log lines at JSR line 565 (one per attempt) and line 571 do show it.

**The record does not show:** which process held P, the attempt count, the elapsed time, the OS, the fork count, or which workflow ran the job.

**Against H1:** nothing in the record. Its rarity fits one failure in many runs, since a holder must fall into a short window.
**For H2:** nothing in the record. H2 stays open.
**Against H3:** the gap has no in-JVM start (LEIT lines 68 to 96), and in-JVM client sockets set reuse (`HttpJettySolrClient.java` line 259).

## Part 3: proposed fix (not applied)

Verdict: the test is fine. It restarts a node on its own port, which the framework allows. The defect is in shared infrastructure: JSR releases a pinned port with no reservation, and the retry can rebind only that same port for 60 seconds. The fix goes in JSR.

**Fix A (recommended): hold the released port until the next start on that port.**
- File: `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`.
- Add a static `ConcurrentHashMap<Integer, ServerSocket>` of reserved ports. It must be static, so that a new runner pinned to the same port, as in `SSLMigrationTest`, sees the entry.
- In `stop()` (lines 611 to 670), after `server.stop()` and the join loop (lines 627 and 655 to 661), when `jettyPort` is set, open `new ServerSocket()`, call `setReuseAddress(false)` explicitly (the Java ServerSocket default is a platform matter, not checked here), bind to `127.0.0.1:jettyPort`, and store it in the map. On IOException, log a warning and continue, which is today's behavior.
- In `start(boolean)` (lines 491 to 513), before the retry block at lines 507 to 513, remove the map entry for the port about to be bound and close that socket. Then the existing `server.start()` and retry run unchanged.
- Cleanup: a runner that is stopped and never restarted keeps its reserved port until a restart or JVM exit. Release the entries for a cluster's runners in `MiniSolrCloudCluster.shutdown()` (MSCC line 615) through a static helper added to JSR.
- Residual window: the time between closing the reservation and Jetty's bind, a few milliseconds. Closing it fully would need the reserved channel itself to become the listener, or SO_REUSEPORT. Not checked in this round.
- Why the same port: node names include the port, and the test compares node names (LEIT lines 119, 126 and 141). Keeping the port keeps node identity.
- Not changed: `retryOnPortBindFailure`. A longer budget does not help if the holder outlives it.

**Fix B (test-side, test 4 only; fallback):** change LEIT lines 94 to 96 to `cluster.startJettySolrRunner(runner, false)` (MSCC lines 501 to 503), which goes to start(false) and takes a fresh port. This removes the exposure for this test only. It leaves the other 44 to 113 suites exposed, and it changes node identity. Not recommended as the primary fix.

## Part 4: verification runs (main side)

**V1: diagnostic on current main, logging only (no behavior change).** Module `:solr:core`; class `org.apache.solr.cloud.LeaderElectionIntegrationTest`, method `testSimpleSliceLeaderElection`; `-Ptests.seed=3E3D9FF553211ED6`; `-Ptests.jvms=4`, with other cluster suites running in parallel forks, so that foreign processes bind port 0. On BindException, log the output of `ss -tanp` filtered to port P (owning PID and state), the elapsed time from the first attempt to the throw, and the count of "Trying to start Jetty on port" lines.
- Confirms H1: a LISTEN socket on P owned by a PID other than the test worker, an elapsed time near 60 seconds, and about 20 attempts.
- Confirms H2: a TIME_WAIT or ESTABLISHED socket on P owned by another PID, with no LISTEN socket.
- Refutes H1 and H2: no socket on P at the failure time, or only one attempt logged. The second case would mean the retry did not engage, and the rethrow analysis in Q2 would be wrong.
- Refutes H3: the holder is the test worker's own PID. That would reopen H3.
- Caveat: the seed reproduces the worker's ordering, not the other forks, so V1 may not fire. If it does not fire, it settles nothing.

**V2: deterministic mechanism test.** Module `:solr:test-framework` (task `:solr:test-framework:test`); class `org.apache.solr.embedded.TestJettySolrRunner`; a new method. Steps: build the config with `JettyConfig.builder().withPortRetryTime(5)`; start a runner; read its port P; stop it; open `new ServerSocket()` with reuse off, bound to `127.0.0.1:P` (a foreign-style holder); then call `start()`.
- On current main: the holder binds, and `start()` throws BindException after about 5 seconds. This shows the gap with no reservation, and that a single foreign listener fails a restart (H4).
- On main plus Fix A: the holder's bind throws BindException, and `start()` succeeds. This shows the fix holds the port.
- Caveat: this is an in-JVM stand-in. It proves the infrastructure gap, not which process held P in CI.

**V3: regression on the real test.** Module `:solr:core`; `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection`; seed `3E3D9FF553211ED6`; on main plus Fix A; repeated with `-Ptests.jvms=4` and load. Confirms the fix: the test passes with no BindException, and V2 is green on the fix tree. Also run the 44 pinned-restart suites (for example TestCoordinatorRole, TestTlogReplica, TestPullReplica) for regressions, because Fix A changes stop and start. Run the spotless check for `solr/test-framework`, because the fix touches that module.

## Owner decisions

1. Keep a stopped node's port reserved until a restart or cluster shutdown (one bound port per stopped node, JVM-wide). Options: (a) yes, with release at cluster shutdown; (b) no, accept the flake. Recommendation: (a).
2. Restarts keep the same port (the current semantics), or move to a fresh port (Fix B, test-only). Recommendation: keep the same port in the framework, and use Fix B only if the owner wants a smaller change for test 4.
3. Raise `portRetryTime` above 60 seconds. Recommendation: no. A longer retry does not help if the holder outlives it.

## Not checked

- The CI log for run `37991927523`. Only the RCA quote in the assignment was available. Not checked: attempt count, elapsed time, the test frame line, runner OS, fork count, and which workflow ran the job.
- Kernel and JDK rules (bind(0) and connect port choice, SO_REUSEADDR and LISTEN conflicts, TIME_WAIT length, the Java ServerSocket default). These are general knowledge, not checked in this repo or on a host. V1 and V2 are the checks for them.
- ZooKeeper client socket options and any reconnect activity during the gap.
- Jetty was checked by bytecode only (javap on the cached jetty-server and jetty-util 12.1.12 jars). No build was run, and the Jetty source was not read. The call from `doStop` into `ServerConnector.close` was not confirmed.
- The 44 and 113 restart counts are grep-level. Each file was not read to confirm a restart of a previously stopped runner.
- Whether Fix A's reservation closes the bind window fully (SO_REUSEPORT, or passing the bound channel to Jetty).
- Port values at `SolrTestCaseHS.java` line 444.
- The probe-then-close tests (`SolrProcessManagerTest.java` lines 74 to 75, `TestSolrCLIRunExample.java` line 331 and others, `AuditLoggerIntegrationTest.java` line 638, `AsyncTrackerSemaphoreLeakTest.java` line 349) were located by grep only and not read. They are a separate allocation pattern and do not use JettySolrRunner.
- No edits, builds, tests, `gh` calls or fetches. The only tool-side inspection was read-only bytecode through the JDK javap on cached jars, plus git reads at the pinned SHA.
