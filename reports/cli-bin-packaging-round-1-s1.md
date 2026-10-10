# CLI, bin scripts and packaging round 1, part S1: SOLR-9342 (draft) and SOLR-7924 (audit only)

Worktree `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`. Nothing built, tested, committed, pushed or posted. The only file written is the 9342 draft.

## Per ticket

**SOLR-9342: draftable. Draft written. PR-ready only after two owner calls.**
- Draft `pr-drafts/cli/SOLR-9342.md`. Size: 4,896 characters with link targets (`LC_ALL=C.UTF-8 wc -m`), 3,777 without the URLs. It is above the roughly 3,500 guide because the ticket has a real timezone-precedence question. Zero en or em dashes. No process vocabulary.
- Title: from `research\jira-context\SOLR-9342.json`, "Solr GC logging not respecting user timezone" (the JSON has a trailing space, dropped).
- Head: `git ls-remote` gives `833e11192a7ff918f7e401bde920cf63c89df063`, matching `833e11192a7`. The branch commits are by Nick Shanin, with no Claude trailers.
- Choice section included: default GC zone UTC versus the host zone until configured.
- Proof: a BATS suite, 10 tests at the head, 9 passed and 1 skipped. Fails on base at the +0530 assertion, per the receipt.

**SOLR-7924: audit only. Do not draft.**
- Head: `git ls-remote` gives `96ef3a52bdbf1948578d122ec2f9d61e0944a8f2`, matching `96ef3a52bdb`.
- The receipt now says the gate completed green (see disagreement 1). The gate is not the blocker. The 18339 landing order is the blocker (see the interactions section).
- Gate needs: the main side confirms the step list for "steps 0 to 5" and places `g7924-gate.log` where it can be read. The brief and claim commit still say "incomplete", so the lead must reconcile them.

**Head checks for interactions:** the 18339 head is `47e53884609c0c7f84e85069881f9d820342e70e`, matching `47e53884609`.

## SOLR-7924 premise and claims

- **Premise (read, not re-run):** base `spinner` calls `sleep $delay` with 0.5 in the loop (base line 462). The integer-only stub rejects "0.5" and prints an error each pass, so the refute fails on base. At the head, the probe (line 457, `sleep "$delay" 2>/dev/null || delay=1`) fails silently and the loop uses 1. The logic supports the receipt's premise.
- **Count:** the number of error lines depends on how many `ps` polls fit in the 2-second background sleep. This is an inference, not a measurement. The test only asserts that lines appear, so "412" or "about 410" is not a fixed fact.
- **Probe cost:** confirmed. The probe runs once before the loop on each `spinner` call. The spinner is called from the stop wait (head line 548) and the start wait (head line 1410). On a fractional-sleep platform, each call adds 0.5 seconds. On an integer-only platform, the failed probe costs nothing.
- **`JAVA_MINOR_VERSION`:** gone. Zero hits in the whole `upstream/main` tree (`8e62c2686882`).
- **J9 `-Xloggc`:** not emitted. There is no `-Xloggc` in any bin script on main. The OpenJ9 branch (main line 1092) uses `-Xverbosegclog`, a different flag. No J9 or AIX run confirms that startup works.
- **Is the integer-only stub a fair stand-in?** It is fair for the failure mode: a fractional argument errors, and the loop must avoid it. It is not a stand-in for AIX itself.
  - (a) The error text and `exit 1` are invented.
  - (b) It rejects "1.5" too, but the 2015 report only says values below 1 fail.
  - (c) It checks that no error appears, not that the fallback delay is 1.
  - (d) It runs `spinner` via `sed` extraction, not the start path.
  - The PR must say the AIX behavior is simulated.
- **Other premise claim:** the draft text the assignment mentions is not in the receipt. The receipt names `files/reviews-2026-10-06-round28-fresh-arrivals/7924.md`, which is not in this workspace. Only the listed claims were checked.

## Spinner survival in SOLR-18339's loop: no

The 18339 diff removes the whole `lsof` branch, including the 7924 start-wait call `spinner $!` (7924 head line 1410). The `StatusTool` wait is a synchronous `run_tool status ... --max-wait-secs` with no subshell, no spinner and no bash sleep. The `spinner` function (lines 453 to 470) is unchanged by 18339. It is still called only from the stop wait (18339 head line 546). After 18339 lands, the 7924 probe covers the stop wait only.

## SOLR-9342 and SOLR-7924 with `bin/solr`

- **Compose: yes.** A temporary index in the scratchpad was used to trial-apply each branch's `bin/solr` diff onto `upstream/main`, with no commits, refs or worktree changes. All combinations apply cleanly: 9342 plus 7924, 9342 plus 18339, 7924 plus 18339, and all three. The hunks are separate: 9342 at lines 1347 to 1356, the 7924 probe at line 457, and 18339 from line 1377. The spinner only polls `ps` and sleeps, and never launches the server JVM. In foreground mode, `exec` replaces the shell before the spinner can run.
- **9342 and 18339:** `StatusTool` inherits only the operator's TZ, which matches 9342's scoping. No change is needed in either.
- **9342 launch, read directly:** both launch paths carry the setting (head line 1352 foreground, line 1356 nohup). The operator's TZ wins (`${TZ:-$SOLR_TIMEZONE}`). No top-level export remains. Tool JVMs (`run_tool`, line 498) no longer inherit a script-set TZ. The default is set at line 1196, before the `start_solr` call at line 1418.

