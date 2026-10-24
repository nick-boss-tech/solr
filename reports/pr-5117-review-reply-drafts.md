# PR #5117 reply drafts (not posted)

Written by vm2, 2026-10-24. Nothing is posted. Each draft waits for Nick's go.

## Review thread 1: drop the duplicate test (comment 4365850955)

Dropped `skippedPartialShardApproximateTotalExcluded` in c148e7924f, since `skippedPartialShardContributesNothing` already checks the same things.

## Review thread 2: use the existing mock helpers (comment 4365869209)

Ported the test to `MockShardRequest`, `MockResponseBuilder` and `MockSortSpecBuilder` in c148e7924f, and removed the core setup.

## Review thread 3: user-facing changelog title (comment 4365873915)

Changed the changelog title to your wording.

## Rebase reply, corrects the "merged" premise (replaces the earlier draft in `reports/pr-followups-round-1.md`, item 2)

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Thanks for the bump, @ercsonusharma. A small correction: #5117 is still open and has not merged. The PR that merged on 2026-10-23 is #5172 (SOLR-18550). This branch is already rebased on it, six commits on top of #5172's merge, and my 14:23 comment covers what changed in the rebase. It is ready for review now.
```
