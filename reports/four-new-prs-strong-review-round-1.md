# Strong review of the four new draft PRs, round 1

Assignment: `assignments/pool-four-new-prs-strong-review-round-1.md` (pr-prepare at `68a737f1198`). Review run at pr-prepare tip `c83b02c657b`. Staffed as three read-only reviewers, one per PR group (SOLR-18539 with SOLR-18540; SOLR-18541; SOLR-18010), plus the coordinator.

Scope of this round: no build, no test, no Gradle, no gate run on this host. Git was read-only except for this report, the draft edits, the gate files and the claim. No PR was opened, closed, flipped, or commented on. No live PR body was edited this round (see "Live bodies" at the end). No fork branch was changed.

Head checks (ls-remote, each matching the assignment):

| Branch | Head | Result |
|---|---|---|
| mergeids-skipped-shard-totals | 4eb996121804 | matches |
| gradlew-msys-double-conversion | 58e07b4571fb | matches |
| combinedquerycomponent-merge-totals | d5ed34125db9 | matches (the PR as opened) |
| solr-18541-combined-query-merge-totals | fcd92f23ba65 | matches (side branch, one commit on d5ed34125db9) |
| solr-18010-submit | 2b5ad801c9e8 | matches |

Claim note: a second claim for this round (`claims/pool-four-new-prs-strong-review-round-1.md`, pushed at `c83b02c657b`) names the same deliverable. This round's claim (`claims/pool-pr-prepare-tip-39efc73-round-1.md`) landed first, at `46ff7895f10`. Under first-push-wins, the later claimant stands down. Whoever writes this report on the branch should check that path before pushing.

## Summary

| PR | Ticket | Verdict | Blocking items | Applied this round |
|---|---|---|---|---|
| #5115 | SOLR-18539 | NOT SATISFIED | numFoundExact rule (decided, patch pending); E1 wording | E1, E2 in the draft |
| #5116 | SOLR-18540 | SATISFIED | none | E8 in the draft (headline) |
| #5117 | SOLR-18541 | NOT SATISFIED | skipped shard with no per-query data keeps exactness; real-exit IndexOutOfBounds wording; head citations | none (needs the head move and a patch) |
| #5118 | SOLR-18010 | NOT SATISFIED | fail-before value unverified; file replacement undisclosed; MultiAuthPluginTest not run; permission-copy failure logged only at debug | E2, E3, E5, E6, E8, E9, E10, E12, E15 in the draft |

## PR #5115, SOLR-18539 (mergeIds skipped-shard totals)

Verdict: NOT SATISFIED.

Checks:
- Head 4eb996121804 matches the branch and the PR. The live body matches the draft as it was before this round.
- The skip condition in the code matches the draft: `thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling` (QueryComponent.java, lines 1180 to 1184 at the head). The fallback and the all-skipped guard match the draft's line references.
- Both test names in E1 exist at the head: `mergedMaxScoreComesFromMergedShards` and `mergedApproximateTotalHitsExcludesSkippedShard`.
- Merge-base `d76fc14b9857` is the merge-base of this branch (checked by git merge-base).

Findings:
1. (blocking, decided) numFoundExact. The head clears exactness only when no shard merged and a skipped shard reported hits. A skipped shard with an inexact count is ignored. A sorted timed-out response can then report numFoundExact true over a numFound that leaves that shard's matches out. The base reported false. Tests at lines 94 and 188 pin the true result.
   Decision under the decision default (the harder, more thorough option, where no owner call is recorded): Option B. A skipped shard that reported hits or an inexact count clears exactness whether or not other shards merged; a skipped shard that reported an exact zero does not change it. The patch is described and not written: skip branch records `skippedShardMayHoldMatches` when numFound > 0 or numFoundExact is not TRUE, the post-loop guard becomes `if (skippedShardMayHoldMatches) hitCountIsExact = false`, tests T1 and T2 flip to FALSE, and T3 pins an all-skipped inexact zero. This must be gated before the verdict can flip. Nick may override the default; if he chooses Option A, E3 and E4 are the wording.
2. (blocking, applied) E1: the skipped shard's maxScore and approximateTotalHits sentence named no test and read as if only the count came through. Applied to the draft as proposed, with the two test names.
3. (applied) E2: the Proof sentence now says which five of the seven failing values are which.
4. (not applied, depends on finding 1) E3, E4, E5: the choice and the all-skipped sentence wait for the decided option. E5 under Option B reads "If no shard is merged ... marked exact only when every skipped shard reported an exact zero".
5. (not applied, shard-side claim not checked) E6: the Limits bullet on when the skip fires and why a completed shard is never skipped. The skip condition is confirmed in the code, but the shard-side writer of the sort-value lists was not read this round, so the second sentence is not published yet.
6. (optional, not applied) E7: the merge-base sentence. The merge-base is confirmed; "ancestor of upstream/main" was not checked.

