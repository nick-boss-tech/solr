# Pool assignment: gradlew Jira ticket draft review, round 2 confirmation

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-gradlew-ticket-review-round2.md.

## Scope: confirmation of the adjudicated items only

Round 1 (`reports/gradlew-ticket-review.md`) returned STILL OPEN with seven evidence disagreements, F1 to F7, and no edits to the draft. The main side adjudicated every item against the script at main `d76fc14b9857e77b830d7a82514b28c523a239c2` and the evidence report; all seven settled for the reviewer. The settlement record is the "Main-side adjudication (2026-10-11)" section appended to `reports/gradlew-ticket-review.md`. Corrections are in `material/gradlew-ticket-draft.md` and, where the same error sat in the ground-truth record, in `reports/gradlew-git-bash-path-bug.md`.

Confirm each item below against the corrected passages and the script. Nothing else is in scope; do not re-open criteria round 1 checked clean, and do not edit the draft or the reports; flag instead.

## Annotation (2026-10-11): verification upgraded, confirmation scope extended

Since this assignment was posted, Nick ran the end-to-end verification himself on the Windows Git Bash host; the record is `reports/gradlew-end-to-end-run.md`. On main `498a09aea97` the trace shows `APP_HOME` becoming `'C;C:/...'` after the second conversion, ending with `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`, exit 1; on branch `gradlew-msys-double-conversion` at `192aacaaf89`, `gradlew -version` prints Gradle 9.7.0, exit 0. The run's scope is `-version` only. The main side has upgraded the draft's verification passages to match: the reproduction is now stated as confirmed by run, and the fix as verified for wrapper startup.

The confirmation scope of this assignment therefore explicitly includes the upgraded verification passages, checked against `reports/gradlew-end-to-end-run.md`:

8. **Verification upgrade.** Every passage in `material/gradlew-ticket-draft.md` that states the verification level (the source note and the Reproduction section) matches the run report exactly: the command (`timeout 30 bash -x ./gradlew -version`), the two refs (`498a09aea97` and `192aacaaf89`), the traced `APP_HOME` and `CLASSPATH` values, the error line, exit codes 1 and 0, Gradle 9.7.0 on the fixed leg, and the not-run list (check, build and any test task; Cygwin; `gradlew.bat`; Linux and macOS; the argument conversion loop). Item 3 (F3) is read against the upgraded text: the draft now presents the trace as the output of the recorded run, and no passage may present the run as broader than `-version`. The verification-level summary in the standing check paragraph after the items is superseded by this annotation.

Two consequences for the closing section: step (1), emailing Nick the run instructions, no longer applies, because the run has already been made at Nick's own direction, and its deliverable line for item 3 is judged under the upgraded wording above. Step (2), the consolidated ticket email, stands unchanged.

## Items to confirm

1. **F1 (fix comment text).** The draft's Fix section now says `APP_HOME` is converted at line 159 and `CLASSPATH` is built from it at line 174; the report's optional-comment line says the same. Confirm against the script: line 174 is `CLASSPATH=$GRADLE_WRAPPER_JAR`, a plain assignment, and line 159 is the only conversion before the block.
2. **F2 (probe wording).** The draft's Mechanism probes sentence says "the same shape", and the Reproduction says the probes "reproduce the failing shape". Confirm no passage in the draft still claims the probes reproduce the trace's exact strings (the probe output carries the MSYS root segment after `C;`).
3. **F3 (trace attribution and verification level).** The Reproduction attributes the failing trace to the reported Git Bash failure and presents the run command as the remaining verification spec; no passage presents the quoted trace as the output of a run that was executed. Confirm the verification level is stated plainly and consistently, and that no passage claims `gradlew` has been run.
4. **F4 (Symptom scope sentence).** The Symptom now states that `gradlew.bat` contains no `cygpath` conversion and that on Linux and macOS the block at line 215 never runs because neither `cygwin` nor `msys` is set there. Confirm no passage claims a `gradlew.bat`, Linux or macOS run as evidence.
5. **F5 (reproduction spec).** The Reproduction names the SOLR-9831 worktree at `f269a70e84f` and the command `timeout 30 bash -x ./gradlew -version`, judging the trace rather than the exit code. Confirm both match the report's reproduction spec.
6. **F6 (argument loop range).** The draft's related-item sentence cites the argument conversion loop at lines 222 to 242 with the `cygpath` call at line 231; the report's Not verified section matches. Confirm against the script (`for` at 222, `done` at 242).
7. **F7 (report line ranges).** The report's check 1 now cites the defaults at lines 107 to 110 and the `case` at lines 111 to 116. Confirm against the script.

One standing check carries over from round 1: the corrected draft must not claim Gradle is blocked on Windows anywhere, in any wording. The verification level is stated plainly instead (cause confirmed by code reading and cygpath probes; the end-to-end run is the remaining verification step).

## Deliverable

`reports/gradlew-ticket-review-round2.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with).

When this round lands SATISFIED, two main-side steps follow, and the reviewer does neither: (1) the main side emails Nick the run instructions for both verification legs, the failing trace in the SOLR-9831 worktree at `f269a70e84f` and the fixed trace on branch `gradlew-msys-double-conversion` at `192aacaaf89`, since the end-to-end run is Nick's to make; and (2) the final text of `material/gradlew-ticket-draft.md` joins the one consolidated email the main side sends Nick carrying every new-ticket text, sent only once each included ticket has been completely reviewed. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
