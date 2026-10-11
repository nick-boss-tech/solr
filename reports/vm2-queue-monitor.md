# vm2 queue monitor

## 2026-10-10 17:32 UTC

First snapshot. No prior monitor state. HEAD is 32660e04b7.

Pool commits since the pool opened, newest first:

- 32660e04b7 Nick Shanin. vm2 onboarding checks: host capabilities and smoke test.
- 704012e95c Nick Shanin. Windows review pass on the assignment pool: vm2 claim recorded, no claim taken.
- 3c218214fe Nick Shanin. Claim: VM2 onboarding checks.
- 0adfc6d261 Nick Shanin. Windows review pass on the assignment pool: no claim taken.
- 7b877b0107 Nick Shanin. Workflow: open assignment pool for all agent hosts, with pilot assignments.

Older history is not replayed.

Claims. claims/pool-vm2-onboarding-checks.md is DONE. Claimant host is vm2. Heartbeat is 2026-10-10T17:24:00Z. Deliverable is hosts/vm2.md. That heartbeat is under 2 hours old, so the claim is not stale.

The other 65 claim files have no heartbeat line. They predate the pool heartbeat rule. They are flagged here as lacking heartbeats, not as active work, and they are not taken over.

Gates. SOLR-18530 is RUNNING on vm1. SOLR-18531 is QUEUED on vm1 behind SOLR-18530. SOLR-18532 is QUEUED on vm1 behind SOLR-18530. No gate is FAILED. No receipt exists yet for those three tickets. The receipt set is 317 files. The latest commit added none.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-testcoordinatorrole-fix, update-processing-groups-cd-review, update-processing-not-gated-review. The last two have slice claims under other names. They still have no claims file matching the assignment slug.

hosts/vm2.md no longer says the onboarding checks are pending. Capabilities are confirmed on 2026-10-10: review, draft, implementation, gate, premise-run, settling-run, sweep, bats. Full gate capability is not yet proven. The smoke test covered changelog parse and tidy only, at the SOLR-17987 receipt head 38abf64231.

## 2026-10-10 17:41 UTC

Changed since the 17:32 snapshot. Origin moved from f11ed56f874 to 3219aac3f85. Two new commits, both by Nick Shanin:

- 3219aac3f8 Claim: SOLR-16630 TestCoordinatorRole fix. Adds claims/pool-solr-16630-testcoordinatorrole-fix.md.
- 66ddf1ba18 Windows review pass on the assignment pool. Updates hosts/windows.md and adds a windows review report.

The SOLR-16630 claim names vm2 as claimant, capability tags implementation and gate, started 2026-10-10T17:40:00Z. Its heartbeat is current, so it is not stale. The assignment's gate job file gates/SOLR-16630.md does not exist yet.

Gates are unchanged. SOLR-18530 is RUNNING on vm1. SOLR-18531 and SOLR-18532 are QUEUED on vm1 behind it. No gate is FAILED. No new receipts.

Stale claims: none. The claims with a timestamped heartbeat are the SOLR-16630 claim and the vm2 onboarding claim, both under 2 hours old. The 65 older claims without a heartbeat line are unchanged from the 17:32 snapshot. They are flagged, not taken over.

Unclaimed assignments: open-update-29-prs, pool-admin-ui-premise-runs, update-processing-groups-cd-review, update-processing-not-gated-review. pool-solr-16630-testcoordinatorrole-fix is now claimed.

hosts/vm2.md is unchanged and still reports the onboarding checks as done.

No action taken by vm2.

## 2026-10-10 17:51 UTC

Changed since the 17:41 snapshot. Origin moved from 3219aac3f85 to 98dee707f68. Five new commits, all by Nick Shanin:

- 150b612228 Monitor entry for the 17:41 snapshot.
- 656b054bf3 Review agent pass: the two update-processing review assignments are already done.
- 4601bcc0da Receipts: SOLR-18530 and SOLR-18531 gates are green, and their gate jobs are marked done.
- bbabe8df75 Claims: completed rounds are marked done, and the workflow now requires the mark. 53 claim files gained a DONE mark, applied by the main agent as a record correction. Two of them were spot-checked against their deliverables on the branch.
- 98dee707f6 Assignment: review round for the three new flaky-fix branches (SOLR-18530, SOLR-18531, SOLR-18532).

Gates. SOLR-18530 moved from RUNNING to GREEN. Its receipt is receipts/SOLR-18530.md. SOLR-18531 moved from QUEUED to GREEN, with receipts/SOLR-18531.md. SOLR-18532 is still QUEUED on vm1 with no receipt. No gate is FAILED.

Claims. No claim file changed in a way that starts or ends work except the DONE marks above. Active claims with a heartbeat: pool-solr-16630-testcoordinatorrole-fix (vm2, heartbeat 2026-10-10T17:40:00Z) and pool-vm2-onboarding-checks (vm2, heartbeat 2026-10-10T17:24:00Z). Neither is stale. gates/SOLR-16630.md does not exist yet.

Stale claims: none. Twelve older claims have neither a DONE mark nor a heartbeat line: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. They have no timestamp to measure, so they are flagged for the main agent to mark, not taken over.

