# PR draft: SOLR-18549 (for Nick to approve; not opened)

Status: DRAFT. Not opened. Opening needs Nick's approval. Head: `solr-18549-submit` at `cf4b869346` (fork), base apache/solr `main` at `c8ab71430f`. Gate: GREEN at this head (receipts/SOLR-18549.md).

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

## Proof

Each class below runs on the base with the head tests kept, and on the head. Counts come from the JUnit XML of the gate run.

- Gate run at `cf4b869346` (receipts/SOLR-18549.md): focused head after cleanTest, 36 tests, 0 failures, 0 errors, 0 skipped, from fresh JUnit XML. Classes: LoggingHandlerTest 3, MetricsHandlerTest 10, GetNodeSystemInfoTest 4, GenericV1RequestProxyTest 10, V2SolrRequestBasedProxyTest 9.
- Base: apache/solr `main` `c8ab71430f` with the production files reverted and only the `standaloneAllMeansLocal()` hook declared (returns `false`). The head tests call that method, so a plain revert does not compile. On this base, 14 of 36 tests fail, each with the ticket's NullPointerException or the wrong status code: LoggingHandlerTest 2, MetricsHandlerTest 2, GetNodeSystemInfoTest 3, GenericV1RequestProxyTest 4, V2SolrRequestBasedProxyTest 3.

## The choice

The question for the maintainer: should a broadcast selector in standalone mode mean "this node" (`nodes=all` on the logging and system-info endpoints), or should every node selector be rejected with 400 outside SolrCloud as it is for metrics? This change takes the first option for the three broadcast endpoints and the second for metrics. Is that split the right call?
