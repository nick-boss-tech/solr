# SOLR-11475 citation and proof review

Result: The draft is not ready to post. It needs 7 FIX changes and has 2 NOTE items. The new head `229947201fd797e57e2d3db1ff2c6552097c9734` is not in this repository, so its tree, history, and link targets are unverified here.

Branch paths below are on `origin/pr-prepare`. Draft: `pr-drafts/update-processing/SOLR-11475.md`. Receipt: `receipts/SOLR-11475.md`. Code paths are at `0de48e492fd4`, which is the gated head and the only local proxy for the code.

## Findings

### 1. FIX: draft line 7, the headline overstates the loop

- Draft line 7 reads: "**PeerSync loops forever when both replicas hold the same version with opposite signs.**"
- Evidence: the endless loop needs an entry after the pair in the other replica's list. The test's `10` is that entry. Base `14c7aac0d15` PeerSync.java lines 805-837 (the `while`) show this. With `otherVersions = [-42]` and `ourUpdates = [42]`, the base code reaches line 835, `rangesToRequest.add(rangeStart + "..." + otherVersions.get(otherUpdatesIndex + 1));`. `get(1)` on a one-entry list throws IndexOutOfBoundsException. This is a trace by reading the code. I did not run it.
- Replace line 7 with: `**PeerSync loops forever when both replicas hold the same version with opposite signs and an older version follows that pair.**`

### 2. FIX: draft line 9, the "What happens today" link points at fixed code

- The link is `.../blob/229947201fd797e57e2d3db1ff2c6552097c9734/solr/core/src/java/org/apache/solr/update/PeerSync.java#L805-L844`.
- At `0de48e492fd4`, lines 826-832 are the new branch. `git diff 14c7aac0d15 0de48e492fd4` adds exactly those seven lines. So the link shows the fix inside a sentence that describes base behavior.
- The base loop is at `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` (parent of fix commit `f45f2577c86`). Its `while` runs from line 805 to line 837. Base line 826 is `} else {`.
- The sentence describes base behavior, so this link is one that is "meant to show base code". It must use the base SHA.
- Replace the link target with: `https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/update/PeerSync.java#L805-L837`

### 3. FIX: draft lines 19 and 24, "never ends" does not match the receipt

- Receipt line 8 reads: "the base run ends in the endless loop and OutOfMemoryError of the audit's Finding C, so it is neither a pass nor a clean assertion failure."
- The receipt supports an endless loop that stops only when memory runs out. It is not a pass and not a clean failure. "Never ends" drops the OutOfMemoryError end.
- Replace line 19 with: `**Five PeerSync test classes pass with this change. A run without the fix is inconclusive, because the old code loops until it runs out of memory on this input.**`
- In line 24, replace `On the code before the fix, this input never ends.` with: `On the code before the fix, this input loops until it runs out of memory.`

### 4. FIX: draft line 21, "0 failures" is not in the receipt

- Receipt line 7 gives "1/1" for each of the five classes and "module check rc=0." It does not say "0 failures." The proof numbers must come only from the receipt.
- Replace `Five tests in total, 0 failures.` with: `Five tests in total, one per class.`

### 5. FIX: draft line 24, the comment link cites lines that make a wrong claim

- The draft link is `PeerSyncTest.java#L454-L457`.
- Test line 454 reads: "    // SOLR-11475: we have version 42 and the other has -42; this used to loop forever." This supports the draft sentence.
- Test lines 455-457 read: "    // The matching 50 and 10 versions around the pair matter: the walk starts at the" / "    // lowest versions. With the pair as the only entry, the old code also loops" / "    // endlessly, adding the same range string on every pass until memory runs out." The claim about the pair as the only entry is wrong (see Finding 6).
- Replace `#L454-L457` with `#L454` in the comment link. The link stays on SHA `229947201fd797e57e2d3db1ff2c6552097c9734` if Finding 8 clears.

### 6. FIX (code, owner action): the test comment at PeerSyncTest.java lines 455-457 is wrong

