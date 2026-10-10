# Post-PR review round 5, slice 2b: SOLR-18531 code check at head c8a67c7

Scope: the code at the head of SOLR-18531, then the Limits statements about release routes in PR 5101 (the live body, read only at item 9). Read-only. No build, Gradle, test, gate or reproduction run.

## Head and sources

- `git ls-remote origin refs/heads/solr-18531-submit` returns c8a67c7267d00e33ad8323c683c040b539989b0d, equal to the named head. A read-only fork fetch went to refs/remotes/origin/solr-18531-submit.
- Base 3f5d4c5bf8ac. Diff against base: 4 files, 226 insertions, 2 deletions (the changelog YAML, MiniSolrCloudCluster.java, JettySolrRunner.java, TestJettySolrRunner.java).
- Paths below are repo-relative at the head, read with `git show c8a67c7...:<path>`:
  - `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java` (JSR)
  - `solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java` (MSCC)
  - `solr/test-framework/src/java/org/apache/solr/util/SocketProxy.java`
  - `solr/test-framework/src/test/org/apache/solr/embedded/TestJettySolrRunner.java`
  - `solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java` (line 849 only)
- Start condition: the header of `receipts/SOLR-18531.md` on origin/pr-prepare reads "GATE GREEN at the branch head (re-gate 3 finished 2026-10-10)", with gated head c8a67c7. The condition is met. The receipt's "In flight" paragraph still describes re-gate 3 as running, so it is stale against that header.

## Reservation call sites at the head

`git grep` over `solr/` at the head finds these and no others:

- Add: `reserveJettyPort()` is called only from `stop()` (JSR 684). It puts the socket with `RESERVED_PORTS.putIfAbsent(jettyPort, socket)` (JSR 731). The map is keyed by jettyPort only.
- Release: `releasePortReservation(port)` at JSR 517 (in `start(boolean)`), `releasePortReservation()` at JSR 978 (in `close()`), and `jetty.releasePortReservation()` at MSCC 645 (in `shutdown()`). The public method (JSR 745-747) delegates to the private static one (JSR 749-754), which removes the entry for the given port.

| Path | Lines | What it releases |
|---|---|---|
| `start(true)` / `start()` on the same port | JSR 506, 512, 517 | The port being bound, before init and the bind. With reusePort true and jettyPort != -1 that is the old jettyPort. |
| `start(false)`, e.g. `startJettySolrRunner(jetty)` | MSCC 489-491 to 503; JSR 512, 517 | config.port only. The old jettyPort entry is not touched. |
| `close()` | JSR 968-979 (stop at 970, release at 978) | The current jettyPort, after stop(). |
| `MiniSolrCloudCluster.shutdown()` | MSCC 627, 644-646 | The current jettyPort, for the runners in the snapshot taken at 627 (those still listed at shutdown). |

## Item verdicts

### 1. N2 guard (second stop): CONFIRMED

JSR 705-711, inside `reserveJettyPort()` (701):

```java
if (RESERVED_PORTS.containsKey(jettyPort)) {
  // A reservation for this port is already held: a second stop() on this runner
  ...
  return;
}
```

On a second stop() the method returns before it creates a ServerSocket or binds. The "Could not reserve port {} after stop; a restart on this port is not protected" WARN (JSR 723-728) is not logged, and the socket from the first stop stays in RESERVED_PORTS. The guard also returns when another runner holds an entry for that port. The port is held either way.

### 2. Release routes: CONFIRMED

The set of routes matches the table above and the rule at JSR 115-124 ("An entry is released in exactly three ways: a runner starts on the port; MiniSolrCloudCluster.shutdown() releases the runners still in the cluster at shutdown; or the runner holding the entry is closed"). Release by start(true) and by start(false) are different, as item 4 shows. Release by close() and by shutdown() both name the current jettyPort only.

One accuracy note on the code comment: at JSR 118-120 it says "the runner holding the entry is closed", but the release is by port number (JSR 749-754), so a close() can remove an entry that another runner placed on the same port. See the completeness notes.

### 3. Runners removed by stopJettySolrRunner: NOT released at shutdown. CONFIRMED (the code matches the stated limit)

- Removal: MSCC 514-519 (`jetty.stop()`, then `jettys.remove(index)` at 517) and MSCC 528-532 (`jetty.stop()`, then `jettys.remove(jetty)` at 530).
- shutdown(): MSCC 627 `List<JettySolrRunner> stoppedJettys = new ArrayList<>(jettys);`, MSCC 632 `jettys.clear();`, MSCC 644-646 `for (final JettySolrRunner jetty : stoppedJettys) { jetty.releasePortReservation(); }`. A removed runner is not in that snapshot. The comment at MSCC 639-643 says the same.
- No other path: MSCC has no `.close()` call at the head. A removed runner is released only by (a) a start on the same port with reusePort true (MSCC 501-503 reaching JSR 517), (b) the caller calling `close()` (JSR 968-978), or (c) JVM exit.

