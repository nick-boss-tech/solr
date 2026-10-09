# Claim: update processing, round 3 close-out

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/update-processing-round-3-closeout.md` (commit `49bc048b3e9`), per `material/update-processing-round-3-closeout.md`, `pr-formula.md`, and the rules in the assignment. Output: drafts under `pr-drafts/update-processing/SOLR-<ticket>.md`, plus `reports/update-processing-round-3-closeout.md` with one row per item.

Items and heads (live fork tips matched these heads when the claim was made):
- SOLR-7022 at `db357868610b`: released. Proof cites the gate at `6a233ab2fdb` per material item 1.
- SOLR-16655 at `5e2317443f`: states the landing order (lands first), material item 2.
- SOLR-12705 at `8624b7c3238b`: states the landing order (rebases onto SOLR-16655), material item 2.
- SOLR-12245 at `f325d5d057`: names `testDistribErrorMessageNamesTheHostOnce` as the fail-before test, material item 3.
- SOLR-11475 at `0de48e492fd`: PeerSync count wording per material item 7.
- SOLR-13696 at `1d0b8a0a73cd`: new draft from material item 9 (gate r7 GREEN), with both folded-in fixes posed as Choices.
- SOLR-6045, SOLR-16673, SOLR-5941: verify the drafts against material items 4, 5, 6. Edit only where a draft contradicts the material.

Waiting, not held:
- SOLR-13943: its draft joins when a further addendum names the stacked head and its gate receipt (material item 10). Not drafted in this close-out.

Source note: the gate logs and the receipts ledger named in the material (for example `g7022r1-gate.log`, `g13696-r7-gate.log`, `test-receipts-63-branches.md`) are main-side records. They are not in this drafting workspace. The drafts rely on the material's record of them, as the round 3 receipts addendum did. The report says so.

Split:
- Subagent 1: SOLR-7022, SOLR-16655, SOLR-12705, SOLR-12245, SOLR-11475.
- Subagent 2: SOLR-13696 (new draft), then verify SOLR-6045, SOLR-16673, SOLR-5941.
- Lead agent: claim commit, head checks, the report, the commits, and the push. Subagents write draft files only.

Not in scope: opening or commenting on pull requests, edits to any `solr-*-submit` branch or live pull request description, builds or test runs, SOLR-13943, SOLR-9637, SOLR-18505 (closed in round 1; its description edit is not authorized and is not applied), and the suggester round 4 assignment (`assignments/suggester-round-4.md`), which is a separate assignment and is not started here.
