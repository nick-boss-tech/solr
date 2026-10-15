# Pool assignment: Schema convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-schema-convergence-round-2.md.

## Purpose

Round 1 (`reports/schema-convergence-round-1.md`) returned NOT CONVERGED: 3 blocking, 25 should-fix and 10 note findings across the eight drafts in `pr-drafts/schema-analysis/`. The main side has applied the holding findings and written its adjudication in `reports/schema-convergence-round-1-response.md`: all 3 blocking applied, 15 should-fix applied to drafts, 8 held branch-side (recorded verbatim in the response), 10 notes standing, nothing rebutted, and one disclosed deviation (SCH-R1-29). Six drafts are CONVERGED on text. This round re-checks the two drafts still NOT CONVERGED, the SOLR-15357 held item, the branch-side items' state, and the SCH-R1-29 deviation.

## Scope

- SOLR-14199: NOT CONVERGED (held in part). Its count confirmation and SchemaVersionSpecificBehaviorTest run ride `gates/SOLR-14199-record.md`; the two owed Limits lines are named in `receipts/SOLR-14199.md`.
- SOLR-15358: NOT CONVERGED. Its Proof placeholder rides `gates/SOLR-15358-proof-record.md`.
- SOLR-15357: CONVERGED on text at the current head, with its internal hold block standing (branch fix and rerun, changelog title, owner decision 2).
- Branch-side items SCH-R1-30 and SCH-R1-37 (SOLR-15945), SCH-R1-31 (SOLR-16977), SCH-R1-32 and SCH-R1-33 (SOLR-17047), SCH-R1-34, SCH-R1-35 and SCH-R1-36 (SOLR-18134): state checks only; those drafts are CONVERGED on text and are not re-audited.
- SOLR-9349 is CONVERGED with no held item and is out of scope.
- Settled decisions recorded in `reports/schema-analysis-round-1.md` are not reopened. Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. For SOLR-14199 and SOLR-15358, read the live drafts and confirm the response's dispositions of every round 1 finding against them (SCH-R1-05 and SCH-R1-06 applied for 14199; SCH-R1-14 and SCH-R1-15 applied for 15358). Flag any disposition the live text does not match.
2. SOLR-14199: check the state of `gates/SOLR-14199-record.md`. If the record run has landed since the response, judge the draft's [CONFIRM COUNT BEFORE POSTING] and [HOLD] markers and the two owed Limits lines against the run's record and the refreshed receipt; if it has not, confirm both markers still stand and the item stays open.
3. SOLR-15358: check the state of `gates/SOLR-15358-proof-record.md`. If the record run has landed, judge the Proof placeholder against the run's recorded base result (the two test names and the failing assertion or error); if it has not, confirm the placeholder still stands and no text was forced.
4. SOLR-15357: check whether the branch fix commit from its internal hold block (the test-name fix) has landed. If it has, check the draft's Proof and links against the new head; if the tip is unchanged, confirm the hold block still stands.
5. The eight branch-side items: check whether the branch commits have landed. If one has, check the fragment title, XML comment, javadoc, upgrade note, filters.adoc line or test comment against the replacement recorded verbatim in the response. If none has landed, record the state and move on.
6. SCH-R1-29 deviation: the response applied the register's bullet text as the bullet's first two sentences and retained the bullet's two schema-version sentences after them. Judge whether the retained sentences state a limit the register's summary does not, and whether the combined bullet reads consistently. Record the judgement as a finding disposition, not a new audit.
7. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (SCH-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs SCH-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/schema-convergence-round-2.md`, with:

- Counts up front: drafts and items re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED, and a state line for each branch-side item.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
