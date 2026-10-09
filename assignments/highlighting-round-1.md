# Assignment: Highlighting round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/highlighting-round-1.md`, then work. Report: `reports/highlighting-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/highlighting/` following `pr-formula.md`.

## Scope

Five tickets, from the owner's inventory (Highlighting section of `branch-focus-inventory-2026-10-08.md`): SOLR-2632, SOLR-2681, SOLR-3704, SOLR-4540, SOLR-16885. All five branches are `solr-<ticket>-submit` on the fork.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-2681: gate green at the live tip `4cb25b1691b`, HighlighterTest 36 of 36, proof recorded.
- SOLR-3704: gate green at the live tip `de63d4e5d5d`, HighlighterTest 36 of 36 plus extra runs, proof recorded.
- SOLR-4540: gate green at the live tip `62c06439fb0`, PR-ready, premise grounded by run.
- SOLR-2632: NO GATE, deliberately. Its premise check showed the production change is behaviorally inert on current main (the Lucene superclass methods already do what the branch adds). Its disposition is an owner call already on record: (a) test-only pin PR, (b) Jira comment and no PR, (c) bank as received. Audit it only to confirm that record still matches the branch and the ticket; do not draft it.
- SOLR-16885: NO GATE and never pipelined; the branch still carries its SOLR-16885-TESTING.md handoff note. Audit what the branch and the note claim and what a premise run would need to show; do not draft it. The premise run and gate are main-side work that follows from the audit.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the three gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming (the ledger review notes name Limits material for 2681 and 3704; check them against the diff). Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix.

## Deliverables

1. `reports/highlighting-round-1.md`: per-ticket verdict (draftable, held with reason, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/highlighting/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
