# Skip record: SOLR-13263

- Ticket: SOLR-13263, Facet Heat Map should support GeoJSON.
- Verdict: keep-open.
- Draft branch: solr-13263-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 3 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-20, updated 2019-06-08, no fix versions. Links: SOLR-8859 (Closed), SOLR-7005 (Closed). The report: the facet heatmap `geom` parameter accepts only WKT or the `[ ]` rectangle syntax, because the parser uses the deprecated JTS-based WKT reader; the ticket asks for GeoJSON input. Twelve comments (2019-02-20 to 2019-05-12). The reporter proposed a patch (2019-02-22, no commit) and then worked on Geo3D polygon handling (2019-05-02 to 2019-05-12). dsmiley notes (2019-03-10) that JTS could be added to Solr and that Geo3D already supports polygons. The design questions (JTS licensing and inclusion, and the GeoJSON envelope equivalent) are unresolved on the ticket.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/util/SpatialUtils.java:37-50` (`parseGeomSolrException`): a string starting with `[` or `{` goes to `parseRectangeSolrException` (line 42-44); any other string goes to `ctx.readShapeFromWkt(geomStr)` (line 47). A GeoJSON object starts with `{`, so it is routed to the rectangle parser, not to any GeoJSON reader.
- `solr/core/src/java/org/apache/solr/search/facet/FacetHeatmap.java:152-155`: the heatmap `geom` argument is parsed with `SpatialUtils.parseGeomSolrException`.
- GeoJSON appears in Solr's Java sources only in `solr/core/src/java/org/apache/solr/response/GeoJSONResponseWriter.java` (a response writer). No code reads GeoJSON geometry for the heatmap `geom` argument.
- The reference guide says Solr supports GeoJSON for indexing (`solr/solr-ref-guide/modules/query-guide/pages/spatial-search.adoc:65, 69-74`, `format=geojson` with `bin/solr post`). That is the indexing path, not the heatmap `geom` argument, so it does not answer the ticket.

## History

- No commit in the pinned history names SOLR-13263 (the evidence tool lists none).
- SOLR-8859 (fb37b3eb8c, 022877fefa) and SOLR-7005 (ac50da1613, 59769898df) are linked; they are the facet heatmap and field-format history, not GeoJSON input. Their ancestry was not checked in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. A request with a GeoJSON polygon in the `geom` argument, against a core with a spatial RPT field, would show the failure; the expected result is a bad-request error from the rectangle or WKT parser.

## Gap analysis

- The code path the ticket names is present: the heatmap geometry parser accepts WKT and the rectangle syntax only, and a GeoJSON object is not parsed.
- The fix is a feature (a new input format), not a removal or a one-line correction, and the ticket records open design questions (JTS inclusion, licensing, the envelope type for GeoJSON, and the Geo3D bounding-box behavior) that a maintainer has not settled.
- The docs' GeoJSON support is for indexing, which does not cover the heatmap argument.

## Verdict justification

keep-open: the code path is confirmed by reading, but whether and how to add GeoJSON input is a maintainer design question on the ticket that is still open. pr-needed would need the design decision and a reproduction; close-fixed and close-obsolete do not fit (no fix, and the parser is present).

Missing for this verdict: (1) a run of a GeoJSON `geom` argument at this sha to show the error; (2) a maintainer decision on the design (JTS inclusion or another parser, and the envelope type).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
