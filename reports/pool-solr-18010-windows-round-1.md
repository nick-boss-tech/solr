# SOLR-18010 Windows Test Report

Run date: 2026-10-23 UTC

## Environment and run

- Commit checked: d742eb99dd28be761b5f60aea6fbfc832e1b7a50 on solr-18010-submit. ls-remote matched this SHA before and after the run.
- Windows: gradlew.bat --version reported Windows 11 10.0 amd64. Get-ComputerInfo reported Windows 10 Enterprise, version 2009, build 26200, 64-bit.
- Java: OpenJDK 21.0.11, Eclipse Temurin 21.0.11+10-LTS.
- Gradle: 9.7.0 from the repository wrapper.
- Command: .\gradlew.bat --no-daemon :solr:core:test --tests org.apache.solr.handler.admin.SecurityConfHandlerTest --tests org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest --tests org.apache.solr.handler.admin.SecurityConfHandlerReaderTest --tests org.apache.solr.security.BasicAuthStandaloneTest
- Build result: BUILD SUCCESSFUL in 1h 4m 2s.
- JUnit XML: solr/core/build/test-results/test/TEST-*.xml.
- No tidy command was run. Error Prone compilation was not enabled.

## Counts from JUnit XML

| Class | Tests | Failures | Errors | Skipped |
| --- | ---: | ---: | ---: | ---: |
| org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest | 8 | 0 | 0 | 8 |
| org.apache.solr.handler.admin.SecurityConfHandlerReaderTest | 1 | 0 | 0 | 1 |
| org.apache.solr.handler.admin.SecurityConfHandlerTest | 8 | 0 | 0 | 4 |
| org.apache.solr.security.BasicAuthStandaloneTest | 2 | 0 | 0 | 0 |
| Total | 19 | 0 | 0 | 13 |

## Passed tests

- SecurityConfHandlerTest: testAtomicReplaceNeedsPosixViewAndNoSymbolicLink, testConcurrentPersistConfLeavesOneWholeDocument, testEditKeepsHardLinkedSecurityJsonShared, testEdit.
- BasicAuthStandaloneTest: testConcurrentEdits, testBasicAuth.

## Skipped tests

| Class | Test | XML assumption message |
| --- | --- | --- |
| SecurityConfHandlerFailurePathsTest | testFailedSyncKeepsExistingSecurityJson | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testRefusedMoveFallsBackToInPlaceWrite | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testDirectorySyncFailureAfterMoveDoesNotFailEdit | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testFailedGroupCopyFallsBackToInPlaceWrite | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testOwnerMismatchFallsBackToInPlaceWrite | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testGroupIsCopiedToTemporaryFile | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testTemporaryFileIsSyncedBeforeItIsMoved | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerFailurePathsTest | testFailedPermissionCopyKeepsExistingSecurityJson | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerReaderTest | testReaderNeverSeesPartialSecurityJsonDuringEdits | org.junit.AssumptionViolatedException: atomic replacement needs POSIX file attributes |
| SecurityConfHandlerTest | testEditInPlaceWhenSolrHomeIsReadOnly | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerTest | testPersistCreatesSecurityJsonWithDefaultMode | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerTest | testEditKeepsExistingSecurityJsonPermissions | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |
| SecurityConfHandlerTest | testTemporaryFileIsNoWiderThanExistingSecurityJson | org.junit.AssumptionViolatedException: POSIX file permissions are not supported on this file system |

## Failures and errors

None. XML counts: 0 failures and 0 errors. No failure or error stack traces were present.

## Warnings

- Gradle scan/resource-usage integration could not read Windows performance counter names: Unable to locate English counter names in registry Perflib 009. Assuming English counters. Error 0x80070057. It also printed: Unable to locate English counter names in registry Perflib 009. Counters may need to be rebuilt.
- The associated scan diagnostic included com.sun.jna.platform.win32.Win32Exception: The parameter is incorrect, from the Windows registry performance-counter lookup.
- Gradle emitted: WARNING: errorprone disabled (skipped on builds not running inside CI environments, pass -Pvalidation.errorprone=true to enable). Error Prone itself was not run.
- Test JVM output included warnings for jdk.incubator.vector and the deprecated Security Manager. It also repeatedly printed that java.security.policy could not add java.net.URLPermission because the CLDR locale provider could not be instantiated. These warnings did not correspond to XML failures or errors.
