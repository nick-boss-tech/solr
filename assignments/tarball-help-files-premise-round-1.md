# Assignment: source-tarball help-file check premise run (round 1)

Status: OPEN
Tags: premise-run, records

## Premise

vm5's release-tarball check (`reports/vm5-release-tarball-dev-tools-check-round-1.md`) found that `./gradlew check` from an unpacked source tarball fails at `:allHelpFilesExit` with "Help file missing: dev-docs/gradle-help/workflow.txt": `gradle/help.gradle` reads help files from `dev-docs/`, and `/dev-docs` is export-ignored in `.gitattributes`, so the files never reach the tarball. If that holds generally, the source release cannot pass its own full check.

## Work

1. Verify the premise from the code: read `gradle/help.gradle` and `.gitattributes` at current main; enumerate which help files the check requires and which trees they live in; confirm each required file's export-ignore status with `git check-attr` or the archive listing method vm5 used.
2. Search Jira and the dev list for prior notice of this failure (terms: allHelpFilesExit, gradle-help, help file missing). Record hits or the absence.
3. If the premise holds, set out the fix shapes with costs: relocate the help files into a tree that ships; generate them at build time; or scope the check to checkouts. Recommend one, with the reason.
4. Deliverables: `reports/tarball-help-files-premise-round-1.md` with the verification, the search result, and the recommendation; and, only if the defect is confirmed and untracked, `drafts/jira-ticket-tarball-help-files-draft.md` (a ticket draft for the owner's go; filing itself is the owner's act).

This assignment writes no product code and files nothing.
