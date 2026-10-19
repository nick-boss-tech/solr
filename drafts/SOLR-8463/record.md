# Skip record: SOLR-8463

- Ticket: SOLR-8463, FacetComponent throws NPE when using shards.tolerant and a shard fails to respond. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-8463-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: FacetComponent throws NPE when using shards.tolerant and a shard fails to respond.

## Code reading

FacetComponent.java:723-724 and :985-986 have null guards at head. The countFacets guard predates the ticket (900363a9b6, SOLR-3557). No commit mentions SOLR-8463. Missing: the NPE stack from the original report (the description has none), and a shards.tolerant run on the head to see which site still throws. That needs a build.

## History

- `900363a9b6`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading gives a verdict but misses the piece noted above.

## Verdict justification

keep-open: the code reading does not settle the question alone. The B1 reading names a missing piece that needs a build or a source check.

Missing for this verdict: The missing piece noted in the code reading below.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
