# Merged PR follow-up promises, ledger, round 1

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
