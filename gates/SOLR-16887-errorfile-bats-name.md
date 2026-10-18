# Gate job: SOLR-16887 restore -XX:ErrorFile, rename the BATS test, then the gate and BATS run at the new head

Status: CLAIMED (claims/pool-vm1-gate-backlog-16887.md)
- Branch: solr-16887-submit. Live head: 42675f65d6fc4362e427d3b4927801466b062ca6 (verified by `git ls-remote origin refs/heads/solr-16887-submit` on 2026-10-14; matches the gated head in `receipts/SOLR-16887.md`).
- Merge base: 9b3a84b1c460981eab09d8ffaef776acc4a184f8 (`git merge-base upstream/main 42675f65d6f`).
- Intended host: a Linux host with the Gradle toolchain and BATS, under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: implementation, gate, bats.
- Job type: branch-side edits adopted in the answers record, then the hardening gate and a BATS run at the new head. The patching host pushes the branch once the gate is green.

## Why this job exists

The receipt calls the branch PR-ready. That verdict predates two adopted fixes, and neither is on the branch at the live head:

- **Dropped ErrorFile.** The branch removes `-XX:ErrorFile=${SOLR_LOGS_DIR}/jvm_crash_%p.log` from `solr/bin/solr` and `solr/bin/solr.cmd`, and from the ref guide example. The ticket asks only for Exit instead of Crash. The flag also sets where the JVM writes a crash file for every fatal error, so removing it moves those files to the JVM default location. Nothing in the changelog or the guide says so.
- **BATS test name.** `solr/packaging/test/test_start_solr.bats` names a test "solr exits on OutOfMemoryError instead of crashing". The test starts Solr and checks the system info text. It causes no OOM.

Sources: `material/core-admin-round-1-answers.md` (SOLR-16887 entry: "restore `-XX:ErrorFile` in both scripts ... rename the BATS test"), and `reports/core-admin-round-1-k6.md` (FIX 6 and FIX 7, with replacement text, and NOTE 8 on heap dumps). The draft `pr-drafts/core-admin/SOLR-16887.md` is not written until this job is DONE.

## Edits (exact)

