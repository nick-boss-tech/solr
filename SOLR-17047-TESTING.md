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

1. `DenseVectorField.hasNonDefaultKnnOptions()` (new): true when any KNN
   option (`knnAlgorithm`, `vectorEncoding`, `similarityFunction`, `hnswM`,
   `hnswEfConstruction`, all `cuvs*` params) differs from its default.
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
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).

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

Suggested tests (not written):

1. Core init with a non-`SolrCoreAware` `CodecFactory` and a schema
   containing a `knn_vector` field with `knnAlgorithm="hnsw"`
   `hnswM="32"` (or any non-default option) → expect init-time
   `SolrException` naming the field type.
2. Same setup but all-default vector options → core loads (no regression).
3. `SchemaCodecFactory` (default codec) + `knnAlgorithm="typo"` →
   init-time failure, not flush-time.
4. Regression: default schema + default codec still loads; run existing
   `TestDenseVectorField` / codec-related suites.

## Patch limits and follow-ups

- **Not compiled or tested.**
- `validateKnnVectorsOptions` only checks the algorithm name; deeper
  per-option value validation (e.g. out-of-range `hnswM`) still happens
  lazily in `buildKnnVectorsFormat` — noted as a possible follow-up, not
  in scope.
- Remove this file before opening the upstream PR.
