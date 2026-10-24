# Assignment: short per-query list IndexOutOfBoundsException in CombinedQueryComponent, round 1

Status: NEEDS-OWNER. One question: may a Jira ticket be filed for this defect, separate from SOLR-18539 and PR #5117? (Filing is public, so the owner decides it.)

- Posted by: vm2, 2026-10-24. Intended after the owner's answer: phone agent (draft, then file on the owner's go).

## Facts

- Defect: `CombinedQueryComponent.mergeIds` reads `docList.get(queryIndex)` without a bounds check (main `79b23df58da`, line 338). A partial shard that returns a shorter per-query list (a `timeAllowed` exit after some sub-queries) throws `IndexOutOfBoundsException`.
- It predates PR #5117. Main has the same unchecked access, and the receipt `receipts/SOLR-18541.md` records a real-exit premise job that reproduced the exception on the unmodified base.
- Fix, committed locally on vm2 as `c91b606355e` (branch `solr-18541-followups`, not yet pushed): `queryIndex < docList.size()` in the same condition, plus a test. The shorter list is skipped like any other partial shard without a per-query entry.
- The scratch repro that found it is saved at `/workspace/logs/vm2/wip-2026-10-24/vm2-18541-head.diff` on vm2.

## Decision already made (rung 3)

- Not added to PR #5117. The reviewer's requested scope was the merge totals, and `reports/pr-5117-followup-plan.md` keeps this failure out of SOLR-18539.
- Not in the SOLR-18539 PR either.

## Question for Nick

File a Jira ticket for this defect, titled along the lines of "CombinedQueryComponent throws IndexOutOfBoundsException when a partial shard returns a shorter per-query list"? Answer yes or no. Yes: the phone agent drafts the ticket text under `jira/` and files it after a second go. No: the fix stays a local commit and the defect stays recorded only here.

## Limits

- No JIRA or GitHub post until the owner answers. No PR. Commit as Nick Shanin, no trailers.
