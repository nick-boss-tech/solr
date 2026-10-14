# Build and docs convergence, round 2: response

Assignment: `assignments/pool-category-closeout-overnight-round-1.md` (W2), governed by `assignments/pool-category-convergence-round-2-settlements.md`. Claim: `claims/pool-category-closeout-w2-build-docs-round-1.md`. Date: 2026-10-13.

Method: records first. The interim report (`reports/build-docs-convergence-round-2.md`) is treated as predating the receipts. Live fork tips, read by `git ls-remote origin` on 2026-10-13, each match their receipt: solr-5821-submit `327547d693e0` (receipt `receipts/SOLR-5821.md`), solr-6430-submit `78cddfb520d6` (receipt `receipts/SOLR-6430.md`), solr-17752-submit `e5c0a64f993c` (unchanged, receipt `receipts/SOLR-17752.md`), solr-17842-submit `008973f63133` (unchanged, receipt `receipts/SOLR-17842.md`). Upstream main read by `git ls-remote upstream`: `d38e0f162372`. The 17752 and 17842 Proof claims are not re-audited, since both are gated at their live tips. No build, test, Gradle or gate run. No push, commit, checkout, reset, stash or rebase. No GitHub write.

## Remainders

1. **SOLR-5821 (BD-R1-09): CONVERGED.** Interim item 1 read the guide wording as not landed (line 76 still "same searcher"). The current record settles it: the live tip `327547d693e0` carries the replacement, "`numFound` is not affected when the replicas are in sync and hold the same committed documents.", and the wording refresh receipt (2026-10-12) records the docs build clean and the rendered page checked. The branch diff against base is one file, three insertions, so no changelog (decision D1). Rung: get more information (record check at the live tip). Artifact: `pr-drafts/build-docs/SOLR-5821.md` (item 3, both Limits passages, Proof dated 2026-10-12, links re-pointed to `327547d693e0`). Named follow-up, not a hold: the replica document-count half, with the SOLR-4260 link in Limits.

2. **SOLR-6430 (BD-R1-13): CONVERGED.** Interim item 2 read the tip as `c0c6e5dd21c4` with the fragment present. The live tip `78cddfb520d6` deletes `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml` and nothing else (one file, 8 deletions; the field-type page blob is identical at both heads). The docs-shaped gate at `78cddfb520d6` is green (logchangeLint exit 0; buildLocalSite exit 0, 69 tasks). Rung: get more information (record check). Artifact: `pr-drafts/build-docs/SOLR-6430.md` (row link re-pointed to `78cddfb520d6`). The interim's "changelog line (line 36)" is not in the draft at this tip, so there is nothing further to delete.

3. **SOLR-17752 (BD-R1-31): HOLD, owner item plus owed gate.** Item 9 of `reports/build-docs-misc-round-1.md` recommends rebasing onto current main and rerunning the focused test. Adopted under the decision default (D3). The rebase is not performed here: it rewrites a published branch, which the record reserves to the owner. The owed gate is `gates/SOLR-17752-base-rebase-rerun.md` (HELD). Rung: do the hard thing as far as this host can: the decision is taken, the job is packaged, the rewrite is held. Also settled by the record: BD-R1-33 (changelog title). The title in `changelog/unreleased/SOLR-17752.yml` at `e5c0a64f993c` matches the draft, and the receipt records the reword at the gated head. BD-R2-02 (WAITING note): the draft carries no such note, so no replacement was applied; the HOLD line now states the decision. BD-R1-31's register bullet ("add a limits bullet") is not added: with the rebase adopted, public text carries no base caveat. If the owner instead opens against `56ec140e3636`, that bullet and the Proof base label change. Artifacts: `pr-drafts/build-docs/SOLR-17752.md` (HOLD line), `gates/SOLR-17752-base-rebase-rerun.md`.

