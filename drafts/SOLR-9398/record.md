# Skip record: SOLR-9398

- Ticket: SOLR-9398, "deleteByQuery blocks during an optimize". JIRA status read 2026-10-20T14:29:11Z (UTC): Open, resolution None, updated 2019-06-08. Linked: LUCENE-7049 (Open).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9398-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:29:11Z (UTC).

## Ticket reading

A deleteByQuery blocks until a merge (especially an optimize) finishes, and other index changes wait behind it. A delete by ID of the same documents does not block. The reporter thinks Solr or Lucene could fix it; the one comment says the issue may need to move to Lucene.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:670-740` (`deleteByQuery`): the path holds `solrCoreState.getUpdateLock()` (synchronized blocks around lines 700-717 and the writer call near line 727-728, `iw.get().deleteDocuments(new DeleteByQueryWrapper(q, ...))`). The delete-by-query runs inside the update lock.
- A delete by ID does not run a query, so it does not take this path; that matches the reporter's observation.

## History

- evidence.py at the pinned head: no commits naming SOLR-9398.

## Reproduction attempts

Not run. A run would start a large merge (optimize) on an index and time a deleteByQuery against a deleteById of the same documents.

## Gap analysis

The lock is held across a Lucene call that runs the query and waits on the writer. Whether the wait is in Solr's lock or Lucene's merge is the open question the ticket and the LUCENE-7049 link raise. The timing run decides which.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of DirectUpdateHandler2.java:670-740; the missing piece is the timing run and the Solr-or-Lucene attribution. close-fixed and close-obsolete are not supported: the path is present. close-cannot-reproduce is not supported: no run was made.

## Limits

This record does not measure the blocking time. It does not change the Lucene code.
