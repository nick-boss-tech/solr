# SOLR-17914 PR preparation after Christos's reply (round 1)

Assignment: `assignments/christos-reply-17914-pr-prep-round-1.md`. Claim: `claims/christos-reply-17914-pr-prep-round-1.md`. Host: vm4. Nothing opened as a PR, and nothing posted to Jira or GitHub.

## Live head

- `solr-17914-create-collection-dialog-stacked` is at `5ea0055190c7e0857c42a3d4ef354e200c250b32`. That matches the gated head in `receipts/SOLR-17914-stacked-on-17913.md`.
- Its base, `solr-17913-collection-details-card`, is at `0a04d2239e` in the stacked branch. The live 17913 tip is `4003196af1`. The only difference is that `4003196af1` removes `pr-draft/SOLR-17913.md`. Excluding `pr-draft/`, the diff between the two is empty, so the 17913 code is unchanged since the gated base.
- The unstacked branch `solr-17914-create-collection-dialog` is at `794ada8150`. It removes `pr-draft/SOLR-17914.md`. It is not a PR vehicle, as `reports/new-ui-figma-reconciliation-round-1.md` already says.

## Gate record

- The existing receipt (`receipts/SOLR-17914-stacked-on-17913.md`, PASS, 53 tests) cites logs whose compile paths point at `/workspace/worktrees/solr-17660`. That worktree no longer exists, so those logs cannot show which tree was built.
- A clean worktree at `5ea0055190` re-ran the same command. Result: rc 0, BUILD SUCCESSFUL in 2m 2s, 53 tests, 0 failures, 0 errors, 2 skipped. The form tests are 4 of 4 and the card tests are 2 of 2. Logs: `/workspace/gates/logs/vm4/christos-17914-stacked-regate/`. Receipt: `receipts/SOLR-17914-stacked-regate-vm4.md`.

## Stacking dependency and options

The PR cannot target main without the SOLR-17913 change. There are three ways to handle it. The owner picks one.

1. **Open SOLR-17913 first, then this PR against main after it merges.** This matches the merge order in `reports/pool-ui-merge-order-round-1.md`. It is the recommended path.
2. **Open this PR now against main, with the SOLR-17913 files in its diff until 17913 merges.** A reviewer sees two features in one diff, and the PR body must say so.
3. **Restack onto apache main.** `git rev-list --count c8ab71430f..upstream/main -- ui/` is 0, so the `ui/` changes would replay with no conflicts. This needs a new branch and a new gate at the new head. It is not done.

A PR into the fork's `solr-17913-collection-details-card` branch is not useful. Apache main has no such branch, so a reviewer cannot see it there.

## PR-prep blockers found

- **Working files on the branch.** `5ea0055190` still carries `pr-draft/SOLR-17913.md` and `pr-draft/SOLR-17914.md`. Both were removed from the 17913 and unstacked tips on 2026-10-26. Against the 17913 tip, the stacked branch adds both files. They must be removed before any PR opens. I did not change the branch.
- **The form is not reachable.** `CreateCollectionForm` has no caller outside its tests and the preview. There is no create request (`POST /api/collections`) in `ui/`. Christos's comment 18133018 asks for "a temporary way of creating collections". This branch does not give one yet.
- **Ticket criteria not covered.** SOLR-17914 also asks for the create success path, the create-failure error, and the advanced options (hidden by default, with labels). This branch covers none of those yet.

The owner needs to decide the blocker above before a PR is useful to Christos. The options are: (i) open the form as a component PR now and tell Christos plainly that it is not yet a way to create collections; (ii) add the create request and a minimal host now, which is new scope that needs a go and a new claim, and which the Collections screen (SOLR-17912) would otherwise own; or (iii) wait for SOLR-17912.

## PR draft

`drafts/pr-solr-17914-create-collection-dialog.md`. It follows `pr-formula.md`: AI header and footer, what happens today, what this change does, proof, the one choice (where the existing-name check lives), and limits. It states the stacking on SOLR-17913 and names Christos as sponsor and promised reviewer, citing comment 18133018. The draft's test counts are from the re-gate above. The old draft in `pr-draft/SOLR-17914.md` said "five strings" and cited the unstacked head. Both are corrected: the stacked change adds three strings.

## Not done

- No PR opened. No Jira or GitHub post. No change to the fork branches.