- File: `solr/core/src/test/org/apache/solr/update/PeerSyncTest.java`, at `0de48e492fd4`.
- Commit `0de48e492fd4` ("SOLR-11475: correct the PeerSyncTest comment on the old code's pair-only behavior") changed lines 456-457. Before, in `78e3fea4957`, lines 456-457 read: "    // lowest versions, and with the pair as the only entry the old code fails on an" / "    // index error instead of looping." After, in `0de48e492fd4`, the same lines read: "    // lowest versions. With the pair as the only entry, the old code also loops" / "    // endlessly, adding the same range string on every pass until memory runs out."
- Trace by reading, not run: the earlier wording matches the base code. The later wording does not. The test loops only because of the `10` after the pair. The base code then adds the same string `-42...10` on every pass. With no entry after the pair, base line 835 throws instead.
- Replace lines 455-457 with:
```
    // The 10 after the pair is the range end the old code adds on every pass, so the walk
    // never ends. Without an entry after the pair, the old code can fail on an index error.
```
- The gate ran at `0de48e492fd4`, which has this comment. A comment-only change makes a new tree. The receipt's tree claim (Task 3) would then no longer cover the head. The owner must either re-run the gate or state that a comment-only change was made after the gate.

### 7. FIX: draft line 25, "Verified 2026-10-08 at this head" names the wrong commit

- Receipt line 4 reads: "Gated head: 0de48e492fd4 (the live tip)". Receipt line 5 reads: "Date: 2026-10-08". Receipt line 11 says the new head has the same tree.
- The run was at `0de48e492fd4`, not at `229947201fd`. The date matches.
- Replace line 25 `Verified 2026-10-08 at this head.` with: `Verified 2026-10-08 at commit 0de48e492fd4. This head has the same tree. The history was rewritten to remove Co-Authored-By trailers.`
- Use this only after Finding 8 clears. I could not confirm the new head's tree.

### 8. NOTE: the new head is not in this repository

- `git cat-file -e 229947201fd797e57e2d3db1ff2c6552097c9734^{commit}` returned rc=128. `git rev-parse 229947201fd797e57e2d3db1ff2c6552097c9734^{tree}` failed with "unknown revision or path not in the working tree." The reflog and `git for-each-ref` show no ref with the prefix `2299472`. The shared object store is `C:/Users/shaninna/dev/Solr-issues/source/.git`.
- The local branch `solr-11475-submit` is at `fa258b86220`, which is an ancestor of origin. `origin/solr-11475-submit` is at `0de48e492fd4`.
- So the new head's tree, commit list, trailers, changelog, and line numbers are unverified here. Every link in the draft depends on the fork holding `229947201fd`. Before posting, the owner should confirm on the fork that `229947201fd` exists and that its tree is `3223028c6f4e2ff44bf67509423e5815114f97b4`.

### 9. NOTE: public commit messages conflict with the draft

- Commit `f45f2577c86` has this body: "Hypothetical, unrun regression test; see SOLR-11475-TESTING.md." That file is not in the branch diff. `git diff --stat 14c7aac0d15 0de48e492fd4` lists three files, and none is a TESTING file. The word "unrun" conflicts with the draft's "pass" claim.
- These subjects will show on the PR if the history is pushed as it is: "SOLR-11475: add hypothetical-reproduction handoff doc" (`fa258b86220`), "SOLR-11475: stage the PeerSync opposite-sign test so the old code loops" (`78e3fea4957`), and "SOLR-11475: remove handoff doc" (`42fb8817b25`).
- This is the owner's call. Any rewrite changes the head SHA and every link in the draft.

## Task results

**Task 1 (draft against receipt).** The five class names and the "1 of 1" counts in draft line 21 match receipt line 7 exactly, in the same order. "Five tests in total" follows from that. "0 failures" is not in the receipt (Finding 4). The draft says "Verified 2026-10-08", which matches receipt line 5. "At this head" is wrong, because the run was at `0de48e492fd4` (Finding 7). Neither "module check" nor "rc=0" appears in the draft (grep found none). The receipt has both, so the public text is clean on this point. The draft has no em dash, and neither does the receipt. For the base run, the receipt supports an endless loop that ends in OutOfMemoryError. It does not support a pass or a clean failure. The draft's "never ends" is close but drops the OutOfMemoryError (Finding 3). I also checked the draft claim that `PeerSyncWithLeaderTest` declares no test of its own. That is supported: `PeerSyncWithLeaderTest.java` line 31 is `public class PeerSyncWithLeaderTest extends PeerSyncTest`, and `PeerSyncTest.test()` at line 80 calls `handleVersionsWithRangesTests()` at line 383. The second claim, about `PeerSyncWithLeaderAndIndexFingerprintCachingTest` extending `PeerSyncWithIndexFingerprintCachingTest`, is supported at lines 28-29.

