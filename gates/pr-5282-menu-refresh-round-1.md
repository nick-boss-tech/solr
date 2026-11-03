# Gate: PR 5282 legacy Admin UI menu refresh, round 1

Status: OPEN. Immutable once posted. Implemented, Proof Pending.

- Required tags: gate, review, records; legacy Admin UI Selenium/browser capability. Intended host: any capable Linux gate host with JDK 21, the pinned Gradle 9.7.0 distribution/build dependencies, Node/npm access for the generated JS client, and working Chrome/Chromium plus ChromeDriver/Selenium Manager. This is `solr/webapp`, not the separate Kotlin `ui/` build.
- Fork/branch: `nick-boss-tech/solr:solr-16398-admin-ui-showcollection`.
- Final pushed head F: `200ff5e5c6c214212f72f37485487f3092e05541`.
- Previous PR head O: `6ee3c9b6370e6e1e3b4da4afcf968a0806cc747f`.
- Original unguarded/early-clear base B: `7855ab485d50b44af2fe9b52e77bf172d8ea663e`.
- Public PR: https://github.com/apache/solr/pull/5282 . Its head moved from O to F by an ordinary fork push; verify the live head before execution. No force push or upstream write is authorized.
- Implementation claim: `claims/pr-5282-menu-refresh-windows-round-1.md`.
- Implementation report: `reports/pr-5282-menu-refresh-implementation-round-1.md`.
- Execution receipt: `receipts/PR-5282-menu-refresh-round-1.md`, owned by the execution host. Add execution claims and terminal reports; leave this job and the Windows claim unchanged.

## Purpose and proof boundaries

F keeps the collection model and lists until a successful refresh, rejects callbacks from obsolete refresh generations (including a route change without a subsequent reset), and makes alias overview follow the refreshed alias for its requested route. It retains the empty-input guard from O. The changelog now describes those actual behaviors and uses Nick's name only.

The nine new tests reuse `AdminUiTestBase`, instantiate the actual registered controllers with queued API callbacks, clone the production selector attributes, exercise the bundled Angular/Chosen widget, and compile the actual alias overview template. The controller uses its real `$timeout`/digest; tests release network callbacks in a controlled order and wait behind that callback using another real timeout. The fixture changes response ordering, not the production implementation. Normal selector navigation goes through Chosen; the empty-handler case additionally calls the handler directly.

Do not claim these tests reproduce a crash automatically on every page load. Programmatic model deletion alone is not sufficient evidence for that event sequence. The fail-before legs below prove specific selection, stale-response and alias-consumer behaviors. The direct empty-input negative proves the handler contract, not a naturally occurring page-load trigger.

## Claim, preflight and repair rules

Read live WORKFLOW.md/START-HERE.md and existing claim/receipt state. Serialize behind the host build lock, apply disk/inode guards, and record host/JDK/browser/driver, caches, worktrees, runner, log and XML paths in a new execution claim. Use fresh detached worktrees for positive F and each baseline overlay. Verify the full fork ref and PR head equal F, record tree IDs and `git diff --check`, and inspect the four-file O-to-F diff before execution.

Run detached tidy preflight at F. If tidy modifies tracked content, make a formatting repair commit with Nick Shanin's identity, ordinary-push to the same fork branch, separately verify the remote head, and post an add-only superseding gate pinned to that new head before accepting positive proof. Any source/test repair has the same requirement. Never force an open PR head. Never silently accept a run at F as proof for a repaired head.

Investigate and record executed failures before retries. A pre-test environment refusal may be relaunched once under WORKFLOW.md; skipped tests, compilation failure, missing browser, missing generated client, startup failure or timeout are not an intended negative result. Do not weaken assertions, broaden severe-console allowlists, disable tests, or add task exclusions to make the job green. If a documented environment repair such as unsetting `NODE_ENV` is needed, record the original failure, the reason and exact successful command. Follow the job's repair/new-head process for patch defects.

## 1. Exact-head positive proof

At F:

```bash
node --check solr/webapp/web/js/angular/app.js
node --check solr/webapp/web/js/angular/controllers/alias-overview.js
./gradlew tidy
git diff --exit-code
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest --tests org.apache.solr.webapp.AdminUiCollectionScreensTest --tests org.apache.solr.webapp.AdminUiCollectionsScreenTest -Ptests.selenium=true -Ptests.seed=5282C0DE
./gradlew :solr:webapp:check -x test
./gradlew check -x test
```

Require every command to succeed, clean tracked state after tidy and checks, and fresh XML/logs. No task exclusions beyond the specified `-x test` are authorized. Do not pass `-PdisableJsClient=true`: the existing Selenium base requires the generated client. Confirm all 9 `AdminUiMenuRefreshTest` methods execute, with zero failures/errors/skips. Neighboring suites currently contain 6 and 6 test methods; report actual counts and explain differences rather than silently accepting missing methods. Preserve output and fresh XML before a subsequent cleanTest.

The nine methods cover:

