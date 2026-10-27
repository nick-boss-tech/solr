# PR 5238 client search dispatch: Windows implementation, round 1

Status: HANDED (Implemented, Proof Pending; Linux gate requested)

Nick requested regressions and a fix for the client-search callback omission found in the replacement for PR 5232. The implementation is pushed to the fork; no public comment or PR was opened.

| Item | Revision |
| --- | --- |
| Original PR 5238 head P | `cc9ea9b1b6b153317217ee458ff6287b5e24cc65` |
| Candidate F1 | `ede5646e2b2281bf4fc1483c2867f0f047088527` |
| Fork branch | `nick-boss-tech/solr:solr-11994-pr5238-submit` |
| Parent relationship | F1 is one commit directly on P |
| Author / committer | Nick Shanin, `nick.boss.us@gmail.com`; no trailers |
| Scope | Seven files, 132 additions, eight deletions |
| Remote verification | GitHub refs API returned F1, 2026-10-27 02:23 UTC |
| Latest PR read | Open at P, one author comment about separate PR 5239, no reviews, 02:23 UTC |
| Implementation claim | `claims/pr-5238-client-dispatch-windows-round-1.md` |
| Gate | `gates/pr-5238-client-dispatch-round-1.md` |
| Expected receipt | `receipts/PR-5238-client-dispatch.md` |
| Board snapshot for final scope review | `fdea1ba698b0cfbadf93aed400f5d43e69b644b2` |

[Pinned follow-up diff](https://github.com/nick-boss-tech/solr/compare/cc9ea9b1b6b153317217ee458ff6287b5e24cc65...ede5646e2b2281bf4fc1483c2867f0f047088527).

## Repair

The original callback guard resolves the context path through the v1 registry. That can be null for a legitimate v2-only search or an embedded `/select` request dispatched through the named/default fallback. Successful searches then lose their event.

Core dispatch and the request-handler API adapter now preserve the originally selected handler in the request context under the `SolrRequestHandler.class` key. `putIfAbsent` keeps the original target when ping or another handler delegates using the same request. SearchHandler compares that dispatch identity rather than requiring a matching v1 registration. The context contract is documented on SolrRequestHandler. Existing nested, shard, pathless and handler opt-out guards remain.

Outbound files:

- `solr/core/src/java/org/apache/solr/core/SolrCore.java`
- `solr/core/src/java/org/apache/solr/api/ApiBag.java`
- `solr/core/src/java/org/apache/solr/request/SolrRequestHandler.java`
- `solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java`
- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerTest.java`
- `solr/core/src/test-files/solr/configsets/search-request-listener-routing/conf/schema.xml`
- `solr/core/src/test-files/solr/configsets/search-request-listener-routing/conf/solrconfig.xml`

Three regressions perform real searches before checking event delivery: a local v2-only handler, explicit embedded `qt=standard`, and embedded default-handler fallback. A separate routing configset omits `/select` and registers `/v2-only-select` exclusively under `/v2`. Existing helper lookups now select the requested collection explicitly because the class creates a second collection. The focused class contains 20 test methods.

## Evidence and limits

Reviewed the selected-handler paths in SolrCore, ApiBag, PluginBag, RequestHandlers and EmbeddedSolrServer. Reviewed the complete outbound diff and both new fixtures. Working and staged whitespace checks passed; the scoped commit left a clean implementation worktree. The plain push landed on the new fork branch and the independent remote read returned the same full SHA.

No Gradle, compiler, formatter, tests or retired Windows queue ran. All runtime claims remain proof pending. The gate requires all 20 focused methods, four affected neighboring classes, clean tidy, root check with tests excluded, and three intended count failures on P plus only the test/fixture overlay. The gate also records the known per-run NODE_ENV condition from earlier Linux work; it does not count those older receipts as new proof.

The older `solr-11994-submit` branch and tested PR 5232 follow-up remain untouched. The contributor's branch and public PR head remain P. The separate tolerant-shard CPU producer issue in PR 5239 / SOLR-18591 is outside this patch. Public follow-up remains pending exact-head Linux evidence and final review under Nick's standing policy.
