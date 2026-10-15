# Pool assignment: SolrCloud convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-solrcloud-convergence-round-2.md.

## Scope: the 12 drafts not converged in round 1, two rebutted findings, and two limited re-checks

Round 1 (`reports/solrcloud-convergence-round-1.md`) returned NOT CONVERGED: 12 of the 18 drafts not converged, with one blocking finding (SC-R1-21), 17 should-fix findings and 26 notes. The main side has applied the round; the record is `reports/solrcloud-convergence-round-1-response.md`. This round re-checks only the 12 drafts that were open:

SOLR-9155, 11479, 12651, 14919, 15035, 15106, 15386, 15863, 16437, 17292, 17680, 17733.

For each draft: verify every disposition in the response file against the live draft text in `pr-drafts/solrcloud/`, and give a verdict of CONVERGED or NOT CONVERGED for that draft. Start with SOLR-15106 (the blocking item) and SOLR-15035 and SOLR-16437 (the two findings the main side decided against the register; see below). Do not re-open the other converged drafts, and do not re-open settled decisions in `material/solrcloud-round-1-answers.md` or in the round 1 response, except where this assignment names an item to judge.

## Two items to judge on the evidence

- **SC-R1-28 (SOLR-15035): rebutted, not applied.** The round prescribed deleting "The ticket summary describes this difference." because no Jira packet for the ticket is on disk. The main side instead confirmed the summary against live Jira on 2026-10-11: "core.properties different when using ADDREPLICA .vs. when the replica created with CREATE". Judge the sentence as it stands against that summary. If it still disagrees, name the exact passage and what the summary would have to say for the sentence to stand.
- **SC-R1-51 (SOLR-16437): optional rewording declined.** The round offered "other APIs that take the same inputs" for the Limits summary's "the other APIs the ticket mentions". The main side read the ticket's closing sentence live from Jira: "Maybe we should update this (and other APIs) to do a best-effort sanity-check of input parameters before shipping them off to the asynchronous work queue." Judge whether the draft's phrase overstates the ticket against that sentence.

## Limited re-checks on two converged drafts

- **SOLR-13186:** only the SC-R1-30 date form (its Proof now reads "recorded 2026-10-06"). The response records the set-wide pick: dates taken from a ledger or log record read "recorded"; the answers' per-draft forms govern. Check the pick was applied as recorded, here and in SOLR-9155, 15106, 15386, 15035 and 15863, and that no draft the answers prescribe as "verified" was changed.
- **SOLR-15674:** only SC-R1-47 (the cache-check wording) and SC-R1-48 (the landing-order sentence), plus the paired version-reset wording the answers require to stay identical: "A new znode starts that version over at 0." must stand in this draft and in `pr-drafts/configsets/SOLR-15478.md`. The 15478 draft itself stays out of scope for the verdict.

## Also in scope

- The two answers corrections the response applied (SC-R1-50, SC-R1-52, in `material/solrcloud-round-1-answers.md`): check the corrected SOLR-16437 entry against receipts/SOLR-16437.md and the merge-base code the correction cites.
- Any finding round 1 graded as a note and the response dispositioned as a branch-side record (SC-R1-04, 05, 33, 35, 36, 49) is settled for this loop; flag it again only if the response misstates the record it rests on.

## Deliverable

`reports/solrcloud-convergence-round-2.md`: verdict first (CONVERGED or NOT CONVERGED overall), then one line per draft (converged, or the exact passage and the evidence it disagrees with), the two judged items with their outcomes, and the two limited re-checks. New findings, if any, are graded blocking, should-fix or note, with exact replacement text for anything the main side should apply. A finding already on the round 1 register keeps its SC-R1 ID; new findings take SC-R2 IDs.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on drafts, receipts, answers and branches; the only writes are the claim, the report, and heartbeat updates. No em dashes in authored text.
