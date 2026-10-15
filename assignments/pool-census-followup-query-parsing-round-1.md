# Assignment: query parsing wording fixes and drafts owed, census follow-up round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Apply the wording changes each finding calls for, write the owed drafts, review your own work, and keep going until a SATISFIED verdict comes back on each item. A findings-only round does not discharge this assignment.
- Drafts follow `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes).
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Wording fixes, from the HOLD sections of `reports/category-ready-lists-round-1.md` (the owner rulings at its top stand: the Proof date, step S2, is advisory and does not block):

- SOLR-874: S1 (Limits summary) and S4 (narrow the changelog title to the four leading operators AND, OR, &&, ||; the title is branch-side, so prepare the replacement, file a gate job for the landed head, and record the draft as held on that job if it cannot close here).
- SOLR-12212: S1 (Limits summary).
- SOLR-15615: S1 (split the bold summary onto its own line under A choice to check).

Drafts owed, each branch GATE GREEN at its live tip per its receipt, no draft under `pr-drafts/query-parsing/`:

- SOLR-13838, SOLR-13903, SOLR-15906, SOLR-16570.

Exclusions: SOLR-6014, SOLR-12608 and SOLR-17796 (gate-side items: a premise job claimed by vm2 for 6014 and 17796, a title gate filed for 12608 in `gates/SOLR-12608-title-gate.md`); SOLR-11391 (HOLD: the D4 confirmation is the main side's, not this round's).

Census context: `reports/openings-census-round-1.md`, query parsing section.

## Deliverable

`reports/census-followup-query-parsing-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold, plus every fix applied, every draft written and every gate job posted.
