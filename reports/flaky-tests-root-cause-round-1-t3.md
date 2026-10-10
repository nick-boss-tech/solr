# Flaky tests round 1, test 3: TestCoordinatorRole.testNRTRestart

Result: the most likely mechanism is the test's own fixed-delay stop of the PULL node (`TestCoordinatorRole.java` lines 259 to 261). That stop closes the same client object the add loop is using, and the loop catches only `SolrException`, so the transport error escapes at line 306. Confidence about 70 percent on this mechanism. Low confidence on the exact sub-path, because the assignment quotes no exception message, timestamps, or log order.

Code basis: `upstream/main` at `8e62c2686882`. No builds, no edits, no fetch.

Short names: TC is `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java`. JSR is `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`. The assignment's path for this file (`solr/core/src/java/...`) does not exist at this SHA. The other short names are the SolrJ and Jetty client classes named in the citations below.

## Part 1: ranked hypotheses

**H1 (about 70 percent): the PULL node is stopped on a fixed timer while the add loop is still adding through it.**

1. The add loop's client is the PULL node's client: TC:221 `client = pullJetty.getSolrClient()`. JSR:847 to 852 returns a cached `HttpJettySolrClient` (field JSR:134).
2. The loop sends `client.add(COLL, d)` at TC:306. `SolrClient.java` lines 130 to 132 and 162 to 167 build an UpdateRequest and call process. `HttpJettySolrClient.java` lines 462 to 534 (`request`) send it to `<pull base URL>/coordinator_test_coll/update`. The URL shape comes from `ClientUtils.java` lines 65 to 72, which was not read in full.
3. The PULL node waits for a leader before it can process the add. `DistributedZkUpdateProcessor.java` line 265 (processAdd), line 274 (setupRequest), line 764 (`getLeaderRetry`). `ZkStateReader.java` lines 158 to 159 set the wait to 4000 ms by default. Lines 968 to 974 return a leader only if its node is in live nodes. Lines 997 to 1029 block for the wait, then throw SERVICE_UNAVAILABLE. While NRT is down, each add sits in the PULL node for about 4 s (an inference from these lines).
4. The timer on the manipulation thread (named at TC:192) stops PULL on a fixed schedule. TC:241 sets `pullServiceTimeMs = 1000 + nextInt(9000)`, seeded. TC:254 restarts NRT. TC:259 sleeps `pullServiceTimeMs`. TC:261 calls `pullJettyF.stop()`. Nothing ties this stop to the add loop's progress.
5. `JettySolrRunner.stop()` (JSR:611 to 670) first closes the cached client at JSR:616 to 617. That is the same object the test holds at TC:221. `HttpJettySolrClient.java` lines 340 to 365 then stop and destroy its Jetty HttpClient, because line 150 sets `closeClient = true` for a client it builds itself. Only after that does JSR:627 `server.stop()` close the server side. JSR:406 to 411 add the GracefulHandler only when graceful shutdown is enabled, and `JettyConfig.java` line 75 defaults that to false. JSR:632 says the stop timeout is 0, so there is no drain.
6. The in-flight add, or the next add attempt, fails at the transport. `HttpJettySolrClient.java` lines 478 to 489 handle failures before the request is committed. Line 476 sets `committed` when headers are written. A committed failure becomes SolrServerException "IOException occurred when talking to server at: <url>", with the IOException as the direct cause (lines 485 and 511 to 513). An uncommitted failure becomes "Connection failed before the request was sent to: <url>", wrapping RequestNotSentException (lines 486 to 488 and 514 to 516). Lines 519 to 525 give "Connection lost at". Lines 505 to 506 give "Server refused connection" when the listener is already gone.
7. Nothing retries. `HttpJettySolrClient.java` lines 462 to 534 are one attempt. The comment at lines 470 to 471 says a failure is safe to retry elsewhere. That refers to callers such as the cloud or load-balancing clients, not this client.
8. The loop catches only SolrException (TC:309). `SolrServerException.java` line 24 shows it extends Exception, and `RequestNotSentException.java` line 29 shows it extends IOException. Neither is caught, so the exception escapes at TC:306.

Consequence: once PULL has been stopped before the first successful add, every later add through this client fails the same way, so the test fails deterministically at that point. The flake is whether the first successful add comes before restart plus `pullServiceTimeMs` (TC:241, 254, 259). Success needs NRT visible as leader on a live node (`ZkStateReader.java` lines 968 to 974) and an accepted forward from the non-leader to the leader (`DistributedZkUpdateProcessor.java` lines 790 to 799). The test's own comments at TC:256 to 258 and TC:299 to 300 describe the intended order: adds succeed once NRT is back, and PULL stops only after that. The code does not enforce that order.

