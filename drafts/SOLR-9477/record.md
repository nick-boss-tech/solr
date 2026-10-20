# Skip record: SOLR-9477

- Ticket: SOLR-9477, "UpdateRequestProcessors ignore child documents". JIRA status read 2026-10-20T13:51:34Z (UTC): Open, resolution None, updated 2020-01-29.
- Verdict: keep-open (kept; re-read at the pinned head). The question is which update processors should apply to child documents, which is a design decision.
- Draft branch: solr-9477-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:51:34Z (UTC).

## Ticket reading

Update request processors ignore child documents, so a JSON document with `_childDocuments_` fails the required-ID check that a processor-chain document without children would pass. The reporter's example fails with "missing required field: id" on a child. Comments from 2016 say processors may differ in whether they should apply to children, and show a per-processor workaround.

## Code reading (pinned head)

- `git grep -l getChildDocuments 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/update/processor`: two files, `AddSchemaFieldsUpdateProcessorFactory.java` and `IgnoreLargeDocumentProcessorFactory.java`.
- The package has 44 update processor factory classes (`git grep -l "class .*UpdateProcessorFactory"`). Two of them handle child documents; the other 42 do not.

## History

- evidence.py at the pinned head: no commits naming SOLR-9477.

## Reproduction attempts

Not run. The reporter's example (a JSON document with `_childDocuments_` through a chain with an ID-requiring processor) is a short run, but the expected behavior for each processor is the design question, not something a run settles.

## Gap analysis

Only two processors handle children; the rest act on the root only or on the whole document without a child path. Whether all processors should apply to children (the ticket's reading) or only those that need to (the 2016 comments) is a design choice.

## Verdict justification

keep-open meets the rubric for an open design question: attempts made are the processor count and the grep; the missing piece is the design decision on which processors apply to children. close-fixed and close-obsolete are not supported. pr-needed is not chosen, since the scope of processors touched is a design choice.

## Limits

This record does not run the example. It does not check the ref guide's text on child documents.
