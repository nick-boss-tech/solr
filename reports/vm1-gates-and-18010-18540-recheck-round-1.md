# vm1 gate claims, SOLR-18010 premise and SOLR-18540 flip: recheck, round 1

Trigger: pr-prepare tip `14f35c2d871`. vm1 claimed four strong-review gate jobs. vm2 recorded the SOLR-18010 symlink premise at the PR head and at the merge-base. The SOLR-18540 receipt records a ready flip. The canonical four-PR report gained an addendum. Round claim: `claims/pool-pr-prepare-tip-14f35c2-round-1.md`.

Scope: drafts, one canonical-report correction, and this report. No live PR body edited. No fork branch changed. No build, test or Gradle run. Nothing posted. No PR state changed.

Heads, checked by ls-remote in the reviewers' reads at this tip: `solr-18010-submit` `2b5ad801c9e8`; `solr-18541-combined-query-merge-totals` `fcd92f23ba65`; `combinedquerycomponent-merge-totals` `d5ed34125db9` (unmoved); `review-r1-solr-18541` `a93f9f6a997e`; `gradlew-msys-double-conversion` `58e07b4571fb`. All match their records. Upstream main is now `d38e0f16237`, so records that say "main" for `d76fc14` need the label fixed.

## Verdicts

| Item | Verdict | Reason |
|---|---|---|
| SOLR-18010 draft | NOT SATISFIED until G1 and G2 are recorded | G1 expects 0600, but vm2's permission record shows 0644 at the same reverted state. G2 is ready. The draft is about 5,345 visible characters, against the 3,500 guide |
| SOLR-18540 draft | SATISFIED, no edit | The head matches. Proof and Limits match the receipt |
| SOLR-18540 ready flip | Recorded, actor not shown | The receipt cites "Nick's 2026-10-12 direction", which is not quoted anywhere on the branch. GitHub shows the change under the wrapper's account, which does not prove Nick acted personally. Owner item |
| 18541 review-patch gate (vm1) | NOT READY | The patch clears exactness on any skipped shard. The canonical report decides Option B, which leaves an exact zero alone. The test `skippedPartialShardWithoutHitsClearsExactness` pins the clearing of an exact zero, so it tests the wrong width. The seed in the gate is wrong: the side head `fcd92f23ba65` was gated with `18541C0FFEE18541`, not the seed the job names |
| QueryComponent real-exit shape premise (vm1) | READY | A Linux premise run, Q1 to Q3 answerable. The width it informs is an owner decision |
| 18010 G1 (vm1) | NOT READY | The expected value conflicts with the recorded 0644. The 0600 reading is from code, not from a run |
| 18010 G2 (vm1) | READY | `MultiAuthPluginTest`, 6 tests, at the head. No changes |
| 18541 score-only exit input (vm1) | NOT READY until the gate file is amended | It says the job uses the branch's fixture only and that no test is added, but the claim runs an uncommitted probe class. Its "merge-base" label for `d76fc14` is wrong: the PR base is `597ed677` |

## SOLR-18010 premise, as recorded

vm2 ran the symlink case on Linux. At head `2b5ad801c9e`, a symlinked `security.json` became a regular file, and the link's target did not receive the edit. At the merge-base `14c7aac0d151`, the link stayed and the target received the edit. Not run: a single-file bind mount (EBUSY unverified), and a second user owning the file. The logs are on vm2 only, not in the repo. The default-manager attempt and its skip reasons are unlogged.

Applied to `pr-drafts/core-admin/SOLR-18010.md`:
1. The Limits line on file replacement now states the one-off Linux result, says no committed test covers it, and says the bind mount was not tested.
2. The Proof line on the permission failure value ("0644 observed") is replaced by "the permission test expects 0640 to survive an edit", with a statement that no failure value is given until the reverted check is completed. The 0644 value is not stated as a result, because the recorded 0644 and the code-reading 0600 disagree.
3. The merge-base sentence names the full merge-base SHA and says what the links point to.
4. The CoreContainer link is labelled as at the merge-base.

Held for the owner:
- Whether the file replacement stays (O2). The symlink result holds only while replace-by-move stays. The bind-mount result is still open.
- The 0644 reconciliation: vm2's permission record and the code-reading 0600 disagree. Archive vm2's permission log, then decide. The draft does not state a value.
- The draft's length: about 5,345 visible characters. The owner decides what to cut. The choice question in the draft also needs the owner's wording.

## SOLR-18540

Checks: the head `58e07b4571fb` matches. No sentence in the draft is made wrong by the flip, so the draft is unchanged. The Proof and Limits match the receipt's run record.

Owner items:
1. Nick confirms the ready flip in writing. The receipt cites a direction that is not on the branch, and the account that made the change is the wrapper's, not a person's.
2. Check the Actions dashboard for #5116. Only the labeller is visible.
3. Explain the failed `validateChangelog` attempt recorded during packaging.
4. Archive vm2's premise logs if they must be on the branch.

Five corrections to the canonical report were proposed by the 18540 reviewer. I applied one: the symlink half of the open owner item is now marked as confirmed by run. I did not apply the rest, for two reasons. One cites `reports/gradlew-end-to-end-run.md` as the run-record table, and I have not verified that the file exists, since the report names a directory. The others edit the addendum, which vm2 wrote.

## 18541 gate jobs, before any run

- The review-patch gate tests the wrong width. Choose the width first. If Option B stands, the test must pin that an exact zero keeps exactness, and the gate must run at the patched head.
- The score-only input job needs its fixture wording and its "no test is added" line corrected, and its base label fixed, before it runs.
- The QueryComponent real-exit premise is ready to run once the width decision is recorded. It informs that decision.

## Owner items, collected

1. SOLR-18541 width: Option B, or any-skip. This sets the review-patch gate's test and the score-only input's fixture.
2. SOLR-18541 seed: put `18541C0FFEE18541` (the seed the side head was gated with) into the review-patch gate's spec.
3. SOLR-18010 0644 versus 0600: reconcile the record against the finding. Archive vm2's permission log.
4. SOLR-18010 G4b, the bind mount: needs a Linux host with mount rights. No current claim covers it.
5. SOLR-18010 Windows O3, the replace and permission copy on Windows: needs a Windows check. None of the four gates covers it, and it was not run here.
6. SOLR-18010 length, about 5,345 characters: choose what to cut.
7. SOLR-18540: the flip direction, in writing; the Actions check on #5116; the failed `validateChangelog` run.
8. Gate status lines for the four vm1 claims still say OPEN. Each claim's heartbeat is 17:10Z, so they are fresh. They are open until a DONE mark lands.

## Housekeeping

- The local ref `refs/heads/pr-prepare` is stale. The worktree HEAD is the one used for reads and pushes.
- The push wrapper printed a warning that it could not write `C:/Users/shaninna/.gitconfig` (permission denied). The push succeeded. The config was not touched.
