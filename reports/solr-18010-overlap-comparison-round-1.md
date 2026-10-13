# SOLR-18010: this side's fix against apache/solr PR #5065, round 1

Recommendation: **Combine.** Offer this side's two concurrency tests and its atomic write to PR #5065 as a suggestion, and do not open a competing PR yet. The draft comment is in the last section. It is for Nick to approve. Nothing has been posted.

Read-only. Nothing built, run, gated, or checked out. PR #5065 was read through the GitHub API on 2026-10-13. The fork branch `solr-18010-submit` is at `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`, which matches the gated head in the receipt.

## 1. The two diffs

**Mechanism.** Both fixes use a Java monitor per handler instance, but they lock different things and do different writes.

- This side (`SecurityConfHandlerLocal.java`): a monitor, `editLock`, held for POST only. `persistConf` writes a temp file in the same directory and then moves it into place with an atomic move, with a non-atomic fallback. It also strips the empty metadata key before writing.
- PR #5065: a monitor, `securityJsonLock`, held for every request, GET included. `persistConf` is unchanged. It still opens the file with `Files.newOutputStream`, which truncates it, and then writes.

The two fixes are equivalent for one-process ordering. Only this side changes how the file is written. Neither side retries on the local path, because `persistConf` either returns true or throws. `reloadSecurityProperties` reads without the lock on both sides.

**File overlap.** This side changes four files. PR #5065 changes three. They share `SecurityConfHandlerLocal.java` and `BasicAuthStandaloneTest.java`. Both add the same import, a lock field after `securityJsonPath`, and a `handleRequestBody` override. A textual conflict is likely. I did not test that. Both add a SOLR-18010 changelog entry under different authors.

**Tests.**

- This side: `SecurityConfHandlerTest.testConcurrentEditsToLocalSecurityJson` (line 203) parks the first edit inside `edit()` with a latch. It asserts that both users survive and that the file is one JSON document. `testConcurrentPersistConfLeavesOneWholeDocument` (line 271) forces an interleave inside serialization and calls `persistConf` directly. `testBasicAuth` gains one sequential POST and a check for no empty key on disk.
- PR #5065: one new test, `testConcurrentEdits`. It sends 20 HTTP POSTs released by a `CyclicBarrier`, and asserts that 20 users exist. It is concurrent, but nothing forces the race, and it does not check that the file is one document. It has no unit test.
- The receipt's counts (SecurityConfHandlerTest 3 of 3, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5) are whole-class totals. Only some of those methods are new.

**Edge cases.**

- Forced interleave of two writes: this side covers it with two tests. PR #5065 does not.
- A failed or partial write: this side writes the temp file first, so a failure leaves the old file. PR #5065 truncates first, so a failure mid-write can leave a short or empty file. Neither side tests a failure.
- The v1 and v2 paths: both locks sit in `handleRequestBody`, so both cover both paths. Neither side tests a v2 HTTP edit.
- A missing `security.json`: neither side changes or tests this.
- Unverified: `Files.createTempFile` creates owner-only files on POSIX, which could tighten the permissions of `security.json`. I did not check this on Windows.

## 2. PR #5065 review state (API, 2026-10-13)

- Open, not a draft, not merged. Head `337ccdd2592a`, one commit by the author, dated 2026-10-09. `mergeable` is true, and `mergeable_state` is "unstable".
- Last activity: a comment on 2026-10-09 at 12:09 UTC. Nothing since, four days before this check.
- Reviews: one APPROVED review by a non-member reviewer (role CONTRIBUTOR), on 2026-10-09, with the body "LGTM." No changes requested. No requested reviewers.
- Comments, all 2026-10-09: "I wonder if there are other places in standalone that might have similar issues to this one?" The author has not answered. The reviewer also wrote "tests pass locally", and "will leave open till monday, no hurry to get this merged." Nothing followed.
- CI on the head: "Run Solr Tests using Crave.io resources" failed. Its annotations say only "Process completed with exit code 1", and no test is named. "gradle check", "Check changelog entry", and "labeler" passed. The approval was given while the Crave run was still in progress. I cannot tell from the API whether the Crave check is required.
- The PR body rejects the temp-file rename, and offers a separate issue for crash-safe writes. That is a design position the author has already taken.
- Assessment: stalled, waiting on a maintainer. It has an approval from a non-member, no maintainer review, an unexplained failing check, an unanswered question, and no author activity in four days.

## 3. This side's standing

- The gated head is `c3685bb37d9d`, and the fork tip matches it. The subject is "SOLR-18010: serialize standalone security.json edits and write the file atomically", dated 2026-10-05. Its parent is `fadc5ee31e3`, the marker-only head.
- Gate counts, from the receipt: SecurityConfHandlerTest 3 of 3, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5. These are the receipt's numbers. The gate log is not on disk, so I could not check them.
- Pre-fix proof, from the receipt: both concurrent-edit tests fail on pre-fix production, at `fadc5ee31e3` and at base `14c7aac0d15`. The failures are a lost assignment and a hybrid document that fails strict parsing. `testEdit` passes on both. The three logs the receipt names (`g18010-gate.log`, `g18010-premise.log`, `g18010-settle.log`) are not on disk, so these quotes come from the receipt only.
- Settling run, from the receipt: the real `persistConf` driven from two threads. On base, a forced interleave corrupted 5 of 5 runs. The marker-only head `fadc5ee31e3` corrupted 5 of 5 as well. So the metadata change alone fixed nothing, and the atomic write is what matters.
- Hygiene: the local worktree branch `solr-18010-submit` points at `9b9354f6378`, which is not an ancestor of the fork tip. Anything gated from that local ref would test the wrong head. Use the fork tip.

## Why combine

- The fix matters: the settling run shows the corruption survives the lock alone when the write truncates first. PR #5065 keeps that write.
- PR #5065 is stalled, but not dead. It has an approval and passing unit tests. A competing PR would duplicate it while the maintainer is still deciding.
- Our differentiation is specific: the atomic write, and tests that force the interleave. Those are offerable as commits to the existing PR, and the author can take or leave them.
- The one real risk: the author has already rejected the rename. The offer has to ask, not insist.
- If the author declines both elements, the fallback is to open ours, and the report should be reopened then.

## Draft comment, for Nick to approve. Not posted.

> Thanks for the fix. Two things we tested here, offered as options and not as a change request. First, a forced interleave test at the `persistConf` level: one edit is parked inside the serializer, and both users must survive as one JSON document. The 20-POST test catches the race only by chance. Second, writing the new contents to a temp file and then moving it into place, so a failed write keeps the old file. Your PR still truncates first, so a failure mid-write can leave a short file. We understand you chose not to do the rename here. Would you like either as a follow-up commit, or would you rather keep this change to the lock?

## Not done

- No comment, PR, JIRA write or email.
- No build, test, gate, or branch change.
- The receipt's logs are missing from disk, so the gate counts and the pre-fix quotes are unverified locally.
