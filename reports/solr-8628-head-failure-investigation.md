# SOLR-8628 head-leg failure investigation

Date: 2026-10-11. Assignment: `assignments/pool-solr-8628-head-failure-investigation.md`. Claim: `claims/pool-solr-8628-head-failure-investigation.md`. Host: windows (review), read-only. No build, test, gate, branch edit, PR, post or commit was made.

## Classification

**(c) A test-shape problem.** The branch's `initIndex` change is never entered by `testCreateCoreOverIndexDirWithOnlyWriteLock` as written, because the minimal configset selects `MockDirectoryFactory`, an ephemeral factory whose `exists()` never sees the on-disk `write.lock`, so base and head run the same code and the test then asserts an on-disk `segments_` file that this factory never writes.

## Sources and limits

- Branch tree: `solr-8628-submit` at `ce8211e05e062444e7afc1c5ea33fb6eff581d8f`, read with `git show` and `git grep` from this worktree (the commit object is present locally). Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`. Worktree HEAD is `e4b9d38a644`, which is the review worktree and not the branch.
- Not readable on this host: the head-leg log and JUnit XML on vm1, and the base-leg JUnit XML on vm3 (down). The head-leg result is taken from the assignment's summary, and the base-leg result from `gates/SOLR-8628-premise.md`.
- Record discrepancy for the main side: `gates/SOLR-8628-premise.md` says the branch-head leg has NOT RUN and leaves its checklist item open. The assignment reports the vm1 run at 2026-10-11T15:10-15:12Z. This report uses the assignment's summary. The gate record needs reconciling.

## 1. The two failure records

Identical: selector `org.apache.solr.core.TestCoreContainer.testCreateCoreOverIndexDirWithOnlyWriteLock`, seed `8628C0FFEE8628A1`, 1 test, 1 failure, 0 errors, 0 skipped, AssertionError "a new index must have been created" at `TestCoreContainer.java:134`. Both legs passed the `cc.create` non-null check (line 126) and the `numDocs == 0` check (line 129).

Differ: only the production `SolrCore.java` (merge-base versus `ce8211e`). The head-leg stack below the top frame and the raw XML are not visible here, so the comparison is at the level of the recorded summaries.

## 2. The scenario test (branch lines 116-141)

- Line 118, `init(home, CONFIGSETS_SOLR_XML)`: a `CoreContainer` from a solr.xml that sets only `configSetBaseDir` and `shareSchema` (`TestCoreContainer.java:513-518`). No `solr.data.home` and no directoryFactory override. The core root defaults to the solr home (`NodeConfig.java:659`), so `cc.create("core1")` gives instance path `home/core1` (`CoreContainer.java:1487-1488`).
- Lines 121-123, `home/core1/data/index/write.lock`. This is the directory the core uses. The minimal configset has `<dataDir>${solr.data.dir:}</dataDir>` (`solr/core/src/test-files/solr/configsets/minimal/conf/solrconfig.xml:24`), which is empty and becomes null (`SolrConfig.java:323-324`). `CoreDescriptor` defaults to `data/` (`CoreDescriptor.java:87`). `findDataDir` (`SolrCore.java:1450-1471`) resolves it to `instanceDir/data` (`DirectoryFactory.java:329-339`). The index directory is `dataDir + "index/"` when there is no index.properties (`SolrCore.java:441-449`). The placement is correct.
- Line 125, `cc.create`: the core is created.
- Lines 126-132, `numDocs == 0`: cannot discriminate. A fresh empty index and a no-op over `write.lock` both report 0 documents, so this passes on base and head whatever the fix does.
- Lines 133-136, `Files.list(indexDir)` must contain `segments_*`: the only check that can discriminate. It sees only what has been written to `home/core1/data/index` on disk.

## 3. The head create path (`SolrCore.java`, head tree)

- Lines 871-874: `indexExists = exists(indexDir) && !containsOnlyWriteLock(indexDir)`. The `&&` short-circuits, so `containsOnlyWriteLock` runs only when `exists` is true.
- Lines 878-881: `indexExists` true, so `solrCoreState.getIndexWriter(this, false)` opens an APPEND writer (`DefaultSolrCoreState.java:253-263` builds it with `create=false`).
- Lines 885-900: `indexExists` false, so `SolrIndexWriter.create(..., create=true)` (line 889) and `IOUtils.closeQuietly(writer)` (line 900).
- Where `segments_` first reaches disk: the gate's Lucene 10.4.0 probe (`gates/SOLR-8628-premise.md`, part 2) shows `segments_1` present after `writer.close()`. The recorded evidence does not show whether the constructor writes it earlier. The searcher open does not write (`initSearcher`, `SolrCore.java:1233-1248`, and the constructor call at line 1149). `SolrIndexWriter.close()` catches and logs any Throwable from `super.close()` (`SolrIndexWriter.java:379-387`), so a failed commit at line 900 would be logged and not thrown. Line 134 is then the only detector.
- Before `initIndex` (line 1111, inside the constructor starting at line 1042), `getNewIndexDir()` (`SolrCore.java:404-420`) gets only the data directory and reads index.properties. Nothing creates or commits an index before `initIndex`.

## 4. Which directory factory the test runs on

- `solr/core/src/test-files/solr/configsets/minimal/conf/solrconfig.xml:26-27`: `class="${solr.directoryFactory:solr.MockDirectoryFactory}"`. `TestCoreContainer` never calls `useFactory`, and no gradle file sets `solr.directoryFactory`.
- `MockDirectoryFactory extends EphemeralDirectoryFactory` (`solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java:30`). Its `create` uses `LuceneTestCase.newDirectory()` (lines 47-), a Lucene test directory, not the path on disk.
- `EphemeralDirectoryFactory.exists` (`EphemeralDirectoryFactory.java:30-44`) looks only at the factory's own cached `Directory` for the path, and returns false when that directory is empty. A `write.lock` file on disk never reaches it.
- Contrast: `CachingDirectoryFactory.exists` (`CachingDirectoryFactory.java:368-377`) checks the disk path (`Files.isReadable`, then `hasNext`). `StandardDirectoryFactory` extends it (`StandardDirectoryFactory.java:46`), and `NRTCachingDirectoryFactory` extends that.

## 5. Why the recorded base result forces the ephemeral path

On an on-disk factory, base's `exists(indexDir)` is true for a directory holding `write.lock` (`CachingDirectoryFactory.java:368-377`). Base would then take the APPEND branch (the `if (indexExists)` block, equivalent to head lines 878-881) and open an IndexWriter in APPEND mode on a directory with no `segments_N`. Lucene reports that as an IndexNotFoundException ("no segments* file found ... files: [write.lock]"). This is Lucene library behavior, not in this tree, and is listed for confirmation in section 9. Even if the open did not throw, the constructor's `initSearcher` (line 1149, `SolrCore.java:1245-1248`, no catch) would open a reader on a directory with no commit. Either way, the failure propagates. `CoreContainer.processCoreCreateException` rethrows anything that is not a `CorruptIndexException` (`CoreContainer.java:1762-1773`), and the SolrCore constructor rethrows from its `catch (Throwable e)` (`SolrCore.java:1182`).

So on an on-disk factory the base leg would error at line 125 or fail at line 126, never at line 134. The recorded base leg reached line 134 with 0 errors. Therefore the base leg ran with `exists()` false, which only the ephemeral factory returns for this directory. The inference depends on the recorded base result, which I could not re-read.

## 6. Consequence for head

Under the ephemeral factory, head's `exists(indexDir)` is false, so the `&&` at line 874 short-circuits and `containsOnlyWriteLock` is never called. Head executes the same instructions as base: create path, RAM-backed directory, 0 documents, nothing on disk. The head failure is identical by construction. There is no divergence point, so (b) is excluded. The assertion at line 134 fails on any tree, because this factory never writes `home/core1/data/index`.

## 7. Why (c) and not (a)

(a) would mean the fix does not change what production takes. On an on-disk factory it does: base takes the APPEND branch and fails, and head takes the create branch (line 889) and, per the gate's Lucene probe, commits `segments_1` to disk at close (line 900). The fix covers the on-disk path. The failure comes from the test selecting an ephemeral factory, while the test names and observes an on-disk outcome.

The verdict rests on one unverified step: that the head create branch on a real on-disk Solr core leaves `segments_1` on disk. The gate's probe covered Lucene alone, not a Solr core. Section 9 tests this directly. If the on-disk head run still lacks `segments_`, the verdict moves to (a).

## 8. Secondary risk for the fix (does not change the classification)

- `SolrCore.java:861` opens the index directory with lock type `"none"`. `CachingDirectoryFactory.get` caches by path and the first caller's lock type wins (`CachingDirectoryFactory.java:388-410`). `StandardDirectoryFactory` maps `"none"` to `NoLockFactory` (`StandardDirectoryFactory.java:69-70`).
- In production the first `get` of the index path is this one, because `getNewIndexDir` gets only the data directory. The cached directory therefore has no lock. The writer created at line 889 calls `get(path, DEFAULT, config.lockType)` (`SolrIndexWriter.java:116`) and receives the same cached, unlocked directory.
- A directory holding only `write.lock` can also belong to a live writer that has not committed yet. The check should use the configured `lockType` or run under the lock. The fix spec should say so.

## 9. Corrected test shape (for (c)) and confirmation run spec

Corrected test shape:

- Setup: keep lines 117-123 unchanged. Make the first statement of the method `useFactory("solr.StandardDirectoryFactory");` (`solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java:370-383`). It must run before `init(home, CONFIGSETS_SOLR_XML)`, because the solrconfig reads `solr.directoryFactory` when the core loads. The base-class teardown calls `resetFactory()` (`SolrTestCaseJ4.java:309`). Many core tests use the same call, e.g. `TestCloudSearcherWarming.java:60`.
- Action: unchanged `cc.create("core1", ...)` at line 125.
- Guard: after `create`, add `assertTrue(core.getDirectoryFactory() instanceof StandardDirectoryFactory);` (`SolrCore.getDirectoryFactory()` is public, `SolrCore.java:473`). A silent fall-back to an ephemeral factory then fails loudly.
- Observables: keep lines 126-132 and the 133-136 `segments_` check. Lines 134-136 now discriminate, because the factory writes that directory.
- Expected behavior: base errors at `cc.create` (no segments file, IndexNotFoundException path) before line 134. Head passes.
- Alternative: `NRTCachingDirectoryFactory` is the production default (the fallback in `DirectoryFactory.loadDirectoryFactory`), and is closer to production parity. `StandardDirectoryFactory` is chosen here for the simplest on-disk behavior.

Confirmation run spec (for a Linux host, JDK 21, under the WORKFLOW claim rules; NOT run here):

- Trees: (i) merge-base `cabedd1d968059215188f4e7563fb303241899ed` with its production `SolrCore.java`; (ii) head `ce8211e05e062444e7afc1c5ea33fb6eff581d8f`. Apply the corrected `TestCoreContainer.java` as a test-file-only overlay to each.
- Command: `./gradlew :solr:core:test --tests org.apache.solr.core.TestCoreContainer.testCreateCoreOverIndexDirWithOnlyWriteLock -Ptests.seed=8628C0FFEE8628A1`. The seed property is read at `gradle/testing/randomization.gradle:57`.
- Expected: (i) 1 test, 1 error at `cc.create` (line 125), IndexNotFoundException "no segments* file found" naming `[write.lock]`, and not a failure at line 134. (ii) 1 test pass, 0 failures, 0 errors, with `segments_*` present in `home/core1/data/index` and the guard satisfied.
- Control: the uncorrected test at head, same seed. Expected: the identical AssertionError at line 134, which confirms the ephemeral-factory diagnosis. Also record the factory class in the log.
- Verdict flips: if (ii) fails with no `segments_` on the on-disk factory, reclassify to (a). If (i) passes, the base path is not as section 5 reasons, and the classification must be reopened.

## 10. Paths checked that did not change the classification

- Empty directory: `CachingDirectoryFactory.exists` returns false for an empty directory, so the ticket's empty-directory case works on main. Out of scope, as the gate says.
- Two minimal configsets: `solr/test-framework/src/test-files/.../minimal/conf/solrconfig.xml:27` defaults to NRTCaching, while the core test-files copy defaults to Mock. The classification does not depend on which one classpath loads, because the recorded base result requires `exists()` to be false. Module test resources are put first by `gradle/java/folder-layout.gradle:25`, but this is not relied on.
- `solr.data.home` unset: `dataHomePath` stays null and the data directory is under the instance directory (`DirectoryFactory.java:329-339`).
- No index.properties: the index directory is `dataDir/index/` (`SolrCore.java:441-449`).
- Diff scope: the branch diff against the merge-base touches only `SolrCore.java` (the `containsOnlyWriteLock` helper and the `&&` in `initIndex`), the test file, a changelog YAML and `SOLR-8628-TESTING.md`. No other code on the path changes.
- Constructor before `initIndex`: no index write or commit (section 3).
- `processCoreCreateException`: only `CorruptIndexException` is handled (`CoreContainer.java:1762-1773`), so the test's error would not be rescued.
- Gate parts 1-2 (Lucene probes): reused from the gate record and not rerun.

Result: classification (c), with the confirmation spec in section 9 as the proof step.
