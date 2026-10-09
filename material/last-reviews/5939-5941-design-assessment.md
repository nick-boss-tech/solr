# Design assessment: SOLR-5939 and SOLR-5941 (2026-10-06)

Assessment only; no code was changed. Both tickets came out of the skip audit flagged as real defects with no small safe fix. This assessment re-verified that claim against the tickets and against origin/main at cabedd1d968 (2026-10-06). Local records before this assessment: both tickets sit in the major-bugs lists and in pipeline/queue.json as pending with no note; no fork branch exists for either; no prior skip analysis was recorded.

Headline result: the skip audit's instinct was right about the shape (neither ticket has a small safe fix) but neither ticket is too big in effort. Both are blocked on design consensus, not on code volume. The correct next step for each is a Jira design discussion with a concrete proposal, then a normal PR once a maintainer picks a direction.

## SOLR-5939: Wrong request potentially on Error from StreamingSolrServer

### The defect

When a leader distributes updates to a node, errors reported by the streaming client are attributed to the first request ever sent to that node in the distribution round, not to the request whose update actually failed. The user-visible consequence sits in the retry machinery: `SolrCmdDistributor.doRetriesIfNeeded` resubmits the stamped request's update, so the update that failed may never be retried while an unrelated earlier update is sent again; the retry decision itself (including the rule that delete-by-query requests are not retried) is computed from the wrong request; and replication-factor accounting is recorded against the wrong request. The ticket was reported by Per Steffensen in 2014 from code inspection, with a demo patch that only documents the problem. Mark Miller agreed it deserved its own issue. Nobody ever attempted a fix, and the ticket has been untouched since 2016. The project's current code documents the ambiguity instead of fixing it: the javadoc on `SolrCmdDistributor.SolrError.req` says the stamped request "might not actually be the request that caused the error".

### Verified state on current main

The mechanism survives intact, in modern dress:

- `StreamingSolrClients.getSolrClient(SolrCmdDistributor.Req)` (solr/core, org.apache.solr.update) caches one client per node URL. The first call builds an `ErrorReportingConcurrentUpdateSolrClient` with that call's Req, and the client keeps it in a final field.
- `ErrorReportingConcurrentUpdateSolrClient.handleError(Throwable)` stamps every error with the captured Req (`error.req = req`), evaluates `req.shouldRetry(error)` on it, and calls `req.trackRequestResult(null, null, false)` on it when not retrying.
- Synchronous submission failures in `SolrCmdDistributor.doRequest` stamp errors correctly; only the streaming path misattributes.
- Consumers of the stamp: `SolrCmdDistributor.doRetriesIfNeeded` (retry decision, retry counter, `submit(err.req, false)` resubmission), `Req.shouldRetry` (node retry policy plus the delete-by-query exclusion read from the stamped request's `uReq`), `Req.trackRequestResult` (leader and rollup replication-factor trackers), and `DistributedZkUpdateProcessor` (line 1244, which reads the stamped request's params when filtering errors).
- The collision needs two distinct Reqs to the same node inside one distributor's life (a distributor is created per update request in `DistributedZkUpdateProcessor`). Concrete cases: the add phase followed by `distribCommit` on the same distributor reuses the client built by the first add Req, so a failed commit is stamped as an add; a node that is both a replica target and a forward target in the same request; and mixed delete and add phases. This bounded frequency matches the ticket's own impact-low label. Re-sent add batches are versioned and largely idempotent, which further bounds the common case; the sharp cases are delete-by-query retry semantics, commit failures resubmitting add batches, and wrong replication-factor accounting.

### Why no small safe fix exists

The attribution is destroyed before any core code can recover it, by design, in SolrJ:

- `ConcurrentUpdateJettySolrClient.doSendUpdateStream` (solr/solrj-jetty) starts from one queued update and keeps merging further queued updates into the same HTTP stream while they belong to it (same destination and params). A failed stream therefore genuinely spans several submitted requests; there is no single originating request to stamp.
- The failure callback that core overrides, `ConcurrentUpdateBaseSolrClient.handleError(Throwable)` (solr/solrj), receives only the exception. The runner's failure sites know the merged document ids and the collection, and SolrJ's newer `ErrorHandler` callback passes exactly those (`onError(ex, failedIds, collection)`), which shows the project's chosen attribution granularity is document ids, not requests. The core subclass never sees even that.
- The obvious small shapes fail on inspection. One client per request defeats the connection sharing, queueing, and ordering model that `StreamingSolrClients` exists to provide. A "currently sending request" field in the client is wrong under merged streams and racy when the runner count is raised (`solr.cloud.replication.runners`). Stamping the error with the most recent request is the same bug with a different victim.

