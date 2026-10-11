# Draft Jira ticket: gradlew fails under Git Bash (MSYS) because APP_HOME and CLASSPATH are converted with cygpath twice

Status: NOT filed. Draft for Nick to file. Nick's rule (2026-10-11): new-ticket texts go to him in one consolidated email, sent only once every included ticket has been completely reviewed; this text joins that email when its review returns SATISFIED.

Source: reports/gradlew-git-bash-path-bug.md on this branch (verdict CAUSE CONFIRMED by reading the script at main d76fc14b9857e77b830d7a82514b28c523a239c2 and by cygpath probes on a Windows Git Bash host; the end-to-end run is the remaining verification step).

Duplicate check (2026-10-11, read-only Jira search): no existing ticket covers this defect. Searches covered gradlew with cygpath, Git Bash, MSYS or MINGW, GradleWrapperMain, and "Could not find or load main class". The only cygpath hit, SOLR-14955, covers the Prometheus Exporter bin scripts. The only GradleWrapperMain hit, SOLR-16641, is a circular task dependency in localSettings, a different defect.

The ticket text below the line is Jira wiki markup: the Title line is the ticket summary and the rest is the description.

---

Type: Bug
Title: gradlew fails under Git Bash (MSYS) with "Could not find or load main class org.gradle.wrapper.GradleWrapperMain" because APP_HOME and CLASSPATH are converted with cygpath twice

🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

h2. Symptom

Running {{./gradlew}} from Git Bash (MSYS or MINGW) on Windows fails before the build starts. The script reaches its final {{java}} call with a classpath of the form {{C;C:/Users/.../gradle/wrapper/gradle-wrapper.jar}}, and the JVM answers:

{noformat}
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
{noformat}

The same checkout works from {{gradlew.bat}} and on Linux and macOS; only the Cygwin and MSYS path through the shell script is broken.

h2. Mechanism

In {{gradlew}} (main, {{d76fc14b9857}}), the paths are converted to Windows form twice on the Cygwin and MSYS path:

 * {{APP_HOME}} is computed in POSIX form ({{/c/Users/...}}) at line 89 and converted once with {{cygpath --path --mixed}} at line 159. That first conversion is correct.
 * {{CLASSPATH}} is set at line 174 from {{GRADLE_WRAPPER_JAR}}, which is built from the already converted {{APP_HOME}} at line 162, so it is already in Windows form too.
 * The block at line 215 ("For Cygwin or MSYS, switch paths to Windows format before running java") then runs {{cygpath --path --mixed}} on {{APP_HOME}} and {{CLASSPATH}} a second time, at lines 216 and 217.

{{cygpath --path}} treats its input as a colon-separated list of paths. Given {{C:/Users/...}}, it splits at the drive-letter colon: the {{C}} part stays relative and the remainder resolves against the MSYS root, and the parts are joined back with {{";"}}. The result is {{C;C:/Users/...}}, which is the classpath the final {{java}} call at line 256 receives, and the wrapper main class is not on it.

Probes on a Git Bash host show the same strings: {{cygpath --path --mixed "/c/Users/..."}} prints {{C:/Users/...}}, and running the same command on that output prints {{C;C:/Users/...}}.

{{JAVACMD}} is converted once, at line 219 with {{cygpath --unix}}, and is not affected.

h2. Reproduction

Environment: Git for Windows Bash (MINGW64), JDK 21 with {{JAVA_HOME}} set, a Solr checkout at the cited main snapshot. Command: {{bash -x ./gradlew -version}}. The trace shows {{APP_HOME=C;C:/Users/...}}, a {{CLASSPATH}} with the same {{C;}} prefix, and the error above.

Stated plainly: this defect is confirmed by reading the script at the cited snapshot and by the cygpath probes above, which reproduce the failing strings. The end-to-end run is the remaining verification step. Linux and macOS cannot reproduce it: the block at line 215 runs only when {{cygwin}} or {{msys}} is true. Cygwin (non-MSYS) takes the same block but was not checked separately.

With the fix below, the same trace should show {{APP_HOME=C:/Users/...}} from the single conversion at line 159, a {{CLASSPATH}} with no {{C;}} prefix, and no "Could not find or load main class" line.

h2. Fix

Delete lines 216 to 218 of {{gradlew}} (the two {{cygpath}} assignments and the blank line after them), keeping line 219:

{noformat}
 # For Cygwin or MSYS, switch paths to Windows format before running java
 if "$cygwin" || "$msys" ; then
-    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
-    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )
-
     JAVACMD=$( cygpath --unix "$JAVACMD" )
{noformat}

Nothing else needs to change: {{APP_HOME}} is already in Windows form from line 159, {{CLASSPATH}} is built from it at line 174, and the steps between them (the WrapperDownloader at line 164 and the GradlePropertiesGenerator at line 179) receive the same paths they receive today. A comment at line 214 noting that {{APP_HOME}} and {{CLASSPATH}} are converted at lines 159 and 174 would prevent a recurrence.

Two checks that come with the fix:

 * The SOLR-16641 validation still passes: {{gradle/validation/gradlew-scripts-tweaked.gradle}} requires the text {{GradlePropertiesGenerator}} in {{gradlew}} and {{gradlew.bat}}, and its only occurrence in {{gradlew}} is line 179, which the deletion does not touch.
 * {{gradlew.bat}} has no equivalent defect: it contains no {{cygpath}} conversion; it sets {{APP_HOME}} from {{%DIRNAME%}} and normalizes it once.

One related item is not part of this fix: the argument conversion loop at lines 220 to 235 also runs {{cygpath --path}} on arguments that look like POSIX paths, and a colon inside such an argument would split the same way. It is outside the reported failure and is named here only so it is not lost.

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
