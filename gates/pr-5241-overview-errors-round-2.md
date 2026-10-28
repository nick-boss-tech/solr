# Gate repair: PR 5241 core overview errors, round 2

Status: DONE

Completed 2026-10-28 (cleanup slice, phone agent): superseded by `gates/pr-5241-overview-errors-round-2-f3-vm4.md` (already DONE) for final proof at head `3b36f32e93d5`, recorded in `receipts/PR-5241-overview-errors.md`. Supersedes `gates/pr-5241-overview-errors-round-1.md`; the NOT GREEN evidence stands.

- Ticket / PR: SOLR-16398 / https://github.com/apache/solr/pull/5241.
- Fork / branch: `nick-boss-tech/solr:solr-16398-submit`.
- Exact repaired head: `56365b43ee72977d1934b5a5c833e1071f75c557` (remotely verified after plain push).
- Previous failed candidate and direct parent: `80ca9374f48f292af159c7e9600019b5947adc49`.
- Original PR production / fail-before base: `aa8272ef089c39df449c519551ef439557b876f8` (unchanged).
- Repair claim: `claims/pr-5241-forbidden-api-repair-windows-round-2.md`.
- Repair report: `reports/pr-5241-forbidden-api-repair-windows-round-2.md`.
- Existing gate owner: vm4, claim `claims/pr-5241-overview-errors-gate-vm4-round-1.md`. Keep that host's ownership unless it explicitly hands this off; do not start a parallel gate elsewhere. Add a round-2 claim naming this job, host, repaired head, lock, runner, worktrees, log paths, and environment.
- Required capability: Linux JDK 21, generated JS client build, Node, working Chrome/Chromium and matching chromedriver; tags gate/review. This is legacy `solr/webapp`, not Kotlin `ui/`.
- Receipt: the gate host updates its own `receipts/PR-5241-overview-errors.md`, preserving the round-1 failures and setup history in the updated receipt or an immutable linked report. Add completion report `reports/pr-5241-overview-errors-gate-round-2.md`.

## Why this is a repair

Round 1's root check genuinely failed at `:solr:webapp:forbiddenApisTest`: the new fixture called forbidden `ServletResponse.getWriter()`. The repaired commit changes only that test file: it imports StandardCharsets and writes the same JSON as explicit UTF-8 bytes through getOutputStream(). No suppression, forbidden-API signature, assertion, production controller, or build configuration changed.

The round-1 receipt also records three browser setup attempts and zero executed test methods. Putting chromedriver on PATH and stopping the old daemon did not solve the observed `Linux ARM is not supported by Selenium Manager` refusal. Those attempts are not passing proof, and round 2 does not authorize repeating unchanged launches.

## Concrete browser setup repair

The implementation owner authorizes a bounded new setup attempt with a specific driver path in the **forked test JVM**, as part of this source-and-environment repair. This is a settled host setup choice, not a request for Nick to decide it again.

Use vm4's already downloaded matching ARM64 chromedriver. Resolve and record its exact absolute executable path under `/workspace/tools/chromedriver-156.0.8078.4/`, executable mode, architecture and `--version`; compare it with the configured Chrome binary/version. Do not assume the directory itself is the executable. Retain the original browser override `-Dtests.selenium.chrome.binary=<absolute-browser-binary>` on every browser command.

Set `-Dwebdriver.chrome.driver=<absolute-driver-executable>` through host `JAVA_TOOL_OPTIONS` for the Gradle launch and its forked test JVM, preserving any unrelated existing options. Verify the property reaches the actual test JVM; a Gradle-client-only `-D` is insufficient because this build forwards only its named browser override. Run with a fresh Gradle process (`--no-daemon`) under the host build lock. Confirm no conflicting driver environment setting overrides the chosen path. This avoids relying on Selenium Manager's ARM binary discovery, without changing the repository test harness.

