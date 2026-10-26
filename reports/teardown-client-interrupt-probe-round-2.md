# Teardown client-interrupt probe, round 2 (vm2): STOPPED at the control arm (scaffold failure, no probe result)

Claim: `claims/teardown-client-interrupt-probe-round-2-vm2.md`. Assignment: `assignments/teardown-client-interrupt-probe-round-2.md`. Scaffold v3: `reports/teardown-client-interrupt-probe-round-2-scaffold.md` (diff SHA-256 `368ad9c67846d4f3383f7812208c95a0f5a49948724ab4e5f8b405362f8d5c90`, base `d298cd0b42602247bee7bf9e2bcf302e725f6690`).

## Outcome

**No probe measurement was taken.** The first invocation (the normal-close control) failed in the scaffold's evidence setup, before any client was built and before any close ran. The assignment says: "If it is incomplete or abnormal, stop and report it; do not run the interrupted arm." The interrupted arm was not run.

Verdict for the record: **INCOMPLETE, scaffold failure (environment/test-harness), not a premise result.** Nothing here says anything about `close()` behaviour, interrupted or not.

## The failure

```
HttpJettySolrClientTest > testCloseProbeWithoutInterrupt FAILED
    java.security.AccessControlException: access denied ("java.io.FilePermission" "build/probe-evidence" "write")
        at java.base/java.nio.file.Files.createDirectories(Files.java:753)
        at org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest$ProbeState.<init>(HttpJettySolrClientTest.java:1056)
        at org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.runCloseProbe(HttpJettySolrClientTest.java:964)
        at org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithoutInterrupt(HttpJettySolrClientTest.java:955)
```

Cause: the v3 scaffold writes evidence to `build/probe-evidence` (relative to the module). The test security policy (`gradle/testing/randomization/policies/solr-tests.policy`, lines 31-32) grants writes only under `java.io.tmpdir`. The scaffold review missed this, because the earlier probe runs did not write to the file system. This is a defect in the v3 scaffold.

## Commands and exit codes

| Step | Command (abridged) | Result |
| --- | --- | --- |
| Compile check (not a probe invocation) | `:solr:solrj-jetty:compileTestJava`, guarded runner | rc 0 after one correction (checked exception in a lambda; the failed attempt is not in the diff) |
| Invocation 1, control | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...testCloseProbeWithoutInterrupt -Ptests.seed=FFF5A3C3AAC69084 ...` via `pool/bin/sweep-run.sh`, `timeout 600` | rc 1 (`BUILD FAILED in 2m 13s`), failure above |
| Invocation 2, interrupted | not run (stop rule) | — |

Logs: `logs/teardown-probe-r3/probe-r3-control.log` and `control-runner.out`. Fresh JUnit XML from this failed run is under the worktree's `build/test-results/test/`. Its counts are not a premise result; it is a failure of the scaffold.

Disk before and after the control: see `logs/teardown-probe-r3/control-status.txt` and the sweep headroom log.

## Invocation budget

The assignment allows at most two filtered invocations. One was used by the failed control. A corrected control run plus the interrupted run would make three, so the scaffold fix needs the assignment author's decision on whether the failed invocation counts. Options:

1. Treat the failed control as a scaffold failure that never executed the probe, and allow one corrected control run plus one interrupted run.
2. Keep the count at two, and stop here with this INCOMPLETE record.

## Proposed scaffold correction (not run, not published as v3.1)

Write evidence to `Path.of(System.getProperty("java.io.tmpdir"), "probe-evidence", arm + ".log")`, which the policy permits. Its compile has not been checked. The correction changes no probe logic, no close call, no snapshot, and no cap. It would be published as a new scaffold, v3.1, with its own hash, before any run.

## Limits

- No close was performed in this round, so there are no close outcomes, tracker states, executor states or stacks to report.
- The worktree `/workspace/worktrees/teardown-probe-r3` is kept for the corrected run.
- No Jira, GitHub, PR or upstream write was made. No production change was made.
