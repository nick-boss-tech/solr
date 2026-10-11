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
