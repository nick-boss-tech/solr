# Admin UI round 1, part U3: SOLR-9831 (branch `solr-9831-submit`, audit only)

Nothing has run. There was no build, no Gradle, no test, no Selenium or Chrome run, and no compile. No `gh` write calls, commits, pushes, posts or file edits. The one network call was a read-only `git ls-remote origin refs/heads/solr-9831-submit`.

## Verdict

- **Premise on current main: holds on a reading.** `logging.html` still renders the flag in the level cell, still has the stray closing span, and the stack trace row still spans four of five columns. `controllers/logging.js` sets the flag per row. The rendered text "WARN false" is predicted by the template, not observed by any run.
- **Core half: holds on a reading.** `Log4j2Watcher.toSolrDocument` copies the MDC and defaults core to an empty string. The branch correctly leaves the Core column alone. Whether a live UI shows the core name for every core-scoped event cannot be settled by reading. The test does not check Core.
- **Head verification:** the live tip is `f269a70e84f0` (`ls-remote`; it matches the claim). The diff against `cabedd1d968` is four files, as the record says. The content matches the description of `logging.html` and the test. The receipt's file list omits the fourth file (see the receipt disagreements).
- **Main drift:** the local `upstream/main` in this worktree resolves to `3f5d4c5bf8ac`, which descends from the assignment's `8e62c2686882`. The premise paths are byte-identical across `cabedd1d968`, `8e62c268` and `3f5d4c5b` (empty diff), so every reading below holds on all three.

## Item 1: the premise (main `3f5d4c5bf8ac`; head `f269a70e84f0`)

- **Level cell with the flag:** `solr/webapp/web/partials/logging.html` line 36 on main is `<td class="level span"><a><span>{{ event.level }} {{event.showTrace}}</span></span></a></td>`. At the head, line 36 is `<span>{{ event.level }}</span>`. Changed.
- **Stray closing span:** the same main line 36, second `</span>`. Removed at the head. The HTML parser ignores an end tag with no open span to close, so the stray tag has no effect on the rendered text. Removing it is markup hygiene that the test cannot detect.
- **Table columns:** the `thead` `th` elements at `logging.html` lines 26 to 30 (Time, Level, Core, Logger, Message) make five.
- **Trace row:** main line 42 `<td colspan="4"><pre>{{event.trace}}</pre></td>`; head line 42 `colspan="5"`. Changed.
- **Flag set per row:** `solr/webapp/web/js/angular/controllers/logging.js` line 52, `event.showTrace = false;`, inside the per-event loop, so every ten-second refresh (line 77) resets it. Line 98, `event.showTrace =! event.showTrace;`, in `toggleRow` (line 97). The row click is wired at `logging.html` line 34, `ng-click="toggleRow(event)"`. So the cell reads "WARN false" when collapsed and "WARN true" when expanded.
- **Not changed by the branch:** the `tfoot` empty-state row at `logging.html` line 47, `<td colspan="4">No Events available</td>`, still spans four of five columns at the head.
- **Core column,** line 37, `{{ event.core }}`: unchanged on main and at the head.

## Item 2: the Core half

