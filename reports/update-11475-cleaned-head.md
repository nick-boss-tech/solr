# SOLR-11475 draft and receipt at the cleaned-history head: round roll-up

Claim: `claims/update-11475-cleaned-head.md` (commit `e4ce51ced81`). Upstream commit checked: `39c856f2d98` ("SOLR-11475 draft and receipt at the cleaned-history head"). Per-area reports: `reports/update-11475-citations.md` and `reports/update-11475-wording.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents reviewed the draft, the receipt, and the code. No build, Gradle run, or test was run. No draft, receipt, submit branch, live PR, or comment was touched.

## What was checked live

- `solr-11475-submit` is at `229947201fd797e57e2d3db1ff2c6552097c9734`, the head the draft and receipt name. The fork's branch was force-updated from `0de48e492fd4` to this head before this round. That was done on the main side. This job did not push to it.
- I fetched the new head read-only. Its tree is `3223028c6f4e2ff44bf67509423e5815114f97b4`, the same tree as the gated head `0de48e492fd4` and the receipt's tree claim. The seven commits on top of the upstream base are all authored and committed by Nick Shanin. No `Co-Authored-By`, `Claude`, `Signed-off-by`, or "Generated with" line appears in any of them.
- The gate log `g11475-fix-gate.log` is not on disk. The receipt is the only source for the counts and the pre-fix outcome.

## Verified by me

- **The pair-only case throws, and does not loop.** On base `14c7aac0d151`, with `otherVersions = [-42]` and `ourUpdates = [42]`, the else branch at `PeerSync.java` L842 calls `otherVersions.get(otherUpdatesIndex + 1)` on a one-entry list. That throws `IndexOutOfBoundsException`. The loop only repeats when an older entry follows the pair. With `(50, -42, 10)`, the walk reaches the pair with the `10` still ahead, and adds `-42...10` on every pass. Both reviewers reached the same result by reading the code. The internal audit's Finding B reached the opposite view. Nothing was run, so this stays a reading. One run would settle it, and that needs your approval.

## Findings, by area

### Headline and body (FIX)

1. **The headline says "loops forever" for any same-version pair with opposite signs.** That holds only when an older version follows the pair. For the pair as the oldest entry, the base code throws an index error, and the fix removes that error too. The draft says neither. Replacement for the summary (wording from the wording report): "PeerSync loops until memory runs out when both replicas hold the same version with opposite signs, and the other replica has an older version after it."
2. **"This input never ends" and "cannot show a clean failure" overstate the receipt.** The receipt says one pre-fix run "ends in the endless loop and OutOfMemoryError". The test's 30-second check does produce a clean failure path (`t.join(30_000)` at L472, `assertFalse` at L477). So the draft's own body contradicts "cannot show a clean failure". Replacement for the summary: "Five PeerSync test classes pass with this change. On the code before the fix, a run ends in an OutOfMemoryError, not a clean failure." The owner should confirm the OutOfMemoryError outcome against the gate log before it is public.
3. **The "What happens today" link points at fixed code.** The link uses the new head and L805-L844. The lines 826-832 are the fix. The sentence describes base behavior, so the link should use base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, lines L805-L837.
4. **The new branch also removes the index error, and the draft does not say so.** Add it to the body. Replacement text is in the wording report, finding 3.

### Proof (FIX)

5. **Counts are not one line per class, and "0 failures" is not in the receipt.** The receipt gives "1/1" for each of the five classes. Use one line per class, as the formula asks.
6. **"Verified 2026-10-08 at this head" names the wrong commit.** The run was at `0de48e492fd4`. The new head has the same tree, which I verified above. Replacement: "Verified 2026-10-08 at `0de48e492fd4`. The later head `229947201fd` has the same tree."

### Test comment (FIX, owner decision)

7. **The test comment in the PR diff is wrong by reading.** `PeerSyncTest.java` L455-L457 says "With the pair as the only entry, the old code also loops endlessly, adding the same range string on every pass until memory runs out." By the trace above, a pair-only input throws on the old code. Commit `42fb8817b25` had the correct text: "with the pair as the only entry the old code fails on an index error instead of looping." Replacement lines: "    // lowest versions. With the pair as the only entry, the old code fails on an" / "    // index error instead of looping." The draft's comment link, `#L454-L457`, should then point at L454 only, or at the corrected range.
   - **Consequence:** any change to the comment changes the tree. The receipt's counts and tree claim would then cover a different tree. The owner must decide whether to re-gate, or to state that a comment-only change was made after the gate. Nothing has been changed on the branch.

### Choice section (FIX)

8. **The alternative's cost is missing.** The formula asks for the options, the one implemented, and the alternative's cost. The draft gives only our route's cost. Replacement for the paragraph is in the wording report, finding 5. The reviewers agree the section meets the bar for a real choice, because a version goes unrequested under our route. The owner should confirm that a maintainer would plausibly pick the other route.
9. The body repeats the summary ("This branch steps over the pair instead."). Cut the repeat.

### Limits and follow-up (owner decision)

10. **The follow-up wording conflicts between two sources.** Owner ruling O2 in `material/review-opened-28-answers.md` says Limits state that a follow-up "is planned", with no "on request". `pr-formula.md` says "an offer to open a follow-up ticket and PR for it on request". No record on the branch shows a planned follow-up for SOLR-11475. The owner must confirm the plan exists before the draft says it.

### Wording and housekeeping (NOTE)

11. "Walk" and "loop" are used for the same thing. "Complete-list flag" should be `completeList`. "Submission" is workspace wording. Replacements are in the wording report, findings 8 to 10.
12. The changelog title repeats the broad claim (`changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml`, lines 2-3). The suggested title is in the wording report, finding 9. Changing it changes the tree.
13. The draft is 3,773 characters. The guide is about 3,500. The fixes above remove repeated text and add some; recount after the edits.
14. The changelog file name in the draft is a bare path. The formula asks for a link. This is the owner's call.
15. Public commit subjects on the new head include "stage the PeerSync opposite-sign test so the old code loops" and "add hypothetical-reproduction handoff doc". The author is Nick Shanin, with no trailers. Subject wording is the owner's call, and a rewrite would change every link in the draft.

## Owner decisions still open

- Confirm the pair-only behavior, by one run or by your reading, and decide the test comment (finding 7). This is the decision that changes the tree.
- Decide whether to re-gate after any comment edit.
- Confirm the OutOfMemoryError outcome against the gate log (finding 2).
- Confirm the follow-up plan, or drop it from Limits (finding 10).
- Confirm the choice section's alternative would plausibly be picked (finding 8).
- Apply the Proof and summary fixes (findings 1 to 6) to the draft. These change public text, so they need your go-ahead.

## Not done

No draft, receipt, changelog, test, or comment was edited. No submit branch, PR, live PR body, comment, or JIRA item was touched. No build, Gradle run, or test was run. The JIRA text for SOLR-11475 was not read. The gate log is not on disk.
