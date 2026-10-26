# Review: incomplete client-interrupt probes and next action

Status: COMPLETE (review; Linux premise unresolved)
Claim: claims/teardown-client-interrupt-probe-review-windows-round-1.md
Host: windows
Reviewed board snapshot: 2753ca1f133d3a07337f264303541cbba105604e
Prepublication snapshot: 5b9b71fbb82058bf539b7b8894d543643f57ce8f; no overlapping probe claim, assignment, or report appeared.
Pinned source: d298cd0b42602247bee7bf9e2bcf302e725f6690

## Decision

Hand this back as one **corrected Linux premise unit**, not a source-fix task.
The two runs establish a repeatable delay within this artificial interrupted-close
probe on vm2. They do not yet establish a live resource leak or a tracker-only
survivor after close completed. Keep both existing verdicts INCOMPLETE.

Do not repeat v2 unchanged with only a longer cap. Repair the observation and
cleanup code first, observe the original close to its terminal outcome, then
classify the captured state. New assignment:
`assignments/teardown-client-interrupt-probe-round-2.md`.

There is no patch to implement from this evidence yet. No teardown source branch
is required to recover the work. The packaging branch solr-5225-bats-followup at
096da6567e3 is unrelated and is not an input.

## What the runs establish

Inputs are the two vm2 claims and reports under the original assignment:

- `reports/teardown-client-interrupt-probe-round-1.md`: the normal arm completed;
  interrupted close had not finished by 30 seconds. Its closer stack was missing.
- `reports/teardown-client-interrupt-probe-round-1-rerun.md`: the interrupted
  closer was TIMED_WAITING at both sampled times in ThreadPoolExecutor.awaitTermination,
  via ExecutorUtil.java:142 and HttpJettySolrClient.java:367. This localizes the
  observed delay to the executor wait. The normal arm again completed.
- Both reports give two tests, zero JUnit failures/errors, but the methods print
  observations without enforcing a complete premise verdict. Those counts are
  reported by vm2; this Windows review did not independently read its raw XML.
  BUILD SUCCESSFUL does not make the incomplete observation complete.
- In both runs a cleanup thread called close again while the original closer was
  still alive. Outcome after that intervention cannot be used to describe the
  original close in isolation.

These are two executions of a synthetic boundary on one host, seed and pinned
tree. They do not reproduce either original PR's CI schedule.

## The diffs are already recoverable from the board

The statement that the test diffs are only local to vm2 is incomplete. Each
scaffold report contains a complete fenced diff. This review extracted the
fences as UTF-8 with LF newlines and a final newline, and computed:

| Board scaffold | Recovered bytes / lines | SHA-256 |
| --- | --- | --- |
| reports/teardown-client-interrupt-probe-round-1-scaffold.md | 5353 / 134 | 2dc73a4038f03013761e5bef7ab58cdf500edeb143c58d088cc45f557d5b12c7 |
| reports/teardown-client-interrupt-probe-round-1-rerun-scaffold.md | 7015 / 175 | f6d7f6f252051b6c628bb97614fb3a3cefde4e5fcb0d023ada97a23dc2c22709 |

Both hashes match the recorded originals exactly. `git apply --numstat` parses
each recovered patch as one test file, with 107 and 148 additions respectively.
The v2 patch also passes `git apply --check` against the pinned original test file
in the read-only evidence copy. No patch was applied, compiled, or run here.

V2 is a cumulative patch against the original test file (index 365f9a2b098 to
88358c506c2), not an incremental patch to apply after v1. Reconstruct from its
fence and apply it directly to clean d298cd0b if needed; do not stack both patches.
The removed scratch worktree is therefore not a blocker. The original log/XML
paths remain vm2-local and are distinct from this verified scaffold recovery.

## Findings in the scaffold and interpretation

1. **Manual release invalidates cleanup/tracker conclusions.** The v2 scaffold
   report lines 129-130 call ObjectReleaseTracker.release(client) merely because
   the client was still tracked at the 30-second sample. V1 does the same at
   lines 112-113. This bypasses production close's release point. Remove it;
   do not substitute clear() or another form of manual untracking.

