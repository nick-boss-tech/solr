# Assignment: SolrCloud draft confirmations and refreshes, census follow-up round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- For each ticket, check the draft against its receipt at the live fork tip; apply the refresh the check calls for (head citations, Proof counts and dates, Limits that no longer match the branch), then re-check until a SATISFIED verdict comes back. A findings-only round does not discharge this assignment.
- Start from the receipts: a branch gated at its live tip is not re-audited. The convergence rule stands.
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- Drafts follow `pr-formula.md` (plain language, no internal process vocabulary, no "we", no em dashes; Proof only from a receipt or gate record).
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Thirteen SolrCloud tickets whose drafts are written but unconfirmed, or owed, outside the round 2 slate:

SOLR-3865, 4754, 5813, 10234, 10641, 11288, 12991, 13186, 13369, 15674, 15863, 16013, 17292.

Starting records: `receipts/SOLR-<ticket>.md` per ticket, the SolrCloud convergence responses (rounds 1 to 3), the batch A review (`reports/solrcloud-ready-review-batch-a-round-1.md`), and the census rows in `reports/openings-census-round-1.md`.

Per-ticket notes:

- SOLR-16013: refresh the draft to the gated fixture head `37b01337eb`. Its Proof comes from the refreshed receipt (OverseerCloseOrderingTest 3 of 3 at both recorded seeds; the pre-fix proof passed with the two premise failures). The production content the draft describes is unchanged in substance; the test-fixture restart is the new fact the draft must carry.
- SOLR-17292: two receipt records exist. Reconcile them in `receipts/SOLR-17292.md`: keep the dated run of record (4 of 4 at the live tip) as the standing record and mark the other as superseded, in place, dated. Then confirm the draft against the standing record.
- SOLR-5813, SOLR-12991, SOLR-13186, SOLR-15863: batch A reviewed them and the base dated runs are done as expected; confirm each draft's Proof states exactly what those runs show, nothing more.
- SOLR-11288: the premise base run is done and holds; the title re-gate landed at the live tip. Confirm the draft against both records.
- SOLR-3865, SOLR-4754, SOLR-10234, SOLR-10641: drafts owed. Write them from the receipts per `pr-formula.md`; where a receipt is thin, name the gap in the deliverable instead of writing around it.
- SOLR-13369, SOLR-15674: confirm against the dated-run and history-scrub records at the live tips.

Exclusions: the slate eight (SOLR-17680 and SOLR-9155 READY; SOLR-17733 and SOLR-14919 gates posted; SOLR-15035 HOLD; SOLR-11479 HOLD behind it; SOLR-15106 and SOLR-15386 owner items), SOLR-12651 and SOLR-16437 (READY TO OPEN), SOLR-13239 (submission-held), SOLR-17281 (parked; its receipt says do not draft it).

## Deliverable

`reports/census-followup-solrcloud-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold, plus every refresh applied and the SOLR-17292 reconcile record.
