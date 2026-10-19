# Skip record: SOLR-16355

- Ticket: SOLR-16355, "When there are no changes to commit, an optimize is skipped". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2022-08-24, updated 2023-01-25, no fix version. Three comments: petdance (2022-08-24) describes the need to optimize two cores; elyograg (2022-08-24) finds that a SolrJ optimize works, and that `optimize=true&maxSegments=1` works in curl; elyograg asks whether the behavior is a bug.
- Verdict: close-answered. This replaces the earlier keep-open verdict; the reference guide answers the question (see below).
- Draft branch: solr-16355-draft-close (new; the earlier draft branch for this ticket is not reused).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 15:07-15:06 UTC.

## Ticket reading

On an index with no uncommitted changes, `update?commit=true&optimize=true` returns at once and does not optimize. With uncommitted changes, the same request optimizes. The reporter found that `optimize=true&maxSegments=1` works on a fully committed index. The question is whether the skipped optimize is a bug.

## Code reading

- `solr/core/src/java/org/apache/solr/update/CommitUpdateCommand.java:56`: `maxOptimizeSegments = Integer.MAX_VALUE` by default.
- `solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:855-856`: for an optimize commit, the code calls `writer.forceMerge(cmd.maxOptimizeSegments)`. With the default `Integer.MAX_VALUE`, the merge target is a segment count no index reaches, so the merge does nothing on an index that is already committed. The commit-level no-op the reporter saw is this default, not a separate skip.
- `solr/core/src/java/org/apache/solr/handler/RequestHandlerUtils.java:99-100`: `maxSegments` is read from the request and defaults to the command's value above.

What this shows at the pin: an optimize without `maxSegments` asks for "no more than Integer.MAX_VALUE segments". That is the documented default, and it is a no-op. The reporter's `maxSegments=1` sets an explicit target, which is why it works.

## Docs

- `solr/solr-ref-guide/modules/indexing-guide/pages/indexing-with-update-handlers.adoc:142-150`: the `maxSegments` parameter has "Default: none" and "makes a best effort attempt to merge the segments down to no more than this number of segments but does not guarantee that the goal will be achieved. Unless there is tangible evidence that optimizing to a small number of segments is beneficial, this parameter should be omitted and the default behavior accepted."
- `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-8.adoc:365-367`: "optimize ... now respect the `maxMergedSegmentMB` configuration parameter", and "If it is absolutely necessary to control the number of segments present after optimize, specify `maxSegments` as a positive integer."

Together these say what the reporter asked: an optimize without `maxSegments` is governed by the merge policy and does not force a single segment, and `maxSegments` is how to control the segment count.

## History

- No commit on the pinned head names SOLR-16355 (evidence tool: no hits).

## Reproduction attempts

Not run in this pass. The reporter's own runs (curl with and without `maxSegments=1`, and SolrJ) are in the comments and agree with the code path above. This pass is review-only.

## Gap analysis

The reporter's question, "is it a bug that optimize does not work on a fully current index without maxSegments=1", is answered by the code default and by the reference guide's own advice to omit `maxSegments` unless needed. The docs do not use the word "no-op", so a reader may still be surprised. That is a documentation wording point, not a defect.

## Verdict justification

close-answered meets the rubric: the reference guide answers the question (the two sentences above, with links to the pages), and the code path at the pin (`CommitUpdateCommand.java:56`, `DirectUpdateHandler2.java:855-856`) confirms the mechanism. close-fixed and close-obsolete are not met: nothing was fixed or removed. pr-needed is not met: the behavior is the documented default, not a defect. keep-open was the earlier verdict, and it no longer fits once the docs answer the question.

## Limits

This record does not run the commands. It does not say the docs wording is ideal, only that they answer the question. It does not post anything to JIRA or GitHub.
