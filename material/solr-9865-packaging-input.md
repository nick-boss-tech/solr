# SOLR-9865 packaging input: the rollback overlap

Read on 2026-10-12 through the GitHub wrapper (read only). The main side makes the packaging call. Nothing here is a posting, and no PR, comment or branch was touched.

## The overlapping PR, as it stands

- PR #5100, branch `solr-18532-submit`, head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, state OPEN, last updated 2026-10-11 14:52Z.
- Its change writes back the saved exact bytes of `index.properties` on rollback. Its Proof is the same test shape as SOLR-9865's regression test, with TestRestoreCore 4 of 4 and RestoreCoreOpTest 1 of 1 at `f1e5031fc3a9` (verified 2026-10-10 per its body).
- Its own body says: "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict."

## What the SOLR-9865 draft says about it

The draft's Choice (third route) and Limits (last bullet) match that text:

- The third route is "the SOLR-18532 change in PR #5100, which keeps the previous index.properties content and writes it back on rollback, so it covers the same failed restore".
- Limits: "If PR #5100 lands first, its change covers this rollback, and this change keeps only the regression test."
- The OWNER NOTE (line 1) holds posting until the packaging call. It says: "if that change lands first, this branch drops its rollback write-back at packaging and keeps only the regression test."

Consistency: the PR's stated follow-up (drop the rollback edit once it lands) and the draft's packaging rule describe the same outcome. No text change is needed for consistency.

## What the main side still decides

1. Packaging: whether SOLR-9865's branch keeps its rollback write-back or drops it once PR #5100 lands. Until that is recorded, the draft's What-this-change and Proof describe the write-back still on the tip (`4937608bb181`, where `RestoreCore.java` line 235 holds `core.modifyIndexProps(previousIndexDirName)`).
2. If the write-back is dropped, What-this-change and Proof must be re-derived at the packaged head, which is test-only on top of PR #5100.
3. The draft stays on its OWNER NOTE hold until item 1 is recorded.

## Not checked

- Whether PR #5100 has merged since 2026-10-11 14:52Z. The read was at that state.
- The PR's gate or CI results.

## Main-side record (2026-10-12)

Judged against the recorded decision (Nick, 2026-10-10): SOLR-18532 lands first; when the SOLR-9865 branch is prepared for opening, it drops its overlapping RestoreCore rollback hunk; the SOLR-18532 PR's Limits already name that follow-up.

The input matches the recorded decision and raises nothing the decision does not cover:

- Item 1 above (keep the rollback write-back or drop it once the SOLR-18532 change lands) is the question the decision already answers: the branch drops the write-back at packaging. No new call is needed, and none is taken here.
- Item 2 (re-derive What-this-change and Proof at the packaged head) is a consequence of that drop: packaging-time work under the same decision, at the test-only head on top of the landed SOLR-18532 change.
- Item 3 (the draft stays on its OWNER NOTE hold until item 1 is recorded) is satisfied by the recorded decision. The hold now stands until the packaging itself is done, not until a further call.
- The consistency section above agrees with the decision: the SOLR-18532 PR body states the same follow-up (drop the rollback edit on the SOLR-9865 branch once it lands), and no text change is needed for consistency.

Settled as packaging-time work. The two not-checked items above are state checks for the packaging pass, not gaps in the decision: the landing order the decision sets (SOLR-18532 first) is verified when the packaging is done.
