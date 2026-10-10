# SOLR-11475 wording review (read only)

Result: not ready to post. The test-class claims are true, but the symptom line and the Proof text claim more than the code and the receipt show, the Proof names a head that was not gated, and the choice section leaves out the alternative's cost.

Scope: draft `pr-drafts/update-processing/SOLR-11475.md` on `origin/pr-prepare` (3,773 characters). Receipt `receipts/SOLR-11475.md`. Code read from tree `0de48e492fd4` (the gated head). Its tree SHA is `3223028c6f4e2ff44bf67509423e5815114f97b4`, which matches the receipt. The new head `229947201fd797e57e2d3db1ff2c6552097c9734` is not in this clone, so I read the gated tree. Line numbers below are from that tree. "By reading" means I traced the code by hand. I ran no test and no build.

## Findings

1. FIX. Proof counts and head. Draft L21 and L25.
Evidence: `pr-formula.md` Proof rule: "One line per test class, counts inline, with the verification date and head." Draft L21 puts all five classes in one bullet. Draft L25 says "Verified 2026-10-08 at this head." The receipt (line 4) says the gated head is `0de48e492fd4`, and its counts (line 7) are for that head. The receipt (line 11) says the later head has the same tree. I checked the tree of `0de48e492fd4`. The "0 failures" in draft L21 is not in the receipt.
Replacement for L21-L25:
```
- `PeerSyncTest`: 1 of 1
- `PeerSyncWithLeaderTest`: 1 of 1
- `PeerSyncWithBufferUpdatesTest`: 1 of 1
- `PeerSyncWithIndexFingerprintCachingTest`: 1 of 1
- `PeerSyncWithLeaderAndIndexFingerprintCachingTest`: 1 of 1
- Verified 2026-10-08 at 0de48e492fd4. The later head 229947201fd797e57e2d3db1ff2c6552097c9734 has the same tree.
```

2. FIX. Proof summary L19 and body L24. The draft says the run "never ends" and "cannot show a clean failure". It also uses the word "inconclusive".
Evidence:
- Draft L23 says the check "fails if the walk has not ended after 30 seconds." The code does that: `t.join(30_000)` at `PeerSyncTest.java` L472, then `assertFalse("handleVersionsWithRanges did not terminate", t.isAlive())` at L477. So L24 contradicts L23.
- Receipt line 8: the pre-fix run "ends in the endless loop and OutOfMemoryError ... so it is neither a pass nor a clean assertion failure." That supports "a run before the fix ended in an OutOfMemoryError, not a clean failure." It does not say the 30 second check was reached.
- Test comment L454: "this used to loop forever." That supports "the input loops on the old code."
- By reading, not run: if the OutOfMemoryError ends thread `t`, `result[0]` stays null, and L478 (`result[0].totalRequestedUpdates`) throws a NullPointerException.
Replacement for L19 (summary):
```
**Five PeerSync test classes pass with this change. On the code before the fix, a run ends in an OutOfMemoryError, not a clean failure.**
```
Replacement for L24 (body):
```
The test comment says the old code looped on this input ([comment](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/test/org/apache/solr/update/PeerSyncTest.java#L454)). The 30 second check is there to fail the test when the loop does not end.
```

3. FIX. The symptom is too broad, and a behavior change is not stated. Draft L7, L9, L15.
Evidence:
- Diff `f45f2577c865^` to `0de48e492fd4` for `PeerSync.java` adds 7 lines and deletes none. So the base loop is the current loop without the new branch (L826-L832).
- Base else-branch line, L842: `rangesToRequest.add(rangeStart + "..." + otherVersions.get(otherUpdatesIndex + 1));`. If the pair is the last entry of the other list, `otherUpdatesIndex + 1` is out of range. By reading, the base code then throws, and does not loop.
- The test input `(50, -42, 10)` has 10 after the pair. By reading, the base loop adds `-42...10` on every pass and never moves. This matches L454 and the draft.
- A pair-only list (`-42` against `42`) throws on the base code by reading. Commit `42fb8817b252` had this text in the test comment: "with the pair as the only entry the old code fails on an index error instead of looping."
- The new branch removes that index error too, because it steps over the pair. The draft does not say so.
Replacement for L7 (summary):
```
**PeerSync loops until memory runs out when both replicas hold the same version with opposite signs, and the other replica has an older version after it.**
```
Replacement for L9 (body, full):
```
PeerSync compares its own recent updates with the versions another replica reports. The same version can appear on both sides with opposite signs: an add on one side, a delete on the other. The comparison loop then never moves past that pair. It keeps adding the same range string until memory runs out ([loop](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L805-L844)). If the pair is the oldest version the other replica reports, the old code fails with an index error instead ([range](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L842)).
```
Replacement for L15 (body, full). This also makes the "absolute values" sentence exact. The new branch runs only when the signs differ, because an exact match is caught by the branch above it (L819-L820):
```
When the two absolute values match and the signs differ, the loop moves both positions down one step and requests nothing for that pair ([new branch](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L826-L832)). This also removes the index error for the case where the pair is the oldest version. The other branches of the loop are unchanged ([loop](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L805-L844)).
```

