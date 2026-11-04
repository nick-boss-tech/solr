# Receipt: PR 5287 configset upload hardening, Linux gate round 1

Status: GATE FAILED at the pinned head. Three independent blockers, itemized below; every other leg passed.

- Gate job: `gates/pr-5287-configset-upload-hardening-round-1.md`. Claim: `claims/pr-5287-configset-upload-hardening-round-1-phone-agent.md`.
- Host: phone agent (Linux VM). Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), serialized under the host build lock. Logs and fresh JUnit XML retained under `~/workspace/tools/gate-5287/` on that host.
- Tested head H1: `04e570d3426496aa8634ac03e556d45bc159af63` on `nick-boss-tech/solr:pr-5287-configset-source-validation`. Contributor parent H0: `19f7c83812a4c81227601ff1abadca84beb05cb9`. Seed: `5287C0DE`. Run: 2026-11-04T16:16:25Z to 16:47:20Z.
- Ancestry and diff scope confirmed at claim time: H1 sits directly on H0; diff is exactly the 7 files in the gate job, +162/-9. PR 5287 live state: open, head unchanged at H0.

## Step results

- Step 0, changelog YAML parse: PASS. `changelog/unreleased/SOLR-18618-configset-upload-from-repository.yml` parses as valid YAML (title, type added, author Houston Putman, SOLR-18618 link).
- Step 1, tidy and `git diff --check`: tidy rc 0 and diff --check rc 0, but tidy itself reformatted one branch file (see Blocker 1). The tree was therefore not clean at H1.
- Step 2, Error Prone compile (`:solr:core:compileJava`, `:solr:core:compileTestJava`, `:solr:test-framework:compileJava`, `:solr:modules:s3-repository:compileTestJava`, `:solr:modules:gcs-repository:compileTestJava` with `-Pvalidation.errorprone=true`): PASS, rc 0.
- Step 3, fail-before proof (H1 tests over H0 production files `ConfigSetsHandler.java`, `ConfigSetAPIBase.java`, `UploadConfigSet.java`), counts from fresh XML:
  - `UploadConfigSetAPITest` (core): 12 tests, 1 failure. `testRepositoryUploadRejectsBodyWhenAvailableIsZero` fails: the mixed-source request is not cleanly rejected at H0 (expected SolrException, got NullPointerException from the repository path). Discriminates.
  - `LocalFSConfigSetUploadFromRepositoryTest` (core): 12 tests, 3 failures. `testV1RejectsBodyWhenRepositoryIsProvided` fails as designed (expected 400, got 200; the mixed-source request is accepted). `testUploadDotPrefixedDirectoriesFromRepository` fails as designed (RemoteSolrException from the server). `testUploadFromRepositoryRejectsSymbolicLinkDirectories` fails in test setup, not on its assertion (see Blocker 2); does not count.
  - `UploadConfigSetFileAPITest` (core): 5 tests, 1 failure. `testSingleFileUploadRejectsSymbolicLink` fails in test setup (Blocker 2); does not count.
  - `GCSConfigSetUploadFromRepositoryTest`: 9 tests, 2 failures, both as designed: `testBodyAndRepositoryAreMutuallyExclusive` (no exception thrown at H0) and `testUploadDotPrefixedDirectoriesFromRepository` (RemoteSolrException).
  - `S3ConfigSetUploadFromRepositoryTest`: 10 tests, all skipped (Blocker 3). The S3 dot-directory fail-before is not demonstrated on this host.
- Step 4, positive head proof at H1, counts from fresh XML:
  - Core: `UploadConfigSetAPITest` 12/12 pass. `LocalFSConfigSetUploadFromRepositoryTest` 12 tests, 1 failure (the symlink test, setup, Blocker 2). `UploadConfigSetFileAPITest` 5 tests, 1 failure (the symlink test, setup, Blocker 2). Disclosure: the gate worktree carried Step 1's tidy reformat of `UploadConfigSetFileAPITest.java` during this step (formatting only, one call joined onto one line); Steps 3 and 5 ran pristine H1, and the same FileAPI failure occurs at pristine H1 in Step 3, so the reformat is not a factor in any verdict.
  - GCS module: 9/9 pass, rc 0.
  - S3 module: 10 tests, all skipped, rc 0 (Blocker 3).
- Step 5, `check -x test`: PASS, rc 0, run in a normal clone at H1 (`~/workspace/solr-clones/5287-check`, `.git` a directory), per this host's Antora worktree rule; 556 actionable tasks, BUILD SUCCESSFUL in 13m 53s.
- Step 6, final remote read: fork branch read back as H1 at the end of the run; the worktree status showed only the Step 1 tidy modification discussed in Blocker 1.

## Blockers

1. Tidy is not clean at H1. Root `tidy` joins the `api.uploadConfigSetFile(...)` call in the new `testSingleFileUploadRejectsSymbolicLink` onto one line (`UploadConfigSetFileAPITest.java`, 1 insertion, 2 deletions, formatting only). Under the gate job's decision table this head cannot receive a green receipt. The scoped repair is committed and pushed as the gate host's decision-table action: `8899ebd9d7d8a9985aef5d3c29b5f9b5189d5ee9` ("SOLR-18618: Apply tidy formatting to UploadConfigSetFileAPITest"), and a root tidy rerun at that head leaves the tree clean.
2. Both new symlink tests fail in their own setup, at both legs and at two seeds (5287C0DE and a 5287BEEF diagnostic rerun), identically. Production code is never reached.
   - `UploadConfigSetFileAPITest.testSingleFileUploadRejectsSymbolicLink` (line 140): `Files.createSymbolicLink` throws `java.security.AccessControlException: access denied ("java.nio.file.LinkPermission" "symbolic")`. The Solr test policy (`gradle/testing/randomization/policies/solr-tests.policy`) grants LinkPermission "hard" only, so a test running under the standard test security manager cannot create a symbolic link. These two tests are the only `createSymbolicLink` calls in the core test tree.
   - `LocalFSConfigSetUploadFromRepositoryTest.testUploadFromRepositoryRejectsSymbolicLinkDirectories` (line 128): `Files.createSymbolicLink(Path.of(dir).resolve("linked"), outsideDir)` throws `java.nio.file.ProviderMismatchException`. The link path comes from `Path.of(dir)` on the default filesystem while `outsideDir` comes from `createTempDir`, which returns a path on Lucene's wrapped test filesystem; the providers do not mix.
   Both are defects in the new test code, not in the production patch and not host-specific. Repair belongs with the implementation owner: build link and target on the same filesystem in the LocalFS test, and give the FileAPI test a link-creation route the test policy allows.
3. The S3 legs are environment-blocked. `S3MockContainerRule` skips the whole class when Docker is unavailable (its own Assume), and this host has no Docker, like every pool host. The S3 positive and fail-before legs can only be proven on a Docker-capable host or by upstream CI.

## Verdict and next step

GATE FAILED for H1 `04e570d3426496aa8634ac03e556d45bc159af63`. The production changes are supported by the legs that could run: body/repository exclusivity and dot-directory imports discriminate in the fail-before leg (core and GCS) and pass at the head, Error Prone is clean, and the root check passes. What is missing is proof for the two symlink behaviors (test repairs needed), tidy cleanliness (repaired at `8899ebd9d7`, which now heads the branch), and any S3 execution (Docker host needed). A superseding gate should be pinned at the head that carries the symlink test repairs on top of `8899ebd9d7`; no public comment is part of this receipt.
