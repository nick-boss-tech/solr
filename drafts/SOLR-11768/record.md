# Skip record: SOLR-11768

- Ticket: SOLR-11768, "NPE in QueryComponent". JIRA status read 2026-10-20T08:01:36Z (UTC): Open, resolution None, updated 2020-01-22. Linked: SOLR-10305 (Open), the same issue per the reporter's comment.
- Verdict: close-answered. This replaces the recorded keep-open: the reference guide answers the cause and the fix (reindex after a docValues change).
- Draft branch: solr-11768-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T08:01:36Z (UTC).

## Ticket reading

A collection got docValues on the ID field through a schema change. Documents indexed before the change have no docValues for ID, so the ID comes back as null in a sort merge, and `QueryComponent.mergeIds` throws an NPE on most queries. The reporter reverted docValues and the error stopped. The reporter's own comment says the schema was changed without reindexing the existing documents.

## Docs (pinned head)

- `solr/solr-ref-guide/modules/indexing-guide/pages/docvalues.adoc:66`: "If you have already indexed data into your Solr index, you will need to completely reindex your content after changing your field definitions in the schema in order to successfully use docValues."
- `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc:67`: the Solr 9 change for adding docValues without reindexing (UninvertDocValuesMergePolicy was an expert utility in Solr 8).

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` mergeIds reads the sort values per shard response; a document without docValues on the ID field has no value to merge. The guide's rule covers this case, so no code change is needed to answer the question.
- The NPE message itself (a missing value turning into a null NPE rather than a clear error) is the one point left. SOLR-10305 is the open ticket for it, and it is not this ticket's question.

## History

- evidence.py at the pinned head: no commits naming SOLR-11768.

## Reproduction attempts

Not run. The answer is in the guide; a run would reproduce only the documented case.

## Gap analysis

The question is "why does the NPE happen after a docValues change". The guide's rule answers it. The one open point, a clearer error message, belongs to SOLR-10305.

## Verdict justification

close-answered meets the rubric: a support question that the reference guide answers, with the page linked (docvalues.adoc, line 66). The rubric's requirement is the link and the sentence that answers the question, both recorded here.

## Limits

This record does not change the NPE text. It does not close SOLR-10305, which is a separate open issue.
