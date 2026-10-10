# Receipt refresh round 2, part b1 (SOLR-8051, SOLR-10305, SOLR-9124), re-check at the fetched heads

## Correction to the earlier pass

The earlier version of this file ran against stale local refs. It read the first-gate heads (55b24f64a8b, 0cf26e5f331, dfc2518214f) before the fetch, found the live heads and fix commits absent, and held all three tickets for that reason. That finding is withdrawn. This pass reads only the fetched refs:

| Ticket | Ref read | Head | Earlier pass read |
|---|---|---|---|
| SOLR-8051 | origin/solr-8051-submit | e50a2aa3437c | 55b24f64a8b (first gate) |
| SOLR-10305 | origin/solr-10305-submit | c43de86d4c13 | 0cf26e5f331 (first gate) |
| SOLR-9124 | origin/solr-9124-submit | 17a279d4dce1 | dfc2518214f (first gate) |

The plain local solr-<ticket>-submit branches are stale and were not used.

What changed:

1. All three live heads and their fix commits are present, and match the receipts. The 8051 null-check reorder, the 10305 prepare-time 400 and its test hook, and the 9124 grouped term-stats path are in the branch.
2. The verdicts stay HELD for all three, but the reasons changed. The code gaps the earlier pass reported are closed. What remains: text on the branch (changelog titles and test javadoc), one premise question (8051), one scope question (10305), and one proof-scope question (9124).
3. Earlier findings resolved: missing heads (F1 earlier), the 8051 reorder (earlier finding 2), the 10305 test hook (earlier finding 8), the 9124 grouped updateStats call (earlier finding 11). Earlier findings still open: the 8051 changelog and javadoc, the 10305 changelog and javadoc, the 9124 changelog. Earlier finding 9 (grouped 10305 requests) is answered below (F8). Earlier finding 12 (shard_i) still holds (F14). Earlier finding 13 (8051 and SOLR-15319 overlap) is carried, not re-checked (F15).

## Scope

Read only. Git reads (`show`, `diff`, `grep`, `ls-tree`) against origin/ refs, the receipts, and the earlier audit reports on origin/pr-prepare. No builds, no tests, no Gradle, no `gh` calls, nothing posted, nothing committed, pushed or fetched. No draft written.

## Findings at the fetched heads

### SOLR-8051

F1. NOTE, verified by reading. `solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java` at e50a2aa3437c: the null-body check (lines 117 to 122) now comes before the `res.getException()` check (lines 123 to 126). Commit e50a2aa3437 is a pure reorder, 4 insertions and 4 deletions in that one file (`git diff 55b24f64a8b e50a2aa3437`). At the base (cabedd1d968), `SolrResponse.getException()` (solrj `SolrResponse.java` lines 48 and 49) does `getResponse().get("exception")` with no null check, so a body-less response throws NullPointerException before the old null check ran. `ExactStatsCacheMergeTest.java` line 33 (`new QueryResponse()`, no exception) builds exactly that state. So the proof claim holds by reading: the test fails on base with NPE and the reorder makes it skip. Not run.

F2. FIX, changelog. `changelog/unreleased/SOLR-8051-exact-stats-missing-shard.yml`, lines 2 and 3: "if a shard returned no response". The fix covers a response with a null body and no exception. The no-server path (a shard with no URLs) sets a shard exception and does not reach that line (earlier audit `reports/search-components-4-s1.md` finding 3; the branch does not change `HttpShardHandler` or `SearchHandler`, so this still holds at head). Replacement, once owner decision 1 is answered and the claim is narrowed: `  ExactStatsCache no longer throws a NullPointerException when merging global stats for a shard response that has no body.`

F3. FIX, test javadoc. `solr/core/src/test/org/apache/solr/search/stats/ExactStatsCacheMergeTest.java` line 27: "A shard that never answered (no live replica)". The state built is a body-less response with no exception, not a no-replica shard, which carries an exception. Replacement: `/** A shard response with no body must not break the global stats merge. */`

F4. NOTE, premise (owner decision 1). No real request path that reaches the body-less, no-exception state is shown. The earlier audit traced `HttpShardHandler` lines 251 to 256 and 302 to 304 at cabedd1d968 and found none. The receipt's proof shows the guard works on that state. It does not show the ticket's symptom. Hold until Nick decides.

### SOLR-10305

