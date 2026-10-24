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

## SOLR-17598: Windows script logic that handles SOLR_LOGS_DIR is faulty

(a) Ask: on Windows, a `SOLR_LOGS_DIR` set in the environment stops Solr from starting, and a path with a space breaks `bin\solr.cmd stop --all`. Sometimes the variable is ignored. The ask is that the environment variable work the same as the value in `solr.in.cmd`. Open, unassigned, no labels. The reporter is not sure the "ignored" case still reproduces. The ticket notes the same may apply to other environment variables on Windows.

(b) Exists today on main, `solr/bin/solr.cmd`:
- Line 42 to 43: `solr.in.cmd` is loaded before the start logic. `solr/bin/solr.in.cmd:114` has `REM set SOLR_LOGS_DIR=logs`, so the file does not set the variable by default and an environment value reaches the start logic.
- Lines 909 to 913: an empty `SOLR_LOGS_DIR` falls back to `%SOLR_SERVER_DIR%\logs`. A set value has its double quotes stripped. Paths with spaces are not quoted here, which the `IF [%SOLR_LOGS_DIR%] == []` form tolerates.
- Lines 917 to 920: if `SOLR_HOME` is under `%SOLR_TIP%\example`, `SOLR_LOGS_DIR` is replaced with `%SOLR_HOME%\..\logs`. This silently ignores the environment value, which matches the "ignored" symptom in the ticket.
- Lines 1136 to 1139: `SOLR_LOGS_DIR_QUOTED` is quoted and used for `-Dsolr.logs.dir`.
- Lines 1148 to 1155: `mkdir` and the writability test are quoted.
- Lines 1158 to 1161: the reserved-name check writes a space-padded pattern file and runs `findstr /i /C:" %SOLR_LOGS_DIR% "`. A path with spaces is compared as a literal, and `findstr` reads `/C:` literally, so spaces are handled.
- Line 1185: the console log redirect uses delayed expansion, `!SOLR_LOGS_DIR!`. A value containing `!` or `&` is the most likely place for a failure that depends on the value, but the ticket's example is a plain path with spaces.
- No `SOLR_LOGS_DIR` reference in the stop path. Stop reads the PID and port files, so the stop failure with a space in the logs path is not explained by the logs variable and needs a Windows run to locate.

(c) Missing: a Windows reproduction. This Linux host cannot run `cmd.exe`. The pool's Windows host can, and the assignment's tags allow a Windows check there. Without a run, the start failure has no confirmed cause. The most likely candidates are the delayed-expansion redirect at line 1185 and the `SOLR_HOME` override at line 919.

(d) Verdict: READY TO VOLUNTEER, with a caveat. A newcomer can start from the ticket text and the lines above, but the fix must be checked on Windows, since nothing on Linux exercises these paths. Smallest first slice: on the Windows host, reproduce the three steps in the ticket with `SOLR_LOGS_DIR` set to a path with a space, and record which line fails (`solr.cmd` run with `@echo on` or the stop path). Do not change the script until that is recorded.

Not verified: no Windows run, no script change. Research only, per the assignment.

## SOLR-17898: Updating collection property via v2 API endpoint fails with 500 response

(a) Ask: `PUT /api/collections/{coll}/properties/{name}` with `{"value": "bar"}` should create or update the collection property. Reported as HTTP 500 with the HK2 error "Could not find a suitable constructor in org.apache.solr.handler.admin.api.CollectionProperty". Open, Major, reporter malliaridis, affects 9.9. Comments: utsav00 (2026-04-19) says the PUT returns status 0 on his run; malliaridis (2026-04-20) reproduced it on 10.0.0, found it working on current main, and said he would report back; utsav00 asked for an update on 2026-06-07; no reply recorded.

