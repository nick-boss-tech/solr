# WON'T DO: backlog-first-100 unit 2 (closure-fixed), vm2

- Refused by: vm2, per WORKFLOW.md WON'T DO (structurally unable to finish).
- Refused item: `assignments/backlog-first-100-pack-slices-round-1.md`, Unit 2 (`closure-fixed`, records 1 to 25). Parent rule 1 requires each closure-fixed draft to cite a fixing version or commit from the store record, and the draft to name it.
- Store evidence (board tip read 2026-10-27): the latest H1 batch revisions hold 78 `closure-fixed` records. None of them carries a fixing version or commit citation (citation fields are csv, skip, branch and audit-draft-branch only).
- Live Jira evidence (anonymous REST read, 2026-10-27 about 08:50Z): 13 of 13 probed `closure-fixed` keys are status Open, resolution none, fixVersions empty: SOLR-1658, SOLR-2274, SOLR-2435, SOLR-2913, SOLR-2936, SOLR-2976, SOLR-3408, SOLR-3693, SOLR-3694, SOLR-3697, SOLR-3739, SOLR-3740, SOLR-3741.
- Consequence: no record can meet rule 1, and the live tickets do not show a fix. The family file `drafts/jira-comments/closure-fixed.md` already states the family is not fillable under the current records.
- Not done: no fill, no draft, no git-log fix search per ticket (that would be new scope, not the unit as written).
- Decision for the owner, not vm2: either the closure-fixed disposition is wrong for these records (they look like open tickets, not fixed ones), or the unit needs a new fix-finding step with its own rules. Recorded as a question, not a claim.
- Tags considered: draft, records, sweep. Rung used: 2 (probe), per material/decision-ladder.md.
