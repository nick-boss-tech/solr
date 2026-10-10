# CLI, bin scripts and packaging round 1, part S6: SOLR-12347 (audit only), the bin/solr landing order, the test-file reconciliation, and Windows parity

Worktree at `76fde6cfa6a`. Read-only: no builds, Gradle, BATS, tests, `gh` writes, commits, pushes or file edits. `merge-tree --write-tree` wrote unreferenced tree objects only. The ticket JSON was read for SOLR-12347 and SOLR-17029 (read only).

## Part A: SOLR-12347 (audit only, head `b77acba2ad60`, base `b6b2b8f1`; receipt NO GATE)

**Premise.** The ticket (`research/jira-context/SOLR-12347.json`) asks to "Raise the default `SOLR_STOP_WAIT` from 3 minutes", because clean shutdowns with many committing cores often miss 3 minutes. Mark Miller's one comment suggests "10-20 minutes even" and says a forced kill causes tlog replay on restart. The ticket gives no number. The branch uses 600 seconds, the low end of that range. Nothing measured exists: the receipt is NO GATE, and the TESTING.md note says nothing was run. The premise that 180 seconds is too short is the reporter's claim.

**Every place the default is stated** (head `b77acba2ad60`):
- `solr/bin/solr` line 155: `: "${SOLR_STOP_WAIT:=600}"` (changed from 180).
- `solr/bin/solr` line 154: `: "${SOLR_START_WAIT:=${SOLR_STOP_WAIT:-180}}"` (the start default is 180 and follows an explicit `SOLR_STOP_WAIT`, as before).
- `solr/bin/solr.cmd` lines 823 and 824: `set SOLR_STOP_WAIT=600` in `:stop_solr` (changed).
- `solr/bin/solr.cmd` lines 1188 and 1189: `set SOLR_START_WAIT=180` (unchanged; the Windows start default is independent of the stop wait).
- `solr/bin/solr.in.sh` line 27: `#SOLR_STOP_WAIT="600"` (a comment, changed).
- `solr/bin/solr.in.sh` line 32: `#SOLR_START_WAIT="180" # follows SOLR_STOP_WAIT when that is set` (a comment, changed; the example value and the "follows" wording disagree, and uncommenting pins 180).
- `solr/bin/solr.in.cmd` line 31: `REM set SOLR_STOP_WAIT=600` (a comment, changed).
- `solr/bin/solr.in.cmd` line 36: `REM set SOLR_START_WAIT=30` (unchanged; a stale example, not a default).
- `solr/solr-ref-guide/modules/deployment-guide/pages/solr-control-script-reference.adoc` line 400: "The command will wait up to 180 seconds ... then will forcefully kill the process (kill -9)." (UNCHANGED, now wrong).
- `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc` lines 738 to 743: the 9.x upgrade note (historical, unchanged).
- `solr/bin/systemd/solr.service` line 29, `ExecStop=.../bin/solr stop`, and line 32, `TimeoutSec=180s` (unchanged).
- `bin/solr` stop usage (lines 436 to 446): no default stated; no usage text mentions the wait.
- `changelog/unreleased/SOLR-12347-stop-wait-default.yml` line 2: "now 600 seconds (was 180)"; start "keeps its 180 second default".
- `SOLR-12347-TESTING.md` line 25 says "Ref guide has no page naming the 180 default." This is contradicted by ref guide line 400. TESTING.md line 24 lists systemd stop timeouts as "not checked"; `solr.service` line 32 answers it.

All four script places read 600 and match each other. The ref guide and the systemd unit were not changed.

**What a 600-second default does on a hung node** (code read, not run):
- Unix `stop_solr` (`bin/solr` line 513) sends the Jetty stop at lines 524 and 525 (not time-bounded by the script), then polls the PID every 2 seconds while elapsed is under `SOLR_STOP_WAIT` (lines 536 to 541), with the spinner (line 546).
- After expiry (lines 556 to 567): a jstack or jattach threaddump with no timeout, then `kill -9` (line 564), then a 10-second sleep. The script does force-kill. A hung node dies about 610 seconds after stop starts, not about 190 seconds. The change moves the kill later; it does not remove it.
- `stop --all` (the loop near line 947) runs one `stop_solr` per PID file, sequentially, so N hung nodes take about N times 610 seconds.
- Windows: `wait_for_process_exit` scales 600 down to 570 seconds (`solr.cmd` lines 1308 to 1311), then runs jstack if present and `taskkill /f`. No record states the 570 seconds.
- systemd: `ExecStop=bin/solr stop` under `TimeoutSec=180s`. systemd stops waiting at 180 seconds, so under the packaged unit the 600-second default is never reached. The branch does not touch this file.
- Docker: not checked.

