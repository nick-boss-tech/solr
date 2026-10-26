# Client close with a caller-owned executor: ownership probe result, round 1 (vm2)

Claim: `claims/teardown-client-close-external-executor-round-1-vm2.md`. Assignment: `assignments/teardown-client-close-external-executor-round-1.md`. Scaffold (final, before execution): `reports/teardown-client-close-external-executor-round-1-scaffold-final.md`, diff SHA-256 `a86109646fe507ef945aafdac792e20c990c0eac416f8c642334bdd51e589beb`. Review: `reports/teardown-client-close-source-review-round-1.md` (commit f612bda33da). Source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`.

## Verdict

- **Control: clean.** Close returned. The tracker released. Both saved raw NIO selectors closed. The caller pool stayed usable (sentinel `alive`).
- **Interrupted: completed close with a saved owned selector still open and an attributed selector worker surviving**, plus a retained tracker entry. The close threw. The HttpClient and ClientConnector are FAILED, not stopped.
- The surviving worker is a thread of this probe's own named pool, `probe-ext-interrupted-21-thread-2`. It is RUNNABLE, inside `ManagedSelector.SelectorProducer.select → nioSelect → EPoll.wait`, at both the terminal snapshot (t=70 ms) and terminal+1 s (t=1085 ms). The pool showed `active=1` at both.
- **Caveat on the wording.** The probe does not show that the selector is leaked for good. Its own cleanup closed the raw selector and the worker exited. The probe also does not show what a production caller does next. The review predicted a caller shutdown would end this worker, and this run did not test that.

## Commands and attempts

- Driver `gates/gates-vm2/run-ext-pair.sh` (PID 27422), under the nonblocking pair lock `/workspace/tools/.teardown-ext-pair.lock`, with `timeout 1200`. Each attempt is logged in `board-evidence-ext-r1/attempt-ledger.txt` before it runs.
- Attempt 0 was refused by the driver's own stale-process guard, before any test ran (`ext-pair-r1-refused-attempt-0/refused.txt`, not a probe invocation). The guard was patched twice and narrowed to real probe scripts and test-worker JVMs. The patched driver ran the pair.
- Invocation 1, control: `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...testExternalExecutorCloseProbeControl -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1` via `pool/bin/sweep-run.sh`, `timeout 600`. Exit 0, `BUILD SUCCESSFUL in ~2m`. XML `tests=1 failures=0 errors=0 skipped=0`, 1.187 s.
- Invocation 2, interrupted: same flags, `testExternalExecutorCloseProbeInterrupted`, started only after the control was clean. Exit 0. XML `tests=1 failures=0 errors=0 skipped=0`, 1.206 s.
- Exactly two filtered invocations. The round 2 overrun is not repeated, and no third run was made.
- JDK 21.0.12.1+1, Linux aarch64. Host lock `/workspace/tools/.gradle-vm2.lock`.
- Runtime pin: the HttpClient class loaded from `jetty-client-12.1.14.jar`, implementation version 12.1.14. This matches the declared version. The earlier 12.1.12 jar I saw in the Gradle cache was not the one used.

## Interrupted arm: timeline (evidence `board-evidence-ext-r1/interrupted.evidence.log`)

| Snapshot | t | Closer | Close outcome | Tracker | HttpClient / ClientConnector | Saved selector 0 | Saved selector 1 | Caller pool |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| pre-close | 44 ms | not started | unavailable | true | started | open | open | active 2 |
| terminal-immediate | 70 ms | TERMINATED | THREW | **true** | FAILED / FAILED | closed | **open** (manager stopped=true) | **active 1, not terminated** |
| terminal-plus-1s | 1085 ms | TERMINATED | THREW | true | FAILED / FAILED | closed | open | active 1 |
| pre-cleanup | 1117 ms | TERMINATED | THREW | true | FAILED / FAILED | closed | open | active 1 |
| after-cleanup | 1170 ms | TERMINATED | THREW | false | stopped | closed | closed | terminated |

- `interruptAtEnd: false`. Interrupt was set before close (`interruptBeforeClose: true`).
- Throwable chain, verbatim head: `java.lang.RuntimeException: Exception on closing client` at `HttpJettySolrClient.close(HttpJettySolrClient.java:364)`. Cause: `java.lang.InterruptedException` at `CountDownLatch.await`, from `org.eclipse.jetty.io.ManagedSelector.doStop(ManagedSelector.java:139)`, through `ContainerLifeCycle` and `SelectorManager.doStop`. This is the same first-stop path the earlier owned-executor probe showed, now with a caller pool.
- The `finally` shutdown did not wait the 60 s the owned case waited. `shutdownExecutor` is false here, so `ExecutorUtil.shutdownAndAwaitTermination` was not called. The caller pool remained up until cleanup.

## Control arm (evidence `board-evidence-ext-r1/control.evidence.log`)

Close returned at the terminal snapshot. The tracker was released, the HttpClient and ClientConnector stopped, and both saved raw selectors read `rawSelectorIsOpen=false`. The sentinel returned `alive` at t=1098 ms. The caller pool was not shut down until cleanup.

## Cleanup (after both terminal captures; labelled separately)

1. The caller-executor sentinel ran first: `alive`.
2. Explicit close of the saved raw selector that was still open: logged.
3. Serial noninterrupted `client.close()`: returned normally at t=1147 ms. The tracker entry was released by that production close.
4. `pool.shutdownNow()` and `awaitTermination(5 s)`: `true`. The surviving worker exited.

The cleanup effects are recorded separately from the original close outcome.

## Answer to the question

After an interrupted close of an owned HttpClient with a caller-owned executor, one saved owned NIO selector stayed open after the original close finished with an exception. Its worker stayed in the caller's pool, inside Jetty's selector loop, at one second after close. The tracker entry was retained as well. This is a completed close with retained tracking and a surviving attributed selector, which is the review's predicted boundary.

It is not established that this leak is permanent. Our cleanup closed the selector, and the pool's shutdown would also end the worker. It is also not a claim about the original CI failures.

## Narrowest next implementation question (for review, not authorized here)

When Jetty's `ManagedSelector.doStop` throws `InterruptedException` at line 139, before it submits `StopSelector`, should Solr's close explicitly stop or close the saved selectors itself when the executor is caller-owned? This is a question about where that responsibility belongs, not a patch. No fix is proposed or made.

## Limits

- One seed, one host, one JDK. The interrupt is set artificially before close. The probe makes no requests.
- The saved-selector worker attribution is by thread name and stack. It is strong for this pool, because the name is unique to the probe. It is not an identity hash on the thread.
- Board evidence: `board-evidence-ext-r1/` (SHA256SUMS included).
- No production change, no Jira/GitHub/PR/upstream write.
