# Assignment: Proof-shape audit, round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- The review agent applies the wording changes its findings call for, in the same loop, and keeps reviewing until each audited ticket has a decided proof shape recorded. A round that only lists findings without applying what is applicable does not discharge this assignment.
- Where the decided shape is a record or a wording change, apply it (the receipt note and the draft's Proof section) and push it.
- Where the decided shape is a run, post a fully specified gate job under gates/ (branch, head, merge-base, module, test classes with packages verified on the branch, seed, the exact base-leg construction, and the deliverable). A Linux host runs it; the reviewer does not run builds.
- Scope questions: decide under the decision default (a recorded recommendation wins; where none exists, take the harder, more thorough option) and the three-rung ladder (where information is missing, get more information; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up). Record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Anything that seems to need a comment is drafted in the deliverable report for the owner instead.
- Authored text: plain language, no internal process vocabulary in any text meant to be public, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt or a gate log.

## Task

Audit every receipt under receipts/ whose proof stands INCONCLUSIVE by construction or by compile, and decide the proof shape for each. The worked precedent is SOLR-14187: where a branch test cannot compile against base production, a pre-named base-compilable scratch vehicle plus an INCONCLUSIVE-by-compile record carries the premise. The other established shape is a base-leg run with the branch-added files reverted in the base-leg tree only (the SOLR-18178 construction). The third outcome is an explicit record that the proof cannot be strengthened, with the draft's Proof rewritten to say so plainly instead of implying a fail-before run exists.

Candidate set from a read of the receipts at posting time; verify the standing state of each receipt before auditing it, because some receipts mention an inconclusive state that a later head superseded, and sweep receipts/ once more for the word INCONCLUSIVE so nothing is missed:

SOLR-10641, SOLR-11475, SOLR-12998, SOLR-15331, SOLR-15357, SOLR-15386, SOLR-15674 (its state carries over by construction), SOLR-15863, SOLR-15895, SOLR-16108, SOLR-17433, SOLR-17539, SOLR-17987, SOLR-18277, SOLR-18341, SOLR-1877, SOLR-3684, SOLR-7550, SOLR-9155, and SOLR-11678 (verify: its live-tip receipt supersedes an inconclusive-by-compile state).

Notes on individual tickets:

- SOLR-12998: the shape decided here is a proposal; the main side ratifies it. Mark its line as a proposal in the report.
- SOLR-14187 is the precedent, not an audit subject.
- SOLR-15357 and SOLR-15331: the receipts classify the tests as pins (the new tests call APIs the change adds). Decide whether a scratch vehicle can still discriminate the premise, or whether the plain record is the honest shape.
- SOLR-11475: the base run ends in the endless loop and OutOfMemoryError of the audit's Finding C, so the proof is neither a pass nor a clean assertion failure. Decide what, if anything, can stand as its fail-before evidence.

## Deliverable

reports/proof-shape-audit-round-1.md: one line per audited ticket with its current proof state, the decided shape, and what was applied (receipt or draft wording) or posted (the gate job file name).
