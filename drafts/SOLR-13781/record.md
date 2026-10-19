# Skip record: SOLR-13781

- Ticket: SOLR-13781, TestContainerReqHandler.testPackageAPI failures imply race condition between update-package and delete-requesthandler. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-13781-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: TestContainerReqHandler.testPackageAPI failures imply race condition between update-package and delete-requesthandler.

## Code reading

TestContainerReqHandler.java and PackageManager.java (the update-package and /node/ext code) were deleted by 515af6d3ec (SOLR-13661 revert). git log --diff-filter=D confirms it.

## History

- `515af6d3ec`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
