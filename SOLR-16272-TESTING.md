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

Files changed:
- `solr/core/src/java/org/apache/solr/packagemanager/RepositoryManager.java`
  (new `DistribFileStore` import; generated `FileStoreApi` was already
  imported)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Drive `installPackage` with an artifact whose signature doesn't verify
  (mini-cluster or mocked file-store post/delete) → expect the original
  exception AND that `/package/<pkg>/<ver>/manifest.json` no longer exists
  afterwards, so a second install attempt gets past "Posting manifest...".
- Registration-failure path: post succeeds, Package API rejects → files
  cleaned up, retry behaves.
- Existing package-manager test suite for regressions.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
