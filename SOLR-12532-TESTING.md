# SOLR-12532 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12532 - "Slop specified in query string is not preserved for certain phrase searches" (Brad Sumersford, 7.4). The ticket comments (Michael Gibney, Steve Rowe) mention an
  existing patch/PR; **check the ticket for it before using this branch** - it may be a better or conflicting implementation.
- Branch: `solr-12532-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test + test schema, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12532.md` in the Solr-issues workspace

## The bug, as understood
`SolrQueryParserBase.getFieldQuery(field, text, slop)` re-applied the query-string slop only to a top-level `PhraseQuery` / `MultiPhraseQuery`. When the field's query analyzer produces a token graph
(e.g. `WordDelimiterGraphFilter` with `preserveOriginal=1 generateWordParts=1` on `can't` -> `can't | can t`), Lucene builds a `SpanNearQuery` (or a boolean of phrase queries) instead, and its slop stayed 0.
So `wdf_partspreserve:"you can't"~2` found nothing for the text "you just can't". SOLR-12243 fixed the `ps` (edismax) case only.

## What the branch changes
- `SolrQueryParserBase`: extracted the slop logic into `applySlop(Query, int)`, which additionally handles `SpanNearQuery` (rebuilt with same clauses/order and the new slop) and recurses through `BooleanQuery` clauses.
- `schema12.xml` (core test conf): new fieldType `wdf_partspreserve` (query + index WDGF `generateWordParts=1 generateNumberParts=1 preserveOriginal=1`) and a `wdf_partspreserve` field.
- `TestSolrQueryParser.testQueryStringSlopOnGraphPhrase`: indexes id 41 "you just can't"; expects `"you can't"~2` -> 1 hit and `"you can't"` (slop 0) -> 0 hits.

## What was guessed (verify these first)
1. **Query shape**: I did not confirm which Query class Lucene returns for this analysis (SpanNearQuery vs BooleanQuery of PhraseQuery vs something else, e.g. a graph-specific wrapper). If it is another type, `applySlop` will not fire and the test still fails.
   Print `debug=query` `parsedquery` on base to see. Also confirm whether, in a `SpanNearQuery`, nested sub-spans (`SpanOrQuery`) must keep slop 0 (this change only touches the outermost span).
2. **Expected hit counts**: with `MockTokenizer` + preserveOriginal the indexed positions of "you just can't" may differ from my mental model (e.g. `can't` and `can`/`t` overlap, positions 2). If slop 1 already matches, the control assertion (slop 0 -> 0 hits) may be wrong.
3. **Shared schema**: `schema12.xml` is used by many core tests; adding a type/field should be inert, but tests that enumerate fields could notice. Doc id `41` may clash with other docs in `TestSolrQueryParser`.
4. Boolean recursion applies the slop to every phrase-like sub-query of a boolean; this is intended for SHOULD-of-paths but could change behavior for synonym expansions on quoted terms. Review.
5. Edismax (`ExtendedSolrQueryParser`) has its own slop handling (SOLR-12243); not touched.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser"
```
Fail-before: revert only `SolrQueryParserBase.java`; `testQueryStringSlopOnGraphPhrase` should fail on the first assertion (0 hits instead of 1).

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
