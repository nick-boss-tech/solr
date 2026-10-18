# PR draft: SOLR-18549 (for Nick to approve; not opened)

Status: DRAFT. Not opened. Opening needs Nick's approval. Head: `solr-18549-submit` at `b89664d164` (fork; the code is the same as `cf4b869346`, the changelog title changed), base apache/solr `main` at `c8ab71430f`. Gate: GREEN at this head (receipts/SOLR-18549.md).

Title: SOLR-18549: Standalone nodes no longer NPE for nodes or node parameters in proxy-capable admin endpoints

## What happens today

On a standalone node (not running in SolrCloud), the `nodes` and `node` parameters fail with a NullPointerException, or with a 500 that wraps one, in four endpoints:

- `/admin/info/logging` (v1), for reads and for `set`. A `set` with `nodes=all` applies the level change on the node and then fails.
- `/admin/info/system` (v1).
- `/api/node/system` (v2).
- `/admin/metrics` (v1), with `node`.

The shared proxy code (`RemoteRequestProxy.validateNodeNames` and `GenericV1RequestProxy.shouldProxy`) reads the ZooKeeper controller without checking that one exists. The Admin UI no longer sends `nodes=all` in standalone mode (SOLR-18317), but other clients and direct calls still get the error.

## What this change does

- For the three broadcast endpoints (logging, system info v1, system info v2), `nodes=all` in standalone mode is answered by the node itself. The `nodes` parameter there selects which nodes to collect from, so `all` degrades to this node.
- Any other `nodes` value in standalone mode returns 400 with the message that the parameter requires SolrCloud.
- `/admin/metrics` keeps the strict behavior: `node=all` and explicit node names return 400 in standalone mode. The metrics `node` parameter selects a scrape destination, not a broadcast.
- The ZooKeeper check now throws a 400 instead of an NPE, in one place (`RemoteRequestProxy`).
- Behavior changes beyond the ticket, stated here so a reviewer can judge them:
  - In SolrCloud, `/api/node/system?nodes=<name>` for a node that is not live now returns 400 ("is not part of cluster") instead of 500. Before the JAX-RS conversion (SOLR-16458) the endpoint returned 400 for this case, and `/admin/info/system` (v1) still does. The 500 came from a catch-all that wrapped every exception in that conversion. Remote errors that already carry a status keep it. Remote I/O failures are still 500.
  - In standalone mode, v2 `/api/node/metrics?node=all` is still answered by the node itself (the v2 handler catches the proxy error and serves locally). Only the v1 `/admin/metrics` endpoint returns 400 for `node=all`. This is out of the ticket's scope, so it is unchanged here.

## The choice

Options considered for a broadcast selector in standalone mode: (a) `nodes=all` means "this node", as the ticket's expected behavior states for the logging and system-info endpoints; (b) reject every node selector with 400 outside SolrCloud, as `/admin/metrics` does. This change takes (a) for the three broadcast endpoints and (b) for `/admin/metrics`. The ticket's expected behavior sets both, so the split follows it.

Not changed here: the V2 logging endpoint (`/api/node/logging/levels`) still returns 400 for `nodes` in standalone mode (SOLR-15823). The ticket leaves whether it should answer `nodes=all` locally open, and this PR does not decide it.
