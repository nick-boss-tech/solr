# Teardown client-interrupt probe, rerun (round 1, with stack capture, vm2): verdict INCOMPLETE

Claim: `claims/teardown-client-interrupt-probe-round-1-rerun-vm2.md`, superseding `claims/teardown-client-interrupt-probe-round-1-vm2.md`. Scaffold: `reports/teardown-client-interrupt-probe-round-1-rerun-scaffold.md` (v2 diff SHA-256 `f6d7f6f252051b6c628bb97614fb3a3cefde4e5fcb0d023ada97a23dc2c22709`). Assignment: `assignments/teardown-client-interrupt-probe-round-1.md`.

## Verdict

**Incomplete.** The without-interrupt arm is a clean boundary. The with-interrupt arm did not return within its 30-second budget. The stack capture shows the closer blocked in the executor-termination wait inside `HttpJettySolrClient.close()`, which runs for up to 60 seconds before it forces shutdown. The cap ended the observation first. Per the assignment, a cap breach is recorded as incomplete, and the cap is not raised here.

The result is not a live-resource or tracker-only survivor after close. Close had not returned, so its tracker release (line 370, after the `finally`) had not run yet. Whether the client leaks depends on what happens after the 60-second wait, which this run did not observe.

## Commands and environment

- One invocation through `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`, `timeout 1200`:
  `./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...testCloseProbeWithoutInterrupt --tests ...testCloseProbeWithInterrupt -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1`
- JDK 21.0.12.1+1, Linux aarch64. Runner rc 0; `BUILD SUCCESSFUL in 5m 32s`.
- Disk: `4.6G free / 479573 ifree` before, `4.6G free / 479571 ifree` after (`logs/teardown-probe-r2/probe-r2.headroom.log`).
- Logs: `/workspace/gates/gates-vm2/logs/teardown-probe-r2/probe-r2.log`. XML: `logs/teardown-probe-r2/probe-r2.xml/TEST-org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.xml`.

## JUnit counts (fresh XML)

2 tests, 0 failures, 0 errors, 0 skipped. `testCloseProbeWithoutInterrupt` 1.038 s. `testCloseProbeWithInterrupt` 60.009 s (30 s arm wait, then 30 s cleanup join).

## Per-arm evidence (`PROBE arm=` lines)

| Field | without-interrupt | with-interrupt |
| --- | --- | --- |
| closer finished within the 30 s budget | true | **false** |
| close return or exception | none | none (closer still running) |
| interrupt flag on the closer after close | false | false |
| tracker entry after close | false | true (close had not returned yet) |
| cleanup close finished | true | true |
| closer alive after cleanup | false | false |

Round 1 recorded `closerAlive=true` for the interrupted arm after cleanup. The rerun recorded `false`. The cleanup close's executor shutdown is what released the closer, and the timing differs between the two runs. The cleanup join ran to its 30 s limit in both runs.

## Stack capture of the interrupted closer (`PROBE-STACK`)

At 5 s and at 30 s the closer thread was `TIMED_WAITING`. Both captures show the same frames (from the XML system-out):

```
probe-closer-with-interrupt state=TIMED_WAITING
  Unsafe.park
  LockSupport.parkNanos(LockSupport.java:269)
  AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:1797)
  ThreadPoolExecutor.awaitTermination(ThreadPoolExecutor.java:1475)
  ExecutorUtil.awaitTermination(ExecutorUtil.java:142)
  ExecutorUtil.awaitTermination(ExecutorUtil.java:135)
  ExecutorUtil.shutdownAndAwaitTermination(ExecutorUtil.java:119)
  HttpJettySolrClient.close(HttpJettySolrClient.java:367)
  HttpJettySolrClientTest.lambda$runCloseProbe$17(HttpJettySolrClientTest.java:977)
```

The pinned source (`solr/solrj-jetty/src/java/.../HttpJettySolrClient.java`, lines 363-369) shows the closer is in the `finally` after `httpClient.stop()` and `destroy()` have already returned without an exception. `shutdownExecutor` is true because the probe builds the client without an executor, so the client owns the `h2sc` cached pool (line 222). `ExecutorUtil.awaitTermination` (`ExecutorUtil.java:135-142`) waits 60 seconds for the pool's tasks. Only after that does it call `shutdownNow` and wait another 60 seconds.

Interrupt flag: the closer was not interrupted when it reached this wait (it is parked in `awaitNanos`, which would throw at once on an interrupt). So the flag was consumed by an earlier call inside close. Which call did so was not captured.

## Thread-name verification

The names are now verified from the run (`PROBE-NAMES`, both arms):
- Server side: `qtp8919640-NN` (Jetty server pool), `qtp8919640-22-acceptor-...oejs.ServerConnector` (acceptor).
- Client side: `h2sc-N-thread-M` (the client executor's threads from `SolrNamedThreadFactory("h2sc")`), and `/127.0.0.1:56144 to /127.0.0.1:38749 workers` (client HTTP/2 selector).
- Round 1's filter (`HttpClient@`, `elector`) matched none of the client threads, so its `none` results were not evidence. The rerun's filter also omits `h2sc` and `workers`.

Thread-to-client attribution: the probe did not map threads to clients. `h2sc-8`, `h2sc-9` and `h2sc-11` appear in both arms, and remain after the clean close in the without-interrupt arm, so they cannot be attributed to the probe clients. `h2sc-22-thread-2` (RUNNABLE) appears only in the interrupted arm's list. Its link to the probe's executor is inferred from the numbering, not verified, and its stack was not captured because `h2sc` was not in the filter.

## Not established

- What the executor's running task is. The probe makes no request, so no task should be running. The stack of `h2sc-22-thread-2` is needed to say why.
- Whether close returns after the 60-second wait, and whether the tracker entry is then released. The cap ended the observation at 30 s.
- Which call consumed the interrupt flag.
- Whether the client leaks a selector or executor thread after a completed close. This needs the arm to run past the 60-second wait.

## Smallest proposed next claim (not authorized here)

One run of the with-interrupt arm only, with the same seed, source and filters, and a per-arm cap that covers the 60-second executor wait (for example 75 s). Add `h2sc` and `workers` to the thread filter so the executor worker's stack is captured. Dump the thread stacks at 5 s, 30 s and 70 s, and record whether close returns, the tracker state after return, and the state of the `h2sc` threads afterwards. This separates a clean return after the bounded wait from a live leak. It does not authorize a fix, a loop, the other ranked probes, or a cap increase without a new claim.

## Limits

- One invocation, one seed, one host, one JDK. Not a reproduction of the CI schedule of PR 5202 or PR 5198.
- The arm budget was exceeded in cleanup as in round 1.
- No Jira, GitHub, PR or upstream write was made. No fix was started.
