# Receipt: PR review apache/solr #3684 (vm4)

- PR: https://github.com/apache/solr/pull/3684, open, not draft, head branch `SOLR_17712_remove_waitForFinalState_default_true` on `abumarjikar/solr`, base `69a632d9b5`, 72 commits, `mergeable_state` unknown at the snapshot. 19 files, +64/-30 (GitHub files API).
- Files: changelog entry (added, 8 lines, `type: improvement`), `solr/CHANGES.txt` (added, empty), API request bodies (3, `@Deprecated` context only), `ActiveReplicaWatcher.java`, `AddReplicaCmd.java` (`waitForFinalState` default false to true, line 116), `CollectionHandlingUtils.java`, six other collection command classes, `TestPullReplica.java`, `CreateCollectionAPITest.java`, `collection-management.adoc` (4 lines), `CollectionAdminRequest.java`.
- Not in the diff: `CommonAdminParams.java`, so the deprecation in the title is not implemented.
- Base checks at `c8ab71430f`: `CommonAdminParams.WAIT_FOR_FINAL_STATE` is defined (line 32); `git cat-file -t c8ab71430f:solr/CHANGES.txt` returns nothing, so the empty `solr/CHANGES.txt` would be a new file the base does not expect.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/3684.md`.
