# Skip record: SOLR-7221

- Ticket: SOLR-7221, ConcurrentUpdateSolrServer does not work with HttpClientBuilder (4.3.1). JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-7221-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: ConcurrentUpdateSolrServer does not work with HttpClientBuilder (4.3.1).

## Code reading

ConcurrentUpdateSolrServer.java removed by 4a6d85790a (SOLR-6895). Its replacements (ConcurrentUpdateBaseSolrClient, ConcurrentUpdateJdkSolrClient) do not call setFollowRedirects or getParams.

## History

- `4a6d85790a`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head (fixed or removed), as described above.

## Verdict justification

close-obsolete: the feature or class the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the B1 reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
