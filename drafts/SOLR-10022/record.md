# Skip record: SOLR-10022

- Ticket: SOLR-10022, solr fieldType  change failed. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-10022-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: solr fieldType  change failed.

## Code reading

Reported path was DIH full-import with clean; DIH removed by a57ba25400 SOLR-14783 Remove DIH from 9.0, no DataImportHandler under solr/modules at 498a09aea9; delete-all path DirectUpdateHandler2.java:392-396 uses IndexWriter.deleteAll

## History

- `a57ba25400`: cited in the C3 reading; ancestor of the pinned head: yes (checked).
- `498a09aea9`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
