# Client close with caller-owned executor: bounded ownership probe, round 1

Status: OPEN
Tags: premise-run, records, review
Intended host: any available pool Linux host holding those tags, JDK 21.0.12.1+1; vm2 has earlier scaffold context

## Purpose and pins

Owner authorized the Windows source review and handover of further testing.
Read `reports/teardown-client-close-source-review-round-1.md` first. This is a
new ownership input, not permission to repeat the completed owned-executor pair.
Round 2's invocation overrun and overwritten first control remain on record.
Source review commit: f612bda33dabca62897e1fba9a196770f2a446ee.

Source: apache/solr **d298cd0b42602247bee7bf9e2bcf302e725f6690**.
Class: org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.
Module: :solr:solrj-jetty. Seed: FFF5A3C3AAC69084.
Linux/JDK 21.0.12.1+1, security manager enabled, ISO-8859-1.

Use a dedicated clean scratch worktree at that source and the host build lock.
Only test scaffold edits; no production changes, dependency changes, public
branch, Jira/GitHub text, broad suite or seed loop. This is not a green-code gate.
The separate DistributedExpand stand-down and BATS scope remain unchanged.

Question: after interrupted close of an owned HttpClient, with its caller-owned
executor still usable, does a saved owned NIO selector remain open or its worker
survive? Solr sets shutdownExecutor=false for a supplied executor. Production
HttpShardHandlerFactory uses this ownership, but its enclosing close subsequently
shuts the pool down; this probe does not claim a permanent factory leak.

## Prepare once, publish before running

Recover v3.1's cumulative diff from
`reports/teardown-client-interrupt-probe-round-2-scaffold-v31.md`; verify SHA-256
`dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`.
Adapt it to the two methods below. Keep all evidence under a fresh directory in
java.io.tmpdir permitted by the test security policy. Do not disable that policy.

Publish the **full cumulative diff**, base SHA, SHA-256, final guarded commands,
actual runner/watchdog arrangement and cleanup code to
`reports/teardown-client-close-external-executor-round-1-scaffold.md` before
either filtered invocation. Compile-only preparation is allowed on Linux and
must be logged; it must not execute tests or widen this unit. Publish the final
scaffold after any compile repair and before tests.

Prevent the earlier duplicate-chain failure: use one nonblocking assignment
driver lock covering the entire pair, distinct from the host build lock so the
guarded runner does not deadlock. Check existing driver/Gradle/test JVM processes
and record driver PID, process tree and attempt ledger. A rejected tool request
does not prove its process stopped. Never launch a second chain without checking
and terminating the first as appropriate. Create unique per-attempt/per-arm
output paths with exclusive creation; refuse existing paths, never truncate them.

## Scaffold requirements

1. Each method creates a fresh ExecutorUtil MDCAware cached thread pool with an
   identifying SolrNamedThreadFactory, and a fresh HttpJettySolrClient using
   `.withExecutor(pool)`. Do not use withHttpClient(parent): that would borrow
   the HttpClient and test a different boundary. Keep the default HTTP/2 transport
   and record its actual runtime class. No requests are needed.
2. Before close, save identity references to client, HttpClient, executor,
   ClientConnector, SelectorManager, each ManagedSelector and each underlying
   java.nio.channels.Selector. Copy getBeans(ManagedSelector.class) before stop,
   then call getSelector() on each. Require the baseline to contain the expected
   two open selectors; stop as a scaffold/setup failure if it does not. Record
   actual Jetty runtime version and JAR/code source. The declared dependency is
   12.1.14; a mismatch must be reported rather than silently substituted.
3. Use exactly one original closer per arm. Set its interrupt immediately before
   client.close only in testExternalExecutorCloseProbeInterrupted. Control must
   record interruptBeforeClose=false. Capture full Throwable cause/suppressed
   chains, closer identity, interrupt status and a completion latch. Before the
   latch completes, result and terminal flag are UNAVAILABLE.
4. Save baseline, pre-close, immediately terminal, and terminal-plus-1-second
   snapshots (if it completes). If still active, sample at 5 seconds and at the
   30-second observation cap. Capture all thread identities/states and complete
   stacks without name filters. Record tracker membership, HttpClient/connector/
   manager/managed-selector lifecycle states, each **saved raw selector isOpen**,
   and executor shutdown/termination/active/queue state at each boundary.
   Jetty removes selector beans even on failed stop: rediscovering an empty list
   after close is not sufficient. An idle worker in the supplied executor is
   expected; attribute selector workers from stacks and the saved resource graph.
5. Do not shut down or interrupt the caller executor before both terminal captures.
   After original close terminates, submit a short bounded sentinel task to show
   whether that executor is still usable. Record rejection or timeout. A pool
   intentionally left running by client.close is not itself a resource leak.
6. Take all original-outcome evidence before cleanup. No concurrent second close,
   manual tracker release/clear, daemon conversion, leak suppression or linger
   increase. After the original closer finishes, allow up to 30 seconds of serial
   noninterrupted cleanup. If necessary explicitly close/wake saved raw selectors,
   attempt serial client close, then caller shutdownNow and a bounded executor
   await (at most 5 seconds per await). Log order, outcomes and every intervention;
   do not call the default 60+60-second shutdown helper in scaffold cleanup.
   Cleanup may change resource state, so label its snapshots separately.
7. If original close is still active at 30 seconds, capture diagnostics and end
   that isolated test JVM/process tree under the prearranged watchdog. No rescue
   close while it runs. Record INCOMPLETE, stop the pair, preserve all evidence.

The normal control is clean only if close returns, tracker releases, saved raw
selectors close, and the caller executor remains usable. If it is abnormal,
incomplete or setup fails, stop; do not spend the interrupted invocation.

## Exact test shape and hard budget

Run serially in separate fresh test JVMs through the guarded host runner. Archive
each arm's stdout/stderr, raw evidence and fresh XML **before** the next cleanTest.
The runner supplies JAVA_HOME, host serialization and watchdog containment.

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testExternalExecutorCloseProbeControl \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testExternalExecutorCloseProbeInterrupted \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

**At most two filtered Gradle invocations total**, one per method, control first.
Count even setup/scaffold failures; no automatic retry, third run or budget
exception. This is a fresh budget, not retroactive approval of round 2's overrun.
Observation cap: 30 seconds per original close, then at most 30 seconds serial
cleanup. Total wall budget: 20 minutes including compilation and both invocations.
The shorter close cap is deliberate: the supplied-executor path omits Solr's
60+60-second executor fallback. If it unexpectedly blocks, report that fact.
No full tidy/check task is requested for this scratch premise unit.

## Result and stopping point

Publish `reports/teardown-client-close-external-executor-round-1.md`, linking its
claim and scaffold. Include source/runtime pins, hash-verified raw evidence on
the board at new unique paths, commands and attempt ledger, host/JDK, exit codes,
fresh XML counts, timestamped resource/flag/exception table and cleanup outcome.
Report any budget violation or lost evidence first. Test XML success alone does
not decide the premise.

Distinguish: clean close; completed close with tracker/lifecycle failure only;
completed close with saved owned selector still open and/or attributed selector
worker surviving; or INCOMPLETE. Preserve any mismatch between object state,
thread state and tracker state. Neither a retained tracker nor a live caller
pool alone proves a live owned-resource survivor. Do not infer original-CI cause.

End with the narrowest evidence-supported next implementation question. This
assignment authorizes no production fix, mid-stop interruption variant, TLS/
scanner variant, other ranked sweep candidate, or further repeat. Post the
result even if incomplete; preserve the scaffold and raw logs.
