# Receipt: non-hosting-node wt routing review, round 1 (Windows)

Status: REVIEW COMPLETE. Recommend an update to existing SOLR-17241, not a new ticket. Prefer a response-filter fix using the already parsed SOLR_PARAMS fallback. No Solr source change, Gradle, probe, public comment or PR in this review.

## Inputs and limits

- Assignment: assignments/solr-wt-non-hosting-review-round-1.md, originally f8ead2dafd41; reviewed at board snapshot 29a56979ee0ceb389647ef8402b324a6d04eed11.
- Claim: claims/solr-wt-non-hosting-review-round-1-windows.md, posted at 339efc2615cf338a5bb76ff0c7a3bbccf5035138.
- Source reviewed: upstream main 852b427c1154ce9eec988b9adaeb212294442126, fetched into the existing Solr object repository and read via git, without using its dirty working tree.
- SOLR-16398 collection-command branch: origin/solr-16398-command-jaxrs at b135a0346a942a6b9743a1840c2b9f259cb24310, fetched and checked; matches the round-3 receipt's abbreviated head.
- Read both wt validation receipts, the posted probe, SOLR-16398 receipt, relevant source/history, and live JIRA issues/searches. Windows host: no builds, runtime reproduction or raw vm6-log inspection. Source findings are independent; runtime findings remain attributed to vm6's records.
- Review date: 2026-10-29, America/New_York. All public-action drafts remain NOT POSTED.

## Q1. Mechanism and evidence (decision ladder rungs 1 and 2)

The core mechanism is supported by code and the hosting/non-hosting debug pair. This is a server response-negotiation defect surfaced by SolrJ, unrelated to binary compatibility.

