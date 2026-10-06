# SOLR-6759 - hypothetical reproduction (nothing was compiled or run)

JIRA (2014): `ExpandComponent` never called `finish()` on the `DelegatingCollector` of a post filter, so a filter that holds
documents back until the end (an ACL filter shaped like collapse) produced an empty `expanded` section. The audit note
said "fixed in 4.10.5"; that was a bulk fix-version move. Since Lucene 8, `LeafCollector.finish()` is called by the searcher itself,
but Solr 9.4 added `DelegatingCollector.complete()` (a hook run once after the whole search, used e.g. by the block
collapse collectors in `AbstractBlockCollector.complete()` to emit the last group). `SolrIndexSearcher` calls it after every
search it runs (`getDocListNC` collector chain, `getDocSet`), but `ExpandComponent.process` runs
`searcher.search(query, collector)` itself and never did.

## Change
`ExpandComponent` calls `complete()` on the post filter head collector right after the search, before the groups are read.
New test `TestExpandComponent.testExpandCompletesPostFilterCollectors` with a test-only `{!completetracking}` post filter
(`CompleteTrackingQParserPlugin`, registered in `solrconfig-collapseqparser.xml`) that counts collectors created vs. completed.

## Guesses to verify first
- The main search and the expand search each build one collector (so `CREATED >= 2`); if the main path builds extra collectors
  that are not completed (e.g. a docset pass), the equality assertion needs loosening.
- `expand.fq` explicitly given with `{!collapse hint=block}` is the user-visible symptom (the last group missing); not covered, the
  tracking filter is the discriminating check.
- `fq={!completetracking}` is picked up by `rb.getFilters()` and reaches `getProcessedFilter` as a post filter (cost >= 100, no cache).

## Fail-before
Expected: the new test fails on `upstream/main` (`COMPLETED` is one less than `CREATED`). Enqueue with `-WithFailBefore`.
