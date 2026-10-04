# SOLR-12556 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. Unlike most tickets, the reproduction already exists in the tree
(an `@AwaitsFix` test), so this branch is a *guessed fix* plus un-ignoring that test. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12556 - "JSON Field Facet refinement can return incorrect counts/stats for sorted buckets -- when using processEmpty" (Hoss, spin-off of SOLR-12343)
- Branch: `solr-12556-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test un-ignore, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12556.md` in the Solr-issues workspace

## The bug, as understood
In `FacetRequestSortedMerger.getRefinement` a shard that did not say `more:true` is still sent candidate buckets for refinement when `processEmpty:true`
(`returnedAllBuckets = !shardHasMore && !freq.processEmpty`). But after refinement `FacetFieldMerger.getMergedResult` (L122) filters buckets with `isBucketComplete`, which only looked at
`shardHasMoreBuckets` (null unless some shard said `more`), so with processEmpty an unrefined bucket looked "complete" and could be returned ahead of a refined bucket that had re-sorted lower.

## What the branch changes
- `FacetRequestSortedMerger.isBucketComplete`: when `freq.processEmpty` is set, a bucket not seen on a shard counts as incomplete even if that shard did not report `more`; also no longer returns early on `shardHasMoreBuckets == null` in that case.
- `TestJsonFacetRefinement.testProcessEmptyRefinement`: removed `@AwaitsFix(bugUrl=...SOLR-12556)`. That existing test (`sort:'debug asc'`, `debug:'debug(numShards)'`, `processEmpty` true/false, overrequest 0..4) encodes the expected behavior (`debug` 2 with processEmpty, 1 without, only bucket `Ax`).

## What was guessed (verify these first)
1. **Do refined buckets get their shard flag set?** The fix relies on `FacetBucket.mergeBucket` calling `mcontext.setShardFlag` when the refinement response is merged, so refined buckets become "seen" and complete. If not, with processEmpty *every* bucket would be dropped and the test would return no buckets.
2. **A second `@AwaitsFix` for the same ticket remains** at `testSortedSubFacetRefinementWhenParentOnlyReturnedByOneShardProcessEmpty` (~L1112). It may or may not be fixed by this change; try removing it too.
3. Possible extra refinement/requests whenever processEmpty is used (expected, intended).
4. Hoss' TODO in `getRefinement` ("should returnsPartial() check processEmpty internally?") hints at a broader cleanup that was deliberately not attempted.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.facet.TestJsonFacetRefinement"
```
Fail-before: revert only `FacetRequestSortedMerger.java` and keep the un-ignored test; `testProcessEmptyRefinement` should fail.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
