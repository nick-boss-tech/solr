# Receipt: wt is ignored for JAX-RS responses served by a node without a replica (vm6, Linux)

- Follows: the round 3 default-split failure recorded in `receipts/SOLR-16398.md` (round 3), which the earlier wire capture (`receipts/solr-v2request-default-parser-wire-vm6.md`) did not reproduce. This receipt explains it. The earlier "not reproduced" conclusion was wrong.
- Result: explained and reproduced. A request with `?wt=javabin` gets a JSON response when the node that receives it hosts no replica of the collection and the endpoint is a local Jersey resource. The split still runs; only the response format is wrong. Not binary-compatibility. Pre-existing: neither `V2HttpCall.java` nor `MediaTypeOverridingFilter.java` differs between `main` (`74197140a6`) and the SOLR-16398 branches.

## Mechanism (code, `main` = `74197140a6`)

1. `solr/core/src/java/org/apache/solr/api/V2HttpCall.java`, `init`: for a collection path, the node looks up a local core for the collection. If none exists, `extractRemotePath` sets `action = REMOTEPROXY`, and the branch sets `action = ADMIN_OR_REMOTEPROXY` and returns early, before `initAdminRequest`. `solrReq` stays null.
2. `V2HttpCall.java` `invokeJerseyRequest` (~line 326): `containerRequest.setProperty(SOLR_QUERY_REQUEST, solrReq)` stores null.
3. `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java` (~lines 66-79): the response filter reads that property. It gets null, so it cannot see `wt`, and takes the `else` branch, which sets `Content-Type: application/json`.
4. The local Jersey resource still runs and returns a complete body. The javabin writer is not chosen, so the client gets JSON and its `checkContentType` throws `Expected mime type in [javabin, octet-stream] but got application/json`.

## Evidence

- Targeted probe (`ProbeRoutingTest`, gate run `solr16398-probe-routing`, rc 0). One-shard, one-replica collection on two nodes. The default split sent to the hosting node returns javabin and succeeds. Sent to the other node, the filter line reads `sqr=false wt=noparams ... val=null` and the client gets the mime error. Log: `/workspace/gates/logs/vm6/solr16398-probe-routing/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.ProbeRoutingTest.xml`.
- Debug run with temporary prints (`solr16398-split-dbg2`, seed `2E89ADFCD886002A`). `V2DBG setSqr ... solrReqNull=true` for the failing split; `FILTERDBG ... sqr=false ... val=null`. Log: `/workspace/gates/logs/vm6/solr16398-split-dbg2/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest.xml`. The prints were reverted from the clone afterwards.
- Round 3 failing run (`solr16398-r3-green`): the split and move calls failed with the mime error, and the failing body contains duplicate `success` keys, as a complete JSON response does. Log: `/workspace/gates/logs/vm6/solr16398-r3-green/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest.xml`.
- Replay with the same seed on the same class (`solr16398-r3-replay`, `-replay-cfg`, `solr16398-split-only`): split fails (and move in one replay). Overseer on, PRS off in the printed config. Node selection is random per request in the cloud client, so which requests hit a non-hosting node varies between runs.
- Why earlier probes passed: the four-shard probe (`solr16398-probe-nodes`) and the seeded `ProbeDefaultParserTest` used collections that both nodes host, so the branch never ran. The rebalance test uses two shards and two replicas, so both nodes host it.

## Why only some tests fail

The split and move tests use one-shard, one-replica collections. A request sent to a node without that replica takes the remote-proxy branch, so its wrong response depends on which node the client picks. The client's node choice follows the test seed, so the failure is intermittent across runs.

## Consequences

- SOLR-16398 round 3 remains PASS on its gate steps, but `CollectionCommandsV2IntegrationTest` for split and move can fail intermittently. This is a product bug, not a test bug. The test should not be reworded to hide it.
- Other JAX-RS endpoints that return a non-javabin response when `wt=javabin` is sent to a non-hosting node are presumably affected too. Not checked.

## Options (not implemented)

1. In the `REMOTEPROXY` early return, set `solrReq` (or the request property) before returning, so the response filter sees `wt`. Smallest change; touches the routing path.
2. In `MediaTypeOverridingFilter`, fall back to the query parameters of the servlet request when the SOLR_QUERY_REQUEST property is null. Localized to the filter; the `wt` value is then read from the HTTP request directly.
3. Interim, for the gate only: use a collection hosted on every node in the split and move tests (for example, two replicas on two nodes). That avoids the path but leaves the product bug.

Recommendation: option 1 or 2 as an owner-side fix on main, with a test that sends the request to a non-hosting node, then a regate. No JIRA or PR posted; those need the owner's go.

AI-generated draft. Nick Shanin directed the work and takes responsibility for this contribution.
