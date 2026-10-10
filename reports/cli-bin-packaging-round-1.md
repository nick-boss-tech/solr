# CLI, bin scripts and packaging round 1: round roll-up

Claim: `claims/cli-bin-packaging-round-1.md` (commit `76fde6cfa6a`). Assignment: `assignments/cli-bin-packaging-round-1.md` (commit `bfc4d3a7026`). Part reports: `reports/cli-bin-packaging-round-1-s1.md` through `-s6.md`. Drafts: `pr-drafts/cli/` (seven files).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six subagents in parallel, split by ticket cluster, within the cap of six. No build, Gradle run, BATS run, or test was run. No branch, live PR, JIRA item, or comment was touched. Nothing was posted. The receipts were not edited.

## Heads

All eleven live tips were verified by `ls-remote`. The eight that the assignment names match it. The three audit-only tickets without a named head (10390, 10667, 12347) match their receipts. The gated tickets' heads are the ones in the drafts. The origin tracking refs were used throughout.

## Verdicts

| Ticket | Verdict | Draft | Owner decision |
|---|---|---|---|
| SOLR-7924 | Audit only. The gate is green at the receipt's head, but the claim and the assignment still say it is incomplete. | none | Reconcile the gate state; retarget after 18339 (the spinner leaves the start path) |
| SOLR-9342 | Draftable, but PR-ready only after two calls: default zone, and the precedence question (blocks PR-ready). | `SOLR-9342.md` | Default zone UTC; precedence (option B recommended); changelog title; Windows scope; the run date |
| SOLR-10390 | Audit only. Superseded by SOLR-18339 when that lands. | none | Withdraw, or keep only its test as a no-lsof guard |
| SOLR-10667 | Audit only. The premise is confirmed by reading, and the placement is right. | none | Exclude `solrclient/**`; dotfiles; "full distribution" wording; target branches |
| SOLR-12347 | Audit only. The 600-second value is consistent across the four script files. The ref guide and systemd still say 180. | none | 600 or more; `solr.service` `TimeoutSec`; ref guide line 400; the test name |
| SOLR-16272 | Draftable. | `SOLR-16272.md` | Amend the changelog title; accept the keep branch with no test, and the single-read race |
| SOLR-16813 | Draftable. | `SOLR-16813.md` | Amend the changelog title to "when a version loads"; keep or drop the Choice |
| SOLR-17029 | Draftable on BATS evidence. | `SOLR-17029.md` | Which Proof counts to publish (the 2026-10-08 run has a test 5 failure); the Limits |
| SOLR-17598 | Draftable by construction only. The Windows run is owed. | `SOLR-17598.md` | Post-strip default (a behavior change); the changelog title; run the Windows check first, or open with the Limits |
| SOLR-18132 | Draftable. | `SOLR-18132.md` | Windows `solr.cmd` scope in Limits; whether to ask the precedence as a Choice |
| SOLR-18339 | Draftable. Timing is owner-held (SOLR-18336). | `SOLR-18339.md` | The no-credentials status wait and its 401 exposure; the wildcard HTTPS question; the timing |

## Drafts written

Seven drafts in `pr-drafts/cli/`, with lengths with links: 9342 (4,896), 16272 (4,508), 16813 (5,817), 17029 (4,857), 17598 (4,388), 18132 (5,386), 18339 (7,637). Every draft is above the roughly 3,500 guide, because the link targets drive the length. The owner decides whether to trim.

Each draft names its head. The evidence shape is stated in each Proof: a BATS suite (9342, 17029, 18339), a premise run and a BATS suite (18132), a construction proof with the Windows run still owed (17598), or a filestore test with the keep branch untested (16272, 16813). No dashes, and no process words, apart from "not JUnit tests", which describes the test type.

