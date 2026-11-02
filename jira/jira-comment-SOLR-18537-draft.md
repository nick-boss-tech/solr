# Draft: SOLR-18537 scope comment (for Nick's per-text go; not posted)

[~epugh] I took a stab at scoping the follow-up [~dsmiley] outlined, and tried the first two steps. Sharing what I found before opening anything.

*Evidence first.* The authentication question in the analysis is settled by the tests that shipped with the filter: at the merged head ({{439b2e5096f}}), {{NodeTargetProxyFilterTest}} passes 14 of 14 and {{NodeTargetProxyFilterAuthTest}} 4 of 4. The auth test's cluster is the standard secured shape, with PKI handling inter-node signing, and it proves the target node authorizes the caller rather than the proxying node. That is the inverse of today's broadcast, where the caller is authorized once and the fan-out then presents the node's own identity, so receiving nodes run no authorization at all. One consequence: a log-level change can now partially apply, with per-node failures, instead of being silently widened. Is that coverage sufficient for the authentication side, or should a dedicated test land alongside the UI change?

*The removal surface.* The proxy package is 348 lines ({{RemoteRequestProxy}}, {{GenericV1RequestProxy}}, {{V2SolrRequestBasedProxy}}) with three test classes. The call sites are six files: {{LoggingHandler}}, {{SystemInfoHandler}}, {{MetricsHandler}} (shared with the v2 {{GetMetrics}}), {{GetNodeSystemInfo}}, and {{NodeLogging}}. The {{nodes}} param is also declared on the v2 APIs in the {{solr/api}} module, so it is part of the generated SolrJ client surface.

*Consumers, tried.* On a local branch against the merged head (not pushed): the metrics resource sends {{X-Solr-targetNode}} as its GET param in place of {{node}}; the Nodes view fetches system info once per node with the proxy header; the logging page lists live nodes and issues one proxied PUT per node in cloud mode, keeping the local call in standalone mode. Three UI files, syntax-checked; there is no JS test harness for these controllers, so the try still wants a live UI pass. The files I expect to touch for this slice are {{services.js}}, {{controllers/cloud.js}}, and {{controllers/logging.js}}.

*The ladder, as I see it:*
 * Deprecate first: UI consumers move now, the {{node}}/{{nodes}} params are marked deprecated with the logging doc and a major-changes note, and the package is deleted in a later release.
 * Remove on main now: same UI move, then the params, call sites, and package come out in this cycle, with the upgrade note carrying the behavior change.
 * UI only for now: land the consumer changes and defer every server-side step.

My recommendation is the first rung: the params are public API surface, and deprecating them costs little once the UI no longer depends on them. [~dsmiley], the route is yours to call; if you would rather see it shaped differently, say so and I will adjust before writing the server-side slice.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
