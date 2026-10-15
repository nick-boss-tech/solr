# Assignment: core admin drafts owed and two refreshes, census follow-up round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Write each owed draft, refresh the two named drafts, review your own work against the receipts at the live tips, apply what the review calls for, and keep going until a SATISFIED verdict comes back on each item.
- Drafts follow `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes).
- Start from the receipts: a branch gated at its live tip is not re-audited.
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Drafts owed, each branch GATE GREEN at its live tip per its receipt, no draft under `pr-drafts/core-admin/`:

SOLR-5262, 8554, 8628, 14098, 15003, 16887, 18278.

Draft refreshes:

- SOLR-12916: the round 2 response corrected its label to NOT CONVERGED because the changelog retitle had not landed. It has since landed and gated at `d62ddfc2461`, the live tip. Judge the draft against that head (round 3's carry) and close it to SATISFIED or name what still stands.
- SOLR-8576: the alias fix landed and gated green at `883bb522ca`, the live tip, after the round 2 response was written. Refresh the draft's marker, head and counts from the receipt at that head and close it to SATISFIED or name what still stands.

Starting records: `reports/core-admin-convergence-round-1-response.md`, `reports/core-admin-convergence-round-2-response.md`, `receipts/SOLR-<ticket>.md` per ticket, and the census rows in `reports/openings-census-round-1.md`.

Exclusions: SOLR-5011 (HOLD: the shared-schema analysis returned UNSAFE), SOLR-16725 and SOLR-17731 (gate jobs posted: `gates/SOLR-16725-branch-edits-topup-gate.md`, `gates/SOLR-17731.md`), SOLR-18010 (open PR, its own loop), SOLR-11939 (READY TO OPEN).

## Deliverable

`reports/census-followup-core-admin-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold, plus every draft written and every refresh applied.