Newer origin commits, after this snapshot was taken: f67d82b376 (Windows review pass, hosts/windows.md heartbeat 17:50 UTC) and e2aec2da28 (claim by windows, host name windows, for slices 1 and 2 of pool-flaky-fix-review-round-1, heartbeat 2026-10-10T17:50:48Z). Slice 3, SOLR-18532, is not claimed; its gate is still QUEUED with no receipt.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, update-processing-groups-cd-review, and update-processing-not-gated-review. The last two are marked done in 656b054bf3 but have no claims file. pool-flaky-fix-review-round-1 is partly claimed (slices 1 and 2 by windows); slice 3 is open once SOLR-18532 is green.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. It records the checks as done, with capabilities confirmed. Full gate capability is still unproven: the smoke test covered changelog parse and tidy only. hosts/windows.md heartbeat moved to 17:42 UTC.

No action taken by vm2.

## 2026-10-10 18:00 UTC

Changed since the 17:51 snapshot. Origin moved from 7709a59140 to 1c5c9785db. Two new commits, both by Nick Shanin:

- 7db30f5f6f Gate job: SOLR-16630 on vm2
- 1c5c9785db Assignment: vm2 gate and runs backlog round 1, with job files for its thirteen claimable jobs

Gates. SOLR-16630 is RUNNING on vm2, started 2026-10-10 UTC. None of its step checkboxes are ticked yet. Thirteen new gate job files were added and all are UNCLAIMED: SOLR-10390-premise, SOLR-11650-baserun, SOLR-12916, SOLR-12998, SOLR-13705-premise, SOLR-16499, SOLR-18391, SOLR-4502, SOLR-5011, SOLR-5262-premise, SOLR-9091, SOLR-9382, and SOLR-9865-17287-recount. SOLR-18530 and SOLR-18531 remain DONE (GREEN). SOLR-18532 is still QUEUED on vm1. No gate is FAILED. No receipts changed.

Claims. No claim file changed. Active claims: pool-solr-16630-testcoordinatorrole-fix (vm2, heartbeat 17:40 UTC), pool-flaky-fix-review-round-1 (windows, heartbeat 17:50 UTC), and pool-vm2-onboarding-checks (vm2, heartbeat 17:24 UTC). The last one has no DONE mark, although hosts/vm2.md records its checks as done.

Stale claims: none by the two-hour heartbeat rule. Twelve older claims have neither a DONE mark nor a heartbeat line: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. They have no timestamp to measure, so they are flagged for the main agent to mark, not taken over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (new in this change, and vm2 is capable of its jobs), update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md no longer says the onboarding checks are pending. It records them as done on 2026-10-10. Full gate capability is still unproven, since the smoke test covered changelog parse and tidy only.

No action taken by vm2.

## 2026-10-10 18:10 UTC

Changed since the 18:00 snapshot. Origin moved from 1c5c9785db to 30a6d1bfc2. Three new commits, all by Nick Shanin:

- e1d13c50ea Flaky-fix review round 1: two slices reviewed, one held.
- 30a6d1bfc2 Windows pool pass: no assignment for this host, heartbeat.
- c46ade72a9 vm2 queue monitor entry for the 18:00 snapshot.

Gates. No gate file changed. SOLR-16630 is still RUNNING on vm2. SOLR-18532 is still QUEUED on vm1 with no receipt. SOLR-18530 and SOLR-18531 remain DONE (GREEN). No gate is FAILED.

Claims. pool-flaky-fix-review-round-1 gained a status line marking slices 1 and 2 DONE (windows). Slice 3, SOLR-18532, is still not claimed. Active claim: pool-solr-16630-testcoordinatorrole-fix (vm2, heartbeat 17:40 UTC). It is not stale, but its next hourly heartbeat is due by 18:40 UTC.

Stale claims: none by the two-hour heartbeat rule. Twelve older claims still have neither a DONE mark nor a heartbeat line: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. They have no timestamp to measure, so they are flagged for the main agent to mark, not taken over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. It records them as done on 2026-10-10. Full gate capability is still unproven, since the smoke test covered changelog parse and tidy only. hosts/windows.md heartbeat moved to 18:06 UTC.

No action taken by vm2.

## 2026-10-10 18:51 UTC

Changed since the 18:10 snapshot. Origin moved from 2028c813d3 to 20266c372b. Five new commits, all by Nick Shanin:

- d4c7143019 SOLR-18532 receipt: gate green at the corrected head; review slice 3 claimable.
- 60461f50cd Receipt punctuation fix.
- 271daeee9f Assignment: review-confidence round 2.
- fc3518ee6e Claim: flaky-fix review round 1, slice 3.
- 20266c372b Assignment: vm2 gate backlog round 2, first gates for never-gated branches.

Gates. Nineteen gate files were added or changed, most of them new UNCLAIMED gates from the vm2 gate backlog round 2 assignment. SOLR-16630 is still RUNNING on vm2. SOLR-18530 and SOLR-18531 are DONE (GREEN). SOLR-18532 has a receipt, receipts/SOLR-18532.md, recording GATE GREEN at 348dd63d85a. Its gate file still has a stale top status line (QUEUED on vm1, head 07a7ead478); the bottom status line says DONE and matches the receipt. No gate is FAILED.