## Receipt and review disagreements (exact wording)

1. **7924 gate.** The receipt, commit `56b68946fa9` (15:45 UTC): "Status: GATE GREEN at the live tip (completed 2026-10-10; supersedes the earlier NO COMPLETED GATE state)." The assignment and brief: "the full gate ... stopped during its tidy step and was never completed". The claim commit `76fde6cfa6a` (15:52 UTC, later) still says "matches; audit only (gate incomplete)". The convergence rule says a receipt at the exact tip settles gate state, so the lead must rule.
2. **7924 count.** The receipt: "at least 100 ... (the grounding run captured about 410)". The assignment and an earlier receipt: "412".
3. **7924 steps.** The receipt: "Steps 0 to 5 all pass", with no step list. An earlier receipt named "compile, module check" as owed. The new receipt does not say they ran.
4. **7924 draft text.** The assignment: "recorded in the receipt". The receipt holds only the path above, which is not reachable.
5. **9342 verdict.** The receipt: "Status: VERIFIED at the live tip. PR-ready." The round-28 review (`research\branch-reviews\round-28\SOLR-9342-review.md`): "**Not ready.**" with a HIGH finding that two timezone sources can diverge. Four later reviews at the same head say the same (`wt\chan-A1`, `chan-A2`, `chan-A3` and code-review, `reviews\solr-9342-submit.md`). The receipt does not address it. The part confirmed the finding by code reading: the operator's TZ, or `-Duser.timezone` in `SOLR_OPTS` (line 1334), splits the logs.
6. **9342 changelog** (`changelog/unreleased/SOLR-9342-gc-log-timezone.yml`, line 2): "`bin/solr` now exports TZ from `SOLR_TIMEZONE` (default UTC) so the GC log uses the same timezone as the Solr log, unless TZ is already set." "Exports" describes the superseded top-level export. "The same timezone as the Solr log" overstates, per item 5. Not fixed by the part.
7. **9342 Windows.** The receipt: "No `solr.cmd` change: the Windows JVM does not read TZ for GC log timestamps, so an equivalent line would be a no-op." Review `chan-A1`, finding 3: "The changelog does not say the fix is Linux/macOS only." The same position, with the changelog silent. `solr.cmd` has no TZ handling (lines 1029 and 1100 only). The JVM claim is not checked here.
8. **9342 BATS assertion.** Review `chan-A1`, finding 5, said the +0530 assertion was unverified. The receipt's head run covers it. Resolved by the receipt.
9. **9342 run date.** The receipt gives no date for the head run. The draft uses 2026-10-07, from the takeover-log entry for the tip change. Confirm before posting.

## Owner decisions

1. **9342 default zone.** Keep UTC as the GC default (drafted, stated as a behavior change), or set TZ only when a zone is configured. The part's lean: keep UTC and say so in the changelog.
2. **9342 precedence (blocks PR-ready).** Option A: derive one zone for both logs. Option B: document `SOLR_TIMEZONE` as the only supported knob, with a test. The part's recommendation: B for this PR.
3. **9342 changelog title.** Rewrite it. Suggested: "bin/solr sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set."
4. **9342 Windows.** Keep `solr.cmd` unchanged and check the JVM claim on Windows, or soften it. Name the Linux and macOS scope in the changelog either way.
5. **9342 run date and build.** Confirm the 2026-10-07 date, and that the BATS distribution was built from this head.
6. **7924 and 18339 order.** The part's recommendation: land 18339 first, then rescope 7924 to the stop wait (title, premise, test name), or drop it. If 7924 lands first, 18339 removes its start-wait effect.
7. **7924 probe.** Keep the 0.5-second probe, or try a zero-length probe such as `sleep 0.0`. Not verified on AIX, and the stub rejects it too.
8. **7924 AIX evidence.** Accept the simulated sleep test, with the PR saying it is simulated, or get a real AIX run.
9. **7924 draftability.** The lead decides whether the receipt makes 7924 draftable this round, or whether it waits for item 6.

## Not checked

- No builds, tests, BATS runs or Gradle. Every BATS count and premise comes from the receipts plus code reading.
- Gate and test logs (`g7924-gate.log`, `g7924-premise2-*.log`, `g9342-tc-*.log`) are not in this worktree or the workspace. The part searched research, env, tools, worktrees and wt. Their contents are unverified.
- Windows JVM behavior with TZ: no Windows run, and the JVM source is not in this repo.
- AIX sleep and shell behavior: no AIX host. The judgments use the 2015 report text.
- OpenJ9 `-Xverbosegclog` timestamps and TZ: not checked.
- The `+0530` unified-logging format: not checked by a run.
- The 7924 draft text in the receipt's path: not reachable.
- The changelog title of 7924 ("start spinner") may be stale after 18339: not checked beyond the `bin/solr` diff.
- 18339's test file was not reviewed beyond the `bin/solr` diff.