**Verdict: partly verified.** The value is consistent across the four script files and is the low end of the commenter's range. The start default stays at 180. The ref guide and the systemd unit still encode 180, so this is not yet a complete default change. The premise is unmeasured.

**First verification** (main side, owed):
1. BATS `test_start_solr.bats` at `b77acba2ad60` (packaged distribution, non-root). Control on base `b6b2b8f1`: must fail the 600 assertion.
2. The test name "start wait stays at three" does not match its body, which asserts that start follows `SOLR_STOP_WAIT=45`, not the 180 default. Rename, or add a default-start assertion.
3. Hung-node timing: SIGSTOP a node, run `solr stop -p` with the default, and time it. Expect the kill at about 610 seconds. Check that the `--stop` call and the jstack step do not block the kill (not traced).
4. systemd: measure `ExecStop` against `TimeoutSec=180s` on a Linux host (owed).
5. Windows: `solr.cmd` stop on a Windows host (no cmd.exe on the VM): confirm 570 seconds and the taskkill path (owed).
6. Fix the ref guide line 400 and TESTING.md lines 24 and 25 before any draft.

## Part B: `solr/bin/solr`

**Pairwise `git merge-tree --write-tree`** (21 pairs):
- **Clean (16):** 7924/9342, 7924/10390, 7924/18132, 7924/18339, 9342/10390, 9342/12347, 9342/18132, 9342/18339, 10390/12347, 10390/17029, 10390/18132, 12347/18132, 12347/18339, 17029/18132, 17029/18339, 18132/18339.
- **Conflict, test file only, all at the end of the file** (each adds after the last test): 7924/12347, 7924/17029, 12347/17029.
- **Conflict, `bin/solr`:** 9342/17029 (tree `500892379aa`). One hunk at the foreground `exec` and background `nohup` lines. 9342 wraps them in `env TZ="${TZ:-$SOLR_TIMEZONE}"`; 17029 changes `$SOLR_ADDL_ARGS` to `"${SOLR_ADDL_ARGS_ARR[@]}"`. The composition, not run: keep the array and add the env prefix to both lines.
- **Conflict, `bin/solr`:** 10390/18339 (tree `3bc9e8c5d65`). One hunk, the start-wait block. Resolution: keep 18339's block.

**Composition points read (not run):**
- 7924/18339 (clean): 18339 removes the start-side `spinner $!`. The spinner function survives for stop only (line 546 at the 18339 head). The 7924 BATS test sources the function by `sed`, so it still works.
- 12347/18339 (clean): 12347's start default feeds `SOLR_START_WAIT`, which 18339 passes as `--max-wait-secs`. The "Waiting up to N seconds" message now comes from `StatusTool.java` line 194 (the 12347 assertion depends on it, not on the `bin/solr` echo that 18339 removes).
- 18132/18339 (clean): 18132's `-Dsolr.ssl.enabled=true` (`bin/solr` line 216) reaches `run_tool`, including the status call. Correct for SSL clusters.
- `SOLR_URL_SCHEME` (used by 18339) is set at `bin/solr` lines 201 and 218, before `start_solr`.

**SOLR-10390 and SOLR-18339 meet head-on:**
- If 18339 lands first, nothing of 10390 remains in `bin/solr`. At base `cabedd1d` the only lsof probes are in the start wait (lines 1385 and 1391), plus the "install lsof" notice (line 1410). Stop and status use ps (`solr_pid_by_port` line 469, `solr_port_listen` line 481). After 18339, `port_is_listening` has no caller. `install_solr_service.sh` line 199 (the lsof recommendation) is also removed by 18339. What is left of 10390 is its test (the fake lsof would then exercise `StatusTool`, not the fallback), its changelog, and its TESTING note. Recommendation: withdraw or rescope the `bin/solr` hunk.
- If 10390 lands first, 18339's wait is `run_tool status` over HTTP. It needs no lsof and no `/dev/tcp` fallback. The conflict resolution deletes `port_is_listening`. 18339 does not depend on 10390.

**SOLR-7924 against the start wait:** after 18339, the spinner is no longer on the start path. The 7924 premise (the start spinner) must be re-scoped to the stop spinner, or retired.

