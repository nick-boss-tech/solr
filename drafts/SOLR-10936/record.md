# Skip record: SOLR-10936

- Ticket: SOLR-10936, ArrayIndexOutOfBoundsException: -65536 when indexing large document. JIRA read in this pass: Open, resolution none, fix versions none, 6 comments (2017-06-22 to 2017-07-25); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C and L).
- Draft branch: solr-10936-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: indexing a large document with thousands of child documents fails with an exception wrapped as "Exception writing document ... to the index; possible analysis error", the cause being an ArrayIndexOutOfBoundsException with index -65536 in the Lucene indexing buffers (TermsHashPerField). After the failure the IndexWriter is closed. The reporter cannot share the document; the comments point to a spell-token field and a text field whose analysis throws. The first comment suspects signed overflow in TermsHashPerField.

## Code reading

- The failing frame is in Lucene, not in this tree. The pin's gradle/libs.versions.toml:39 sets apache-lucene = "10.4.0", and Lucene sources are not in the fork (see the SOLR-14497 removal recorded in the audit notes).
- The Solr-side wrapper is present: solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:419-445 (addDoc) rethrows a RuntimeException as "Exception writing document id N to the index" with BAD_REQUEST unless the core has a tragic exception, which matches the message in the ticket.
- The -65536 value is not a Solr constant (the only 65536 in solr/core/src/java are the spelling flag at spelling/QueryConverter.java:54 and buffer sizes in TransactionLog.java:215 and 236, none of which is the indexing buffer).

So the defect the ticket reports was in the Lucene 6.x indexing code, and the question whether Lucene 10.4.0 still has it is not answered by this tree.

## History

- No commit at the pin names SOLR-10936 (git log --grep at the pin: none).
- The Lucene upgrade history that replaced the 6.x indexing buffers was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a document of the reporter's shape (thousands of child documents, text fields with spelling-token analysis) indexed at the pin (Lucene 10.4.0), or a synthetic document with many tokens and a large number of positions per field, recording whether the write fails with the wrapped exception.

## Gap analysis

The failure site is in Lucene 10.4.0 (external), and the Solr wrapper that reports it is present. Whether the overflow still exists in Lucene 10.4.0 is an open question this tree cannot answer by reading.

## Verdict justification

keep-open: the reported defect's location is an external dependency, the pinned Lucene (10.4.0) has not been run against the ticket's shape of document, and no commit in this repo closes or removes it. close-obsolete is not applicable: the code the ticket names was never in this tree, so there is no removal to check. close-fixed is not supported: no Solr fix commit names the ticket.

Missing for a closing verdict: a reproduction at the pin (Lucene 10.4.0) that either fails (confirm the open defect and route to Lucene) or passes on a large-document test (close-cannot-reproduce).

## Limits

This record does not run an indexing test, does not inspect Lucene 10.4.0 sources, and does not post anything to JIRA or GitHub.
