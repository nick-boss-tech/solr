# Assignment: eDisMax final review pass to SATISFIED, round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- The review agent applies the patches or wording changes its findings call for, in the same loop, and keeps reviewing in further rounds until a SATISFIED verdict comes back on each item in scope. A round that only lists findings without applying them does not discharge this assignment.
- Wording findings: apply the fix to the draft under pr-drafts/edismax/ and push it.
- Code findings against a submit branch: the reviewer writes the patch on that branch in its own worktree and pushes it, then posts a gate job under gates/ naming the patched head and the tests to run; a Linux host runs the gate. Ungated code never counts as reviewed, and an item's review continues only on a gated head.
- Scope questions: decide under the decision default (a recorded recommendation wins; where none exists, take the harder, more thorough option) and the three-rung ladder (where information is missing, get more information; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up in the draft's Limits). Record each decision in the deliverable report. Only an item that survives all three rungs comes back as an owner item.
- History rewrites on a submit branch (squashes, trailer removal) are not review-side work under this assignment: record them as owner items with the state of the branch, do not perform them.
- No comments are posted by the review side anywhere. Anything that seems to need a comment is drafted in the deliverable report for the owner instead.
- Authored text: plain language, no internal process vocabulary in any text meant to be public, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt or a gate log. Citations to code a change produces link the head SHA; citations to a pre-change symptom link the merge-base and say so.

## Scope

The ten gated PR-ready eDisMax drafts, each under pr-drafts/edismax/ and each GATE GREEN per its receipt:

SOLR-2309, SOLR-2988, SOLR-3243, SOLR-3729, SOLR-3962, SOLR-6009, SOLR-6320, SOLR-7120, SOLR-12092, SOLR-14913.

Starting record, do not redo it:

- reports/edismax-round-3.md carries the per-ticket verdicts, the important findings and the landing order. The draft fidelity parts reports/draft-fidelity-searchcomponents-edismax-s11.md and -s12.md cover these drafts.
- SOLR-7120's receipt stands GATE GREEN at 4664014a919 (its handoff note was removed by the packaging commit). SOLR-14913 gated green at its live tip b80221f46d3 (TestExtendedDismaxParser 40 of 40 in the live-tip top-up run, 2026-10-13).
- Start from the ledger: verify each draft against its receipt at the live tip; do not re-audit a gated branch. Round 3 named the starting findings per ticket, among them: SOLR-3243's negated form is unstated and untested (test and changelog title are branch-side work; the draft must state the position plainly in the meantime), SOLR-6009's changelog claim and SOLR-6320's test comment, SOLR-2988's premise base naming, SOLR-3962's reconcile to SOLR-3729's helper (the draft must state the dependency and the landing order; the reconcile itself happens after SOLR-3729 lands), SOLR-12092's flagged sentence (neither the changelog title nor the guide entry states the consequence at the gated head, so the sentence stays flagged until the branch fix lands), SOLR-14913's changelog wording.
- Landing order from round 3 is part of what the final report restates: SOLR-3729 lands first in its family (then SOLR-3962, then SOLR-3243); SOLR-6009 before SOLR-6320; the insertion-point conflicts (SOLR-2309 with SOLR-2988, SOLR-12092 with SOLR-2309) keep both blocks.

## Exclusions

- SOLR-14638: parked, audit only. Excluded.
- SOLR-3923: GATE GREEN at 723f61ee4ab but it has no draft, and round 3 records its premise run as still owed. Not a draft review; excluded from this pass.
- SOLR-4362: a draft exists, but the ticket stands Held on a branch defect (round 3 verdict). This pass does not settle it; excluded.

## Deliverable

reports/edismax-final-review-round-1.md: one line per in-scope ticket, SATISFIED (READY TO OPEN) or the owner item that survived the ladder, plus the landing order the openings should follow and a list of every wording fix applied and every gate job posted.
