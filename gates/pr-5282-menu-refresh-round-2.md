# Gate: PR 5282 legacy Admin UI menu refresh, round 2

Status: OPEN. Supersedes round 1 for positive and fail-before proof at the updated source head. Implemented, Proof Pending.

- Required capabilities: Linux gate host with JDK 21, Gradle 9.7.0 and build dependencies, Node/npm access, and working Chrome/Chromium plus ChromeDriver/Selenium Manager outside the Hatch browser harness. Required tags: `gate`, `review`, `records`.
- Fork/branch: `nick-boss-tech/solr:solr-16398-admin-ui-showcollection`.
- Exact head G: `82954b9df687d8239c4344b292b3afff5005f898` (tree `9294ed1c2de8cb0058a66815d8bcbc4ccba2d570`; parent F: `200ff5e5c6c214212f72f37485487f3092e05541`).
- Previous tested candidate F: `200ff5e5c6c214212f72f37485487f3092e05541`.
- Original review head O: `6ee3c9b6370e6e1e3b4da4afcf968a0806cc747f`.
- Original unguarded base B: `7855ab485d50b44af2fe9b52e77bf172d8ea663e`.
- Public PR: https://github.com/apache/solr/pull/5282 . The fork branch and PR head were separately read back at G after the ordinary push. Check again before executing. No force push or upstream write is authorized.
- Implementation report: `reports/pr-5282-menu-refresh-ci-failure-followup-2026-11-03.md`.
- Earlier job and partial receipt: `gates/pr-5282-menu-refresh-round-1.md` and `receipts/PR-5282-menu-refresh-round-1.md`. Leave those records unchanged.

## Failure found at F and repair at G

Upstream [Admin UI Tests run 41680303677](https://github.com/apache/solr/actions/runs/41680303677) ran at F and failed in `AdminUiMenuRefreshTest.testSelectorNavigationAndEmptyInput`. The archived XML reports 12 executions, 4 failures, 0 errors and 0 skips; all four failures are repeats of that one method. The assertion is `Option 'shelf' not found in select menu_refresh_selector`; the other eight distinct methods passed.

The test clones the production selector after the dashboard has initialized in standalone mode. That DOM selector is disabled before its SolrCloud data loads. The fixture then compiles the clone under its independent mocked SolrCloud scope, so the inherited `disabled` attribute prevented Chosen from opening. G removes that stale attribute before compiling the clone. This is a test-fixture repair; production JavaScript is unchanged from F. The Linux run must establish whether the repair is sufficient.

The test retains its controlled API callbacks and real Angular `$timeout`/digest so the response-order cases stay repeatable. It reuses the existing `AdminUiTestBase` Selenium harness and Chosen helper. Several neighboring webapp tests also use Selenium `JavascriptExecutor` for browser-side setup or assertions. Do not remove the regression suite or weaken its assertions to make the job green.

## Claim and preflight

Before running, check live `WORKFLOW.md`/`START-HERE.md`, the current remote branch and PR head, this job, the previous receipt, and all existing claims. Claim this gate in a new execution claim. The phone-agent round-1 host had no usable Chrome; do not use it for Selenium unless a working system browser/driver is now verified. Serialize Gradle with the host lock, apply disk/inode guards, and record JDK, browser/driver, caches, worktrees, commands, logs, XML and artifacts. Use fresh detached worktrees for G and each baseline overlay. Record tree IDs, `git diff --check`, and exact test/production hashes.

No source or formatting repair is allowed to count as proof for G. If tidy or another required step changes tracked source, ordinary-push the repair to the same fork branch, verify the remote and PR heads, and post another add-only gate pinned to that new head before continuing. Never force the open PR head. Investigate executed failures before retrying. Environment refusals and skipped tests are not positive or fail-before proof.

## 1. Positive proof at G

Run each step and preserve fresh logs/XML before a later `cleanTest`:

```bash
node --check solr/webapp/web/js/angular/app.js
node --check solr/webapp/web/js/angular/controllers/alias-overview.js
./gradlew tidy
git diff --exit-code
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest --tests org.apache.solr.webapp.AdminUiCollectionScreensTest --tests org.apache.solr.webapp.AdminUiCollectionsScreenTest -Ptests.selenium=true -Ptests.seed=5282C0DE
./gradlew :solr:webapp:check -x test
./gradlew check -x test
```

Require every command to succeed, a clean tracked tree after tidy/checks, and fresh results. All 9 `AdminUiMenuRefreshTest` methods must execute with zero failures, errors or skips. Record actual counts in the two neighboring browser suites (six methods each in the prior source review) and explain any differences. Do not pass `-PdisableJsClient=true` or add task exclusions. The nine test purposes are listed in round 1's gate; retain all of them.

## 2. Fail-before for the added stale-response and alias behaviors

In a fresh O worktree, overlay only the test file from G and run:

```bash
git restore --source=82954b9df687d8239c4344b292b3afff5005f898 -- solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest -Ptests.selenium=true -Ptests.seed=5282C0DE
```

All production files must remain byte-identical to O. Expect nine executed methods and the five behavioral failures listed in round 1 (stale alias response, stale collection callback, stale system/core/route callback, cold alias overview, and alias overview displaying an inherited collection). The other four methods should pass. Record the first actual assertion per method; do not count fixture, startup, console, compile or browser failures as intended negative proof.

## 3. Fail-before for selection retention and the empty-input guard

In a separate fresh B worktree, overlay the same test file from G and run:

```bash
git restore --source=82954b9df687d8239c4344b292b3afff5005f898 -- solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest.testSelectionSurvivesRefreshAndClearsWhenRemoved --tests org.apache.solr.webapp.AdminUiMenuRefreshTest.testSelectorNavigationAndEmptyInput -Ptests.selenium=true -Ptests.seed=5282C0DE
```

Keep production code byte-identical to B. Expect exactly two executed methods and the two intended runtime failures: selection clears before the refreshed options arrive, and direct empty handler input dereferences `collection.type`. Record the actual assertion/stack. The second behavior proves the handler contract only; it does not establish a natural page-load trigger.

## Receipt and authorization boundary

GREEN requires the complete positive chain and both specified baseline overlays at their pinned heads, with all logs, XML, artifacts, exact hashes and final remote/PR head reads recorded. The execution host owns `receipts/PR-5282-menu-refresh-round-2.md` and any terminal report. Complete verification only for exact G.

This gate does not authorize a PR comment, PR-body change, JIRA write, approval, merge, or another force push. Public replies to the two new inline comments remain drafts unless Nick separately authorizes them or the standing clean-gate comment authorization applies after all required proof is green and live discussion is refreshed.
