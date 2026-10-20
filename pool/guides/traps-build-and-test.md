# Guide: build and test traps

Hard-won lessons from gated branches, written down so each one is paid for once. Every entry names the trap, the symptom, and the rule. Add an entry in the same push that pays for a new one; an entry without a date and a ticket is folklore and gets challenged.

## Gradle and tidy

- **Tidy prunes `gradle/libs.versions.toml`.** A tidy run can remove a version entry nothing references (seen: `oshai-logging`) as a side effect on an unrelated branch. Restore the file after validation runs unless the branch means to change it; a tree that differs from the gated head invalidates the gate. (2026-09-29)
- **Tidy drift is a commit, not a surprise.** Run tidy on the final tree after your last edit and before committing, so the commit you push is the tree the gate will see. A gate that stops because tidy reformats branch files gets the tidy commit and a re-run; that is the standing rule for our own prepared branches. (2026-10-07)
- **One Gradle build at a time per host.** Concurrent builds deadlock on the Gradle cache locks and can leave a stale daemon squatting on the lock. If a build fails with a lock timeout, clear the stale daemons and lock files and re-run serially. Chain multiple Gradle runs in one invocation instead of running them in parallel. (2026-10-01, 2026-10-03)

## Compile and lint

- **Error Prone is off locally and on in CI.** Local builds skip it outside CI, and CI treats its warnings as errors. Always compile with `-Pvalidation.errorprone=true` before calling a branch done; the classic catch is `==` on objects that CI rejects while the local build stays green. (2026-09-30, PR #4965)
- **Changing a field's visibility is a whole-tree change.** Before narrowing or widening a field, grep the whole tree for the field names, not just the owning module: cross-module subclasses exist (a core class extends a SolrJ streaming class and reads its fields). (2026-10-04, SOLR-17433)
- **No fully-qualified class names in new code.** They trip Error Prone; import instead. (2026-10-04)

## Tests

- **New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.** Reviewers flag the J4 base classes. When a test needs `assumeWorkingMockito()`, the rule is by base class: a plain `SolrTestCase` subclass must static-import it from `SolrTestCaseJ4`; a `SolrCloudTestCase` or `SolrTestCaseJ4` subclass inherits it and must NOT import it (the import fails lint as unused). (2026-09-29, 2026-10-03, 2026-10-04)
- **In a `SolrTestCase` subclass, JUnit 5 lifecycle annotations do not fire.** Use JUnit 4 `@BeforeClass` and `@Before`. A test that boots cores from the standard test configuration must call `SolrTestCaseJ4.newRandomConfig()` from a JUnit 4 `@BeforeClass`, or core init fails on a missing system property. Also, `assertThat` in such a class resolves to the inherited method; a static import of `MatcherAssert.assertThat` fails lint as unused, so drop the import and call the inherited one. (2026-10-03)
- **Test sources do not all live in the same layout.** Core tests sit under `solr/core/src/test/java/...`, but some modules (scripting is the known case) keep tests under `src/test/org/...` and test files under `src/test-files/`. Find the file before assuming the path. (2026-10)
- **Copy each run's results aside before the next run in the same module.** A later run overwrites the earlier run's JUnit XML and output files. A verdict that cites results must cite a copy that survives. (2026-10-19)

## Changelog

- **Changelog titles with a colon and a space must be quoted.** Apache's Validate Changelog workflow parses the fragments as plain YAML, and an unquoted `Title: with a colon` is invalid. Two PRs failed CI on exactly this while the entry read fine. Quote the title; it renders identically. (2026-10-05)

## Gate reading

- **A gate log holds several runs; count them separately.** The pre-fix proof step runs the new tests against the base, where failures are the PASS condition, before the head run. Never total failures across a whole log: separate the steps by their headers and quote the head run's numbers for the head, and the proof step's numbers for the proof. (2026-10-09)
- **A test claim stands on its log file and its fresh JUnit XML, not on console output.** Console and tool output have fabricated results before now; the files on disk are the evidence. (2026-10-07)
- **Get a test class's exact package before dispatching or running it by name.** Never construct the package from the class name or the feature area; grep the branch for the test file and copy the package and the full Gradle project path (`:solr:core`, not `core`) from the source. Four dispatches failed with "No tests found" on guessed packages and read as test failures until reproduced locally. (2026-10-05)

## Git mechanics

- **During a cherry-pick conflict, never resolve with a blanket `git add -A`.** A second conflicted file you did not look at gets marked resolved with its conflict markers committed. Before continuing, grep the whole tree for `<<<<<<<` and confirm zero, and diff the result against the intended content. (2026-10-04)
- **Never edit a script by a line number carried over from another tree.** The same line number is a different line in a branch whose file differs by a few lines. Anchor edits on the target line's content in the exact tree being edited, and syntax-check (`bash -n` for shell) before running. (2026-10-04)
