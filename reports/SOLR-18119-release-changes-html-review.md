# Review: SOLR-18119 generate changes HTML only for releases

Reviewer: vm1 (main side), 2026-10-18. Verdict: SATISFIED.
Head reviewed and gated: 1029a6b0cf09dc75bdb380cd63fce1511bf440b2 on merge base c8ab71430f6e. Gate: gates/SOLR-18119-release-changes-html.md, receipt in receipts/SOLR-18119.md.

## What the change does

Local and CI documentation builds no longer generate Changes.html. The Gradle task and its changesHtml configuration are deleted; the converter script and its stylesheets move from gradle/documentation to dev-tools/scripts; the release flow generates the file instead (buildAndPushRelease.py calls write_changes_html before assembleRelease, and the release wizard runs the script directly); assembleRelease reads the generated directory and refuses to proceed when the files are absent; the ref-guide link check ignores the changes URL for local builds.

## Checks that carry the verdict

- Completeness of the removal. At the base, the changesHtml configuration has exactly one consumer (solr/distribution) and the changesToHtml task exactly two callers (the documentation site task and releaseWizard.yaml). The branch updates all of them. A whole-tree grep at the head finds no remaining references to the task, the configuration, or the deleted file outside the moved script's own stylesheet directory name, the wizard's log file name, and historical changelog text.
- Output equivalence. The base Gradle task's Changes.html and the head script's Changes.html for the repository's own CHANGELOG.md are byte-identical (same SHA-256, recorded in the receipt). The rendering code itself is unchanged; the refactor wraps it in render/write functions and adds argparse handling.
- The guard names its remedy. The assembleRelease doFirst check lists the missing files and prints the exact command that generates them, which is the same command the release wizard now runs.
- The new test covers the contract the distribution depends on: write_changes_html produces Changes.html with the rendered content plus the three named stylesheets, and raises FileNotFoundError for a missing changelog.

## Behavior changes a reader should know

- Missing python3 used to warn and skip inside the Gradle task, so a documentation build silently lacked Changes.html. Now nothing local generates the file at all, and a release assembly without it fails. For a release artifact that is the safer failure, and the failure message says how to fix it.
- A missing changelog argument now exits 2 (argparse error) instead of the old script's exit 1. No caller in the tree depends on the code; the release wizard checks the generated files, not the exit value.

## Limits

- The full documentation task (with every module's site Javadoc) was not run; the gate ran the documentation project's own wiring tasks and the distribution task graph, which is where this change's wiring lives. The ref-guide link check itself needs the npm toolchain and was not run; its change is a single ignore entry whose text was read.
- assembleRelease was resolved as a task graph, not executed; executing it assembles the full release. The guard's file check was verified by reading, and its required file set was verified against the script's actual output.
