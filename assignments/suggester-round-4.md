# Assignment: round 4, suggester audits

Round 4 opens the suggester area (8 branches in
branch-focus-inventory-2026-10-08.md), following the
inventory's suggested batching order: suggester third,
after eDisMax (already through two review rounds in the old
system) and update processing (rounds 1 to 3 and their
close-out). This round is audits only; drafts follow in a
later round once the audits settle, the same shape update
processing used.

Rules: verify each live fork tip first and audit the tip
you find (record it; if it differs from the inventory head,
say so and audit the live tip). No submit-branch edits, no
gates or tests run by the review side, no PRs, no comments.
Claim first with a commit under claims/, then one audit per
ticket under audits/suggester/, then a report at
reports/suggester-round-4.md.

## Scope

In scope (7): SOLR-9227, SOLR-9968, SOLR-10937, SOLR-11844,
SOLR-14171, SOLR-17215, SOLR-17393.

Out of scope (1): SOLR-9637, held on the owner's stacking
call (it stacks on SOLR-17393). Do not audit it. The
SOLR-17393 audit must note the stack dependency and what
17393 landing would change for 9637, without deciding it.

Sequencing note from the inventory: SOLR-9227 before
SOLR-9968, because both edit the same suggester test class;
audit them in that order and cross-check the interaction.

## Per-branch audit contents

For each branch, from the branch diff against its base and
the last review on record (the main side's review files and
the receipts ledger are sources; cite what you use):

- What the change does, in plain terms, and the main files.
- Gate state: the receipt (head, counts, date) or a plain
  statement that none is on record for the live tip.
- The last review's findings and whether each is answered
  at the live tip.
- Blockers: owner calls, stacking, unproven premises,
  anything that would hold a draft.
- A draft-readiness verdict: ready to draft, draft with
  named Limits or Choices, or held, with the reason.

The report carries the per-branch table, the verdicts, and
any owner decisions the audits surface, stated as decisions
needed, not taken.