Claims. New claim pool-flaky-fix-review-round-1-slice-3 (windows, heartbeat 18:48 UTC). Active claims: pool-solr-16630-testcoordinatorrole-fix (vm2, heartbeat 17:40 UTC) and pool-flaky-fix-review-round-1 (windows, heartbeat 17:50 UTC; slices 1 and 2 DONE, slice 3 now has its own claim). The SOLR-16630 claim's hourly heartbeat is more than an hour old and is due now. It is not stale under the two-hour rule until 19:40 UTC.

Stale claims: none by the two-hour heartbeat rule. The same twelve older claims as before still have neither a DONE mark nor a heartbeat line. Their last changes are dated 2026-10-08 to 2026-10-10 by commit time, so they are well past two hours by that measure. They are flagged for the main agent to mark, not taken over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-review-confidence-round-2, pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2 (new in this change), update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is marked DONE on 2026-10-10. Full gate capability is still unproven, since the smoke test covered changelog parse and tidy only.

No action taken by vm2.

## 2026-10-10 19:00 UTC

Changed since the 18:51 snapshot. Origin moved from 20266c372b to 9a9c74a6ed. Five new commits, all by Nick Shanin:

- ff7a0710f6 SOLR-16630 gate green on vm2: receipt, job DONE, claim DONE.
- ae58c8aa2c vm2 coordination ideas for the git-based pool.
- 14f98e0996 Claim: SOLR-12998 live-tip gate, vm2.
- f8b56e8a0c Flaky-fix review round 1: slice 3 reviewed, draft held.
- 9a9c74a6ed Claim: review-confidence round 2, four slices.

Gates. SOLR-16630 is now DONE (GREEN) at 9bea59741a, with receipts/SOLR-16630.md added. SOLR-12998 is CLAIMED by vm2 at 19:00 UTC; its gate has not started, and no receipt exists yet. No gate is FAILED.

Claims. New claims: pool-vm2-gate-backlog-12998 (vm2, heartbeat 19:00 UTC, SOLR-12998 gate) and pool-review-confidence-round-2 (windows, heartbeat 18:59 UTC). The SOLR-16630 claim is DONE. Active claims: pool-vm2-gate-backlog-12998 and pool-review-confidence-round-2, and pool-flaky-fix-review-round-1 (windows, heartbeat 17:50 UTC; the slice 3 claim is DONE with a heartbeat at 18:48 UTC).

Stale claims: none with a heartbeat older than two hours. Correction to the earlier sections: the twelve older claims that have neither a DONE mark nor any heartbeat line (review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, update-processing-scope-the-eight) were previously described as not stale. Their claim text dates them 2026-10-08 or 2026-10-09, and they have no heartbeat at all, so under the two-hour rule they count as stale. They are flagged only; vm2 does not take them over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (job 1, SOLR-12998, is claimed under pool-vm2-gate-backlog-12998; jobs 2 to 13 are unclaimed), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE on 2026-10-10. Full gate capability is still unproven, since the smoke test covered changelog parse and tidy only.

No action taken by vm2.

## 2026-10-10 19:21 UTC

Changed since the 19:00 section. Origin moved from 9a9c74a6ed to eef706c385. Five new commits, all by Nick Shanin:

- 63e0971c95 SOLR-18530 receipt and draft at the post-changelog head; corrected the SOLR-18532 gate job head.
- cb6f8ed48c Assignment: post-PR review round for the flaky-fix PRs.
- 1ce02469d4 Flaky-fix review: added slice 4 for the SOLR-16630 fix, gated on vm2.
- 57ce9c60d9 SOLR-12998 gate on vm2: steps green at head, proof leg blocked.
- eef706c385 Claim: SOLR-18391 graceful-create gate, vm2.

Gates. SOLR-12998: steps green at 62a17a116b5 (tidy, compile, focused tests, module check). The proof leg is BLOCKED because the branch tests do not compile against merge-base production. The claim is DONE with outcome BLOCKED ON PROOF LEG, and receipts/SOLR-12998.md was refreshed. SOLR-18391: gate job is open and claimed by vm2 (heartbeat 19:18 UTC); no receipt yet. SOLR-18532: gate DONE and GREEN at 348dd63d85, with receipts/SOLR-18532.md. SOLR-18530: receipt refreshed. No gate is FAILED. None is marked RUNNING in the gate file itself.

Claims. New claim: pool-vm2-gate-backlog-18391 (vm2, heartbeat 19:18 UTC). Active claims with heartbeats in the last two hours: pool-vm2-gate-backlog-18391, pool-review-confidence-round-2 (heartbeat 18:59 UTC), pool-flaky-fix-review-round-1-slice-3 (18:48 UTC), and pool-flaky-fix-review-round-1 (17:50 UTC).

Stale claims: none active with a heartbeat older than two hours. Still unmarked, with no heartbeat line at all, and dated 2026-10-08 or 2026-10-09: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. Under the two-hour rule they are stale. They are flagged only; vm2 does not take them over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-flaky-fix-post-pr-review-round-1 (new; review round for SOLR-18530, SOLR-18531 and SOLR-18532, no claim file yet), pool-vm2-gate-backlog-round-1 (jobs 1 and 2, SOLR-12998 and SOLR-18391, are claimed under ticket-named files; the other jobs have no claim), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE on 2026-10-10.

No action taken by vm2.

## 2026-10-10 19:30 UTC

Changed since the 19:21 section. Origin moved from eef706c385 to 80ed2a7882. Five new commits, all by Nick Shanin:

