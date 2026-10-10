# Pool assignment: vm2 gate and runs backlog, round 2

Capability tags: `gate`, `premise-run`, `bats`. Intended host: vm2, which has finished onboarding with full gate capability confirmed (hosts/vm2.md). Per WORKFLOW.md, any capable Linux host may claim a job if vm2 is busy; the Windows host never claims any of this (build work). Staffing: 1 to 2 subagents across the whole backlog; one job at a time per host.

Purpose: round 1 of this backlog (assignments/pool-vm2-gate-backlog-round-1.md) covers the gates and runs owed on branches already in the pipeline. Round 2 continues the supply so vm2 does not run dry: first gates for branches that have never been gated anywhere, one gate job file per job under `gates/`, each written from the branch's receipt. Round 1 has priority: a round 2 job is claimable once the claimant's round 1 jobs are done or handed back, and every round 2 job file carries that line. Every head below was verified by ls-remote on 2026-10-10 and matches its receipt exactly; no receipt had lagged.

Most of these branches arrived with a premise nobody has run. Where a receipt says the premise is unverified, or a branch's TESTING.md admits its reproduction was guessed, the job starts with a premise run and gates only if the premise holds. A premise that does not hold is a result, not a failed job: it is recorded in the receipt as NO GATE by finding with the run evidence, and the job and claim are marked DONE.

## Host note for vm2 (from hosts/vm2.md)

GRADLE_USER_HOME must point under /workspace on vm2. Its home directory lives on the small root filesystem (about 4.5G free), where a Gradle cache does not fit; the onboarding smoke run already pulled caches into the root filesystem once. Set GRADLE_USER_HOME to a path under /workspace for every run under this backlog. Open-file limit is 65536, which passes.

## The jobs

Work the groups in this order. Each line names the job file, which carries the branch, head, job type, proof shape and step checklist.

### Streaming expressions NO GATE pair

1. SOLR-9852 premise run, then first gate: `gates/SOLR-9852.md`. Head 31f58dbe8e6; fresh arrival, premise unverified, the premise run and first gate are recorded as owed.
2. SOLR-10882 premise run, then first gate: `gates/SOLR-10882.md`. Head 83fc3dfeb24; the inventory records no topic, the main file changed is ArrayEvaluator.java in the streaming eval package, and the premise is read from the ticket and the diff.

### SolrJ and clients NO GATE set

3. SOLR-3498 premise run, then first gate: `gates/SOLR-3498.md`. Head 812598302dee; the handoff claims ContentWriterUpdateRequest.setCommitWithin is a no-op.
4. SOLR-10364 premise run, then first gate: `gates/SOLR-10364.md`. Head 502bdbf033fa; bean binding collection types in DocumentObjectBinder. SOLR-4422 is gated on the same file; read the two diffs together, as the job file states.
5. SOLR-11356 premise run, then first gate: `gates/SOLR-11356.md`. Head 8474e5a3a26d; ConcurrentUpdateJettySolrClient stream reuse across credentials, module solrj-jetty.
6. SOLR-14187 premise run, then first gate: `gates/SOLR-14187.md`. Head 45b0f7ce34f; async admin helpers dropping per-request credentials on the completion wait.

### CLI and bin fresh arrivals

7. SOLR-10667 premise run, then first gate: `gates/SOLR-10667.md`. Head 32b594f280c; the premise is a distribution assembly without the fix showing the LTR example directory missing, then with it, plus the branch's test_modules.bats check, which has never been run.
8. SOLR-12347 premise verification, then first gate: `gates/SOLR-12347.md`. Head b77acba2ad6; the SOLR_STOP_WAIT default raise to 600 across the bin scripts, verified through test_start_solr.bats against base scripts and at the head.

### Security

9. SOLR-11678 premise run first, gate only if it holds: `gates/SOLR-11678.md`. Head 55d8cd189d1. The branch's TESTING.md admits the reproduction was guessed, never run. The premise is a keystore whose key password differs from its store password, unusable on base and usable at the head. If the premise fails, record and stop; no gate.
10. SOLR-12161 pin verification, then first gate: `gates/SOLR-12161.md`. Head 1725cbd8489. Test-only pin in BasicAuthIntegrationTest; the gate is the pin verification shape in its receipt: the test at the head and against base production, which settles pin versus live defect and the exception type the failure surfaces as.