4. FIX. The branch test comment is wrong by reading. The draft cites it at L24. The comment is in the PR diff, so a maintainer will read it.
File: `solr/core/src/test/org/apache/solr/update/PeerSyncTest.java`, L456-L457 (tree `0de48e492fd4`).
Evidence: `git diff 42fb8817b252 0de48e492fd4 -- PeerSyncTest.java` shows the only change is this comment. The text "fails on an index error instead of looping" was replaced by "loops endlessly, adding the same range string on every pass until memory runs out." By reading (see Finding 3), a pair-only list throws on the old code. So the new sentence is wrong. The internal audit (`audits/update-processing/SOLR-11475.md`, Finding B) traced the same case and reached the opposite view. The two traces disagree at the `get(otherUpdatesIndex + 1)` call. One run would settle it. I did not run it.
Replacement for L456-L457 of the comment:
```
    // lowest versions. With the pair as the only entry, the old code fails on an
    // index error instead of looping.
```
Consequence: any comment edit changes the tree. The 2026-10-08 counts and the receipt's tree claim would then cover a different tree. The owner must decide when the proof is run again.

5. FIX. Choice section, draft L29-L33. The section does not state the alternative's cost. It also repeats the summary in the body.
Evidence:
- `pr-formula.md` template: "Options considered; the one implemented; the alternative's cost." Draft L31 gives only the implemented route's cost ("The cost is that this replica does not fetch the other side's version through this walk.").
- Draft L31 opens with "This branch steps over the pair instead." That repeats the summary in L29. The formula says a claim in the summary is not restated in the body.
- Code comment `PeerSync.java` L828-L830: the pair "is not something we can request a range for." The loop builds only ranges (L796, L842).
- `PeerSyncTest.java` L478-L479 asserts `totalRequestedUpdates` is 0 and `versionsAndRanges` is null. So the new check fixes the stepping-over choice in place.
Replacement for L31 (whole line):
```
The other route asks the other side for that one version. The loop builds only ranges, and the code comment says this pair is "not something we can request a range for" ([comment](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L828-L830)). So that route would cost a request that is not a range. Our route leaves this replica without the other side's version of that update through this loop. The new check asserts that nothing is requested for the pair ([assert](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/test/org/apache/solr/update/PeerSyncTest.java#L478-L479)), so that assertion changes too if the other route is chosen.
```
The question on L33 stays as it is.

6. NOTE. Does the choice section meet the bar? Keep it. The formula bar: "a maintainer must plausibly pick the other route, usually because the implemented route carries a cost or a behavior change someone could reject." Our route leaves a version unrequested. That is a behavior someone could reject. The internal audit calls it "Owner call A." It is not a narrow-scope call. Owner to confirm that a maintainer would plausibly pick the other route.

7. NOTE. Limits follow-up wording, draft L39-L40 ("A follow-up submission is planned ..."). Two sources disagree.
- Owner ruling O2 in `material/review-opened-28-answers.md` (2026-10-09): "Limits name the gap and state that a follow-up submission is planned; 'on request' phrasing comes out." The draft follows O2.
- `pr-formula.md` (2026-10-04) still says: "with an offer to open a follow-up ticket and PR for it on request."
- No record on the branch I could find says a follow-up for SOLR-11475 is planned. A search of the branch for "follow-up" and "between nodes" found none for this ticket. The owner must confirm the plan exists before posting.
- "submission" is workspace wording. If the owner allows plain words: "A follow-up PR is planned for a test that runs a peer sync between nodes."

8. NOTE. "complete-list flag", draft L40. The code name is `completeList` (`PeerSyncTest.java` L458: `for (boolean completeList : new boolean[] {false, true})`). Replacement for the phrase: "run with both values of the `completeList` parameter."

9. NOTE. The changelog title in the branch file `changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml` (lines 2-3, tree `0de48e492fd4`) has the same broad claim as Finding 3: "PeerSync no longer loops forever (and runs out of memory) when computing the versions to request if both replicas hold the same version with opposite signs." Suggested: "PeerSync no longer loops or fails with an index error when computing the versions to request if both replicas hold the same version with opposite signs." This is outside the draft. Changing it also changes the tree (see Finding 4).

10. NOTE. "walk" versus "loop", draft L23. The draft uses "loop" in L9 and L15 and "walk" in L23 and L31. Replacement for L23: "It runs the loop on a thread and fails if the loop has not ended after 30 seconds." (L31 is covered by Finding 5.)

11. NOTE. Length, whole draft. The draft is 3,773 characters. The formula's length guide is about 3,500, unless the ticket is unusually complex. This ticket is not. Findings 2 and 5 remove repeated text. Findings 1, 3 and 5 add some text. Recount after the edits.

