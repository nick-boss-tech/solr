# Assignment: ui-volunteer-drafts-round-1 (staged asks for the new UI tickets)

- Status: DONE. Items 1 to 3 drafted by vm6 (see `reports/ui-volunteer-drafts-round-1.md`, item status section); SOLR-17660 excluded (live elsewhere). Original status line follows: OPEN. Intended host: any host with the `draft` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: comment drafts only. Nothing posts; every draft waits for the owner's go. The evidence base is the four assessment reports under `reports/` on this board and `UI-PLAYBOOK.md`.

## Items, in order

1. **SOLR-17861 staged ask (draft).** The ranked starter. Evidence: `reports/new-ui-platform-build-round-1.md` (updateLicenses aggregates `:solr` subprojects only; the UI is a separate build; the wasmJs dependencies sit outside its reach). Slice proposed: a `ui`-local check of LICENSE/NOTICE presence and SHA-1 checksums for the desktopApp JVM runtime classpath resolved for linux_x64, wired into `./gradlew -p ui check`. Files expected to be touched, phrased as an expectation: `ui/build.gradle.kts` and any new build logic under `ui/`. Addressee: [~malliaridis]. Shape: the staged ask in `comment-formula.md` (evidence, sufficiency question, ladder, recommendation, no review ask). Deliverable: `jira/jira-comment-SOLR-17861-draft.md` plus a COMMENTS.md entry.
2. **Remaining READY TO VOLUNTEER tickets.** Work the verdicts in `reports/new-ui-collections-configsets-round-1.md` and `reports/new-ui-index-query-components-round-1.md` in each report's ranking order: one staged-ask draft per ticket the report marks ready, each naming its smallest first slice and expected files from the report. One item per claim under the board's small-claims rule; name the ticket in the claim file.
3. **SOLR-17676 finish.** When the probe report for `assignments/ui-17676-nobrowser-probe-round-1.md` lands, fold its outcome into `jira/jira-comment-SOLR-17676-draft.md` (the ladder's unverified rung becomes a checked fact either way) and mark the draft ready in COMMENTS.md.

## Not in this round

- SOLR-17660 was claimed by vm2 on the old board on 2026-10-24; treat it as live and do not draft or claim it here.
- SOLR-17970 needs ASF infrastructure; no ask is drafted for it.
- SOLR-17845 already has a status-note draft (`jira/jira-comment-SOLR-17845-draft.md`); it is not re-drafted here.
