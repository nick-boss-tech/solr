# Streaming expressions round 1, part S6: SOLR-17433 and SOLR-17143 combined branch, and cross-suite landing order

Scope and method: read only. No builds, tests, `gh` writes, commits, pushes, or posts. Heads used are the origin heads named in the assignment. Repo-relative paths are at the stated commit in the Solr source tree. Receipt: `receipts/SOLR-17433.md`. JIRA packets (read only): `research/jira-context/SOLR-17433.json` and `SOLR-17143.json`.

**Local ref warning.** All seven local branch refs in this repo differ from origin. For example, the local `solr-17433-17143-submit` is `534d8129a10`, one local commit on an older base (the pre-reconcile version the receipt describes). Origin `42b6c4fc9158` is the gated head. The owner should confirm which fork ref is authoritative before any push.

## Part A: SOLR-17433 and SOLR-17143 (branch `solr-17433-17143-submit`, head `42b6c4fc9158`)

**Verdict: retire the combined branch as submitted. Do not ship it.** The 17433 symptom in the ticket is already gone for default clients on main. The 17143 constructors duplicate a route main already has. Nothing in the branch proves the runtime change.

**Head and diff.** Origin head `42b6c4fc9158` confirmed. The merge-base with `upstream/main` (`8e62c2686882`) is `14c7aac0d151`. The diff against the merge-base is 3 files, +55 and -8:
- `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/SolrClientCache.java`
- `solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/SolrClientCacheTest.java`
- `solr/solr-ref-guide/modules/query-guide/pages/streaming-expressions.adoc`

No changelog YAML is included. A trial merge onto `upstream/main` is clean, and main has not changed the two Java files since `14c7aac0d151`.

**File-by-file split.**
- `SolrClientCache.java` lines 168 to 169 (SOLR-17433 part): `builder.withRequestTimeout(Long.MAX_VALUE)` on every builder the cache creates. This is the only line that answers the ticket's "Total timeout 60000 ms elapsed".
- `SolrClientCache.java` lines 43 to 44 and 52 to 79 (SOLR-17143 part): `minConnTimeout` and `minSocketTimeout` become per-instance (they were static), plus new constructors `(int, int)` and `(HttpSolrClient, int, int)`, and `configuredMinTimeout`. This turns the ticket's suggested workaround (a custom cache with a higher timeout) into API.
- `SolrClientCacheTest.java` from line 96, `testCustomTimeoutFloorsAreApplied` (17143 part); from line 107, `testStreamingClientsDoNotInheritSeedRequestTimeout` (17433 part).
- `streaming-expressions.adoc` line 102 documents the custom cache, and calls the request timeout a "timeout floor", which is inaccurate for the 17433 change.

**What the cache change alters on main.**
- **Premise.** The ticket's 60-second symptom is a SolrJ 9.4 to 9.6 symptom (the ticket's Versions field). Commit `dd89cd604a6` (SOLR-17776, 2025-06-18) changed the cache's idle logic. It is in `releases/solr/10.0.0` and in no 9.x tag. Before it, 9.6.0 `SolrClientCache.java` lines 193 and 197 set `idleTimeout = minSocketTimeout` (60 s). Main, `SolrClientCache.java` lines 148 to 149, now uses `Math.max(minSocketTimeout, builder.getIdleTimeoutMillis())`.
- **Builder defaults.** An unset idle timeout returns `SolrHttpConstants.DEFAULT_SO_TIMEOUT` = 600000 (`SolrHttpConstants.java` line 23; `HttpSolrClient.java` lines 571 to 575). So on main the cache's default idle is 600 s, and the 60 s floor does not bind.
- **Request timeout.** An unset request timeout falls back to the idle timeout (`HttpSolrClient.java` lines 597 to 601). `HttpJettySolrClient.java` lines 648 to 649 apply idle and request timeouts to the Jetty request. So on main a default cache stream has a 600 s total cap, not 60 s. This is derived from the code and history. It was not run.
- **The unbounded timeout removes that 600 s cap** for every client built through `newHttpSolrClientBuilder`. That covers `SolrStream` (`SolrStream.java` line 136 default cache, line 307 `getHttpSolrClient`), `CloudSolrStream` (line 414 default cache), `TopicStream` (its own default cache, `TopicStream.java` lines 299 and 353 on the 10322 head), and the cache's cloud clients (`SolrClientCache.java` line 115 passes `newHttpSolrClientBuilder(null)` into `newCloudSolrClientBuilder`). `InternalSolrClientCache.java` line 103 falls through to the base builder, so on-demand cloud clients created inside a Solr node also lose the cap. Still bounded: stalled Jetty connections, by the 600 s idle timeout. Not affected: `HttpSolrClient`'s own default, `UpdateShardHandler` (it builds its own Jetty clients at lines 105 to 118; line 117 already uses `withRequestTimeout(Long.MAX_VALUE)`, which is a precedent), and SOLR-5754's update path (not built by the cache).
- **JDK path (a real risk).** The idle timeout is "not applicable to the JDK HttpClient" (`HttpSolrClient.java` lines 562 to 563). So on that path the request timeout is the only bound (`HttpJdkSolrClient.java` line 477). With the branch it becomes `Duration.of(Long.MAX_VALUE, ms)`, so a stalled request on the JDK path has no bound at all. The ticket's maintainers asked for exactly this JDK and Jetty consistency check (JIRA comments 18039401 and 18093321). The JDK client is used only when Jetty is not on the classpath.
- **Seeded caches (a behavior change).** `withHttpClient` copies the seed's request timeout (`HttpSolrClient.java` lines 470 to 471). The branch's override at line 169 runs after that copy, so a seed's explicit request timeout is silently discarded. The second new test asserts exactly that.
- **Floors.**
  - Default constructor: the same values as main (`configuredMinTimeout` reads the same properties and clamps to 60000). No change.
  - Connection floor: main clamps the connection timeout to at least 60 s (main `SolrClientCache.java` line 146). The new `(int, int)` constructors store the caller's values unclamped (branch lines 59 to 61 and 70 to 75), so a value below 60 s is accepted. "Floor" is a misnomer for those values.
  - Idle floor: on the default path it never binds, because the 600 s default is larger. It binds only for a seed with a lower idle timeout (the Jetty seed copy, `HttpJettySolrClient.java` lines 1056 to 1057), or when a caller passes a value. Through the new constructor a caller can raise the idle timeout above 600 s (the branch test uses 700000).
  - Consequence: a `socketTimeout` property below 600000 changes nothing on main or on the branch. The 17143 workaround in the ticket (`socketTimeout=120000`) is a no-op on main. The ref-guide sentence about `socketTimeout` already overstates this.
