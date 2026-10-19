# Skip record: SOLR-13481

- Ticket: SOLR-13481, Re-try of solr request will not happen with different live servers, if one request throws Exception.
- Verdict: keep-open. This replaces the earlier close-obsolete verdict on solr-13481-draft-close (8311ae5d82).
- Draft branch: solr-13481-draft-open (new branch from origin/coordination; the close draft is left in place).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier B2 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-19, updated 2019-06-08, no fix versions, no links. The description says `LBHttpSolrClient.request` stops trying other servers when `doRequest` throws, so the request fails even though other live servers exist. The 2019-06-06 comment (dsmiley) asks for a link to the code rather than pasted code, and for the problem to be described.

## Code reading

Read at the pin:

- The class `LBHttpSolrClient` is not defined. `git grep -c 'class LBHttpSolrClient' ... -- '*.java'` matches only `LBHttpSolrClientBadInputTest`. The successor is `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:123` (abstract), with the Jetty implementation `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/LBJettySolrClient.java:29` (`extends LBAsyncSolrClient`). `HttpJettySolrClient.java:565` builds `LBJettySolrClient`.
- The failover loop is in `LBSolrClient.request` (`LBSolrClient.java:571-598`): it loops over the endpoint iterator and returns on a null exception from `doRequest`; otherwise it continues to the next endpoint, and throws `SolrServerException` (no live servers) only when the iterator is exhausted.
- `LBSolrClient.doRequest` (`:633-686`) returns an exception, so the loop continues, for: a SolrException with a retry code (`:650`), a SocketException unless the request is non-retryable (`:660`), a SocketTimeoutException (`:666`), and a SolrServerException with an IO or timeout root cause (`:674`), plus connect and request-not-sent cases (`:680`).
- The same method throws out of the loop, which ends failover, for: a non-retryable SolrException (`:652-657` area, the `throw e` branch), a non-retryable SocketException or timeout, a SolrServerException without a retryable cause (`:682-683` area), and every other Exception, which is wrapped as `SolrServerException` at `:684-685` (`} catch (Exception e) {` is at :684).

So the ticket's pattern, a thrown exception ending the loop, still exists at the pin for any exception that is not classified as retryable, including the generic `catch (Exception e)` at :684.

## History

- No commit at the pin names SOLR-13481 (evidence tool: none).
- The `catch (SocketTimeoutException e)` text first appears in `f80e8e1167` (2018-12-16, a merge of the http2 branch) by a pickaxe search. The history of the later catch changes was not traced in this pass.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- close-obsolete does not hold. The class is renamed (`LBSolrClient`, `LBJettySolrClient`) and the failover loop is still present. A rename is not the removal close-obsolete requires.
- close-fixed does not hold: no commit names the ticket, and the generic catch still ends the loop.
- Whether a generic, non-IO exception from one server should fail over to the next is a design choice, and the code's current split (retry on IO and retry codes, fail on everything else) is deliberate in its comments at `:644-649`. The ticket asks for a different split.
- Missing piece: a run that makes one server throw a non-IO exception (for example, a RuntimeException from the client) while a second live server answers, on the pin. The result would show whether the request fails when another server would succeed. No such run is recorded.

## Verdict justification

keep-open: the failover loop is present under the new class names, the generic exception path still ends it, and a run is needed to show whether that is the reported failure. The earlier close-obsolete verdict is withdrawn because its premise, that the class was removed with no remaining use, is not the whole picture: the behavior the ticket asks about is still in code.

Missing for a closing or PR verdict: the probe run described above, with the seed or input, the result, and the log path.

## Limits

This record does not establish a reproduction. It does not test the classification of exceptions beyond reading it. It does not post anything to JIRA or GitHub.
