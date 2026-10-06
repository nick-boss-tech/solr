# SOLR-17055 - hypothetical reproduction (not run)

Nothing here was compiled or run. The fix and test were guessed from reading `upstream/main`.

## JIRA context
`{!knn f=v topK=3}` on a two-shard collection returned six documents (`numFound=6`): each shard
returns its own topK and the coordinator summed them. The user asked for topK overall. The old note
called this "by design, needs a merge design change"; this branch picks the design.

## Design
`QueryComponent.mergeIds` recognises a top-level `SolrKnnFloatVectorQuery` / `SolrKnnByteVectorQuery`
main query (new `getTopK()` accessors) and
- caps `numFound` at topK;
- for score ordering (no explicit `sort`) sizes the merge queue at `min(start+rows, topK)`, so the
  global best topK by score survive and the rest are dropped.
Explicit `sort` keeps all shard docs (only `numFound` is capped), because the topK-by-similarity
set cannot be recovered from sort values.

## Guessed / verify first
- `rb.getQuery()` is the parsed `SolrKnn*VectorQuery` on the coordinator; wrapped forms (block-join
  parent/child knn, `fq` knn, rerank, `{!bool}` combos) are not trimmed.
- Test `DistributedKnnTopKTest` uses `schema-vector-catchall.xml` with the default `solrconfig.xml`
  (not verified to be compatible) and relies on `query(...)` also comparing against the single
  control core, which gives topK = 3 too.
- Later stored-field fetch (`GET_FIELDS`) stage sees only the trimmed ids; assumed fine.
- Fail-before: revert `QueryComponent` and the test returns 9 (3 shards x 3).
