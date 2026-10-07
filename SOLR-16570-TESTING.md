# SOLR-16570 hypothetical reproduction

Status: hypothetical, unrun. Nothing was compiled and Gradle was not used.

## Ticket
`{!collapse nullPolicy=expand hint=top_fc}` on an empty index throws a `NullPointerException` in
`CollapsingQParserPlugin$OrdScoreCollector.<init>`. A comment points at SOLR-16611, and the audit
note called it fixed ("PR #1274 merged"). That fix is present: `getDocValuesProducer` returns
`DocValues.emptySorted()` when the uninverting reader has no values.

## Finding on current `upstream/main`
`CollapsingQParserPlugin.getTopFieldCacheReader` (used by the collapse post filter and, twice, by
`ExpandComponent`) does

    if (f.indexed() && f.isUninvertible()) type = SORTED;
    return UninvertingReader.wrap(new ReaderWrapper(...), Map.of(collapseField, type)::get);

For a string field that is `docValues=true` but not indexed, or indexed with `uninvertible=false`
(the default for every schema at version 1.7 or later, so `_default` string fields), `type` stays
null and `Map.of(collapseField, null)` throws a `NullPointerException`. `hint=top_fc` is accepted
by the request-level check (docValues enabled is enough), so a user who adds the hint to a collapse
on an ordinary docValues string field gets a 500.

## Change
- `getTopFieldCacheReader` returns `searcher.getSlowAtomicReader()` when there is nothing to
  uninvert (`type == null`), so the callers' `getSortedDocValues(field)` reads the docValues
  directly. Collapse and both `ExpandComponent` call sites are fixed by the one change.
- `schema11.xml`: new dynamic field `*_s_dv_not_uninvert` (indexed, docValues, `uninvertible=false`).
- `TestCollapseQParserPlugin.testTopFcHintOnDocValuesWithoutUninversion`: collapse with and without
  `hint=top_fc` returns the same two group heads (`max=test_i`), plus `expand=true` returns doc 1
  in group `a`.

## Expected
Before the fix the `hint=top_fc` iteration fails with a `NullPointerException` (HTTP 500). After
the fix both iterations pass.

## Risky guesses
- `SlowCompositeReaderWrapper.getSortedDocValues` is assumed to serve the field directly (it does
  for the `DocValues.getSorted(searcher.getSlowAtomicReader(), ...)` path in the same class).
- The expand assertion assumes `solrconfig-collapseqparser.xml` registers the expand component on
  `/select` (TestExpandComponent uses the same config).
- Did not touch the original empty-index report: that shape is already covered by SOLR-16611.

## Verify later
`:solr:core:test --tests org.apache.solr.search.TestCollapseQParserPlugin`, then
`org.apache.solr.handler.component.TestExpandComponent`.
