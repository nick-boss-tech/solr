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
production: when the lone clause is `MUST` and its query is a `PostFilter`
(`org.apache.solr.search.PostFilter`), it is returned directly. (Round 4
review: the first version of this patch unwrapped for any occur, which dropped
the negation of a lone `-{!frange ...}` and returned the opposite result set;
`MUST_NOT` now stays wrapped.) A lone MUST is semantically "apply this
filter", identical to what the SHOULD path already produced. The same edit is
mirrored in the checked-in generated
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

Tests added (round 4, not compiled or run), in `TestCollapseQParserPlugin`:

1. `testCollapseFilterIsNotWrappedWhenRequired`: the ticket's
   `fq={!tag=collapse_tag}{!collapse field=group_s}` under `q.op=OR` and `AND`,
   and an explicit `+{!collapse ...}` under `OR`; each returns one doc per group.
2. `testNegatedPostFilterStaysNegated`: `{!frange}` positive and
   `-{!frange}` negated under both operators; the negated form must return the
   complement (guards the `MUST_NOT` regression found in review).

Still untested: facet exclusion through the tag, and multi-clause forms
(`q={!collapse ...} foo:bar` with `q.op=AND` still wraps; out of scope).

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
