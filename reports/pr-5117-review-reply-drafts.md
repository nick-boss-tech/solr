# PR #5117 reply drafts (not posted)

Written by vm2, 2026-10-24. Nothing is posted. Each draft waits for Nick's go.

## Review thread 1: drop the duplicate test (comment 4365850955)

Dropped `skippedPartialShardApproximateTotalExcluded` in c148e7924f, since `skippedPartialShardContributesNothing` already checks the same things.

## Review thread 2: use the existing mock helpers (comment 4365869209)

Ported the test to `MockShardRequest`, `MockResponseBuilder` and `MockSortSpecBuilder` in c148e7924f, and removed the core setup.

## Review thread 3: user-facing changelog title (comment 4365873915)

Changed the changelog title to your wording.
