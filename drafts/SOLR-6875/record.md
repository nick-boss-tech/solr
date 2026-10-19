# Skip record: SOLR-6875

- Ticket: SOLR-6875, No data integrity between replicas. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-6875-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: No data integrity between replicas.

## Code reading

The named mechanisms are gone: StreamingSolrServers has no hits (last removed by ef809a0f10, SOLR-6895), and LIR was removed by a37a213975 (SOLR-11812). The reported symptom (replica document counts diverging) is not tied to one path at head, so absence is not shown. Showing it needs a cluster test, which needs a build and is out of scope here.

## History

- `ef809a0f10`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).
- `a37a213975`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading gives a verdict but misses the piece noted above.

## Verdict justification

keep-open: the code reading does not settle the question alone. The B1 reading names a missing piece that needs a build or a source check.

Missing for this verdict: The missing piece noted in the code reading below.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
