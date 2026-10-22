# Review: SOLR-18200, branch solr-18200-textprofile-tiebreak (unit 37)

Head reviewed: f8c4ee22bdd218ddfbf287fe0e389c17a5198f8f. Base: c8ab71430f. Ticket: SOLR-18200, "TextProfileSignature produces different signatures for identical input".

## Pass 1

- Ticket fit. The ticket reports that `TextProfileSignature` gives different signatures for identical input. The cause is the tie-break: tokens with equal counts keep HashMap iteration order, which changes on resize. The branch breaks ties by token text, so the order is fixed. The change fits the ticket.
- Scope. A three-line comparator change in `TextProfileSignature.java`, one test, and a PR draft under `pr-drafts/`. No changelog file.
- Finding, major (compatibility, owner, rung 5): the new order changes the signature for any document with equal-frequency tokens. Signatures already stored in existing indexes for such documents will no longer match signatures computed by the new code. The branch does not say whether that needs a reindex note or a deprecation step. Recorded for the owner, not changed.
- Finding, minor: the test compares two signatures computed in the same run. It proves order-independence, but it does not pin any signature value, so a change to the output format would not be caught. Recorded, not repaired.
- Finding, minor: no changelog file, though this changes indexed signatures. Recorded, not repaired.
- Proof leg: the test fails on base for the ticket's reason (order-dependent signatures).
- Repairs: none.

Regate: `gates/SOLR-18200-review-pass-1.md`, GATE GREEN at f8c4ee22bd on vm4.

## Pass 2

Review (2026-10-22, vm4), same head f8c4ee22bd. - Pass 1 major finding, unchanged, and confirmed in the diff: the tie-break changes stored signatures for any document with equal-frequency tokens. The owner must decide whether the change needs a reindex note. Not repaired.
- Pass 1 minor findings, unchanged: the test does not pin a signature value; no changelog file.
- Repairs: none.

Regate: `gates/SOLR-18200-review-pass-2.md`, GATE GREEN at f8c4ee22bd on vm4.

## Pass 3

Review (2026-10-22, vm4), same head f8c4ee22bd. - No new findings against the head. The pass 1 findings stand: the tie-break changes stored signatures for documents with equal-frequency tokens (major, owner, rung 5); the test does not pin a signature value (minor); no changelog file (minor).
- Repairs: none.

Regate: `gates/SOLR-18200-review-pass-3.md`, GATE GREEN at f8c4ee22bd on vm4.

Final head: f8c4ee22bdd218ddfbf287fe0e389c17a5198f8f. Unit 37 DONE with the three pass sections.
