# Review: gradlew Jira ticket draft, round 2 confirmation

Verdict: **STILL OPEN.** The draft `material/gradlew-ticket-draft.md` is clean on all seven adjudicated items. Two wording fixes remain in the supporting reports, and one non-blocking note applies to the adjudication. No draft edit was needed, and none was made.

Scope: the seven items of `assignments/pool-gradlew-ticket-review-round2.md`, checked against the corrected draft and the script at main `d76fc14b9857e77b830d7a82514b28c523a239c2`. Nothing was edited, built, run, posted or filed. The reviewer does not file in Jira, create the ticket or email anyone.

## One line per item

1. **F1, fix comment text: CONFIRMED.** Draft line 63 reads "APP_HOME is converted at line 159 and CLASSPATH is built from it at line 174". Line 174 is `CLASSPATH=$GRADLE_WRAPPER_JAR`, a plain assignment; line 159 is the `APP_HOME` conversion. Non-blocking note: "line 159 is the only conversion before the block" is literally false, because line 153 also runs `cygpath`, on `GRADLE_TEMPDIR`. The draft does not rely on "only". The adjudication in `reports/gradlew-ticket-review.md` (about line 41) should say "the only `APP_HOME` conversion".

2. **F2, probe wording: CONFIRMED for the draft; FINDING for the report.** The draft says "the same shape" (line 38) and "the failing shape" (line 46), and no draft passage claims an exact string match. `reports/gradlew-git-bash-path-bug.md` line 29 says the double-conversion output "is exactly the string in the failing trace". The probe output (line 34) carries the MSYS root segment after `C;`, and the trace is `C;C:/Users/.../gradle/wrapper/gradle-wrapper.jar`. These have the same shape, not the same string. **Suggested wording for the report:** "That is the same shape as the failing trace."

3. **F3, trace attribution and verification level: CONFIRMED.** Draft line 44 attributes the trace to the reported failure, not to a run in this review. Line 46 names the end-to-end run as the remaining verification step and gives the command as the spec. No passage presents the quoted trace as the output of an executed run. The report says `gradlew` was not executed (lines 3 and 81). The `bash -n` parse check cited at report line 68 is not a run of `gradlew`.

