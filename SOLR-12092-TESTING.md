# SOLR-12092 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12092 - "Edismax - Stopwords - Should exclude ManageStopFilterFactory" (Manish, 6.6/7.2). One-sentence ticket, no comments.
- Branch: `solr-12092-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test + test schema, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
With `defType=edismax&stopwords=false`, `ExtendedDismaxQParser.ExtendedSolrQueryParser.noStopwordFilterAnalyzer` strips the query-time stop filter from a field's analyzer, but it only recognises
`org.apache.lucene.analysis.core.StopFilterFactory`. `ManagedStopFilterFactory` (REST-managed stopwords) is a separate class (`BaseManagedTokenFilterFactory` subclass), so it was never removed.

## What the branch changes
- `ExtendedDismaxQParser`: new `isStopFilter(TokenFilterFactory)` (StopFilterFactory or ManagedStopFilterFactory), used in both places the method checked `instanceof StopFilterFactory`
  (the "stop filter in the indexer" guard and the removal loop).
- `schema-rest.xml`: new fieldType `managed_en_query_stop` (index analyzer: tokenizer only; query analyzer: tokenizer + `ManagedStopFilterFactory managed="english"`).
- `TestManagedStopFilterFactory.testEdismaxStopwordsFalseKeepsManagedStopwords`: PUT stopwords a/an/the, reload, add a field of that type, index "one", query `the one` with `mm=100%`.
  Default -> 1 hit ("the" dropped); `stopwords=false` -> 0 hits ("the" kept and required).

## What was guessed (verify these first)
1. **Compile**: `ManagedStopFilterFactory` is public with a public no-arg-map constructor in `org.apache.solr.rest.schema.analysis`; import placed between `request` and `schema`. Spotless ordering assumed.
2. **Test flow**: copied the add-field / reload steps from `testManagedStopwords`. Whether the managed word set is live for the new field type right after `restTestHarness.reload()` (the existing test reloads once more after deleting a word) is unverified.
3. **Expected counts**: assumes edismax `mm=100%` is honored with `qf` on a single field and that `the` kept means the clause `the` is required. If edismax falls back to "all stopwords" handling, the second assertion could differ.
4. **Request URL**: the query is a raw `/select?` string with `%25`/`%20` escapes, as in sibling tests; `assertQ(String, String...)` is the helper those tests use.
5. **Scope**: other stop-like factories (e.g. `SuggestStopFilterFactory`) are not covered, matching the ticket.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.rest.schema.analysis.TestManagedStopFilterFactory" --tests "org.apache.solr.search.TestExtendedDismaxParser"
```
Fail-before: revert only `ExtendedDismaxQParser.java`; the `stopwords=false` assertion should fail with numFound 1.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
