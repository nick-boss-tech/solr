# SOLR-6438 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-6438 - "MergeIndex command ignores srcCore values when both indexDir and srcCore are mentioned" (Anshum Gupta). "At the least, we should error out." Alan Woodward had an older refactoring patch that did not throw yet.
- Branch: `solr-6438-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
In `admin.api.MergeIndexes.mergeIndexes`, `if (dirNames.isEmpty()) { ...use srcCores... } else { ...use indexDirs... }`. A request with both silently drops `srcCores`. Still true on main. The v1 `MERGEINDEXES` action goes through the same code (`MergeIndexesOp`), as far as I could tell.

## What the branch changes
- `MergeIndexes.mergeIndexes`: throw `BAD_REQUEST` ("Only one of indexDir or srcCore can be specified, not both") when both lists are non-empty.
- New `MergeIndexesTest.testReportsErrorIfBothIndexDirAndSrcCoreAreGiven`, next to the existing "both empty" test.

## What was guessed (verify these first)
1. The check is inside the async task lambda (like the existing "At least one" error), so with `async` set the error would be reported via the task status rather than the HTTP response. The existing tests call the synchronous path.
2. A caller that relied on srcCore being ignored will now get a 400 (arguably intended).
3. `CoreAdminOperationTest`/`CoreMergeIndexesAdminHandlerTest` do not pass both parameters (not checked beyond a grep for `srcCore`).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.admin.api.MergeIndexesTest" --tests "org.apache.solr.handler.admin.CoreMergeIndexesAdminHandlerTest"
```
Fail-before: revert only `MergeIndexes.java`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
