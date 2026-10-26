# Decision assignment for phone-agent: backlog first-100 Unit 1 (from vm5, round 1, 2026-10-26)

Status: OPEN. Owner decision needed on four points. Nothing here posts to Jira.

## Context

vm5 is triaging the closure-not-a-bug family for the first-100 pack (`assignments/backlog-first-100-pack-slices-round-1.md`, Unit 1). Drafts are in `drafts/unit-1-comments/` (rounds 1 to 3). Code checks so far are in `reports/backlog-first-100-unit-1-round-1.md`.

## Decision 1: closure wording for ticket-text and ruling-based closures

The round 1 and 2 template says "I looked at this against current main." That is only true for records with a code citation. Round 3 drafts (19 not-a-bug records) use a different opening, "I reviewed this ticket," because they were not code-checked.

Options:
- A. Keep the code-check wording. Only records with a current-main citation go in the pack.
- B. Allow the "I reviewed this ticket" wording for ruling-based closures, as round 3 does. Requires the owner to accept a closure that rests on ticket text and maintainer discussion.

Recommendation: B for ruling-based, A for anything that says "now" in code.

## Decision 2: duplicates with no recorded target

None of the 7 closure-duplicate records has a recorded target (see `drafts/unit-1-comments/round-3.md`). The pack rule needs a closed or fixed original, or the live original.

Options:
- A. phone-agent searches JIRA for each target; a record without a target goes to overflow.
- B. Drop the whole closure-duplicate family from this pack.

Recommendation: A, time-boxed to one pass.

## Decision 3: conflict flags on closure-fixed (77 records)

Every closure-fixed record in the H1 batches has `conflict: true`. Pack rule 2 skips them. vm6 cleared a similar flag for closure-obsolete under the decision ladder.

Options:
- A. Owner clears the flags for closure-fixed, as for obsolete.
- B. Keep the flags, and closure-fixed stays out of this pack.

## Decision 4: closure-obsolete overlap

vm6 has Unit 3 (records 1 to 25). About 42 unflagged obsolete records are unclaimed.

Options:
- A. vm5 takes them, using a removal-evidence check per record.
- B. Leave them unclaimed until vm6 finishes Unit 3.

## Ask for phone-agent

1. Put each of the four decisions to the owner as a single question, with the options above.
2. For Decision 2, run one JIRA pass to look for targets for the seven records in `round-3.md`, and report what you find.
3. Report answers on `coordination-ui` as a reply file, not as a Jira comment.
