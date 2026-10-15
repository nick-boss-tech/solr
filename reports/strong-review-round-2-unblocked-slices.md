# Strong review round 2: unblocked slices

Assignment: `assignments/pool-strong-review-round-2-unblocked-slices.md`. Claim: `claims/pool-strong-review-round-2-unblocked-slices.md` (already on the branch, heartbeat 2026-10-15T18:50:00Z; this run did not push it). Host: windows.

Scope of this run: no build, Gradle, or test; no commit or push; no comment, PR state change, or JIRA write. The one public write was the PR #5117 body PATCH in slice A, which the assignment authorizes. No verdict changed.

## Summary

| Slice | Result |
|---|---|
| A. SOLR-18541, PR #5117 | Applied. Live body replaced from the draft and read back: match. Head unchanged at a93f9f6a997e748514cf2575a6e94c5f1c01f85e. No comments or reviews. Length flagged for Nick. |
| B1. SOLR-18010 Choice, PR #5118 | Prepared as draft text, waiting on the verdict. Live body not touched. Conflicts with owner ruling 3, flagged. |
| B2. E13 changelog title | Not written as a new gate job. The corrected title is already committed and gated at 571307400b6917ce458db989f29febb5cff8a600. |
| B3. O4 records list | Written below, item by item. The receipt is not rewritten. |

## Slice A: PR #5117 body at the new head

Pre-checks, in order:
- `git ls-remote origin refs/heads/combinedquerycomponent-merge-totals`: a93f9f6a997e748514cf2575a6e94c5f1c01f85e. This is the head the assignment names.
- `pr view 5117 --json 'comments,reviews,headRefOid,body'` before the edit: 0 comments, 0 reviews, headRefOid a93f9f6a997e748514cf2575a6e94c5f1c01f85e. The live body was still the round 1 text (it named d5ed34125db9, "7 of 7" and "Verified 2026-10-12"), so the edit was needed.

Draft: `pr-drafts/search-components/SOLR-18541.md`. Changes this round:
1. Proof: the bracketed HOLD is removed. The fail-before is now claimed only for the case where a skipped shard reports hits. With production reverted to d5ed34125db9, that test fails with `numFoundExact expected:<false> but was:<true>`, as recorded at the fcd92f23ba65 gate in `receipts/SOLR-18541.md`. The no-count and no-hits tests have no recorded fail-before at a93f9f6a, so the body does not claim one.
2. Staging note 8 records the resolution.

