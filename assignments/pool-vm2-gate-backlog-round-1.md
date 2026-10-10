# Pool assignment: vm2 gate and runs backlog, round 1

Capability tags: `gate`, `premise-run`, `settling-run`, `bats`. Intended host: vm2, which has finished onboarding with full gate capability confirmed (hosts/vm2.md). Per WORKFLOW.md, any capable Linux host may claim a job if vm2 is busy; the Windows host never claims any of this (build work). Staffing: 1 to 2 subagents across the whole backlog; one job at a time per host.

Purpose: the main side owes a backlog of gates and focused runs recorded in the category answers files. vm2 reported no further assignments addressed to it; this backlog names the jobs, one gate job file per job under `gates/`, each verified against its receipt and the answers file that records the debt. Every head below was verified by ls-remote on 2026-10-10 and matches its receipt exactly.

## Host note for vm2 (from hosts/vm2.md)

GRADLE_USER_HOME must point under /workspace on vm2. Its home directory lives on the small root filesystem (about 4.5G free), where a Gradle cache does not fit; the onboarding smoke run already pulled caches into the root filesystem once. Set GRADLE_USER_HOME to a path under /workspace for every run under this backlog. Open-file limit is 65536, which passes.

## Tranche 1: claimable now

Work the jobs in this order. Each line names the job file, which carries the branch, head, focused classes, proof shape and step checklist. Items 1 and 2 are finished and are not claimable; they stay in the list so the numbering is stable.

1. SOLR-12998 live-tip gate: `gates/SOLR-12998.md`. Run finished 2026-10-10T19:17Z; gate steps green at 62a17a116b5; proof leg blocked (the branch test sources do not compile at merge-base); main agent decision pending; not claimable. Do not re-run.
2. SOLR-18391 graceful-create gate: `gates/SOLR-18391.md`. DONE 2026-10-10T19:45Z; gate green at adcda10b501; proof leg mismatch resolved by the main agent decision recorded in receipts/SOLR-18391.md (ACCEPTED). Do not re-run; not claimable.
3. SOLR-4502 first gate with premise run: `gates/SOLR-4502.md`. Head 4491f5162c1; premise run first (create on an unloaded container, search fails with the NPE), packaging removes the TESTING.md the tip adds, focused list is the new test plus the rest of TestCoreContainer.
4. SOLR-5011 first gate with premise run: `gates/SOLR-5011.md`. Head f20ffe48078 (ticket 5011). Per the audit correction in gates/SOLR-5011.md, no branch test covers the shared-schema scenario; the job file's corrected proof shape and record-only path govern.
5. SOLR-12916 first gate with premise run: `gates/SOLR-12916.md`. Head ebe5db37433; premise per the corrected shape in gates/SOLR-12916.md (QuerySenderListenerTest.testFlatNameValueQueryFromConfigApi on base and at head, then the round trip or the stated limit); gate at the cleaned head after packaging.
6. SOLR-16499 first gate with premise run: `gates/SOLR-16499.md`. Head 6a2ff7618d9 (the origin tip; do not use any stale local branch). The GitHub corroboration run at this head is not a gate.
7. SOLR-5262 premise run only: `gates/SOLR-5262-premise.md`. Head ade8b80264a. Three showings: the ${solr.core.ulogDir} config fails on base; testUlogDirDefaultsToDataDir fails on base; both pass at the head. No gate steps; the first gate is a later job.
8. SOLR-9091 first gate: `gates/SOLR-9091.md`. Head e31bdaa4d27, with a focused fail-before proof. Packaging folded into the job: TESTING.md removal and the test comment correction whose replacement is already written in the Replication report (part g1, finding 3).
9. SOLR-9382 first gate: `gates/SOLR-9382.md`. Head c0b5fec1be2. Packaging folded into the job: the changelog title replacement with the correct example path, TESTING.md removal, and the two over-length lines, all recorded in the Replication answers before the gate.
10. TestRestoreCore recount: `gates/SOLR-9865-17287-recount.md`. One focused run at each head (9865 at 4937608bb18, 17287 at 6957daf8261), counted from fresh JUnit XML, settling receipts that say 4 tests against head files that declare 3 @Test methods. One confirmation settles both drafts' [CONFIRM: count] placeholders.
11. SOLR-11650 base run: `gates/SOLR-11650-baserun.md`. Base run for testFollowerDetailsRedactLeaderUrlPassword at head e4f5e941cd8; the branch is already gated green, and this case has no base run yet.
12. SOLR-10390 premise run: `gates/SOLR-10390-premise.md`. Head 4af4a6834e2. The branch's BATS test under a PATH without lsof, against base bin/solr and at the head, so the /dev/tcp fallback is the path exercised.
13. SOLR-13705 premise run: `gates/SOLR-13705-premise.md`. Head 5f141fb2af3. The deterministic reflection spec: test files only on base 9b3a84b1c460, where testLazilyInitializedSingletonIsVolatile is expected to fail; the same class at the head must pass.

