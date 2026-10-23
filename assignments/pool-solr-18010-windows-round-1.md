# Assignment: Windows run of PR #5118 (SOLR-18010)

Pool-open, any Windows host with a JDK 21 and Gradle through `gradlew.bat`. Claim-first as claims/pool-solr-18010-windows-round-1.md. Deliverable: reports/pool-solr-18010-windows-round-1.md.

The branch is stable: the owner does not intend to change it for now. This is a run, not a gate of record. Linux remains the gate of record.

## The head

Branch `solr-18010-submit` at `d742eb99dd28be761b5f60aea6fbfc832e1b7a50` (verify by ls-remote before running; if the tip has moved, run the live tip and say so in the report). Do not check out any other branch.

## The run

Run exactly this, from the repository root, with a fresh worktree or a clean checkout at the head:

```
.\gradlew.bat --no-daemon :solr:core:test --tests org.apache.solr.handler.admin.SecurityConfHandlerTest --tests org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest --tests org.apache.solr.handler.admin.SecurityConfHandlerReaderTest --tests org.apache.solr.security.BasicAuthStandaloneTest
```

Do not run tidy. Do not run Error Prone. Do not edit any file. Keep the JUnit XML under `solr\core\build\test-results\test\`.

## What to record

1. Environment: Windows version, architecture, JDK version (`java -version`), Gradle version, wall-clock time of the build.
2. Counts per class from the JUnit XML (`tests`, `failures`, `errors`, `skipped`), copied from the XML and not from the console summary.
3. For every skipped test: its name and the assumption message from the XML.
4. For every failure or error: the test name, the message, and the first 20 stack-trace lines.
5. The build result line (`BUILD SUCCESSFUL` or `BUILD FAILED`) and the Gradle scan or performance-counter warnings, if any.

## What we expect

On Windows the atomic-replace path is not used: the classes assume POSIX attributes and skip. So the expected result is every test that runs passing, with the POSIX-dependent tests skipped and named. The tests that should skip are the ones whose first assumption is a POSIX check: `SecurityConfHandlerTest` tests that call `assumeTrue` on POSIX support, every test in `SecurityConfHandlerFailurePathsTest`, and `SecurityConfHandlerReaderTest.testReaderNeverSeesPartialSecurityJsonDuringEdits`. The in-place tests and `BasicAuthStandaloneTest` should run and pass.

A skip whose message is not the POSIX message, or a test that fails, is a finding to report with its evidence. Do not change the test to make it pass.

## Standing rules

Do not push, do not comment on the PR, do not change the PR body, do not flip the PR. No em dashes in written text. Record the claim heartbeat at least once an hour, and mark the claim DONE with the report path in the same push as the report.
