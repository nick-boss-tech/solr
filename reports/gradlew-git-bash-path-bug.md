# Report: gradlew converts paths twice under Git Bash

Verdict: **CAUSE CONFIRMED** by reading the script at three refs and by cygpath probes on the Windows Git Bash host. `gradlew` was not executed (no Gradle is allowed on this host), so the failing run itself is not reproduced here. The reproduction spec is at the end.

## Refs read

- `upstream/main` resolves to `d76fc14b98`, two commits ahead of `3f5d4c5bf8a` (the assignment's ref). The defect lines are the same at both.
- `f269a70e84f` (the SOLR-9831 branch head) exists locally. The merge-base with `upstream/main` is `cabedd1d968`.
- `gradlew`, `gradlew.bat` and `gradle/validation/` are identical between `3f5d4c5bf8a`, `f269a70e84f` and `upstream/main`.

## Cause

In `gradlew`, `APP_HOME` and `CLASSPATH` are converted by `cygpath --path --mixed` twice on the Cygwin and MSYS path:

| Line | What happens |
|---|---|
| 89 | `APP_HOME` from `cd -P` and `$PWD`: POSIX form (`/c/Users/...`). |
| 118 | `CLASSPATH` built from the POSIX `APP_HOME` (overwritten at 174). |
| 153 | `GRADLE_TEMPDIR` converted once. Correct. |
| 159 | `APP_HOME` converted with `cygpath --path --mixed`. First conversion. Correct. |
| 162 | `GRADLE_WRAPPER_JAR` built from the mixed `APP_HOME`. |
| 164, 179 | `$JAVACMD` (still unconverted at this point) runs the WrapperDownloader and GradlePropertiesGenerator steps. |
| 174 | `CLASSPATH=$GRADLE_WRAPPER_JAR`, already mixed. Correct. |
| 215 | `if "$cygwin" || "$msys" ; then` |
| **216-217** | **Defect.** `APP_HOME` and `CLASSPATH` go through `cygpath --path --mixed` a second time, on values already in `C:/` form. |
| 219 | `JAVACMD=$( cygpath --unix "$JAVACMD" )`. Single conversion, correct. |
| 256 | `-classpath "$CLASSPATH"` in the final exec. |

Why the second call breaks: `cygpath --path` splits its input on `:`. Given `C:/Users/...`, the drive letter `C` stays relative and the rest resolves against the MSYS root. The two parts are joined with `;`, which gives `C;C:/Users/...`. That is exactly the string in the failing trace.

Probes on this host, run through the Bash tool:

- `cygpath --path --mixed "/c/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester"` gives `C:/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`.
- `cygpath --path --mixed "C:/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester"` gives `C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`.

The second output has the same shape as the trace in the assignment.

I read lines 159 and 213 to 219 on `upstream/main` myself and they match the table.

## Proposed fix (text only, not applied)

Delete lines 216 to 218 (the two `cygpath` assignments and the blank line after them). Keep line 219.

```diff
--- a/gradlew
+++ b/gradlew
@@ -213,9 +213,6 @@
 
 # For Cygwin or MSYS, switch paths to Windows format before running java
 if "$cygwin" || "$msys" ; then
-    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
-    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )
-
     JAVACMD=$( cygpath --unix "$JAVACMD" )
 
     # Now convert the arguments - kludge to limit ourselves to /bin/sh
```

Why this is enough: `APP_HOME` is already in mixed form from line 159, and `CLASSPATH` is built from it at line 174. Both already have the form the final exec needs. Nothing above line 216 changes, so the WrapperDownloader (164) and GradlePropertiesGenerator (179) steps receive the same paths they receive today.

Optional: a comment at line 214 saying that `APP_HOME` is converted at line 159 and `CLASSPATH` is built from it at line 174.

## Checks

1. **Linux and macOS are unchanged.** Lines 107 to 110 default `cygwin`, `msys`, `darwin` and `nonstop` to false. The case at 111 to 116 sets `cygwin` only for `CYGWIN*` and `msys` only for `MSYS*` or `MINGW*`. On Linux and macOS the test at 215 is false, so lines 216 to 218 never run. Static reading only; no Linux or macOS host was run.
2. **Validation script still passes.** `gradle/validation/gradlew-scripts-tweaked.gradle` line 35 requires the text `GradlePropertiesGenerator` in `gradlew` and `gradlew.bat` (SOLR-16641). In `gradlew` the only occurrence is line 179, which the diff leaves alone.
3. **`gradlew.bat` has no equivalent defect.** It has no `cygpath`, `cygwin` or `msys` text. `APP_HOME` is set from `%DIRNAME%` (line 33) and normalised once (line 36). `GRADLE_WRAPPER_JAR` (79) and `CLASSPATH` (87) are plain assignments, and `-classpath "%CLASSPATH%"` is at line 104. No path is converted twice.
4. **The syntax of the edited script.** `sed` removed lines 216 to 218 from `f269a70e84f:gradlew` and `bash -n` passed with no output. `git apply --check` could not run, because the worktree does not track `gradlew`.

## Reproduction spec

For the Windows host only, and only when Nick asks; the end-to-end run is the remaining verification step and Nick runs it himself. Linux and macOS cannot reproduce this defect.

- Environment: Git for Windows Bash (MINGW64), JDK 21 with `JAVA_HOME` set, working directory the SOLR-9831 worktree at `f269a70e84f`.
- Command: `timeout 30 bash -x ./gradlew -version`. A fixed script may start the Gradle distribution download, and `timeout` kills it. Judge the trace, not the exit code.
- Failing trace (from the assignment): `APP_HOME=C;C:/Users/.../wt/SOLR-9831`, `CLASSPATH='C;C:/.../gradle/wrapper/gradle-wrapper.jar'`, the final exec with `-classpath 'C;C:/...'`, then `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`.
- A fixed script should show `APP_HOME=C:/Users/.../wt/SOLR-9831` with one conversion (line 159), `CLASSPATH` with no `C;` prefix, the final exec with `-classpath C:/.../gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain -version`, one remaining `cygpath --unix` call (line 219), and no `Could not find or load main class` line.

## Not verified

- `gradlew` was not run. The verdict rests on the line reads and the cygpath probes, which reproduce the trace's shape.
- The POSIX form of `APP_HOME` at line 89 is inferred from the code and the trace, not observed.
- Cygwin (non-MSYS) was not checked.
- The argument loop at lines 222 to 242 runs `cygpath --path` on arguments that look like POSIX paths (line 231). A colon in such an argument would be split the same way. That is outside the reported trace, so it is not part of this fix.
- The scratchpad trace file the assignment cites was not available to the investigator; the trace lines come from the assignment.
