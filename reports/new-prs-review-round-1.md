# Review of the four draft PRs toward SATISFIED, round 1

Verdicts: all four are NOT SATISFIED. Nothing was applied. The assignment asks the reviewer to apply wording edits to live PR bodies and code patches to submit branches. The scheduled-job rules for this check forbid both, so every fix below is a proposal for the owner to authorize.

Read-only. Nothing was edited, posted, built, gated, or run. Live PR state came from GET calls, and heads from `ls-remote`.

| PR | Ticket | Head reviewed | Gate receipt at that head | Verdict | Holding findings |
|---|---|---|---|---|---|
| #5115 | SOLR-18539 | `4eb996121804` | Yes (packaging gate green) | NOT SATISFIED | F1: the choice text and the behavior line claim numFoundExact ignores skipped shards, which is false when every shard is skipped |
| #5116 | SOLR-18540 | `58e07b4571fb` | Yes (Linux run at this head) | NOT SATISFIED | G1: the "What this change does" link points at head lines that are not the deleted lines |
| #5117 | SOLR-18541 | `d5ed34125db9` | Yes (gate green) | NOT SATISFIED | C1: a code defect, see below; the fix needs a patch and a new gate |
| #5118 | SOLR-18010 | `c3685bb37d9d` | Yes (gate green, on base `14c7aac`) | NOT SATISFIED | W3 to W5: behavior and scope not disclosed; W5 is a branch change to the changelog title |

A SATISFIED verdict on #5115, #5116 and #5117 would be reported as READY TO FLIP. #5118 gets no flip, whatever the verdict.

## PR #5115 (SOLR-18539)

- Live: open, draft, head matches. Base ref is `597ed677`, and the PR diffs against merge-base `d76fc14b`, as the body says. No comments or reviews. Upstream checks are action_required, so none has run.
- Body matches the draft, apart from a trailing newline.
- Receipt: packaging gate green at `4eb996121804`, 17 tests, seed `F2C2B3B7EFFDB021`. The receipt's time (02:51 UTC) matches the body's 20:51 MDT.
- Code: the skip, the fallback, the folds, and the all-skipped guard match the code at the head. The seven base failures match the assertions. Verified by reading, not by running.
- **F1 (holds the verdict).** The choice section says "a skipped shard contributes to none of the four", and the behavior line says numFoundExact covers only merged shards. Both are false when every shard is skipped, because the guard at lines 1241-1243 clears numFoundExact. Proposed replacement for the choice's bold line and first sentence:
  > **Merged numFound, maxScore and approximateTotalHits now cover only the merged shards.** A skipped shard adds nothing to those three. numFoundExact ignores skipped shards too, except when every shard is skipped and a skipped shard reported hits, which clears it ([L1241-L1243](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1241-L1243)).
  Behavior line, first sentence: "Behavior change: merged numFound, maxScore and approximateTotalHits now cover only the shards whose documents are merged. numFoundExact follows the merged shards too, except when every shard is skipped (see the choice below)."
  Optional last question: "Should numFoundExact also go false when a skipped shard reported hits and other shards were merged, as it does when every shard is skipped?"
- **F2 (non-blocking).** Add `segmentTerminatedEarly` (lines 1146-1154) to the sentence about folds from every shard.
- **F3 (non-blocking).** The body is about 5,100 bytes, over the guide. Trim only if the owner wants the guide held.

## PR #5116 (SOLR-18540)

