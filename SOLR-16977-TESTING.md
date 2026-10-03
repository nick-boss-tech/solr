# SOLR-16977 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`{!knn f=vector topK=1}[0.0, 0.0, 0.0, 0.0]` (all-zero query vector) used to
fail deep inside result rendering with
`IllegalArgumentException: docID must be >= 0 and < maxDoc=2 (got
docID=2147483647)` — a zero-magnitude query vector produces undefined
(cosine NaN) scores and the bogus doc ID only surfaces when Solr fetches
the stored document for the hit.

`DenseVectorField.getKnnVectorQuery` now validates the parsed query vector
up front, when the field's similarity function is `COSINE` (both `FLOAT32` and
`BYTE` encodings), and throws `SolrException(ErrorCode.BAD_REQUEST, "KNN query
vector must not be an all-zero vector when the similarity function is
cosine")` instead. The original `switch` building the query is unchanged.

Round-3 review correction: the first version rejected an all-zero query vector
for every similarity function. The field type's default similarity is
`EUCLIDEAN`, where a zero query is a valid "nearest to the origin" query, so
existing working queries would have become 400s. `DOT_PRODUCT` and
`MAXIMUM_INNER_PRODUCT` give degenerate but defined scores and are not rejected
either.

Notes for the reviewer:

- `getKnnVectorQuery` is the single funnel: only caller is
  `KnnQParser.parse()`, and neither quantized subclass overrides it, so
  all `{!knn}` paths (hnsw/flat, float/byte, quantized types) get the check.
- Query-side only, per the ticket: already-indexed all-zero *document*
  vectors are a separate, murkier problem (can't be rejected at query time;
  index-time validation is a bigger behavioral call) and are out of scope.
- Not verified: whether the reported `docID=2147483647` failure still
  reproduces with the Lucene version on `main` (nothing was run); the new
  tests assert the validation itself, not the old failure.

Files changed:
- `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java`
- `solr/core/src/test/org/apache/solr/schema/DenseVectorFieldTest.java`
- `changelog/unreleased/SOLR-16977.yml` (new)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Tests added (round-3 patch pass, **not compiled or run**), in
`DenseVectorFieldTest`, building `DenseVectorField` instances directly with the
existing 3-argument constructor (as `VectorSimilaritySourceParserTest` does), so
no core or new schema is needed:

- `zeroQueryVector_cosineSimilarity_shouldBeRejected`: FLOAT32 and BYTE, a
  `[0, 0, 0, 0]` query → `SolrException` BAD_REQUEST mentioning "all-zero".
- `zeroQueryVector_nonCosineSimilarity_shouldBeAccepted`: EUCLIDEAN,
  DOT_PRODUCT and MAXIMUM_INNER_PRODUCT with both encodings → a query is built.
- `nonZeroQueryVector_cosineSimilarity_shouldBeAccepted`: cosine with
  `[0, 0, 0, 1]` → `KnnFloatVectorQuery` / `KnnByteVectorQuery`.

Queued for the verification run: `org.apache.solr.schema.DenseVectorFieldTest`
and `org.apache.solr.search.vector.KnnQParserTest` (existing knn query
coverage), with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Document-side all-zero vectors are not handled (the ticket's second case);
  say so in the PR.
- Changelog fragment added: `changelog/unreleased/SOLR-16977.yml`.
- Remove this file before opening the upstream PR.
