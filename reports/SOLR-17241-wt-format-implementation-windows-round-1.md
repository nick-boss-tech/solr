# SOLR-17241 wt response-format implementation, Windows round 1

Status: IMPLEMENTED, PROOF PENDING. Public comment HOLD by Nick's direction.

- Implementation claim: claims/SOLR-17241-wt-format-implementation-windows-round-1.md at 8604d52eafa49b5eb52df783dca892b769fe787c.
- Review: receipts/solr-wt-non-hosting-review-round-1.md; JIRA search appendix reports/solr-wt-non-hosting-jira-search-round-1-windows.md. Existing Open SOLR-17241 is the recommended eventual public thread; no new ticket, comment or PR was posted.
- Base: upstream/main 9859d8bb2e8d63061116b8281639a4beb1be480c. Relevant V2HttpCall/filter/integration code is unchanged from the reviewed main 852b427c1154ce9eec988b9adaeb212294442126.
- Pushed fork branch: solr-17241-submit, bded8997e57e7cc0974aa766251735f93f289f87. Author/committer Nick Shanin. GitHub ref separately read back and matched. One commit, four outbound files, 249 insertions and two deletions.
- Linux handoff: gates/SOLR-17241-wt-format-round-1.md. Required receipt: receipts/SOLR-17241.md. No runtime proof yet.

## Change and architectural fit

On a non-hosting node V2HttpCall may invoke the local collection-level Jersey resource without a SolrQueryRequest. The filter previously treated that as missing params and selected JSON even when SolrJ sent wt=javabin. The operation could complete before SolrJ rejected the response MIME type.

MediaTypeOverridingFilter now retains SolrQueryRequest params precedence and uses the already-populated SOLR_PARAMS property when that request object is absent. This reuses the existing parsed query-param transport and fixes the response-negotiation consumer. It does not manufacture a core request, change proxy/routing decisions, reparse a body or introduce an API/dependency. Error responses and resource exemptions still bypass the override. Existing wt/Accept/default behavior remains intact.

SOLR_PARAMS is already consumed by Jersey's injection path and resources; the reviewed current V2HttpCall unconditionally sets it alongside the nullable SOLR_QUERY_REQUEST. The patch is limited to format negotiation and introduces no authentication, authorization, network-destination or persistence boundary.

## Outbound files and verification design

1. solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java: missing-request fallback to SOLR_PARAMS.
2. solr/core/src/test/org/apache/solr/jersey/MediaTypeOverridingFilterTest.java: eight focused methods using the existing SolrTestCase and Mockito dependency. Real header maps model response mutation. Covers formats, precedence, Accept/defaults, absent params, errors/exemptions and blank/unknown wt. No new reusable helper or cluster fixture.
3. solr/core/src/test/org/apache/solr/handler/V2ApiIntegrationTest.java: extends the existing real two-node cluster suite with one-replica reload. Resolves hosting/non-hosting nodes from collection state, explicitly targets both, uses JavaBinResponseParser and verifies replacement of the hosted core. Reuses conf1, existing clients and collection cleanup rather than adding a standalone probe.
4. changelog/unreleased/SOLR-17241-wt-non-hosting-node.yml: user-visible response-format fix, author Nick Shanin, existing JIRA link.

The new integration method covers a second real path through the filter in addition to existing query/content-negotiation cases. Keeping the hosting request first provides a positive control before the deterministic non-hosting failure. Holding a reference to the previous core allows identity comparison without sleeps or coarse timestamps.

Windows verification: reviewed every outbound file and base-to-head diff; diff --check clean; branch status clean after commit; review-branch -StatOnly confirms only these four files. Mockito 5.23.0 is already available. No Gradle, compiler, formatter or local test queue ran on Windows. Local implementation checkout is sparse; the gate requires a full isolated checkout.

Acceptance requires tidy, Error Prone main/test compilation, both complete focused suites, module/root checks, and matching fail-before controls with the candidate tests on the original production filter. See the gate for exact seed, controls, evidence and repair rules. Until its receipt is green at the live fork head this patch is not verified or submission-ready.

## Limits and next step

The earlier diagnosis establishes main reload behavior; this patch does not claim released-version/backport coverage. SOLR-16398's current split/move tests explicitly use a JSON parser, so their 276-test green receipt remains JSON-path proof. Default binary coverage and integration of this shared fix into that separate branch remain follow-up work. No SOLR-16398 or SOLR-18600 file changed here.

Decision ladder: reviewed evidence/history at rungs 1-3; localized implementation and test design at rung 4 under Nick's explicit direction. Next: capable Linux host runs the named gate and writes the exact-head receipt. Public communication remains held; a green result will support a concrete comment/patch offer for Nick to authorize.
