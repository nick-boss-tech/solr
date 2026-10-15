# Pool assignment: vm2 gate backlog, round 3

Status: DONE (GATE GREEN at 1a1875d95f36)

Capability tags: `gate`. Intended host: vm2. Any capable Linux host may take a job when vm2 is busy, under the WORKFLOW.md claim rules. Windows never claims build work. Staffing: one gate job at a time per host; no subagents needed.

## Why this round

On 2026-10-13 the main side read the vm2 claims against the gate files. Two gate claims have stopped heartbeating, and their jobs were ruled on or re-decided after the claim went quiet:

- SOLR-12505: the claim's last activity is the interrupted run that started 01:00Z. Amendment 2 (2026-10-13) rules on the proof leg and says only that leg re-runs.
- SOLR-14919: the claim's last heartbeat is 04:22Z. The main side decided on 2026-10-13 that vm2 re-runs the gate at `8f1f4ef91e`.

The third job is new. SOLR-17292 has a passing gate with no run date, which the batch B review held on.

Each job is already written up in its gate file. This assignment only sets vm2's order and the holds.

## Order

1. **SOLR-12505, proof leg only**, per `gates/SOLR-12505-fetchstream-fix-amendment-2.md`.
   - Refresh `claims/pool-vm2-gate-backlog-12505.md` with a heartbeat line before any work.
   - Record the new test's exact trigger input (the `on=` value) in `gates/SOLR-12505-fetchstream-fix.md`. Read it from the test at `ee67d374219275907ef1b78e3d2b455b6a15e43f`. Amendment 2 requires this before the re-run. The main side cannot read that commit locally.
   - Re-run the proof leg alone: `FetchStream.java` reverted to `5f20e1171bc890509d76e4f5f7defcc4fc8c102c`, branch tests kept. Record the failure content and say which of amendment 2's two contents it matches (Content 1 or Content 2). If it matches neither, record that and stop.
   - Do not re-run the other legs. Amendment 2 item 4 says the interrupted run needs no focused-head re-run.
   - If the proof leg matches, follow amendment 2 item 3 as written, including the plain fast-forward push to `solr-12505-submit`.

2. **SOLR-14919, re-run at `8f1f4ef91e`**, per the main-side re-run decision in `gates/SOLR-14919-processor-only-gate.md`.
   - Refresh `claims/pool-vm2-gate-backlog-14919.md` with a heartbeat line at pickup.
   - The earlier legs are recorded in run 2's log, so the re-run may cover the remaining focused-head test (fresh JUnit XML, seed recorded) and the module check. A full re-run is also allowed.
   - On green, the on-green steps in the gate file apply: refresh `receipts/SOLR-14919.md`, confirm the draft's citations, mark the job and the claim DONE.

3. **SOLR-17292, dated run of record**, per `gates/SOLR-17292-run-date.md`. This is a record job. It runs the focused class only if the original log has no dates.

## Holds (do not claim)

Updated 2026-10-13 after the held-item decisions. Record: `material/held-items-decisions-2026-10-13.md`.

- **SOLR-16013**: decided as a code fix, pushed at `ecda7b862ba`. The hardening gate is `gates/SOLR-16013-interrupt-fix.md`, OPEN and claimable by vm2 or vm1. The earlier hold is lifted.
- **SOLR-12998**: decided 2026-10-10 (gate GREEN, proof inconclusive by construction). No proof run is owed. The hold is cleared.
- **SOLR-7394**: decided. The fail-before clauses are cut and named as a follow-up. The changelog title is pushed at `a92a0d89b20`, and its check is `gates/SOLR-7394-changelog-title.md`, OPEN.
- **SOLR-18280**: the head rerun already ran and passed (25 of 25, 2026-10-13 14:23Z). The seed and reproduce line for the failing 12:55Z run are owed from vm2: `gates/SOLR-18280-failing-run-seed.md`. Do not relaunch.
- **SOLR-10667 and SOLR-12347**: blocked by the vm2 npm cache. vm1 takes them under the workaround, but its retry claims have had no heartbeat since 2026-10-11 and must be refreshed first. vm2 leaves both alone.
- **SOLR-18532**: queued on vm1 behind SOLR-18530.
- **pr-drift round 1, item (d), SOLR-11431**: already recorded in `receipts/SOLR-11431.md` at 2026-10-13T10:55Z. No run owed.

## Rules

- Claim before work. Heartbeat at least hourly. A claim with no heartbeat for 2 hours is stale, and a gate may be taken over only when its recorded log has stopped growing (WORKFLOW.md).
- A gate that ran tests and failed is not relaunched by another host on sight. Record the failure and report it.
- No fork push except the step a job file names. No PR is opened, closed, flipped or commented on. Nothing is posted publicly.
- A green gate writes its receipt on pr-prepare, marks its gate file DONE and marks its claim DONE in the same push sequence.
