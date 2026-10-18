We could not reproduce this on Linux against Solr main at commit 498a09aea9, and we do not have the macOS environment from the report, so this stays open.

What we checked:

- The backup-restore test code at that commit. The 2022 fix raised the wait on a restore call to 30 seconds. Two backup calls still wait 20 seconds: one in testRestoreToOriginalCollection and one in testRestoreToOriginalSucceedsWithErrors.

How we tried to reproduce it:

- Ran the LocalFSCloudIncrementalBackupTest class (which includes both of those tests) three times at commit 498a09aea9, including once with extra CPU load on the machine. All three runs passed. The slowest test case took 13.2 seconds, under the 20-second wait.

What this means:

- The failure appears to depend on timing in the macOS environment, which we could not test here.

If you still see this:

- A failing log from a current Solr main build that names the test method, the failing line, and the time the backup took. Or a run on macOS of the same test. Please reopen the ticket with that information.
