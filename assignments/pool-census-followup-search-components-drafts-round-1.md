# Assignment: search components drafts for the gated branches that have none, census follow-up round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Write each draft, then review your own draft against the receipt and the branch, apply what the review calls for, and keep going until a SATISFIED verdict comes back on each item. A draft that has not been through that loop does not discharge this assignment.
- Drafts follow `pr-formula.md` exactly: the AI header, a bold one-line summary opening every section, Proof numbers only from the ticket's receipt, citations as blob links at the head SHA (pre-change symptoms at the merge-base, saying so), plain language, no internal process vocabulary, no "we" for Nick plus agent, no em dashes.
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Twelve search-components branches, each GATE GREEN at its live tip per its receipt, with no draft under `pr-drafts/search-components/`:

SOLR-6759, 8003, 8051, 8088, 8767, 8954, 9124, 9595, 10305, 16155, 17055, 17841.

Starting records: `receipts/SOLR-<ticket>.md` per ticket and the census rows in `reports/openings-census-round-1.md`.

Per-ticket notes:

- Draft only what the receipt supports. A few receipts are thin or carry no gate date on their gated line (SOLR-10305, SOLR-9124, SOLR-9595, SOLR-17055); where a Proof count or a premise result is not in the receipt, say so in the deliverable report as a named gap instead of writing around it.
- SOLR-8051: SOLR-15319 lands after this branch. The draft's Limits names that order as a follow-up fact, in plain words.
- SOLR-6193 is excluded (HOLD: scope disputed in its receipt). SOLR-18541 is excluded (its own loop on the open PR).

## Deliverable

`reports/census-followup-search-components-drafts-round-1.md`: one line per ticket, SATISFIED with the draft path, or the named gap or hold that survives the ladder; plus the list of drafts written.