**Notes per draft that affect submission:**
- **SOLR-9342.** The receipt says PR-ready. Round 28 review said "Not ready", with a high finding that two timezone sources can diverge (operator TZ, or `-Duser.timezone` in `SOLR_OPTS`). The part confirmed the divergence by reading. Precedence must be decided before the PR is ready. The changelog says "exports", which describes the superseded top-level export, and "the same timezone as the Solr log", which overstates.
- **SOLR-16272.** The changelog title overstates ("removes the files it already uploaded"; "uninstall no longer refuses"). Uninstall is unchanged. The keep branch has no test, and the draft says so.
- **SOLR-16813.** The changelog title says "when the node joins", but the trigger is the version load. The Choice (best effort versus mandatory) stays, because the ticket asks that question.
- **SOLR-17029.** The Proof gives the 2026-10-04 counts with the date. A 2026-10-08 run also exists, and it had a test 5 failure, which the draft does not mention. The draft says nothing about test 5 (owner decision).
- **SOLR-17598.** The post-strip empty check adds a default for a quote-only value, which base never set. The draft states it as a behavior change. The changelog title, "no longer fails to parse ... whose value contains spaces or parentheses", states a fixed behavior the change has not yet shown on Windows.
- **SOLR-18132.** The Windows `solr.cmd` lines were dropped under a recorded owner-provisional call. The record does not say it is public-facing. The rationale holds on reading. The draft's precedence behavior (an explicit `solr.ssl.enabled=false` overrides an https `urlScheme` for tools) is stated as intended. The dependency on SOLR-18056 is named.
- **SOLR-18339.** The recorded Choice (the status wait sends no credentials, so a 401 from a secured node is a failed start) is in the draft, and the probe can authenticate through the customizer when system properties carry the credentials. The wildcard-bind certificate question is open in Limits.

## Landing order for `solr/bin/solr`

Seven branches edit the script. The pairwise `merge-tree` results (21 pairs): 16 clean; two conflicts in `bin/solr` (9342 with 17029, and 10390 with 18339); three conflicts in the end-of-file test insert (7924, 12347 and 17029, which all add after the last test).

Recommended order:
1. **SOLR-18339 first.** It rewrites the whole start-wait block, and it removes all lsof use from `bin/solr` and `install_solr_service.sh`. Its timing is owner-held (SOLR-18336).
2. **SOLR-17029, then SOLR-9342.** 17029 changes what the two launch lines consume. 9342 only wraps them, so landing 17029 first writes the wrap once against the final lines.
3. **SOLR-12347** (the defaults at lines 154 and 155; no `bin/solr` conflict), with the `solr.service` and ref guide decisions.
4. **SOLR-18132** (the SSL block, line 216). No ordering constraint with 18339.
5. **SOLR-7924 last** (the spinner function), after its premise is re-scoped.

If the owner lands 10390 first instead, 18339 must delete `port_is_listening` and re-run its gate. That is not recommended.

**Test file:** the end-of-file trio (7924, 12347 and 17029) needs one agreed join order. The other inserts (9342, 10390 and 18339) use distinct anchors.

**SOLR-11678** (outside this round, Security): it edits the same SSL block around `bin/solr` lines 207 to 300 that holds 18132's line 216. Note the overlap when either side lands.

## SOLR-10390 and SOLR-18339

If 18339 lands first, nothing of 10390 remains in `bin/solr` (its probe has no other caller). Its test would then exercise `StatusTool` and no longer the fallback. Recommendation: withdraw 10390, or resubmit only the test as a no-lsof guard. Either way, drop the `bin/solr` fallback.

The 10390 receipt says the branch detects the port "without lsof", which overstates: lsof stays first whenever `lsof -v` prints "revision". The fallback also regresses one combination (no lsof and no `/dev/tcp`), which reports a false failure, where main reports success.

## SOLR-7924

The receipt now reads GATE GREEN at `96ef3a52bdbf` (2026-10-10). The claim commit (`76fde6cfa6a`, later) and the assignment still say the gate is incomplete. The lead must rule which is current.

The premise holds by reading: base sleeps 0.5 seconds, which the integer-only stub rejects. The count of error lines is an inference, not a measurement. The AIX evidence is simulated, and the PR must say so. The probe is `sleep 0.5` with a fallback to 1, and a zero-length probe is untested.

