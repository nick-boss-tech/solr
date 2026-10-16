# Assignment: vm2 open-work list, round 1

Status: OPEN
Capability tags: [coordination, gate, implementation, premise-run, reading]. Intended host: vm2. Posted by the main side, 2026-10-15.

## Occasion and how open work is found

vm2 reported that there are no further assignments for it. The pool record does not agree: the gate jobs in section A and the assignments in section B are open and claimable by vm2 today. Open work on this branch is found in `OPEN-WORK.md` at the branch root, which is generated from the `Status:` line under each file's title in `assignments/` and `gates/`. A file whose Status line reads OPEN is unclaimed and claimable. The Intended host line does not narrow that: wording such as "a Linux host", "any capable Linux host", or no host line at all includes vm2. Only a file whose Status line reads CLAIMED or DONE, or one named in section D below, is not available.

## The ask

Claim this assignment under `claims/` per WORKFLOW.md, then work the list. For each item: file its claim under `claims/`, set the item's own Status line to CLAIMED naming the claim file in the same push, and follow the item's own steps. Order is the vm2 coordinator's call; interleaving one long item (the SOLR-16290 fix, the eDisMax application) with the short gates keeps the gate capacity busy. If an item turns out to be blocked, record the blocker in the item's own file and move to the next one; hand an item back only in its own file, with the reason stated. Section C lists work that is already vm2's under live claims, so nothing there is claimed twice. This assignment is DONE when every section A and B item is finished or handed back with a stated reason; record the outcome in the claim as the list drains.

## Section A: open gate jobs

1. `gates/SOLR-16290-fix.md` — implementation plus gate. Branch solr-16290-submit at ff760120c817. The funded real facet fix from the standing calls settlement: the branch today is a test-only pin, and the pin test at that head is the fix's regression test. Scope, acceptance, and the exclusions composition question are stated in the job; the fix adds the changelog fragment because the branch is test-only today.
2. `gates/SOLR-12651-title.md` — small. Branch solr-12651-submit at de3f3604f0ee. A title-only commit setting the changelog fragment title to the exact text stated inline in the job, then the light gate the job names (parse with byte equality of the parsed title, tidy guard, Error Prone compile, the named cloud test counted from fresh JUnit XML, module check; no proof re-run, with the reason stated in the job). Plain push on green and a receipt refresh.
3. `gates/SOLR-4754-text-fixes-and-premise.md` — Branch solr-4754-submit at e4ea4bfde80e, already GATE GREEN at this head with a compile-level proof shape. Text and message fixes plus a premise run, per the job's steps.
4. `gates/SOLR-10667-dotfiles-cause.md` — diagnosis. Branch solr-10667-submit at 9ce9ef1db5e9, GATE GREEN at this head. The premise is confirmed (the tracked example `.gitignore` is absent from the assembled distribution); this job traces which layer drops it, using the discriminator stated in the job (inspect the module packaging output before the distribution copy). No fix in this job; the fix decision follows from the companion record the job names.
5. `gates/SOLR-13943-stacked-run.md` — run only. Branch solr-13943-submit at ca443f6f6806, stacked on the SOLR-13696 repair. A recorded normal-mode run of the moved class: no awaitsfix flag, no push and no rebase under this job. An unreliable run records that the branch waits on SOLR-13059, per the job.
6. `gates/SOLR-16725-branch-edits-topup-gate.md` — Branch solr-16725-submit at be1838ef8ccf, GATE GREEN at this head per its receipt. The recorded branch edits and the top-up gate at the moved head, per the job's steps.
7. `gates/SOLR-10627-sec-r1-02-title.md` — small. Branch solr-10627-submit, gated head 5a15dc0ba220. Land the changelog title carried by the attached patch (`gates/SOLR-10627-sec-r1-02-title.patch`, which inherits this job's status) and verify the parse, per the job's owed steps.
8. `gates/SOLR-11678-sec-r1-07-keystore-reading.md` — reading job, no build and no head pinned. Establish the keystore type of the standalone premise run from the record and write the answer where the job directs.
9. `gates/SOLR-15106-claim-verification.md` — premise verification. Branch solr-15106-submit at 40b7e5d0efa7. Establish the actual message count OverseerProcessorExitTest sends, against the figure the draft attributes to the ticket. Outcomes may be verified, corrected, or unverified with a reason. No branch code changes are authorized under this job.

## Section B: open assignments for either Linux host

1. `assignments/pool-edismax-patches-apply-round-1.md` — implementation plus gate. Apply the six eDisMax draft patches P1 to P6 from the final review report in order, push fast-forward per patch, post each patch's named gate job, remove the authoring trailers on the three SOLR-12092 commits as part of P5, and remove the review flag comment the assignment names.
2. `assignments/pool-solr-11475-proof-fix-round-1.md` — implementation plus gate. Branch solr-11475-submit at 229947201fd7. Repair the fail-before proof so it ends in an assertion instead of an OutOfMemoryError (a bounded loop or a time limit in the test; a bigger heap is not a repair), then the full gate. The sign-mismatch readiness blocker is out of scope for the assignment and is not erased by a green gate.
3. `assignments/pool-solr-13059-diagnosis-round-1.md` — diagnosis only. Repeated seeded runs of TimeRoutedAliasUpdateProcessorTest on the SOLR-13696 head, classify the failures, no code.
4. `assignments/pool-solr-8009-routed-noslice-test-round-1.md` — implementation plus gate. Write the missing routed no-slice real-time-get test for the SOLR-8009 guard and prove it discriminates. The branch is already gate green at e600375aaee3; this test fills the guard's own case, which the open PR's Limits already name as a gap.

## Section C: already vm2's under live claims (do not re-claim)

- `gates/SOLR-5941-getbool-fix.md` — the predicate amendment landed in the job file on 2026-10-15 and discharges the scope stop; resume and finish under claims/pool-vm2-gate-backlog-5941.md.
- `gates/SOLR-7520.md` — full gate at the tidy-fixed head 15cc9ba5a63c, claimed 2026-10-15.
- `gates/SOLR-12245-delta-gate.md` — claimed 2026-10-15.
- `gates/SOLR-8088-tidy-fix.md` — claimed under claims/pool-vm2-gate-backlog-8088-tidy.md.
- `gates/SOLR-8628-packaging.md` and `assignments/pool-vm2-finish-list-round-1.md` — the packaging push and the finish list's remaining sections, including the SOLR-17055 proof rerun and its two follow-up tests.
- `gates/SOLR-15712-udvas-version-gate.md` with `gates/SOLR-15712-udvas-test-fix.md`, `gates/SOLR-16887-errorfile-bats-name.md` (steps 4 to 6), `gates/SOLR-17055-sort-path.md`, `gates/SOLR-8954-fullsolrcloud-rtg.md` — claimed 2026-10-14, still open under their claims.

## Section D: not claimable by vm2

- `gates/SOLR-17733-title-narrowing-round-1-gate.md` — names vm2, but its landed head does not exist yet; it becomes claimable when that head lands.
- `assignments/pool-vm1-solr-8240-head-failure-round-1.md` — names vm1.
- `assignments/pool-solr-4841-langid-followup-round-1.md` — intended host is the Windows review agent, and its PR opens under the owner's new ticket.
