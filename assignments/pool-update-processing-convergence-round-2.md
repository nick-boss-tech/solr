# Pool assignment: update processing convergence, round 2

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-update-processing-convergence-round-2.md.

## Purpose

Round 1 (`reports/update-processing-convergence-round-1.md`) returned NOT CONVERGED: 105 findings across the 28 drafts in `pr-drafts/update-processing/` (7 blocking, 71 should-fix, 27 notes). The main side has applied the blocking and should-fix replacements and recorded every disposition in `reports/update-processing-convergence-round-1-response.md`, including the three decision reconciliations (UPC-R1-103, UPC-R1-104, UPC-R1-232) and the notes it applied as plain corrections (UPC-R1-11, UPC-R1-216, UPC-R1-222, UPC-R1-227(d)). Round 2 verifies that application and re-checks the drafts, so the loop can close.

## Order of work

Re-check these drafts first; they are the ones that changed since their last review before round 1:

1. Changed after round 3: SOLR-5754, SOLR-5939, SOLR-5941, SOLR-6045, SOLR-6065, SOLR-7504.
2. Changed on 2026-10-10: SOLR-12705, SOLR-13943.

Then re-check the remaining drafts the round 1 application changed: SOLR-11475, SOLR-11483, SOLR-12245, SOLR-12703, SOLR-12864, SOLR-13265, SOLR-14262, SOLR-14718, SOLR-16356, SOLR-16655, SOLR-16673, SOLR-16910, SOLR-3657, SOLR-4841, SOLR-5065, SOLR-5505, SOLR-5887, SOLR-6973, SOLR-7022. SOLR-13696 stays gate-claim only, as in round 1.

## Method for every draft

1. Read the draft in full, in its current form.
2. For each round 1 finding on that draft, check the disposition in the response file against the draft as it now stands: the replacement text is present, it sits where the finding specified, and it did not disturb the surrounding claims. A disposition that does not match the draft is a carried finding under its UPC-R1 ID.
3. Check the reconciled items read consistently in the final text: SOLR-14718 (UPC-R1-103 and UPC-R1-104), SOLR-7022 (UPC-R1-232 and its Limits consequence), SOLR-5941 (UPC-R1-214).
4. Re-run the round 1 checks on the draft: every number in Proof and Limits against the receipt, the addendum or the material the draft names, at the head it names; every code citation at the named SHA (head SHA for code the change produces, merge-base for pre-change symptom code, stated in the text); the draft against `pr-formula.md` (bold one-line summary opening each section, plain language, no internal process vocabulary); Limits against the audit's open findings.
5. Grade anything found: blocking, should-fix or note. Carried findings keep their UPC-R1 ID; new findings get UPC-R2-nn IDs. Give the exact replacement text for every blocking and should-fix finding.

## Deliverable

`reports/update-processing-convergence-round-2.md`, with:

- Counts up front: drafts checked, drafts converged, drafts not converged by grade.
- A verdict per draft: CONVERGED or NOT CONVERGED. A draft is CONVERGED only if every round 1 finding on it is verified applied (or its recorded disposition accepted) and this pass raises no new blocking or should-fix finding on it.
- The register of carried and new findings, each with the draft, the grade, the exact text at issue, the evidence, and the exact replacement text where the grade requires one.
- An overall convergence line for the category.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. Settled decisions recorded in `material/update-processing-*.md` and in the round 1 response are not reopened; if a draft now contradicts one, flag it and quote the decision. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.
