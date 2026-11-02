# SOLR-18537 scope comment, round 2 (POSTED 2026-11-02, Jira comment 18136782, read back byte-identical)

Round 2 reframing (2026-11-02): Smiley, the ticket's assignee,
commented at 20:58Z volunteering for the deprecation himself:
deprecate the {{nodes}} param for removal in Solr 11, with the admin
UI shifting to the new mechanism. The route is therefore settled;
this comment answers Eric's ask, supports Smiley's plan with the
auth evidence and the full inventory, and offers the tried UI slice
as the admin UI shift he describes. No internal process vocabulary.

---

[~epugh] I took a stab at scoping this, and tried the first pieces. [~dsmiley], your note settles the direction; this is support for that plan rather than another proposal.

*On the security side:* the tests that shipped with the filter already cover the authorization change. At the merged head ({{439b2e5096f}}), {{NodeTargetProxyFilterTest}} passes 14 of 14 and {{NodeTargetProxyFilterAuthTest}} 4 of 4. The auth test's cluster is the standard secured shape, with PKI handling inter-node signing, and it proves the target node authorizes the caller rather than the proxying node. Today's {{nodes}} fan-out does the opposite: the caller is authorized once, and the proxied requests then present the node's own identity, so receiving nodes run no authorization at all. Moving callers to the header mechanism closes that widening; the one visible consequence is that a multi-node action can now partially apply, with per-node failures, where the fan-out hid them.

*For the deprecation, the full set:* the {{nodes}} handling lives in {{LoggingHandler}}, {{SystemInfoHandler}}, {{MetricsHandler}} (shared with the v2 {{GetMetrics}}), {{GetNodeSystemInfo}}, and {{NodeLogging}}, all through the {{handler/admin/proxy}} package ({{RemoteRequestProxy}}, {{GenericV1RequestProxy}}, {{V2SolrRequestBasedProxy}}; 348 lines plus three test classes). The param is also declared on the v2 APIs in the {{solr/api}} module, so it is part of the generated SolrJ client surface. {{MetricsHandler}}'s single-node {{node}} param rides the same machinery.

*The admin UI shift, tried:* on a local branch against the merged head (not pushed), the metrics resource sends {{X-Solr-targetNode}} as its GET param in place of {{node}}; the Nodes view fetches system info once per node with the proxy header; the logging page lists live nodes and issues one proxied PUT per node in cloud mode, keeping the local call in standalone mode. The files are {{services.js}}, {{controllers/cloud.js}}, and {{controllers/logging.js}}. Syntax-checked only; there is no JS test harness for these controllers, so the slice still wants a live UI pass before it counts as done. Happy to open that as its own PR if the split works for you both, or hand the diff over if you would rather fold it into the deprecation work.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
