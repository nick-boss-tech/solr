# PR #5117 reply drafts (not posted)

Written by vm2, 2026-10-24, after the PR branch moved to `c148e7924f`. Nothing is posted. Each draft waits for Nick's go.

## Review thread 1: drop the duplicate test (comment 4365850955)

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Done. I dropped `skippedPartialShardApproximateTotalExcluded`. It ran the same setup as `skippedPartialShardContributesNothing` and checked two things that test already checks.
```

## Review thread 2: use the existing mock helpers (comment 4365869209)

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Done in c148e7924f. The class now builds its shard responses with `MockShardRequest`, the response with `MockResponseBuilder` and the sort spec with `MockSortSpecBuilder`. The private shard, header, request and core helpers are gone. The component is set up with `init` and `inform` on a mocked core, so the test no longer starts a core.

To make that work I extended the shared helpers, adding methods without changing their existing behaviour: per-query and sort-value shard responses and an errored-shard builder on `MockShardRequest`, combiner-query, shards-tolerant and schema-field settings on the builders. `QueryComponentPartialResultsTest` still passes. The `doc` and `docList` data builders stay in the test.
```

## Review thread 3: user-facing changelog title (comment 4365873915)

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Done. The changelog title now uses your wording.
```

## Corrected rebase reply (replaces vm6's draft in `reports/pr-followups-round-1.md`, item 2)

The earlier draft said "four commits". The branch is now six commits on top of #5172's merge commit `79b23df58d`, so the count changes. The rest of that draft's facts still hold: #5117 is open and not merged, and #5172 is the PR that merged.

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Thanks for the bump, @ercsonusharma. A small correction: #5117 is still open and has not merged. The PR that merged on 2026-10-23 is #5172 (SOLR-18550). This branch is already rebased on it, six commits on top of #5172's merge, and my 14:23 comment covers what changed in the rebase. It is ready for review now.
```
