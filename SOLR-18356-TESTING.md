# SOLR-18356 handoff (remove before the PR)

Review: `research/branch-reviews/round-7/SOLR-18356-review.md` in the workspace. Nothing here was compiled or run.

## What is claimed

Removes the 8.0-deprecated `DocsStreamer.convertLuceneDocToSolrDoc(Document, IndexSchema)`. All in-tree callers already
used the 3-arg overload.

## Be skeptical about

- The first commit on this branch also added `externalizeStoredValues` and a nonexistent
  `solrConfig.enableDocValuesIteratorCache`; the second commit reverts both. If that EmbeddedSolrServer / DocValues
  work is wanted it needs its own ticket; it survives only in commit `6d3b45315b7`.
- This branch's base predates `major-changes-in-solr-11.adoc`, so no upgrade note was added. Add a short "Removed
  Features" entry once the branch sits on current `main`.
- The changelog file name is mixed case; rename before the PR.
- No behavioural test exists or is needed; the real check is that `solr/core` compiles.

## How to verify

`:solr:core:test --tests org.apache.solr.search.ReturnFieldsTest` (queued), plus `gradlew check -x test`.
