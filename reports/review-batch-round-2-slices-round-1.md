# Review batch from the main side: three assignments taken, round 1

Trigger: pr-prepare tip `dfe8e39d171`, from `f8bee3f`. The main side posted five assignments and a coordinator note. This round took three of them. Round claim: `claims/pool-pr-prepare-tip-dfe8e39-round-1.md`.

Taken:
1. `assignments/pool-decision-prep-15357-15144-round-1.md`. Report: `reports/decision-prep-15357-15144-round-1.md`.
2. `assignments/pool-retire-round-final-reports-round-1.md`. Report: `reports/retire-packs-round-1.md`.
3. `assignments/pool-strong-review-round-2-unblocked-slices.md`. Prepared live body: `reports/live-body-18541-prepared-round-1.md`. Draft edits: two drafts, described below.

Held, not taken this round: `assignments/pool-branch-review-verdicts-landing-round-1.md` (it lands reports on pr-prepare and then pushes patches and edits live bodies) and `assignments/pool-category-convergence-round-2-settlements.md` (pushes and live body edits per category). Both wait for your direction, since this job's rules forbid those pushes and body edits.

Scope of this round: reading and reports. Draft edits only in `pr-drafts/search-components/SOLR-18541.md` and `pr-drafts/core-admin/SOLR-18010.md`. No live PR body edited. No submit or fork branch changed. No PR, comment or gate touched. No build, test or Gradle run.

## 1. Decision preparation (SOLR-15357 and SOLR-15144)

- SOLR-15357: recommend staying narrow. SOLR-15358 waits on owner decision 3. The fact that would change this: a decision to land SOLR-15358 with default docValues-as-stored in the same window, which would require widening the fix.
- SOLR-15144: recommend holding. The fact that would change this: a record or a gated distributed run showing the branch's current change alone returns documents in the distributed test.
- Premise conflicts recorded in the report: the assignment describes a decoration path, but the diff changes only shared copy-target bookkeeping. The round 35 troubleshooting report named in the SOLR-15144 receipt is not in the tree, so the coordinator discard point is unpinned.

## 2. Retire packs

- RETIRE CLEAN: SOLR-17722, SOLR-17882 (its premise is dead; round 12 and 15 goal files are not in the tree, and the owner accepts the call), SOLR-12743, SOLR-17825, SOLR-10198 (merged as PR #4965), SOLR-9759 (superseded by PR #5048).
- RETIRE CLEAN, conditional: SOLR-18119-submit. The owner closes PR #4999 and then deletes the branch. It holds while PR #5061 lands or is accepted.
- SOLR-17372: RETIRE on NOT SUPPORTED. The receipt is dated 2026-10-12. Owner word is needed; option (i) in that receipt is not closed by it.
- SOLR-18277: the receipt text is held, not written. The gate is green at `17c0e644812`, merged as `0cc328310f8`. SplitShardCmdCleanupTest is inconclusive (its helper is absent at base). Four fork runs show the ShardSplitTest failed-split tests passing at base, so there is no fail-before pin. Before the branch is deleted, record the CI SHAs `35c9e995228` and `44c35a3deaf`. The Jira Fixed box is unchecked.

## 3. Strong review slices (SOLR-18541 and SOLR-18010)

Heads checked by ls-remote: `combinedquerycomponent-merge-totals` and `review-r1-solr-18541` are at `a93f9f6a997e`, matching the receipt and the live PR #5117 (draft, no comments or reviews). `solr-18010-submit` is at `2b5ad801c9e8`. `review-r1-solr-18010` is at `00cf8148c3`.

SOLR-18541 draft (edited, not postable yet): the head links move to `a93f9f6a997e`. The receipt supports 10 of 10 at that head (receipt line 7: 10 tests, 0 skipped, 0 failures, 0 errors, seed `C0FFEE42C0FFEE42`). The skip wording now says any skipped shard clears `numFoundExact`. Limits narrow the first-sub-query exit claim to sorts on fields other than score, and to fabricated responses. Still on HOLD: the fail-before for the two new tests at `a93f9f6a997e` is not in the receipt, and gate step 7 is unchecked. The live body differs from the prepared text; applying it needs your go-ahead.

SOLR-18010 draft (edited, under HOLD markers): the Proof adds MultiAuthPluginTest 6 of 6 (from gate G2), and the "keeps them" clause is qualified. The crash sentence now says "before the move". The Choice is rewritten and held. Two of its statements are not yet checked against a record: that the new file takes its group from the process or the folder, and that a file the system creates owner-only keeps that property. The changelog title patch is in the scratchpad (`SOLR-18010-changelog-title.patch`) for a Linux host to apply. The live symlink bullet is wrong (it says neither case was run; the symlink case was run and confirmed), so the live body needs correcting when you approve.

## 4. Owner items

1. SOLR-15357: stay narrow, or decide to land SOLR-15358 with the default docValues change.
2. SOLR-15144: hold, unless a distributed run shows the current change alone returns documents.
3. SOLR-17372: your word on the NOT SUPPORTED retire case.
4. SOLR-18277: approve the receipt text, and record the CI SHAs before the branch is deleted.
5. SOLR-18119-submit: close PR #4999 and delete the branch, once PR #5061 lands or is accepted.
6. SOLR-18541: the live body apply go-ahead, and a fail-before run for the two new tests at `a93f9f6a997e`.
7. SOLR-18010: the Choice verdict, the warning change on the PR branch, the changelog title commit and re-head, and the two unchecked group-inheritance statements.
8. The three cited SOLR-18010 logs (`g18010-gate.log`, `g18010-premise.log`, `g18010-settle.log`) are on no reachable record, so the base-failure claims rest on the receipt's narration.
