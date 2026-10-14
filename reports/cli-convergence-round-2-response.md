# CLI convergence, round 2: response (category close-out, W2)

Reviewer: Windows review coordinator (Claude Code), working for Nick Shanin. Date: 2026-10-14, UTC night.
Governing records: `assignments/pool-category-convergence-round-2-settlements.md`, `assignments/pool-category-closeout-overnight-round-1.md` (W2), `claims/pool-category-closeout-w2-cli-round-1.md`, `reports/cli-convergence-round-2.md` (interim), `reports/cli-convergence-round-1-response.md`, and the later record `reports/cli-and-streaming-recheck-round-1.md` (SOLR-10667).

Nothing was built, tested, gated, pushed, committed or posted on this host. Writes: `pr-drafts/cli/SOLR-10667.md` and `pr-drafts/cli/SOLR-18339.md` (edited, re-read), four gate job files under `gates/`, and this report.

## Live fork tips (git ls-remote at this pass)

- solr-10667-submit `9ce9ef1db5e9`: receipt GATE GREEN at this head (2026-10-13).
- solr-9342-submit `833e11192a7f`: receipt VERIFIED at this tip.
- solr-16272-submit `d2cf817391692`: receipt GATE GREEN at this tip.
- solr-16813-submit `1b288170e8aa`: receipt GATE GREEN at this tip.
- solr-17598-submit `8c91cf047a96`: receipt NO GATE APPLIES, Windows run owed.
- solr-18339-submit `47e53884609c`: receipt GATE GREEN at this tip.

All six match the heads in the interim report or its later records.

## Remainders: the five open items in the interim report

