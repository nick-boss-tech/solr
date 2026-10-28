# Gate: PR 5241 core overview replication errors, round 1

Status: DONE

Completed 2026-10-28 (cleanup slice, phone agent): this round finished NOT GREEN and was superseded for final proof by the round-2 chain ending at head `3b36f32e93d5`; its evidence stands as recorded. Final proof: `receipts/PR-5241-overview-errors.md`.

- Ticket and upstream PR: SOLR-16398, https://github.com/apache/solr/pull/5241.
- Intended host: any available capable Linux gate host with JDK 21, Chrome/Chromium, chromedriver or Selenium Manager access, and the branch's Gradle and generated JS client dependencies. This is the AngularJS `solr/webapp` module, not the Kotlin `ui/` build.
- Required tags: gate, review; the host must also have the stated browser capability. Windows implements and reviews only; it runs no Gradle or tests.
- Fork: `nick-boss-tech/solr`; branch: `solr-16398-submit`.
- Exact offered head: `80ca9374f48f292af159c7e9600019b5947adc49`.
- Original PR head / fail-before production base: `aa8272ef089c39df449c519551ef439557b876f8`.
- Implementation claim: `claims/pr-5241-overview-errors-windows-round-1.md`.
- Implementation report: `reports/pr-5241-overview-errors-windows-round-1.md`.
- Receipt destination: `receipts/PR-5241-overview-errors.md` (new, gate host owns it).
- Source delta: `solr/webapp/web/js/angular/controllers/core-overview.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiCoreOverviewErrorsTest.java` only.

## Claim and preflight

Refresh the live fork head and PR discussion before claiming. A newer contributor head requires an ancestry/scope check; do not silently substitute it for this job's fail-before base. A different offered fork head requires a superseding gate pinned to that head. Claim only if no live claim already holds this gate. The gate claim must record host/toolchain/browser versions, local build lock, positive and negative worktree paths, runner path, log directory, and UTC start time. Serialize all legs under the host build lock.

Use a clean detached positive worktree at the offered head. Fetch from the fork and verify the full SHA. Confirm the parent is the original PR head and the two-file follow-up delta is ticket scoped. Run `git diff --check` and `./gradlew tidy`. Require a clean tree. If tidy changes files, make a scoped formatting repair commit with Nick Shanin's ICLA identity and no trailers, fast-forward push to this fork branch, verify its remote SHA, and add a superseding gate naming the new exact head before final proof. Do not count a run against an uncommitted repair as proof of this SHA.

The module already disables its test security manager to permit external browser binaries. Do not alter that configuration or add global test exclusions. Do not pass `-PdisableJsClient=true`: these tests need the real generated JS bundle. Chrome must be available to the test JVM, through its documented binary discovery, `CHROME_BIN`, or `-Dtests.selenium.chrome.binary=/absolute/path/to/chrome` recorded in the receipt. A missing browser, unavailable driver, JS build failure, or skipped Selenium group is setup failure, not a test result.

## Required proof legs

1. Positive focused class at the exact offered head:

   ```bash
   ./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreOverviewErrorsTest -Ptests.selenium=true -Ptests.seed=5241C0DE
   ```

   Require exactly three executed tests, zero failures, errors, or skips:

   - `testReplicationForbiddenShowsPermissionFailure`: replication-only HTTP 403 produces the visible permission banner, without the missing-configuration message.
   - `testReplicationServerErrorShowsMessage`: replication-only HTTP 503 produces the actual server error in the global exception UI, without the missing-configuration message.
   - `testReplicationUnauthorizedRedirectsToLogin`: replication-only HTTP 401 redirects to the Basic login form, clears the stale authorization header, and preserves the overview return location.

   Each test first requires successful real overview reads and an idle Angular $http client, then invokes only the existing controller's replication refresh method. A test-only Jetty filter fails that real HTTP request; the generated client, callback, ApiErrorHandler, and rendered UI remain real. The test waits for the filter's recorded status before asserting the UI. It allows console errors only for that exact replication endpoint, not arbitrary JavaScript or network failures.

2. Neighboring success paths and authentication at the same head:

   ```bash
   ./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest --tests org.apache.solr.webapp.AdminUiCoreAdminScreenTest --tests org.apache.solr.webapp.AdminUiReplicationStandaloneTest --tests org.apache.solr.webapp.AdminUiReplicationScreenTest --tests org.apache.solr.webapp.AdminUiSecurityAuthTest -Ptests.selenium=true -Ptests.seed=5241C0DE
   ```

   Require zero failures, errors, or skips; record actual per-class counts. Do not describe this as abort-replication coverage: the existing standalone method does not exercise abort.

3. Root checks at the same head:

   ```bash
   env -u NODE_ENV ./gradlew check -x test
   ```

   Record the real result with no silent exclusions. NODE_ENV is explicitly unset for this command so production-only npm installation does not omit build/check dependencies; disclose this execution condition in the receipt. Also run `node --check solr/webapp/web/js/angular/controllers/core-overview.js` with the host/build Node runtime, and finish with `git diff --check` and a clean worktree.

4. Fail-before proof in a separate worktree at original PR head `aa8272ef089c39df449c519551ef439557b876f8`, overlaid with **only** `AdminUiCoreOverviewErrorsTest.java` from the final offered head. Do not copy the fixed production JavaScript. Record the synthetic tree/diff and test-file hash. Run the same focused-class command as leg 1 with the same seed and Selenium opt-in.

   Require all three new methods to fail on their intended UI checks after their healthy-start and injected-status waits pass: missing permission banner for 403, missing server-message banner for 503, and missing login form/redirect for 401. The old production callback instead sets the generic configuration message. Preserve method-level XML, assertion messages, and browser failure artifacts demonstrating those outcomes. A compile failure, healthy-start timeout, missing injected request, missing browser/client, skip, or unrelated console failure does not satisfy this leg. If a new method passes on original production, investigate and correct the discrimination before declaring GREEN.

## Evidence and completion

Keep a separate log and fresh JUnit XML copy for each test leg before cleanTest runs again. Retain browser versions, seeds, exact commands/environment, start/end times, actual test counts, and exit codes. Preserve screenshots, page source, and console logs for the expected negative failures. Record unrelated setup failures separately from patch failures. Executed failures are investigated/repaired under WORKFLOW.md; do not blindly rerun them or weaken assertions. A setup refusal before tests execute may be repaired and relaunched once with its cause recorded.

GREEN requires clean tidy at the final pinned head, passing focused and neighbor legs, successful root/JS checks, and discriminating fail-before proof for all three regressions. Write the receipt with links to retained logs/XML and a concise factual draft of the upstream follow-up as its final section. Add a completion report naming this gate and implementation claim; leave these immutable input files unchanged. No public comments, PR creation, contributor-branch changes, or JIRA writes are part of this gate. Until the receipt is green at the offered remote head, status remains **Implemented, Proof Pending**.
