# Pool assignment: SolrJ convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-solrj-convergence-round-2.md.

## Purpose

Round 1 (`reports/solrj-convergence-round-1.md`) returned NOT CONVERGED: 71 findings (5 blocking, 38 should-fix, 28 notes) against 19 drafts. The main side has applied the holding findings and written its adjudication in `reports/solrj-convergence-round-1-response.md`. Seventeen drafts are converged on their text; SOLR-10364 and SOLR-11356 are not, because three blocking findings wait on packaged heads that were local to vm2 when round 1 ran, and one of them also waits on a branch change. This round is a confirmation round scoped to those two drafts, one disclosed deviation, and the packaging title landing checks.

## Scope

- The two drafts still NOT CONVERGED: SOLR-10364 and SOLR-11356, in `pr-drafts/solrj/`.
- The SJ-R1-53 deviation on SOLR-11356 (item 3).
- The five packaging title landing checks (item 4).
- The 17 converged drafts are out of scope except for item 5; do not re-audit them.
- Gate state is settled by the receipt in `receipts/` at the exact live tip, read together with the response's fact checks.

## What to check

1. For every finding listed against an in-scope draft in the round 1 register, read the live draft and confirm the response's disposition: applied (the replacement text is present, word for word in substance), held (the item waits on a state named in the response), or standing (notes the response leaves). Flag any disposition the live text does not match.
2. The packaged heads: read the live tips with `git ls-remote`. SOLR-10364's packaged head was `efb1e6717f9f` (local to vm2; live tip then `502bdbf033fa`); SOLR-11356's was `6120dae28d04a07763f44d4bc5e9d9793af025f6` (local to vm2; live tip then `8474e5a3a26d`). If a packaged head (or a successor carrying the packaging commit) has been pushed, re-check that draft against the pushed head: every head citation and the Proof head. If a tip is unchanged, confirm the hold still stands and record the state.
3. SOLR-11356, SJ-R1-49 and SJ-R1-53: for SJ-R1-49, check whether the branch now copies the headers in the stream key; if it does, check the draft against the register's post-fix texts recorded in the response, and check the SJ-R1-51 Limits summary for the revision the response marks. For SJ-R1-53, judge the deviation: the response applied "the base commit the branch starts from" where the register prescribed "the merge-base with upstream/main", on the evidence in receipts/SOLR-11356.md (the receipt records `cabedd1d968` is not on main's history and `git merge-base` against origin/main returns `e044bf20b405`). Mark the deviation CONFIRMED or DISPUTED with the evidence.
4. Packaging titles: check the changelog fragment titles at the live tips of the SOLR-3999, SOLR-4335, SOLR-4336, SOLR-6046 and SOLR-8536 branches against the replacement texts recorded verbatim in the response. If one has landed, check it word for word; if not, record the state.
5. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions, the SOLR-17866 owner flag) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (SJ-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs SJ-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/solrj-convergence-round-2.md`, with:

- Counts up front: drafts re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
