# Assignment: Search components, full-category review to SATISFIED

Capability tags: [review]. Intended host: Windows (review coordinator).

This branch carries the pr-prepare tree at its base tip 699d446731 plus this file. All deliverables for this assignment land on THIS branch under reports/. Nothing here goes on pr-prepare itself.

## Owner directive: run to SATISFIED

- The review agent applies the patches or wording changes its findings call for, in the same loop, and keeps reviewing in further rounds until a SATISFIED verdict comes back on each item in scope. A round that only lists findings without applying them does not discharge this assignment.
- Wording findings: apply the fix to the draft text in this tree. (No live PR descriptions are edited under this assignment; these drafts are not open PRs yet.)
- Code findings against a submit branch: the reviewer writes the patch on that branch in its own worktree and pushes it, then posts a gate job under gates/ on pr-prepare naming the patched head and the tests to run; a Linux host runs the gate. Ungated code never counts as reviewed, and an item's review continues only on a gated head.
- Scope questions: decide under the decision default (a recorded recommendation wins; where none exists, take the harder, more thorough option) and the three-rung ladder (where information is missing, get more information; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up in the draft's Limits). Record each decision in the deliverable reports. Only an item that survives all three rungs comes back as an owner item.
- No comments are posted by the review side anywhere. Anything that seems to need a comment is drafted in the deliverable report for the owner instead.
- Authored text: plain language, no internal process vocabulary in any text meant to be public, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt or a gate log. Citations to code a change produces link the head SHA; citations to a pre-change symptom link the merge-base and say so.

## Task

Claim first: record the claim as a commit on this branch (a claims/ file in this tree, following the WORKFLOW.md claim shape: host, slices, started timestamp in UTC) before starting work.

Review every draft under pr-drafts/search-components/ to SATISFIED. At the base tip that is 50 drafts, each with a receipt under receipts/.

Starting record, do not redo it:

- The category was worked in four sub-batches: reports/search-components-1.md, reports/search-components-2.md, reports/search-components-3.md and reports/search-components-4.md, with 71 receipts across the category.
- The gate slate stands green at the live tips, including SOLR-11391, SOLR-16570, SOLR-8088, SOLR-12044, SOLR-6759, SOLR-8003, SOLR-10694, SOLR-10305, SOLR-8051, SOLR-9124, SOLR-9595 and SOLR-17055.
- Start from the ledger: a branch whose receipt stands at its exact live tip is not re-audited. Verify the draft against the receipt and the branch head; spend the rounds where the draft and the record disagree, where a citation or count does not trace, or where the draft has never had a convergence pass. This category has had premise work, sub-batch reviews and a draft fidelity pass, but no convergence round on the drafts.

Working shape: define sub-batches from the receipt set (the four recorded sub-batches are the natural grouping), write one verdict report per sub-batch under reports/ on this branch, then the category summary at reports/search-components-review-summary.md. The summary names every in-scope ticket with one line: SATISFIED and READY TO OPEN, or the owner item that survived the ladder, stated in one sentence.

The summary also accounts for search-component receipts that have no draft under pr-drafts/search-components/ yet (gated, draft owed, not reviewed here). At the base tip that includes SOLR-16570, SOLR-8088, SOLR-6759, SOLR-8003, SOLR-10305, SOLR-8051, SOLR-9124, SOLR-9595 and SOLR-17055; verify the list at claim time. SOLR-11391's draft sits under pr-drafts/query-parsing/ and belongs to that category's pass, not this one.

Two verification notes:

- SOLR-18109: an upstream change under this key has already merged. Verify the draft still has something of its own to submit before spending rounds on it, and record the outcome in the summary.
- SOLR-15319: its production fix lives on the SOLR-8051 branch and it lands after that one. Review its draft text only; the landing dependency is a fact for its Limits, not a finding against the draft beyond noting it.

## Exclusions

- SOLR-18541 and the mergeIds work (SOLR-18539): under their own review assignment. Skip.
- SOLR-17372: premise not supported, retire pending owner word. Skip.
- SOLR-16290: a held pin. Skip.
- SOLR-16155: no PR of its own (covered by an upstream change). Skip.

## Discharge

This assignment is discharged when the category summary on this branch names a verdict for every in-scope draft, with every wording fix applied in this tree and every code fix either gated or recorded as an owner item.