**Landing order (recommended):**
1. **SOLR-18339 first.** It rewrites the whole start-wait block that 10390 and 7924 touch, and it removes all lsof use from `bin/solr` and `install_solr_service.sh`. The gate is green at `47e53884609c` (11 of 11 BATS; the control fails the two new tests). Timing is owner-held (SOLR-18336).
2. **SOLR-17029, then SOLR-9342.** 17029 changes what the two launch lines consume; 9342 only wraps them. Landing 17029 first writes the wrap once against the final lines. The second lander re-runs its focused BATS.
3. **SOLR-12347** (defaults at lines 154 and 155; no `bin/solr` conflict). Lands with the owner's decisions on `solr.service` `TimeoutSec` and the ref guide.
4. **SOLR-18132** (the SSL block, `bin/solr` line 216). No ordering constraint with 18339. The nearest `bin/solr` contact point for SOLR-11678.
5. **SOLR-7924 last** (the spinner function), after its premise is re-scoped. No textual conflict.

Alternative, if the owner lands 10390 first: 10390, then 18339 deletes `port_is_listening`, and 18339 needs its gate re-run. Not recommended.

**Test file:** 18339's two inserts and the 9342 and 10390 inserts are mid-file with distinct anchors. The end-of-file trio (7924, 12347, 17029) needs one agreed join order.

