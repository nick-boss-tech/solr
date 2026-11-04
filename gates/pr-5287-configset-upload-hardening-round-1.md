# Gate job: PR 5287 configset repository upload hardening, round 1

Status: OPEN. No Linux gate claim is recorded yet.

- Branch: `pr-5287-configset-source-validation`. Head: `04e570d3426496aa8634ac03e556d45bc159af63` (fork tip confirmed through the GitHub API on 2026-11-04). Stop if the fork tip differs and reconcile before running.
- Prior gated head: none. Contributor parent: `19f7c83812a4c81227601ff1abadca84beb05cb9`. PR base: `ad26fdf2c6bee8168eae043b94833f05643d1995`. Merge-base with `upstream/main`: `1fdb455a524a7b8d334d5baea04e395ed4d2d7f8`.
- Intended host: any capable Linux gate host with JDK 21 and the branch Gradle wrapper/dependencies, serialized under its build lock.
- Capability tags: gate, implementation, review, records.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.configsets.UploadConfigSetAPITest`, `org.apache.solr.handler.configsets.UploadConfigSetFileAPITest`, `org.apache.solr.cloud.LocalFSConfigSetUploadFromRepositoryTest`, `org.apache.solr.s3.S3ConfigSetUploadFromRepositoryTest`, and `org.apache.solr.gcs.GCSConfigSetUploadFromRepositoryTest`. Seed: `5287C0DE`.
- Implementation claim: `claims/pr-5287-configset-upload-hardening-round-1-windows.md` (recovery record; see its posting note).
- Implementation report: `reports/pr-5287-configset-upload-hardening-round-1.md`.
- Receipt reserved: `receipts/PR-5287-configset-upload-hardening-round-1.md`, written by the executing gate host.
- Diff scope against `19f7c83812a4c81227601ff1abadca84beb05cb9`: 7 files, 162 insertions and 9 deletions. `solr/core/src/java/org/apache/solr/handler/admin/ConfigSetsHandler.java`; `solr/core/src/java/org/apache/solr/handler/configsets/ConfigSetAPIBase.java`; `solr/core/src/java/org/apache/solr/handler/configsets/UploadConfigSet.java`; `solr/core/src/test/org/apache/solr/cloud/LocalFSConfigSetUploadFromRepositoryTest.java`; `solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetAPITest.java`; `solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetFileAPITest.java`; `solr/test-framework/src/java/org/apache/solr/cloud/api/collections/AbstractConfigSetUploadFromRepositoryTest.java`.
- Why: the patch rejects request bodies when repository input is selected in both API versions, rejects symlink entries and validates canonical allowed paths in the new local repository importer, and imports dot-prefixed directories. It adds focused regression coverage. The symlink check is included because the traversal path is introduced by this PR; it is scoped to repository-backed configset upload.

Links: Ticket or parent: SOLR-18618, PR 5287. Category: existing PR follow-up. Siblings: none known. Supersedes: none.

## Before claiming

Refresh `coordination-ui`, the fork branch and live PR state. Check for an existing live gate claim or receipt. Confirm the remote branch still equals the pinned head, preserve its ancestry, run the detached tidy preflight, and record host, clean checkout, JDK, runner, log and XML locations in a new Linux gate claim. A posted job is not a claim or a pass. Do not run alongside another gate on the same host.

## What to run

Use the host's standard gate runner and a clean task-specific worktree. Retain each command's log and fresh JUnit XML. Do not run the build tool outside the gate runner.

- Step 0: parse `changelog/unreleased/SOLR-18618-configset-upload-from-repository.yml` with the repository's changelog YAML parser. Require valid YAML; retain the parser output.
- Step 1: run `./gradlew tidy` and `git diff --check`. Require no tidy changes and a clean tree. If tidy changes a file, follow the repair route below; this head cannot receive a green receipt.
- Step 2: Error Prone compile all changed Java source and test source sets: `./gradlew :solr:core:compileJava :solr:core:compileTestJava :solr:test-framework:compileJava :solr:modules:s3-repository:compileTestJava :solr:modules:gcs-repository:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, fail-before proof: in a disposable checkout based on H1, restore only the three changed production files (`ConfigSetsHandler.java`, `ConfigSetAPIBase.java`, and `UploadConfigSet.java`) from contributor parent H0. Keep H1's test files and changelog. Run the focused suites listed in Step 4 at seed `5287C0DE`. Require runtime assertion failures for all of these behaviors: `UploadConfigSetAPITest.testRepositoryUploadRejectsBodyWhenAvailableIsZero` accepts the mixed-source request; `LocalFSConfigSetUploadFromRepositoryTest.testV1RejectsBodyWhenRepositoryIsProvided` accepts the mixed-source request; `LocalFSConfigSetUploadFromRepositoryTest.testUploadFromRepositoryRejectsSymbolicLinkDirectories` follows a local directory symlink; `UploadConfigSetFileAPITest.testSingleFileUploadRejectsSymbolicLink` accepts a local file symlink; and `testUploadDotPrefixedDirectoriesFromRepository` fails to import the expected files in each LocalFS, S3 and GCS concrete test class. Record each intended assertion failure from fresh XML/logs. Compile, setup, missing-test, skip or unrelated assertion failures do not count. Restore the unmodified H1 checkout before Step 4.
- Step 4, positive head proof: on a clean H1 checkout, run each command separately with fresh XML:

  `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.configsets.UploadConfigSetAPITest --tests org.apache.solr.handler.configsets.UploadConfigSetFileAPITest --tests org.apache.solr.cloud.LocalFSConfigSetUploadFromRepositoryTest -Ptests.seed=5287C0DE --rerun-tasks`

  `./gradlew :solr:modules:s3-repository:cleanTest :solr:modules:s3-repository:test --tests org.apache.solr.s3.S3ConfigSetUploadFromRepositoryTest -Ptests.seed=5287C0DE --rerun-tasks`

  `./gradlew :solr:modules:gcs-repository:cleanTest :solr:modules:gcs-repository:test --tests org.apache.solr.gcs.GCSConfigSetUploadFromRepositoryTest -Ptests.seed=5287C0DE --rerun-tasks`

  Require all selected tests to execute with zero failures, errors or skips. Read the counts from the fresh XML, including the new methods listed in Step 3.