| Reference at 852b427c11 | Assessment |
| --- | --- |
| V2HttpCall.java:90 | Query parameters are parsed before the branch. wt remains available in queryParams. |
| V2HttpCall.java:163-171 | With no local core, extractRemotePath may set REMOTEPROXY; it becomes ADMIN_OR_REMOTEPROXY and returns before request parsing/admin initialization. Correct. |
| V2HttpCall.java:182 and :211-222 | The later initAdminRequest is skipped. It would create solrReq, set request type/action, and propagate the calling-lock header. Correct. |
| V2HttpCall.java:326 | The Jersey context receives the null solrReq value. Correct. |
| V2HttpCall.java:331 AND :333 | Both the servlet request AND parsed queryParams (SOLR_PARAMS) are attached unconditionally. The second property offers a smaller fallback than reparsing the servlet request. |
| V2HttpCall.java:376-391 | ADMIN_OR_REMOTEPROXY first tries the local container-level Jersey application. If a resource exists, it handles the operation locally and returns; only an unmatched resource calls sendRemoteProxy. This case is not necessarily an HTTP-proxy response being reformatted. |
| MediaTypeOverridingFilter.java:55-78 | Successful, non-exempt resources use solrReq params for wt. Null solrReq skips that override. With no Accept or literal */*, line 78 selects JSON. Explicit Accept can still be honored; not every null-request response necessarily defaults to JSON. |

Pinned source: [V2HttpCall](https://github.com/apache/solr/blob/852b427c1154ce9eec988b9adaeb212294442126/solr/core/src/java/org/apache/solr/api/V2HttpCall.java#L163), [filter](https://github.com/apache/solr/blob/852b427c1154ce9eec988b9adaeb212294442126/solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java#L66).

The validation receipt's wt-main-clean and wt-main-dbg excerpts show the expected contrast: local replica has sqr=true/wt=javabin; non-hosting request has sqr=false/val=null followed by a client MIME exception. The debug ct= value is before the filter override and is not a final wire Content-Type measurement. This is consistent diagnostic evidence, not a new independent execution on this host.

Two evidence-packaging corrections are needed before claiming a reusable regression test:

1. The board's ProbeRoutingTest.java currently contains ONLY split calls (probeHostedNode/probeOtherNode), no reload leg. The validation receipt says the expanded reload source is the same board file, but that claim does not match the reviewed snapshot. Preserve/upload the actual expanded reload source from vm6; do not cite the posted split-only source as an exact upstream reload reproducer.
2. The posted probe catches exceptions, prints a truncated message and exits normally. rc=0 is successful diagnostic execution, not a passing assertion of response correctness. The excerpts are truncated and do not independently demonstrate a reload side effect. Source order supports the interpretation that the endpoint runs before response filtering, but the future regression should explicitly assert both response format and completed operation. The body/header/raw logs were not inspected here.

These limits do not overturn the mechanism, but they narrow the proof claim and explain what the next fix gate must retain.

## Q2. Duplicate search and history (rung 1)

JIRA was queried read-only through the configured apache-jira connector, per the workspace bootstrap. It was not queried anonymously because the configured connector is the workspace's preferred route. The seven requested/supplementary searches cover MediaTypeOverridingFilter, wt=javabin, MIME+javabin, REMOTEPROXY, remote-proxy+v2, Expected mime type and V2HttpCall. Two broad searches were capped at 50 results by the connector; date partitions and key-exclusion queries recovered all 96 and 82 hits respectively. The completed union is 237 distinct issue packets. No truncated first page is treated as a complete search.

Every hit, status and screening disposition is in reports/solr-wt-non-hosting-jira-search-round-1-windows.md. The local evidence packet retains query text, counts, issue descriptions and selected issue/comment snapshots. Detailed issue/comment reading was concentrated on the candidates below; unrelated hits are explicitly marked as search-packet screening, not a full review of their comment histories.

| Issue | Live status | Relationship |
| --- | --- | --- |
| [SOLR-17241](https://issues.apache.org/jira/browse/SOLR-17241) | Open | Best existing vehicle. Same collection-reload endpoint, two-node/one-replica topology and non-hosting receiver; its 9.5 stack fails in MediaTypeOverridingFilter because solrQueryRequest is null. Today's MIME mismatch is the remaining response-format consequence after null guards. No comments at review time. Recommend adding this evidence here and letting maintainers confirm whether they prefer a separate linked follow-up. |
| [SOLR-17086](https://issues.apache.org/jira/browse/SOLR-17086) | Open | Same null-request/filter family, reported for backup and ADDREPLICA in 9.4. Related broader endpoint evidence, not a reason to file another reload report. Jason Gerlowski previously requested a reproducible case. |
| SOLR-18324 | Resolved | Null-request secondary NPE/logging/metrics cascade; its fix does not repair response negotiation. |
| SOLR-18487 | Resolved | Same non-hosting/null-request precondition, different AdminCmdContext failure. Its comments discuss initializing a real request, but the merged fix changes admin command context handling rather than V2HttpCall/filter. Do not call that comment a landed upstream initialization fix. |
| SOLR-18492 | Open | Distinct RETRY action loss when no remote core can be resolved; not this successful local Jersey response formatting defect. |
| SOLR-18483 | Open | Proposal to remove wt from V2; a comment acknowledges SolrJ still sends wt=javabin. Not a landed contract change or an existing fix for topology-dependent wt handling. |
| SOLR-10998 | Closed | Defines current negotiation intent: wt takes precedence; otherwise Accept; JSON if neither. Relevant fix/history and tests, not a duplicate. |
| SOLR-17317 | Closed | Fixed missing json case in wt-to-media mapping. Different cause. |
| SOLR-11266 | Closed | Older native V2 text/plain-versus-JSON Content-Type issue. Different cause. |
| SOLR-12868 / SOLR-11619 | Resolved / Closed | Historical V2 proxy-path/action defects. Different from a locally found Jersey resource ignoring wt. |
| SOLR-17740 | Closed | Prior payload-consumption bug in V2 request parsing. Relevant caution when considering more eager parsing, not this format defect. |

History actually checked:

- c058ea5dac37fbcc54f2944ce3d6fad2bded86fc (SOLR-15751, 2024-12-27) added the filter/helper null guard. It prevents the older null dereference but uses a default media type when params are absent; it does not recover wt.
- e6d9dc520331697684a1051ddbe18e2d3174cd98 (SOLR-10998, 2025-03-21) introduced the present wt/Accept/JSON-default decision order. This is direct evidence for preserving negotiation, not a proposal.
- af2a3c742b50004c69fe2be48c51f904ebdff012 (SOLR-17317) fixes the json mapping case.
- The file histories at the pinned head show neither the null guards of SOLR-18324 nor the command-context fix of SOLR-18487 modifying the two routing/filter files. The early-return path remains in the pinned source.

Affected-version claim: current main at 852b427c11 is directly reproduced in the validation record. Historical 9.4/9.5 tickets establish the earlier null-context failure, not proof that those release jars produce this precise current JSON-versus-javabin symptom. Do not label every 9.x/10.x version affected without checking.

## Q3. Fix selection (rung 4, supported by rungs 1 and 3)

Choose OPTION 2's filter-local strategy, refined to use the already parsed SOLR_PARAMS context property rather than reparsing HTTP_SERVLET_REQ.

Retain solrQueryRequest.getParams() as the primary source when present. Only when the request/params are unavailable, fall back to (SolrParams) requestContext.getProperty(RequestContextKeys.SOLR_PARAMS). V2HttpCall stores this at line 333 from queryParams parsed at line 90. InjectionFactories.java:111 already exposes that property as SolrParams, and SolrRequestAuthorizer consumes it as well. This reuses existing request infrastructure and preserves duplicate/encoded query-param handling.

Option 1 is defensible as a broader request-context repair, but calling initAdminRequest before the early return is not a safe one-line change: it assigns action=ADMIN and requestType=ADMIN, so careless insertion removes the remote fallback decision. Even a parse-only variant changes request/body lifecycle and must correctly propagate container/lock/path context. SOLR-18487's discussion supports evaluating that larger invariant separately. Nothing in this review demonstrates that initializing a full request is necessary merely to recover wt already present in SOLR_PARAMS.

The localized filter fix should preserve:

- Status >=400 bypass, null resource-class bypass and ZookeeperRead exemption (filter lines 55-63).
- Existing non-null request params as authoritative, including any handler-derived params. Do not replace those wholesale with raw servlet params.
- Existing mapping of json/xml/javabin and other supported wt values through V2ApiUtils.java:94-113; no new response format or writer registry.
- wt precedence over Accept; explicit Accept when wt is absent; JSON default for absent Accept or literal */*. Blank/unknown wt behavior should remain as currently implemented, not be redesigned in this fix.

