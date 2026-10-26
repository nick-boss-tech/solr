# Client-interrupt probe round 2: repair scaffold and observe terminal close

Status: OPEN
Tags: premise-run, records, review
Intended host: any available pool Linux host with those tags and JDK 21.0.12.1+1; vm2 has the original run artifacts

## Direction and inputs

Owner requested review of vm2's two incomplete results and a handback if needed.
The review is `reports/teardown-client-interrupt-probe-review-windows-round-1.md`.
Review commit: 1aa3c8d6cb6897a4e1f32251b72a08face242be9.
This unit supersedes the execution recipe in
`assignments/teardown-client-interrupt-probe-round-1.md`; do not re-claim that
recipe or re-run its v2 scaffold unchanged. Preserve all prior records.

Source: apache/solr main d298cd0b42602247bee7bf9e2bcf302e725f6690.
Seed: FFF5A3C3AAC69084. JDK: 21.0.12.1+1, Linux.
Class: org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.
Module: :solr:solrj-jetty. Use a dedicated clean scratch worktree and host build
lock; no overlapping gate. No production edit, new production hook, dependency
change, public branch, Jira/GitHub text, speculative fix, seed loop or broad suite.

## Recover first; no reconstruction from memory

The board contains both full original diffs in the scaffold reports. Extract the
single fenced diff block as UTF-8/LF with final newline and verify its SHA-256:

- reports/teardown-client-interrupt-probe-round-1-scaffold.md:
  2dc73a4038f03013761e5bef7ab58cdf500edeb143c58d088cc45f557d5b12c7.
- reports/teardown-client-interrupt-probe-round-1-rerun-scaffold.md:
  f6d7f6f252051b6c628bb97614fb3a3cefde4e5fcb0d023ada97a23dc2c22709.

Windows recovered both byte-for-byte. V2 is cumulative against the pinned source,
not a delta to apply after v1. It applies to the original test file. The deleted
vm2 scratch tree does not prevent recovery. If on vm2, preserve its surviving
original run logs and XML before changing anything; record their paths/hashes.
Do not block another capable host on access to vm2-local logs.

## Repair the test-only scaffold before execution

1. Remove every manual ObjectReleaseTracker.release/clear for the probe client.
   Only production close may remove its entry in this experiment. A leaked entry
   causing classMethod failure is evidence; do not force green XML.
2. Each arm gets one fresh client and one original closer. Keep default owned
   executor semantics. No supplied external executor that would change
   shutdownExecutor=false. No requests are needed for this boundary probe.
3. Save the exact client's identity, its HttpClient reference and the actual
   executor reference (getHttpClient().getExecutor()) before close. Record their
   runtime classes/identity hashes, lifecycle and executor state. If it is a
   ThreadPoolExecutor, record active count, queue size, pool size, shutdown and
   termination state. Do not infer ownership from a pool number alone.
4. Capture all thread identities/names/states and complete stacks, with no name
   filter and no arbitrary frame truncation. Take a baseline before client
   construction, after construction, while close is unfinished at 5/30/65/125s,
   and immediately at return or cap. Use monotonic timestamps. Record exact
   resource state at each snapshot; distinguish attributed workers from unknowns.
5. Capture close outcome and Throwable with full cause/suppressed chains in the
   closer, including completion latch and interrupt state. Print these again
   after that latch fires. Until completion, outcome/flag is UNAVAILABLE, not
   the default value of an unfilled AtomicReference/AtomicBoolean.
6. Never start cleanup close while the original closer is active. After original
   close finishes, take the terminal tracker/resource snapshot first. Then allow
   at most 30 seconds of serial cleanup on a noninterrupted thread. Log every
   cleanup failure; do not swallow it. Use finally for the controller's cleanup,
   and verify thread termination explicitly. No manual untracking in cleanup.
7. If original close remains active at the observation cap, take all diagnostics
   and terminate that isolated test JVM and its process tree under the runner's
   watchdog. Record INCOMPLETE and preserved evidence; no concurrent rescue close.
   Arrange this containment before running, rather than leaving a closer alive
   into another test. No thread-leak suppressions, daemon conversion or longer
   framework linger to hide the result.
8. Keep production timeouts and code unchanged. Reaching close's finally does not
   show stop/destroy succeeded: finally also runs after catch/rethrow. Likewise,
   no requests does not imply no Jetty lifecycle tasks. The review cites source.

Before execution, post a full cumulative v3 test diff with its SHA-256, base SHA,
precise watchdog/cleanup arrangement and final commands to
`reports/teardown-client-interrupt-probe-round-2-scaffold.md`. A summary of changes
without the complete diff is not the scaffold deliverable.

## Run shape and explicit cap change

This assignment changes the observation cap from 30 to **150 seconds per arm**.
Reason: ExecutorUtil's normal path permits 60 seconds, shutdownNow, and a second
60 seconds. A 75-second cap cannot cover both waits. Allow at most 30 additional
seconds of cleanup only after original close terminates. Total budget remains
**20 minutes including compilation, both invocations, snapshots and cleanup**.

Use the guarded host runner, same security-manager/encoding/seed flags. Run these
two commands serially in separate fresh test JVMs; archive each XML before the
next cleanTest. The first must be a completed clean normal-close control. If it
is incomplete or abnormal, stop and report it; do not run the interrupted arm.

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithoutInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

At most two filtered invocations total, no automatic repeat or cap increase.
Record environment/scaffold failures separately from executed premise outcomes.
The observation methods may report an expected failure signature; the verdict
comes from terminal captured evidence, not BUILD SUCCESSFUL or aggregate counts.
No full tidy/check gate is requested for this scratch premise unit.

## Result

Add `reports/teardown-client-interrupt-probe-round-2.md`, with source/scaffold
pins, host/JDK, commands and exit codes, per-arm fresh XML counts, log paths,
timestamped terminal state, stacks, exception chains and cleanup outcome. Include
the relevant raw evidence in the report or a new board evidence file, rather
than only a vm-local path. Preserve the scaffold and logs even when incomplete.

Choose the narrowest supported result: delayed clean close; completed close with
retained tracker entry; completed close with live owned resource; both survivors;
or INCOMPLETE. A retained entry sampled while close is still running is not a
post-close survivor. Report any serial cleanup's effect separately from the
original close result. Do not assert original-CI causation from this probe.

This unit ends with the evidence and a smallest proposed follow-up. It authorizes
no production fix, third run, other ranked probe, BATS work, public post or
DistributedExpand settling pair. The earlier DistributedExpand stand-down holds.
