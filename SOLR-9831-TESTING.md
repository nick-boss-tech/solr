# SOLR-9831 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
Shawn Heisey: the Admin UI logging tab shows an empty Core column and "ERROR false" in the Level column. A patch was
attached, but the audit note said "Admin UI angular, obsolete UI".

## What the code shows on main
The angular Admin UI is still the shipped UI (`solr/webapp/web/js/angular/`, `index.html`), so the note was wrong.
`solr/webapp/web/partials/logging.html` still renders `{{ event.level }} {{event.showTrace}}` in the level cell
(`showTrace` is the row's expand/collapse flag, set to `false` in `controllers/logging.js`), plus a stray closing
`</span>`. The trace row uses `colspan="4"` although the table has five columns (Time, Level, Core, Logger, Message).
The Core column half of the ticket is already handled: `Log4j2Watcher.toSolrDocument` copies the MDC and defaults `core`
to an empty string.

## Change
`logging.html`: drop `{{event.showTrace}}` and the extra `</span>` from the level cell, trace row `colspan="5"`.
`AdminUiLoggingScreenTest.testEventsViewerShowsWarnings` now also checks the level cell of the probe event is exactly
`WARN`.

## Guessed / verify first
- The XPath finds the row's `tbody` through the message cell class `message`; the first `tr` of the `tbody` is the event
  row, the second the trace row.
- The test is skipped (existing `Assume`) when the shared-JVM log watcher is blind.
- Fail-before: without the HTML change the cell text is `WARN false`.