12. NOTE. Changelog line, draft L42. The formula says file citations are real links at the head SHA. The template shows a bare path, so this is the owner's call. If the owner wants the link: "Changelog: [`changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml`](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml)".

13. NOTE. The OutOfMemoryError outcome (Finding 2) rests on receipt line 8 alone. The gate log `g11475-fix-gate.log` is not on disk. Read the gate log before the public text states the outcome. No wording change until then.

## Task results

Task 1 (test classes). All structural claims are true at the gated tree. The five classes exist (`PeerSyncTest.java`, `PeerSyncWithLeaderTest.java`, `PeerSyncWithBufferUpdatesTest.java`, `PeerSyncWithIndexFingerprintCachingTest.java`, `PeerSyncWithLeaderAndIndexFingerprintCachingTest.java`). `PeerSyncTest.java` has `@Test` at L78 and `test()` at L80. `PeerSyncWithBufferUpdatesTest.java` has `@Test` at L61 and `test()` at L63. `PeerSyncWithIndexFingerprintCachingTest.java` has `@Test` at L55 and `test()` at L57. `PeerSyncWithLeaderTest.java` (L31) extends `PeerSyncTest` and has no `@Test` and no `test()`; it overrides only `testOverlap` and `assertSync`. `PeerSyncWithLeaderAndIndexFingerprintCachingTest.java` (L28-L29) extends `PeerSyncWithIndexFingerprintCachingTest` and overrides only `assertSync`. Neither subclass overrides `test()`. So the claims that `PeerSyncWithLeaderTest` runs `PeerSyncTest.test()`, and that `PeerSyncWithLeaderAndIndexFingerprintCachingTest` runs `PeerSyncWithIndexFingerprintCachingTest.test()`, are true. The total of five test methods is true. The "1 of 1" counts come from the receipt only. The "0 failures" wording is not in the receipt. The new head itself was not checked (not in this clone).

Task 2 (new check and timeout). True: the check is at `PeerSyncTest.java` L453-L481. `test()` (L383) calls `handleVersionsWithRangesTests()` (L441), which calls the check at L450. So the check runs in `PeerSyncTest` and, by inheritance, in `PeerSyncWithLeaderTest`. The walk runs on a daemon thread (L463-L470), joined for 30 seconds (L472), with the failure at L477. The "never ends" claim is supported for the test's input by L454 and by reading. The draft's citation of the comment is partly wrong: L456-L457 is wrong by reading (Finding 4). "So a run cannot show a clean failure" is not supported as a general claim. The 30 second check is a clean failure path, and the draft's own L23 says so. The receipt shows one run that ended in an OutOfMemoryError. Verdict: partly supported. Use Finding 2.

Task 3 (choice section). The section meets the bar (Finding 6). The implemented option is stated. The alternative's cost is missing (Finding 5). The section ends with a pointed question. The body repeats the summary (Finding 5).

Task 4 (formula, plain language, vocabulary). The AI header is the first line (L1). The Jira link is on L3. Each of the five sections opens with a bold one-line summary (L7, L13, L19, L29, L37). The changelog line is at L42. The footer at L44-L46 matches the template. The draft has no em dash and no en dash. It has none of: gate, receipt, ledger, rc=0, audit, Finding C, gate log, history cleanup, trailer, or internal file names. Vocabulary to fix: "inconclusive" (L19, Finding 2), "submission" (L39-L40, Finding 7), "walk" (L23, L31, Finding 10), "complete-list flag" (L40, Finding 8). The length is 3,773 characters (Finding 11). Citation line ranges match the gated tree for L805-L844, L826-L832, L453-L481, and L454-L457. The changelog path is bare (Finding 12).

## Not checked

- The new head `229947201fd797e57e2d3db1ff2c6552097c9734` is not in this clone. `git cat-file` and `git rev-parse` both fail for it. I did not fetch, per the rules. I read the gated tree `0de48e492fd4`, and checked that its tree SHA matches the receipt. I did not check the new head's tree, so the claim "the later head has the same tree" is the receipt's claim, not mine.
- The gate log `g11475-fix-gate.log` is not on disk. The counts and the OutOfMemoryError outcome come from the receipt only.
- No test or build was run. The base-code behavior (index error for a pair at the end of the list, endless loop for the test input) is from reading. The NullPointerException path in Finding 2 is from reading.
- The internal audit's Finding B reached the opposite view on the pair-only case. One run would settle it. I did not run it.
- Whether a maintainer would pick the other route (Finding 6) is a judgment. I did not check it.
- Whether a single-version request would work on the other route. I did not check it.
- Whether a follow-up for SOLR-11475 is planned (Finding 7). No record on the branch shows it.
- The Jira text (`research/jira-context/SOLR-11475.json`) is not on this branch. I did not check the ticket's symptom wording.
- The changelog body and author lines were not reviewed beyond the title.
- The "submission" and "on request" wording depends on owner rulings in `material/review-opened-28-answers.md`. I did not check whether those rulings apply to this ticket.
