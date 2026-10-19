# Skip record: SOLR-6561

- Ticket: SOLR-6561, LBHttpSolrServer's aliveCheckExecutor is leaking connection when ResponseParser is null. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-6561-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: LBHttpSolrServer's aliveCheckExecutor is leaking connection when ResponseParser is null.

## Code reading

gone | The old LBHttpSolrServer class is absent; current solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:123,186 contains its replacement and only a historical mention. JIRA says the reporter stopped passing a null ResponseParser.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