### The smallest design-level change that fixes it

Plumb originating-request identity through the streaming path and rebuild retry units from it. In outline: SolrJ's error reporting gains a way to hand the callback the set of enqueued updates (or an opaque per-update token) that went into the failed stream, complementing the document ids it already computes; `StreamingSolrClients` maps those back to the Reqs it submitted; `SolrCmdDistributor` then creates one error per affected Req (or retries at document granularity) so `shouldRetry`, retry counters, and the replication trackers all run against the right requests. Blast radius: a public SolrJ client class's callback surface (external subclasses exist, so the change must be additive, for example a new callback with a default that delegates to `handleError(Throwable)`), the Jetty streaming client, the core streaming-clients wrapper, and the distributor's retry loop. No wire format change: the remote side is untouched, and mixed-version clusters are unaffected because all of this is sender-side. Test surface exists and is adequate: `SolrCmdDistributorTest` and `MockStreamingSolrClients` on the core side, `ConcurrentUpdateJettySolrClientTest` and its test base on the SolrJ side. A unit-level reproduction of the defect is cheap (two distinct Reqs to one node through the mock clients, fail the second, observe the stamp); the original 2014 demo took the same approach against a distributed test, which is the expensive variant and is not needed to pin the bug.

### Partial options

- Fail-the-batch semantics: when a streaming error cannot be attributed, treat every outstanding Req to that node as errored instead of retrying the stamped one. This removes the worst harm (silently retrying the wrong update while the failed one is accounted as tracked) at the cost of surfacing more failures to clients. It is a much smaller change, confined to core, but it is still a behavior change in distributed update error handling and needs maintainer agreement.
- Cache-key split: give commits and delete-by-query requests their own client instances per node, so cross-kind misattribution (the commit-stamped-as-add case, the delete-by-query exclusion misfiring) cannot happen. Same-kind misattribution remains. Contained, but partial and somewhat arbitrary; maintainers may read it as a workaround rather than a fix.
- Documentation alone is already effectively done (the `SolrError.req` javadoc) and does not change behavior.

### Verdict

Doable only with maintainer agreement on the design first. The code volume is days, not weeks, once the attribution model is chosen; the choice itself (per-request errors versus per-document retries versus fail-the-batch) changes error-handling semantics that other code, including `DistributedZkUpdateProcessor`'s error filtering, already depends on. Recommended sequence: a Jira comment mapping the 2014 report onto the current classes with the options above, a maintainer pick, then a PR. A cold PR picking one semantics unilaterally would very likely stall in review, which is the same failure mode that has kept the ticket open since 2014.

## SOLR-5941: CommitTracker should use the default UpdateProcessingChain for autocommit

### The defect

