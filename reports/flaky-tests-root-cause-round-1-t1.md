# Flaky tests round 1, test 1: GCSInstallShardTest, suite teardown collects restore errors

Result: the most likely mechanism is a non-restore exception that escapes one JettySolrRunner.stop() during MiniSolrCloudCluster.shutdown(). It is not the restore IndexNotFoundException that the main-side RCA names. Confidence: high that the RCA's collection path does not exist in the code at `upstream/main` `8e62c2686882`; medium that the stop-time exception is unrelated to restore. The identity of the suppressed exception is not in the record, and it is the one fact that settles this.

Citation key, all at `8e62c2686882`:
- TF = `solr/test-framework/src/java/org/apache/solr`
- CORE = `solr/core/src/java/org/apache/solr`
- GCSM = `solr/modules/gcs-repository/src/java/org/apache/solr/gcs`
- GCST = `solr/modules/gcs-repository/src/test/org/apache/solr/gcs`

## Answers to the four questions

**Q1. Which thread records the restore exception, and how does it reach the set checkForExceptions reads?**
No such set exists, and no path from a restore error to `checkForExceptions` was found. `checkForExceptions` reads only the futures from the `stopJettySolrRunner` callables in `shutdown()` (`TF/cloud/MiniSolrCloudCluster.java` lines 627 to 637 and 718 to 736). A restore error reaches it only if it escapes `JettySolrRunner.stop()` (`TF/embedded/JettySolrRunner.java` lines 611 to 670). The restore error is thrown on the thread running `InstallCoreData.installCoreData` (`CORE/handler/admin/api/InstallCoreData.java` lines 91 to 97) and logged with `log.warn` at `CORE/handler/RestoreCore.java` lines 178 and 218. It returns to the caller as a request error and is stored in no shared collection.

The escape routes traced all catch and log: `CORE/core/SolrCores.java` lines 118 to 124 (per-core close catches Throwable, rethrows only Error), and `CORE/core/CoreContainer.java` lines 1258 to 1264, 1267 to 1271 and 1287 to 1292. The one call with no catch is `CORE/servlet/CoreContainerProvider.java` lines 112 to 133, which calls `cc.shutdown()` unguarded. Whether a call inside `cc.shutdown()` throws is H1 below.

**Q2. An expected product of the suite's injection, or a restore that should have succeeded?**
Expected. Both restore failures in the record come from test methods that expect them.
- Injected failure: `TF/cloud/api/collections/AbstractIncrementalBackupTest.java` line 583 (static `portsToFailOn`) and lines 594 to 609 (`copyFileTo` and `copyIndexFileTo` throw UnsupportedOperationException for an injected port). The port is set at `AbstractInstallShardTest.java` lines 255 to 256 in `testInstallSucceedsOnASingleError` (lines 249 to 286), which expects the injected node's replica to recover (lines 262 to 267).
- Nonexistent location: `AbstractInstallShardTest.java` lines 190 to 219 expect RemoteSolrException and state FAILED. The location comes from lines 133 and 380 to 386.

What decides which: the injected node is decided by the static set plus the per-node hostPort (`TF/embedded/JettySolrRunner.java` lines 320 to 323; read in `GCST/GCSInstallShardTest.java` line 48). The nonexistent case is decided by the location URI passed to the install (`AbstractInstallShardTest.java` lines 195, 203 and 213).

The injected error cannot be the IndexNotFoundException. The injected copy throws, `RestoreCore` wraps it (`CORE/handler/RestoreCore.java` lines 177 to 181) and rethrows it (lines 194 to 197), so the restore never reaches line 207.

**Q3. What asks for a segments file in a restore directory that has none?**
The IndexWriter open at `CORE/handler/RestoreCore.java` line 211, inside the try at lines 210 to 215:
1. `doRestore` opens `restore.<timestamp>` (lines 102 to 115).
2. The download loop iterates `repository.listAllFiles()` (line 145). If the list is empty, nothing is copied.
3. `modifyIndexProps` points index.properties at the restore dir (line 207; `CORE/core/SolrCore.java` lines 1465 to 1489).
4. `newIndexWriter(false)` calls `createMainIndexWriter`, which opens `core.getNewIndexDir()` (`CORE/update/DefaultSolrCoreState.java` lines 253 to 264; `CORE/core/SolrCore.java` lines 404 to 420) with create=false (`DefaultSolrCoreState.java` line 259), which becomes OpenMode.APPEND (`CORE/update/SolrIndexWriter.java` lines 166 to 167).
5. APPEND on a directory with no commit throws IndexNotFoundException. This is Lucene behavior, not code in this repo. The record's "no segments* file found" text matches Lucene's message.

