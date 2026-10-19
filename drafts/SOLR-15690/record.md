# Skip record: SOLR-15690

- Ticket: SOLR-15690, "Updates in datasource.xml are not reflecting in Solr Console". JIRA status Open, resolution none, fix versions none, created 2021-10-11, updated 2021-10-19. Checked 2026-10-19 UTC.
- Verdict: close-obsolete.
- Draft branch: solr-15690-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15690-vm2.md).

## Ticket reading

The reporter edited datasource.xml (a DataImportHandler configuration file) and found that the change did not show in the Solr admin console, even after restarting ZooKeeper and Solr. The report has no comments and names no test class. The screenshot it refers to is not in the report text.

## Code reading

- datasource.xml is a DataImportHandler configuration file. DataImportHandler was removed from Solr by commit a57ba254001 (SOLR-14783: Remove DIH from 9.0, 2020-08-29), an ancestor of the pinned head. The removal predates the ticket (filed 2021-10-11).
- `git ls-tree -r --name-only origin/main | grep -i datasource`: no output.
- `git grep -l -i 'datasource.xml\|db-data-config' origin/main -- solr/core/src solr/solrj solr/webapp solr/solr-ref-guide`: no output. No code, UI, or documentation path at the pinned head reads datasource.xml.

Commands at the pinned head:
- git merge-base --is-ancestor a57ba254001 origin/main: ancestor.
- git ls-tree -r --name-only origin/main | grep -i datasource: none.
- git grep -l -i 'datasource.xml\|db-data-config' origin/main -- solr/core/src solr/solrj solr/webapp solr/solr-ref-guide: none.

## History

- a57ba254001 (2020-08-29): SOLR-14783 removing DIH from 9.0. Ancestor of the pinned head.
- Commits on origin/main naming SOLR-15690: none.

## Reproduction attempts

Not applicable. The file the report edits is not read by any code at the pinned head, so the reported display cannot be produced on this code.

## Gap analysis

The reported component and its configuration file are not in Solr main. Whether the older release that carried DataImportHandler shows the reported stale display is not something this pass can check.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about (DataImportHandler and its datasource.xml) was removed (a57ba254001, ancestor of the pinned head), and a search of main finds no remaining use (the empty grep results above). The minimum evidence for this verdict is the grep command and its empty result at the pinned head, which is recorded here.

## Limits

This record does not test any release that carried DataImportHandler. It does not check the screenshot. It does not check for a third-party copy of DataImportHandler.
