# Pool assignment: open review fixes lane, round 1

Capability tags: `draft` (edits), `review` (verification). Intended host: windows. Staffing: 2, split by slice. Claim path: claims/pool-review-open-fixes-lane-round-1.md (slice-level claims by slice are fine).

## Purpose

Several review rounds closed NOT CONVERGED or STILL OPEN, and their replacement text has not landed on the drafts. The review coordinator verifies that fix lanes land. This lane applies the mechanical replacements those rounds specify, and records each one as applied, already present, or excluded. It is wording and citation work only. It does not reopen any finding or decision.

## Sources

Each item below names the record that specifies the replacement. Read the record for the exact text.

- Convergence round 3, `reports/update-processing-convergence-round-3.md`: the should-fix replacements for SOLR-12705, 7022, 14718, 13265, 16673, 6973, 16655, 3657, 5065, 5941 and 7504.
- Draft fidelity round 2, `reports/draft-fidelity-confirmation-round-2.md`: SOLR-18368 (line 1 lead-in) and SOLR-9396 (Choice, lines 26 and 28).
- Gradlew ticket round 3, `reports/gradlew-ticket-review-round3.md`: `material/gradlew-ticket-draft.md` line 5 (the source-note date) and line 51 (the argument loop claim).
- CombinedQueryComponent ticket round 2, `reports/combinedquerycomponent-ticket-review-round2.md`: `material/combinedquerycomponent-ticket-draft.md` line 48 (the M1 and M2 phrase), and the label on lines 20 and 56, and the removal of the causal clause on line 54.

## Slice F1: the update-processing drafts (`pr-drafts/update-processing/`)

Apply, in each named draft, the should-fix replacements from the convergence round 3 report, with these exclusions:

- **Exclude SOLR-6973 line 17.** The blocking behavior statement needs the owner's confirmation. Leave the line as it is.
- **Exclude every length item** (SOLR-5941, SOLR-7504, SOLR-6973). Length is a main-side decision.
- **Exclude SOLR-5941 line 41** (the `CommitThroughNonLeaderTest` identity). The main side confirms it from the fix gate log first.
- **Exclude SOLR-16655 line 25 and SOLR-12705 line 28.** Each names a record the main side must confirm first (the pre-fix overlay, and the combined tree's SOLR-12705 head).
- **Exclude SOLR-12705 line 39** (the SOLR-7504 pointer). It applies only if the main side agrees.

Every other should-fix replacement in the convergence round 3 report for these drafts is applied.

## Slice F2: the other drafts

- `pr-drafts/security/SOLR-18368.md` line 1: change "decides whether a PR opens and under which key" to "decides under which key the PR opens", per draft fidelity round 2.
- `pr-drafts/search-components/SOLR-9396.md` lines 26 and 28: name both spellings, `$row.field` and `${row.field}`, per draft fidelity round 2.
- `material/gradlew-ticket-draft.md` line 5: drop the date "executed 2026-10-11".
- `material/gradlew-ticket-draft.md` line 51: replace the loop claim with the text in the gradlew round 3 report.
- `material/combinedquerycomponent-ticket-draft.md` line 48: change the M1 and M2 phrase to "whose question is posed in Fix options".
- `material/combinedquerycomponent-ticket-draft.md` lines 20 and 56: add "(from code reading, not run)" to the real-exit sentences. Do not otherwise change them.
- `material/combinedquerycomponent-ticket-draft.md` line 54: delete the clause "which is why this was not visible".
- **Exclude** the Symptom bullet 3, fix option 3 and line 66 scoping, and the line 68 overlap wording. Those need the main side's wording.

## Method

For each replacement, first read the draft's current passage. If the quoted text is already gone or already the replacement, record it as already present. Otherwise apply it exactly, then re-read the passage and confirm the replacement is present and the surrounding text still reads coherently. Do not change any other text.

## Deliverable

`reports/review-open-fixes-lane-round-1.md`, with the counts up front (applied, already present, excluded), and one line per replacement naming the draft, the line and the outcome. Excluded items are listed with the reason. Commit the edited drafts and the report in the same push as the DONE claim.

## Limits

No PR, comment, live PR body, branch or receipt edit. No build, test or gate. Nothing is posted or filed. Commit as Nick Shanin with no trailers and no em dashes. No PR numbers in file names or commit messages.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
