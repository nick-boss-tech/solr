# Assignment: RC3 vote-thread note, round 1 (draft only)

## Background

Solr 10.1.0 RC3 is on a vote thread started by Andrzej Bialecki on
2026-10-22. The vote closes Tuesday 2026-10-27 11:00 UTC (05:00 MDT).
The RC2 to RC3 delta audit (`reports/rc3-delta-audit-round-1.md`,
RC3 tip `749712b719` on `branch_10_1`) found no defect in any release
artifact. Two release-note items from that audit bear on the vote.
This assignment turns those two items into a short vote-thread note
for the owner. Nothing in this assignment is posted or sent by the
claimant.

## Work

Draft a short vote-thread note (dev-list email text) presenting the
two vote-bearing findings below, with precise file and line
citations, and letting the release manager decide whether they
warrant action before the release. Re-verify every citation against
the RC3 tip before drafting; do not copy the audit's line numbers
blindly.

Finding 1, dependency list cross-category duplicates in CHANGELOG.md
at RC3:

- `langchain4j-*` (5 jars) appears as added (line 291, version
  1.19.0) and as upgraded (line 292: cohere and hugging-face
  0.35.0 to 1.19.0-beta29; core, mistral-ai and open-ai 0.35.0 to
  1.19.0). The versions for cohere and hugging-face differ between
  the two lines.
- `google-api-services-storage-v1` appears as added at rev20260524
  (line 291) and as removed at rev20240621 (line 293). It is one
  upgrade.
- Cause, per the audit: `dev-tools/scripts/dependencyChanges.py`
  compares license filenames, so a version bump reads as a removal
  plus an addition, or as an addition plus an upgrade. Suggested fix
  for the release manager: key the comparison on artifact name, or
  hand-check the three categories against `solr/licenses` before
  the vote.

Finding 2, cuVS wording against the ticket state:

- `major-changes-in-solr-10.adoc` line 88 and
  `dense-vector-search.adoc` line 1038 say the cuVS problem "is
  expected to be fixed in a subsequent Solr 10.x release".
- SOLR-18556 (Open, affects 10.1) carries no fix version and no
  target. The note states an expectation the ticket does not hold.
- Suggested wording: "no fix is scheduled yet; see SOLR-18556".

Leave out, deliberately:

- Cosmetic items: the "accelertion" typo, the AGENTS.md hunk in the
  SOLR-17820 backport, the "refer SOLR-18556 for updates" wording.
  A vote thread is for release-affecting concerns; nitpicks would
  read as noise.
- The question whether a known-issue line belongs in the changelog
  for cuVS. That is a convention question for the owner, not a
  finding, and it does not belong in the note.

## Rules

- Draft only. Nothing is posted, no email is sent, and no vote is
  cast by the claimant. The owner posts himself if he chooses, and
  the owner decides any vote. Do NOT draft a +1 or -1 on his behalf.
  The note presents findings and asks the release manager whether
  they warrant a fix, nothing more.
- Write as the owner ("I"), short, factual, friendly. No em dashes.
  No "we" for the owner plus the agent. No internal process
  vocabulary: no gate, receipt, ledger, or audit jargon. The note
  states what was checked and where, in plain language.
- Do not overclaim. The audit checked files and records; it did not
  build or run the release. The draft must not imply a smoke run
  or a build happened.
- The bar is high: the owner posts on the dev list rarely and
  deliberately, so the draft must be good enough for him to decide
  at a glance whether to send it, edit it, or drop it.

## Deliverables

- `drafts/rc3-vote-note-draft.md`: the draft email text, ready for
  the owner to send as-is or edit.
- `reports/rc3-vote-note-round-1.md`: one paragraph stating what
  was included, what was left out and why, and the sources checked
  at the RC3 tip.

## Deadline

Deliver by Monday 2026-10-26 evening MDT, so the owner has time to
decide before the vote closes Tuesday 2026-10-27 11:00 UTC.

Claim first per WORKFLOW. One claim covers this whole assignment;
it is one unit of work.
