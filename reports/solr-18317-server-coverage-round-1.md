# SOLR-18317 server branch coverage check, round 1

- Date: 2026-10-14 (UTC). Assignment: assignments/pool-solr-18317-server-coverage-round-1.md. Claim: claims/pool-solr-18317-server-coverage-round-1.md.
- Branch read: solr-18317-server-submit at ae918a03fa7 (eight commits on 0d2a4649c7, no merges, as the assignment says).
- Main read: upstream/main at 7767d36d29f.
- SOLR-15823 pair read: #5030 at 1f60cd39baa (solr-15823-submit) and #5031 at 6fa54c4de8c (solr-15823-levels-submit). Both are open and both branch from e432df19c4, the commit that merged #5001.
- Read-only only: git fetch and show, gh pr view. No builds, no runs, no Jira, no PR, comment or branch edit.

## Result

- Four of the five starting items are covered on main or moot. The fifth item's server behavior is not covered.
- One gap is live on main and is not covered by the pair. A standalone node throws a NullPointerException when a proxy-capable admin endpoint gets a nodes or node parameter. The Admin UI no longer sends that parameter in standalone mode, so the gap shows only for other clients, older UIs and direct calls.
- A new-ticket draft for that gap is at the end. Nothing is filed.

## Confirmed itemization, with corrections

- Item 1 (proxy layer): as stated. The branch adds a standalone opt-in (standaloneAllMeansLocal), shouldProxyTo, and requireZkController with a 400 message. The downcast restore in 42418afcae is a no-op in the net diff.
- Item 2 (metrics and Admin UI caller): corrected. The branch's MetricsHandler.java change is a comment only (commit 6a3002106b). The metrics behavior comes from the shared proxy in item 1: standalone node=all gets a 400 instead of an NPE. The Admin UI logging caller change (services.js, logging.js) is already on main from #5001.
- Item 3 (opt-in at call sites): corrected. MetricsHandler does not declare the opt-in and keeps the strict default. The opt-in is declared in LoggingHandler (v1), SystemInfoHandler (v1) and GetNodeSystemInfo (V2).
- Item 4 (proxy cleanup and system-info coverage): corrected. The "unused shouldProxyTo parameter" is removed inside the branch's own history, so the final branch has no unused parameter and main has nothing to match. The GetNodeSystemInfo change that rethrows SolrException is part of this item.
- Item 5 (Admin UI mode on system-info failure): as stated, but already on main. app.js lines 558 to 568 carry the same fallback, from #5001.

## Verdicts

### Item 1: proxy layer, standalone nodes=all answered locally, no NPE. NOT COVERED.

