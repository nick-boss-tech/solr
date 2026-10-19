# Skip record: SOLR-17751

- Ticket: SOLR-17751, SolrException: Unable to read response info for shard. JIRA status as recorded in the B3 reading: Open, 2025-05-02, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-17751-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrException: Unable to read response info for shard. B3 row type: support. Skip note from the B3 reading: The throwing path is still present. With no repro or comments, the one-hour burst cause is undiagnosed.

## Code reading

solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java:66-73 (non-tolerant path throws "Unable to read <key> info for shard")

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
