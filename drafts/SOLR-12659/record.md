# Skip record: SOLR-12659

- Ticket: SOLR-12659, HttpSolrClient.createMethod does not handle both stream and large data.
- Verdict: keep-open.
- Draft branch: solr-12659-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 7 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-08-11, updated 2019-06-08, no fix versions, no links. The report: a `ContentStreamUpdateRequest` that carries a stream (`addContentStream()`) sends all its other parameters on the URL, and the server rejects the request with "URI is too large". The reporter places the cause in `HttpSolrClient.createMethod()`, where the presence of stream data is not taken into account. One comment (2018-08-12, elyograg): the fix needs a low-level change to how Solr handles requests and how SolrJ builds them.

## Code reading

Read at the pin:

- `HttpSolrClient.createMethod` and `ContentStreamUpdateRequest` have no hits under `solr` (`git grep -l createMethod` and `git grep -l ContentStreamUpdateRequest` return nothing). The Apache HttpClient client that held the reporter's `createMethod` was moved out of main by SOLR-17962, and `ContentStreamUpdateRequest` was removed by 78862e7f54 (SOLR-18351), so the named code is gone.
- The behavior is still present in the current JDK client, `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpJdkSolrClient.java`:
  - `HttpJdkSolrClient.java:316-318`: when a `contentWriter` (stream) is present, the body is the stream (`BodyPublishers.ofInputStream`), and `queryParams` is not reduced.
  - `HttpJdkSolrClient.java:336-337`: `reqb.uri(new URI(url + queryParams.toQueryString()))` builds the URI from all `queryParams`, so the stream case sends every parameter on the URL.
  - By contrast, the no-stream branch (`:319-326`) moves the URL parameters out and puts the rest in the body.
- The current client refuses multipart (`HttpJdkSolrClient.java:302-304`, "This client does not support multipart."), so the usual fix (send the parameters in the body with the stream) is not available as is.

## History

- SOLR-17962 (`cf7f5079e8`) and 78862e7f54 (SOLR-18351): ancestors of the pin; the removed classes named by the old reading.
- No commit at the pin names SOLR-12659 (the evidence tool lists none).

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is a stream request with a long parameter set through `HttpJdkSolrClient` at the pin, showing the URI length and the server's response.

## Gap analysis

- The reading confirms the stream-plus-parameters path still puts all parameters on the URL in the JDK client.
- A fix needs either a body-based request for stream content (which the client does not support today without multipart) or a server-side change, which is the design question the ticket's comment raises.
- The old named code is gone, which does not close the bug in the current client.

## Verdict justification

keep-open: the code path that produces the long URI is present in the current client, but the fix is a design change (request body or multipart for stream content) that the ticket does not settle, and no run has been made. close-obsolete does not fit (the behavior exists in the current client, though the named class is gone). pr-needed needs the design decision.

Missing for this verdict: the maintainer decision on sending stream requests' parameters in the body (or multipart support), and a run of a long-parameter stream request through the JDK client at the pin.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
