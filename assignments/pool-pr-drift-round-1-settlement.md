# Pool assignment: PR drift round 1 settlement

Status: DONE

Capability tags: `review`, `public-edit` (authorised by the owner for our own PR descriptions and titles). Intended host: windows. Staffing: up to 3 subagents. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

reports/pr-drift-round-1.md corrected fifteen open PR descriptions and listed the rest as owner decisions. Since that report, the owner has ruled on the items below, and the coordinator authorisation for our own PR descriptions and titles covers applying them. Under the patch-loop directive of 2026-10-12, the review side settles these items itself: it applies each edit, reads the result back, and records the outcome in the deliverable, instead of sending the items back to the main side.

## Item 1: module-check wording on four bodies

The drift round flagged the module-check sentence on four bodies: the live text prints a specific check command, and the receipt on pr-prepare phrases the step as "module check passes" without printing the command. The four:

- PR #4968, SOLR-18178. Receipt: receipts/SOLR-18178.md, gate log g4968r3-gate.log.
- PR #5002, SOLR-11431. Receipt: receipts/SOLR-11431.md, gate log g11431r29-gate.log.
- PR #5003, SOLR-7394. Receipt: receipts/SOLR-7394.md, gate logs g7394r29-gate.log and g7394r29-gate2.log.
- PR #5004, SOLR-16130. Receipt: receipts/SOLR-16130.md, gate log g16130r30-gate.log.

For each PR: pull the live body, find the module-check sentence, and check the ticket's receipt and, where the receipt names a gate log that is available, that log.

- Where the record shows the exact command the body prints, keep the sentence unchanged and record the item as KEPT, citing the record that shows the command.
- Where the record does not show that command, reword the sentence to what the record supports, using the receipt's own phrasing for the step, by targeted replacement of that sentence only. Read the edited body back and record the item as REWORDED, quoting the before and after text exactly.

Change nothing else in these four bodies in this item.

## Item 2: three title replacements with recorded recommendations

Each title below carries a recorded recommendation in the named report. Before applying any of them, verify the proposed title against the branch's actual change (the diff at the PR head and the ticket's receipt). A title that does not match the change is not applied; record it as NOT APPLIED with the reason.

1. PR #4996, SOLR-18280. Proposed title, from reports/replication-backup-round-1-g4.md item 7: "SOLR-18280: Fix `TestReplicationHandler.testUrlAllowList` failures". The current title calls the failures flaky; the ticket record describes reproducible failures with seeds that reproduce reliably, and the branch is a test-only fix for that test.
2. PR #5003, SOLR-7394. Proposed title, from reports/solrcloud-round-1-p6.md item 12: "SOLR-7394: Clear the recovering shard-terms entry and restore the pre-recovery term when recovery is abandoned". The current title and the changelog omit the term restore; the body and the receipt describe it (the term saved at recovery start is written back instead of 0).
3. PR #5090, SOLR-14262. Proposed title, from reports/review-opened-28-g6.md item 1: "SOLR-14262: A commit that is skipped because the update log is not ACTIVE now reports commitIgnored in the response header instead of looking successful." This is the branch's changelog title text with the ticket key in front of it. For this PR, also remove the planned cloud follow-up its body carries: the recorded owner ruling is that SOLR-14262 carries no cloud follow-up, so the Limits sentence that offers or plans one comes out. Nothing replaces it.

For each applied title, read the PR back after the edit and confirm the live title matches the proposed title exactly. Record the item as TITLE APPLIED with the read-back result. For SOLR-14262, the report also quotes the follow-up sentence that was removed.

## Rules

- Titles and descriptions of our own PRs only, under the coordinator authorisation. No head changes, no force pushes, no commit-message rewrites, no comments, no review replies, no closes.
- Every edit is a targeted replacement of the named sentence or title. No text outside the named items changes.
- Read back every edited PR (title and body as applicable) and confirm it matches what was sent before recording the item as settled.
- Public text follows the standing rules: plain language, no internal process vocabulary, no "we" for the owner plus agent, no em dashes.
- If a record needed for an item cannot be found, or the live text differs from what the drift report describes, do not improvise: record the item as NOT APPLIED with what was found.

## Out of scope

- The SOLR-13706 PR's optional title idea (PR #5015 in the drift report): deferred as a named follow-up. Do not apply it in this round.
- The SOLR-18506 PR's squash-message note (PR #5029 in the drift report): a merge-time matter. No action.
- The drift report's run-record items (claims that need a run recorded before they can stand) and the SOLR-18391 changelog wording on PR #5027: both are posted as gate jobs and are settled there, not here.
- The SOLR-18368 alias-delete wording question, if it appears in any record read for this round: that PR is not open yet. No action.
- Every other owner decision listed in reports/pr-drift-round-1.md stays open. This round settles only items 1 and 2 above.

## Deliverable

reports/pr-drift-round-1-settlement.md, with one entry per item:

- Item 1, per PR: KEPT with the record cited, REWORDED with the before and after text quoted exactly, or NOT APPLIED with the reason.
- Item 2, per PR: TITLE APPLIED with the read-back result, or NOT APPLIED with the reason. For SOLR-14262, the removed follow-up sentence is quoted.
- A closing line stating which of the seven items are settled and which remain open.
