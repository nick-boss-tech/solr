# SOLR-16272 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`bin/solr package install` was not repeatable: if any artifact post failed
(e.g. `Signature does not match any public key`), the exception propagated
out of `RepositoryManager.installPackage()` with no cleanup. The user was
then stuck — retrying failed on the leftover `manifest.json` (`Path already
exists /package/<pkg>/<ver>/manifest.json`) and `bin/solr package
uninstall` refused because the package was never registered (`Package ...
doesn't exist. Use the install command ... first`). Only manual file-store
deletion escaped.

Fix (one file, client-side only, no server changes): `installPackage()`
now tracks every successfully posted file-store path (manifest + each
artifact) in `postedFiles`, and a new `cleanupPartialInstall()` helper
deletes them before the original exception is rethrown. Cleanup mirrors the
deletion steps in `PackageManager.uninstall()`:

- `DistribFileStore.deleteZKFileEntry(packageManager.zkClient, filePath)`
  for the ZK file entry;
- the generated `FileStoreApi.DeleteFile` request (same usage pattern as
  `FileStoreApi.SyncFile` already in this file) for the stored file.

Cleanup is best-effort: per-file failures are logged at warn and swallowed
so the original install failure is what the user sees. The failed install
is now atomic from the user's perspective: retry and uninstall both behave.

Both failure modes are covered: `catch (SolrException e)` (Package API
registration failures, rethrown as-is) and `catch (SolrServerException |
IOException e)` (post failures, wrapped as before). The pre-post
"no manifest found" `SolrException` still triggers cleanup, but
`postedFiles` is empty at that point, so it's a no-op.

Round-3 review change (registration failures): the Package API call is wrapped,
so a client-side failure (timeout, connection reset) reaches the same catch as
a definite server rejection, although the server may already have registered the
version. Deleting the files of a registered version would break it (`isJarInuse`
only protects versions deployed to a collection). So once the registration
request has been sent (`registrationAttempted`), `cleanupPartialInstall` keeps the
files unless `mayBeRegistered` confirms the version is not registered: it
re-reads the registered instances through `packageManager.getPackageInstance`
and, if that check itself fails, treats the version as possibly registered. Failures
before the registration request (the signature case from the ticket) clean up as
before. The cleanup message now uses `runtime.println` instead of `printSuccess`.

Known limits, unchanged: a file that already existed with identical content is
treated as posted by this run (the file store accepts an identical re-post), so
it is removed on a later failure; users already stranded by an earlier failed
install are not helped (a retry with changed content still hits "Path already
exists"); a file whose post threw after the server persisted it is not tracked.

Files changed:
- `solr/core/src/java/org/apache/solr/packagemanager/RepositoryManager.java`
  (new `DistribFileStore` import; generated `FileStoreApi` was already
  imported)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test added (round-3 patch pass, not compiled or run)

- `PackageToolTest#testFailedInstallLeavesNoPostedFilesBehind`: copies the
  test repository into a temp directory under another package name
  (`question-answer-badsig`) with a corrupted signature, serves it with a second
  `LocalWebServer`, adds it with `add-repo`, runs `install` (failing on the
  signature), and then asks the file store for the manifest through
  `FileStoreApi.GetMetadata`; the entry must be absent. It is not asserted
  whether the tool reports the failure by status or by exception. The second
  server stays up until `@AfterClass` because every repository is refreshed on
  each package command.
- Not covered: the registration-failure paths (definite rejection removes files;
  a lost response keeps them), which need a way to make the Package API call
  fail.

Queued for the verification run: `org.apache.solr.cli.PackageToolTest`
(`:solr:core:test`), with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Changelog fragment added: `changelog/unreleased/SOLR-16272.yml`.
- Remove this file before opening the upstream PR.
