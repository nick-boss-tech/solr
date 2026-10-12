# Pool assignment: small open notes on three converged drafts, round 1

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

Three converged drafts carry small notes that the rounds named and nothing applied: `pr-drafts/update-processing/SOLR-12864.md` (the round 3 note to delete Limits line 31), and `pr-drafts/update-processing/SOLR-5939.md` and `SOLR-6065.md` (link forms). This round applies or settles each note under the ladder.

## Checks

1. SOLR-12864: read the round 3 note in `reports/update-processing-convergence-round-3.md` and the draft's Limits. Decide whether the Limits line is true and supported. If the note is correct, delete the line. If the line is supported by the code, keep it and record that the note was checked.
2. SOLR-5939 and SOLR-6065: read the link-form note in the round report that names them (search `reports/update-processing-convergence-round-*.md` for the ticket numbers). Make the link form match the convention the other converged drafts use, using the same head SHA the draft already names. Do not change the head or the wording.
3. For each draft, verify every link in the edited text still resolves at the head it names (`git cat-file -e <sha>:<path>`).

## Ladder

Information first, from the round note and the code. Where the note is correct, the effort route is to apply it. Where the note is optional, defer it as a named Limits follow-up with a stated plan, and say so.

## Rules

- Change only the lines the notes name. No builds, no tests, no Gradle, no posting.

## Deliverable

`reports/small-open-notes-round-1.md`: each note, its verdict (APPLIED, KEPT WITH LINK, or DEFERRED), the before and after text, and the ladder rung. Apply the edits in the same push. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
