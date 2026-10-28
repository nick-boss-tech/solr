# Report: PR 5255 alias-lock follow-up gate, round 4 (vm5)

Receipt: `receipts/PR-5255-alias-lock-followup.md`. Head `52dacc23b5bb67d254c111c948733da7bc4a9f81`.

Timeline, from `results.txt`:
- Tidy passed at the round-1 head with one file reformatted. The formatting was committed as `c09411a16c`.
- Round 2 failed to compile at `c09411a16c`. Repaired as `bf5098b3da`.
- Round 3 ran, with 2 of 6 focused tests failing on both seeds. A single-method probe showed SolrJ `waitFor` deletes the status before the test reads it. Repaired as `52dacc23b5`.
- Round 4 at `52dacc23b5`: tidy, focused (both seeds), and neighbors ran. Root check failed in the worktree, because Antora needs a full clone. Root check passed in `/tmp/pr5255-clone`. The negative proof passed its intended shape on the parent.
- Interruptions: session teardown twice and one VM crash. Each relaunch re-ran only unfinished steps. Completed evidence was not reused across heads.

Open for the owner: whether the inherited `@AwaitsFix` skip (`CollectionsAPISolrJTest.testCreateAndDeleteShard`, unchanged from parent) is acceptable under this gate's zero-skip bar.

Nothing public was posted: no PR, comment, or JIRA action.