Autocommits bypass the update processing chain. `CommitTracker.run()` (solr/core, org.apache.solr.update) builds a `CommitUpdateCommand` against a bare synthetic request and calls `core.getUpdateHandler().commit(command)` directly. An explicit commit sent by a client instead travels through the request handler into the chain, and every configured update processor gets its `processCommit` call (the chain tail, `RunUpdateProcessorFactory`'s processor, is what calls the update handler). The user-visible symptom from the ticket: a custom update processor that does work on commit sees nothing for autocommits, so behavior differs depending on whether a commit came from the autocommit timer or from a client, which the reporter called incoherent. Reported by Ludovic Boutros in 2014 with a small starting patch; assigned to a maintainer (Shalin Shekhar Mangar) who never responded on the ticket; untouched since 2016.

### Verified state on current main

- The bypass is exactly as reported: `CommitTracker.run()` on current main still calls `core.getUpdateHandler().commit(command)` with a `SolrQueryRequestBase` carrying empty params. No chain is created.
- The machinery the 2014 patch used still exists: `SolrCore.getUpdateProcessingChain(String)` for chain lookup, and the distribution-control params (`DistributedUpdateProcessor.COMMIT_END_POINT`, `DistributingUpdateProcessorFactory.DISTRIB_UPDATE_PARAM`) that the chain's distributed processors parse. The recovered 2014 patch routed the autocommit through the default chain with distribution skipped and the commit marked as end point. It also shows its age: it never finishes or closes the per-request processor it creates, a lifecycle gap a modern version must close.
- One part of the use case is already served by a different hook, verified on main: `UpdateHandler` fires registered `SolrEventListener` postCommit and postSoftCommit callbacks inside the same commit method autocommit calls (UpdateHandler.java lines 93 and 105). Anything that only needs to react when a commit happens, regardless of source, can use solrconfig listeners today. What remains unserved is narrower than the ticket's framing: a chain processor that must see, transform, or veto the commit itself as part of the chain.

### Why no small safe fix exists

The 2014 patch is small, and that is the trap: the code is easy, the semantics are not settled.

- Which chain: documents reach a core through many chains (different request handlers, `update.chain` request params). Autocommit is a core-level timer with no originating request, so there is no recorded chain to reuse. The ticket title says "the default chain", but adopting it means autocommit suddenly runs processors that document authors may have placed only in non-default chains, and skips processors placed only in the chains the documents actually used. Either choice surprises someone.
- SolrCloud behavior: the chain is the distribution mechanism. An explicit commit entering the chain on a leader is distributed to replicas by `DistributedZkUpdateProcessor.processCommit`; an autocommit must stay local (each replica autocommits on its own schedule, and `CommitTracker.run()` already takes care to stamp a new version clock only for leader hard commits). Keeping it local means leaning on the skip and end-point params, after which the commit traverses a chain whose distributed stages are all inert: defensible, but it is a design statement that needs a maintainer's name on it, and the params were designed for forwarded requests, not for a timer.
- Synthetic request context: processors receive a request with empty params and no user, no request handler, and no response consumer. Processors that read commit-related params, check permissions, or write response fields behave differently or fail in ways that only surface with third-party processors, which cannot be enumerated from this codebase.
- Failure and lifecycle semantics: a processor throwing during an explicit commit fails a client request; the same throw during autocommit has nowhere to go (the current code logs "auto commit error" and moves on), and each autocommit must create, finish, and close a fresh processor instance because chain processors carry per-request state (the distributed processor most of all). None of this is hard to code; all of it is behavior someone must choose.

### The smallest design-level change that fixes it

Adopt the 2014 patch's shape with modern lifecycle handling: in `CommitTracker.run()`, build the synthetic request with the distribution-skip and commit-end-point params set, obtain the default chain from the core, create a processor, call `processCommit`, then finish and close it, keeping the existing error logging. Blast radius: one core class's behavior, but the behavioral blast radius is every installed processor that overrides `processCommit`, in both standalone and Cloud deployments, plus test surface that today assumes autocommit is invisible to processors. Test surface: `CommitTracker` is exercised indirectly through autocommit tests in the update handler tests; a dedicated test with a recording processor in the default chain (autocommit fires, processor saw the commit; in Cloud mode, no distribution occurred) would need building, and the Cloud half wants a small SolrCloud test fixture. No public API or wire change.

### Partial options

- Documentation: state in the ref guide's update processor and autocommit material that autocommits do not traverse the processor chain, that `processCommit` fires only for client-sent commits, and that `SolrEventListener` postCommit listeners are the hook that fires for every commit. Accurate today, zero risk, and it converts a silent trap into documented behavior. Worth doing on its own.
- Startup warning: when a core's default chain contains a processor that overrides `processCommit` and autocommit is configured, log a warning that autocommits will not reach it. Small, safe, and surfaces the incoherence at the moment it can still be acted on. Also worth doing on its own; it pairs naturally with the documentation.
- The full fix without prior agreement is not a partial option; it is the design decision taken unilaterally.

### Verdict

Doable only with maintainer agreement on the design first. A decade with a maintainer assignee and a working starting patch produced no movement, which reads as missing consensus on the semantics (chain choice, Cloud locality, synthetic request context), not as missing effort. Recommended sequence: a Jira comment restating the problem in current terms, naming the listener hook that already covers the reaction use case, and putting the specific design questions to the project; the documentation and warning partials can proceed as a separate small PR without waiting, and they remain useful even if the full fix is later approved. Once the semantics are picked, the implementation is a normal, modest PR.

## Summary

| Ticket | Defect still on main | Small safe fix | Verdict |
| --- | --- | --- | --- |
| SOLR-5939 | Yes, same mechanism in StreamingSolrClients and SolrCmdDistributor | No: attribution is destroyed by stream merging in SolrJ | Design agreement first, then a normal PR (days of plumbing) |
| SOLR-5941 | Yes, CommitTracker.run() still calls the update handler directly | No: the code is small but the semantics (chain choice, Cloud locality, synthetic request) are unsettled | Design agreement first; docs plus a startup warning are doable now |
