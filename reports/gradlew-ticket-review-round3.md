# Review: gradlew Jira ticket draft, round 3 confirmation

Verdict: **STILL OPEN.** Items 1 to 3 are confirmed. Item 4 has one unsupported date in the draft's source note and one loop claim that the trace contradicts, plus a minor precision point in the same passage. No passage claims Gradle is blocked on Windows.

Scope: the four items of `assignments/pool-gradlew-ticket-review-round3.md`. Nothing was written, committed, built, run or posted. The draft `material/gradlew-ticket-draft.md` was not edited; each fix below is for the main side.

## One line per item

1. **"Exactly the string" wording in the bug report: CONFIRMED.** `reports/gradlew-git-bash-path-bug.md` line 31 reads "That is the same shape as the failing trace". The probe output (line 36) is `C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`. The traced `APP_HOME` in `reports/gradlew-end-to-end-run/trace-repro.txt` line 55 is `C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/worktrees/gradlew-repro`. Both carry the Git install segment after `C;` and differ only in the worktree leaf: the same shape, not the same string. No "same string", "exactly the string" or "failing strings" wording remains in the bug report, the draft, the run report or the adjudication.

2. **Blocked-on-Windows wording: CONFIRMED.** Line 3 of the bug report reads "`gradlew` was not executed in this investigation, so the failing run itself is not reproduced here." The word "allowed" no longer appears in the bug report. The remaining non-block statements are: the bug report line 74 ("For the Windows host only, and only when Nick asks"), which gates who runs the reproduction; the run report line 7 ("WORKFLOW.md keeps builds off the Windows host for claimed work"), a process rule plus the record that Nick ran it himself; and the review's line 5, a scope statement. In the draft, the only "block" words are code references, "the block at line 215" (lines 26, 34 and 51). The adjudication in `reports/gradlew-ticket-review.md` has no block claim. `reports/gradlew-ticket-review-round2.md` line 26 quotes the removed phrase as the finding it settled, which is a quotation and not a claim.

3. **Adjudication wording, F1 paragraph: CONFIRMED.** `reports/gradlew-ticket-review.md` line 41 reads "The only `APP_HOME` conversion before the block is line 159". At main `d76fc14b9857e77b830d7a82514b28c523a239c2`, `cygpath` appears at lines 153, 159, 216, 217, 219 and 231. Line 153 converts `GRADLE_TEMPDIR`, so the `APP_HOME` qualifier is required. Line 159 is the only `APP_HOME` conversion before the block at 215. Line 174 is `CLASSPATH=$GRADLE_WRAPPER_JAR`, built from the converted value through line 162.

4. **Verification passages against the run report: FINDING.**
   - **Source note, draft line 5: unsupported date.** The draft says "the end-to-end run, executed 2026-10-11". `reports/gradlew-end-to-end-run.md` gives no date, and neither trace file carries a run date (`trace-fix.txt` line 77, "Build time: 2026-08-06", is Gradle's build stamp, not the run). The only 2026-10-11 in the record is the annotation in `reports/gradlew-git-bash-path-bug.md` lines 5 and 76, which says the run "has since been executed" and gives no run date.
     - **Fix:** drop the date, or cite the annotation's date as the record date ("recorded 2026-10-11").
   - **Loop claim, draft line 51, and run report line 33: the trace contradicts the wording.** The draft says "The argument conversion loop at lines 222 to 242 is not exercised by `-version`". The loop is entered: `trace-fix.txt` lines 56 to 60 and `trace-repro.txt` lines 60 to 64 show `for arg in "$@"`, the `case`, `false`, `shift` and `set -- -version`. What is not reached is the `cygpath` call at line 231, because the `-*` branch at line 225 is false.
     - **Fix, draft line 51 and run report line 33:** "The argument conversion loop at lines 222 to 242 runs once over `-version` without converting it; its `cygpath` call at line 231 is not reached."
   - **Minor, same loop point:** the rest of the scope sentence in line 51 is accurate; only the loop claim needs the change.

## Matches checked in item 4

- The command: draft line 44 and run report line 14 both give `timeout 30 bash -x ./gradlew -version`.
- The refs: draft lines 5, 44 and 46 to 47 match run report lines 11 and 12 (main `498a09aea97`; branch `gradlew-msys-double-conversion` `192aacaaf89`). Both resolve. `git diff 498a09aea97 192aacaaf89 -- gradlew` shows only the deletion at lines 216 to 218. `git diff d76fc14b9857 498a09aea97 -- gradlew` is empty, so the draft's main line numbers hold on the repro ref.
- The repro trace: `APP_HOME` after the first conversion at line 30; after the second conversion at line 55 (the draft's `C;C:/Users/...` is the elided form of the same value); `CLASSPATH` at line 57; the exec at line 75 with `-classpath 'C;C:/...'`; the error line at 76 matches exactly.
- The fix trace: `APP_HOME` at line 30 (`C:/...`); `CLASSPATH` at line 38 with no `C;` prefix; the exec at line 71. No second `APP_HOME` conversion; line 54 is the `cygpath --unix` on `JAVACMD` only.
- Gradle 9.7.0 on the fixed leg: `trace-fix.txt` line 74.
- The JDK: the trace path at line 17 matches. The draft writes "Eclipse Adoptium jdk-..." and the run report "Eclipse Adoptium/jdk-..."; this is notation only, the same JDK.
- Draft line 49, "the run confirms them": `trace-repro.txt` line 54 shows `cygpath --path --mixed` on the `C:/` path returning the `C;C:/` form, which is the behaviour the probes show. CONFIRMED.
- The not-run list (draft line 51) matches run report lines 31 to 35 and the assignment's list.

## Not findings (outside item 4's named passages; they do not drive the verdict)

- Draft line 26 ("Only the Cygwin and MSYS path ... is broken") and line 20 ("Running `./gradlew` from Git Bash (MSYS or MINGW) on Windows fails") state scope beyond the run, which was one MINGW64 host and wrapper startup only. The reasons on line 26 are static, so a "by reading the script" qualifier would match run report line 67 ("Static reading only").
- Both trace files show the wrapper downloader fetching `gradle-wrapper.jar` from `raw.githubusercontent.com` (v8.10.2) on both legs (`trace-repro.txt` and `trace-fix.txt` lines 33 and 34). The run report does not record this network step. This is not a draft mismatch; the run report could name it.

## Consequence

With item 4 open, the draft does not yet join the consolidated email. The record needs the two fixes above, and the source-note date needs either removal or the annotation's date. Once they are applied, a short re-check of the changed lines closes this round.

## Scope

Read-only. The draft, the reports and the run record were not edited. Nothing was built, run, posted or filed.
