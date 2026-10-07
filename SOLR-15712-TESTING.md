# SOLR-15712 hypothetical reproduction

Status: hypothetical, unrun. Nothing was compiled and Gradle was not used.

## Ticket
`java.lang.ArrayIndexOutOfBoundsException` in `BytesRef.utf8ToString` called from
`SolrDocumentFetcher.decodeDVField` when a response returns a collated sort field. The ticket was
closed as a duplicate of SOLR-15777, which stopped `ICUCollationField` from defaulting
`useDocValuesAsStored`.

## Finding on current `upstream/main`
- `ICUCollationField` clears `USE_DOCVALUES_AS_STORED` and rejects an explicit `true`.
- The core `CollationField` (JDK collator) has no such guard. It writes the same kind of binary
  collation key into `SortedDocValuesField` / `SortedSetDocValuesField`, and with schema version
  1.6 or later `FieldType.setArgs` defaults `useDocValuesAsStored=true`.
- `SolrDocumentFetcher` puts every `stored=false docValues=true useDocValuesAsStored=true` field in
  `nonStoredDVsUsedAsStored`, so `fl=*` (and the default `fl`) decodes the key with
  `BytesRef.utf8ToString()`: garbage text, or the exception in the ticket.
- `schema-collate-dv.xml` (used by `TestCollationFieldDocValues`) has exactly that shape, but every
  existing test uses `fl=id`.

## Change
- `CollationField.init` clears `USE_DOCVALUES_AS_STORED` and throws (`FORBIDDEN`, same call as the
  ICU field: `XmlConfigFile.assertWarnOrFail`) when the fieldType sets it explicitly.
- `CollationField.checkSchemaField` throws when a field sets it explicitly.
- `TestCollationFieldDocValues.testDocValuesAreNotUsedAsStored`: all six `sort_*` fields have
  docValues and `useDocValuesAsStored()` is false; `fl=*` returns no `sort_*` entries.

## Expected
- Before the fix the test fails on the `useDocValuesAsStored()` assertion (and `fl=*` returns or
  throws on the key bytes).
- After the fix it passes.

## Risky guesses
- Hard failure for an explicit `useDocValuesAsStored="true"` (the ICU class also fails hard for
  `luceneMatchVersion` 9.0 or later). Existing user schemas that set it would no longer load; a
  warning only would be the gentler choice.
- An explicit `fl=sort_de` still decodes the key (same as ICU): `fl` naming a field bypasses the
  udvas flag. Not addressed.
- No test for the explicit-true failure path (needs a second schema file).

## Verify later
`:solr:core:test --tests org.apache.solr.schema.TestCollationFieldDocValues` plus
`org.apache.solr.schema.TestCollationField`.
