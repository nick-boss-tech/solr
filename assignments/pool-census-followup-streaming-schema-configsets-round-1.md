# Assignment: drafts owed for streaming, schema and configsets branches, census follow-up round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Write each draft from its ticket's receipt, review your own draft against the receipt and the branch, apply what the review calls for, and keep going until a SATISFIED verdict comes back on each item.
- Drafts follow `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes).
- Start from the receipts: a branch gated at its live tip is not re-audited.
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Every branch below is GATE GREEN at its live tip per its receipt, with no draft on file.

Streaming (drafts to `pr-drafts/streaming/`):

- SOLR-14200, SOLR-15326, SOLR-17433 (branch `solr-17433-17143-submit`; the ticket is SOLR-17433).

Schema (drafts to `pr-drafts/schema-analysis/`):

- SOLR-10131, SOLR-10403, SOLR-15712 (the schema round 1 NO GATE set: their receipts record the gate state that applies; write the Proof to match the receipt's own shape, not a template).

Configsets (draft to `pr-drafts/configsets/`):

- SOLR-7323.

Starting records: `receipts/SOLR-<ticket>.md` per ticket and the census rows in `reports/openings-census-round-1.md`.

Exclusion: SOLR-11922 (streaming; HOLD: its receipt records the premise as not holding on current main; retire-round material, not a draft).

## Deliverable

`reports/census-followup-streaming-schema-configsets-round-1.md`: one line per ticket, SATISFIED with the draft path, or the named gap that survives the ladder.
