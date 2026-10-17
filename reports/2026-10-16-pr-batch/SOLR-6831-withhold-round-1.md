# SOLR-6831 withhold pivot data on omitted refinement, round 1

Assignment: `assignments/pool-review-2026-10-17-6831-withhold-round-1.md`. Claim: `claims/pool-vm3-6831-withhold-round-1.md` (vm3).

## Head

- Start: `solr-6831-submit` at `4afed04987b2343af58261b83c4b49fda0af45c0`, matching the assignment. No stop was needed.
- Fix commit: `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528` (author Nick Shanin, no trailers), pushed fast-forward to `solr-6831-submit`.
- The PR head moves when this lands. PR 5134 (apache/solr) is open and still shows `4afed04987b2343af58261b83c4b49fda0af45c0` until the PR is updated.

## Tests before

- Production code at `4afed04987b2343af58261b83c4b49fda0af45c0`, with the new assertions added to `TestQueryLimits` and nothing else changed.
- Seed `6831C0FFEE6831`. Log: `/workspace/gates/logs/vm3/6831-before.log` on vm3. Counts from fresh JUnit XML: 9 tests, 2 failures, 0 errors.
- Both failures are the new no-pivot-data assertion: `testPivotFacetRefinementStopsWhenOmittedPartialResults` and `testPivotFacetRefinementWithholdsPivotDataWhenAnyShardOmits`. The `omitted` header assertion in each already passed. The response showed `partialResults: omitted` with pivot data present.

## Tests after

- Change: `FacetComponent.refinePivotFacets` marks the coordinator's pivot output as withheld when a shard refinement answer has the header value `omitted`. `finishStage` then skips the `facet_pivot` output. The local-trip path and the early stop on partial answers are unchanged.
- Seed `6831C0FFEE6831` for all runs below. Logs under `/workspace/gates/logs/vm3/6831-*.log`.
- Focused run (`6831-focused.log`), counts from fresh JUnit XML:
  - `TestQueryLimits`: 9 tests, 0 failures, 0 errors. Both new assertions pass.
  - `DistributedFacetPivotLargeTest` 1, `DistributedFacetPivotLongTailTest` 1, `DistributedFacetPivotSmallAdvancedTest` 1, `DistributedFacetPivotSmallTest` 1, `DistributedFacetPivotWhiteBoxTest` 1, `FacetPivot2CollectionsTest` 10, `FacetPivotSmallTest` 11, `TestPivotHelperCode` 3. All 0 failures and 0 errors.
- Cloud run (`6831-cloudpivot.log`): `TestCloudPivotFacet` 1 test, 0 failures, 0 errors.
- Module check (`6831-check.log`): `:solr:core:check -x test` BUILD SUCCESSFUL.
- Tidy (`6831-tidy.log`): BUILD SUCCESSFUL, no changes beyond the commit.
- Not run: the proof leg against the pre-fix base. The assignment defers it to Nick.

## Push

- `git ls-remote origin refs/heads/solr-6831-submit` before the push: `4afed04987b2343af58261b83c4b49fda0af45c0`.
- Push: `4afed04987..5c1124bdef`, fast-forward, no force.
- Verified after the push: `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528`.

## Draft

- Applied to the live PR 5134 body on 2026-10-17 at 18:17Z, at the direction of the operator. The body was checked after the PATCH: "8 of 8" is gone, "9 of 9" is present, the "is skipped" sentence is gone, and the new withhold test sentence is present. The PR head is `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528`.

- `pr-drafts/2026-10-16-review/PR-5134-limits.md` corrects the PR 5134 Limits bullet, the omitted-answer sentence in the Proof section, and the test count. The live PR body is not edited.

## Verdict

- PASS. The withhold rule is in place at `5c1124bdef`, and both new assertions pass in the focused run.
- The PR head moves to `5c1124bdef`. The draft is ready for the main agent to review. Opening or editing the PR stays with the main agent.
- Overlap noted: `claims/pool-review-2026-10-16-code-round-2-item-1-6831.md` (vm2, test-only check at `4afed04987`) has no deliverable on the branch. It was not touched.

## Proof leg (pre-fix base, run 2026-10-17)

- Decision on the omitted-answer behavior is recorded in the assignment (withhold, Nick, 2026-10-17). No question is open for Nick.
- Pre-fix production at `4afed04987b2343af58261b83c4b49fda0af45c0` with the head test file: the two new no-pivot-data assertions fail on the pivot assertion, the `omitted` header passes. Seed `6831C0FFEE6831`. Log `/workspace/gates/logs/vm3/6831-before.log`. Counts from fresh JUnit XML: 9 tests, 2 failures, 0 errors.
- PR base `8373d0392a612b0b7ff901b75b5ca041c1f85a2c` (apache/solr main, from the PR API) with the head test file overlaid and no branch production changes. Seed `6831C0FFEE6831`, run with `-DleaderVoteWait=60000` through `tests.jvmargs`. Log `/workspace/gates/logs/vm3/6831-proof-base.log`. Counts from fresh JUnit XML: 9 tests, 6 failures, 0 errors.
  - `SolrServerException: No live SolrServers available to handle this request` in `testPivotFacetRefinementStopsWhenPartial`, `testPivotFacetRefinementWithholdsPivotDataWhenAnyShardOmits`, `testPivotFacetRefinementStopsWhenOmittedPartialResults`. This is not a cluster startup error. Cause, from the log and the stack trace:
    - Each test's first query reaches a coordinator, which returns HTTP 500 `No pivot refinement response from shard` (base `FacetComponent.refinePivotFacets`). The first 500 is logged 121 ms into `testPivotFacetRefinementStopsWhenPartial` (log line 1772 onward), and the test fails 42 ms later at the same query (TestQueryLimits.java:272).
    - SolrJ's `LBSolrClient` treats 500 as retryable and marks the endpoint a zombie (`makeServerAZombie`, `LBSolrClient.java:650`). Once every coordinator has returned 500 for the request, the iterator has no live endpoints and throws `No live SolrServers`.
    - The startup log before the first test has only `solr.logs.dir` warnings and normal leader election. The pre-fix run at `4afed04987` has zero 500 responses, so it never reaches this path.
    - `-DleaderVoteWait=60000` was not the fix, and it is not needed. Its premise (a startup timeout) does not match this log.
    - Cause at the base: `FacetComponent.java:1041` on the base throws `No pivot refinement response from shard` for any null pivot answer. There is no `haveCompleteResults` guard. That throw is the defect that commit `4afed04987` fixes, not the withhold change. The client reports the resulting 500 as endpoint exhaustion.
- Decision (vm3, 2026-10-17, taken on the evidence above because the main side is away): the pre-fix leg at `4afed04987` is conclusive for the withhold change. Both new no-pivot-data assertions fail on the pivot assertion, with pivot data present, no 500 responses, no infrastructure errors, seed `6831C0FFEE6831`. The PR-base leg is not a proof of the withhold change. It shows only that the base lacks the branch's fixes (6 of 9 fail, 3 of them through the `4afed04987` defect).
  - Assertion failures at the base, as expected because the base has none of the branch's production changes: `testPivotFacetStopsBelowTopLevel` (no partial result), `testPivotFacetTruncatesWhenLimitsTrip` (no pivot data at all), `testPivotFacetOmitsResultsWhenPartialResultsDisallowed` (detail does not name the pivot limit check).
- Net: the withhold assertions discriminate at `4afed04987` (the pre-fix production). At the PR base they are INCONCLUSIVE because of the startup error, not because of a verdict on the fix.
