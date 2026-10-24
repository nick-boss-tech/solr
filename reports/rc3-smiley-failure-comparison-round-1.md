# RC3 smoke failures compared with David Smiley's two data points (round 1)

Sources: `reports/rc3-smoke-loop-round-1.md` and the vm5 run logs and test XML under `/workspace/smoke-rc3/tmp/run-00N` (completed runs 001, 002, 003, 005 and 006). Run 004 has no test output left, and run 007 was stopped early.

## Item (a): a metrics-related failure, and a second failure with the same root cause

Vm5 run 003 has one metrics-related failure: `TestTermsQParserPlugin.classMethod`. Its message is "ObjectTracker found 16 object(s) that were not released" with 16 `SolrMetricsContext` objects, created through `CoreAdminHandler.initializeMetrics` during `CoreContainer.loadInternal`. No other vm5 run has a metrics leak.

Run 003 also has `TestTaskManagement.classMethod`, "Error shutting down MiniSolrCloudCluster". Its suppressed cause is a timeout waiting for the `closeThreadPool` to shut down. Nothing in its output names metrics, so the two are not shown to share a root cause.

Verdict: partial. A metrics-related failure is present (run 003 only). The "same root cause" pairing cannot be tested from these logs. Smiley's first failure name is not in the vote post, so a name-level match is not possible.

## Item (a), JDK version

Smiley reports that the original JDK 21 release failed and JDK 21.0.12 passed. Vm5 ran `openjdk version "21.0.12.1" 2026-08-18 LTS` (every run log records `Java 21 JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`). Five completed runs failed on that JDK, with the same four methods in each.

Verdict: no match. Vm5's failures persist on the JDK that Smiley reports as passing, so the JDK version does not explain them.

## Item (b): the BATS security-manager test on JDK 25

Vm5 ran no BATS test and no JDK 25 run. The loop only ran Java 21 Gradle tests.

Verdict: cannot tell. The absence is expected, and this host gives no evidence on that item.

## Limits

- Smiley's original failure name is not given in the vote post, so the metrics comparison is by category only.
- Only vm5 data is used. No check on another host or on 10.0.0.
- No re-run, no post, no vote.
