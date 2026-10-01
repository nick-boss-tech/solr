# SOLR-16108 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

With the CompositeId router and `router.field` defined, SPLITSHARD with
`splitByPrefix=true` computed split ranges from a distribution that had
nothing to do with where documents would land, so splits could be degenerate
(reporter saw all docs in one sub-shard, the other empty).

Root cause: `SplitOp.getRanges()` computed `routeFieldName` from the
collection's router props and then ignored it — it always called
`getHashHistogramFromId()` on the unique-key field. That method extracts
route prefixes from id terms; with `router.field`, ids carry no prefix, so
every term was skipped, the histogram came back empty, `getSplits()` returned
null ("no data"), and the split fell back to a blind even split of the hash
range.

Fix (`solr/core/src/java/org/apache/solr/handler/admin/SplitOp.java`):

- `getRanges()` now records whether `router.field` was explicitly configured
  (`hasRouteField`) before defaulting `routeFieldName` to the unique key.
- New `getHashHistogramFromRouteField()` iterates the route field's own
  terms, hashing each complete route value with
  `CompositeIdRouter.getSearchRangeSingle(term, null, collection)` and
  counting `termsEnum.docFreq()` (route values repeat across docs, unlike
  unique ids). Hash collisions between route values merge counts, mirroring
  the existing id-path logic.
- Phase 2 (`SolrIndexSplitter.split` terms path, with `field` = the route
  field per `SolrIndexSplitter` lines ~133-145) hashes route-value term
  strings via `sliceHash(term, null, null, null)` → murmur on the whole
  value for separator-less terms — identical to `getSearchRangeSingle` for
  such values (`preprocessRouteKey` is identity), so phase 1 and phase 2 now
  agree on where each route value lands.

The no-`router.field` path is untouched (still the id-prefix histogram).
The reporter's second note (terms-path hash derivation) was audited against
current main: with `field` = route field, phase 2 already hashes route
values correctly; SOLR-18335's docValues rework is unaffected.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Follow `ShardSplitTest`'s existing router.field split tests (added by
  SOLR-18335): small collection with router.field, two or more distinct
  route values spread across the hash space, SPLITSHARD with splitByPrefix,
  assert docs land in the sub-shard whose range contains their route-value
  hash — not all in one, none missing.
- Unit-level: `getHashHistogramFromRouteField` returns ranges reflecting the
  route-value distribution; route values with docFreq > 1 count all docs.
- Regression: plain CompositeId (no router.field) split behavior unchanged;
  existing `ShardSplitTest` suite.

## Patch limits and follow-ups

- **Not compiled or tested.**
- `getSplits()` can still return null (blind even split) when the route
  field has no indexed terms at all — same fallback as before, out of scope.
- Docs sharing one routing key must co-locate by composite-id semantics;
  "uniform doc distribution" is not the goal — phase-1/phase-2 consistency is.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
