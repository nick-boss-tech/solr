# External-executor close result: source review, round 2

Status: DONE; boundary premise confirmed, production fix pending
Host: windows
Claim: claims/teardown-client-close-source-review-round-2.md
Board snapshot: 9c0cbfda481a5d0cdd34c5f297c8a80ddb1efe68
Result reviewed: reports/teardown-client-close-external-executor-round-1.md,
posted at caadee98de8
Decision ladder: rung 1, final scaffold, preserved raw evidence and exact source

## Decision and answer to the source question

Accept the narrow premise: after original close **threw and terminated**, a saved
owned selector remained open, its attributed selector worker remained active, and
the caller executor remained usable. This strengthens the earlier owned-executor
result to an observed resource survivor at the client-close boundary. It does not
establish a permanent leak, an original-CI reproduction, or a persistent
HttpShardHandlerFactory leak.

**Do not implement direct raw-selector cleanup in Solr as the first fix.** The
same owned HttpClient needs orderly shutdown whether its executor is supplied or
created internally. Executor ownership determines who shuts down the pool; it
does not transfer ownership of the client's selectors to that pool's caller.
The scaffold's direct selector close is test containment, not a validated
production shutdown recipe.

The next source candidate is narrow: preserve and temporarily clear a
pre-existing interrupt **after the existing async drain**, perform the usual
owned-resource cleanup, and restore that interrupt in an outer finally that also
covers executor-cleanup failure. Keep borrowed-client and caller-executor
ownership intact. This prevents the demonstrated entry condition from immediately
aborting Jetty's first stop wait. It is a proposed fix boundary, not a tested
implementation, and does not solve interruption arriving during stop.
Keep tracker release tied to successful cleanup; moving it into an unconditional
finally would conceal the failed boundary rather than repair it.

No further unchanged premise run is needed. The next Linux execution should prove
a concrete source candidate, with failing-before and passing-after assertions.
This review posts no new test invocation assignment or expanded run budget.

## Independent evidence checks

Solr M: d298cd0b42602247bee7bf9e2bcf302e725f6690.
Jetty source J: 15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0, tag jetty-12.1.14.
Both raw arms record jetty-client-12.1.14.jar and implementationVersion 12.1.14.
The final cumulative fenced diff independently hashes to
`a86109646fe507ef945aafdac792e20c990c0eac416f8c642334bdd51e589beb`.
The earlier scaffold is superseded and was not used.

