# Linux follow-up: bounded interrupted Jetty client close probe, round 1

Status: OPEN
Tags: premise-run, records, review
Intended host: any available pool Linux host holding those tags and the JDK 21 toolchain; one local build lock, no concurrent gate on that host

## Authority and relationship to the sweep

Owner direction in the Windows chat, 2026-10-25: "I think you could claim it and
implement changes or produce an audit and then we can run tests on Linux side
when you post the assignment back to the board".

The Windows audit is delivered at
`reports/teardown-failure-sweep-round-1.md`, commit
`56fed234eead9cc4d423b1c014aa28c129017256`. This assignment takes only its
rank-1 deciding probe. The census and source map are complete; do not restart
them. The previous Windows refusal was superseded by the owner's audit direction
and `claims/teardown-failure-sweep-round-1-windows-audit.md`.

This is a new narrow premise unit, not a revival of
`assignments/distributed-expand-threadleak-settling-round-1.md`. Its stand-down
still applies. Do not run DistributedExpandComponentTest, CustomSort's whole
class, CloudHttp2's whole class, a branch_9x/main pair, or any seed loop here.

## Deciding question

When an otherwise identical Jetty client is closed on a thread whose interrupt
flag is already set, does close throw, leave its client identity tracked, leave
selector/executor resources live, or some combination of these? Compare with a
normal close. A tracker entry alone does not prove a live thread leak.

Two independent historical selector sightings motivate the unit: PR 5202
CustomSort at seed FFF5A3C3AAC69084 (interrupted ManagedSelector stop, retained
factory context/client and selector thread), and PR 5198 DistributedExpand at
85F9F77FD74C1D41 (selector thread only). The probe tests a plausible boundary;
it does not claim to recreate either CI schedule.

## Pinned inputs and scaffold

- Source tree: apache/solr main
  `d298cd0b42602247bee7bf9e2bcf302e725f6690`. Use a dedicated clean scratch
  worktree; never another host's or assignment's worktree. Do not substitute a
  newer tip. Record `git rev-parse HEAD` and the starting clean diff.
- Module: `:solr:solrj-jetty`.
- Existing test class:
  `org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest`, file
  `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java`.
- Add only two scratch test methods: `testCloseProbeWithoutInterrupt` and
  `testCloseProbeWithInterrupt`, plus private test helpers if needed. No
  production edit, dependency change, new production hook, or public branch.
  Retain the complete test diff and its SHA-256 in the scaffold deliverable
  before running; source SHA plus scaffold hash is the exact probe input.
- Seed for both: `FFF5A3C3AAC69084`.
- Linux JDK 21.0.12.1+1. If that JDK is unavailable, record the environment
  limitation instead of quietly changing it. Match the host's resource guards.

Use two fresh clients with the same builder settings and normal owned-client
lifecycle. Perform construction on the test thread. In each arm, call close on
a dedicated thread; immediately before close, set that thread's interrupt flag
only in the interrupted arm. No outstanding network request is required for
this first discriminator. Use observable readiness/latches, not a timing sleep.

For each arm capture before finally cleanup:

1. Close return or full exception/cause chain, and interrupt flag on the closer.
2. Whether the exact client identity is still in ObjectReleaseTracker. Do not
   compare only total map size, which can include unrelated fixture objects.
3. HttpClient lifecycle state and selector/executor live thread identities and
   stacks. Separate a running worker from a tracked object after partial close.
4. A timestamped snapshot immediately after close, and at most a bounded
   one-second observation interval if a worker is still exiting.

Each arm must release/stop its resources and join its closer in finally, using
an un-interrupted cleanup thread if needed. Capture the evidence before cleanup;
make assertions on captured state after cleanup. Never call
ObjectReleaseTracker.clear or suppress a leak check to obtain green XML. If
cleanup cannot finish inside the per-arm budget, capture stacks and terminate
the isolated test JVM under the runner's timeout; record an incomplete probe,
not a clean premise result, and do not launch another run automatically.

## Single run and cost cap

Claim before scaffold work. Use the host's documented guarded runner and build
lock. One invocation, only these method filters:

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithoutInterrupt \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

Cap: 30 seconds per arm including close/observation/cleanup; 20 minutes total
including compilation. Use a bounded runner timeout, preserve its exit status,
and do not increase a cap or widen filters to obtain a result. A pre-test
environment/scaffold failure is recorded separately from an executed probe.
No full tidy/check ladder is required for this scratch premise unit; it is not
a source contribution or a gate receipt proving a patch.

## Verdict and deliverables

Add these two files, not edits to this assignment or the claim:

- `reports/teardown-client-interrupt-probe-round-1-scaffold.md`: full test-only
  diff, SHA-256, pinned base, and any build/environment discrepancy. Preserve
  the exact artifact used, even on failure.
- `reports/teardown-client-interrupt-probe-round-1.md`: commands, host/JDK,
  source/scaffold pins, per-arm evidence, log paths, fresh JUnit XML counts,
  cleanup state and one of: live resource survivor; tracker-only survivor;
  both; clean boundary; incomplete environment/scaffold/run.

A clean interrupted arm ends the unit. A failed boundary ends with its captured
evidence and the smallest proposed next claim. Neither outcome authorizes a
fix, a repeat loop, the other four ranked probes, a Jira ticket/comment, GitHub
comment, PR body edit or upstream write. Do not label either outcome a
reproduction/non-reproduction of the original CI job without matching that job's
tree and schedule. Post the files and report the verified board commit.
