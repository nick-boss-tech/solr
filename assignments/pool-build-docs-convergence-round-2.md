# Pool assignment: Build/docs convergence, round 2

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-build-docs-convergence-round-2.md.

## Purpose

Round 1 (`reports/build-docs-convergence-round-1.md`) returned NOT CONVERGED: 2 blocking, 23 should-fix and 11 note findings across the eight drafts in `pr-drafts/build-docs/`. The main side has applied the holding findings and written its adjudication in `reports/build-docs-convergence-round-1-response.md`: 22 findings applied, 3 held (two branch-side, one owner item), 11 notes standing, nothing rebutted. Five drafts are CONVERGED on text (SOLR-3684, 16322, 16914, 17252, 17842) and are out of scope. This round re-checks only the three drafts still NOT CONVERGED and the state of their held items.

## Scope

- SOLR-5821: BD-R1-09 (branch-side guide wording commit).
- SOLR-6430: BD-R1-13 (branch-side changelog fragment drop).
- SOLR-17752: BD-R1-31 (owner item: the branch base decision).
- The five CONVERGED drafts are out of scope; do not re-audit them.
- Settled decisions recorded in `reports/build-docs-misc-round-1.md` are not reopened. Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. For each in-scope draft, read the live draft and confirm the response's disposition of every round 1 finding against it: applied (the replacement text is present, word for word in substance), held (the item is recorded in the response with its state), or standing (notes). Flag any disposition the live text does not match.
2. SOLR-5821, BD-R1-09: check whether the guide wording commit has landed on the branch (`git ls-remote origin` for the live tip, then the branch diff). If it has, check the `DistributedTest` invariant bullet's third item against the replacement recorded in the response, and check the draft's Proof and Limits against the new head. If it has not, confirm the item still stands and say so.
3. SOLR-6430, BD-R1-13: check whether the branch commit dropping `changelog/unreleased/SOLR-6430.yml` has landed. If it has, confirm the draft's changelog line is gone and the changelog-lint sentence is re-verified at the new head. If it has not, confirm the item still stands.
4. SOLR-17752, BD-R1-31: check whether the owner's base decision has been recorded since the response (the draft's WAITING note, the receipt, or `reports/build-docs-misc-round-1.md`). If the branch was rebased onto `14c7aac0d151` or the PR is to open against merge-base `56ec140e3636`, judge the draft's Proof base label against that state and report which state you found. If nothing has moved, confirm the item still stands as an owner item.
5. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (BD-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs BD-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/build-docs-convergence-round-2.md`, with:

- Counts up front: drafts re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
