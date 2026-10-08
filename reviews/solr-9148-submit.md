# solr-9148-submit

- Branch: origin/solr-9148-submit
- Head: 30f0d7a42d50 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits, 5 files (+93/-2). `SQLHandler.java` (+8: copies each repeated `fq` into a `solr.sql.fq.N` property, lines 133-139), `SolrTable.java` (+30/-2: `addFilterQueries` at line 123 and five call sites), `TestSQLHandler.java` (+45), `sql-query.adoc` (+4, a note on `fq`), changelog `SOLR-9148-sql-filter-queries.yml` (+8, type `added`)
- Verdict: Close (every query builder receives the filter queries. The gap is one untested path.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-9148-review.md` (verdict Close) was written at snapshot `f8758ebe763`, an ancestor of the head. The delta (`git diff f8758ebe763 30f0d7a42d50`) is the ref-guide note only (`sql-query.adoc`, +4). The code is unchanged since the snapshot, so the round-28 code analysis still applies.

## Verified code facts

- `SolrTable.java` builds `ModifiableSolrParams` at five sites (lines 311, 540, 661, 801, 895). Each is followed by `addFilterQueries(…, properties)` at lines 313, 544, 663, 805 and 897, so no query path misses `fq`.
- The five paths are select (`handleSelect`), group-by map-reduce (`handleGroupByMapReduce`), group-by facet (`handleGroupByFacet`), select-distinct map-reduce (`handleSelectDistinctMapReduce`, line 731) and stats (`handleStats`).
- The `fq` values come only from the request's `fq` parameter (`SQLHandler`). The internal `solr.sql.fq.N` names are written only from those values, so no other request input can set them.

## Findings (ranked)

1. **LOW, verified. Coverage of the query paths.** All five builders receive the filter queries (see the facts above). No path is missing.

2. **LOW, proof. One path has no test I could see.** The test diff covers repeated `fq` on select, stats, facet and group-by map-reduce. I did not find a test through `handleSelectDistinctMapReduce` (line 731, call at 805). Add one, since that is the path the diff does not exercise.

3. **LOW, verified. The handler does not enforce access.** `fq` is client-supplied. The handler passes the filters through and does not check them. An access filter holds only if the layer in front of Solr sets `fq`, the same as for `/select`. The new ref-guide sentence is accurate and needs no change.

4. **LOW, hypothesis. The JDBC sentence.** The ref guide says "The JDBC driver cannot set filter queries, since it has no request parameters." The JDBC client code I could read (under `solrj-streaming`'s `io/sql`) has no `fq` handling, which agrees with the sentence. I did not check whether connection-URL properties reach the handler as request parameters, so the claim is not verified end to end.

## Owner calls (not decided here)

- None blocking.

## Interactions with other branches

- None. This branch shares no code with the eDisMax, update-processor or grouping branches.

## Not checked

- Not compiled, formatted, or run.
- The `/export` and `numWorkers` branches were checked for parameter building only, not for runtime behaviour.
- Whether JDBC connection properties can reach the handler as request parameters (finding 4).
- Spotless was not run.
- No GitHub or JIRA writes.
