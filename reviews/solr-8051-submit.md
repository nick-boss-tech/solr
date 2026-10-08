# solr-8051-submit

- Branch: origin/solr-8051-submit
- Head: 44588ce6719e (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +76/-3. `solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java` (read `nl = res.getResponse()` before the `SHARD_NAME` lookup; `continue` when `nl` is null), `solr/core/src/test/org/apache/solr/search/stats/ExactStatsCacheMergeTest.java` (new, one method, no assertions, no `@Test`), changelog `changelog/unreleased/SOLR-8051-exact-stats-missing-shard.yml` (`type: fixed`, author Nick Shanin), and `SOLR-8051-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none applied. One proposed fix (finding 2) needs write access to `wt\SOLR-8051`, which the approved permission rule does not cover.

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-8051-TESTING.md` is labeled "hypothetical reproduction (nothing was compiled or run)" and is treated as unverified.

## Findings (ranked)

1. **Owner call A (see below). Silent skip for a null response.** `ExactStatsCache.java:121-123` already skips a response with an exception in every mode, without checking `shards.tolerant`. The branch extends the same skip to a null response (`ExactStatsCache.java:122-127`). The debug log names the right shard, since `shard` is set per iteration from `r.getShard()`. Whether a null response with no exception can happen when `shards.tolerant=false` was not traced in `HttpShardHandler`. If it can, the global stats silently exclude that shard instead of failing the request.

2. **LOW, verified convention note.** `ExactStatsCacheMergeTest.java:30` declares `public void testShardWithoutResponseIsSkipped()` with no `@Test`. Every other test method in the same package uses `@Test` (`TestDefaultStatsCache.java:44`, `TestDistribIDF.java:68,140,258`, `TestExactStatsCache.java:36`). `TestSlowCompositeReaderWrapper.java` on `main` has two unannotated `test*` methods (lines 45 and 104), so this codebase does run unannotated methods; the omission is most likely harmless. Correction: an earlier draft of this review rated it MEDIUM as a possible silent skip. Proposed fix, for consistency with the package: add `import org.junit.Test;` and `@Test` on the method. Not applied (see the Patch line above).

3. **Verified (checked, no issue). The helpers used by the test exist with the signatures it uses.** `SolrQueryRequestBase(SolrCore, SolrParams)` is public and the class is concrete (`SolrQueryRequestBase.java:47,71`), so the anonymous `{}` subclass compiles. `ShardResponse` has no explicit constructor, so the default is public, and `setSolrResponse(SolrResponse)` exists (`ShardResponse.java:73`). `QueryResponse()` is public and empty (`QueryResponse.java:104`). `ExactStatsCache` has no explicit constructor. `StatsCache.mergeToGlobalStats(SolrQueryRequest, List<ShardResponse>)` is public (`StatsCache.java:174`).

4. **Verified (checked, no issue). Fail-before on `main` is a real error.** On `main` the loop dereferences `res.getResponse().get(SHARD_NAME)` (`ExactStatsCache.java:121-122` before the branch), which throws `NullPointerException` for the test's `QueryResponse()`. The failure is the behaviour under test, not a missing API.

5. **Verified (checked, no issue). Order inside the loop.** The exception check comes before `nl` is read, and `nl` is read before any of its keys. The `TERM_STATS_KEY`, `TERMS_KEY` and `COL_STATS_KEY` reads that follow run only on a non-null `nl`.

## Owner calls (not decided here)

- **A. Skip vs fail for a null response.** If `nl == null` can occur with `shards.tolerant=false`, should the merge skip that shard (current branch, same as exception responses today) or fail the request? Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before run, no Spotless, no Error Prone.
- Whether `HttpShardHandler` can produce a response with a null body and no exception when `shards.tolerant=false` (finding 1 and owner call A).
- Whether the Solr randomized runner runs unannotated `test*` methods. Finding 2 cites `main` evidence that it does, but the runner itself was not read.
- Whether `QueryResponse()` leaves the response `NamedList` null (the field default was not traced in `SolrResponseBase`).
- `SOLR-8051-TESTING.md` is treated as unverified. Left in place.