F5. NOTE, verified by reading. The 400 check is in `QueryComponent.prepare` at `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` lines 148 to 152 at c43de86d4c13 (inside `if (rb.isDistrib)`). It came in with 578171295c8. The later commit c43de86d4c1 is test-only (5 insertions, `MinimalSchemaTest.java`), as the receipt says. The test hook sets `AllowListUrlChecker.ENABLE_URL_ALLOW_LIST` to "false" before `initCore`, the same pattern as `TestTolerantSearch.java` line 74 and `DistributedDebugComponentTest.java` line 61.

F6. FIX, changelog. `changelog/unreleased/SOLR-10305-distributed-query-requires-unique-key.yml`, line 2: "...instead of a NullPointerException while merging shard responses." The NPE is at the first statement of `createMainQuery` (head line 794; the dereference is head line 798, base line 793). `regularDistributedProcess` calls it at head line 651, at STAGE_EXECUTE_QUERY, before any shard request is answered. So "while merging shard responses" is wrong. Replacement: `  A distributed query (shards parameter) against a schema without a uniqueKey now fails with a 400 "Distributed search requires a uniqueKey field in the schema" instead of a NullPointerException.`

F7. FIX, test javadoc. `solr/core/src/test/org/apache/solr/MinimalSchemaTest.java` line 76: "...was a NullPointerException in mergeIds". The NPE is in `createMainQuery` (F6). Replacement: `/** SOLR-10305: a shards request on a schema without a uniqueKey must be a 400, not a NullPointerException */`

F8. NOTE, scope (owner decision 3). Two parts.
(a) Standalone with defaults: `HttpShardHandler.prepDistributed` (base lines 482 to 492, not changed by the branch) returns 403 for a shards request when there is no ZooKeeper, the URL allow list is enabled (the default), and no explicit list is set. `SearchHandler` runs `prepDistributed` before component `prepare`. So in that setup the request gets 403 before and after this change. The new 400 is reached only where the request passes that check (SolrCloud, allow list disabled, or an explicit list). The changelog is right for those cases, not for the default standalone case.
(b) Grouped requests (earlier finding 9, answered): the new check in `prepare` also covers grouped distributed requests. On base they already fail with NPE before any response, in `SearchGroupsRequestFactory.java` lines 77 to 79 (first-phase FL built from `getUniqueKeyField()` unconditionally, at STAGE_TOP_GROUPS). So the change turns an NPE into a 400 there too. It does not add a new rejection.

F9. NOTE, receipt wording, main side. `receipts/SOLR-10305.md`, the paragraph "The branch itself", says "instead of the NullPointerException that mergeIds produced". It should say "that createMainQuery produced (QueryComponent.java, base line 793), before any shard response". Flagged, not edited.

F10. NOTE, proof claims. `MinimalSchemaTest` has 4 `@Test` methods at c43de86d4c13 (3 on base plus the new one), as the receipt says. The `assertQEx` overload used (`solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java` line 1069) checks the code and that the message contains "uniqueKey", so the assertion is as strong as the receipt describes. Run counts and failure details: receipt only; the gate log is not on disk.

### SOLR-9124

F11. NOTE, verified by reading. `QueryComponent.java` at 17a279d4dce1: `groupedDistributedProcess` (line 600) sets `PURPOSE_SET_TERM_STATS` and calls `sendGlobalStats` for each shard request at the group stages (lines 631 to 634; the condition excludes STAGE_GET_FIELDS). `handleGroupedResponses` (line 681) calls `updateStats` at lines 682 to 684, the same call as `handleRegularResponses` (lines 705 to 707). Commit 17a279d4dce adds 4 lines to this file, as the receipt says. Commit 23a3674d8c3 changes the test hook to `group.field` "id" (1 insertion, 1 deletion). The code is present and matches the receipt.

F12. FIX, changelog. `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml`, line 2: "so group scores match the ungrouped ones." The added check, `TestBaseStatsCache.java` lines 79 to 94 (`checkGroupedScores`), compares the control grouped query with the sharded grouped query. It makes no ungrouped comparison. Replacement: `  ...so grouped scores on a sharded query match the same grouped query on one node.` (full title in F13).

F13. FIX, changelog, cache list. The title names "(ExactStatsCache, LRUStatsCache)". The receipt's recorded proof, with `QueryComponent.java` reverted, has exactly one failing class: `TestExactStatsCache`. `TestLRUStatsCache` and `TestExactSharedStatsCache` run the same inherited `checkGroupedScores` and pass on base, so the proof shows no change for LRU. `LRUStatsCache` extends `ExactStatsCache` (`LRUStatsCache.java` line 54), so the shared code path applies in code, but no recorded run shows it. Replacement, scoped to what the proof shows: `  Distributed grouped queries now send the merged global term statistics (ExactStatsCache) to the shards in the group stages, so grouped scores on a sharded query match the same grouped query on one node.`