**Task 2 (citations).** The old head `0de48e492fd4` is local (`cat-file -e` rc=0). The new head is not local, so the requested `git diff 0de48e492fd4 229947201fd...` could not run. I did not fetch. The nearest check I could run was `git diff 14c7aac0d15 0de48e492fd4 -- solr/core/src/java/org/apache/solr/update/PeerSync.java`. It has one hunk, `@@ -823,6 +823,13 @@`, with seven lines added at lines 826-832 and none removed. The loop starts at line 805 in both base and fixed code. The loop's closing brace moved from line 837 (base) to line 844 (fixed). Link by link, using `0de48e492fd4` as the proxy for the new head: lines 805-844 show the loop, including the fix. Lines 826-832 show the new branch: the `== Math.abs(...)` test, a three-line comment, and the two decrements, with no range request. That matches the draft sentence. Test lines 453-481 are the full new method. It runs the walk on a thread and checks `t.join(30_000)` (line 472) and `assertFalse(t.isAlive())` (line 477). That matches the draft sentence. The comment link at 454-457 is covered by Findings 5 and 6. The sentence "The other branches of the loop are unchanged" is supported by the diff. No removed lines, and the equal and less-than branches and the else body are unchanged. The "What happens today" link is the wrong SHA for base behavior (Finding 2).

**Task 3 (history rewrite).** `git rev-parse 0de48e492fd4^{tree}` returned `3223028c6f4e2ff44bf67509423e5815114f97b4`. That matches the receipt's tree (receipt line 11). `git rev-parse 229947201fd797e57e2d3db1ff2c6552097c9734^{tree}` failed, so the new head's tree is unverified (Finding 8). I could not list the commits on the new head. The nearest substitute is the old chain from the upstream base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` to `0de48e492fd4`. That base is the merge-base with the local `upstream/main` ref (`8e62c2686882aa704480ab13b6a60ee8f7b5c8af`). The old chain has seven commits, and each has author and committer "Nick Shanin <nick.boss.us@gmail.com>": `f45f2577c86`, `527f98a1e0d`, `fa258b86220`, `706cb11897a`, `78e3fea4957`, `42fb8817b25`, `0de48e492fd`. Trailers in the old chain: "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>" appears in exactly three commits, `f45f2577c86`, `527f98a1e0d`, and `fa258b86220`. That matches receipt line 11. The seven old commits have no "Signed-off-by", no "Generated with", and no other Claude line. Whether the rewrite removed these from the new head is unverified.

**Task 4 (changelog).** The `ls-tree` at the new head could not run. At the old head, `changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml` exists. It has nine lines. `type: fixed` is a valid key in `changelog/logchange-config.yml`. The author is `- name: Nick Shanin`. The file has no placeholder ("ICLA pending" or "Solr Issues Workspace"). The link is `name: SOLR-11475` with `url: https://issues.apache.org/jira/browse/SOLR-11475`. Its shape matches the sibling fragment `SOLR-1092-parallelizebackups.yml`. The draft's line 42 names the same path, so that part is supported. I read the YAML by eye and did not parse it. The indentation is consistent. The `title: >` folded scalar is indented correctly.

## Not checked

- The new head `229947201fd797e57e2d3db1ff2c6552097c9734`. It is not in this repository. Its tree, commit list, trailers, changelog, and line numbers are unverified. I used `0de48e492fd4` as a proxy. That proxy is only as good as the receipt's tree claim, and I could verify that claim only for `0de48e492fd4` itself.
- Whether the fork `nick-boss-tech/solr` holds the new head, the base SHA, or any link target. I made no GitHub calls and no fetches, as the rules say.
- The gate log `g11475-fix-gate.log` and the receipts ledger. Neither is on disk. "INCONCLUSIVE", "1/1", and the tree claim come from the receipt text only.
- No test runs and no builds. Findings 1, 5, and 6 rest on reading base `14c7aac0d15` (PeerSync.java lines 789-837) and the test file. They are not confirmed by a run.
- Whether `upstream/main` is current. The merge-base used the local ref, and I did not fetch.
- Other checkouts or repositories under `dev`. I searched only the shared object store and its refs for the new head's prefix.
- The public prose in "A choice to check", "Limits", and "AI assistance". I checked only the claims the task named.
- The YAML was not parsed, as the task required.
