# Skip record: SOLR-17747

- Ticket: SOLR-17747, "Not able to optimize solr 9.8.1 HNSW index". JIRA status Open, resolution none, created and last updated 2025-04-29, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-17747-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The reporter built an HNSW index (two `DenseVectorField` types, 256 dimensions, float and byte) on Solr 9.8.0, tried to optimize it to a single segment, and saw no merge and no error. The merge policy in the report is `TieredMergePolicyFactory` with `maxMergedSegmentMB=100000`, `maxMergeAtOnce=25`, `segmentsPerTier=25`. The report says the same happens on 9.8.1 and that it was also raised on the mailing list. It does not give the request (in particular whether `maxSegments` was passed), the document count, the segment count, the segment sizes, or the log. No comments.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:855-857`: on a commit with `optimize`, the handler calls `writer.forceMerge(cmd.maxOptimizeSegments)`; with `expungeDeletes` it calls `forceMergeDeletes()`.
- `solr/core/src/java/org/apache/solr/update/CommitUpdateCommand.java:56`: `maxOptimizeSegments = Integer.MAX_VALUE`, with the comment "So we respect MaxMergeSegmentsMB by default".
- `solr/core/src/java/org/apache/solr/handler/RequestHandlerUtils.java:99-100`: the `maxSegments` request parameter sets `maxOptimizeSegments`; if it is absent the default is used.
- `solr/solr-ref-guide/modules/indexing-guide/pages/indexing-with-update-handlers.adoc` (the `maxSegments` entry): a best-effort target, and the guide advises leaving it out unless there is evidence that a small segment count helps.
- `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-8.adoc` (line 367): with `TieredMergePolicy`, `optimize` and `expungeDeletes` respect `maxMergedSegmentMB` (default 5000).
- No Solr code path handles `DenseVectorField` specially in the merge or optimize path; the vector fields appear only in the document builder and the update processor.

## History

- Not searched on the fork for SOLR-17747. `git log --grep` at the pinned head for the key is empty (see the Skip ticket evidence output: no commit names it).
- The optimize path and the `maxOptimizeSegments` default predate this ticket; the commits were not traced in this closeout.

## Reproduction attempts

1. Lucene-level probe, not a Solr run. Lucene core 10.4.0 (the version `gradle/libs.versions.toml` declares on the pinned head), jar from the local Gradle cache. The probe writes 10 one-document segments (NoMergePolicy, commit after each document), then reopens with `TieredMergePolicy` and calls `forceMerge`. Command: `javac -cp <lucene-core-10.4.0.jar> Probe.java` then `java -cp .:<jar> Probe`. Source and log: `/workspace/gates/logs/vm5/skip-probe-17747/` (`Probe.java`, `run.log`).
   - Result: `maxMergedSegmentMB=5000`, `forceMerge(Integer.MAX_VALUE)` gives 1 segment. `maxMergedSegmentMB=100000`, `forceMerge(Integer.MAX_VALUE)` gives 1 segment. `forceMerge(1)` gives 1 segment in both cases.
   - Conclusive: no for the ticket's symptom. The probe covers small plain-field segments only. It has no vector fields, no Solr request, and no segment near the size limit.
   - Next variant that separates the explanations (not run): build an HNSW index through Solr with the report's schema, enough documents to produce segments above `maxMergedSegmentMB`, then compare the segment count before and after `optimize=true` with and without `maxSegments=1`.

## Gap analysis

For small segments, Lucene's forced merge with `TieredMergePolicy` reduces the count, so the default `optimize` path is not a plain no-op in this version. The ticket's symptom would need one of three things the report does not give: segments at or above the size limit (which forced merging skips), a request that passes `maxSegments` with a value the policy considers already met, or behavior specific to the vector codec's merge. Each of these is a separate explanation, and the report does not separate them.

## Verdict justification

keep-open: the attempts (code reading of the optimize path and the default, the reference guide and upgrade note, and the Lucene probe) are named, and the missing piece is a Solr-level reproduction with the report's schema and a request, plus segment counts before and after. The probe result is not a reproduction of the symptom, so close-cannot-reproduce does not apply. No code path gives a defect to point at, so pr-needed does not apply.

## Limits

This record does not reproduce the HNSW symptom in Solr. It does not check the mailing-list thread the report mentions. It does not establish whether the vector codec changes merge behavior in Lucene 10.4.0; the probe did not use vector fields.
