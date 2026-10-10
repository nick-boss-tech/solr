# Assignment: eDisMax round 3 (consistency pass on the settled family, first audit for the two ungated tickets, drafts where the record leaves drafting to do)

Claim first: add `claims/edismax-round-3.md`, then work. Report: `reports/edismax-round-3.md`. Drafts, for the tickets this round finds draftable, go in `pr-drafts/edismax/` following `pr-formula.md`.

Staffing: run this round with 5 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together. Suggested clusters: (2309, 2988, 4362), (3243, 3729, 3962), (6009, 6320), (12092, 14913), (3923, 7120, 14638).

## Scope

Thirteen tickets, from the owner's inventory (eDisMax and extended dismax section of `branch-focus-inventory-2026-10-08.md`): SOLR-2309, SOLR-2988, SOLR-3243, SOLR-3729, SOLR-3923, SOLR-3962, SOLR-4362, SOLR-6009, SOLR-6320, SOLR-7120, SOLR-12092, SOLR-14638, SOLR-14913. All branches are `solr-<ticket>-submit` on the fork. Every live tip was checked against the inventory on 2026-10-09 and all thirteen match it.

## What the earlier rounds already settled (do not re-derive)

The six round 27 members (2309, 2988, 3243, 3729, 3962, 6320) went through the inventory-queue pipeline and a family review round in 2026-10-06, then a second family pass (round 32, 2026-10-07) that also covered 4362 and 6009; round 33 (2026-10-07) dispositioned 12092 and 14913, and round 36 (2026-10-08) closed the delta findings on 3729 and 6320. Settled, and not open to re-audit: every finding those rounds raised was dispositioned with runs, the branches were re-gated after each disposition, and the verdicts stand (all ten reviewed tickets PR-ready at their current heads). Per-ticket settled points the drafts must reflect, not relitigate: 3243's unfielded [* TO *] widening is intended and stated; 3962's boosted and parenthesized match-all spellings are handled; 4362's F5 shape (term~2 or term^2 beside a phrase) stays a Limits line; 6009's coverage was closed with test additions only; 14913's core premise and its all-invalid pin shapes are proven. Where an earlier report already carries a draft text for the current head (3729 and 6320 in the round 36 reports, 12092 in the round 33 report), check it against the formula and the plain-language rule and adopt or correct it in `pr-drafts/edismax/`; do not rewrite from scratch without a reason.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-2309: gate green at the live tip 06f5a1c4a87, TestExtendedDismaxParser 40 of 40, proof recorded.
- SOLR-2988: gate green at the live tip d2d144dfd9f, 42 of 42, proof recorded.
- SOLR-3243: gate green at the live tip 1db99c13662, 42 of 42, proof recorded.
- SOLR-3729: gate green at the live tip 0fe7e503945, 41 of 41, proof recorded. One owner call is already on record (below).
- SOLR-3923: NO GATE, never pipelined, branch still carries its TESTING.md note. First audit here; do not draft it.
- SOLR-3962: gate green at the live tip e7d5f350503, 43 of 43, proof recorded.
- SOLR-4362: gate green at the live tip e96a057439c, 40 of 40, proof recorded.
- SOLR-6009: gate green at the live tip a41bb034a1f, 41 of 41, proof recorded.
- SOLR-6320: gate green at the live tip cb710c96353, 44 of 44, proof recorded. Two owner calls are already on record (below).
- SOLR-7120: NO GATE, never pipelined, branch still carries its TESTING.md note, and the inventory gives it no topic line. First audit here; do not draft it.
- SOLR-12092: gate green at the live tip ca9573dabd3, 82 focused tests across nine classes, proof recorded.
- SOLR-14638: PARKED, no gate, held under review on the owner's side. Audit only; do not draft it.
- SOLR-14913: gate green on the gated tree; the live tip b80221f46d3 adds one comment-only commit. One owner call is already on record (below).

## Owner decisions already on record (state them; do not re-pose them as new)

- SOLR-3729: keep the broader match-all spellings (signed, boosted, parenthesis forms, and a *:* inside a larger group), or narrow to the ticket's (*:*) alone. The draft states the implemented broader set and poses this in its Choice.
- SOLR-6320 finding 1 remedy: a mixed-case And or Or neighbour is not counted as an explicit operator, so both words promote and the query falls back. The remedy is the owner's call, posed as the Choice in the round 36 draft.
- SOLR-6320 demotion rule: a standing owner call; the draft states its compatibility effect plainly (a query that used to fall back now parses) and names the contrary expectation recorded on the ticket.
- SOLR-14913: ratify the all-invalid alias behavior (MatchNoDocsQuery at the head, against the base's silently dropped clause). Recommendation on record: keep MatchNoDocsQuery.

## Interactions to check

All thirteen branches change ExtendedDismaxQParser.java (the outer class or its ExtendedSolrQueryParser inner class) and share the TestExtendedDismaxParser class and its schema12.xml fixtures, so landing order matters more here than in any other category. Check the hunks pairwise where the records name a coupling, and state a landing order for the family in the report, using trial merges (git merge-tree) where two branches touch the same method:

- SOLR-3729 (splitIntoClauses standalone match-all) and SOLR-3962 (the pf skip in addPhraseFieldQueries): 3962's isMatchAllDocsClause mirrors 3729's standalone shape, including the plain-number boost rule. State which lands first and what the second must reconcile.
- SOLR-2988 (pf shingle joining in addShingledPhraseQueries) and SOLR-4362 (pf2 slop clauses): both work the phrase-field machinery; check for hunk overlap and changed context.
- SOLR-2309 (inner-class getQuery FUZZY path) and SOLR-3243 (inner-class getRangeQuery): recorded as separate hunks with no direct interaction; confirm that still holds at the current heads.
- SOLR-6320 (rebuildUserQuery) and SOLR-6009 (regexp handling in getQuery): separate methods on the record; confirm.
- SOLR-12092 (noStopwordFilterAnalyzer) and SOLR-2309 both turn on stopword handling in different methods; state whether a query can reach both changes and whether the order matters.
- SOLR-14913 (alias handling in getQueries and getMultiTermQueries) fans clauses out over qf fields like several siblings; check its hunks against 3243 and 6009.
- SOLR-3923 and SOLR-7120 are unaudited: place their hunks on the same map as part of their first audit.

## The audit

For the ten reviewed tickets, this is a consistency pass: read the Jira ticket, the branch diff at the receipt's head, the receipt, and the latest per-ticket report named in the receipt. Confirm the three agree; flag any drift, do not re-run the settled verdicts. Then judge what drafting remains: tickets whose latest report already carries a formula draft need that draft checked and filed; the rest need a draft written from the record. For 3923 and 7120, do the full first audit: what the branch claims, what its TESTING.md note claims, whether the diff matches the ticket, and what a premise run would need to show; the premise run and gate are main-side work that follows. For 14638, audit only: confirm the branch still matches the ticket and the park note, and say what would need to happen before it could move. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; a defect in a branch's own new code is fixed in that branch, not deferred to a follow-up; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a PR description must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/edismax-round-3.md`: per-ticket verdict (draftable with the draft filed, held with reason, consistency result for the reviewed tickets, first-audit result for 3923 and 7120, audit result for the parked 14638), the family landing order, plus any disagreement with the receipts.
2. Drafts in `pr-drafts/edismax/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. The owner decisions listed above go in the report as already on record, not as new questions.
