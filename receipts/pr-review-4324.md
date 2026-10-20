# Receipt: PR review apache/solr #4324 (vm4)

- PR: https://github.com/apache/solr/pull/4324, open, not draft, head branch `SOLR-18207_avoid_duplicate_vector_storage` on `iamsanjay/solr`, 6 files, +327/-6 (GitHub files API), updated 2026-09-20, `mergeable_state` unknown at the snapshot.
- Files: `DenseVectorField.java` (+27/-1, new `useVectorValuesAsStored` option), `SolrDocumentFetcher.java` (+136/-5, KNN values read path), `DenseVectorFieldTest.java` (+69), `schema-densevector-derived-stored.xml` (added, +54), `bad-schema-densevector-derived-stored-multivalued.xml` (added, +22), `dense-vector-search.adoc` (+19).
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4324.md`.