- 80ed2a7882 Claim: flaky-fix post-PR review round 1, slices 1 and 3.
- 883ac04082 Review confidence round 2: draft verification, live bodies, premise re-checks.
- 31a686e0f6 SOLR-18532 draft: named the SOLR-9865 follow-up in Limits.
- 140304dadc vm2 queue monitor report commit (an earlier monitor run).
- 67f255e31b SOLR-18532 draft cleared for posting at the squashed head; receipt and post-PR slice updated.

Gates. No gate file changed in this range. SOLR-18532 is DONE and GREEN at 348dd63d85; its receipt was refreshed. SOLR-18391 is CLAIMED by vm2 (heartbeat 19:18 UTC) with no receipt yet. SOLR-12998 is BLOCKED ON PROOF LEG; its steps were green at 62a17a116b5. No gate is FAILED or RUNNING.

Claims. New claim: pool-flaky-fix-post-pr-review-round-1 (windows, active, heartbeat 19:29 UTC). pool-review-confidence-round-2 is now DONE. Active claims with heartbeats in the last two hours: pool-flaky-fix-post-pr-review-round-1, pool-vm2-gate-backlog-18391, pool-vm2-gate-backlog-12998 (heartbeat 18:56 UTC, claim DONE), pool-flaky-fix-review-round-1-slice-3 (18:48 UTC), pool-solr-16630-testcoordinatorrole-fix (18:52 UTC).

Stale claims: none active with a heartbeat older than two hours. pool-vm2-onboarding-checks has a last heartbeat of 17:24 UTC, but its claim is DONE, so it is not stale. Still unmarked, with no heartbeat line and no DONE marker, dated 2026-10-08 or 2026-10-09: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum (last heartbeat 2026-10-09 08:03 UTC), and update-processing-scope-the-eight. Under the two-hour rule they are stale. They are flagged only; vm2 does not take them over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (jobs 1 and 2 are claimed under ticket-named files; the others have no claim), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE on 2026-10-10.

No action taken by vm2.

## 2026-10-10 19:50 UTC

Changed since the 19:30 section. Origin moved from 80ed2a7882 to 4f488e98de. Six new commits, all by Nick Shanin:

- e4889be53e Flaky-fix post-PR review round 1: two open PRs reviewed, findings reported.
- af7d707503 Claim: flaky-fix review round 1, slice 4.
- 50ef5ea2a5 SOLR-18531 draft cleared at the re-gated head; receipt refreshed; post-PR slice active.
- 9224143a7a SOLR-18391 gate on vm2: green at head, proof leg mismatch recorded.
- 7f7caf8bc1 SOLR-18391 claim result on vm2.
- 4f488e98de Slice B drifts applied to live PR bodies; drafts synced and SOLR-16673 receipt refreshed.

Gates. gates/SOLR-18391.md is DONE: green at head adcda10b501, with the proof leg recorded as a mismatch. At base, CreateCollectionCleanupTest has 2 failures, not the 1 the receipt predicted (testCreateDoesNotDeleteExistingCollectionOnStaleView also fails, with 500 where 400 was expected). PlacementPluginIntegrationTest.testAssignForMissingCollection fails as predicted. The main agent decides. receipts/SOLR-18391.md, receipts/SOLR-18531.md and receipts/SOLR-16673.md were refreshed. No gate is RUNNING or FAILED.

Claims. Done since the last section: pool-vm2-gate-backlog-18391 (DONE 19:45 UTC) and pool-flaky-fix-post-pr-review-round-1 (DONE, windows, slices 1 and 3). New claim: pool-flaky-fix-review-round-1-slice-4 (SOLR-16630, windows, review and draft, active, heartbeat 19:42 UTC). Active claims with heartbeats in the last two hours: pool-flaky-fix-review-round-1-slice-4.

Stale claims: none active with a heartbeat older than two hours. The unmarked claims with no heartbeat line, dated 2026-10-08 or 2026-10-09, are unchanged from the previous section: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum (last heartbeat 2026-10-09 08:03 UTC), and update-processing-scope-the-eight. Under the two-hour rule they are stale. They are flagged only; vm2 does not take them over.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (jobs 1 and 2 are claimed under ticket-named files; the others have no claim), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE, and the host file records the checks as confirmed.

No action taken by vm2.

## 2026-10-10 20:00 UTC

What changed since the last monitor run (last seen 5cccba9c97, now a6dc93351d). Two new commits by Nick Shanin:

- 5b5eb64a5d Flaky-fix review round 1: slice 4 reviewed, draft written, proof held.
- a6dc93351d Claim: flaky-fix post-PR review round 1, slice 2.

Claims. pool-flaky-fix-review-round-1-slice-4 (SOLR-16630) is DONE. Its draft is ready, and the proof claim is held for owner decision O1. New claim pool-flaky-fix-post-pr-review-round-1-slice-2 (SOLR-18531) is active on windows, review tag only, with the last heartbeat at 19:53 UTC.

Gates. No gate is RUNNING or FAILED. gates/SOLR-12998.md remains BLOCKED ON PROOF LEG: the gate steps are green at head 62a17a116b5, but the proof leg did not run because the branch tests do not compile against merge-base production. The main agent decides. No new receipts in this range.

