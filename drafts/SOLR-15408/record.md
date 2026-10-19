# Skip record: SOLR-15408

- Ticket: SOLR-15408, ConcurrentUpdate issue of solr 8.8.1. JIRA status as recorded in the B3 reading: Open, 2021-06-23, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-15408-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: ConcurrentUpdate issue of solr 8.8.1. B3 row type: support. Skip note from the B3 reading: The throwing path is still present. With no repro, the root cause stays undiagnosed.

## Code reading

solr/solrj/src/java/org/apache/solr/client/solrj/impl/StallDetection.java:119-124 throws the reported "Request processing has stalled" IOException, wired at ConcurrentUpdateBaseSolrClient.java:212

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code path is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
