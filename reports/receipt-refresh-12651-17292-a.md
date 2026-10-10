# Receipt refresh round 2, part a: SOLR-12651

Result: draftable after the draft edits below. The refreshed receipt (commit `a266ae9c509`) matches the branch. The live head is `f3131d1ee846`. The old gated head `90032e274b7` is still reachable, and `git log 90032e274b7..f3131d1ee846` shows exactly two commits (`5fb9fb01717`, `f3131d1ee84`), which matches the receipt. One branch item is recommended before the PR opens: the changelog title overclaims (finding 10).

The draft was edited in place. No gate log, JUnit XML, or test run was available, so the receipt's counts (2 of 2) and "exactly one failure" are the receipt's claims, not verified here.

## Findings

Draft: `pr-drafts/solrcloud/SOLR-12651.md`.

1. FIX, line 23 (Proof bullet). The old head `90032e274b7` and the date 2026-10-04 were stale. The refreshed receipt gives the head `f3131d1ee846`, 2 of 2 at the head from fresh JUnit XML, exactly one failure with `RestoreCmd.java` at the merge-base, and a live-tip gate finished 2026-10-10. Edited.
2. FIX, line 24 (deleted). It held the old head, the word "owed", and a sentence the refreshed receipt supersedes. The two-commit history is internal and is not needed publicly. Deleted.
3. FIX, line 21 (Proof summary). "The copy-failure check fails on the base code and passes with this change. The property-upload check still needs a run." is superseded. Replaced with one claim, and the counts stay in the bullet, so the claim is stated once.
4. FIX, line 9 (body). "If a step fails, nothing removes the collection." is too broad. Base `RestoreCmd.java` already cleans up when replica creation reports a failure (base blob `14c7aac0d151`, around line 284, "Restore failed to create initial replicas" followed by `cleanupCollection`). The accurate claim covers steps that throw. Edited to "If a step throws an error, nothing removes the collection."
5. FIX, line 7 (bold summary). "with copied data that cannot be read" is wider than the evidence. A property-upload failure happens before any shard data is copied. Edited to "A RESTORE that throws an error after it creates the new collection leaves that collection in place."
6. NOTE, proof scope (lines 21 to 23, receipt line 7). The receipt does not name the failing assertion. In `errorRestore` (head lines 131 to 198) the copy-failure block comes before the property-upload block, and the test stops at its first failed assertion. On base the run most likely fails at the copy check and never reaches the property-upload check. The draft does not claim that the property-upload check fails on base, and it should not. The gate's failure line would confirm which assertion failed. The class count of 2 includes the inherited `testRestoreFailure` (`AbstractCloudBackupRestoreTestCase.java` line 168) alongside `test()` (line 121). No edit.
7. NOTE, the Choice (lines 25 to 31). Present and framed as a question for the submission. It does not claim the gate settles it. The ticket commenter is named in the receipt, and the draft says "A comment on the ticket", which is fine. No edit.
8. NOTE, line 17 ("Behavior change: ..."). It restates the bold summary on line 13. Optional: keep only "The collection name is free again for a new attempt." No edit made.
9. NOTE, Limits (lines 33 to 39), checked against head. Bullet 1 holds: `addReplicasToShards` (head `RestoreCmd.java` line 319) and `restoringAlias` (line 324) run after the catch that closes at line 316. Bullet 2 holds: with a user async id, a shard copy failure is recorded into results by `waitForAsyncCallsToComplete` (`CollectionHandlingUtils.java`, around lines 752 to 815 at head) and does not throw, so the catch never runs. Not in the draft: on that async path the restore appears to continue after a recorded failure. This was read, not run, and it is unchanged from base. An optional Limits sentence is the lead's call. No edit.
10. NOTE, branch changelog (`changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml`, not edited). Its title says a RESTORE that "fails after the new collection has been created now deletes the new collection". At head, failures in `addReplicasToShards` and `restoringAlias` come after the catch and do not delete the collection, so the title overclaims and disagrees with the draft. Suggested title, which needs a branch commit and is the owner's decision: "A RESTORE collection operation that fails before its restored shards become active now deletes the new collection instead of leaving the partially restored collection behind." The YAML reads as valid by eye; it was not parsed by a tool.
11. NOTE, anchors and links, all verified at the full head `f3131d1ee8463388c68f18deab3c4ac3262ead83`: `RestoreCmd.java` #L265-L316 (declared at 265, try at 266, outer catch closes at 316); `errorRestore` #L131-L198 (declared at 131, closing brace at 198); changelog path exists at head. Old head references remaining in the draft: none. Dashes: none. Process words (receipt, gate, round, takeover, premise, owed): none.
12. NOTE, formula and length. Bold one-line summaries are on all five sections. The AI header and AI assistance footer are present, and the Jira link is on line 3. Length: 3,246 characters (UTF-8), 3,250 bytes, after edits. Before edits it was 3,486 bytes. Both are under the roughly 3,500 guide.

## Edits made (draft only)

- Line 7. Old: "**A RESTORE that fails after it creates the new collection leaves that collection in place, with copied data that cannot be read.**" New: "**A RESTORE that throws an error after it creates the new collection leaves that collection in place.**"
- Line 9. Old: "If a step fails, nothing removes the collection." New: "If a step throws an error, nothing removes the collection."
- Line 21. Old: "**The copy-failure check fails on the base code and passes with this change. The property-upload check still needs a run.**" New: "**The restore test fails on the base code and passes with this change.**"
- Line 23. Old: "- TestLocalFSCloudBackupRestore 2 of 2 at 90032e274b7, verified 2026-10-04. On the base code, a restore that fails while copying shard data leaves its new collection behind. The checks are in [errorRestore](...f3131d1ee8463388c68f18deab3c4ac3262ead83...#L131-L198)." New: "- TestLocalFSCloudBackupRestore 2 of 2 at f3131d1ee846, verified 2026-10-10. With RestoreCmd.java at the merge-base, the run has exactly one failure: a failed restore leaves its new collection behind. The checks are in [errorRestore](...same link...)."
- Line 24. Deleted, with the blank line before "## A choice to check" kept.

## Not checked

- Gate logs `g12651-livetip-gate.log`, `g12651-gate2.log` and `g12651-premise.log` were not found in the research, receipts or worktree folders. A full-tree search timed out, so their absence across the whole tree is not proven.
- JUnit XML and the seed `12651C0FFEE12651` are not on disk where looked. The counts come from the receipt.
- The ticket text (the Solr 7.x symptom) was not checked against JIRA.
- Base: the upstream/main merge-base `14c7aac0d151` was used, and the branch diff against it is exactly the three expected files. The receipt does not name its base SHA. If the gate used a different base, the proof sentence needs a recheck.
- The async behavior in finding 9 was read from `CollectionHandlingUtils`, not run.
- No builds, Gradle, tests, `gh` write calls or commits.
