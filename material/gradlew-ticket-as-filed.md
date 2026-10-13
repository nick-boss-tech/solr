AI text below.

h2. Symptom

Running \{{./gradlew}} from Git Bash (MSYS or MINGW) on Windows fails before the build starts. The script reaches its final \{{java}} call with a classpath of the form \{{C;C:/Users/.../gradle/wrapper/gradle-wrapper.jar}}, and the JVM answers:

{noformat}
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
{noformat}

Only the Cygwin and MSYS path through the shell script is broken: \{{gradlew.bat}} contains no \{{cygpath}} conversion, and on Linux and macOS the block at line 215 never runs because neither \{{cygwin}} nor \{{msys}} is set there.

h2. Mechanism

In \{{gradlew}} (main, \{{d76fc14b9857}}), the paths are converted to Windows form twice on the Cygwin and MSYS path:

 * \{{APP_HOME}} is computed in POSIX form (\{{/c/Users/...}}) at line 89 and converted once with \{{cygpath --path --mixed}} at line 159. That first conversion is correct.
 * \{{CLASSPATH}} is set at line 174 from \{{GRADLE_WRAPPER_JAR}}, which is built from the already converted \{{APP_HOME}} at line 162, so it is already in Windows form too.
 * The block at line 215 ("For Cygwin or MSYS, switch paths to Windows format before running java") then runs \{{cygpath --path --mixed}} on \{{APP_HOME}} and \{{CLASSPATH}} a second time, at lines 216 and 217.

{\{cygpath --path}} treats its input as a colon-separated list of paths. Given \{{C:/Users/...}}, it splits at the drive-letter colon: the \{{C}} part stays relative and the remainder resolves against the MSYS root, and the parts are joined back with \{{";"}}. The result is \{{C;C:/Users/...}}, which is the classpath the final \{{java}} call at line 256 receives, and the wrapper main class is not on it.

Probes on a Git Bash host show the same shape: \{{cygpath --path --mixed "/c/Users/..."}} prints \{{C:/Users/...}}, and running the same command on that output prints \{{C;C:/Users/...}}.

{\{JAVACMD}} is converted once, at line 219 with \{{cygpath --unix}}, and is not affected.

h2. Reproduction

Confirmed by run. On a Windows Git Bash (MINGW64) host with JDK 21 (\{{JAVA_HOME}} set to Eclipse Adoptium jdk-21.0.11.10-hotspot), the command \{{timeout 30 bash -x ./gradlew -version}} ran on two trees:

 * On main at \{{498a09aea97}}: after the first conversion the trace shows \{{APP_HOME}} as \{{C:/Users/...}}; after the second conversion it shows \{{APP_HOME}} as \{{'C;C:/Users/...'}} and \{{CLASSPATH}} as \{{'C;C:/.../gradle/wrapper/gradle-wrapper.jar'}}; the final \{{java}} call runs with that classpath and the JVM answers "Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain". Exit code 1.
 * On branch \{{gradlew-msys-double-conversion}} at \{{192aacaaf89}}, whose \{{gradlew}} differs from main's only by the deletion of lines 216 to 218 below: the second conversion does not happen, \{{APP_HOME}} stays \{{C:/Users/...}}, \{{CLASSPATH}} has no \{{C;}} prefix, and \{{gradlew -version}} prints Gradle 9.7.0. Exit code 0.

The reading of the script at the cited snapshot (\{{d76fc14b9857}}) and the cygpath probes in the Mechanism section came first; the run confirms them.

Stated plainly, the scope of the run: only \{{gradlew -version}} ran, so the fix is verified for wrapper startup. \{{gradlew check}}, \{{gradlew build}} and any test task were not run. Cygwin (non-MSYS) was not run; it takes the same block at line 215. \{{gradlew.bat}}, Linux and macOS were not run; on Linux and macOS the block at line 215 never runs because neither \{{cygwin}} nor \{{msys}} is set there. The argument conversion loop at lines 222 to 242 runs once over \{{-version}} without converting it; its \{{cygpath}} call at line 231 is not reached.

h2. Fix

Delete lines 216 to 218 of \{{gradlew}} (the two \{{cygpath}} assignments and the blank line after them), keeping line 219:

{noformat}
 # For Cygwin or MSYS, switch paths to Windows format before running java
 if "$cygwin" || "$msys" ; then
-    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
-    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )
-
     JAVACMD=$( cygpath --unix "$JAVACMD" )
{noformat}

Nothing else needs to change: \{{APP_HOME}} is already in Windows form from line 159, \{{CLASSPATH}} is built from it at line 174, and the steps between them (the WrapperDownloader at line 164 and the GradlePropertiesGenerator at line 179) receive the same paths they receive today. A comment at line 214 noting that \{{APP_HOME}} is converted at line 159 and \{{CLASSPATH}} is built from it at line 174 would prevent a recurrence.

Two checks that come with the fix:

 * The SOLR-16641 validation still passes: \{{gradle/validation/gradlew-scripts-tweaked.gradle}} requires the text \{{GradlePropertiesGenerator}} in \{{gradlew}} and \{{gradlew.bat}}, and its only occurrence in \{{gradlew}} is line 179, which the deletion does not touch.
 * \{{gradlew.bat}} has no equivalent defect: it contains no \{{cygpath}} conversion; it sets \{{APP_HOME}} from \{{%DIRNAME%}} and normalizes it once.

One related item is not part of this fix: the argument conversion loop at lines 222 to 242 also runs \{{cygpath --path}} (at line 231) on arguments that look like POSIX paths, and a colon inside such an argument would split the same way. It is outside the reported failure and is named here only so it is not lost.

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for the above text.