Stale claims. None. No active claim has a heartbeat older than two hours (cutoff 18:00 UTC). These claims have no heartbeat line and no DONE mark, and their dates are 2026-10-08 or 2026-10-09 (round-4-answers-and-13696-r8 has no date): review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. They are stale under the two-hour rule and are flagged only.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (jobs 1 and 2 are claimed under ticket-named files), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md records the onboarding checks as confirmed on 2026-10-10. They are not pending. The onboarding claim is DONE.

No action taken by vm2.

## 2026-10-10 20:11 UTC

What changed since the last monitor run (last seen 15c1461bba, now f4bfa147a3). One new commit by Nick Shanin: f4bfa147a3 Flaky-fix post-PR review round 1: slice 2 reviewed, drift reported. It adds the slice 2 section to reports/flaky-fix-post-pr-review-round-1.md and the part reports for SOLR-18531 (s2a, which records drift against the live PR, and s2b, which checks the receipt and head code).

Claims. claims/pool-flaky-fix-post-pr-review-round-1-slice-2 (SOLR-18531, windows, review tag only) is now DONE. Its last heartbeat was 19:53 UTC. hosts/windows.md shows a heartbeat at 20:02 UTC and records slices 1, 2 and 3 of that round as DONE.

Gates. No gate file or receipt changed in this range. No gate is RUNNING or FAILED. gates/SOLR-12998.md remains BLOCKED ON PROOF LEG: the gate steps are green at head 62a17a116b5, but the proof leg did not run because the branch tests do not compile against merge-base production. gates/SOLR-18391.md is DONE with a proof-leg mismatch. gates/SOLR-18532.md contains both a QUEUED line and a DONE line; the DONE line matches receipts/SOLR-18532.md, so the QUEUED line looks stale.

Stale claims. None. No active claim has a heartbeat older than two hours (cutoff 18:10 UTC). These claims have no heartbeat line and no DONE mark, and their dates are 2026-10-08 or 2026-10-09 (round-4-answers-and-13696-r8 has no date): review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. They are stale under the two-hour rule and are flagged only.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-vm2-gate-backlog-round-1 (jobs 1 and 2 are claimed under ticket-named files), pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE and the host file records the checks as confirmed on 2026-10-10.

No action taken by vm2.

## 2026-10-10 20:20 UTC

What changed since the last monitor run (last seen b9bd979e08, now 63b308e080). Two new commits by Nick Shanin: 42173b9b46 (SOLR-18391 proof decision recorded; SOLR-16630 base runs and title fix assigned) and 63b308e080 (flaky-fix post-PR review fixes: corrected drafts, receipts and gate job head). The first adds assignments/pool-solr-16630-base-runs.md. The second edits drafts for SOLR-18530, SOLR-18531 and SOLR-18532, receipts for SOLR-18391, SOLR-18530 and SOLR-18531, and gates/SOLR-18531.md. No new claim files were added.

Claims. No claim was added or changed in this range. No claim is active: every claim with a heartbeat line is marked DONE.

Gates. No gate is RUNNING or FAILED. SOLR-18531 and SOLR-16630 are DONE with gate green. SOLR-12998 remains DONE with outcome BLOCKED ON PROOF LEG, as recorded before.

Stale claims. Flagged, none taken over. These claims have no heartbeat line and no DONE mark, and their dates (2026-10-08 or 2026-10-09) are well past the 2-hour cutoff of 18:20 UTC: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, update-processing-scope-the-eight.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-base-runs (new in 42173b9b46), pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, update-processing-not-gated-review. The vm2 gate-backlog jobs are claimed under ticket-named claim files.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE.

No action taken by vm2.

## 2026-10-10 20:30 UTC

What changed since the last monitor run (last seen 63b308e080, now 12b80c539a). Two new commits by Nick Shanin: b1dfd670f9 (assignment for post-PR review round 2 of the flaky-fix PRs, SOLR-18530, SOLR-18531 and SOLR-18532) and 12b80c539a (claim for that round, slices 1 and 3). The only new files are assignments/pool-flaky-fix-post-pr-review-round-2.md and claims/pool-flaky-fix-post-pr-review-round-2.md. No gate or receipt file changed in this range.

Claims. The new claim is active. Its claimant is windows (review tag), with a heartbeat at 20:29:49 UTC. Slice 2 (SOLR-18531) is not claimed, because the assignment waits for a new branch head to be recorded. No other claim changed.

Gates. No gate file changed in this range.

Stale claims. Flagged, none taken over. These claims have no heartbeat line and no DONE mark. Their last commits are from 2026-10-08 or 2026-10-09, well past the 2-hour cutoff of 18:30 UTC: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-base-runs, pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review. The vm2 gate-backlog jobs are claimed under ticket-named claim files.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE and the host file records the checks as confirmed on 2026-10-10.

No action taken by vm2.

## 2026-10-10 20:40 UTC

What changed. One new commit on origin/pr-prepare: 0f3db70f19 (Nick Shanin), flaky-fix post-PR review round 2. It adds reports for slices 1 and 3 and a roll-up report, marks claims/pool-flaky-fix-post-pr-review-round-2.md DONE, and updates hosts/windows.md by one line. Slice 2 is inactive because no new head was recorded. No gate or receipt file changed.

