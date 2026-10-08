# solr-8954-submit

- Branch: origin/solr-8954-submit
- Head: 1d981abe7000 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base e432df19c4a5, 23 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `RealTimeGetComponent.java` (`createSubRequests`: IDs whose `getTargetSlice` returns null go to `unroutedIds`, then one shard request to ALL shards, `:1058-1098`), `CustomCollectionTest.java` (+5, three RTG asserts on an implicit-router collection), changelog `SOLR-8954-rtg-implicit-router-no-route.yml` (`type: fixed`). No `SOLR-8954-TESTING.md` on the tip.
- Verdict: Needs work (the implicit-router case is covered, but the fan-out also fires for compositeId collections that have a router field, which the changelog and tests do not describe)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-8954-review.md` (verdict Needs work) was written at the same head (1d981abe7000). No delta.

- Bulk F1 (MEDIUM, all-router fan-out for unrouted IDs): **confirmed, and wider than the bulk wording.** The bulk review says "every router whose `getTargetSlice` returns null"; the head shows that `CompositeIdRouter` does return null for this RTG case, under a condition the bulk review did not name. See finding 1.

## Findings (ranked)

1. **MEDIUM, verified by reading. Unrouted IDs fan out for compositeId collections too.** `createSubRequests` checks only `slice == null`; it does not check the router (`RealTimeGetComponent.java:1058-1068`, then the ALL-shards request at `:1090-1097`). `CompositeIdRouter.getTargetSlice` returns null when `sdoc == null && route == null` and the collection has a route field (`CompositeIdRouter.java:160-166`, the `getRouteField(collection) != null` check). RTG passes `sdoc = null`. So an RTG on a default compositeId collection with `router.field` configured and no `_route_` now asks every shard. In base the `continue` at `:1061` dropped the ID. The changelog ("implicit router") and both tests describe only the implicit case, so the widened behavior is not described.
   - The SOLR-8009 precedent (`origin/solr-8009-submit:solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:1072`, `if (coll.getRouter() instanceof ImplicitDocRouter)`) guards the same fallback to the implicit router. The bulk review's suggestion to apply that guard is the minimal fix.
   - Proposed fix (not applied): add the `ImplicitDocRouter` guard around the unrouted-ID branch, or keep the fallback for compositeId + router.field and add a test that shows the intended result there. See owner call 1.

2. **LOW, verified. Changelog and code disagree on scope.** The changelog says "on a collection with the implicit router"; the code path does not check the router (finding 1). The changelog is correct only if finding 1 is fixed.

3. **LOW, verified. Test coverage is implicit-only.** The three added asserts are on the implicit-router collection in `CustomCollectionTest` (`:178-187`). No test covers a compositeId collection with a router field and no `_route_`, so the widened branch is not exercised.

## Owner calls (not decided here)

1. **Should compositeId collections with a router field fan out for RTG without `_route_`?** The ticket describes the implicit router. Options: restrict the fallback to `ImplicitDocRouter` (matches SOLR-8009's guard), or accept the wider fan-out as intended and document it with a test. The bulk review asks for one of the two, with evidence. It is posed here and not decided.

## Proposed fixes (not applied; the owner decides)

- Finding 1: restrict the unrouted-ID fallback to `ImplicitDocRouter`, or add a compositeId test if owner call 1 keeps the wider behavior.
- Finding 2: no change needed if finding 1 is fixed. Otherwise make the changelog say what the code does.
- Finding 3: add a compositeId + router.field case to the test.

## Interactions with other branches

- SOLR-8009 has the router guard that this branch lacks (see finding 1). That branch was not reviewed in this round; only its guard line was read.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- `ImplicitDocRouter.getTargetSlice` was read only up to the `route`/`sdoc` branches (with both null, `shard` stays null). The rest of the method, which turns that into a null slice, was not read; the implicit null return is otherwise taken from the changelog and the test.
- Whether a compositeId collection with a router field and no `_route_` is a common RTG call pattern in practice was not checked.
- The JIRA ticket text was not re-read; the implicit-router premise is from the bulk review.
- No GitHub or JIRA writes were made.
