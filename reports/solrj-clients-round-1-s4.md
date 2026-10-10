# SolrJ and clients round 1, part S4: load balancing (SOLR-5220, SOLR-14298, SOLR-18341)

Drafts are written to `pr-drafts/solrj/`. Nothing was built, tested, posted, committed or pushed. Only the three draft files were written. Temporary files are in the scratchpad. One dangling commit object was created with `commit-tree` for a three-way trial merge; no refs were written.

## Per ticket

**SOLR-5220: draftable.** Gated at the live tip per the receipt. The change and its three tests match the code, with one scope correction: the code changes only 403 and 404, not all 4xx (see receipt disagreement 1).
- Draft `pr-drafts/solrj/SOLR-5220.md`, 3,501 characters with links.
- Head `e415c409044c4a6cc846cddfe56c1d7b9f262767`: `ls-remote` matches the assignment and the local remote-tracking ref.
- Base: merge-base `cabedd1d968059215188f4e7563fb303241899ed`.
- No Choice section: the assignment names none (owner decision 10).

**SOLR-14298: draftable.** Gated at the live tip per the receipt.
- Draft `pr-drafts/solrj/SOLR-14298.md`, 3,733 characters with links.
- Head `67e75ea1eea4433d3b9fe8bb97b32bbc38ad373c`: `ls-remote` matches.
- Base: merge-base `e432df19c4a50b9d54d7fba545397b859cc54f98`.
- Has the Choice (the tag as implemented, versus a ping route, versus a settable check query) and Limits (the tag makes the check identifiable, not cheaper; cheaper is a follow-up offer; the new test checks the query object, not the request on the wire).

**SOLR-18341: drafted, proof stated as inconclusive by construction (no fail-on-base claim). Not submit-ready.**
- Draft `pr-drafts/solrj/SOLR-18341.md`, 9,683 characters with links (about 7,100 without link targets). It is above the 3,500 guide because of five choices and the 15-file scope.
- Head `458b098719d7ac988c47942eee05dbc2a83e7707`: `ls-remote` matches.
- Base: merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- See owner decisions 1 to 4.

**Head verification:** all three `ls-remote` results match the assignment. The remote-tracking refs `refs/remotes/origin/solr-<ticket>-submit` already exist locally and equal the live heads, so no explicit fetch was needed. No SolrJ source file changed on `upstream/main` since the 18341 base. `LBSolrClient.java` is unchanged on `upstream/main` since the 5220 and 14298 bases.

## Load balancer landing order and trial merges

Trial merges (`git merge-tree --write-tree`, no refs written):
- 5220 with 14298: clean, in both orders.
- 5220 with 18341: clean, in both orders.
- 14298 with 18341: clean, in both orders.
- 5220 and 14298 combined, then 18341: clean.

**Overlap:** no shared hunk.
- 5220 edits the zombie step in `doRequest` (head lines 650 to 652).
- 18341 edits the comment just above it (head lines 643 to 644). It also replaces the `SocketException`, `SocketTimeoutException` and `SolrServerException` catches further down with one catch for `SolrServerException` or `IOException`, plus a new `mayFailOver` method.
- One unchanged line (the `RETRY_CODES` if) separates the 5220 and 18341 edits. The merge is clean but fragile if that line changes.
- 14298 edits only the static initializer and `zombieCheckQuery` (head lines 157 to 180). It does not touch `doRequest` or `checkAZombieServer` (head lines 756 to 760), and the alive check uses the four-argument `doRequest`, which 18341 does not change.

**Semantic interaction:** 18341 makes idempotent updates retriable, so a 403 or 404 on an update now fails over. Without 5220, that answer would also mark the server a zombie.

**Landing order:** 5220 before 18341 (required by that interaction). 14298 is independent of both and can land at any point. 5220 before 14298 is not required: both orders merge clean, and the changes do not interact at run time. The receipts say the two "meet in the same method area"; they do not. 18341 changes `LBAsyncSolrClient`, and 5220 does not. After 5220, the async client still marks a server a zombie on 403 and 404. The 5220 draft names this in Limits.

## Self-check per draft

