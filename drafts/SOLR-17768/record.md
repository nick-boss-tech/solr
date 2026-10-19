# Skip record: SOLR-17768

- Ticket: SOLR-17768, "Solr client might not be closed in AdminHandlersProxy". JIRA status Open, resolution none, created and last updated 2025-05-27, checked 2026-10-19 UTC.
- Verdict: close-obsolete.
- Draft branch: solr-17768-draft-close.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The ticket reports that in `AdminHandlersProxy`, the `SolrClient` created for each node's response is closed only on the success path, so an `ExecutionException` or `TimeoutException` leaves it open. The code shown is the loop that fetches each node's future, closes its client, and adds the result. No comments, no linked issues, no test named.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `git ls-tree` on `solr/core/src/java/org/apache/solr/handler/admin/AdminHandlersProxy.java` at the pinned head: the file does not exist.
- `git grep -n "AdminHandlersProxy" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/` returns one hit: `solr/solrj/src/java/org/apache/solr/client/solrj/response/SystemInfoResponse.java:33`, a comment that names the old class. No code at the pinned head uses it.
- Replacement: `solr/core/src/java/org/apache/solr/handler/admin/proxy/RemoteRequestProxy.java`. `callRemoteNode` (around lines 121-140) sends the request with `requestAsync` through `zkController.getCoreContainer().getDefaultHttpSolrClient()`, a shared client that the proxy does not create or close per node. `bulkProcessResponses` (lines 93-110) reads each future with a 10 second timeout and logs failures; it creates no client to close.
- The per-node `SolrClient` the ticket describes no longer exists at the pinned head.

## History

- `730398a940` (2026-04-27), SOLR-16738: "Refactor AdminHandlersProxy for better extensibility (#3991)". It deletes `AdminHandlersProxy.java` and `AdminHandlersProxyTest.java` and adds the `proxy` package (`RemoteRequestProxy`, `GenericV1RequestProxy`, `V2SolrRequestBasedProxy`). Ancestor of the pinned head: yes.
- The commit does not name SOLR-17768. The ticket's leak was removed as a side effect of the refactor, not fixed in place.

## Reproduction attempts

Not attempted. The code that the report describes is not on main, so a reproduction at the pinned head would test the replacement code, not the reported path. The close-obsolete verdict does not require one.

## Gap analysis

The reported leak needs a per-node `SolrClient` that is created and then closed only on success. At the pinned head, the proxy calls a shared client through `requestAsync`, and no code path creates a client per node. The class that had the leak no longer exists, and nothing in the solr/ tree uses it (the only hit is a comment).

## Verdict justification

close-obsolete: "the feature, class or code the ticket is about has been removed, and a search of main finds no remaining use." Minimum evidence: the pinned head sha (above); the removing commit `730398a940` and its ancestry (above); the grep and its result. The grep result is not empty: it returns one comment in `SystemInfoResponse.java:33`. The rubric asks for no remaining use, and that comment is not a use. The record discloses the hit rather than hiding it.

Alternative considered: close-fixed. Not chosen. `730398a940` removed the class as part of a refactor; the commit does not fix the reported close-on-error path in place.

## Limits

This record does not establish whether the replacement code has its own client-lifecycle issues. It does not check the released branches. It does not run tests. The stale comment in `SystemInfoResponse.java:33` remains; removing it is a separate change and is not part of this closeout.
