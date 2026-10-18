# SOLR-18119 PR draft (for Nick to approve; no PR exists yet)

Status: DRAFT. Not opened. Opening the PR needs Nick's approval.
Head: solr-18119-remove-changes2html at 1029a6b0cf09 (the same commit is also pushed as solr-18119-release-python; the PR opens from the remove-changes2html name). Gate green and review satisfied: receipts/SOLR-18119.md, reports/SOLR-18119-release-changes-html-review.md.
Ticket: SOLR-18119, status Open. Direction on the ticket (Jan Høydahl, 2026-10-18): remove changes2html from the build, make it release-only, stay Python.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

## What happens today

**The Gradle build generates Changes.html through a Python script on every documentation build, and quietly skips it when python3 is missing.**

- The documentation build runs [changes-to-html.gradle](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/gradle/documentation/changes-to-html.gradle) to turn CHANGELOG.md into Changes.html. (Link at the merge base; this change deletes the file.)
- When python3 is not found, the task logs a warning and does nothing, which is the confusing failure this ticket describes.
- The HTML form is only needed when a release is published to the website; the binary distribution has shipped the markdown changelog since 10.0.

## What this change does

**Changes.html is generated only during a release, by the release tooling, using the same Python script moved out of the Gradle build.**

- The Gradle side is deleted: the changes-to-html build file, its apply line in the root build, the documentation site task's dependency on it, and the changesHtml configuration the distribution consumed.
- [changes2html.py](https://github.com/apache/solr/blob/1029a6b0cf09dc75bdb380cd63fce1511bf440b2/dev-tools/scripts/changes2html.py) and its three stylesheets move to dev-tools/scripts. The script gains an --output-dir mode and an importable write function; without the flag it prints to stdout as before.
- [buildAndPushRelease.py](https://github.com/apache/solr/blob/1029a6b0cf09dc75bdb380cd63fce1511bf440b2/dev-tools/scripts/buildAndPushRelease.py) generates Changes.html into the documentation site tree before assembling the release, and [releaseWizard.yaml](https://github.com/apache/solr/blob/1029a6b0cf09dc75bdb380cd63fce1511bf440b2/dev-tools/scripts/releaseWizard.yaml) runs the script directly instead of the Gradle task.
- [assembleRelease](https://github.com/apache/solr/blob/1029a6b0cf09dc75bdb380cd63fce1511bf440b2/solr/distribution/build.gradle) reads the generated directory and fails with the exact command to run when the files are missing, instead of shipping a release without them.
- The reference guide's site link check ignores the changes URL for local builds, since the file now exists only in a release build.
- A unit test for the script covers the write path and the missing-file failure.
- With this change, no Gradle task invokes python3; the conversion was the only place the build did. Python remains in dev-tools/scripts, which is release tooling run by hand.

## Proof

**The release path produces the same file the build used to produce.**

- The script's Changes.html for this repository's CHANGELOG.md is byte-identical to the deleted Gradle task's output (SHA-256 b3539c06a3111c4c6026ee863fe0f7a2e0dfd292a5e7da1f99bb0178559634ea from both runs).
- The Gradle changesToHtml task runs at the merge base and does not exist at this head.
- The script's unit tests pass, 2 of 2.
- Tidy is clean, and the build configuration, the documentation tasks this change rewires, and the distribution release task graph all pass at this head.

## Limits

**Two deliberate behavior changes, both in the release path.**

- A release assembly without the generated files now fails, where the old task warned and skipped when python3 was missing. The failure message names the missing files and the command that generates them.
- A missing changelog file makes the script exit 2 (an argparse error) instead of 1. No caller in the tree depends on the old code.
- A locally built documentation site still links to a Changes page that only a release build generates.

## Changelog

No changelog entry: this changes build and release tooling only. On this ticket's earlier branch an entry for a release-tooling change was removed as not changelog worthy.

---

*AI-generated description, human-reviewed.*
