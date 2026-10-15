# Pool assignment: CLI convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-cli-convergence-round-2.md.

## Purpose

Round 1 (`reports/cli-convergence-round-1.md`) returned NOT CONVERGED: 1 blocking, 15 should-fix and 29 note findings across the nine drafts in `pr-drafts/cli/`. The main side has applied the holding findings and written its adjudication in `reports/cli-convergence-round-1-response.md`: 11 should-fix applied to drafts, 4 held branch-side, the blocking item CLI-R1-04 adopted under the decision default and held for branch work, 29 notes standing, nothing rebutted. Eight drafts are CONVERGED on text and are out of scope except where a branch-side title item below names one. This round re-checks only SOLR-10667 (still NOT CONVERGED) and the state of the branch-side title items.

## Scope

- SOLR-10667: CLI-R1-04 (the adopted `solrclient/**` exclusion: branch change, head move, re-gate, draft rewrite) and CLI-R1-05 (branch-side changelog title, rides the same branch work).
- CLI-R1-03 (SOLR-9342), CLI-R1-14 (SOLR-16272), CLI-R1-17 (SOLR-16813): branch-side changelog title state checks only; the drafts themselves are CONVERGED on text and are not re-audited.
- Settled decisions recorded in `reports/cli-bin-packaging-round-1.md` are not reopened, including decision 10's adoption recorded in the response. Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. SOLR-10667: read the live draft and confirm the OWNER NOTE hold line is present in the form the response records, and that the copy-all Change text is still the pre-adoption text (it is rewritten only after the branch work). Then check the branch state: if the exclusion commit has landed on `solr-10667-submit`, check whether a re-gate receipt exists at the new head and whether the draft has been rewritten against it; judge the draft against whichever state you find and report it. If the tip is unchanged, confirm CLI-R1-04 and CLI-R1-05 still stand.
2. The three title items: check whether the changelog fragment commits have landed on the SOLR-9342, SOLR-16272 and SOLR-16813 branches. If one has, check the fragment title against the replacement recorded in the response and that the draft's changelog link points at the new head. If none has landed, record the state and move on.
3. For SOLR-10667, verify the response's dispositions of CLI-R1-06 and CLI-R1-07 against the live draft (applied text present, word for word in substance). Flag any disposition the live text does not match.
4. Fresh eyes, narrowly: a claim in the SOLR-10667 draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions, the other eight drafts) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (CLI-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs CLI-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/cli-convergence-round-2.md`, with:

- Counts up front: items re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict for SOLR-10667: CONVERGED or NOT CONVERGED, and a state line for each of CLI-R1-03, CLI-R1-14 and CLI-R1-17.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
