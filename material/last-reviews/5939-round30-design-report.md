# SOLR-5939: errors stamped with the wrong request in distributed updates

Round 30 design branch, built 2026-10-06 at Nick's direction (pick a sensible minimal
design, implement it on a branch, and let the PR description's Choices and Limits make
the case). PR NOT opened; opening is Nick's call.

## Shipped

- Branch `solr-5939-submit` at **8f7a36fa6dd2be126c9c689fe8c0951268ff217a**, one commit
  on base origin/main cabedd1d968, pushed to the fork as a new branch, ls-remote
  verified. Author Nick Shanin <nick.boss.us@gmail.com>, no Claude trailer.
- GH corroboration: run **37574728851** (ci/5939-attribution,
  org.apache.solr.update.StreamingSolrClientsErrorAttributionTest, :solr:core),
  in progress when recorded; conclusion to be recorded in the takeover log when it lands.

## Design as implemented (per-update attribution, the preferred shape)

- SolrJ, `ConcurrentUpdateBaseSolrClient` (additive only, no existing signature changed):
  a runner-confined `ThreadLocal` collector; a new `protected final recordStreamedUpdate(Update)`
  hook that stream implementations call for each update actually written into the current
  stream; and a new protected callback `handleError(Throwable, List<UpdateRequest> requests,
  List<String> failedIds, String collection)` whose default implementation delegates to the
  previous whole-stream error path, so every existing subclass and the `UpdateErrorHandler`
  route behave exactly as before. The Runner calls the new callback at its three stream
  failure sites (non-OK status, RemoteSolrException, other Throwable). When an
  implementation recorded nothing, the callback receives just the request the stream was
  started with, so behavior degrades to the old attribution, never worse.
- `ConcurrentUpdateJettySolrClient` records each update just before writing it into the
  merged stream (updates polled but re-queued for different params or collection are never
  recorded). `ConcurrentUpdateJdkSolrClient` records its single update (it does not merge).
- Core, `StreamingSolrClients`: an identity registry `Map<UpdateRequest, SolrCmdDistributor.Req>`
  (synchronized IdentityHashMap) populated in `getSolrClient(Req)`, the one place every Req
  passes immediately before submission, and shared with each built error client. Its lifetime
  is one distribution round; entries for failed requests are removed as their errors are
  reported, and a resubmitted request is re-registered by its next `getSolrClient` call.
- Core, `ErrorReportingConcurrentUpdateSolrClient`: overrides the new callback and records
  one `SolrError` per request actually in the failed stream, sharing the exception, each
  stamped with its own Req, so `doRetriesIfNeeded` evaluates each real update, the
  delete-by-query no-retry rule is judged per request, and `trackRequestResult` failure
  entries land on the right trackers. A request with no registry entry falls back to the
  client's captured first Req for that record only. `handleError(Throwable)` keeps the old
  behavior for failures that are not stream failures.
- `SolrError.req` javadoc rewritten: the old note (the stamped request might not be the one
  that caused the error) is replaced with the new contract (a stream failure is recorded
  once against each request in the stream, because it cannot be narrowed further).
- Deliberately unchanged: `onSuccess` attribution for merged streams (Limits item).

## Premise (verdicts from fresh JUnit XML in worktree solr-5939-gate)

New test class `StreamingSolrClientsErrorAttributionTest` (org.apache.solr.update,
extends SolrTestCase). It drives the real streaming client through the public
`SolrCmdDistributor` API against a node on a closed loopback port, so every stream fails
with a connection error. Note on the assignment text: `MockStreamingSolrClients` cannot
reproduce this defect, because it throws synchronously from `request()` and exercises the
distributor's own catch block, which already stamps the correct request; the misattribution
only happens on the asynchronous streaming path, hence the real client against a dead port.
The same `ModifiableSolrParams` instance is passed to each distrib call, mirroring
production and making stream merging deterministic; the assertions hold whether the
updates merge into one stream or run as two, on both trees.

On base production cabedd1d968 (test only added), both tests FAILED, exactly as designed
(run log g5939-premise2.log, JUnit XML in the worktree):

- `testMergedStreamErrorIsAttributedToEveryRequestInTheStream`: expected error doc ids
  [1, 2] but was [1]. One error for the whole merged stream, stamped with the first add;
  the second add's failure left no record against its own request.
- `testDeleteByQueryRetryRuleIsJudgedOnItsOwnRequest`: expected 2 errors but was 1. The
  single error is stamped with the delete-by-query request, its no-retry rule swallows the
  failure, and the retriable add in the same stream is never resubmitted and never recorded.

At head 8f7a36fa6dd both tests pass (2/2).

## Gate (log g5939-gate3.log, all steps under the test-queue flock)

