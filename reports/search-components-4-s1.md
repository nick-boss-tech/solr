# Search components round 1, sub-batch 4, part s1 (SOLR-8051 and SOLR-15319)

Result: SOLR-15319 is draftable at 4bda91f46fc but held (landing order, changelog title, proof wording); SOLR-8051 is audit only and needs a FIX: its change does not stop the null-body NullPointerException it names, and its own test fails by reading.

Scope: read only. Git reads (`show`, `diff`, `grep`, `log`, `rev-parse`) against the remote-tracking refs, the receipts, and the Jira packets under `research/jira-context/` in the main checkout. No `gh` calls, no builds, no tests, nothing posted, nothing committed. Heads: SOLR-8051 live tip `55b24f64a8bd9cc29bdebd993530c8774f3695cd` (moved from the named `44588ce6719`); SOLR-15319 live tip `4bda91f46fc18142809c919d7c563129fdf7fb96` (matches the receipt).

## Findings

1. FIX. SOLR-8051. `solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java` at `55b24f64a8b`, line 117 (`if (res.getException() != null)`) runs before the new null check at lines 122-126. Evidence: `SolrResponse.getException()` (`solr/solrj/src/java/org/apache/solr/client/solrj/SolrResponse.java` lines 48-49, upstream main) does `getResponse().get("exception")` with no null check. `SimpleSolrResponse` (`HttpShardHandler.java` lines 165-190) does not override it, and neither do `QueryResponse.java` or `SolrResponseBase.java`. So a null body throws NullPointerException at line 117, and the new check at line 122 is never reached. `ExactStatsCacheMergeTest.java` lines 33-35 at `55b24f64a8b` builds exactly that state (`new QueryResponse()`, no exception), so the test fails with NullPointerException on its own branch. This is by reading; it was not run.
   Replacement for lines 116-129 of that file:
   ```java
         SolrResponse res = r.getSolrResponse();
         NamedList<Object> nl = res.getResponse();
         if (nl == null) {
           // no server answered for this shard (not all cores are up)
           log.debug("Empty response from shard={}", shard);
           continue;
         }
         if (res.getException() != null) {
           log.debug("Exception response={}", res);
           continue;
         }
         if (nl.get(ShardParams.SHARD_NAME) != null) {
           shard = (String) nl.get(ShardParams.SHARD_NAME);
         }
   ```
   Moving the check first is safe: `getException()` reads the same response, so a null body never carries an exception.

2. FIX. SOLR-8051 changelog. `changelog/unreleased/SOLR-8051-exact-stats-missing-shard.yml` (at `55b24f64a8b`) says ExactStatsCache "no longer throws a NullPointerException when merging global stats if a shard returned no response." Per finding 1, the code still throws there. Replacement: none yet. Keep the title out of any PR until finding 1 is fixed and a proof is recorded.

3. NOTE. SOLR-8051 premise. The Jira packet `research/jira-context/SOLR-8051.json` (a 2015 ticket, "NPE is thrown when not all cores are up") points at an old line, `nl.get(TERM_STATS_KEY)`, and a comment about "no server hosting shard". In the current code, `HttpShardHandler.java` lines 251-256 set a shard exception for a shard with no URLs and leave the body null. Traced paths: with `shards.tolerant=true`, `ExactStatsCache.java` line 102 skips that response (line 106 at the SOLR-15319 tip). With `shards.tolerant=false`, `SearchHandler.java` lines 646-654 collect the shard exception and throw before `updateStats` (`QueryComponent.java` line 786). So the no-server case does not reach line 117. The only route read is a ShardResponse with no exception and a null body, from `HttpShardHandler.java` lines 302-304 (`ssr.nl = rsp.getResponse()`). Nothing shows that happens. Replacement: none. Owner decision 2.

4. NOTE. SOLR-8051 moved head. `receipts/SOLR-8051.md` line 4 and the assignment name `44588ce6719e` as the live tip. The remote-tracking ref `origin/solr-8051-submit` is `55b24f64a8bd`, commit "SOLR-8051: remove handoff doc" (2026-10-10). The local `refs/heads/solr-8051-submit` is also stale at `44588ce6719e`. `git diff 44588ce6719e origin/solr-8051-submit` shows one file, `SOLR-8051-TESTING.md`, deleted (22 lines). The code, changelog, and test are identical between the two heads. Replacement for receipt line 4 (the receipt belongs to the main side, so this is flagged, not edited): `Head: 55b24f64a8bd9cc29bdebd993530c8774f3695cd (branch solr-8051-submit, live tip); 44588ce6719e is the prior head.`