- Dashes: zero em or en dashes in all three.
- Process words (gate, receipt, ledger, rc=0, owed, round, JUnit, pre-fix, branch, worktree, handoff, takeover, queue, disposition): none in 5220 or 14298. None in 18341 either, except the word "claim" once in the Proof line, which is not on the list.
- Head references: every GitHub link uses a full 40-character SHA. Head-state links use the head SHA. Base-state links use the merge-base and say "base". The only non-blob GitHub link is a cross-repo PR reference, written as a full link (apache/solr PR 4829 in 18341).
- The Jira link line, bold one-line summaries, AI header and footer, and changelog link at the head are present in all three.
- No Lucene version is named, so the Lucene rule does not apply.

## SOLR-18341 design record: five points

The record is `wt/SOLR-18341/SOLR-18341-TESTING.md` ("Be skeptical about", items 1 to 5). It is an untracked handoff in another worktree, not in the 15-file branch. Each point was checked against the head diff:
1. **503 route error not replayed for update-typed requests, even idempotent ones.** Confirmed (`CloudSolrClient` head lines 718 to 724). It lands in Choices item 1, and in the Cloud behavior bullet on 503 route errors.
2. **Base policy narrower than "unchanged".** Confirmed for the load balancer: UNSPECIFIED (generic requests with no type), SECURITY, STREAMING and collection-tied ADMIN lose failover. The "unchanged" claim holds only for collection-less ADMIN. It lands in Choices item 2 and the load balancer behavior bullet.
3. **INVALID_STATE replays gated.** Confirmed (lines 793 to 809). The record does not mention that the cached collection state is also not cleared for non-retriable requests on this path. The draft says so. It lands in Choices item 3 and the Cloud behavior bullet on invalid-state and 404 errors.
4. **Removed protected `CloudSolrClient.wasCommError`; `LBSolrClient.isConnectException` no longer governs HTTP clients; no upgrade note.** Confirmed. It lands in Choices item 4 and in Limits (new protected methods). The missing upgrade note is kept out of public text (owner decision 5).
5. **Connect-class exemption only in the load balancer.** Confirmed. It lands in Choices item 5 and the Cloud behavior bullet (refused connections included).

Extra findings not in the record, all in the draft: the sync load balancer now fails over on any `IOException`; the load balancer now retries idempotent updates on 500, 503, timeout and reset (wider than the ticket; behavior list); JDK connect timeouts and Jetty EOF or closed-channel errors now count as communication errors; the Jetty error text changes for one path (Limits); unknown atomic operators are not retriable.

## Receipt and record disagreements (exact wording)

1. **SOLR-5220 receipt, line 7, and assignment line 36:** "A client-error (4xx) response no longer marks the server a zombie." The code (head line 651): `serverMayBeDown = e.code() != 403 && e.code() != 404`. Only 403 and 404 change. Other 4xx codes were already not zombie-marked: they are outside `RETRY_CODES` and are rethrown. The draft says 403 and 404.
2. **SOLR-5220 receipt, lines 4 to 5:** "the gated tree was `7650584a756`, verified byte-identical in content to the tip." The tip tree is `333d3a64e5cffd3f49fa2d9098df9ebc29b2bac4`. Object `7650584a756` is not in this worktree's or the main checkout's object store. The same receipt says the tip differs from the gated history "only by the dropped handoff document", which would make the trees differ. Not verifiable here.
3. **SOLR-5220 receipt, line 7, and SOLR-14298 receipt, line 7:** "the two meet in the same method area." They share no method: 14298 edits the static initializer and `zombieCheckQuery`; 5220 edits `doRequest`. 5220 and 18341 share `doRequest`; 14298 does not.
4. **SOLR-14298 receipt, line 7:** "a settable check query as Paul Noble suggested." The Jira comment author is "Noble Paul" (`research/jira-context/SOLR-14298.json`, comment 17762595). The draft uses Noble Paul.
5. **SOLR-18341 design record, "Fixed in this pass":** "State invalidation (`maybeStale` / `refresh`) is no longer skipped for non-retriable requests; only the replay is gated." True for the communication-error path (lines 735 to 753). Not true for invalid-state and 404: the cache removal sits inside the gated block (lines 806 to 808, within 793 to 809).
6. **SOLR-18341 research note** (`research/131-solr18341-research-note.md`), P1 review repair dated 2026-09-30: "Removed the unrelated SOLR-18368 Javadoc hunk from `CloudSolrClient`." At head `458b098719d7` the hunk is still present: `CloudSolrClient.java` line 1550 contains "(SOLR-18368)."
7. **SOLR-18341 research note, "Left in this slice on purpose", item 3:** "SECURITY / STREAMING types are untested." Head `SolrRequestRetriableTest` lines 45 to 50 assert the default policy for both. Out of date.
8. **SOLR-18341 research note, same section, item 2:** "wasCommError still instanceofs the root cause once." The ticket says "a top-level instanceof SocketException || UnknownHostException". The base code uses `SolrException.hasCause` over the cause chain (base `CloudSolrClient.java` lines 213 to 216) and includes `RequestNotSentException`. The draft repeats neither description.
9. **SOLR-18341 research note proof counts for 2026-09-30** (`SolrRequestRetriableTest` 13, `WrappedSolrRequestTest` 29, `CloudSolrClientCacheTest` 6) differ from the receipt (15, 28, 11). The note says its run predates the head. The draft uses the receipt counts.

