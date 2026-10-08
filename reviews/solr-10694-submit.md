# solr-10694-submit

- Branch: origin/solr-10694-submit
- Head: 093d90c62de (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 18 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java` (+24/-3: new `writeCellVal`, used at the three cell-write sites), `solr/core/src/test/org/apache/solr/response/TestCSVResponseWriter.java` (+43: `testStructuredValuesAreWrittenAsJsonCells`), the changelog fragment, and `SOLR-10694-TESTING.md` (kept in place).
- Verdict: Nearly
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code, except where marked as hypothesis or not traced. Patches: none.

## Premise check (hypothetical-reproduction handoff)

The TESTING doc says the CSV writer drops a cell for map, NamedList, iterator, and array values, so every later column shifts left. This is verified on base.

- VERIFIED: `TabularResponseWriter` overrides `writeNamedList`, `writeMap`, `writeArray` (both forms) with empty bodies (`TabularResponseWriter.java:121`, `:136`, `:139`, `:142`). A map, NamedList, or array cell is written as nothing.
- VERIFIED: `TextWriter.writeIterator(IteratorWriter)` is a no-op (`solrj/.../common/util/TextWriter.java:254-256`), and `writeMap(String, MapWriter)` delegates to a no-op `writeMap(MapWriter)` (`:246-251`). So `MapWriter` and `IteratorWriter` cells are dropped too.
- VERIFIED: the premise matches the cell-writing code. `CSVWriter.writeSolrDocument` writes each cell with `writeVal` (`CSVResponseWriter.java:394-443` on base), so a dropped value leaves an empty cell and the next column moves left.

## Change check

- VERIFIED: `writeCellVal` (`CSVResponseWriter.java:82-98`) replaces `writeVal` at all three cell sites: the multi-valued loop (`:418`), the polyfield path (`:436`), and the single-valued path (`:441`).
- VERIFIED: the single-valued path unpacks a `Collection` to its first element before writing (`:427-429`), and the multi-valued path iterates the elements (`:417-419`). So a whole `Collection` never reaches the JSON branch.
- VERIFIED: `Path` is excluded from the `Iterable` case, so existing `Path` output is unchanged.
- VERIFIED: the imports (`MapWriter`, `IteratorWriter`, `Utils`, `Path`, `Iterator`) are present (`CSVResponseWriter.java:36`, `:39`, `:44-45`, `:50`), and `Utils.toJSONString(Object, int)` exists (`solrj/.../util/Utils.java:262`).
- VERIFIED: the noggit `JSONWriter` handles `Map` (`:90`), `Iterator` (`:94`), and `Object[]` (`:100`), so those JSON cases are supported.
- VERIFIED: `NamedList` implements `MapWriter` (`solrj/.../util/NamedList.java:58-59`), so the test's NamedList cell takes the `MapWriter` path.
- NOT TRACED: how `Utils.toJSON` serializes a `MapWriter` or `IteratorWriter` (the `SolrJSONWriter` side), and the `-1` indent meaning as compact output. The test's expected `{"x":5}` assumes both.

## Findings (ranked)

LOW (owner call): The output format is the author's pick. Compact JSON in a cell is not specified by the ticket (the TESTING doc says so too). The change also makes `[explain]` (a NamedList) print as JSON where it used to be empty. The existing `testCSVOutput` expectation should be unaffected, because its docs carry no explain value. Owner decision on whether JSON is the format, and whether the `[explain]` change is wanted.

LOW (hypothesis, not tested): Multi-valued fields with map or NamedList elements go through the multi-value printer (`mvPrinter`). The JSON commas and quotes inside such a cell are not tested, and the mv printer's escaping was not traced. The TESTING doc flags the same gap.

LOW (coverage): The test covers single-valued cells only: a map, a NamedList, and a missing field. The multi-valued path, `IteratorWriter`, `Iterator`, and `Object[]` are not exercised.

## Verified correct (by reading; not run)

- The test's expected rows follow the code path. `{"a":1}` and `{"x":5}` are written through `writeStr(..., true)` with CSV quoting, which doubles the quotes. The missing `meta_s` takes `writeNull` (`:397-400`), so row 3 is `3,,again`.
- The test's helpers exist in the same package or are imported: `SolrQueryResponse` (same package), `NamedList` (added), `SolrReturnFields`, `StringWriter`.
- The changelog fragment matches the upstream format (`type: fixed`, ICLA author, JIRA link).
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

1. The cell format: compact JSON, or another format? (See the first LOW.)

## Not checked

- Nothing was compiled or run.
- `SolrJSONWriter`'s handling of `MapWriter` and `IteratorWriter`, and the `-1` indent meaning (see the NOT TRACED note).
- The `CSVPrinter` quoting rules and the `mvPrinter` escaping for JSON in multi-valued cells.
- The `new CSVResponseWriter().write(buf, req, rsp)` call pattern used by the test was not compared with the file's other tests.
- Upstream conflicts. The branch is 18 commits behind `upstream/main`.
