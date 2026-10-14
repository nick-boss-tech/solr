# Gate job: SOLR-16887 restore -XX:ErrorFile, rename the BATS test, then the gate and BATS run at the new head

- Status: FAILED at the head BATS leg (gate run 2026-10-14T14:30:50Z to 15:10:03Z). Not relaunched; left for the main agent. See Gate run record below. Posted by the review lane (round 3, census follow-up S5, 2026-10-14).
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
