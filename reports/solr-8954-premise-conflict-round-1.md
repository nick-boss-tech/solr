# SOLR-8954 premise conflict: investigation round 1

Date: 2026-10-14. Investigation and records only. No branch code changed, nothing pushed to the submit branch. Sources: `receipts/SOLR-8954.md`, `gates/SOLR-8954-fullsolrcloud-rtg.md` (with its recorded Result), `claims/pool-vm2-gate-backlog-8954.md`, `reports/search-components-2.md`, and the branch diff read at both heads.

## The conflict

The receipt said "GATE GREEN at the live tip" for head 1d981abe7000bacf7cc3473059f8bf693a9c7ce9. The 2026-10-05 gate ran CustomCollectionTest (4 of 4), TestRandomFlRTGCloud (2 of 2) and TestRealTimeGet (4 of 4). It never ran FullSolrCloudDistribCmdsTest. A measured run filed from the census follow-up (search components drafts) ran that class's composite-router real-time get test at the head and at the merge-base:

- Head 1d981abe7000: FAIL, tests=1 failures=1 errors=0, `java.lang.AssertionError: expected:<0> but was:<1>`, seed 15942F994B64B75C, rc=1.
- Merge-base e432df19c4a50b9d54d7fba545397b859cc54f98: PASS, tests=1 failures=0 errors=0, rc=0.
- Run by vm2, log `gates/logs/vm2/queue-g8954-gate-20261014T100614Z.out`, one method per run, worktree clean after each run. The stack line was not captured because the base run overwrote the JUnit XML before it was read; the assertion text matches only the no-route assertion in the method (below).

## What the branch changes

The diff against the merge-base is three files: a changelog fragment, a test addition in CustomCollectionTest, and one production change in `RealTimeGetComponent`. In the distributed real-time get path, an id whose router returns a null target slice was silently skipped. The branch collects those ids and, after the routed requests, adds one shard request carrying them with shards set to ALL. The code comment gives the motivating case ("e.g. the implicit router with no _route_: the id says nothing about its shard"), and the changelog title scopes the change to "a collection with the implicit router". The branch's own premise stands and is untouched by this finding: base production with only the branch test file fails `testRouteFieldForImplicitRouter` at the first assertNotNull on getById (seed F3CB251B521A18D7), because `ImplicitDocRouter.getTargetSlice` returns null for a get with no `_route_`, no document, and no route value, so the document is never asked for.

## What the failing test pins

`testRTGCompositeRouterWithRouterField` (FullSolrCloudDistribCmdsTest.java, identical at both heads; the branch does not touch the file) builds a `compositeId` collection with router field `routefield_s`, indexes documents with route values europe and africa, then asserts:

- line 300-301: a real-time get of id "3" with no `_route_` returns 0 documents;
- with `_route_` europe, the same get returns 1 document.

That expectation follows the router's own contract. `CompositeIdRouter.getTargetSlice` returns null, deliberately, for a get or delete-by-id (no document) with no route when the collection defines a route field; the method's comment states this. The document was routed at index time by the route field's value combined with the id, so a bare id cannot be routed; null is the signal for "not routable", and the base component treats null as "ask nobody", which is what returns 0.

## Classification: (a) the branch change causes the failure

The branch treats every null slice as "unrouted, ask all shards", for every router. For the implicit router that reading is right. For compositeId with a router field it is wrong: it overrides a deliberate router contract, and the no-route get now fans out to all shards and finds the document, which is the measured 1 against the expected 0. The compositeId effect is also outside the branch's own stated scope, since its changelog and comment name the implicit router.

Not (b): the failing test does not encode the behavior this ticket sets out to change. The ticket's premise is the implicit-router skip, and that premise is proven on its own test. The compositeId expectation is a separate, deliberate contract.

Not (c): the run measured what it claims. Both heads were verified by ls-remote at claim time, the base is the true merge-base of the branch (checked with git merge-base), one method ran per checkout, and the failure is the exact assertion a reading of the head predicted in the job file before the run.

## Owner decision 1

From `reports/search-components-2.md`, Owner decisions, item 1: "SOLR-8954: close it, or make it a test-only follow-up. Recommended: 8009 carries the all-shards fix." The same report's verdict row for this branch reads "Held; do not land as written", and its landing-order section has 8954 not landing as written and conflicting with 8009 on merge.

## Correction made and recommended next step

`receipts/SOLR-8954.md` no longer carries an unqualified GATE GREEN: its status is scoped to the classes the 2026-10-05 gate ran, and it now records the measured conflict, this classification, and the owner decision.

Recommended next step, under owner decision 1: the branch does not land as written; close the ticket or reduce it to a test-only follow-up, with SOLR-8009 carrying the all-shards fix. If the fix is ever revived on this ticket, its shape is to scope the all-shards fan-out to the case where the id carries no routing information at all (the implicit router), leave the compositeId-with-router-field null contract alone, and re-gate with FullSolrCloudDistribCmdsTest in the run list.