After 18339, the spinner is no longer on the start path (18339 removes `spinner $!`). The 7924 premise must be re-scoped to the stop spinner, or retired.

## SOLR-12347

The branch sets `SOLR_STOP_WAIT` to 600 in `bin/solr`, `solr.cmd`, `solr.in.sh` and `solr.in.cmd`. The four script places agree. The ticket gives no number, and the reporter's commenter suggests 10 to 20 minutes. The start default stays at 180, and follows an explicit stop wait.

Not complete as a default change:
- The ref guide at `solr-control-script-reference.adoc` line 400 still says 180 seconds and "kill -9". The TESTING note says no page names the default, which is wrong.
- `solr.service` still has `TimeoutSec=180s`, so under systemd the 600-second default is never reached.
- The test name "start wait stays at three" does not match the body, which tests the start following a 45-second stop wait, not the 180 default.

On a hung node the script force-kills after about 610 seconds, not 190. The change moves the kill later and does not remove it. Verification is owed on main: BATS against the base control, a hung-node timing run, a systemd measurement, and a Windows stop run.

## SOLR-10667

The premise is confirmed by reading. `gradle/solr/packaging.gradle` `assemblePackaging` copies no example directory. Only `solr/modules/ltr/example` exists among the modules. The placement in the packaging spec is right, and the distribution spec would hard-code one module.

Cautions before any draft: `from("example")` copies the gitignored `solrclient/` directory, which makes the distribution depend on local state, so exclude `solrclient/**`. It also copies the tracked `.gitignore`, which the TESTING note omits. Gradle may report an implicit dependency, which the premise run would show. The changelog should say "full distribution", because the slim distribution has no modules. The BATS check exercises the assembled and installed full distribution, and it fails on base by reading.

## Windows parity

| Branch | Windows | Record |
|---|---|---|
| 7924 | unchanged; no spinner or lsof in `solr.cmd` | none |
| 9342 | deliberately unchanged | consistent with the diff; the JVM claim is not verified here |
| 10390 | unchanged | none |
| 12347 | `solr.cmd` and `solr.in.cmd` changed | consistent; the effective 570-second Windows wait is unrecorded |
| 17029 | unchanged | none |
| 17598 | `solr.cmd` only | consistent |
| 18132 | unchanged; `solr.cmd` identical to base | owner-provisional; the rationale holds on reading |
| 18339 | unchanged; `solr.cmd` already waits via `StatusTool` | consistent for the credential item |

Records that contradict the diff or the tree: 12347 (the TESTING note and the ref guide), 12347 (systemd, not checked against `solr.service`), the 12347 test name, the 18339 comment "same path as `bin/solr.cmd`" (the URL selection differs), and the 7924 gate state.

## Receipt and assignment corrections (main side)

- **SOLR-9342:** "Status: VERIFIED ... PR-ready" against the round 28 "Not ready" finding. The run date is missing.
- **SOLR-16272:** the changelog title overstates the rule. The receipt's "rolls back the files it posted" is incomplete.
- **SOLR-16813:** the changelog title's trigger ("joins") is wrong; the code trigger is the version load. The "6 tests with exactly 1 failure" is this branch's file run against base code.
- **SOLR-17029:** the assignment's `-a` path does not exist; only `--jvm-opts` remains. The 2026-10-04 run's log is not in the worktree, and the run's head is not confirmed.
- **SOLR-17598:** the receipt's "defined-but-empty defaulting" is restored is not what base does. The changelog states a fixed behavior before the Windows run.
- **SOLR-18339:** "StatusTool takes credentials only from its CLI option" is incomplete (the customizer and system properties also apply).
- **SOLR-7924:** the gate state (GREEN in the receipt, incomplete in the claim and assignment); the count (100-plus, about 410, against 412); the steps (no list).
- **SOLR-10390:** "without lsof" (overstated); the TESTING note's claim about `install_solr_service.sh` (wrong on main).
- **SOLR-12347:** the TESTING note's claim that no page names the 180 default (wrong).
- **SOLR-10667:** the commit `045e1569396` subject overstates (it adds only the BATS check).

