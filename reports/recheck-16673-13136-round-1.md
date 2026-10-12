# Receipt refresh recheck, SOLR-16673 and SOLR-13136, round 1

Verdict. SOLR-16673: the draft was STALE and is now CURRENT on head and facts. It names the live tip `2adeae3bf8f1`, the links resolve there, and the Proof matches the refreshed receipt. Three judgement calls are open for the main side, so the draft is not ready to publish. SOLR-13136: QUALIFY. There is no draft. The receipt needs qualifiers, and it hides a behaviour change whose owner decision is still open. Nothing was posted, and no PR or comment was touched.

Inputs: commits `3a126275957` (SOLR-16673 receipt refresh) and `65fa54a7b7a` (SOLR-13136 vm2 receipt). Claim `claims/pool-recheck-16673-13136-round-1.md`. Assignment `assignments/pool-recheck-16673-13136-round-1.md`. Live tips checked with `ls-remote`: `solr-16673-submit` at `2adeae3bf8f14a64147d0dabc078b31ff378b4ab`, `solr-13136-submit` at `486b38775565f7e369d5429c356ca83d750d710a`.

## SOLR-16673

Hold status. The branch was held because the draft named `d7170b12f312`. The fork tip is `2adeae3bf8`, which the receipt also names as gated head and live tip. The hold is lifted for the head. Publication still waits on the decisions below.

What the diff shows. `d7170b12f312..2adeae3bf8` changes one file, `solr/solr-ref-guide/modules/configuration-guide/pages/update-request-processors.adoc`, with two inserted lines. No production code changed, so the code links at `d7170b12f312` resolve at `2adeae3bf8` with the same line numbers.