Adversarial cases, on code reading only: a skipped shard with zero hits; with hits; with an inexact count; all shards skipped; no shard skipped; a skipped shard with a non-zero maxScore. Results: the head is correct for all except an inexact skipped count (finding 1).

Cross-PR: the QueryComponent rule (this PR) and the CombinedQueryComponent rule (SOLR-18541) disagree when the merged count is non-zero. Under the decided option, both move to the same rule. See SOLR-18541 below.

Draft edits applied: `pr-drafts/search-components/mergeids-skipped-shard-totals.md`, E1 and E2.

## PR #5116, SOLR-18540 (gradlew under Git Bash)

Verdict: SATISFIED.

Checks:
- Head 58e07b4571 matches. The deletion is three lines in the Cygwin and MSYS block. The first APP_HOME conversion at line 159 is needed before the WrapperDownloader call, and CLASSPATH is built from the converted APP_HOME, so the deletion is minimal and correct.
- The traces in `reports/gradlew-end-to-end-run/` match the run-record table in the receipt.
- The body claims only wrapper startup, not a full build. That is correct.

Owner items (none blocks the verdict):
- O2: no CI run is visible on the head (`pr checks` shows only the labeler). Check the Actions dashboard.
- O3: the receipt records a failed Gradle `validateChangelog` attempt during packaging, with no error text. Explain it, or rely on the PR's validate-changelog job once it runs.
- O4: exit codes 0 and 1 appear only in the run record. The traces do not capture `$?`. The Linux log is on vm1, not on the branch.
- O5 (optional): a run from a repo path containing a space would cover that case. The body does not claim it.

Draft edit applied: `pr-drafts/build-docs/SOLR-18540.md`, E8 (headline now states that CLASSPATH is converted again after being built from APP_HOME).

## PR #5117, SOLR-18541 (CombinedQueryComponent merge totals)

Verdict: NOT SATISFIED.

Head answer: the draft and the live body name d5ed34125db9, the PR as opened. The PR should carry fcd92f23ba65 (one commit on top, a clean fast-forward, checked by ancestor and merge-tree, no force). Places that still name d5ed and depend on the head: the skip link (lines 389 to 408 at d5ed), the changelog link, "7 of 7", and "Verified 2026-10-12" in the Proof paragraph; the draft's staging lines 4 and 5.

Findings:
1. (high, blocking) A skipped shard with no per-query data keeps exactness. Real exit on the first sub-query (run record Q1): the skip fires, `docs` is null, the flag stays true, and the merge reports numFound 1,500 while the two shards hold 21,500 matches. The changelog's own rule says this shard reports no exact count. Fix: the skip clause becomes `if (docs == null || docs.getNumFound() > 0 || Boolean.FALSE.equals(docs.getNumFoundExact()))`. Needs a patch and a gate (GJ-1, GJ-2 below).
2. (blocking, wording) The Limits paragraph calls the list access a nearby gap with no size check, and says the real-exit shape is code reading only. Run Q2 (an exit on a later sub-query) threw IndexOutOfBoundsException at lines 339 to 340 on main. The body also says a skipped shard contributes nothing to exactness, which is false at fcd92f23ba65.
3. (placement, decided under the default) The IndexOutOfBounds is its own ticket, named in Limits. It is pre-existing on main, untouched by this PR, a different defect class from the totals fixes, and its correct fix needs an owner decision (treat a missing entry as skipped with an unknown count, or fail) plus a real-exit fixture. A score-only NPE at line 421 of the side branch is the same family and was read, not run.
4. (decided under the default) maxHitsTerminatedEarly: keep the union including skipped shards; no change in this PR. The flag gates approximateTotalHits and is the only signal that the approximate total is an estimate. Run P: the only flagged shard was the skipped one, and a winning-shard rule would make that incomplete response look complete. The body does not pose this as a choice; its only choice is numFound max versus distinct documents.
5. (cross-PR, decided) Align with SOLR-18539 as Option B (any skipped shard with hits makes the merged count inexact). Both patches must land together.
6. (tests) All eight values match the probe values. Wrong or misleading in the tests: the fallback comment "(2 then 7)" should read 2 then 5, sum 7; `skippedPartialShardContributesNothing` now asserts that exactness changes; `skippedPartialShardApproximateTotalCounted` asserts the opposite of its name; the class javadoc omits the skip clause. Coverage gaps: mixed case, skipped exact zero, skipped inexact zero, skipped no-entry.

