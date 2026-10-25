# CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure (unit 2, round 1)

Verdict: reproduces at the RC3 tip and at apache/main with the loop's seed. Root cause not established. The lead is the data-directory computation on the unload path after a failed reload, but that is a hypothesis until a probe confirms it.

## Reproduction (focused, host lock, JDK 21.0.12.1, seed 787C6229591C6F1E)

| Tree | Commit | Log | Result |
|---|---|---|---|
| RC3 tip | 749712b7194 | `/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log` | 1 test, 1 failed (3m 48s): "The data directory was not cleaned up on unload after a failed core reload" |
| apache/main | 13c693d805f | `/workspace/smoke-rc3/coreadmin-repro/main-run1.log` | 1 test, 1 failed (2m 11s): same message |

The same method failed in the RC3 smoke loop (run 001 seed 787C6229591C6F1E; run 002 and later), so the failure is repeatable across seeds and across the loop runs.

## What the test does (`solr/core/src/test/org/apache/solr/handler/admin/CoreAdminHandlerTest.java` lines 403 to 445)

1. Starts a Jetty with a core `corex` whose data directory is read from `CoreAdminRequest.getCoreStatus(...).dataDir` (lines 421 to 426), and asserts it exists.
2. Replaces `solrconfig.xml` with `bad-error-solrconfig.xml` and expects the reload to throw.
3. Unloads with `setDeleteDataDir(true)` and `setDeleteInstanceDir(false)` (lines 439 to 441).
4. Asserts `Files.notExists(dataDir)` for the path captured in step 1 (lines 443 to 445).

## Code read (main tree; not yet traced at runtime)

- `CoreContainer.java` lines 2079 to 2089: unload of a name that is in `coreInitFailures` calls `SolrCore.deleteUnloadedCore(loadFailure.cd, deleteDataDir, deleteInstanceDir)`.
- `SolrCore.java` lines 3313 to 3321: `deleteUnloadedCore` deletes `instanceDir.resolve(cd.getDataDir())`, where `cd` is the CoreDescriptor from the failed load, and `cd.getDataDir()` may be relative.
- The test checks the path reported by `status.dataDir`, taken before the bad reload. If the two paths differ, the unload deletes one directory and the assertion checks another. SOLR-9433 (reopened) covers a clean-up path that uses an incorrect path to delete the data directory on core-create failure, so the lead connects to a known area.

## Hypothesis and the probe that tests it (not run)

Hypothesis: after the failed reload the core is removed from the loaded set and recorded in `coreInitFailures`, so unload takes the `deleteUnloadedCore` branch and computes the data directory from `cd.getDataDir()`, which does not match the directory in `status.dataDir`.

Probe: on a scratch tree at apache/main, print `status.dataDir`, the `cd.getDataDir()` value, and the branch taken (loaded-core close or `deleteUnloadedCore`) just before the unload in the test; a run then shows whether the two paths differ and which branch ran. A matching path with a surviving directory would point elsewhere. The probe is a diagnostic only and is not a fix.

## Not done

- No probe run, no fix branch, no gate, no PR, no JIRA comment, no dev-list text.
- The proof-pair rule (`assignments/rc3-proof-pair-requirement-round-1.md`): this unit provides the pre leg for SOLR-18587 only; the post leg needs a fix.
