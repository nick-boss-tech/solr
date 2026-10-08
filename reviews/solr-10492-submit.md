# solr-10492-submit

- Branch: origin/solr-10492-submit (the PR branch; ci/10492-grouping is a test-run variant with the same solr/ code plus a fork-only workflow commit)
- Head: ecf21e2192c
- Base: upstream/main at 9d7cc2884e8 (merge-base e2cdb2d7e8a, 35 commits behind, 7 ahead)
- Scope: SimpleFacets.java, TestDistributedGrouping.java, changelog fragment. No fork-only files.
- Verdict: Needs work
- Reviewer: spot-check, 2026-10-07

## Findings (ranked)

- **MEDIUM (hypothesis):** No fail-before evidence. The new TestDistributedGrouping block compares distributed output with the control, so it should fail without the fix, but that is not shown. Needs a fail-before run before PR.
- **LOW (hypothesis):** `groupField` now falls back to the raw `group.field` request parameter when there is no grouping spec. Check that no non-facet request path changes behaviour.
- **Checked, OK:** the `getGroupedCounts(..., Predicate<BytesRef> termFilter)` call matches the upstream signature.
- **Housekeeping:** the branch is 35 commits behind upstream/main. Rebase before PR (owner decision, not done here).

## Not checked

Nothing was compiled or run. Line numbers not taken.
