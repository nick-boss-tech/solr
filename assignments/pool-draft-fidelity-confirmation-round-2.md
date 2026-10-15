# Pool assignment: draft fidelity confirmation round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-draft-fidelity-confirmation-round-2.md.

## Scope

Round 1 (`reports/draft-fidelity-confirmation-round.md`) found DRIFT in seven drafts. The main side applied six of the seven fixes and left one draft unchanged with a stated reason. The record of the fixes, and of the one non-application, is `reports/draft-fidelity-confirmation-round-response.md`. Read that file in full before starting.

This round re-checks the seven drafts in place, and nothing else:

- `pr-drafts/metrics/SOLR-17987.md`
- `pr-drafts/search-components/SOLR-12556.md`
- `pr-drafts/edismax/SOLR-4362.md`
- `pr-drafts/build-docs/SOLR-16914.md`
- `pr-drafts/cli/SOLR-17029.md`
- `pr-drafts/security/SOLR-18368.md`
- `pr-drafts/search-components/SOLR-9396.md`

For each draft: read the live text at the passages the response names, and check (a) the applied text is present as the response records it, (b) the surrounding text still reads coherently, with no orphaned reference, no duplicated claim, and no section summary contradicting its section, and (c) no claim in the checked passages exceeds what the cited receipt, settlement or source states.

Three checks have a specific ground, stated here so the round does not repeat round 1's one error:

- SOLR-16914 was not changed. Its "What happens today" passage describes the state before the change, so check it against the base `14c7aac0d151` and the branch diff against that base, not against the post-change text at the head.
- SOLR-12556: check the qualified bold summary against `receipts/SOLR-12556.md` line 9 and the draft's own body and Choice. The gated changelog title states the change without the qualification; the response records why the draft now carries it anyway, and changelog narrowing is a packaging item, not part of this check.
- SOLR-17987: check the environment variable name against the guide text and the mapping at the head, not against the rename recorded as branch work.

## Deliverable

reports/draft-fidelity-confirmation-round-2.md: one verdict per draft, CONFIRMED or DRIFT, with the exact passage and the record it disagrees with for any DRIFT. A draft that was left unchanged counts as CONFIRMED only if the response's stated reason holds against the base record named above. Do not edit any draft, receipt or report; flag instead.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
