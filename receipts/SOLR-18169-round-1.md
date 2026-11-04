# Receipt: SOLR-18169 Linux gate, round 1

Status: GATE GREEN at final head `dd90eafa345fb71e514a3f28dd0ffc6a8c9e8168`.

Branch `solr-18169-slim-no-examples` on the fork. Base `498a09aea97aac86c2cc7ac114113b946d249942`. Implementation commit `50117e60af3358c5f2e39f2bde9852817115c23d` (Windows host, claim `claims/solr-18169.md`), plus the gate's tidy commit `dd90eafa345f` ("SOLR-18169: tidy formatting in TestSolrCLIRunExample", one line join in the new test helper), pushed by the gating host under the standing tidy rule. Gate run by the phone agent on this Linux host after the helper dispatch was refused; logs at `~/workspace/tools/gate-18169.log` and `~/workspace/tools/gate-18169-followup.log` on that host.

Diff at the final head (4 files): `solr/packaging/build.gradle` (example content moved from the slim distribution contents to the full contents), `solr/core/src/java/org/apache/solr/cli/RunExampleTool.java` (the default missing example directory now reports that examples are not included in the slim distribution and points at the full distribution; the explicit `--example-dir` error is unchanged), `solr/core/src/test/org/apache/solr/cli/TestSolrCLIRunExample.java` (three new tests), `changelog/unreleased/solr-18169-slim-no-examples.yml`.

## Legs

1. Packaging installs at the implementation head: `:solr:packaging:installSlimDist :solr:packaging:installFullDist`, BUILD SUCCESSFUL. The installed slim tree (`solr-11.0.0-SNAPSHOT-slim`) has no `example/` directory; its top level is CHANGELOG.md, LICENSE.txt, NOTICE.txt, README.txt, bin, docker, lib, licenses, server. The installed full tree has `example/` containing README.md, exampledocs, and films.
2. Focused tests at the implementation head: `:solr:core:test --tests "org.apache.solr.cli.TestSolrCLIRunExample"`, fresh JUnit XML: 9 tests, 0 failures, 0 errors, 0 skipped.
3. Same test suite at the final head (after the tidy commit): 9 tests, 0 failures, 0 errors, 0 skipped, fresh XML.
4. Proof without the fix: base `498a09aea97a` plus only the new test file. 9 tests, exactly 1 failure: `testMissingDefaultExampleDirectoryExplainsSlimDistribution` fails because the base code answers with the old "Value of --example-dir option is invalid!" message instead of the slim message. The other 8 pass, including the two tests that pin preserved behavior (explicit `--example-dir` error, unsupported example with the directory present).
5. `:solr:core:check -x test -Pvalidation.errorprone=true` at the implementation head: failed at `:solr:core:compileTestJava` on two Error Prone `DirectInvocationOnMock` warnings in `solr/core/src/test/org/apache/solr/handler/admin/api/CoreReplicationAPITest.java` (lines 213 and 217), a file this branch does not touch. The same compile at the base fails identically with the same two warnings, so the failure is pre-existing at the base and not caused by this branch. Every other check task that ran (ecjLint main and test, forbiddenApis, compileJava with Error Prone) passed.
6. Tidy: `:solr:core:tidy` reformatted one line in the branch's test file; committed and pushed as `dd90eafa345f`; a tidy re-run at the final head leaves the tree clean.

## Limits

- No live `bin/solr start -e techproducts` run against the slim tree: this host runs as root and Solr refuses to start; the error path is covered by the unit tests in leg 4 instead.
- The Docker slim image was not built; it is assembled from the slim distribution, which leg 1 verifies directly.
- No PR opened and no Jira or GitHub comment posted; those need the owner's go.
