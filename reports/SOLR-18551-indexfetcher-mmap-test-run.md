# SOLR-18551 focused IndexFetcher mmap test run

Assignment: assignments/pool-solr-18551-mmap-test-run.md. Claim: claims/pool-solr-18551-mmap-test-run.md. Run by vm2, 2026-10-17, from 03:50Z to 04:00Z. Log: logs/vm2/18551-head.log, summary logs/vm2/18551-result.txt (both on vm2 under /workspace/gates/logs/vm2/).

## Result: STOPPED at the head run

The head does not compile its test sources, so no test ran and the fail-before leg was not run. The assignment says to record the output and stop when the head run fails.

## Tested commit

- Branch solr-18551-submit, live at the start of the run: ca4c660fb0bf28eec40d7906e2065fd2052c0260 (ls-remote matched before the run).
- Base for the fail-before leg, not reached: 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9.

## Command and seed

`./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/18551-tests -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.IndexFetcherTest`

The assignment gives `-Dtests.seed` only. The runner also passes `-Ptests.seed` with the same value, because the Solr build reads the seed from the project property. Both use the seed the assignment names.

## Head run

- `:solr:core:compileTestJava` failed, rc=1, in 6m57s. No test class ran and no JUnit XML was written.
- Compiler output, from logs/vm2/18551-head.log:

  ```
  IndexFetcherTest.java:86: error: slowFileExists(Directory,String) in IndexFetcherTest cannot override slowFileExists(Directory,String) in LuceneTestCase
    private static boolean slowFileExists(Directory dir, String fileName) throws Exception {
    attempting to assign weaker access privileges; was public
  1 error
  ```

- The helper is called from lines 44, 58, 67 and 79 of the same file. The method is declared `private static` but overrides a `public` method of `LuceneTestCase`. Java does not allow a weaker access level on an override, so the file cannot compile at this head.

## Fail-before leg

Not run. The assignment requires the head run to pass 3 of 3 before the fail-before leg, so the leg is not reached.

## Deviations from the assignment

- My runner did not stop when the head compile failed. It went on to create the base worktree. I stopped it after the head rc=1 line was written and before any Gradle run on the base. The base worktree was removed afterward, and the scratch directories are gone. No Gradle run happened at the base.

## Source

No source was edited, committed, or pushed for this run. Branch solr-18551-submit is unchanged at ca4c660fb0bf28eec40d7906e2065fd2052c0260.

## What is needed to proceed

A new commit on the branch that makes the helper's access match `LuceneTestCase`, or a rename of the helper so it no longer overrides. That change belongs to the branch owner. The assignment does not allow vm2 to edit source, so this run is not relaunched. Once a new head is posted, the assignment can be re-run as a new assignment.
