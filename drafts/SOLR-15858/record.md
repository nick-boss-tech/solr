# Skip record: SOLR-15858

- Ticket: SOLR-15858, "ConfigSetsHandler requires DIR entries in the uploaded ZIPs". JIRA status Open, resolution none, fix versions none, created 2021-12-20, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: close-obsolete (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-15858-draft-close (the earlier keep-open branch solr-15858-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15858-vm2.md).

## Ticket reading

The reporter says a configset zip that holds files in sub-folders, without explicit DIR entries, fails with NoNodeException. The cause given is ConfigSetsHandler.createZkNodeIfNotExistsAndSetData, which assumes the entry path has no sub-path elements. The suggested fix is to check for "/" in each entry name and create the ZooKeeper path first. The report has no comments and names no test class.

## Code reading

- The named method is not in origin/main. `git grep -c 'createZkNodeIfNotExistsAndSetData' origin/main` returns no matches (empty result, run at the pinned head).
- It was removed by commit 1a922dc99aa (SOLR-15258: ConfigSetService add CRUD operations, subsume ZkConfigManager, 2021-04-03). That commit is an ancestor of the pinned head. The removal predates the ticket (filed 2021-12-20), so the report was written against an older release.
- The current zip upload path is solr/core/src/java/org/apache/solr/handler/configsets/UploadConfigSet.java:101-115. It skips entries where zipEntry.isDirectory() (line 107) and sends each file to configSetService.uploadFileToConfig(configSetName, filePath, bytes, true) with the full relative path, sub-folders included.
- solr/core/src/java/org/apache/solr/cloud/ZkConfigSetService.java:196-218: uploadFileToConfig builds the ZooKeeper path and calls zkClient.makePath(filePath, data, CreateMode.PERSISTENT, null, !overwriteOnExists) at line 218. makePath creates missing parent nodes, so a file in a sub-folder does not need its DIR entry first.

Commands at the pinned head:
- git grep -c 'createZkNodeIfNotExistsAndSetData' origin/main: no matches.
- git log --oneline -S 'createZkNodeIfNotExistsAndSetData' origin/main: 1a922dc99aa is the removal.
- git grep -n 'makePath' origin/main -- solr/core/src/java/org/apache/solr/cloud/ZkConfigSetService.java: line 218.

## History

- Commits on origin/main naming SOLR-15858: none.
- The removal commit 1a922dc99aa is an ancestor of the pinned head (checked with git merge-base --is-ancestor).

## Reproduction attempts

Not run in this pass. The current behaviour for a zip without DIR entries (a file in a sub-folder) was inferred from the code path above, not observed. A test would upload such a zip through the configset API on the pinned head. The close verdict rests on the removal of the named code, which the rubric accepts as the evidence for close-obsolete; the reproduction is noted as a gap below.

## Gap analysis

The failing code the ticket names is gone. The replacement path creates parent ZooKeeper nodes through makePath. The one thing not observed directly is an end-to-end upload of a sub-folder file with no DIR entry on this head.

## Verdict justification

close-obsolete meets the rubric: the feature or code the ticket is about has been removed (1a922dc99aa, ancestor of the pinned head), and a search of main finds no remaining use (the empty grep above). The rubric's minimum evidence for this verdict is the grep command and its empty result at the pinned head, which is recorded here. Requirements not fully met: the end-to-end upload was not run on this head, so the replacement path's behaviour is from the code reading only.

## Limits

This record does not show that a current upload of a zip without DIR entries succeeds. It does not check the older 8.x release the reporter used. It does not check other callers of uploadFileToConfig for the same issue.