- Step 5: run `./gradlew check -x test`. Require success and report any execution condition or failure.
- Step 6: confirm the worktree is clean and separately read the fork ref. Require the remote SHA to equal H1, the tested full SHA.

If any step cannot be verified, record it and stop. Do not change the acceptance criteria or edit the contributor branch.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes H1 | default: stop green proof for H1 | Make only a scoped repair commit on this fork branch, read back its SHA, and post a superseding exact-head gate before rerunning proof | gate host |
| Public PR comment | decided: not part of the gate | After a green receipt, the Windows owner will refresh the live PR and discussion, check for an equivalent comment, then offer the pinned diff once. Keep public wording focused on fixes and verification | Nick Shanin |

## On green

1. Write `receipts/PR-5287-configset-upload-hardening-round-1.md` with the exact head, host, JDK, seed, commands, exit codes, start/end times, logs, fresh XML test counts, all fail-before assertion outcomes, tidy, Error Prone and check results, and final remote SHA.
2. Do not post a public comment from the gate host. Report the receipt and any execution condition; the owner handles the separate live PR refresh and comment.
3. Mark the gate claim DONE in the same push as the receipt. Completion is for the receipt's exact tested head only.

## Checklist

- [ ] H1 confirmed from fork remote; ancestry and diff scope confirmed
- [ ] detached tidy preflight and changelog YAML parse
- [ ] tidy clean and `git diff --check`
- [ ] Error Prone compile
- [ ] fail-before proof for both body paths, both symlink paths, and dot directories in all three providers
- [ ] focused positive suites at H1 with counts from fresh XML
- [ ] `check -x test`
- [ ] final remote branch read matches tested SHA; clean worktree
- [ ] receipt written and gate claim completed

## Deliverable and limits

- A Linux receipt at the exact tested head. If a repair changes H1, use a new ordinary fork commit and a superseding gate before claiming proof.
- No force push, no push to `apache/solr` or Houston's fork, no PR body edit, no JIRA, and no public comment as part of this job. Commit board records as Nick Shanin with no trailers. Use no em dashes.