## Owner decisions

1. **SOLR-18341:** remove the SOLR-18368 Javadoc hunk from `CloudSolrClient.java` (line 1550) before any PR. It is out of scope, and the research note says it was already removed.
2. **SOLR-18341:** competing apache/solr PR 4829 (SOLR-18402). The research note says not to submit separately while it stands. The draft names it in "What happens today". Its live status was not checked. Decide whether to name it, and whether to hold the PR.
3. **SOLR-18341:** the load balancer widening (idempotent updates retried after 500, 503, timeout and reset; the sync client fails over on any `IOException`; admin, untyped, security and streaming requests no longer retried). Decide: keep as drafted, add a Choice for the update widening, or restore failover for some types. The draft offers the broader route in Choices 2 and 5.
4. **SOLR-18341:** the invalid-state and 404 path does not clear cached state for non-retriable requests. Decide whether to fix before the PR (clear the entry, keep the replay gate), or keep it as Choice 3.
5. **SOLR-18341:** the removed protected `CloudSolrClient.wasCommError`. Keep as Choice 4, or add a deprecated delegate, and write the upgrade note the record asks for.
6. **SOLR-18341:** the changelog (`changelog/unreleased/SOLR-18341.yml`) is type "changed". Its title does not name the load balancer change or the new public method. The research note suggested an added entry. Decide the wording.
7. **SOLR-18341:** the draft is 9,683 characters, above the 3,500 guide. Trim Choices or Limits if preferred.
8. **SOLR-5220:** confirm the sentence "The run used the same source content as this head." The gated tree identity could not be verified (disagreement 2).
9. **SOLR-5220:** confirm the 403 and 404 scope against the receipt's "4xx" wording (disagreement 1). Confirm the drafted Limit offering a follow-up for `LBAsyncSolrClient`.
10. **SOLR-5220:** the ticket's own proposal (stop retrying 403 and 404 altogether; Jan Hoydahl, 2014) and the shutdown counter-argument (Mark Miller) are a live alternative. The assignment names no Choice, so the draft has none. Decide whether to add one.
11. **Landing order:** 5220 before 18341 (recommended). 14298 is independent.
12. **SOLR-14298:** the draft attributes the settable-query suggestion to "Noble Paul" (Jira). The receipt says "Paul Noble".

## Not checked

- No builds, tests or Gradle runs (per the rules). Trial merges are textual only; whether the merged trees compile is unknown.
- Gated tree `7650584a756`: not in the repository, so its identity claim is unverified (disagreement 2).
- Live status of apache/solr PR 4829 and SOLR-18402: not checked (no network or `gh` calls).
- Jetty connect timeouts: not verified whether a Jetty connect timeout that the base name-based `isConnectException` check matched is now classified as unsent. No test covers it.
- SOLR-5220, which two of three tests fail on base: deduced from the test code (the 403 and 404 cases). The receipt gives only the count.
- SOLR-14298 "appears in the logs": based on the ticket's sample slow-request log. Solr's logging code was not read. "Solr does not act on it": verified only that the string appears in SolrJ sources at head `67e75ea1eea4`.
- SOLR-18341 Choice 3 cache time to live (60 seconds): read from the field default in code, not from a runtime test.
- SOLR-18341 Jira context was read from the main checkout's local JSON; JIRA was not called.
- Other parts' interactions (for example SOLR-17866 in `SolrRequest.java`, and `CloudSolrClient` with SOLR-14967) were out of this part.