Not verified: whether a real partial per-query entry ever has numFoundExact FALSE; whether a skipped zero-count FALSE entry occurs; whether the Q1 record's "0 per-query entries" means the key is absent or null (an empty list would have thrown at line 340); the duplicate-adjustment behavior, by reading only.

Draft edits applied: none this round. The edits wait for the head move (owner go-ahead: fork fast-forward of `combinedquerycomponent-merge-totals` from d5ed to fcd92f23ba65, no force) and for the patch.

Gate jobs written: `gates/SOLR-18541-score-only-exit-input.md` (the score-only exit question, on the merge-base, ticket input only). GJ-1 (the patched merge-totals class, with the no-entry, inexact-zero and mixed tests failing without the patch) and GJ-2 (the real-exit premise test at the patched head) are not written yet; they need the patch.

## PR #5118, SOLR-18010 (standalone security.json edits)

Verdict: NOT SATISFIED.

Checks:
- Head 2b5ad801c9e8 matches the branch and the PR. The live body matches the draft as it was before this round. The merge-base 14c7aac0d15 is confirmed.
- Proof counts match the receipt and the source: SecurityConfHandlerTest 4 of 4, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5 (unchanged).
- No em dashes and no "we" in the body.

Lock scope (reading): one SecurityConfHandlerLocal per CoreContainer. The edit lock covers every POST path (v1, v2, plugin POST, placeholder APIs). Not covered: GETs, the startup reload, other CoreContainers, and other processes sharing SOLR_HOME. The body is read inside the lock, so a stalled authorized POST blocks other edits (low). No deadlock found by reading.

Atomic write (reading): a failed write and a process crash leave the old file. A power loss is not covered (no fsync; the base had the same gap). The non-atomic fallback is practically unreachable on Linux. On Windows the JDK opens reads with FILE_SHARE_DELETE, so Solr's own reads do not block the replace; a third-party handle without delete sharing can block it, and a GET during the replace may fail briefly (not run here).

Findings:
1. (high, blocking) B1: the recorded fail-before value "0644 observed" cannot come from the code it names. Since `9e3ccafe39` the replacement is created with `Files.createTempFile` and no attributes. The JDK 21 helper gives 0600 on POSIX, and the body's own Choice says the new file is owner-only, so the body contradicts itself. The gate record dismissed the 0600 prediction without evidence. The line must not ship until gate G1 shows what the test reports.
2. (medium, blocking for the decision) B2: the permission-copy failure is logged at DEBUG only. Decision under the default: the reviewer's recommendation is warn and proceed for an IOException on a POSIX file, and stay silent for UnsupportedOperationException (Windows). Failing the edit would reject edits the base accepts on the same volumes. This needs a code change and gate G3, and it is also owner item O1 below, because the Choice section is about the same behavior.
3. (medium, owner item) B4: replacing the file instead of writing in place changes behavior the body does not disclose. A symbolic link is no longer updated at its target (the edit reports success). A single file bind-mounted into a container should fail every edit with EBUSY (gates G4a and G4b, not run). On Windows, the replacement takes the folder's access control list, not the file's. The disclosure E11 waits for the owner's choice (O2).
4. (medium) B3: the Choice is not posed fairly. The copy keeps mode bits only (not owner or group), so who can read can still change. The alternative's merit (a credentials file that should stay owner-only) is absent, and there is no fail-the-edit option. Rewrite as E4 after B2 is decided.
5. (medium, blocking for the verdict) B6: MultiAuthPluginTest (6 tests, 19 standalone HTTP POSTs) runs the changed write path on every edit and is not in the focused run. Gate G2.
6. (low) B5: the changelog title's "or leave a partly written file" overclaims, since the fallback path is not atomic and there is no fsync. Fix E13 on the fork branch (owner-directed; see below).

