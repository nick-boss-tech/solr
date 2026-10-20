# Skip record: SOLR-10974

- Ticket: SOLR-10974, Replication - Unable to download tlog. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2017-06-29 and 2017-07-10, support and steps to add a replica); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and C).
- Draft branch: solr-10974-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: after adding a replica by the admin UI, replication copies the data and then repeats indefinitely. The log shows "Unable to download tlog.0000000000002131263.1571535118797897728 completely. Downloaded 0!=871". The first comment asks the reporter to use the user list; the second gives the steps to add a replica.

## Code reading

- solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:1777-1795 (FileFetcher.cleanup): if bytesDownloaded differs from the expected size, the partial file is deleted and, unless the fetch was aborted or the generation is invalid, a SolrException SERVER_ERROR "Unable to download <fileName> completely. Downloaded N!=size" is thrown. This is the ticket's message.
- IndexFetcher.java:406-416 (fetchLatestIndex): the only retry is for InvalidIndexGenerationException (the leader's generation changed); the tlog size mismatch is a SolrException, so it is not retried here and propagates to the caller.
- The tlog files are downloaded in downloadTlogFiles (called from fetchLatestIndexOnce; the call site was not traced line by line in this pass). The repeated attempts in the ticket are consistent with the outer replication schedule re-running after the failure.

So the failure path in the ticket is present at the pin: a tlog file whose size differs from the advertised size fails the fetch, and nothing in the fetcher retries that case itself.

## History

- No commit at the pin names SOLR-10974 (git log --grep at the pin: none).
- The file history of IndexFetcher.java was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a leader with active indexing (the tlog is being written), a replica added through the replication handler, and a fetch that copies a tlog whose size changes during the copy, recording the "Unable to download ... completely" message at the pin.

## Gap analysis

The code path matches the ticket's message. What is not established is why the advertised tlog size differs from the bytes copied in the reporter's setup (for example, a tlog still being appended to, or a size read before the copy). A run with live indexing would show whether the size mismatch occurs.

## Verdict justification

keep-open: the failure path is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. The support comments do not answer the question.

Missing for a closing verdict: a replication run at the pin with live indexing, recording whether the tlog size mismatch occurs and whether the fetch repeats indefinitely; zero occurrences would support close-cannot-reproduce.

## Limits

This record does not run replication, does not trace the downloadTlogFiles call site line by line, and does not post anything to JIRA or GitHub.