**H2 (about 10 percent): the pooled HTTP/2 connection from the test client to the PULL node went stale, with no restart and no stop.** PULL is not restarted before line 306; its only start is TC:359. Its server connector idle timeout is 260 s (JSR:110, 277, 291). The client's own idle timeout is disabled (`HttpJettySolrClient.java` line 294). The gap since the client's last use (TC:223) is seconds.

**H3 (under 5 percent): the stale session is to NRT, the restarted node, as the assignment suggests.** The test client has no connection to NRT. NRT is contacted only by qaJetty's queries (TC:226 to 231, 275 to 276) and by the PULL node's server-side forward. A server-side failure comes back as an HTTP error, which `HttpSolrClient.java` lines 197 to 262 turn into RemoteSolrException, which TC:309 catches.

## Part 2: evidence

- What the record proves (from the assignment's quote): `client.add` at TC:306 threw SolrServerException caused by ClosedChannelException on the HTTP/2 client transport, after the NRT restart step. Nothing else is quoted.
- What the record only suggests: the PULL stop at TC:261 fell inside the add loop. The quote does not say so.
- What the code shows: items 1 to 8 in Part 1, each with a line citation.
- Inferences, marked as such: during NRT downtime, adds are in flight most of the time (about 4 s per attempt). The stop catches an add only if the first successful add comes after restart plus `pullServiceTimeMs`. A ClosedChannelException is what Jetty raises when a connection closes under an in-flight stream. Jetty's own code is not in the repo, so this is inferred, not read.
- Reading the record's "caused by" line: if ClosedChannelException is the direct cause of SolrServerException, the committed branch ran (lines 485 or 511 to 513), so the request was on the wire when the connection closed. If RequestNotSentException is the direct cause, the uncommitted branch ran (lines 486 to 488 or 514 to 516), and the request was never written. Both follow from the stop. The message decides which, and the quote does not give it.
- What would weaken H1: (a) the add failure is logged before "stopping PULL jetty" (TC:260), which would mean the add was failing while PULL was still alive; (b) the failing URL is not the PULL node's base URL; (c) the connection is shown closed from NRT's side and not from the stop.

## Part 3: proposed fix (test side; not applied)

File TC, method `testNRTRestart`. The defect is the test's timer and its narrow catch, not the shared infrastructure.

Primary change (keeps PULL alive until an add has succeeded):

(a) Before the executor submit at TC:242, add `CountDownLatch addDone = new CountDownLatch(1);` and the import for `java.util.concurrent.CountDownLatch`.

(b) In the manipulation lambda, keep `Thread.sleep(pullServiceTimeMs);` at TC:259. Directly after it, before the log at TC:260, add `if (!addDone.await(2, TimeUnit.MINUTES)) { log.warn("NRT add did not succeed in time; stopping PULL anyway"); }`. The lambda's catch at TC:263 already wraps exceptions.

(c) In the add loop, after `client.commit(COLL);` at TC:307 and before `break;` at TC:308, add `addDone.countDown();`.

Why: PULL stays up until an add through it has succeeded. That matches the order in the test's comments (TC:256 to 258 and 299 to 300). It stops the fixed timer from closing the client mid-add. It adds no `random()` calls, so seed 681E2A715B2CE1D3 keeps the same three sleep values (TC:236 to 241).

Deadlock check: the main thread does not wait on the manipulation future until TC:354, so the await cannot block the add loop.

Secondary change (only together with the primary): at TC:309, also catch SolrServerException and IOException, keeping the same sleep. The added document has a fixed id (TC:297), so a resend after a transport error overwrites rather than duplicates. Do not make this change alone. After a close, the same client fails every time, so the loop would spin until the test timeout.

Rejected alternatives: (i) using `cluster.getSolrClient()` in the add loop changes what the test exercises, because the designed path is the PULL node's failover. (ii) changing `JettySolrRunner.stop()` so it stops closing the cached client is an infrastructure change with broad reach. JSR:825 to 827 documents that the client closes on stop, and other suites may depend on it. The shared code behaves as documented, so the infrastructure does not need a fix for this flake.

Verdict: the test is at fault. Its stop timer is not tied to the add, and its retry catches only SolrException.

## Part 4: main-side verification

Tree A, current main `8e62c2686882` (a settling run is already queued on the main side): module `:solr:core`, class `org.apache.solr.search.TestCoordinatorRole`, method `testNRTRestart`, seed `681E2A715B2CE1D3`.
- Confirms H1 if: the failure is at TC:306 as SolrServerException, and the log shows "NRT jetty restarted." (TC:255), then "stopping PULL jetty ..." (TC:260), then "PULL jetty stopped." (TC:262), all before the failure, with no "successfully added another doc" line (TC:317). The message reads "IOException occurred when talking to server at: .../coordinator_test_coll/update" (committed), or "Connection failed before the request was sent" or "Connection lost at" (uncommitted or after close). All three fit H1 if they come after TC:260.
- Refutes H1 if: the failure is logged before TC:260, or the URL is not the PULL node's, or the connection is shown closed from NRT's side. Then re-rank H2 and H3 and read the server logs.
- Not settled if the seed passes. Then compare with the original failing run's log.

Tree B, main plus the primary fix (secondary optional): same class, method and seed.
- Confirms H1 if the test passes and the log shows "successfully added another doc" (TC:317) before "stopping PULL jetty ..." (TC:260). The pass counts only if that order holds.
- Refutes H1 if it still fails at TC:306 while PULL is alive. Then the transport error has another source.
- Then run at least three other seeds on Tree B to check the class, not only this seed.

## Pattern section input (test 3 side)

Does a client session to a restarted node get reused for the first post-restart request? No. The trace:
- Client object at the failing add: `client` (TC:221), an `HttpJettySolrClient` from JSR:847 to 852 (built at JSR:849).
- Session object: the Jetty HttpClient in the field `httpClient` (`HttpJettySolrClient.java` line 116, built at lines 280 to 283). It holds a pooled HTTP/2 connection to the PULL node's port. Jetty internals not read.
- Node and endpoint: the PULL node (`pullJetty`, TC:215 to 216). Base URL from JSR:762 to 764 (host 127.0.0.1, JSR:290). Path `/coordinator_test_coll/update`.
- Restarted node before line 306: NRT (TC:254). The client never connects to NRT. PULL is not restarted before 306.
- Closing event: TC:261 (`pullJettyF.stop()`, on the manipulation thread), then JSR:616 to 617 (client closed), then JSR:627 (server stop).
- Note on the assignment's wording: it says the add targets "the coordinator collection's /update endpoint". The coordinator is qaJetty (TC:187 to 190), which the add never contacts. The likely meaning is the collection path on the PULL node. Check the URL in the record.

Shared-infrastructure hazard for test 2 and others: JSR:616 to 617 closes the cached client on stop, and JSR:847 to 852 returns the same cached object to every caller. A test that keeps that reference across its node's stop gets a closed client. If test 2 does the same, its mechanism matches this one (a test-scheduled stop under an in-flight request), not stale-session reuse. Test 2 is undetermined from this side, because test 2 was not read here.

Owner decisions: none.

## Not checked

1. Jetty HTTP/2 client and server internals (no Jetty source in the repo). Not read: which exception an in-flight stream gets on `HttpClient.stop()` or `server.stop()`, whether Jetty retries internally (`HttpJettySolrClient.java` lines 862 to 863 hint at a GOAWAY retry), and the stop ordering of connectors and handlers.
2. The failure record's full text: the exception message, stack, and timestamps. Only the assignment's summary was available.
3. The settling run logs (queued on the main side).
4. ZooKeeper ephemeral-node removal when NRT stops. The CoreContainer shutdown path was not read. The 4 s wait argument assumes NRT leaves the live nodes on stop (`ZkStateReader.java` lines 968 to 974).
5. The time from NRT restart to a leader being visible to the PULL node. Not measurable from code. It decides whether the failure fires.
6. `ZkStateReader.java` lines 158 to 159 read the wait timeout once per JVM. `TestPullReplica.java` line 96 sets it to 1000 ms. Whether suites share a JVM in this build was not checked, so the effective timeout in the failing run is unknown.
7. Test 2 was not read, per instructions. The test 2 side of the pattern verdict is not made here.
8. The repo-wide count of suites with the stop-while-holding-a-client pattern was not done. A quick grep was too loose to use.
9. One scratch write went to `/tmp` by mistake and was deleted. Nothing was written to the worktree.
