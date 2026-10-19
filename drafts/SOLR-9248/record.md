# Skip record: SOLR-9248

- Ticket: SOLR-9248, HttpSolrClient not compatible with compression option. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-9248-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: HttpSolrClient not compatible with compression option.

## Code reading

GzipDecompressingEntity/DeflateDecompressingEntity not in tree (git grep no hits at pin); removed with Apache HttpClient code, see cf7f5079e8 SOLR-17962 Move deprecated SolrClient impls (Apache HttpClient) to test-framework and 31405c513d SOLR-18188; HttpSolrClient.java:69 is now abstract with no gzip handling

## History

- `cf7f5079e8`: cited in the C3 reading; ancestor of the pinned head: yes (checked).
- `31405c513d`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