F14. NOTE, receipt wording, main side. `receipts/SOLR-9124.md`, History, says `shard_i` "is multivalued in the distributed test schema". `solr/core/src/test-files/solr/collection1/conf/schema.xml` line 713 defines `*_i` as `<dynamicField name="*_i" type="int" indexed="true" stored="true"/>`, with no multiValued attribute (the multivalued one is `*_is`, line 715). The branch does not change the schema. So the stated reason is not supported. The real first-gate reason is not on disk. Flagged, not edited.

### Carried, not re-checked here

F15. SOLR-8051 and SOLR-15319 both change the same block of `ExactStatsCache.java` (earlier audit findings 6 to 7). Only the three fetched refs were read in this pass. Landing order is owner decision 2.

## Verdicts

**SOLR-8051: HELD (premise and text). Changed from the earlier pass: the code finding is resolved, the hold is not.** At e50a2aa3437c the null-body check sits above the exception check (F1). Held because: no real path to a body-less shard response is shown (F4, owner decision 1); the changelog title says "if a shard returned no response" (F2); and the test javadoc says "no live replica" (F3). Draftable once owner decision 1 is answered and the changelog and javadoc are fixed on the branch. Then draft with the head named in the Proof.

**SOLR-10305: HELD (text and scope). Changed: heads and the test hook are now verified, the hold is for text and scope.** At c43de86d4c13 the 400 check and the test hook are present (F5, F10). Held because: the changelog names the wrong NPE site (F6); the test javadoc names mergeIds (F7); the 403 comes first in the default standalone setup (F8a, owner decision 3). Draftable once F6 and F7 are fixed on the branch and owner decision 3 is answered. The earlier question about grouped requests is answered (F8b): no new rejection.

**SOLR-9124: HELD (one changelog title on the branch). Changed: the code is now verified, the hold is for text and proof scope.** At 17a279d4dce1 the grouped send and the grouped updateStats are present and match the receipt (F11). Held because the changelog overstates: the "ungrouped" comparison (F12) and the LRUStatsCache naming (F13). Draftable once the title is corrected on the branch (F13 replacement) and owner decision 4 is answered. The receipt's shard_i reason is wrong (F14) but does not block the PR text.

## Owner decisions (for Nick; not decided here)

1. SOLR-8051 premise. Either show a real request path that reaches a body-less shard response, with a reproduction, or narrow the claim to the merge step (F2, F3) and keep the ticket out of the first wave (F4).
2. SOLR-8051 and SOLR-15319 landing order. Whichever lands last keeps the null check above the exception check (F15).
3. SOLR-10305 scope. Accept the 400 as the fix for SolrCloud and allow-list-disabled setups, with the title in F6. Or narrow further. The default standalone case (F8a) is not fixed by this branch.
4. SOLR-9124 proof. Accept one discriminating class (`TestExactStatsCache`) with the title scoped to ExactStatsCache (F13). Or ask for a verify run of the other classes against base first, which needs a verify decision.

## Branch edits needed before any draft (none made, none committed)

- SOLR-8051: changelog title (F2, once narrowed) and `ExactStatsCacheMergeTest.java` line 27 (F3).
- SOLR-10305: changelog line 2 (F6) and `MinimalSchemaTest.java` line 76 (F7).
- SOLR-9124: changelog line 2 (F12 and F13).

These are text-only. The javadoc edits touch test files. The receipt rule is to rerun the focused proof when code or test setup changes. A comment-only edit is not setup, but say so in the receipt if it lands.

## Drafts

None written. No ticket is draftable at its fetched head, so there is nothing to name a head in.

## Not checked

- Builds, tests, Gradle and gate results. The gate logs (g8051-regate.log, g10305-regate.log, g9124-regate2.log) are not on disk. A search of the Solr-issues tree (depth 5, source excluded) found none. Proof counts and failure lists come from the receipts only.
- Changelog YAML parse. The three files were read; their shape is valid, but they were not parsed.
- Test compile. `StatsCache.mergeToGlobalStats(SolrQueryRequest, List<ShardResponse>)` is public (`StatsCache.java` line 174), and `ExactStatsCache` is a public class. The default constructor used in the 8051 test was not checked.
- SOLR-15319 head. Outside the three named refs; not read.
- Public text vocabulary check. No draft, so none run.
- The worktree holds other modified and untracked files (pr-drafts/solrcloud, other reports). They were not touched.
