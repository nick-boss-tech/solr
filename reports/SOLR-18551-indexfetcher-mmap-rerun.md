# SOLR-18551 focused IndexFetcher mmap test rerun

Assignment: assignments/pool-solr-18551-mmap-test-rerun.md. Claim: claims/pool-solr-18551-mmap-test-rerun.md. Run by vm4, 2026-10-17, from 04:56Z to 05:28Z. Logs and JUnit XML are on vm4 under /workspace/gates/logs/vm4/ (18551-head.log, 18551-head-junit.xml, 18551-failbefore.log, 18551-failbefore-junit.xml).

## Result: head passes 3 of 3, fail-before fails at the openInputCalls assertion

Both legs ran as the assignment requires. The head leg is green. The fail-before leg fails on all three tests at the `openInputCalls` assertion, which is the expected failure. The proof is established.

## Tested commits

- Head: branch solr-18551-submit at 02c82ea6c8858495c4b1b2286167b77628d89485 ("SOLR-18551: avoid test helper name collision"). Confirmed with ls-remote before the run.
- Fail-before base: 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (SOLR-16396 | Migrate GET config sections query, searchComponent & queryResponseWriter to JAX-RS (#5125)). Only solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java was overlaid from the head commit. The file does not exist at the base commit, so the overlay adds it. Nothing else changed in the scratch checkout.

## Command and seed

`./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/18551-rerun-tests -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.IndexFetcherTest`

Run from the same command on both commits. Seed 18551C0FFEE18551, as in the assignment. The runner and environment were JDK 21.0.12.1 and Gradle 9.7.0 (wrapper).

## Head run

- Build: BUILD SUCCESSFUL in 18m 44s, rc=0. Test compilation passed.
- JUnit: tests=3, skipped=0, failures=0, errors=0.
- Tests passed: testSlowFileExistsReturnsFalseForMissingFile, testSlowFileExistsUsesLengthWithoutOpeningExistingFile, testSlowFileExistsTreatsEmptyFileAsPresent.

## Fail-before run

- Build: BUILD FAILED in 7m 01s, rc=1, at :solr:core:test. Test compilation passed.
- JUnit: tests=3, skipped=0, failures=3, errors=0.
- Failure for each test: `java.lang.AssertionError: expected:<0> but was:<1>`.
- Failing lines in the base-overlay test file: IndexFetcherTest.java:45 (testSlowFileExistsUsesLengthWithoutOpeningExistingFile), :59 (testSlowFileExistsTreatsEmptyFileAsPresent) and :68 (testSlowFileExistsReturnsFalseForMissingFile). All three are `assertEquals(0, dir.openInputCalls)`. The pre-fix implementation opens the file, so the counter reaches 1.

Classification: expected failure at the openInputCalls assertion. Not a compile error, not a skip, and not an unrelated failure.

## Deviations

None. The head leg and the fail-before leg each ran once, with no relaunch and no change to the command or seed.

## Source

No source was edited, committed, or pushed for the branch. Branch solr-18551-submit is unchanged at 02c82ea6c8858495c4b1b2286167b77628d89485. PR #5173 was not touched.

## Host and resources

- Root filesystem before the head run, 04:56Z: 3.8 GB available, 457,332 inodes free. At the end of the fail-before run, 05:28Z: 3.8 GB available, 457,331 inodes free.
