# Unit 1 report: closure-not-a-bug, records 1 to 25 (vm5, round 1, 2026-10-26)

Status: PARTIAL. No drafts filled. The unit cannot deliver 25 drafts as the family template is written. Decision needed from the owner (rung 5).

## What was screened

Source: latest H1 revision per batch (`material/harmonized-backlog/batches/h1-*.json`, 16 batches). Disposition `closure-not-a-bug`: 127 records. Excluded by pack rule 2: 2 conflict-flagged. Remaining eligible: 125. No record is Critical or Blocker; all are Bug type, Major priority.

Basis per record, from the audit evidence in `queue-current.json` (archive branch `archive/coordination-2026-10-23`, `material/skip-audit/`):

| Basis | Count | Example |
|---|---|---|
| Audit-3 Tier 1 pass: ticket text only, and the audit says "needs maintainer call" | 105 | SOLR-1557 ("Basis is ticket text, not a fresh code repro") |
| Code or ruling cited in the audit | about 11 | SOLR-1780 (`AddUpdateCommand.extractIdsIfNeeded` on upstream/main; "by design per Hoss") |
| Other: a reading from the ticket or a Hoss comment | 19 | SOLR-5376 (FC/FCS downgrade "by design") |

## The conflict

The family template says: "I looked at this against current main," and it requires `CODE_OR_DOC_CITATION` ("Not fillable without it"). Pack rule 1 says the basis may be "ticket text or a maintainer's ruling". Those two disagree. For 105 records the audit did not look at current main and said the call was still open, so the template's first-person claim would be false.

## What is fillable now

About 11 records have a code or ruling basis that the template can cite honestly, and some of the 19 "other" have a Hoss ruling. Those are not drafted yet and not live-checked. The unit would need 25 from this pool, so the shortfall is about 14 even with all of them.

## Decision for the owner

Choose one:
1. Keep the template as written. Then Unit 1 has about 11 to 20 fillable drafts and the rest must be dropped from the pack.
2. Change the template for the ticket-text basis: a closure that says "closing as not a bug on the ticket text and the existing maintainer discussion, with no fresh code check." Then all 125 are fillable after a live check. That changes what the comment says, so it's the owner's call.

## Not done

- No drafts filled, no live Jira checks run for this unit.
- Unit 2 (`closure-fixed`) not screened.
- Nothing posted. No Jira action.