Edits made to `pr-drafts/update-processing/SOLR-16673.md` (uncommitted until this round's push):

| Location | Before | After | Basis |
|---|---|---|---|
| Proof, bold line | "The only change since the earlier tested head is the changelog title." | "The only changes since the earlier tested head are the changelog title and one ref-guide sentence, and no production code changed." | Diff of the two heads |
| Proof, bullet 1 | "At the earlier head `d5c19e64ba1b` (verified 2026-10-04), the formatting check passes, the Error Prone compile passes, and the module checks pass. ... Before the fix, at `d5c19e64ba1b`, the new test fails; the gate record gives PASS only, and the observed failure is not recorded." | "At head `2adeae3bf8f1` (verified 2026-10-12), the changelog YAML parse passes, tidy passes with no tracked files changed, the Error Prone compile passes, and the module check passes. `ParsingFieldUpdateProcessorsTest` passes 43 of 43, with 0 skipped, 0 failures and 0 errors. Before the fix, the focused tests fail on the unpatched code, so the new test fails without the fix." | Receipt lines for the 2adeae3 gate and its counts; the pre-fix sentence drops the head and the observed-failure claim, which the receipt does not support |
| Proof, bullet 2 | "The one commit past the earlier head, at `d7170b12f312` (verified 2026-10-09), edits the changelog fragment. The fragment parses as YAML." | "The commit at `d7170b12f312` (verified 2026-10-09) edits the changelog fragment, and the fragment parses as YAML. The commit at `2adeae3bf8f1` (verified 2026-10-12) adds one sentence to the ref guide." | Commit count was wrong (two commits past the earlier head) |
| Nine links on lines 17, 25, 31, 34 | `blob/d7170b12f312b364cb280b943a042707e553ea64/` | `blob/2adeae3bf8f14a64147d0dabc078b31ff378b4ab/` | Live tip; every link resolves with `git cat-file -e` |

The merge-base link on line 11 (`14c7aac0d151`) is unchanged. The process-word scan (gate, receipt, claim, vm1, vm2, job) finds nothing in the draft. No dash was introduced.

Open questions for the main side (recorded, not decided here):

1. Whether the ref-guide sentence belongs in outbound text. The sentence is "For these four numeric processors, non-numeric values, including empty strings, are left unmodified." (adoc line 403). The draft's test covers only empty strings, so an outbound claim about non-empty non-numeric values would be untested.
2. Whether to keep a history sentence for the earlier gate at `d5c19e64ba1b` (2026-10-04, 43 tests, 0 failures). Its receipt record is in the History line. It is omitted from the draft.
3. Where the pre-fix run was made. The receipt says "pre-fix proof PASS" with no head, run, or log. The draft now states the failure without a head. If the main side can name the head, the sentence can carry it.
4. Wording. The receipt says "for these four processors"; the adoc says "four numeric processors". The draft does not quote the sentence, so nothing to fix yet, but the outbound quote should use the adoc wording.

Also noticed, not changed: `audits/update-processing/SOLR-16673.md` line 4 says "Head at audit: `d5c19e64ba1b`. ... No movement." That is a dated audit record and is historical. The main side may want a note that the head has moved.

## SOLR-13136

Verdict: QUALIFY. There is no draft for SOLR-13136 in `pr-drafts/`, and no content in `reports/` or `changelog/` that the worktree would publish from this receipt.

Checked and consistent:
- Head: the fork branch is at `486b387755`, matching the receipt, gate file and claim.
- Run window, gate steps, status word and counts (`CreateShardCmdTest` 5 of 5, `CreateShardConstructionTest` 1 of 1) agree across the receipt, the gate file and the claim.

Qualifiers the receipt needs (recorded for the main side, receipt not edited here):

1. "Prior head 2ad44409ffe is absent from the local object store" is not true for this checkout. The commit resolves locally and differs from the tip by 5 files (799 insertions, 248 deletions). The receipt should say which store it means, or drop the statement.
2. "Treat it as the tip's full change set against main" contradicts the same line, which says local main may be stale. The 252-file figure is a vm2 worktree stat and is unverified here. Drop the "full change set" clause.
3. The branch history was rewritten. `2ad44409ffe` is not an ancestor of the tip. The receipt does not say so.
4. The phrase "This branch already has a live PR on apache/solr, so it is a consistency pass only" understates a behaviour change. At `2ad44409ffe`, `CreateShardCmd` deletes the half-created slice when the replica wait or buffered-update apply fails. At the tip, a failure there propagates without deleting the shard, and the shard is left in CONSTRUCTION. The receipt's GREEN shows the tip's tests pass; it does not show the gated behaviour. Whether the live PR's behaviour is acceptable is an owner decision that `reports/solrcloud-round-1-p6.md` records as open.
5. The proof line "the new CreateShardCmdTest fails before the fix and passes after it" names no head, run, or log. Its ledger citation ("in-flight table, 2026-10-02") is not in this worktree. The receipt also says the 2ad head had "a P1 fix pushed by the gate lane", while the gate file forbids any push. Nothing here explains that push.

Public-text issue to surface (not acted on): `reports/solrcloud-round-1-p6.md` line 21 quotes the live PR's Proof as "Gate record for this branch at head `486b3877556` (this round, 2026-10-06)". Commit `486b38775565` is dated 2026-10-07, and the receipt's only run at this head is 2026-10-12. If that sentence is on the live PR, it conflicts with the commit date. Correcting a live PR needs the user's authorization. Nothing was posted.

Open question for the main side: `reports/solrcloud-round-1-p6.md` line 70 lists `486b3877556` as an owed main-side gate, and the vm2 consistency pass may or may not satisfy it. The owner decision on live failure behaviour should come first, then the gate at the chosen head.

## Not done

- No build, test, Gradle run, `gh` call, PR or comment, or edit to any submit branch.
- PR #5017 state was not checked (no GitHub access in this review).
- The gate logs under `gates/logs/vm2/` are not in this worktree, so the receipt's counts were checked against the static test count, not run output.
- The scratchpad tip is updated after this push, since both receipt commits are now reviewed.