### 4. Fresh-port restart leaves the old reservation: CONFIRMED

- JSR 506 `public synchronized void start(boolean reusePort)`. JSR 512 `int port = reusePort && jettyPort != -1 ? jettyPort : this.config.port;`. JSR 517 `releasePortReservation(port);`.
- With reusePort false, `port` is config.port, so the entry for the old jettyPort is not removed. jettyPort is then overwritten at JSR 335 (`jettyPort = getFirstConnectorPort();`). close() and shutdown() release only the new value, so the old entry is left in the map.
- Route: MSCC 489-491 `startJettySolrRunner(JettySolrRunner jetty)` calls `startJettySolrRunner(jetty, false)` (490), which calls `jetty.start(reusePort)` (MSCC 503).

### 5. Proxy port: no reservation covers it. CONFIRMED

- JSR 207-214: `proxy = new SocketProxy(0, ...)` (209), then `setProxyPort(proxy.getListenPort())` (213). JSR 336-338: `hostPort` uses proxyPort when set. JSR 545-551: `proxy.reopen()` (547) or `proxy.open(...)` (549) on start. JSR 638-640: `proxy.close()` (639) on stop. JSR 825: `getLocalPort(false)` returns proxyPort when set.
- RESERVED_PORTS is keyed by jettyPort only (JSR 705, 722, 731). Nothing reserves the proxy port.
- SocketProxy.java: the constructor binds port 0 (102-111). `close()` (168-179) closes the acceptor at 177 (`Acceptor.close()` at 491-498). `reopen()` (204-225) binds `proxyUrl.getPort()` at 218. Its catch (220-224) logs only at debug (221-223) and does not rethrow.

### 6. Standalone runners: CONFIRMED

- `stop()` reserves for every runner (JSR 684). `reserveJettyPort()` has no cluster check.
- Only `start(boolean)` on the same port (JSR 517) and `close()` (JSR 978) release it. A standalone runner that is stopped and never closed or restarted keeps its entry until a same-port start or JVM exit. JSR 118-124 says so ("In every other case the entry is held until the JVM exits"), and JSR 741-743 says so for the removed-runner case.

### 7. Served-traffic test: CONFIRMED

- TestJettySolrRunner.java 177: `public void testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart() throws Exception {`
- Served before the stop (198-201):

```java
try (HttpJettySolrClient client =
    new HttpJettySolrClient.Builder(runner.getBaseUrl().toString()).build()) {
  new CoresApi.GetAllCoreStatus().process(client);
}
```

- `runner.stop();` at 203. Foreign probes with reuse false and true at 209-217. Restart and serve again at 220-226. The other reservation test is at 122 (`testStoppedRunnerKeepsItsPortUntilRestart`).
- Reading note, not run: the reuse=true probe (209-217) expects a refusal. The code comment at JSR 718-720 says that refusal is verified only on Linux. The test has no platform guard, and Windows address-reuse semantics differ, so that probe may not behave as asserted there.

### 8. Linux bind: the premise is CONTRADICTED by the current receipt; the bind behaviour is CHANGED at the head

The receipt does not say the variant "did not reproduce". The current Correction bullet in `receipts/SOLR-18531.md` (origin/pr-prepare) reads:

> "Correction: the earlier record that the served-traffic case did not reproduce came from an uncommitted scratch variant and was wrong. The committed test reproduced the gap: without address reuse on the reservation socket, the reservation bind failed over the TIME_WAIT connections a served node leaves behind, and the port sat unprotected. This head fixes that bind and covers the case with the committed test."

The wording the brief quotes is from an earlier version of the receipt (commit 50ef5ea2a57, line 11): "Linux traffic-before-stop variant (two seeds) did not reproduce the TIME_WAIT gap." The current receipt retracts it.

Bind behaviour, read from the code:

- Round 1 head a0150bf: JSR 703 `socket.setReuseAddress(false);`, then the bind at 704. This is the F2 failure mode.
- Head 351914f (before the N2 commit): JSR 713 `socket.setReuseAddress(true);`.
- Head c8a67c7: JSR 721 `socket.setReuseAddress(true);`, bind at 722. The N2 commit left the bind call unchanged; the lines moved only because of the guard and comments.

