# SOLR-17047 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`SolrCore.initCodec()` fail-fast validation rejected field types with
configured postings/docValues formats under a non-`SolrCoreAware`
`CodecFactory`, but never inspected `DenseVectorField` KNN options — so
vector options were silently ignored with a custom codec, and invalid
options (e.g. a misspelled `knnAlgorithm`) only blew up lazily at first
segment flush inside `SchemaCodecFactory.getKnnVectorsFormatForField`.

Changes:

1. `DenseVectorField.hasNonDefaultKnnOptions()` (new): true when any option
   the codec's KNN vectors format applies (`knnAlgorithm`, `hnswM`,
   `hnswEfConstruction`, all `cuvs*` params) differs from its default.
   (Round-3 review correction: the first version also counted
   `vectorEncoding` and `similarityFunction`, which the field type applies
   itself and the codec never reads; that would have rejected every cosine
   field under `LuceneDefaultCodecFactory` / `SimpleTextCodecFactory`.)
   Overridden to return `true` in `BinaryQuantizedDenseVectorField` and
   `ScalarQuantizedDenseVectorField` (quantization inherently requires
   `SchemaCodecFactory`, which is the only caller of their overridden
   `buildKnnVectorsFormat()`).
2. `SolrCore.initCodec()`: in the non-`SolrCoreAware` branch, a
   `DenseVectorField` with non-default KNN options now throws
   `SolrException(SERVER_ERROR)` naming the field type and codec class,
   symmetric with the existing postings/docValues checks.
3. `SchemaCodecFactory`: extracted the hnsw/flat support check from
   `getKnnVectorsFormatForField` into `validateKnnAlgorithm`, and added
   `validateKnnVectorsOptions(IndexSchema)`; `initCodec` calls it whenever
   the factory is a `SchemaCodecFactory` (explicit or default), so an
   unsupported `knnAlgorithm` fails at core init instead of first flush.

Notes for the reviewer:

- `CuVSCodecFactory` implements `SolrCoreAware`, so the new else-branch
  check can never false-positive on cuvs-based setups.
- A `DenseVectorField` with all-default options + a custom non-`SolrCoreAware`
  codec still loads (deliberate: per the ticket, only *configured* options
  "require" `SchemaCodecFactory`).
- `getKnnVectorsFormatForField` behavior is unchanged (same exception, now
  via the extracted method).
- Behavior change to mention in the PR: with `SchemaCodecFactory`, a
  `DenseVectorField` type with an unsupported `knnAlgorithm` now stops the core
  from loading even if no field uses the type or no segment was ever flushed.
- `validateKnnVectorsOptions` is now package-private (only `SolrCore`, same
  package, calls it).
- Changelog fragment added: `changelog/unreleased/SOLR-17047.yml`.

Files changed:
- `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java`
- `solr/core/src/java/org/apache/solr/schema/BinaryQuantizedDenseVectorField.java`
- `solr/core/src/java/org/apache/solr/schema/ScalarQuantizedDenseVectorField.java`
- `solr/core/src/java/org/apache/solr/core/SchemaCodecFactory.java`
- `solr/core/src/java/org/apache/solr/core/SolrCore.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Tests added (round-3 patch pass, **not compiled or run**), in `BadIndexSchemaTest`
next to the existing postings-format/codec case, with two new schemas under
`src/test-files/solr/collection1/conf/`:

- `testKnnVectorOptionsButNoSchemaCodecFactory`: `LuceneDefaultCodecFactory`
  (`solrconfig-lucene-codec.xml`) + a `DenseVectorField` with
  `knnAlgorithm="flat"` (`bad-schema-codec-knn-options-mismatch.xml`) → init
  fails with "codec does not support".
- `testVectorFieldWithoutKnnCodecOptionsAndNoSchemaCodecFactory`: the same
  codec factory with `schema-densevector.xml` (cosine and BYTE types, no codec
  options) → the core loads (regression for the first version's false positive).
- `testUnsupportedKnnAlgorithmFailsAtCoreInit`: `SchemaCodecFactory`
  (`solrconfig_codec.xml`) + `knnAlgorithm="typo"`
  (`bad-schema-codec-knn-algorithm-unsupported.xml`) → init fails with "typo KNN
  algorithm is not supported".

Queued for the verification run: `org.apache.solr.schema.BadIndexSchemaTest`,
`org.apache.solr.schema.DenseVectorFieldTest` and
`org.apache.solr.core.TestSchemaCodecFactoryDefaults` (existing coverage), with
Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- `validateKnnVectorsOptions` only checks the algorithm name; deeper
  per-option value validation (e.g. out-of-range `hnswM`) still happens
  lazily in `buildKnnVectorsFormat` — noted as a possible follow-up, not
  in scope.
- Remove this file before opening the upstream PR.