Step 2 is empty for a missing location: `GCSM/GCSBackupRepository.java` lines 202 to 225 list blobs by prefix and return an empty array when none exist. So the source is the nonexistent-location install (`AbstractInstallShardTest.java` lines 190 to 219). The rollback at `RestoreCore.java` lines 215 to 240 catches it and throws SolrException (lines 238 to 239), which is the expected failure.

The record names a restore directory as the place the exception came from, and line 211 is the only route found that opens the restore dir. That is evidence for this route, not proof, because the exception text is not quoted in full.

A second open happens at `RestoreCore.java` line 236, after the rollback deletes index.properties (line 227), so it opens the default index dir. No way was found for it to fire in these tests, because each test builds a fresh collection (`AbstractInstallShardTest.java` lines 192 and 251). This is an inference.

**Q4. Does the same collection hazard exist in other suites?**
Yes. Suites that inject errors and shut down through MiniSolrCloudCluster:
- Extending `AbstractInstallShardTest`: `solr/core/src/test/org/apache/solr/cloud/api/collections/LocalFSInstallShardTest.java` (class at line 25, injected repo at line 34); `solr/modules/s3-repository/src/test/org/apache/solr/s3/S3InstallShardTest.java` (lines 45 and 55).
- Extending `AbstractIncrementalBackupTest`: `solr/core/src/test/org/apache/solr/cloud/api/collections/LocalFSCloudIncrementalBackupTest.java` (lines 36 and 62); `GCST/GCSIncrementalBackupTest.java` (lines 31 and 56); `solr/modules/s3-repository/src/test/org/apache/solr/s3/S3IncrementalBackupTest.java` (lines 44 and 75).

All share the static `portsToFailOn` (`AbstractIncrementalBackupTest.java` line 583), which is never reset. All inherit `SolrCloudTestCase.shutdownCluster` (`TF/cloud/SolrCloudTestCase.java` lines 157 to 166) and the MiniSolrCloudCluster shutdown check, which every MiniSolrCloudCluster suite uses. What is GCS-specific: the stash clear in `tearDownClass` (`GCST/GCSInstallShardTest.java` lines 69 to 72) and GCS listing.

LocalFS hits the same empty-location path, because `CORE/core/backup/repository/LocalFileSystemRepository.java` lines 122 to 124 return an empty list for a missing dir. A passing LocalFS run whose log shows the same IndexNotFoundException lines would show those lines are routine. This is an inference; no LocalFS log was read.

## Part 1: ranked hypotheses

**H1 (most likely; medium confidence). A non-restore exception escapes one node's stop during teardown. The IndexNotFoundException lines are logs from expected failures in the same suite.**

Causal chain:
1. `shutdown()` runs `stopJettySolrRunner(jetty)` on a closer pool and collects the futures (`TF/cloud/MiniSolrCloudCluster.java` lines 627 to 635).
2. `stopJettySolrRunner(JettySolrRunner)` calls `jetty.stop()` (lines 528 to 532).
3. `checkForExceptions` adds each ExecutionException cause as suppressed and logs it with its stack (lines 724 to 727). The outer "Error shutting down" exception is thrown at lines 636 to 640.
4. `JettySolrRunner.stop()` rethrows whatever escapes `server.stop()` (`TF/embedded/JettySolrRunner.java` lines 626 to 630; only TimeoutException is caught there), the reserved-executor wait (lines 651 to 652), and the `server.join` loop (lines 655 to 661).
5. Jetty calls the servlet context destroy during `server.stop()` (Jetty behavior, not read here). That calls `CoreContainerProvider.close()` and then `cc.shutdown()` with no try (`CORE/servlet/CoreContainerProvider.java` lines 112 to 133).
6. Inside `CoreContainer.shutdown()` (`CORE/core/CoreContainer.java` lines 1220 to 1330), the unguarded calls are `waitForPendingTasksToComplete` (line 1224), `preClose` (line 1240), `tryCancelAllElections` (line 1244), and `zkSys.close()` in the finally (line 1313). Any of them can throw out of stop. None is restore code, but the record does not say which one threw. This is the gap.

