# SolrJ and clients round 1 draft fidelity, slice 4

Slice: SolrJ drafts SOLR-12094, SOLR-14298, SOLR-14967, SOLR-17866 and SOLR-18341, in `pr-drafts/solrj/`. Worktree `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD `3576773b2f8` (origin tip, per the lead). The drafts, receipts and round reports were read from the working tree. `git status` shows no changes under `pr-drafts/solrj`, `receipts` or `reports` for these five drafts. Round roll-up `reports/solrj-clients-round-1.md` names claim `5bbcedf2d1e` and assignment `dd6c4c7e397`; neither file was re-read. Category round reports checked: `-s3.md` (12094), `-s4.md` (14298, 18341), `-s5.md` (14967, 17866), and the roll-up for owner decisions. Answers material: none. A grep of `material/` for the five numbers returned no hits, and no `material/*answers*` file covers them.

Head check: `git ls-remote origin refs/heads/solr-<n>-submit`, 2026-10-10. Each live tip matches the head the draft names and the head in its receipt.

## Verdicts

| Draft | Head checked (live = named) | Verdict |
|---|---|---|
| SOLR-12094 | `8d957a73f4fb` = `8d957a73f4fb1e8e6e72e53df4faa14266702e53` | CONSISTENT |
| SOLR-14298 | `67e75ea1eea4` = `67e75ea1eea4433d3b9fe8bb97b32bbc38ad373c` | DRIFT (1 item) |
| SOLR-14967 | `ee76643f5b3f` = `ee76643f5b3fa1809d204b6c4953aa9d54120589` | CONSISTENT |
| SOLR-17866 | `3f9367d796a6` = `3f9367d796a6446fa2f0549d8e9763e07831e6e8` | CONSISTENT |
| SOLR-18341 | `458b098719d7` = `458b098719d7ac988c47942eee05dbc2a83e7707` | DRIFT (6 items) |

Commit SHAs in every draft resolve locally (`git cat-file -t`). Citations were checked by exporting each cited file at its SHA (`git show <sha>:<path>`) and reading the cited lines. Blob links were not opened in a browser.

## SOLR-12094

Verdict: CONSISTENT.

Checked:
- Proof counts match `receipts/SOLR-12094.md`: TestJsonRecordReader 10/10 at head, 1 of 10 fails on base (`testMappedFieldAfterSplitIsRejected`), JsonLoaderTest 31/31, date 2026-10-05.
- Citations hold the cited code: JsonRecordReader.java at head, lines 313-315, 370-379 and 415-422; JsonLoader.java line 262; test lines 250-279 and 273-281. The base store at lines 401-405 (`c3cdf7b46e8`) is labelled "base code".
- Changelog `changelog/unreleased/SOLR-12094-json-record-reader-trailing-field.yml` exists at head. Its title matches the bold summary in substance.
- Limits match round s3: the behavior change, the relaxed mode as a follow-up offer, and unmapped fields still dropped. Jira comments 9 to 11 in the snapshot support the error-or-buffer framing.

Optional notes, not blocking:
- The test link `#L250-L282` runs two lines past the method, which ends at line 279. `#L250-L279` is exact.
- The draft has no separate title line. The PR title is not in the draft.
- No "choice" section. The assignment names none, and round s3 places the error-or-buffer question in Limits.
- About 4,340 characters with links, above the guide. Owner decision.
- Plain language: "buffering" and "emitted" are compressed. Optional.

## SOLR-14298

Verdict: DRIFT (1 item).

1. Draft says: "The ticket reports checks that took 14.5 to 19 seconds on a shard with about 1.2 billion matching documents ([ticket](https://issues.apache.org/jira/browse/SOLR-14298)). This change leaves that cost in place. Cheaper checks are a follow-up that we can open on request."
   - Evidence: `receipts/SOLR-14298.md` records only the 9/9, 1 of 9 and rc=0 counts. It has no timing or document figure, so under the brief the figures are unsupported. The figures are correct against the Jira snapshot `research/jira-context/SOLR-14298.json`: the comment by Dinesh Kumar Naik (2021-11-11) lists 14 slow-request lines with QTime 14553 to 19169 ms and hits 1226420962 to 1228021741. Either the main side adds these figures to the receipt, or the draft drops them.
   - Replacement: "The ticket reports that these checks can take many seconds on a very large shard ([ticket](https://issues.apache.org/jira/browse/SOLR-14298)). This change leaves that cost in place. Cheaper checks are a follow-up that we can open on request."

Checked (consistent):
- Proof: LBSolrClientTest 9/9 at head, 1 of 9 with the tag line reverted, solrj check with `-x test` rc=0. All match the receipt. The 2026-10-06 date is the receipt's round 27 record date.
- Base `e432df19c4a5` has no `ZOMBIE_CHECK_PARAM` or `zombieCheckQuery`, so "a base run cannot check it" holds.
- Citations at head: LBSolrClient.java 157 and 175 (tag), 162-176 (static setup), 178-180 (`zombieCheckQuery`), 756-760 (the check). LBSolrClientTest.java 54-61. All hold the cited code.
- Choice: both live alternatives are in the ticket. David Smiley proposed the ping handler (2023-09-11). Noble Paul proposed a settable check query (2023-09-07). Attribution to Noble Paul matches round s4 item 12.
- Limits: the tag does not make the check cheaper, and the test checks the query object, not the request on the wire. Both match round s4.
- Changelog `changelog/unreleased/SOLR-14298-zombie-check-trace-param.yml` exists at head. Type `changed`, and its title matches the draft.

Optional notes, not blocking:
- "Solr does not act on the parameter": a grep of `solr/core`, `solr/server`, `solr/solrj` and `solr/webapp` at `67e75ea` finds no `zombieservercheck` reference outside SolrJ. This is a grep, not a review.
- "It appears in the request parameters that Solr writes to its logs, such as the slow-request log": the ticket's sample slow-request lines show `params=` with the check query. Solr's logging code was not read (round s4 says so).
- The changelog link uses a descriptive file name. The round's `SOLR-<n>.yml` pattern is a template, and the linked file exists.

## SOLR-14967

Verdict: CONSISTENT.

Checked:
- Proof matches `receipts/SOLR-14967.md`: CloudSolrClientCacheTest 9/9 at head, the other eight tests pass against the first version, tidy and Error Prone rc=0, solrj check rc=0, date 2026-10-03.
- The three test names exist at CloudSolrClientCacheTest.java lines 199, 225 and 251. The caller-value test asserts that the stale-state retry sends no `_stateVer_`, so "the retry sends `caller:999`" describes the first version, as round s5 item 1 says.
- Citations: base CloudSolrClient.java 673-679 (the `// else: ???` comment at 679). Head 705-708 (copy with the computed version) and 709-713 (removal of the caller value). `RecordingCloudSolrClient` overrides `sendRequest` at line 492, which supports "no test reaches a server". The changelog `changelog/unreleased/SOLR-14967.yml` exists at head.
- Limits match round s5: no base run for the three new tests, and the draft claims none.

Optional notes, not blocking:
- "Against the first version of the change" cannot be reproduced from a branch commit. The receipt's "banked patch alone" matches no commit (round s5 item 2). The draft gives no hash. Receipt fix, main side.
- Landing interaction, not draft text: at 18341's head, the 14967 helper's `UNSPECIFIED` type makes two of the three new tests throw (round roll-up, s5). The helper should be `QUERY` in whichever lands second.

## SOLR-17866

Verdict: CONSISTENT.

Checked:
- Proof matches `receipts/SOLR-17866.md`: GenericSolrRequestTest 4/4 at head, exactly 2 of 4 fail on base `97d973814336`, date 2026-10-07.
- Citations: ClientUtils.java base line 65 (the `requiresCollection()` check). SolrRequest.java head 308-312 (`process` calls `collectionProvided`). GenericSolrRequest.java 34-39 (javadoc) and 109-113 (hook override). CloudSolrClient.java 639 and LBSolrClient.java 575 read the flag. HttpJdkSolrClient.java 234 (default collection). GenericSolrRequestTest.java 45-72 covers all four tests. The draft's statement that `WrappedSolrRequest` does not forward the hook holds: `WrappedSolrRequest.java` does not override `collectionProvided`.
- Both side effects in "What this change does" and Limits are in code (round s5 item 4, which says the receipt omits them).
- Choice: privatizing the field is a live alternative with a real cost (breaking API). Limits match round s5.
- Changelog `changelog/unreleased/SOLR-17866-generic-request-collection.yml` exists at head.

Optional notes, not blocking:
- Branch changelog title is wrong, not the draft. It says "unless setRequiresCollection(false) was called explicitly". The code opts out on any explicit call (GenericSolrRequest.java lines 98-100 and 109-113), as the draft says. Suggested title for the branch: "GenericSolrRequest.process(client, collection) no longer ignores the collection for clients that use a root URL, unless setRequiresCollection was called explicitly."
- The ticket's concrete alternative is a constructor that takes `requiresCollection`, with the setter deprecated (a 2025-08 ticket comment). The draft mentions the constructor but does not pose it as a choice. Naming it would sharpen the Choice.
- Round roll-up owner decision 3: not submit-ready before the upstream check. Not draft text.

## SOLR-18341

Verdict: DRIFT (6 items).

Checked: Proof counts (WrappedSolrRequestTest 28/28, CloudSolrClientCacheTest 11/11, LBAsyncSolrClientTest 7/7, LBSolrClientRetryUnsentTest 8/8, SolrRequestRetriableTest 15/15, solrj check and solrj-jetty compile and check, date 2026-10-04) match `receipts/SOLR-18341.md`. The "inconclusive by construction" statement matches receipt line 6. The five choices match the five design-record points in round s4. The ticket supports the atomic `inc` double-apply and the reset-after-write claims, and it says admin, security and unspecified requests keep today's behavior.

1. Draft says: "`LBSolrClient` retries an update only after a connection failure, where nothing was sent ([base lines 574-576](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java#L574-L576))."
   - Evidence: base LBSolrClient.java lines 574-576 hold only the `isAdmin` and `isNonRetryable` flags. The connection-failure rule is at base lines 675-680: an update (`isNonRetryable`) fails over only on `isConnectException` or `RequestNotSentException`, with the comment "Nothing of the request reached the server".
   - Replacement: "`LBSolrClient` retries an update only after a connection failure, where nothing was sent ([base lines 675-680](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java#L675-L680))."

2. Draft says: "A `_version_` above 0 may be retried, because a replay then fails with a 409 ([lines 412-427](https://github.com/nick-boss-tech/solr/blob/458b098719d7ac988c47942eee05dbc2a83e7707/solr/solrj/src/java/org/apache/solr/client/solrj/request/UpdateRequest.java#L412-L427))."
   - Evidence: head UpdateRequest.java 412-427 is the top-level `isRetriable()` only. The operator rules (`inc` and scalar `add` rejected; `set`, `remove`, `removeregex`, `add-distinct` and child-document `add` permitted; unknown operators rejected) are in `isAtomicMapRetriable`, lines 477-505. The `_version_` check is in `hasOptimisticVersion`, below line 427. The cited range misses that code.
   - Replacement: "A `_version_` above 0 may be retried, because a replay then fails with a 409 ([lines 412-505](https://github.com/nick-boss-tech/solr/blob/458b098719d7ac988c47942eee05dbc2a83e7707/solr/solrj/src/java/org/apache/solr/client/solrj/request/UpdateRequest.java#L412-L505))."

3. Draft says: "A shared cause-chain helper for `wasCommError` and `isConnectException` is a follow-up, tracked as [SOLR-9355](https://issues.apache.org/jira/browse/SOLR-9355). We can open it on request."
   - Evidence: the ticket's Related section (`research/jira-context/SOLR-18341.json`) names SOLR-9355 as a related defect at the leader-to-replica hop, whose fix scans the cause chain. It does not say SOLR-9355 tracks this helper. No receipt, round report or snapshot records a follow-up ticket.
   - Replacement: "A shared cause-chain helper for `wasCommError` and `isConnectException` is a follow-up. We can open it on request."

4. Draft says: "The Jetty client has no dedicated test."
   - Evidence: `solr/solrj-jetty/src/test` at `458b098` contains HttpJettySolrClientTest, HttpJettySolrClientCompatibilityTest and HttpJettySolrClientProxyTest. The true statement is that no test checks this error text. The lines 525-530 and the base "Connection lost at" text (base line 525) are correct.
   - Replacement: "No test checks this error text. When nothing was sent, its error text now reads "Connection failed before the request was sent to" instead of "Connection lost at" ([lines 525-530](https://github.com/nick-boss-tech/solr/blob/458b098719d7ac988c47942eee05dbc2a83e7707/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L525-L530))."

5. Draft says: "Its cached state stays until it expires, about 60 seconds by default ([time to live](https://github.com/nick-boss-tech/solr/blob/458b098719d7ac988c47942eee05dbc2a83e7707/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1646))."
   - Evidence: head CloudSolrClient.java line 1646 is `timeToLiveMs = 60 * 1000L` in `StateCache`, and the builder default at line 1309 is 60 seconds. The figure is correct, but `receipts/SOLR-18341.md` does not record it. Under the brief this is a receipt gap. Restore the figure once the receipt records it, or use the replacement.
   - Replacement: "Its cached state stays until it expires ([default time to live](https://github.com/nick-boss-tech/solr/blob/458b098719d7ac988c47942eee05dbc2a83e7707/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1646))."

6. Draft says: "This PR makes no fail-on-base claim."
   - Evidence: "claim" is on the public-vocabulary list (round check 7, brief item 7). The receipt's line 6 says the draft must state the inconclusive proof without a fail-on-base claim.
   - Replacement (the whole bold Proof line): "**Inconclusive by construction. The new tests call `isRetriable()`, which the base code does not have, so a base run cannot show a failure. This PR does not say the new tests fail on base.**"

Checked (consistent): the behavior bullets match head code. Load balancer 500, 503, timeout and reset failover for updates (`RETRY_CODES` at head LBSolrClient 630-650). Admin with a collection, untyped, security and streaming no longer retried. Cloud skips the replay after a 503 route error (CloudSolrClient 721-726). Cloud skips the replay for a non-retriable update after invalid-state or 404, and the cache removal is inside the gated block (lines 793-809, removal at 806-808). The JDK connect timeout is covered (HttpJdkSolrClient 233-237). Jetty EOF and closed-channel errors are covered (HttpJettySolrClient 569-573). Limits match round s4: new protected methods are `LBSolrClient.mayFailOver`, `HttpSolrClient.wasRequestUnsent` and `wasCommError`. `CloudSolrClient.wasCommError` is removed (base line 213 exists, head has none).

Optional notes, not blocking:
- PR apache/solr #4829, read live with `gh pr view` (read only): OPEN, draft PR, head `SOLR-18402-consolidate-retry-unsent-logic`, title "SOLR-18402: Consolidate wasRequestUnsent / wasCommError (retry) logic", updated 2026-09-30. "Open change" holds. "Draft PR" is more exact. "Makes different choices for stale-state retries" was not checked against the PR diff.
- The branch still has the SOLR-18368 Javadoc hunk at `CloudSolrClient.java` line 1550 (round owner decision 1). Branch fix, not draft text.
- Changelog `changelog/unreleased/SOLR-18341.yml` has type `changed` and title "SolrJ adds SolrRequest.isRetriable() so CloudSolrClient and LBSolrClient no longer double-apply atomic inc/add on comm errors." The draft has no title line. Round owner decision 6 asks that the title name the load balancer change and the new public method. Branch side.
- The load balancer update widening has no Choice. Round owner decision 3 is open (keep as drafted, add a Choice, or restore failover for some types). The draft matches "keep as drafted".
- About 9,683 characters with links, above the guide (round owner decision 7).
- Plain language: "idempotent", "replay", "non-retriable" and "route error" are compressed. "Safe to send twice" could replace "idempotent" for a maintainer reader. Optional.

## Not done

- No build, Gradle, test, gate or test-queue command. No commit, push, PR comment, review, close or edit. No Jira call.
- Jira text was read from the snapshots in `research/jira-context` only. Snapshot dates were not checked.
- GitHub: one read-only `gh pr view 4829`. Its diff was not read.
- Blob links were checked by object content at each SHA, not opened in a browser.
- Claim and assignment files were not re-read; the slice came from the lead's message.
- No answers material exists for these five tickets.
- The SOLR-14967 "first version" run cannot be checked from git (receipt-only claim).
- Plain-language review is light, and only optional notes were written.
