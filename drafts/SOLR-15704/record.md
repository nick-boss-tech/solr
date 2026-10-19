# Skip record: SOLR-15704

- Ticket: SOLR-15704, "Delta import with multiple entities: working partially". JIRA status Open, resolution none, fix versions none, created 2021-10-20, updated 2021-11-17. Checked 2026-10-19 UTC.
- Verdict: close-obsolete.
- Draft branch: solr-15704-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15704-vm2.md).

## Ticket reading

The reporter calls DataImportHandler (DIH) delta imports for two entities, one after the other from PHP. Only the first is run; the second request is logged and then ignored, unless a pause is added between the calls. The report is against DIH's dataimport.properties and its delta import handling. One comment (Ausername, 2021-10-21) confirms from the log that the second request is never acted on. No test class is named.

## Code reading

- The component the ticket is about is DataImportHandler. `git ls-tree -r --name-only origin/main | grep -ci dataimport` returns 0: no DIH files exist at the pinned head.
- DIH was removed by commit a57ba254001 (SOLR-14783: Remove DIH from 9.0, 2020-08-29). That commit is an ancestor of the pinned head (git merge-base --is-ancestor). The removal predates the ticket (filed 2021-10-20), so the report describes a component that was already gone from main when it was filed. It is from a release that still carried DIH (8.x).
- `git log --diff-filter=D --format='%h %ad %s' --date=short origin/main -- solr/contrib/dataimporthandler` returns a57ba254001 as the deletion.

Commands at the pinned head:
- git ls-tree -r --name-only origin/main | grep -ci dataimport: 0.
- git merge-base --is-ancestor a57ba254001 origin/main: ancestor.

## History

- a57ba254001 (2020-08-29): SOLR-14783 removing DIH from 9.0. Ancestor of the pinned head.
- Commits on origin/main naming SOLR-15704: none.

## Reproduction attempts

Not applicable. The component is not in this tree, so the reported behaviour cannot run here. The reproduction would need a Solr 8.x release with DIH.

## Gap analysis

The reported component is gone from main. Whether DIH in the 8.x line still has the reported behaviour is not something this pass can check, because the 8.x line is not in this repository. The ticket's question (is this a bug or expected) is answered for main only by the component's removal.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about (DataImportHandler) was removed (a57ba254001, ancestor of the pinned head), and a search of main finds no remaining use (the empty grep above). The minimum evidence for this verdict is the grep command and its empty result at the pinned head, which is recorded here.

## Limits

This record does not test DIH on any release. It does not establish whether the behaviour exists in the 8.x line. It does not check for a third-party DIH package.
