# Receipt: wasm browser leg for PR 5258 at d02ccdcdd8 (vm6, 2026-10-30)

- Job: `assignments/wasm-browser-test-5258-round-1.md` (HANDED to vm6 by `67c5a6b37c`; predecessor vm5 claim RELEASED).
- Claim: `claims/solr-5258-wasm-browser-leg-round-1-vm6.md` (started 2026-10-30T19:49:04Z).
- Host: vm6, Linux 6.1 aarch64 (Amazon Linux 2023). Capability tags: gate, ui, build.
- Head tested: `d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a` (PR #5258 head; verified with `git rev-parse HEAD` in the scratch worktree before the run).
- Verdict: **PASS**. 19 suites, 69 tests: 66 passed, 0 failures, 0 errors, 3 skipped (all three are pre-existing `@Ignore`, see below).
- Completed: 2026-10-30T20:04:05Z (UTC, time of this receipt's push).

## Browser

| Item | Value |
| --- | --- |
| Binary (`CHROME_BIN`) | `/opt/playwright-browsers/chromium-current`, a symlink to `chromium_headless_shell-1248/chrome-headless-shell-linux-arm64/chrome-headless-shell` |
| Version | Google Chrome for Testing 156.0.8078.4 (`--version`) |
| Karma launcher used | `ChromeHeadless156.0.8078.4` (in the JUnit `testsuite` names) |
| No-sandbox | Not applied. The run is not as root (user `claude`), and Karma's headless launch succeeded without it. |

## Per-suite counts (fresh XML, `ui/shared/build/test-results/wasmJsBrowserTest/`)

| Suite | Tests | Failures | Errors | Skipped |
| --- | --- | --- | --- | --- |
| components.auth.AuthUtilsTest | 7 | 0 | 0 | 0 |
| components.auth.AuthUtilsTestJs | 4 | 0 | 0 | 1 |
| components.auth.viewmodel.AuthenticationStateHolderTest | 1 | 0 | 0 | 0 |
| components.auth.viewmodel.BasicAuthStateHolderTest | 3 | 0 | 0 | 0 |
| components.auth.viewmodel.OAuthStateHolderTest | 5 | 0 | 0 | 0 |
| components.collections.data.HttpCollectionsRepositoryTest | 5 | 0 | 0 | 0 |
| components.configsets.viewmodel.ConfigsetsStateHolderTest | 3 | 0 | 0 | 0 |
| components.environment.viewmodel.EnvironmentStateHolderTest | 2 | 0 | 0 | 0 |
| components.start.viewmodel.StartStateHolderIntegrationTest | 5 | 0 | 0 | 0 |
| views.auth.BasicAuthContentTest | 4 | 0 | 0 | 0 |
| views.auth.UserAuthenticationContentTest | 1 | 0 | 0 | 1 |
| views.collections.CreateCollectionErrorsTest | 3 | 0 | 0 | 0 |
| views.collections.CreateCollectionFormTest | 10 | 0 | 0 | 0 |
| views.components.SolrCardTest | 1 | 0 | 0 | 0 |
| views.configsets.ConfigsetsDropdownTest | 3 | 0 | 0 | 0 |
| views.configsets.CreateConfigsetDialogTest | 2 | 0 | 0 | 0 |
| views.configsets.ImportConfigsetDialogTest | 4 | 0 | 0 | 0 |
| views.environment.EnvironmentContentTest | 1 | 0 | 0 | 0 |
| views.start.StartContentTest | 5 | 0 | 0 | 1 |
| **Total** | **69** | **0** | **0** | **3** |

All 19 XML files are timestamped 19:58:04Z to 19:58:07Z, from this run. Copies are in `receipts/solr-5258-wasm-browser-leg-round-1-xml/`.

## Skipped tests

All three are `@Ignore` lines with a TODO that predates the PR head, so they are not PR changes:

- `ui/shared/src/wasmJsTest/kotlin/org/apache/solr/ui/components/auth/AuthUtilsTestJs.kt:70` (`@Ignore`, present at 555e1bf, in the base).
- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/auth/UserAuthenticationContentTest.kt:34` (`@Ignore // TODO Currently fails on wasmJs`, from c78939a620b, 2026-09-21).
- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/start/StartContentTest.kt:62` (`@Ignore // TODO Currently fails on wasmJs`, from c78939a620b, 2026-09-21).

## Commands and exit codes

| Step | Command | Exit |
| --- | --- | --- |
| Scratch worktree | `git worktree add --detach /workspace/worktrees/pool-vm6-5258-wasm d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a` (from `/workspace/solr`) | 0 |
| Wrapper jar | copied from `/workspace/solr/gradle/wrapper/gradle-wrapper.jar`; sha256 `2db75c40782f5e8ba1fc278a5574bab070adccb2d21ca5a6e5ed840888448046` matches `gradle/wrapper/gradle-wrapper.jar.sha256` at `d02ccdcdd8` | n/a |
| Root properties | `gradle.properties` copied from `/workspace/solr/gradle.properties` (gitignored; it differs from `gradle/template.gradle.properties` at line 103, which is the locally generated copy) | n/a |
| Run | `env -u NODE_ENV CHROME_BIN=/opt/playwright-browsers/chromium-current bash pool/bin/sweep-run.sh /workspace/worktrees/pool-vm6-5258-wasm /workspace/gates/logs/vm6/solr-5258-wasm-round-1 wasm-browser-attempt1 -p ui :shared:wasmJsBrowserTest --no-daemon --console=plain` | 0 (`BUILD SUCCESSFUL in 5m 46s`, 42 actionable tasks, all executed, none up to date) |

The sweep run takes the host lock `/workspace/tools/.gradle-vm6.lock`. It ran with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1` (Temurin 21.0.12.1+1), Gradle 9.7.0 from the branch wrapper, and `GRADLE_USER_HOME=/workspace/gradle-home`. `:kotlinWasmNpmInstall` ran and passed on this host, which the phone agent's VM could not complete.

`pool/bin/sweep-run.sh` is run with `bash` because the file is not executable in the board worktree.

## Host guard

- Before the run: 4.6G free on `/`, 479314 free inodes (`pre 2026-10-30T19:52:21Z`).
- After the run: 4.3G free on `/`, 448736 free inodes (`rc=0 end 2026-10-30T19:58:09Z`). Both are well above the limits (2 GB free; inodes above 15%).

## Logs

Host-local, in `/workspace/gates/logs/vm6/solr-5258-wasm-round-1/`:

- `wasm-browser-attempt1.log` (full Gradle output, 79 lines)
- `wasm-browser-attempt1.headroom.log`
- `driver-attempt1.out`

Portable copies: XML results in `receipts/solr-5258-wasm-browser-leg-round-1-xml/`.

## Not run or not established

- The vm5 log files named in the handoff (`/workspace/gates/logs/vm5/solr-5258-wasm-round-1/`) are on vm5's disk. They are not reachable from vm6, and this run did not depend on them. The three vm5 attempts stopped at `compileTestDevelopmentExecutableKotlinWasmJs` on VM reboots. This run completed without a reboot.
- Only the wasm browser suite was run, as assigned. Other PR 5258 checks (spotless, the JVM suite) are recorded in `receipts/pr-5258-17913-removal.md` and were not repeated.

## Side effects

- No GitHub, JIRA, or PR action. No push to the PR branch. The scratch worktree is detached at the PR head, with the ignored copies only and no tracked changes.
- Board: claim and this receipt, plus the assignment status set to DONE.
