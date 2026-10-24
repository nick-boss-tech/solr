# Report: new-ui-platform-build-round-1

Status: DONE. Claim: `claims/new-ui-platform-build-round-1.md`. Research only; no code changed, nothing posted to Jira or GitHub, no PR opened.

Reference point: `apache/solr` main, read through the GitHub API on 2026-10-24. Jira read through the REST API.

## SOLR-17970: Start publishing new UI desktop clients to nightlies

(a) Ask: build and publish Windows (.msi), Unix (.deb) and macOS (.pkg) installers of the Compose desktop client to nightlies.apache.org through Jenkins, only when the UI module changes. Open, Major, labels `jenkins`, `new-ui`, `ui`, unassigned. Jason Gerlowski offered Jenkins help on 2025-12-01; Christos asked for a pairing session, and there is no record that it happened.

(b) What exists today:
- `ui/desktopApp/build.gradle.kts` already declares `nativeDistributions` with `TargetFormat.Dmg`, `TargetFormat.Msi`, `TargetFormat.Deb`, `packageVersion = "1.0.0"`, and per-OS names and icons. The dependency is `compose.desktop.currentOs`, so the build packages for the OS it runs on.
- Nightlies precedent: `dev-docs/asf-jenkins.adoc` lists `Solr-reference-guide-*`, which pushes to nightlies.apache.org, and `Solr-Smoketest-*`, which builds a snapshot release.
- CI: every workflow in `.github/workflows/` runs on `ubuntu-latest` (13 uses) or a self-hosted runner (1). `new-ui-check.yml` runs `./gradlew -p ui check` on PRs and has no packaging step.
- Jenkins agents: `dev-docs/asf-jenkins.adoc` lists two Linux agents, `lucene-solr-1` and `lucene-solr-2`, shared with Lucene.

(c) What is missing:
- No Windows or macOS build host. Compose packaging is per OS, so `.msi` and `.dmg` cannot come from the Linux agents.
- No Jenkins job and no nightlies path. The ticket says that only people with sufficient access can create and test the jobs.
- No path filter for `ui/**`, which the acceptance criterion requires.
- Naming mismatch: the ticket asks for `.pkg`; the build produces `.dmg` (`ui/desktopApp/build.gradle.kts`).

(d) Verdict: NEEDS INFRA. The named infrastructure is Windows and macOS build agents, plus ASF INFRA or PMC approval for Jenkins job creation and nightlies publishing.
- Smallest first slice: a Linux-only `.deb` job for `ui/**` changes, with the path filter. It can be drafted in the repo, but it needs INFRA to run it.
- Recorded choice (rung 1): publish what the build produces, `.dmg` for macOS, and correct the ticket wording. The ticket is not edited here.

## SOLR-17861: Extend updateLicenses task to cover KMP module

(a) Ask: make `updateLicenses` and the license checks cover the Kotlin Multiplatform UI module, for all targets, with output that does not depend on the host OS. Open, Minor, labels `gradle`, `new-ui`, `ui`, components Admin UI, Gradle, scripts and tools. Unassigned.

(b) What exists today:
- `gradle/validation/jar-checks.gradle` defines `updateLicenses` (around line 343), `validateJarChecksums`, `validateJarLicenses` and `checkDanglingLicenseFiles`. They aggregate tasks from `subprojects` of `:solr` only.
- `jarValidation` gets its dependencies only inside `plugins.withType(JavaPlugin)` (around lines 84 to 110). A KMP module is not a Java project in that sense.
- The UI is a separate build since commit `c78939a620` ("Make UI module independent sub-project", #4650). `ui/settings.gradle.kts` sets `rootProject.name = "solr-ui"`. The root `settings.gradle` does not include it.
- `ui/build.gradle.kts` configures only Spotless. It defines no licenses task.
- `ui/desktopApp/build.gradle.kts` uses `compose.desktop.currentOs`, so the resolved jars differ by OS. This is the source of the lock-state divergence the ticket describes.
- The wasmJs dependencies are in `ui/kotlin-js-store/wasm/yarn.lock`. Jar checks do not cover them.

(c) What is missing:
- A licenses task in the `ui` build, or a decision to include `ui` in the root build. The root build has no path to the UI today.
- LICENSE and NOTICE coverage per target. Environment-independence requires enumerating every desktop classifier, not only the one the host resolves.
- Coverage of the yarn lock for wasmJs.

(d) Verdict: TOO BIG AS STATED.
- Recorded choice (rung 1): keep `ui` as its own build, since #4650 made it independent on purpose, and put the licenses task in `ui`, not in the root.
- Smallest first slice: a `ui`-local check of LICENSE and NOTICE presence and SHA-1 checksums for the `desktopApp` JVM runtime classpath, resolved for `linux_x64` only, wired into `./gradlew -p ui check`. The other OS classifiers and the wasmJs yarn lock are later slices.

## SOLR-17676: Enable wasmJs tests

(a) Ask: run the wasmJs UI tests reliably in CI and locally, without developer project changes. Open, Major, labels `new-ui`, `ui`, component Admin UI, unassigned.

(b) What exists today:
- The disable from PR #3195 (merged 2025-02-18) is not in the current build files. `ui/build.gradle.kts` has no test configuration.
- `ui/shared/build.gradle.kts` lines 31 to 35 declare `wasmJs { browser() }` with the default Karma setup and `binaries.executable()`. The tests live under `ui/shared/src/wasmJsTest`.
- `.github/workflows/new-ui-check.yml` runs `./gradlew -p ui check` on `ubuntu-latest`, which has Chrome preinstalled.
- Evidence of a passing run: run 39835983878 (branch `admin-ui-node-picker`, 2026-10-22, conclusion success), job 120031832202. The log shows `:shared:wasmJsBrowserTest` and `:shared:jvmTest` executing, and `BUILD SUCCESSFUL in 4m 2s`. Link: https://github.com/apache/solr/actions/runs/39835983878

(c) What is missing, or not verified:
- Not verified: a local run on a machine without a browser, which the ticket's second criterion covers. I did not run it.
- Not verified: Jenkins agents. They are Linux, and Chrome is not guaranteed there.
- The ticket describes the state before the tests were re-enabled. The CI evidence above shows that state no longer holds on GitHub Actions.

(d) Verdict: ALREADY DONE for GitHub Actions (evidence above). The owner or Christos should decide whether to close the ticket or re-scope it to local and Jenkins runs. Recorded choice (rung 1): no PR and no code change here.

## Ranking as first contributions for a build-oriented contributor

1. SOLR-17861. It is the only one of the three with a concrete slice that needs no infrastructure and no owner access: a Gradle task in `ui/` with a clear scope.
2. SOLR-17970. The repo-side Linux packaging job can be drafted, but running it on Windows and macOS and publishing to nightlies needs INFRA and PMC.
3. SOLR-17676. It is already done on GitHub Actions, so the only work left is the local and Jenkins check, which is small.

Reason for the top pick: SOLR-17861 is the one where a build-oriented contributor can write and test real Gradle code today, without waiting on INFRA or on a verdict about what the ticket already covers.
