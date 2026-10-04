# SOLR-12871 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12871 - "sort=childfield(currency_field) desc fails with exception about REWRITABLE field type" (reported against 6.6 by Mikhail Khludnev). The old skip note said "real fix needs rewrite-API/Lucene design decision; bug persists on main". Reopened in audit round audit-1 (Tier 1 batch 15).
- Branch: `solr-12871-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`ChildFieldValueSourceParser.BlockJoinSortFieldValueSource.getSortField` takes the child field's `SortField` type and hands it to Lucene's `ToParentBlockJoinSortField`, whose constructor throws `UnsupportedOperationException: Sort type REWRITEABLE is not supported` for fields whose Solr sort is a rewritable one (the ticket's example is `CurrencyFieldType`). The user sees an opaque 500-style failure from deep in Lucene.

## Design choice and why
The ticket's own wish (support functions/currency through rewriting underneath the sort field) is a larger feature and is not attempted. The ticket also says "at least it's good to start documenting the workaround". This branch delivers the small, safe part: reject the unsupported field type up front, at sort-parse time, with a message naming the field and its type. The check lives in `parse`, next to the existing "field not found" `SyntaxError`, so it produces the same kind of error as the other `childfield()` argument mistakes.

## What the branch changes
- `ChildFieldValueSourceParser.parse`: after resolving the `SchemaField`, `sf.getSortField(false).getType() == Type.REWRITEABLE` raises a `SyntaxError`.
- Test: `TestNestedDocsSort.testRewriteableSortFieldTypeIsRejected` parses `childfield(amount,$q) desc` (`amount` is a `CurrencyFieldType` field in `schema.xml`) and expects a `SolrException` with code 400.

## Guesses to verify first
1. `CurrencyFieldType.getSortField` really reports `Type.REWRITEABLE` (via `ValueSource.getSortField`) and has no side effects when called at parse time.
2. `SortSpecParsing` wraps the `SyntaxError` into a `SolrException` with `BAD_REQUEST` (the existing `testAbsentField` only checks for `SolrException`).
3. Other field types that produce `REWRITEABLE` sorts (for example `ExternalFileField`) are rejected the same way, which is intended.
4. Ref guide: the `childfield()` section may deserve a sentence about the unsupported field types; not edited here.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.join.TestNestedDocsSort"
```
Fail-before: remove the new check; `testRewriteableSortFieldTypeIsRejected` should then see the Lucene exception (not a 400) or no exception, because the Lucene constructor is only reached from `getSortField(boolean)`, which `parseSortSpec` calls for the function sort (if it does not fail there, the test would need to call `getSortField` on the parsed value source).

## Not done
No JIRA comment, no PR.
