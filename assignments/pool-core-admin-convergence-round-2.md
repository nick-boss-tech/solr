# Pool assignment: Core admin convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-core-admin-convergence-round-2.md.

## Purpose

Round 1 (`reports/core-admin-convergence-round-1.md`) returned NOT CONVERGED: 18 of the 21 drafts were NOT CONVERGED, with 7 blocking, 28 should-fix and 29 note findings. The main side has applied the holding findings and written its adjudication in `reports/core-admin-convergence-round-1-response.md`. This round is a confirmation round: it re-checks only the drafts round 1 marked NOT CONVERGED, verifies each disposition in the response against the live draft text, and settles the items whose state may have moved since.

## Scope

- The 18 drafts round 1 marked NOT CONVERGED: SOLR-4502, 5011, 6438, 8576, 9750, 12007, 12916, 13246, 15024, 15805, 16499, 16725, 16849, 17297, 17377, 17708, 17731, 18010, all in `pr-drafts/core-admin/`.
- The three drafts round 1 marked CONVERGED (SOLR-4989, 8275, 11939) are out of scope; do not re-audit them.
- Settled decisions recorded in `material/core-admin-round-1-answers.md` are not reopened. Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. For every finding listed against an in-scope draft in the round 1 register, read the live draft and confirm the response's disposition: applied (the replacement text is present, word for word in substance), branch-side (the item is recorded in the response with its replacement text), or standing (notes the response leaves). Flag any disposition the live text does not match.
2. SOLR-8576, CA-R1-24 (the one rebutted, not-applied should-fix): re-check the state. Read the live tip of `solr-8576-submit` with `git ls-remote origin`. If the alias fix has landed and `CollectionsAPISolrJTest` has run at a new head, judge the draft against that head; if the tip is unchanged, confirm the [OWED BEFORE POSTING] marker still stands and the item stays open.
3. Branch-side items CA-R1-01 (SOLR-4502) and CA-R1-34 (SOLR-12916): check whether the changelog fragment commits have landed on those branches. If one has, check the fragment title against the replacement recorded in the response and, for SOLR-12916, that the draft's changelog link points at the new head. If neither has landed, record the state and move on.
4. SOLR-5011: check the draft's text against `reports/solr-5011-shared-schema-analysis.md` and its receipt only. The draft's hold is the main side's pending the narrowing in `gates/SOLR-5011-narrowing.md`; the hold itself is not a finding, and the narrowing's state is reported, not judged.
5. SOLR-17708: confirm the draft now names the gated live tip `2eceb5473037` everywhere a head is cited, except the Limits route link, which the response points at the base commit under CA-R1-83.
6. SOLR-17731: if `gates/SOLR-17731.md` has landed since the response, the head and links move with it; check the draft against the receipt at the live tip in that case, and report which state you found.
7. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (CA-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs CA-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/core-admin-convergence-round-2.md`, with:

- Counts up front: drafts re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