**SOLR-11678 overlap** (this round's side only; not audited): the SSL block around `bin/solr` lines 207 to 300 at the 18132 head, which holds 18132's line 216. A key manager password change would likely touch it.

Also: the 7924 receipt now reads GATE GREEN at `96ef3a52bdbf` (2026-10-10). The claim and the assignment still say the gate is incomplete. Not reconciled here (part S1's scope).

## Part C: `test_start_solr.bats`

**Base:** 9 `@test` at each base (`cabedd1d`, `97d97381`, `b6b2b8f1`, `14c7aac0`), with the same nine names: "SOLR-11740 check 'solr stop' connection"; "stop command for single port"; "check stop command doesn't hang"; "SOLR-16976 solr starts with remote JMX enabled"; "deprecated system properties converted to modern properties"; "start with custom jetty options"; "webapp is deployed at the /solr context"; "-c flag prints no-op warning and still starts in cloud mode"; and "bootstrapping a configset".

| Branch | Base | Head | Added | Insert anchor |
|---|---|---|---|---|
| 7924 | 9 | 10 | "spinner falls back to whole-second sleeps when sleep rejects fractions" | end of file |
| 9342 | 9 | 10 | "SOLR-9342 GC log follows SOLR_TIMEZONE" | before "check stop command doesn't hang" |
| 10390 | 9 | 10 | "SOLR-10390 start detects the listening port without a working lsof" | before "bootstrapping a configset" |
| 12347 | 9 | 10 | "SOLR-12347 stop waits ten minutes by default and start wait stays at three" | end of file |
| 17029 | 9 | 12 | "SOLR-17029 quoted whitespace in SOLR_OPTS", "SOLR-17029 quoted whitespace in --jvm-opts", "SOLR-17029 dollar signs in SOLR_OPTS are not expanded" | end of file |
| 18339 | 9 | 11 | "start returns nonzero when Solr exits before becoming ready", "SOLR-18339 start waits on the configured bind address" | before "SOLR-11740 check 'solr stop' connection" and before "stop command for single port" |

No branch edits an existing test (the diffs are insertions only). Head equals base plus added for every branch. All six landings give 18 tests. The receipts agree: 9342 "9 ok, 1 skipped" is 10; 17029 "11 ok, 1 skipped" is 12; 18339 "11 of 11".

**Collisions:** no duplicate names. Insert positions: 7924, 12347 and 17029 share the end-of-file position, and `merge-tree` confirms that all three pairs conflict. So the requirement that "no two depend on the same insert position" is NOT met for those three. 9342, 10390 and 18339 use distinct anchors.

**Content notes:** 12347's name claims the start wait "stays at three", but the body does not test the 180 default. 10390's fake-lsof test exercises the fallback only while that fallback exists.

## Part D: Windows parity

| Branch | Windows files changed | Windows status | Record | Check |
|---|---|---|---|---|
| 7924 | none | unchanged; `solr.cmd` has no spinner or lsof | none | undecided (no record; likely not applicable) |
| 9342 | none | deliberately unchanged | receipt: the Windows JVM does not read TZ for GC log timestamps; `solr.cmd` line 1100 already sets `-Duser.timezone` from `SOLR_TIMEZONE` | consistent with the diff; the JVM claim not verified here |
| 10390 | none | unchanged; `solr.cmd` uses netstat, no lsof | receipt silent | undecided (no record) |
| 12347 | `solr.cmd` line 824, `solr.in.cmd` line 31 | changed | receipt, TESTING.md and changelog agree | consistent; the 570-second effective Windows wait is unrecorded |
| 17029 | none | unchanged; the `set_jvm_opts` path is untouched | receipt silent; the ticket has no Windows mention | undecided (no record) |
| 17598 | `solr.cmd` only | changed | receipt: `solr.cmd` only | consistent |
| 18132 | none; `solr.cmd` blob `6748816` is identical to base | deliberately unchanged (owner-provisional) | receipt: dropped; Windows inherits the variables | consistent. The rationale holds on reading: `solr.cmd` lines 81 to 85 set `SOLR_SSL_ENABLED` in the environment before java starts, and `EnvUtils` maps `SOLR_*` environment variables to `solr.*` properties (`EnvUtils.java` around lines 210 and 246). `bin/solr` derives it at lines 207 to 209, after the export loop at lines 115 and 116, so the Unix line is needed |
| 18339 | none | unchanged; `solr.cmd` already waits via `StatusTool` (`solr.cmd` line 1181) | receipt: the credential gap flagged, not fixed (`solr.cmd` parity) | consistent for the credential item |

**Records that contradict the diff or the tree:**
1. 12347 TESTING.md line 25 ("no page naming the 180 default") against the ref guide line 400 (180, unchanged).
2. 12347 TESTING.md line 24 ("systemd not checked") against `solr.service` line 32 (`TimeoutSec=180s`, left in place).
3. The 12347 test name against its body (Part C).
4. The 18339 `bin/solr` comment "same path as `bin/solr.cmd`": the `StatusTool` class is shared, but the URL selection differs. `solr.cmd` passes no `--solr-url` (`solr.cmd` line 1181) and relies on pid-file discovery (the `StatusTool` scan path, read from the code). The Unix call passes an explicit URL (`bin/solr` line 1392).
5. The 7924 receipt (GATE GREEN) against the claim and the assignment (gate incomplete).

Consistent with the diff: 9342, the 12347 files, 17598 and 18132.

## Owner decisions

1. **12347 stop default:** 600 (as on the branch), or 900 or 1200. Recommendation: 600, only with the systemd and docs fixes.
2. **`solr.service` `TimeoutSec=180s`:** raise it with the stop default (recommended), or accept the 180-second cap under systemd.
3. **Ref guide line 400:** update it in 12347 (recommended).
4. **`bin/solr` landing:** 18339 first, with 10390 withdrawn or rescoped (recommended), or 10390 first. The 18339 timing is owner-held (SOLR-18336).
5. **7924 after 18339:** retarget to the stop spinner, or retire.
6. **Windows:** accept 9342's recorded "deliberately unchanged" (the rationale is unverified here); accept 18132's owner-provisional drop (the rationale holds on reading); decide 17029's Windows parity (no record); decide 10390 and 7924 (no record, likely not applicable).
7. **18339:** the credential gap (`bin/solr` and `solr.cmd`) and the wildcard HTTPS question, for the maintainers before opening.
8. **The end-of-file join order** for 7924, 12347 and 17029.
9. **12347 test name against its assertion.**

## Not checked

- No builds, tests, BATS or Gradle. The `merge-tree` results are textual only. The composition points were read, not run.
- Windows behavior (cmd.exe, the 570-second scaling, taskkill, the JVM TZ claim) was read from code only; there is no Windows host.
- systemd behavior (the `ExecStop` cut at `TimeoutSec`, and the signal sequence) was read from the unit file only.
- Docker and Kubernetes stop timeouts: not checked.
- Jetty `--stop` with a hung listener, and whether jstack or jattach can block the kill: not traced or run.
- The `StatusTool` pid-file discovery and the "Waiting up to" messages: read, not executed.
- SOLR-11678: not audited; the overlap is named from this side only.
- The diffs use each branch's merge-base with `upstream/main`, and the bases differ. `upstream/main` was the local ref (`8e62c2686882`), not refetched.
- The 7924 gate state was not reconciled.
