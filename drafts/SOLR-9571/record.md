# Skip record: SOLR-9571

- Ticket: SOLR-9571, "Time-allowed detection logic in LBHttpSolrClient should return a fake partial results response". JIRA status read 2026-10-20T13:29:30Z (UTC): Open, resolution None, updated 2019-06-08. No comments.
- Verdict: keep-open (kept; re-read at the pinned head). The requested fake partial response is not implemented; the misleading message was changed.
- Draft branch: solr-9571-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:29:30Z (UTC).

## Ticket reading

When the time allowed for a request runs out in the load-balancing client, the client should return a fake response with a partial-response header, as if a node had hit the timeout. Today it throws "no live servers available", which is misleading.

## Code reading (pinned head)

- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:421-428`: when the time is exceeded (`isTimeExceeded`, line 421) the client throws `SolrServerException` with the message "The processing limits for to this request were exceeded, see cause for details", with the previous exception as cause. The "no live servers" message is no longer thrown for this case.
- No code in this path builds a partial-results response: a grep for `partial` in `LBSolrClient.java` shows only comments and the time-limit names.

## History

- evidence.py at the pinned head: no commits naming SOLR-9571.

## Reproduction attempts

Not run. A run would set a short `timeAllowed` on a request to a load-balanced client with all nodes slow and check the exception and the response.

## Gap analysis

The complaint about the misleading text is partly addressed (the message now names the processing limits). The fake partial response itself is not implemented, and whether it should be a client-side fake is a design choice.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of LBSolrClient.java:413-436 and the grep for partial responses; the missing piece is the design decision on a client-side partial response, and a run of the time-limit path. close-fixed is not supported: the requested response is not implemented. close-obsolete is not supported: the path is present.

## Limits

This record does not run the time-limit case. It does not check the server-side timeAllowed partial response, which is a different path.