Claims. No claim is active past the cutoff. The heartbeat-tracked claims are all DONE. The most recent heartbeat is 20:29:49 UTC on the round 2 claim, which is now DONE.

Stale claims. Flagged, none taken over. These claims have no heartbeat line and no DONE mark. The ones that state a date were claimed 2026-10-09, well before the 18:40 UTC cutoff: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-base-runs, pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review. The round 1 vm2 gate-backlog jobs are claimed under ticket-named claim files.

Gates. A text search of gates/ found no RUNNING or FAILED status lines.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE.

No action taken by vm2.

## 2026-10-10 20:50 UTC

What changed. Four new commits by Nick Shanin, up to ae3bb783d7. Round 3 of the flaky-fix post-PR review was assigned (pool-flaky-fix-post-pr-review-round-3) and claimed by windows for slices 1 and 3 (SOLR-18530, SOLR-18532). The round 3 claim heartbeat is 20:47:50 UTC. Slice 2 (SOLR-18531) stays inactive because its receipt records GREEN at an older head than the new one. Gate-related edits: gates/SOLR-18532.md and receipts/SOLR-18530.md and receipts/SOLR-18532.md were updated, and the pr-drafts for SOLR-18530 and SOLR-18532 changed. No gate is RUNNING or FAILED now. SOLR-18530 and SOLR-18532 are recorded GREEN.

Claims. The round 3 claim is active and within the heartbeat window. All other claims are either DONE or unchanged.

Stale claims. Flagged, none taken over. These claims have no heartbeat line and no DONE mark, and their last recorded dates are 2026-10-08 or 2026-10-09, well past the 2-hour cutoff: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-base-runs, pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. It records them as confirmed on 2026-10-10, with full gate capability not yet proven by the smoke test.

No action taken by vm2.

## 2026-10-10 21:00 UTC

What changed. One new commit on origin/pr-prepare: 62b49dec9a (Nick Shanin), flaky-fix post-PR review round 3 confirmation pass. It marks claims/pool-flaky-fix-post-pr-review-round-3.md DONE and updates hosts/windows.md. Round 3 verdicts: SOLR-18530 still open (1 item), SOLR-18532 still open (2 items), SOLR-18531 inactive. No gate or receipt file changed.

Claims. No claim is active. The round 3 claim is DONE; its last heartbeat was 20:47:50 UTC.

Stale claims. None active past the 2-hour cutoff (19:00 UTC). Flagged, none taken over. These claims have no heartbeat line and no DONE mark: review-28-round-2, review-corrections-28, review-plain-language-28, round-4-answers-and-13696-r8, solr-13696-fix-project, update-processing-audit-group-a, update-processing-audit-group-c, update-processing-audit-group-d, update-processing-not-gated-group-a, update-processing-not-gated-group-b, update-processing-round-3-closeout-addendum, and update-processing-scope-the-eight. The round-3 closeout addendum is dated 2026-10-09 08:03 UTC, the latest date in any of them.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-base-runs, pool-vm2-gate-backlog-round-1, pool-vm2-gate-backlog-round-2, update-processing-groups-cd-review, and update-processing-not-gated-review.

Gates. No gate file changed in this range. No gate is RUNNING. The FAILED text in gates/SOLR-18532.md is a historical first run at 07a7ead4783; the current status there is GREEN at 348dd63d85a.

Hosts. hosts/vm2.md does not say the onboarding checks are pending. The onboarding claim is DONE (2026-10-10).

No action taken by vm2.

## 2026-10-10 21:37 UTC

What changed. vm2 claimed and finished round 1 job 3, SOLR-4502 (gates/SOLR-4502.md, claims/pool-vm2-gate-backlog-4502.md). Premise run on base cabedd1d96 failed as predicted: create on the unloaded container throws a create-time NPE, not the load() message. Packaging commit removed SOLR-4502-TESTING.md locally (not pushed to the fork). Gate green at packaged head 0ee4c644ee63: changelog OK, tidy clean, Error Prone compile OK, TestCoreContainer 26 run, 0 failed, 3 skipped (Windows-only), new test passed, module check rc 0. Receipt receipts/SOLR-4502.md. Job and claim DONE.

Claims. No vm2 claim is active. Round 1 jobs 1 and 2 were already BLOCKED or DONE; job 4, SOLR-5011, is next and unclaimed.

Caveats. The search-time NPE named in the ticket was not run separately; the premise was shown at create time. Guard placement remains the owner's call.

No action taken beyond this job.

## 2026-10-10 22:01 UTC

What changed. vm2 claimed round 1 job 4, SOLR-5011 (gates/SOLR-5011.md, claims/pool-vm2-gate-backlog-5011.md), and finished it BLOCKED ON PROOF LEG. Gate steps are green at the local packaged head 5b2cfdfe4709 (packaging removes SOLR-5011-TESTING.md; not pushed): changelog OK, tidy clean, compileTestJava OK. The proof leg did not show the premise. At cabedd1d96 with the branch's SolrResourceLoader.java and test, CoreCloseResourceLoaderTest fails at line 44 (oldCore.close()) with "Too many closes on SolrCore" because the refcount was already zero, so the line 45 assertTrue(oldLoader.isClosed()) was never reached. Focused tests at head and the module check were not run; the job stopped per protocol. No receipt written.

