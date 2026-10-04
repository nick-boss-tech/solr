# SOLR-9048 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9048 - "{!parent } {!child } throws NPE if underneath query parser yields no clauses" (Mikhail Khludnev). Example: `{!child of=inStock:true}{!field f=title_en v='and'}` with `and` a stop word; the original stack was an NPE in `ToChildBlockJoinQuery.hashCode`.
- Branch: `solr-9048-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`FiltersQParser.parseImpl` (base of the block join parsers) adds `clause.getKey().getQuery()` to a `BooleanQuery.Builder` without a null check. A sub parser that analyzes everything away returns `null`. On main this surfaces as an NPE in `BooleanQuery.Builder.add` (the old `hashCode` symptom is gone but the NPE remains). Code path re-read on main: `BlockJoinParentQParser`/`BlockJoinChildQParser` already treat an empty clause list as "all children / all parents".

## What the branch changes
- `FiltersQParser.parseImpl`: skip clauses whose sub query is `null`. The result is then the existing "no clauses" behavior (same as `{!parent which=...}` with no query).
- New `BJQParserTest.testSubQueryWithoutAnyTermsAfterAnalysis`: `{!parent which=... v=$sub}` and `{!child of=... v=$sub}` with `sub={!field f=subject v=and}`.

## What was guessed (verify these first)
1. `{!field f=subject v=and}` really returns a `null` query in `schema15.xml` (`subject` is a `text` field with `StopFilterFactory`, `and` in the default stop set). If `{!field}` returns an empty `BooleanQuery`/`MatchNoDocs` instead, the test passes without the fix.
2. Semantics: skipping a null clause means "no constraint" (all parents / all children). One could argue an all-stopword query should match nothing; kept consistent with `FiltersQParser.parse`, which returns `MatchAllDocsQuery` for no clauses.
3. Expected counts: 6 parents; `klm.length` children for one parent via `fq` (copied from `testJustParentsFilterInChild`).
4. `{!filters}` and other `FiltersQParser` subclasses also change behavior for null sub queries (previously NPE).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.join.BJQParserTest"
```
Fail-before: revert only `FiltersQParser.java`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
