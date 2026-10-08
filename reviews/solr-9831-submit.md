# solr-9831-submit

- Branch: origin/solr-9831-submit
- Head: f269a70e84f0 (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +46/-2. `solr/webapp/web/partials/logging.html` (level cell drops `{{event.showTrace}}` and a stray `</span>`; trace row `colspan` 4 to 5), `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.java` (level-cell assertion added to `testEventsViewerShowsWarnings`), changelog `changelog/unreleased/SOLR-9831-admin-ui-logging-level-column.yml` (`type: fixed`, author Nick Shanin), and `SOLR-9831-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no Selenium test, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-9831-TESTING.md` is labeled "hypothetical reproduction (not run)" and is treated as unverified.

## Findings (ranked)

1. **MEDIUM, verified. The new assertion can be skipped.** The level-cell check sits in `testEventsViewerShowsWarnings` after `Assume.assumeTrue(watcherSawProbe)`. On a JVM where the shared log watcher is blind, the test is skipped before the new check runs. A skipped test gives no failure, so the fail-before proof cannot come from this test on such a JVM. The author's note says the same. Not patched: the skip exists for a documented shared-JVM reason, so whether the level check should run without it is an owner call (see A).

2. **LOW, verified. Change-describing comment.** The repo's `AGENTS.md` says changes should not carry code comments that communicate the change. `AdminUiLoggingScreenTest.java` adds `// SOLR-9831: the level cell showed the row's internal "showTrace" flag ...`. Wording-only. Not patched.

3. **Verified (checked, no issue). The fail-before expectation holds by reading.** `controllers/logging.js:52` sets `event.showTrace = false` on each event, so the old cell `{{ event.level }} {{event.showTrace}}` renders `WARN false`, which fails `assertEquals("WARN", ...)`. The new template renders `WARN`.

4. **Verified (checked, no issue). Template.** The table has five headers (`logging.html:26-30`: Time, Level, Core, Logger, Message), so `colspan="5"` on the trace row is right. The level cell's `<span>`/`<a>` markup is balanced after removing the stray `</span>`. The `ng-show="event.showTrace && event.trace"` trace row is unchanged.

5. **Verified (checked, no issue). Test mechanics.** The XPath finds the event row as `tr[1]` of the `tbody` whose message cell contains the probe text. The probe string has no quote characters, so the XPath literal is valid. The probe is logged with `log.warn`, so `WARN` is the expected level. `assertEquals` comes from the JUnit base (`AdminUiTestBase` extends `SolrCloudTestCase`), like the other assertions in the package.

## Owner calls (not decided here)

- **A. Skip behaviour (finding 1).** Options: (a) keep the level check behind the existing `Assume`, and accept that it is skipped on a blind-watcher JVM; (b) move the level check into a test that does not depend on the shared-JVM watcher, for example a unit test on the template, or on a page that renders a known event; (c) make the `Assume` skip only the event-count part. Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before proof, no Spotless, no Selenium run (needs Chrome).
- Whether `findElement` finds the row immediately after `waitUntil` returns: the wait checks page source, and the row is rendered in the same `ng-repeat` as the probe message, but timing was not run.
- `SOLR-9831-TESTING.md` is treated as unverified. Left in place.