5. NOTE. SOLR-8051 test scope. `ExactStatsCacheMergeTest.java` lines 27-35 has no assertion, and its javadoc says "A shard that never answered (no live replica)". The state it builds (a null body with no exception) is not one that the traced paths in finding 3 produce. A no-replica shard carries a shard exception. Replacement for the javadoc line: `/** A shard response with no body must not break the global stats merge. */`

6. FIX. SOLR-15319 has the same hazard. `ExactStatsCache.java` at `4bda91f46fc`: line 120 (`if (res.getException() != null)`) runs before the null body is checked, line 124 reads `nl`, and line 125 calls `perShardKey(nl, ...)`, which dereferences `nl` (line 320). A null body throws NullPointerException at line 120. The base has the same check at line 117 (`b5c71bc5573c`), so this is not new. The gate did not test a null body, so its proof does not cover it. Replacement for lines 119-125:
   ```java
         SolrResponse res = r.getSolrResponse();
         NamedList<Object> nl = res.getResponse();
         if (nl == null) {
           // no server answered for this shard
           log.debug("Empty response from shard={}", r.getShard());
           continue;
         }
         if (res.getException() != null) {
           log.debug("Exception response={}", res);
           continue;
         }
         String shard = perShardKey(nl, r.getShard());
   ```
   This changes the head, so it needs a fresh gate. Draft Limits names the gap until then. Owner decision 6.

7. NOTE. Composition and landing order, SOLR-8051 with SOLR-15319. Both change the same block of `ExactStatsCache.java`. Base lines 115-124 hold the shard-name block and `NamedList nl = res.getResponse()`. SOLR-8051 edits the shard-name block (base lines 121-123) and adds a null check. SOLR-15319 deletes the shard-name block and replaces `String shard = r.getShard()` (base line 115) with `perShardKey(nl, r.getShard())`. The two diffs rewrite the same lines, so a rebase will conflict. Recommended order: SOLR-8051 first, after finding 1 is fixed and has its own proof. Then rebase SOLR-15319 onto it, keep the null check above `res.getException()`, and run a fresh gate on the new head. If SOLR-15319 lands first, SOLR-8051 must be rewritten against the new block, with its null check moved above `res.getException()` again. Either order needs one textual merge. The null check has to sit above the exception check in whichever lands last.

8. FIX. SOLR-15319 changelog title. `changelog/unreleased/SOLR-15319.yml` says "ExactStatsCache and its subclasses now use the term statistics of every collection...". `ExactSharedStatsCache.java` line 37 and `LRUStatsCache.java` line 54 extend it. They use the same keys (`ExactSharedStatsCache.java` lines 66-68 and 86-88, `LRUStatsCache.java` lines 185-192), so the change does reach them, but no receipt covers their tests. The gate ran only `TestDistribIDF` and `TestExactStatsCacheLegacyResponse`. Subclass tests exist: `TestExactSharedStatsCache.java`, `TestLRUStatsCache.java`, `TestExactSharedStatsCacheCloud.java`, `TestLRUStatsCacheCloud.java`. Replacement title: `ExactStatsCache now uses the term statistics of every collection in a multi-collection query, instead of only those of shards whose name can be derived from the collection of the receiving core`. Keep "and its subclasses" only after a fresh run of those four classes at the new head. A title edit moves the head.

9. FIX. SOLR-15319 proof wording. `receipts/SOLR-15319.md` line 7 records a failing run only for `TestExactStatsCacheLegacyResponse` (1 of 3 fails, `testLegacyResponsesWithSameShardNameKeyApart`), "against the received code with the old bare-name fallback". No failing run is recorded for `TestDistribIDF.testMultiCollectionUsesStatsOfEveryCollection` (`TestDistribIDF.java` line 189). The receipt also shows no run against upstream main. The draft's Proof therefore says only what is recorded: the counts pass at the head, and the legacy test fails on the old key. Add "fails without this change" to the multi-collection test only after a run is recorded.