1. `solr/bin/solr`, the flag line at the head (line 1332). Keep the comment above it as is. Replace

   `    "-XX:+ExitOnOutOfMemoryError" \`

   with

   `    "-XX:+ExitOnOutOfMemoryError" "-XX:ErrorFile=${SOLR_LOGS_DIR}/jvm_crash_%p.log" \`

2. `solr/bin/solr.cmd`. Directly after the line `set START_OPTS=%START_OPTS% -XX:+ExitOnOutOfMemoryError`, add

   `set START_OPTS=%START_OPTS% -XX:ErrorFile="%SOLR_LOGS_DIR%\jvm_crash_%%p.log"`

3. `solr/solr-ref-guide/modules/configuration-guide/pages/system-info-handler.adoc`, around line 236. Directly after `"-XX:+ExitOnOutOfMemoryError",`, add

   `"-XX:ErrorFile=/opt/solr/../logs/jvm_crash_%p.log",`

4. `solr/packaging/test/test_start_solr.bats`, line 135. Replace

   `@test "solr exits on OutOfMemoryError instead of crashing" {`

   with

   `@test "solr starts with ExitOnOutOfMemoryError" {`

Do not change the changelog fragment or the other hunks. If an edit does not match the text above at the head, stop, and record the difference here before going on.

## Steps

1. Apply the four edits. Commit them as the ticket's branch commit, with the commit identity the branch already uses (Nick Shanin). Check `git diff 42675f65d6f` shows only these four files.
2. Changelog YAML parse of `changelog/unreleased/SOLR-16887-exit-on-oom.yml` with a real YAML parser.
3. Tidy, then Error Prone compile, then the module check, as the receipt's r35 gate ran them. A tidy change to another file invalidates the run; record it here if that happens.
4. BATS premise leg: the merge base `9b3a84b1c460` with the head's `test_start_solr.bats` overlaid. Expected: the test fails at the flag assertion, because the base system info carries `CrashOnOutOfMemoryError`.
5. BATS head leg at the new head, using the runner that produced `g16887r35-gate-head.log`. Expected: the renamed test passes ("ok 1"), with the flag assertion passing and no `CrashOnOutOfMemoryError` in the output.
6. Read the system info output from the head leg and record whether `-XX:ErrorFile=` appears in it, with the path form.

## Gate failure rule

If a step runs tests and records a failure, do not relaunch. Record the failing test, the log line, and a classification in this file first. The main agent decides on any failure the lane cannot classify.

## Gate run record (FAILED)

- Gated head: 8c1a3faa530271a471393f3acf313d9ff7dacf2b, a local commit on 42675f65d6 with the four edits applied. Not pushed; the push waits for green. The live fork head is still 42675f65d6.
- Steps 1 to 3 (edits, changelog YAML parse, tidy, Error Prone compile, module check): rc=0 each, tree clean after tidy.
- Step 4, premise leg at 9b3a84b1c460: rc=1, but not at the flag assertion. Failing test: "solr starts with ExitOnOutOfMemoryError", `test_start_solr.bats` line 137: `solr assert --started http://localhost:${SOLR_PORT} --timeout 5000` failed with status 100. Log: "ERROR: Solr is not running on url http://localhost: after 5 seconds". Solr had logged "Started Solr server on port 8983". The flag assertion never ran, so the premise is not established.
- Step 5, head leg at 8c1a3faa53: rc=1, same test, same line, same log lines. The renamed test did not reach its flag check. Step 6 not recorded: the system info request was never made, and the head leg shows no `-XX:ErrorFile=` line.
- Classification: not shown to be a branch defect. The Gradle bats task sets `SOLR_PORT` (`solr/packaging/build.gradle` lines 329-330), and the runner `g16887-gate.sh` calls bats directly without it, so the assertion URL has an empty port. Suspected runner defect, not confirmed by a rerun. Not relaunched. The main agent decides whether to re-run with `SOLR_PORT` and `SOLR_PORT_LISTEN` set as the Gradle task sets them.
- Evidence: `/tmp/16887-tests/premise.xml`, `/tmp/16887-tests/head.xml`, failure snapshot `/tmp/16887-tests/head/failures/1-1`, logs `/workspace/gates/logs/vm2/16887-*.log`, run output `queue-g16887-gate-20261014T143050Z.out`. Gate worktree `/workspace/gates/solr-16887-gate` kept at 8c1a3faa53 for inspection.

## Main agent classification (2026-10-14): runner defect, confirmed by the code

The suspected runner defect is confirmed. Classification: (a) runner defect. The branch change is not implicated, and this is not a port collision or a timing flake.

Evidence:

- Both legs failed identically at `test_start_solr.bats` line 137 with the assertion URL `http://localhost:` (empty port). The premise leg runs merge-base production `9b3a84b1c460` with only the head's BATS file overlaid, so a defect in the branch change cannot produce that failure; the failure sits in the harness both legs share.
- Solr itself started in both legs (the log records "Started Solr server on port 8983"). The branch's `bin/solr` start path works; what failed is the test's own started-check against a URL with no port.
- `SOLR_PORT` has exactly one source: the Gradle bats task in `solr/packaging/build.gradle` exports it (`environment SOLR_PORT: solrPort`, alongside `SOLR_PORT_LISTEN`, `SOLR2_PORT`, `SOLR3_PORT`, `ZK_PORT`). Nothing in `test_start_solr.bats` or `bats_helper.bash` sets or defaults it. The runner `g16887-gate.sh` invoked `bats` directly without exporting any of them, so `${SOLR_PORT}` expanded empty in every `${SOLR_PORT}` assertion in the file, not just this test's.
- The r35 gate that stands GREEN at `42675f65d6f` ran through a runner that supplied this environment, which is why the same assertion passed there. The premise for this ticket was already established in that run (base fails at the flag assertion), so this run's premise leg failing early does not reopen the premise.

Re-run requirement (the defect to not repeat): the runner must run the BATS legs through the Gradle bats task, or export `SOLR_PORT`, `SOLR_PORT_LISTEN`, `SOLR2_PORT`, `SOLR3_PORT` and `ZK_PORT` the way `solr/packaging/build.gradle` sets them before invoking `bats` directly (a free base port, with `SOLR_PORT_LISTEN` equal to `SOLR_PORT`). Steps 1 to 3 already passed at `8c1a3faa530271a471393f3acf313d9ff7dacf2b` and do not need redoing; the re-run covers steps 4 to 6 only, at that same head, which is kept in the vm2 gate worktree `/workspace/gates/solr-16887-gate`. This job is OPEN and claimable by a Linux host under the WORKFLOW.md claim rules; the failed claim `claims/pool-vm2-gate-backlog-16887.md` is discharged by this classification.

## Classification of the escalated failure (2026-10-15): runner defect, confirmed again from the record and the code

The escalation added to `claims/pool-vm2-gate-backlog-16887.md` by the vm2 audit of 2026-10-16T00:56Z records no new run. It re-raises the single follow-up run of 2026-10-14 (run output `queue-g16887-gate-20261014T143050Z.out`, gated head `8c1a3faa530`), and no re-run of steps 4 to 6 appears in any record since the re-issue in the 2026-10-14 classification above. That classification therefore stands, restated here with its deciding points verified against the code at `42675f65d6f`:

- The failure sits in the harness, before the assertions this gate exists to check. Both legs stopped at the started-check (`test_start_solr.bats` line 137, `solr assert --started http://localhost:${SOLR_PORT} --timeout 5000`) with status 100 and the URL rendered as `http://localhost:` (empty port), while Solr logged "Started Solr server on port 8983" in both legs. The flag assertion never ran, and step 6's system info request was never made.
- A branch defect cannot produce this failure: the premise leg runs merge-base production `9b3a84b1c460` with only the head's BATS file overlaid, and it failed identically. A test defect is excluded as well: the same started-check and flag assertion passed in the r35 gate at `42675f65d6f` when the runner supplied the port environment, so the test's assertions are satisfiable and were met at this branch's gated head. A product startup failure is excluded because Solr did start; the check polled a portless URL.
- `SOLR_PORT` has exactly one source: the Gradle bats task in `solr/packaging/build.gradle` picks a free base port and exports the family at lines 329 to 333 (`SOLR_PORT`, `SOLR_PORT_LISTEN`, `SOLR2_PORT` at +1, `SOLR3_PORT` at +2, `ZK_PORT` at +1000). Nothing under `solr/packaging/` assigns it, and neither the test file nor `bats_helper.bash` sets or defaults it. The runner `g16887-gate.sh` invoked `bats` directly without that environment, so every `${SOLR_PORT}` URL in the run was portless.

No BATS leg was re-run for this classification: the record and the code settle it, and the gated head `8c1a3faa530` exists only in the vm2 gate worktree (the fork head is still `42675f65d6f`), so it is not available to run elsewhere.

Next step for the claim holder: re-run steps 4 to 6 only, at `8c1a3faa530` in the kept vm2 gate worktree `/workspace/gates/solr-16887-gate`, through the Gradle bats task, or with `SOLR_PORT`, `SOLR_PORT_LISTEN`, `SOLR2_PORT`, `SOLR3_PORT` and `ZK_PORT` exported the way `solr/packaging/build.gradle` sets them. Per the 2026-10-14 ruling, that is an environment correction to a harness failure, not a relaunch of a gate whose assertions ran and failed. Steps 1 to 3 stand as passed at that head and do not need redoing. On green, follow the On green section below.

## On green

- Push the branch with a plain fast-forward to `solr-16887-submit`. Record the new head here.
- Refresh `receipts/SOLR-16887.md` with the new head, the BATS counts from the head leg under the new test name, the premise result at the merge base, and a line recording that the ErrorFile restore and the test rename landed in this head. Remove the "PR-ready" wording until the main agent decides.
- Mark this job DONE here and the claim DONE, in the same push as the receipt.

## Step checklist

- [ ] step 1: the four edits applied; diff against `42675f65d6f` shows only those files; commit with the branch identity
- [ ] step 2: changelog YAML parse
- [ ] step 3: tidy, Error Prone compile, module check at the new head
- [ ] step 4: BATS premise leg at `9b3a84b1c460`
- [ ] step 5: BATS head leg at the new head under the renamed test
- [ ] step 6: ErrorFile presence in the head-leg system info recorded
- [ ] push to `solr-16887-submit`; receipt refreshed; job and claim DONE

## vm3 relaunch record (2026-10-17 to 2026-10-18): BLOCKED on the BATS teardown, not a verdict

- Head: b0223fd235 on the gate worktree (rebuilt from 42675f65d6 with the four edits; content matches the job file, the commit hash differs from 8c1a3faa53 because the commit timestamp differs). Steps 1 to 3 are not repeated under this relaunch; the fork branch is not pushed.
- Host gaps found on this fresh VM: no lsof (bin/solr falls back and prints the "Please install lsof" note; the stop path needs it), and the JS client's npm prepare step cannot find @babel/plugin-syntax-dynamic-import. The Gradle switch -PdisableJsClient=true is used for the BATS legs (documented in settings.gradle; the BATS tests do not use the admin UI).
- Step 4, premise leg at 9b3a84b1c4 with the head's BATS file overlaid, run directly from the packaging dir with the Gradle task's environment: not ok, at test/test_start_solr.bats line 140, assert_output --partial "-XX:+ExitOnOutOfMemoryError". The base system info lists -XX:+CrashOnOutOfMemoryError and no Exit flag. This matches the expected premise. Log: gates/logs/vm3/16887-premise-direct.log.
- Full-file run via Gradle (premise overlay, -PdisableJsClient=true): test 1 (SOLR-11740 stop connection) failed because the stop path could not confirm the listener without lsof; stopped after test 1. Log: gates/logs/vm3/16887-premise-run4.log.
- Step 5, head leg at b0223fd235, direct BATS on the target test: the test body passes (the flag assertion finds -XX:+ExitOnOutOfMemoryError in the system info) but teardown fails at `solr stop --all`. Two runs: without lsof (gates/logs/vm3/16887-head-direct.log) and with an ss-backed lsof shim on PATH (gates/logs/vm3/16887-head-direct-shim.log). On this host a graceful stop does not end the Solr JVM: the Jetty shutdown service logs a stop command, the JVM keeps running, and the script forces a kill. Cause not established; this is an environment issue, not the branch change, but it is unresolved.
- Step 6 (system info ErrorFile path) not reached.
- Verdict for now: BLOCKED. No product result is recorded, so the gate is neither GREEN nor FAILED. Not relaunched until the teardown stop path is understood. The branch is not pushed.

## Handoff to vm1 (2026-10-18, from vm3)

vm3 releases this job. vm1 should claim it under claims/pool-vm1-gate-backlog-16887.md.

- Head to gate: b0223fd235 (the four edits in the job file). Patch: `gates/SOLR-16887-head-b0223fd235.patch`, a diff from 42675f65d6 that reverses cleanly on b0223fd235. Apply it on a fresh worktree of 42675f65d6 with `git apply`, then commit as Nick Shanin. The commit hash will differ from b0223fd235 unless the timestamp matches, so record the new hash in the receipt.
- Steps 1 to 3 (edits, changelog parse, tidy, Error Prone, module check) were not run on vm3 for this head. Steps 1 to 3 at 8c1a3faa53 passed on vm2; rerun them at the new head.
- Use `-PdisableJsClient=true` for every Gradle run here. The JS client npm prepare step fails with a missing `@babel/plugin-syntax-dynamic-import` on a fresh host, and BATS does not use the admin UI. Record that flag in the receipt as a deviation.
- Run the BATS legs with `SOLR_PORT`, `SOLR_PORT_LISTEN`, `SOLR2_PORT`, `SOLR3_PORT` and `ZK_PORT` set as `solr/packaging/build.gradle` sets them, or through `:solr:packaging:integrationTests --tests test_start_solr.bats` (the task that did run on vm3, premise leg only).
- Install lsof on vm1 before the BATS run. vm3 had no lsof, and the stop path depends on it.

What vm3 found (for vm1 to check, not a settled cause):

- Premise leg at 9b3a84b1c4 with the head's BATS file overlaid: fails at test/test_start_solr.bats line 140 (`assert_output --partial "-XX:+ExitOnOutOfMemoryError"`). The base system info shows `CrashOnOutOfMemoryError`. This is the expected premise.
- Head target test (`solr starts with ExitOnOutOfMemoryError`): the test body passes, including the flag check. The failure is in teardown, where `solr stop --all` returns 1. Two runs (with and without an ss-backed lsof shim) show the same result.
- On vm3, `solr stop` sends the Jetty stop command, and `solr.log` shows "Performing 'stop' command" followed by "Exiting JVM". The Solr JVM still ran about 17 seconds later, with the main thread parked and no shutdown progress. The script then force-killed it with `kill -9`, which the script reports as "ERROR: Failed to kill previous Solr Java process".
- Hypothesis, not confirmed: a non-daemon thread (possibly the embedded ZooKeeper server started on port 39900 for this run) keeps the JVM alive after the stop. vm1 should check this with a thread dump taken during the stop wait, and check whether the same happens without the flag change at 9b3a84b1c4.
- The `/workspace/tools/shim/lsof` script on vm3 is a local workaround, not part of the repo. Do not use it for the record.

Records on vm3: `gates/logs/vm3/16887-premise-direct.log` (premise), `16887-head-direct.log` and `16887-head-direct-shim.log` (head). The vm3 worktree `/workspace/worktrees/16887` is detached at b0223fd235 and stays until vm1 confirms the patch matches.
