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
