# Assignment: SolrJ draft confirmations and one draft owed, census follow-up round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- For each ticket, check the draft against its receipt at the live fork tip and the SolrJ convergence records; apply the fix the check calls for and re-check until a SATISFIED verdict comes back. Write the one owed draft and put it through the same loop.
- Drafts follow `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes).
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Draft confirmations (draft exists under `pr-drafts/solrj/`, branch GATE GREEN at its live tip per its receipt, no convergence verdict on record in the round 1 or round 2 responses):

SOLR-2018, 3722, 4422, 4424, 5220, 7709, 12094, 14187, 14967.

Draft owed:

- SOLR-3498: the packaged head `2ca0b3743427` was pushed by vm2 on 2026-10-13 and is the live tip; its receipt records the gate at that head. Write the draft from the receipt per `pr-formula.md`.

Starting records: `reports/solrj-convergence-round-1-response.md`, `reports/solrj-convergence-round-2-response.md`, `receipts/SOLR-<ticket>.md` per ticket, and the census rows in `reports/openings-census-round-1.md`.

Exclusions: SOLR-11356 (gate job posted for its header-copy item: `gates/SOLR-11356-sj-r1-49-header-copy.md`), SOLR-17866 (owner item: the submit-ahead-of-design flag), and the READY TO OPEN set (SOLR-3999, 4335, 4336, 6046, 8536, 10364, 14298, 18341; their branch title commits are opening-time packaging under W4, not this round).

## Deliverable

`reports/census-followup-solrj-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold, plus every fix applied and the SOLR-3498 draft path.
