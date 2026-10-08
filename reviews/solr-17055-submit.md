# solr-17055-submit

- Branch: origin/solr-17055-submit
- Head: f0c401a290b2 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base 97d973814336, 19 commits behind)
- Scope: 6 files, +136/-1. `solr/core/.../handler/component/QueryComponent.java` (`mergeIds` caps `numFound` at knn `topK`; for score ordering sizes the merge queue at `min(start+rows, topK)`; new `getKnnTopK`), `solr/core/.../search/vector/SolrKnnFloatVectorQuery.java` and `SolrKnnByteVectorQuery.java` (new `getTopK()`), `DistributedKnnTopKTest.java` (new, 3-shard), changelog fragment, `SOLR-17055-TESTING.md` (author's hypothetical note, left in place).
- Verdict: Needs work (the score-order path is sound; the explicit-`sort` path is an owner call, see HIGH)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

HIGH (posed, not patched; owner call): with an explicit `sort`, the response can hold more docs than `numFound`. The `sort != null` branch skips the topK cap on the queue (`QueryComponent` around lines 1019-1021: `queueSize = offset + count` unless `sort == null`), but `numFound` is still capped (around line 1239). `setResultIdsAndResponseDocs` (around lines 1313-1321) builds the page from `shardDocQueue.resultIds(offset)`, so the page is every shard doc up to `rows`. Example: 3 shards, `topK=3`, `sort=id asc`, `rows=20` gives `numFound=3` with up to 9 docs. That is the same symptom the ticket reports (more hits than topK). The author's note says this is intentional ("the topK-by-similarity set cannot be recovered from sort values"). But the chosen design still returns `docs.size() > numFound`, which is not a valid response. Options for the owner: (a) two passes, first the global top-K by score and then sort that set; (b) reject `sort` with a knn `topK` (400); (c) accept and document. Not patched, because each option is a design choice.

MEDIUM (verified by reading, no test): the new `DistributedKnnTopKTest` covers only score ordering. It does not exercise `sort`, so the HIGH case above is not caught by any test in the branch.

MEDIUM (author-flagged, not checked): wrapped knn forms (block-join parent/child, knn in `fq`, rerank, `{!bool}` combinations) are not trimmed. `getKnnTopK` only looks at a top-level `SolrKnn*VectorQuery` on `rb.getQuery()`, so those still return up to topK per shard summed.

LOW (verified): the new code comment in `mergeIds` ("A knn query returns up to topK hits from every shard, but the user asked for topK overall") describes the change. AGENTS.md asks for no code comments that communicate the change. Left as is; trivial to drop.

LOW (verified): test comment `// vectors move away from [1,0,0,0]` does not match the query vector used (`[1.0, 0.0, 0.1, 0.1]`). The ordering assertion is still correct for the `cosine` field (`vector` uses `knn_vector_cosine` in `schema-vector-catchall.xml`): the score falls monotonically with `i`, so doc 1 is the closest.

verified (checked against the code):
- Score-order cap is correct. Each shard returns up to topK, so the global top-K by score is inside the union of shard results. Capping the queue at `min(start+rows, topK)` and then applying `start` gives the right page. With `topK=3, start=2, rows=5` the page is one doc.
- `numFound` cap is correct for score order: total knn hits = min(topK, matched docs).
- `Query` is imported (`QueryComponent.java:50`). `getTopK()` exists on both query classes, and the pattern-matching `instanceof` is fine for the core module's Java level.
- Test's fail-before (revert QueryComponent and the test expects 3 but gets 9) matches the code path.

## Not checked
- Nothing built, formatted, or run. No Gradle, no tests.
- Whether `schema-vector-catchall.xml` with the default `solrconfig.xml` supports `DistributedKnnTopKTest` as the author suspects (TESTING note).
- The `GET_FIELDS` stage with trimmed ids (author assumed fine; not traced).
- Rerank and block-join forms (see MEDIUM).
