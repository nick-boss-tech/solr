# PR draft: configset file delete rejects names that resolve outside the configset directory

Branch: `hardening-configset-delete-containment` on the fork (head 3905ad78e8, based on 498a09aea9). No PR opened.

## What happens today

`FileSystemConfigSetService.deleteFilesFromConfig` resolves each requested file name against the configset directory and deletes whatever it resolves to. It does not check that the resolved path stays inside that directory. A name with `../` segments resolves outside the configset and the delete goes through. Sibling paths in the same class already verify containment: `uploadFileToConfig` drops names that resolve outside the configset root (SOLR-17543), and `getConfigDir` rejects config names outside the base.

## What this change does

Before deleting anything, `deleteFilesFromConfig` checks every requested name with `FileUtils.isPathAChildOfParent`, the same helper the upload path uses. A name that resolves outside the configset directory fails the call with a `BAD_REQUEST` error, and no file in the list is deleted. Names inside the configset are deleted as before. The check runs on the whole list first so a rejected name does not leave earlier names half-deleted.

Behavior change, stated openly: a call that names a path outside the configset now fails with an error. Before, it deleted the outside target. The upload path logs and skips instead of failing, because uploads are bulk and quiet by design. This method is not bulk, so it reports the error.

## Proof

Test `TestFileSystemConfigSetService.testDeleteFilesRejectsNamesOutsideConfigSet` covers a `../` name (rejected, the outside file is untouched, the normal file in the same call is not deleted) and a normal name (deleted).

- Base `498a09aea9` with only the production file reverted: 1 of 3 tests fails, `testDeleteFilesRejectsNamesOutsideConfigSet`, "expected SolrException to be thrown, but nothing was thrown". Log `/workspace/gates/logs/vm4/configset-item3-failbefore.log`, XML `configset-item3-failbefore-TEST.xml`.
- Head 3905ad78e8: 3 of 3 tests pass, 0 failures, 0 skipped. Log `/workspace/gates/logs/vm4/configset-item3-test.log`, XML `configset-item3-TEST-TestFileSystemConfigSetService.xml`, 2026-10-19.
- `:solr:core:spotlessJavaCheck` passes. Log `/workspace/gates/logs/vm4/configset-item3-spotless.log`.

## Question for the maintainer

Should a name that resolves outside the configset fail the whole call with an error (this draft), or be skipped with a warning, as the upload path does?
