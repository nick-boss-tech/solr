# Open work

This file lists the pool work that is open, claimed, or unconfirmed on this branch. It is generated from the Status line under the title of each file in `assignments/` and `gates/`, per the census of 2026-10-15 (generated 2026-10-15, from pr-prepare tip cac628fdb26b). If this file and a job file's own Status line ever disagree, the job file's Status line governs, and this file is regenerated from the lines at the next census or stamp pass.

Counts at generation: 7 open assignments, 22 open gate jobs, 12 claimed files, 5 unconfirmed files. The old finding aid `assignments/pool-shared-gate-backlog-buffer.md` is superseded by this file.

## Open assignments

### Intended host: either Linux host (vm1 or vm2); Windows never claims build work

- `assignments/pool-edismax-patches-apply-round-1.md`: Assignment: eDisMax draft patches P1 to P6, application round 1 (tags: implementation, gate)
- `assignments/pool-solr-11475-proof-fix-round-1.md`: Assignment: SOLR-11475 proof fix, round 1 (tags: implementation, gate)
- `assignments/pool-solr-13059-diagnosis-round-1.md`: Assignment: SOLR-13059 diagnosis, round 1 (tags: gate, premise-run)
- `assignments/pool-solr-8009-routed-noslice-test-round-1.md`: Assignment: SOLR-8009 routed no-slice test, round 1 (tags: implementation, gate)

### Intended host: Windows (review agent)

- `assignments/pool-solr-4841-langid-followup-round-1.md`: Assignment: build the SOLR-4841 follow-up test (language detection through a custom identifier), round 1 (tags: implementation, review)

### Intended host: vm1

- `assignments/pool-vm1-solr-8240-head-failure-round-1.md`: Assignment: SOLR-8240 head failure, round 1 (tags: implementation, gate)

### Intended host: Windows (review coordinator), or the main side for items marked owner

- `assignments/pool-vm2-open-decisions-round-1.md`: Pool assignment: open decisions from the vm2 queue, round 1 (tags: review)

## Open gate jobs

### Intended host: not stated

