# Pool assignment: vm2 open work handed to vm1, round 2

Status: OPEN
Capability tags: gate, premise-run, implementation, reading. Intended host: vm1 (Linux). Claim each item under `claims/` before starting (WORKFLOW.md).
Posted by: vm2 gate backlog coordinator, 2026-10-16T17:07Z, at the direction of Nick Shanin, who asked for vm2's work to be handed to vm1 and the vm2 lane wound down.

vm2 stops taking work with this handoff. Nothing below is started on vm2 except where the item says a run was already recorded. Each item lists its state, where to resume, and what is owed. Items not listed here stay as they are on the board.

## Items

1. SOLR-10667 dotfiles cause trace (steps 3 and 4 of the job). Job: `gates/SOLR-10667-dotfiles-cause.md`. Claim: `claims/pool-vm2-gate-backlog-10667-cause.md` (BLOCKED, vm2, 2026-10-16). Head `9ce9ef1db5e9`. Steps 1 and 1b are recorded: the tracked `solr/modules/ltr/example/.gitignore` is absent from the module packaging output and from the distribution, and the `liblinear` symlink arrives as a copied directory. Not found: the rule that writes the module's `example` directory. The job's named `solrclient/**` exclude is absent from `gradle/solr/packaging.gradle` at this head, so the job's references may be stale. Next: run `:solr:modules:ltr:assemblePackaging --debug` and find the copy rule; if the job's premise text is wrong, record that in the job file and ask the main agent before any branch change. No branch change without a new claim.

2. SOLR-10667 dotfiles premise record. Job: `gates/SOLR-10667-dotfiles.md`. Claim: `claims/pool-vm2-gate-backlog-10667-dotfiles.md` (marked STALE on 2026-10-16). The 2026-10-15 run ended with the cause not recorded. Fold this into item 1; do not re-run the premise unless item 1 needs it.

3. SOLR-8009 routed no-slice real-time-get test. Assignment: `assignments/pool-solr-8009-routed-noslice-test-round-1.md` (OPEN). Branch `solr-8009-submit`, head `e600375aaee30d66694b69e1d05e276801327e73`, PR #5141 is a draft. The guard's own case, a `_route_` naming no slice of the collection, has no test; the PR's Limits names it. Write the test, prove it discriminates against the guard, then the gate.

4. SOLR-16290 fix. Job: `gates/SOLR-16290-fix.md` (OPEN). Branch `solr-16290-submit` at `ff760120c817`. Implementation plus gate, per the job's scope and exclusions.

5. SOLR-17055 follow-up (finish list, section C). Claim: `claims/pool-vm2-finish-list-round-1-c.md`. Gate RED at local commit `5d6b81d86b` (pins maxScore for score-sorted and id-sorted knn requests), not pushed, not relaunched. The commit sat only in vm2's local clone; it is exported here as `gates/SOLR-17055-followup-5d6b81d86b.patch`, which applies on the fork tip `solr-17055-submit` at `2fe0986158`. Spotless, compile and module check passed there; the focused run failed. Re-run the focused class on vm1 and record the failure shape in the claim before deciding anything. Do not push.

6. Finish list, section A. Claim: `claims/pool-vm2-finish-list-round-1-a.md` (CLAIMED, no progress recorded). The list is `assignments/pool-vm2-finish-list-round-1.md`. Re-read the list, and claim each remaining item fresh under its own claim before starting it.

7. SOLR-14919 gate re-run. Claim: `claims/pool-vm2-gate-backlog-14919.md`. The first run failed on a spotless javadoc wrap at `c1fa37e33c`. A re-run at `8f1f4ef91e` was queued (`gates/g14919-gate.sh`) and never started. Check the job's current Status line before running it.

8. SOLR-11479 status check. Claim: `claims/pool-vm2-gate-backlog-11479.md`. The last record is a heartbeat of 2026-10-11 with focused and check passes at `360dca60fc`. No closing Status line exists. Confirm whether its job is DONE, and close the claim if so; run nothing unless the job is still open.

## Not handed over

- SOLR-8628 packaging (`claims/pool-vm2-gate-backlog-8628.md`): the owner paused it on 2026-10-11. Hold until Nick Shanin lifts the pause.
- SOLR-16013 interrupt fix (`claims/pool-vm2-gate-backlog-16013-interrupt-fix.md`, FAILED) and SOLR-15712 and SOLR-16887 (ESCALATED): the main agent decides these. Do not re-run.
- SOLR-10305 (vm1 probe): already open at `assignments/pool-solr-10305-nouk-distributed-probe-round-1.md`. Nothing to add here.
- SOLR-13059 diagnosis: DONE (`assignments/pool-solr-13059-diagnosis-round-1.md`).
- SOLR-4754: DONE by vm1 (`claims/pool-vm1-gate-4754.md`, GATE GREEN at `3409d4c2bd`).
- SOLR-10627 draft head-link move and the PR #5138 and #5144 body re-points: owed to the main agent.

## Done when

Each item is either finished (its own Status line says DONE with the head) or handed back in its own file with the reason. vm1 writes its outcomes in the item's own file and claim, not in this assignment.
