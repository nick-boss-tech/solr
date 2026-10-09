# Assignment: final review round, batch 2, update processing

## Purpose

Round 1 (`assignments/update-processing-final-round.md`) drafted
12 of the update processing branches and held 5. Since then the
held branches' items were resolved on the main side, and the
branches that were in flight (the eight decision items and their
companions) have landed and gated. This assignment is the final
round for those branches: a final review of each at its current
head, and a PR draft for each, written to this branch under
`pr-drafts/update-processing/`. Three drafts from round 1 also get
targeted updates. The formula is `pr-formula.md`; follow it
exactly, as in round 1.

Material: `material/update-processing-final-round-2.md` carries
the head each draft is written against, the proof source for each
branch, and the per-branch notes. Decisions are in `TESTING.md`,
including the "Decisions on the eight DISCUSS items" and "Final
round outcomes" sections.

## In scope

New drafts (12): SOLR-4841, SOLR-5754, SOLR-5939, SOLR-5941,
SOLR-16673, SOLR-5065, SOLR-6065, SOLR-7504, SOLR-12703,
SOLR-12705, SOLR-6045, SOLR-14718.

Draft updates (3): SOLR-13265 (Proof gains the fail-before
verdict), SOLR-16356 (Proof cites the head receipt), SOLR-7022
(re-verify against the new head; the delta is a changelog reword).

## Out of scope, and why

- SOLR-16655: a probe found its wider child-document descent
  changes output under an unselected parent field; the owner's
  gate-or-accept call is pending. Do not touch its draft.
- SOLR-12245: its settling run returned the response-detail call
  to the owner; pending.
- SOLR-13696 and SOLR-13943: blocked on a pre-existing defect in
  alias creation that the owner is routing; neither can gate yet.
- SOLR-11475: the owner's sign-mismatch call is still open.
- SOLR-18505: closed in round 1; its description edit is applied.

## Per branch, in order

1. Verify the live fork tip of `solr-<ticket>-submit` equals the
   head named in the material file. If it moved, hold that branch,
   record the new tip in the report, and go on.
2. Final review: read the branch's outbound diff at that head
   against its audit (`audits/update-processing/SOLR-<ticket>.md`,
   including any settling or probe notes appended since round 1)
   and the decisions in `TESTING.md`. Confirm every adopted
   decision is reflected and nothing undecided is presented as
   settled. Confirm the tree is submission-clean: no root-level
   testing notes, changelog fragment present and valid as plain
   YAML, changelog type matching the change.
3. If the final review finds a defect or an unsupported claim, do
   not draft. Record the branch as held with the finding stated
   plainly.
4. Write or update the draft at
   `pr-drafts/update-processing/SOLR-<ticket>.md`, named for the
   verified head. Proof comes only from the proof source named in
   the material file.

## Rules

- Claim first: add `claims/update-processing-final-round-2.md`
  before starting, and commit it on this branch.
- Drafts and the report are the only deliverables. Do not open
  pull requests, do not post comments, and do not edit any submit
  branch or any live pull request description in this round.
- Commit as Nick Shanin, no trailers, no em dashes anywhere, no
  PR numbers in file names or commit messages (ticket keys only).
- Deliverable report: `reports/update-processing-final-round-2.md`,
  one row per in-scope branch: head verified, proof source used,
  draft path or update made, or held with the reason.