- Changelog YAML parse ok (`changelog/unreleased/SOLR-5939.yml`, type fixed).
- tidy rc=0 (gradle/libs.versions.toml restored after).
- Error Prone compile rc=0 (`-Pvalidation.errorprone=true`, :solr:solrj, :solr:solrj-jetty,
  :solr:core compileJava plus :solr:core compileTestJava). The first gate run caught two
  MissingOverride warnings on the new test's setUp/tearDown (kept out of the PR
  description per the formula); fixed with @Override and the rerun was clean.
- Focused tests, counted from fresh JUnit XML: StreamingSolrClientsErrorAttributionTest
  2/2, SolrCmdDistributorTest 1/1, ConcurrentUpdateJettySolrClientTest 11/11,
  ConcurrentUpdateJdkSolrClientTest 11/11, SolrExampleStreamingTest 43 tests, 0 failures,
  1 skipped.
- Module check rc=0 (:solr:solrj:check, :solr:solrj-jetty:check, :solr:core:check, -x test).

Process notes: two earlier gate-chain launches were lost to the night's flock contention
(one killed after the Error Prone catch, one vanished with an empty log during a VM
churn window); every verdict above rests on the g5939-gate3.log chain and the JUnit XML
in this worktree. The very first premise invocation also returned a compile-failure text
that did not match this worktree's state (the same test compiled cleanly minutes later);
it was discarded and the premise rests only on the premise2 run's XML.

## Draft PR description (complete; PR not opened)

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-5939

## What happens today

`StreamingSolrClients` keeps one streaming update client per node, and that client is built with the first request sent to the node. Later updates are merged into the same stream, but when the stream fails, the error is recorded against the captured first request, whichever requests the failed stream actually carried. `SolrCmdDistributor` then acts on that wrong attribution. A second update in a failed stream leaves no error record against its own request, and its failure is tracked against the first request's replication tracker instead. When a delete-by-query goes first and an add is merged behind it, the one recorded error is judged by the delete-by-query no-retry rule, so the add is never retried and effectively disappears.

## What this change does

The streaming client now reports which requests it actually wrote into a stream. `ConcurrentUpdateBaseSolrClient` gains a recording hook that stream implementations call for each update written (the Jetty and JDK clients both do), and stream failures are delivered through a new `handleError` callback carrying the requests in the failed stream, in write order. The new callback's default implementation keeps the previous whole-stream behavior, and no existing callback signature changes. On the core side, `StreamingSolrClients` remembers which distributor request each submitted `UpdateRequest` belongs to, and the error client records one error per request in the failed stream, sharing the exception, so each request gets its own retry decision and its own tracker entry. If a stream implementation does not report its membership, the failure is attributed to the request the stream started with, as before.

## Proof

New test `StreamingSolrClientsErrorAttributionTest` (org.apache.solr.update, :solr:core) drives the real streaming client against a node where nothing listens. On the base code both tests fail: two adds in one stream produce a single error stamped with the first add (expected error doc ids [1, 2], got [1]), and a delete-by-query followed by an add produces one error total, so the add is never retried. With this change both pass, 2/2. Also green at this head (verified 2026-10-06 at 8f7a36fa6dd): SolrCmdDistributorTest, ConcurrentUpdateJettySolrClientTest 11/11, ConcurrentUpdateJdkSolrClientTest 11/11, SolrExampleStreamingTest (43 tests, 0 failures, 1 skipped).

## Choices to check

Position taken here: one error record per request actually in the failed stream, sharing the stream's exception. Two broader or different routes were considered:

1. Plumb per-document identity through the SolrJ callback surface, so an error could name the exact documents, or the exact request, that the remote node rejected. A stream gets one response for the whole batch, so the client cannot know which update in the stream the server rejected; this route needs server-side changes or per-request sends, giving up stream merging. Happy to do it in this PR if maintainers prefer that direction.
2. Fail the whole batch on any stream error: record the failure once and treat every update sent to the node as failed, with no per-request retry decisions. Simpler, but it throws away retries for updates the stream never carried and keeps a wrong, if coarser, attribution. Happy to switch to it in this PR if maintainers prefer.

Was per-request attribution the right middle call?

## Limits

* Within a failed stream, which single update the remote node rejected is still unknowable from the stream response; every request in the stream gets the error and its own retry decision. A request that would have succeeded on its own can be retried once more than strictly needed.
* Success attribution is unchanged: when a merged stream succeeds, replication-factor credit still goes through the client's original onSuccess hook rather than being split per request in the stream.
* A stream implementation that never reports its membership degrades to the previous behavior for that stream: the error is attributed to the request the stream started with.
* Per-document retry granularity is not attempted; retries remain per request.

Changelog: `changelog/unreleased/SOLR-5939.yml`

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
