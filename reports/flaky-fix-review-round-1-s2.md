# Flaky-fix review round 1, slice 2: SOLR-18531

Head checked: `origin/solr-18531-submit` = `a0150bf71e8e4d630fe55ae04482951a5ca3e179`, matching the named head. Base `3f5d4c5bf8ac`. Four files changed (+124, -2): `JettySolrRunner.java`, `MiniSolrCloudCluster.java`, `TestJettySolrRunner.java`, and the changelog YAML.

## Verdict: HOLD

Do not open the PR yet. The draft is written as a hold draft (`pr-drafts/flaky-fixes/SOLR-18531.md`), with its open items tagged `[HOLD]`.

Blocking before opening:
1. F1: the shutdown release misses runners that were stopped and removed from the cluster list. The stated behavior does not hold for the no-restart case.
2. F2: on Linux, the reservation bind may fail for a node that served traffic before stopping, and the code then runs without protection. Not verified here. The deterministic test cannot see it.
3. F4: a restart on a fresh port leaves the old port reserved. Small fix.

Disclose or decide (owner): F3 (proxy port), F5 (standalone runners), and the `TestPullReplica` skip (O3).

Sources: `assignments/pool-flaky-fix-review-round-1.md`, `pr-formula.md`, `WORKFLOW.md`, `reports/flaky-tests-root-cause-round-1-t4.md`, `receipts/SOLR-18531.md`, `gates/SOLR-18531.md`, the branch diff, and the four changed files at head. The ticket text is not in any local file I searched (workspace root CSV, session notes, research folder), so the verdict rests on the assignment summary and the t4 report. The "implementation notes" the brief names are not in the workspace as a separate file. The commit message is the only implementation note, and it does not mention proxies. The proxy analysis below is from the code and from `reports/flaky-tests-root-cause-round-1-t2.md` lines 33-36, 136 and 149.

## Answers to the audit questions

**Q1. Reservation cleanup on every exit path.** Partly. A socket is held after a successful `stop()` (`JettySolrRunner.java` 677-679). It is released at the top of every `start(boolean)` (512), before `init` and the bind, so success and failure both drop it. Cluster runners still in the list are released at shutdown (`MiniSolrCloudCluster.java` 642-644). A failed reservation closes its socket (710). A second stop closes its new socket (713-715). An exception before line 679 holds nothing. Paths that keep a socket until the JVM exits: runners removed by `stopJettySolrRunner` (F1), the old port after `start(false)` (F4), and standalone runners (F5).

**Q2. Suites that never restart.** Not met for the common case. Cluster runners still listed are released after the stops finish and before the shutdown error is rethrown (MSCC 642-647). The order is correct. But `stopJettySolrRunner` removes the runner from `jettys` (MSCC 517 and 530), and the shutdown snapshot is taken from `jettys` (627). So a runner stopped and left down is never released (F1). Standalone runners are never released (F5).

**Q3. The proxy-port subtlety.** Not handled. The reservation is keyed by `jettyPort` only (704, 713, 724, 728). With a proxy, clients and peers use the proxy port (`hostPort` at 332-333; `getLocalPort()` at 796-804). Stop closes the proxy (633-635), and start rebinds it on the same port (540-542). `SocketProxy.reopen()` catches a failed bind at debug level and does not raise it (`SocketProxy.java` 204-225, 220-224). No reservation covers the proxy port (F3). The cluster's runners are built with the 3-argument constructor (`JettySolrRunner.java` 181-183), which sets `enableProxy=false`, so the t4 test-4 path is not affected. The proxy runners are built in `AbstractFullDistribZkTestBase.java` 849 and `SolrJettyTestRule.java` 77-85, and their helper restarts are affected.

**Q4. Does the change match the t4 finding?** In its core, yes. The static map (120), the hold on stop with address reuse off on 127.0.0.1 (696-716), the release on start (512), the release at cluster shutdown (642-644), and the same-port restart all match t4 Fix A. The new test has the shape of t4 V2: a foreign bind is refused, then the restart works (`TestJettySolrRunner.java` 119-158). Differences: shutdown misses removed runners (F1), which t4 specified as "a cluster's runners"; the proxy port is not addressed (F3); the release runs earlier than t4's "before the retry block", which is fine and needed, since a restart would otherwise collide with its own reservation. The residual window t4 names (release to Jetty bind) remains. t4 V1 (diagnostic) and V3 (44 suites, repeats, load) are not in the receipt.

**Q5. Changelog YAML.** Plain, authored Nick Shanin, no placeholders, no em dashes, LF endings, ASCII only (checked). The title overclaims (F10). A test-framework changelog precedent exists (`changelog/unreleased/SOLR-18171-test-framework-external-usage-NPE.yml`).

## Findings

**F1. Blocking. Runners removed by `stopJettySolrRunner` are not released at shutdown.** `MiniSolrCloudCluster.java` 514-519 and 528-532 remove the runner from `jettys`. `shutdown()` snapshots `jettys` at 627 and releases only that snapshot at 642-644. A runner the test stopped and left down is never released, so its port stays bound until the JVM exits or a later start on that port. The commit message ("MiniSolrCloudCluster.shutdown() releases its runners reservations") and the Javadoc at `JettySolrRunner.java` 718-721 both overstate this. No test covers shutdown release. Fix: keep a list of cluster-stopped runners in both stop overloads and release that list at shutdown.

