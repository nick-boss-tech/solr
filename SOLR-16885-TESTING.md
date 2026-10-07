# SOLR-16885 hypothetical reproduction

Status: hypothetical, unrun. Nothing was compiled and Gradle was not used.

## Ticket
Since Solr 9.0, highlighting a multi-term query on a field with `termVectors="true"
termOffsets="true"` but without `termPositions="true"` throws
`IndexOutOfBoundsException: start 8, end 7, length 16` from
`DefaultPassageFormatter.append`. Hossman's analysis: root cause is Lucene (LUCENE-12431, the
`UnifiedHighlighter` now needs positions to read term vector offsets), "not likely a fix in Solr".
Workaround is a schema change plus a reindex.

## Idea
Solr cannot change the Lucene reader, but it picks the offset source. `UnifiedHighlighter.
getOffsetSource` returns `TERM_VECTORS` for any field that has vectors ("we can't also check if the
TV has offsets"). `SolrExtendedUnifiedHighlighter.getOffsetSource` already overrides it for
`hl.offsetSource`; the schema knows the term vector flags.

## Change
`SolrExtendedUnifiedHighlighter.getOffsetSource`: when `hl.offsetSource` is not given and Lucene
would choose `TERM_VECTORS`, but the schema field has no `termPositions`, use `ANALYSIS` (re-analyze
the stored text). An explicit `hl.offsetSource=term_vectors` is still honoured.

Test: `schema-unifiedhighlight.xml` gets `text_tv_offsets` (`termVectors` + `termOffsets`, no
positions) and `text4`; `TestUnifiedSolrHighlighter.testTermVectorOffsetsWithoutPositions` highlights
`text4:(crappy document)` and expects both terms in `<em>`.

## Expected
Before the change the request fails with the exception of the ticket (if Lucene still behaves as
described). After it, the snippet comes from analysis and highlights `crappy` and `document`.

## Risky guesses
- Lucene's current behaviour: the exception may need a longer document or a different term order to
  trigger; the test uses repeated terms (`crappy ... crappy document`) to get overlapping passages.
  If Lucene fixed it since 9.x, the test passes without the change (a pin only).
- Fields with vectors but no offsets also take the analysis path only when positions are missing; a
  field with vectors and positions but no offsets is unchanged.
- Falling back to analysis is slower for large stored fields; the schema change is still the real fix.

## Verify later
`:solr:core:test --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter`.
