# PR 5287 configset upload hardening: Linux gate receipt, round 2

- Gate: `gates/pr-5287-configset-upload-hardening-round-2.md`. Executed by: phone agent (Linux VM), 2026-11-04T17:38:59Z to 18:14Z, plus the step 5 rerun noted below.
- Head (H3): `d0cf2471f37ae973ca06390ec195cf51f8fccbb3` on `pr-5287-configset-source-validation` (nick-boss-tech/solr). Base: `ad26fdf2c6bee8168eae043b94833f05643d1995`. Contributor parent (H0): `19f7c83812a4c81227601ff1abadca84beb05cb9`. Diff against H0 is 9 files: the round 1 seven, plus the tidy repair inside `UploadConfigSetFileAPITest`, the symlink test fix inside `LocalFSConfigSetUploadFromRepositoryTest`, and `gradle/testing/randomization/policies/solr-tests.policy`.
- Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), one build at a time under the host build lock. Seed for focused runs: `5287C0DE`.

## Verdict

GATE PASSED at H3. Every step the gate defines passed on this host; the only non-executed legs are the S3 suites, environment-blocked exactly as in round 1 (Docker testcontainer, no Docker on any pool host). The new behavior now has complete parent-code discrimination: with H0's production classes restored, the patched tests fail on their assertions, including both symlink tests, which in round 1 could not execute at all.

## Steps

- Step 0 (changelog YAML parses): PASS, rc 0.
- Step 1 (tidy and diff hygiene at H3): PASS. `:solr:core:tidy`, `:solr:solrj:tidy`, `:solr:test-framework:tidy`, `:solr:modules:s3-repository:tidy`, `:solr:modules:gcs-repository:tidy`, and root `tidy`, all rc 0; `git diff --check` rc 0; the worktree stayed clean throughout (no repair commit needed this round).
- Step 2 (Error Prone compile): PASS, rc 0 (`:solr:core:compileTestJava :solr:test-framework:compileTestJava :solr:modules:s3-repository:compileTestJava :solr:modules:gcs-repository:compileTestJava -Pvalidation.errorprone=true`).
- Step 3 (fail-before, seed `5287C0DE`, H3 tests with H0's three production files restored): DISCRIMINATES.
  - Core LocalFS, 12 tests, 4 assertion failures, 0 setup errors: `testV1RejectsBodyWhenRepositoryIsProvided` (expected 400, got 200), `testUploadFromRepositoryRejectsSymbolicLinkDirectories` (expected 400, got 200; in round 1 this test errored in setup and never reached the assertion), `testUploadDotPrefixedDirectoriesFromRepository` (RemoteSolrException from the server), `testBodyAndRepositoryAreMutuallyExclusive` (expected RemoteSolrException, none thrown).
  - Core UploadConfigSetAPITest, 12 tests, 1 failure: `testRepositoryUploadRejectsBodyWhenAvailableIsZero` throws NullPointerException on the parent code instead of the clean rejection the branch implements.
  - Core UploadConfigSetFileAPITest, 5 tests, 1 failure: `testSingleFileUploadRejectsSymbolicLink` expects a SolrException and none is thrown (in round 1 this test also errored in setup).
  - GCS, 9 tests, 1 failure: `testUploadDotPrefixedDirectoriesFromRepository` (RemoteSolrException).
  - S3, 10 tests, all 10 skipped: environment-blocked (see below).
  - Variance note: the body and repository mutual-exclusivity discrimination differs between rounds by provider (round 1: GCS instance failed, LocalFS instance passed; round 2: LocalFS instance failed, GCS instance passed). The parent code decides whether a body is present with `InputStream.available()`, which is timing-dependent, so exactly one instance tripping per run is the expected parent-code behavior. The branch replaces that check with a stream read and both instances pass deterministically at the head.
- Step 4 (positive runs at H3, seed `5287C0DE`, fresh XML, rc 0): core LocalFS 12 of 12, UploadConfigSetAPITest 12 of 12, UploadConfigSetFileAPITest 5 of 5, GCS 9 of 9; zero failures, zero errors, zero skips in every suite that executed. S3 10 of 10 skipped, environment-blocked.
- Step 5 (root `check -x test`): PASS on rerun. Layout disclosure: this step ran in a normal clone at H3 (`~/workspace/solr-clones/5287-check2`), because Antora's local content source cannot read a git worktree. The first run failed only at `:solr:webapp:verifyUiWasm`: the Gradle download of solr-ui-wasm.zip through this host's egress relay was corrupted (checksum bca9df80... against the expected 65449430... in gradle.properties). The same URL fetched with curl hashes to exactly the expected value; that verified copy was seeded into the clone's build directory and the rerun is green: rc 0, 555 tasks, about 5 minutes. The mismatch is a host download artifact, not branch content: the branch does not touch the webapp or the UI.
- Step 6 (final remote read): the GitHub API reports the fork branch tip as `d0cf2471f37ae973ca06390ec195cf51f8fccbb3`, identical to the tested head.

## Environment blocker, unchanged from round 1

- `S3ConfigSetUploadFromRepositoryTest` uses `S3MockContainerRule`, a Docker testcontainer that skips the suite through JUnit `Assume` when Docker is unavailable. No pool host has Docker. The S3 execution of the shared dot-directory and exclusivity test code remains unproven here; the same shared code passes under the LocalFS provider (real cluster) and the GCS provider (in-JVM fake). Options unchanged: a Docker-capable host runs the S3 legs, or upstream CI covers them once the follow-up branch is attached to PR 5287.

## Evidence on the gate host

- Logs and fresh JUnit XML for every step: `~/workspace/tools/gate-5287-r2/` on the phone agent host (`master.log`, `step0.log` through `step6-*.txt`, `step*-xml-*` directories, `step5-check.log` and `step5-check-rerun.log`).
