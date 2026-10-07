# SOLR-16322 hypothetical reproduction

**Status: not run.** This branch is a source-backed hypothesis; no Gradle or
test tools were run.

The failure summary registers afterTest on each Gradle Test task, including
the generated test_N tasks used by beast. The old summary built the copyable
command from project.testOptionsForReproduceLine, which contains the root
tests.seed. When a beast subtask derives a different seed, the printed command
therefore does not replay the failure.

## Reproduction to try

1. In a clean Solr checkout, run the failing test with beast duplication and
   enough duplicates to reproduce a distinct subtask seed. For example:

       ./gradlew :solr:core:beast -Ptests.dups=16 --tests "org.apache.solr.uninverting.TestUninvertingReader.testSortedSetIntegerManyValues"

2. Compare the seed printed for the failing test_N task with the
   -Ptests.seed=... value in the final failure summary's Reproduce with line.
   The old code is expected to print the root seed; this change should print the
   failing task's tests.seed.
3. Copy the generated command and confirm that running the named test with that
   seed reproduces the same failure.

The source-level regression target is gradle/testing/failed-tests-at-end.gradle:
genFailInfo must read task.systemProperties['tests.seed'], replace any root
seed option, and put the failed task's seed in the single-test reproduction
command. Both the ordinary test task and a generated beast task should retain
their existing reproduction options when no task-specific seed is present.
