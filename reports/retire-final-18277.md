# Retire pack: SOLR-18277 (three fork branches)

Verdict: RETIRED (owner ruling 2026-10-13). The fix merged as 0cc328310f8. The branches are labelled retired and left in place. The record is complete (receipts/SOLR-18277.md, written 2026-10-13, checked and corrected 2026-10-15).
Round item: reports/retire-candidates-review-round-rr2.md, item 3 (SALVAGE FIRST, evidence only). The salvage was the receipt, and it is done.

## Premise state

receipts/SOLR-18277.md (completed record): "Status: GATE GREEN (hardened) at the gated head, and merged." It states that no fail-before proof exists for the fix, and that "no run in this record fails on base and passes at the fix. SOLR-18277 has no fail-before pin." The round's finding is the same: fork runs show testSplitAfterFailedSplit and testSplitAfterFailedSplit2 passing on base production code, so the branch's tests do not pin the fix.

## Heads checked (ls-remote origin, 2026-10-15)

- solr-18277-submit: 17c0e64481285e8683c6bf974d9c7f9dfc73555a (PR #4959 head).
- ci/solr-18277: 35c9e9952285c3d4f4c428b62da637ceb6e48c6b.
- ci/solr-18277-proof: 44c35a3deafd765893ab24f2d41df95a3723efee.

PR #4959 (GitHub API, 2026-10-15): state MERGED, mergedAt 2026-10-05T10:46:07Z, mergeCommit 0cc328310f8f4162572ea5bd5256cea29543e261, headRefOid 17c0e644812.

## What the branches hold, and whether main has it

- solr-18277-submit: six commits above base 0d2a4649c79. Four files: changelog/unreleased/SOLR-18277.yml, SplitShardCmd.java, ShardSplitTest.java, SplitShardCmdCleanupTest.java. These are the fix and its tests, now on main. Live main has not changed SplitShardCmd.java, ShardSplitTest.java or SplitShardCmdCleanupTest.java since d76fc14b (checked 2026-10-15).
- ci/solr-18277 (35c9e995228, one commit on 17c0e644812): adds .github/workflows/fork-test-runner.yml (48 lines). Fork tooling only.
- ci/solr-18277-proof (44c35a3deaf, one commit on base 0d2a4649c79): the same workflow file, plus ShardSplitTest.java with two @AwaitsFix(bugUrl SOLR-18277) annotations removed. Those two annotations are absent on upstream main (checked 2026-10-15).

## Gainable and taken

- Gainable: nothing in code. The run evidence is in the receipt. The workflow file is fork tooling and is recorded by SHA in the receipt.
- Taken: the fix is merged as 0cc328310f8.

## Owner step

- Done: receipt written and completed (receipts/SOLR-18277.md). The CI commit SHAs are recorded there (35c9e995228 and 44c35a3deaf), so fork-test-runner.yml stays recoverable.
- Branches stay in place under the 2026-10-13 ruling. Nothing is deleted.
- Deletion step, NOT taken and superseded by that ruling. It would apply only if the owner reverses the ruling. Names in full, on origin (the fork, nick-boss-tech/solr): `solr-18277-submit`, `ci/solr-18277`, `ci/solr-18277-proof`. Example: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-18277-submit`, then the same for the two ci/ names.
- PR to close: none. #4959 is merged.
- Jira note: none needed for the retirement. Nothing is drafted.
- Jira status: the retire-round assignment records SOLR-18277 as Fixed on 2026-10-06. The slices report records the Jira Fixed box as unchecked. Neither was checked in this pass (no Jira access here). Confirm it before any Jira write, but it does not gate the retirement.

## Condition

None for the retirement. The verdict rests on the record. If a later reader asks whether the fix is pinned by a test, the answer is no, as recorded in the receipt. An optional fail-before for the unit class would need a variant that does not call the new helper. That is not required for the retire decision.

## Gaps

- The harden record (g4959-harden2.log) is not in this worktree, so gate results are cited from the receipt.
- The counts for the four fork runs are as the round read them from the logs (rr2). The conclusions, heads and branches were checked through the API on 2026-10-15.
