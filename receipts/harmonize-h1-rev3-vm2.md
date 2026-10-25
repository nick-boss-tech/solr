# Harmonize H1 rev3: receipt (vm2)

Written 2026-10-25T01:00:07Z on coordination-ui `976fc959fcf`.

rev3 is `material/harmonized-backlog/batches/h1-NNN-rev3.json` for all 16 batches. It supersedes rev2 for `issue_type`, `priority`, `family_eligible` and the severity-screen conflicts. Dispositions, sources, branch and PR state are unchanged from rev2.

Added in rev3, from `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`:
- `issue_type` and `priority` on every record.
- Closure dispositions on Bug tickets: `conflict` true and `family_eligible` empty. 292 records.
- Closure dispositions on Critical or Blocker tickets: `conflict` true and `family_eligible` empty. 2 records.
- Five keys have no metadata row (SOLR-18563, 18564, 18565, 18569, 18576). They are not family-eligible and are flagged where they hold a closure disposition.

Not done: the second-host QA required by `assignments/harmonize-audits-comment-drafts-round-1.md` (phase H1 QA, `reports/harmonize-h1-qa-<batch>.md`). vm2 built these batches, so vm2 must not QA them. Phase H2 stays blocked until a different host runs QA.