**F2. Blocking until verified. The reservation bind may fail on Linux after the node served traffic.** `JettySolrRunner.java` 703-704 binds with address reuse off. 705-711 log a WARN and return with no reservation, and nothing retries. As I understand the Linux bind rule (not checked here: no Linux host, and no test runs allowed), a non-reuse bind fails while any socket on that local port exists, including TIME_WAIT. When Jetty stops, it appears to close accepted connections first, which leaves server-side TIME_WAIT sockets on the node's port for about 60 seconds. So a node that served traffic may fail its reservation at stop. The t4 Q3 TIME_WAIT analysis covers the holder side with reuse on. It does not cover the reservation's own bind, which is non-reuse by design. The new test stops a runner that served no requests (`TestJettySolrRunner.java` 119-158), so the test cannot show this. Verification once the owner approves it: a variant that serves one request before stop and asserts the foreign bind is refused, or a check of CI logs for "Could not reserve port" after nodes that served traffic. If confirmed, see O1.

**F3. Disclose or fix (O2). The proxy port has no reservation, and its failure is silent.** Proxy runners: `JettySolrRunner.java` 195-209; `AbstractFullDistribZkTestBase.java` 849; `SolrJettyTestRule.java` 77-85. `stop()` closes the proxy at 633-635 (`SocketProxy.close()` 168-179, acceptor socket closed at 491-498). `start()` reopens it at 540-542. `SocketProxy.reopen()` 204-225 swallows a failed bind (220-224). A foreign holder makes the node register at a port with no listener, with no BindException and no assertion. Nothing in the branch addresses this.

**F4. Fix (small). A restart on a fresh port orphans the old reservation.** `MiniSolrCloudCluster.java` 489-491 routes `startJettySolrRunner(jetty)` to `start(false)`. `start(false)` picks `config.port` (`JettySolrRunner.java` 507), usually 0, so the release at 512 does nothing for the old port. After the start, `jettyPort` is the new port (330). The old socket stays in `RESERVED_PORTS`, and shutdown releases only the current `jettyPort` (723-725; MSCC 642-643). The socket stays until the JVM exits or a later start on that exact port. Fix: release the old `jettyPort` when a start moves to a different port.

**F5. Owner (O4). Standalone runners hold their port until the JVM exits.** `JettySolrRunner.close()` (946-952) calls `stop()`, which reserves the port. Nothing releases it except a start on the same port. About 15 test files construct `JettySolrRunner` directly (grep-level count), and none of them go through cluster shutdown.

**F6. Minor. A failed start gives up its reservation without restoring it.** `start(boolean)` releases at 512, before `init` (513-519) and the bind (521-527). If the bind or a later step fails, the port is unprotected for any later attempt. No socket leaks. State it in the Javadoc or accept it.

**F7. Minor. A second stop logs a false warning.** A second `stop()` on the same runner tries to bind its own reserved port (696-704). The bind fails and logs "a restart on this port is not protected" (705-711). The first reservation stays in place, so protection holds. This happens on ordinary runs when a test stops a node directly and shutdown stops it again while it is still listed.

**F8. Minor. Release is by port number, not by owner.** `releasePortReservation(int)` (`JettySolrRunner.java` 727-732) removes whatever socket is in the map for that port. A shutdown can remove a reservation another runner placed on the same port. Low risk; note only.

**F9. No action. An exception before line 679 leaves no reservation.** No socket is held, so there is no leak. The port is simply unprotected in that case.

**F10. Fix (changelog). The title overclaims.** `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml` lines 1-3 say a restart "can no longer fail with BindException". Given F1 to F3 and the Linux question, that is not established. Narrow it to what the change does.

**F11. Test gap.** `TestJettySolrRunner.java` 119-158 covers only: stop with no traffic, a foreign bind refused, and a same-port restart. It does not cover shutdown release (F1), a restart on a fresh port (F4), a proxy runner (F3), a node that served traffic (F2), or a failed start (F6).

**Proof gap (O3).** `receipts/SOLR-18531.md` records `TestPullReplica` as 39 tests skipped, 0 failures. None of them ran. The receipt gives no skip reason. The t4 V3 asked for 44 pinned-restart suites with `tests.jvms=4` and load. The receipt records one seed and three suites. The LEIT pass is one run of a failure seen once in CI, so it does not show the flake is fixed.

## Owner decisions (not taken)

- **O1.** If F2 is confirmed on Linux: (a) retry the reservation bind for a bounded time after stop; (b) hold a listening socket instead (this blocks foreign binds even with address reuse on, but connections to the port would wait in the gap instead of being refused); or (c) accept the gap and state it in Limits. Decide after the Linux check.
- **O2.** Proxy port (F3): reserve it too, and make a failed proxy rebind an error; or state it as a limit.
- **O3.** `TestPullReplica` (proof gap above): run it in a configuration where its tests execute (needs an approved verify run); or keep the disclosure in the draft.
- **O4.** Standalone runners (F5): release in `close()` after stop, or accept holds until the JVM exits.
- **O5.** Restart route: keep the same port with a reservation (this change, and t4's recommendation), or move the one affected test to a fresh port (t4 Fix B, fallback).

## Draft status

`pr-drafts/flaky-fixes/SOLR-18531.md` is written to the current head. Proof numbers come only from the receipt. The 39 skipped `TestPullReplica` tests are stated as skipped, not as passing. The `[HOLD]` tags mark F1, F4, F2 and the disclosure choices. The draft must not be opened until the three blocking items clear.

## Not checked

No build, Gradle, test, Selenium or gate run. No Linux host. The TIME_WAIT bind rule is general kernel knowledge, not confirmed in this repo (F2). Jetty stop behavior was not re-read (t4 checked bytecode only). CI logs were not available; the receipt is taken as written. The `TestPullReplica` skip reason is unknown. No claim was changed; the lead marks the claim.