10. NOTE, verified, no change needed. The response-side key and the lookup key match. `ShardResponse.shard` is the `rb.shards` element (`HttpShardHandler.java` line 225, called from the submit loop, `SearchHandler.java` lines 554-564, with `actualShards = rb.shards`). The lookup at `ExactStatsCache.java` line 269 uses the same string, including replica lists joined by `|` (`HttpShardHandler.java` line 571). `doSendGlobalStats` skips a missing key with `continue` (lines 272-275), so a shard with no stats adds nothing and does not throw.

11. NOTE. SOLR-15319 dead code. `StatsUtil.shardUrlToShard` (`StatsUtil.java` line 55 at `4bda91f46fc`) has no callers. It stays in place. Removing it changes the head.

12. NOTE. SOLR-15319 GitHub runs. `receipts/SOLR-15319.md` line 8 names runs 37683158242 and 37683278146. Their conclusions are not in the record. The draft cites neither.

13. NOTE. Lucene. Neither draft names Lucene behavior, so the 9.x and 10.x check does not apply.

14. NOTE. SOLR-15319 draft. The draft is at `pr-drafts/search-components/SOLR-15319.md`. Its PR text is about 4,100 bytes, mostly link URLs; the visible text is about 3,000 characters. It has no em or en dash and no process words above the separator. The reviewer notes sit below a separator line and must be removed before pasting.

## Task results

- SOLR-8051 (audit only, not drafted): FIX before any proof. The live tip moved from `44588ce6719` to `55b24f64a8b` (finding 4). The code, changelog, and test are unchanged, and only a handoff doc was removed. The change does not stop the NullPointerException it names (finding 1), so the changelog claim is false (finding 2). The Jira case is not reproduced on the traced paths (finding 3). Its test has no assertion and builds a state the traced paths do not produce (finding 5). Owner decision 2.

- SOLR-15319 (draftable at `4bda91f46fc`, held): the draft is at `pr-drafts/search-components/SOLR-15319.md`. Holds: landing order with SOLR-8051 (finding 7), the changelog title (finding 8), and proof wording limited to the recorded runs (finding 9). The receipt's counts match the test files (4 and 3 `@Test` methods). The response-side and lookup keys match (finding 10). The null-body gap stays until a guard is added with a fresh gate (finding 6); the draft names it in Limits.

## Owner decisions

1. Landing order: SOLR-8051 first (after its fix and its own proof), then SOLR-15319 rebased and re-gated; or SOLR-15319 first with the null guard added and re-gated. Either way, whichever lands second gets a new head.
2. SOLR-8051: fix and prove the null-body path, or close as not reproduced. The Jira case looks handled in tolerant and non-tolerant modes on the traced paths. Any claim needs a reproduction through HTTP.
3. SOLR-15319 changelog: run the four subclass test classes at a new head and keep "and its subclasses", or narrow the title (finding 8).
4. SOLR-15319 proof: run a failing check for `testMultiCollectionUsesStatsOfEveryCollection` before opening, or leave that claim out (finding 9). A run needs your verify decision; drain is denied to me.
5. SOLR-15319 choice section: confirm the draft's choice (collection and shard name keys with a URL fallback, versus keying every shard by URL). The receipt records no alternative; the choice is this reviewer's proposal.
6. SOLR-15319 null body: decide whether the guard in finding 6 goes into this PR. Without it, the PR's Limits must keep naming the gap.

## Not checked

- No builds, tests, or gate runs. The 8051 test failure is from reading `SolrResponse.getException()`, not from a run.
- Gate logs named in the receipt (`g15319r35-gate.log`) are not in this worktree. Proof numbers come only from `receipts/SOLR-15319.md`.
- No `gh` calls at all, so no live PR state for either ticket. Neither ticket has a PR in the records read.
- Remote heads come from the existing remote-tracking refs. I did not run `ls-remote` or `fetch`, so I could not re-confirm the claim's 2026-10-09 values.
- The 15319 GitHub run conclusions (not in the record).
- Mixed-version behavior: whether an older coordinator ignores the new `shard.collection` key.
- Whether `LBSolrClient` can return a null body with no error. `Rsp.getResponse()` was read only as an accessor (`LBSolrClient.java` line 498).
- The non-tolerant path was read to `SearchHandler.java` lines 646-654, not in full.
- The bodies of the subclass tests and of the base `TestDistribIDF` file beyond the counts.
- SOLR-8051 changelog YAML parse: not gated and not parsed.
- Lucene: no Lucene behavior is named in either draft, so no 9.x or 10.x check was made.
