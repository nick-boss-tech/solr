# Pool assignment: Streaming convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-streaming-convergence-round-2.md.

## Purpose

Round 1 (`reports/streaming-convergence-round-1.md`) returned NOT CONVERGED: 3 blocking, 12 should-fix and 12 note findings across six drafts. The main side has applied the holding findings and written its adjudication, including one rebuttal, in `reports/streaming-convergence-round-1-response.md`. Five drafts are converged on their text; SOLR-12505 is not, because ST-R1-23 is a branch-side precondition. This round is a confirmation round scoped to the open and rebutted items and the branch-side titles.

## Scope

- SOLR-12505 (ST-R1-23), in `pr-drafts/streaming/`.
- The ST-R1-27 rebuttal on SOLR-12657.
- The branch-side changelog titles for SOLR-10882 (ST-R1-08) and SOLR-14231 (ST-R1-33).
- The other converged drafts (SOLR-9852, SOLR-10322) are out of scope except for item 5; do not re-audit them.
- Gate state is settled by the receipt in `receipts/` at the exact live tip, read together with the response's fact checks.

## What to check

1. SOLR-12505, ST-R1-23: read the live tip of `solr-12505-submit` with `git ls-remote` and check `FetchStream` at that head. If the branch now escapes or validates the `on=` field name, with a test, judge the draft against the new head, including whether its citations and Proof still describe the branch. If the tip is unchanged (head `5f20e1171bc890509d76e4f5f7defcc4fc8c102c`, line 239 appending the field name unescaped), confirm the item stays open and the draft stays NOT CONVERGED.
2. The ST-R1-27 rebuttal: receipts/SOLR-12657.md records the re-gate log line `GATE RUNNER DONE 2026-10-11T04:04:02Z`. The response rebuts the finding on the ground that this is 2026-10-10 22:04 MDT and the settled date convention C1 keeps local MDT dates in PR bodies. Judge the evidence and the convention, and mark the item CONFIRMED (rebuttal accepted) or STILL OPEN with the reason.
3. Branch-side titles: check the changelog fragment titles at the live tips of `solr-10882-submit` and `solr-14231-submit` against the replacement texts recorded verbatim in the response. If one has landed, check it word for word; if not, record the state.
4. SOLR-12505's applied items (ST-R1-21, 22, 24) and SOLR-12657's applied item (ST-R1-26): confirm the replacement text is present in the live drafts, word for word in substance.
5. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions, including the ST-R1-12 rebase-before-opening call and the ST-R1-35 length call recorded in the response) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (ST-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs ST-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/streaming-convergence-round-2.md`, with:

- Counts up front: items re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