This is the chain the code permits with no restore exception crossing into the check.

**H2 (the RCA's mechanism; rejected by the code). A restore exception recorded on a server thread reaches `checkForExceptions`.** Q1 shows there is no set and no route. For H2 to hold, a restore exception would have to escape `cc.shutdown()`, and each restore-path catch traced logs instead.

**H3 (lower; timing, not restore-specific). A TimeoutException from the reserved-executor wait.** `TF/embedded/JettySolrRunner.java` lines 651 to 652 call `TimeOut.waitFor` (`CORE/util/TimeOut.java` lines 85 to 90), which throws TimeoutException after 30 seconds. The comment at lines 646 to 650 says the wait "doesn't always seem to work", so a stuck reserved thread would surface here uncaught. This is the first thing to check in Run 1.

**H4 (rejected as the teardown cause; a real hazard). Leftover state from the injection or failed restores.** The injected port set is never cleared (`AbstractIncrementalBackupTest.java` line 583; `AbstractInstallShardTest.java` lines 255 to 256). The download-failure path leaves partial `restore.<ts>` dirs (`RestoreCore.java` lines 187 to 205; cleanup only in rollback at lines 234 to 235). `CachingDirectoryFactory.close()` gives up after about 12 seconds with a SolrException (`CORE/core/CachingDirectoryFactory.java` lines 166 to 177), but the caller catches and logs it (lines 181 to 183). So refcount leftovers produce logged errors, not a shutdown exception. The port set leaks into later tests in the class, which can cause test-level failures, not this teardown error.

## Part 2: evidence

- **The record proves:** the failing unit is the class-level teardown (classMethod), and the shutdown check did see a stop exception. "Error shutting down MiniSolrCloudCluster" is thrown only at `MiniSolrCloudCluster.java` lines 637 to 639. IndexNotFoundException "no segments* file found" lines occurred in restore directories during the run. The error-injecting repository was loaded in the run.
- **The record suggests, but does not prove:** that the IndexNotFoundException is what the shutdown check collected. The record does not quote the suppressed cause, and the code gives no route for it. That the restore caused the teardown failure is co-occurrence only.
- **The code proves:** `checkForExceptions` sees only stop-callable exceptions (`MiniSolrCloudCluster.java` lines 718 to 736). The injected error is UnsupportedOperationException and cannot reach the IndexNotFoundException (`RestoreCore.java` lines 177 to 197). The IndexNotFoundException with a restore-dir path comes only from the line 211 open after the download loop (the Q3 chain).
- **Evidence for H1:** consistent with all of the above. The only gap is the missing cause.
- **Evidence against H1:** none found in the code or the record.
- **Evidence against H2:** Q1, from the code. H2 would need the suppressed cause to be an IndexNotFoundException with RestoreCore frames. The record does not show that.
- **H3:** no evidence either way beyond the code path.
- **H4 as teardown cause:** none. The code says the refcount path logs.

## Part 3: proposed fix (not applied)

Do not change the shutdown check (`MiniSolrCloudCluster.java` lines 636 to 640). Silencing it hides real teardown failures.

The fix depends on the suppressed cause, which is read from the existing log (see Part 4, Run 1):
- **TimeoutException at `JettySolrRunner.java` line 652:** change `TF/embedded/JettySolrRunner.java` `stop()`, which is shared by every MiniSolrCloudCluster suite. Make the reserved-executor wait at lines 651 to 652 log a warning on timeout, the same as the `server.stop` branch at lines 628 to 630, or drop the wait as the comment at lines 646 to 650 suggests. This is the only non-restore timeout found that escapes stop.
- **RuntimeException from an unguarded call in `CoreContainer.shutdown()`:** wrap that one call in the same try and log pattern used at lines 1261 to 1271 in `CORE/core/CoreContainer.java`. The escape is a production shutdown defect.
- **IndexNotFoundException with RestoreCore frames:** the route analysis would then be wrong. No fix until those frames are read.

Hardening that applies regardless of cause (shared test framework; it does not explain this teardown failure):
- `TF/cloud/api/collections/AbstractIncrementalBackupTest.java`, `setUpTrackingRepo()` (`@Before`, lines 109 to 112): add `ErrorThrowingTrackingBackupRepository.portsToFailOn = Set.of();`. The same reset in `TF/cloud/api/collections/AbstractInstallShardTest.java`, `deleteTestCollections()` (`@After`, lines 91 to 96), or in a `finally` around the injection at lines 255 to 286. The Set is already used in both files. The injected port otherwise leaks across tests and suites in one JVM.

Optional, GCS only: `GCST/GCSInstallShardTest.java` `tearDownClass` (lines 69 to 72) clears the static stash. JUnit 4 runs subclass `@AfterClass` methods before the superclass `shutdownCluster`, so the stash is cleared while the cluster is still up. No shutdown path that reads the stash was found, so no change now. If Run 1 shows a GCS frame in the shutdown stack, move the clear after cluster shutdown.

Separate product defect, flagged and not this flake's fix: `CORE/handler/RestoreCore.java` line 227 deletes index.properties on rollback instead of restoring the previous pointer. That is correct for a core on the default index dir and wrong if the core had a non-default index before the install. These tests use fresh collections, so they do not hit it. Owner decision if wanted.

## Part 4: main-side verification

**Run 1 (no new test run; read the existing log).** Use job `114192758248` of run `37987579785`, or the queued run's log. Find "Error shutting down MiniSolrCloudCluster" and the ERROR line logged by `checkForExceptions` (`MiniSolrCloudCluster.java` line 726), with its cause and stack.
- Confirms H1 if the cause's top frames are in `JettySolrRunner.stop`, `CoreContainer.shutdown`, or the servlet destroy, and no RestoreCore frame appears. The cause type names the unguarded call to fix.
- Confirms H3 if the cause is a TimeoutException with the message "Timeout waiting for reserved executor to stop." (`JettySolrRunner.java` line 652).
- Refutes H1 and supports the RCA if the cause has RestoreCore frames. Then the Q1 analysis missed an escape route, and those frames are needed to find it.

**Run 2 (already queued; the settling run).** Module `:solr:modules:gcs-repository`, class `org.apache.solr.gcs.GCSInstallShardTest`, seed `B94347D2600CC75D`, tree current main `8e62c2686882` with no fix. Read the same log for the cause. One run cannot refute H1, because a pass does not rule out a timing flake. Repeat the same command five times and count failures. A reproduced failure with the same cause confirms the seed reproduces it. A pass in all five with no failure means the flake is timing-bound, not seed-bound.

**Run 3 (main, method-filtered, same class and seed).** Run `testInstallReportsErrorsAppropriately` alone (no injection), then `testInstallSucceedsOnASingleError` alone (with injection). If teardown fails in the no-injection run, the restore path is not needed for the teardown error, which supports H1 or H3 over H4. If it fails only in the injection run, the leaked injected state matters (H4).

**Run 4 (log comparison, no new run).** Check LocalFSInstallShardTest logs from passing CI runs for the same IndexNotFoundException lines in restore dirs. If they appear on passing runs, those lines are routine and not the teardown cause.

After a fix, the tree is main plus the cause-specific fix and hardening, and the check is Run 2 repeated five times. Five clean runs do not prove a timing flake is gone. They only fail to reproduce it.

Owner decisions: none needed now. The RestoreCore rollback defect (`RestoreCore.java` line 227) is a separate owner call, if you want it fixed.

## Not checked

- The job log for run `37987579785` / job `114192758248`, and the queued settling run's log. The suppressed cause is the single fact that separates H1 from H2, and the assignment does not quote it.
- Lucene's APPEND behavior and exact exception text (library code, not in this repo).
- The collection-level install command that turns the core error into "Could not install data to collection" (not read).
- The S3 listing and S3 backup repository code (not read). The S3 rows in Q4 rest on inheritance only.
- Test method order and the six skipped tests for seed `B94347D2600CC75D` (randomized runner ordering not computed; the record gives no skip reasons).
- Jetty's servlet-context stop behavior (external library) and JUnit 4 `@AfterClass` ordering (JUnit behavior, not in the repo).
- Nothing was run. No builds, tests, Gradle, edits, or posts.
