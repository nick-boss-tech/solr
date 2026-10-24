[~malliaridis] I looked at SOLR-17861 (extend updateLicenses to the KMP module). I have not run anything yet. The points below are read from the code on apache/solr main on 2026-10-24.

 * {{gradle/validation/jar-checks.gradle}} defines {{updateLicenses}} (line 343). It collects the {{licenses}} tasks from the subprojects of the Solr build (line 339), so the {{ui}} build is outside it.
 * The UI has been its own build since [PR #4650|https://github.com/apache/solr/pull/4650]. {{ui/settings.gradle.kts}} sets {{rootProject.name = "solr-ui"}} (line 56), and the root build does not include it.
 * {{ui/build.gradle.kts}} configures Spotless only. It defines no licenses task.
 * {{ui/desktopApp/build.gradle.kts}} uses {{compose.desktop.currentOs}} (line 29), so the resolved desktop jars differ by host OS. That is the divergence the ticket describes.
 * The wasmJs dependencies are locked in {{ui/kotlin-js-store/wasm/yarn.lock}}, which the jar checks do not read.

Does this evidence meet the bar you set for the ticket? That is the one question I am asking.

The options as I see them:
 * Option A: keep {{ui}} as its own build and add a licenses check inside it. The ticket stays open for the other targets.
 * Option B: include {{ui}} in the root build, so {{updateLicenses}} covers it directly.
 * Option C: close the ticket on the grounds that the root build does not need to cover {{ui}}, and leave the lock check to the UI build's own CI.

My recommendation is Option A. #4650 made {{ui}} independent on purpose, and a check inside {{ui}} can start without changing the root build. The first slice would be a LICENSE and NOTICE presence check, with SHA-1 checksums for the desktop JVM runtime classpath resolved for {{linux_x64}}, wired into {{./gradlew -p ui check}}. I expect to touch {{ui/build.gradle.kts}} and new build logic under {{ui/}}. The other OS classifiers and the wasmJs yarn lock would be follow-up slices.

No review is asked here. If the direction is right and work follows, I will ask about that then.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
