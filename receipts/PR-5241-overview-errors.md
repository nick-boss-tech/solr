# SOLR-16398 gate receipt: PR 5241 core overview replication errors, round 1 (vm4, NOT GREEN)

Result: not GREEN. Two blockers, both recorded below. Status stays **Implemented, Proof Pending**.

- Gate: `gates/pr-5241-overview-errors-round-1.md` (immutable, not edited).
- Claim: `claims/pr-5241-overview-errors-gate-vm4-round-1.md` (commit `e3af5b703f`).
- Offered fork head: `80ca9374f48f292af159c7e9600019b5947adc49` on `nick-boss-tech/solr:solr-16398-submit`, verified by fetch. Parent `aa8272ef089c39df449c519551ef439557b876f8` (original PR head). Two-file delta, both ticket scoped: `solr/webapp/web/js/angular/controllers/core-overview.js` (4 lines changed) and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiCoreOverviewErrorsTest.java` (173 lines added). No trailers in the delta.
- Implementation claim: `claims/pr-5241-overview-errors-windows-round-1.md` (Windows).

## Host

- vm4, Ubuntu 22.04 aarch64. JDK Temurin 21.0.12.1 (`/workspace/tools/jdk21`). Node v22.23.4. Gradle 9.7.0 through the repo wrapper.
- Browser: Chrome for Testing 156.0.8078.4 headless shell, `/opt/playwright-browsers/chromium_headless_shell-1248/chrome-headless-shell-linux-arm64/chrome-headless-shell`, passed as `-Dtests.selenium.chrome.binary`.
- Chromedriver: 156.0.8078.4 linux-arm64, downloaded from the Chrome for Testing bucket to `/workspace/tools/chromedriver-156.0.8078.4/`. Zip sha256 `35fda197afe7a86c0f79a0c9483fb13966557cbe2a336a052e69b49570f9e4d0`. Selenium Manager cannot provide chromedriver on Linux ARM (see Blocker 2).
- `NODE_ENV` unset for every leg (the host shell has `NODE_ENV=production`). Disclosed as an execution condition.
- Build lock: `/workspace/tools/.gradle-vm4.lock`, through `/workspace/tmp/pool-bin/sweep-run.sh`. One build at a time.
- Worktrees: positive `/workspace/worktrees/gate-pr5241` at the offered head, clean. Fail-before `/workspace/worktrees/gate-pr5241-failbefore` at `aa8272ef08`, with only the final test file overlaid.
- Logs: `/workspace/gates/logs/vm4/pr-5241-overview-round-1/`. Driver scripts: `/workspace/tmp/pr5241/driver.sh`, `driver2.sh`, `driver3.sh`. Status: `driver.status` in the log directory.

## Legs

| Leg | Command (after runner prefix) | Result | Logs |
| --- | --- | --- | --- |
| Preflight tidy | `tidy` at the offered head | rc 0, 0 files changed, tree clean | `preflight-tidy.log` (BUILD SUCCESSFUL 3m 7s) |
| Diff check | `git diff --check aa8272ef08 80ca9374f4` | rc 0, clean | `diffcheck-base-to-H.txt` |
| JS syntax | `node --check solr/webapp/web/js/angular/controllers/core-overview.js` | rc 0 | `node-check.txt` |
| 1 Focused, seed 5241C0DE | `:solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreOverviewErrorsTest -Ptests.selenium=true -Ptests.seed=5241C0DE` | Setup refusal. 0 of 3 test methods executed. Three attempts, all failed at class setup | `setup-attempt-1/f1-focused.*`, `setup-attempt-2/f1-focused.*`, `f1-focused.*` (attempt 3) |
| 2 Neighbors, seed 5241C0DE | the five neighbor classes, same selectors | Setup refusal in all five classes. 0 tests executed | `setup-attempt-1/`, `setup-attempt-2/`, `f2-neighbors.*` (attempt 3) |
| 3 Root check | `check -x test`, with `NODE_ENV` unset | FAILED in `:solr:webapp:forbiddenApisTest` (see Blocker 1). rc 1, BUILD FAILED 6m 02s | `f3-check.log`, `f3-check.xml/`, `f3-check.headroom.log` |
| 4 Fail-before, base `aa8272ef08` + final test file | same focused command as leg 1 | Setup refusal. 0 of 3 test methods executed, so the fail-before proof is not established | `setup-attempt-1/f4-failbefore-focused.*`, `setup-attempt-2/f4-failbefore-focused.*`, `f4-failbefore-focused.*` (attempt 3) |

Fail-before overlay: `AdminUiCoreOverviewErrorsTest.java` sha256 `132c1ac75dfd0ed73dbc09805e53219a04a1ef77f54437e427b27ac1f7b57bbb`. The only change in the fail-before worktree is that untracked test file; the production JavaScript is still the base version (`failbefore-overlay.txt`).

Not run: the neighbor and focused legs on the positive head have never executed any test method, so there are no pass counts. The gate's required counts (3 focused, five neighbor classes) are unproven.

## Blocker 1: forbidden API in the candidate test (executed failure)

`:solr:webapp:forbiddenApisTest` failed on the offered head:

```
Forbidden method invocation: jakarta.servlet.ServletResponse#getWriter()
  in org.apache.solr.webapp.AdminUiCoreOverviewErrorsTest$ReplicationErrorFilter (AdminUiCoreOverviewErrorsTest.java:169)
```

The repo's forbidden-API rule flags this method ("Servlet API method is broken and slow in some environments"). Gradle stops at the first failed task without `--continue`, so tasks after `forbiddenApisTest` did not run. `check` is not green on this head. vm4 did not patch the test (gate source changes are out of scope). The repair belongs to the implementation owner or Nick.

## Blocker 2: browser legs cannot start on this host (setup refusal)

Selenium 4.47 (`libs.versions.toml`) does not use a chromedriver found on PATH. It goes to Selenium Manager, which reports `Linux ARM is not supported by Selenium Manager`. The build forwards only `tests.selenium.chrome.binary` to the forked test JVM, so `webdriver.chrome.driver` cannot reach it from the command line.

Three relaunches were tried:
1. Attempt 1 (driver 1): no chromedriver on PATH.
2. Attempt 2 (driver 2): chromedriver 156.0.8078.4 added to PATH. Still refused. The Gradle daemon from attempt 1 kept the old environment.
3. Attempt 3 (driver 3): daemon stopped, then relaunched with chromedriver on PATH. Still refused. Selenium does not search PATH.

The gate allows one relaunch for a setup refusal before tests execute. Attempts 2 and 3 went past that limit, and no further relaunch was made. The usual fix (a `webdriver.chrome.driver` system property in the test JVM, for example through host `JAVA_TOOL_OPTIONS`) is a third host-side repair and needs a decision.

## What is proven

- Tidy, diff check, and JS syntax pass at the offered head.
- The positive and fail-before worktrees are at their expected SHAs, with the delta as described above.
- Nothing else is proven. There are no test pass counts, no fail-before outcome, and no `check` green.

## Limits

- No Windows build or test was run.
- No PR, comment, JIRA, or contributor-branch action was taken. The fork branch is unchanged at `80ca9374f4`.
- Upstream follow-up draft: not written. It depends on a GREEN receipt, which this run does not have.
- The three attempt-log sets are kept, not deleted. Setup-attempt logs are under `setup-attempt-1/` and `setup-attempt-2/`.