- **API change.** `minConnTimeout` and `minSocketTimeout` were `protected static final` (main lines 40 to 44). The only in-tree user is `InternalSolrClientCache.java` line 52, which still compiles. Any external subclass that reads them statically breaks.

**SOLR-17143 premise.** The ticket's trigger was a 120 s server connector idle timeout plus a client idle timeout. The client default on main is already 600 s, and the server connector is outside the cache. Michael Gibney's comment (17909417, 2025-01-02) says the real fix is a heartbeat, gated on SOLR-16367 and SOLR-17286. The seed route on main (`new SolrClientCache(seedClient)`, with a seed whose idle timeout is set) already gives the per-instance effect. So the constructors add API, not capability.

**Proof.** The gate is green at `42b6c4fc` (SolrClientCacheTest, hardened gate). The new tests check builder getters only. No test runs a live stream past any timeout, so the runtime change is unproven. This matches the receipt's "inconclusive by construction".

**Ticket context (17433).** Critical, open, labelled pull-request-available. Comment 18089737 (2026-06-17) says a contributor (Vishnu Priya) is picking it. Comments 18093087 and 18094112 propose a different fix: a default request timeout of -1 in `HttpSolrClient`, with a JDK guard in `decorateRequest`. David Smiley accepted that direction and said "PR welcome" (18039401, 18093321). The branch does neither.

