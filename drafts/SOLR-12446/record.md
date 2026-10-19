# Skip record: SOLR-12446

- Ticket: SOLR-12446, Misleading client response when IllegalArgumentException is thrown in SolrIndexSearcher. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12446-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Misleading client response when IllegalArgumentException is thrown in SolrIndexSearcher.

## Code reading

solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:428-433: when no endpoint is left, the message still starts "No live SolrServers available to handle this request", with the last exception attached as the cause. The misleading top-level text remains.

## History

- None cited in the B2 reading.

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. B2 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
