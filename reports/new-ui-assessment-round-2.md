# Report: new-ui-assessment-round-2

Research only. Main read against apache/solr main 79b23df58d. Each section is one claim under the small-claims rule.

## SOLR-17572: Migrate Gradle files to use Kotlin DSL

(a) Ask: move the Groovy Gradle scripts to Kotlin DSL, "either one by one or all together". Open, labels `gradle`, `newdev`. Unassigned. One comment (dsmiley, 2025-03-02) with no decision.

(b) Exists today on main:
- 97 Groovy `.gradle` files, 12,204 lines in total. 56 are under `gradle/`, 33 under `solr/`, 5 under `build-tools/`, plus the root `build.gradle` and `settings.gradle` and one under `platform/`.
- 8 Kotlin `.gradle.kts` files. Five are under `ui/`, which is already Kotlin DSL and uses `ui/gradle/libs.versions.toml`. Three are outside `ui/` and show the team already accepts Kotlin DSL per file: `gradle/testing/coverage.gradle.kts`, `gradle/validation/dependency-analyze.gradle.kts`, `solr/webapp/js-client/build.gradle.kts`.
- Dynamic Groovy that a mechanical port would have to rewrite by hand: 78 `apply from:` calls, 183 `ext` uses (`ext.` or `ext {}`), and about 730 closure or `def` lines. Example: `build.gradle` passes shared state through `apply from:` into `pluginManagement` via `delegate`, in `settings.gradle` (line 18).
- The build-infra and missing-doclet included builds are Groovy scripts that call into `build-tools/build-infra/src` (9 Java sources).
- Gradle is 9.7.0 (`gradle/wrapper/gradle-wrapper.properties`).

(c) Missing: no Kotlin DSL conversion plan, no rule for when a Groovy file may stay Groovy, and no version-catalog-only pass over the root build. A Kotlin script can still `apply(from = ...)` a Groovy file, so the port can be done one file at a time without a flag day.

(d) Verdict: TOO BIG AS STATED. The ticket says "all together" as an option, which is not startable. Smallest first slice: `build-tools/missing-doclet/build.gradle` (44 lines, uses only `plugins`, `apply from:` and a `java` and `tasks.withType` block). Port it to `build.gradle.kts`, keep its `apply from:` calls as Groovy, and run that included build's `build` task to prove it. The next slices are `build-tools/missing-doclet/settings.gradle` (31 lines) and `build-tools/build-infra/settings.gradle` (31 lines).

Not verified: no build or port was run. Research only, per the assignment.

## SOLR-17585: CodeCache full during Gradle builds

(a) Ask: the Gradle build ran out of JIT CodeCache space again, similar to SOLR-16103. The ticket suggests raising `-XX:ReservedCodeCacheSize` to 120m in `gradle.properties` and asks for the cause if it can be found. Open, unassigned, no labels, no comments. Filed 2024-12-05, last updated 2025-01-30. The only evidence is one GitHub Actions run link, which is from December 2024.

(b) Exists today on main:
- `gradle/template.gradle.properties:78`: `org.gradle.jvmargs=-Xmx2g -XX:ReservedCodeCacheSize=256m ...`. The daemon has 256m. It was raised from the default by `54123941b1` ("build: Fix Out of space in CodeCache error (#3034)", 2025-01-17). That commit landed after the ticket was filed and before its last update, and it fixes the same symptom.
- `gradle/testing/defaults-tests.gradle:53`: test JVMs use `-XX:ReservedCodeCacheSize=120m`. That is the SOLR-16103 value from `c9d54e68b6` (2022-03-18). So the 120m in the ticket is the test-JVM setting, not the daemon setting.
- The ticket's suggestion, applied to `gradle.properties` as written, would lower the daemon from 256m to 120m, the opposite of what #3034 did.

(c) Missing: no reproduction on main. The Actions run is out of retention, and a reproduction would need a full build on the gate host with the daemon at the ticket's value. Nothing in the repo records a recurrence after #3034.

(d) Verdict: ALREADY DONE, on the evidence that #3034 raised the daemon's code cache and no later occurrence is recorded. Not reproduced. If the owner wants it confirmed, the next step is one clean build on the gate host with the daemon at 256m, watching for CodeCache errors. That needs a gate-host build, not research. Do not apply the ticket's 120m value to the daemon.

Not verified: no build was run. Research only, per the assignment.
