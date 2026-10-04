# SOLR-12608 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses. This ticket was first skipped for lack of repro data and revisited because the user wanted a guessed fix and test.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12608 - "Edismax: Out of memory error with a query full of *." (Federico Grillini, 7.2.1/7.4, no comments). `q=********` with edismax and faceting made the node run out of memory; about 4888 `*` were enough.
- Branch: `solr-12608-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood (hypothesis)
A term made only of `*` characters reaches `SolrQueryParserBase.getWildcardQuery`; with more than a single `*` it is not recognized as match-all and is turned into a `WildcardQuery`/automaton whose size grows with the number of stars. Lucene added `determinizeWorkLimit` (default 10,000) in 7.3 which may already make this fail fast with `TooComplexToDeterminizeException`; I could not check without running it. Runs of unescaped `*` are equivalent to one `*`, so they can be collapsed before anything is built.

## What the branch changes
- `SolrQueryParserBase.collapseRepeatedWildcards` (public static, escape aware) applied at the top of `getWildcardQuery`; after collapsing, `*` alone takes the existing match-all / existence-query path.
- `TestSolrQueryParser.testRepeatedWildcardsAreCollapsed`: unit assertions for the helper (including escaped stars) plus a 5000-star query with `lucene` and `edismax` that must complete with status 0.

## What was guessed (verify these first)
1. The OOM comes from the wildcard automaton and not from elsewhere (e.g. facet code, `debugQuery` rendering, or edismax's clause splitting/escaping creating a huge string per clause). If edismax rewrites the term before `getWildcardQuery`, the fix has no effect.
2. A test that only asserts "does not fail" may pass on main already if Lucene's work limit kicks in; the helper assertions are the stricter part.
3. `v_t` exists as a field in the schema used by `TestSolrQueryParser` (used by other tests in the class) and `qf=v_t` is valid for edismax.
4. Other wildcard-ish entry points (`getPrefixQuery`, `getRegexpQuery`, edismax's own `getWildcardQuery` override with its `val.equals("*")` check) were not changed.
5. Semantics: collapsing is only valid because `*` has no ordinal meaning; `?` runs are left alone.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser"
```
Fail-before: revert only `SolrQueryParserBase.java`; the helper tests will not compile (new method), so for a behavioral fail-before check only the 5000-star requests.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
