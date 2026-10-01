# SOLR-17796 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no javacc regeneration, no tests). A reviewer must compile and
test before this goes anywhere near a PR.

## What the patch does

`fq={!tag=t}{!collapse field=x}` throws
`UnsupportedOperationException: ... does not implement createWeight` when
`q.op=AND`, but works with `q.op=OR`. Root cause: with the AND default
operator, the lone collapse clause gets `Occur.MUST`; the single-clause
unwrap in the query parser only unwrapped SHOULD, so the `CollapsingPostFilter`
ended up wrapped in a `BooleanQuery`, which bypassed the post-filter routing
in `SolrIndexSearcher.getProcessedFilter` and forced Weight-based evaluation
that PostFilters cannot do.

The patch extends the single-clause unwrap in `QueryParser.jj`'s `Query()`
production: when the lone clause's query is a `PostFilter`
(`org.apache.solr.search.PostFilter`), it is returned directly for ANY occur
(MUST/SHOULD/MUST_NOT is untouched — only the single-clause path). A lone
MUST is semantically "apply this filter", identical to what the SHOULD path
already produced. The same edit is mirrored in the checked-in generated
`QueryParser.java` (see below). Non-PostFilter single clauses are completely
unchanged.

Files changed:
- `solr/core/src/java/org/apache/solr/parser/QueryParser.jj`
- `solr/core/src/java/org/apache/solr/parser/QueryParser.java` (generated)

Also fixed as drive-by in the touched hunk: the `.jj` used stale
`getOccur()`/`getQuery()` accessors (Lucene 10 renamed these to
`occur()`/`query()`); the generated file already used the new names. Now
consistent.

## Regeneration note (important)

The `javacc` Gradle task regenerates `QueryParser.java` from the `.jj`
(`gradlew javacc`), but it is NOT wired into `compileJava` — the checked-in
generated file is what compiles. Both files were hand-edited to match. The
reviewer should run the javacc task and diff to confirm the generated output
matches the hand edit (modulo the task's standard cleanups).

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:javacc
git diff --stat   # confirm regeneration matches the hand edit
~/workspace/tools/solr-gradle.sh :solr:core:spotlessApply
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
~/workspace/tools/solr-gradle.sh :solr:core:test \
  --tests "org.apache.solr.search.TestCollapseQParserPlugin" \
  -Pvalidation.errorprone=true
```

Suggested new tests (not written):

1. Parser-level: parse `{!collapse field=<single-valued string w/ docValues>}`
   via `LuceneQParser` with `q.op=AND` → assert the result is the bare
   `CollapsingPostFilter`, not a `BooleanQuery`; same with `q.op=OR`
   (unchanged behavior).
2. Same for `{!frange ...}` (`FunctionRangeQuery`, also a PostFilter) with
   `q.op=AND`.
3. Integration: the ticket's repro — `fq={!tag=t}{!collapse field=...}`
   with `q.op=AND` returns 200 with collapsed results; facet exclusion via
   the tag still works.

## Patch limits and risks

- **Not compiled or tested.** The javacc `{if ("" != null) return ...;}`
  wrapping in the generated file was done by hand — regeneration must confirm
  it.
- The fix deliberately does NOT implement `createWeight` on
  `CollapsingPostFilter`: a PostFilter filters relative to a base DocSet, and
  Weight evaluation has no base set — collapsing over all docs then
  intersecting would pick different group heads (silently wrong results).
- Known remaining edge (out of scope): multiple clauses with `q.op=AND` where
  one is a collapse sub-query (e.g. `q={!collapse field=x} foo:bar`) still
  wraps the PostFilter in a BooleanQuery and hits the same UOE. That needs a
  broader design decision about PostFilters inside BooleanQueries.
- Only the three `PostFilter` implementations are affected by the unwrap:
  `CollapsingPostFilter`, `AnalyticsQuery`, `FunctionRangeQuery`. All route
  through `getProcessedFilter`'s post-filter path when bare.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
