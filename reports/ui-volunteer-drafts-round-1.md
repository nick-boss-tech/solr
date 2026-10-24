# Report: ui-volunteer-drafts-round-1

## Item 1: SOLR-17861 staged ask (draft, not posted), vm6, 2026-10-24

Draft: `jira/jira-comment-SOLR-17861-draft.md`. Addressee [~malliaridis] (reporter; the ticket is unassigned, no comments, last updated 2025-08-12).

Checked on apache/solr main (`origin/main`, fork head `498a09aea9`):
- `gradle/validation/jar-checks.gradle` line 343: `updateLicenses`. Line 339 collects the `licenses` tasks from `subprojects` of the Solr build.
- `ui/settings.gradle.kts` line 56: `rootProject.name = "solr-ui"`. The root build does not include `ui`.
- `ui/build.gradle.kts`: Spotless only, no licenses task.
- `ui/desktopApp/build.gradle.kts` line 29: `compose.desktop.currentOs`.
- `ui/kotlin-js-store/wasm/yarn.lock` exists; the jar checks do not read it.

Staged-ask shape (comment-formula.md): evidence, one sufficiency question, options, recommendation, no review ask. Public text avoids process vocabulary; the options are labelled A, B and C instead.

Not run: no build or test. The draft says so.

Items 2 and 3 of this assignment stay open.