1. `testSelectionSurvivesRefreshAndClearsWhenRemoved`: retain the old model through system/list/alias stages, publish a matching refreshed object, clear a removed selection, no unintended navigation.
2. `testFailedRefreshPreservesSelection`: list and alias failures retain the old selection/options and surface current errors.
3. `testRootPageRefreshClearsSelectionAfterLoading`: root-page success clears the selection only when loading completes.
4. `testSelectorNavigationAndEmptyInput`: Chosen navigates correctly for a collection and alias; undefined/null handler input is safe.
5. `testOlderAliasResponseCannotReplaceNewerSelection`: reversed alias completion cannot replace the newer model/list.
6. `testOlderCollectionResponseDoesNotStartAliasRequest`: an obsolete collection reply cannot launch another alias request.
7. `testObsoleteSystemCoreAndRouteChangeCallbacksAreIgnored`: old mode/error/core callbacks and callbacks invalidated by route change cannot publish state.
8. `testAliasOverviewLoadsAndTracksRefreshedDetails`: cold alias scope obtains its route's alias and later target/property changes appear in the actual template.
9. `testAliasOverviewRejectsPreviousSelectionAndClearsWhenRemoved`: an inherited collection is not shown as the requested alias, and alias removal clears the view.

## 2. Fail-before for the newly repaired stale-response and alias defects

In a fresh detached O worktree, overlay only the unchanged new Java test from F:

```bash
git restore --source=200ff5e5c6c214212f72f37485487f3092e05541 -- solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest -Ptests.selenium=true -Ptests.seed=5282C0DE
```

All production files must remain byte-identical to O. Record `git diff --name-only O`, the test hash matching F and hashes of both production JavaScript files. Expect 9 executed methods with exactly these five behavioral assertion failures; the other four are expected to pass:

- `testOlderAliasResponseCannotReplaceNewerSelection`: old A replaces newer B after reverse completion.
- `testOlderCollectionResponseDoesNotStartAliasRequest`: old collection response starts an additional alias request.
- `testObsoleteSystemCoreAndRouteChangeCallbacksAreIgnored`: stale system mode changes cloud state and/or stale core error is surfaced. Record the actual first failed assertion; later subassertions in this method are pins unless independently discriminated.
- `testAliasOverviewLoadsAndTracksRefreshedDetails`: the cold alias view remains at the initial empty snapshot after the menu loads.
- `testAliasOverviewRejectsPreviousSelectionAndClearsWhenRemoved`: the inherited collection is immediately exposed as the requested alias.

Save fresh logs/XML and failure artifacts. An unexpected pass or different failure must be investigated; do not count fixture/console/startup failures as the intended negative proof. Additional assertions are coverage pins unless their discrimination is separately demonstrated.

## 3. Fail-before for the original selection-retention and guard fixes

In a separate fresh detached B worktree, overlay the same unchanged Java test from F. Keep all production code byte-identical to B:

```bash
git restore --source=200ff5e5c6c214212f72f37485487f3092e05541 -- solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java
./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests org.apache.solr.webapp.AdminUiMenuRefreshTest.testSelectionSurvivesRefreshAndClearsWhenRemoved --tests org.apache.solr.webapp.AdminUiMenuRefreshTest.testSelectorNavigationAndEmptyInput -Ptests.selenium=true -Ptests.seed=5282C0DE
```

Expect exactly 2 executed methods and 2 failures: the selected model/widget is cleared after the system response while collection/alias requests are pending; and direct empty handler input throws when accessing `collection.type`. The latter may be reported as a Selenium JavaScript exception rather than an assertion failure and is the specific intended runtime error. It is not a browser startup or compilation error. Record the actual failing assertion/stack and do not describe it as an every-page-load reproduction.

If tidy or source repairs supersede F, replace all F overlays with the superseding exact tested head and preserve tests unchanged within each matched pair. The old production bases O and B stay pinned unless a separately documented compatibility repair is required; a missing API cannot be accepted as negative proof.

## Receipt and closeout

GREEN requires all positive commands and suites at the final verified head, clean formatting/root checks, and both specified baseline overlays with intended runtime failures. The receipt must include full heads/tree IDs, exact commands/seed, browser/driver and JDK versions, actual method counts, return codes, fresh log/XML paths and hashes, production/test overlay hashes, each expected failure and its first failing assertion/stack, repairs/environment conditions, and final local/remote/PR head checks.

Finish the receipt with corrected PR description wording tied to the actual results. Explain generation ownership, atomic successful publication, alias synchronization, preserved selection on current request failure, and the proof limits above. Do not carry forward the unsupported deterministic-page-load explanation. Windows prepared a draft in the implementation report; only replace proof-pending wording with results actually obtained.

This job does not authorize a PR body edit, public comment, JIRA write, approval or merge. Hand back the receipt and result. The existing PR body is still the old snapshot and must not be mistaken for exact-head F verification. A posted job is a handoff, not an execution claim or passing gate.