- Live: open, draft, head matches. Base ref `597ed677`, merge-base `d76fc14b`. No comments or reviews. Two files: the changelog and `gradlew`, with 3 lines deleted. Upstream checks are action_required.
- Receipt: packaged head `58e07b4571` gate-lite green (changelog parse, `bash -n`, a clean status). Linux `gradlew -version` ran at that head, Gradle 9.7.0, exit 0. The Windows run was at `192aacaaf89`, and the gradlew blob is identical at both heads, so only the changelog changed.
- Code: the deleted lines are merge-base lines 216-218. The guard at line 158 covers Cygwin and MSYS. CLASSPATH is set after the conversion. `gradlew.bat` has no cygpath.
- **G1 (holds the verdict).** The "What this change does" paragraph points the deleted lines at head lines 214-216, which are not the deleted lines. Proposed replacement for its first two sentences:
  > The deleted lines are the second APP_HOME conversion, the second CLASSPATH conversion, and the blank line after them, at [lines 216 to 218 of the merge-base](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/gradlew#L216-L218), in the Cygwin and MSYS block. The JAVACMD conversion in the same block stays ([lines 215 to 216 at the PR head](https://github.com/nick-boss-tech/solr/blob/58e07b4571fbbfc83c14bdd28283cd573acb0d7a/gradlew#L215-L216)).
- **G2 to G5 (non-blocking).** Remove "packaged" from the Linux sentence. Add after the bold line: "The repository has no automated test for gradlew, so this proof is a manual run, one per tree." Say "shows that the script still starts the wrapper on Linux" in place of "confirms the script is intact there". Say the unmodified base's gradlew is identical to the merge-base.
- Owner note: the receipt mentions a failed `validateChangelog` attempt with no error text. No Gradle task of that name exists at the merge-base. Not a body defect.

## PR #5117 (SOLR-18541)

- Live: open, draft, head matches. Base `597ed677`. No comments or reviews. Three files.
- Body matches the draft.
- Receipt: gate green at the head. Pre-fix at base: 7 tests run, 7 failures. Head: 7 run, 0 failures. Seed `C0FFEE42C0FFEE42`.
- **Date conflict (owner item).** The receipt says the gate finished 2026-10-13 05:10:53 UTC. The body says "Verified 2026-10-12". The staging note says "gate 2026-10-12". The gate logs are on vm1 and not visible here.
- The skip, the fallback and the tests match the code at the head. The seven base failures match the assertions.
- **C1 (holds the verdict; a code defect).** `hitCountIsExact` starts true, and a skip never clears it. If every shard in a query is skipped, numFound is 0 and numFoundExact stays true, even though a partial header was set. A real early exit sends no per-query entry, so this can happen in production. No test has every shard partial.
  Proposed fix: in the skip branch, before `continue;`, add `hitCountIsExact = false;`. Add a test with two partial shards and no sort values, asserting numFound 0 and numFoundExact false, and add a false assertion to `skippedPartialShardContributesNothing`. Change the changelog title so exactness holds "only when every shard reports an exact count and none is skipped". Replace the body sentence "A skipped shard contributes nothing to that query's numFound, approximateTotalHits or exactness." with "A skipped shard adds nothing to that query's numFound or approximateTotalHits, and it clears the merged exactness flag, because its matches are missing from the count."
  This needs a branch patch, a push, and a gate at the new head. Leaving the flag true needs at least a Limits line, which the reviewer does not recommend.
- **Wording (same round).**
  - "and a shard with no per-query entry can reach the document loop under a score-only sort" becomes "and a shard with no per-query entry that is not skipped reaches the document loop at line 418, where it fails with a NullPointerException (line 411 at the merge-base)."
  - "a real exit of the settled shape sends no per-query entry for the unprepared query" becomes "a real early exit sends no per-query entry for the query it did not reach."
  - Delete "and 0 when it sent no per-query entry". Line 405's zero branch never completes a merge, because line 418 calls `docs.size()` for every shard that is not skipped.
  - Link the Proof's test class to `CombinedQueryComponentMergeTotalsTest.java` at the head.
  - "follows the form QueryComponent uses" becomes "follows the form [QueryComponent uses at the merge-base](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1173-L1175)".
- The body is about 5,400 characters, over the guide (advisory).

## PR #5118 (SOLR-18010)

- Live: open, draft, head matches. The PR base is `597ed677`, but the branch forks at `14c7aac0d151`, which is 74 commits behind `597ed677`. Four files. No comments or reviews.
- Body matches the draft.
- Receipt: gate green at `c3685bb37d9d`, on base `14c7aac`. Counts 3 of 3, 1 of 1, 5 of 5. The gate logs are not on disk.
- The lock covers POST only, and the temp file with atomic move matches the code. The two tests fail on base for the stated reasons, by reading, not by running.
- **W3 (holds the verdict; behavior not disclosed).** The temp file is created owner-only on POSIX systems, so `security.json` takes owner-only permissions after an edit. The earlier permissions are not kept. Proposed Limits bullet: "The temporary file is created owner-only on POSIX systems, so security.json takes owner-only permissions after an edit. The earlier file's permissions are not kept." The alternative is a code change that copies the existing permissions before the move. That is the owner's call.
- **W4 (holds the verdict; behavior not disclosed).** The `""` key is now stripped from the written copy. A GET after an edit therefore no longer returns that key. Append this to the third bullet of "What this change does".
- **W5 (holds the verdict; branch change).** The changelog title says concurrent edits "can no longer corrupt the file or overwrite each other". The lock covers one process only, and the Limits say so. Proposed title: "Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other or leave a partly written file. The file also no longer carries the internal version metadata." This changes the branch, so it needs a YAML parse and a new head.
- **W1 and W2 (wording).** "point to the base commit, 14c7aac0d15" becomes "point to the merge-base commit". The second base-code sentence should say the first test fails because one edit's change is lost, and the second because the file holds part of one document and part of another.
- Low-severity note: a code comment says a crash "leaves the previous document in place". There is no fsync, so that holds for a process stop, not for power loss.

## Owner items

1. **Which rule governs.** The assignment asks for live body edits, submit-branch patches, and gate jobs. The scheduled-job rules forbid live body edits and submit-branch edits. Until you decide, no fix on any of the four is applied.
2. **#5117 C1.** A code patch and a gate at a new head, or a Limits line that discloses the all-skipped case. The reviewer recommends the patch.
3. **#5118 rebase.** The branch is 74 commits behind the PR base. A rebase needs a force-push on the fork and a new gate.
4. **#5118 Windows rename risk.** A write can fail on Windows while another handle has `security.json` open, and reads do not take the lock. No test covers it. Add a test, or a Limits line.
5. **Dates.** #5117's body says "Verified 2026-10-12", and the gate was 2026-10-13 UTC. #5118's receipt says "Opened 2026-10-12", and it was opened 2026-10-13 05:41 UTC.
6. **The offer comment on #5065** (comment `6139135892`, created 2026-10-13 05:45:50 UTC). Its text differs from the overlap draft, and it says "as a draft in #5118". The overlap report still says "Not posted", which is out of date. Whether to edit it is your call, and I have not touched it.
7. **Length.** #5115 is about 5,100 bytes, #5116 about 3,700 bytes, #5117 about 5,400 characters, and #5118 about 5,350 characters. The guide is advisory.

## Not done

- No live PR edit, submit-branch edit, comment, PR open, close, or flip.
- No build, test, gate, or Gradle run.
- Test results at base were traced from the code, not run.
