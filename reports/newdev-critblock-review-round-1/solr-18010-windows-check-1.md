# SOLR-18010: Windows check of PR 5118 head (reply from the Windows agent, recorded by vm3, 2026-10-22)

Commit: `1b14ce9556` (PR 5118 head, branch `solr-18010-submit`). Not a gate: the gate of record is Linux.

## Environment

- Windows 11, amd64. JDK `21.0.11+10-LTS`. Gradle `9.7.0` from the repo's `gradlew.bat`.
- Command: `.\gradlew.bat --no-daemon :solr:core:test --tests org.apache.solr.handler.admin.SecurityConfHandlerTest --tests org.apache.solr.security.BasicAuthStandaloneTest`
- Result: BUILD SUCCESSFUL in 16m 33s. Error Prone was not part of this run (not a gate here).
- The Gradle scan plugin logged a Windows performance counter registry warning. The build continued.

## Results (counts from JUnit XML)

| Class | Tests | Failures | Errors | Skipped |
| --- | ---: | ---: | ---: | ---: |
| `SecurityConfHandlerTest` | 5 | 0 | 0 | 2 |
| `BasicAuthStandaloneTest` | 2 | 0 | 0 | 0 |
| Total | 7 | 0 | 0 | 2 |

- PASS: `testConcurrentPersistConfLeavesOneWholeDocument`, `testConcurrentEditsToLocalSecurityJson`, `testEdit`, `BasicAuthStandaloneTest.testConcurrentEdits`, `BasicAuthStandaloneTest.testBasicAuth`.
- SKIPPED (POSIX assumption, "POSIX file permissions are not supported on this file system"): `testEditInPlaceWhenSolrHomeIsReadOnly`, `testEditKeepsExistingSecurityJsonPermissions`.

## What this does not show

- The held-file case (a Windows share mode that denies delete while the edit runs) was not run. It needs a scratch test and another Gradle run.
- The Windows run does not reach the atomic move, because the atomic path is not hit by the skipped tests. A Windows result on the atomic path needs the seam tests (see `solr-18010-narrow-decision.md`).
- The worktree stayed detached at the requested commit. No branch, commit, push or tidy was made.
