# Gate job: SOLR-12651 changelog title correction, title stated inline

- Branch: solr-12651-submit. Head to build on: `de3f3604f0ee12935080c8f3d418b6bd1be3013e` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing). Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
Status: OPEN
- Capability tags: gate.
- Intended host: a Linux host (vm1 or vm2). Claim first under `claims/` per WORKFLOW.md.
- Job type: one branch commit that changes only the `title:` of one changelog fragment, then the hardening gate at the new head, a plain push on green, and the receipt refresh.
- Everything else on this branch is done: the history scrub landed (`gates/SOLR-12651-history-scrub.md`, DONE), the full gate is green at `de3f3604f0ee` (log `logs/vm2/queue-g12651-scrub-gate-20261013T070906Z.out`), and the base run is recorded in `receipts/SOLR-12651.md`. This title job is the only step the SolrCloud first-rung round left open on the branch.

## Why this job exists

The SolrCloud HOLD first-rung settlement (`reports/solrcloud-hold-first-rung-round-1.md`, SOLR-12651 section) corrected the changelog title and left its landing as that round's OWNER ITEM, because the review lane may not push. This job discharges that item under the owner's decision routing (2026-10-14): the title text is settled and recorded, so the remaining work is a pool gate job, not an owner decision.

The defect, from the settlement: the title on the branch says the cleanup applies "unless the restore runs asynchronously." The code does not support that. Property upload and replica setup run inside the same try block for async restores, so their failures are cleaned up in both modes. Only a shard copy that fails in an async restore is not cleaned up, because that failure is recorded in the async status (`CollectionHandlingUtils.java`, `waitForAsyncCallsToComplete`, lines 794 to 816 at the head). The corrected title narrows the carve-out to that case. It supersedes the replacement in `reports/solrcloud-round-1-p2.md` finding 9, which carried the overbroad wording.

## The edit

File: `changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml` on solr-12651-submit at the head named above. Only the title text changes; the fragment's structure, its `type: fixed` line, authors and links stay as they are.

Current title (verbatim, at `de3f3604f0ee`, where the fragment carries it in block style under `title: >`):

```
A RESTORE that fails before its shards become active now deletes the new collection instead of leaving a partly restored collection behind, unless the restore runs asynchronously.
```

Replacement title (verbatim, as settled in `reports/solrcloud-hold-first-rung-round-1.md`):

```
A RESTORE that fails before its shards become active now deletes the new collection instead of leaving a partly restored collection behind, except when a shard copy fails in an async restore.
```

- The replacement contains no `": "`, so it parses as a plain scalar or in the fragment's block style; the parse step below checks the parsed title value, not the file's layout.
- One commit on top of the confirmed tip, Nick Shanin identity, no trailer. Suggested subject: `SOLR-12651: narrow the changelog title async carve-out`. The diff of the commit must be the title text in that one fragment and nothing else; stop and record here if any other file differs.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-12651-submit` must equal `de3f3604f0ee12935080c8f3d418b6bd1be3013e`. If it has moved, stop and record the new tip in this file; do not build on an unrecorded head.
2. Make the edit above in a worktree at that head (copy `gradle.properties` into the worktree). Commit it as specified. `git show --stat` on the commit must list only the changelog fragment.
3. Changelog YAML parse of the edited fragment with a real YAML parser (the repository validator `dev-tools/scripts/validate-changelog-yaml.py`, or the gate step 0 parser). Pass means the file parses and its parsed `title` equals the replacement text above, byte for byte.
4. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop, record what tidy changed in this file, and do not proceed on an uncommitted tree. A title-only YAML edit is expected to leave tidy clean.
5. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`, through the host's Gradle route for gates (on vm1, `~/workspace/tools/solr-gradle.sh`; never `./gradlew` there). rc must be 0.
6. Focused tests, counted from fresh JUnit XML only, module `:solr:core`: the receipt's focused set, `TestLocalFSCloudBackupRestore`. Confirm the class's exact package by grep before composing any run command; compose the run in a separate step from the grep. Expected count at the new head, matching the receipt at the current tip: 2 of 2, skipped 0, failures 0, errors 0.
7. Module check: `:solr:core:check -x test`, rc 0.
8. No pre-fix proof re-run in this job. The change is a changelog title only and cannot affect any test outcome; the branch's proof stands as recorded (base-code run at `404f13b501c9`, the same tree as the current head: 2 tests, 1 failing by content, in `receipts/SOLR-12651.md`). Do not re-run a proof leg for this job.
9. Never two Gradle builds at once on a host: steps 4 to 7 run serially, behind the test-queue flock like every other gate run.

## On green

1. Push the commit plainly (fast-forward, no force) to `fork` `solr-12651-submit`. Pushes to the owner's own branches carry standing authorization (2026-10-14). Verify the new tip by `git ls-remote` after the push.
2. Refresh `receipts/SOLR-12651.md`: add a section for this job with the new head, the one-fragment diff scope, the YAML parse result (title byte-equal to the replacement), the tidy guard result, the compile and module check results, and the step 6 counts with the run date, all from the fresh JUnit XML. Note that the first-rung round's OWNER ITEM is discharged by this job.
3. Mark this job DONE with the new head and the gate outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file. A gate that executed tests and failed is not relaunched until the main side has investigated (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