(b) Exists today:
- Fork main `498a09aea9`, `solr/core/src/java/org/apache/solr/handler/admin/api/CollectionProperty.java` lines 45 to 50: the three-argument `CoreContainer` constructor carries `@Inject`.
- That annotation was added by `9ead8599b1` (SOLR-12224, Jason Gerlowski, 2026-01-26, PR #4071), in the same commit that added the list and get read APIs. Before it, the constructor had no annotation (`9ead8599b1^`, line 41).
- Tag `releases/solr/10.0.0` (apache/solr, commit `6c6c48a6f7`, 2026-02-12): the constructor at line 41 has no annotation, and `9ead8599b1` is not an ancestor of the tag. 10.0.0 has the unannotated constructor, which is the state the ticket reports.
- Among the 49 admin API classes in the package with a `CoreContainer` constructor, 44 carry `@Inject`. The five without it are the base classes (`AdminAPIBase`, `BackupAPIBase`, `CoreAdminAPIBase`), `InstallCoreData`, and `UpgradeCoreIndex`.
- The v2 test gap: nothing under `solr/core/src/test` calls the v2 collection-property endpoint (grep for `CollectionProperty` and `api/collections/.../properties` finds no v2 request). A regression on that path would not be caught.
- Reading that settles the mechanism (rung 1): the failing class and the working class differ only by the annotation on the constructor the error names. The HK2 source was not read, so "HK2 needs `@Inject` on a non-default constructor" is an inference from the error text and this diff.
- Same pattern, untested: `InstallCoreData` (`solr/core/src/java/org/apache/solr/handler/admin/api/InstallCoreData.java` lines 44 to 50) has a four-argument constructor with no `@Inject`. It is registered as a v2 Jersey resource in `CoreAdminHandler.java` line 333 for `POST /api/cores/{coreName}/install` (`solr/api/src/java/org/apache/solr/client/api/endpoint/InstallCoreDataApi.java` line 32). No test covers that route. By the same mechanism it should fail on main the same way. This is a prediction, not an observation. `UpgradeCoreIndex` has the same shape but is not a registered Jersey resource.

(c) Open:
- No reproduction was run. Both "fails on 10.0.0 because of the missing annotation" and "the annotation fixes it" are rung 1 inferences. The probe that would settle it: on a checkout of `releases/solr/10.0.0`, PUT the property and expect a 500 with the HK2 message; on main, remove the annotation and expect the same. Neither was run here.
- Whether the 10.0.x line gets the one-line backport is a release decision for the owner, not something this read can settle.
- Whether the `InstallCoreData` v2 route is broken on main needs the same probe.
- The 9.9 line was not checked.

(d) Verdict: ALREADY DONE on main, fixed by `9ead8599b1` as a side effect of adding read APIs, not as a targeted fix. The 10.0.x line still has the bug. Not a volunteer item as written: the remaining work is a backport decision, a regression test that does not exist, and confirming the `InstallCoreData` route. Smallest first slice: a test under `solr/core/src/test/org/apache/solr/handler/admin/api/` that PUTs and GETs a collection property through the v2 path, run on main and on the 10.0.0 tag. No main-code change is needed on main.

Not verified: no build or test was run for this ticket. Research only, per the assignment.

## SOLR-18214: Base configset during configset creation is not optional

(a) Ask: `baseConfigSet` in the v2 configset create should be truly optional and omittable. The ticket reports that omitting it sends `null`, which fails with "Base ConfigSet does not exist: null", and that `_default` cannot be the fallback once a user has deleted `_default`. Open, Major, reporter malliaridis, labels `newdev`, `pull-request-available`. Jira comments: epugh (2026-04-27) says the semantics feel odd and he will think about it; janhoy and malliaridis (2026-09-04) discuss the choice and say there was no consensus.

(b) Exists today on main (fork `498a09aea9`):
- `solr/core/src/java/org/apache/solr/handler/configsets/CloneConfigSet.java` lines 65 to 73: the base is checked with `checkConfigExists(requestBody.baseConfigSet)` and passed on unchanged. A null base fails the check. No defaulting happens on the v2 path.
- `solr/api/src/java/org/apache/solr/client/api/model/CloneConfigsetRequestBody.java` lines 29 to 30: `@JsonProperty(defaultValue = DEFAULT_CONFIGSET)`. The `defaultValue` is schema metadata, not a runtime default; the reported `null` path shows the field stays null when omitted.
- The command layer already defaults: `solr/core/src/java/org/apache/solr/cloud/ConfigSetCmds.java` line 62 maps an empty base to `DEFAULT_CONFIGSET_NAME`, and line 121 uses `DEFAULT_CONFIGSET_NAME` when `BASE_CONFIGSET` is absent.
- The reference guide says the default is `_default` (`solr/solr-ref-guide/modules/configuration-guide/pages/configsets-api.adoc` around line 379).
- Deletion of `_default` is not guarded: no "cannot delete" check was found in `DeleteConfigSet.java` or `ConfigSetCmds.java`.
- Git: `CloneConfigSet.java` last changed in `7b6d7ce4c4` (SOLR-15701, #4264). History does not show an intentional decision to drop the default.
- PR: apache/solr #4368, "SOLR-18214: Default configset clone base to _default when omitted", by an external contributor, was closed without merge on 2026-09-04. It changes `CloneConfigSet` to resolve an omitted base to `_default`, with a unit test and a SolrCloud test (`TestConfigSetsAPI.testV2CreateWithoutBaseConfigsetUsesDefault`). Its body reports `:solr:core:test` and `spotlessCheck` passing in Docker on JDK 21. Not reproduced here. The ticket still carries `pull-request-available` with no open PR.

(c) Open design questions, which the ladder does not settle:
- Option 1, default to `_default`: matches the docs, the command layer, and PR #4368. janhoy calls this back-compatible.
- Option 2, no base when omitted (the ticket author's stated preference, "I would choose"): changes the documented behavior. janhoy says requiring an explicit base is a back-compat break before 11.0.
- Option 3, make `_default` undeletable, or give a clear error when the default base is missing: janhoy's alternative for the "users delete `_default`" case. Not settled by any comment.
- Rung ledger: rung 1 shows the command layer and docs both default to `_default`. Rung 3 is split: the ticket author states option 2 as his choice, while the docs state `_default`. Rung 4 options are recorded above. The choice is between maintainers' stated positions, so it goes to the author.

(d) Verdict: NEEDS CHRISTOS. Question: "When `baseConfigSet` is omitted on the v2 create, should it resolve to `_default` (as the docs, the command layer and PR #4368 do), or create with no base? And should `_default` become undeletable, or should a missing default base get a clear error?"
Smallest first slice, once option 1 is chosen: resolve an omitted base to `DEFAULT_CONFIGSET_NAME` in `CloneConfigSet.cloneExistingConfigSet` (lines 65 to 73) and add a `CloneConfigSetAPITest` case. The `_default`-deletion part is a separate slice and needs its own decision.

Not verified: no build or test was run. Research only, per the assignment.

## SOLR-17657: Evaluate and update checksum and signature verification

(a) Ask: evaluate Gradle's native dependency verification (`gradle/verification-metadata.xml`, with sha256 and optionally PGP signatures) and move to it if preferred. The ticket's acceptance criteria are: the GitHub workflows keep verifying checksums and optionally signatures; if native, update the developer guide, remove the custom checksum tasks, and remove the checksum files in `solr/licenses`. If not native, extend the existing tasks so the new UI module's checksums are generated too. Open, labels `checksum`, `gradle`, `integrity`, `verification`, `pull-request-available`. Unassigned. The ticket is addressed to Christos Malliaridis (malliaridis).

(b) Exists today on main:
- `gradle/validation/jar-checks.gradle` holds the custom checksum logic. `validateJarChecksums` (line 209) compares each dependency against `solr/licenses/<jar>.sha1`, and a missing file fails the build (line 223). The file says the checks share state without file outputs, so they must run together.
- `solr/licenses` holds 373 `.sha1` files and the license and notice files.
- `.github/renovate.json:26` lists `solr/licenses/*.sha1` as a file filter, so Renovate already regenerates these files on dependency bumps.
- No `gradle/verification-metadata.xml` exists on main. PR apache/solr#3828, which tests the native approach, has not merged. The ticket's comment from pkarwasz (2025-11-02) describes the experiment: the migration is straightforward, but it raises a maintenance question.
- The ticket says the new UI module is a Kotlin Multiplatform module that does not use the JavaPlugin, so the jar checks do not cover it. The top-level `settings.gradle` does not include `ui` as a Gradle build from the lines I checked. The `ui/` build is separate, so this is not confirmed here.

(c) Missing: a decision. The pkarwasz comment sets out two paths. (1) Keep sha256 metadata and have Renovate regenerate it with `--write-verification-metadata sha256` during its cleanup run. Integrity only, no authenticity. (2) Add PGP signature verification. That is all-or-nothing: builds fail when a key is missing, and the experiment found about 100 missing keys. Choosing between them is the ticket's open question, and the ticket is addressed to the maintainer.

(d) Verdict: NEEDS CHRISTOS. Question: should Solr move to Gradle's native verification metadata with sha256 maintained by Renovate (path 1), or add PGP signature verification with its roughly 100 missing keys (path 2)?

Not verified: no build was run and PR #3828 was not inspected beyond its ticket comment. Research only, per the assignment.
