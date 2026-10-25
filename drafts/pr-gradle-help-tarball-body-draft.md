# PR body draft: build: Ship dev-docs/gradle-help in the source tarball

Status: DRAFT, not opened. Branch `solr-gradle-help-ship-in-tarball` (head `d2923cb65b`, base `07fdebdda5`). Proof receipt: `receipts/gradle-help-tarball-fix-round-1.md`.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

## What happens today

`/dev-docs export-ignore` in `.gitattributes` came in with #4551 on 2026-07-04 (`dd5dc65d45`). Since then, `git archive` (the source tarball, built by `solr/distribution/source-release.gradle`) has no `dev-docs/` at all. `gradle/help.gradle` lists 11 files under `dev-docs/gradle-help/` and makes `check` depend on `allHelpFilesExit`, which fails when any of them is missing. From an archive, `./gradlew check` therefore fails at `:allHelpFilesExit` ("Help file missing: dev-docs/gradle-help/workflow.txt"). The releases 10.0.0 and 9.10.1 predate the rule and their archives include the files.

## What this change does

Un-exports `dev-docs/gradle-help/` in `.gitattributes`, so the 11 help files, and the `README.md` beside them, return to the source archive. The rest of `dev-docs/` stays excluded.

The source-release RAT scan has no git and scans everything, so it would now reach `dev-docs/gradle-help/README.md`. This change adds `exclude "dev-docs"` to that branch of `gradle/validation/rat-sources.gradle`, matching the git-checkout branch.

## Proof

Run on head `d2923cb65b`, from fresh `git archive` trees:
- Base `07fdebdda5`: `:allHelpFilesExit` fails with "Help file missing". Head: it passes, with 12 help files in the archive.
- Head: `rat` passes (online run, BUILD SUCCESSFUL).
- Mutation (head without the fallback exclude): `rat` fails on `/dev-docs/gradle-help/README.md  Unknown license`. So the second hunk is needed.

Full logs: receipt `receipts/gradle-help-tarball-fix-round-1.md`.

## The choice

Should the help text ship in the source tarball, or should `allHelpFilesExit` only run in a git checkout?

Context: the help files are plain text, and the check exists to catch a missing file. Skipping the check in an archive would hide the failure instead of fixing it, and `:help*` tasks would still fail there. The files are plain text, and RAT excludes `**/*.txt`, so shipping them creates no license-header obligation; `README.md` is covered by the fallback exclude above.

Questions, in order:
1. Does `#4551`'s intent (no ASF headers on files that do not need them) still hold? Yes: the help files do not get headers, and they are not in the license scan.
2. Should the archive build pass `check`? If yes, ship the help text (this change). If the maintainers want the archive to skip the check, the alternative is a condition in `gradle/help.gradle`.

Recommendation: ship the help text, as this change does. Question: is that the right call?

## Limits

- Not run here: full `check` from the archive, and `precommit` on this branch.
- The 52-file scan of `dev-docs/` for license text (`checkDevDocsLicenseHeaders` on the guard PR) still applies: the new files have no headers.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