The body already carried the rest of the round 1 list. The exactness rule says any skipped shard clears numFoundExact. The proof is 10 of 10 at the new head. Limits names the Q2 IndexOutOfBoundsException as a follow-up, with no ticket filed. The Choice on multi-query numFoundExact stays as posed (owner decision 1; the reviewer's recommended change is not added). Code links point to a93f9f6a997e. Pre-change symptom links point to the merge-base 597ed67740831566b708b793f52554b2e49e7e17, and the text says so.

Link check at a93f9f6a (read with git grep and git show): the skip block and totals are at lines 389 to 411 (the skip comment at 389, the exactness clear at 394, the numFound and exactness fold at 398 to 402, the approximate total at 405 to 411). The unchecked list access is at lines 338 to 340. The changelog fragment `changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml` exists at the head.

Write path: the body text was taken from the draft by script, not retyped from the JSON. Scripts and copy, in the session scratchpad:
- `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\extract-5117.ps1` (writes the body copy)
- `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\live-body-5117-round2.md` (the body copy that was PATCHed)
- `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\check-5117.ps1` (the comparison)

The PATCH was `gh.ps1 api -X PATCH repos/apache/solr/pulls/5117 -F body=@<the body copy>`. The response showed updated_at 2026-10-15T18:54:36Z.

Read-back, from a separate `pr view 5117 --json 'body,headRefOid,comments,reviews'` after the PATCH, plus the comparison script:
- headRefOid: a93f9f6a997e748514cf2575a6e94c5f1c01f85e (unchanged).
- comments: 0. reviews: 0.
- The stored body equals the body copy character for character (6,817 characters, 7 links).
- The body has no em dashes, no en dashes, and no first-person plural.

Open for Nick:
- Length. The body has 5,746 visible characters with link URLs removed (6,817 with them). The guide in `pr-formula.md` and in AGENTS.md is about 3,500. Round 1 deferred the cut, and this round did not cut, because the Choice and Limits text is owner-decided and the slice did not ask for a cut. A cut needs a decision.
- The fail-before for the two no-count and no-hits tests. Step 7 of `gates/SOLR-18541-review-r1-patch.md` is still unchecked.

## Slice B1: SOLR-18010 Choice (PR #5118), prepared only

Artifact: `pr-drafts/core-admin/SOLR-18010.md`, a block in the staging notes after "The verdict is not decided here." The block is marked WAITING ON THE VERDICT. The live body was not edited, and its Choice section, with its HOLD brackets, is unchanged.

The prepared text covers the points round 1 named:
- The copy keeps the permission bits only, not the owner or the group, so who can read the file can still change after an edit.
- The alternative's merit: a credentials file that should stay owner-only, stated plainly.
- Fail-the-edit as a live alternative.
- Warn and proceed on a failed copy, per O1's recorded recommendation. The gate record does not change it. The text says the head logs at debug level, and the warning is in review patch e99be67c9a6f only.
- The default mode of a new file is not confirmed, because the test with the copy removed reported 0644 where 0640 was expected.

Before it goes live it needs two things: the verdict, and the warning change committed and gated on solr-18010-submit.

Conflict to decide: owner ruling 3 (round 2 assignment, closed 2026-10-13, and the "Owner ruling" section of `receipts/SOLR-18010.md`) says the permission-copy disposition waits for the post-merge rework and that no code or draft change follows from it now. This block is a draft change, prepared because slice B1 asks for it. Nick should decide whether to keep the block as a waiting artifact or delete it.

## Slice B2: E13 changelog title, not written

I did not write `gates/SOLR-18010-strong-review-round-2-title.md`. The change is already on the branch, and a second job would gate the same change again.
- `git log -1 origin/solr-18010-submit` and `ls-remote` both show 571307400b6917ce458db989f29febb5cff8a600.
- The title line of `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml` at that tip reads: "Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other. The file also no longer carries the internal version metadata." This matches the replacement in `gates/SOLR-18010-changelog-title.md` and drops "or leave a partly written file." The text has no ": " inside the scalar, so it parses unquoted. I checked this by reading only.
- `gates/SOLR-18010-changelog-title.md` is DONE: GATE GREEN at 571307400b69, changelog YAML parse rc=0 (the parse check the job names: `dev-tools/scripts/validate-changelog-yaml.py`, or the gate's step 0 parser), recorded in the 2026-10-15 section of `receipts/SOLR-18010.md`. The draft's citations already point to 571307400b69.

## Slice B3: O4, the three logs cited by the receipt

Method:
1. A recursive name search of `C:\Users\shaninna\dev` for `*18010*` returned about 170 files. None is a log for these gates. The files are research notes, worktree copies of the records, gate and claim files, and changelog fragments. The search exited with code 1 at the end. I read that as an access error in the tree, not as a truncated list.
2. `git log --all -- "*g18010*"` on the source checkout returned nothing. No commit has a path with that name.
3. An exact-name search of the pool worktree found the three names only in records that cite them. The same miss is recorded in `reports/solr-18010-overlap-comparison-round-1.md` (line 46) and `reports/answers-and-receipts-round-2-r3.md` (lines 28, 37 and 63).

Item 1. `g18010-gate.log`, cited at `receipts/SOLR-18010.md` line 5 (the gate of c3685bb37d9d, 2026-10-05).
- Where it is: not on any branch and not on this host. The receipt gives no host path. The vm2 claim `claims/pool-vm2-gate-backlog-18010.md` (line 8) names the runner `/workspace/gates/g18010-gate.sh` for the 2026-10-13 gate at 9e3ccafe39. That gate's log is `logs/vm2/queue-g18010-gate-20261013T142306Z.out`. The runner name suggests where the 2026-10-05 log might be on vm2, but that is not verified.
- The commit c3685bb37d9 exists in the source checkout. Its gate results are not on any reachable record.
- Not supported by any reachable record: the 2026-10-05 tidy, Error Prone and module check results; the counts on line 6 (SecurityConfHandlerTest 3 of 3, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5 at c3685bb37d9); and the line 5 statement that the changelog YAML step was verified standalone at the tip on 2026-10-10. No gate file or claim has that date. The branch's changelog checks are dated 2026-10-13 and 2026-10-15.

Item 2. `g18010-premise.log`, cited at line 7 (base failures at 14c7aac0d15 and fadc5ee31e3; testEdit passes on both).
- Where it is: not found anywhere reachable.
- Both commits exist in the source checkout, so a gate can repeat the test on each of them.
- Not supported by any reachable record: the failure texts (a lost assignment, and a hybrid document that fails strict parsing), "testEdit passes on both", and "the premise discriminates". Reading supports part of it. At base, `SecurityConfHandlerLocal.java` lines 86 and 87 open the file, which empties it, and then write, and the edit path takes no lock (`answers-and-receipts-round-2-r3.md` line 37).
- Consequence: the draft's Proof sentence about base failures at 14c7aac0d15 rests on this log. The draft says so in its staging notes.

Item 3. `g18010-settle.log`, cited at line 8 as "found and on disk".
- Where it is: not on disk. The exact-name searches contradict "found and on disk" (`reports/answers-and-receipts-round-2.md` line 67 and `reports/answers-and-receipts-round-2-r3.md` line 28). Its source, the "takeover log 2026-10-04", is also not on disk (line 8, and r3 line 63).
- Not supported: the "found and on disk" wording; the drive (an 85-byte and a 6,091-byte document, from two threads); the counts of 5 of 5 forced and 4 of 50 unforced; and "identically" at fadc5ee31e3. The figures were measured at base 14c7aac0d15 and at fadc5ee31e3. The receipt says so, and they do not measure the live tip, which the line 8 claim "this gate covers" concerns. The statement that the marker strip "never fixed the ticket's corruption" rests on the same missing run.
- Line 9 ("recorded in the main side's takeover log and re-verified 2026-10-10") is also unsupported by any reachable record.

Other receipt citations with the same gap, outside the three-log list: the logs under `logs/vm2/` for the 2026-10-13 perms gate, the G1, G2, G3 and G4 legs, and the 2026-10-15 changelog title gate. None is on this host. The branch facts they support check out at the level of heads and title text (ls-remote and git). Their test counts and exit codes cannot be checked here.

Recommendation, not done here: archive the three logs, or the vm2 equivalents, to a reachable record, or have the receipt say they are not archived, as `gates/SOLR-18010-permissions-fail-before-and-multiauth.md` line 45 already asks. Until then the base-failure claims for #5118 rest on the receipt's narration.

## Waiting slices, restated

- SOLR-18539 (PR #5115). The input this assignment listed, the QueryComponent real-exit settling run, is recorded (`gates/queryComponent-real-exit-shape-premise.md`, 2026-10-13, which supports Option B). The narrowed patch is GREEN at f9b0e5d722, and `ls-remote` shows `mergeids-skipped-shard-totals` at f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff. The branch move is therefore not waiting any more, and the assignment's note that it waits on the settling run is stale. Still open: the #5115 body rewrite against f9b0e5d722, a later slice. Not touched here.
- SOLR-18010 verdict (PR #5118). The inputs are recorded: G3 at e99be67c9a6f (`gates/SOLR-18010-review-r1-g3.md`, GREEN 2026-10-13T19:52Z), and the route-1 re-gate at 00cf8148c3 (`gates/SOLR-18010-review-r1-patch.md`, GREEN 2026-10-13T20:40Z, with the parent fail-before showing one expected failure, per `receipts/SOLR-18010.md`). I did not re-verify those logs. The verdict does not move in this assignment. Owner ruling 3 keeps the PR a draft until PR #5065 merges. The warning change is not on solr-18010-submit yet, and the live #5118 body still cites 2b5ad801c9e8.
- SOLR-18541 score-only exit input. `gates/SOLR-18541-score-only-exit-input.md` is status OPEN and unclaimed, with no result recorded. It still waits on that gate.

## Needs Nick

1. #5117 length: cut the body toward the 3,500 guide, or accept 5,746 visible characters.
2. The B1 block in `pr-drafts/core-admin/SOLR-18010.md` against owner ruling 3: keep it as a waiting artifact, or delete it.
3. Whether the two #5117 fail-before results (gate step 7) should be run before the body claims them.
4. O4: archive the three logs, or accept that the #5118 base-failure claims rest on the receipt narration.
5. The live #5118 body still cites 2b5ad801c9e8. It needs a sync after the verdict.

## Files

- Changed: `pr-drafts/search-components/SOLR-18541.md` (Proof HOLD removed, staging note 8), `pr-drafts/core-admin/SOLR-18010.md` (prepared Choice block in the staging notes), and this report.
- Live write: PR #5117 body only.
- Not touched: the pr-prepare branch (no commit), `receipts/`, `gates/`, and `claims/`.
