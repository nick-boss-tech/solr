# Open work

This file lists the pool work that is open, claimed, or unconfirmed on this branch. It is generated from the Status line under the title of each file in `assignments/` and `gates/` (last generated 2026-10-18, from coordination tip 54a59669d6240bc5a1729180025a20c8739e9343). The SOLR-16673 assignment and gates below were added after that snapshot. If this file and a job file's own Status line ever disagree, the job file's Status line governs, and this file is regenerated from the lines at the next census or stamp pass.

Counts at generation: 6 open assignments, 2 open gate jobs, 13 claimed files, 5 unconfirmed files. The old finding aid `assignments/pool-shared-gate-backlog-buffer.md` is superseded by this file.

## Open assignments

### Intended host: Windows (review agent)

- `assignments/pool-solr-4841-langid-followup-round-1.md`: Assignment: build the SOLR-4841 follow-up test (language detection through a custom identifier), round 1

### Intended host: Windows (review coordinator), or the main side for items marked owner

- `assignments/pool-vm2-open-decisions-round-1.md`: Pool assignment: open decisions from the vm2 queue, round 1

### Intended host: vm1 (vm2 can take it if vm1 is busy)

- `assignments/pool-solr-10305-nouk-distributed-probe-round-1.md`: Assignment: SOLR-10305 distributed requests without a uniqueKey, probe and server-side check, round 1

### Intended host: either Linux host (vm1 or vm2); Windows never claims build work

- `assignments/pool-edismax-patches-apply-round-1.md`: Assignment: eDisMax draft patches P1 to P6, application round 1
- `assignments/pool-solr-13059-fix-round-1.md`: Assignment: SOLR-13059 test fix, round 1
- `assignments/pool-solr-8009-routed-noslice-test-round-1.md`: Assignment: SOLR-8009 routed no-slice test, round 1
- `assignments/pool-solr-16673-remaining-issues-round-1.md`: Pool assignment: SOLR-16673 remaining Schema Designer issues, round 1. Fixes and tests are pushed; both Linux gates remain open.

## Open gate jobs

### Intended host: any capable Linux host

- `gates/SOLR-16673-uniquekey.md`: Gate job: SOLR-16673 numeric uniqueKey response
- `gates/SOLR-16673-rootfield.md`: Gate job: SOLR-16673 nested root field type

## Claimed work

- `assignments/pool-merged-pr-followup-promises-round-1.md`: claim `claims/pool-merged-pr-followup-promises-round-1.md`. Pool assignment: audit of merged PR descriptions for follow-up promises, round 1
- `assignments/pool-retire-round-final-reports-round-1.md`: claim `claims/pool-retire-round-final-reports-round-1.md`. Pool assignment: retire candidates, final evidence packs, round 1
- `assignments/pool-strong-review-round-2-unblocked-slices.md`: claim `claims/pool-strong-review-round-2-unblocked-slices.md`. Pool assignment: strong review round 2, the unblocked slices
- `assignments/pool-vm1-solr18119-review-fixes-round-1.md`: claim `claims/pool-vm1-solr18119-review-fixes-round-1.md`. Pool assignment: SOLR-18119 review findings follow-up
- `assignments/pool-vm2-finish-list-round-1.md`: claim `claims/pool-vm2-finish-list-round-1-a.md`. Assignment: vm2 finish list, round 1
- `assignments/pool-vm2-open-work-list-round-1.md`: claim `claims/pool-vm2-open-work-list-round-1.md`. Assignment: vm2 open-work list, round 1
- `assignments/solr-13696-fix-project.md`: claim `claims/solr-13696-fix-project.md`. Assignment: SOLR-13696 fix project (repair the re-enabled routed-alias tests)
- `gates/SOLR-10667-dotfiles-cause.md`: claim `claims/pool-vm2-gate-backlog-10667-cause.md`. Gate job: SOLR-10667 example `.gitignore` in the distribution (cause trace, then the fix decision from gates/SOLR-10667-dotfiles.md)
- `gates/SOLR-15712-udvas-test-fix.md`: claim `claims/pool-vm2-gate-backlog-15712.md`. Fix for the SOLR-15712 version gate: corrected TestCollationFieldUDVAS
- `gates/SOLR-15712-udvas-version-gate.md`: claim `claims/pool-vm2-gate-backlog-15712.md`. Gate job: SOLR-15712 version-gated useDocValuesAsStored refusal (ICU rule on the core CollationField), with the changelog title to match
- `gates/SOLR-16887-errorfile-bats-name.md`: claim `claims/pool-vm2-gate-backlog-16887.md`. Gate job: SOLR-16887 restore -XX:ErrorFile, rename the BATS test, then the gate and BATS run at the new head
- `gates/SOLR-17055-sort-path.md`: claim `claims/pool-vm2-gate-backlog-17055-fix.md`. Gate job: SOLR-17055 sort path (premise run first, then fix and gate)
- `gates/SOLR-8628-packaging.md`: claim `claims/pool-vm2-gate-backlog-8628-packaging.md`. Gate job: SOLR-8628, packaging and top-up gate

## Unconfirmed

These files could not be settled from the records at the census. Each carries `Status: UNCONFIRMED`; the census note for it is:

- `assignments/pool-admin-ui-premise-runs.md`: its claim declares itself PAUSED and not a live claim, and the deliverable is absent
- `assignments/update-processing-round-3-closeout.md`: deliverable on the branch, but the addendum claim carries no DONE line and records a held draft
- `gates/SOLR-12505-fetchstream-fix-amendment-1.md`: the directed re-run executed NOT GREEN and amendment 2 ruled on it, but the checklist is unticked and the head never landed
- `gates/SOLR-18541-score-only-exit-input.md`: the claim is marked DONE, but the job file has no result section and no receipt records the answers
- `gates/pr-drift-round-1-run-records.md`: the file's two status lines conflict; items (b), (c) and (d) have final classifications, item (a) owes a main-agent decision
