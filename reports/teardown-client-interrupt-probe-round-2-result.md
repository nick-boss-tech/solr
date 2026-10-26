# Teardown client-interrupt probe, round 2 (vm2): result of the corrected v3.1 pair

Supersedes for the corrected run: the STOPPED record `reports/teardown-client-interrupt-probe-round-2.md` (scaffold failure, kept unchanged). Scaffold: `reports/teardown-client-interrupt-probe-round-2-scaffold-v31.md` (diff SHA-256 `dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`). Source `d298cd0b42602247bee7bf9e2bcf302e725f6690`, seed `FFF5A3C3AAC69084`, JDK 21.0.12.1+1, Linux aarch64, security manager on.

## Verdict

- **Control (no interrupt): clean boundary.** Close returned normally in about 53 ms. The tracker entry was released. The executor terminated. The HttpClient stopped.
- **Interrupted: tracker-only survivor.** The original close finished at 60.048 s with a `RuntimeException: Exception on closing client`, caused by an `InterruptedException`. The tracker entry was still present at the terminal snapshot. Resource state does not show a live resource after the close: the executor was terminated, the closer had terminated, the selector thread was gone, and the HttpClient was in the FAILED state, not stopped. So this is not classified as a live-resource survivor.

## Interrupted arm: mechanism from the captured evidence

Evidence file: `board-evidence-r2/interrupted.evidence.log` (the full all-thread snapshots, no filter).

1. The interrupt flag was set before close (`interruptBeforeClose: true`).
2. The first point of contact was Jetty's selector stop, not the executor. The terminal throwable chain is:
   - `RuntimeException: Exception on closing client` at `HttpJettySolrClient.close(HttpJettySolrClient.java:364)`.
   - Cause: `InterruptedException` at `CountDownLatch.await`, from `org.eclipse.jetty.io.ManagedSelector.doStop(ManagedSelector.java:139)`, reached through `ClientConnector.doStop` and `SelectorManager.doStop`.
   - The interrupt flag was consumed by that `InterruptedException`. `interruptAtEnd: false` is therefore explained by the exception, not by a clear elsewhere.
3. The `finally` in `close()` then ran `ExecutorUtil.shutdownAndAwaitTermination` (`HttpJettySolrClient.java:367`). The closer was `TIMED_WAITING` in `ExecutorUtil.awaitTermination` (`ExecutorUtil.java:142`) at both the 5 s and 30 s snapshots.
4. The active worker at 30 s was `h2sc-21-thread-2`, the Jetty client's selector loop: `ManagedSelector.SelectorProducer.select` → `nioSelect` → `EPoll.wait`. The executor reported `active=1, shutdown=true, terminated=false`.
5. The worker ended before the terminal snapshot. The executor reported `active=0, terminated=true`, and the `h2sc-21` threads were gone.
6. Inference (not directly observed): the first 60 s `awaitTermination` timed out, `shutdownNow` interrupted the selector, which exited, and the second wait returned. This matches the timing and the `ExecutorUtil` code path (`ExecutorUtil.java:129-149`), but no thread interrupt was captured.

Executor and resource timeline (interrupted arm):

| Snapshot | t | closer | executor | HttpClient | tracker |
| --- | --- | --- | --- | --- | --- |
| after construction | 20 ms | not started | active 2, not shut down | started | true |
| unfinished at 5 s | 5.0 s | TIMED_WAITING in executor wait | active 1, shutdown | failed | true |
| unfinished at 30 s | 30.0 s | TIMED_WAITING in executor wait | active 1, shutdown | failed | true |
| terminal latch | 60.0 s | TERMINATED | terminated, active 0 | failed | **true** |
| after cleanup close | 60.1 s | TERMINATED | terminated | stopped=true | **false** |

The 65 s and 125 s snapshots were not taken, because the close finished at 60 s, inside the 150 s cap.

## Control arm

Evidence file: `board-evidence-r2/control.evidence.log`. Terminal: `closeOutcome: RETURNED`, `interruptAtEnd: false`, tracker `false`, executor `terminated=true`, HttpClient `stopped=true`. Its selector thread and executor threads were absent at the terminal snapshot.

## Cleanup

After the original closer had terminated, a second `close()` on a noninterrupted thread returned normally in about 41 ms (`cleanup close returned normally`, `t=60089ms`). It released the tracker entry through the production path. No manual untracking was used. This effect is reported separately from the original close.

## XML and runs

- Control: `tests=1 failures=0 errors=0 skipped=0`, `testCloseProbeWithoutInterrupt` 0.115 s, `BUILD SUCCESSFUL in 2m 6s`, rc 0.
- Interrupted: `tests=1 failures=0 errors=0 skipped=0`, `testCloseProbeWithInterrupt` 60.152 s, `BUILD SUCCESSFUL in 2m 55s`, rc 0.
- Gradle XML passes because the probe asserts nothing. The verdict comes from the evidence files.

## Invocation accounting (must be read with this report)

The authorized budget was two additional filtered invocations: a corrected control, then the interrupted arm. The actual record:

1. **My error:** the rejected request's command was not stopped. It started its own chain (PID 16518, no timeout wrapper). I then launched a second chain (PID 16807) with `timeout 1200`, without checking for the first. Both chains share the log directory.
2. **Control ran twice.** Both runs were normal (rc 0). The first run's evidence and XML were overwritten by the second, so only the second is preserved. Both runs are reported as control invocations.
3. **Interrupted ran once.** Chain A's interrupted arm completed (rc 0). Chain B's queued interrupted arm was killed before it started, and it never ran.
4. Total filtered invocations after the failed v3 control: **three** (two control, one interrupted). The authorization covered two. The extra control is an unauthorized repeat caused by my error.

The interrupted result above therefore comes from the single interrupted invocation. The control result is the second control run. Both are clean. The extra invocation changed no probe input, and no evidence was edited.

## Limits

- One seed, one host, one JDK. Not a reproduction of the CI schedule of PR 5202 or PR 5198. Both arms set the interrupt artificially before close.
- Tracker-only survivor is classified as the assignment defines it: a retained entry after a returned exception, with no resource-specific live state. The HttpClient is FAILED and not stopped, which is a lifecycle fact, not a live thread.
- No fix, no production change, no Jira/GitHub/PR/upstream write.

## Proposed follow-up (for review to decide; not run)

The reviewer should decide whether the tracker-only result warrants a source look at the `ManagedSelector` stop path under interrupt. The probe does not authorize a fix. If a further observation is wanted, it needs a new claim and must count against the budget.