Draft edits applied: `pr-drafts/core-admin/SOLR-18010.md`:
- E2: the concurrency Proof paragraph now names the merge-base and says what each test covers.
- E3: the last Proof line now cites "the test reports of that run" instead of "fresh JUnit XML". The fresh-XML claim is not supported by a record on this branch (see O4).
- E5: the first bullet describes the one-at-a-time behavior precisely.
- E6: the plain-move sentence now says the fallback is used only where atomic moves are unsupported.
- E8: the version-marker bullet says the marker goes after an edit, and an existing file keeps it until then.
- E9: the fallback Limits bullet states the crash risk.
- E10: the Windows Limits bullet states the locked-file case. The reviewer's clause about a brief read failure is left out until the Windows check (O3) is run.
- E12: the cloud-mode link now points at the merge-base, with "unchanged by this PR".
- E15 (optional): a bullet on the missing fsync.

Held (not applied), with the reason:
- E1 (the 0644 line): gate G1 first. The line stays as it is in the draft and the live body, and must not ship as written.
- E4 (the Choice section): waits for B2 (O1).
- E7 (the copy sentence with the warning clause): waits for B2.
- E11 (the file-replacement Limits bullet): waits for O2, after G4a and G4b.
- E13 (changelog title): needs a commit on `solr-18010-submit`, which is a fork branch change; not done this round.
- E14 (PR title, optional): a live title change; not done this round.
- E16 (code comment): part of the code change set after O1.

Owner items:
- O1 permission-copy failure: warn and proceed (recommended), or fail the edit.
- O2 file replacement: keep replace-by-move and disclose (E11), resolve symlink targets before the move, or fall back to an in-place write where a move is refused (gives back base behavior and loses atomicity there). Decide after G4a and G4b.
- O3 Windows read and replace: accept and disclose (E10), retry the move briefly, or take the edit lock on the read path. Needs a Windows check that this host cannot run.
- O4 records: the receipt cites `g18010-gate.log`, `g18010-premise.log` and `g18010-settle.log`. None is on this branch or on this host. The permissions re-gate XML sits on vm2. The base-fail claims stand on the receipt's narration until the logs are archived.
- O5 the other contributor's PR (per the overlap report only; not read or acted on in this round): the forced-interleave test cannot be adopted without the atomic write, and the lock test can be adopted alone. The draft stays a draft under every answer the other contributor might give. No answer moves the PR out of draft; that stays with the owner while the offer is outstanding.
- O6 the changelog title (E13) and the optional PR title (E14) need a commit on the fork branch and the changelog validator. Code changes (B2, E16, the optional `createTempFile` wrap) need a patch and gate G3 after O1.
- O7 the verdict flips to SATISFIED only after B1 to B6 close. It does not flip the PR to ready; that stays with the owner.

## Gate jobs written this round (not run)

- `gates/SOLR-18010-permissions-fail-before-and-multiauth.md`: G1 (blocking), G2 (blocking), G3 (after a code change), G4a and G4b (manual Linux).
- `gates/SOLR-18541-score-only-exit-input.md`: the score-only exit on the merge-base, ticket input only.

Not yet written, waiting on decisions: the patch and gates for SOLR-18539 (Option B, decided under the default, patch first), the patch and GJ-1 and GJ-2 for SOLR-18541, and the code change and G3 for SOLR-18010 (after O1).

## Live bodies

The live PR bodies for #5115 and #5118 matched their drafts before this round. The draft edits above are not yet on the live bodies, so the live bodies and drafts differ for the edited sentences. The scheduled-check rules for this round forbid live body edits. Nick's standing patch authority covers them. Sync the live bodies for #5115, #5116 and #5118 in a later round, after the owner decisions, and read each body back after the edit. #5117 is not touched until the head moves.

## Items that are not this round's

- SOLR-16630 (#5106): checked the live body against its receipt. The head is a31707acfdb and the Proof leads with the 9 of 9 run at that head. No pending-gate wording. Matches.
- The SOLR-12505 proof-leg record and the vm2 round 3 work are vm2's and are not reviewed here.
- The vm2 queue monitor says SOLR-14919 is still re-running and that two vm2 coordinator passes ran at once. The main side owns that check, not this round.

## SOLR-18010 premise addendum (vm2, 2026-10-13)

- Symlinked security.json, one set-user-role edit, run at the PR head 2b5ad801c9 and at the merge-base 14c7aac0d1 (seed 18010C0FFEE18010, gates/SOLR-18010-replace-semantics-premise.md, logs in logs/vm2/18010-premise-*). Confirmed by run: at the PR head the edit replaces the symlink with a regular file and leaves the symlink target unchanged; at the merge-base the symlink survives and the target receives the edit.
- Single-file bind mount: not run. The gate account has no mount rights. The EBUSY reading stays unverified.
- Ownership under a second user: not run. The gate account has no second user.

