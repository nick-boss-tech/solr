# SOLR-6430 - docs only (nothing was compiled or run)

JIRA (2014, 4.9): sorting a `TrieDateField` ascending put a document with no value *between* a 1930 date and a 2000 date; the
reporter expected missing values lowest. The audit note said "TrieDate removed", but the behaviour carried over to the point fields.

## What main does
`FieldType.missingValue(...)` returns `null` when neither `sortMissingFirst` nor `sortMissingLast` is set, and
`getNumericSort` then builds a `SortField` with no `missingValue`. Lucene's numeric comparators use `0` for a missing value, so a
`DatePointField` document without a value sorts as `1970-01-01T00:00:00Z`, i.e. exactly between pre-1970 and later dates. Not a
bug in the comparator; the property table in the ref guide simply did not say it.

## Change
One sentence in the `sortMissingFirst`/`sortMissingLast` row of `field-type-definitions-and-properties.adoc`. No code change: making
missing values lowest by default would change sort order for every existing numeric field.

## Guesses to verify first
- The `0` default for all numeric point types (int, long, float, double, date) - read from `missingValue` and Lucene's
  `SortField` contract, not run. A quick `TestSortMissing`-style check with one doc per case would confirm it.
- The string statement (missing first ascending) is Lucene's `STRING_FIRST` default for a `SortField` without `missingValue`.

No test and no fail-before.
