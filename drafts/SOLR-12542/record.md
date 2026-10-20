# Skip record: SOLR-12542

- Ticket: SOLR-12542, Trying to export on a non doc-values field gives no response. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments.
- Verdict: keep-open.
- Draft branch: solr-12542-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: the export handler, sorting (sort=a_b desc) on a boolean field without DocValues, returns HTTP 500 with an empty body; the server log shows "a_b must have DocValues to use this feature". The skip note from the queue says it is a duplicate of SOLR-12543 and that the empty body was fixed (now an EXCEPTION document in JSON) with HTTP 200 remaining.

## Code reading

- solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java:634: getSortDoc throws IOException(field + " must have DocValues to use this feature.") for a sort field whose schema field has no DocValues. Same message as the ticket.
- solr/core/src/java/org/apache/solr/handler/export/ExportBuffers.java:97: the constructor calls exportWriter.getSortDoc, so the error is raised while writeDocs builds the buffers (ExportWriter.java, writeDocs, the ExportBuffers construction).
- solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java:176-193 (writeException) writes an EXCEPTION document with status 400, but it is called only for the field-list, stream-expression, and scoring errors, not for this sort-time error.
- solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java:195-347 (_write): the only catch around the writeMap that calls writeDocs is for EOFException (line 348). The IOException from getSortDoc therefore leaves _write uncaught and is not written as an EXCEPTION document at this pin.
- solr/core/src/test/org/apache/solr/handler/export/TestExportWriter.java:1725-1750 (testSortingWithoutDocValues): the test expects an IOException from h.query for a sort on a stored-only field, which matches the code path above: the error escapes the writer rather than becoming a JSON body.

So the skip note's claim that the empty body was fixed by an EXCEPTION document does not hold for this sort path at the pin. The EXCEPTION document path (writeException) covers the other errors, not this one.

## History

- The DocValues sort check was not changed by a fix commit that names SOLR-12542 (git log --grep at the pin: no commit names SOLR-12542).
- The commit that added the stored-field support for the export writer, 45adb11a9e (SOLR-18071), is an ancestor of the pin and is the most recent change to ExportWriter.java in the file history at this pin for the sort path; it does not change getSortDoc (checked by the file history at the pin).
- SOLR-12543 is named by the skip note; this record does not re-check it.

## Reproduction attempts

Not run. A live request (start Solr at the pin, index a boolean field without DocValues, query /export with sort on that field, and record the HTTP status, Content-Type, and body) would settle whether the client still gets an empty 500 body or a partial 200 body. Not done in this pass.

## Gap analysis

The code at the pin still raises the ticket's error from getSortDoc and does not write it as a JSON EXCEPTION document for this sort case, so the ticket's symptom (no readable error for the client) appears still present in code. Whether the HTTP response is an empty 500 or a partial 200 body is not established without a live request. Neither a fix commit that closes the ticket nor a removed-code path was found, so no close verdict is supported.

## Verdict justification

keep-open: the code path at the pin reproduces the ticket's error message and its non-EXCEPTION delivery, so close-fixed and close-obsolete are not supported; close-cannot-reproduce needs a run attempt, which is not done here.

Missing for a closing verdict: a live HTTP request at the pin that shows the client response for the sort-without-DocValues case, and the status of SOLR-12543 if the duplicate is to be closed with it.

## Limits

This record does not run a live request, does not re-check SOLR-12543, and does not post anything to JIRA or GitHub.
