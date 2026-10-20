# Skip record: SOLR-10712

- Ticket: SOLR-10712, UpdateRequest.setDocIterator() writes the payload in memory before sending to server. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2022-10-14, a maintainer asking whether the issue is overcome by events); updated 2022-10-14.
- Verdict: close-fixed (unchanged status; this record replaces the earlier record on this branch, which cited the same fix commit without checking the writer at the pin).
- Draft branch: solr-10712-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: the javabin update path builds the whole request body in a ByteArrayOutputStream (BAOS) through a ContentStream before sending it, so a large set of documents (setDocIterator) is buffered in client memory and can cause OutOfMemoryError. The 2022 comment asks whether the setDocIterator path is already streamed.

## Code reading

- solr/solrj/src/java/org/apache/solr/client/solrj/request/JavaBinRequestWriter.java:34-50 (getContentWriter): for an UpdateRequest, returns a ContentWriter whose write(OutputStream os) calls new JavaBinUpdateRequestCodec().marshal(updateRequest, os) directly on the output stream. No BAOS buffer and no ContentStream are created on this path.
- The ContentStream path with getbuf() that the ticket quotes is not in the tree: git grep -- solr for a BAOS-backed getContentStream on UpdateRequest returns no match in solrj's request package.
- solr/solrj/src/java/org/apache/solr/client/solrj/request/MultiContentWriterRequest.java:124 still uses a Utils.BAOS, in readByteBuffer, which reads an input stream into a buffer for a different purpose (response reading), not the update write path.
- solr/solrj/src/java/org/apache/solr/client/solrj/request/UpdateRequest.java:349 (setDocIterator) is present, and the writer above streams the request it carries.

So the write path the ticket describes now marshals to the output stream.

## History

- Commit 78862e7f54 (SOLR-18351, 2026-09-02, "remove ContentStream from SolrJ's client write path (#4811)") is an ancestor of the pin (checked with merge-base). It is the change that removed the ContentStream-based write path the ticket quotes.
- No commit at the pin names SOLR-10712 (git log --grep at the pin: none); the linkage to this ticket is by code path, since the commit subject does not cite it.

## Reproduction attempts

Not run in this pass. A reproduction would send a large setDocIterator update through the client at the pin and record the client heap use (or confirm streaming with a heap profile). The C scope of the earlier record ran no builds or tests.

## Gap analysis

The code path the ticket names is gone at the pin and the update is written to the output stream. The change is linked by code path, not by a ticket reference, and the memory behavior was not measured.

## Verdict justification

close-fixed: the fix commit 78862e7f54 is on main at the pin (ancestor checked), and the changed code (JavaBinRequestWriter.java:34-50 writing to the output stream, and the absence of the buffered ContentStream) is present at the pin. The rubric's close-fixed check is met, with linkage by code path.

Caveat for the owner: the close should cite 78862e7f54 and state that the memory behavior was not measured in this pass.

## Limits

This record does not run a large update or a heap measurement, does not check the 2022 maintainer question beyond the comment text, and does not post anything to JIRA or GitHub.