Decision for the main agent. Whether the test's premise (h.reload() keeps the old core open while the test holds a reference) holds on base and on head is not established by this run. The head focused run is the next thing to check, and it was not run. The shared-schema scenario from part k3 is not covered by any branch test; recorded as the finding in the job file.

Claims. No vm2 claim is active. Round 1 job 5 (SOLR-12916) is next and unclaimed.

## 2026-10-10 22:27 UTC

What changed. vm2 claimed round 1 job 5, SOLR-12916 (gates/SOLR-12916.md, claims/pool-vm2-gate-backlog-12916.md), at 22:02Z and finished it GREEN at packaged head 50b1f4bcab03 (packaging removes SOLR-12916-TESTING.md; local only, not pushed). Premise run on base cabedd1d96 failed as predicted: testFlatNameValueQueryFromConfigApi "expected:<2> but was:<0>". Gate steps green at head: changelog OK, tidy clean, Error Prone compile OK, QuerySenderListenerTest 2 of 2 passed, module check rc 0. Receipt receipts/SOLR-12916.md. Job and claim DONE.

Caveats. No Config API round trip was run; the stated limit from the TESTING.md stands. The XML path change is the owner's call.

Claims. No vm2 claim is active. Round 1 job 6 (SOLR-16499) is next and unclaimed.

No action taken beyond this job.

## 2026-10-10 23:01 UTC

What changed. vm2 finished round 1 job 6, SOLR-16499 (gates/SOLR-16499.md, claims/pool-vm2-gate-backlog-16499.md). The gate run had completed at 22:53:44Z, but its records were never closed out. Premise held on base: testParallelAndTimeoutAreForwardedToTheOverseerMessage failed with "expected:<4> but was:<2>". Gate green at packaged head 51b7addddd45 (packaging removes SOLR-16499-TESTING.md; local only, not pushed): changelog OK, tidy clean, compile OK, ReplaceNodeAPITest tests=4 failures=0 from fresh XML, module check rc 0. Receipt receipts/SOLR-16499.md rewritten at the gated head (it previously recorded NO GATE). Job and claim DONE.

Caveats. The reference-guide and OpenAPI or SolrJ regeneration checks are not part of this gate and remain owed for the draft stage. The DISCUSS call on the route is not taken here.

Claims. No vm2 claim is active. Round 1 job 7 (SOLR-5262 premise run) is next and unclaimed.

## 2026-10-10 23:26 UTC

What changed. vm2 claimed and finished round 1 job 7, SOLR-5262 premise run (gates/SOLR-5262-premise.md, claims/pool-vm2-gate-backlog-5262.md). No gate steps were run. Base cabedd1d96 with the branch test as the vehicle: testUlogDirDefaultsToDataDir fails (expected:<data/> but was:<null>); a scratch ${solr.core.ulogDir} substitution probe fails with "No system property or default value specified for solr.core.ulogDir". Head ade8b80264: TestCoreDescriptorImplicitProperties 3/3 and the probe pass. Receipt receipts/SOLR-5262.md updated; still NO GATE. The probe covers substitution only, not a full core load, and that limit is recorded in the receipt. Job and claim DONE.

Claims. No vm2 claim is active. Round 1 job 8 (SOLR-9091 first gate) is next and unclaimed.

## 2026-10-10 23:34 UTC

What changed. vm2 claimed round 1 job 8, SOLR-9091 first gate (gates/SOLR-9091.md, claims/pool-vm2-gate-backlog-9091.md), commit 958f272219 on this branch. Packaging is local only, not pushed: packaged head e6bfa5c626 removes SOLR-9091-TESTING.md and corrects the TestRestoreCore comment. Gate launched 23:33:35Z; proof leg is base RestoreCore.java (cabedd1d96) with the branch test kept. The gate was started once by mistake and killed before any test ran; the rerun is the only run.

Claims. The vm2 claim is active with a heartbeat at 23:34 UTC. The next run should read /workspace/gates/logs/vm2/9091-gate.out and finish the job if the gate has ended.

No action taken beyond this job.

## 2026-10-11T00:03Z (UTC), vm2 queue run

- SOLR-9091 gate finished (gate end 2026-10-10T23:52:40Z). Green at packaged head e6bfa5c626 (local only). Proof leg failed by content as predicted: testRestoreRejectsCorruptBackupFile, no exception on base, TestRestoreCore.java:284. Focused TestRestoreCore tests=4 failures=0, module check rc 0. Receipt receipts/SOLR-9091.md written, job and claim marked DONE.
- vm2 claimed SOLR-9382 (tranche 1, job 9). Packaging commit 2952bae9d3 is local only: changelog title, TESTING.md removal, two long lines, per the g3 report findings 12, 15, 16. Proof base is cabedd1d96 with ReplicationHandler.java reverted in the proof worktree only. Gate started 2026-10-11T00:08:24Z, log /workspace/gates/logs/vm2/9382-gate.out.

## 2026-10-11T00:32Z (UTC), vm2 queue run

- SOLR-9382 gate finished (gate end 2026-10-11T00:27:03Z). Green at packaged head 2952bae9d3 (local only). Proof leg failed by content as predicted: testGlobExpandsToMatchingFiles, TestReplicationConfFileGlob.java:50. Focused tests tests=4 failures=0, module check rc 0. Receipt receipts/SOLR-9382.md written, job and claim marked DONE.
- Next claimable for vm2 is round 1 job 10 (SOLR-9865/17287 recount). Not claimed in this run.

