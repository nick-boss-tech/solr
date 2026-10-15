# Pool assignment: draft fidelity review, Suggester drafts

Status: DONE

Capability tags: `review`, `draft`. Intended host: windows. Staffing: 1. Claim path: claims/pool-draft-fidelity-suggester.md.

Purpose: give all eight Suggester drafts an independent fidelity pass against the live branch tips, the receipt evidence for each ticket, and the ratified round 4 decisions. Fix wording and citation drift directly in the drafts. Do not silently change a claim, a number, a date, a head, or a decision: flag those in the report for the main side.

## Scope

- `pr-drafts/suggester/SOLR-9227.md`
- `pr-drafts/suggester/SOLR-9968.md`
- `pr-drafts/suggester/SOLR-10937.md`
- `pr-drafts/suggester/SOLR-11844.md`
- `pr-drafts/suggester/SOLR-14171.md`
- `pr-drafts/suggester/SOLR-17215.md`
- `pr-drafts/suggester/SOLR-17393.md`
- `pr-drafts/suggester/SOLR-9637.md`

Decision record: `material/suggester-round-4-decisions.md` and the answers it ratifies in `material/suggester-round-4-answers.md`. Receipt evidence: use `receipts/<ticket>.md` where a receipt file is present; for this category, also use the receipt sections and later receipt corrections recorded in `material/suggester-round-4-answers.md` and `material/suggester-draft-round-answers.md`, because those files carry gate evidence that is not repeated in a separate receipt file for every ticket. If the records disagree, do not pick one silently: flag the conflict in the report.

## Checks for every draft

1. Verify the head named in the draft against the live fork tip with `git ls-remote`. Verify the branch and stacking base where the draft names one.
2. Compare every Proof claim, test count, skip count, verification date, and proof limitation with the receipt evidence. A number or date that the evidence does not support is flagged, not edited into a different number or date.
3. Check every citation anchor against the code at the named SHA. Code the change produces links the head SHA. A pre-change symptom links the merge-base or other recorded base for that draft and is described as such. The changelog line links the head SHA.
4. Compare the title with the branch changelog fragment at the named head.
5. Compare What happens today, What this change does, any Choice, and Limits with the round 4 decisions. In particular, check that a decision recorded as a Choice remains a Choice, a decision recorded as a Limit remains in Limits, and a named follow-up is stated as a plan rather than as work already done.
6. Check the formula presentation: AI header and footer, a bold one-line summary opening each section, plain language, and no internal process vocabulary in public draft text.
7. Fix directly only where the correct wording or citation follows mechanically from the evidence and does not alter the substance: a wrong line anchor, a stale link form, wording that is broader than its cited code, or internal vocabulary with an equivalent plain statement. Record every direct edit in the report with the file, the old text, the replacement text, and the supporting evidence.

## SOLR-9637 stacking check

SOLR-9637 follows SOLR-17393. Its draft must state that relationship accurately in the change description and in Proof.

Check the stacking base named in the SOLR-9637 draft against the current SOLR-17393 record and live tip. The ratified decision is that SOLR-9637 opens as its own PR after SOLR-17393 lands, rebased onto the landed SOLR-17393 tip. The draft must not imply that it opens independently or before that landing. Its Proof must also distinguish evidence run on the stacked base from evidence on a later rebased tree: if the proof ran against the stacked base and no proof on the rebased tree is recorded, the draft must say so plainly.

A changed stacking SHA, a changed proof base, or a statement that the rebase has already happened is a claim or number change. Flag it in the report rather than rewriting it directly unless the live tip and recorded receipt evidence make the correction exact and unambiguous; even then, list the edit and its evidence in the report.

## Deliverable and limits

Deliverable: `reports/draft-fidelity-suggester.md`, with one verdict per draft (CONSISTENT, FIXED, or FLAGGED), every direct edit listed, and every flagged claim, number, date, head, record conflict, or decision issue stated with the exact text at issue and the evidence needed or found.

The eight draft files above are the only branch files this assignment may edit. Do not edit receipts, material, reports other than the deliverable, branch code, or live PR text. No builds or test runs.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverables). No em dashes in authored text.
