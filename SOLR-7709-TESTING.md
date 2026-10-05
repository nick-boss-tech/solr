# SOLR-7709 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"Solr JavaBinCodec multi valued fields take only the last value per document from the javabin
buffer" (2015, no comments). A javabin document that repeats a field name
(`field_2=v1, field_2=v2, field_2=v3`) ends with only `v3`; the same document as XML keeps all
three. The reporter points at `readSolrDocument` / `readSolrInputDocument` using `setField`.

## Bug mechanism
Both readers in `JavaBinCodec` call `setField(fieldName, fieldVal)` for every pair in the stream,
so a repeated name replaces the earlier value. Solr's own writers emit each name once (multiple
values travel as one collection), so only external or hand-built streams hit it.

## Fix
When the document already contains the name, call `addField` (which appends, and merges
collections) instead of `setField`. The first occurrence still uses `setField`, so the normal
path and its collection handling are unchanged. Applies to `SolrDocument` and `SolrInputDocument`.

## What the test pins
`TestJavaBinCodec.testRepeatedFieldNamesKeepAllValues` writes documents whose `writeMap` emits
`multi` three times (and `single` once) through the real codec, then reads them back as both
`SolrInputDocument` and `SolrDocument`.

## What was guessed / verify first
- That anonymous subclasses overriding `size()` and `writeMap` are accepted by
  `writeSolrInputDocument` / `writeSolrDocument` (they only call those two for field output);
  if the writer also iterates entries elsewhere the test setup needs a hand-written stream.
- Semantics: some may argue a repeated name in a binary stream should be rejected, not merged.
  The ticket asks for XML-like merging, which is what this does.
- `SolrDocument.addField` with an existing scalar creates an `ArrayList`; the test expects a
  `List` of three values in order.
- Fail-before: revert only `JavaBinCodec.java`; the test should fail with `[value_3]`.
- Spotless formatting.
