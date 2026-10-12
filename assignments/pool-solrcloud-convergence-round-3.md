# Pool assignment: SolrCloud convergence, round 3

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-solrcloud-convergence-round-3.md.

## Scope: the three drafts still open after round 2, and the round 2 record corrections

Round 2 (`reports/solrcloud-convergence-round-2.md`) returned NOT CONVERGED: nine of the twelve re-checked drafts converge, and three do not (SOLR-15035, 16437 and 17680, one should-fix item each: SC-R1-31, SC-R2-21, SC-R2-22). The main side has applied the round; the record is `reports/solrcloud-convergence-round-2-response.md`. This round re-checks only the three open drafts:

SOLR-15035, SOLR-16437, SOLR-17680.

For each draft: verify every disposition in the round 2 response against the live draft text in `pr-drafts/solrcloud/`, and give a verdict of CONVERGED or NOT CONVERGED for that draft. Do not re-open the other fifteen drafts, and do not re-raise the settled items: SC-R1-28 (round 2 confirmed the sentence stands), SC-R1-51 (settled by applying SC-R2-21), SC-R1-21 (closed), the SC-R1-30 date-form pick, or the length acceptances recorded in the round 1 and round 2 responses, except where this assignment names an item to check.

## Record corrections to check

- **Answers gate lists (SC-R2-02):** `material/solrcloud-round-1-answers.md` now lists SOLR-11479 (`360dca60fcfa`) and SOLR-12651 (`f3131d1ee846`) as gated green at the live tip, with the correction noted. Check both against receipts/SOLR-11479.md and receipts/SOLR-12651.md, and that neither ticket still appears in the older-head or no-gate lists.
- **Answers SOLR-16437 bullet (SC-R2-21 record note):** the ADOPTED bullet now says "the other APIs the ticket's closing sentence suggests", with a dated correction note. Check it against the ticket's closing sentence as quoted in the round 1 response and against the corrected draft Limits line.
- **SOLR-15478 landing order (SC-R2-26):** `pr-drafts/configsets/SOLR-15478.md` gained the Limits bullet "This branch is meant to land after SOLR-15674." Check the sentence is present, that it matches the landing order the answers adopt (SOLR-15674 first), and that the paired version-reset wording ("A new znode starts that version over at 0.") still stands identical in both drafts. The 15478 draft otherwise stays out of scope for the verdict.
- **Round 1 response corrections (SC-R2-01, SC-R2-25):** the round 2 response corrects two statements in `reports/solrcloud-convergence-round-1-response.md` (title recoverability via `git show 0e287a38159`, and three-named versus four-in-history drafts). Check the corrections against the branch history; the underlying decision (no title lines in drafts) is settled and is not re-opened.

## Deliverable

`reports/solrcloud-convergence-round-3.md`: verdict first (CONVERGED or NOT CONVERGED overall), then one line per draft (converged, or the exact passage and the evidence it disagrees with), and the four record checks with their outcomes. New findings, if any, are graded blocking, should-fix or note, with exact replacement text for anything the main side should apply. A finding already on an earlier register keeps its ID (SC-R1 or SC-R2); new findings take SC-R3 IDs.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on drafts, receipts, answers and branches; the only writes are the claim, the report, and heartbeat updates. No em dashes in authored text.
