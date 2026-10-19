# Skip record: SOLR-17086

- Ticket: SOLR-17086, "v2 Backup and Replica creation: HTTP 500 error messages". JIRA status Open, resolution none, created 2023-11-28, last updated 2024-01-18, checked 2026-10-19 UTC.
- Verdict: close-fixed.
- Draft branch: solr-17086-draft-close.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Two v2 API problems on 9.4. The first (the one with a fix candidate) is an HTTP 500, "An exception was not mapped due to exception mapper failure", on a v2 backup request. The stack trace shows a `NullPointerException` in `MediaTypeOverridingFilter.filter` (via `V2ApiUtils.getMediaTypeFromWtParam`) because `solrQueryRequest` is null. The backup itself completes. Comments: gerlowskija (2023-12-14) could not reproduce; ozlerhakan (2023-12-19) gives a setup: a two-node SolrCloud on Kubernetes, a collection with rf=1 and a v2 backup request.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java:69`: `final var params = (solrQueryRequest != null) ? solrQueryRequest.getParams() : null;` The null guard that the ticket's NPE needs.
- `solr/core/src/java/org/apache/solr/handler/api/V2ApiUtils.java:92`: `getMediaTypeFromWtParam(params, ...)` is called only with the non-null params from line 69.
- `solr/core/src/java/org/apache/solr/api/V2HttpCall.java:326`: `containerRequest.setProperty(RequestContextKeys.SOLR_QUERY_REQUEST, solrReq)` sets the request property on each Jersey request, which the filter reads.

## History

- `c058ea5dac` (2024-12-27), "SOLR-15751: Add v2 COLSTATUS and \"segments\" APIs (#2912)". It changes `MediaTypeOverridingFilter.java` (6 lines). Its parent (`c058ea5dac^`) has the unguarded call `solrQueryRequest.getParams()` at line 68, the NPE site in the ticket. The commit adds the null guard at line 69. Ancestor of the pinned head: yes (`git merge-base --is-ancestor`).
- The commit does not name SOLR-17086. The guard is part of a change for other APIs; its effect on this ticket's NPE is shown by the before and after lines above.
- No commit on the fork names SOLR-17086 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted on the pinned head. The reporter's setup (a Kubernetes cluster, S3 repository, a v2 backup request on a collection with rf=1) is not something a closeout run reproduces. The code-path check is enough for close-fixed: the NPE line is shown in the parent commit and is guarded on main. Name of the test that would show the guard working: none in the fork for this path (not searched).

## Gap analysis

The NPE in the ticket is the unguarded `solrQueryRequest.getParams()` at the parent commit. At the pinned head the same line is guarded, so the NPE cannot occur there. The ticket's second problem (the stack trace in the first part of the description is the only one reported as HTTP 500) is not separately addressed in this record. The reporter's backup-completes observation is unchanged by this fix.

## Verdict justification

close-fixed: "A commit on main fixes the reported problem, and the fixed code is still on main." Minimum evidence: the fixing commit sha (`c058ea5dac`), its ancestry on the pinned head (checked), and the changed code present at the head (`MediaTypeOverridingFilter.java:69`). The commit is not labeled with the ticket key; the record says so, and the closeout relies on the before and after lines rather than the subject.

## Limits

This record does not reproduce the reporter's HTTP 500 on any release. It does not check the 9.4 branch. It does not check the second problem in the ticket beyond what the description says. It does not search for a unit test that covers the guard.
