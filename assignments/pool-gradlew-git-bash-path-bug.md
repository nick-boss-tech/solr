# Pool assignment: gradlew converts paths twice under Git Bash (investigation and fix proposal)

Capability tags: `review`. Staffing: 1. Claim path: claims/pool-gradlew-git-bash-path-bug.md.

Background: on the Windows host, `./gradlew` fails under Git Bash (MINGW64) before Gradle starts, with `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`. `gradlew.bat` is not affected. The wrapper jar is present, valid (SHA-256 matches `gradle/wrapper/gradle-wrapper.jar.sha256`) and contains `org/gradle/wrapper/GradleWrapperMain.class`. The JDK loads that class from the jar when given a Windows path (`javap -cp` finds it), so the jar and the JDK are fine.

Evidence (a `bash -x ./gradlew -version` trace taken on the SOLR-9831 worktree at `f269a70e84f`; the trace is in the reviewing host's scratchpad, not on the branch, so the lines are quoted here):

- `APP_HOME=C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/SOLR-9831`
- `CLASSPATH='C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/SOLR-9831/gradle/wrapper/gradle-wrapper.jar'`
- The final `exec` passes `-classpath 'C;C:/...'` to Java, which cannot find the jar.

Cause (to be confirmed by the investigator): `cygpath --path --mixed` runs on `APP_HOME` and `CLASSPATH` twice. The first conversion is in the Lucene block at lines 152 to 159 (`if [ "$cygwin" = "true" -o "$msys" = "true" ]`, with `APP_HOME=` at line 159). The second is at lines 215 to 217 (`if "$cygwin" || "$msys"`). The second call receives an already-mixed path such as `C:/Users/...`, and `cygpath --path` treats the colon as a list separator, so the value is mangled. `GRADLE_TEMPDIR` (line 153) is converted once and is not affected. The same lines exist on upstream main at `3f5d4c5bf8a` (lines 153, 159, 215 to 217), so the bug is on main, not only on the Solr-issues fork.

Work:

1. Confirm the cause by reading `gradlew` on upstream main and at the branch head. Check the order of the lines: the WrapperDownloader step (lines 162 to 172) and the GradlePropertiesGenerator step (lines 176 to 185) run `"$JAVACMD"` before line 219 converts `JAVACMD`, so any fix must keep those steps working with the path form they receive.
2. Propose the smallest fix. The likely shape is to convert `APP_HOME` and `CLASSPATH` once, not twice. Show the exact diff as text. Do not apply it to any branch.
3. Check that the fix leaves Linux and macOS unchanged (`cygwin` and `msys` are both false there, so neither conversion runs).
4. Check `gradle/validation/gradlew-scripts-tweaked.gradle` (it requires the `GradlePropertiesGenerator` snippet, SOLR-16641). The fix must keep that snippet.
5. Check that `gradlew.bat` has no equivalent defect. It should not, because it does not call `cygpath`.
6. Write the reproduction spec: the command to run on Git Bash on Windows (`bash -x ./gradlew -version` from the worktree, with `JAVA_HOME` set, bounded by `timeout 30`), the failing trace lines above, and the trace lines a fixed script should show instead. Linux hosts cannot reproduce the bug, so the spec names who runs it: the Windows host, and only when Nick asks, because Gradle is blocked on that host.

Deliverable: `reports/gradlew-git-bash-path-bug.md` with the cause and the lines it rests on (main and the branch head), the proposed diff as text, the checks in steps 3 to 5, and the reproduction spec in step 6. No edit to `gradlew`, `gradlew.bat` or any branch. No PR, comment or Jira write. No builds, no Gradle and no tests. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverable).
