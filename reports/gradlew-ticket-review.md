# Review: gradlew Jira ticket draft

Verdict: **STILL OPEN.** The draft (`material/gradlew-ticket-draft.md`) has seven evidence disagreements (F1 to F7). No wording or markup fix was needed, so no edit was made to the draft. Every item below is a fact disagreement, which the assignment says to flag, not edit. The main side owns the fixes.

Scope: read against `reports/gradlew-git-bash-path-bug.md` and the script at main `d76fc14b9857e77b830d7a82514b28c523a239c2`. No Jira, email, GitHub or comment write. No build, Gradle, test or gate. The Jira duplicate-search claims in the draft's preface were not checked; this review has no Jira access.

## Evidence disagreements (flagged, not edited)

**F1. Draft line 63, fix section (blocking for the fix text).** The draft suggests a comment at line 214 saying `APP_HOME` and `CLASSPATH` "are converted at lines 159 and 174". Line 174 is `CLASSPATH=$GRADLE_WRAPPER_JAR`, an assignment with no `cygpath` call. Only line 159 converts `APP_HOME`. The report's line 61 repeats the same error. **Suggested text:** "APP_HOME is converted at line 159 and CLASSPATH is built from it at line 174." (Criteria 2 and 5.)

**F2. Draft line 38, the probes.** The draft says the probes "show the same strings" as the trace. The report's probe output (line 34) is `C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`, which has an MSYS root segment after `C;`. The trace is `C;C:/Users/.../gradle/wrapper/gradle-wrapper.jar`. They have the same shape, not the same string. **Suggested:** "the same shape." (Criterion 2.)

**F3. Draft lines 42 to 46 (reproduction) and line 26 (verification level).** The report says `gradlew` was not run (lines 3, 72 and 81), and that the trace file was not available to the investigator, so the trace lines come from the assignment (line 85). The draft's line 44 says "The trace shows" after a stated command, which presents the quoted trace as the output of that command. Line 46 says the failing run was not executed, so the two statements conflict. Line 46 also says "on the host that reported it", while the report and the draft's line 5 name the Windows Git Bash host; no evidence identifies a separate reporter host. **Needed from the author:** retitle the section as not run, attribute the trace to the assignment's quoted trace, reword or cut "The trace shows", and align the host wording with line 5. (Criterion 3.)

**F4. Draft line 26 (Symptom).** "The same checkout works from `gradlew.bat` and on Linux and macOS." No run of `gradlew.bat`, Linux or macOS appears in the report, which says "Static reading only; no Linux or macOS host was run." (Criterion 3.)

**F5. Draft line 44 (environment and command), against report lines 74 and 75 (low severity).** The draft says "a Solr checkout at the cited main snapshot" and `bash -x ./gradlew -version`. The report says the SOLR-9831 worktree at `f269a70e84f`, and `timeout 30 bash -x ./gradlew -version`, judging the trace rather than the exit code, because a fixed script may start the Gradle download and not return. (Criteria 2 and 3.)

**F6. Draft line 70, the argument loop, "lines 220 to 235" (low severity).** In the script the `for` loop runs at lines 222 to 242. The `cygpath` call is at line 231, and the POSIX-path test is at lines 226 to 227. The assignment's range does cover the call, so keep it or widen it. (Criterion 4.)

**F7. Report, not the draft.** Report line 65 places the `case` statement at lines 110 to 115. In `gradlew` it is lines 111 to 116, with the defaults at 107 to 110. The draft does not cite these lines, so this is a correction to the report only.

## Criteria checked clean

1. **Title.** It names `gradlew`, Git Bash (MSYS), the quoted symptom, and the cause. "Converted with cygpath twice" holds in the sense the draft uses: `APP_HOME` has explicit calls at 159 and 216, and `CLASSPATH` has its only explicit call at 217 after inheriting the 159 conversion. The symptom rests on the assignment's trace (F3).
2. **Line numbers, read at `d76fc14b9857`.** 89, 159, 162, 164, 174, 179, 215, 216, 217, 218, 219 and 256 all match, except the description at line 174 (F1) and the probe wording (F2). `GradlePropertiesGenerator` occurs in `gradlew` only at line 179. `gradle/validation/gradlew-scripts-tweaked.gradle` line 35 checks the text in `gradlew` and `gradlew.bat`. `gradlew.bat` has no `cygpath`, `cygwin` or `msys` text, sets `APP_HOME` from `%DIRNAME%` at line 33, and normalises it at line 36.
3. **Fix.** The diff at draft lines 54 to 61 matches the script at lines 213 to 219 and the report's diff. Delete lines 216 to 218, keep 219. The reasons at lines 63 and 64 are correct except F1.
4. **Not-verified items.** Cygwin (non-MSYS) is named as not checked (line 46). The argument loop is named as outside the fix (line 70). Clean apart from F6.
5. **Wiki markup.** Code spans in `{{...}}`, bullets as ` * `, `h2.` headings, and closed `{noformat}` blocks. No GitHub markdown in the ticket text.
6. **Disclosure.** The AI header at line 16 is the first element after Type and Title, in the underscore form. The footer at line 72 is the last element and carries the prescribed text.
7. **Process vocabulary.** None in the ticket text (lines 13 to 72), and no "we" for Nick plus the agent. The preface (lines 3, 5, 7 and 9) uses process language and must not be pasted into Jira.

## Next step

Once F1 and F3 are fixed in `material/gradlew-ticket-draft.md` by the main side, the review can be re-run on the changed lines. F2, F4, F5 and F6 are wording and citation fixes. F7 is a report correction.
