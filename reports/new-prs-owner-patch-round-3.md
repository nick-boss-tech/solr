# Branch patches for the draft PRs, round 3

Status at this write-up: one patch pushed and one committed but blocked. Nothing has been opened, marked ready, rebased, or commented on.

## SOLR-18010 (PR #5118): changelog title

- Pushed to `solr-18010-submit`: `c3685bb37d9d` to `9e3ccafe390` (a fast-forward, through `research\push-fork-ff.ps1`).
- Change: the changelog title now states the scope of the fix. Changelog only. Code is identical to the gated head.
- Gate job pushed: `gates/SOLR-18010-changelog-regate.md` (commit `cc866d727ef`). It is OPEN until vm2 claims it.
- Still owed on the PR: the changelog link and the "Head" line in the body name the old head. Update them after the changelog gate lands.

## SOLR-18541 (PR #5117): exactness, blocked on the push

- Committed locally in the detached worktree `wt/SOLR-18541-c1`: `fcd92f23ba6`, parent `d5ed34125db`. Author Nick Shanin, no trailers.
- Production change: in the skip branch of `CombinedQueryComponent.java`, `hitCountIsExact = false` when the skipped shard reported hits (`docs.getNumFound() > 0`). Three added lines.
- Test: a new test with two partial shards that both skip and report hits, asserting numFound 0 and numFoundExact false. One added exactness assertion in `skippedPartialShardContributesNothing`.
- Changelog title updated to say exactness holds only when every shard reports an exact count and no skipped shard reported hits.
- NOT PUSHED. `research\push-fork-ff.ps1` accepts only `solr-` and `review-` branch names, and `combinedquerycomponent-merge-totals` does not match. A plain git push has no credentials here. The branch is still at `d5ed34125db`. Owner decision needed: allow this branch name in the wrapper, or push it from a shell with the fork credentials.
- Not yet done, waiting on the push: the gate job at the new head, and the PR body update.

## Notes on the 5117 change

- The skipped-shard condition differs from QueryComponent's all-skipped rule, which folds skipped shards before the skip. In CombinedQueryComponent the skip comes first by design (the comment above the skip). The hits condition is the rule the review asked for, so the body must describe it as written.
- Two comments near the skip still describe only counts. They are slightly stale and were left unchanged.

## Not done

- No PR opened, closed, marked ready, rebased or commented on.
- No build, test, or gate run on this host.
