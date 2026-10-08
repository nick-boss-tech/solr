# solr-12245-submit

- Branch: origin/solr-12245-submit
- Head: 4a93167458b5
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind)
- Scope: 6 commits. `solr/core/.../update/processor/DistributedUpdateProcessor.java` (+22/-2, new `describe()` helper and two call sites), `DistributedUpdateProcessorTest.java` (+61, one new test), changelog `SOLR-12245-distrib-update-error-names-replica.yml` (+9, type `changed`). The handoff doc from `2d0ab31f2ae` is removed at head (`4a93167458b`).
- Verdict: Nearly (the code matches its changelog; the ticket framing is an owner call below)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Round 33 verification disposition: at this head (`4a93167458b5`), per the handoff.
- Bulk review (round 28, `research/branch-reviews/round-28/SOLR-12245-review.md`): Needs work. It was written against the same head, so its findings are checked again here.
- Head unchanged: the listed head matches `origin/solr-12245-submit`.
- Bulk HIGH (ticket framing) confirmed: the changed files contain no `MDC` references, so the branch does not change MDC. It changes the exception message. See the owner call below.
- Bulk MEDIUM (client-visible text) downgraded: see finding 3.

## Findings (ranked)

1. **HIGH, verified (framing, not code).** SOLR-12245's title is about MDC not being set on distributed-update errors. `grep MDC` over `DistributedUpdateProcessor.java` and `SolrCmdDistributor.java` at this head returns nothing. The branch changes the message text only: `describe()` appends `(while sending to <node url>, collection=..., shard=...)`. The ticket asks for the distributing side; the branch names the target. The changelog text is accurate about the message change, but the ticket title and any PR text must say so. Owner call, below.

2. **LOW, hypothesis.** `SolrError.req` is documented as possibly not the request that caused the error, because batches are merged (`SolrCmdDistributor.java:485-491`, javadoc). The node is the target of the failing batch, and `StreamingSolrClients` is per target, so the URL, collection and shard are probably right even when a different `Req` in the batch failed. Not verified in `StreamingSolrClients`.

3. **LOW, verified (downgraded from the bulk MEDIUM).** The exception is returned to the client at `DistributedZkUpdateProcessor.java:1367` and `RoutedAliasUpdateProcessor.java:229`. The message now includes a core-level URL, collection and shard. SolrJ already puts server base URLs into client-visible text (`solrj/.../RemoteSolrException.java:42`, `ConcurrentUpdateBaseSolrClient.java:350`), so the added exposure is small. Owner may still prefer the detail in logs only. Not decided here.

4. **LOW, verified (test coverage).** `testDistribErrorMessageNamesTheTargetReplica` covers one error with a full node. It does not cover the null guards in `describe()` (`error.req == null`, `node == null`, null collection, null shard), a null exception message, or the multi-error format. Hypothesis: the null branches are correct on reading, and a short extra test would pin them.

5. **Verified, no defect.** Signatures match. `Req(UpdateCommand cmd, Node node, UpdateRequest uReq, boolean synchronous)` is called with `(null, node, null, false)`. The anonymous `Node` implements all eight abstract methods (`getUrl`, `checkRetry`, `getCoreName`, `getBaseUrl`, `getNodeProps`, `getCollection`, `getShardId`, `getMaxRetries`). `SolrError` has an implicit no-arg constructor. `IOException` and `List` are imported. The existing test `testStatusCodeOnDistribError_NotSolrException` builds `SolrError` with `req` null, so its expected text is unchanged.

6. **Verified, no consumer in repo.** `during distributed update` appears only at the construction site and in tests. `TolerantUpdateProcessor` reads `errors` and metadata, not the message.

## Changelog

`type: changed`, title says the message names replica URL, collection and shard. The text describes the diff accurately and does not claim an MDC change. The author is the ICLA name.

## Owner call (not decided here)

How should SOLR-12245 be framed? The ticket asks about MDC and about naming "the shard / replica and collection name who is distributing the update". The branch names the target replica in the message. Options:

- (a) Keep the message change. Retitle the JIRA or the PR so it says "name the target replica in async distributed-update errors", not "MDC". This is the state the changelog already describes.
- (b) Keep the ticket as an MDC fix and add a thread-context change. That is a different patch, not this branch.
- Separately: whether the detail belongs in the client response or only in logs (finding 3).

## Not checked

- Not compiled, formatted (spotless), or run. Error Prone: the branch history shows a fix for an Error Prone warning at the gate (`a65fc9f0b58`), but no gate result file is cited, so the current state is a hypothesis.
- `StreamingSolrClients` batching not read (finding 2).
- Whether the current `main` sets MDC on the logging thread (bulk review's open question). Not checked.
- Nothing pushed or posted to GitHub or JIRA.