- `gates/SOLR-10627-sec-r1-02-title.md`: Gate job: SOLR-10627 SEC-R1-02 changelog title (landing and parse) (pinned head: 5a15dc0ba220)
  - Attachment: `gates/SOLR-10627-sec-r1-02-title.patch` (a patch file; it inherits this job's status and is not stamped separately)

### Intended host: any capable Linux host

- `gates/SOLR-10667-dotfiles-cause.md`: Gate job: SOLR-10667 example `.gitignore` in the distribution (cause trace, then the fix decision from gates/SOLR-10667-dotfiles.md) (pinned head: 9ce9ef1db5e9)
- `gates/SOLR-12245-delta-gate.md`: Gate job: SOLR-12245 delta review of the live head's new commit, the coordinator log check, and a gate at the live head (pinned head: f325d5d0576e)
- `gates/SOLR-12705-live-head-gate.md`: Gate job: SOLR-12705 gate at the live head, with the counter-level check the audit names (pinned head: aa56b7b1be4c)
- `gates/SOLR-13943-stacked-run.md`: Gate job: SOLR-13943 recorded normal-mode run of the moved class on the stacked tree (pinned head: ca443f6f6806)
- `gates/SOLR-16655-live-head-gate.md`: Gate job: SOLR-16655 gate at the live head (decided descent fix) (pinned head: 5e2317443f41)
- `gates/SOLR-16725-branch-edits-topup-gate.md`: Gate job: SOLR-16725 recorded branch edits and top-up gate at the moved head (pinned head: be1838ef8ccf)
- `gates/SOLR-5065-live-head-gate.md`: Gate job: SOLR-5065 gate at the live head, with the suggester pin test's fail-before verdict (pinned head: ab894a996c8a)
- `gates/SOLR-6045-live-head-gate.md`: Gate job: SOLR-6045 gate at the live head (decided factory fix and post-gate commits) (pinned head: 04208ba7f0b4)
- `gates/SOLR-6065-cloud-test-gate.md`: Gate job: SOLR-6065 gate at the live head, with the cloud test in the run (pinned head: 3d2cec9e1ab3)
- `gates/SOLR-7504-live-head-gate.md`: Gate job: SOLR-7504 gate at the live head (every-value scan and null guard) (pinned head: 2fe06bfd917f)
- `gates/SOLR-7520-tidy-fix.md`: Gate job: SOLR-7520 tidy fix (formatting-only commit owed at a6dc792b36) (pinned head: a6dc792b365b)

### Intended host: vm2 only

- `gates/SOLR-11678-sec-r1-07-keystore-reading.md`: Reading job: SOLR-11678 SEC-R1-07, keystore type of the standalone premise run (pinned head: )

### Intended host: a Linux host

- `gates/SOLR-12651-title.md`: Gate job: SOLR-12651 changelog title correction, title stated inline (pinned head: de3f3604f0ee)
- `gates/SOLR-16290-fix.md`: Gate job: SOLR-16290 real facet fix (funded by standing calls round 1, item 4) (pinned head: ff760120c817)

### Intended host: either Linux host

- `gates/SOLR-15106-claim-verification.md`: Premise job: SOLR-15106 claim verification, the 1200 queued messages figure (pinned head: 40b7e5d0efa7)
- `gates/SOLR-5941-getbool-fix.md`: Gate job: SOLR-5941 third getBool site fix (audit finding 3) (pinned head: a4df7bfd214b)
- `gates/SOLR-8088-tidy-fix.md`: SOLR-8088 tidy fix and re-gate (pinned head: 0f473402de03)

### Intended host: vm2

- `gates/SOLR-17733-title-narrowing-round-1-gate.md`: Gate job: SOLR-17733 changelog title narrowing (round 1) (pinned head: 636196b7954a)
  - Attachment: `gates/SOLR-17733-title-narrowing-round-1.patch` (a patch file; it inherits this job's status and is not stamped separately)
- `gates/SOLR-7520.md`: Gate job: SOLR-7520 (distributed grouping completes the post filter on shards) (pinned head: a6dc792b365b)
- `gates/SOLR-8003.md`: Gate job: SOLR-8003 (fl glob before a raw value transformer) (pinned head: 85a8d3233939)

### Intended host: a Linux host with the Gradle toolchain

- `gates/SOLR-4754-text-fixes-and-premise.md`: Gate job: SOLR-4754 text fixes and premise run (pinned head: e4ea4bfde80e)

## Claimed work

- `assignments/pool-merged-pr-followup-promises-round-1.md`: claim `claims/pool-merged-pr-followup-promises-round-1.md`, heartbeat 2026-10-15T18:50:00Z. Pool assignment: audit of merged PR descriptions for follow-up promises, round 1
- `assignments/pool-retire-round-final-reports-round-1.md`: claim `claims/pool-retire-round-final-reports-round-1.md`, heartbeat 2026-10-15T18:50:00Z. Pool assignment: retire candidates, final evidence packs, round 1
- `assignments/pool-strong-review-round-2-unblocked-slices.md`: claim `claims/pool-strong-review-round-2-unblocked-slices.md`, heartbeat 2026-10-15T18:50:00Z. Pool assignment: strong review round 2, the unblocked slices
- `assignments/pool-vm2-finish-list-round-1.md`: claim `claims/pool-vm2-finish-list-round-1-a.md`, heartbeat 2026-10-14T23:26Z. Assignment: vm2 finish list, round 1
- `assignments/solr-13696-fix-project.md`: claim `claims/solr-13696-fix-project.md`, heartbeat 2026-10-08. Assignment: SOLR-13696 fix project (repair the re-enabled routed-alias tests)
- `gates/SOLR-15712-udvas-test-fix.md`: claim `claims/pool-vm2-gate-backlog-15712.md`, heartbeat 2026-10-14. Fix for the SOLR-15712 version gate: corrected TestCollationFieldUDVAS
- `gates/SOLR-15712-udvas-version-gate.md`: claim `claims/pool-vm2-gate-backlog-15712.md`, heartbeat 2026-10-14. Gate job: SOLR-15712 version-gated useDocValuesAsStored refusal (ICU rule on the core CollationField), with the changelog title to match
- `gates/SOLR-16887-errorfile-bats-name.md`: claim `claims/pool-vm2-gate-backlog-16887.md`, heartbeat 2026-10-14. Gate job: SOLR-16887 restore -XX:ErrorFile, rename the BATS test, then the gate and BATS run at the new head
- `gates/SOLR-17055-sort-path.md`: claim `claims/pool-vm2-gate-backlog-17055-fix.md`, heartbeat 2026-10-15. Gate job: SOLR-17055 sort path (premise run first, then fix and gate)
- `gates/SOLR-7550.md`: claim `claims/pool-vm2-gate-backlog-7550.md`, heartbeat 2026-10-15. Gate job: SOLR-7550 (peer sync, core initialization failure rule)
- `gates/SOLR-8628-packaging.md`: claim `claims/pool-vm2-gate-backlog-8628-packaging.md`, heartbeat 2026-10-14. Gate job: SOLR-8628, packaging and top-up gate
- `gates/SOLR-8954-fullsolrcloud-rtg.md`: claim `claims/pool-vm2-gate-backlog-8954.md`, heartbeat 2026-10-14. Run job: SOLR-8954 measured run of the existing composite-router RTG test (premise-run)

## Unconfirmed

These files could not be settled from the records at the census. Each carries `Status: UNCONFIRMED`; the census note for it is:

- `assignments/pool-admin-ui-premise-runs.md`: its claim declares itself PAUSED and not a live claim, and the deliverable is absent
- `assignments/update-processing-round-3-closeout.md`: deliverable on the branch, but the addendum claim carries no DONE line and records a held draft
- `gates/SOLR-12505-fetchstream-fix-amendment-1.md`: the directed re-run executed NOT GREEN and amendment 2 ruled on it, but the checklist is unticked and the head never landed
- `gates/SOLR-18541-score-only-exit-input.md`: the claim is marked DONE, but the job file has no result section and no receipt records the answers
- `gates/pr-drift-round-1-run-records.md`: the file's two status lines conflict; items (b), (c) and (d) have final classifications, item (a) owes a main-agent decision