- Main: RemoteRequestProxy.validateNodeNames (line 152) reads coreContainer.getZkController().zkStateReader at line 154 without a standalone check. GenericV1RequestProxy.shouldProxy (line 45) returns true for any non-empty nodes value. So every proxy-capable endpoint that receives a nodes or node value throws a NullPointerException on a standalone node. ZkContainer.java lines 53 to 54 documents that a null controller means standalone.
- Affected endpoints on main: v1 logging (LoggingHandler line 95), v1 system info (SystemInfoHandler lines 53 to 54), V2 node system info (GetNodeSystemInfo.proxyToNodes, lines 70 to 85), v1 metrics (MetricsHandler lines 112 to 113, with the node parameter).
- Pair: neither PR changes RemoteRequestProxy, GenericV1RequestProxy, V2SolrRequestBasedProxy, SystemInfoHandler, MetricsHandler, or the v1 tail of LoggingHandler (line 95 at both heads). The V2 logging code adds a standalone guard that returns 400 "The 'nodes' parameter is only supported in SolrCloud mode" (#5030 line 120, #5031 line 132). So V2 logging now gives a 400 for any nodes value, all included. It does not answer locally, which is the branch's behavior.
- Live scenarios after the pair merges, standalone node, from code reading:
  - GET /solr/admin/info/logging?nodes=all: the list is built, then the proxy throws an NPE.
  - GET /solr/admin/info/logging?set=...&nodes=all: the level change is applied locally (LoggingHandler, the set block), then the proxy throws an NPE at line 95. The change happens and the request fails.
  - GET /solr/admin/info/system?nodes=all: NPE.
  - GET /api/node/system?nodes=all: NPE, which the catch at line 85 turns into a 500 SERVER_ERROR.
  - GET /solr/admin/metrics?node=all, or node with a real node name: NPE at MetricsHandler line 113.
- These verdicts rest on code reading. No run was done, so the exact HTTP status is not confirmed.

### Item 2a: Admin UI logging caller. COVERED on main, and the pair keeps it.

- Main: services.js line 194 has "setLevel": {} with no nodes default. logging.js line 173 adds params.nodes = "all" only when isCloudEnabled is true. This is the #5001 change (commit e432df19c4).
- Pair: #5030 moves the level change to LoggingV2.modifyLocalLogLevel and sends nodes only when cloud (logging.js lines 164 to 173), and it removes the v1 Logging factory.

### Item 2b: metrics narrowing. NOT COVERED.

- The branch gives a 400 for standalone node=all (through item 1). Main throws an NPE there (item 1).
- The Admin UI does not send node=all. The only metrics calls with a node value are in cloud.js line 422, which sends explicit node names from the Cloud view. The Cloud menu is hidden when not in cloud mode (index.html line 181, ng-show="isCloudEnabled"). Whether that view can be reached by URL in standalone mode is not verified.
- So this gap is for direct calls, and possibly the Cloud route, which is not confirmed.

### Item 3: opt-in at call sites. NOT COVERED.

- On the branch the opt-in makes nodes=all answer locally for v1 logging, v1 system info and V2 node system info. Main has no such opt-in. Neither PR adds a local answer on any endpoint; V2 logging gives a 400. The gap is the same as item 1 for these three call sites.
- Where the opt-in sits (on call sites, not in the proxy) is a design choice, not a behavior, so it is not counted separately.

### Item 4: proxy cleanup and system-info coverage.

- Cleanup: MOOT. The final branch's shouldProxyTo(String) has one parameter. Main has no such method, so there is nothing to cover.
- GetNodeSystemInfoTest standalone cases: NOT COVERED. Main's GetNodeSystemInfo.proxyToNodes (lines 70 to 85) has no standalone guard. Its catch at line 85 wraps the NPE as a 500 SERVER_ERROR. The branch answers nodes=all locally, returns 400 for an explicit node, and rethrows the SolrException rather than wrapping it.

### Item 5: Admin UI mode on system-info failure. COVERED on main.

- app.js line 565 on main sets isCloudEnabled to false when the system-info call fails. That is the #5001 change. The pair's logging.js still waits for isCloudEnabled to be defined (#5030 lines 176 to 179, and main line 181). The fallback resolves that wait, so the page does not hang.

## Live gap: one ticket

The defect is one: the shared proxy code uses the ZooKeeper controller in standalone mode without checking for it. Four endpoints show it: v1 logging, v1 system info, V2 node system info and v1 metrics. The pair does not cover any of them. The fix belongs in the shared proxy code, so one ticket covers all four.

## Decisions for Nick before the ticket is filed

1. The expected answer for nodes=all on the logging and system-info endpoints. The branch answers from the node itself. The pair's V2 logging answers 400. If the pair merges as written, v1 logging keeps the NPE while V2 logging returns 400. The ticket text below assumes the branch behavior for v1 and V2 logging alike. Confirm that.
2. The expected answer for metrics with node=all or an explicit node in standalone mode. The branch gives 400. The ticket text uses 400.
3. Whether the ticket should be filed before the pair merges, or after. Filing now gives the pair's author a clear target for the v1 tail.

## Premise run

No gate file is filed. Every verdict above is settled by reading the code, so the assignment's rule for a runtime premise does not apply. A premise run would confirm the HTTP status of each scenario and the local-first effect of v1 set with nodes=all. It is worth running only if the status code goes into the ticket.

## Drafted ticket text (NOT COVERED case, for Nick's decision)

🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

{{nodes}} and {{node}} parameters on a standalone node cause a NullPointerException in several admin endpoints. The affected endpoints are the v1 logging endpoint ({{/admin/info/logging}}, for reads and for {{set}}), the v1 system info endpoint ({{/admin/info/system}}), the v2 node system info endpoint ({{/api/node/system}}), and the v1 metrics endpoint ({{/admin/metrics}}). For example, {{GET /solr/admin/info/logging?nodes=all}} on a node that is not running in SolrCloud fails with a NullPointerException. A {{set}} request with {{nodes=all}} applies the level change on that node and then fails the same way. The shared proxy code reads the ZooKeeper controller without checking whether one exists, and each of these endpoints goes through that code. The Admin UI no longer sends {{nodes=all}} in standalone mode, but any other client, an older UI, or a direct call still gets the error.

Expected behavior: a standalone node should not throw. For the logging and system info endpoints, {{nodes=all}} should be answered by the node itself, and any other {{nodes}} value should return a 400 error that says the parameter requires SolrCloud. For {{/admin/metrics}}, {{node=all}} and explicit node names should return a 400 error with the same kind of message.

Nothing above is filed or posted. The text waits for Nick's decision on the three points in the decisions list.

## What this check did not do

- No runs, builds or tests. No Jira ticket, PR, comment or branch edit, including solr-18317-server-submit.
- The branch's test assertions were read by name only, not in full.
- The Cloud route in standalone mode, reached by URL, was not checked.
- No live PR body was read. The pair's PR bodies were not compared with the diffs.
