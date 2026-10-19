# Skip record: SOLR-17086

- Ticket: SOLR-17086, "v2 Backup and Replica creation: HTTP 500 error messages". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-11-28, updated 2024-01-18, no fix version. Two comments: gerlowskija (2023-12-14) could not reproduce; ozlerhakan (2023-12-19) gives a reproduction attempt on a two-node cluster with replica creation through the v2 API.
- Verdict: keep-open. This replaces the earlier vm5 closeout (DONE, pre-remediation), which recorded keep-open in the queue but cited a close draft (`solr-17086-draft-close`). This record does not use that close draft.
- Draft branch: solr-17086-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:19-14:20 UTC.

## Ticket reading

Two v2 API problems on 9.4: a backup request sometimes returns "An exception was not mapped due to exception mapper failure. The HTTP 500 response will be returned", although the backup completes; and replica creation returns a similar 500. The ticket names a reported NPE in a filter. The reporter's stack trace is in the ticket text.

## Code reading

- `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java:67-69`: the filter reads `SOLR_QUERY_REQUEST` and guards it with `(solrQueryRequest != null) ? ... : null` (line 69), with a TODO asking whether a null request is valid. The filter NPE the ticket describes is guarded at the pin.
- `solr/core/src/java/org/apache/solr/jersey/CatchAllExceptionMapper.java:70-71`: reads `SOLR_QUERY_REQUEST` and passes it to `processAndRespondToException` (the call at the end of the method). There is no null check on that value in the mapper.
- `solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java:325-327`, `processReceivedException(SolrQueryRequest req, ...)`: the first statement is `SolrCore core = req.getCore();`. A null `req` throws here. That is the unguarded path: if the mapper receives a null request, the mapper itself fails, which matches the ticket's "exception mapper failure" wording.
- `solr/core/src/java/org/apache/solr/api/V2HttpCall.java:326-327`: sets `SOLR_QUERY_REQUEST` and `SOLR_QUERY_RESPONSE` on the Jersey container request for the v2 path.

What this shows at the pin: the filter NPE is covered, but the mapper still dereferences a request that can be null, with no guard. Whether the ticket's failures reach that path depends on whether the request property is unset for some request, which the V2 path sets before dispatch. The reachability of a null request is not established by this reading.

## History

- No commit on the pinned head names SOLR-17086 (evidence tool: no hits).
- SOLR-15751 is named in the queue note as the change that fixed the filter NPE as a side effect. Not traced in this pass.

## Reproduction attempts

Not attempted in this pass. The two comments record earlier attempts (gerlowskija could not reproduce; ozlerhakan's two-node run is described in the ticket, its result is not stated). This pass is review-only and runs no builds.

## Gap analysis

The filter NPE the ticket names is guarded. The mapper's unguarded dereference is a residual path, and it is the plausible site for the "exception mapper failure" message. Reachability is the open question: a request that reaches the mapper without `SOLR_QUERY_REQUEST` set, such as one on an async or error path. The backup case in the ticket is async, which is a candidate for that path, but the ticket does not show it.

Missing piece for a verdict change: the full stack trace from the ticket's backup failure (to confirm the NPE site is `processReceivedException`), and either a reproduction that reaches the mapper with a null request, or a code argument that such a request cannot occur.

## Verdict justification

keep-open meets the rubric: the code path is read with file and line, the earlier attempts are named, and the missing piece is named (the stack trace and a reachability check). close-obsolete and close-fixed are not met: the mapper path is still present unguarded, and no fix commit is identified. pr-needed is not met as a shown defect: the unguarded dereference is latent, and reachability is not established. A change is possible (a null guard in the mapper or in `processReceivedException`) and would need a test; that decision is for the owner after the reachability check.

## Limits

This record does not reproduce the 500 and does not establish reachability. It does not confirm the SOLR-15751 side effect. It does not post anything to JIRA or GitHub. The earlier vm5 close draft is not endorsed here.