## Claim protocol

- One job at a time per host. To claim a job: set the job file's Status line to CLAIMED with your host name and a UTC timestamp, and add a claim file at `claims/pool-vm2-gate-backlog-<ticket>.md` (for the recount job, `claims/pool-vm2-gate-backlog-9865-17287-recount.md`; for the run jobs, the job file's ticket stem). Push both in one commit; first push wins.
- Heartbeat: append a heartbeat line with a fresh UTC timestamp to the claim file at least once an hour while the job runs, per WORKFLOW.md. A claim with no heartbeat for 2 hours is stale and may be taken over under the WORKFLOW.md rules; a gate claim additionally needs its recorded log stopped.
- Gate jobs follow the standard gate: changelog parse, tidy with a clean tree afterward (a tidy-modified tree is GATE INVALID), Error Prone compile, the proof leg the job file specifies, focused tests counted from fresh JUnit XML, module check. Update the job file's step checklist as each step finishes.
- On green: the claimant writes or refreshes `receipts/<TICKET>.md` itself at the gated head (for run jobs, records the outcome in the named receipt or receipts), marks the job file DONE, and marks the claim DONE, in the same push sequence.
- On failure: a gate or run that executed tests and failed is recorded in the job file and the claim, and the claimant stops. No relaunch, no retry, no fixing the branch under this backlog; the main agent investigates. A run that never executed tests (environment refusal, VM replacement) may be relaunched once by the claimant, recorded in the job file.
- Packaging folded into a job (TESTING.md removal, the recorded wording and title corrections for 9091 and 9382) is done by the claimant as packaging commits before gating, and the gate covers the packaged head, which the claimant records in the job file. No other branch edits under this backlog.

## Second tranche: named, not yet claimable

These jobs need branch work or an owner call first. They are listed so vm2 can see them coming; do not claim or start them until a follow-up assignment or a job file marks them claimable.

- SOLR-11479: after its branch corrections land (SolrCloud round 1 answers).
- SOLR-8430: after its limiter cap fix lands; the job then is a focused re-run at the new head (Replication answers).
- SOLR-17708: after its changelog title fix lands; the job then is a top-up run (Core admin answers).
- SOLR-6711: after Nick's call on the persistence default (Replication answers, DISCUSS item); the job then is its first gate.
- SOLR-5589: after its disable-rule fix lands. The Replication answers adopt a production fix (part g3, finding 10: disable only when a leader section is present and disabled) plus a changelog title replacement, and record the first gate as owed after those fixes. That is implementation work before any run, so this ticket is not a pure run and sits here rather than in tranche 1. Once the fix lands, its first gate joins this backlog by job file.

## Rules that bind this backlog

- Proof numbers and counts come only from the run's own log and fresh JUnit XML, recorded in the job file and the receipt. A step that passes on failure count alone has not proven anything; proof legs check the failure is the premise failure, as each job file states.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text.
- At most 6 to 7 subagents running at once in total across all hosts combined; this backlog's staffing of 1 to 2 counts toward that total.
- PR openings and decision slates stay with the main agent. Nothing in this backlog opens a PR or takes a DISCUSS call.
