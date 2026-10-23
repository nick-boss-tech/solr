# Review report: SOLR-18446 path-scoped collection routing (round 1)

Reviewer: vm3. Assignment: `assignments/pool-review-solr-18446-round-1.md`. Claim: `claims/pool-vm3-review-SOLR-18446-round-1.md`.

Reviewed head: `bc15134268e2fba2e29fb3fbc62546065906713d` on `solr-18446-path-routing-phone-agent-2`, confirmed by ls-remote at claim time (2026-10-23T03:16Z) and again at report time (2026-10-23T03:30Z). Base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. All line numbers below are at the reviewed head.

## Verdict: FIX FIRST

The change does what the ticket asks for the v1 `HttpSolrCall` path, and its new test discriminates. It is not ready to open for four reasons: one entry path still lets the parameter win, existing tests rely on the old precedence and are not covered by the gate, one ref-guide example becomes wrong, and two sentences in the PR draft are false. No repair was made: every item is outside repair scope (it changes expected values, touches files outside the diff, or changes routing beyond the ticket's ask).

### Fixes specified

1. **V2 entry path** (`solr/core/src/java/org/apache/solr/api/V2HttpCall.java:135-137`). Replace `queryParams.get(COLLECTION_PROP, origCorename)` with `origCorename`, and drop the "&collection= takes precedence" comment. V2 already rejects a multi-collection resolution (lines 143-152), so no narrowing logic is needed there. Add one case to `HttpSolrCallCloudTest` or a V2 test: `/____v2/c/collection1/select?collection=collection2` must route to collection1. Alternative if the owner wants V2 kept out of this ticket: say so explicitly in the PR body and the changelog title, and leave V2 for SOLR-18470. The decision is the owner's; the ticket text supports including it (see answer 2).
2. **Tests that rely on the old precedence.** Each must be rewritten to put the intended collections in the URL path (or use a `CloudSolrClient`, which moves the parameter into the path), and the gate for this ticket must run them:
   - `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZkTest.java:1541` and `:1546` (`testMultipleCollections`): `HttpSolrClient` on `collection1` with `collection=collection2,collection3`.
   - `solr/core/src/test/org/apache/solr/search/stats/TestDistribIDF.java:163` and `:172` (`testMultiCollectionQuery`): path `collection1`, parameter `collection1,collection2`.
   - `solr/core/src/test/org/apache/solr/security/RuleBasedAuthorizationCoreCollectionTest.java:125` (`testSearchAcrossPermittedCollections`): path is the public collection's core, parameter is the alias covering both; the test expects the private document back.
   - `solr/core/src/test/org/apache/solr/handler/component/DistributedQueryComponentOptimizationTest.java` (`testDistributedQueryDoesNotReadFromZk`, the `HttpJettySolrClient.query(COLLECTION, ... "collection", COLLECTION + "," + secondColl)` calls near lines 749 and 757): will still pass but no longer reaches the second collection, so its `existsBefore == existsAfter` assertion is vacuous. Move the second collection into the path.
   These four findings come from static reading of the call chain (the Explore search and my own read of `HttpSolrCall.java:281-297`). I did not run them. They are predictions, and the rewrite step must run each class once at the old and once at the new head.
3. **Ref guide.** `solr/solr-ref-guide/modules/deployment-guide/pages/solrcloud-distributed-requests.adoc:277-285` ("collection Parameter") shows `/solr/collection1/select?collection=collection1,collection2,collection3`. At this head that searches `collection1` only. Say the parameter can only narrow a path that names several collections (an alias or a comma list), and change the example to such a path.
4. **PR draft** corrected in `receipts/SOLR-18446.md` (see answer 6).

Also recommended, not blocking: restore the CloudSolrClient null-path case in `AliasIntegrationTest.searchSeveralWays` (see answer 3), and add a regression for an alias whose path is narrowed by the parameter.

## Answers

### 1. Precedence

At v1 `HttpSolrCall`, yes, with these exceptions and edges:

- Container handlers return before any collection resolution (`HttpSolrCall.java:245`). These are the node-level APIs the ticket excludes. The `collection` parameter on `/admin/collections`, `/admin/cores` and the like is a handler argument, not routing.
- Path names a core (`HttpSolrCall.java:255-270`): `def` is the core's collection (`:282`), so the parameter is ignored unless the collection is a multi-collection list.
- Path names a collection or alias (`:283`): resolved from the path alone. The parameter is read only when the path resolves to more than one collection (`:285`), and then only to narrow (`:286-296`).
- After routing, `addCollectionParamIfNeeded(getCollectionsList())` (`:351`, body `:796-817`) overwrites a disagreeing `collection` parameter on the request with the routed list, so the handler downstream sees the path's collection. This is what makes the whole request path-scoped, not only the core lookup.
- **Entry path where the parameter still wins: `V2HttpCall.init()`** (`V2HttpCall.java:135-137`). It overrides `HttpSolrCall.init()` and never calls super. `/____v2/c/<coll>/...` and `/____v2/collections/<coll>/...` with `?collection=other` still route to `other`, and the `{collection}` template value stays the path's name (`:190`). `CoordinatorV2HttpSolrCall` inherits it. No test exercises this.
- **Edge, behavior change not in the draft:** a path with no collection segment (`/solr/select?collection=c1`). `path.indexOf('/', 1)` is -1 (`:255`), `def` is null, `collectionsList` is empty (`:283`), no core is found, and the call ends in the 404 at `:358`. Before the change the parameter alone selected the collection. This follows from the ticket ("the URL path alone should govern") but the draft says such requests route as before. I derived this by reading the code and did not run a probe.
- **Edge, not covered by any test:** path `collection1,collection2` (or an alias to two) with a parameter that matches neither. The intersection is empty, so the parameter is silently ignored and the request goes to both (`:285-295`). The ticket allows the parameter to "disambiguate multiple possibilities"; silently ignoring a non-matching value is a choice the ticket does not make. Not run.

### 2. Ticket fit

Ticket SOLR-18446, "Remove 'collection' param routing support in HttpSolrCall" (Blocker, main 11.0, unassigned):

> The URL path alone should govern which collection receives a collection-scoped request. Node level APIs (e.g. admin) don't count here. Consequently, the "collection" parameter should be removed or altered in semantics from the present day.
> It'd be harmless if the collection param were to assist in disambiguating a core name in the path, if we want that, as that prevents a 404.
> It'd be harmless if the collection param disambiguated multiple possibilities if the path implied more than one (i.e. was an alias to multiple collections or was comma delimited).
> ... The scope of the current ticket is basically HttpSolrCall resolving to a "core" locally or remotely.

Path-over-parameter is the behavior asked for. The multi-collection narrowing is the second carve-out. The core-name carve-out is not implemented (a core name in the path ignores the parameter), which the ticket marks as optional. Two scope readings remain open: V2HttpCall is a subclass of HttpSolrCall and serves collection-scoped requests, so "the URL path alone" arguably covers it; the ticket's last sentence can also be read as limiting scope to the base class. I treat it as in scope because the title says HttpSolrCall and the subclass is the same resolution step. The sibling SOLR-18470 ("Limit collection handlers' scope to URL path") is about handlers and stays out.

### 3. Aliases

`AliasIntegrationTest` changed in `searchSeveralWays` (base lines about 956-975, head `:968-973`). The old recursion put the alias or list into the `collection` parameter and masked the path with a random value from `{null, "bogus", "collection2", "collection1"}`, asserting the parameter won. Under path-over-parameter that assertion is wrong by design, so the masked path was replaced by the real list as the path, and the comment about null was deleted.

Alias requests still resolve as before: the path is run through `resolveCollectionListOrAlias` (`HttpSolrCall.java:283`) unchanged, and the full class passed at the head (9 tests, 0 failures, seed 18446; see answer 5). The behavior that did change is intended by the ticket: the parameter no longer replaces the path, so an alias path with a different parameter now serves the alias, and for a multi-collection alias the parameter narrows instead of replacing.

Side effect to note: removing the `null` case drops the happy-path coverage of a `CloudSolrClient` request that carries the collection only as a parameter (the client moves it into the URL path, per the search in answer 4). The only remaining such use is the error case at `AliasIntegrationTest.java:912-919`. Restoring a null-path call in the recursion is a small test change; it is not repair scope here because it changes what the test asserts.

### 4. Other callers

Searched (static, by the Explore agent and myself, at `bc15134268`): `"collection"`, `COLLECTION_PROP`, `CoreAdminParams.COLLECTION`, `UpdateParams.COLLECTION`, `CommonParams.COLLECTION`, `?collection=`, `&collection=` over `solr/core/src/java`, `solr/modules/*/src/java`, `solr/solrj*/src/java`, `solr/cross-dc-manager`, `solr/webapp`, `solr/packaging`, `solr/bin`, `solr/docker`, `solr/example`, `solr/server`, `dev-tools`, `solr/**/src/test`, `solr/test-framework`, and `solr/solr-ref-guide`.

- **SolrJ clients.** `CloudSolrClient` and `CloudHttp2SolrClient` move a parameter-only collection into the URL path (`SolrRequest.getCollection()` reads the parameter; `CloudSolrClient.java:593-604` and `:1026-1038`). Safe. `HttpSolrClient`, `HttpJettySolrClient`, `HttpJdkSolrClient` do not: they build the URL from the method argument or default collection only (`ClientUtils.buildRequestUrl`, `ClientUtils.java:65`). A parameter-only request through them now 404s; with a default collection the path wins. No production caller found that relies on it.
- **Production.** `ReindexCollectionCmd.java:415,452` (`/stream` with `collection` set) goes through a `CloudSolrClient`, so the path carries it. `TopicStream.java:457` and `MirroringUpdateRequestProcessorFactory.java:271` likewise use `CloudSolrClient`. Shard sub-requests go to core URLs. All other `collection=` uses are `/admin/collections`, `/admin/cores` or `/api/collections` (container handlers). No reliance found.
- **Tests.** Reliance found and listed under fix 2.
- **Docs.** Reliance found and listed under fix 3.
- **Shell/bats.** Only `/admin/collections?action=BACKUP` uses in `solr/packaging`; none affected.
- **Alias creation, internal forwarding.** Alias APIs are admin handlers (early return). Remote proxying copies the original query string to the remote node, which applies the same rule to a core URL, so forwarding is consistent.

### 5. Test quality

`HttpSolrCallCloudTest.testCollectionPathGovernsRouting` (`HttpSolrCallCloudTest.java:74-78`, helper `:80-95`) discriminates. I ran it myself, once per side, at seed 18446 on vm3 (JDK 21.0.12.1, own Gradle home):

| Leg | Code | Result |
| --- | --- | --- |
| Head | `bc15134268` | `HttpSolrCallCloudTest` 4 tests and `AliasIntegrationTest` 9 tests: 13 run, 0 skipped, 0 failures, 0 errors. rc=0, 7m27s. |
| Base production | `HttpSolrCall.java` restored to `b73f03976f`, tests from head | `testCollectionPathGovernsRouting`: 1 run, 1 failure, `java.lang.AssertionError: expected:<[collection1]> but was:<[collection2]>`. rc=1, 3m19s. |

The base-leg failure is the first assertion (path `/collection1/select` with `collection=collection2`), which is the intended one. After the base leg `HttpSolrCall.java` was restored and `git diff bc15134268` is empty. Logs and XML: `/workspace/gates/logs/vm3/review-18446/` (`head-leg.log`, `base-leg.log`, `head-xml/`, `base-xml/`, `summary.txt`, `df-before.txt`, `df-after.txt`). The phone-agent-2 and vm1 gate logs named in the receipt are on other hosts and were not readable here, so the reruns above are the evidence, not a reading of those logs.

Coverage limits of the new test (the receipt already admits the first): no actual alias narrowing case, no disjoint-parameter case, no V2 case, no root-path case. The second assertion (comma list narrowed by the parameter) passes at head; I did not run it on base separately because the first assertion already fails there.

### 6. Draft accuracy

Read against the diff and the runs. Holds: four files change including the changelog; the pre-fix proof fails on main's production code; head runs 4 tests in `HttpSolrCallCloudTest` with 0 failures; the squash tree equals the gated tree (receipt, verified by tree SHA).

Does not hold, corrected in `receipts/SOLR-18446.md`:

- "What happens today" said the ticket reports the path as the address the client chose. The ticket says the URL path alone should govern collection-scoped requests; the sentence now quotes that.
- "Limits" said only the disagreement case changes and that requests naming no collection in the path route as before. Requests with no collection segment (`/select?collection=c1`) now 404, and a multi-collection path with a non-matching parameter now ignores the parameter. The Limits section now states both and names the V2 gap.
- "Proof" said "the full gate ran on this exact tree". The gate ran two test classes; four other test classes rely on the old precedence (fix 2). The sentence now says which classes ran and that the others are being updated.

The draft is left marked as not ready to send until fixes 1 to 3 land.

## Repairs

None. No commit was made to `solr-18446-path-routing-phone-agent-2`.

## Limits observed

No PR opened, no comments, no Jira edits, no state change. The review worktree `/workspace/worktrees/solr-18446` held the head for the runs and has been removed. df before and after the builds: root 38% used, inodes 9% used both times (`df-before.txt`, `df-after.txt`).
