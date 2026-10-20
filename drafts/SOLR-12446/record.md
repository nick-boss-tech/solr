# Skip record: SOLR-12446

- Ticket: SOLR-12446, Misleading client response when IllegalArgumentException is thrown in SolrIndexSearcher. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments.
- Verdict: keep-open.
- Draft branch: solr-12446-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: an IllegalArgumentException raised inside SolrIndexSearcher.buildAndRunCollectorChain on a shard reaches the client as "No live SolrServers available to handle this request", with the shard's error (RemoteSolrException, status 500) only in the root-error metadata. The reporter simulated the exception with a test message and posted the response, which is in the Jira text read through the REST record (the evidence tool output is truncated at 1200 characters).

## Code reading

- solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:280 (buildAndRunCollectorChain) is the named method and still exists at the pin; it is called from the search paths (for example lines 1876, 1906, 1995, 2023).
- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:519-521 and 645-650 (Throwable handling and sendError): a shard-side exception is returned to the caller as an error response with its status (500 for an unexpected exception), not as a 400 or a message naming the exception class as the top-level message.
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:256-258: for a non-v2 request with an error body and a non-200 status, the client throws RemoteSolrException(urlExceptionMessage, httpStatus, error) with skipRetry left false.
- solr/solrj/src/java/org/apache/solr/client/solrj/RemoteSolrException.java:33, 43, 63-65, 143-144: skipRetry defaults to false for that constructor; only the v2 path (HttpSolrClient.java:252) passes true.
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:133 (RETRY_CODES includes 500) and 644-652 (doRequest): a RemoteSolrException with status 500 and skipRetry false is retried, and makeServerAZombie marks the server as a zombie.
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:931-937: when the endpoints are exhausted, the message is built as "No live SolrServers available to handle this request" (with or without the tried counts). This is the ticket's top-level message.

So the chain in the ticket is still present at the pin: a shard-side error becomes a retryable RemoteSolrException, the LB client zombies each server, and the final message is the generic "No live SolrServers" text, with the shard's exception only in the cause chain.

## History

- No commit at the pin names SOLR-12446 (git log --grep at the pin: none).
- Whether any later change adjusted the 500-retry rule or the final message was not traced through file history in this pass.

## Reproduction attempts

Not run. A reproduction needs a two-node SolrCloud at the pin with an injected IllegalArgumentException in SolrIndexSearcher.buildAndRunCollectorChain (or a test hook) and a distributed query that reaches the shard, recording the coordinator's response msg and root-error-class. Not built or run in this pass.

## Gap analysis

The mechanism the ticket describes is present in the code at the pin. A close verdict would need either a fix commit that changes the message or the retry rule, or a run showing the response no longer reads as a dead cluster. Neither was found.

## Verdict justification

keep-open: the mechanism is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. Whether the message should change is a design question (the retry rule for 500s and the wording of the final error).

Missing for a closing verdict: a two-node run at the pin that records the coordinator response for an injected shard exception, and a maintainer decision on whether a shard 500 should be retried and reported as such.

## Limits

This record does not run a SolrCloud reproduction, does not trace the file-history diffs, and does not post anything to JIRA or GitHub.
