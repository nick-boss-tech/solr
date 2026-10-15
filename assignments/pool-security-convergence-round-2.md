# Pool assignment: Security convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-security-convergence-round-2.md.

## Purpose

Round 1 (`reports/security-convergence-round-1.md`) returned NOT CONVERGED: 4 blocking, 5 should-fix and 3 note findings across five drafts. The main side has applied the holding findings it could and written its adjudication in `reports/security-convergence-round-1-response.md`. Three blocking findings and one should-fix wait on packaged heads that were local to vm2 when round 1 ran. This round is a confirmation round: it re-checks only the drafts still NOT CONVERGED, verifies each disposition in the response against the live draft text, and settles the held items against the state at the time of the round.

## Scope

- The two drafts still NOT CONVERGED: SOLR-11678 and SOLR-12161, in `pr-drafts/security/`.
- The SOLR-10627 changelog fragment title landing check in item 3 below.
- The three converged drafts (SOLR-10627 text, SOLR-13705, SOLR-18368) are out of scope except for item 4; do not re-audit them.
- Gate state is settled by the receipt in `receipts/` at the exact live tip, read together with the response's fact checks.

## What to check

1. For every finding listed against an in-scope draft in the round 1 register, read the live draft and confirm the response's disposition: applied (the replacement text is present, word for word in substance), held (the item waits on a state named in the response), or standing (notes the response leaves). Flag any disposition the live text does not match.
2. The packaged heads: read the live tips with `git ls-remote`. SOLR-11678's packaged head was `611eae2464536fdae61b8cae32abac97ee3a453b` (local to vm2; live tip then `55d8cd189d15367b6896f0ea07570d3395ce404b`); SOLR-12161's was `cdd265168a2a` (live tip then `1725cbd8489889a1dad75279e4b2c11b0ec93d66`). If a packaged head (or a successor carrying the packaging commit) has been pushed, re-check that draft against the pushed head: every head citation, the Proof head, and for SOLR-11678 also SEC-R1-07 (the standalone run's keystore type, from the gate log named in receipts/SOLR-11678.md) and SEC-R1-08 (the gate date at the pushed head). If a tip is unchanged, confirm the hold still stands and record the state.
3. SOLR-11678, SEC-R1-05: check whether the branch now adds the `SOLR_SSL_KEY_MANAGER_PASSWORD` mapping in `EnvToSyspropMappings.properties` with a test assertion. If it has, check SEC-R1-04's replacement text, recorded verbatim in the response, against the new branch state. SOLR-10627: check whether the changelog fragment title on the branch has been replaced with the text recorded in the response; if it has not, record the state.
4. SOLR-12161 and SOLR-18368: confirm the applied should-fix replacements (SEC-R1-10, SEC-R1-11) are present in the live drafts, word for word in substance.
5. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (SEC-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs SEC-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/security-convergence-round-2.md`, with:

- Counts up front: drafts re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
