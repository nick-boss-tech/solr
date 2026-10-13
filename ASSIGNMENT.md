# Assignment: Query parsing, full-category review to SATISFIED

Capability tags: [review]. Intended host: Windows (review coordinator).

This branch carries the pr-prepare tree at its base tip 699d446731 plus this file. All deliverables for this assignment land on THIS branch under reports/. Nothing here goes on pr-prepare itself.

## Owner directive: run to SATISFIED

- The review agent applies the patches or wording changes its findings call for, in the same loop, and keeps reviewing in further rounds until a SATISFIED verdict comes back on each item in scope. A round that only lists findings without applying them does not discharge this assignment.
- Wording findings: apply the fix to the draft text in this tree. For the one live-PR item in scope (SOLR-16130, text review only), apply wording fixes to the draft file here; description edits on the live PR stay with the main side.
- Code findings against a submit branch: the reviewer writes the patch on that branch in its own worktree and pushes it, then posts a gate job under gates/ on pr-prepare naming the patched head and the tests to run; a Linux host runs the gate. Ungated code never counts as reviewed, and an item's review continues only on a gated head.
- Scope questions: decide under the decision default (a recorded recommendation wins; where none exists, take the harder, more thorough option) and the three-rung ladder (where information is missing, get more information; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up in the draft's Limits). Record each decision in the deliverable reports. Only an item that survives all three rungs comes back as an owner item.
- No comments are posted by the review side anywhere. Anything that seems to need a comment is drafted in the deliverable report for the owner instead.
- Authored text: plain language, no internal process vocabulary in any text meant to be public, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt or a gate log. Citations to code a change produces link the head SHA; citations to a pre-change symptom link the merge-base and say so.

## Task

Claim first: record the claim as a commit on this branch (a claims/ file in this tree, following the WORKFLOW.md claim shape: host, slices, started timestamp in UTC) before starting work.

Review every draft under pr-drafts/query-parsing/ to SATISFIED. At the base tip that is 18 drafts.

Starting record, do not redo it:

- Round 1 covered 25 tickets, 22 of them gated: assignments/query-parsing-round-1.md and reports/query-parsing-round-1-q1.md through reports/query-parsing-round-1-q8.md (commit 9bfcb2ff1c1). That round was an answers pass; this category has had no convergence round on the drafts.
- Start from the ledger: a branch whose receipt stands at its exact live tip is not re-audited. Verify the draft against the receipt and the branch head; spend the rounds where the draft and the record disagree, where a citation or count does not trace, or where the draft has never been checked against its receipt at the live tip.
- SOLR-11391's draft sits in this folder and is in scope here, even though the ticket also appears in search-component gate slates.
- SOLR-6014: a premise run landed on 2026-10-13 and its verdicts are recorded in receipts/SOLR-6014.md (the cache cases return the whole corpus; the cost case and both filter-query cases fail at search time with a NullPointerException). Verify the draft's Limits state those verdicts plainly.

Working shape: define sub-batches from the draft and receipt set as the work needs, write one verdict report per sub-batch under reports/ on this branch, then the category summary at reports/query-parsing-review-summary.md. The summary accounts for all 25 round 1 tickets, one line each: SATISFIED and READY TO OPEN for a reviewed draft, the exclusion reason for an excluded ticket, the owner item that survived the ladder, or "no draft yet" where that is the state.

## Exclusions

- SOLR-13202: its PR has merged upstream. Skip.
- SOLR-16130: a live PR; text review only (see the directive above).
- SOLR-17882: the premise does not hold on main; it sits in the retire round. Skip.

## Discharge

This assignment is discharged when the category summary on this branch accounts for all 25 round 1 tickets, with every wording fix applied in this tree and every code fix either gated or recorded as an owner item.