## Owner decisions

1. **SOLR-9342:** keep UTC as the GC default (drafted), or set TZ only when a zone is configured. Precedence: derive one zone for both logs, or document `SOLR_TIMEZONE` as the only knob with a test (recommended). The changelog title. Windows: keep `solr.cmd` unchanged and check the JVM claim, or soften it. The run date and the build.
2. **SOLR-16272:** amend the changelog title on the branch (a changelog-only commit) to the keep rule. Add a registration-failure test, or ship with the Limit (recommended). Accept the single-read race (recommended).
3. **SOLR-16813:** amend the changelog title to "when a version loads". Keep the Choice (recommended).
4. **SOLR-17029:** publish the 2026-10-04 counts only, or also state the 2026-10-08 result (which had the test 5 failure). Confirm no Choice. Keep the Limits.
5. **SOLR-17598:** keep the post-strip check (a stated behavior change), or drop it to match base. Soften the changelog title before the Windows run, or keep it. Run the Windows check first (recommended), or open with the Limits.
6. **SOLR-18132:** whether the Windows scope goes in Limits (the owner-provisional call is not recorded as public-facing). Whether the precedence is asked as a Choice. Whether to open the 9.10.1 port and the early-failure request as follow-ups.
7. **SOLR-18339:** the no-credentials status wait with the 401 exposure flagged for the maintainers, or a fix first. The wildcard HTTPS question: add a BATS case, or keep it open (recommended). The timing (SOLR-18336).
8. **SOLR-10390:** withdraw as superseded, or keep the test as a guard.
9. **SOLR-7924:** reconcile the gate state. Retire the start-spinner premise after 18339. The AIX evidence: accept the simulated test with the PR saying so, or get a real run.
10. **SOLR-10667:** exclude `solrclient/**` (recommended); dotfiles; the "full distribution" wording; whether the ref guide page is updated in the same change; the target branches beyond main.
11. **SOLR-12347:** 600 seconds, or 900 or 1200 (recommended: 600 with the fixes). Raise `solr.service` `TimeoutSec` (recommended). Update ref guide line 400 in this change (recommended). Rename the test.
12. **Landing order:** 18339 first, then 17029, then 9342, then 12347, then 18132, then 7924 last (recommended). The end-of-file trio join order (7924, 12347 and 17029).
13. **Windows:** accept 9342's "deliberately unchanged" (rationale not verified). Accept 18132's owner-provisional drop (the rationale holds). Decide 17029's Windows parity (no record).
14. **Length:** all seven drafts exceed the 3,500 guide with links. Trim or accept.
15. **Authoritative fork ref** for the branches whose local refs differ from origin.

## Main-side work owed

- Gate reconciliation for SOLR-7924 (the claim and the assignment against the receipt).
- The SOLR-9342 run date, and the build of the BATS distribution from the head.
- The SOLR-17598 Windows confirmation (a spaced, parenthesized `SOLR_LOGS_DIR`, then `bin\solr.cmd start`).
- The SOLR-12347 premise run (BATS on the control and head), the hung-node timing, a systemd measurement, and a Windows stop run.
- The SOLR-10390 premise run, if it is kept (a PATH without lsof on Linux or macOS; a stub lsof that prints "revision" for `-v`).
- The SOLR-10667 premise run (assemble the full distribution at base, show the missing example directory, apply the fix, reassemble, and check the Gradle output).
- The SOLR-11356, SOLR-3498 and SOLR-14187 items are not in this round (they belong to the SolrJ round).
- Receipt corrections listed above.

## Not done

- No build, Gradle run, BATS run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The gate and test logs named in the receipts (for example `g7924-gate.log`, `g17029-bats2.log`, `g9342-tc-*.log`) are not in this worktree or the workspace. The counts rest on the receipts.
- The changelog YAML was read by eye, not parsed by a tool.
- The Windows behavior and the systemd behavior were read from code and the unit file only. There is no Windows host and no systemd host here.