4. **F4, Symptom scope sentence: CONFIRMED.** Draft line 26 states that `gradlew.bat` has no `cygpath`, `cygwin` or `msys` text (no matches in main's `gradlew.bat`), and that on Linux and macOS the block at line 215 never runs, because `cygwin` and `msys` default to false (lines 107 and 108) and are set only by the `case` at lines 111 to 116. No passage cites a `gradlew.bat`, Linux or macOS run as evidence. Report line 65 says "Static reading only".

5. **F5, reproduction spec: CONFIRMED.** Draft line 46 names the SOLR-9831 worktree at `f269a70e84f` and the command `timeout 30 bash -x ./gradlew -version`, judging the trace rather than the exit code. This matches report lines 74 and 75. `gradlew` at `f269a70e84f` and at main has an empty diff, so the "identical to the cited snapshot" claim holds.

6. **F6, argument loop range: CONFIRMED.** At main, `for arg do` is at line 222, `arg=$( cygpath --path --ignore --mixed "$arg" )` at line 231, and `done` at line 242. Draft line 70 and report line 84 both cite 222 to 242 with the call at 231.

7. **F7, report line ranges: CONFIRMED.** At main, `cygwin=false` is at line 107, `msys=false` at 108, `darwin` and `nonstop` at 109 and 110, the `case` at 111, and `esac` at 116. Report line 65 matches.

## Standing check: no passage says Gradle is blocked on Windows

- **Draft: clean.** The only block references are code references to "the block at line 215" (lines 26 and 34).
- **FINDING, report: `reports/gradlew-git-bash-path-bug.md` line 3** says "gradlew was not executed (no Gradle is allowed on this host)". That is the blocked-on-Windows claim in other words. **Suggested wording:** "gradlew was not executed in this investigation."
- Not findings: `reports/gradlew-ticket-review.md` line 5 ("No build, Gradle, test or gate") is a scope statement. Report line 72 ("only when Nick asks") limits who runs the end-to-end test; it is not a block claim.

## Notes outside the seven items (non-blocking, round 1 scope)

- Draft lines 36 and 44, and report line 27, call line 256 "the final java call". Line 256 is one argument (`-classpath`) in the `set --` block. The exec is `exec "$JAVACMD" "$@"` at line 292. Correct the wording to name line 292.
- The adjudication (`reports/gradlew-ticket-review.md`, about line 45) says the report states "not run" at its line 72. Line 72 does not say that; lines 3 and 81 do. Correct the reference.

## Record for the main side

Two report edits and one adjudication edit are needed for the record to match the draft. None changes the draft. The reviewer did not make them, because the assignment asks for flags, not edits, on the reports.

## Gate and verification level

No build, test or gate was run in this review. The draft's stated verification level (cause confirmed by code reading and cygpath probes; the end-to-end run is the remaining step) is consistent across the seven items.

## Main-side settlement (2026-10-11)

The three wording fixes this round asked for are applied. The ticket draft's substance was not touched. Commit carrying these fixes is on this branch with this settlement.

**Fix 1 (round 2 item 2, report wording).** `reports/gradlew-git-bash-path-bug.md`, the "Why the second call breaks" paragraph (cited as line 29 in the round 2 report; line 31 in the current file): "That is exactly the string in the failing trace" now reads "That is the same shape as the failing trace", the reviewer's suggested wording verbatim. Checked against `reports/gradlew-end-to-end-run.md`: the run's traced value is `C;C:/Users/.../gradlew-repro` while the probe output carries the MSYS root segment (`C;C:/Users/shaninna/AppData/Local/Programs/Git/...`), so the two differ as strings and agree in shape; the run record supports the corrected wording and nothing in it conflicts.

**Fix 2 (standing check finding, report line 3).** `reports/gradlew-git-bash-path-bug.md` line 3: the parenthetical "(no Gradle is allowed on this host)" is deleted. The sentence now reads "`gradlew` was not executed in this investigation, so the failing run itself is not reproduced here", using the reviewer's suggested wording. Checked against the run record: Nick ran the end-to-end commands himself, outside this investigation and not as a claimed job, so "in this investigation" stays accurate; the report's own annotation already marks its not-executed statements as recording the state when the report was written. No conflict.

**Fix 3 (round 2 item 1 note, adjudication wording).** `reports/gradlew-ticket-review.md` line 41, the F1 paragraph of the main-side adjudication: "The only conversion before the block is line 159 (`APP_HOME`)" now reads "The only `APP_HOME` conversion before the block is line 159". Verified against the script record in the same report's cause table: line 153 also runs `cygpath`, on `GRADLE_TEMPDIR`, so the unqualified "only conversion" was false and the `APP_HOME` qualifier is required.

**Superseded round 2 statements about the draft (flagged, not errors in this round).** This round's confirmations were written against the draft's line numbers before the verification upgrade. Three passages describe the pre-upgrade text and are superseded by the current draft, which the main side upgraded from `reports/gradlew-end-to-end-run.md`: item 3 (the Reproduction "names the end-to-end run as the remaining verification step" and no passage presents the trace as an executed run's output; the current Reproduction is headed "Confirmed by run" and presents the traced values as the recorded run's output), item 5 (the SOLR-9831 worktree at `f269a70e84f` as the draft's reproduction spec; the current draft names the two trees actually run, main at `498a09aea97` and branch `gradlew-msys-double-conversion` at `192aacaaf89`, and reports exit codes 1 and 0), and the closing verification-level paragraph above ("the end-to-end run is the remaining step"). The round 2 assignment's annotation had already extended the scope to the upgraded passages; round 3 re-confirms the current passages against the run report directly. The confirmations for items 1, 2, 4, 6 and 7 were spot-checked against the current draft text and still match it.

**Not applied in this settlement.** The round 2 report's two non-blocking notes outside the seven items (the "final java call" wording for line 256, where the exec is at line 292; and the adjudication's reference to the report's line 72 for the "not run" statement, where lines 3 and 81 are the references) were outside the three fixes this settlement was scoped to. They stand in the round 2 report above as the record; the main side holds them for a later record pass.

Round 3 is posted as `assignments/pool-gradlew-ticket-review-round3.md`, scoped to these three fixes and to confirmation of the upgraded verification passages against the run report.