So the head changes the reservation bind from non-reuse to reuse. That removes the TIME_WAIT bind failure that round 1 F2 described for the reservation bind. A bind that still fails logs a WARN and runs unprotected (JSR 723-728). The held socket refusing later binds is a reading, not a run: a bound Java ServerSocket is listening (JSR 714-722), and the code comment at 718-720 says a listening socket refuses later binds on Linux with or without reuse. I did not confirm the receipt's Linux result.

### 9. Limits statements about release routes (live body, PR 5101): all CONFIRMED

| Limits statement (as read) | Code at head | Verdict |
|---|---|---|
| "Runners with a socket proxy in front reserve only their Jetty port." | JSR 705, 722, 731 (jettyPort only) | CONFIRMED |
| "The shared cloud test base uses such runners." | AbstractFullDistribZkTestBase.java 849 (`new JettySolrRunner(..., true)`) | CONFIRMED |
| "The proxy port closes on stop and rebinds on start, with no reservation." | JSR 638-640, 545-551; SocketProxy 168-179, 204-218 | CONFIRMED |
| "A failed rebind is logged at debug level and not raised." | SocketProxy 220-224 (debug at 221-223) | CONFIRMED |
| "A runner stopped with `stopJettySolrRunner` and never restarted is removed from the cluster's list, so cluster shutdown does not release its port." | MSCC 514-519, 528-532, 627, 644-646 | CONFIRMED |
| "Closing the runner releases it." | JSR 968-978 | CONFIRMED |
| "Otherwise the port stays reserved until a later start on that port or the JVM exits." | JSR 517 (same-port start), JSR 118-124 | CONFIRMED |
| "A restart on a fresh port leaves the old port reserved: closing the runner releases only the port it currently holds" | JSR 512, 517, 745-747, 978 | CONFIRMED |
| "Standalone runners that are stopped and never restarted or closed also keep their port until a later start on that port or the JVM exits." | JSR 684, 517, 978 | CONFIRMED |

Other release-related statements in the body, outside the Limits list, also match the code: the static map (JSR 125), "the next start on that port closes the socket before Jetty binds the port" (JSR 517 precedes init at 521 and start at 530), shutdown releasing the runners still listed "after the stops finish" (MSCC 636-646, linked as L639-L646), a runner's close() releasing its socket (JSR 978), and the reuse-set bind (linked as L715-L722, matching JSR 721-722).

The body's bullet "The reservation's refusal of later binds is verified on Linux. On Windows ... not verified there" is not a release-route statement. It matches the code comment at JSR 718-720. The receipt does not name the OS of vm1, so the "verified on Linux" evidence cannot be checked here.

Unsupported statements found: none.

## Completeness notes (not contradictions; the body does not claim otherwise)

- (a) Round 1 F6 is not in Limits. `start(boolean)` releases the entry at JSR 517 before init (521) and the bind (530). If the start fails after that, the port is unprotected and nothing restores the entry.
- (b) Round 1 F8 is not in Limits, and it touches the body's "a runner's `close()` releases its own socket" (What this change does). `releasePortReservation(int)` (JSR 749-754) removes whatever socket is in the map for that port. Scenario from the code: runner A stopped on port P (entry held). Runner B created on P and started (JSR 517 releases A's entry), then stopped (B's entry). A.close() then calls stop(), which returns early at 705-711, and releases port P at 978, which removes B's entry while B is still stopped. So close() can release another runner's reservation. This is a code reading, not run.
- (c) The receipt's "In flight" paragraph is stale against its own header (GATE GREEN at c8a67c7).

## Overall verdict

SATISFIED for this part only. Every Limits statement about release routes (items 2 to 6, and the bulleted Limits in item 9) matches the head code. The slice 2 verdict as a whole is not decided here: the Proof section, the N1 line placement, the round 4 items and the whole-body read belong to other parts.

## Not checked

- Any test, build, gate or log result. The receipt's re-gate 3 log on vm1, the 4-of-4 and 2-of-4 counts, and the TestPullReplica runs were taken as written.
- The OS of vm1, and the Linux kernel behaviour behind items 7 and 8 (the TIME_WAIT bind and the listening-socket refusal). Those are readings of the code and comments.
- Jetty's stop behaviour that leaves server-side TIME_WAIT sockets (the premise of round 1 F2).
- Other test code that calls `stopJettySolrRunner` and then `close()`, which sets how often item 3 happens in practice. Not surveyed.
- The changelog YAML title (round 1 F10), and SolrJettyTestRule proxy use (cited by round 1 at line 77-85). Not checked at the head.
- Round 4 slice 2 items other than release routes (the Proof seed phrase, bold summaries on all sections, the N1 line placement) and the whole-body read. Not in this part.