2. **The second close changes the experiment.** V2 lines 132-141 launch a fresh
   cleanup close while the first is unresolved, then wait another 30 seconds.
   Its effect on executor shutdown, Jetty state and tracker release confounds
   the first close's eventual outcome. Observe the first close to completion
   before any serial cleanup. If it hits the hard observation cap, dump state
   and end the isolated test JVM; do not race another close to obtain green XML.

3. **Finally does not imply successful Jetty stop.** The rerun report line 53
   concludes that httpClient.stop and destroy returned without an exception
   because the stack reached the finally. That conclusion is unsupported.
   [HttpJettySolrClient.java:350-370](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L350)
   runs that finally after both successful stop and the catch/rethrow path.
   The original close exception can remain pending while finally waits. Capture
   the actual Throwable after the closer terminates before deciding which happened.

4. **The flag field is not sampled until the closer ends.** V2 lines 77-92
   initialize AtomicBoolean to false and update it only in the closer's finally.
   While closerDone is false, printed false is the field's default, not a completed
   close's interrupt state. The parked wait is consistent with an earlier cleared
   interrupt, but neither the atomic field nor the stack identifies the clearing
   call. Record unfinished samples as unavailable.

5. **Name filtering still omits relevant workers.** V2 lines 166-175 omit h2sc
   and workers despite the name listing in the rerun report. Capture all threads
   and full stacks instead of guessing another substring list. Capture the exact
   executor identity via client.getHttpClient().getExecutor(), with its state,
   active count, queue and lifecycle where its runtime type permits. A name or
   fresh thread number alone does not establish ownership. The accessor and the
   private owned-executor setup are at
   [HttpJettySolrClient.java:215-225](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L215).

6. **No request does not imply no executor task.** The report's line 68 makes
   that assumption. The constructor configures an HTTP/2 transport, gives Jetty
   the client executor and starts HttpClient before any request:
   [lines 286-314](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L286).
   Lifecycle/selector work must be distinguished from request work. The unknown
   worker stack remains needed; absence of requests is not a leak verdict.

7. **The observation budget must cover both executor waits.**
   [ExecutorUtil.java:129-149](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java#L129)
   permits 60 seconds, then shutdownNow, then another 60 seconds. My original
   30-second assignment could decide a fast boundary or return incomplete, but
   could not observe that whole timeout path. The proposed 75 seconds likewise
   does not cover the second wait. Round 2 explicitly changes the observation
   cap to 150 seconds and separately budgets cleanup, without changing production
   timeout values. It remains a capped discriminator, not a reproduction loop.

## Bounded next unit

Run the corrected normal control first, then the corrected interrupted arm in
separate fresh test JVMs, same source/seed/JDK/flags. At most two filtered
invocations, under one host lock, 20 minutes total including compile. Allow up to
150 seconds of observation per arm and at most 30 seconds of serial cleanup
after original close terminates. Capture terminal return/exception, exact client
tracking, owned executor and Jetty state before cleanup. Full thread snapshots
at 5, 30, 65 and 125 seconds while unfinished, and at terminal outcome/cap.

If the original close is still live at 150 seconds, terminate the isolated test
process tree after diagnostics and record INCOMPLETE. No second close runs against
that still-active closer. Stop after an incomplete/abnormal control rather than
starting the interrupted arm. No automatic third invocation.

A delayed clean close ends this premise with its measured latency. A returned
exception with retained tracking is a tracker survivor; call it a live resource
survivor only if resource-specific state supports that claim. A completed close
with retained resources is a separate result. Classify only the boundary tested;
do not attribute an original CI failure from this artificial scheduling alone.

## Disposition

Decision ladder rung 1 settles the scaffold/interpretation defects; rung 2 remains
necessary to obtain terminal resource evidence. No source fix or new Jira ticket
is justified yet. The next required execution is Linux work. This report and the
new assignment are the handoff; earlier reports/claims are preserved unchanged.
The DistributedExpand stand-down and the other four ranked candidates remain
outside this unit. No Windows Gradle/test run, source change, public message or
upstream write was performed.
