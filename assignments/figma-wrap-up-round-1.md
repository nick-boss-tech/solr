# Assignment: wrap up the Figma node index and the new-UI tickets (round 1)

Status: OPEN. Posted by vm4, 2026-10-24, at Nick Shanin's direction. For the phone agent. Capability tags: records, draft. No Gradle builds are needed: every gate listed below has already run. Nobody edits this assignment. Answers and new files are added, not edited in place.

## Goal

Finish the Figma-linked new-UI work to the point where the owner can open PRs and the Christos questions can be asked. Everything that can be done without the owner is in this assignment. Everything that needs the owner is listed in "Owner decisions" with a default.

## 1. Current state (board tip at posting, `reports/` and `receipts/` on `origin/coordination-ui`)

### Figma node index

- Done. `claims/figma-node-index-round-1-windows-done.md` and `reports/figma-node-index-round-1.md` (commit `5484c58cb1`). Ten of eleven cited nodes read, screenshots under `material/figma/`. Node `87-12957` is recorded as not found; do not substitute a nearby node.
- The Windows host's claim is `claims/figma-node-index-round-1-windows.md`. The assignment status line still reads OPEN. That is correct under WORKFLOW.md; do not edit it.

### UI branches

Canonical branch and head are the ones in the claim and receipt (workflow answer Q8).