Primary reference: [ChromeDriverService's driver property](https://github.com/SeleniumHQ/selenium/blob/trunk/java/src/org/openqa/selenium/chrome/ChromeDriverService.java) and [DriverFinder's explicit-property branch](https://github.com/SeleniumHQ/selenium/blob/trunk/java/src/org/openqa/selenium/remote/service/DriverFinder.java). The host should check its pinned Selenium dependency when validating startup; the references explain the intended setup, not a claim that a browser ran here.

Allow one browser startup attempt with this concrete setup change. If it still refuses, retain the new failure and inspect it before another attempt; do not run the neighbor/fail-before legs through a known setup refusal or add more blind relaunches. The prior attempt-limit breach stays recorded. Source/assertion changes, setup failures, and genuine test failures remain separate facts.

## Required legs at the repaired head

All commands run on Linux under the existing host build lock. Unset `NODE_ENV` for the build/test invocations as in round 1, and disclose it in the receipt. Use the actual browser and driver settings above for every browser leg. Do not disable the JS client, Selenium test group, or checks.

1. Fresh detached worktree at `56365b43ee72977d1934b5a5c833e1071f75c557`. Confirm direct parent `80ca9374f48f292af159c7e9600019b5947adc49`, original PR ancestry, and the one-file repair delta. Run `git diff --check` and `./gradlew tidy`; require clean tree. Run `./gradlew :solr:webapp:forbiddenApisTest` first to verify the reported failure is repaired before spending time on browser legs. If tidy/source repair changes the head, commit with Nick Shanin's ICLA identity, plain-push to the fork, remotely verify it, and add another superseding job before final proof. Never count dirty-tree proof as proof of a SHA.

2. Focused positive browser class (add the recorded browser property, JAVA_TOOL_OPTIONS driver property, and `--no-daemon`):

   ```bash
   ./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreOverviewErrorsTest -Ptests.selenium=true -Ptests.seed=5241C0DE
   ```

   Require exactly three executed methods, zero failures/errors/skips: `testReplicationForbiddenShowsPermissionFailure`, `testReplicationServerErrorShowsMessage`, and `testReplicationUnauthorizedRedirectsToLogin`. Healthy overview reads and the injected-status wait must pass before the UI assertions. Require 403 permission warning, 503 actual server-message banner, and 401 visible Basic login with stale auth header cleared and return route preserved. Only expected console errors for the exact replication URL may be allowed.

3. Neighbor browser classes at the same head and with the same environment:

   ```bash
   ./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiCoreAdminScreenTest --tests org.apache.solr.webapp.AdminUiReplicationStandaloneTest --tests org.apache.solr.webapp.AdminUiReplicationScreenTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5241C0DE
   ```

   Require zero failures/errors/skips and record actual per-class counts. Do not claim abort-replication coverage from these selectors.

4. Full root check `env -u NODE_ENV ./gradlew check -x test` at the repaired head, no silent exclusions. The earlier run stopped at forbiddenApisTest and did not prove later tasks; a targeted forbiddenApisTest pass does not replace this full check. Run the controller's `node --check`, final `git diff --check`, and clean status at this same head. Earlier tidy/Node/whitespace passes cover only `80ca9374f48` and are historical.

5. Separate fail-before worktree at `aa8272ef089c39df449c519551ef439557b876f8` with **only the repaired test file** from `56365b43ee72977d1934b5a5c833e1071f75c557` overlaid. Do not copy the fixed controller or the old getWriter test. Record the overlay diff and new file hash. Run the focused command from leg 2 with the same seed and working browser environment. Require all three methods to fail at the intended UI checks, after successful healthy-start and HTTP-status injection: missing permission warning (403), missing server-message banner (503), missing login form (401). Compile/setup failures or missing injection are not fail-before evidence.

## Evidence and completion

Use new round-2 log prefixes/directories. Save fresh method-level XML before each cleanTest, with exact SHAs, test-file hashes, commands, browser/driver/JDK versions, environment condition, start/end times, actual counts, exit codes, and negative browser screenshots/page source/console logs. Retain all three round-1 setup attempt sets and its forbidden-API failure. If executed tests fail, diagnose and repair rather than rerunning or weakening assertions.

GREEN requires every leg above at the final pinned head and a remotely matching branch tip. Until then the contribution remains **Implemented, Proof Pending**. Do not post a public follow-up, change the contributor's branch, edit the PR body, open a PR, or write JIRA as part of the gate.