- 2026-10-11T01:12Z: vm2 claimed and finished round 1 job 10 (TestRestoreCore recount, SOLR-9865 and SOLR-17287). Count confirmed at 4 at both heads, 0 failures from fresh JUnit XML; receipts updated. Next claimable for vm2: round 1 job 11 (SOLR-11650 base run), then jobs 12 and 13.

## 2026-10-11T01:24Z (UTC), vm2 queue run

- vm2 claimed round 1 job 11 (SOLR-11650 base run) at 01:14Z. Run finished 01:23Z. Premise held: testFollowerDetailsRedactLeaderUrlPassword fails on base production with the password visible in leaderUrl (TestUserManagedReplicationWithAuth.java:216; JUnit tests=1 failures=1). Receipt receipts/SOLR-11650.md updated; job and claim marked DONE.
- Next claimable for vm2: round 1 job 12 (SOLR-10390 premise run, BATS without lsof).

## 2026-10-11T01:49Z (UTC), vm2 queue run

- vm2 claimed round 1 job 12 (SOLR-10390 premise run) at 01:25Z (gates/SOLR-10390-premise.md, claims/pool-vm2-gate-backlog-10390.md). Premise HOLDS on the added BATS test: base cabedd1d96 bin/solr fails at the 'Please install lsof' refute (test_start_solr.bats:129); head 4af4a6834e passes the test body. The bats report is "not ok" only from teardown, where solr stop --all took the 30s wait and was force-killed on this host. A manual stop reproduces the same timeout on base and head, so it is host timing, not the branch. Receipt receipts/SOLR-10390.md updated; no gate run. Job and claim marked DONE.
- Caveat: the distribution was built with -PdisableJsClient=true because the js-client npm step failed on this host (missing @babel/plugin-syntax-dynamic-import). The JS client does not feed bin/solr or this test.
- Next claimable for vm2: round 1 job 13 (SOLR-13705 premise run).

## 2026-10-11T02:07:48Z (UTC), vm2 queue run

- vm2 claimed round 1 job 13 (SOLR-13705 premise run) at 01:51Z (gates/SOLR-13705-premise.md, claims/pool-vm2-gate-backlog-13705.md).
- Premise HELD: base 9b3a84b1c460 fails testLazilyInitializedSingletonIsVolatile on the volatile assertion (tests=2 failures=1); head 5f141fb2af passes both tests (tests=2 failures=0). Receipt receipts/SOLR-13705.md updated; job and claim marked DONE.
- Round 1 is now exhausted for vm2 except SOLR-5011 (BLOCKED on proof leg, recorded). Round 2 is claimable by the next run.

## 2026-10-11T02:32Z (UTC), vm2 queue run

- vm2 claimed round 2 job 2 (SOLR-10882 premise run, then first gate) at 02:09Z; round 1 was exhausted. Round 2 job 1 (SOLR-9852) is held by vm1 and was not touched.
- Premise HELD: on base e044bf20b4 production with the branch test kept, arrayMixedTypesSortTest fails with the ClassCastException the handoff predicted (ArrayEvaluatorTest.java:148); head 83fc3dfeb2 passes 7/7 from fresh XML.
- Packaging commit 5d7d07a5b1 (removes SOLR-10882-TESTING.md) is local only. Gate GREEN at that head: changelog OK, tidy clean, compile rc 0, proof leg as above, focused 7/7, module check rc 0.
- Receipt receipts/SOLR-10882.md written; job and claim marked DONE.
- Next claimable for vm2: round 2 job 3 (SOLR-3498 premise run, then first gate).

## 2026-10-11T02:57Z

- Round 1 has no claimable job for vm2: SOLR-5011 is BLOCKED on the proof leg, the rest are DONE or not claimable.
- Claimed round 2 job 3, SOLR-3498 (premise run, then first gate), 02:34Z.
- Premise holds: base fails on the commitWithin request parameter (TestContentWriterUpdateRequest.java:32, expected 1234 but was null); head passes 2/2.
- Gate GREEN at packaged head 2ca0b3743427 (local, TESTING.md removed, not pushed). Changelog OK, tidy clean, compile rc 0, focused 2/0, 6/0, 18/0, module check rc 0.
- Receipt receipts/SOLR-3498.md written; job and claim marked DONE.
- Next claimable for vm2: round 2 job 4, SOLR-10364.

## 2026-10-11T03:23Z

- Claimed round 2 job 4, SOLR-10364 (premise run, then first gate), 02:58Z. Round 1 had no claimable vm2 job left.
- Premise holds: base (DocumentObjectBinder at cabedd1d96, branch test kept) fails testSetFields with "Can not set java.util.Set field ... to ImmutableCollections$ListN"; head passes 6/0.
- Gate GREEN at packaged head efb1e6717f9 (local, TESTING.md removed, not pushed). Changelog OK, tidy clean, compile rc 0, focused TestDocumentObjectBinder 6/0, module check rc 0.
- Receipt receipts/SOLR-10364.md written; job and claim marked DONE. Interaction with SOLR-4422 noted (import-block textual conflict only).
- Next claimable for vm2: round 2 job 5, SOLR-11356.