This changes the selected format for successful null-request Jersey responses that explicitly ask for wt, which is the intended correction. It is not limited to javabin or reload: xml/json requests on the same path should be covered too. It does not modify dispatch, permissions, body parsing or remote URL selection. No new trust boundary was identified; this is a compatibility/negotiation source assessment, not a runtime security audit.

## Q4. SOLR-16398 impact and other endpoints (rungs 1-3)

The V2HttpCall and MediaTypeOverridingFilter files are byte-identical between pinned main 852b427c11 and pushed b135a0346a94. This confirms the shared mechanism is inherited, rather than introduced by the collection-command patch.

However, the assignment overstates the current test impact. At the actual pushed/gated head, CollectionCommandsV2IntegrationTest.java:135 explicitly sets JsonMapResponseParser. HttpSolrClient.java:162 sets wt to parserToUse.getWriterType(); JsonMapResponseParser returns json. The tests therefore expect JSON, including on a non-hosting receiver. Their one-shard/one-replica topology alone does not make these current JSON-parser tests suffer the described MIME mismatch.

The initial/default-parser runs and direct default-javabin probes CAN expose the topology-dependent product bug. Their symptom should not be relabeled as recurring failure of the exact currently pushed JSON-parser tests. The 276-test green result is evidence of the tested configuration; it does not cover the default SolrJ path.

