# gradlew end-to-end run: repro confirmed, fix starts the wrapper

Verdict: **Repro confirmed** on `origin/main` (498a09aea97). **Fix confirmed** for `gradlew -version` on `origin/gradlew-msys-double-conversion` (192aacaaf89). Scope is `gradlew -version` only.

## Who ran it and under what authority

Nick ran the commands himself on the Windows host, with the `!` prefix, at his direct request. It was not a claimed gate job: no claim file, gate file or receipt was filed for it. WORKFLOW.md keeps builds off the Windows host for claimed work. This run is recorded here because it was run on that host at Nick's request, so the coordinators have the result on the branch.

## Setup

- Repro tree: detached worktree at `origin/main` 498a09aea97.
- Fix tree: detached worktree at `origin/gradlew-msys-double-conversion` 192aacaaf89.
- The two `gradlew` files differ only at lines 216 to 218, the three lines the fix deletes.
- Command per tree: `timeout 30 bash -x ./gradlew -version`, under Git Bash MINGW64, with `JAVA_HOME` set to `Eclipse Adoptium/jdk-21.0.11.10-hotspot`.
- Full unfiltered traces are in this folder: `trace-repro.txt` and `trace-fix.txt`.

## Results

| Check | Repro (origin/main) | Fix (192aacaaf89) |
|---|---|---|
| `APP_HOME` after the first conversion (trace line 30) | `C:/Users/shaninna/dev/Solr-issues/worktrees/gradlew-repro` | `C:/Users/shaninna/dev/Solr-issues/worktrees/gradlew-fix` |
| `APP_HOME` after the second conversion | `'C;C:/Users/.../gradlew-repro'` (line 55) | not present (the second conversion is deleted) |
| `CLASSPATH` | `'C;C:/.../gradle-wrapper.jar'` (line 57) | `C:/.../gradle-wrapper.jar` (line 38) |
| `exec` java line | `-classpath 'C;C:/...'` (line 75) | `-classpath C:/.../gradle-wrapper.jar` (line 71) |
| Outcome | `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`, exit 1 | Gradle 9.7.0 prints its version info, exit 0 |

The fix run reports Launcher JVM 21.0.11 (Eclipse Adoptium 21.0.11+10-LTS), Daemon JVM at the same JDK, and OS Windows 11 10.0 amd64.

## What this does not cover

- `gradlew check`, `gradlew build`, or any test task. Only the wrapper startup ran.
- Cygwin (non-MSYS). Not checked.
- The argument conversion loop at lines 222 to 242 runs once over `-version` without converting it; its `cygpath` call at line 231 is not reached. The fix does not touch the loop.
- `gradlew.bat`, Linux and macOS. Not run.
- One run per tree, on this host.

## Earlier attempts

Two earlier attempts in this session are not evidence. The first two invocations pasted `JAVA_HOME` with a line break inside the path, so `gradlew` stopped at its `JAVA_HOME` check. The first run's `grep` filter hid the error line. The split path fails the same `-x` test the wrapper uses. The results above come from the run with the correct path.

## Effect on the review

`reports/gradlew-ticket-review.md` (verdict STILL OPEN) flagged F3: the Jira draft's reproduction section says the run was not executed. This run answers that, so the draft can cite it. Editing the draft is the main side's call. F1 and the other review items are unchanged. This report does not edit the draft.

## Fix commit

No change to `192aacaaf89`.
