# PR #5141 draft: SOLR-8009 body, new failure disclosed and trimmed (review item 4)

- Written against head: e600375aaee30d66694b69e1d05e276801327e73 (live branch solr-8009-submit, draft PR, read through the API 2026-10-17).
- Visible characters: 3,477 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: receipts/SOLR-8009.md, section "Moved tip verification, 2026-10-15, at e600375aaee3" (FullSolrCloudDistribCmdsTest 10 of 10, TestRealTimeGet 4 of 4, TestRandomFlRTGCloud 2 of 2, TestAddFieldRealTimeGet 1 of 1, fresh JUnit XML).
- The new sentence about SERVICE_UNAVAILABLE is traced in the source (HttpShardHandler.java line 255, "no servers hosting shard", recorded as SERVICE_UNAVAILABLE). No run covers it. Limits says so. Nick may want a test before this goes live.
- Links still point at the earlier head 8795661 in places. Re-check them against e600375aaee3 when applied.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

(unchanged) SOLR-8009: An unrouted id on an implicit-router collection is now sent to every shard

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-8009

## What happens today

**A real-time get with no `_route_` finds nothing on an implicit-router collection, even when the document is stored.**

On a collection that uses the implicit router, `/get?id=<id>` with no `_route_` and no `shards` parameter returns no document. The distributed path in `RealTimeGetComponent.createSubRequests` asks the router for a slice for each id. The implicit router returns no slice when there is no route, and the code skips the id ([base code](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1065-L1067)). The Jira report describes a NullPointerException on an older release. On this base the request returns an empty result with no error.

## What this change does

**An unrouted id on an implicit-router collection is now sent to every shard.**

- Ids with no slice are collected only when the collection uses the implicit router and no `_route_` was given ([RealTimeGetComponent.java](https://github.com/nick-boss-tech/solr/blob/e600375aaee30d66694b69e1d05e276801327e73/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1072-L1076)).
- They go out in one more request to all shards ([RealTimeGetComponent.java](https://github.com/nick-boss-tech/solr/blob/e600375aaee30d66694b69e1d05e276801327e73/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1098-L1104)). That is the same request the code already builds when `shards` is given.
- If a shard has no live replica, the fan-out reports "no servers hosting shard" as SERVICE_UNAVAILABLE. A request that does not set `shards.tolerant` fails with that error. The base returned an empty result for these ids.
- Other routers do not change.

## Proof

**`FullSolrCloudDistribCmdsTest.testRealTimeGetImplicitRouterWithoutRoute` fails on the base code and passes with this change.**

- The new test builds a two-shard implicit collection, adds one document to each shard, and checks a get by one id and a get by two ids with no route. It checks the returned titles, and checks that a get routed to the other shard returns nothing ([FullSolrCloudDistribCmdsTest.java](https://github.com/nick-boss-tech/solr/blob/e600375aaee30d66694b69e1d05e276801327e73/solr/core/src/test/org/apache/solr/cloud/FullSolrCloudDistribCmdsTest.java#L157-L187)). On base `14c7aac0d15`, the first `assertNotNull` fails because the get returns nothing.
- Verified 2026-10-15 at head `e600375aaee3`: FullSolrCloudDistribCmdsTest 10 of 10, TestRealTimeGet 4 of 4, TestRandomFlRTGCloud 2 of 2, TestAddFieldRealTimeGet 1 of 1. All pass.

## A choice to check

**An unrouted get on an implicit collection fans out to every shard. The other option was to fail it.**

- Implemented: fan out, so the document is found wherever it is. The cost is one request to each shard for every unrouted get, which is slower than a routed get.
- Alternative: return a 400 that asks the caller to send `_route_` or `shards`. Callers that now get an empty result would get an error instead.

Was fanning out the right call here, or should an unrouted get fail?

## Limits

**Duplicate results and compositeId collections with no route are not fixed here, and some cases have no test.**

- An id that sits on more than one shard can come back more than once in a get by several ids. The existing `shards` form does the same, as [SOLR-8954](https://issues.apache.org/jira/browse/SOLR-8954) reports. This change does not fix it.
- A compositeId collection with a router field and no `_route_` still returns nothing, as before. I can open a follow-up ticket and PR for those on request.
- A get with a `_route_` that names no slice of the collection is not tested. That case is rejected with a 400 error before this code runs, on the base code and with this change alike.
- No test covers a shard that has no live replica during the fan-out, so the SERVICE_UNAVAILABLE behavior above is not run.

Changelog: [changelog/unreleased/SOLR-8009-rtg-implicit-router.yml](https://github.com/nick-boss-tech/solr/blob/e600375aaee30d66694b69e1d05e276801327e73/changelog/unreleased/SOLR-8009-rtg-implicit-router.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