### Build, docs and misc NO GATE set

11. SOLR-6430 premise verification, then first gate (docs shape): `gates/SOLR-6430.md`. Head 22f83870c4a; date sort order for null values and pre-1970 dates, documented in the field type page.
12. SOLR-7119 premise verification, then first gate (docs shape): `gates/SOLR-7119.md`. Head 9593f4bd0d6; tag and exclude local params for interval facets, documented in the faceting page.
13. SOLR-11700 premise verification, then first gate (docs shape): `gates/SOLR-11700.md`. Head c513388be05; WordDelimiterGraphFilterFactory token positions, documented in the filters page. A version-dependent finding has to name every Lucene version that applies (10.4.0 on main and branch_10x, 9.12.3 on branch_9x).
14. SOLR-17356 premise verification, then first gate (docs shape): `gates/SOLR-17356.md`. Head ea7fc15cade; the rewritten Ukrainian language analysis page, checked against the current analysis factories. SOLR-16914 edits the same page in a different section.
15. SOLR-16322 premise exercise, then first gate in the shape that fits: `gates/SOLR-16322.md`. Head 65e0b8d7c79; the failed-tests-at-end Gradle script should print the failing task's seed. No test gate shape fits a Gradle script change directly; the job file names the steps that do.
16. SOLR-17722 premise run, then first gate: `gates/SOLR-17722.md`. Head fee3a26beb3; CrossDC update content type param mirroring. The register calls the premise unproven and no premise run exists; this job runs it. The retire call is the owner's and is not taken here.

Excluded from this round: SOLR-9039, whose premise platform is OSX, which neither Linux host can run; and SOLR-13705, which is already in round 1 as a premise job.

## Claim protocol

- One job at a time per host. To claim a job: set the job file's Status line to CLAIMED with your host name and a UTC timestamp, and add a claim file at `claims/pool-vm2-gate-backlog-<ticket>.md`. Push both in one commit; first push wins.
- Heartbeat: append a heartbeat line with a fresh UTC timestamp to the claim file at least once an hour while the job runs, per WORKFLOW.md. A claim with no heartbeat for 2 hours is stale and may be taken over under the WORKFLOW.md rules; a gate claim additionally needs its recorded log stopped.
- Gate jobs follow the standard gate: changelog parse, tidy with a clean tree afterward (a tidy-modified tree is GATE INVALID), Error Prone compile, the proof leg the job file specifies, focused tests counted from fresh JUnit XML, module check. Update the job file's step checklist as each step finishes. The docs-shaped and build-tooling jobs follow the step lists in their own job files instead; where a standard step does not apply, the job file says so.
- Premise outcomes: where a job starts with a premise run or verification, the gate steps run only if the premise holds. If it does not hold, the claimant records the outcome in `receipts/<TICKET>.md` as NO GATE by finding with the run evidence, marks the job file DONE, and marks the claim DONE, in the same push sequence. That is a completed job.
- On green: the claimant writes or refreshes `receipts/<TICKET>.md` itself at the gated head, marks the job file DONE, and marks the claim DONE, in the same push sequence.
- On failure: a gate or run that executed tests and failed is recorded in the job file and the claim, and the claimant stops. No relaunch, no retry, no fixing the branch under this backlog; the main agent investigates. A run that never executed tests (environment refusal, VM replacement) may be relaunched once by the claimant, recorded in the job file.
- Packaging folded into a job (TESTING.md or handoff-note removal, where the job file records it) is done by the claimant as a packaging commit before the gate steps, and only after the premise holds where the job has one. The gate covers the packaged head, which the claimant records in the job file. No other branch edits under this backlog.

## Rules that bind this backlog

- Proof numbers and counts come only from the run's own log and fresh JUnit XML, recorded in the job file and the receipt. A step that passes on failure count alone has not proven anything; proof legs check the failure is the premise failure, as each job file states.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text.
- At most 6 to 7 subagents running at once in total across all hosts combined; this backlog's staffing of 1 to 2 counts toward that total.
- PR openings and decision slates stay with the main agent. Nothing in this backlog opens a PR or takes a DISCUSS call.
