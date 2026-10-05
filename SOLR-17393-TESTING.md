# SOLR-17393 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"Solr Suggester not working as expected for collection with multiple shards": with
`AnalyzingLookupFactory` + `DocumentDictionaryFactory`, a multi-shard collection returns
suggestions for `TEST` in an inconsistent order (the closest match is not on top, and the
order changes between suggester builds); a single shard behaves. No stack trace, no data.

## Bug mechanism (guessed)
`SuggestComponent.merge` collects the per-shard `LookupResult`s into a
`Lookup.LookupPriorityQueue`, which orders only by `value` (the weight). When no `weightField`
is configured every suggestion has the same weight, so the merged order is decided by the heap's
tie behavior and by the order in which shard responses are iterated, which varies from request
to request. A single shard returns the lookup's own order and is unaffected.

## Fix
`merge` sorts the combined results by weight (highest first), then by suggestion text, and keeps
the first `count`. `merge` became package-private so it can be unit tested.

## What the test pins
`SuggestComponentMergeTest`
- `testHigherWeightComesFirst`: weights still dominate across shards.
- `testEqualWeightsDoNotDependOnShardResponseOrder`: equal weights give the same list for three
  shard orderings.

## What was guessed / verify first
- That the reporter had equal weights (the ticket does not say). With real, distinct weights the
  merge was already correct and this branch changes nothing for them.
- That tie-breaking by text matches what users expect; a single shard orders ties by the
  lookup's own rules (for `AnalyzingLookup`, the analyzed form), so the order can still differ
  between one and many shards.
- The old code may pass the second test by luck; the fail-before check should be run with a few
  seeds. Revert only `SuggestComponent.java` to test it.
- Spotless formatting (line length of the new javadoc) and the `Comparator` generics.
