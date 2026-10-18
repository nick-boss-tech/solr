# SOLR-18549 review, round 1 (2026-10-18T20:19:52Z)

Branch: `solr-18549-submit` at `cf4b869346`, base apache/solr `main` `c8ab71430f`. Reviewed by vm5, which also cut the branch, so this is a self-review. An independent reviewer should still read it before it goes to Nick.

## Read

- Every subclass and caller of the proxy classes: `LoggingHandler`, `SystemInfoHandler`, `MetricsHandler.createMetricProxy`, `GetMetrics.proxyToNodes`, `GetNodeSystemInfo.proxyToNodes`, and the two proxy base classes. The shared validator change reaches all of them.
- The shared check in `shouldProxyTo`: a missing or empty node selector stays local, and a standalone `all` stays local only where `standaloneAllMeansLocal()` holds. Any other standalone value goes on to the validator, which now returns 400.
- `requireZkController` and `requireSolrCloud`: the standalone 400 text. With the flag on, the text also says `all` is treated as this node.

## Findings

1. **Cluster-mode behavior change beyond the ticket (needs a PR line or a narrower catch).** `GetNodeSystemInfo.proxyToNodes` now rethrows every `SolrException` unchanged. Before, every failure became a 500 "Error occurred while proxying to other node". In SolrCloud, `/api/node/system?nodes=<name>` for a node that is not live now returns 400 "Requested node … is not part of cluster" (`RemoteRequestProxy.callRemoteNode`), and remote errors keep their codes. The 400 passthrough is needed for the standalone case, so the simplest fix is to narrow the catch to the standalone error. The other option is to state the change in the PR.
2. **v1 and v2 metrics disagree on standalone `all`.** v1 `/admin/metrics?node=all` now returns 400, as the ticket asks. v2 `GetMetrics.proxyToNodes` catches every exception, logs a warning and serves locally, so v2 `node=all` in standalone is still answered locally. That is the same result as before the change, only with a different log line. The PR must say which behavior is intended for v2, or this stays as an open item.
3. **Test coverage matches the ticket.** The proof leg fails 14 of 36 tests on the base, all with the ticket's NPE or the wrong status. The head passes 36 of 36 after `cleanTest`. No test covers the cluster-mode change in finding 1.
4. **Naming.** The changelog title and the PR title match the ticket summary. The fragment is `changelog/unreleased/SOLR-18549.yml`, not the SOLR-18317 name on apache main.
5. **Javadoc and comments.** The `standaloneAllMeansLocal` Javadoc matches the code. The comment in `MetricsHandler` describing the strict default is accurate for v1 only, which is finding 2.

## Recommendation

Fix or state finding 1 before opening, and decide finding 2 with Nick. Nothing else blocks the PR.
