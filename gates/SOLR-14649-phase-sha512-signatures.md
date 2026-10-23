# Gate job: SOLR-14649 (package signatures require SHA-512)

Status: DONE (GREEN at gated head `1767e9dce81453233bad30c49b5b19dfff8a017a`; receipt `receipts/SOLR-14649.md`). Run 1 was HANDED BACK at head 25589eb5e6. Owner approved the re-sign and the diff-scope widening to the two key files (Nick, 2026-10-23).

- Branch: `solr-14649-submit`. Head: `800e91188e0d9f975b9096c9413915ea18713b23` (fork tip, confirmed by ls-remote on 2026-10-23). If the tip has moved at claim time, stop and record the new tip here.
- Base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`. Prior gated head: none recorded.
- Gate ID: `SOLR-14649-phase-sha512-signatures`. Claim file: `claims/pool-<host>-gate-SOLR-14649-phase-sha512-signatures.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.util.TestCryptoKeys`, `org.apache.solr.filestore.TestDistribFileStore`, `org.apache.solr.pkg.TestPackages`, `org.apache.solr.handler.TestContainerPlugin`, `org.apache.solr.pkg.PackageStoreSchemaPluginsTest`, `org.apache.solr.cli.PackageToolTest`. Seed: `014649C0FFEE0001`.
- Diff scope: 11 files, 199 insertions and 165 deletions: CryptoKeys.java; TestCryptoKeys.java; TestDistribFileStore.java; TestPackages.java; TestContainerPlugin.java; PackageStoreSchemaPluginsTest.java; PackageToolTest.java; runtimecode/sig.txt; question-answer-repository/repository.json; package-manager-internals.adoc; SOLR-14649-package-signatures-sha512.yml.
- Why: This head switches both package and filestore verifier overloads from SHA1withRSA to SHA512withRSA, adds direct acceptance and rejection coverage, updates signature fixtures, and corrects the Ref Guide signing commands. This is the first proof request for the pushed implementation.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-14649-package-signatures-sha512.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: Restore CryptoKeys.java to Base while keeping TestCryptoKeys.java at Head; all four focused tests should fail, with SHA-512 acceptance false and SHA-1 rejection false for both byte-buffer and stream verification. Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.util.TestCryptoKeys --tests org.apache.solr.filestore.TestDistribFileStore --tests org.apache.solr.pkg.TestPackages --tests org.apache.solr.handler.TestContainerPlugin --tests org.apache.solr.pkg.PackageStoreSchemaPluginsTest --tests org.apache.solr.cli.PackageToolTest -Ptests.seed=014649C0FFEE0001`. Count all six classes from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| Failure outside repair scope, or any failure after the retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## Run record

- Preflight (step 5a, vm6): exit 3 at 2026-10-23T15:30Z on head `800e91188e0d9f975b9096c9413915ea18713b23`. Tidy changed one file, `solr/core/src/test/org/apache/solr/filestore/TestDistribFileStore.java`. Patch: `/workspace/gates/logs/vm6/preflight/SOLR-14649-phase-sha512-signatures/tidy.patch`.
- Step 1 repair (vm6, 2026-10-23T15:36Z): the tidy output applied as one commit, `25589eb5e6c894c3af06fdd12c904573309748bc` ("SOLR-14649: gate repair: tidy TestDistribFileStore"), pushed fast-forward to `solr-14649-submit` from the gate head. Tidied tree sha `ee45250115cbbe492e86313385a31f63482aff18`. Steps 2 to 5 run on this repaired head.
- Step 3 proof leg, read from the job: the focused class is `TestCryptoKeys` (four `@Test` methods: `testByteBufferAcceptsSha512`, `testByteBufferRejectsSha1`, `testInputStreamAcceptsSha512`, `testInputStreamRejectsSha1`). In a separate worktree at the repaired head, `solr/core/src/java/org/apache/solr/util/CryptoKeys.java` is restored to base `79b23df58daac14b92cc35f23b8b81f72098b4ba`, and the four tests are run with the job seed.
- Step 3 result (job `s14649-proof`, logs `/workspace/gates/logs/vm6/s14649-proof/`, seed `014649C0FFEE0001`, JUnit XML 2026-10-23T15:48:49Z): 4 tests, 4 failures, 0 errors. Failures, all as expected: `testByteBufferAcceptsSha512` (`ByteBuffer verification should accept SHA-512`, TestCryptoKeys.java:47), `testInputStreamAcceptsSha512` (`InputStream verification should accept SHA-512`, line 61), `testByteBufferRejectsSha1` (`ByteBuffer verification should reject SHA-1`, line 54), `testInputStreamRejectsSha1` (`InputStream verification should reject SHA-1`, line 68). Job rc=1 at the proof step, the expected outcome.
- Step 2 and steps 4 and 5 (job `s14649-head`, started 2026-10-23T15:53Z on the repaired head `25589eb5e6`): died at compile before any test ran. Gradle was not started by the guard; the rebase that replaced `pool/bin` mid-run is the recorded cause.
- Head run `s14649-head-r2`: refused at compile, rc=4, by the inode guard (NFS `/workspace` reports zero inode counters). Fixed on coordination in `d3614ec136`, which checks the local root when a mount reports zero counters.
- Head run `s14649-head-r3` (job `/workspace/gates/logs/vm6/s14649-head-r3/`, scripts from `/workspace/gates/snapshots/s14649-head-r3/bin`, head `25589eb5e6`): compile rc=0 (16:45:35Z to 16:54:11Z), head-clean rc=0 (16:54:11Z to 16:54:52Z). head-focused started 16:54:52Z, last log write 16:59:59Z, then the process died with no `rc=` line, no live process at 17:01Z, and no partial test XML. Cause not established from the logs. Possibly the background job does not survive the session sleeping between wakes; this is not verified. Check is at `s14649-head-r3`: the focused run and check step are not done. Gate not GREEN; no receipt written.
- Head run `s14649-head-r4` (job `/workspace/gates/logs/vm6/s14649-head-r4/`, same head, same seed, frozen scripts): head-clean rc=0 (17:21:44Z to 17:22:56Z); head-focused rc=1 (17:22:56Z to 17:25:33Z); check not run. Focused XML counts: 15 tests, 1 failure, 0 errors, 0 skipped. TestCryptoKeys 4 of 4, TestDistribFileStore 1 of 1, TestContainerPlugin 3 of 3, PackageStoreSchemaPluginsTest 1 of 1, TestPackages 4 of 4 all pass. Failure: `org.apache.solr.cli.PackageToolTest.testPackageTool`, `AssertionError: Non-zero status returned for: [--solr-url, ..., install, question-answer:1.0.0, ...] expected:<0> but was:<1>` (PackageToolTest.java:422, from :125). Server log: the signed PUT of `question-answer-request-handler-1.0.jar.tmp` returns status=400. The fixture `solr/core/src/test-files/solr/question-answer-repository/repository.json` signature was written for the old verifier; the test's own comment (lines 51 to 55) says the jar must be re-signed with `openssl dgst -sha512` for the install step. Re-signing changes a test-resource value, which the Gate repairs section puts out of scope. Not repaired, not relaunched. Decision for Nick: re-sign the fixture as a separate test-resource commit (needs approval), or hand this gate back for implementation.

## On green

1. Write or refresh `receipts/SOLR-14649.md` with a new top section for gate ID `SOLR-14649-phase-sha512-signatures`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-14649-phase-sha512-signatures.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
