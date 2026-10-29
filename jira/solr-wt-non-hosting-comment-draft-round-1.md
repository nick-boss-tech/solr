# Draft update to SOLR-17241

Status: NOT POSTED. Prepared for Nick's review and explicit authorization. No new ticket recommended.

## Proposed JIRA comment (wiki markup)

[~gerlowskija] A two-node probe on main {{852b427c1154ce9eec988b9adaeb212294442126}} shows a remaining response-format failure with this same reload/non-hosting-node setup. With one shard and one replica, {{POST /api/collections/{c}/reload?wt=javabin}} succeeds through the hosting node; through the other node, SolrJ rejects a JSON response because it expected javabin.

The code and temporary diagnostic output agree: the {{ADMIN_OR_REMOTEPROXY}} early return leaves {{solrReq}} null. The local Jersey resource is found, but {{MediaTypeOverridingFilter}} cannot read {{wt}} from {{SOLR_QUERY_REQUEST}} and chooses JSON when {{Accept}} is absent. The earlier null guards prevent the NPE reported here, but do not recover the requested format. SOLR-17086 describes the related filter failure on backup/replica creation.

There is already a parsed-parameter fallback: {{V2HttpCall}} sets {{RequestContextKeys.SOLR_PARAMS}} from the HTTP query parameters even on this path ([source|https://github.com/apache/solr/blob/852b427c1154ce9eec988b9adaeb212294442126/solr/core/src/java/org/apache/solr/api/V2HttpCall.java#L333]). I recommend using that only when the filter's ordinary request params are unavailable. This would preserve the {{wt}}/{{Accept}} precedence from SOLR-10998 without changing request dispatch or parsing the body again. SOLR-18483's proposal to remove {{wt}} is separate from keeping today's behavior consistent.

The expected implementation scope is {{MediaTypeOverridingFilter.java}} plus deterministic HTTP coverage in {{V2ApiIntegrationTest.java}}: address both nodes of a one-replica collection and assert javabin on both, while retaining Accept-only, JSON-default, error and exempt-resource behavior. No fix has been tested yet, and other endpoints have not been reproduced on this main revision.

Would you consider this remaining format issue appropriate to address under SOLR-17241 with that scope?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