Rungs: record (a record at the live tip settles it); get information (a reading or a gate job naming the run owed); do the hard thing (wording applied or a gate job posted); decision (a recorded decision applied under the decision default); defer (a named follow-up in the draft's Limits).

- **CLI-R1-04, SOLR-10667 (blocking): CONVERGED.** Interim said: OPEN, the copy has no exclude. Now the branch at 9ce9ef1db5 carries `exclude "solrclient/**"` (packaging.gradle line 85), the receipt is GATE GREEN at that head, and the draft is on the exclusion route with the OWNER NOTE gone and links at 9ce9ef1db5. Rung: record plus branch read. Artifacts: none new for this item.
- **CLI-R1-05, SOLR-10667 (title): CONVERGED.** Interim said: OPEN. The changelog at 9ce9ef1db5 reads "The full Solr distribution now includes the Learning To Rank example files (modules/ltr/example)." verbatim. Rung: record plus branch read. Artifacts: none.
- **CLI-R1-03, SOLR-9342 (title): HOLD, owed gate.** Interim said: OPEN, the live title still says bin/solr exports TZ. Tip and title are unchanged. The round 1 replacement holds against the code at the tip (bin/solr line 1196 defaults SOLR_TIMEZONE to UTC; lines 1352 and 1356 set the server JVM to TZ="${TZ:-$SOLR_TIMEZONE}"). Rung: do the hard thing. Artifact: `gates/SOLR-9342-title-fix.md`. Why it cannot close here: a title commit needs a push and a changelog parse on a Linux host.
- **CLI-R1-14, SOLR-16272 (title): HOLD, owed gate.** Interim said: OPEN, the title still ends "uninstall no longer refuses". Tip and title are unchanged. Owner decision 2 (adopted) puts the keep rule in the title, and the replacement matches the cleanup and mayBeRegistered logic (RepositoryManager.java lines 258-263 and 299-311 at the tip). Rung: decision 2, then do the hard thing. Artifact: `gates/SOLR-16272-title-fix.md`. Why it cannot close here: a title commit needs the full Linux gate (Error Prone, focused PackageToolTest).
- **CLI-R1-17, SOLR-16813 (title): HOLD, owed gate.** Interim said: OPEN, the live title still begins "A node that joins a cluster". The title at 1b288170e8a is the narrowed title of that commit. It names the join and does not carry decision 3's "when a version loads". Rung: decision 3 (adopted), then do the hard thing. Artifact: `gates/SOLR-16813-title-fix.md`. Why it cannot close here: the same full Linux gate as SOLR-16272 (TestPackages).

## Named holds (closed by the convergence rule)

- **SOLR-18339: HOLD, owner items.** The secured-node Choice (the status wait sends no credentials, so a secured node that answers 401 reads as a failed start), the opening timing (the SOLR-18336 precondition in the receipt's last bullet), and the HTTPS wildcard certificate-name question (owner decision 7). Rung: record (receipt GATE GREEN at 47e53884609c). Artifact: `pr-drafts/cli/SOLR-18339.md`, one Choice line changed from "We left this as it is" to "This change leaves it as it is" (no "we" in PR text).
- **SOLR-17598: HOLD, owed Windows run (NO GATE APPLIES).** Receipt and draft agree: proof by construction, and the confirming run (SOLR_LOGS_DIR with spaces and parentheses, then bin\solr.cmd start) is owed. The draft's Proof and its last Limits bullet keep that visible (checked). Rung: record. Why it cannot close here: the run is a Windows test, which this assignment forbids on this host; the windows-check tag allows it in a later window.

## Later records on SOLR-10667, and new items

- **CLI-R2-01, dotfiles (note, owner decision 10): HOLD, owed gate.** Decision 10 lists dotfiles with no recommendation, so the harder option applies: the example's tracked `.gitignore` ships. The receipt shows it absent and the cause unknown. The gate job runs the premise first and changes the branch only if the cause sits in the copy spec; otherwise it records NO BRANCH CHANGE. Rung: get information, then do the hard thing. Artifact: `gates/SOLR-10667-dotfiles.md`. Draft: a Limits bullet now names the omission.
- **CLI-R2-01, target branches beyond main (note, owner decision 10): HOLD, owner item.** The target of a PR is part of the opening, which the owner approves. Not settled here; no draft change.
- **Recheck item, literal fails-on-base claim (SOLR-10667): CONVERGED.** The draft makes no BATS fails-on-base claim. Its only base statement is that the base distribution has no `modules/ltr/example` (checked 2026-10-11). Rung: record (draft read).
- **Recheck item, choice section (SOLR-10667): SETTLED.** Added "A choice to check": exclude the folder at packaging (implemented) versus have the build write the client outside `example/` (the cost: the build file and README step 40 change). Pointed question at the end. Rung: decision (the recheck left the alternative to the owner; the alternative has a real cost, so the PR formula's choice section applies, and the decision default takes the fuller text). Artifact: `pr-drafts/cli/SOLR-10667.md`.
- **New, local liblinear link (SOLR-10667): HOLD, follow-up.** README step 2 suggests a soft link at `modules/ltr/example/liblinear`, which the exclusion does not cover, and no run has copied the distribution with it present. Limits bullet added. Rung: defer as a named follow-up, with a record run in step 1b of the dotfiles gate job. Artifacts: `pr-drafts/cli/SOLR-10667.md`, `gates/SOLR-10667-dotfiles.md`.

## Recorded dispositions not reopened

- CLI-R1-06 and CLI-R1-07 (SOLR-10667): confirmed in the interim report and not reopened. The CLI-R1-07 follow-up framing for the ref guide page is a recorded round 1 disposition, so the convergence rule keeps it. Note for the owner: decision 10 leaves "whether the ref guide page is updated in the same change" with no recommendation, so the harder option was the in-change update. The owner may take that at opening.
- CLI-R1-01 (SOLR-9342): the round 1 response left the precedence test in decision 1 ("documenting SOLR_TIMEZONE ... with a test") to the owner. It is outside the five remainders and was not re-audited. Flagged for the owner.

## Decisions recorded (decision default)

1. SOLR-10667 dotfiles: ship the example's tracked `.gitignore` (harder option; no recommendation on record). The gate job runs the premise first.
2. SOLR-10667 generated client: the choice section names the alternative to the packaging exclusion (the build writes the client outside `example/`), which the recheck left to the owner and which has a real cost.
3. SOLR-10667 local liblinear link: disclosed in Limits now; copy behavior recorded by a run, not changed here.
4. SOLR-10667 ref guide page: the round 1 disposition stands (not reopened); the in-change update is noted for the owner.
5. SOLR-10667 target branches: left to the owner as an opening decision.
6. SOLR-16272 title: decision 2 governs (the keep-rule title).
7. SOLR-16813 title: decision 3 governs; the narrowed title commit 1b288170e8a does not satisfy it.
8. SOLR-9342 title: the round 1 replacement, which the code at the tip supports (decision 1 gives no recommended title).

## Artifacts

- `pr-drafts/cli/SOLR-10667.md`: edited (Choice section added; Limits bullets for the `.gitignore` and the liblinear link); re-read.
- `pr-drafts/cli/SOLR-18339.md`: edited (one Choice line); re-read.
- `gates/SOLR-9342-title-fix.md`: new, OPEN.
- `gates/SOLR-16272-title-fix.md`: new, OPEN.
- `gates/SOLR-16813-title-fix.md`: new, OPEN.
- `gates/SOLR-10667-dotfiles.md`: new, OPEN (premise, then a fix if the cause is in the copy spec; copy-scope record for the liblinear link).
- `reports/cli-convergence-round-2-response.md`: this report.

The receipts for SOLR-9342, SOLR-16272 and SOLR-16813 still read GATE GREEN or VERIFIED at the pre-title heads. The title gate jobs refresh them on green. Receipts are not written from this host.

## Not done, and why

- No build, test, Gradle, gate run, push, commit, checkout, stash, reset or rebase. No GitHub writes, comments or PR edits.
- The claim `claims/pool-category-closeout-w2-cli-round-1.md` is not marked DONE: this session's writes are limited to the draft folders, `gates/` and this report. The coordinator marks it DONE with these artifacts.
- The worktree is shared with other sessions. During this pass HEAD moved from `5205dda997a` to `f62a86722ee` (a sibling session's commit on a detached HEAD, touching three SolrCloud reports, not this report's files). The tree also holds uncommitted edits from the build-docs, streaming and SolrJ workstreams and their claims; none of them was touched here. This report's edits are uncommitted and need a commit by the coordinator, on a branch that holds the W2 work.

## Owner items this round could not settle

- SOLR-18339: the secured-node Choice (a 401 with no credentials), the opening timing (SOLR-18336), and the HTTPS wildcard certificate-name question.
- SOLR-10667: target branches beyond main (opening decision).
- SOLR-10667: the ref guide page in the same change (round 1 disposition stands; the owner may take the in-change update).
- SOLR-17598: the Windows run is owed (windows-check).
- SOLR-9342: decision 1's precedence test (outside the remainder set; note only).
