# Assignment: final review round and PR drafts, update processing

## Purpose

The update processing and atomic updates category (29 branches) has
been audited branch by branch (`audits/update-processing/`), its
decisions are recorded in `TESTING.md`, and most branches are gated.
This assignment is the final round for the 18 branches listed below:
a final review of each branch at its current head, and a PR draft
for each, written to this branch under
`pr-drafts/update-processing/`.

Material: `material/update-processing-final-round.md` carries the
head each draft is written against, the proof source for each
branch, and the per-branch draft notes from the audits and the
decisions. The formula is `pr-formula.md` on this branch; follow it
exactly, including the presentation rules (a bold one-line summary
opens every section, citations are real links at the draft's head,
simple language, the AI header at the top and the AI assistance
footer at the end).

## In scope (18)

SOLR-4841, SOLR-5505, SOLR-5887, SOLR-6973, SOLR-5939, SOLR-5754,
SOLR-5941, SOLR-12864, SOLR-3657, SOLR-13265, SOLR-7022,
SOLR-11483, SOLR-14262, SOLR-16655, SOLR-16673, SOLR-16356,
SOLR-16910, and SOLR-18505 (closing item only, no new draft).

## Out of scope, and why

- In flight on the main side (a fix or a settling run is running;
  they join a later final-round batch): SOLR-5065, SOLR-6065,
  SOLR-7504, SOLR-12245, SOLR-12705, SOLR-12703 (error-message
  tweak), SOLR-14718, SOLR-6045, SOLR-13943, SOLR-13696 (gate
  running).
- SOLR-11475: gated green, but its sign-mismatch behavior call is
  still the owner's. No draft until he makes it.

Do not draft, edit, or comment on any out-of-scope branch.

## Per branch, in order

1. Verify the live fork tip of `solr-<ticket>-submit` equals the
   head named in the material file. If it moved, hold that branch,
   record the new tip in the final-round report, and go on to the
   next branch. Do not draft against a moved tip.
2. Final review: read the branch's outbound diff at that head
   against its audit (`audits/update-processing/SOLR-<ticket>.md`)
   and the decisions in `TESTING.md`. Confirm every audit finding
   marked addressed is addressed in the tree, every adopted
   decision is reflected, and nothing undecided is presented as
   settled. Confirm the tree is submission-clean: no root-level
   testing notes, the changelog fragment present where the branch
   has one and valid as plain YAML, and the changelog type matching
   the change.
3. If the final review finds a defect, a tree problem, or a claim
   the evidence does not support: do not draft. Record the branch
   in the final-round report as held, with the finding stated
   plainly. A held branch is a valid outcome.
4. Write the draft to `pr-drafts/update-processing/SOLR-<ticket>.md`,
   named for the head verified in step 1. Proof comes only from
   the proof source named in the material file. Apply the
   per-branch draft notes from the material file. One existing
   draft (`SOLR-6973.md`) was written during the audit round with
   Proof awaiting gate; replace its Proof with the receipt in the
   material file and re-verify the rest of the draft against the
   final review.

## Closing item: SOLR-18505

SOLR-18505 already has a live pull request, gated at an earlier
head. Its audit asks for a one-sentence exactness fix in the
description's corroboration line. Apply that single edit to the
live pull request's description, nothing else: no other wording
changes, no comments. Record the edit in the final-round report
with the sentence before and after.

## Rules

- Claim first: add `claims/update-processing-final-round.md`
  before starting, and commit it on this branch.
- Drafts and the report are the only deliverables. Do not open
  pull requests, do not post comments, and do not edit any submit
  branch. Description edits are limited to the SOLR-18505 closing
  item.
- Commit as Nick Shanin, no trailers, no em dashes anywhere, no
  PR numbers in file names or commit messages (ticket keys only).
- Deliverable report: `reports/update-processing-final-round.md`,
  one row per in-scope branch: head verified, proof source used,
  draft path or held with the reason, and for SOLR-18505 the
  description edit record.
