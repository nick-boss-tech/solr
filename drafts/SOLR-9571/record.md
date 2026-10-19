# Skip record: SOLR-9571

- Ticket: SOLR-9571, Time-allowed detection logic in LBHttpSolrClient should return a fake partial results response. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9571-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Time-allowed detection logic in LBHttpSolrClient should return a fake partial results response.

## Code reading

solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:421-424 and 926-940 still throw SolrServerException when time allowed is exceeded instead of returning a fake partialResults response (message is no longer misleading but behavior asked for is absent)

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
