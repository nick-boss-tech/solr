# SOLR-10694 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
Shalin Shekhar Mangar: serialization of `IteratorWriter` and `MapWriter` was only implemented for the JSON and javabin
writers, which is "trappy"; all response writers should serialize these two types. The audit note said "fixed in
6.7/7.0 (fix version set)" and an earlier read concluded every writer goes through `TextWriter.writeVal`.

## What the code shows on main
`TextWriter.writeVal` does dispatch to `writeMap(String, MapWriter)` / `writeIterator(String, IteratorWriter, boolean)`,
but their defaults are `// todo` no-ops. The CSV writer (`CSVWriter extends TabularResponseWriter`) overrides neither,
and `TabularResponseWriter` stubs `writeMap`, `writeArray` and `writeNamedList` with empty bodies. So for a document
field whose value is a `Map`, `NamedList`, `MapWriter`, `IteratorWriter`, iterator or array, `writeSolrDocument` calls
`writeVal`, nothing is printed, and that row has one cell fewer: every later column moves left by one.

## Change
`CSVWriter.writeSolrDocument` writes cell values through a new `writeCellVal`, which prints those structured values as
compact JSON (`Utils.toJSONString(val, -1)`) in one escaped cell; everything else still goes through `writeVal`. `Path`
(an `Iterable`) is excluded because `writeVal` already handles it. Top-level, non-document responses are unchanged.

Test: `TestCSVResponseWriter.testStructuredValuesAreWrittenAsJsonCells` (direct `SolrDocumentList`, no index) with a
map-valued, a `NamedList`-valued and a missing `meta_s`, checking the following `foo_s` column keeps its position.

## Guessed / verify first
- Whether the XML/JSON/javabin paths for `MapWriter` are really complete was not re-audited; only CSV was fixed.
- Compact JSON in a CSV cell is a pick (ticket gives no format); a `[explain]` NamedList value will now appear as JSON
  where it used to be empty (the existing `testCSVOutput` expectation for `[explain]` is an *empty* cell because the
  docs there carry no explain value, so it should be unaffected).
- Elements of a multi-valued field that are maps are written with the multi-value printer, whose delimiter/escape
  settings may mangle the commas inside the JSON; that case has no test.
- `SolrDocument.addField` keeps a `Map`/`NamedList` value raw (checked in `SolrDocument.setField`).
