# Receipt: PR review apache/solr #4458 (vm4)

- PR: https://github.com/apache/solr/pull/4458, open, not draft, head branch `docvalues-skip-list` on `iamsanjay/solr`, 11 files, +140/-6 (GitHub files API), updated 2026-09-15, `mergeable_state` unknown at the snapshot.
- Core files: `FieldProperties.java` (+3/-1), `FieldType.java` (+32), `PointField.java` (+14/-2), `SchemaField.java` (+6/-1). Tests: `TestSchemaField.java` (+19), `TestCodecSupport.java` (+22), `TestFieldResource.java` (+3/-2), `BadIndexSchemaTest.java` (+6), new `bad-schema-unsupported-skip-list.xml` (+23), `schema_codec.xml` (+6), `schema_postingsformat.xml` (+6).
- Base checks at `c8ab71430f`: `FieldProperties.propertyNames` has 20 entries (bits 0 to 19), so the new `skipList` entry maps to bit 20. `SchemaField.java` imports `DocValuesSkipIndexType` and its `docValuesSkipIndexType()` returns `NONE` (a stub).
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4458.md`.
