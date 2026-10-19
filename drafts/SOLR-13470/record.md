# Skip record: SOLR-13470

- Ticket: SOLR-13470, SolrException msg not always propagated to HttpClient if exception occurs during response writing.
- Verdict: keep-open.
- Draft branch: solr-13470-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) and the earlier record (vm2, 2026-10-19, checked against upstream main c8ab71430f) are replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-14, updated 2019-06-08, no fix versions. Links: problem/incident SOLR-13471 (Open), problem/incident SOLR-13474 (Closed). The description says that because of SOLR-13471, a client reading a javabin response may not see the `error` block, so the RemoteSolrException it builds has no correct message. The 2019-05-14 comment (hossman) attaches a patch that hardens assertions on the expected exception.

## Code reading

Read at the pin:

- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:250-262`: the client parses the response with `processor.processResponse`; a parse failure becomes `RemoteSolrException(..., e.getMessage(), e)` at :250-253. It reads `rsp.get("error")` at :254. When the status is not 200 and `error` is null, it throws `RemoteSolrException` with the generic text "non ok status: <code>, message: <reason>" and no cause at :259-262. A missing `error` block therefore gives the generic message, which is the ticket's symptom.
- The `error` block is written by the server's response writer, which is the path SOLR-13471 concerns (`solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:513-520`, `JavaBinResponseWriter.java:52-62`).
- The SOLR-13474 trigger: commit `6cc67e433e` (2019-05-20, "Fix 'Search is temporarily disabled' logic to be consistent for entire request") changes `SolrCore.java`, `SolrQueryRequestBase.java`, `DefaultSolrCoreState.java` and one test. It does not touch the response writer or the `error` block. So the audit's note that the trigger was fixed by SOLR-13474 is not supported by that commit at the pin; the record does not rely on it.

## History

- `6cc67e433e` (SOLR-13474): ancestor of the pin; not the response-error fix (see above).
- No commit at the pin names SOLR-13470 (evidence tool: none).

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The ticket depends on SOLR-13471 (still Open, record keep-open). If the response writer leaves a missing or unreadable `error` block after a mid-write failure, the client-side fallback at `HttpSolrClient.java:259-262` gives the generic message. Whether that happens on the pin is the open run question in SOLR-13471.
- The audit's close-side reasoning (duplicate, trigger fixed by SOLR-13474) does not hold: the cited trigger commit is about a different logic. So the verdict cannot be closed as a duplicate of a fixed trigger.
- Missing piece: a run that makes the response writer fail mid-write and checks the message a SolrJ client receives. It is the same run SOLR-13471 needs, plus the message assertion.

## Verdict justification

keep-open: the ticket is a symptom of SOLR-13471, which is itself open; the client code path that yields the generic message is present at the pin; no run shows the message on the pin, and the audit's duplicate-of-a-fixed-trigger argument is not supported at the pin.

Missing for a closing or PR verdict: the mid-write failure run with its client-side message, shared with SOLR-13471.

## Limits

This record does not establish a reproduction. It does not check the upstream commit cited in the audit. It does not post anything to JIRA or GitHub.