| # | Ticket | Canonical branch | Gated head | Live tip | Receipt | Gate | Status |
|---|---|---|---|---|---|---|---|
| 1 | SOLR-17911 file content viewer | `solr-17911-file-content-viewer` | `8047b99ef0` | same | `receipts/SOLR-17911.md` (vm6) | PASS | ready for owner go |
| 2 | SOLR-17887 file tree | `solr-17887-file-tree` | `9c7f72aa0e` | same | `receipts/SOLR-17887.md` (vm6) | PASS | ready for owner go |
| 3 | SOLR-17909 breadcrumb | `solr-17909-breadcrumb-wrap` | `77a38638b0` | `8a2d511368` (adds `pr-draft/SOLR-17909.md` only) | `receipts/SOLR-17909.md` (vm6; the failed run at `17c6860195` is superseded) | PASS | ready for owner go |
| 4 | SOLR-17913 collection details card | `solr-17913-collection-details-card` | `0a04d2239e` | same | `receipts/SOLR-17913.md` (vm6) | PASS | ready for owner go |
| 5 | SOLR-17914 create collection dialog, stacked on 4 | `solr-17914-create-collection-dialog-stacked` | `5ea0055190` | same | `receipts/SOLR-17914-stacked-on-17913.md` (vm4) | PASS, full suite 53 tests | ready for owner go; PR body must say stacked on 17913 |
| 5a | SOLR-17914 unstacked (superseded for PR) | `solr-17914-create-collection-dialog` | `a502747ec4` | `352cd12db8` (adds `pr-draft/SOLR-17914.md`) | `receipts/SOLR-17914.md` (vm6) | PASS | do not open a PR from it |
| 6 | SOLR-17660 basic list component | `solr-17660-basic-list-upstream` | `e1d7bfde5b` | same | `receipts/SOLR-17660-upstream.md` (vm4) | PASS, 52 tests, 0 failures | ready for owner go |
| 6a | SOLR-17660 on fork main (superseded) | `solr-17660-basic-list-main` | `1e8aca0ffc` | `3ab8f52d01` | `receipts/SOLR-17660.md` (vm4) | PASS | do NOT open a PR: its base is the fork's main, and its diff against apache main is 1128 files |
| 7 | SOLR-17850 solr.SearchHandler form | `solr-17850-search-handler-form` | `887778ce5d` | same | `receipts/SOLR-17850.md` (vm6) | PASS | HELD: field set needs Christos |
| 8 | SOLR-17849 Index & Query screen (contains 17850's commit) | `solr-17849-index-query-screen` | `b91f75ed93` | same | `receipts/SOLR-17849.md` (vm6, 18 tests) | PASS | HELD behind 7; `IndexQueryContent.kt` calls `SearchHandlerForm` from 7 |

Superseded or duplicate branches, not to be deleted: `solr-17660-basic-list` (fork, old base `90ff62171a`), `solr-17909-breadcrumb` (Nick, `fdb53f6a02`).

### Integration and base facts

- Integration check: branch `ui-integration-check-vm4` at `ec6601afb6`, report `reports/pool-ui-integration-check-vm4.md`. The seven vm6 branches merged on `c8ab71430f`: only `strings.xml` conflicts; two label names are duplicated with identical text and kept once. Result: 24 suites, 83 tests, 0 failures.
- Apache base: `upstream/main` is `13c693d805` (the fork has an `upstream` remote for `apache/solr`). The vm6 branches are based on `c8ab71430f`. Since `c8ab71430f`, 26 commits landed on apache main, none touching `ui/`. The vm6 branches therefore merge cleanly onto `upstream/main`. Re-check this before each PR: `git log --oneline c8ab71430f..upstream/main -- ui/` must stay empty.
- The fork's `main` (`498a09aea9`) has diverged from apache main: 4395 commits on the fork that apache lacks, 2 on apache that the fork lacks. Do not base any UI PR on the fork's `main`.

### Merge order (decided by vm4, rung 1 and 4 of the ladder: `reports/pool-ui-merge-order-round-1.md`, commit `07b80432ec`)

1. SOLR-17911, SOLR-17887, SOLR-17909 are independent. Any order.
2. SOLR-17913, then SOLR-17914 stacked (use the stacked branch, not the unstacked one).
3. SOLR-17660 upstream is independent of all the above. Any time.
4. SOLR-17850 and then SOLR-17849 wait for the Christos answer.

## 2. Work for the phone agent (no owner decision needed)

A1. **Reconciliation report.** Add `reports/new-ui-figma-reconciliation-round-1.md`. It supersedes the verdicts in `reports/new-ui-index-query-components-round-1.md` and `reports/new-ui-collections-configsets-round-1.md`, which were written before the Figma node read and before vm6's gates. Those two files say "Figma not opened" and carry verdicts like READY TO VOLUNTEER for 17909, 17911 and 17660. Do not edit them. Use the table in section 1. For each ticket give: the Figma node from `reports/figma-node-index-round-1.md`, the branch and head, the receipt, and the next step.

A2. **Correction for the pool-ui assignment (workflow Q1).** Add `assignments/pool-ui-tickets-round-1-receipt-correction-round-1.md`. It says that a claim is DONE only when its receipt exists, citing WORKFLOW.md "Gates" and rule 5. The original assignment is not edited.

A3. **Gate job files (workflow Q3).** The four gate runs vm4 has done have no `gates/` file. Add one per run, marked "retroactive: run before this file existed". Runs and heads:
   - `solr-17660-basic-list-main` at `1e8aca0ffc` (superseded, kept for the record)
   - `ui-integration-check-vm4` at `ec6601afb6` (combined check, one line per ticket)
   - `solr-17914-create-collection-dialog-stacked` at `5ea0055190`
   - `solr-17660-basic-list-upstream` at `e1d7bfde5b`
   Logs are on vm4 under `/workspace/gates/logs/vm4/`. If you cannot verify a path from the board, say so in the file.

A4. **Scoped list and generated index.** Update the scoped list and regenerate `OPEN-WORK.md` with `pool/bin/open-work.py`, so the UI tickets show their real state. Do not hand-edit `OPEN-WORK.md`.

A5. **Playbook.** `UI-PLAYBOOK.md` says "Where the tickets stand" and gives the old verdicts. Do not edit it. Post the proposed text as `drafts/ui-playbook-status-round-1.md`, for whoever maintains the playbook to adopt. Include the Q7 fallback (vm4 Mesa route: micromamba, conda-forge `libegl libgl libglvnd mesalib`, `LD_LIBRARY_PATH` to the prefix's `lib`).

A6. **Verify before any PR.** Before the owner opens any PR, confirm for each canonical branch in the table: its receipt exists on the board, its live tip equals the gated head or differs only by a `pr-draft/` file, and `upstream/main` has no `ui/` commit since `c8ab71430f`. Put the result in the reconciliation report.

A7. **Stale branch names.** Nothing to change. Record the canonical branch in each PR body.

## 3. Owner decisions (rung 5: each is a public or scope choice)

Each question below has a default. Reply in a new file `reports/figma-wrap-up-owner-answers-round-1.md`, or by message to the phone agent.

O1. **Open PRs for items 1 to 6 in the table?** Default: yes for the independent ones (17911, 17887, 17909, 17913, 17660 upstream) and for 17914 stacked after 17913 is merged. Each PR body names its gated head and its receipt path, and its draft comes from the `pr-draft/` file on the branch. Nothing is opened until the owner says so.

O2. **Attribution line in PR bodies.** The Claude Code default asks for a "Generated with Claude Code" footer on PR descriptions. WORKFLOW.md says factory commits carry no attribution trailers, and vm4's AGENTS.md says the same for commits. Default: no attribution footer in pool-authored PR bodies and commits, since the pool rule covers both. The owner should confirm, because this changes what the PR bodies look like.

O3. **Christos before PRs?** UI-PLAYBOOK says the staged ask comes first: evidence, a question, then wait for engagement before asking for review. Default: post the staged question to Christos (O4) first, and open the independent PRs after that. Alternative: open PRs now and ask for review through the PR. Owner decides.

O4. **Post the SOLR-17850 field-set question to Christos?** Draft: `jira/jira-comment-SOLR-17850-field-set-draft.md` (commit `4c99604bf4`). It lists the parameters the form covers now and asks whether the set is complete. Default: post, after the owner reviews the wording. Nothing is posted by a host.

O5. **Split SOLR-17849 so its header lands first?** The header branch `solr-17849-index-query-header` (`fb51fbcb23`) is separate from the screen. The screen depends on 17850's form, which the header does not use. Default: no split now. Revisit if Christos is slow on 17850.

O6. **Batch head counts as the local gate for PR bodies (workflow Q5)?** Default: yes. The PR body names the batch head and the per-ticket focused result.

O7. **Default GL-free test route (workflow Q7).** Pool script `material/ui-test-setup/run-ui-jvm-tests.sh` where it works, with the Mesa route as the fallback. vm4 has not tried the pool script. Default: the pool script is the default; run it once on `solr-17660-basic-list-upstream` to confirm it works. That run is a gate, so it needs a claim first.

O8. **Jira actions on SOLR-17660 and the Figma tickets (workflow Q11).** SOLR-17660 is unassigned and has no comments from vm4. Default: no Jira action until the owner says so.

O9. **Who runs batch A (workflow Q6).** vm6 keeps the seven claims. Default: vm6, as Q6 says. vm4 has already run the combined check, so batch A has no remaining gate work for vm4.

## 4. Do not

- Do not open a PR, post a comment, or file or change a Jira ticket. These need the owner's go (O1, O4, O8).
- Do not base any UI PR on the fork's `main`. Use `upstream/main`.
- Do not edit a claim, receipt, gate job or assignment that already exists. Add new files.
- Do not force push. Do not delete the superseded branches.
- Do not run Gradle. The gates are done. O7 is the only exception, after its claim.
- Do not put the 17849 and 17850 PRs forward before the Christos answer, unless the owner decides otherwise under O3 or O5.

## 5. Open items with no owner yet

- `work-store` question: the Windows host saved the Figma captures under `material/figma/`. The table-of-contents CSS and the screenshot the owner pasted earlier are not saved. Ask the owner whether they belong under `material/figma/` too.
- The Figma file itself: the Windows host signed in with Nick's own browser login. No Figma token exists in `/workspace/.env`, and none is needed for the node index.

## Deliverables

- `reports/new-ui-figma-reconciliation-round-1.md` (A1)
- `assignments/pool-ui-tickets-round-1-receipt-correction-round-1.md` (A2)
- `gates/` retroactive files for the four vm4 runs (A3)
- Regenerated `OPEN-WORK.md` and the scoped list (A4)
- `drafts/ui-playbook-status-round-1.md` (A5)
- The verification result in the reconciliation report (A6)
- `reports/figma-wrap-up-owner-answers-round-1.md` once the owner answers (O1 to O9)

## Limits

- Commit as Nick Shanin, no trailers. No em dashes in authored text.
- Board edits are single-writer where the rule says so, and additions otherwise.
- No Jira or GitHub comment, no PR.