Recommendation: track/fix the shared response-negotiation defect separately under SOLR-17241. Do not erase the existing green run, change tests to increase replicas, suppress failures or merge an unexplained server repair into SOLR-16398. For the collection-command slice, HOLD an unqualified submission-ready/default-client compatibility claim until a deterministic non-hosting default-javabin test passes with the shared fix, or maintainers explicitly accept a narrower supported-client scope. Other SOLR-16398 slices have their own receipts; this finding is not a blanket invalidation of all four slices. A temporary JSON-parser choice is valid test coverage of JSON, not proof of default-client behavior.

On main, reload is the only runtime-confirmed endpoint in the reviewed records. Additional source-reachable candidates include:

| Endpoint | Source evidence / qualification |
| --- | --- |
| POST /collections/{c}/shards/{s}/replicas | CreateReplicaApi.java:32-35; CollectionsHandler registers CreateReplica at :1223; implementation uses SubResponseAccumulatingJerseyResponse at :81 and admin submission at :92. Same collection-prefix/null-local-core/Jersey response route for a valid request. Source reachability only, not a new runtime reproduction. |
| POST /collections/{c}/shards | CreateShardApi.java:27-30; CollectionsHandler :1230; response and submission at CreateShard.java:79/:92. Same exposure when request preconditions allow creation (e.g. appropriate collection routing). Source reachability only. |
| DELETE /collections/{c}/shards/{s} | DeleteShardApi.java:29; CollectionsHandler :1236. Same candidate route for a valid deletable shard, not the specially handled whole-collection DELETE. Source reachability only. |

Do not claim all V2 endpoints fail. Whole-collection DELETE has an earlier initAdminRequest short circuit at :154-158; node/global paths, true remote-proxy responses, exempt raw resources and error statuses have different behavior. Split and rebalance are not comparable runtime oracles on pinned main as the validation receipt explains.

## Next proof and maintenance requirements

The proposed source change is not implemented or tested here. A Linux implementation/gate should first preserve the expanded reload probe source and turn it into assertions: two nodes, one replica; deterministically address each node with wt=javabin; assert the response media/body parses and verify reload completion. The existing V2ApiIntegrationTest uses one shard with TWO replicas and its negotiation tests call global /collections, so it misses this topology. Reuse that SolrCloudTestCase/configset infrastructure while adding a collection-scoped non-hosting case; a new server framework is unnecessary.

Test the local filter branch with and without SOLR_QUERY_REQUEST, existing SOLR_PARAMS, wt versus conflicting Accept, Accept-only, no headers, xml/json/javabin, blank/unknown wt, errors and exempt resources. Preserve real request/proxy fallback and payload behavior; do not broaden the fix to unrelated negotiation rules. Demonstrate fail-before on the pinned unmodified behavior and pass-after at the pushed repair head. Then add deterministic default-javabin split/move coverage to the SOLR-16398 slice and obtain fresh proof for any changed branch head. Raw fixture/reporting repairs alone do not require rerunning the existing large suite.

Architecture: response negotiation should use params already placed into the Jersey context; creating a SolrQueryRequest only to recover a query parameter adds unnecessary coupling to dispatch. Maintenance/reuse: use SOLR_PARAMS and existing V2ApiIntegrationTest infrastructure. Verification: source/history review plus attributed Linux diagnostics; no executed fix proof yet. No current overall readiness endorsement is implied by closing the source-review questions.

## Owner decision (rung 5)

Recommendation: post the prepared evidence/fix proposal ONCE to existing SOLR-17241, with SOLR-17086/SOLR-10998/SOLR-18483 as context, instead of opening a new issue. This is an existing-ticket update, not the clean-gate PR-follow-up exception.

One question: May I post the prepared update to SOLR-17241 proposing the SOLR_PARAMS fallback and a deterministic non-hosting regression test?

Draft: jira/solr-wt-non-hosting-comment-draft-round-1.md (NOT POSTED). No new-ticket draft is needed because an existing matching vehicle was found. Posting is expressly reserved to Nick by this assignment and the workspace public-communication rules.

Completion is recorded add-only in claims/solr-wt-non-hosting-review-round-1-windows-done.md. The assignment's request to edit its shared Status line conflicts with WORKFLOW.md's worker add-only invariant; its source file is left unchanged, with the done record and this receipt providing completion evidence for index regeneration.
