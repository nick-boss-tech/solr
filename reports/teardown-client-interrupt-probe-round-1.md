# Teardown client-interrupt probe, round 1 (vm2): verdict INCOMPLETE

Claim: `claims/teardown-client-interrupt-probe-round-1-vm2.md`. Assignment: `assignments/teardown-client-interrupt-probe-round-1.md`. Scaffold and diff hash: `reports/teardown-client-interrupt-probe-round-1-scaffold.md`.

## Verdict

**Incomplete.** The without-interrupt arm is a clean boundary. The with-interrupt arm did not finish its close within the 30-second arm budget, and its closer stack was not captured. The assignment says to record an incomplete probe in that case, not a clean premise result. Within the incomplete arm, the tracker entry was still present after close. A live-resource survivor is not established, because the thread probe was not reliable (see Limits).

## Commands and environment

- Command, run once through `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`, with `timeout 1200`:
  `./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...testCloseProbeWithoutInterrupt --tests ...testCloseProbeWithInterrupt -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1`
- JDK 21.0.12.1+1, Linux aarch64, Gradle wrapper. Runner rc 0; `BUILD SUCCESSFUL in 6m 29s`.
- Disk before and after: `4.6G free / 479582 ifree` before, `4.6G free / 479574 ifree` after (`logs/teardown-probe/probe-r1.headroom.log`).
- Logs: `/workspace/gates/gates-vm2/logs/teardown-probe/probe-r1.log`; XML: `logs/teardown-probe/probe-r1.xml/TEST-org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.xml`.

## JUnit counts (fresh XML)

2 tests, 0 failures, 0 errors, 0 skipped. `testCloseProbeWithoutInterrupt` 0.024 s. `testCloseProbeWithInterrupt` 60.009 s.

## Per-arm evidence (`PROBE arm=` lines from system-out)

| Field | without-interrupt | with-interrupt |
| --- | --- | --- |
| tracked after build | true | true |
| HttpClient-like threads before close | none | none |
| closer finished within 30 s | true | **false** |
| close return or exception | none | none (closer still running) |
| interrupt flag on closer after close | false | false (closer had not returned) |
| tracker entry after close | **false** | **true** |
| HttpClient-like threads after close | none | none |
| HttpClient-like threads after 1 s | none | none |
| cleanup close finished | true | true |
| closer alive after cleanup | false | **true** |

## Reading the evidence

- The without-interrupt arm is clean: close returned, the tracker entry was released by `close()`, and no HttpClient-like thread remained.
- In the with-interrupt arm, the closer thread did not return within 30 seconds. The tracker entry remained after close, which matches the source: `HttpJettySolrClient.close()` (`solr/solrj-jetty/src/java/.../HttpJettySolrClient.java:347-370`) only releases the tracker on line 370, after `httpClient.stop()`/`destroy()` in the `try`. Any exception or hang before line 370 leaves the entry.
- The cleanup close from a fresh, non-interrupted thread finished. The first closer thread was still alive after cleanup. So the blocking was not cleared by a second close.
- Live-thread evidence is not usable. The probe checks names `HttpClient@` and `elector`, and Jetty's thread names were not checked on this host. Both arms show `none`, so the result cannot tell a stopped client from a renamed one.

## Not captured (required by the assignment)

- The closer's stack at the moment it was blocked. The probe did not dump it. This is the missing evidence that would tell whether the hang is in `asyncTracker.waitForComplete()`, `httpClient.stop()`, or `ExecutorUtil.shutdownAndAwaitTermination`.
- Full exception chain from the closer, since it never returned.
- Thread states of every HttpClient and selector thread, using the Jetty naming.

## Cleanup state

Cleanup finished. The probe released the tracker entry and ran a non-interrupted close on a fresh thread. The first closer thread was still alive when the test method returned. No fix or repeat was started.

## Smallest proposed next claim (not authorized)

One probe only: repeat the with-interrupt arm once with the same seed and cap, and dump all thread stacks (names and frames) at 5 s and 25 s after the closer starts, before cleanup. Also dump the thread names of the HttpClient transport so the thread filter is verified. This decides whether the blocked closer is in the tracker wait, the Jetty stop, or the executor shutdown. It does not authorize a fix, a loop, or the other ranked probes.

## Limits

- One invocation, one seed, one host, one JDK. The result does not reproduce the CI schedule of PR 5202 or 5198.
- The arm budget was exceeded by the closer (30 s wait) and by cleanup (about 30 s). The run is recorded as incomplete for that reason.
- The thread-name filter is unverified (see above).
- No Jira, GitHub, PR or upstream write was made.
