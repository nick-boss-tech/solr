# Skip record: SOLR-10917

- Ticket: SOLR-10917, Port starvation issue in Luke. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; updated 2019-06-08.
- Verdict: close-fixed (unchanged status; this record replaces the earlier record on this branch, which cited the same fix commit but had not checked the test or the proxy path at the pin).
- Draft branch: solr-10917-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: a Luke request for a collection whose replicas are all down is proxied from node to node and rejected by each, so one request multiplies into thousands of forwarded requests and starves the ports of the cluster (the reporter saw 10000 requests in under a second). The log line is "Error trying to proxy request for url: .../config" from the request-forwarding path.

## Code reading

- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:117: the forwarding counter parameter _forwardedCount (INTERNAL_REQUEST_COUNT).
- HttpSolrCall.java:913-916: totalReplicas is the count of replicas across the collection's slices.
- HttpSolrCall.java:930-933: a request whose forwarded count exceeds totalReplicas is stopped with INVALID_STATE "No active replicas found for collection"; this is the loop limit the ticket's flood needs.
- HttpSolrCall.java:638: the "Error trying to proxy request for url" message from the proxy path is still present at the pin.
- Commit b8648c60e7 (SOLR-13793, 2019-10-15, "Limiting number of forwards to total replicas in collection to avoid deadly forwarding loops") is an ancestor of the pin (checked with merge-base); its diff adds the limit and adds solr/core/src/test/org/apache/solr/cloud/TestQueryingOnDownCollection.java, which is present at the pin.

## History

- The fix commit's subject does not cite SOLR-10917; the linkage is by code path (the forwarding loop for a down collection) and by the test for the down-collection case.
- No commit names SOLR-10917 (git log --grep at the pin: none).

## Reproduction attempts

Not run in this pass. TestQueryingOnDownCollection at the pin is the test that covers the down-collection forwarding case; running it would be the reproduction check. The class of request in the ticket (Luke on a down collection) is the case the limit guards.

## Gap analysis

The changed code is present, and the limit stops the forwarding recursion the ticket describes. The ticket's exact method name (remoteQuery) no longer exists at the pin, and the proxy path now sits in HttpSolrCall around line 638; the behavior is the same path under a different name.

## Verdict justification

close-fixed: a fix commit (b8648c60e7) is on main at the pin (ancestor checked), and the changed code (HttpSolrCall.java:930-933) is present at the pin. The rubric's close-fixed check is met, with linkage by code path because the commit does not cite the ticket.

Caveat for the owner: the close should cite b8648c60e7 and state that the test was not run in this pass.

## Limits

This record does not run TestQueryingOnDownCollection, does not reproduce the flood, and does not post anything to JIRA or GitHub.
