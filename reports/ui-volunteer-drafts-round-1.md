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

## Item 2: SOLR-17909 staged ask (draft, not posted), vm6, 2026-10-24

Draft: `jira/jira-comment-SOLR-17909-draft.md`. Addressee [~malliaridis] (reporter; unassigned, no comments, last updated 2025-09-09). The index-query report marks SOLR-17909 READY TO VOLUNTEER. SOLR-17660 is excluded, SOLR-17911 and SOLR-17849 are left for other claims.

Checked on main (`498a09aea9`): `components/main/MainComponent.kt:40` (`childStack`), `components/main/integration/DefaultMainComponent.kt:55` (`childStack(...)`), `views/main/MainContent.kt:51` (subscribes). `views/components/` has no breadcrumb. The Jira acceptance criteria match the report.

Not verified: the Decompose pop-to-index call against the library version in this repo. The draft names it as the one open check.

## Item 3: SOLR-17676 probe result folded in (verified, not redone here)

Commit `5810dc9ac3` ("17676 draft: fold in the no-browser probe result (hard fail, not a skip)") already updated `jira/jira-comment-SOLR-17676-draft.md`. Checked: the draft now states the probe result, and the item is covered. No second edit made by this claim.

## Item 2 (second ticket): SOLR-17911 staged ask (draft, not posted), vm6, 2026-10-24

Draft: `jira/jira-comment-SOLR-17911-draft.md`. Addressee [~malliaridis] (reporter; unassigned, no comments, last updated 2025-09-10).

Correction to the index-query report. Its verdict for SOLR-17911 ("no viewer under ui/", READY TO VOLUNTEER, smallest slice a stateless viewer) is out of date. The component already exists on the fork branch `solr-17911-file-content-viewer`, at commit `8047b99ef0`, with two commits by Nick Shanin (`4c069a4c60`, `8047b99ef0`), a draft PR description at `pr-draft/SOLR-17911.md`, and a test file. Nothing is on main.

Checked on the branch by reading the code, not by running it:
- `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/FileContentViewer.kt`: plain-string input, numbered lines, gutter in `DisableSelection`, text in `SelectionContainer`, horizontal and vertical scroll.
- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/files/FileContentViewerTest.kt`: three `@Test` functions (line numbers, escapes, scrolling).
- The ticket's acceptance criteria are met on the branch, except the optional `editable` parameter, which the ticket allows.

Not re-run here. The branch's 3-of-3 result comes from the pool-ui-tickets assignment, which records a gate run on 8047b99ef09 by vm2. The draft says so.

The staged ask therefore asks for the sufficiency decision and offers Options A to C, recommending A (open the PR from the branch, screen wiring later).
