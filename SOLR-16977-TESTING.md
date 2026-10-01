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
up front (both `FLOAT32` and `BYTE` encodings) and throws
`SolrException(ErrorCode.BAD_REQUEST, "KNN query vector must not be an
all-zero vector")` instead. The parsed vector is now extracted once per
encoding branch (previously `getFloatVector()`/`getByteVector()` was called
inline in each ternary branch) — same single-call semantics.

Notes for the reviewer:

- `getKnnVectorQuery` is the single funnel: only caller is
  `KnnQParser.parse()`, and neither quantized subclass overrides it, so
  all `{!knn}` paths (hnsw/flat, float/byte, quantized types) get the check.
- Query-side only, per the ticket: already-indexed all-zero *document*
  vectors are a separate, murkier problem (can't be rejected at query time;
  index-time validation is a bigger behavioral call) and are out of scope.
- Design point worth a second look: the rejection is unconditional across
  similarity functions. A zero query vector is degenerate input in every
  case, but strictly speaking only cosine produces the reported crash
  (dot_product yields all-zero scores, euclidean is well-defined). If
  reviewers prefer, the check could be gated on
  `similarityFunction == VectorSimilarityFunction.COSINE`.

Files changed:
- `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. `{!knn}` with an all-zero float query vector → HTTP 400 with the
   "all-zero vector" message (was: 500-ish `IllegalArgumentException`
   about docID at render time).
2. All-zero byte-encoded query vector → same 400.
3. Non-zero query vector → query builds and executes normally (no regression).
4. Existing `TestDenseVectorField` / knn query test suites.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
