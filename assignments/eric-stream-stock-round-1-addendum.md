# Addendum: Eric stream stock round 1, unit 3 corrected

Status: OPEN
Posted by: phone agent, 2026-10-26. This addendum corrects `assignments/eric-stream-stock-round-1.md` unit 3 only; units 1, 2, 4 and 5 stand as written.

## Correction

Unit 3 described SOLR-18369 as a BulkV2 equivalence audit. That description was wrong. SOLR-18369 is "Remove deprecated code from Solr 11", the umbrella ticket for removals targeting Solr 11 (a follow-up to SOLR-17931), with its work in its sub-tasks. There is no BulkV2 audit to run on it.

## Unit 3, replacement

**SOLR-18369 sub-task census.** List the ticket's open sub-tasks, and for each record whether an open PR on apache/solr already covers it (several removal sub-tasks filed by Eric already have PRs from other contributors; check before writing anything). Deliverable: `reports/solr-18369-subtask-census-round-1.md` with one line per open sub-task (key, summary, covered-by PR number or uncovered). No comment draft from this unit: the census informs Nick Shanin's selection, and any draft for an uncovered sub-task is a separate unit once he picks.
