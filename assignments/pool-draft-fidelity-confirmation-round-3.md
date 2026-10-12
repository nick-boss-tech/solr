# Pool assignment: draft fidelity confirmation round 3

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-draft-fidelity-confirmation-round-3.md.

## Scope

Round 2 (`reports/draft-fidelity-confirmation-round-2.md`) confirmed five of seven drafts and found DRIFT in two. Both fixes are applied; the record of the fixes is `reports/draft-fidelity-confirmation-round-2-response.md`. Read that file in full before starting.

This round re-checks the two fixed drafts in place, and nothing else:

- `pr-drafts/security/SOLR-18368.md`
- `pr-drafts/search-components/SOLR-9396.md`

For each draft: read the live text at the passages the response names, and check (a) the applied text is present as the response records it, (b) the surrounding text still reads coherently, with no orphaned reference, no duplicated claim, and no section summary contradicting its section, and (c) no claim in the checked passages exceeds what the cited receipt or source states.

Two grounds, stated here so the round checks the right record:

- SOLR-18368: check the banner lead-in at line 1 against the owner-call paragraph in `receipts/SOLR-18368.md` (line 9). The banner's options (a) and (b) stand as written and are not part of this check; the flag about option (a) is already recorded in the response and is not re-opened.
- SOLR-9396: check the Choice passages (lines 26 and 28) against the head pattern at `a5ab2eda4e6` (`SubQueryAugmenterFactory.java` line 307) and `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52, which name both spellings. The Limits bullets are unchanged and already name both.

## Deliverable

reports/draft-fidelity-confirmation-round-3.md: one verdict per draft, CONFIRMED or DRIFT, with the exact passage and the record it disagrees with for any DRIFT. Do not edit any draft, receipt or report; flag instead.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