**Options.**
1. **Retire the combined branch.** Evidence: the 60 s premise is stale on main (the real cap is 600 s). The branch's one behavior change is cache-only and differs from the maintainers' agreed direction. The 17143 constructors duplicate the seed route. There is no runtime proof and no changelog. Cost: 17433 stays open, and the unbounded total timeout it asks for is still undone.
2. **Ship as-is.** For: it removes the 600 s total cap the ticket asks about, the gate is green, and the seed override is tested. Against: the JDK path loses its only bound, seeded callers silently lose request timeouts, Solr's internal on-demand cloud clients change, public constructors accept unclamped values, the API changes from static to instance, there is no runtime proof and no changelog, and it overlaps a contributor's claimed approach.
3. **Split.** 17143 part: drop it (the seed route exists on main). 17433 part: a narrow fresh change limited to the request-timeout default (the ticket's -1 direction, or the single cache line), with a changelog and a runtime test. That is a new small branch, not this one. The 17433 ask is real and Critical, so a narrow fix has value. The proof gap cannot be closed inside this branch.

**Recommendation: option 1, retire the combined branch now.** If the owner wants the unbounded total timeout, open a narrow 17433 follow-up (option 3) and coordinate it with the contributor who claimed the ticket. Reason: on main the symptom in the ticket is already gone for default clients (600 s, not 60 s). The branch's extra surface (floors, constructors, seed override) is not what the maintainers asked for. Nothing in the branch proves the runtime change.

**Interactions with the client cache.** SOLR-10322's `TopicStream` and `CloudSolrStream` use default-constructed caches, so they would inherit the unbounded timeout if 17433 shipped. There is no code dependency, and 10322 does not need to wait for 17433. If 17433 is retired, nothing changes for 10322. If it ships, the 10322 proof was run on the pre-17433 cache, so a combined run is owed for whichever lands second.

## Part B: shared-suite pairs

Method: `git merge-tree --write-tree` on each head pair (a trial merge that writes no ref). Hunk-to-method mapping from `git diff -U0` against each head's merge-base. All six heads also trial-merge clean onto `upstream/main` (exit 0).

1. **StreamExpressionTest: SOLR-10322 (`80ce9d7a3c8a`) with SOLR-12657 (`16a0a69e8398`). CLEAN** (tree `d4c0098d3a98`). Overlapping methods: none. 10322 adds 24 lines inside `testSubFacetStream` (lines 2639 to 2662 on its head). 12657 adds 111 lines as two new methods, `testFacetStreamMinMaxOnDateField` (line 1423) and `testFacetStreamMinMaxOnDateFieldParallelRollup` (line 1469), inserted between `testRollupStdMetric` and `testFacetStream`. No import hunks on either side. No shared main file: 10322 touches `TopicStream.java`; 12657 touches `FacetStream.java` and the metrics classes.
   - **Landing order:** none required. Suggested: 12657 first (gate green at its exact live head), then 10322 (moved head, and an older base `97d973814336`; disclose both). If both land, the second one's StreamExpressionTest proof was run without the other. A combined run is a main-side item if the owner wants it.

2. **StreamDecoratorTest: SOLR-11922 (`da29a963236f`) with SOLR-12505 (`5f20e1171bc8`). CLEAN** (tree `f3def494b535`). Overlapping methods: none. 11922 adds one new test, `testParallelCartesianProductStream` (line 5483), between `assertLong` and `assertString`. Its diff touches no imports and does not call `fetch()`. 12505 adds two import lines (32 and 64) and one new test, `testFetchStreamWithNonLuceneDefaultDefType` (line 951, hunk 946 to 1007), before `testFetchStream` (line 1009). Among the six heads, only 12505 touches `FetchStream.java`.
   - **Landing order:** 12505 is the only gated ticket here and can land on its own. 11922 is held (NO GATE). If the owner keeps it, it can land after 12505 in either order. The 11922 branch also carries `SOLR-11922-TESTING.md` (23 lines), which must not ship in any PR.

3. **StreamingTest: SOLR-14200 (`10991af9bdac`) with SOLR-15326 (`dab6a4663169`). CONFLICTING** (exit 1; one conflict hunk in `StreamingTest.java`; tree `5294e31984a0`, read from the trial tree). Overlapping methods: none by name, and no existing method body is edited on either side. Both sides are pure insertions at the same base point (after base line 3241, following `testCloudStreamClientCache`). 14200 adds `testCloudStreamMissingManualShardMappingFailsClearly`. 15326 adds `testCloudStreamExportSortAddsUniqueKeyTieBreaker`. The conflict is only the adjacent insertion point. 14200's other two new tests (`testTupleStreamGetShardsWithMissingManualShardMapping` and `testTupleStreamGetShardsLocalFilterDoesNotMutateContextShards`, base line 3081) merge cleanly. No shared main file: 14200 touches `TupleStream`, `ParallelStream` and `SqlStream`; 15326 touches `CloudSolrStream`.
   - **Resolution:** keep both blocks.
   - **Landing order, if the owner keeps both:** 15326 first (one 22-line test; its change is the export tie-breaker, the only thing its settling proof pins). Then 14200 rebases and keeps both blocks. 14200 goes second because its `TupleStream.getShards` change has the wider blast radius and needs owner review. If either is retired, the conflict disappears.

Both 14200 and 15326 are retire candidates, so the order matters only if the owner keeps them. No gated ticket is blocked by a pair overlap.

## Owner decisions

- 17433 and 17143 combined branch: retire (recommended), ship, or split.
- Whether to open a narrow SOLR-17433 follow-up (request-timeout default only, with changelog and runtime proof), coordinated with the contributor who claimed the ticket.
- Drop the 17143 constructors (recommended, since the seed route already exists on main).
- If anything ships from this branch: accept the seeded-cache request-timeout override and the JDK-path unbounded timeout, or drop them.
- If 17433 ships, re-run the 10322 StreamExpressionTest proof on the combined tree.
- Keep or retire 11922 (held; its branch carries a TESTING note that must not ship).
- If 14200 and 15326 are both kept: land 15326 first, then 14200 rebased with both test blocks.
- Confirm the authoritative fork ref for all seven branches (local refs differ from origin).

## Not checked

- Runtime behavior. Whether Jetty's request timer or the JDK HttpClient timer behaves with `Long.MAX_VALUE` ms was not run; Jetty and JDK source are not in the workspace. The JDK-path finding comes from code and javadoc. `UpdateShardHandler` line 117 is the only in-tree precedent.
- Whether the 60 s symptom reproduces on main: derived from the code and commit history, not run.
- Whether a competing upstream PR for SOLR-17433 exists: no `gh` reads were run. The JIRA packet names no PR, though the ticket carries the pull-request-available label.
- Whether the StreamingTest merge compiles: only the trial merge and the conflict region were checked.
- Changelog files for the Part B tickets were not reviewed.
- Whether 12505's defType change affects any other suite beyond the diff stats (that is part S4's check).
- Interplay of 14200 and 15326 with the parallel and cloud machinery: left to part S5.
- The SOLR-5754 interaction is limited to the fact that `UpdateShardHandler` is not built by `SolrClientCache`.