4. **SOLR-17842 (ladder): HOLD, landing order.** The owner call was discharged 2026-10-13, so it is not a hold. The published-results question is a named follow-up in Limits (rung: defer as a follow-up). The placeholder is a real defect and gets the hard thing (rung: do the hard thing): the guide's example `-rff work/jmh-<build>.json` puts an unquoted `<` in a shell command, which is read as input redirection, so the example cannot run. Patch: `gates/SOLR-17842-placeholder-fix.patch` (checked: it applies to the live-tip blob and reproduces the edited file exactly). Owed: the branch commit pushed by the branch's owner, then the light gate at the new head (`gates/SOLR-17842-placeholder-fix.md`), before the PR opens. Draft: HOLD line; the Proof's classes check is re-dated against upstream main `d38e0f1623` (2026-10-13). The draft carries no WAITING banner at this tip, so none was removed. Artifacts: `pr-drafts/build-docs/SOLR-17842.md`, `gates/SOLR-17842-placeholder-fix.patch`, `gates/SOLR-17842-placeholder-fix.md`.

## Decisions (decision default, recorded here)

- D1 (SOLR-5821, item 11): changelog none, adopted (branch diff is one file). Searcher wording: plain words, adopted (the applied sentence). Document-count half: keep open with the SOLR-4260 link, adopted.
- D2 (SOLR-6430, item 13): drop the changelog, adopted and done at `78cddfb520d6`.
- D3 (SOLR-17752, item 9): rebase onto current upstream main, adopted. The proof base named is upstream main at the rebase (read as `d38e0f1623` on 2026-10-13), not `14c7aac0d151` (interim BD-R2-04). Execution is an owner item.
- D4 (SOLR-17842, item 10): keep the ticket open, adopted. The placeholder fix is a branch commit by the branch's owner: patch delivered, not pushed.
- D5 (SOLR-17842, published results): a named follow-up in Limits, per the assignment, not a hold.

## Owner items and holds

- Owner item: direction for the SOLR-17752 rebase (rewrite of a published branch). Owed after direction: `gates/SOLR-17752-base-rebase-rerun.md`.
- Landing order: the SOLR-17842 placeholder commit, pushed by the branch's owner and gated, before the PR opens.
- Named follow-ups, not holds: the SOLR-5821 document-count half (SOLR-4260); the SOLR-6430 code change for missing dates (an alternative, outside a docs-only change); the SOLR-17842 published results and cross-release comparisons; the SOLR-17752 log lines that print request params (already in Limits).
- Record gap, not added: the BD-R1-36 toolchain pins for SOLR-17842 (JDK 21.0.12.1, Gradle 9.7.0) appear neither in the draft nor in `receipts/SOLR-17842.md`. They are not added to public text, because pr-formula requires receipt grounding. The owner or the light-gate log is the source if the pins are wanted.

## Artifacts

- Edited, re-read: `pr-drafts/build-docs/SOLR-5821.md`, `pr-drafts/build-docs/SOLR-6430.md`, `pr-drafts/build-docs/SOLR-17752.md`, `pr-drafts/build-docs/SOLR-17842.md`.
- New: `gates/SOLR-17842-placeholder-fix.patch` (not pushed), `gates/SOLR-17842-placeholder-fix.md` (OPEN), `gates/SOLR-17752-base-rebase-rerun.md` (HELD, owner item).
- This report: `reports/build-docs-convergence-round-2-response.md`.

## Notes for the coordinator

- The claim `claims/pool-category-closeout-w2-build-docs-round-1.md` is left at IN PROGRESS, since claims are outside this assignment's write set. It should be marked DONE by the coordinator.
- Other sessions have uncommitted files in this worktree (`pr-drafts/cli/SOLR-10667.md`, `pr-drafts/streaming/SOLR-10882.md`, `pr-drafts/streaming/SOLR-12505.md`, `gates/SOLR-12505-changelog-title.md`, `gates/SOLR-14231-fail-before.md`). They were not touched.
- The worktree HEAD moved during this run from `5205dda997a` to `f62a86722ee`, a SOLR-18539 and SolrCloud slate commit made by another session. It does not touch the build-docs paths. This response's edits are uncommitted and were left as they are.
- Round 2 for build-docs: 2 CONVERGED (SOLR-5821, SOLR-6430), 2 named holds (SOLR-17752, SOLR-17842). No remainder is left open.
