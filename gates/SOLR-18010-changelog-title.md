# Gate job: SOLR-18010 changelog title correction (E13, finding B5), title stated inline

- Branch: solr-18010-submit. Head to build on: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (fork tip, verified by `git ls-remote` on 2026-10-14 at filing). Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
Status: DONE (GATE GREEN at 571307400b69)
- Capability tags: gate.
- Job type: one branch commit that changes only the `title:` line of one changelog fragment, then the hardening gate at the new head, a plain push on green, and the records refresh.
- This job supersedes the scratchpad patch named in `pr-drafts/core-admin/SOLR-18010.md` (staging notes, E13) and in `reports/review-batch-round-2-slices-round-1.md`: `SOLR-18010-changelog-title.patch` was never placed under `gates/` or anywhere else on this branch, so there is nothing to apply. Do not recreate the patch file; the replacement title is stated verbatim below and the edit is made directly.

## Why this job exists

Finding B5 in `reports/four-new-prs-strong-review-round-1.md` (SOLR-18010 section, finding 6; held edit E13 in the same report): the changelog title's clause "or leave a partly written file" overclaims. Where the file system does not support an atomic move the code makes a plain move instead, and the file is not flushed to disk before it is replaced, so a crash or a power failure can still leave an empty, stale or partly written file. The draft's own Limits state the flush gap plainly. The title must not promise what the fallback path and the missing flush do not deliver.

The replacement text was never recorded verbatim in any record: the round 2 slice that was to prepare it (`assignments/pool-strong-review-round-2-unblocked-slices.md`, slice B item 2) has no deliverable report and produced no patch. The replacement below is therefore composed under the decision default: it removes only the overclaiming clause and keeps every other word of the current title. Both titles are quoted in full so the claimant applies exactly this text and no other.

## The edit

File: `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`, line 1, on solr-18010-submit at the head named above.

Current title (verbatim, at `2b5ad801c9e8`):

```
title: Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other or leave a partly written file. The file also no longer carries the internal version metadata.
```

Replacement title (verbatim; composed under the decision default, 2026-10-14, for the reason above):

```
title: Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other. The file also no longer carries the internal version metadata.
```

- Only the `title:` line changes. The `type:` line stays `fixed` (per `dev-docs/changelog.adoc`, `fixed` is for improvements that fix buggy behavior, with correcting data corruption as its named example; the fragment already carries it and this job does not revisit the type).
- The replacement is a plain YAML scalar with no `": "` inside it, so it parses unquoted, as the current title does.
- One commit on top of the confirmed tip, Nick Shanin identity, no trailer. Suggested subject: `SOLR-18010: correct the changelog title partly written file claim`. The diff of the commit must be the one title line and nothing else; stop and record here if any other file differs.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-18010-submit` must equal `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`. If it has moved, stop and record the new tip in this file; do not build on an unrecorded head.
2. Make the edit above in a worktree at that head (copy `gradle.properties` into the worktree). Commit it as specified. `git show --stat` on the commit must list only the changelog fragment, 1 insertion and 1 deletion.
3. Changelog YAML parse of the edited fragment with a real YAML parser (the repository validator `dev-tools/scripts/validate-changelog-yaml.py`, or the gate step 0 parser). Pass means the file parses and its `title` equals the replacement text above, byte for byte. An unquoted `": "` would be invalid YAML; the replacement contains none.
4. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop, record what tidy changed in this file, and do not proceed on an uncommitted tree. A title-only YAML edit is expected to leave tidy clean.
5. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`, through the host's Gradle wrapper for gates (on vm1, `~/workspace/tools/solr-gradle.sh`; never `./gradlew` there). rc must be 0.
6. Focused tests, counted from fresh JUnit XML only, seed `18010C0FFEE18010`, module `:solr:core`. The set is the one the receipt records at the current tip (`receipts/SOLR-18010.md`, permission re-gate and G2 sections). Confirm each class's exact package by grep before composing any run command; compose the run in a separate step from the grep. Expected counts at the new head, matching the current tip: `SecurityConfHandlerTest` 4 of 4, `V2SecurityAPIMappingTest` 5 of 5, `BasicAuthStandaloneTest` 1 of 1, `MultiAuthPluginTest` 6 of 6; skipped 0, failures 0, errors 0.
7. Module check: `:solr:core:check -x test`, rc 0.
8. No pre-fix proof leg in this job. The change is a changelog title only and cannot affect any test outcome; the branch's proof stands as recorded (premise failures at the merge-base `14c7aac0d15` and the permission fail-before at `9e3ccafe39`, both in `receipts/SOLR-18010.md`). Do not re-run a proof leg for this job.
9. Never two Gradle builds at once on a host: steps 4 to 7 run serially, behind the test-queue flock like every other gate run.

## On green

1. Push the commit plainly (fast-forward, no force) to `fork` `solr-18010-submit`. The branch backs Nick's own open draft PR; pushes to his own open PR branches carry standing authorization (2026-10-14). Verify the new tip by `git ls-remote` after the push.
2. Refresh `receipts/SOLR-18010.md`: add a section for this job with the new head, the one-line diff scope, the YAML parse result, the tidy guard result, the compile and module check results, and the step 6 counts with the seed and the run date, all from the fresh JUnit XML.
3. Refresh `pr-drafts/core-admin/SOLR-18010.md`: re-point every `2b5ad801c9` citation link to the new head (the draft's staging notes require this for any new commit on the branch), and clear the E13 staging note, replacing it with a line recording that the title correction landed under this job at the new head. Note in the receipt that the live PR body still cites the old head until its next sync; that sync is main-side follow-on, not part of this job.
4. Record in the receipt and in this file, plainly: PR #5118 stays a draft awaiting PR #5065's merge, per the owner ruling of 2026-10-13 recorded in `receipts/SOLR-18010.md`. This job does not flip the PR to ready, does not edit its live body, and does not change that wait in any way.
5. Mark this job DONE with the new head and the gate outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file. A gate that executed tests and failed is not relaunched until the main side has investigated (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
