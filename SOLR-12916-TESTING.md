# SOLR-12916 - hypothetical reproduction (nothing was compiled or run)

JIRA (2018, 7.5): listeners added through the Config API (`"queries": [ ["q", "*:*", "rows", 1] ]`) were deserialized into
`List<List>`, but `QuerySenderListener` required `List<NamedList>`. The audit note said "fixed by SOLR-9359 (convertQueriesToList)".

## What main does
SOLR-9359 added `convertQueriesToList`, which accepts a `NamedList` (JSON object form `{"q":"*:*"}`) and, for XML, an `ArrayList` of
`NamedList`. The *ticket's own form* (array of flat name/value arrays) still falls into the `else` branch and is logged
"ignoring unsupported warming config (...) ... nested lists in JSON from Config API" - the code comment even names this case. So the
warming query is silently dropped.

## Change
`convertQueriesToList` converts a flat even-sized list of `String` name / plain value pairs into a `NamedList`, at the entry level and
one level down (the XML-style wrapper). Lists containing lists or `NamedList`s are not flat and keep the old handling. New
`QuerySenderListenerTest.testFlatNameValueQueryFromConfigApi` (pure unit test of the static helper).

## Guesses to verify first
- How the Config API actually nests the array (entry value vs. one extra wrapper): both levels are handled, but a real
  `add-listener` round trip (`TestConfigOverlay` / `TestSolrConfigHandler`) was not run.
- An XML `<arr name="queries"><str>a</str><str>b</str></arr>` was previously warned and ignored; it now becomes the parameter
  `a=b`. Considered acceptable, flag in review.

## Fail-before
Expected: the new test fails on `upstream/main` (size 0 and a missing `q`). Enqueue with `-WithFailBefore`.
