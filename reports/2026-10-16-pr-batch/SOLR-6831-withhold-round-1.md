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

- `pr-drafts/2026-10-16-review/PR-5134-limits.md` corrects the PR 5134 Limits bullet, the omitted-answer sentence in the Proof section, and the test count. The live PR body is not edited.

## Verdict

- PASS. The withhold rule is in place at `5c1124bdef`, and both new assertions pass in the focused run.
- The PR head moves to `5c1124bdef`. The draft is ready for the main agent to review. Opening or editing the PR stays with the main agent.
- Overlap noted: `claims/pool-review-2026-10-16-code-round-2-item-1-6831.md` (vm2, test-only check at `4afed04987`) has no deliverable on the branch. It was not touched.
