# SOLR-2632 testing notes (hypothetical reproduction, nothing was compiled or run)

Ticket (2011): `q=+inStock:true +_query_:"{!boost b=... v=$qq defType=dismax}"` returns empty highlighting;
`q={!boost ...}` alone works because `BoostQParserPlugin.getHighlightQuery()` delegates to the base parser.
When the boost is nested in a boolean query the highlighter receives the whole `BooleanQuery`, whose
clause is now a `FunctionScoreQuery` (formerly `BoostedQuery`). Mark Miller's 2011 comment: the span scorer
needs to unwrap it.

## Change
`DefaultSolrHighlighter`: `CustomSpanTermExtractor.extract` (original highlighter) and the anonymous
`FieldQuery.flatten` (FastVectorHighlighter) unwrap `FunctionScoreQuery.getWrappedQuery()`.

## Test
`HighlighterTest.testHighlightQueryWrappedInBoost`: `+id:1 +_query_:"{!boost b=3 v=$qq}"` with
`qq=<field>:keyword`, for `hl.method=original` (`t_text`) and `fastVector` (`tv_text`).

## Guesses to verify first
- Recent Lucene may already unwrap `FunctionScoreQuery` in `WeightedSpanTermExtractor`/`FieldQuery`; then
  the test passes without the fix and the ticket is simply stale (a useful close-out finding).
- Whether hl.q defaults to `q` here (it does through `rb.getQparser().getHighlightQuery()`); the nested
  `{!boost}` should still be the child of a `BooleanQuery`.
- `{!boost b=3 ...}`: constant `b` is parsed as a function; `boostByValue` with a constant should be fine.
- Not covered: `hl.method=unified` (uses its own weight extraction).