The actual board directory is **board-evidence/ext-r1/**. The result's
`board-evidence-ext-r1/` spelling is wrong. Git-object byte hashes match the
published SHA256SUMS.txt:

| Artifact | Bytes | SHA-256 |
| --- | ---: | --- |
| control.evidence.log | 332663 | 00c74051eb13390253e6b780d6e703f8fb6fcd093833d36c4968d0e5d6a39b7c |
| interrupted.evidence.log | 347026 | f1b9cb939bd3887e0094b269e30dd51b38694a52f48a4bcf7376a0591e9c2113 |
| attempt-ledger.txt | 437 | 914443c589e86f9390e70083485be040548c92735ced0fc05543486c790ec63a |

The final diff saves two managed/raw selectors before close, checks executor
identity, uses one original closer and a completion latch, and captures terminal
state before any containment. No manual tracker removal occurs. The interrupted
arm alone sets its flag. Both sentinels return alive before cleanup.

| Observation before cleanup | Control | Interrupted |
| --- | --- | --- |
| Terminal snapshot, relative to evidence origin | t=67 ms, RETURNED | t=70 ms, THREW |
| Tracker entry | released | retained |
| HttpClient / connector | stopped / stopped | failed / failed |
| Raw selectors at terminal and +1 second | both closed | selector 1 open, selector 0 closed |
| Caller pool | active 0, usable | active 1, usable |
| Original closer flag at completion | false | false after entering close interrupted |

At t=1085 ms the interrupted raw selector identity 641126829 is still open.
Thread `probe-ext-interrupted-21-thread-2`, id 42, identity 834306031, remains
RUNNABLE in EPoll.wait / ManagedSelector.SelectorProducer. These identities also
appear in its earlier snapshots. The result's statement that no thread identity
hash was captured is overly cautious: the raw log contains one, although it does
not directly map a Java stack frame to a particular raw selector object.

One further correction: the result table's parenthetical `manager stopped=true`
is not the raw manager state. The raw SelectorManager has stopped=false; the
saved **ManagedSelector[1]** reports stopped=true despite its raw selector being
open. That mismatch makes physical selector measurement important and prevents
using lifecycle state alone as a cleanup oracle.

Explicit raw-selector close starts only at t=1134 ms, after the snapshots and
sentinel. Serial client close then returns, tracker releases, and caller
shutdownNow/awaitTermination succeeds. No intermediate snapshot isolates which
cleanup step ended the worker; do not claim that timing was directly measured.

## Why direct selector cleanup is not the first production change

[ManagedSelector.doStop:130-148](https://github.com/jetty/jetty.project/blob/15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0/jetty-core/jetty-io/src/main/java/org/eclipse/jetty/io/ManagedSelector.java#L130)
changes its started guard, submits connection cleanup, and waits at line 139
before submitting StopSelector. The raw cause chain identifies that wait.
Interruption skips the stop submission. Retrying a lifecycle stop is not a
general recovery because that guard has already changed. Directly closing the
raw selector also bypasses this lifecycle sequence and has not been tested with
connections or pending requests by the no-request probe.

The [JDK Selector.close contract](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/channels/Selector.html#close())
releases selector resources, invalidates keys and deregisters channels. It does
not specify complete Jetty endpoint, connection, scheduler or client shutdown.
Treating the successful containment action as sufficient HttpClient cleanup
would therefore be an unsupported inference.

At M, [HttpJettySolrClient.close:347-370](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L347)
drains first, then stops/destroys its owned HttpClient and scanner. It shuts down
only an internally created executor. The proposed flag handling belongs between
the existing drain and resource cleanup. The interruptible drain at lines
949-960 must retain its behavior; clearing the flag before that drain could
reintroduce a shutdown wait for pending requests.

[HttpShardHandlerFactory.close:345-357](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java#L345)
subsequently shuts down the supplied pool in finally. The probe intentionally
captures before such caller action. Do not generalize its one-second survivor
into an assertion that factory teardown permanently leaks.

## Proof required for a concrete fix

Use small assertion-bearing tests, not the diagnostic scaffold as a permanent
test. A source candidate needs these contracts covered on Linux:

- Pre-interrupted original close of an owned HttpClient returns cleanly, closes
  both saved selectors, releases tracking and preserves the closer's interrupt.
  Cover both internally created and supplied executors. With a supplied pool,
  prove a sentinel still runs and the pool is not shut down by client close.
- Closing a borrowed HttpClient wrapper preserves the parent client and pool;
  use the parent afterward. Preserve normal noninterrupted close behavior.
- Preserve the existing interruptible async-drain/abort behavior. Run the
  applicable focused existing tests when the final diff is known; do not claim
  that masking all of close is equivalent.

For failing-before proof, the supplied-executor regression can assert the
demonstrated failure on M, then pass with the candidate. Capture original state
before any serial cleanup. Use bounded cleanup/watchdogs so the expected base
failure does not leave its selector worker in the test JVM. Pin the pushed
candidate head and its exact proof tasks before requesting a gate. A passing
pre-interrupt regression must not be described as proof against a fresh
interrupt during stop. Any solution to that broader window needs separate
lifecycle reasoning and deterministic coverage; no new timing/seed sweep now.

## Execution accounting and records follow-up

The preserved ledger records control at 02:35:24Z, interrupted at 02:37:35Z,
and driver end at 02:39:34Z. Status files record rc=0 for each. This supports the
reported pair; unlike round 2, there is no preserved evidence of a duplicate
invocation. The pre-run guard refusal and its two repairs are disclosed in the
result, but the posted ledger begins at the successful driver. It does not itself
contain the refusal.

The board snapshot has raw evidence/status/ledger and the test diff, but no raw
refused.txt, final driver/control-checker script, Gradle logs or archived XML.
The scaffold says its driver is copied into the record; it is not present there.
Have the gate host archive those **existing** artifacts as an add-only evidence
record if retained. This is a records-only follow-up, not grounds to rerun the
pair. XML counts and the claim of no earlier executed test are reported rather
than independently verified from those missing artifacts. These limits do not
invalidate the captured post-close selector observation.

Windows work: evidence/scaffold hashes, source and lifecycle review only. No
production patch, Gradle/test run, public issue/PR/Jira write or new run budget.
Earlier overrun records and the DistributedExpand stand-down remain unchanged.
