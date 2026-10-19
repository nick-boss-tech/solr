# Skip record: SOLR-13785

- Ticket: SOLR-13785, Optimistic concurrency issue for nested documents. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-09-23, updated 2020-01-21, no fix versions, no links; one comment (2019-11-01, dsmiley): overlapping child and parent IDs are erroneous, and nested documents have no protection against it.
- Verdict: close-answered.
- Draft branch: solr-13785-draft-close (new; supersedes the keep-open draft solr-13785-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The docs answer and the version-conflict code were read at this head (detached worktree).
- Source: remediation redo of batch 05, item 21, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the maintainer comment from audit evidence; the docs are checked here.

## Ticket reading

With a nested document, an optimistic-concurrency update (using the `_version_` check) returned "version conflict" when a child document in one block had the same id as a parent document in another. The reporter says there was no concurrent update, and asks why the conflict arises.

## The answer, from the docs at the pinned head

- solr/solr-ref-guide/modules/indexing-guide/pages/indexing-nested-documents.adoc:275: "Even child documents need a _globally_ unique `id`."
- The same page, :296: "Solr demands that the `id` of _all_ documents in a collection be unique."

So the overlap is a user error under the documented rule. The version conflict is the result of two documents sharing one id, not of a concurrency bug.

## Code reading (for the version check)

- solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:425 is where the "version conflict for" error is raised. The check compares the expected and actual version of the document found under that id, so a child that reuses a parent's id is compared against the parent's version, which is the situation in the report.

## History

- The rule on the page predates the pin; the commit that added it was not traced in this pass.

## Reproduction attempts

Not needed for close-answered. The docs sentence answers the question as asked.

## Gap analysis

The reporter asked why a conflict occurs without a concurrent update. The docs state the uniqueness requirement, and the code shows the conflict is raised against whichever document carries the shared id. Whether Solr should reject duplicate ids across a block at write time (a feature the maintainer said is not provided because it would be expensive) is not asked by the ticket as written.

## Verdict justification

close-answered: the support question is answered by the reference guide, with the page link and the two sentences above, at the pinned head. The ticket asks why the conflict happens, and the docs answer it: ids must be unique across all documents, child documents included.

## Limits

This record does not test a duplicate-id write. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