- `Log4j2Watcher.java` on main: `toSolrDocument` at line 297. The MDC copy is at lines 311 to 315 (`event.getContextMap()` into the document). The default is at lines 317 and 318: `if (!doc.containsKey("core")) doc.setField("core", "");`. Blame on lines 317 and 318 is commit `624d128b5e74` (SOLR-7887, the log4j2 upgrade, 2018-03-25). The branch does not touch this file.
- MDC key: `MDCLoggingContext.java` lines 81 to 87 (`setCoreName` puts `CORE_NAME_PROP` into the MDC, and removes it when null) and lines 142 to 145 (set from the `CoreDescriptor`). `CORE_NAME_PROP` is `"core"` (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ZkStateReader.java` line 88).
- The shipped watcher: `LogWatcher.java` lines 174 to 179 pick Log4j2 when SLF4J's factory is Log4j's. `JulWatcher.toSolrDocument` (`jul/JulWatcher.java` lines 151 to 164) has no MDC copy and no core default, but it is not the shipped path.
- The 2016 Jira comment (`research/jira-context/SOLR-9831.json`, comment 15726348) traced the empty core to the log4j 1 `LoggingEvent.getMDC` call, which is not on main.
- **Verdict:** the branch correctly leaves Core alone. The PR body may say Core is unchanged and why. The title and the changelog must not claim a Core fix.

## Item 3: the Assume skip

- Head test lines 45 to 65 (base lines 45 to 65 are identical): `ensureCloudCluster` (line 47); the probe is logged (line 51); up to `WAIT_TIMEOUT` the admin API is polled for the probe (lines 56 to 62); `Assume.assumeTrue("This node's log watcher does not receive events (shared-JVM log4j state); skipping", watcherSawProbe)` at lines 63 to 65. The Assume is on base, not new. The new assertion (head lines 76 to 84) runs after it and inherits the skip.
- A blind watcher means JUnit records the method as SKIPPED. No cell assertion, no UI open, and no console-error check runs. **A skipped run is zero evidence about the cell.** The comment at lines 53 to 55 says blindness happens when other UI classes ran first, so a suite run can skip often while the build still shows green. A base run that skips is also no fail-before evidence.
- **A passing (not skipped) run shows:** the watcher returned the probe through `/admin/info/logging`; the Angular viewer rendered the probe (the `waitUntil` at lines 68 to 74 would time out otherwise); the level cell of the first `tr` of the `tbody` containing the probe text reads exactly "WARN" after trim; and there are no severe console errors.
- **A passing run does NOT show:** anything about the trace row or its colspan (the probe has no throwable, the test never clicks a row, and the colspan is not asserted); ERROR or other levels (WARN only, which is the same template line, so a reading); the Core column; the stray tag (`getText` ignores markup, and the parser ignores the tag); the expanded text "WARN true" (no click); or anything at all when the test was skipped.

## Item 4: the changelog title

- The title (`changelog/unreleased/SOLR-9831-admin-ui-logging-level-column.yml` line 2): "Admin UI logging screen no longer prints an internal "false" next to each log level, and the stack trace row spans all columns."
- Clause 2 matches: colspan 4 to 5 on a five-column table. "All columns" is exact.
- Clause 1 is exact only for collapsed rows. Before the change, the cell printed the flag value: "false" when collapsed and "true" when expanded (`controllers/logging.js` lines 97 and 98). The title names only "false". Optional precision: "an internal flag value next to each log level".
- The title does not claim the stray tag (it has no visible effect, so that is fine) and does not claim a Core fix (correct).
- Format: a plain YAML scalar with no colon-space, which parses. Type `fixed`. Author Nick Shanin (the ICLA name). The link is SOLR-9831. This matches the sibling fragments.

## Item 5: the premise-run spec (main-side work owed; not run)

1. Re-check the live head immediately before running: `origin/solr-9831-submit` must be `f269a70e84f0`.
2. Compile the `solr/webapp` test sources at the head. The new `assertEquals` (head line 84) has no static import. It resolves through the inherited superclass chain (`AdminUiTestBase` extends `SolrCloudTestCase`, which extends `SolrTestCaseJ4`, which extends `SolrTestCase`, which extends `LuceneTestCase`). Sibling tests on main use `assertEquals` the same way (`AdminUiCollectionsScreenTest.java` line 149), but the compile is the proof. Also run spotless on the changed Java file; formatting was not checked here.
3. **Base run:** `cabedd1d968` with only the head test file overlaid (`src/test` only). Run the single method `org.apache.solr.webapp.AdminUiLoggingScreenTest#testEventsViewerShowsWarnings` with Chrome, not inside a shared class run. Expected: EXECUTED (not skipped), and it FAILS at the new `assertEquals` (head line 84) with expected "WARN" and actual "WARN false". That message is the fail-before evidence. A failure on another line, or a skip, does not count.
4. **Head run:** `f269a70e84f0`, the same single method. Expected: EXECUTED and PASSES, with the cell reading "WARN" and no severe console errors.
5. Record per run: the head SHA, executed or skipped, pass or fail, and the assertion message.

**Outcomes:**
- Base fails at line 84 with "WARN false", and the head passes: the premise is confirmed by a run for the level cell only (not the colspan, and not Core).
- Base skipped: NOT_PROVEN. Rerun with a clean watcher, alone.
- Base passes with "WARN": the test does not detect the bug. Reread the premise and the XPath.
- Head fails at line 84 with "WARN false" or other text: the change is incomplete, or the reading of the markup is wrong.
- Head fails with `NoSuchElement` on the XPath, or a `waitUntil` timeout: the class-name or visibility assumptions are wrong. No cell verdict.
- Compile failure: stop and fix before any run.
- The colspan has no run path in this test. A check needs a probe with a throwable and a row click, which is new test scope (an owner decision).

**Run order:** smallest first in any batch (9831, then 9818, then 9759), per the assignment. All three share `AdminUiTestBase` and Chrome.

## Receipt disagreements (exact wording)

1. **`receipts/SOLR-9831.md` line 7:** "Diff against merge-base `cabedd1d968059215188f4e7563fb303241899ed` (4 files): `solr/webapp/web/partials/logging.html` only (...), the Selenium test `AdminUiLoggingScreenTest` (...), and `changelog/unreleased/SOLR-9831-admin-ui-logging-level-column.yml`." The count says 4, the list names 3, and "only" is contradicted by the list that follows. The fourth file is `SOLR-9831-TESTING.md` at the repository root (added, 26 lines; `git diff --stat` confirms), which the receipt does not name.
2. **`assignments/admin-ui-round-1.md`, the SOLR-9831 entry:** "changes only `solr/webapp/web/partials/logging.html`". The literal diff touches four files; three of them are the test, the changelog and the note. The statement is accurate only if "changes" means product code.
3. **`assignments/admin-ui-round-1.md`** pins `upstream/main` as `8e62c2686882`; the local `upstream/main` is `3f5d4c5bf8ac`. A stale pin. The premise files are unchanged.
4. **Tip commit:** the receipt says the tip "adds only that TESTING.md note". This agrees (`git show --stat f269a70e84f0`).
5. **Receipt line 8, the skip wording:** agrees with this report.
6. **Commit `e4aa23b3e4d` message body,** not in the receipt: "Hypothetical, unrun regression test; see SOLR-9831-TESTING.md." This conflicts with the assignment's rule that public PR text carries no internal process vocabulary, if the commits reach the PR.

## Owner decisions

1. **Keep `SOLR-9831-TESTING.md` out of the outbound commit set?** It says "hypothetical", "not run" and "guessed", and `pr-formula.md` does not list it. Recommendation: exclude it.
2. **Rewrite commit `e4aa23b3e4d`'s body before the PR?** A branch rewrite is an owner call. Recommendation: yes, before any PR.
3. **The `tfoot` "No Events available" colspan (`logging.html` line 47):** fix it in this branch (one token, the same file, the same defect class, untested), or name it in Limits. The owner picks.
4. **Trace-row colspan coverage:** accept it as a reading-only Limit, or extend the test (a probe with a throwable, a row click, and an assertion of colspan 5). This is an owner call, since it is new test scope.
5. **The level check is skipped whenever the watcher is blind:** accept the existing Assume and state it in Limits (recommended), or restructure the check.
6. **Title precision:** an optional wording change, from item 4.
7. **Core half:** the PR body states that Core is unchanged and why (recommended). No Core claim in the title or the changelog. Whether a run should check Core is an owner call.
8. **Do not present the premise as verified** until the premise run passes at both ends.

## Not checked

- Any compile, spotless, test, Selenium or Chrome run. "WARN false" and the Angular boolean rendering are readings of the template, not observations.
- The `LuceneTestCase` declaration is not in this checkout (it is an external dependency). The `assertEquals` resolution rests on sibling-test usage, not on a compile.
- Live JIRA was not called. The local snapshot says Status "Open", Updated 2019-06-08, and it may be stale.
- GitHub PR state, CI and Actions were not checked (no `gh` calls).
- Whether the watcher is blind in practice, and whether every core-scoped event carries the core MDC key at log time, cannot be settled by reading.
- The fork tip was read by `ls-remote` only; no objects were fetched.
