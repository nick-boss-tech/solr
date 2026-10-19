# Skip record: SOLR-13781

- Ticket: SOLR-13781, TestContainerReqHandler.testPackageAPI failures imply race condition between update-package and delete-requesthandler. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-09-19, updated 2020-01-21, no fix versions, no links; five comments, the last from noble.paul (2019-09-20).
- Verdict: close-obsolete.
- Draft branch: solr-13781-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep, deletion check and ancestry run at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 05, item 22, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) cited the deletion commit 515af6d3ec; that is re-checked here.

## Ticket reading

The package test TestContainerReqHandler.testPackageAPI failed about 8% of the time, at a request to the node-level /node/ext endpoint that still found a request handler the test had deleted. The reporter thought the update-package call was still running when the delete was issued.

## Grep and checks at the pinned head

- `git grep -n "TestContainerReqHandler" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. The test class is not in the tree.
- `git grep -n "node/ext" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/core/src/java/*' | wc -l` gives 0. The /node/ext endpoint the test queried is not in the core source.
- `git grep -n "class PackageManager\b" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives one class, solr/core/src/java/org/apache/solr/packagemanager/PackageManager.java:75. This is a different class from the one the ticket involves: it is the command-line package manager added by SOLR-13662 (d9f41f8a5a, 2019-11-14), not the server-side update-package code the test used. The earlier record's grep count is not a match for the deleted class.
- `git show --diff-filter=D --name-only --format='' 515af6d3ec` lists the deleted files, among them solr/core/src/java/org/apache/solr/core/PackageManager.java and solr/core/src/test/org/apache/solr/handler/TestContainerReqHandler.java.
- `git merge-base --is-ancestor 515af6d3ec 498a09aea97aac86c2cc7ac114113b946d249942` succeeds (515af6d3ec, "SOLR-13661: Reverting all half-baked stuff ...").

## History

- 5a01a8b362 (2019-09-19, "SOLR-13781: AwaitsFix TestContainerReqHandler.testPackageAPI") is on the pin. It marked the test as AwaitsFix before the revert removed it.

## Reproduction attempts

Not applicable. The test and the endpoint it used are absent at the pin.

## Gap analysis

The named test, the server-side package-update path, and the /node/ext endpoint are removed. The new PackageManager is a separate CLI class and is not the code the ticket reports. The race in the ticket is not shown to exist in the new code, and the record does not claim it was checked there.

## Verdict justification

close-obsolete: the test and the code the ticket names are removed by 515af6d3ec (an ancestor of the pin), the grep for the test class and the /node/ext endpoint at the pin finds no match (empty results above), and the AwaitsFix marking on the pin (5a01a8b362) shows the test was already disabled before removal. The record does not claim the new CLI package manager was checked for the same race.

## Limits

This record does not run any test. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
