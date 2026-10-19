# Skip record: SOLR-7612

- Ticket: SOLR-7612, CloudSolrServer.setIdField is misnamed; change to setRouteField. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-7612-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: CloudSolrServer.setIdField is misnamed; change to setRouteField.

## Code reading

gone | setIdField was deprecated in 6bf5f4a87f (SOLR-11868) and removed in 291068d18a (SOLR-15608). git grep for setIdField or setRouteField finds no code hits at 498a09ae, only CHANGELOG.md:2304.

## History

- `6bf5f4a87f`: cited in the C2 reading; ancestor of the pinned head: yes (checked).
- `291068d18a`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
