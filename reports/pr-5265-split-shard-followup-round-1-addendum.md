# Addendum: PR 5265 description updated (phone agent, at the owner's direction)

Add-only; follows `reports/pr-5265-split-shard-followup-round-1.md`, whose remaining-limits section left the PR description update to the owner.

- 2026-10-30T02:54Z: at the owner's direction, the PR #5265 description was edited (own-PR description edits; no comment posted). Changes: the Proof section now cites the follow-up head `0495ffa2b00772189d2f92d83b32140f1b5e8b39` and the full test set (SplitShardAPITest 4, SplitShardMessageMappingTest 3, TestCollectionAPIs 2, plus `:solr:core:check -x test`); the draft-status sentence was added after the AI header; the Limits section gained a SOLR-17241 paragraph stating the non-hosting-node `wt` defect is pre-existing, is addressed in its own PR under SOLR-17241, and that PR may need to merge before this one so the migrated endpoint answers the default javabin client correctly from every node.
- Read-back verified: new head cited, SOLR-17241 paragraph present, no stale references to `6c56a18236`.
