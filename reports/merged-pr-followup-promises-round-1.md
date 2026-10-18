# Merged PR follow-up promises, ledger, round 1

## Pass dated 2026-10-18 (current)

Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. Claim: `claims/pool-merged-pr-followup-promises-round-1.md` (vm4 took over the stale windows claim on 2026-10-18). Read-only: GitHub reads, `ls-remote` on the fork, and JIRA reads. Nothing posted, filed or edited on GitHub or JIRA. This ledger now lives on `coordination`; the 2026-10-13 to 2026-10-15 passes were on `pr-prepare` and are kept below.

Focus for this pass, requested by Nick: the SOLR-4841 language-detection follow-up and SOLR-18549.

Merged set, re-derived with `repo:apache/solr author:nick-boss-tech is:pr is:merged`: 10 PRs, all base `main`, all with `merged_at` set. Two are new since the 2026-10-15 pass: #5027 (SOLR-18391, merged 2026-10-17T00:24Z) and #5062 (SOLR-18523, merged 2026-10-17T00:11Z). No seed PR failed to come back merged.

Counts for this pass:

- Merged PRs: 10.
- Promises found in merged bodies: 3 (#4953, #5001, #5070), unchanged.
- Discharged: 0. Open: 3. Not yet due: 0.
- Named shapes outside the merged set: unchanged, all pending merge or not yet due.

### Focus items

#### SOLR-4841 language-detection follow-up (#5070, merged 2026-10-10)
Promise, verbatim from the merged body: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: OPEN. Checked 2026-10-18:
- The fork has no `solr-4841-langid-followup-submit` branch. It has `solr-4841-submit` (`bda9f9d640`), which is the merged change.
- `assignments/pool-solr-4841-langid-followup-round-1.md` is still OPEN. Its intended host is Windows for the build and review rounds. No gate file exists at `gates/SOLR-4841-langid-followup.md`.
- No new JIRA ticket exists. A search for "language detection custom identifier" and "DetectedLanguage" finds only older tickets. SOLR-4841 itself is still Open.
Next step, in order: (1) Nick approves and files the new JIRA ticket, which is a public write and his call. (2) The Windows review agent builds the test on `solr-4841-langid-followup-submit` off current main and runs review to SATISFIED. (3) A Linux host gates it. vm4 can claim the gate once the branch and gate file exist. (4) Nick approves opening the PR, which discharges the promise.
Whose call: Nick for the ticket and the PR opening. The build is the Windows agent's under the assignment.

#### SOLR-18549 for the SOLR-18317 server-side promise (#5001, merged 2026-10-05)
Promise, verbatim from the merged body: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: OPEN. The path chosen in the 2026-10-15 pass was (a): open a new ticket, gate the server branch under its key, then open the PR linking the ticket. Checked 2026-10-18:
- SOLR-18549 exists in JIRA, status Open, reporter nickshanin, created 2026-10-16. Its summary is "Standalone node throws NPE for nodes or node parameters in proxy-capable admin endpoints". It names SOLR-18317 as the Admin UI half. Its expected behavior covers the v1 logging, v1 system info, v2 node system and v1 metrics endpoints.
- No PR on apache/solr mentions SOLR-18549. No gate file exists for it.
- Two fork heads carry SOLR-18317 work, and the records do not say which one the 18549 PR should use:
  - `solr-18317-server-submit` at `ae918a03fa`, the branch the merged body names. Its tip commits are the testing handoff notes and their removal, plus the proxy and system-info changes.
  - `solr-18317-ready` at `c5d63b1a67`, with the commit "SOLR-18317: Treat standalone nodes=all as local", on top of two commits that change build configuration. This branch was not in the 2026-10-15 records. Its base and content were not analyzed in this pass.
Next step: Nick picks the head and approves opening the PR. The pool gates the chosen head under SOLR-18549 and files the gate job, since the server branch has no gate today. When the PR opens with the ticket link, the promise is discharged.
Whose call: Nick for the head, the ticket and the PR. The gate is the pool's.

### Merged PRs

#### #4939 (SOLR-18135), merged 2026-09-24 (UTC)
No follow-up promise in the description.

#### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30 (UTC)
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
State: OPEN. Candidate discharge #5004 (SOLR-16130) is still open, unchanged. Whether an open PR satisfies "I plan to submit" is Nick's ruling.

#### #4959 (SOLR-18277), merged 2026-10-05 (UTC)
No follow-up promise in the description.

#### #4965 (SOLR-10198), merged 2026-10-07 (UTC)
No follow-up promise in the description.

#### #4995 (SOLR-18196), merged 2026-10-03 (UTC)
No follow-up promise in the description.

#### #5001 (SOLR-18317), merged 2026-10-05 (UTC)
Promise as above. State: OPEN, see the focus section.

#### #5012 (SOLR-13202), merged 2026-10-07 (UTC)
No follow-up promise in the description. The Limits line is a scope exclusion.

#### #5027 (SOLR-18391), merged 2026-10-17 (UTC), new this pass
No follow-up promise. The body says "Follow-up at adcda10b501." and then describes a test added in that commit. That points to work inside the PR, not to future work. The body also says "Happy to open a follow-up ticket and PR for them on request." That is an offer on request, not a promise.

#### #5062 (SOLR-18523), merged 2026-10-17 (UTC), new this pass
No follow-up promise. "Happy to open a follow-up ticket for it." is an offer on request. The body also says the companion PR for SOLR-18119 offers anchor checking as a follow-up change. That is the same SOLR-18119 anchor item as #5061 below, and it stays pending merge.

#### #5070 (SOLR-4841), merged 2026-10-10 (UTC)
Promise as above. State: OPEN, see the focus section.

### Seed promise shapes (open PRs or no merged carrier)

Checked against live bodies and open status on 2026-10-18. None is in the merged set, so none is a ledger entry yet.

- #5100 (SOLR-18532), open. Promise unchanged; `solr-9865-submit` is still at `4937608bb1`. PENDING MERGE.
- #5104 (SOLR-2681), open. Unchanged: the nested-form plan is still unconfirmed. PENDING MERGE, Nick to confirm or reword.
- #5061 (SOLR-18119), open. The anchor follow-up has no named plan. PENDING MERGE.
- #5089 (SOLR-13943), open. The six-test follow-up has no branch and no PR. PENDING MERGE.
- #5088 (SOLR-13696), open. Unchanged, not re-read this pass. PENDING MERGE.
- SOLR-17393 and SOLR-9637: not re-checked this pass. NOT YET DUE; the trigger is the SOLR-17393 PR merging.

### Discharges proposed, none executed

- SOLR-4841 follow-up: Nick files the ticket, the pool builds, reviews and gates, and Nick approves the PR.
- SOLR-18317 promise: Nick picks the head, the pool gates it under SOLR-18549, and Nick approves the PR linking the ticket.
- #4953: Nick rules whether open #5004 satisfies the promise, or it waits for its merge.

### Not verified this pass

- The content of `solr-18317-ready` beyond its commit subjects.
- Whether commit `adcda10b501` (cited by #5027) is reachable from main.
- Owner rulings from earlier passes, carried as recorded.

Everything below this heading is history: the 2026-10-15 pass, the round 2 pass of 2026-10-14 and the 2026-10-13 pass, kept as recorded on `pr-prepare` at `335be1be51`.

## Pass dated 2026-10-15

Pass dated 2026-10-15 (pr-prepare tip at pass start `c5a6b5e7ca0`). Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. This run updates the ledger in place. The file already held the 2026-10-13 pass and the round 2 pass of 2026-10-14; both stay below, unchanged, as history. Where this pass differs from them, this pass is current. The assignment is still named round 1, but this is the third recorded pass, so the round numbering is for the owner to settle.

Merged set, re-derived this pass with `research\gh.ps1 pr list --repo apache/solr --author nick-boss-tech --state merged --limit 200`: total 8, all base `main`, all with `mergedAt` set (UTC). It matches the seed list, and no seed PR failed to come back merged.

Open set, re-derived with `--state open --limit 200`. A first attempt without the limit returned only 30 PRs (the gh default), so it was discarded; the census below uses the full list.

Counts for this pass:

- Merged PRs: 8.
- Promises found in merged bodies: 3 (#4953, #5001, #5070).
- Discharged: 0. Open: 3. Not yet due: 0.
- Named shapes outside the merged set: 6 entries, all pending merge or not yet due (see the seed section). Absent from the set: #5100 and #5104 are open, #5090 is open with no promise, and #5061 is open (pending merge, out of ledger).

### Merged PRs

#### #4939 (SOLR-18135), merged 2026-09-24 (UTC)
No follow-up promise in the description.

#### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30 (UTC)
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
The same body also says "If the tests are not needed, I can remove them." That is an offer about this PR's tests, moot after merge, so it is not counted as a follow-up.
State: OPEN. Candidate discharge: #5004 (SOLR-16130, "Add cross-collection export regression test"), open, author nick-boss-tech, head `solr-16130-test-followup` at `3c48dec4b79f`, which matches the fork branch. Its live body says it "contains no production change" and that the root cause "was fixed and merged in #4953 (SOLR-8291)". The 2026-10-14 pass verified its file list is test-only.
Open question for Nick: whether an open PR satisfies "I plan to submit", or whether the promise waits for #5004 to merge. This ledger keeps it open until he rules.
Next step: no pool action. Nick rules on the wording. Merging #5004 is a maintainer call.

#### #4959 (SOLR-18277), merged 2026-10-05 (UTC)
No follow-up promise in the description. The sentence "The size of the rollback window was not measured." is a limit, not a promise.

#### #4965 (SOLR-10198), merged 2026-10-07 (UTC)
No follow-up promise in the description.

#### #4995 (SOLR-18196), merged 2026-10-03 (UTC)
No follow-up promise in the description. (In Eastern time the merge falls on 2026-10-02; the UTC date is used here.)

#### #5001 (SOLR-18317), merged 2026-10-05 (UTC)
Promise, verbatim: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: OPEN. The fork branch exists at `ae918a03fa7b`, matching the 2026-10-14 pass. No open or merged PR has that head (open census). The discussion thread on #5001 gives the context: Nick's comment of 2026-10-04 says "The server-side proxy changes are preserved on a separate branch for a later PR." Eric Pugh's comment of 2026-10-05 13:38:52Z asks whether Nick would take "a follow up to this PR" to move to the V2 logging APIs and points to SOLR-15823.
Owner ruling, recorded in the 2026-10-14 pass and not re-checkable on GitHub: Nick ruled the promise kept, relying on the SOLR-15823 pair #5030 and #5031 (both open). The 2026-10-14 pass also records SOLR-18317 as Resolved in Jira; Jira was not queried this pass.
Next step: Nick chooses one of two paths. (a) Open a PR from `solr-18317-server-submit`, which needs his go-ahead and a ticket decision, because SOLR-18317 is resolved. (b) Confirm the ruling in writing, with the SOLR-15823 pair as the discharge. The coverage check is in `reports/solr-18317-server-coverage-round-1.md`. Whose call: Nick.
Update, 2026-10-15: the coverage check found the server-side gap live on main and not covered by #5030 or #5031, which contradicts the 2026-10-14 ruling above as a discharge. Path (b) is not recommended. The recommendation is path (a), with a new ticket first. The reasoning and the draft ticket text are in `reports/pool-handoff-decisions-2026-10-15.md`. The promise stays open until Nick confirms the path.

#### #5012 (SOLR-13202), merged 2026-10-07 (UTC)
No follow-up promise in the description. The Limits line, "A missing `v` parameter (the query being joined) is not checked by this change and is not covered by the new tests," is a scope exclusion, not a promise.

#### #5070 (SOLR-4841), merged 2026-10-10 (UTC)
Promise, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: OPEN. No fork branch carries that test: `solr-4841-langid-followup-submit` is not on the fork (`ls-remote`), and `ci/4841-langid-r28` is the CI copy of the merged change (2026-10-14 pass). Nick ruled on 2026-10-14 that the promise is to be executed.
Record this pass: `assignments/pool-solr-4841-langid-followup-round-1.md` exists and directs a build of the test on `solr-4841-langid-followup-submit` off current main, review rounds to SATISFIED, and a gate job at `gates/SOLR-4841-langid-followup.md`. That gate file does not exist yet. The assignment also records Jan Høydahl's direction (reported by Nick, 2026-10-14) that the follow-up get a new Jira ticket, with packaging re-pointed at that ticket. The 2026-10-14 pass did not record this.
Next step: the pool builds the test, files the gate job, and runs review to SATISFIED. Opening the branch needs Nick's approval and the new ticket number. Whose call: the build is the pool's under the assignment; the opening and the ticket are Nick's.

### Summary of merged PRs

| Merged PR | Ticket | Merged (UTC) | Promise state |
|---|---|---|---|
| #4939 | SOLR-18135 | 2026-09-24 | none |
| #4953 | SOLR-8291, SOLR-13217 | 2026-09-30 | open (#5004 open and test-only; discharges on merge or Nick's ruling) |
| #4959 | SOLR-18277 | 2026-10-05 | none |
| #4965 | SOLR-10198 | 2026-10-07 | none |
| #4995 | SOLR-18196 | 2026-10-03 | none |
| #5001 | SOLR-18317 | 2026-10-05 | open (branch at `ae918a03`, no PR; owner ruling kept, recorded 2026-10-14) |
| #5012 | SOLR-13202 | 2026-10-07 | none |
| #5070 | SOLR-4841 | 2026-10-10 | open (assigned for build; gate job not yet filed) |

### Seed promise shapes (open PRs or no merged carrier)

These are checked against live bodies. None is in the merged set, so none is a ledger entry yet. Each enters the ledger when it merges.

#### #5100 (SOLR-18532), open, head `7dfd3d98d0a2`
Promise, verbatim: "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict."
State: PENDING MERGE. `solr-9865-submit` is still at its gated head `4937608bb181`, so the rollback edit has not been dropped.
Next step: after #5100 merges, drop the rollback edit on `solr-9865-submit`, re-gate, and open. Whose call: Nick.

#### #5104 (SOLR-2681), open, head `a3b1ea7994d9`
Promise, verbatim: "A follow-up pull request is planned for the nested forms."
State: PENDING MERGE. No fork branch for the nested forms exists (`ls-remote`). The plan is not confirmed in the records. `reports/highlighting-answers-check-2681.md` (lines 25 to 31) says no record it read shows a plan, and asks Nick to confirm before the sentence stands.
Correction: the 2026-10-13 pass cited `material/highlighting-round-1-answers.md` line 9 as the source of the plan. That file has no "nested" text, so the citation is withdrawn.
Next step: Nick confirms the nested-form plan, or the Limits line is reworded to an offer "on request", as the check suggests. Whose call: Nick.

#### #5061 (SOLR-18119), open, head `660faedd026`
Out of this round's ledger, because #5061 is open. It enters the ledger when it merges.
Named follow-up, verbatim from the body: "A follow-up change can restore the anchor check; it is not in this PR."
Record: the anchor follow-up has no named plan or owner (`reports/build-docs-misc-round-1-g6.md` line 42, re-read this pass).
Next step: before merge, Nick names the plan (which PR, by whom). Whose call: Nick.

#### SOLR-13943 and SOLR-13059 (the assignment's "#5090")
The SOLR-13943 PR is #5089, not #5090. #5089 (open, head `ca443f6f6806`) carries the promise, verbatim: "A follow-up submission for those six tests is planned." The full sentence in the Limits section reads: "The six tests left in `TimeRoutedAliasUpdateProcessorTest` keep the class-level `@AwaitsFix`, so they stay skipped in normal mode. `testPreemptiveCreation` is one of them. A follow-up submission for those six tests is planned."
State: PENDING MERGE. No fork branch and no open PR for the SOLR-13059 six-test follow-up (`ls-remote` and open census).
Next step: Nick schedules the six-test follow-up. Whose call: Nick.

#### #5090 (SOLR-14262), open
Checked as the seed "#5090". The live body has no SOLR-13059 promise and no planned follow-up. Its Limits name scope gaps (SolrCloud, docs, autocommit overlap, "This branch does not address that overlap"), which are limits, not promises.
Record: No follow-up promise in the description. Not merged.

#### #5088 (SOLR-13696), open (new this pass, not a seed)
Promise, verbatim from the Limits section: "A follow-up submission to restore that coverage is planned." The coverage is commitWithin coverage, which the change removes.
State: PENDING MERGE. It enters the ledger when #5088 merges.

#### SOLR-17393 and SOLR-9637 (no merged carrier)
Assignment wording: the merge-only scope on 17393 names the per-shard top-count cut as a follow-up, and 9637 follows as its own PR once 17393 lands, rebased onto it.
Search result: no merged description carries this. The eight merged bodies do not mention either ticket, and a search of all apache/solr PRs for SOLR-17393, SOLR-9637 and SOLR-13059 returns no PR for either ticket.
The text exists only in unmerged drafts. `pr-drafts/suggester/SOLR-17393.md`: "The per-shard fix is a planned follow-up submission." `pr-drafts/suggester/SOLR-9637.md`: "This PR opens after SOLR-17393 lands, rebased onto the landed tip."
Fork branches `solr-17393-submit` (`626241e647e5`) and `solr-9637-submit` (`c50fa4ffd932`) exist. No PR is open for either.
State: NOT YET DUE. The trigger is the SOLR-17393 PR merging, and that PR is not yet opened.
Next step: Nick opens the 17393 PR. After it lands, Nick rebases 9637 onto it and opens that PR. Whose call: Nick.

### Discharges proposed, none executed

- #4953: Nick rules whether an open #5004 satisfies "I plan to submit", or the promise waits for its merge.
- #5001: Nick chooses between opening from `solr-18317-server-submit` and confirming the SOLR-15823 pair as the discharge.
- #5070: the pool builds and gates the test under `assignments/pool-solr-4841-langid-followup-round-1.md`; Nick approves the opening and the new ticket.

### Not verified this pass

- Owner rulings from 2026-10-14 (#5001 kept; #5070 to be executed). The #5070 direction appears in the assignment text. The #5001 ruling is not in any file read this pass; it is carried from the 2026-10-14 pass.
- Jira status (SOLR-18317 Resolved, SOLR-15823 Open, SOLR-13059, SOLR-17393, SOLR-9637). Jira was not queried this pass.
- The SOLR-16130 receipt (GATE GREEN) and the #5004 file list are carried from the 2026-10-14 pass, not re-read.
- The nested-form plan for #5104: no record confirms it.
- The anchor-check plan for #5061: none named.
- Fork branches for the nested forms and for the SOLR-13059 follow-up: none found on the fork.

Everything below this heading is history: the 2026-10-13 pass and the round 2 pass of 2026-10-14, kept as recorded.

## Earlier pass, 2026-10-13

Pass dated 2026-10-13 (pr-prepare tip `1faa41f34d2`). Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. Later rounds update this ledger in place.

Merged set, derived from the GitHub search API (`repo:apache/solr author:nick-boss-tech is:pr is:merged`, total 8, all base `main`, all with `merged_at` set). The seed list matches the search exactly: no seed is missing and no extra merged PR is returned. Closed without merge and out of scope: #4967 (SOLR-18134).

Assignment corrections, for the main side: the assignment's seed-list note says "#5090 (SOLR-13943)". Live titles show #5089 is the SOLR-13943 PR and #5090 is SOLR-14262. Neither is in the merged set, so the SOLR-13943 entry below is pending merge. Also, #5100, #5104 and #5061 are open, so their promises enter the ledger only when they merge.

Reading method: each body read as it stands now on the closed PR, through the API `pulls/<n>` `body` field. Promises quoted verbatim.

## Ledger

### #4939 (SOLR-18135), merged 2026-09-24
No follow-up promise in the description.

### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
State: OPEN. A candidate, #5004 (SOLR-16130), is open and unmerged. It has not been verified as the planned test-only PR.
To discharge: confirm that #5004 is the test-only PR for SOLR-16130. If it is, the promise rests on an open PR and discharges when that PR merges. Whose call: Nick's. Any public PR needs his go-ahead.

### #4959 (SOLR-18277), merged 2026-10-05
No follow-up promise in the description.

### #4965 (SOLR-10198), merged 2026-10-07
No follow-up promise in the description.

### #4995 (SOLR-18196), merged 2026-10-03
No follow-up promise in the description.

### #5001 (SOLR-18317), merged 2026-10-05
Promise, verbatim: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: OPEN. The fork branch exists (head `ae918a03`). No PR has been opened from it.
To discharge: open the PR from `solr-18317-server-submit`. Whose call: Nick's.

### #5012 (SOLR-13202), merged 2026-10-07
No follow-up promise in the description. One scope-gap line about the missing `v` parameter is a near-miss, recorded here and not counted as a promise.

### #5070 (SOLR-4841), merged 2026-10-10
Promise, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: OPEN. No matching fork branch was found. The branch `ci/4841-langid-r28` was not inspected.
To discharge: inspect `ci/4841-langid-r28` to see whether it is the planned test. Whether to submit it is Nick's call.

## Named shapes from the assignment

These promises are named in the assignment, and each sits in an open PR or in no merged PR. They enter the ledger when their PR merges.

### #5100 (SOLR-18532), open, not in the merged set
Promise, named in the assignment: a Limits line naming the follow-up on SOLR-9865, whose branch drops its overlapping rollback hunk when prepared for opening, because #5100 lands first.
State: PENDING MERGE. #5100 is open (head `7dfd3d98`). The fork branch `solr-9865-submit` is still at its gated head `4937608b`, so the rollback hunk has not been dropped. No SOLR-9865 PR exists.
To discharge: after #5100 merges, drop the rollback hunk on `solr-9865-submit`, re-gate, and open the PR. The drop is the packaging decision Nick has already recorded. Opening the PR is Nick's call.

### SOLR-17393 and SOLR-9637
Promise, named in the assignment: the merge-only scope on 17393 names the per-shard top-count cut as a follow-up, and 9637 follows as its own PR once 17393 lands, rebased onto it.
State: NOT YET DUE. No merged PR body carries this promise. No apache/solr PR, of any author, mentions either ticket. The fork branches `solr-17393-submit` (`626241e6`) and `solr-9637-submit` (`c50fa4ff`) exist.
To discharge: Nick opens the 17393 PR. After it lands, rebase 9637 onto it and open that PR. Whose call: Nick's.

### #5104 (SOLR-2681), open, not in the merged set
Promise, named in the assignment: a Limits line on the nested form, with a follow-up pull request planned.
State: PENDING MERGE. #5104 is open. No nested-form branch exists on the fork. The plan is in `material/highlighting-round-1-answers.md` at line 9. `reports/highlighting-answers-check-2681.md` (lines 25 to 31) asks Nick to confirm it.
To discharge: Nick confirms the nested-form plan, or rewords the Limits line to "on request". The branch follows after #5104 merges.

### #5061 (SOLR-18119), open, not in the merged set
Promise, named in the assignment: fragment anchor validation carried as a named follow-up promise, rather than code.
State: PENDING MERGE. #5061 is open (head `660faedd`). The anchor follow-up has no plan or owner (`reports/build-docs-misc-round-1-g6.md`, line 42).
To discharge: before #5061 merges, Nick names the anchor plan in one sentence (which PR, by whom). Whose call: Nick's.

### SOLR-13943 and SOLR-13059 (assignment cites #5090; see the correction above)
Promise, named in the assignment: a Limits promise tied to the SOLR-13059 follow-up, the test-only fix for `TimeRoutedAliasUpdateProcessorTest`.
State: PENDING MERGE. The SOLR-13943 PR is #5089, which is not merged. The SOLR-13059 six-test follow-up is OPEN: no branch and no PR.
To discharge: Nick schedules the six-test follow-up. A fail-before run is needed first. Whose call: Nick's.

## Summary

| Merged PR | Ticket | Merged | Promise state |
|---|---|---|---|
| #4939 | SOLR-18135 | 2026-09-24 | none |
| #4953 | SOLR-8291, SOLR-13217 | 2026-09-30 | open (candidate #5004, unverified) |
| #4959 | SOLR-18277 | 2026-10-05 | none |
| #4965 | SOLR-10198 | 2026-10-07 | none |
| #4995 | SOLR-18196 | 2026-10-03 | none |
| #5001 | SOLR-18317 | 2026-10-05 | open (branch exists, no PR) |
| #5012 | SOLR-13202 | 2026-10-07 | none |
| #5070 | SOLR-4841 | 2026-10-10 | open (no matching branch found) |

Open promises from merged PRs: three (#4953, #5001, #5070). All three are waiting on Nick's call. Named shapes that sit in open PRs or in no merged PR: five, none discharged.

No discharge in this ledger has been executed. No PR was opened, closed or changed, no comment was posted, and no ticket was filed.

## Round 2 hold

`assignments/pool-four-new-prs-strong-review-round-2.md` did not start in this round. Its own rule waits for its seven gate inputs to be DONE or for the owner to direct a subset. At this pass, six of the seven are OPEN. The symlink premise is DONE. Its two owner rulings are recorded in the assignment and are not reopened here.

## Round 2, 2026-10-14

Pass dated 2026-10-14 (pr-prepare tip at pass start `72d4f126d0`). The standing rule (Nick, 2026-10-13) re-runs this audit whenever one of our PRs merges; this pass also re-checks every round 1 state.

Merged set, re-derived from the GitHub search API with round 1's query: total still 8, identical to round 1's set. The latest merge is #5070 on 2026-10-10, which round 1 already covered. No PR has merged since round 1, so round 2 newly covers no merged PR and extracts no new promises.

### State re-checks of round 1 items (all read live on 2026-10-14)

#### #4953 promise (SOLR-16130 test-only PR): candidate verified, promise still open

Promise, verbatim from round 1: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."

Round 1 left the candidate #5004 unverified. Verified now, from three independent reads: #5004's head branch is `solr-16130-test-followup` at `3c48dec4b79f`; its file list is test-only (`CrossCollectionJoinQueryTest.java` and the ccjoin test configset's `solrconfig.xml`); and its body states the root cause "was fixed and merged in #4953 (SOLR-8291)" and "This PR contains no production change; it adds the regression test for the reported scenario." Its receipt (`receipts/SOLR-16130.md`) records GATE GREEN at that head. State: OPEN, now resting on a verified PR; the promise discharges when #5004 merges. #5004 is open and unmerged at this pass. The verification item round 1 left open is discharged; the promise itself is not.

#### #5001 promise (server-side SOLR-18317 PR): owner ruled kept, 2026-10-14

Promise, verbatim from round 1: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."

Disposition: OWNER RULED KEPT (Nick, 2026-10-14). Nick considers the promise kept: the paired follow-up work for the GET/PUT side was built and awaits Pugh's review. Factual note, recorded alongside the ruling so the ledger stays honest: the server-side branch `solr-18317-server-submit` stands built at `ae918a03fa7b`, the round 1 head, and no separate PR has been opened from it as of this round 2 audit; a main-side API check on 2026-10-14 confirms only #5001 names SOLR-18317. If that branch is ever to open as its own PR, it needs Nick's word like any opening.

Addendum (2026-10-14, later): the link behind the ruling is verified. On #5001, Eric Pugh commented at 2026-10-05T13:38:52Z: "Okay, @nick-boss-tech I took a stab at trying to move us to using the V2 Logging APIs, but we don't yet properly handle everything there... So, I was wondering if you would be interested in a follow up to this PR to tackle that work? I'm going to add more detail to https://issues.apache.org/jira/browse/SOLR-15823". That follow-up is the SOLR-15823 pair, both open: #5030 (the PUT side: V2 logging level changes broadcast to named nodes, the Admin UI migrated to V2) and #5031 (the GET side: nodes broadcast on the V2 logging levels listing). SOLR-15823 ("Split v2 /node/logging API into separate GET and PUT APIs and deal with single node/all nodes logic") is still Open in Jira; SOLR-18317 is Resolved. The split-out server branch was never opened, and with SOLR-18317 resolved it cannot be opened under that ticket now. Whether its standalone nodes=all narrowing is fully covered by current main plus the SOLR-15823 pair is an open code question; Nick has been offered a check on it. Update (2026-10-14): the coverage check is assigned as `assignments/pool-solr-18317-server-coverage-round-1.md`, with per-item verdicts against current main and the SOLR-15823 pair as its deliverable. Nick's recorded expectation is that a new ticket will likely be needed for whatever gap remains; the assignment includes a drafted ticket text for that case, which waits for his decision and is not filed by the assignment.

#### #5070 promise (detection-running test): no change; round 1's open inspection item resolved

Promise, verbatim from round 1: "A follow-up submission is planned for a test that runs language detection through a custom identifier."

Round 1 left the branch `ci/4841-langid-r28` uninspected. Inspected now: its tip is `41c0087c8a29` and it is the CI copy of the merged change, carrying the same `CustomLanguageIdentifierTest` that merged in #5070. That test builds a `DetectedLanguage` from another package and checks the two getters; #5070's own Limits says of it, "It does not run language detection." No fork branch carries a test that runs language detection through a custom identifier. State: OPEN, unchanged, still owed. Whether to submit it is Nick's call.

Addendum (2026-10-14, later): Nick ruled the promise is to be executed: the SOLR-4841 ticket is still open and the follow-up was never built. State: OPEN, assigned to the review agent on 2026-10-14 under `assignments/pool-solr-4841-langid-followup-round-1.md`, which directs building the detection-running test on branch `solr-4841-langid-followup-submit`, review rounds to SATISFIED, and a gate job (`gates/SOLR-4841-langid-followup.md`) for a Linux host. The assignment does not authorize an opening; the branch joins an openings slate for Nick's approval once gated and satisfied.

#### Named shapes: no state changes

- #5100 (SOLR-18532): still open at `7dfd3d98d0a2`; `solr-9865-submit` still at `4937608b`, so the rollback hunk has not been dropped. PENDING MERGE, unchanged.
- SOLR-17393 and SOLR-9637: branches unchanged (`solr-17393-submit` at `626241e6`, `solr-9637-submit` at `c50fa4ff`); neither has an open PR yet. NOT YET DUE, unchanged.
- #5104 (SOLR-2681): still open at `a3b1ea7994d9`. PENDING MERGE, unchanged.
- #5061 (SOLR-18119): still open at `660faedd026e`. PENDING MERGE, unchanged.
- #5089 (SOLR-13943) and SOLR-13059: #5089 still open at `ca443f6f6806`; the SOLR-13059 follow-up still has no fork branch and no PR. PENDING MERGE, unchanged.

### Round 2 summary

Newly covered merged PRs: none (no merges since round 1). Open promises from merged PRs: three recorded (#4953, #5001, #5070), of which #5001 was ruled KEPT by the owner on 2026-10-14 (see its item above), leaving #4953 and #5070 still owed. One state change since round 1: the #4953 candidate is verified as the planned test-only PR, so that promise now rests on #5004 and discharges at its merge. One open item resolved with no state change: `ci/4841-langid-r28` is not the #5070 follow-up test. Every discharge remains Nick's call. Addendum (2026-10-14, later): the #5070 promise is now assigned to the review agent for execution (see its subsection above); the #4953 promise is unchanged.

No discharge in this ledger has been executed. No PR was opened, closed or changed, no comment was posted, and no ticket was filed.
